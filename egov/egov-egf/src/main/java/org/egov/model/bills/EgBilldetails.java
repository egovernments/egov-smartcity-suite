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

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.egov.commons.CChartOfAccounts;
import org.egov.infra.persistence.entity.AbstractPersistable;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EG_BILLDETAILS")
@SequenceGenerator(name = EgBilldetails.SEQ_EG_BILLDETAILS, sequenceName = EgBilldetails.SEQ_EG_BILLDETAILS, allocationSize = 1)
public class EgBilldetails extends AbstractPersistable<Integer> implements java.io.Serializable {

    private static final long serialVersionUID = -6045669915919744421L;
    public static final String SEQ_EG_BILLDETAILS = "SEQ_EG_BILLDETAILS";

    @Id
    @GeneratedValue(generator = SEQ_EG_BILLDETAILS, strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "billid")
    private EgBillregister egBillregister;

    private BigDecimal functionid;

    private BigDecimal glcodeid;

    private BigDecimal debitamount;

    private BigDecimal creditamount;

    private Date lastupdatedtime;

    @Length(max = 250)
    private String narration;

    @Transient
    private CChartOfAccounts chartOfAccounts;

    @OrderBy("id")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "egBilldetailsId", targetEntity = EgBillPayeedetails.class)
    private Set<EgBillPayeedetails> egBillPaydetailes = new HashSet<EgBillPayeedetails>(0);

    public EgBilldetails() {
    }

    public EgBilldetails(final Integer id, final EgBillregister egBillregister,
            final BigDecimal glcodeid, final Date lastupdatedtime) {
        this.id = id;
        this.egBillregister = egBillregister;
        this.glcodeid = glcodeid;
        this.lastupdatedtime = lastupdatedtime;
    }

    public EgBilldetails(final Integer id, final EgBillregister egBillregister,
            final BigDecimal functionid, final BigDecimal glcodeid, final BigDecimal debitamount,
            final BigDecimal creditamount, final Date lastupdatedtime, final Set<EgBillPayeedetails> egBillPaydetailes,
            final String narration) {
        this.id = id;
        this.egBillregister = egBillregister;
        this.functionid = functionid;
        this.glcodeid = glcodeid;
        this.debitamount = debitamount;
        this.creditamount = creditamount;
        this.lastupdatedtime = lastupdatedtime;
        this.egBillPaydetailes = egBillPaydetailes;
        this.narration = narration;
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

    public BigDecimal getFunctionid() {
        return functionid;
    }

    public void setFunctionid(final BigDecimal functionid) {
        this.functionid = functionid;
    }

    public BigDecimal getGlcodeid() {
        return glcodeid;
    }

    public void setGlcodeid(final BigDecimal glcodeid) {
        this.glcodeid = glcodeid;
    }

    public BigDecimal getDebitamount() {
        return debitamount;
    }

    public void setDebitamount(final BigDecimal debitamount) {
        this.debitamount = debitamount;
    }

    public BigDecimal getCreditamount() {
        return creditamount;
    }

    public void setCreditamount(final BigDecimal creditamount) {
        this.creditamount = creditamount;
    }

    public Date getLastupdatedtime() {
        return lastupdatedtime;
    }

    public void setLastupdatedtime(final Date lastupdatedtime) {
        this.lastupdatedtime = lastupdatedtime;
    }

    public Set<EgBillPayeedetails> getEgBillPaydetailes() {
        return egBillPaydetailes;
    }

    public void setEgBillPaydetailes(final Set<EgBillPayeedetails> egBillPaydetailes) {
        this.egBillPaydetailes = egBillPaydetailes;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(final String narration) {
        this.narration = narration;
    }

    public void addEgBillPayeedetail(final EgBillPayeedetails egbillpayee) {
        if (egbillpayee != null)
            getEgBillPaydetailes().add(egbillpayee);
    }

    public void removeEgBillPayeedetail(final EgBillPayeedetails egbillpayee) {
        if (egbillpayee != null)
            getEgBillPaydetailes().remove(egbillpayee);
    }

    public CChartOfAccounts getChartOfAccounts() {
        return chartOfAccounts;
    }

    public void setChartOfAccounts(final CChartOfAccounts chartOfAccounts) {
        this.chartOfAccounts = chartOfAccounts;
    }

}
