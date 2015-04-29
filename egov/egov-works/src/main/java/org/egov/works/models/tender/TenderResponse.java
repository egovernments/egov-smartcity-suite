package org.egov.works.models.tender;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.validation.regex.Constants;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.Money;
import org.egov.infstr.utils.DateUtils;
import org.egov.pims.model.PersonalInformation;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.workflow.WorkFlow;
import org.hibernate.validator.constraints.Length;

public class TenderResponse extends WorkFlow{
	public enum TenderResponseStatus{
		CREATED,APPROVED,REJECTED,CANCELLED
	}
	public enum Actions{
		SAVE,SUBMIT_FOR_APPROVAL,REJECT,CANCEL,approval;

		public String toString() {
			return this.name().toLowerCase();
		}
	}	
	
	@Valid
	private TenderEstimate tenderEstimate;
	
	@Length(max=10,message="tenderResponse.rank.length")
	@OptionalPattern(regex=Constants.ALPHANUMERIC_WITHSPACE,message="tenderResponse.rank.alphaNumeric")
	private String rank;
	 
	//@Required(message="tenderResponse.type.null")
	@Length(max=25,message="tenderResponse.type.length")
	@OptionalPattern(regex=Constants.ALPHANUMERIC_WITHSPACE,message="tenderResponse.type.alphaNumeric")
	private String type;
	
	//@Required(message="tenderResponse.percQuotedRate.null")	
	private double percQuotedRate; 
	
	//@Required(message="tenderResponse.percNegotiatedRate.null")
	private double percNegotiatedAmountRate;
	
	@Required(message="tenderResponse.negotiationDate.null")
	@DateFormat(message="invalid.fieldvalue.negotiationDate") 
	private Date negotiationDate;
	
	private String negotiationNumber;
	
	private String narration;
	
	private String status;
	
	private double totalAmount;
	
	private Collection<EstimateLineItemsForWP> activitiesForWorkorder;
	
	private double workOrderAmount;
	
	@Valid
	private List<TenderResponseActivity> tenderResponseActivities = new LinkedList<TenderResponseActivity>();
	
	@Valid
	private List<TenderResponseContractors> tenderResponseContractors = new LinkedList<TenderResponseContractors>();
		
	//added by prashanth on jan 9th 2010
	//@Required(message = "tenderResponse.negotiationPreparedBy.null")
	private PersonalInformation negotiationPreparedBy;
	
	private Integer approverUserId;
	
	private Long documentNumber;
	
	@Length(max = 50, message = "negotiation.resolutionNumber.length")
	private String resolutionNumber;
	
	@DateFormat(message="invalid.fieldvalue.resolutionDate")
	private Date resolutionDate;
	
	private List<String> tenderNegotiationsActions = new ArrayList<String>();
	
	private List<WorksPackageDetails> worksPackageDetails = new LinkedList<WorksPackageDetails>();
	
	private Set<SetStatus> setStatuses = new HashSet<SetStatus>();
	
	private EgwStatus egwStatus;
	private String formattedTotalAmount;
	
	private double tenderNegotiatedValue;
	
	public PersonalInformation getNegotiationPreparedBy(){
		return negotiationPreparedBy;
	}

	public void setNegotiationPreparedBy(PersonalInformation negotiationPreparedBy) {
		this.negotiationPreparedBy = negotiationPreparedBy;
	}
		
	public TenderEstimate getTenderEstimate() {
		return tenderEstimate;
	}

	public void setTenderEstimate(TenderEstimate tenderEstimate) {
		this.tenderEstimate = tenderEstimate;
	}

		public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getPercQuotedRate() {
		return percQuotedRate;
	}
	

	public String getFormattedPercQuotedRate() {
		NumberFormat nf=NumberFormat.getInstance();
		nf.setMaximumFractionDigits(10);
		return nf.format(percQuotedRate); 
	}
	
	public String getFormattedPercNegotiatedAmountRate() {
		NumberFormat nf=NumberFormat.getInstance();
		nf.setMaximumFractionDigits(10);
		return nf.format(percNegotiatedAmountRate); 
	}


	public void setPercQuotedRate(double percQuotedRate) {
		this.percQuotedRate = percQuotedRate; 
	}
	
	

	public double getPercNegotiatedAmountRate() {
		return percNegotiatedAmountRate; 
	}

