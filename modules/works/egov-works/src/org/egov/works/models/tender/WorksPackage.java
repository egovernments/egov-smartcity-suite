package org.egov.works.models.tender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.Money;
import org.egov.infstr.models.validator.OptionalPattern;
import org.egov.infstr.models.validator.ValidateDate;
import org.egov.infstr.models.validator.constants.ValidatorConstants;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.rjbac.dept.Department;
import org.egov.pims.model.PersonalInformation;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.estimate.OverheadValue;
import org.egov.works.models.workflow.WorkFlow;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class WorksPackage extends WorkFlow{
	public enum WorkPacakgeStatus{
		CREATED,CHECKED,APPROVED,REJECTED,CANCELLED,RESUBMITTED
	}
	public enum Actions{
		SUBMIT_FOR_APPROVAL,APPROVE,REJECT,CANCEL;

		public String toString() {
			return this.name().toLowerCase();
		}
	}

	@NotEmpty(message="wp.name.is.null")
	private String name;
	private String description;
	@NotNull(message="wp.userDepartment.is.null")
	private Department userDepartment;
	@NotNull(message="wp.preparedBy.is.null")
	private PersonalInformation preparedBy; 
	@NotNull(message="wp.packageDate.is.null")
	@ValidateDate(allowPast=true, dateFormat="dd/MM/yyyy",message="invalid.packagedate")
	private Date packageDate;
	@NotEmpty(message="wp.wpNumber.is.null")
	private String wpNumber;
	private String employeeName;
	private Money workValueIncludingTaxes;
	private List<WorksPackageDetails> worksPackageDetails = new LinkedList<WorksPackageDetails>();
	
	@NotEmpty(message="wp.tenderFileNumber.is.null")
	@Length(max=50,message="wp.tenderFileNumber.length")
	@OptionalPattern(regex=ValidatorConstants.caseNumberRegx,message="wp.tenderFileNumber.alphaNumeric")
	private String tenderFileNumber;
	private Long documentNumber;
	
	public Long getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Department getUserDepartment() {
		return userDepartment;
	}
	public void setUserDepartment(Department userDepartment) {
		this.userDepartment = userDepartment;
	}
	public PersonalInformation getPreparedBy() {
		return preparedBy;
	}
	public void setPreparedBy(PersonalInformation preparedBy) {
		this.preparedBy = preparedBy;
	}
	public Date getPackageDate() {
		return packageDate;
	}
	public void setPackageDate(Date packageDate) {
		this.packageDate = packageDate;
	}
	public String getWpNumber() {
		return wpNumber;
	}
	public void setWpNumber(String wpNumber) {
		this.wpNumber = wpNumber;
	}
	
	public void addEstimates(WorksPackageDetails wpDetailsObj)
	{
		this.worksPackageDetails.add(wpDetailsObj);
	}
		
	public List<ValidationError> validate() {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		if(worksPackageDetails.isEmpty())
		{
			errors.add(new ValidationError("estimates",
			"estimates.null"));
		}
		return errors;
	}
	public List<WorksPackageDetails> getWorksPackageDetails() {
		return worksPackageDetails;
	}
	public void setWorksPackageDetails(List<WorksPackageDetails> worksPackageDetails) {
		this.worksPackageDetails = worksPackageDetails;
	}
	
	public String getStateDetails() {
		return "WorksPackage : " + getWpNumber();
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Money getWorkValueIncludingTaxes() {
		double amt=0;
		if(!worksPackageDetails.isEmpty()){
			for(WorksPackageDetails wpd:worksPackageDetails) {
				amt+= wpd.getEstimate().getWorkValueIncludingTaxes().getValue();
			}
		}
		workValueIncludingTaxes=new Money(amt);
		return workValueIncludingTaxes;
	}
	public void setWorkValueIncludingTaxes(Money workValueIncludingTaxes) {
		this.workValueIncludingTaxes = workValueIncludingTaxes;
	}
	
	public Collection<EstimateLineItemsForWP> getActivitiesForEstimate()
	{
		Map<Long,EstimateLineItemsForWP> resultMap = new HashMap<Long, EstimateLineItemsForWP>();
		for(Activity act:getAllActivities())
		{			
			EstimateLineItemsForWP estlineItem = new EstimateLineItemsForWP();
			if(act.getSchedule()!=null){
				if(resultMap.containsKey(act.getSchedule().getId())){
					EstimateLineItemsForWP preEstlineItem = resultMap.get(act.getSchedule().getId());
					preEstlineItem.setQuantity(act.getQuantity() + preEstlineItem.getQuantity());
						if(DateUtils.compareDates(act.getAbstractEstimate().getEstimateDate(),
				  			preEstlineItem.getEstimateDate())){
				  		preEstlineItem.setRate(act.getSORCurrentRate().getValue());
				  		preEstlineItem.setAmt(preEstlineItem.getQuantity()*act.getRate().getValue());
				  		preEstlineItem.setActivity(act);
				  		if(act.getSchedule().hasValidMarketRateFor(act.getAbstractEstimate().getEstimateDate())){
				  			preEstlineItem.setMarketRate(preEstlineItem.getQuantity()*act.getSORCurrentMarketRate().getValue());
				  		}
				  		else{ 
				  			preEstlineItem.setMarketRate(act.getAmount().getValue());
				  		} 
			  		}
					else{
							preEstlineItem.setRate(preEstlineItem.getRate());
					  		preEstlineItem.setAmt(preEstlineItem.getQuantity()*preEstlineItem.getRate());
					  		preEstlineItem.setActivity(act);
					  		if(act.getSchedule().hasValidMarketRateFor(act.getAbstractEstimate().getEstimateDate())){
					  			preEstlineItem.setMarketRate(preEstlineItem.getQuantity()*act.getSORCurrentMarketRate().getValue());
					  		}
					  		else{ 
					  			preEstlineItem.setMarketRate(act.getAmount().getValue()); 
					  		} 
					}
				  	resultMap.put(act.getSchedule().getId(), preEstlineItem);
				}
				else{
					addEstLineItem(act, estlineItem);
					resultMap.put(act.getSchedule().getId(), estlineItem); 
				}
			}
			if(act.getNonSor()!=null)
			{
				addEstLineItem(act, estlineItem);
				resultMap.put(act.getNonSor().getId(), estlineItem);
			}
		}
		return getEstLineItemsWithSrlNo(resultMap.values());
	}

	private void addEstLineItem(Activity act,EstimateLineItemsForWP estlineItem) {
		if(act.getSchedule()==null){
			estlineItem.setCode("");
			estlineItem.setSummary("");
			estlineItem.setDescription(act.getNonSor().getDescription());
			estlineItem.setRate(act.getRate().getValue());
			estlineItem.setMarketRate(act.getAmount().getValue());
		}
		else{
			estlineItem.setCode(act.getSchedule().getCode());
			estlineItem.setDescription(act.getSchedule().getDescription());
			estlineItem.setRate(act.getSORCurrentRate().getValue());
			if(act.getSchedule().hasValidMarketRateFor(act.getAbstractEstimate().getEstimateDate())){
				estlineItem.setMarketRate(act.getQuantity()*act.getSORCurrentMarketRate().getValue());
			}
			else{
				estlineItem.setMarketRate(act.getAmount().getValue()); 
			}
			estlineItem.setSummary(act.getSchedule().getSummary());
		}
		estlineItem.setActivity(act);
		estlineItem.setAmt(act.getQuantity()*act.getRate().getValue());		
		estlineItem.setEstimateDate(act.getAbstractEstimate().getEstimateDate()); 
		estlineItem.setQuantity(act.getQuantity());
		estlineItem.setUom(act.getUom().getUom());
		estlineItem.setConversionFactor(act.getConversionFactor());
	}
	
	
	public List<Activity> getAllActivities()
	{		
		List<Activity> actList = new ArrayList<Activity>();
		for(AbstractEstimate ab:getAllEstimates())
			actList.addAll(ab.getActivities());
		return actList;
	}
	
	public List<Activity> getSorActivities()
	{		
		List<Activity> actList = Collections.EMPTY_LIST;
		for(Activity act:getAllActivities()){
			if(act.getSchedule()!=null)
				actList.add(act);
		}
		return actList;
	}
	
	public List<Activity> getNonSorActivities()
	{		
		List<Activity> actList = Collections.EMPTY_LIST;
		for(Activity act:getAllActivities()){
			if(act.getNonSor()!=null)
				actList.add(act);
		}
		return actList;
	}
	
	public double getTotalAmount()
	{
		double totalAmt=0;
		for(EstimateLineItemsForWP act:getActivitiesForEstimate()){
		   totalAmt+=act.getAmt();
		}
		return totalAmt;
	}
	
	public double getMarketRateTotalAmount()
	{
		double totalAmt=0;
		for(EstimateLineItemsForWP act:getActivitiesForEstimate())
		   totalAmt+=act.getMarketRate();
		return totalAmt;
	}
	
	private Collection<EstimateLineItemsForWP> getEstLineItemsWithSrlNo(Collection<EstimateLineItemsForWP> actList)
	{
		int i=1;
		Collection<EstimateLineItemsForWP> latestEstLineItemList = new ArrayList<EstimateLineItemsForWP>();
		for(EstimateLineItemsForWP act:actList){
		   act.setSrlNo(i);
		   latestEstLineItemList.add(act);
			i++;
		}
		return latestEstLineItemList;
	}
	
	public List<AbstractEstimate> getAllEstimates() {
		List<AbstractEstimate> abList = new ArrayList<AbstractEstimate>();
		if(this!=null && !this.getWorksPackageDetails().isEmpty())
		{
			for(WorksPackageDetails wpd:this.getWorksPackageDetails())
				abList.add(wpd.getEstimate());
		}
		return abList;
	}
	public String getTenderFileNumber() {
		return tenderFileNumber;
	}
	public void setTenderFileNumber(String tenderFileNumber) {
		this.tenderFileNumber = tenderFileNumber;
	}
	public String getPackageNumberWithoutWP() {
		if(StringUtils.isNotBlank(this.wpNumber)){
			String number[] =  this.wpNumber.split("/");
			return number.length==0?"0":number[2]+"/"+number[3];
		}
		return "0";
	}
}
