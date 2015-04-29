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
package org.egov.works.services.contractoradvance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.EGOVException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.StringUtils;
import org.egov.model.advance.EgAdvanceReqPayeeDetails;
import org.egov.model.advance.EgAdvanceRequisitionDetails;
import org.egov.model.advance.EgAdvanceRequisitionMis;
import org.egov.model.masters.AccountCodePurpose;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.DrawingOfficer;
import org.egov.pims.service.EisUtilService;
import org.egov.works.models.contractoradvance.ContractorAdvanceRequisition;
import org.egov.works.models.contractoradvance.ContractorAdvanceRequisitionNumberGenerator;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.revisionEstimate.RevisionAbstractEstimate;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;

public class ContractorAdvanceServiceImpl extends PersistenceService<ContractorAdvanceRequisition,Long> implements ContractorAdvanceService{
	
	protected PersistenceService persistenceService;
	private WorksService worksService;
	private EisUtilService eisService;
	@Autowired
        private CommonsService commonsService;
	private ContractorAdvanceRequisitionNumberGenerator contractorAdvanceRequisitionNumberGenerator;
	private static final String CONTRACTOR_ADVANCE_ACCOUNTCODE_PURPOSE="CONTRACTOR_ADVANCE_ACCOUNTCODE";
	private static final String CONTRACTOR_ADVANCE_REQUISITION="ContractorAdvanceRequisition";
	private static final String ACCOUNTDETAIL_TYPE_CONTRACTOR = "contractor";
	private String PENDING_FOR_VERIFICATION = "Pending for Verification";
	private String PENDING_FOR_VALIDATION = "Pending for Validation";
	private String PENDING_FOR_APPROVAL = "Pending for Approval";
	private String PENDING_FOR_RECTIFICATION = "Pending for Rectification";
	private String PENDING_FOR_CHECK = "Pending for Check";
	private PersistenceService<RevisionAbstractEstimate,Long> revisionAbstractEstimateService;
	private WorkflowService<ContractorAdvanceRequisition> workflowService;	
	private static final String ARF_TYPE="Contractor";	
	
	private static final Logger LOGGER = Logger.getLogger(ContractorAdvanceServiceImpl.class);
	
	public BigDecimal getAdvancePaidByWOEstimateId(Long workOrderEstimateId) throws ValidationException {
		BigDecimal advanceAlreadyPaid = BigDecimal.ZERO;
		try{				
			advanceAlreadyPaid = (BigDecimal)persistenceService.find("select sum(advanceRequisitionAmount) from ContractorAdvanceRequisition where status.code<>'CANCELLED' and workOrderEstimate.id = ?",workOrderEstimateId);			
		}
		catch (ValidationException validationException) {
			throw new ValidationException(validationException.getErrors());				 
		}
		return advanceAlreadyPaid;
	}
	
	public BigDecimal getAdvancePaidByWOEstIdForView(Long workOrderEstimateId, Long contractorAdvanceRequisitionId) throws ValidationException {
		BigDecimal advanceAlreadyPaid = BigDecimal.ZERO;
		try{				
			advanceAlreadyPaid = (BigDecimal)persistenceService.find("select sum(advanceRequisitionAmount) from ContractorAdvanceRequisition where status.code<>'CANCELLED' and workOrderEstimate.id = ? and id < ?",workOrderEstimateId, contractorAdvanceRequisitionId);			
		}
		catch (ValidationException validationException) {
			throw new ValidationException(validationException.getErrors());				 
		}
		return advanceAlreadyPaid;
	}
	
