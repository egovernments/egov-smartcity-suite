package org.egov.works.models.tender;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.StateAware;
import org.egov.lib.rjbac.dept.Department;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeServiceImpl;
import org.egov.tender.interfaces.Tenderable;
import org.egov.tender.interfaces.TenderableGroup;
import org.egov.works.models.estimate.ActivityComparator;
import org.egov.works.models.rateContract.IndentDetailComparator;
import org.hibernate.validator.constraints.NotEmpty;


public class TenderFile  extends StateAware implements org.egov.tender.interfaces.TenderFile{
	private static final Logger logger = Logger.getLogger(TenderFile.class);
	private String owner;
	public enum TenderFileStatus{
		CREATED,CHECKED,APPROVED,REJECTED,CANCELLED,RESUBMITTED
	}
	public enum Actions{
		SUBMIT_FOR_APPROVAL,APPROVE,REJECT,CANCEL;

		public String toString() {
			return this.name().toLowerCase();
		}
	}
	
	private EmployeeServiceImpl empService = new EmployeeServiceImpl();
	
	public String getHodName() {
		List<PersonalInformation> personalInfoList = null;
		try{
		    personalInfoList =  empService.getAllHodEmpByDept(getDepartment().getId());
		}catch(Exception ec){
			logger.error(ec.getMessage());
		}
		
		if(personalInfoList.size()>0){
		    return personalInfoList.get(0).getEmployeeName();
		}
		else {
			return null;
		}	
	}
	
	private String fileNumber;
	
	@NotNull(message="tenderFile.fileDate.is.null")
	private Date fileDate;
	
	@NotEmpty(message="tenderFile.fileName.is.null")
	private String fileName;
	
	private String fileDescription;
	
	@NotNull(message="tenderFile.department.is.null")
	private Department department;
	
	private String remarks;
	
	private EgwStatus egwStatus;
	
	@NotNull(message="tenderFile.preparedBy.is.null") 
	private PersonalInformation preparedBy; 
	
	private Long documentNumber;
	
	@Valid
	private List<TenderFileDetail> tenderFileDetails = new LinkedList<TenderFileDetail>();
	
	@Valid
	private List<TenderFileNewsPapers> tenderFileNewsPapers = new LinkedList<TenderFileNewsPapers>();
	
	private List<String> tenderFileActions = new ArrayList<String>();
	private boolean quotationFlag;

	private transient String additionalWfRule;
	private transient BigDecimal amountWfRule;
	
	
	public String getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}

	public Date getFileDate() {
		return fileDate;
	}

	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = StringEscapeUtils.unescapeHtml(fileName);
	}

	public String getFileDescription() {
		return fileDescription;
	}

	public void setFileDescription(String fileDescription) {
		this.fileDescription = StringEscapeUtils.unescapeHtml(fileDescription);
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = StringEscapeUtils.unescapeHtml(remarks);
	}

	public EgwStatus getEgwStatus() {
		return egwStatus;
	}

	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}

	public List<TenderFileDetail> getTenderFileDetails() {
		return tenderFileDetails;
	}

	public void setTenderFileDetails(List<TenderFileDetail> tenderFileDetails) {
		this.tenderFileDetails = tenderFileDetails;
	}
	
	public void addTenderFileDetail(TenderFileDetail tenderFileDetail) {
		this.tenderFileDetails.add(tenderFileDetail);
	}
	
	
	@Override
	public String getStateDetails() {
		return "Tender File : " + getFileNumber();
	}
	
	public List<ValidationError> validate()	{
		List<ValidationError> validationErrors = new ArrayList<ValidationError>(); 		
		return validationErrors;
	}

	public PersonalInformation getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(PersonalInformation preparedBy) {
		this.preparedBy = preparedBy;
	}

	public Long getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}
	
	
	public List<String> getTenderFileActions() {
		return tenderFileActions;
	}

	public void setTenderFileActions(List<String> tenderFileActions) {
		this.tenderFileActions = tenderFileActions;
	}
		
	@Override
	public Long getId(){
		return this.id;
	}
	
	@Override
	public String getDescription() {
		return this.fileDescription;
	}

	@Override
	public EgwStatus getStatus() {
		return this.egwStatus;
	}

	@Override
	public Date getTenderDate() {
		return this.fileDate;
	}
	
	@Override
	public Set<TenderableGroup> getTenderGroups() {
		Set<TenderableGroup> tenderGroup= new HashSet<TenderableGroup>();
		for(TenderFileDetail tenderFileDetail: tenderFileDetails) {
			if(tenderFileDetail.getIndent()==null)
				tenderGroup.add(tenderFileDetail.getAbstractEstimate());
			else if(tenderFileDetail.getAbstractEstimate()==null)
				tenderGroup.add(tenderFileDetail.getIndent());
		}
		return tenderGroup;
	}
	
	@Override
	public Set<Tenderable> getTenderEntities() {
		Set<Tenderable> tenderEntities = Collections.EMPTY_SET;
		Set<Tenderable> activitySet = new TreeSet<Tenderable>(new ActivityComparator());
		Set<Tenderable> indentSet = new TreeSet<Tenderable>(new IndentDetailComparator());
		for(TenderFileDetail tenderFileDetail: tenderFileDetails) {
			if(tenderFileDetail.getIndent()==null)
				activitySet.addAll(tenderFileDetail.getAbstractEstimate().getActivities());
			else if(tenderFileDetail.getAbstractEstimate()==null && tenderFileDetail.getTenderFileIndentDetails()!=null) {
				for(TenderFileIndentDetail tenderFileIndentDetail: tenderFileDetail.getTenderFileIndentDetails()){
					indentSet.add(tenderFileIndentDetail.getIndentDetail());
				}
			}				
		}
		if(activitySet.isEmpty())
			tenderEntities = indentSet;
		else
			tenderEntities = activitySet;
		return tenderEntities;
	}

	@Override
	public Boolean combineTenderableGroups() {
		return false;
	}

	public boolean getQuotationFlag() {
		return quotationFlag;
	}

	public void setQuotationFlag(boolean quotationFlag) {
		this.quotationFlag = quotationFlag;
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

	public List<TenderFileNewsPapers> getTenderFileNewsPapers() {
		return tenderFileNewsPapers;
	}

	public void setTenderFileNewsPapers(
			List<TenderFileNewsPapers> tenderFileNewsPapers) {
		this.tenderFileNewsPapers = tenderFileNewsPapers;
	}
	
	public void addTenderFileNewsPapers(TenderFileNewsPapers tenderFileNewsPapers) {
		this.tenderFileNewsPapers.add(tenderFileNewsPapers);
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
}

