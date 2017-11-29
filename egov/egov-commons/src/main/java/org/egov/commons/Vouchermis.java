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
package org.egov.commons;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import static org.egov.commons.Vouchermis.SEQ_VOUCHERMIS;

@Entity
@Table(name = "VOUCHERMIS")
@SequenceGenerator(name = SEQ_VOUCHERMIS, sequenceName = SEQ_VOUCHERMIS, allocationSize = 1)
public class Vouchermis implements java.io.Serializable {

    public static final String SEQ_VOUCHERMIS = "SEQ_VOUCHERMIS";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = SEQ_VOUCHERMIS, strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundsourceid")
    private Fundsource fundsource;

    private Integer billnumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "divisionid")
    private Boundary divisionid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentid")
    private Department departmentid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schemeid")
    private Scheme schemeid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subschemeid")
    private SubScheme subschemeid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "functionaryid")
    private Functionary functionary;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucherheaderid", nullable = true)
    private CVoucherHeader voucherheaderid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "functionid")
    private CFunction function;
    private String sourcePath;
    @Column(name = "budgetary_appnumber")
    private String budgetaryAppnumber;
    private Boolean budgetCheckReq = true;

    public Vouchermis() {
    }

    public Vouchermis(Long id) {
        this.id = id;
    }

    public Boolean isBudgetCheckReq() {
        return budgetCheckReq;
    }

    public void setBudgetCheckReq(Boolean budgetCheckReq) {
        this.budgetCheckReq = budgetCheckReq;
    }

    public Functionary getFunctionary() {
        return functionary;
    }

    public void setFunctionary(Functionary functionary) {
        this.functionary = functionary;
    }

    /*
     * public Vouchermis(Integer id, Fundsource fundsource, Integer billnumber, Integer divisionid, String schemename, String
     * accountcode, String accounthead, String contractamt, String cashbook, String natureofwork, String assetdesc, String
     * userdept, String demandno, String narration, String currentyear, Integer departmentid, String deptacchead, String
     * subaccounthead, Integer projectcode, String concurrancePn, Integer zonecode, Integer wardcode, Integer divisioncode,
     * Integer month, String grossded, String emdSecurity, String netdeduction, String netamt, String totexpenditure, Integer
     * voucherheaderid, String billregisterid, Integer acountDepartment, Integer projectfund, Short concurranceSn, Integer
     * segmentid, Integer subSegmentid, Date updatedtimestamp, Date createtimestamp, String iutStatus, String iutNumber, Integer
     * schemeid, Integer subschemeid,Functionary functionary) { this.id = id; this.fundsource = fundsource; this.billnumber =
     * billnumber; this.divisionid = divisionid; this.schemename = schemename; this.accountcode = accountcode; this.accounthead =
     * accounthead; this.contractamt = contractamt; this.cashbook = cashbook; this.natureofwork = natureofwork; this.assetdesc =
     * assetdesc; this.userdept = userdept; this.demandno = demandno; this.narration = narration; this.currentyear = currentyear;
     * this.departmentid = departmentid; this.deptacchead = deptacchead; this.subaccounthead = subaccounthead; this.projectcode =
     * projectcode; this.concurrancePn = concurrancePn; this.zonecode = zonecode; this.wardcode = wardcode; this.divisioncode =
     * divisioncode; this.month = month; this.grossded = grossded; this.emdSecurity = emdSecurity; this.netdeduction =
     * netdeduction; this.netamt = netamt; this.totexpenditure = totexpenditure; this.voucherheaderid = voucherheaderid;
     * this.billregisterid = billregisterid; this.acountDepartment = acountDepartment; this.projectfund = projectfund;
     * this.concurranceSn = concurranceSn; this.segmentid = segmentid; this.subSegmentid = subSegmentid; this.updatedtimestamp =
     * updatedtimestamp; this.createtimestamp = createtimestamp; this.iutStatus = iutStatus; this.iutNumber = iutNumber;
     * this.schemeid = schemeid; this.subschemeid = subschemeid; this.functionary=functionary; }
     */

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fundsource getFundsource() {
        return this.fundsource;
    }

    public void setFundsource(Fundsource fundsource) {
        this.fundsource = fundsource;
    }

    public Integer getBillnumber() {
        return this.billnumber;
    }

    public void setBillnumber(Integer billnumber) {
        this.billnumber = billnumber;
    }

    public Boundary getDivisionid() {
        return this.divisionid;
    }

    public void setDivisionid(Boundary divisionid) {
        this.divisionid = divisionid;
    }

    public Department getDepartmentid() {
        return this.departmentid;
    }

    public void setDepartmentid(Department departmentid) {
        this.departmentid = departmentid;
    }

    public Scheme getSchemeid() {
        return this.schemeid;
    }

    public void setSchemeid(Scheme schemeid) {
        this.schemeid = schemeid;
    }

    public SubScheme getSubschemeid() {
        return this.subschemeid;
    }

    public void setSubschemeid(SubScheme subschemeid) {
        this.subschemeid = subschemeid;
    }

    public CVoucherHeader getVoucherheaderid() {
        return voucherheaderid;
    }

    public void setVoucherheaderid(CVoucherHeader voucherheaderid) {
        this.voucherheaderid = voucherheaderid;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getBudgetaryAppnumber() {
        return budgetaryAppnumber;
    }

    public void setBudgetaryAppnumber(String appnumber) {
        this.budgetaryAppnumber = appnumber;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(CFunction function) {
        this.function = function;
    }
}
