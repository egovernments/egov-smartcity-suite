/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.model.advance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.EgwStatus;
import org.egov.infra.workflow.entity.StateAware;

public class EgAdvanceRequisition extends StateAware implements java.io.Serializable{ 

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
	//this.id = id;
	this.advanceRequisitionNumber = advanceRequisitionNumber;
	this.status = status;
	this.advanceRequisitionDate = advanceRequisitionDate;
	this.advanceRequisitionAmount = advanceRequisitionAmount;
	this.narration = narration;
	this.arftype = arftype;
	this.egAdvanceReqMises = egAdvanceReqMises;
	this.egAdvanceReqDetailses = egAdvanceReqDetailses;
}

public String toString()
{
	return ("EgAdvanceRequisition ( Id :  "+ (null != this.getId()?this.getId():"") + "EgAdvanceRequisition arftype: " +(null != this.getArftype()?this.getArftype():"") +")");
}
}
