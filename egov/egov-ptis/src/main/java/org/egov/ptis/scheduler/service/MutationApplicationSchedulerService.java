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

package org.egov.ptis.scheduler.service;

import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REGISTRATION_PENDING;

import java.util.List;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.service.property.PropertyService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author subhash
 *
 */
@Service
@Transactional(readOnly = true)
public class MutationApplicationSchedulerService {

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<PropertyMutation> transferWorkflowService;

    @Autowired
    @Qualifier("propertyMutationService")
    private PersistenceService<PropertyMutation, Long> propertyMutationService;

    @Transactional
    public void updateMutations() {
        List<PropertyMutation> mutations = getRegistrationPendingMutations();
        for (PropertyMutation mutation : mutations) {
            // TODO--call web api from CARD to get registration details
            transitionWorkFlow(mutation);
            //propertyService.updateIndexes(mutation, APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP);
            propertyMutationService.persist(mutation);
        }
    }

    private void transitionWorkFlow(final PropertyMutation propertyMutation) {
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        final WorkFlowMatrix wfmatrix = transferWorkflowService.getWfMatrix(propertyMutation.getStateType(), null,
                null, propertyMutation.getType(), propertyMutation.getCurrentState().getValue(), null);
        propertyMutation.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                .withOwner(propertyMutation.getState().getOwnerPosition())
                .withNextAction(wfmatrix.getNextAction());
    }

    private List<PropertyMutation> getRegistrationPendingMutations() {
        List<PropertyMutation> mutations = propertyMutationService.findAllBy(
                "from PropertyMutation p where p.state.nextAction = ?",
                WF_STATE_REGISTRATION_PENDING);
        return mutations;
    }
}
