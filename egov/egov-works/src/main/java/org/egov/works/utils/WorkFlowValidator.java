/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.works.utils;

import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class WorkFlowValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowValidator.class);


    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<?> workflowService;
    
    @Autowired
    private PositionMasterService positionMasterService;
    
    @Autowired
    private EisCommonService eisCommonService;

    /*
     * api to validate the assignee in workflow
     */
    public Boolean isValidAssignee(final StateAware<?> state, final Long approverPositionId) {
        String currentState = null;
        if (state.getCurrentState() != null)
            currentState = state.getCurrentState().getValue();
        final WorkFlowMatrix wfmatrix = workflowService.getWfMatrix(state.getStateType(), null, null, null,
                currentState, null);
        Position pos = positionMasterService.getPositionById(approverPositionId);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(wfmatrix.toString() + " " + wfmatrix.getNextDesignation());
			LOGGER.debug(pos.toString() + " " + pos.getDeptDesig().getDesignation().getName());
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(wfmatrix.toString() + ": " + wfmatrix.getNextDesignation());
			LOGGER.info(pos.toString() + ": " + pos.getDeptDesig().getDesignation().getName());
		}
        return eisCommonService.isValidAppover(wfmatrix, pos);

    }
    
	public Boolean isApplicationOwner(final User currentUser, final StateAware<?> state) {
		return positionMasterService.getPositionsForEmployee(currentUser.getId())
				.contains(state.getCurrentState().getOwnerPosition());
	}

}