	public void setPercNegotiatedAmountRate(double percNegotiatedAmountRate) {
		this.percNegotiatedAmountRate = percNegotiatedAmountRate;
	}

	public Date getNegotiationDate() {
		return negotiationDate;
	}

	public void setNegotiationDate(Date negotiationDate) {
		this.negotiationDate = negotiationDate;
	}

	public List<TenderResponseActivity> getTenderResponseActivities() {
		return tenderResponseActivities;
	}

	public void setTenderResponseActivities(
			List<TenderResponseActivity> tenderResponseActivities) {
		this.tenderResponseActivities = tenderResponseActivities;
	}
	
	public void addTenderResponseActivity(TenderResponseActivity tenderResponseActivity) {
		this.tenderResponseActivities.add(tenderResponseActivity);
	}

	public String getNegotiationNumber() {
		return negotiationNumber;
	}

	public void setNegotiationNumber(String negotiationNumber) {
		this.negotiationNumber = negotiationNumber;
	}
	
	public double getTotalAmount()
	{
		return totalAmount;
	}
	
	public List<ValidationError> validate()	{
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if(!tenderResponseContractors.isEmpty() && (tenderResponseContractors.get(0)!=null && tenderResponseContractors.get(0).getContractor()!=null
				&& tenderResponseContractors.get(0).getContractor().getId()==null || tenderResponseContractors.get(0).getContractor().getId()==0 
				|| tenderResponseContractors.get(0).getContractor().getId()==-1)){
			validationErrors.add(new ValidationError("contractor", "tenderResponse.contractor.null"));
		}
		else if(tenderResponseContractors==null) {
			validationErrors.add(new ValidationError("contractor", "tenderResponse.contractor.null"));
		}
		return validationErrors;
	}

	@Override
	public String getStateDetails() {
		return "Tender Negotiation : " + getNegotiationNumber();
	}
		
	/**
	 * @return the narration
	 */
	public String getNarration() {
		return narration;
	}

	/**
	 * @param narration the narration to set
	 */
	public void setNarration(String narration) {
		this.narration = narration;
	}

	public Integer getApproverUserId() {
		return approverUserId;
	}

	public void setApproverUserId(Integer approverUserId) {
		this.approverUserId = approverUserId;
	}

	public Long getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Collection<EstimateLineItemsForWP> getActivitiesForWorkorder() {
		return activitiesForWorkorder;
	}

	public void setActivitiesForWorkorder(
			Collection<EstimateLineItemsForWP> activitiesForWorkorder) {
		this.activitiesForWorkorder = activitiesForWorkorder;
	}

	public double getWorkOrderAmount() {
		return workOrderAmount;
	}

	public void setWorkOrderAmount(double workOrderAmount) {
		this.workOrderAmount = workOrderAmount;
	}

	public List<String> getTenderNegotiationsActions() {
		return tenderNegotiationsActions;
	}

	public void setTenderNegotiationsActions(List<String> tenderNegotiationsActions) {
		this.tenderNegotiationsActions = tenderNegotiationsActions;
	}

	public String getResolutionNumber() {
		return resolutionNumber;
	}

	public void setResolutionNumber(String resolutionNumber) {
		this.resolutionNumber = resolutionNumber;
	}

	public Date getResolutionDate() {
		return resolutionDate;
	}

	public void setResolutionDate(Date resolutionDate) {
		this.resolutionDate = resolutionDate;
	}
	
	public List<TenderResponseContractors> getTenderResponseContractors() {
		return tenderResponseContractors;
	}

	public void setTenderResponseContractors(
			List<TenderResponseContractors> tenderResponseContractors) {
		this.tenderResponseContractors = tenderResponseContractors;
	}
	
	public void addTenderResponseContractors(TenderResponseContractors tenderResponseContractors) {
		this.tenderResponseContractors.add(tenderResponseContractors);
	}
		
	public Money getTotalNegotiatedQuantity() {
		Money totalNegotiatedQuantity;
		double qty=0;
		for (TenderResponseActivity tra : tenderResponseActivities) {
			qty+=tra.getNegotiatedQuantity();
		}
		totalNegotiatedQuantity = new Money(qty);
		return totalNegotiatedQuantity;
	}
	
