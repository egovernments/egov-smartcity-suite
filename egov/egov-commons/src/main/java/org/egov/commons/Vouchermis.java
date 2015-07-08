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

import java.util.Date;

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

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.hibernate.search.annotations.DocumentId;
@Entity
@Table(name = "VOUCHERMIS")
@SequenceGenerator(name = Vouchermis.SEQ_VOUCHERMIS, sequenceName = Vouchermis.SEQ_VOUCHERMIS, allocationSize = 1)
public class Vouchermis implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String SEQ_VOUCHERMIS = "SEQ_VOUCHERMIS";

    @DocumentId
    @Id
    @GeneratedValue(generator = SEQ_VOUCHERMIS, strategy = GenerationType.SEQUENCE)
	private Long id;
    @ManyToOne
	@JoinColumn(name = "fundsourceid")
	private Fundsource fundsource;

	private Integer billnumber;
	@ManyToOne
	@JoinColumn(name = "divisionid")
	private Boundary divisionid;

	private String schemename;

	private String accountcode;

	private String accounthead;

	private String contractamt;

	private String cashbook;

	private String natureofwork;

	private String assetdesc;

	private String userdept;

	private String demandno;

	private String narration;

	private String currentyear;
	@ManyToOne
	@JoinColumn(name = "departmentid")
	private Department departmentid;

	private String deptacchead;

	private String subaccounthead;

	private Integer projectcode;
	@Column(name="concurrance_pn")
	private String concurrancePn;

	private Integer zonecode;

	private Integer wardcode;

	private Integer divisioncode;

	private Integer month;

	private String grossded;
	@Column(name="emd_security")
	private String emdSecurity;

	private String netdeduction;

	private String netamt;

	private String totexpenditure;

	// private Integer voucherheaderid;

	private String billregisterid;
	@Column(name="acount_department")
	private Integer acountDepartment;

	private Integer projectfund;
	@Column(name="concurrance_sn")
	private Short concurranceSn;

	private Integer segmentid;
	@Column(name="sub_segmentid")
	private Integer subSegmentid;

	private Date updatedtimestamp;

	private Date createtimestamp;
	@Column(name="iut_status")
	private String iutStatus;
	@Column(name="iut_number")
	private String iutNumber;
	@ManyToOne
	@JoinColumn(name = "schemeid") 
	private Scheme schemeid;
	@ManyToOne
	@JoinColumn(name = "subschemeid")
	private SubScheme subschemeid;
	@ManyToOne
	@JoinColumn(name = "functionaryid")
	private Functionary functionary;
	@ManyToOne
	@JoinColumn(name = "voucherheaderid",nullable = true)
	private CVoucherHeader voucherheaderid;
	@ManyToOne
	@JoinColumn(name = "functionid")
	private CFunction function;
	private String sourcePath;
	@Column(name="budgetary_appnumber")
	private String budgetaryAppnumber;
	private Boolean budgetCheckReq = true;

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

	public Vouchermis() {
	}

	public Vouchermis(Long id) {
		this.id = id;
	}

	/*
	 * public Vouchermis(Integer id, Fundsource fundsource, Integer billnumber, Integer divisionid, String schemename, String accountcode, String accounthead, String contractamt, String cashbook, String natureofwork, String assetdesc, String userdept, String demandno, String narration, String
	 * currentyear, Integer departmentid, String deptacchead, String subaccounthead, Integer projectcode, String concurrancePn, Integer zonecode, Integer wardcode, Integer divisioncode, Integer month, String grossded, String emdSecurity, String netdeduction, String netamt, String totexpenditure,
	 * Integer voucherheaderid, String billregisterid, Integer acountDepartment, Integer projectfund, Short concurranceSn, Integer segmentid, Integer subSegmentid, Date updatedtimestamp, Date createtimestamp, String iutStatus, String iutNumber, Integer schemeid, Integer subschemeid,Functionary
	 * functionary) { this.id = id; this.fundsource = fundsource; this.billnumber = billnumber; this.divisionid = divisionid; this.schemename = schemename; this.accountcode = accountcode; this.accounthead = accounthead; this.contractamt = contractamt; this.cashbook = cashbook; this.natureofwork =
	 * natureofwork; this.assetdesc = assetdesc; this.userdept = userdept; this.demandno = demandno; this.narration = narration; this.currentyear = currentyear; this.departmentid = departmentid; this.deptacchead = deptacchead; this.subaccounthead = subaccounthead; this.projectcode = projectcode;
	 * this.concurrancePn = concurrancePn; this.zonecode = zonecode; this.wardcode = wardcode; this.divisioncode = divisioncode; this.month = month; this.grossded = grossded; this.emdSecurity = emdSecurity; this.netdeduction = netdeduction; this.netamt = netamt; this.totexpenditure = totexpenditure;
	 * this.voucherheaderid = voucherheaderid; this.billregisterid = billregisterid; this.acountDepartment = acountDepartment; this.projectfund = projectfund; this.concurranceSn = concurranceSn; this.segmentid = segmentid; this.subSegmentid = subSegmentid; this.updatedtimestamp = updatedtimestamp;
	 * this.createtimestamp = createtimestamp; this.iutStatus = iutStatus; this.iutNumber = iutNumber; this.schemeid = schemeid; this.subschemeid = subschemeid; this.functionary=functionary; }
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

	public String getSchemename() {
		return this.schemename;
	}

	public void setSchemename(String schemename) {
		this.schemename = schemename;
	}

	public String getAccountcode() {
		return this.accountcode;
	}

	public void setAccountcode(String accountcode) {
		this.accountcode = accountcode;
	}

	public String getAccounthead() {
		return this.accounthead;
	}

	public void setAccounthead(String accounthead) {
		this.accounthead = accounthead;
	}

	public String getContractamt() {
		return this.contractamt;
	}

	public void setContractamt(String contractamt) {
		this.contractamt = contractamt;
	}

	public String getCashbook() {
		return this.cashbook;
	}

	public void setCashbook(String cashbook) {
		this.cashbook = cashbook;
	}

	public String getNatureofwork() {
		return this.natureofwork;
	}

	public void setNatureofwork(String natureofwork) {
		this.natureofwork = natureofwork;
	}

	public String getAssetdesc() {
		return this.assetdesc;
	}

	public void setAssetdesc(String assetdesc) {
		this.assetdesc = assetdesc;
	}

	public String getUserdept() {
		return this.userdept;
	}

	public void setUserdept(String userdept) {
		this.userdept = userdept;
	}

	public String getDemandno() {
		return this.demandno;
	}

	public void setDemandno(String demandno) {
		this.demandno = demandno;
	}

	public String getNarration() {
		return this.narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getCurrentyear() {
		return this.currentyear;
	}

	public void setCurrentyear(String currentyear) {
		this.currentyear = currentyear;
	}

	public Department getDepartmentid() {
		return this.departmentid;
	}

	public void setDepartmentid(Department departmentid) {
		this.departmentid = departmentid;
	}

	public String getDeptacchead() {
		return this.deptacchead;
	}

	public void setDeptacchead(String deptacchead) {
		this.deptacchead = deptacchead;
	}

	public String getSubaccounthead() {
		return this.subaccounthead;
	}

	public void setSubaccounthead(String subaccounthead) {
		this.subaccounthead = subaccounthead;
	}

	public Integer getProjectcode() {
		return this.projectcode;
	}

	public void setProjectcode(Integer projectcode) {
		this.projectcode = projectcode;
	}

	public String getConcurrancePn() {
		return this.concurrancePn;
	}

	public void setConcurrancePn(String concurrancePn) {
		this.concurrancePn = concurrancePn;
	}

	public Integer getZonecode() {
		return this.zonecode;
	}

	public void setZonecode(Integer zonecode) {
		this.zonecode = zonecode;
	}

	public Integer getWardcode() {
		return this.wardcode;
	}

	public void setWardcode(Integer wardcode) {
		this.wardcode = wardcode;
	}

	public Integer getDivisioncode() {
		return this.divisioncode;
	}

	public void setDivisioncode(Integer divisioncode) {
		this.divisioncode = divisioncode;
	}

	public Integer getMonth() {
		return this.month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getGrossded() {
		return this.grossded;
	}

	public void setGrossded(String grossded) {
		this.grossded = grossded;
	}

	public String getEmdSecurity() {
		return this.emdSecurity;
	}

	public void setEmdSecurity(String emdSecurity) {
		this.emdSecurity = emdSecurity;
	}

	public String getNetdeduction() {
		return this.netdeduction;
	}

	public void setNetdeduction(String netdeduction) {
		this.netdeduction = netdeduction;
	}

	public String getNetamt() {
		return this.netamt;
	}

	public void setNetamt(String netamt) {
		this.netamt = netamt;
	}

	public String getTotexpenditure() {
		return this.totexpenditure;
	}

	public void setTotexpenditure(String totexpenditure) {
		this.totexpenditure = totexpenditure;
	}

	/*
	 * public Integer getVoucherheaderid() { return this.voucherheaderid; } public void setVoucherheaderid(Integer voucherheaderid) { this.voucherheaderid = voucherheaderid; }
	 */

	public String getBillregisterid() {
		return this.billregisterid;
	}

	public void setBillregisterid(String billregisterid) {
		this.billregisterid = billregisterid;
	}

	public Integer getAcountDepartment() {
		return this.acountDepartment;
	}

	public void setAcountDepartment(Integer acountDepartment) {
		this.acountDepartment = acountDepartment;
	}

	public Integer getProjectfund() {
		return this.projectfund;
	}

	public void setProjectfund(Integer projectfund) {
		this.projectfund = projectfund;
	}

	public Short getConcurranceSn() {
		return this.concurranceSn;
	}

	public void setConcurranceSn(Short concurranceSn) {
		this.concurranceSn = concurranceSn;
	}

	public Integer getSegmentid() {
		return this.segmentid;
	}

	public void setSegmentid(Integer segmentid) {
		this.segmentid = segmentid;
	}

	public Integer getSubSegmentid() {
		return this.subSegmentid;
	}

	public void setSubSegmentid(Integer subSegmentid) {
		this.subSegmentid = subSegmentid;
	}

	public Date getUpdatedtimestamp() {
		return this.updatedtimestamp;
	}

	public void setUpdatedtimestamp(Date updatedtimestamp) {
		this.updatedtimestamp = updatedtimestamp;
	}

	public Date getCreatetimestamp() {
		return this.createtimestamp;
	}

	public void setCreatetimestamp(Date createtimestamp) {
		this.createtimestamp = createtimestamp;
	}

	public String getIutStatus() {
		return this.iutStatus;
	}

	public void setIutStatus(String iutStatus) {
		this.iutStatus = iutStatus;
	}

	public String getIutNumber() {
		return this.iutNumber;
	}

	public void setIutNumber(String iutNumber) {
		this.iutNumber = iutNumber;
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
