/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.abstractestimate.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.entity.EstimateTechnicalSanction;
import org.egov.works.abstractestimate.entity.FinancialDetail;
import org.egov.works.abstractestimate.entity.MultiYearEstimate;
import org.egov.works.abstractestimate.entity.OverheadValue;
import org.egov.works.abstractestimate.repository.AbstractEstimateRepository;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.entity.enums.LineEstimateStatus;
import org.egov.works.master.service.OverheadService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class EstimateService {

    @PersistenceContext
    private EntityManager entityManager;

    private final AbstractEstimateRepository abstractEstimateRepository;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;

    @Autowired
    private EstimateTechnicalSanctionService estimateTechnicalSanctionService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private OverheadService overheadService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public EstimateService(final AbstractEstimateRepository abstractEstimateRepository) {
        this.abstractEstimateRepository = abstractEstimateRepository;
    }

    public AbstractEstimate getAbstractEstimateById(final Long id) {
        return abstractEstimateRepository.findOne(id);
    }

    @Transactional
    public AbstractEstimate createAbstractEstimate(final AbstractEstimate abstractEstimate, final MultipartFile[] files)
            throws IOException {
        AbstractEstimate newAbstractEstimate = null;
        final AbstractEstimate abstractEstimateFromDB = getAbstractEstimateByEstimateNumber(abstractEstimate
                .getEstimateNumber());
        if (abstractEstimateFromDB == null) {
            for (final MultiYearEstimate multiYearEstimate : abstractEstimate.getMultiYearEstimates())
                multiYearEstimate.setAbstractEstimate(abstractEstimate);
            for (final FinancialDetail financialDetail : abstractEstimate.getFinancialDetails())
                financialDetail.setAbstractEstimate(abstractEstimate);
            for (final OverheadValue obj : abstractEstimate.getOverheadValues()) {
                obj.setAbstractEstimate(abstractEstimate);
                obj.setOverhead(overheadService.getOverheadById(obj.getOverhead().getId()));
            }
            for(Activity act : abstractEstimate.getActivities()) {
                act.setAbstractEstimate(abstractEstimate);
            }
            newAbstractEstimate = abstractEstimateRepository.save(abstractEstimate);
        } else
            newAbstractEstimate = updateAbstractEstimate(abstractEstimateFromDB, abstractEstimate);
        final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, newAbstractEstimate,
                WorksConstants.ABSTRACTESTIMATE);
        if (!documentDetails.isEmpty()) {
            abstractEstimate.setDocumentDetails(documentDetails);
            worksUtils.persistDocuments(documentDetails);
        }
        return newAbstractEstimate;

    }

    @Transactional
    public AbstractEstimate updateAbstractEstimate(final AbstractEstimate abstractEstimateFromDB,
            final AbstractEstimate newAbstractEstimate) {
        abstractEstimateFromDB.setEstimateDate(newAbstractEstimate.getEstimateDate());
        abstractEstimateFromDB.setEstimateNumber(newAbstractEstimate.getEstimateNumber());
        abstractEstimateFromDB.setName(newAbstractEstimate.getName());
        abstractEstimateFromDB.setDescription(newAbstractEstimate.getDescription());
        abstractEstimateFromDB.setWard(newAbstractEstimate.getWard());
        abstractEstimateFromDB.setNatureOfWork(newAbstractEstimate.getNatureOfWork());
        abstractEstimateFromDB.setLocation(newAbstractEstimate.getLocation());
        abstractEstimateFromDB.setParentCategory(newAbstractEstimate.getParentCategory());
        abstractEstimateFromDB.setCategory(newAbstractEstimate.getCategory());
        abstractEstimateFromDB.setExecutingDepartment(newAbstractEstimate.getExecutingDepartment());
        abstractEstimateFromDB.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE,
                LineEstimateStatus.CREATED.toString()));
        abstractEstimateFromDB.setProjectCode(newAbstractEstimate.getProjectCode());
        abstractEstimateFromDB.setLineEstimateDetails(newAbstractEstimate.getLineEstimateDetails());
        abstractEstimateRepository.save(abstractEstimateFromDB);
        return abstractEstimateFromDB;
    }

    @Transactional
    public AbstractEstimate createAbstractEstimateOnLineEstimateTechSanction(
            final LineEstimateDetails lineEstimateDetails, final int i) {
        final AbstractEstimate savedAbstractEstimate = abstractEstimateRepository
                .save(populateAbstractEstimate(lineEstimateDetails));
        saveTechnicalSanction(savedAbstractEstimate, i);
        return savedAbstractEstimate;
    }

    private AbstractEstimate populateAbstractEstimate(final LineEstimateDetails lineEstimateDetails) {
        final AbstractEstimate abstractEstimate = new AbstractEstimate();
        abstractEstimate.setEstimateDate(lineEstimateDetails.getLineEstimate().getLineEstimateDate());
        abstractEstimate.setEstimateNumber(lineEstimateDetails.getEstimateNumber());
        abstractEstimate.setName(lineEstimateDetails.getNameOfWork());
        abstractEstimate.setDescription(lineEstimateDetails.getNameOfWork());
        abstractEstimate.setWard(lineEstimateDetails.getLineEstimate().getWard());
        abstractEstimate.setNatureOfWork(lineEstimateDetails.getLineEstimate().getNatureOfWork());
        if (lineEstimateDetails.getLineEstimate().getLocation() != null)
            abstractEstimate.setLocation(lineEstimateDetails.getLineEstimate().getLocation().getName());
        abstractEstimate.setParentCategory(lineEstimateDetails.getLineEstimate().getTypeOfWork());
        abstractEstimate.setCategory(lineEstimateDetails.getLineEstimate().getSubTypeOfWork());
        abstractEstimate.setExecutingDepartment(lineEstimateDetails.getLineEstimate().getExecutingDepartment());
        abstractEstimate.setWorkValue(lineEstimateDetails.getActualEstimateAmount().doubleValue());
        abstractEstimate.setEstimateValue(lineEstimateDetails.getActualEstimateAmount());
        abstractEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.ABSTRACTESTIMATE,
                AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString()));
        abstractEstimate.setProjectCode(lineEstimateDetails.getProjectCode());
        abstractEstimate.setApprovedDate(lineEstimateDetails.getLineEstimate().getTechnicalSanctionDate());
        abstractEstimate.setLineEstimateDetails(lineEstimateDetails);
        abstractEstimate.addFinancialDetails(populateEstimateFinancialDetails(abstractEstimate));
        abstractEstimate.addMultiYearEstimate(populateMultiYearEstimate(abstractEstimate));
        return abstractEstimate;
    }

    private FinancialDetail populateEstimateFinancialDetails(final AbstractEstimate abstractEstimate) {
        final FinancialDetail financialDetail = new FinancialDetail();
        financialDetail.setAbstractEstimate(abstractEstimate);
        financialDetail.setFund(abstractEstimate.getLineEstimateDetails().getLineEstimate().getFund());
        financialDetail.setFunction(abstractEstimate.getLineEstimateDetails().getLineEstimate().getFunction());
        financialDetail.setBudgetGroup(abstractEstimate.getLineEstimateDetails().getLineEstimate().getBudgetHead());
        financialDetail.setScheme(abstractEstimate.getLineEstimateDetails().getLineEstimate().getScheme());
        financialDetail.setSubScheme(abstractEstimate.getLineEstimateDetails().getLineEstimate().getSubScheme());
        return financialDetail;
    }

    private MultiYearEstimate populateMultiYearEstimate(final AbstractEstimate abstractEstimate) {
        final MultiYearEstimate multiYearEstimate = new MultiYearEstimate();
        multiYearEstimate.setAbstractEstimate(abstractEstimate);
        multiYearEstimate.setFinancialYear(financialYearHibernateDAO.getFinYearByDate(abstractEstimate
                .getEstimateDate()));
        multiYearEstimate.setPercentage(100);
        return multiYearEstimate;
    }

    private EstimateTechnicalSanction saveTechnicalSanction(final AbstractEstimate abstractEstimate, final int i) {
        final EstimateTechnicalSanction estimateTechnicalSanction = new EstimateTechnicalSanction();
        estimateTechnicalSanction.setAbstractEstimate(abstractEstimate);
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(abstractEstimate.getLineEstimateDetails().getLineEstimate().getTechnicalSanctionNumber());
        if (i > 0) {
            stringBuilder.append("/");
            stringBuilder.append(i);
        }
        estimateTechnicalSanction.setTechnicalSanctionNumber(stringBuilder.toString());
        estimateTechnicalSanction.setTechnicalSanctionDate(abstractEstimate.getLineEstimateDetails().getLineEstimate()
                .getTechnicalSanctionDate());
        estimateTechnicalSanction.setTechnicalSanctionBy(abstractEstimate.getLineEstimateDetails().getLineEstimate()
                .getTechnicalSanctionBy());

        // TODO: move to cascade save with AbstractEstimate object once
        // AbstractEstimate entity converted to JPA
        return estimateTechnicalSanctionService.save(estimateTechnicalSanction);
    }

    public AbstractEstimate getAbstractEstimateByEstimateNumber(final String estimateNumber) {
        return abstractEstimateRepository.findByEstimateNumberAndEgwStatus_codeNotLike(estimateNumber,
                AbstractEstimate.EstimateStatus.CANCELLED.toString());
    }

    public AbstractEstimate getAbstractEstimateByEstimateNumberAndStatus(final String estimateNumber) {
        return abstractEstimateRepository.findByLineEstimateDetails_EstimateNumberAndEgwStatus_codeEquals(
                estimateNumber, AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());
    }

    public AbstractEstimate getAbstractEstimateByLineEstimateDetailsForCancelLineEstimate(final Long id) {
        return abstractEstimateRepository.findByLineEstimateDetails_IdAndEgwStatus_codeEquals(id,
                AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());
    }

    public Double getEstimateValueForLineEstimate(final LineEstimateDetails lineEstimateDetails) {
        Double estimateValue = 0d;
        final String qryStr = "select totalbillpaidsofar from egw_mv_work_progress_register where ledid =:ledid ";
        final Query query = entityManager.createNativeQuery(qryStr).setParameter("ledid", lineEstimateDetails.getId());
        estimateValue = query.getResultList() != null && !query.getResultList().isEmpty() ? (double) query
                .getResultList().get(0) : 0d;
                return estimateValue;
    }

    public void populateDataForAbstractEstimate(final LineEstimateDetails lineEstimateDetails, final Model model,
            final AbstractEstimate abstractEstimate) {
        final LineEstimate lineEstimate = lineEstimateDetails.getLineEstimate();
        abstractEstimate.setLineEstimateDetails(lineEstimateDetails);
        abstractEstimate.setExecutingDepartment(lineEstimateDetails.getLineEstimate().getExecutingDepartment());
        abstractEstimate.setWard(lineEstimateDetails.getLineEstimate().getWard());
        if (lineEstimate.getLocation() != null)
            abstractEstimate.setLocation(lineEstimate.getLocation().getName());
        abstractEstimate.setNatureOfWork(lineEstimate.getNatureOfWork());
        abstractEstimate.setParentCategory(lineEstimate.getTypeOfWork());
        abstractEstimate.setCategory(lineEstimate.getSubTypeOfWork());
        abstractEstimate.setProjectCode(lineEstimateDetails.getProjectCode());
        if (lineEstimate.getWorkCategory().equals(WorksConstants.SLUM_WORK))
            model.addAttribute("workCategory", "Slum Work");
        else
            model.addAttribute("workCategory", "Non Slum Work");

        final List<FinancialDetail> financialDetailList = new ArrayList<FinancialDetail>();
        final FinancialDetail financialDetails = new FinancialDetail();
        financialDetails.setFund(lineEstimate.getFund());
        financialDetails.setFunction(lineEstimate.getFunction());
        financialDetails.setScheme(lineEstimate.getScheme());
        financialDetails.setSubScheme(lineEstimate.getSubScheme());
        financialDetails.setBudgetGroup(lineEstimate.getBudgetHead());
        financialDetailList.add(financialDetails);
        abstractEstimate.setFinancialDetails(financialDetailList);
        model.addAttribute("estimateValue", getEstimateValueForLineEstimate(lineEstimateDetails));
        model.addAttribute("lineEstimateDetails", lineEstimateDetails);
        model.addAttribute("abstractEstimate", abstractEstimate);
        model.addAttribute("lineEstimate", lineEstimate);
        model.addAttribute("workOrder",
                letterOfAcceptanceService.getWorkOrderByEstimateNumber(lineEstimateDetails.getEstimateNumber()));
        final List<AppConfigValues> values = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_SHOW_SERVICE_FIELDS);
        final AppConfigValues value = values.get(0);
        if (value.getValue().equalsIgnoreCase("Yes"))
            model.addAttribute("isServiceVATRequired", true);
        else
            model.addAttribute("isServiceVATRequired", false);

    }

}
