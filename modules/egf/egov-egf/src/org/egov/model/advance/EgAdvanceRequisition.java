package org.egov.model.advance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.EgwStatus;
import org.egov.infstr.models.StateAware;

public class EgAdvanceRequisition extends StateAware implements java.io.Serializable{ 

//private Long id;
private String advanceRequisitionNumber;
private EgwStatus status;
private Date advanceRequisitionDate;
private BigDecimal advanceRequisitionAmount;
private String narration;
private String  arftype;

private EgAdvanceRequisitionMis egAdvanceReqMises; 	
private Set<EgAdvanceRequisitionDetails> egAdvanceReqDetailses = new HashSet<EgAdvanceRequisitionDetails>(0);

public EgAdvanceRequisition() {
}

public String getStateDetails() {
	return this.getAdvanceRequisitionNumber();
}

/*public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}
*/
public String getAdvanceRequisitionNumber() {
	return advanceRequisitionNumber;
}

public void setAdvanceRequisitionNumber(String advanceRequisitionNumber) {
	this.advanceRequisitionNumber = advanceRequisitionNumber;
}

public EgwStatus getStatus() {
	return status;
}

public void setStatus(EgwStatus status) {
	this.status = status;
}

public Date getAdvanceRequisitionDate() {
	return advanceRequisitionDate;
}

public void setAdvanceRequisitionDate(Date advanceRequisitionDate) {
	this.advanceRequisitionDate = advanceRequisitionDate;
}

public BigDecimal getAdvanceRequisitionAmount() {
	return advanceRequisitionAmount;
}

public void setAdvanceRequisitionAmount(BigDecimal advanceRequisitionAmount) {
	this.advanceRequisitionAmount = advanceRequisitionAmount;
}

public String getNarration() {
	return narration;
}

public void setNarration(String narration) {
	this.narration = narration;
}

public String getArftype() {
	return arftype;
}

public void setArftype(String arftype) {
	this.arftype = arftype;
}

public EgAdvanceRequisitionMis getEgAdvanceReqMises() {
	return egAdvanceReqMises;
}

public void setEgAdvanceReqMises(EgAdvanceRequisitionMis egAdvanceReqMises) {
	this.egAdvanceReqMises = egAdvanceReqMises;
}

public Set<EgAdvanceRequisitionDetails> getEgAdvanceReqDetailses() {
	return egAdvanceReqDetailses;
}

public void setEgAdvanceReqDetailses(
		Set<EgAdvanceRequisitionDetails> egAdvanceReqDetailses) {
	this.egAdvanceReqDetailses = egAdvanceReqDetailses;
}
public void addEgAdvanceReqDetails(EgAdvanceRequisitionDetails advanceReqDetail)
{
	if(advanceReqDetail!=null)
	{
		getEgAdvanceReqDetailses().add(advanceReqDetail);
	}
}

public EgAdvanceRequisition(Long id, String advanceRequisitionNumber,
		EgwStatus status, Date advanceRequisitionDate,
		BigDecimal advanceRequisitionAmount, String narration, String arftype,
		EgAdvanceRequisitionMis egAdvanceReqMises,
		Set<EgAdvanceRequisitionDetails> egAdvanceReqDetailses) {
	super();
	this.id = id;
	this.advanceRequisitionNumber = advanceRequisitionNumber;
	this.status = status;
	this.advanceRequisitionDate = advanceRequisitionDate;
	this.advanceRequisitionAmount = advanceRequisitionAmount;
	this.narration = narration;
	this.arftype = arftype;
	this.egAdvanceReqMises = egAdvanceReqMises;
	this.egAdvanceReqDetailses = egAdvanceReqDetailses;
}

}
