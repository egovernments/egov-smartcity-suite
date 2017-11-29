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
package org.egov.model.bills;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.entity.AbstractPersistable;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "EG_BILLREGISTERMIS")
@SequenceGenerator(name = EgBillregistermis.SEQ_EG_BILLREGISTERMIS, sequenceName = EgBillregistermis.SEQ_EG_BILLREGISTERMIS, allocationSize = 1)
public class EgBillregistermis extends AbstractPersistable<Integer> implements java.io.Serializable {

    private static final long serialVersionUID = -4947159761135531623L;

    public static final String SEQ_EG_BILLREGISTERMIS = "SEQ_EG_BILLREGISTERMIS";

    @Id
    @GeneratedValue(generator = SEQ_EG_BILLREGISTERMIS, strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "billid")
    private EgBillregister egBillregister;

    private BigDecimal segmentid;

    private BigDecimal subsegmentid;

    private Date paybydate;

    @ManyToOne
    @JoinColumn(name = "fieldid")
    private Boundary fieldid;

    private BigDecimal subfieldid;

    @ManyToOne
    @JoinColumn(name = "functionaryid")
    private Functionary functionaryid;

    @Length(max = 30)
    private String sanctionedby;

    private Date sanctiondate;

    @Length(max = 200)
    private String sanctiondetail;

    @Length(max = 1024)
    private String narration;

    private Date lastupdatedtime;

    @Length(max = 30)
    private String disbursementtype;

    private BigDecimal escalation;

    private BigDecimal advancepayments;

    private BigDecimal securedadvances;

    private BigDecimal deductamountwitheld;

    private BigDecimal month;

    @ManyToOne
    @JoinColumn(name = "departmentid")
    private Department egDepartment;

    @ManyToOne
    @JoinColumn(name = "financialyearid")
    private CFinancialYear financialyear;

    @ManyToOne
    @JoinColumn(name = "fundsourceid")
    private Fundsource fundsource;

    @ManyToOne
    @JoinColumn(name = "fundid")
    private Fund fund;

    @ManyToOne
    @JoinColumn(name = "billsubtype")
    private EgBillSubType egBillSubType;

    @Length(max = 350)
    private String payto;

    private String mbRefNo;

    @ManyToOne
    @JoinColumn(name = "functionid")
    private CFunction function;

    @ManyToOne
    @JoinColumn(name = "schemeid")
    private Scheme scheme;

    @ManyToOne
    @JoinColumn(name = "subschemeid")
    private SubScheme subScheme;

    @ManyToOne
    @JoinColumn(name = "voucherheaderid")
    private CVoucherHeader voucherHeader;

    @Length(max = 150)
    private String sourcePath;

    @Length(max = 50)
    private String partyBillNumber;

    private Date partyBillDate;

    @Length(max = 50)
    private String inwardSerialNumber;

    @Length(max = 30)
    @Column(name = "budgetary_appnumber")
    private String budgetaryAppnumber;

    @Transient
    private Long schemeId;

    @Transient
    private Long subSchemeId;

    private Boolean budgetCheckReq = true;

    public Boolean isBudgetCheckReq() {
        return budgetCheckReq;
    }

