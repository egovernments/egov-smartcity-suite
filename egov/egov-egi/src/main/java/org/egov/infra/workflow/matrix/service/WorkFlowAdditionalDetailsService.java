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

package org.egov.infra.workflow.matrix.service;

import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.matrix.entity.WorkFlowAdditionalRule;
import org.egov.infra.workflow.matrix.repository.WorkFlowAdditionalRuleRepository;
import org.egov.infra.workflow.service.WorkflowTypeService;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WorkFlowAdditionalDetailsService {


    public static final String OBJECTTYPEID_ID = "objecttypeid.id";
    public static final String ADDITIONAL_RULE = "additionalRule";

    @Autowired
    @Qualifier("entityQueryService")
    private PersistenceService entityQueryService;

    @Autowired
    private WorkflowTypeService workflowTypeService;

    @Autowired
    private WorkFlowAdditionalRuleRepository workFlowAdditionalRuleRepository;

    public List getAllModuleTypeforStatus() {
        return this.entityQueryService.findAllBy(" select distinct(moduletype) from EgwStatus order by moduletype asc");
    }

    public List<WorkflowTypes> getobjectTypeList() {
        return workflowTypeService.getAllWorkflowTypes();
    }

    @Transactional
    public WorkFlowAdditionalRule save(WorkFlowAdditionalRule wfAdditionalRule) {
        return workFlowAdditionalRuleRepository.save(wfAdditionalRule);
    }

    public List<WorkFlowAdditionalRule> getAdditionalRulesbyObject(final Long objectType) {
        return entityQueryService.getSession().createCriteria(WorkFlowAdditionalRule.class).
                add(Restrictions.eq(OBJECTTYPEID_ID, objectType)).list();
    }

    public WorkFlowAdditionalRule getObjectbyTypeandRule(final Long objectType, final String additionalRules) {
        final Criteria crit = entityQueryService.getSession().createCriteria(WorkFlowAdditionalRule.class);
        crit.add(Restrictions.eq(OBJECTTYPEID_ID, objectType));
        if ("-1".equals(additionalRules)) {
            crit.add(Restrictions.isNull(ADDITIONAL_RULE));
        } else {
            crit.add(Restrictions.eq(ADDITIONAL_RULE, additionalRules));
        }
        List<WorkFlowAdditionalRule> wfAdditionalRules = crit.list();
        if (!wfAdditionalRules.isEmpty()) {
            return wfAdditionalRules.get(0);
        } else {
            return null;
        }
    }

    public WorkFlowAdditionalRule getObjectbyTypeandRule(final Long objectId, final Long objectType, final String additionalRules) {
        final Criteria crit = entityQueryService.getSession().createCriteria(WorkFlowAdditionalRule.class);
        crit.add(Restrictions.eq(OBJECTTYPEID_ID, objectType));
        crit.add(Restrictions.ne("id", objectId));
        if (additionalRules == null) {
            crit.add(Restrictions.isNull(ADDITIONAL_RULE));
        } else {
            crit.add(Restrictions.eq(ADDITIONAL_RULE, additionalRules));
        }
        List<WorkFlowAdditionalRule> wfAdditionalRules = crit.list();
        if (!wfAdditionalRules.isEmpty()) {
            return wfAdditionalRules.get(0);
        } else {
            return null;
        }

    }
}
