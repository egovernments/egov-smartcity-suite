package org.egov.works.models.measurementbook;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Money;
import org.egov.infstr.models.validator.GreaterThan;
import org.egov.infstr.models.validator.OptionalPattern;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.constants.ValidatorConstants;
import org.egov.works.models.revisionEstimate.RevisionType;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.hibernate.validator.constraints.Length;


public class MBDetails extends BaseModel {
	
	@Required(message = "mbdetails.mbheader.null")
	private MBHeader mbHeader;
	@Required(message = "mbdetails.activity.null")
	private WorkOrderActivity workOrderActivity;
	@GreaterThan(value=0,message="mbdetails.quantity.non.negative")
	private double quantity;
	@Length(max = 400, message = "mbdetails.remark.length")
	private String remark;
	
	//------------------------Fields for calculations---------------------
	private double prevCumlvQuantity;
	private double currCumlvQuantity;
	private double amtForCurrQuantity;
	private double cumlvAmtForCurrCumlvQuantity;
	private Date mbdetailsDate;
	@OptionalPattern(regex=ValidatorConstants.alphaNumeric,message="mbdetails.ordernumber")
	private String OrderNumber;
	//-------------------------------------------------------------------
	
	// --- for RE
	private double totalEstQuantity;
	
	private List<MBMeasurementSheet> mbMeasurementSheetList = new LinkedList<MBMeasurementSheet>();
	
	private double partRate;
	private double reducedRate;
	
	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if (mbHeader != null
				&& (mbHeader.getId() == null || mbHeader.getId() == 0 || mbHeader.getId() == -1)) {
			validationErrors.add(new ValidationError("mbHeader", "mbdetails.mbheader.null"));
		} 
		if (workOrderActivity != null
				&& (workOrderActivity.getId() == null
						|| workOrderActivity.getId() == 0 || workOrderActivity.getId() == -1)) {
			validationErrors.add(new ValidationError("workOrderActivity", "mbdetails.activity.null"));
		} 
		return validationErrors;
	}
	
	public void setMbHeader(MBHeader mbHeader) {
		this.mbHeader = mbHeader;
	}
	
	public MBHeader getMbHeader() {
		return mbHeader;
	}
	
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getQuantity() {
		return quantity;
	}

	public WorkOrderActivity getWorkOrderActivity() {
		return workOrderActivity;
	}

	public void setWorkOrderActivity(WorkOrderActivity workOrderActivity) {
		this.workOrderActivity = workOrderActivity;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * Get Cumulative quantity upto pervious entry 
	 */
	public double getPrevCumlvQuantity() {
		return prevCumlvQuantity;
	}
	
	public void setPrevCumlvQuantity(double prevCumlvQuantity) {
		this.prevCumlvQuantity = prevCumlvQuantity;
	}

	/**
	 * Get Cumulative quantity including current entry
	 */
	public double getCurrCumlvQuantity() {
		return currCumlvQuantity;
	}

	public void setCurrCumlvQuantity(double currCumlvQuantity) {
		this.currCumlvQuantity = currCumlvQuantity;
	}

	/**
	 * Get Amount for current entry 
	 */
	public double getAmtForCurrQuantity() {
		return amtForCurrQuantity;
	}

	public void setAmtForCurrQuantity(double amtForCurrQuantity) {
		this.amtForCurrQuantity = amtForCurrQuantity;
	}

	/**
	 * Get Cumulative amount including current entry
	 */
	public double getCumlvAmtForCurrCumlvQuantity() {
		return cumlvAmtForCurrCumlvQuantity;
	}

	public void setCumlvAmtForCurrCumlvQuantity(double cumlvAmtForCurrCumlvQuantity) {
		this.cumlvAmtForCurrCumlvQuantity = cumlvAmtForCurrCumlvQuantity;
	}

	public Date getMbdetailsDate() {
		return mbdetailsDate;
	}

	public void setMbdetailsDate(Date mbdetailsDate) {
		this.mbdetailsDate = mbdetailsDate;
	}

	public String getOrderNumber() {
		return OrderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		OrderNumber = orderNumber;
	}

	public List<MBMeasurementSheet> getMbMeasurementSheetList() {
		return mbMeasurementSheetList;
}

	public void setMbMeasurementSheetList(
			List<MBMeasurementSheet> mbMeasurementSheetList) {
		this.mbMeasurementSheetList = mbMeasurementSheetList;
	}
	
	public void addMbMeasurementSheet(MBMeasurementSheet mBMeasurementSheet) {
		 this.mbMeasurementSheetList.add(mBMeasurementSheet);
	}
	
	public Money getAmount() {
		double amt = 0;
		if((workOrderActivity.getActivity().getParent()==null) || (workOrderActivity.getActivity().getRevisionType()!=null && workOrderActivity.getActivity().getRevisionType().equals(RevisionType.EXTRA_ITEM))){ 
			if(getReducedRate()==0){
			amt = workOrderActivity.getApprovedRate() * quantity * workOrderActivity.getActivity().getConversionFactor();
			}
			else if(getReducedRate()>0){
				amt = getReducedRate() * quantity * workOrderActivity.getActivity().getConversionFactor();
			}
		}
		else{
			if( getReducedRate()==0){
			amt = workOrderActivity.getApprovedRate() * quantity;
			}
			else if(getReducedRate()>0){
				amt = getReducedRate() * quantity;
			}
		}
		return new Money(amt);
		
	}

	public double getTotalEstQuantity() {
		return totalEstQuantity;
	}

	public void setTotalEstQuantity(double totalEstQuantity) {
		this.totalEstQuantity = totalEstQuantity;
	}

	public double getPartRate() {
		return partRate;
	}

	public void setPartRate(double partRate) {
		this.partRate = partRate;
	}

	public double getReducedRate() {
		return reducedRate;
	}

	public void setReducedRate(double reducedRate) {
		this.reducedRate = reducedRate;
	}
}
