package org.egov.works.models.workorder;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.egov.commons.EgwStatus;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Money;
import org.egov.infstr.models.StateAware;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.measurementbook.MBHeader;
import javax.validation.Valid;

public class WorkCompletionDetail extends StateAware{
	private WorkOrder workOrder;
	private Date workCompletionDate;
	private String contractPeriod;
	private double defectLiabilityPeriod;
	private String reasonForExtension;
	private EgwStatus egwStatus;
	
	public WorkOrder getWorkOrder() {
		return workOrder;
	}
	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}
	
	private Set<MBHeader> mbHeaders = new HashSet<MBHeader>();

	public Set<MBHeader> getMbHeaders() {
		return mbHeaders;
	}
	public void setMbHeaders(Set<MBHeader> mbHeaders) {
		this.mbHeaders = mbHeaders;
	}
	public Date getWorkCompletionDate() {
		return workCompletionDate;
	}
	public void setWorkCompletionDate(Date workCompletionDate) {
		this.workCompletionDate = workCompletionDate;
	}
	@Override
	public String getStateDetails() {
		return "Work Order: "+this.workOrder.getWorkOrderNumber();
	}
	public String getContractPeriod() {
		return contractPeriod;
	}
	public void setContractPeriod(String contractPeriod) {
		this.contractPeriod = contractPeriod;
	}
	public double getDefectLiabilityPeriod() {
		return defectLiabilityPeriod;
	}
	public void setDefectLiabilityPeriod(double defectLiabilityPeriod) {
		this.defectLiabilityPeriod = defectLiabilityPeriod;
	}
	public String getReasonForExtension() {
		return reasonForExtension;
	}
	public void setReasonForExtension(String reasonForExtension) {
		this.reasonForExtension = reasonForExtension;
	}
	public EgwStatus getEgwStatus() {
		return egwStatus;
	}
	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}

}
