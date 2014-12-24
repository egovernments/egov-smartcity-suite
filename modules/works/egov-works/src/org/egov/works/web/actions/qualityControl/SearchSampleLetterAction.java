package org.egov.works.web.actions.qualityControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.web.actions.SearchFormAction;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.qualityControl.SampleLetterHeader;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;

public class SearchSampleLetterAction extends SearchFormAction {
	private SampleLetterHeader sampleLetterHeader = new SampleLetterHeader(); 
	private DepartmentService departmentService;
	private String testSheetNumber;
	private String workordernumber;
	private Date fromDate;
	private Date toDate;
	private Long contractorId;
	private PersistenceService<Contractor, Long> contractorService;
	private String sampleLetterNumber;
	private String coveringLetterNumber;
	private Integer deptId; 
	private Map<String,Object> criteriaMap=null;
	private static final String DATE_FORMAT="dd-MMM-yyyy";
	private WorksService worksService; 
	private String sourcePage;

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return sampleLetterHeader;
	}
	
	public void prepare() {
		if(!(sourcePage!=null && sourcePage.equalsIgnoreCase("searchSLForCreateJob")))
			addDropdownData("deptListForSearch",departmentService.getAllDepartments());
	}
	
	
	public Map<String,Object> getContractorForSampleLetter() {
		Map<String,Object> contractorsList = new LinkedHashMap<String, Object>();
		List<Contractor> contractorList=contractorService.findAllByNamedQuery("getContractorsWithSampleLetter");
		
		if(contractorList!=null && !contractorList.isEmpty()) {
			for(Contractor contractor :contractorList){
				contractorsList.put(contractor.getId()+"", contractor.getCode()+" - "+contractor.getName());
			}			
		}
		return contractorsList; 
	}
	
	public String search(){
	criteriaMap = new HashMap<String, Object>();
	if(StringUtils.isNotBlank(workordernumber))
		criteriaMap.put("WORKORDER_NO",workordernumber);
	if(StringUtils.isNotBlank(testSheetNumber))
		criteriaMap.put("TESTSHEET_NO",testSheetNumber);
	if(contractorId!=null && contractorId!=-1)
		criteriaMap.put("CONTRACTOR_ID", contractorId);
	if(!(sourcePage!=null && sourcePage.equalsIgnoreCase("searchSLForCreateJob"))){
		if(getDeptId()!=null && getDeptId()>0)
			criteriaMap.put("DEPT_ID",getDeptId());
	}
	if(StringUtils.isNotBlank(sampleLetterNumber))
		criteriaMap.put("SAMPLELETTER_NO",sampleLetterNumber);
	if(StringUtils.isNotBlank(coveringLetterNumber))
		criteriaMap.put("COVERINGLETTER_NO",coveringLetterNumber);
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
		setActions(searchResult.getList());
	return retVal;
	}
	
	private void setActions(List<SampleLetterHeader>  sampleLetterHeaderList) {
		String actions = worksService.getWorksConfigValue("SAMPLELETTER_SEARCH_ACTIONS");
		for(SampleLetterHeader slh: sampleLetterHeaderList){
			if(actions!=null && ! actions.equals("")){
				slh.getSampleLetterActions().addAll(Arrays.asList(actions.split(",")));
				if(slh.getEgwStatus()!=null && slh.getEgwStatus().getCode().equalsIgnoreCase("CANCELLED")){
					if(slh.getSampleLetterActions().contains("View Sample Letter PDF"))
						slh.getSampleLetterActions().remove("View Sample Letter PDF");
					if(slh.getSampleLetterActions().contains("View Covering Letter PDF"))
						slh.getSampleLetterActions().remove("View Covering Letter PDF");
				}
			}
		} 
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		// TODO Auto-generated method stub
		String dynQuery;
		if(sourcePage!=null && sourcePage.equalsIgnoreCase("searchSLForCreateJob")){
			 dynQuery =" from SampleLetterHeader slh where slh.id is not null and slh.egwStatus.code='APPROVED' " +
			 		"and slh.id not in (select sampleLetterHeader.id from JobHeader where egwStatus.code!='CANCELLED') ";
		} 
		else
			 dynQuery =" from SampleLetterHeader slh where slh.id is not null and slh.egwStatus.code!='NEW' " ;
		
		
		List<Object> paramList = new ArrayList<Object>();
		
		if(criteriaMap.get("FROM_DATE") != null && criteriaMap.get("TO_DATE")==null) {
			dynQuery = dynQuery + " and slh.sampleLetterDate >= ? ";
			paramList.add(criteriaMap.get("FROM_DATE"));

		}else if(criteriaMap.get("TO_DATE") != null && criteriaMap.get("FROM_DATE")==null) {
			dynQuery = dynQuery + " and slh.sampleLetterDate <= ? ";
			paramList.add(criteriaMap.get("TO_DATE"));
		}else if(criteriaMap.get("FROM_DATE") != null && criteriaMap.get("TO_DATE")!=null) {
			dynQuery = dynQuery + " and slh.sampleLetterDate between ? and ? ";
			paramList.add(criteriaMap.get("FROM_DATE")); 
			paramList.add(criteriaMap.get("TO_DATE"));
		}
		if(criteriaMap.get("WORKORDER_NO") != null){
			dynQuery = dynQuery + " and UPPER(slh.testSheetHeader.workOrder.workOrderNumber) like '%"+criteriaMap.get("WORKORDER_NO").toString().trim().toUpperCase()+"%'";
		}
		if(criteriaMap.get("TESTSHEET_NO") != null){
			dynQuery = dynQuery + " and UPPER(slh.testSheetHeader.testSheetNumber) like '%"+criteriaMap.get("TESTSHEET_NO").toString().trim().toUpperCase()+"%'";
		}
		if(criteriaMap.get("CONTRACTOR_ID") != null){
			dynQuery = dynQuery + " and slh.testSheetHeader.workOrder.contractor.id = ? ";
			paramList.add(criteriaMap.get("CONTRACTOR_ID"));
		}
		if(criteriaMap.get("SAMPLELETTER_NO") != null){
			dynQuery = dynQuery + " and UPPER(slh.sampleLetterNumber) like '%"+criteriaMap.get("SAMPLELETTER_NO").toString().trim().toUpperCase()+"%'";
		}
		if(criteriaMap.get("COVERINGLETTER_NO") != null){
			dynQuery = dynQuery + " and UPPER(slh.coveringLetterNumber) like '%"+criteriaMap.get("COVERINGLETTER_NO").toString().trim().toUpperCase()+"%'";
		}
		if(!(sourcePage!=null && sourcePage.equalsIgnoreCase("searchSLForCreateJob"))){
			if(criteriaMap.get("DEPT_ID") != null){
				dynQuery = dynQuery + " and slh.testSheetHeader.workOrder.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=slh.testSheetHeader.workOrder.id and " +
						" we.estimate.executingDepartment.id = ?) ";
				paramList.add(criteriaMap.get("DEPT_ID"));
			}
		}
		
	String testSheetSearchQuery="select distinct slh "+	dynQuery;
	String countQuery = "select distinct count(slh) " + dynQuery;
	return new SearchQueryHQL(testSheetSearchQuery, countQuery, paramList);
	}
	
	public SampleLetterHeader getSampleLetterHeader() {
		return sampleLetterHeader;
	}

	public void setSampleLetterHeader(SampleLetterHeader sampleLetterHeader) {
		this.sampleLetterHeader = sampleLetterHeader;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public String getTestSheetNumber() {
		return testSheetNumber;
	}

	public void setTestSheetNumber(String testSheetNumber) {
		this.testSheetNumber = testSheetNumber;
	}

	public String getWorkordernumber() {
		return workordernumber;
	}

	public void setWorkordernumber(String workordernumber) {
		this.workordernumber = workordernumber;
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

	public void setContractorService(
			PersistenceService<Contractor, Long> contractorService) {
		this.contractorService = contractorService;
	}

	public String getSampleLetterNumber() {
		return sampleLetterNumber;
	}

	public void setSampleLetterNumber(String sampleLetterNumber) {
		this.sampleLetterNumber = sampleLetterNumber;
	}

	public String getCoveringLetterNumber() {
		return coveringLetterNumber;
	}

	public void setCoveringLetterNumber(String coveringLetterNumber) {
		this.coveringLetterNumber = coveringLetterNumber;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Map<String, Object> getCriteriaMap() {
		return criteriaMap;
	}

	public void setCriteriaMap(Map<String, Object> criteriaMap) {
		this.criteriaMap = criteriaMap;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public String getSourcePage() {
		return sourcePage;
	}

	public void setSourcePage(String sourcePage) {
		this.sourcePage = sourcePage;
	}

}
