/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.models.estimate;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringEscapeUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Fundsource;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.Money;
import org.egov.infstr.utils.StringUtils;
import org.egov.pims.model.PersonalInformation;
import org.egov.works.models.masters.DepositCode;
import org.egov.works.models.revisionEstimate.RevisionType;
import org.hibernate.validator.constraints.Length;


public class AbstractEstimate extends StateAware{
	public enum EstimateStatus{
		CREATED,TECH_SANCTION_CHECKED,TECH_SANCTIONED,BUDGETARY_APPR_CHECKED,BUDGETARY_APPROPRIATION_DONE, 
		ADMIN_CHECKED,ADMIN_SANCTIONED, REJECTED,CANCELLED,COMP_CERTIFICATE, APPROVED
	}
	public enum Actions{
		SUBMIT_FOR_APPROVAL,TECH_SANCTION,BUDGET_DETAILS_SAVE,BUDGET_APPROPRIATION,ADMIN_SANCTION,REJECT,CANCEL;

		public String toString() {
			return this.name().toLowerCase();
		}
	}
	private Boundary ward;
	
	private String name;
	 	 
	private Date estimateDate;
	
	private String description;
	
	private String location;
	
	private WorkType type;
	
	private EgwTypeOfWork category;
	private EgwTypeOfWork parentCategory;
	
	private Department userDepartment;

	private Department executingDepartment;
	
	private PersonalInformation estimatePreparedBy;
	
	private BigDecimal budgetAvailable = new BigDecimal("0.0");
	
	@Valid
	private List<OverheadValue> overheadValues = new LinkedList<OverheadValue>();
	
	@Valid
	private List<AssetsForEstimate> assetValues = new LinkedList<AssetsForEstimate>();
	
	@Valid
	private List<Activity> activities = new LinkedList<Activity>();
	@Valid
	private List<MultiYearEstimate> multiYearEstimates = new LinkedList<MultiYearEstimate>();
	
	private Set<AbstractEstimateAppropriation> abstractEstimateAppropriations = new HashSet<AbstractEstimateAppropriation>();

	private Money workValue;
	private String estimateNumber;
	private Long documentNumber;
	private List<FinancialDetail> financialDetails = new LinkedList<FinancialDetail>();	
	private ProjectCode projectCode;
	private String budgetApprNo;
	private String document;
	//private 
	private String positionAndUserName;
	@Length(max = 50, message = "estimate.resolutionNumber.length")
	private String resolutionNumber;
	
	@DateFormat(message="invalid.fieldvalue.resolutionDate")
	private Date resolutionDate;
	
	private Fundsource fundSource;
	
	private Integer approverUserId;
	private DepositCode depositCode;
	private String budgetRejectionNo;
	private EgwStatus egwStatus;
	//for changing boundary and department
	private Department oldExecDepartment;
	private Department oldUserDepartment;
	private String oldEstimateNumber;
	private Boundary oldWard;
	private AbstractEstimate parent;
	private Money totalAmount;
	private Date approvedDate;
	private String isCopiedEst="N";
	private List<EstimatePhoto> photoList = new ArrayList<EstimatePhoto>();
	private BigDecimal lat;
	private BigDecimal lon;
	//This is added to store old user departments of estimates whose user dept needed to be changed to regional departments from backend  
	private Department previousUserDepartment;
	private Date budgetApprDate;
	
