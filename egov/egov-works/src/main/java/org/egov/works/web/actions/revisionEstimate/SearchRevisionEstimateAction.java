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
package org.egov.works.web.actions.revisionEstimate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.EgwStatus;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.service.CommonsService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.works.models.contractoradvance.ContractorAdvanceRequisition;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.WorkType;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.revisionEstimate.RevisionAbstractEstimate;
import org.egov.works.models.revisionEstimate.RevisionType;
import org.egov.works.models.revisionEstimate.RevisionWorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.RevisionEstimateService;
import org.egov.works.services.WorksService;
import org.egov.works.services.contractoradvance.ContractorAdvanceService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Sathish P
 *
 */
@ParentPackage("egov")
public class SearchRevisionEstimateAction extends SearchFormAction { 

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(SearchRevisionEstimateAction.class);
	private AbstractEstimate estimates = new AbstractEstimate();
	private RevisionEstimateService revisionEstimateService;
	private Date fromDate;
	private Date toDate;
	@Autowired
        private EmployeeService employeeService;
	private PersonalInformationService personalInformationService;
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",LOCALE);
	private List<RevisionAbstractEstimate> revEstimateList;
	private WorksService worksService;
	private WorkOrderEstimate workOrderEstimate;
	private String estimateNumber;
	private String workOrderNumber;
	private Integer execDept=-1;
	private Integer reStatus;
	private Long revWOId; 
	private String source;
	private PersistenceService<RevisionAbstractEstimate,Long> revisionAbstractEstimateService;
	private PersistenceService<RevisionWorkOrder,Long> revisionWorkOrderService;
	private String messageKey;
	private String revisionEstimateNumber;
	@Autowired
        private CommonsService commonsService;
	private String cancelRemarks;
	private String cancellationReason;
	public static final String UNCHECKED="unchecked";
	public static final String CANCEL_RE = "cancelRE";
	private MeasurementBookService measurementBookService;
	private ContractorAdvanceService contractorAdvanceService;
	
	public SearchRevisionEstimateAction(){		
		addRelatedEntity("category", EgwTypeOfWork.class);
		addRelatedEntity("parentCategory", EgwTypeOfWork.class);
		addRelatedEntity("executingDepartment", Department.class);
		addRelatedEntity("type", WorkType.class);
		addRelatedEntity("egwStatus", EgwStatus.class);		
	}

	@Override
	public Object getModel() {	
		return estimates;
	}
	
	@Override
	public void prepare() {
		super.prepare(); 
		AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		ajaxEstimateAction.setEmployeeService(employeeService);
		ajaxEstimateAction.setPersonalInformationService(personalInformationService);
		addDropdownData("statusList", persistenceService.findAllBy("from EgwStatus s where moduletype=? and code in ('CREATED','REJECTED','RESUBMITTED','CANCELLED','BUDGETARY_APPR_CHECKED','BUDGETARY_APPROPRIATION_DONE','BUDGETARY_APPR_VALIDATED','APPROVED') order by orderId",AbstractEstimate.class.getSimpleName()));
		addDropdownData("executingDepartmentList", persistenceService.findAllBy("from DepartmentImpl dt"));
		addDropdownData("typeList", persistenceService.findAllBy("from WorkType dt"));
		addDropdownData("parentCategoryList", getPersistenceService().findAllBy("from EgwTypeOfWork etw1 where etw1.parentid is null")); 
		addDropdownData("categoryList", Collections.emptyList());
		populateCategoryList(ajaxEstimateAction, estimates.getParentCategory() != null);
		if(CANCEL_RE.equals(source)) {
			EgwStatus egwstat=(EgwStatus) persistenceService.find("from EgwStatus where moduletype=? and code = 'APPROVED' order by orderId",AbstractEstimate.class.getSimpleName());
			setReStatus(egwstat.getId());
		}
	}
	
	public String beforeSearch() {		
		return "search";
	}
		
