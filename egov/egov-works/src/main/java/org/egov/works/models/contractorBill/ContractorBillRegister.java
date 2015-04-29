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
package org.egov.works.models.contractorBill;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.egov.model.bills.EgBillregister;

public class ContractorBillRegister extends EgBillregister{
	public enum BillStatus{
		CREATED,APPROVED,REJECTED,CANCELLED
	}
	private Integer partbillNo;
	private List<AssetForBill> assetDetailsList = new LinkedList<AssetForBill>();
	private List<DeductionTypeForBill> deductionTypeList = new LinkedList<DeductionTypeForBill>();
	private Long documentNumber;
	private String owner;
	private List<String> billActions = new ArrayList<String>();
	private long workOrderId;
	private Long workflowDepartmentId;
	private Integer workflowDesignationId;
	private Integer workflowApproverUserId;
	private Integer workflowWardId;
	private String workflowapproverComments;
	
	private List<StatutoryDeductionsForBill> statutoryDeductionsList = new LinkedList<StatutoryDeductionsForBill>();

	@Override
	public String getStateDetails() {
		return "Contractor Bill No: " + getBillnumber(); 
	} 
	
	public List<AssetForBill> getAssetDetailsList() {
		return assetDetailsList;
	}

	public void setAssetDetailsList(List<AssetForBill> assetDetailsList) {
		this.assetDetailsList = assetDetailsList;
	}
	
	public void addAssetDetails(AssetForBill assetForBill) {
		this.assetDetailsList.add(assetForBill);
	}
	
	public void addDeductionType(DeductionTypeForBill deductionTypeForBill) {
		this.deductionTypeList.add(deductionTypeForBill);
	}

	public List<DeductionTypeForBill> getDeductionTypeList() {
		return deductionTypeList;
	}

	public void setDeductionTypeList(List<DeductionTypeForBill> deductionTypeList) {
		this.deductionTypeList = deductionTypeList;
	}

	public Integer getPartbillNo() {
		return partbillNo;
	}

	public void setPartbillNo(Integer partbillNo) {
		this.partbillNo = partbillNo;
	}

	public Long getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<String> getBillActions() {
		return billActions;
	}

	public void setBillActions(List<String> billActions) {
		this.billActions = billActions;
	}

	public long getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(long workOrderId) {
		this.workOrderId = workOrderId;
	}

	public Long getWorkflowDepartmentId() {
		return workflowDepartmentId;
	}

	public void setWorkflowDepartmentId(Long workflowDepartmentId) {
		this.workflowDepartmentId = workflowDepartmentId;
	}

	public Integer getWorkflowDesignationId() {
		return workflowDesignationId;
	}

	public void setWorkflowDesignationId(Integer workflowDesignationId) {
		this.workflowDesignationId = workflowDesignationId;
	}

	public Integer getWorkflowApproverUserId() {
		return workflowApproverUserId;
	}

	public void setWorkflowApproverUserId(Integer workflowApproverUserId) {
		this.workflowApproverUserId = workflowApproverUserId;
	}

	public Integer getWorkflowWardId() {
		return workflowWardId;
	}

	public void setWorkflowWardId(Integer workflowWardId) {
		this.workflowWardId = workflowWardId;
	}

	public String getWorkflowapproverComments() {
		return workflowapproverComments;
	}

	public void setWorkflowapproverComments(String workflowapproverComments) {
		this.workflowapproverComments = workflowapproverComments;
	}

	public List<StatutoryDeductionsForBill> getStatutoryDeductionsList() {
		return statutoryDeductionsList;
	}

	public void setStatutoryDeductionsList(
			List<StatutoryDeductionsForBill> statutoryDeductionsList) {
		this.statutoryDeductionsList = statutoryDeductionsList;
	}
	
	public void addStatutoryDeductions(StatutoryDeductionsForBill statutoryDeductionsForBill) {
		this.statutoryDeductionsList.add(statutoryDeductionsForBill);
	}
}