	public Collection<EstimateLineItemsForTR> getNegotiationDetails() 
	{
		Map<Long,EstimateLineItemsForTR> resultMap = new HashMap<Long, EstimateLineItemsForTR>();
		for(TenderResponseActivity tra:getTenderResponseActivities())
		{			
			EstimateLineItemsForTR estlineItem = new EstimateLineItemsForTR();
			if(tra.getActivity().getSchedule()!=null){
				if(resultMap.containsKey(tra.getActivity().getSchedule().getId())){
					EstimateLineItemsForTR preEstlineItem = resultMap.get(tra.getActivity().getSchedule().getId());
					preEstlineItem.setQuantity(tra.getActivity().getQuantity() + preEstlineItem.getQuantity());
						if(DateUtils.compareDates(tra.getActivity().getAbstractEstimate().getEstimateDate(),
				  			preEstlineItem.getEstimateDate())){
				  		preEstlineItem.setRate(tra.getActivity().getSORCurrentRate().getValue());
				  		preEstlineItem.setAmt(preEstlineItem.getQuantity()*tra.getActivity().getRate().getValue());
				  		preEstlineItem.setActivity(tra.getActivity());
				  		if(tra.getActivity().getSchedule().hasValidMarketRateFor(tra.getActivity().getAbstractEstimate().getEstimateDate())){
				  			preEstlineItem.setMarketRate(preEstlineItem.getQuantity()*tra.getActivity().getSORCurrentMarketRate().getValue());
				  		}
				  		else {
				  			preEstlineItem.setMarketRate(tra.getActivity().getAmount().getValue());
				  		}
			  		}
						preEstlineItem.setTenderResponseQuotes(tra.getTenderResponseQuotes());
						preEstlineItem.setNegotiatedRate(tra.getNegotiatedRate());
						resultMap.put(tra.getActivity().getSchedule().getId(), preEstlineItem);  
				}
				else{
					addEstLineItem(tra.getActivity(), estlineItem);
					estlineItem.setNegotiatedRate(tra.getNegotiatedRate());
					estlineItem.setTenderResponseQuotes(tra.getTenderResponseQuotes());
					resultMap.put(tra.getActivity().getSchedule().getId(), estlineItem); 
				}
			}
			if(tra.getActivity().getNonSor()!=null)
			{
				addEstLineItem(tra.getActivity(), estlineItem);
				estlineItem.setNegotiatedRate(tra.getNegotiatedRate());
				estlineItem.setTenderResponseQuotes(tra.getTenderResponseQuotes());
				resultMap.put(tra.getActivity().getNonSor().getId(), estlineItem); 
			}
		}
		return getEstLineItemsWithSrlNo(resultMap.values());
	}
	
	public List<WorksPackageDetails> getWorksPackageDetails() { 
		return worksPackageDetails;
	}

	public void setWorksPackageDetails(List<WorksPackageDetails> worksPackageDetails) {
		this.worksPackageDetails = worksPackageDetails;
	}
	
	private void addEstLineItem(Activity act,EstimateLineItemsForTR estlineItem) {
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

	
	private Collection<EstimateLineItemsForTR> getEstLineItemsWithSrlNo(Collection<EstimateLineItemsForTR> actList)
	{
		int i=1;
		Collection<EstimateLineItemsForTR> latestEstLineItemList = new ArrayList<EstimateLineItemsForTR>();
		for(EstimateLineItemsForTR act:actList){ 
		   act.setSrlNo(i);
		   latestEstLineItemList.add(act);
			i++;
		}
		return latestEstLineItemList;
	}

	public Set<SetStatus> getSetStatuses() {
		return setStatuses;
	}

	public void setSetStatuses(Set<SetStatus> setStatuses) {
		this.setStatuses = setStatuses;
	}

	public EgwStatus getEgwStatus() {
		return egwStatus;
	}

	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}

	public String getFormattedTotalAmount() {
		return formattedTotalAmount;
	}

	public void setFormattedTotalAmount(String formattedTotalAmount) {
		this.formattedTotalAmount = formattedTotalAmount;
	}

	public double getTenderNegotiatedValue() {
		return tenderNegotiatedValue;
	}

	public void setTenderNegotiatedValue(double tenderNegotiatedValue) {
		this.tenderNegotiatedValue = tenderNegotiatedValue;
	}

}