	public ContractorAdvanceRequisition save(ContractorAdvanceRequisition contractorAdvanceRequisition, String actionName, Long advanceAccountCode) throws ValidationException {
		boolean shouldAddAdvanceDetails  = false;
		try{			
			if(contractorAdvanceRequisition.getStatus()==null || (ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.REJECTED.toString().equalsIgnoreCase(
					contractorAdvanceRequisition.getStatus().getCode())	&& StringUtils.isBlank(contractorAdvanceRequisition.getState().getNextAction()))
					||  ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.NEW.toString().equalsIgnoreCase(contractorAdvanceRequisition.getStatus().getCode())){
				shouldAddAdvanceDetails  = true;
			}
			if(contractorAdvanceRequisition.getStatus() == null) {
				contractorAdvanceRequisition.setStatus(commonsService.getStatusByModuleAndCode(CONTRACTOR_ADVANCE_REQUISITION,"NEW"));
			}
			setARFNumber(contractorAdvanceRequisition); 
			contractorAdvanceRequisition = setContractorAdvanceRequisitionMis(contractorAdvanceRequisition);
			
			if(shouldAddAdvanceDetails)
				contractorAdvanceRequisition = setContractorAdvanceDetails(contractorAdvanceRequisition, advanceAccountCode);
			
			contractorAdvanceRequisition = (ContractorAdvanceRequisition)persist(contractorAdvanceRequisition);
			contractorAdvanceRequisition= workflowService.transition(actionName, contractorAdvanceRequisition,contractorAdvanceRequisition.getWorkflowapproverComments());
			
			//TODO - Need to know alternative for how to find previous when workflow ended
			/**if(contractorAdvanceRequisition.getCurrentState()!=null && contractorAdvanceRequisition.getCurrentState().getValue()!=null 
					&& contractorAdvanceRequisition.getCurrentState().getPrevious() != null &&  
					(ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.APPROVED.toString().equalsIgnoreCase(contractorAdvanceRequisition.getCurrentState().getPrevious().getValue()) || 
							ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.CANCELLED.toString().equalsIgnoreCase(contractorAdvanceRequisition.getCurrentState().getPrevious().getValue())))
			{
				contractorAdvanceRequisition.setStatus(commonsService.getStatusByModuleAndCode(CONTRACTOR_ADVANCE_REQUISITION,
						contractorAdvanceRequisition.getCurrentState().getPrevious().getValue()));
				//Source path for view ARF needs to be set to view from Advance Payment screen 
				contractorAdvanceRequisition.getEgAdvanceReqMises().setSourcePath("/egworks/contractoradvance/contractorAdvanceRequisition!edit.action?sourcepage=search&id="+contractorAdvanceRequisition.getId());
			}
			else **/{
				contractorAdvanceRequisition.setStatus(commonsService.getStatusByModuleAndCode(CONTRACTOR_ADVANCE_REQUISITION,
						contractorAdvanceRequisition.getCurrentState().getValue()));
			}
			contractorAdvanceRequisition = (ContractorAdvanceRequisition)persist(contractorAdvanceRequisition);
		}
			catch (ValidationException validationException) {
				throw new ValidationException(validationException.getErrors());				 
			}
			return contractorAdvanceRequisition;
	}
		
	/*
	 *  Generate Contractor Advance Requisition number
	 */		
	public void setARFNumber(ContractorAdvanceRequisition contractorAdvanceRequisition) {	
		try{
			CFinancialYear financialYear=commonsService.getFinancialYearByDate(contractorAdvanceRequisition.getAdvanceRequisitionDate());
			if(financialYear == null) { 
				throw new ValidationException(Arrays.asList(new ValidationError("advancerequisition.arfdate.financialyear.invalid","advancerequisition.arfdate.financialyear.invalid")));
			}
		
			if((contractorAdvanceRequisition.getAdvanceRequisitionNumber() == null) || (contractorAdvanceRequisition.getAdvanceRequisitionNumber() != null &&
					arfNumberChangeRequired(contractorAdvanceRequisition, financialYear)))
			contractorAdvanceRequisition.setAdvanceRequisitionNumber(contractorAdvanceRequisitionNumberGenerator.getARFNumber(contractorAdvanceRequisition, financialYear, persistenceService));
		}
		catch (ValidationException exception) {
		   throw exception;
		}
		catch (Exception e) { 
			throw new ValidationException(Arrays.asList(new ValidationError("advancerequisition.arfdate.financialyear.invalid","advancerequisition.arfdate.financialyear.invalid")));
		}
	}
	
