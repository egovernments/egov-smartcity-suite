package org.egov.works.models.qualityControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.egov.commons.Period;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;
import org.egov.works.models.masters.Rate;
import org.hibernate.validator.constraints.Length;
import javax.validation.Valid;
import org.joda.time.LocalDate;

public class TestMaster extends BaseModel{

	@Required(message="testMaster.testName.null") 
	@Length(max=256,message="testMaster.testName.length")
	private String testName;
	
	@Length(max=1024,message="testMaster.remarks.length")
	private String remarks;
	
	@Length(max=256,message="testMaster.unit.length")
	private String unit;
	private MaterialType materialType;
	
	@Valid
	private List<TestRate> testRates = new LinkedList<TestRate>();
	
	public void addRate(TestRate rat){
		this.testRates.add(rat);
	}
	
	public List<ValidationError> validate()	{
		List<ValidationError> errorList = null;
		removeEmptyRates();
		
		if((errorList = checkForNoRatePresent()) != null) {
			return errorList;
		}
		
		if((errorList = validateDateRanges())!=null) {
			return errorList;
		}

		if((errorList = validateRates())!=null) {
			return errorList;
		}
		return errorList;
	}

	private void removeEmptyRates() {
	List<TestRate> emptyRateObjs = new LinkedList<TestRate>();
	for(TestRate rat : testRates) {
		 if((rat.getRate() == null || rat.getRate().getValue() == 0.0) && (rat.getStartDate() == null && rat.getEndDate() == null)) {
			emptyRateObjs.add(rat);
		 }
	}
	testRates.removeAll(emptyRateObjs);
	}
	
	private List<ValidationError> checkForNoRatePresent() {
		if(testRates!= null && testRates.isEmpty()) {
			return Arrays.asList(new ValidationError("testRate","testMaster.rate.altleastone_Rate_needed"));
		}
		else {
			return null;
		}
	}	
	
	private List<ValidationError> validateDateRanges() {		
		List<Period> validDates = new ArrayList<Period>();	
		validDates.add(0, new Period(testRates.get(0).getStartDate(),testRates.get(0).getEndDate()));
		
		Date existingStartDate = null;
		Date existingEndDate = null;
		Date checkStartDate = null; 
		Date checkEndDate = null;
		Period existingPeriod = null;
		Period checkPeriod1 = null;
		boolean flag1 = true;
		int k=1;
		
		for(int i=1;i<testRates.size();i++)	{
			checkStartDate = ((TestRate)testRates.get(i)).getStartDate();
			checkEndDate = ((TestRate)testRates.get(i)).getEndDate();
			checkPeriod1 = new Period(checkStartDate, checkEndDate);
			
			for(int j=0;j<validDates.size();j++) {
				existingStartDate = validDates.get(j).getStartDate();
				existingPeriod = validDates.get(j);
				
				if(validDates.get(j).getEndDate()==null) {
					existingEndDate=null;
				}else{
					existingEndDate=validDates.get(j).getEndDate();
				}
				
				//check if the period to be checked is within any of the existing periods.
				if(isWithin(existingPeriod, checkStartDate) || isWithin(checkPeriod1, existingStartDate) || (checkEndDate!=null && isWithin(existingPeriod, checkEndDate)) || (existingEndDate!=null && isWithin(checkPeriod1, existingEndDate)))	{
					flag1 = false;
					break;
				}
				else if((checkEndDate!=null && existingEndDate!=null) && (isWithin(existingPeriod, checkEndDate) ||  isWithin(checkPeriod1, existingEndDate)))	{
					flag1 = false;
					break; 
				}
			}
		
		if(flag1) {		
			validDates.add(k++, checkPeriod1);
		}
		else {			
			return Arrays.asList(new ValidationError("dateoverlap","testMaster.rate.dates.overlap"));
		}			
	}		
	return null;
	}
	
	public boolean isWithin(Period period,Date dateTime){
		LocalDate start=new LocalDate(period.getStartDate()); 
		LocalDate end=null;
		if(period.getEndDate()!=null){
			end=new LocalDate(period.getEndDate());
		}
		LocalDate date=new LocalDate(dateTime);
		
		if(end==null) {
			return start.compareTo(date)<=0;
		}
		else {
			return start.compareTo(date)<=0 && end.compareTo(date)>=0;
		}
	}

	protected List<ValidationError> validateRates() {
		List<ValidationError> errorList = null;
		boolean openEndedRangeFlag = false;
		
		for (TestRate rate : testRates) {
			if(rate.getEndDate() == null && openEndedRangeFlag) {
				return Arrays.asList(new ValidationError("openendedrange","testMaster.rate.multiple.openendedrange"));
			}
			if(rate.getEndDate() == null)	{
				openEndedRangeFlag = true; 
			}
			
			errorList = rate.validate();
			
			if(errorList!=null)	{
				return errorList;
			}
		}		
		return errorList;
	}
	
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public MaterialType getMaterialType() {
		return materialType;
	}
	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}
	public List<TestRate> getTestRates() {
		return testRates;
	}
	public void setTestRates(List<TestRate> testRates) {
		this.testRates = testRates;
	}
}