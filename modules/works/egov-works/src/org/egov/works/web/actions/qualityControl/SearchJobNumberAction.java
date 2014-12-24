package org.egov.works.web.actions.qualityControl;

import java.util.ArrayList;
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
import org.egov.web.actions.SearchFormAction;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.qualityControl.JobHeader;
import org.egov.works.utils.WorksConstants;

public class SearchJobNumberAction extends SearchFormAction{
	
	private JobHeader jobHeader = new JobHeader();
	private PersistenceService<Contractor, Long> contractorService;
	private String testSheetNumber;
	private String workordernumber;
	private Date fromDate;
	private Date toDate;
	private Long contractorId;
	private String sampleLetterNumber;
	private String coveringLetterNumber;
	private String jobNumber;
	private Map<String,Object> criteriaMap=null;
	private static final String DATE_FORMAT="dd-MMM-yyyy";

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return jobHeader;
	}

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		// TODO Auto-generated method stub
		String dynQuery;
	    dynQuery =" from JobHeader jh where jh.id is not null and jh.egwStatus.code!='NEW' " ;
		
		
		List<Object> paramList = new ArrayList<Object>();
		
		if(criteriaMap.get("FROM_DATE") != null && criteriaMap.get("TO_DATE")==null) {
			dynQuery = dynQuery + " and jh.jobDate >= ? ";
			paramList.add(criteriaMap.get("FROM_DATE"));

		}else if(criteriaMap.get("TO_DATE") != null && criteriaMap.get("FROM_DATE")==null) {
			dynQuery = dynQuery + " and jh.jobDate <= ? ";
			paramList.add(criteriaMap.get("TO_DATE"));
		}else if(criteriaMap.get("FROM_DATE") != null && criteriaMap.get("TO_DATE")!=null) {
			dynQuery = dynQuery + " and jh.jobDate between ? and ? ";
			paramList.add(criteriaMap.get("FROM_DATE")); 
			paramList.add(criteriaMap.get("TO_DATE"));
		}
		if(criteriaMap.get("WORKORDER_NO") != null){
			dynQuery = dynQuery + " and UPPER(jh.sampleLetterHeader.testSheetHeader.workOrder.workOrderNumber) like '%"+criteriaMap.get("WORKORDER_NO").toString().trim().toUpperCase()+"%'";
		}
		if(criteriaMap.get("TESTSHEET_NO") != null){
			dynQuery = dynQuery + " and UPPER(jh.sampleLetterHeader.testSheetHeader.testSheetNumber) like '%"+criteriaMap.get("TESTSHEET_NO").toString().trim().toUpperCase()+"%'";
		}
		if(criteriaMap.get("CONTRACTOR_ID") != null){
			dynQuery = dynQuery + " and jh.sampleLetterHeader.testSheetHeader.workOrder.contractor.id = ? ";
			paramList.add(criteriaMap.get("CONTRACTOR_ID"));
		}
		if(criteriaMap.get("SAMPLELETTER_NO") != null){
			dynQuery = dynQuery + " and UPPER(jh.sampleLetterHeader.sampleLetterNumber) like '%"+criteriaMap.get("SAMPLELETTER_NO").toString().trim().toUpperCase()+"%'";
		}
		if(criteriaMap.get("COVERINGLETTER_NO") != null){
			dynQuery = dynQuery + " and UPPER(jh.sampleLetterHeader.coveringLetterNumber) like '%"+criteriaMap.get("COVERINGLETTER_NO").toString().trim().toUpperCase()+"%'";
		}
		if(criteriaMap.get("JOB_NO") != null){
			dynQuery = dynQuery + " and UPPER(jh.jobNumber) like '%"+criteriaMap.get("JOB_NO").toString().trim().toUpperCase()+"%'";
		}
		
	String testSheetSearchQuery="select distinct jh "+	dynQuery;
	String countQuery = "select distinct count(jh) " + dynQuery;
	return new SearchQueryHQL(testSheetSearchQuery, countQuery, paramList);
	}
	
	public Map<String,Object> getContractorForJobNumber() {
		Map<String,Object> contractorsList = new LinkedHashMap<String, Object>();
		List<Contractor> contractorList=contractorService.findAllByNamedQuery("getContractorsWithJobNo");
		
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
		if(StringUtils.isNotBlank(sampleLetterNumber))
			criteriaMap.put("SAMPLELETTER_NO",sampleLetterNumber);
		if(StringUtils.isNotBlank(coveringLetterNumber))
			criteriaMap.put("COVERINGLETTER_NO",coveringLetterNumber);
		if(StringUtils.isNotBlank(jobNumber))
			criteriaMap.put("JOB_NO",jobNumber);
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

	public JobHeader getJobHeader() {
		return jobHeader;
	}

	public void setJobHeader(JobHeader jobHeader) {
		this.jobHeader = jobHeader;
	}

	public void setContractorService(
			PersistenceService<Contractor, Long> contractorService) {
		this.contractorService = contractorService;
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

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public Map<String, Object> getCriteriaMap() {
		return criteriaMap;
	}

	public void setCriteriaMap(Map<String, Object> criteriaMap) {
		this.criteriaMap = criteriaMap;
	}

	public Long getContractorId() {
		return contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

}