	/**
	 * The method return true if the ARF number has to be re-generated
	 * @param entity an instance of <code>ContractorAdvanceRequisition</code> containing the Advance Requisition date 
	 * @param financialYear an instance of <code>CFinancialYear</code> representing the financial year for the Advance Requisition date.
	 * @return a boolean value indicating if the Advance Requisition number change is required.
	 */
	private boolean arfNumberChangeRequired(ContractorAdvanceRequisition entity, CFinancialYear financialYear) {
		String[] arfNum = entity.getAdvanceRequisitionNumber().split("/");		
		if(arfNum[3].equals(financialYear.getFinYearRange())){
			return false;
		}
		return true;
	}
	
	private ContractorAdvanceRequisition setContractorAdvanceRequisitionMis(ContractorAdvanceRequisition contractorAdvanceRequisition) {
		try {
			EgAdvanceRequisitionMis advanceRequisitionMis = null;
			if(contractorAdvanceRequisition.getId() == null)
				advanceRequisitionMis = new EgAdvanceRequisitionMis();
			else
				advanceRequisitionMis = contractorAdvanceRequisition.getEgAdvanceReqMises();
			
			if(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getUserDepartment() != null)	
				advanceRequisitionMis.setEgDepartment((Department)contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getUserDepartment());
			if(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFinancialDetails().get(0).getFunction() != null)
				advanceRequisitionMis.setFunction(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFinancialDetails().get(0).getFunction());
			if(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getWard() != null)
				advanceRequisitionMis.setFieldId((Boundary)contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getWard());
			if(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFinancialDetails().get(0).getFunctionary() != null)
				advanceRequisitionMis.setFunctionaryId(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFinancialDetails().get(0).getFunctionary());
			if(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFinancialDetails().get(0).getFund() != null)
				advanceRequisitionMis.setFund(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFinancialDetails().get(0).getFund());
			if(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFinancialDetails().get(0).getScheme() != null)
				advanceRequisitionMis.setScheme(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFinancialDetails().get(0).getScheme());
			if(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFinancialDetails().get(0).getSubScheme() != null)
				advanceRequisitionMis.setSubScheme(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFinancialDetails().get(0).getSubScheme());
			if(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFundSource() != null)
				advanceRequisitionMis.setFundsource(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFundSource());
			advanceRequisitionMis.setReferencenumber(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getEstimateNumber());
			if(contractorAdvanceRequisition.getWorkOrderEstimate().getWorkOrder().getContractor().getName() != null)	
				advanceRequisitionMis.setPayto(contractorAdvanceRequisition.getWorkOrderEstimate().getWorkOrder().getContractor().getName());
			advanceRequisitionMis.setLastupdatedtime(new Date());
			
			
			advanceRequisitionMis.setEgAdvanceRequisition(contractorAdvanceRequisition);
			contractorAdvanceRequisition.setEgAdvanceReqMises(advanceRequisitionMis);
		}
		catch (Exception e) {
			throw new ValidationException(Arrays.asList(new ValidationError("error",e.getMessage())));
		}
		return contractorAdvanceRequisition;
	}

