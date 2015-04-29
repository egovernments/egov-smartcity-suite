package org.egov.works.services.contractoradvance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationException;
import org.egov.pims.commons.DrawingOfficer;
import org.egov.works.models.contractoradvance.ContractorAdvanceRequisition;
import org.egov.works.models.estimate.AbstractEstimate;

public interface ContractorAdvanceService {
	
	/**
	 * @param contractorAdvanceRequisition, actionName, advanceAccountCode
	 * @ save Contractor Advance Object and return ContractorAdvanceRequisition
	 * @throws ValidationException
	 */
	public ContractorAdvanceRequisition save(ContractorAdvanceRequisition contractorAdvanceRequisition, String actionName, Long advanceAccountCode) throws ValidationException;

	
	/**
	 * @param Id
	 * @return ContractorAdvanceRequisition Object by passing Id
	 * @throws ValidationException
	 */
	public ContractorAdvanceRequisition getContractorAdvanceRequisitionById(Long Id) throws ValidationException;
	
	/**
	 * @param workOrderEstimateId, contractorAdvanceRequisitionId
	 * @return Advance already paid by passing workOrderEstimateId 
	 * @throws ValidationException
	 */
	public BigDecimal getAdvancePaidByWOEstimateId(Long workOrderEstimateId) throws ValidationException;

	/** 
	 * @param workOrderEstimateId, contractorAdvanceRequisitionId
	 * @return Advance already paid by passing workOrderEstimateId and contractorAdvanceRequisitionId. 
	 * It gets all the advance made till this ARF. It is used in modify(workflow) and view. 
	 * @throws ValidationException
	 */
	public BigDecimal getAdvancePaidByWOEstIdForView(Long workOrderEstimateId, Long contractorAdvanceRequisitionId) throws ValidationException;

	/**
	 * Get List of Contractor Advance payable account codes based on Account code Purpose 'CONTRACTOR_ADVANCE_ACCOUNTCODE'
	 */
	public List<CChartOfAccounts> getContractorAdvanceAccountcodes() throws ValidationException;
	
	/**
	 * @param abstractEstimate
	 * @return Estimate value including RE's if any
	 * @throws ValidationException
	 */
	public BigDecimal getTotalEstimateValueIncludingRE(AbstractEstimate abstractEstimate) throws ValidationException;
	

	/**
	 * @param contractorAdvanceRequisition
	 * @return the functionary as UAC for the workflow
	 * @throws ValidationException
	 */   
	public Integer getFunctionaryForWorkflow(ContractorAdvanceRequisition contractorAdvanceRequisition);
	 
	/**
	 * @param contractorAdvanceRequisition, actionName
	 * Cancel Contractor Advance Object 
	 * @throws ValidationException
	 */
	public void cancelContractorAdvanceRequisition(ContractorAdvanceRequisition contractorAdvanceRequisition, String actionName) throws ValidationException;
	
	/**
	 * @return get all Status for ContractorAdvanceRequisition object excluding 'NEW' status
	 */
	public List<EgwStatus> getAllContractorAdvanceRequisitionStatus();
	
	/**
	 * @return get all distinct DrawingOfficers from ContractorAdvanceRequisition object which are already created
	 */
	public List<DrawingOfficer> getAllDrawingOfficerFromARF();
	
	/**
	 * Get List of Drawing officer from given list of designations which are read from app config values based on the date passed 
	 * and for given search criteria from auto complete as name or code(query string) 
	 */
	public List<HashMap> getDrawingOfficerListForARF(String query, Date advanceRequisitionDate);
	
	/**
	 * @param workOrderEstimateId
	 * @return Advance COA object used in Advance Requisition of Work Order estimate
	 */
	public CChartOfAccounts getContractorAdvanceAccountcodeForWOE(Long workOrderEstimateId);
	
	/**
	 * @param workOrderEstimateId, asOnDate
	 * @return Advance already paid by passing workOrderEstimateId and asOnDate where Payment Voucher Status=0(Approved)
	 */
	public BigDecimal getTotalAdvancePaymentMadeByWOEstimateId(Long workOrderEstimateId, Date asOnDate);
	
	/**
	 * @param workOrderEstimateId
	 * @return ContractorAdvanceRequisition object which is in work flow 
	 */
	public ContractorAdvanceRequisition getContractorARFInWorkflowByWOEId(Long workOrderEstimateId);
}
