/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.works.abstractestimate.service;

import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infra.admin.master.entity.User;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.EstimateTechnicalSanction;
import org.egov.works.abstractestimate.entity.FinancialDetail;
import org.egov.works.abstractestimate.entity.MultiYearEstimate;
import org.egov.works.abstractestimate.repository.AbstractEstimateRepository;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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
    public AbstractEstimate createAbstractEstimateOnLineEstimateTechSanction(final LineEstimateDetails lineEstimateDetails,
            final int i) {
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
        if(lineEstimateDetails.getLineEstimate().getLocation() != null)
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
        multiYearEstimate.setFinancialYear(financialYearHibernateDAO.getFinYearByDate(abstractEstimate.getEstimateDate()));
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
        estimateTechnicalSanction
                .setTechnicalSanctionDate(abstractEstimate.getLineEstimateDetails().getLineEstimate().getTechnicalSanctionDate());
        estimateTechnicalSanction
                .setTechnicalSanctionBy(abstractEstimate.getLineEstimateDetails().getLineEstimate().getTechnicalSanctionBy());

        // TODO: move to cascade save with AbstractEstimate object once AbstractEstimate entity converted to JPA
        return estimateTechnicalSanctionService.save(estimateTechnicalSanction);
    }

    public AbstractEstimate getAbstractEstimateByEstimateNumber(final String estimateNumber) {
        return abstractEstimateRepository.findByEstimateNumberAndEgwStatus_codeNotLike(estimateNumber,
                AbstractEstimate.EstimateStatus.CANCELLED.toString());
    }

    public AbstractEstimate getAbstractEstimateByEstimateNumberAndStatus(final String estimateNumber) {
        return abstractEstimateRepository.findByLineEstimateDetails_EstimateNumberAndEgwStatus_codeEquals(estimateNumber,
                AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());
    }
    
    public AbstractEstimate getAbstractEstimateByLineEstimateDetailsForCancelLineEstimate(final Long id) {
        return abstractEstimateRepository.findByLineEstimateDetails_IdAndEgwStatus_codeEquals(id,
                AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());
    }

    public List<User> getCreatedByForEstimatePhotograph() {
        return abstractEstimateRepository.findCreatedByForEstimatePhotograph(
                AbstractEstimate.EstimateStatus.TECH_SANCTIONED.toString());
    }

}