    public void setBudgetCheckReq(final Boolean budgetCheckReq) {
        this.budgetCheckReq = budgetCheckReq;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(final String sourcePath) {
        this.sourcePath = sourcePath;
    }

    /**
     * @return the mbRefNo
     */
    public String getMbRefNo() {
        return mbRefNo;
    }

    /**
     * @param mbRefNo the mbRefNo to set
     */
    public void setMbRefNo(final String mbRefNo) {
        this.mbRefNo = mbRefNo;
    }

    public EgBillregistermis() {
    }

    public EgBillregistermis(final Integer id, final EgBillregister egBillregister,
            final Date lastupdatedtime, final Date paybydate) {
        this.id = id;
        this.egBillregister = egBillregister;
        this.lastupdatedtime = lastupdatedtime;
        this.paybydate = paybydate;
    }

    public EgBillregistermis(final Integer id, final EgBillregister egBillregister, final CFunction function,
            final Fund fundid, final BigDecimal segmentid, final BigDecimal subsegmentid,
            final Boundary fieldid, final BigDecimal subfieldid,
            final Functionary functionaryid, final String sanctionedby, final Date sanctiondate,
            final String sanctiondetail, final String narration, final Date lastupdatedtime,
            final String disbursementtype, final BigDecimal escalation,
            final BigDecimal advancepayments, final BigDecimal securedadvances,
            final BigDecimal deductamountwitheld, final Department departmentid,
            final BigDecimal month, final CFinancialYear financialyear,
            final Fundsource fundsource, final Date paybydate, final EgBillSubType egBillSubtype,
            final String ptyBillNumber, final Date ptyBillDate, final String inwrdSlNumber) {
        this.id = id;
        this.egBillregister = egBillregister;
        fund = fundid;
        this.function = function;
        this.segmentid = segmentid;
        this.subsegmentid = subsegmentid;
        this.fieldid = fieldid;
        this.subfieldid = subfieldid;
        this.functionaryid = functionaryid;
        this.sanctionedby = sanctionedby;
        this.sanctiondate = sanctiondate;
        this.sanctiondetail = sanctiondetail;
        this.narration = narration;
        this.lastupdatedtime = lastupdatedtime;
        this.disbursementtype = disbursementtype;
        this.escalation = escalation;
        this.advancepayments = advancepayments;
        this.securedadvances = securedadvances;
        this.deductamountwitheld = deductamountwitheld;
        egDepartment = departmentid;
        this.month = month;
        this.financialyear = financialyear;
        this.fundsource = fundsource;
        this.paybydate = paybydate;
        egBillSubType = egBillSubtype;
        partyBillNumber = ptyBillNumber;
        partyBillDate = ptyBillDate;
        inwardSerialNumber = inwrdSlNumber;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(final Integer id) {
        this.id = id;
    }

    public EgBillregister getEgBillregister() {
        return egBillregister;
    }

    public void setEgBillregister(final EgBillregister egBillregister) {
        this.egBillregister = egBillregister;
    }

    public BigDecimal getSegmentid() {
        return segmentid;
    }

    public void setSegmentid(final BigDecimal segmentid) {
        this.segmentid = segmentid;
    }

    public BigDecimal getSubsegmentid() {
        return subsegmentid;
    }

    public void setSubsegmentid(final BigDecimal subsegmentid) {
        this.subsegmentid = subsegmentid;
    }

    public Boundary getFieldid() {
        return fieldid;
    }

    public void setFieldid(final Boundary fieldid) {
        this.fieldid = fieldid;
    }

    public BigDecimal getSubfieldid() {
        return subfieldid;
    }

    public void setSubfieldid(final BigDecimal subfieldid) {
        this.subfieldid = subfieldid;
    }

    public Functionary getFunctionaryid() {
        return functionaryid;
    }

    public void setFunctionaryid(final Functionary functionaryid) {
        this.functionaryid = functionaryid;
    }

    public String getSanctionedby() {
        return sanctionedby;
    }

    public void setSanctionedby(final String sanctionedby) {
        this.sanctionedby = sanctionedby;
    }

    public Date getSanctiondate() {
        return sanctiondate;
    }

    public void setSanctiondate(final Date sanctiondate) {
        this.sanctiondate = sanctiondate;
    }

    public String getSanctiondetail() {
        return sanctiondetail;
    }

    public void setSanctiondetail(final String sanctiondetail) {
        this.sanctiondetail = sanctiondetail;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(final String narration) {
        this.narration = narration;
    }

    public Date getLastupdatedtime() {
        return lastupdatedtime;
    }

    public void setLastupdatedtime(final Date lastupdatedtime) {
        this.lastupdatedtime = lastupdatedtime;
    }

    public String getDisbursementtype() {
        return disbursementtype;
    }

    public void setDisbursementtype(final String disbursementtype) {
        this.disbursementtype = disbursementtype;
    }

    public BigDecimal getEscalation() {
        return escalation;
    }

    public void setEscalation(final BigDecimal escalation) {
        this.escalation = escalation;
    }

    public BigDecimal getAdvancepayments() {
        return advancepayments;
    }

    public void setAdvancepayments(final BigDecimal advancepayments) {
        this.advancepayments = advancepayments;
    }

    public BigDecimal getSecuredadvances() {
        return securedadvances;
    }

    public void setSecuredadvances(final BigDecimal securedadvances) {
        this.securedadvances = securedadvances;
    }

    public BigDecimal getDeductamountwitheld() {
        return deductamountwitheld;
    }

    public void setDeductamountwitheld(final BigDecimal deductamountwitheld) {
        this.deductamountwitheld = deductamountwitheld;
    }

    public BigDecimal getMonth() {
        return month;
    }

    public void setMonth(final BigDecimal month) {
        this.month = month;
    }

    public Department getEgDepartment() {
        return egDepartment;
    }

    public void setEgDepartment(final Department egDepartment) {
        this.egDepartment = egDepartment;
    }

    public CFinancialYear getFinancialyear() {
        return financialyear;
    }

    public void setFinancialyear(final CFinancialYear financialyear) {
        this.financialyear = financialyear;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public Fundsource getFundsource() {
        return fundsource;
    }

    public void setFundsource(final Fundsource fundsource) {
        this.fundsource = fundsource;
    }

    public Date getPaybydate() {
        return paybydate;
    }

    public void setPaybydate(final Date paybydate) {
        this.paybydate = paybydate;
    }

    public String getPayto() {
        return payto;
    }

    public void setPayto(final String payto) {
        this.payto = payto;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(final Scheme scheme) {
        this.scheme = scheme;
    }

    public SubScheme getSubScheme() {
        return subScheme;
    }

    public void setSubScheme(final SubScheme subScheme) {
        this.subScheme = subScheme;
    }

    public CVoucherHeader getVoucherHeader() {
        return voucherHeader;
    }

    public void setVoucherHeader(final CVoucherHeader voucherHeader) {
        this.voucherHeader = voucherHeader;
    }

    public EgBillSubType getEgBillSubType() {
        return egBillSubType;
    }

    public void setEgBillSubType(final EgBillSubType egBillSubType) {
        this.egBillSubType = egBillSubType;
    }

    public String getPartyBillNumber() {
        return partyBillNumber;
    }

    public void setPartyBillNumber(final String partyBillNumber) {
        this.partyBillNumber = partyBillNumber;
    }

    public Date getPartyBillDate() {
        return partyBillDate;
    }

    public void setPartyBillDate(final Date partyBillDate) {
        this.partyBillDate = partyBillDate;
    }

    public String getInwardSerialNumber() {
        return inwardSerialNumber;
    }

    public void setInwardSerialNumber(final String inwardSerialNumber) {
        this.inwardSerialNumber = inwardSerialNumber;
    }

    public String getBudgetaryAppnumber() {
        return budgetaryAppnumber;
    }

    public void setBudgetaryAppnumber(final String budgetaryAppnumber) {
        this.budgetaryAppnumber = budgetaryAppnumber;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

    public Long getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(final Long schemeId) {
        this.schemeId = schemeId;
    }

    public Long getSubSchemeId() {
        return subSchemeId;
    }

    public void setSubSchemeId(final Long subSchemeId) {
        this.subSchemeId = subSchemeId;
    }

}
