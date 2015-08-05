/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wtms.web.controller.workflow;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.workflow.CustomizedWorkFlowService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.wtms.web.contract.WorkflowContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public abstract class GenericWorkFlowController {
    
    @Autowired
    protected CustomizedWorkFlowService customizedWorkFlowService;
    
   
    public abstract StateAware getModel();
   
    
    @ModelAttribute(value = "approverDepartmentList")
    public List<Department> findAllApproverDeparment() {
        return Collections.emptyList();
    }
    
    
    @ModelAttribute("workflowcontainer")
    public WorkflowContainer getWorkflowContainer() {
      return new WorkflowContainer();
    }

    /**
     * Used to get valid actions that needs to be performed Based on these value workflow buttons will be rendered
     */
   
    @ModelAttribute(value="validactionList")
    public List<String> getValidActions(@ModelAttribute("workflowcontainer") WorkflowContainer container) {
        List<String> validActions = Collections.emptyList();
        if (null == getModel() || null == getModel().getId()) {
            validActions = Arrays.asList("Forward");
        } else {
            if (getModel().getCurrentState() != null) {
                validActions = this.customizedWorkFlowService.getNextValidActions(getModel()
                        .getStateType(), container.getWorkFlowDepartment(),
                         container.getAmountRule(),
                        container.getAdditionalRule(), getModel().getCurrentState().getValue(),
                        container. getPendingActions(), getModel().getCreatedDate());
            } else {
                // FIXME This May not work
                validActions = this.customizedWorkFlowService.getNextValidActions(getModel()
                        .getStateType(), container.getWorkFlowDepartment(),
                        container.getAmountRule(),
                       container.getAdditionalRule(), State.DEFAULT_STATE_VALUE_CREATED,
                       container. getPendingActions(), getModel().getCreatedDate());
            }
        }
       return validActions;
    }
    
    @ModelAttribute(value="nextaction")
    public String getNextAction(@ModelAttribute("workflowcontainer") WorkflowContainer container) {

        WorkFlowMatrix wfMatrix = null;
        if ( getModel()!=null && getModel().getId() != null) {
            if (getModel().getCurrentState() != null) {
                wfMatrix = this.customizedWorkFlowService.getWfMatrix(getModel().getStateType(),
                        container.getWorkFlowDepartment(), container.getAmountRule(), container.getAdditionalRule(), getModel()
                                .getCurrentState().getValue(), container.getPendingActions(), getModel()
                                .getCreatedDate());
            } else {
                wfMatrix = this.customizedWorkFlowService.getWfMatrix(getModel().getStateType(),
                        container.getWorkFlowDepartment(), container.getAmountRule(), container.getAdditionalRule(),
                        State.DEFAULT_STATE_VALUE_CREATED, container.getPendingActions(), getModel()
                                .getCreatedDate());
            }
        }
        return wfMatrix == null ? "" : wfMatrix.getNextAction();
    }
   
}
