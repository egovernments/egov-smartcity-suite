package org.egov.works.web.actions.qualityControl;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.qualityControl.TestMaster;
import org.egov.works.models.qualityControl.TestRate;
import org.egov.works.models.qualityControl.TestSheetHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.qualityControl.TestSheetHeaderService;
 
public class AjaxTestSheetAction extends BaseFormAction{
	
	private static final String SEARCH_ESTIMATENUMBER="searchEstimateNumber";
	private static final String QC_TEST_NAMES="testNames";
	private static final String QC_TEST_CHARGES="testCharges";
	private static final String SEARCH_TESTSHEETNUMBER="searchTestSheetNumber";
	private static final String SEARCH_WONUMBER="searchWONumber";
	private String query;
	private AbstractEstimateService abstractEstimateService;
	private TestSheetHeaderService testSheetHeaderService;
	private WorkOrderService workOrderService;
	private Long materialTypeId;
	private List<TestMaster> testMasterList=new LinkedList<TestMaster>();
	private static final Logger LOGGER = Logger.getLogger(AjaxTestSheetAction.class);
	private Long testMasterID;
	private double testCharges;
	private String recId;
	private String type;

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String searchEstimateNumberAjax(){
		return SEARCH_ESTIMATENUMBER;
	}
	
	
	public Collection<AbstractEstimate> getEstimateList() {
		if(type!=null && type.equalsIgnoreCase("searchWOForSampleLetter")) 
			return abstractEstimateService.findAllBy("from AbstractEstimate where parent.id is null and id in " +
					"(select woe.estimate.id from WorkOrderEstimate woe where woe.workOrder.id in " +
					"(select workOrder.id from TestSheetHeader where id not in " +
					"(select testSheetHeader.id from SampleLetterHeader where egwStatus.code!='CANCELLED') and egwStatus.code='APPROVED')) " +
					"and upper(estimateNumber) like '%' || ? || '%' order by id", query.toUpperCase());
		else
			return abstractEstimateService.findAllBy("from AbstractEstimate where parent.id is null and upper(estimateNumber) like '%' || ? || '%'", query.toUpperCase());
		
		//return abstractEstimateService.findAllBy("from woe.estimate from WorkOrderEstimate woe where woe.workOrder.parent.id is null and upper(woe.estimate.estimateNumber) like '%' || ? || '%'", query.toUpperCase());
			
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}
	
	public String populateTestName(){
		try{	
			testMasterList=persistenceService.findAllBy("from TestMaster where materialType.id = ?",materialTypeId); 
		}catch(Exception e){
			LOGGER.error("Error while loading TestMaster." + e.getMessage());
			throw new EGOVRuntimeException("Unable to load TestMaster information",e);
		}
		return QC_TEST_NAMES;
	}
	
	public String populateTestCharges(){
		try{	
			List<TestRate> testRateList=new LinkedList<TestRate>();
			testRateList= persistenceService.findAllBy("from TestRate tr where tr.testMaster.id = ?" +
					" and ((? between tr.startDate and tr.endDate ) or (tr.startDate<=? and tr.endDate is null))",testMasterID,new Date(),new Date());
			if(testRateList!=null && testRateList.size()!=0){
				for(TestRate tr : testRateList){
					testCharges=testCharges+tr.getRate().getValue();
				}
			}
		}catch(Exception e){
			LOGGER.error("Error while loading TestCharges." + e.getMessage());
			throw new EGOVRuntimeException("Unable to load TestCharges information",e);
		}
		return QC_TEST_CHARGES;
	}
	
	public String searchTestSheetNumberAjax(){
		return SEARCH_TESTSHEETNUMBER;
	}
	
	public Collection<TestSheetHeader> getTestSheetHeaderList() { 
		if(type!=null && type.equalsIgnoreCase("searchWOForSampleLetter"))
			return testSheetHeaderService.findAllBy(" select tsh from TestSheetHeader tsh where tsh.id not in (select slh.testSheetHeader.id from  SampleLetterHeader slh where slh.egwStatus.code!='CANCELLED') and tsh.egwStatus.code='APPROVED' and  upper(testSheetNumber) like '%' || ? || '%' order by id", query.toUpperCase());
		else
			return testSheetHeaderService.findAllBy("from TestSheetHeader where upper(testSheetNumber) like '%' || ? || '%'", query.toUpperCase());
	} 
	
	public String searchWONumberAjax(){ 
		return SEARCH_WONUMBER;
	}
	
	public Collection<WorkOrder> getWorkOrderList() {   
		if(type!=null && type.equalsIgnoreCase("searchWOForSampleLetter"))
			return workOrderService.findAllBy("select distinct(tsh.workOrder) from TestSheetHeader tsh where tsh.id not in (select slh.testSheetHeader.id from  SampleLetterHeader slh where slh.egwStatus.code!='CANCELLED') and tsh.egwStatus.code='APPROVED' and upper(tsh.workOrder.workOrderNumber) like '%' || ? || '%'", query.toUpperCase());
		else
			return workOrderService.findAllBy("select distinct(tsh.workOrder) from TestSheetHeader tsh where upper(tsh.workOrder.workOrderNumber) like '%' || ? || '%'", query.toUpperCase());
	}

	public Long getMaterialTypeId() {
		return materialTypeId;
	}

	public void setMaterialTypeId(Long materialTypeId) {
		this.materialTypeId = materialTypeId;
	}

	public List<TestMaster> getTestMasterList() {
		return testMasterList;
	}

	public void setTestMasterList(List<TestMaster> testMasterList) {
		this.testMasterList = testMasterList;
	}

	public Long getTestMasterID() {
		return testMasterID;
	}

	public void setTestMasterID(Long testMasterID) {
		this.testMasterID = testMasterID;
	}

	public double getTestCharges() {
		return testCharges;
	}

	public void setTestCharges(double testCharges) {
		this.testCharges = testCharges;
	}

	public void setTestSheetHeaderService(
			TestSheetHeaderService testSheetHeaderService) {
		this.testSheetHeaderService = testSheetHeaderService;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	public String getRecId() {
		return recId;
	}

	public void setRecId(String recId) {
		this.recId = recId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
