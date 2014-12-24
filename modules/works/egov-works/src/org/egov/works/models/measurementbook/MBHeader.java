package org.egov.works.models.measurementbook;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.Money;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.models.validator.CheckDateFormat;
import org.egov.infstr.models.validator.GreaterThan;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.ValidateDate;
import org.egov.pims.model.PersonalInformation;
import org.egov.works.models.revisionEstimate.RevisionAbstractEstimate;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Min;
import javax.validation.Valid;

public class MBHeader extends StateAware{
	
	public enum MeasurementBookStatus{
		NEW,CREATED,CHECKED,REJECTED,RESUBMITTED,CANCELLED,APPROVED
	}
	
	public enum Actions{
		SAVE,SUBMIT_FOR_APPROVAL,REJECT,CANCEL,APPROVAL;

		public String toString() {
			return this.name().toLowerCase();
		}
	}	
	
	@Required(message = "mbheader.workorder.null")
	private WorkOrder workOrder;
	@Required(message = "mbheader.mbrefno.null")
	@Length(max = 50, message = "mbheader.mbrefno.length")
	private String mbRefNo;
	@Required(message = "mbheader.mbPreparedBy.null")
	private PersonalInformation mbPreparedBy;
	@Length(max = 400, message = "mbheader.contractorComments.length")
	private String contractorComments;
	@Required(message = "mbheader.mbdate.null")
	@ValidateDate(allowPast=true,dateFormat="dd/MM/yyyy",message="mbheader.mbDate.futuredate")
	@CheckDateFormat(message = "invalid.fieldvalue.mbDate")
	private Date mbDate;
	@Required(message = "mbheader.mbabstract.null")
	@Length(max = 400, message = "mbheader.mbabstract.length")
	private String mbAbstract;
	@Required(message = "mbheader.fromPageNo.null")
	@GreaterThan(value=0,message="mbheader.fromPageNo.non.negative")
	private Integer fromPageNo;
	@Min(value=0,message="mbheader.toPageNo.non.negative")
	private Integer toPageNo;

	private WorkOrderEstimate workOrderEstimate;
	private Long documentNumber;
	private Integer approverUserId;
	private Set<MBBill> mbBills = new HashSet<MBBill>();

	
	private RevisionAbstractEstimate revisionEstimate;
	
	private EgwStatus egwStatus;
	private transient String additionalWfRule;
	
	@Valid
	private List<MBDetails> mbDetails = new LinkedList<MBDetails>();
	private String owner;
	private List<String> mbActions = new ArrayList<String>();
	private WorkOrderEstimate revisionWorkOrderEstimate;

	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if (workOrder != null
				&& (workOrder.getId() == null || workOrder.getId() == 0 || workOrder
						.getId() == -1)) {
			validationErrors.add(new ValidationError("workOrder", "mbheader.workorder.null"));
		} 
		if (mbPreparedBy != null
				&& (mbPreparedBy.getIdPersonalInformation() == null
						|| mbPreparedBy.getIdPersonalInformation() == 0 || mbPreparedBy
						.getIdPersonalInformation() == -1)) {
			validationErrors.add(new ValidationError("mbPreparedBy", "mbheader.mbPreparedBy.null"));
		} 
		if ((fromPageNo != null && toPageNo != null) && fromPageNo > toPageNo) {
			validationErrors.add(new ValidationError("toPageNo", "mbheader.toPageNo.invalid"));
		} 
		if(mbDate != null && workOrder!=null && workOrder.getWorkOrderDate() != null && mbDate.before(workOrder.getWorkOrderDate())){
			validationErrors.add(new ValidationError("mbDate", "mbheader.mbDate.invalid"));
		}
		
		return validationErrors;
	}
	
	public Money getMbAmount() {
		Money mbAmount= new Money(0);
		for (MBDetails mbd : mbDetails) {
			mbAmount = mbAmount.add(mbd.getAmount());
		}
		return mbAmount;
	}
	
	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setMbRefNo(String mbRefNo) {
		this.mbRefNo = mbRefNo;
	}

	public String getMbRefNo() {
		return mbRefNo;
	}

	public void setMbDate(Date mbDate) {
		this.mbDate = mbDate;
	}

	public Date getMbDate() {
		return mbDate;
	}

	public void setMbAbstract(String mbAbstract) {
		this.mbAbstract = StringEscapeUtils.unescapeHtml(mbAbstract);
	}

	public String getMbAbstract() {
		return mbAbstract;
	}

	public Integer getFromPageNo() {
		return fromPageNo;
	}

	public void setFromPageNo(Integer fromPageNo) {
		this.fromPageNo = fromPageNo;
	}

	public Integer getToPageNo() {
		return toPageNo;
	}

	public void setToPageNo(Integer toPageNo) {
		this.toPageNo = toPageNo;
	}

	public PersonalInformation getMbPreparedBy() {
		return mbPreparedBy;
	}

	public void setMbPreparedBy(PersonalInformation mbPreparedBy) {
		this.mbPreparedBy = mbPreparedBy;
	}

	public String getContractorComments() {
		return contractorComments;
	}

	public void setContractorComments(String contractorComments) {
		this.contractorComments = StringEscapeUtils.unescapeHtml(contractorComments);
	}

	public List<MBDetails> getMbDetails() {
		return mbDetails;
	}

	public void setMbDetails(List<MBDetails> mbDetails) {
		this.mbDetails = mbDetails;
	}

	public void addMbDetails(MBDetails mbDetails) {
		this.mbDetails.add(mbDetails);
	}
	
	//to show in inbox
	@Override
	public String getStateDetails() {
		return "MbHeader : " + getMbRefNo();
	}

	public Set<MBBill> getMbBills() {
		return mbBills;
	}

	public void setMbBills(Set<MBBill> mbBills) {
		this.mbBills = mbBills;
	}


	public Integer getApproverUserId() {
		return approverUserId;
	}

	public void setApproverUserId(Integer approverUserId) {
		this.approverUserId = approverUserId;
	}
	public WorkOrderEstimate getWorkOrderEstimate() {
		return workOrderEstimate;
	}

	public void setWorkOrderEstimate(WorkOrderEstimate workOrderEstimate) {
		this.workOrderEstimate = workOrderEstimate;
	}


	public Long getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<String> getMbActions() {
		return mbActions;
	}

	public void setMbActions(List<String> mbActions) {
		this.mbActions = mbActions;
	}

	public RevisionAbstractEstimate getRevisionEstimate() {
		return revisionEstimate;
	}
	
	public void setRevisionEstimate(RevisionAbstractEstimate revisionEstimate) {
		this.revisionEstimate = revisionEstimate;
}

	public EgwStatus getEgwStatus() {
		return egwStatus;
	}

	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}

	public WorkOrderEstimate getRevisionWorkOrderEstimate() {
		return revisionWorkOrderEstimate;
	}

	public void setRevisionWorkOrderEstimate(
			WorkOrderEstimate revisionWorkOrderEstimate) {
		this.revisionWorkOrderEstimate = revisionWorkOrderEstimate;
	}

	public String getAdditionalWfRule() {
		return additionalWfRule;
	}

	public void setAdditionalWfRule(String additionalWfRule) {
		this.additionalWfRule = additionalWfRule;
	}
}