	public String searchRE(){
		return "search";
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {		 
			StringBuffer query = new StringBuffer(300);
			List<Object> paramList = new ArrayList<Object>();
			query.append("from WorkOrderEstimate woeP, WorkOrderEstimate woeC where woeP.estimate.id= woeC.estimate.parent.id and woeC.estimate.egwStatus.code!='NEW' ");
			if(reStatus != null && reStatus != -1){
				query.append(" and woeC.estimate.egwStatus.id = ?");
				paramList.add(reStatus);
			}
			
			if(getExecDept()!=null && getExecDept()!= -1){
				query.append(" and woeC.estimate.executingDepartment.id = ?");
				paramList.add(getExecDept());
			}
			if(null !=estimates.getEstimateNumber() && StringUtils.isNotEmpty(estimates.getEstimateNumber()) ){
				query.append(" and woeC.estimate.estimateNumber like '%'||?||'%'");
				paramList.add(estimates.getEstimateNumber());
			}
			if(estimates.getType()!=null){
				query.append(" and woeP.estimate.type.id = ?");
				paramList.add(estimates.getType().getId());
			}
			if(null != fromDate && getFieldErrors().isEmpty()){
				query.append(" and woeC.estimate.estimateDate >= ?");
				paramList.add(fromDate);
			}
			if(null != toDate && getFieldErrors().isEmpty()){
				query.append(" and woeC.estimate.estimateDate <= ?");
				paramList.add(toDate);
			}
			if(estimates.getCategory()!=null){
				query.append(" and woeP.estimate.category.id = ?");
				paramList.add(estimates.getCategory().getId()); 
			}
			if(estimates.getParentCategory()!=null){
				query.append(" and woeP.estimate.parentCategory.id = ?");
				paramList.add(estimates.getParentCategory().getId()); 
			}
			
			if(null !=workOrderNumber && StringUtils.isNotEmpty(workOrderNumber) ){
				query.append(" and woeC.workOrder.parent.workOrderNumber like '%'||?||'%'");
				paramList.add(workOrderNumber);
			}
			if(source!=null && source.equalsIgnoreCase(CANCEL_RE))
			{
				query.append(" and woeC.estimate.id = (select max(absEst.id) from AbstractEstimate absEst where absEst.parent.id = woeP.estimate.id and  " +
						" absEst.egwStatus.code!= ? ) ");
				paramList.add(WorksConstants.CANCELLED_STATUS);
			}	
			LOGGER.debug("SearchRevisionEstimate | prepareQuery | query >>>> "+query.toString()); 
			return new SearchQueryHQL("select woeC "+query.toString(), "select count(distinct woeC.id) "+query.toString(), paramList);
		
	}
		
	public String list(){
		boolean isError=false;
		if(fromDate!=null && toDate==null){
			addFieldError("enddate",getText("search.endDate.null"));
			isError=true;
		}
		if(toDate!=null && fromDate==null){
			addFieldError("startdate",getText("search.startDate.null"));		
			isError=true;
		}
		
		if(!DateUtils.compareDates(getToDate(),getFromDate())){
			addFieldError("enddate",getText("greaterthan.endDate.fromDate"));
			isError=true;
		}
		
		if(isError){
			return "search";
		}
		setPageSize(WorksConstants.PAGE_SIZE);
		search();
		showOwnerName();
		return "search";
	}
		
	public List<String> getActionsList() { 
		String actions = worksService.getWorksConfigValue("RE_SHOW_ACTIONS");
		if(actions!=null)
		  return Arrays.asList(actions.split(","));
		return new ArrayList<String>();
	}
	
	protected void populateCategoryList(
			AjaxEstimateAction ajaxEstimateAction, boolean categoryPopulated) {
		if (categoryPopulated) {
			ajaxEstimateAction.setCategory(estimates.getParentCategory().getId());
			ajaxEstimateAction.subcategories();
			addDropdownData("categoryList", ajaxEstimateAction.getSubCategories());		
		}
		else {
			addDropdownData("categoryList", Collections.emptyList());
		}
	}
	
	@ValidationErrorPage(value="search")  
	public String cancelApprovedRE() {
		String cancellationText = null;
		RevisionWorkOrder revWorkOrder = revisionWorkOrderService.findById(revWOId, false);
		RevisionAbstractEstimate re= revisionAbstractEstimateService.findById(revWorkOrder.getWorkOrderEstimates().get(0).getEstimate().getId(), false);
		if(!validateRECancellation(revWorkOrder, re))
			return list();
		
		validateARFForRE(re);
		
		revWorkOrder.setEgwStatus(commonsService.getStatusByModuleAndCode("WorkOrder","CANCELLED")); 
		PersonalInformation prsnlInfo=employeeService.getEmpForUserId(worksService.getCurrentLoggedInUserId());			
		String empName="";
		re.setEgwStatus(commonsService.getStatusByModuleAndCode("AbstractEstimate","CANCELLED"));
		if(prsnlInfo.getEmployeeFirstName()!=null)
			empName=prsnlInfo.getEmployeeFirstName();
		if(prsnlInfo.getEmployeeLastName()!=null)
			empName=empName.concat(" ").concat(prsnlInfo.getEmployeeLastName()); 
		if(cancelRemarks != null  && StringUtils.isNotBlank(cancelRemarks))
			cancellationText=cancellationReason+" : "+cancelRemarks+". Revision Estimate Cancelled by: "+empName;
		else
			cancellationText=cancellationReason+". Revision Estimate Cancelled by: "+empName;
		
		//TODO - The setter methods of variables in State.java are protected. Need to alternative way to solve this issue.
		//Set the status and workflow state to cancelled
		/****State oldEndState = re.getCurrentState();
		Position owner = prsnlInfo.getAssignment(new Date()).getPosition();
		oldEndState.setCreatedBy(prsnlInfo.getUserMaster());
		oldEndState.setModifiedBy(prsnlInfo.getUserMaster());
		oldEndState.setCreatedDate(new Date());
		oldEndState.setModifiedDate(new Date());
		oldEndState.setOwner(owner);
		oldEndState.setValue(WorksConstants.CANCELLED_STATUS);
		oldEndState.setText1(cancellationText);
		
		re.changeState("END", owner, null); ***/

		//Release the budget
		revisionEstimateService.releaseBudget( re);
		revisionEstimateNumber=re.getEstimateNumber(); 
		messageKey=revisionEstimateNumber+": "+getText("revisionEstimate.Cancel"); 
		return SUCCESS;
	}

	private boolean validateRECancellation(
			RevisionWorkOrder revWorkOrder, RevisionAbstractEstimate revEstimate) {
		List<WorkOrderActivity> revWoaList = persistenceService.findAllBy("from WorkOrderActivity where workOrderEstimate.workOrder.id=?",revWorkOrder.getId());
		List<Long> activtityIdList = new ArrayList<Long>();
		if(revWoaList!=null && revWoaList.size()>0)
		{
			List<MBHeader> mbheaderlist = new ArrayList<MBHeader>();
			String errorMessage;
			//First check if any non tendered or lumpsum items are present, if yes then dont allow to cancel
			for(WorkOrderActivity revWoa:revWoaList)
			{
				if(revWoa.getActivity().getRevisionType()!=null &&  (revWoa.getActivity().getRevisionType().equals(RevisionType.LUMP_SUM_ITEM) || revWoa.getActivity().getRevisionType().equals(RevisionType.NON_TENDERED_ITEM)))
				{
					mbheaderlist = measurementBookService.findAllBy("select distinct mbd.mbHeader from MBDetails mbd where mbd.workOrderActivity.workOrderEstimate.estimate.id=? and mbd.workOrderActivity.workOrderEstimate.workOrder.id=? and  mbd.workOrderActivity.activity.id=? " +
							"and mbd.mbHeader.egwStatus.code<>'CANCELLED'", revEstimate.getId(),revWorkOrder.getId(), revWoa.getActivity().getId());
					if(mbheaderlist != null && !mbheaderlist.isEmpty())
					{
						StringBuffer mbNos = new StringBuffer();
						for(MBHeader mbHdr: mbheaderlist)
							mbNos.append(mbHdr.getMbRefNo()+", ");
						errorMessage = getText("cancelRE.MB.created.message")+mbNos.toString().substring(0, mbNos.length()-2)+". "+getText("cancelRE.MB.created.message.part2");
						addActionError(errorMessage);
						return false;
					}
				}
			}	
			for(WorkOrderActivity revWoa: revWoaList )//Add only additional quantity items activities
				if(revWoa.getActivity().getRevisionType()!=null &&  revWoa.getActivity().getRevisionType().equals(RevisionType.ADDITIONAL_QUANTITY))
					activtityIdList.add(revWoa.getActivity().getParent().getId()); // Passing parent of Rev Work order activity, as only these can have MBs created for them
			if(activtityIdList!=null && activtityIdList.size()>0)
			{
				Query qry = getPersistenceService().getSession().createQuery(" select workOrderActivity.activity.id, nvl(sum(quantity),0)  from MBDetails where mbHeader.egwStatus.code!='CANCELLED' and workOrderActivity.activity.id in (:activtityIdList) group by workOrderActivity.activity.id ");
				qry.setParameterList("activtityIdList", activtityIdList);
				List<Object[]> activityIdQuantityList =  qry.list();
				if(activityIdQuantityList!=null && activityIdQuantityList.size()>0)
				{
					for(WorkOrderActivity revWoa:revWoaList)
					{
						if(revWoa.getActivity().getRevisionType()!=null && ! revWoa.getActivity().getRevisionType().equals(RevisionType.ADDITIONAL_QUANTITY))
							continue;
						for(Object[] activityIdQuantity: activityIdQuantityList)
						{
							if(Long.parseLong(activityIdQuantity[0].toString())==revWoa.getActivity().getParent().getId().longValue())
							{
								Long activityId = null;
								if(revWoa.getActivity().getParent()==null)
									activityId = revWoa.getActivity().getId();
								else
									activityId = revWoa.getActivity().getParent().getId();
								double originalQuantity = (Double) persistenceService.find("select sum(woa.approvedQuantity) from WorkOrderActivity woa  group by woa,woa.activity having activity.id = ?",activityId);
								Object revEstQuantityObj = persistenceService.find(" select sum(woa.approvedQuantity*nvl(decode(woa.activity.revisionType,'REDUCED_QUANTITY',-1,'ADDITIONAL_QUANTITY',1,'NON_TENDERED_ITEM',1,'LUMP_SUM_ITEM',1),1)) from WorkOrderActivity woa where woa.activity.abstractEstimate.egwStatus.code = 'APPROVED'  and woa.activity.abstractEstimate.id != ? group by woa.activity.parent having (woa.activity.parent is not null and woa.activity.parent.id = ? )  ",revEstimate.getId(),revWoa.getActivity().getParent().getId());
								double revEstQuantity = (revEstQuantityObj==null?0.0:(Double)revEstQuantityObj);
								if((originalQuantity+revEstQuantity)>=Double.parseDouble( activityIdQuantity[1].toString()))
									continue;
								else
								{
									MBDetails mbDetails =  (MBDetails) persistenceService.find(" from MBDetails mbd where mbd.mbHeader.egwStatus.code != 'CANCELLED' and mbd.workOrderActivity.activity.id = ? and (mbdetailsDate is not null or OrderNumber is not null) ",revWoa.getActivity().getParent().getId());
									if(mbDetails !=null)
									{
										Double maxPercent =  worksService.getConfigval();
										if(maxPercent!=null)
											maxPercent += 100;
										else
											maxPercent  = 100d;
										Double maxAllowedQuantity  = maxPercent*(originalQuantity+revEstQuantity)/100;
										if(maxAllowedQuantity>=Double.parseDouble( activityIdQuantity[1].toString()))
											continue;
										else
										{
											addActionError(getText("cancelRE.MBs.present.message"));
											return false;
										}
									}
									else
									{
										addActionError(getText("cancelRE.MBs.present.message"));
										return false;
									}
								}
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	/*
	 * Validate is there any Advance Requisition forms created for the Work Order Estimate pertaining to this RE  
	 */
	private void validateARFForRE(RevisionAbstractEstimate revisionEstimate) { 
		String arfNo = "";
		BigDecimal advanceAmount = new BigDecimal(0); 
		List<WorkOrderEstimate> woeList = (List<WorkOrderEstimate>) persistenceService.findAllBy(" from WorkOrderEstimate woe where woe.workOrder.egwStatus.code = 'APPROVED' and woe.estimate=?",revisionEstimate.getParent());
		if(woeList != null && !woeList.isEmpty()) {
			for(WorkOrderEstimate woe : woeList) {
				for(ContractorAdvanceRequisition arf : woe.getContractorAdvanceRequisitions()) { 
					if(!arf.getStatus().getCode().equalsIgnoreCase(ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.CANCELLED.toString())) {
						advanceAmount = advanceAmount.add(arf.getAdvanceRequisitionAmount());
						if(!arfNo.equals(""))
							arfNo = arfNo.concat(", ").concat(arf.getAdvanceRequisitionNumber());
						else
							arfNo = arfNo.concat(arf.getAdvanceRequisitionNumber());
					}
				}
			}
		}
		if(!arfNo.equals("")) {
			BigDecimal totalEstimateValueIncludingRE = contractorAdvanceService.getTotalEstimateValueIncludingRE(revisionEstimate.getParent());			
			if(totalEstimateValueIncludingRE.subtract(new BigDecimal(revisionEstimate.getTotalAmount().getValue())).longValue() < advanceAmount.longValue()) {
				throw new ValidationException(Arrays.asList(new ValidationError("cancelRE.arf.created.message",getText("cancelRE.arf.created.message",new String[]{arfNo}))));
			}						
		}
	}
		
	
	/**
	 * @return List of abstract estimates with "positionAndUserName" populated
	 */
	@SuppressWarnings(UNCHECKED)
	private void showOwnerName() {
		List<WorkOrderEstimate> woEstimateList = new LinkedList<WorkOrderEstimate>();
		
		Iterator iter = searchResult.getList().iterator();
		while(iter.hasNext()) {
			Object row = (Object)iter.next();
			WorkOrderEstimate woe = (WorkOrderEstimate) row;
			if(!woe.getEstimate().getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.APPROVED) && !woe.getEstimate().getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.CANCELLED_STATUS)){
				PersonalInformation emp = employeeService.getEmployeeforPosition(woe.getEstimate().getState().getOwnerPosition());
				if(emp!=null){
					if(emp.getUserMaster()!=null){
						woe.getEstimate().setPositionAndUserName(emp.getUserMaster().getName());
					}
				}
			}
			woEstimateList.add(woe);
		}
		searchResult.getList().clear();
		HashSet<WorkOrderEstimate> uniqueWOEstimateList=new HashSet<WorkOrderEstimate>(woEstimateList);
		searchResult.getList().addAll(uniqueWOEstimateList);
	}
	
	public AbstractEstimate getEstimates() {
		return estimates;
	}

	public void setEstimates(AbstractEstimate estimates) {
		this.estimates = estimates;
	}
	
	public Date getFromDate() {
		return fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public List<RevisionAbstractEstimate> getRevEstimateList() {
		return revEstimateList;
	}

	public void setRevEstimateList(List<RevisionAbstractEstimate> revEstimateList) {
		this.revEstimateList = revEstimateList;
	}

	public WorkOrderEstimate getWorkOrderEstimate() {
		return workOrderEstimate;
	}

	public void setWorkOrderEstimate(WorkOrderEstimate workOrderEstimate) {
		this.workOrderEstimate = workOrderEstimate;
	}

	public String getEstimateNumber() {
		return estimateNumber;
	}

	public void setEstimateNumber(String estimateNumber) {
		this.estimateNumber = estimateNumber;
	}

	public Integer getExecDept() {
		return execDept;
	}

	public void setExecDept(Integer execDept) {
		this.execDept = execDept;
	}

	public Integer getReStatus() {
		return reStatus;
	}

	public void setReStatus(Integer reStatus) {
		this.reStatus = reStatus;
	}

	public String getWorkOrderNumber() {
		return workOrderNumber;
	}

	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}

	public Long getRevWOId() {
		return revWOId;
	}

	public void setRevWOId(Long revWOId) {
		this.revWOId = revWOId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public void setRevisionAbstractEstimateService(
			PersistenceService<RevisionAbstractEstimate, Long> revisionAbstractEstimateService) {
		this.revisionAbstractEstimateService = revisionAbstractEstimateService;
	}

	public void setRevisionWorkOrderService(
			PersistenceService<RevisionWorkOrder, Long> revisionWorkOrderService) {
		this.revisionWorkOrderService = revisionWorkOrderService;
	}

	public String getRevisionEstimateNumber() {
		return revisionEstimateNumber;
	}

	public void setRevisionEstimateNumber(String revisionEstimateNumber) {
		this.revisionEstimateNumber = revisionEstimateNumber;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public String getCancelRemarks() {
		return cancelRemarks;
	}

	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public void setRevisionEstimateService(
			RevisionEstimateService revisionEstimateService) {
		this.revisionEstimateService = revisionEstimateService;
	}

	public void setMeasurementBookService(
			MeasurementBookService measurementBookService) {
		this.measurementBookService = measurementBookService;
	}

	public void setContractorAdvanceService(
			ContractorAdvanceService contractorAdvanceService) {
		this.contractorAdvanceService = contractorAdvanceService;
	}

}