/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.model.brs;

import org.egov.commons.Bankaccount;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CVoucherHeader;
import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.model.instrument.InstrumentHeader;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "BANKENTRIES")
@SequenceGenerator(name = BrsEntries.SEQ_BANKENTRIES, sequenceName = BrsEntries.SEQ_BANKENTRIES, allocationSize = 1)
public class BrsEntries extends AbstractPersistable<Long>
{
    private static final long serialVersionUID = 8924403787977830824L;

    public static final String SEQ_BANKENTRIES = "SEQ_BANKENTRIES";

    @Id
    @GeneratedValue(generator = SEQ_BANKENTRIES, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Length(max = 20)
    private String refNo;

    @Length(max = 20)
    private String type;

    private Date txnDate;

    private BigDecimal txnAmount;

    @Length(max = 100)
    private String remarks;
    @ManyToOne
    @JoinColumn(name = "glcodeid")
    private CChartOfAccounts glCodeId;

    @ManyToOne
    @JoinColumn(name = "instrumentheaderid")
    private InstrumentHeader instrumentHeaderId;

    @ManyToOne
    @JoinColumn(name = "voucherheaderid")
    private CVoucherHeader voucherHeaderId;

    @ManyToOne
    @JoinColumn(name = "bankaccountid")
    private Bankaccount bankaccountId;

    private Integer isreversed;
    
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bankentries", targetEntity = BrsEntrieMis.class)
    private Set<BrsEntrieMis> bankentriesMis = new HashSet<BrsEntrieMis>(0);


    // added for DishonoredCheque
    @Transient
    private String voucherNumber = "";
    @Transient
    private String cgnum = "";
    @Transient
    private String payinSlipVHeaderId = "";
    @Transient
    private String fundId = "";
    @Transient
    private String fundSourceId = "";
    @Transient
    private String chequeNumber = "";
    @Transient
    private String chequeDate = "";
    @Transient
    private String amount = "";
    @Transient
    private String lotNumber = "";
    @Transient
    private String lotType = "";
    @Transient
    private String field = "";
    @Transient
    private String voucherType = "";
    @Transient
    private String bankName = "";
    @Transient
    private String accNumber = "";
    @Transient
    private String accIdParam = "";
    @Transient
    private String payTo = "";
    @Transient
    private String payCheque = "";
    @Transient
    private String departmentId;
    @Transient
    private String functionaryId;
    @Transient
    private String functionId = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(Date txnDate) {
        this.txnDate = txnDate;
    }

    public BigDecimal getTxnAmount() {
        return txnAmount;
    }

    public void setTxnAmount(BigDecimal txnAmount) {
        this.txnAmount = txnAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public CChartOfAccounts getGlCodeId() {
        return glCodeId;
    }

    public void setGlCodeId(CChartOfAccounts glCodeId) {
        this.glCodeId = glCodeId;
    }

    public InstrumentHeader getInstrumentHeaderId() {
        return instrumentHeaderId;
    }

    public void setInstrumentHeaderId(InstrumentHeader instrumentHeaderId) {
        this.instrumentHeaderId = instrumentHeaderId;
    }

    public CVoucherHeader getVoucherHeaderId() {
        return voucherHeaderId;
    }

    public void setVoucherHeaderId(CVoucherHeader voucherHeaderId) {
        this.voucherHeaderId = voucherHeaderId;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public String getCgnum() {
        return cgnum;
    }

    public void setCgnum(String cgnum) {
        this.cgnum = cgnum;
    }

    public String getPayinSlipVHeaderId() {
        return payinSlipVHeaderId;
    }

    public void setPayinSlipVHeaderId(String payinSlipVHeaderId) {
        this.payinSlipVHeaderId = payinSlipVHeaderId;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getFundSourceId() {
        return fundSourceId;
    }

    public void setFundSourceId(String fundSourceId) {
        this.fundSourceId = fundSourceId;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getLotType() {
        return lotType;
    }

    public void setLotType(String lotType) {
        this.lotType = lotType;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public String getAccIdParam() {
        return accIdParam;
    }

    public void setAccIdParam(String accIdParam) {
        this.accIdParam = accIdParam;
    }

    public String getPayTo() {
        return payTo;
    }

    public void setPayTo(String payTo) {
        this.payTo = payTo;
    }

    public String getPayCheque() {
        return payCheque;
    }

    public void setPayCheque(String payCheque) {
        this.payCheque = payCheque;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getFunctionaryId() {
        return functionaryId;
    }

    public void setFunctionaryId(String functionaryId) {
        this.functionaryId = functionaryId;
    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public Bankaccount getBankaccountId() {
        return bankaccountId;
    }

    public void setBankaccountId(Bankaccount bankaccountId) {
        this.bankaccountId = bankaccountId;
    }

    public Integer getIsreversed() {
        return isreversed;
    }

    public void setIsreversed(Integer isreversed) {
        this.isreversed = isreversed;
    }

    public Set<BrsEntrieMis> getBankentriesMis() {
        return bankentriesMis;
    }

    public void setBankentriesMis(Set<BrsEntrieMis> bankentriesMis) {
        this.bankentriesMis = bankentriesMis;
    }

}
