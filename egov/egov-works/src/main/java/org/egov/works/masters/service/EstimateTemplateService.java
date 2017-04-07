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
package org.egov.works.masters.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.entity.UOM;
import org.egov.commons.service.TypeOfWorkService;
import org.egov.commons.service.UOMService;
import org.egov.works.abstractestimate.entity.EstimateTemplateSearchRequest;
import org.egov.works.abstractestimate.entity.NonSor;
import org.egov.works.masters.entity.EstimateTemplate;
import org.egov.works.masters.entity.EstimateTemplateActivity;
import org.egov.works.masters.entity.ScheduleOfRate;
import org.egov.works.masters.repository.EstimateTemplateRepository;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
@Transactional(readOnly = true)
public class EstimateTemplateService {

    private final EstimateTemplateRepository estimateTemplateRepository;

    @Autowired
    private TypeOfWorkService typeOfWorkService;

    @Autowired
    private UOMService uomService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ScheduleCategoryService scheduleCategoryService;

    @Autowired
    private NonSorService nonSorService;

    @Autowired
    public EstimateTemplateService(final EstimateTemplateRepository estimateTemplateRepository) {
        this.estimateTemplateRepository = estimateTemplateRepository;
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Transactional
    public EstimateTemplate create(final EstimateTemplate estimateTemplate) {
        return estimateTemplateRepository.save(estimateTemplate);
    }

    @Transactional
    public EstimateTemplate update(final EstimateTemplate estimateTemplate) {
        return estimateTemplateRepository.save(estimateTemplate);
    }

    @Transactional
    public void remove(final EstimateTemplate estimateTemplate) {
        estimateTemplateRepository.delete(estimateTemplate);
    }

    public List<EstimateTemplate> getAll() {
        return estimateTemplateRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public EstimateTemplate getEstimateTemplateById(final Long estimateTemplateId) {
        return estimateTemplateRepository.findOne(estimateTemplateId);
    }

    public EstimateTemplate getEstimateTemplateByName(final String name) {
        return estimateTemplateRepository.findByName(name);
    }

    public EstimateTemplate getEstimateTemplateByCode(final String code) {
        return estimateTemplateRepository.findByCode(code);
    }

    public List<EstimateTemplate> getEstimateTemplateByCodeIgnoreCase(final String code) {
        return estimateTemplateRepository.findByCodeContainingIgnoreCase(code);
    }

    public void loadModelValues(final Model model, final String mode) {
        if (!org.apache.commons.lang.StringUtils.isBlank(mode) && mode.equals(WorksConstants.VIEW))
            model.addAttribute("typeOfWork",
                    typeOfWorkService.getTypeOfWorkByPartyType(WorksConstants.PARTY_TYPE_CONTRACTOR));
        else
            model.addAttribute("typeOfWork",
                    typeOfWorkService.getActiveTypeOfWorksByPartyType(WorksConstants.PARTY_TYPE_CONTRACTOR));
        model.addAttribute("uomList", uomService.findAll());
        model.addAttribute("scheduleCategoryList", scheduleCategoryService.getAllScheduleCategories());
    }

    public void createEstimateTemplateActivities(final EstimateTemplate estimateTemplate) {
        EstimateTemplateActivity estimateTemplateActivity;
        estimateTemplate.getEstimateTemplateActivities().clear();
        for (final EstimateTemplateActivity eta : estimateTemplate.getTempEstimateTemplateSorActivities()) {
            estimateTemplateActivity = getEstimateTemplateActivity();
            final ScheduleOfRate sor = eta.getSchedule();
            sor.setUom(eta.getSchedule().getUom());
            estimateTemplateActivity.setSchedule(sor);
            estimateTemplateActivity.setEstimateTemplate(estimateTemplate);
            estimateTemplate.getEstimateTemplateActivities().add(estimateTemplateActivity);
        }
        int indexForNonSOr = 0;
        for (final EstimateTemplateActivity eta : estimateTemplate.getTempEstimateTemplateNonSorActivities())
            if (estimateTemplate.getTempEstimateTemplateNonSorActivities().get(indexForNonSOr).getNonSor().getUom()
                    .getId() != null) {
                estimateTemplateActivity = getEstimateTemplateActivity();
                estimateTemplateActivity.setNonSor(createNonSor(eta));
                estimateTemplateActivity.setUom(uomService.findOne(eta.getNonSor().getUom().getId()));
                estimateTemplateActivity.setValue(eta.getValue());
                estimateTemplateActivity.setEstimateTemplate(estimateTemplate);
                estimateTemplate.getEstimateTemplateActivities().add(estimateTemplateActivity);
                indexForNonSOr++;
            }

    }

    private EstimateTemplateActivity getEstimateTemplateActivity() {
        return new EstimateTemplateActivity();
    }

    @Transactional
    public NonSor createNonSor(final EstimateTemplateActivity eta) {
        final NonSor nonSor = new NonSor();
        final UOM uom = uomService.findOne(eta.getNonSor().getUom().getId());
        nonSor.setDescription(eta.getNonSor().getDescription());
        nonSor.setUom(uom);
        return nonSorService.save(nonSor);
    }

    public void setEstimateTemplateTempSorAndNonSorList(final EstimateTemplate estimateTemplate) {
        final List<EstimateTemplateActivity> etaSorList = new ArrayList<>();
        final List<EstimateTemplateActivity> etaNonSorList = new ArrayList<>();
        for (final EstimateTemplateActivity eta : estimateTemplate.getEstimateTemplateActivities())
            if (eta.getSchedule() != null)
                etaSorList.add(eta);

        for (final EstimateTemplateActivity eta : estimateTemplate.getEstimateTemplateActivities())
            if (eta.getNonSor() != null)
                etaNonSorList.add(eta);

        estimateTemplate.setTempEstimateTemplateSorActivities(etaSorList);
        estimateTemplate.setTempEstimateTemplateNonSorActivities(etaNonSorList);

    }

    public List<EstimateTemplate> searchEstimateTemplates(
            final EstimateTemplateSearchRequest estimateTemplateSearchRequest) {

        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(EstimateTemplate.class);
        if (estimateTemplateSearchRequest != null) {
            if (StringUtils.isNotBlank(estimateTemplateSearchRequest.getTemplateName()))
                criteria.add(Restrictions.ilike("name", estimateTemplateSearchRequest.getTemplateName(),
                        MatchMode.ANYWHERE));
            if (StringUtils.isNotBlank(estimateTemplateSearchRequest.getTemplateCode()))
                criteria.add(Restrictions.ilike("code", estimateTemplateSearchRequest.getTemplateCode(),
                        MatchMode.ANYWHERE));

            if (estimateTemplateSearchRequest.getTypeOfWork() != null)
                criteria.add(Restrictions.eq("typeOfWork.id", estimateTemplateSearchRequest.getTypeOfWork()));
            if (estimateTemplateSearchRequest.getSubTypeOfWork() != null)
                criteria.add(Restrictions.eq("subTypeOfWork.id", estimateTemplateSearchRequest.getSubTypeOfWork()));

            criteria.add(Restrictions.eq("status", estimateTemplateSearchRequest.isStatus()));

        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }
}
