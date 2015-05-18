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
package org.egov.works.models.contractoradvance;

import org.egov.eis.entity.DrawingOfficer;
import org.egov.model.advance.EgAdvanceRequisition;
import org.egov.works.models.workorder.WorkOrderEstimate;

public class ContractorAdvanceRequisition extends EgAdvanceRequisition {

    private static final long serialVersionUID = -8267145890478916279L;

    public enum ContractorAdvanceRequisitionStatus {
        NEW, CREATED, CHECKED, REJECTED, RESUBMITTED, CANCELLED, APPROVED
    }

    private WorkOrderEstimate workOrderEstimate;
    private DrawingOfficer drawingOfficer;

    private Long workflowDepartmentId;
    private Integer workflowDesignationId;
    private Integer workflowApproverUserId;
    private Integer workflowWardId;
    private String workflowapproverComments;
    private String ownerName;

    @Override
    public String getStateDetails() {
        return "Contractor ARF : " + getAdvanceRequisitionNumber();
    }

    public WorkOrderEstimate getWorkOrderEstimate() {
        return workOrderEstimate;
    }

    public void setWorkOrderEstimate(final WorkOrderEstimate workOrderEstimate) {
        this.workOrderEstimate = workOrderEstimate;
    }

    public DrawingOfficer getDrawingOfficer() {
        return drawingOfficer;
    }

    public void setDrawingOfficer(final DrawingOfficer drawingOfficer) {
        this.drawingOfficer = drawingOfficer;
    }

    public Long getWorkflowDepartmentId() {
        return workflowDepartmentId;
    }

    public void setWorkflowDepartmentId(final Long workflowDepartmentId) {
        this.workflowDepartmentId = workflowDepartmentId;
    }

    public Integer getWorkflowDesignationId() {
        return workflowDesignationId;
    }

    public void setWorkflowDesignationId(final Integer workflowDesignationId) {
        this.workflowDesignationId = workflowDesignationId;
    }

    public Integer getWorkflowApproverUserId() {
        return workflowApproverUserId;
    }

    public void setWorkflowApproverUserId(final Integer workflowApproverUserId) {
        this.workflowApproverUserId = workflowApproverUserId;
    }

    public Integer getWorkflowWardId() {
        return workflowWardId;
    }

    public void setWorkflowWardId(final Integer workflowWardId) {
        this.workflowWardId = workflowWardId;
    }

    public String getWorkflowapproverComments() {
        return workflowapproverComments;
    }

    public void setWorkflowapproverComments(final String workflowapproverComments) {
        this.workflowapproverComments = workflowapproverComments;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

}