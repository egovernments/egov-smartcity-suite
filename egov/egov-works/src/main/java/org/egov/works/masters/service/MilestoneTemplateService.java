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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.service.TypeOfWorkService;
import org.egov.works.masters.entity.MilestoneTemplate;
import org.egov.works.masters.entity.MilestoneTemplateActivity;
import org.egov.works.masters.repository.MilestoneTemplateRepository;
import org.egov.works.milestone.entity.SearchRequestMilestoneTemplate;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
@Transactional(readOnly = true)
public class MilestoneTemplateService {

    @PersistenceContext
    private EntityManager entityManager;

    private final MilestoneTemplateRepository milestoneTemplateRepository;

    @Autowired
    private TypeOfWorkService typeOfWorkService;

    @Autowired
    public MilestoneTemplateService(final MilestoneTemplateRepository milestoneTemplateRepository) {
        this.milestoneTemplateRepository = milestoneTemplateRepository;
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Transactional
    public MilestoneTemplate save(final MilestoneTemplate milestoneTemplate) {
        return milestoneTemplateRepository.save(milestoneTemplate);
    }

    public MilestoneTemplate getMilestoneTemplateById(final Long id) {
        return milestoneTemplateRepository.findOne(id);
    }

    public MilestoneTemplate getMilestoneTemplateByCode(final String code) {
        return milestoneTemplateRepository.findByCodeIgnoreCase(code);
    }

    public List<MilestoneTemplate> findMilestoneTemplateCodeForMilestone(final String code) {
        return milestoneTemplateRepository.findByCodeContainingIgnoreCase(code);
    }

    public List<MilestoneTemplate> searchMilestoneTemplate(
            final SearchRequestMilestoneTemplate searchRequestMilestoneTemplate) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(MilestoneTemplate.class);
        if (searchRequestMilestoneTemplate != null) {
            if (searchRequestMilestoneTemplate.getName() != null)
                criteria.add(Restrictions.ilike("name", searchRequestMilestoneTemplate.getName(), MatchMode.ANYWHERE));
            if (searchRequestMilestoneTemplate.getDescription() != null)
                criteria.add(Restrictions.ilike("description", searchRequestMilestoneTemplate.getDescription(),
                        MatchMode.ANYWHERE));
            criteria.add(Restrictions.eq("typeOfWork.id", searchRequestMilestoneTemplate.getTypeOfWork()));
            if (searchRequestMilestoneTemplate.getSubTypeOfWork() != null)
                criteria.add(Restrictions.eq("subTypeOfWork.id", searchRequestMilestoneTemplate.getSubTypeOfWork()));
            if (searchRequestMilestoneTemplate.getTemplateCode() != null)
                criteria.add(Restrictions.ilike("code", searchRequestMilestoneTemplate.getTemplateCode(),
                        MatchMode.ANYWHERE));
            createCriteriaForTemplateStatus(searchRequestMilestoneTemplate, criteria);
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    private void createCriteriaForTemplateStatus(final SearchRequestMilestoneTemplate searchRequestMilestoneTemplate,
            final Criteria criteria) {
        if (searchRequestMilestoneTemplate.getTemplateStatus() != null
                && Boolean.TRUE.toString().equalsIgnoreCase(searchRequestMilestoneTemplate.getTemplateStatus()))
            criteria.add(Restrictions.eq("status", true));
        if (searchRequestMilestoneTemplate.getTemplateStatus() != null
                && Boolean.FALSE.toString().equalsIgnoreCase(searchRequestMilestoneTemplate.getTemplateStatus()))
            criteria.add(Restrictions.eq("status", false));
    }

    @Transactional
    public MilestoneTemplate update(final MilestoneTemplate milestoneTemplate) {
        return milestoneTemplateRepository.save(milestoneTemplate);
    }

    public void createMilestoneTemplateActivity(final MilestoneTemplate milestoneTemplate) {
        MilestoneTemplateActivity milestoneTemplateActivity;
        milestoneTemplate.getMilestoneTemplateActivities().clear();
        for (final MilestoneTemplateActivity mta : milestoneTemplate.getTempMilestoneTemplateActivities()) {
            milestoneTemplateActivity = new MilestoneTemplateActivity();
            milestoneTemplateActivity.setStageOrderNo(mta.getStageOrderNo());
            milestoneTemplateActivity.setDescription(mta.getDescription());
            milestoneTemplateActivity.setPercentage(mta.getPercentage());
            milestoneTemplateActivity.setMilestoneTemplate(milestoneTemplate);
            milestoneTemplate.getMilestoneTemplateActivities().add(milestoneTemplateActivity);
        }
    }

    public void loadModelValues(final Model model, final MilestoneTemplate milestoneTemplate) {
        model.addAttribute("milestoneTemplate", milestoneTemplate);
        model.addAttribute("typeOfWork",
                typeOfWorkService.getActiveTypeOfWorksByPartyType(WorksConstants.PARTY_TYPE_CONTRACTOR));
    }

}
