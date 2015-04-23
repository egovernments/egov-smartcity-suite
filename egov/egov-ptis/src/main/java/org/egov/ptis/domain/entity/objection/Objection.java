/**
 * 
 */
package org.egov.ptis.domain.entity.objection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.validator.annotation.CompareDates;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.hibernate.validator.constraints.Length;

/**
 * @author manoranjan
 * 
 */
@CompareDates(fromDate = "dateOfOutcome", toDate = "recievedOn", dateFormat = "dd/MM/yyyy", message = "dateOfOutcome.greaterThan.recievedOn")
public class Objection extends StateAware {

	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;

	private EgwStatus egwStatus;

	private BasicProperty basicProperty;

	@Length(max = 50, message = "objection.objectionNumber.length")
	private String objectionNumber;

	@ValidateDate(allowPast = true, dateFormat = "dd/MM/yyyy", message = "objection.receivedOn.futuredate")
	@org.egov.infra.persistence.validator.annotation.DateFormat(message = "invalid.fieldvalue.receivedOn")
	private Date recievedOn;

	@Length(max = 256, message = "objection.objectionNumber.length")
	private String recievedBy;

	private String details;

	private String docNumberObjection;

	private String docNumberOutcome;

	@Valid
	private List<Hearing> hearings = new LinkedList<Hearing>();

	@Valid
	private List<Inspection> inspections = new LinkedList<Inspection>();

	@ValidateDate(allowPast = true, dateFormat = "dd/MM/yyyy", message = "objection.outcomedate.futuredate")
	@org.egov.infra.persistence.validator.annotation.DateFormat(message = "invalid.fieldvalue.outcomedate")
	private Date dateOfOutcome;

	private String remarks;// for dateOfOutcome

	private Boolean objectionRejected;
	public static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

	@Override
	public String getStateDetails() {
		return getBasicProperty().getUpicNo();
	}

	public EgwStatus getEgwStatus() {
		return egwStatus;
	}

	public String getObjectionNumber() {
		return objectionNumber;
	}

	@Required(message = "objection.receiviedOn.null")
	public Date getRecievedOn() {
		return recievedOn;
	}

	@Required(message = "objection.receiviedBy.null")
	@Length(max = 256, message = "objection.receivedBy.length")
	public String getRecievedBy() {
		return recievedBy;
	}

	@Required(message = "objection.details.null")
	@Length(max = 1024, message = "objection.details.length")
	public String getDetails() {
		return details;
	}

	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}

	public void setObjectionNumber(String objectionNumber) {
		this.objectionNumber = objectionNumber;
	}

	public void setRecievedOn(Date recievedOn) {
		this.recievedOn = recievedOn;
	}

	public void setRecievedBy(String recievedBy) {
		this.recievedBy = recievedBy;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public List<Hearing> getHearings() {
		return hearings;
	}

	public void setHearings(List<Hearing> hearings) {
		this.hearings = hearings;
	}

	public List<Inspection> getInspections() {
		return inspections;
	}

	public Date getDateOfOutcome() {
		return dateOfOutcome;
	}

	public String getRemarks() {
		return remarks;
	}

	public Boolean getObjectionRejected() {
		return objectionRejected;
	}

	public void setInspections(List<Inspection> inspections) {
		this.inspections = inspections;
	}

	public void setDateOfOutcome(Date dateOfOutcome) {
		this.dateOfOutcome = dateOfOutcome;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setObjectionRejected(Boolean objectionRejected) {
		this.objectionRejected = objectionRejected;
	}

	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}

	public String getDocNumberObjection() {
		return docNumberObjection;
	}

	public String getDocNumberOutcome() {
		return docNumberOutcome;
	}

	public void setDocNumberObjection(String docNumberObjection) {
		this.docNumberObjection = docNumberObjection;
	}

	public void setDocNumberOutcome(String docNumberOutcome) {
		this.docNumberOutcome = docNumberOutcome;
	}

	public String getFmtdReceivedOn() {
		if (recievedOn != null)
			return dateFormat.format(recievedOn);
		else
			return "";
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		
		sb.append("UcipNo :").append(null!=basicProperty?basicProperty.getUpicNo():" ");
		sb.append("status :").append(null!= egwStatus?egwStatus.getDescription():" ");
		sb.append("objectionNumber :").append(null!= objectionNumber?objectionNumber:" ");
		
		return sb.toString();
	}

}