	private ContractorAdvanceRequisition setContractorAdvanceDetails(ContractorAdvanceRequisition contractorAdvanceRequisition, Long advanceAccountCode) {
		try {
			EgAdvanceRequisitionDetails egAdvanceRequisitionDetails = new EgAdvanceRequisitionDetails();
			for (EgAdvanceRequisitionDetails advanceRequisitionDetails : contractorAdvanceRequisition.getEgAdvanceReqDetailses()) {	
				egAdvanceRequisitionDetails = advanceRequisitionDetails;
			}
			egAdvanceRequisitionDetails.setChartofaccounts((CChartOfAccounts)persistenceService.find("from  CChartOfAccounts where id=?",advanceAccountCode));
			egAdvanceRequisitionDetails.setFunction(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFinancialDetails().get(0).getFunction());
			egAdvanceRequisitionDetails.setDebitamount(contractorAdvanceRequisition.getAdvanceRequisitionAmount());
			egAdvanceRequisitionDetails.setNarration(contractorAdvanceRequisition.getNarration());
			egAdvanceRequisitionDetails.setEgAdvanceRequisition(contractorAdvanceRequisition);			
	
			if(egAdvanceRequisitionDetails.getChartofaccounts() != null && egAdvanceRequisitionDetails.getChartofaccounts().getGlcode() != null) {
				
				List<Accountdetailtype> detailCode = commonsService.getAccountdetailtypeListByGLCode(egAdvanceRequisitionDetails.getChartofaccounts().getGlcode());			
				if (detailCode!=null && !detailCode.isEmpty()) {	
					Accountdetailtype adt = commonsService.getAccountDetailTypeIdByName(egAdvanceRequisitionDetails.getChartofaccounts().getGlcode(), ACCOUNTDETAIL_TYPE_CONTRACTOR);
					if(adt != null){
						egAdvanceRequisitionDetails.getEgAdvanceReqpayeeDetailses().add(setRequisitionPayeeDetail(contractorAdvanceRequisition, egAdvanceRequisitionDetails,adt));	
					}
					if(adt == null) {
						List<ValidationError> errors=new ArrayList<ValidationError>();
						errors.add(new ValidationError("advancerequisition.validate_glcode_for_subledger","advancerequisition.validate_glcode_for_subledger")); 
						throw new ValidationException(errors);					
					}
				}
			}	
			contractorAdvanceRequisition.getEgAdvanceReqDetailses().clear(); 
			contractorAdvanceRequisition.addEgAdvanceReqDetails(egAdvanceRequisitionDetails);
		}
		catch (Exception e) {
			throw new ValidationException(Arrays.asList(new ValidationError("error",e.getMessage())));
		}
		return contractorAdvanceRequisition;
	}

	private EgAdvanceReqPayeeDetails setRequisitionPayeeDetail(ContractorAdvanceRequisition contractorAdvanceRequisition, EgAdvanceRequisitionDetails advanceRequisitionDetails,Accountdetailtype accountdetailtype) {
		EgAdvanceReqPayeeDetails egAdvanceReqPaydetail = new EgAdvanceReqPayeeDetails();
		for (EgAdvanceReqPayeeDetails advanceReqPaydetail : advanceRequisitionDetails.getEgAdvanceReqpayeeDetailses()) {	
			egAdvanceReqPaydetail = advanceReqPaydetail;
		}
		egAdvanceReqPaydetail.setAccountdetailKeyId(Integer.valueOf(contractorAdvanceRequisition.getWorkOrderEstimate().getWorkOrder().getContractor().getId().toString()));
		egAdvanceReqPaydetail.setNarration(advanceRequisitionDetails.getNarration());
		egAdvanceReqPaydetail.setAccountDetailType(accountdetailtype);
		egAdvanceReqPaydetail.setDebitAmount(advanceRequisitionDetails.getDebitamount());
		egAdvanceReqPaydetail.setEgAdvanceRequisitionDetails(advanceRequisitionDetails);
		return egAdvanceReqPaydetail;
	}
	
	
	public ContractorAdvanceRequisition getContractorAdvanceRequisitionById(Long Id) throws ValidationException {
		ContractorAdvanceRequisition contractorAdvanceRequisition = null;
		try {
			contractorAdvanceRequisition = (ContractorAdvanceRequisition) findById(Id, false);
		} catch (ValidationException validationException) {
			throw new ValidationException(validationException.getErrors());
		}
		return contractorAdvanceRequisition;
	}
	
