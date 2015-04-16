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
package org.egov.commons;

import java.math.BigDecimal;
import java.util.Date;

public class Chequedetail implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private Bankbranch bankbranch;

	private Bankaccount bankaccount;

	private Bank bank;

	private CVoucherHeader voucherheader;

	private Date chequedate;

	private String chequenumber;

	private Date receiptdate;

	private BigDecimal amount;

	private BigDecimal regionid;

	private boolean isdeposited;

	private String payinslipnumber;

	private Date payinslipdate;

	private String narration;

	private String payto;

	private BigDecimal paidbyid;

	private String approvedby;

	private Date created;

	private BigDecimal modifiedby;

	private Date lastmodified;

	private Boolean ispaycheque;

	private Boolean isreversed;

	private Character chequetype;

	private String bankname;

	private String type;

	private BigDecimal chqstatus;

	private Integer detailTypeId;

	private Integer detailKeyId;

	public Chequedetail() {
		//For hibernate to work
	}

	public Chequedetail(Date chequedate, String chequenumber, Date receiptdate, BigDecimal amount, boolean isdeposited) {
		this.chequedate = chequedate;
		this.chequenumber = chequenumber;
		this.receiptdate = receiptdate;
		this.amount = amount;
		this.isdeposited = isdeposited;
	}

	public Chequedetail(Bankbranch bankbranch, Bankaccount bankaccount, Bank bank, CVoucherHeader voucherheader, Date chequedate, String chequenumber, Date receiptdate, BigDecimal amount, BigDecimal regionid, boolean isdeposited, String payinslipnumber,
			Date payinslipdate, String narration, String payto, BigDecimal paidbyid, String approvedby, Date created, BigDecimal modifiedby, Date lastmodified, Boolean ispaycheque, Boolean isreversed, Character chequetype, String bankname, String type,
			BigDecimal chqstatus, Integer detailTypeId, Integer detailKeyId) {
		this.bankbranch = bankbranch;
		this.bankaccount = bankaccount;
		this.bank = bank;
		this.chequedate = chequedate;
		this.chequenumber = chequenumber;
		this.receiptdate = receiptdate;
		this.amount = amount;
		this.regionid = regionid;
		this.isdeposited = isdeposited;
		this.payinslipnumber = payinslipnumber;
		this.payinslipdate = payinslipdate;
		this.narration = narration;
		this.payto = payto;
		this.paidbyid = paidbyid;
		this.approvedby = approvedby;
		this.created = created;
		this.modifiedby = modifiedby;
		this.lastmodified = lastmodified;
		this.ispaycheque = ispaycheque;
		this.isreversed = isreversed;
		this.chequetype = chequetype;
		this.voucherheader = voucherheader;
		this.bankname = bankname;
		this.type = type;
		this.chqstatus = chqstatus;
		this.detailTypeId = detailTypeId;
		this.detailKeyId = detailKeyId;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Bankbranch getBankbranch() {
		return this.bankbranch;
	}

	public void setBankbranch(Bankbranch bankbranch) {
		this.bankbranch = bankbranch;
	}

	public Bankaccount getBankaccount() {
		return this.bankaccount;
	}

	public void setBankaccount(Bankaccount bankaccount) {
		this.bankaccount = bankaccount;
	}

	public Bank getBank() {
		return this.bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public Date getChequedate() {
		return this.chequedate;
	}

	public void setChequedate(Date chequedate) {
		this.chequedate = chequedate;
	}

	public String getChequenumber() {
		return this.chequenumber;
	}

	public void setChequenumber(String chequenumber) {
		this.chequenumber = chequenumber;
	}

	public Date getReceiptdate() {
		return this.receiptdate;
	}

	public void setReceiptdate(Date receiptdate) {
		this.receiptdate = receiptdate;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getRegionid() {
		return this.regionid;
	}

	public void setRegionid(BigDecimal regionid) {
		this.regionid = regionid;
	}

	public boolean isIsdeposited() {
		return this.isdeposited;
	}

	public void setIsdeposited(boolean isdeposited) {
		this.isdeposited = isdeposited;
	}

	public String getPayinslipnumber() {
		return this.payinslipnumber;
	}

	public void setPayinslipnumber(String payinslipnumber) {
		this.payinslipnumber = payinslipnumber;
	}

	public Date getPayinslipdate() {
		return this.payinslipdate;
	}

	public void setPayinslipdate(Date payinslipdate) {
		this.payinslipdate = payinslipdate;
	}

	public String getNarration() {
		return this.narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getPayto() {
		return this.payto;
	}

	public void setPayto(String payto) {
		this.payto = payto;
	}

	public BigDecimal getPaidbyid() {
		return this.paidbyid;
	}

	public void setPaidbyid(BigDecimal paidbyid) {
		this.paidbyid = paidbyid;
	}

	public String getApprovedby() {
		return this.approvedby;
	}

	public void setApprovedby(String approvedby) {
		this.approvedby = approvedby;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public BigDecimal getModifiedby() {
		return this.modifiedby;
	}

	public void setModifiedby(BigDecimal modifiedby) {
		this.modifiedby = modifiedby;
	}

	public Date getLastmodified() {
		return this.lastmodified;
	}

	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	public Boolean getIspaycheque() {
		return this.ispaycheque;
	}

	public void setIspaycheque(Boolean ispaycheque) {
		this.ispaycheque = ispaycheque;
	}

	public Boolean getIsreversed() {
		return this.isreversed;
	}

	public void setIsreversed(Boolean isreversed) {
		this.isreversed = isreversed;
	}

	public Character getChequetype() {
		return this.chequetype;
	}

	public void setChequetype(Character chequetype) {
		this.chequetype = chequetype;
	}

	public String getBankname() {
		return this.bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public CVoucherHeader getVoucherheader() {
		return voucherheader;
	}

	public void setVoucherheader(CVoucherHeader voucherheader) {
		this.voucherheader = voucherheader;
	}

	public BigDecimal getChqstatus() {
		return chqstatus;
	}

	public void setChqstatus(BigDecimal chqstatus) {
		this.chqstatus = chqstatus;
	}

	public Integer getDetailKeyId() {
		return detailKeyId;
	}

	public void setDetailKeyId(Integer detailKeyId) {
		this.detailKeyId = detailKeyId;
	}

	public Integer getDetailTypeId() {
		return detailTypeId;
	}

	public void setDetailTypeId(Integer detailTypeId) {
		this.detailTypeId = detailTypeId;
	}

}
