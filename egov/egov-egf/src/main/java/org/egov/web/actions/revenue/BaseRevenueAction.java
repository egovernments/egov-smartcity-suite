package org.egov.web.actions.revenue;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;

import org.egov.egf.revenue.Grant;
import org.egov.infra.admin.master.entity.Department;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.web.actions.BaseFormAction;

public class BaseRevenueAction extends BaseFormAction {
	private static final long serialVersionUID = 1594209619636642478L;
	protected List<Grant> grantsList;
	protected List<Department> departmentList;
	protected List<CFinancialYear> finYearList;
	protected List<String> periodList;
	protected String grantsType;
	protected List<Grant> grantSearchList;
	protected String mode;
	protected Grant grant = new Grant();

	public BaseRevenueAction(){
		
	}
	
	@Override
	public Grant getModel() {
		
		return grant;
	}
	
	public void prepare()
	{
		this.mode="view";
		finYearList=persistenceService.findAllBy("from CFinancialYear  where isActiveForPosting=1 order by finYearRange DESC");
		departmentList=persistenceService.findAllBy("from Department order by deptName");
		//addDropdownData("finanYearList", persistenceService.findAllBy("from CFinancialYear  where isActiveForPosting=1 order by finYearRange "));
		
	}
	
	public String newForm(){
		this.mode="create";
		grantsList = new ArrayList<Grant>();
		grantsList.add(new Grant());
		return "new";
	}
	
	public String beforeModify(){
		StringBuffer query = new StringBuffer();
		query.append("From Grant gr where gr.financialYear.id=? and gr.grantType=? and gr.department.id=?");
		this.grantsList = persistenceService.findAllBy(query.toString(),grant.getFinancialYear().getId(),grant.getGrantType(),grant.getDepartment().getId());
		if(this.mode.equals("edit")){
			return "edit";
		}else{
			return "view";
		}
	}
	
	public String saveOrupdate(){
		//Grant gtr = grantsList.get(0);
		//persistenceService.setType(Grant.class);
		for(Grant gtr:grantsList){
			gtr.setDepartment((Department)persistenceService.find("from Department where id=?",gtr.getDepartment().getId()));
			gtr.setFinancialYear((CFinancialYear)persistenceService.find("from CFinancialYear where id=?",gtr.getFinancialYear().getId()));
			gtr.setAccrualVoucher((CVoucherHeader)persistenceService.find("from CVoucherHeader where id=?",gtr.getAccrualVoucher().getId()));
			if(gtr.getIhID().getId()!=null){
				gtr.setIhID((InstrumentHeader)persistenceService.find("from InstrumentHeader where id=?",gtr.getIhID().getId()));
			}
			else{
				gtr.setIhID(null);
			}
			if(gtr.getGeneralVoucher().getId()!=null){
				gtr.setGeneralVoucher((CVoucherHeader)persistenceService.find("from CVoucherHeader where id=?",gtr.getGeneralVoucher().getId()));
			}
			else{
				gtr.setGeneralVoucher(null);
			}
			if(gtr.getReceiptVoucher().getId()!=null){
				gtr.setReceiptVoucher((CVoucherHeader)persistenceService.find("from CVoucherHeader where id=?",gtr.getReceiptVoucher().getId()));
			}
			else{
				gtr.setReceiptVoucher(null);
			}
			gtr.setGrantType(getGrantsType());
		}
		persistenceService.setType(Grant.class);
		for(Grant gtr:grantsList){
			persistenceService.persist(gtr);
		}
		return "result";
	}
	
	public List<Grant> getGrantsList() {
		return grantsList;
	}

	public void setGrantsList(List<Grant> grantsList) {
		this.grantsList = grantsList;
	}

	public List<Department> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}

	public List<CFinancialYear> getFinYearList() {
		return finYearList;
	}

	public void setFinYearList(List<CFinancialYear> finYearList) {
		this.finYearList = finYearList;
	}

	public List<String> getPeriodList() {
		return periodList;
	}

	public void setPeriodList(List<String> periodList) {
		this.periodList = periodList;
	}

	public String getGrantsType() {
		return grantsType;
	}

	public void setGrantsType(String grantsType) {
		this.grantsType = grantsType;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

}
