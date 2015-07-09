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
package org.egov.billsaccounting.model;

// Generated Feb 13, 2007 1:14:54 PM by Hibernate Tools 3.1.0.beta5

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.egov.model.bills.EgBillregister;
import org.egov.commons.EgwStatus;
import org.egov.commons.Relation;
import org.egov.infra.admin.master.entity.Department;
import org.egov.billsaccounting.model.Worksdetail;

 

/**
 * Contractorbilldetail generated by hbm2java
 */
public class Contractorbilldetail implements java.io.Serializable {

	// Fields

	private Integer id;

	private Worksdetail worksdetail;
	
	//private Integer billId;
	
	private Date billDate;
	
	
	private EgBillregister egBillregister; 


	private EgwStatus egwStatus;

	
	private Long voucherHeaderId;

	private Relation relation;

	private String billnumber;

	private Date billdate;

	private BigDecimal otherrecoveries= new BigDecimal(0);

	private BigDecimal paidamount= new BigDecimal(0);

	private BigDecimal billamount= new BigDecimal(0);

	private BigDecimal passedamount= new BigDecimal(0);

	private String approvedby;

	private BigDecimal payableaccount= new BigDecimal(0);

	private String narration;

	private BigDecimal tdsamount= new BigDecimal(0);

	private Boolean tdspaidtoit;

	private BigDecimal advadjamt= new BigDecimal(0);

	private Boolean isreversed;

	private BigDecimal assetid;

	private BigDecimal capRev;

	private Department generatedby;

	private BigDecimal securitydeposity= new BigDecimal(0);

	private Date lastmodifieddate;
	
	

	private String billtype;
	
	private Date paybydate;
	
	private BigDecimal materialAdjAmt= new BigDecimal(0);

	private Set egwBillDeductionses = new HashSet(0);

	private Set subledgerpaymentheaders = new HashSet(0);
 
	
	// Constructors

	
	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Worksdetail getWorksdetail() {
		return this.worksdetail;
	}

	public void setWorksdetail(Worksdetail worksdetail) {
		this.worksdetail = worksdetail;
	}

	public EgwStatus getEgwStatus() {
		return this.egwStatus;
	}

	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}

	

	public Relation getRelation() {
		return this.relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public String getBillnumber() {
		return this.billnumber;
	}

	public void setBillnumber(String billnumber) {
		this.billnumber = billnumber;
	}

	public Date getBilldate() {
		return this.billdate;
	}

	public void setBilldate(Date billdate) {
		this.billdate = billdate;
	}

	public BigDecimal getOtherrecoveries() {
		return this.otherrecoveries;
	}

	public void setOtherrecoveries(BigDecimal otherrecoveries) {
		this.otherrecoveries = otherrecoveries;
	}

	public BigDecimal getPaidamount() {
		return this.paidamount;
	}

	public void setPaidamount(BigDecimal paidamount) {
		this.paidamount = paidamount;
	}

	public BigDecimal getBillamount() {
		return this.billamount;
	}

	public void setBillamount(BigDecimal billamount) {
		this.billamount = billamount;
	}

	public BigDecimal getPassedamount() {
		return this.passedamount;
	}

	public void setPassedamount(BigDecimal passedamount) {
		this.passedamount = passedamount;
	}

	public String getApprovedby() {
		return this.approvedby;
	}

	public void setApprovedby(String approvedby) {
		this.approvedby = approvedby;
	}

	public BigDecimal getPayableaccount() {
		return this.payableaccount;
	}

	public void setPayableaccount(BigDecimal payableaccount) {
		this.payableaccount = payableaccount;
	}

	public String getNarration() {
		return this.narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public BigDecimal getTdsamount() {
		return this.tdsamount;
	}

	public void setTdsamount(BigDecimal tdsamount) {
		this.tdsamount = tdsamount;
	}
	/**
	 * @return Returns the voucherHeaderId.
	 */
	public Long getVoucherHeaderId() {
		return voucherHeaderId;
	}
	/**
	 * @param voucherHeaderId The voucherHeaderId to set.
	 */
	public void setVoucherHeaderId(Long voucherHeaderId) {
		this.voucherHeaderId = voucherHeaderId;
	}
	public Boolean getTdspaidtoit() {
		return this.tdspaidtoit;
	}

	public void setTdspaidtoit(Boolean tdspaidtoit) {
		this.tdspaidtoit = tdspaidtoit;
	}

	public BigDecimal getAdvadjamt() {
		return this.advadjamt;
	}

	public void setAdvadjamt(BigDecimal advadjamt) {
		this.advadjamt = advadjamt;
	}

	public Boolean getIsreversed() {
		return this.isreversed;
	}

	public void setIsreversed(Boolean isreversed) {
		this.isreversed = isreversed;
	}

	public BigDecimal getAssetid() {
		return this.assetid;
	}

	public void setAssetid(BigDecimal assetid) {
		this.assetid = assetid;
	}

	public BigDecimal getCapRev() {
		return this.capRev;
	}

	public void setCapRev(BigDecimal capRev) {
		this.capRev = capRev;
	}

	public Department getGeneratedby() {
		return this.generatedby;
	}

	public void setGeneratedby(Department generatedby) {
		this.generatedby = generatedby;
	}

	public BigDecimal getSecuritydeposity() {
		return this.securitydeposity;
	}

	public void setSecuritydeposity(BigDecimal securitydeposity) {
		this.securitydeposity = securitydeposity;
	}

	public Date getLastmodifieddate() {
		return this.lastmodifieddate;
	}

	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}

	public String getBilltype() {
		return this.billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public Set getEgwBillDeductionses() {
		return this.egwBillDeductionses;
	}

	public void setEgwBillDeductionses(Set egwBillDeductionses) {
		this.egwBillDeductionses = egwBillDeductionses;
	}

	public Set getSubledgerpaymentheaders() {
		return this.subledgerpaymentheaders;
	}

	public void setSubledgerpaymentheaders(Set subledgerpaymentheaders) {
		this.subledgerpaymentheaders = subledgerpaymentheaders;
	}

	public BigDecimal getMaterialAdjAmt()
	{
		return materialAdjAmt;
	}

	public void setMaterialAdjAmt(BigDecimal materialAdjAmt)
	{
		this.materialAdjAmt = materialAdjAmt;
	}

	public Date getPaybydate()
	{
		return paybydate;
	}

	public void setPaybydate(Date paybydate)
	{
		this.paybydate = paybydate;
	}

	
	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public EgBillregister getEgBillregister() {
		return egBillregister;
	}

	public void setEgBillregister(EgBillregister egBillregister) {
		this.egBillregister = egBillregister;
	}

}