	public EgwStatus getEgwStatus() {
		return egwStatus;
	}

	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}

	@Required(message="estimate.name.null")
	@Length(max=1024,message="estimate.name.length")   
	public String getName() { 
		return name;
	}

	public String getNameJS() { 
		return StringUtils.escapeJavaScript(name);
	}

	public String getNameJson() {
		return StringUtils.escape(name);
	}
	
	public void setName(String name) {
		this.name = StringEscapeUtils.unescapeHtml(name);
	}

	@Required(message="estimate.date.null")
	@DateFormat(message="invalid.fieldvalue.estimateDate") 
	public Date getEstimateDate() {
		return estimateDate;
	}

	public void setEstimateDate(Date estimateDate) {
		this.estimateDate = estimateDate;
	}

	@Required(message="estimate.desc.null")
	@Length(max=1024,message="estimate.desc.length")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Length(max=250,message="estimate.loc.length")
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	@Required(message="estimate.natureofwork.null")
	public WorkType getType() {
		return type;
	}

	public void setType(WorkType type) {
		this.type = type;
	}

	public EgwTypeOfWork getCategory() {
		return category;
	}

	public void setCategory(EgwTypeOfWork category) {
		this.category = category;
	}

	@Required(message="estimate.userDept.null")
	public Department getUserDepartment() {
		return userDepartment;
	}

	public void setUserDepartment(Department userDepartment) {
		this.userDepartment = userDepartment;
	}

	@Required(message="estimate.executingDept.null")
	public Department getExecutingDepartment() {
		return executingDepartment;
	}

	public void setExecutingDepartment(Department executingDepartment) {
		this.executingDepartment = executingDepartment;
	}

	@Required(message="estimate.preparedBy.null")
	public PersonalInformation getEstimatePreparedBy() {
		return estimatePreparedBy;
	}

	public void setEstimatePreparedBy(PersonalInformation estimatePreparedBy) {
		this.estimatePreparedBy = estimatePreparedBy;
	}

	@Required(message="estimate.ward.null")
	public Boundary getWard() {
		return ward;
	}

	public void setWard(Boundary ward) {
		this.ward = ward;
	}

	public Money getWorkValue() {
		double amt=0;
		for (Activity activity : activities) {
			if(activity.getRevisionType()!=null && activity.getRevisionType().equals(RevisionType.REDUCED_QUANTITY))
				amt-=activity.getAmount().getValue();
			else
				amt+=activity.getAmount().getValue();
		}
		workValue = new Money(amt);
		return workValue;
	}
	
	
	/**
	 * This method returns the grand total of the work value for all the  activities 
	 * ( both SOR and Non SOR combined)
	 * 
	 * @return a double value representing the rounded figure of the total of the 
	 * grand total of the work value for all the  activities 
	 * ( both SOR and Non SOR combined)
	 */
	public Money getWorkValueIncludingTaxes() {
		double amt=0;
		for (Activity activity : activities) {
			if(activity.getRevisionType()!=null && activity.getRevisionType().equals(RevisionType.REDUCED_QUANTITY))
				amt=amt-(activity.getAmount().getValue()+activity.getTaxAmount().getValue());
			else
				amt+=activity.getAmount().getValue()+activity.getTaxAmount().getValue();
		}
		return new Money(amt);
	}
	
	public Money getTotalTax() {
		double amt=0;
		for (Activity activity : activities) {
			if(activity.getRevisionType()!=null && activity.getRevisionType().equals(RevisionType.REDUCED_QUANTITY))
				amt=amt-(activity.getTaxAmount().getValue());
			else
				amt+=activity.getTaxAmount().getValue();
		}
		return new Money(amt);
	}

	public void setWorkValue(Money workValue) {
		this.workValue = workValue;
	}

	public List<OverheadValue> getOverheadValues() {
		return overheadValues;
	}

	public void setOverheadValues(List<OverheadValue> overheadValues) {
		this.overheadValues = overheadValues;
	}
	public void addOverheadValue(OverheadValue overheadValue) {
		this.overheadValues.add(overheadValue);
	}
	
	public List<AssetsForEstimate> getAssetValues() {
		return assetValues;
	}
	
	public void setAssetValues(List<AssetsForEstimate> assetValues) {
		this.assetValues = assetValues;
	}
	public void addAssetValue(AssetsForEstimate assetValue) {
		this.assetValues.add(assetValue);
	}
	
	
	public List<FinancialDetail> getFinancialDetails() {
		return financialDetails;
	}

	public void setFinancialDetails(List<FinancialDetail> financialDetails) {
		this.financialDetails = financialDetails;
	}

	public void addFinancialDetails(FinancialDetail financialDetails) {
		this.financialDetails.add(financialDetails);
	}
	
	public Money getTotalAmount() {
		double totalAmt=getWorkValue().getValue();
		for (OverheadValue oh : overheadValues) {
			totalAmt+=oh.getAmount().getValue();
		}
		return new Money(Math.round(totalAmt));
	}
	
	public List<Activity> getActivities() {
		return activities;
	}
	
	public ProjectCode getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(ProjectCode projectCode) {
		this.projectCode = projectCode;
	}

	public String getBudgetApprNo() {
		return budgetApprNo;
	}

	public void setBudgetApprNo(String budgetApprNo) {
		this.budgetApprNo = budgetApprNo;
	}
	
	
	public Collection<Activity> getSORActivities() {
		return CollectionUtils.select(activities, new Predicate(){
			public boolean evaluate(Object activity) {
				return ((Activity)activity).getSchedule()!=null;
			}});
	}
	
	private String totalSOREstimatedAmt;
	private String totalSORServiceTaxPerc;
	private String totalSORServiceTaxAmt;
	private String totalSORAmtInclTax;
	
	public Collection<Activity> getWorkOrderSORActivities() {
		Collection<Activity> sorActivities = CollectionUtils.select(activities, new Predicate(){
			public boolean evaluate(Object activity) {
				return ((Activity)activity).getSchedule()!=null;
			}});
		
		NumberFormat nf =NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		double totalSOREstimatedAmt = 0;
		double totalSORServiceTaxPerc = 0;
		double totalSORServiceTaxAmt = 0;
		double totalSORAmtInclTax=0;
		for(Activity activity : sorActivities){
			activity.getRate().setValue(Double.valueOf(nf.format(Math.round(activity.getRate().getValue()))));
			activity.getAmount().setValue(Double.valueOf(nf.format(Math.round(activity.getAmount().getValue()))));
			activity.getTaxAmount().setValue(Double.valueOf(nf.format(Math.round(activity.getTaxAmount().getValue()))));
			activity.getAmountIncludingTax().setValue(Double.valueOf(nf.format(Math.round(activity.getAmountIncludingTax().getValue()))));
			
			totalSOREstimatedAmt += activity.getAmount().getValue();
			totalSORServiceTaxPerc += activity.getServiceTaxPerc();
			totalSORServiceTaxAmt += activity.getTaxAmount().getValue();
			totalSORAmtInclTax += activity.getAmountIncludingTax().getValue();
		}
		
		setTotalSOREstimatedAmt(nf.format(totalSOREstimatedAmt));
		setTotalSORServiceTaxPerc(nf.format(totalSORServiceTaxPerc));
		setTotalSORServiceTaxAmt(nf.format(totalSORServiceTaxAmt));
		setTotalSORAmtInclTax(nf.format(totalSORAmtInclTax));
		return sorActivities;
	}
	
	private String totalNonSOREstimatedAmt;
	private String totalNonSORServiceTaxPerc;
	private String totalNonSORServiceTaxAmt;
	private String totalNonSORAmtInclTax;
	
	public Collection<Activity> getWorkOrderNonSORActivities() {
		Collection<Activity> nonsorActivities = CollectionUtils.select(activities, new Predicate(){
			public boolean evaluate(Object activity) {
				return ((Activity)activity).getNonSor()!=null;
			}});
		
		NumberFormat nf =NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		
		double totalNonSOREstimatedAmt = 0;
		double totalNonSORServiceTaxPerc = 0;
		double totalNonSORServiceTaxAmt = 0;
		double totalNonSORAmtInclTax=0;
		
		for(Activity activity : nonsorActivities){
			activity.getRate().setValue(Double.valueOf(nf.format(Math.round(activity.getRate().getValue()))));
			activity.getAmount().setValue(Double.valueOf(nf.format(Math.round(activity.getAmount().getValue()))));
			activity.getTaxAmount().setValue(Double.valueOf(nf.format(Math.round(activity.getTaxAmount().getValue()))));
			activity.getAmountIncludingTax().setValue(Double.valueOf(nf.format(Math.round(activity.getAmountIncludingTax().getValue()))));
			
			totalNonSOREstimatedAmt += activity.getAmount().getValue();
			totalNonSORServiceTaxPerc += activity.getServiceTaxPerc();
			totalNonSORServiceTaxAmt += activity.getTaxAmount().getValue();
			totalNonSORAmtInclTax += activity.getAmountIncludingTax().getValue();
		}
		
		setTotalNonSOREstimatedAmt(nf.format(totalNonSOREstimatedAmt));
		setTotalNonSORServiceTaxPerc(nf.format(totalNonSORServiceTaxPerc));
		setTotalNonSORServiceTaxAmt(nf.format(totalNonSORServiceTaxAmt));
		setTotalNonSORAmtInclTax(nf.format(totalNonSORAmtInclTax));
		return nonsorActivities;
	}

	public Collection<Activity> getNonSORActivities() {
		return CollectionUtils.select(activities, new Predicate(){
			public boolean evaluate(Object activity) {
				return ((Activity)activity).getNonSor()!=null;
			}});
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public void addActivity(Activity activity) {
		this.activities.add(activity);
	}

	public String getEstimateNumber() {
		return estimateNumber;
	}

	public void setEstimateNumber(String estimateNumber) {
		this.estimateNumber=estimateNumber;
	}

	public EgwTypeOfWork getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(EgwTypeOfWork parentCategory) {
		this.parentCategory = parentCategory;
	}

	public List<MultiYearEstimate> getMultiYearEstimates() {
		return multiYearEstimates;
	}

	public void setMultiYearEstimates(List<MultiYearEstimate> multiYearEstimates) {
		this.multiYearEstimates = multiYearEstimates;
	}
	
	public void addMultiYearEstimate(MultiYearEstimate multiYearEstimate) {
		this.multiYearEstimates.add(multiYearEstimate);
	}
	
	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		validationErrors.addAll(validateActivities());
		validationErrors.addAll(validateOverheads());
		validationErrors.addAll(validateAssets());
		validationErrors.addAll(validateMultiYearEstimates());
		validationErrors.addAll(validateFinancialDetails());
		
		return validationErrors;
	}
		
	public List<ValidationError> validateFinancialDetails() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		for(FinancialDetail financialDetail: financialDetails) {
			validationErrors.addAll(financialDetail.validate());
		}
		return validationErrors;
	}
	
	public List<ValidationError> validateActivities() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		for(Activity activity: activities) {
			validationErrors.addAll(activity.validate());
		}
		return validationErrors;
	}
	
	public List<ValidationError> validateOverheads() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		for(OverheadValue overheadValue: overheadValues) {
			validationErrors.addAll(overheadValue.validate());
		}
		
		return validationErrors;
	}
	
	public List<ValidationError> validateAssets() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		for(AssetsForEstimate assetValue: assetValues) {
			validationErrors.addAll(assetValue.validate());
		}
		
		return validationErrors;
	}

	public List<ValidationError> validateMultiYearEstimates() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		for(MultiYearEstimate multiYearEstimate: multiYearEstimates) {
			validationErrors.addAll(multiYearEstimate.validate());
		}
		
		return validationErrors;
	}

	/**
	 * This method returns the least of the financial years chosen from the multi year 
	 * estimates
	 *  
	 * @return an instance of <code>CFinancialYear</code> representing the least 
	 * financial year
	 */
	public CFinancialYear getLeastFinancialYearForEstimate(){
		CFinancialYear minfinYr = null;
		
		Date minStartDate = new Date();
		Date startDate;
		
		if(getMultiYearEstimates()!=null){
			minStartDate=getMultiYearEstimates().get(0).getFinancialYear().getStartingDate();
			minfinYr= getMultiYearEstimates().get(0).getFinancialYear();
		}
		for(int i=1;i<getMultiYearEstimates().size();i++){
			startDate=getMultiYearEstimates().get(i).getFinancialYear().getStartingDate();
			
			if(startDate.before(minStartDate)){
				minStartDate=startDate;
				minfinYr= getMultiYearEstimates().get(i).getFinancialYear();
			}
			
		}
		return minfinYr;
	}
	
	@Override
	public String getStateDetails() {
		return "AbstractEstimate : " + getEstimateNumber();
	}

	public String getTotalSOREstimatedAmt() {
		return totalSOREstimatedAmt;
	}

	public void setTotalSOREstimatedAmt(String totalSOREstimatedAmt) {
		this.totalSOREstimatedAmt = totalSOREstimatedAmt;
	}

	public String getTotalSORServiceTaxPerc() {
		return totalSORServiceTaxPerc;
	}

	public void setTotalSORServiceTaxPerc(String totalSORServiceTaxPerc) {
		this.totalSORServiceTaxPerc = totalSORServiceTaxPerc;
	}

	public String getTotalSORServiceTaxAmt() {
		return totalSORServiceTaxAmt;
	}

	public void setTotalSORServiceTaxAmt(String totalSORServiceTaxAmt) {
		this.totalSORServiceTaxAmt = totalSORServiceTaxAmt;
	}

	public String getTotalSORAmtInclTax() {
		return totalSORAmtInclTax;
	}

	public void setTotalSORAmtInclTax(String totalSORAmtInclTax) {
		this.totalSORAmtInclTax = totalSORAmtInclTax;
	}

	public String getTotalNonSOREstimatedAmt() {
		return totalNonSOREstimatedAmt;
	}

	public void setTotalNonSOREstimatedAmt(String totalNonSOREstimatedAmt) {
		this.totalNonSOREstimatedAmt = totalNonSOREstimatedAmt;
	}

	public String getTotalNonSORServiceTaxPerc() {
		return totalNonSORServiceTaxPerc;
	}

	public void setTotalNonSORServiceTaxPerc(String totalNonSORServiceTaxPerc) {
		this.totalNonSORServiceTaxPerc = totalNonSORServiceTaxPerc;
	}

	public String getTotalNonSORServiceTaxAmt() {
		return totalNonSORServiceTaxAmt;
	}

	public void setTotalNonSORServiceTaxAmt(String totalNonSORServiceTaxAmt) {
		this.totalNonSORServiceTaxAmt = totalNonSORServiceTaxAmt;
	}

	public String getTotalNonSORAmtInclTax() {
		return totalNonSORAmtInclTax;
	}

	public void setTotalNonSORAmtInclTax(String totalNonSORAmtInclTax) {
		this.totalNonSORAmtInclTax = totalNonSORAmtInclTax;
	}

	public Long getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
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
    
	@Required(message="estimate.fund.null")
	public Fundsource getFundSource() {
		return fundSource;
	}

	public void setFundSource(Fundsource fundSource) {
		this.fundSource = fundSource;
	}
	
	public void setApproverUserId(Integer approverUserId) {
		this.approverUserId = approverUserId;
	}

	public Integer getApproverUserId() {
		return approverUserId;
	}

	public String getPositionAndUserName() {
		return positionAndUserName;
	}

	public void setPositionAndUserName(String positionAndUserName) {
		this.positionAndUserName = positionAndUserName;
	}

	public BigDecimal getBudgetAvailable() {
		return budgetAvailable;
	}
	
	public void setBudgetAvailable(BigDecimal budgetAvailable) {
		this.budgetAvailable = budgetAvailable;
	}

	public String getBudgetRejectionNo() {
		return budgetRejectionNo;
	}

	public void setBudgetRejectionNo(String budgetRejectionNo) {
		this.budgetRejectionNo = budgetRejectionNo;
	}

	public DepositCode getDepositCode() {
		return depositCode;
	}

	public void setDepositCode(DepositCode depositCode) {
		this.depositCode = depositCode;
	}
	
	public String getCurrentStateCreatedDate() {
        String createdDate="";
        if(getCurrentState()!=null )
        	createdDate=new java.text.SimpleDateFormat("dd/MM/yyyy").format(getCurrentState().getCreatedDate());
        return createdDate;                
	}

	public Set<AbstractEstimateAppropriation> getAbstractEstimateAppropriations() {
		return abstractEstimateAppropriations;
	}
	public void setAbstractEstimateAppropriations(Set<AbstractEstimateAppropriation> abstractEstimateAppropriations) {
		this.abstractEstimateAppropriations = abstractEstimateAppropriations;
	}

	public Department getOldExecDepartment() {
		return oldExecDepartment;
	}

	public void setOldExecDepartment(Department oldExecDepartment) {
		this.oldExecDepartment = oldExecDepartment;
	}

	public String getOldEstimateNumber() {
		return oldEstimateNumber;
	}

	public void setOldEstimateNumber(String oldEstimateNumber) {
		this.oldEstimateNumber = oldEstimateNumber;
	}

	public Boundary getOldWard() {
		return oldWard;
	}

	public void setOldWard(Boundary oldWard) {
		this.oldWard = oldWard;
	}
	
	public AbstractEstimate getParent() {
		return parent;
	}

	public void setParent(AbstractEstimate parent) {
		this.parent = parent;
	}

	public Department getOldUserDepartment() {
		return oldUserDepartment;
	}

	public void setOldUserDepartment(Department oldUserDepartment) {
		this.oldUserDepartment = oldUserDepartment;
	}

	public void setTotalAmount(Money totalAmount) {
		
		this.totalAmount = totalAmount;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getIsCopiedEst() {
		return isCopiedEst;
	}

	public void setIsCopiedEst(String isCopiedEst) {
		this.isCopiedEst = isCopiedEst;
	}

	public List<EstimatePhoto> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(List<EstimatePhoto> photoList) {
		this.photoList = photoList;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public BigDecimal getLon() {
		return lon;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public void setLon(BigDecimal lon) {
		this.lon = lon;
	}
	
	public String toString() {
       return "AbstractEstimate ( Id : " + this.getId() + "Estimate No: " + this.estimateNumber +")";
    }

	public Department getPreviousUserDepartment() {
		return previousUserDepartment;
	}

	public void setPreviousUserDepartment(Department previousUserDepartment) {
		this.previousUserDepartment = previousUserDepartment;
	}

	public Date getBudgetApprDate() {
		return budgetApprDate;
	}

	public void setBudgetApprDate(Date budgetApprDate) {
		this.budgetApprDate = budgetApprDate;
	}

}
