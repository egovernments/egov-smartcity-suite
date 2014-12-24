package org.egov.works.models.measurementbook;

import org.egov.infstr.models.BaseModel;
import org.egov.works.models.estimate.MeasurementSheet;
import org.egov.works.models.workorder.WorkOrderMeasurementSheet;
import org.hibernate.validator.constraints.Length;

public class MBMeasurementSheet extends BaseModel{
	private Long no;
	private double uomLength;
	private double width;
	private double depthOrHeight;
	
	@Length(max = 1024, message = "estimate.measurementSheet.remarks.length") 
	private String remarks;
	private char identifier;
	
	private MBDetails mbDetails;
	private WorkOrderMeasurementSheet woMeasurementSheet;
	
	private double quantity;
	
    private double cumulativeQuantity;
    
    private double mbMSheetTotalQuantity;
    
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public double getUomLength() {
		return uomLength;
	}
	public void setUomLength(double uomLength) {
		this.uomLength = uomLength;
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
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
		
	public MBDetails getMbDetails() {
		return mbDetails;
	}
	public void setMbDetails(MBDetails mbDetails) {
		this.mbDetails = mbDetails;
	}
	
	public double getCumulativeQuantity() { 
		return cumulativeQuantity;
	}
	public void setCumulativeQuantity(double cumulativeQuantity) {
		this.cumulativeQuantity = cumulativeQuantity;
	}
	
	public double calculateQuantity(){
		String signedQuantity;
		double mbMSheetQuantity=this.getQuantity();
		if(this.getMbDetails()!=null && this.getMbDetails().getTotalEstQuantity()!=0){
		if(this.getWoMeasurementSheet().getMeasurementSheet().getIdentifier()=='D'){
			signedQuantity="-".concat(Double.toString(mbMSheetQuantity));
			mbMSheetQuantity=Double.parseDouble(signedQuantity);
		}
		}
		else{
			if(this.getIdentifier()=='D'){
				signedQuantity="-".concat(Double.toString(mbMSheetQuantity));
				mbMSheetQuantity=Double.parseDouble(signedQuantity); 
			}
		}
			
		return mbMSheetQuantity;
	}

	public WorkOrderMeasurementSheet getWoMeasurementSheet() {
		return woMeasurementSheet;
	}
	public void setWoMeasurementSheet(WorkOrderMeasurementSheet woMeasurementSheet) {
		this.woMeasurementSheet = woMeasurementSheet;
	}
	public double getMbMSheetTotalQuantity() {
		return mbMSheetTotalQuantity;
	}
	public void setMbMSheetTotalQuantity(double mbMSheetTotalQuantity) {
		this.mbMSheetTotalQuantity = mbMSheetTotalQuantity;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public char getIdentifier() {
		return identifier;
	}
	public void setIdentifier(char identifier) {
		this.identifier = identifier;
	}
	
	
}
