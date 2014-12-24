package org.egov.works.web.actions.qualityControl;

import java.util.ArrayList;
import java.util.List;

import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.qualityControl.JobHeader;
import org.egov.works.models.qualityControl.SampleLetterHeader;
import org.egov.works.models.qualityControl.TestSheetHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.qualityControl.AllocateJobService;
import org.egov.works.services.qualityControl.SampleLetterService;
import org.egov.works.services.qualityControl.TestSheetHeaderService;

public class AjaxJobNumberAction  extends BaseFormAction{
	private List<TestSheetHeader> testSheetList= new ArrayList<TestSheetHeader>();
	private List<WorkOrder> workOrderList= new ArrayList<WorkOrder>();
	private List<SampleLetterHeader> sampleLetterList= new ArrayList<SampleLetterHeader>();
	private List<JobHeader> jobNumberList= new ArrayList<JobHeader>();
	private TestSheetHeaderService testSheetHeaderService;
	private String query;
	private WorkOrderService workOrderService;
	private SampleLetterService sampleLetterService;
	private AllocateJobService allocateJobService;
	private String mode;
	private static final String TESTSHEET_LIST = "testSheet";
	private static final String WORKORDER_LIST = "workOrder";
	private static final String SAMPLELETTER_LIST = "sampleLetter";
	private static final String JOBNUMBER_LIST = "jobNumber";
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String searchTestSheetNumberAjax(){
			testSheetList= testSheetHeaderService.findAllBy(" select distinct(jh.sampleLetterHeader.testSheetHeader) from  JobHeader jh where jh.egwStatus.code!='NEW' and  upper(jh.sampleLetterHeader.testSheetHeader.testSheetNumber) like '%' || ? || '%'", query.toUpperCase());
		return TESTSHEET_LIST;
	}
	
	public String searchWONumberAjax(){
			workOrderList= workOrderService.findAllBy(" select distinct(jh.sampleLetterHeader.testSheetHeader.workOrder) from  JobHeader jh where jh.egwStatus.code!='NEW' and  upper(jh.sampleLetterHeader.testSheetHeader.workOrder.workOrderNumber) like '%' || ? || '%'", query.toUpperCase());
		return WORKORDER_LIST;
	}
	
	public String searchSampleLetterNumberAjax(){ 
		mode="sampleLetter";
			sampleLetterList= sampleLetterService.findAllBy(" select jh.sampleLetterHeader from  JobHeader jh where jh.egwStatus.code!='NEW' and  upper(jh.sampleLetterHeader.sampleLetterNumber) like '%' || ? || '%'", query.toUpperCase());
		return SAMPLELETTER_LIST;
	} 
	
	public String searchCoveringLetterNumberAjax(){
		mode="coveringLetter";
			sampleLetterList= sampleLetterService.findAllBy(" select jh.sampleLetterHeader from  JobHeader jh where jh.egwStatus.code!='NEW' and  upper(jh.sampleLetterHeader.coveringLetterNumber) like '%' || ? || '%'", query.toUpperCase());
		return SAMPLELETTER_LIST;
	}
	
	public String searchJobNumberAjax(){ 
		jobNumberList= allocateJobService.findAllBy(" from  JobHeader  where egwStatus.code!='NEW' and  upper(jobNumber) like '%' || ? || '%'", query.toUpperCase());
		return JOBNUMBER_LIST; 
	}
 
	public List<TestSheetHeader> getTestSheetList() {
		return testSheetList;
	}

	public void setTestSheetList(List<TestSheetHeader> testSheetList) {
		this.testSheetList = testSheetList;
	}

	public List<WorkOrder> getWorkOrderList() {
		return workOrderList;
	}

	public void setWorkOrderList(List<WorkOrder> workOrderList) {
		this.workOrderList = workOrderList;
	}

	public List<SampleLetterHeader> getSampleLetterList() {
		return sampleLetterList;
	}

	public void setSampleLetterList(List<SampleLetterHeader> sampleLetterList) {
		this.sampleLetterList = sampleLetterList;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setTestSheetHeaderService(
			TestSheetHeaderService testSheetHeaderService) {
		this.testSheetHeaderService = testSheetHeaderService;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	public void setSampleLetterService(SampleLetterService sampleLetterService) {
		this.sampleLetterService = sampleLetterService;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void setAllocateJobService(AllocateJobService allocateJobService) {
		this.allocateJobService = allocateJobService;
	}

	public List<JobHeader> getJobNumberList() {
		return jobNumberList;
	}

	public void setJobNumberList(List<JobHeader> jobNumberList) {
		this.jobNumberList = jobNumberList;
	}
}
