package org.egov.works.models.estimate;

import java.text.DecimalFormat;

import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.Length;

public class MeasurementSheet extends BaseModel{
	private Long no;
	private double uomLength;
	private double width;
	private double depthOrHeight;
		
	@Length(max = 1024, message = "estimate.measurementSheet.remarks.length") 
	private String remarks;
	private char identifier;
	private Activity activity;
	
	private Integer slNo;
	private String uom;
	private double quantity;
	
	private String recId;
	
	private Integer  mbExtraItemSlNo;
	private Long consumedNo;
	private double consumedLength;
	private double consumedWidth;
	private double consumedDH;
	private double consumedQuantity;
	
	
	public String getUomLengthString() {
		return numberFormat(Double.toString(uomLength)).toString();
	}
	
	public double getUomLength() {
		return uomLength; 
	}
	public void setUomLength(double uomLength) { 
		this.uomLength = uomLength;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) { 
		this.remarks = remarks;
	}
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	public String getWidthString() {
		return numberFormat(Double.toString(width)).toString();
	}
	
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	
	public String getDepthOrHeightString() {
		return numberFormat(Double.toString(depthOrHeight)).toString();
	}
	
	public double getDepthOrHeight() {
		return depthOrHeight;
	}
	public void setDepthOrHeight(double depthOrHeight) {
		this.depthOrHeight = depthOrHeight;
	}
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public char getIdentifier() {
		return identifier;
	}
	public void setIdentifier(char identifier) {
		this.identifier = identifier;
	}
	public String getRecId() {
		return recId;
	}
	public void setRecId(String recId) {
		this.recId = recId;
	}
	public Integer getSlNo() {
		return slNo;
	}
	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	
	public String getQuantityString() {
		return numberFormat(Double.toString(quantity)).toString();
	}
	  
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public Integer getMbExtraItemSlNo() {
		return mbExtraItemSlNo;
	}
	public void setMbExtraItemSlNo(Integer mbExtraItemSlNo) {
		this.mbExtraItemSlNo = mbExtraItemSlNo;
	}
	public Long getConsumedNo() {
		return consumedNo;
	}
	public void setConsumedNo(Long consumedNo) {
		this.consumedNo = consumedNo;
	}
	public double getConsumedLength() {
		return consumedLength;
	}
	public void setConsumedLength(double consumedLength) {
		this.consumedLength = consumedLength;
	}
	public double getConsumedWidth() {
		return consumedWidth;
	}
	public void setConsumedWidth(double consumedWidth) {
		this.consumedWidth = consumedWidth;
	}
	public double getConsumedDH() {
		return consumedDH;
	}
	public void setConsumedDH(double consumedDH) {
		this.consumedDH = consumedDH;
	}
	public double getConsumedQuantity() {
		return consumedQuantity;
	}
	public void setConsumedQuantity(double consumedQuantity) {
		this.consumedQuantity = consumedQuantity;
	}
	
	public static StringBuffer numberFormat(final String strNumberToConvert) {
        String strNumber="",signBit="";
        if(strNumberToConvert.startsWith("-")) {
            strNumber=""+strNumberToConvert.substring(1,strNumberToConvert.length());
            signBit="-";
        }
        else strNumber=""+strNumberToConvert;
        DecimalFormat dft = new DecimalFormat("##############0.#####");
        String strtemp=""+dft.format(Double.parseDouble(strNumber));
        StringBuffer strbNumber=new StringBuffer(strtemp);
       if(signBit.equals("-"))strbNumber=strbNumber.insert(0,"-");
        return strbNumber;
    }
    
}
