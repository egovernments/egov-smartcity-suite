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

package org.egov.infra.workflow.service;

import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.WorkflowAction;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * The Interface WorkflowService.
 *
 * @param <T> the generic type
 */
public interface WorkflowService<T extends StateAware> {

    /**
     * Moves the stateAware from the current state to the next state. The actual logic of moving the stateAware depends on the
     * state of the stateAware, and the Action
     *
     * @param workflowAction the action
     * @param stateAware     the state aware stateAware
     * @param comment        the comment
     * @return stateAware after state transition.
     */
    T transition(WorkflowAction workflowAction, T stateAware, String comment);

    /**
     * Moves the stateAware from the current state to the next state. The actual logic of moving the stateAware depends on the
     * state of the stateAware, the Action and the corresponding business rules encapsulated in a Script. The script name should
     * be '<stateAware's workflowtype>.workflow.<action name>'. If this script was not found, the implementation will look for a
     * script named '<stateAware's workflowtype>.workflow'. If this is not found either, then an EgovRuntimeException is thrown
     * with the message key as 'workflow.script.notfound'
     *
     * @param actionName the action name
     * @param stateAware the state aware stateAware
     * @param comment    the comment
     * @return stateAware after state transition.
     */
    T transition(String actionName, T stateAware, String comment);

    /**
     * Returns a set of valid actions that can be executed on the stateAware. The actions are determined using the current state
     * of the stateAware
     *
     * @param stateAware the state aware
     * @return List of valid Actions
     */
    List<WorkflowAction> getValidActions(T stateAware);

    /**
     * Returns WorkFlowMatrix for following combination of arguments
     *
     * @param type           the Object type of object
     * @param department     Name of the department
     * @param amountRule     the amount
     * @param additionalRule the additional rule defined for the objecttype
     * @param currentState   the current state of the object
     * @param pendingAction  the pendingActions for the objecttype
     * @return WorkFlowMatrix Object
     */

    WorkFlowMatrix getWfMatrix(String type, String department, BigDecimal amountRule, String additionalRule, String currentState,
                               String pendingAction);

    /**
     * Returns WorkFlowMatrix for following combination of arguments
     *
     * @param type           the Object type of object
     * @param department     Name of the department
     * @param amountRule     the amount
     * @param additionalRule the additional rule defined for the objecttype
     * @param currentState   the current state of the object
     * @param pendingAction  the pendingActions for the objecttype
     * @return WorkFlowMatrix Object
     */

    WorkFlowMatrix getWfMatrix(String type, String department, BigDecimal amountRule, String additionalRule, String currentState,
                               String pendingAction, Date date);

    /**
     * Returns WorkFlowMatrix for following combination of arguments
     *
     * @param type           the Object type of object
     * @param department     Name of the department
     * @param amountRule     the amount
     * @param additionalRule the additional rule defined for the objecttype
     * @param currentState   the current state of the object
     * @param pendingAction  the pendingActions for the objecttype
     * @param date
     * @param designation    of user
     * @return WorkFlowMatrix Object
     */
    WorkFlowMatrix getWfMatrix(String type, String department, BigDecimal amountRule, String additionalRule, String currentState,
                               String pendingAction, Date date, String designation);
}