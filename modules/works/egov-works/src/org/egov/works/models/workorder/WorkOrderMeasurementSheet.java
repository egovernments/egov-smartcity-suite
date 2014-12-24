package org.egov.works.models.workorder;

import org.egov.infstr.models.BaseModel;
import org.egov.works.models.estimate.MeasurementSheet;

public class WorkOrderMeasurementSheet extends BaseModel {
	
	private Long no;
	private double length;
	private double width;
	private double depthOrHeight;
	
	private WorkOrderActivity woActivity;
	private MeasurementSheet measurementSheet;  
	
	private double quantity;
		
	 private double cumulativeQuantity; 
		
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getDepthOrHeight() {
		return depthOrHeight;
	}
	public void setDepthOrHeight(double depthOrHeight) {
		this.depthOrHeight = depthOrHeight;
	}
	public WorkOrderActivity getWoActivity() {
		return woActivity;
	}
	public void setWoActivity(WorkOrderActivity woActivity) {
		this.woActivity = woActivity;
	}
	public MeasurementSheet getMeasurementSheet() {
		return measurementSheet;
	}
	public void setMeasurementSheet(MeasurementSheet measurementSheet) {
		this.measurementSheet = measurementSheet;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getCumulativeQuantity() {
		return cumulativeQuantity;
	}
	public void setCumulativeQuantity(double cumulativeQuantity) {
		this.cumulativeQuantity = cumulativeQuantity;
	}
}
