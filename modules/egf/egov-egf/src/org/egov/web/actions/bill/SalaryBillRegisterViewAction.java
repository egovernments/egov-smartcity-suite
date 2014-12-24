package org.egov.web.actions.bill;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.bills.EgBillregister;
import org.egov.web.actions.BaseFormAction;

public class SalaryBillRegisterViewAction extends BaseFormAction{
	private Date fromDate; 
	private Date toDate;
	private BigDecimal month;
	private DepartmentImpl department;
	private List<EgBillregister> billRegisterList = new ArrayList<EgBillregister>();
	
	public SalaryBillRegisterViewAction() {
		addRelatedEntity("departmentList", DepartmentImpl.class);
	}
	
	@Override
	public void prepare() {
		super.prepare();
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		addDropdownData("departmentList", masterCache.get("egi-department"));
	}
	
	@Override
	public String execute() throws Exception {
		return "search";
	}
	
	public String ajaxSearch(){
		if(department.getId()!= -1 && !(new BigDecimal("-1").equals(month)))
			billRegisterList.addAll(persistenceService.findAllBy("from EgBillregister where billdate<=? and billdate>=? and egBillregistermis.egDepartment.id=? and egBillregistermis.month=? order by billdate", toDate,fromDate,department.getId(),month));
		else if(department.getId()== -1 && !(new BigDecimal("-1").equals(month)))
			billRegisterList.addAll(persistenceService.findAllBy("from EgBillregister where billdate<=? and billdate>=? and egBillregistermis.month=? order by billdate", toDate,fromDate,month));
		else if(department.getId()!= -1 && (new BigDecimal("-1").equals(month)))
			billRegisterList.addAll(persistenceService.findAllBy("from EgBillregister where billdate<=? and billdate>=? and egBillregistermis.egDepartment.id=? order by billdate", toDate,fromDate,department.getId()));
		else
			billRegisterList.addAll(persistenceService.findAllBy("from EgBillregister where billdate<=? and billdate>=? order by billdate", toDate,fromDate));
		return "result";
	}
	
	@Override
	public Object getModel() {
		return null;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setMonth(BigDecimal month) {
		this.month = month;
	}
	public BigDecimal getMonth() {
		return month;
	}
	public void setDepartment(DepartmentImpl department) {
		this.department = department;
	}
	public DepartmentImpl getDepartment() {
		return department;
	}

	public void setBillRegisterList(List<EgBillregister> billRegisterList) {
		this.billRegisterList = billRegisterList;
	}

	public List<EgBillregister> getBillRegisterList() {
		return billRegisterList;
	}

}
