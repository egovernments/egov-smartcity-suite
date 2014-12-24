package org.egov.works.models.qualityControl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.models.validator.CheckDateFormat;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.ValidateDate;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.model.PersonalInformation;
import org.hibernate.validator.constraints.Length;

public class SampleLetterHeader extends StateAware{
	
	private String sampleLetterNumber;
	private String coveringLetterNumber;
	private TestSheetHeader testSheetHeader;
	
	@Required(message = "sampleLetter.designation1.null")
	private DesignationMaster designation1;
	
	@Required(message = "sampleLetter.designation2.null")
	private DesignationMaster designation2;
	
	@Required(message = "sampleLetter.sampleCollectedBy1.null")
	private PersonalInformation sampleCollectedBy1;
	
	@Required(message = "sampleLetter.sampleCollectedBy2.null")
	private PersonalInformation sampleCollectedBy2;
	
	@CheckDateFormat(message="invalid.fieldvalue.dispatchDate") 
	private Date dispatchDate;
	
	@CheckDateFormat(message="invalid.fieldvalue.castingDate") 
	@ValidateDate(allowPast=true, dateFormat="dd/MM/yyyy",message="invalid.castingDate") 
	private Date castingDate;
	
	@Required(message = "sampleLetter.samplingDate.null")
	@CheckDateFormat(message="invalid.fieldvalue.samplingDate") 
	@ValidateDate(allowPast=true, dateFormat="dd/MM/yyyy",message="invalid.samplingDate") 
	private Date samplingDate;
	
	@Required(message = "sampleLetter.location.null")
	private String location;
	
	@Length(max=1024,message="sampleLetter.remarks.length")
	private String remarks;
	
	private EgwStatus egwStatus;
	private List<SampleLetterDetails> sampleLetterDetails = new LinkedList<SampleLetterDetails>();
	
	private transient String additionalWfRule;
	private transient BigDecimal amountWfRule;
	
	private Date sampleLetterDate;
	
	private double slTotalAmount;
	
	private List<String> sampleLetterActions = new ArrayList<String>();

	@Override
	public String getStateDetails() {
		// TODO Auto-generated method stub
		return "Sample Letter : " + getSampleLetterNumber() +"  Covering Letter : " + getCoveringLetterNumber();
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


	public TestSheetHeader getTestSheetHeader() {
		return testSheetHeader;
	}


	public void setTestSheetHeader(TestSheetHeader testSheetHeader) {
		this.testSheetHeader = testSheetHeader;
	}


	public DesignationMaster getDesignation1() {
		return designation1;
	}


	public void setDesignation1(DesignationMaster designation1) {
		this.designation1 = designation1;
	}


	public DesignationMaster getDesignation2() {
		return designation2;
	}


	public void setDesignation2(DesignationMaster designation2) {
		this.designation2 = designation2;
	}


	public PersonalInformation getSampleCollectedBy1() {
		return sampleCollectedBy1;
	}


	public void setSampleCollectedBy1(PersonalInformation sampleCollectedBy1) {
		this.sampleCollectedBy1 = sampleCollectedBy1;
	}


	public PersonalInformation getSampleCollectedBy2() {
		return sampleCollectedBy2;
	}


	public void setSampleCollectedBy2(PersonalInformation sampleCollectedBy2) {
		this.sampleCollectedBy2 = sampleCollectedBy2;
	}


	public Date getDispatchDate() {
		return dispatchDate;
	}


	public void setDispatchDate(Date dispatchDate) {
		this.dispatchDate = dispatchDate;
	}


	public Date getCastingDate() {
		return castingDate;
	}


	public void setCastingDate(Date castingDate) {
		this.castingDate = castingDate;
	}


	public Date getSamplingDate() {
		return samplingDate;
	}


	public void setSamplingDate(Date samplingDate) {
		this.samplingDate = samplingDate;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public EgwStatus getEgwStatus() {
		return egwStatus;
	}


	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}


	public List<SampleLetterDetails> getSampleLetterDetails() {
		return sampleLetterDetails;
	}


	public void setSampleLetterDetails(List<SampleLetterDetails> sampleLetterDetails) {
		this.sampleLetterDetails = sampleLetterDetails;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public void addSampleLetterDetails(SampleLetterDetails sldetails){
		this.sampleLetterDetails.add(sldetails);
	}

	public String getAdditionalWfRule() {
		return additionalWfRule;
	}

	public void setAdditionalWfRule(String additionalWfRule) {
		this.additionalWfRule = additionalWfRule;
	}

	public BigDecimal getAmountWfRule() {
		return amountWfRule;
	}

	public void setAmountWfRule(BigDecimal amountWfRule) {
		this.amountWfRule = amountWfRule;
	}

	public Date getSampleLetterDate() {
		return sampleLetterDate;
	}

	public void setSampleLetterDate(Date sampleLetterDate) {
		this.sampleLetterDate = sampleLetterDate;
	}

	public double getSlTotalAmount() {
		for(SampleLetterDetails sld : sampleLetterDetails){
			slTotalAmount=slTotalAmount+(sld.getSampleQuantity()*sld.getTestSheetDetails().getTestCharges().getValue());
		}
		return slTotalAmount; 
	}

	public List<String> getSampleLetterActions() {
		return sampleLetterActions;
	}

	public void setSampleLetterActions(List<String> sampleLetterActions) {
		this.sampleLetterActions = sampleLetterActions;
	}


}
