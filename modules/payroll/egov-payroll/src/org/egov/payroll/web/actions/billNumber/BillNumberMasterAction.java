package org.egov.payroll.web.actions.billNumber;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Bankbranch;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.dept.dao.DepartmentDAO;
import org.egov.masters.model.BillNumberMaster;
import org.egov.pims.commons.Position;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.SQLQuery;


/**
 * Master to Create/Modify and view Bill Numbers
 * 
 * @author subhash
 *
 */

@SuppressWarnings("serial")
public class BillNumberMasterAction extends BaseFormAction {
	
	DepartmentDAO deptDao = new DepartmentDAO();
	PersistenceService<BillNumberMaster, Integer> billNumberMasterService;
	private List<Department> deptList;
	private Integer deptId;
	private static final String CREATE = "create";
	private static final String VIEW = "view";
	private static final String SEARCH_RESULTS = "searchResults";
	private static final String BILLNUMBERLISTQUERY="BILL-NUMBER-LIST";
	private String query;
	private List<BillNumberMaster> billNumList = new ArrayList<BillNumberMaster>();

	private List<BillNumberMaster> billNoList;
	private List<BillNumberBean> billNumberBeans = new ArrayList<BillNumberBean>();

	private Integer departmentId ;
	@Override
	public Object getModel() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public void prepare() {
		deptList = deptDao.getAllDepartments();
		addDropdownData("deptList", deptList);		
		if (parameters.get("deptId") != null) {
			SQLQuery qry = getPersistenceService().getSession()
					.createSQLQuery("select * from egeis_billnumber_master where id_department = ?")
					.addEntity(BillNumberMaster.class);
			qry.setInteger(0, Integer.valueOf(parameters.get("deptId")[0]));
		
			List<BillNumberMaster> billNumbers = qry.list();
			if(billNumbers.isEmpty()){
				billNumberBeans.add(new BillNumberBean());
			}
			
			for (BillNumberMaster number : billNumbers) {
				BillNumberBean tempBean = new BillNumberBean();
				tempBean.setBillNumberId(number.getId().toString());
				tempBean.setBillNumber(number.getBillNumber());
				tempBean.setPositionId(number.getPosition().getId().toString());
				tempBean.setPositionName(number.getPosition().getName());
				billNumberBeans.add(tempBean);
			}
		}
	}
	
	public void validate() {
		if (deptId == null || deptId == -1) {
			addActionError(getText("error.deptNo"));
		}
	}
	
	public void validateDetails() {
		List<String> billNumStrList = new ArrayList<String>();
		for (BillNumberBean billNumberBean : billNumberBeans) {

			if (StringUtils.isBlank(billNumberBean.getBillNumber())) {
				addActionError("Please enter Bill Number");			
				break;
			}
			
			if (StringUtils.isBlank(billNumberBean.getPositionName())) {
				addActionError("Please enter Position");	
				break;
			}
						
			if (billNumberBean.getPositionId().isEmpty()) {
					addActionError("Selected Position : " + billNumberBean.getPositionName() + " is not correct");
			}
			
			BillNumberMaster existBillNumMstr = billNumberMasterService.find("from BillNumberMaster b where b.billNumber = ?",
					billNumberBean.getBillNumber());
			
			if (existBillNumMstr != null) {
				if (billNumberBean.getBillNumberId() == null || billNumberBean.getBillNumberId().equals("")) {// Create
					addActionError("Bill Number : " + billNumberBean.getBillNumber() + " is already created");
					break;
				} else if (!billNumberBean.getBillNumberId().equals(existBillNumMstr.getId().toString())) {// Update
					addActionError("Bill Number : " + billNumberBean.getBillNumber() + " is already created");
					break;
				}
			}
			
			for(String billNumStr : billNumStrList){
				if(billNumStr.equals(billNumberBean.getBillNumber())){
					addActionError("Bill Number " + billNumberBean.getBillNumber() + " is entered more than once, Bill Number must be unique");
				}
			}
			billNumStrList.add(billNumberBean.getBillNumber());
		}
	}
	
	@SkipValidation
	public String newForm() {
		return NEW;
	}
	
	@ValidationErrorPage(value = "new")
	public String createForm() {		
		return CREATE;
	}

	@ValidationErrorPage(value = "new")
	public String view() {
		return VIEW;
	}
	
	@SkipValidation
	public String save() {
		validateDetails();
		if (hasActionErrors()) {
			return CREATE;
		}
		
		for (BillNumberBean billNumberBean : billNumberBeans) {
			BillNumberMaster billNumberMaster = new BillNumberMaster();
			DepartmentImpl dept = (DepartmentImpl) persistenceService.find("from org.egov.lib.rjbac.dept.DepartmentImpl d where d.id = ?", deptId);
			Position pos = (Position) persistenceService.find("from org.egov.pims.commons.Position p where p.id = ?", Integer.valueOf(billNumberBean.getPositionId()));
			
			if (StringUtils.isNotBlank(billNumberBean.getBillNumberId())) {
				billNumberMaster.setId(Integer.valueOf(billNumberBean.getBillNumberId()));
			}
			billNumberMaster.setBillNumber(billNumberBean.getBillNumber());
			billNumberMaster.setDepartment(dept);
			billNumberMaster.setPosition(pos);
			if (billNumberMaster.getId() == null) {
				billNumberMasterService.getSession().persist(billNumberMaster);
			} else {
				billNumberMasterService.getSession().merge(billNumberMaster);
			}
		}				
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String getBillNumberList(){
		if (StringUtils.isNotBlank(query)) {
			query=(query.toLowerCase()+"%");
			billNumList = billNumberMasterService.findPageByNamedQuery(BILLNUMBERLISTQUERY, 0, 30, query).getList();
		}
		
		return SEARCH_RESULTS;
	}
	
	@SkipValidation
	public String getBillNumberListByDepartment()
	{
		billNoList = billNumberMasterService.findAllBy("from BillNumberMaster b where  b.department.id = ? ",
				departmentId);
		return  "result";
	}
	
	public List<Department> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<Department> deptList) {
		this.deptList = deptList;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public PersistenceService<BillNumberMaster, Integer> getBillNumberMasterService() {
		return billNumberMasterService;
	}

	public void setBillNumberMasterService(
			PersistenceService<BillNumberMaster, Integer> billNumberMasterService) {
		this.billNumberMasterService = billNumberMasterService;
	}

	public List<BillNumberBean> getBillNumberBeans() {
		return billNumberBeans;
	}

	public void setBillNumberBeans(List<BillNumberBean> billNumberBeans) {
		this.billNumberBeans = billNumberBeans;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<BillNumberMaster> getBillNumList() {
		return billNumList;
	}

	public void setBillNumList(List<BillNumberMaster> billNumList) {
		this.billNumList = billNumList;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public List<BillNumberMaster> getBillNoList() {
		return billNoList;
	}

	public void setBillNoList(List<BillNumberMaster> billNoList) {
		this.billNoList = billNoList;
	}

}

