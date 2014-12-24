package org.egov.works.web.actions.qualityControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.web.actions.SearchFormAction;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.qualityControl.TestSheetHeader;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;

public class SearchTestSheetAction extends SearchFormAction {
	private TestSheetHeader testSheetHeader = new TestSheetHeader(); 
	private static final Logger LOGGER = Logger.getLogger(SearchTestSheetAction.class);
	private CommonsService commonsService;
	private WorkOrderService workOrderService;
	
	private String testSheetNumber;
	private String status;
	private Date fromDate;
	private Date toDate;
	private Long contractorId;
	private String workordernumber;
	private Map<String,Object> criteriaMap=null;
	private List<TestSheetHeader> testSheetHeaderList=new ArrayList<TestSheetHeader>();
	private static final String DATE_FORMAT="dd-MMM-yyyy";
	private WorksService worksService; 
	private PersistenceService<Contractor, Long> contractorService;

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return testSheetHeader;
	}
	
	public void prepare() {
		
	}
	
	public List<EgwStatus> getTestSheetStatuses() {
		List<EgwStatus> tsStatusList=commonsService.getStatusByModule(TestSheetHeader.class.getSimpleName());
		tsStatusList.remove(commonsService.getStatusByModuleAndCode(TestSheetHeader.class.getSimpleName(), "NEW"));		
		return tsStatusList; 
	}
	
	public Map<String,Object> getContractorForTestSheet() { 
		Map<String,Object> contractorsList = new LinkedHashMap<String, Object>();
		List<Contractor> contractorList=contractorService.findAllByNamedQuery("getContractorsWithTestSheet");
		
		if(contractorList!=null && !contractorList.isEmpty()) {
			for(Contractor contractor :contractorList){
				contractorsList.put(contractor.getId()+"", contractor.getCode()+" - "+contractor.getName());
			}			
		}
		return contractorsList; 
	}
			

	public String search(){
		criteriaMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(status) && !getStatus().equals("-1"))	{
			criteriaMap.put("STATUS",status);
		}
		if(StringUtils.isNotBlank(workordernumber))
			criteriaMap.put("WORKORDER_NO",workordernumber);
		if(StringUtils.isNotBlank(testSheetNumber))
			criteriaMap.put("TESTSHEET_NO",testSheetNumber);
		if(contractorId!=null && contractorId!=-1)
			criteriaMap.put("CONTRACTOR_ID", contractorId);
		if(fromDate!=null && toDate!=null && !DateUtils.compareDates(getToDate(),getFromDate()))
			addFieldError("enddate",getText("greaterthan.endDate.fromDate"));
		if(toDate!=null && !DateUtils.compareDates(new Date(),getToDate()))
			addFieldError("enddate",getText("greaterthan.endDate.currentdate"));
		if(!getFieldErrors().isEmpty())
			return SUCCESS;
		if(fromDate!=null && toDate==null){
			criteriaMap.put("FROM_DATE",new Date(DateUtils.getFormattedDate(getFromDate(),DATE_FORMAT )));
		}else if(toDate!=null && fromDate==null){
			 criteriaMap.put("TO_DATE",new Date(DateUtils.getFormattedDate(getToDate(),DATE_FORMAT )));
		}else if(fromDate!=null && toDate!=null && getFieldErrors().isEmpty()){
			criteriaMap.put("FROM_DATE", new Date(DateUtils.getFormattedDate(getFromDate(),DATE_FORMAT )));
		    criteriaMap.put("TO_DATE",new Date(DateUtils.getFormattedDate(getToDate(),DATE_FORMAT)));
		}
		
			setPageSize(WorksConstants.PAGE_SIZE);
			String retVal = super.search();
		
		return retVal;
	}
	
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		String dynQuery =" from TestSheetHeader tsh where tsh.id is not null and tsh.egwStatus.code!='NEW'" ; 
		
			List<Object> paramList = new ArrayList<Object>();
			
			if(criteriaMap.get("STATUS") != null) {
				dynQuery = dynQuery + " and tsh.egwStatus.code=? ";
				paramList.add(criteriaMap.get("STATUS"));
			}
			if(criteriaMap.get("FROM_DATE") != null && criteriaMap.get("TO_DATE")==null) {
				dynQuery = dynQuery + " and tsh.testSheetDate >= ? ";
				paramList.add(criteriaMap.get("FROM_DATE"));

			}else if(criteriaMap.get("TO_DATE") != null && criteriaMap.get("FROM_DATE")==null) {
				dynQuery = dynQuery + " and tsh.testSheetDate <= ? ";
				paramList.add(criteriaMap.get("TO_DATE"));
			}else if(criteriaMap.get("FROM_DATE") != null && criteriaMap.get("TO_DATE")!=null) {
				dynQuery = dynQuery + " and tsh.testSheetDate between ? and ? ";
				paramList.add(criteriaMap.get("FROM_DATE")); 
				paramList.add(criteriaMap.get("TO_DATE"));
			}
			if(criteriaMap.get("WORKORDER_NO") != null){
				dynQuery = dynQuery + " and UPPER(tsh.workOrder.workOrderNumber) like '%"+criteriaMap.get("WORKORDER_NO").toString().trim().toUpperCase()+"%'";
			}
			if(criteriaMap.get("TESTSHEET_NO") != null){
				dynQuery = dynQuery + " and UPPER(tsh.testSheetNumber) like '%"+criteriaMap.get("TESTSHEET_NO").toString().trim().toUpperCase()+"%'";
			}
			if(criteriaMap.get("CONTRACTOR_ID") != null){
				dynQuery = dynQuery + " and tsh.workOrder.contractor.id = ? ";
				paramList.add(criteriaMap.get("CONTRACTOR_ID"));
			}
			
		String testSheetSearchQuery="select distinct tsh "+	dynQuery;
		String countQuery = "select distinct count(tsh) " + dynQuery;
		return new SearchQueryHQL(testSheetSearchQuery, countQuery, paramList);
	}
	
	
	public List<String> getTestSheetActions() { 
		String actions = worksService.getWorksConfigValue("TESTSHEET_SEARCH_ACTIONS");
		if(actions!=null)
		  return Arrays.asList(actions.split(","));
		return new ArrayList<String>();
	}

	public TestSheetHeader getTestSheetHeader() {
		return testSheetHeader;
	}

	public void setTestSheetHeader(TestSheetHeader testSheetHeader) {
		this.testSheetHeader = testSheetHeader;
	}

	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	public String getTestSheetNumber() {
		return testSheetNumber;
	}

	public void setTestSheetNumber(String testSheetNumber) {
		this.testSheetNumber = testSheetNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Long getContractorId() {
		return contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	public String getWorkordernumber() {
		return workordernumber;
	}

	public void setWorkordernumber(String workordernumber) {
		this.workordernumber = workordernumber;
	}

	public Map<String, Object> getCriteriaMap() {
		return criteriaMap;
	}

	public void setCriteriaMap(Map<String, Object> criteriaMap) {
		this.criteriaMap = criteriaMap;
	}

	public List<TestSheetHeader> getTestSheetHeaderList() {
		return testSheetHeaderList;
	}

	public void setTestSheetHeaderList(List<TestSheetHeader> testSheetHeaderList) {
		this.testSheetHeaderList = testSheetHeaderList;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public void setContractorService(
			PersistenceService<Contractor, Long> contractorService) {
		this.contractorService = contractorService;
	} 

}