	public List<HashMap> getDrawingOfficerListForARF(String query, Date advanceRequisitionDate) {
		List<HashMap> drawingOfficers = new ArrayList<HashMap>();
		try {
			List<DesignationMaster> designationsList = new LinkedList<DesignationMaster>();
			List<Integer> designationsIdList = new LinkedList<Integer>();
			String drawingOfficerDesignations = worksService.getWorksConfigValue("CONTRACTORADVANCE_DRAWINGOFFICER_DESIGNATIONS");
	
			if (drawingOfficerDesignations != null) {
				String[] designationNames = drawingOfficerDesignations.toUpperCase().split(",");
				List<String> desgListUpper = new ArrayList<String>(Arrays.asList(designationNames));
				designationsList.addAll(persistenceService.findAllByNamedQuery("getDesignationForListOfDesgNames",	desgListUpper));
			}
			for(DesignationMaster desigMaster: designationsList) {
				designationsIdList.add(desigMaster.getDesignationId());
			}
				
			drawingOfficers.addAll(this.eisService.getListOfDrawingOfficers(designationsIdList, advanceRequisitionDate, query));
		} catch (Exception e) {
			LOGGER.error("Error in method getDrawingOfficerListForARF"+e.getMessage());
		}
		return drawingOfficers;
	}
	
	
	public List<CChartOfAccounts> getContractorAdvanceAccountcodes() throws ValidationException {
		List<CChartOfAccounts> coaList = Collections.EMPTY_LIST;
		AccountCodePurpose accountCodePurpose = (AccountCodePurpose)persistenceService.find("from AccountCodePurpose where name = ?",CONTRACTOR_ADVANCE_ACCOUNTCODE_PURPOSE);
		if(accountCodePurpose != null) {
			 try{
				coaList = commonsService.getAccountCodeByPurpose(Integer.valueOf(accountCodePurpose.getId().toString()));
			 }
			 catch (EGOVException e) {
			 	throw new ValidationException(Arrays.asList(new ValidationError("error","advancerequisition.advance.accountcode.load.error")));
			 }
		}
		return coaList;
	}
	
	public BigDecimal getTotalEstimateValueIncludingRE(AbstractEstimate abstractEstimate) throws ValidationException { 
		BigDecimal totalEstimateValue = new BigDecimal(abstractEstimate.getTotalAmount().getValue());
		try{				
			List<RevisionAbstractEstimate> revisionEstimateList = revisionAbstractEstimateService.findAllByNamedQuery("REVISION_ESTIMATES_BY_ESTID", abstractEstimate.getId());
			for(RevisionAbstractEstimate revisionAbstractEstimate: revisionEstimateList) {
				totalEstimateValue = totalEstimateValue.add(new BigDecimal(revisionAbstractEstimate.getTotalAmount().getValue()));
			}
		}
		catch (ValidationException validationException) {
			throw new ValidationException(validationException.getErrors());				 
		}
		return totalEstimateValue;
	}
	
	public Integer getFunctionaryForWorkflow(ContractorAdvanceRequisition contractorAdvanceRequisition) {
		Integer workflowFunctionaryId = null;
		if(contractorAdvanceRequisition != null 
				&& contractorAdvanceRequisition.getId()!=null && contractorAdvanceRequisition.getCurrentState()!=null 
				&& contractorAdvanceRequisition.getCurrentState().getNextAction() !=null
				&& (contractorAdvanceRequisition.getCurrentState().getNextAction().equalsIgnoreCase(PENDING_FOR_VERIFICATION) 
						|| contractorAdvanceRequisition.getCurrentState().getNextAction().equalsIgnoreCase(PENDING_FOR_VALIDATION)   
						|| contractorAdvanceRequisition.getCurrentState().getNextAction().equalsIgnoreCase(PENDING_FOR_APPROVAL)
						|| contractorAdvanceRequisition.getCurrentState().getNextAction().equalsIgnoreCase(PENDING_FOR_RECTIFICATION)
						|| (contractorAdvanceRequisition.getCurrentState().getNextAction().equalsIgnoreCase(PENDING_FOR_CHECK)  
								&& contractorAdvanceRequisition.getCurrentState().getValue().equalsIgnoreCase("REJECTED") )))
		{
			Functionary func = (Functionary) persistenceService.find(" from  Functionary where upper(name) = ?","UAC");
			if(func != null)
				workflowFunctionaryId = func.getId();
		}
		return workflowFunctionaryId;
	}
	
