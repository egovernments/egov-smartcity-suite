package org.egov.tender.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.egov.commons.ContractorGrade;
import org.egov.commons.EgwStatus;

public class TenderUnit implements java.io.Serializable{
	private Long id;
	private BigDecimal emd;
	private BigDecimal formCost;
	private EgwStatus status;
	private TenderNotice tenderNotice;
	private String timeLimit;
	private ContractorGrade classOfContractor;
	private Date dateofSale;
	private Date enddateofSale; 
	private String submissionTime;
	private String openTime;
	private Date dateofSubmission;
	private Date bidMeetingDate;
	private Date dateOfOpeningOfEtender;
	private Date modifiedDate;
	private String tenderGroupRefNumber;
	private BigDecimal estimatedCost;
	private String tenderGroupNarration;
	private String supplierGrade;
	private List<ContractorGrade> contractorGradeList=new ArrayList<ContractorGrade>();
	
	
	


	public List<ContractorGrade> getContractorGradeList() {
		return contractorGradeList;
	}

	public void setContractorGradeList(List<ContractorGrade> contractorGradeList) {
		this.contractorGradeList = contractorGradeList;
	}

	private Set<TenderableEntityGroup> tenderableGroups = new HashSet<TenderableEntityGroup>(0);
	private Set<TenderableEntity> tenderEntities= new TreeSet<TenderableEntity>(new TenderableEntityComparator());
	private Set<GenericTenderResponse> tenderResponses= new HashSet<GenericTenderResponse>(0);
	
	private String tenderUnitNumber;
	
	public String getTenderUnitNumber()
	{
		StringBuffer tenderNoTemp=new StringBuffer();
		if(this.tenderableGroups.isEmpty())
			tenderNoTemp.append(this.getTenderNotice().getTenderFileRefNumber());

		else if(this.tenderEntities.isEmpty())
		{
			if(this.tenderableGroups.size()==1)
				tenderNoTemp.append(((TenderableEntityGroup)(this.tenderableGroups.toArray()[0])).getNumber());
			else
			{
				for(TenderableEntityGroup group:this.tenderableGroups)
					tenderNoTemp.append(group.getNumber()).append('/');
			}
		}

		else{
			if(this.tenderableGroups.size()==1){
				for(TenderableEntityGroup group : this.tenderableGroups){
					if(group.getEntities().isEmpty()){
						tenderNoTemp.append(this.getTenderNotice().getTenderFileRefNumber());
						break;
					}
					else
						tenderNoTemp.append(((TenderableEntityGroup)(this.tenderableGroups.toArray()[0])).getNumber());
				}
			}
			else
			{
				for(TenderableEntityGroup group:this.tenderableGroups){
					if(group.getEntities().isEmpty()){
						tenderNoTemp.append(this.getTenderNotice().getTenderFileRefNumber());
						break;
					}
					else
					{
						tenderNoTemp.append(group.getNumber()).append('/');
					}
				}
			}
		}
		tenderUnitNumber = tenderNoTemp.toString();
		return tenderUnitNumber;
	}

	public BigDecimal getEstimatedCost() {
		return estimatedCost==null?BigDecimal.ZERO:estimatedCost.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	public void setEstimatedCost(BigDecimal estimatedCost) {
		this.estimatedCost = estimatedCost;
	}
	//TODO: supplier grade is a dropdown.
	public String getSupplierGrade() {
		return supplierGrade;
	}
	public void setSupplierGrade(String supplierGrade) {
		this.supplierGrade = supplierGrade;
	}
	public String getTenderGroupNarration() {
		return tenderGroupNarration;
	}
	public void setTenderGroupNarration(String tenderGroupNarration) {
		this.tenderGroupNarration = tenderGroupNarration;
	}
	public String getTenderGroupRefNumber() {
		return tenderGroupRefNumber;
	}
	public void setTenderGroupRefNumber(String tenderGroupRefNumber) {
		this.tenderGroupRefNumber = tenderGroupRefNumber;
	}
	public Set<GenericTenderResponse> getTenderResponses() {
		return tenderResponses;
	}
	public void setTenderResponses(Set<GenericTenderResponse> tenderResponses) {
		this.tenderResponses = tenderResponses;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}
	public ContractorGrade getClassOfContractor() {
		return classOfContractor;
	}
	public void setClassOfContractor(ContractorGrade classOfContractor) {
		this.classOfContractor = classOfContractor;
	}
	public Date getDateofSale() {
		return dateofSale;
	}
	public void setDateofSale(Date dateofSale) {
		this.dateofSale = dateofSale;
	}
	public Date getDateofSubmission() {
		return dateofSubmission;
	}
	public void setDateofSubmission(Date dateofSubmission) {
		this.dateofSubmission = dateofSubmission;
	}
	public Date getBidMeetingDate() {
		return bidMeetingDate;
	}
	public void setBidMeetingDate(Date bidMeetingDate) {
		this.bidMeetingDate = bidMeetingDate;
	}
	public Date getDateOfOpeningOfEtender() {
		return dateOfOpeningOfEtender;
	}
	public void setDateOfOpeningOfEtender(Date dateOfOpeningOfEtender) {
		this.dateOfOpeningOfEtender = dateOfOpeningOfEtender;
	}
	public TenderNotice getTenderNotice() {
		return tenderNotice;
	}
	public void setTenderNotice(TenderNotice tenderNotice) {
		this.tenderNotice = tenderNotice;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BigDecimal getEmd() {
		return emd;
	}
	public void setEmd(BigDecimal emd) {
		this.emd = emd;
	}
	public BigDecimal getFormCost() {
		return formCost;
	}
	public void setFormCost(BigDecimal formCost) {
		this.formCost = formCost;
	}
	public EgwStatus getStatus() {
		return status;
	}
	public void setStatus(EgwStatus status) {
		this.status = status;
	}
	public Set<TenderableEntityGroup> getTenderableGroups() {
		return this.tenderableGroups;
	}
	public void setTenderableGroups(Set<TenderableEntityGroup> tenderableGroups) {
		this.tenderableGroups = tenderableGroups;
	}
	public void addTenderableGroups(TenderableEntityGroup tenderableGroup)
	{
		if(this.tenderableGroups == null)
			this.tenderableGroups=new HashSet<TenderableEntityGroup>(0);
		if(tenderableGroup!=null)
			this.tenderableGroups.add(tenderableGroup);

	}
	public void addTenderEntities(TenderableEntity tenderEntity)
	{
		if(tenderEntity!=null){
			getTenderEntities().add(tenderEntity);
			}
	}
	public Set<TenderableEntity> getTenderEntities() {
		return tenderEntities;
	}
	public void setTenderEntities(Set<TenderableEntity> tenderEntities) {
		this.tenderEntities = tenderEntities;
	}
	
	public Date getEnddateofSale() {
		return enddateofSale;
	}
	
	public void setEnddateofSale(Date enddateofSale) {
		this.enddateofSale = enddateofSale;
}
	
	public String getSubmissionTime() {
		return (submissionTime==null?"":submissionTime);
	}

	public void setSubmissionTime(String submissionTime) {
		this.submissionTime = submissionTime;
	}

	public String getOpenTime() {
	 return (openTime==null?"":openTime);
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}




	

}
