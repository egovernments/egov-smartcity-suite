package org.egov.model.advance;

import java.util.Date;

import org.egov.commons.CVoucherHeader;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;


public class EgAdvanceRequisitionMis implements java.io.Serializable {

	private Long id;
	private BoundaryImpl fieldId;
	private BoundaryImpl subFieldId;
	private DepartmentImpl egDepartment;
	private Date lastupdatedtime;
	private Scheme scheme;
	private SubScheme subScheme;
	private CVoucherHeader voucherheader;
	private EgAdvanceRequisition egAdvanceRequisition; 
	private Fundsource fundsource;
	private Fund fund;
	private Functionary functionaryId;
	private String payto;
	private Date paybydate;
	private String referencenumber;
	private String sourcePath;
	private String partyBillNumber;
	private Date partyBillDate;
	
	public EgAdvanceRequisitionMis() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BoundaryImpl getFieldId() {
		return fieldId;
	}
	public void setFieldId(BoundaryImpl fieldId) {
		this.fieldId = fieldId;
	}
	public BoundaryImpl getSubFieldId() {
		return subFieldId;
	}
	public void setSubFieldId(BoundaryImpl subFieldId) {
		this.subFieldId = subFieldId;
	}
	public DepartmentImpl getEgDepartment() {
		return egDepartment;
	}
	public void setEgDepartment(DepartmentImpl egDepartment) {
		this.egDepartment = egDepartment;
	}
	public Date getLastupdatedtime() {
		return lastupdatedtime;
	}
	public void setLastupdatedtime(Date lastupdatedtime) {
		this.lastupdatedtime = lastupdatedtime;
	}
	public Scheme getScheme() {
		return scheme;
	}
	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
	}
	public SubScheme getSubScheme() {
		return subScheme;
	}
	public void setSubScheme(SubScheme subScheme) {
		this.subScheme = subScheme;
	}
	public CVoucherHeader getVoucherheader() {
		return voucherheader;
	}
	public void setVoucherheader(CVoucherHeader voucherheader) {
		this.voucherheader = voucherheader;
	}
	public EgAdvanceRequisition getEgAdvanceRequisition() {
		return egAdvanceRequisition;
	}
	public void setEgAdvanceRequisition(EgAdvanceRequisition egAdvanceRequisition) {
		this.egAdvanceRequisition = egAdvanceRequisition;
	}
	public Fundsource getFundsource() {
		return fundsource;
	}
	public void setFundsource(Fundsource fundsource) {
		this.fundsource = fundsource;
	}
	public Fund getFund() {
		return fund;
	}
	public void setFund(Fund fund) {
		this.fund = fund;
	}
	public Functionary getFunctionaryId() {
		return functionaryId;
	}
	public void setFunctionaryId(Functionary functionaryId) {
		this.functionaryId = functionaryId;
	}
	public String getPayto() {
		return payto;
	}
	public void setPayto(String payto) {
		this.payto = payto;
	}
	public Date getPaybydate() {
		return paybydate;
	}
	public void setPaybydate(Date paybydate) {
		this.paybydate = paybydate;
	}
	public String getReferencenumber() {
		return referencenumber;
	}
	public void setReferencenumber(String referencenumber) {
		this.referencenumber = referencenumber;
	}
	public String getSourcePath() {
		return sourcePath;
	}
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	public String getPartyBillNumber() {
		return partyBillNumber;
	}
	public void setPartyBillNumber(String partyBillNumber) {
		this.partyBillNumber = partyBillNumber;
	}
	public Date getPartyBillDate() {
		return partyBillDate;
	}
	public void setPartyBillDate(Date partyBillDate) {
		this.partyBillDate = partyBillDate;
	}
	public EgAdvanceRequisitionMis(Long id, BoundaryImpl fieldId,
			BoundaryImpl subFieldId, DepartmentImpl egDepartment,
			Date lastupdatedtime, Scheme scheme, SubScheme subScheme,
			CVoucherHeader voucherheader,
			EgAdvanceRequisition egAdvanceRequisition, Fundsource fundsource,
			Fund fund, Functionary functionaryId, String payto, Date paybydate,
			String referencenumber, String sourcePath, String partyBillNumber,
			Date partyBillDate) {
		super();
		this.id = id;
		this.fieldId = fieldId;
		this.subFieldId = subFieldId;
		this.egDepartment = egDepartment;
		this.lastupdatedtime = lastupdatedtime;
		this.scheme = scheme;
		this.subScheme = subScheme;
		this.voucherheader = voucherheader;
		this.egAdvanceRequisition = egAdvanceRequisition;
		this.fundsource = fundsource;
		this.fund = fund;
		this.functionaryId = functionaryId;
		this.payto = payto;
		this.paybydate = paybydate;
		this.referencenumber = referencenumber;
		this.sourcePath = sourcePath;
		this.partyBillNumber = partyBillNumber;
		this.partyBillDate = partyBillDate;
	}
	
}
