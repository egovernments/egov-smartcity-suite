package org.egov.works.models.qualityControl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.infstr.models.StateAware;
import org.egov.works.models.workorder.WorkOrder;

public class TestSheetHeader extends StateAware{
	
	private String testSheetNumber;
	private WorkOrder workOrder;
	private Date testSheetDate;
	private EgwStatus egwStatus;
	private double testSheetCharges;
		
	//@Valid
	private List<TestSheetDetails> testSheetDetails = new LinkedList<TestSheetDetails>();

	@Override
	public String getStateDetails() {
		// TODO Auto-generated method stub
		return "Test Sheet : " + getTestSheetNumber();
	}

	public void addTestSheetDetails(TestSheetDetails tsd){
		this.testSheetDetails.add(tsd);
	}
	
	public String getTestSheetNumber() {
		return testSheetNumber;
	}

	public void setTestSheetNumber(String testSheetNumber) {
		this.testSheetNumber = testSheetNumber;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	public Date getTestSheetDate() {
		return testSheetDate;
	}

	public void setTestSheetDate(Date testSheetDate) {
		this.testSheetDate = testSheetDate;
	}

	public EgwStatus getEgwStatus() {
		return egwStatus;
	}

	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}

	public List<TestSheetDetails> getTestSheetDetails() {
		return testSheetDetails;
	}

	public void setTestSheetDetails(List<TestSheetDetails> testSheetDetails) {
		this.testSheetDetails = testSheetDetails;
	}

	public double getTestSheetCharges() {
		for(TestSheetDetails tsd : testSheetDetails){
			testSheetCharges=testSheetCharges+tsd.getTestCharges().getValue();
		}
		return testSheetCharges;
	}
}