	public void cancelContractorAdvanceRequisition(ContractorAdvanceRequisition contractorAdvanceRequisition, String actionName) throws ValidationException {
		if(contractorAdvanceRequisition.getId ()!= null){
			contractorAdvanceRequisition = workflowService.transition(actionName, contractorAdvanceRequisition,contractorAdvanceRequisition.getWorkflowapproverComments());
			contractorAdvanceRequisition.setStatus(commonsService.getStatusByModuleAndCode(CONTRACTOR_ADVANCE_REQUISITION, WorksConstants.CANCELLED_STATUS));
			persist(contractorAdvanceRequisition);
		}
	}	
	
	public List<EgwStatus> getAllContractorAdvanceRequisitionStatus() {
		return persistenceService.findAllBy("from EgwStatus s where s.moduletype=? and s.code <>'NEW' order by orderId",ContractorAdvanceRequisition.class.getSimpleName());
	}
	
	public List<DrawingOfficer> getAllDrawingOfficerFromARF() { 
		List<DrawingOfficer> drawingOfficerList = persistenceService.findAllByNamedQuery("getAllDrawingOfficerFromARF"); 
		return drawingOfficerList;
	}
	
	public CChartOfAccounts getContractorAdvanceAccountcodeForWOE(Long workOrderEstimateId) {
		CChartOfAccounts advanceCOA = null;
		List<ContractorAdvanceRequisition> arfList = persistenceService.findAllBy("select distinct arf from ContractorAdvanceRequisition arf where arf.workOrderEstimate.id = ? and arf.status.code = ? and arf.arftype = ?",workOrderEstimateId, 
				ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.APPROVED.toString(), ARF_TYPE);
		if(arfList != null && !arfList.isEmpty()) {
			for (EgAdvanceRequisitionDetails advanceRequisitionDetails : arfList.get(0).getEgAdvanceReqDetailses()) {
				advanceCOA = advanceRequisitionDetails.getChartofaccounts();
			}
		}
		return advanceCOA;
	}
	
	public BigDecimal getTotalAdvancePaymentMadeByWOEstimateId(Long workOrderEstimateId, Date asOnDate) {
		BigDecimal advanceAlreadyPaid = BigDecimal.ZERO;
			advanceAlreadyPaid = (BigDecimal)persistenceService.find("select sum(advanceRequisitionAmount) from ContractorAdvanceRequisition where status.code = ? and workOrderEstimate.id = ? " +
					" and egAdvanceReqMises.voucherheader.status = 0 and egAdvanceReqMises.voucherheader.voucherDate <= ?", 
					ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.APPROVED.toString(), workOrderEstimateId, asOnDate);
		return advanceAlreadyPaid;
	}
	
	public ContractorAdvanceRequisition getContractorARFInWorkflowByWOEId(Long workOrderEstimateId) {
		ContractorAdvanceRequisition arf = find("from ContractorAdvanceRequisition arf where arf.workOrderEstimate.id = ? and arf.status.code not in(?,?) and arf.arftype = ?",workOrderEstimateId, 
				ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.APPROVED.toString(), ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.CANCELLED.toString(), ARF_TYPE);		
		return arf;
	}
	
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public void setContractorAdvanceRequisitionNumberGenerator(
			ContractorAdvanceRequisitionNumberGenerator contractorAdvanceRequisitionNumberGenerator) {
		this.contractorAdvanceRequisitionNumberGenerator = contractorAdvanceRequisitionNumberGenerator;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setRevisionAbstractEstimateService(
			PersistenceService<RevisionAbstractEstimate, Long> revisionAbstractEstimateService) {
		this.revisionAbstractEstimateService = revisionAbstractEstimateService;
	}
	
	public void setContractorAdvanceWorkflowService(WorkflowService<ContractorAdvanceRequisition> workflow) {
		this.workflowService = workflow;
	} 
}