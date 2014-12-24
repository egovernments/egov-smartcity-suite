package org.egov.pims.web.actions.nomineeMaster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.EisUtilService;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.validator.annotations.Validation;
@ParentPackage("egov")
@Validation()
/**
 * @author DivyaShree MS
 */
public class SearchForEmployeeAction extends BaseFormAction 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EmployeeView empview = new EmployeeView();
	private Integer designationId;
	List<EmployeeView> employeeList=null;
	private String mode;
	private EmployeeService employeeService;
	private EisUtilService eisService;
	
	
	public Object getModel() 
	{
	
		return empview;
	}
	@SkipValidation
	public String searchEmployee()
	{
		setMode(mode);
		return NEW;
	}
	
	public SearchForEmployeeAction()
	{
		
		
		addRelatedEntity("deptId",org.egov.lib.rjbac.dept.DepartmentImpl.class);
		addRelatedEntity("employeeStatus",org.egov.commons.EgwStatus.class);
		addRelatedEntity("functionary",org.egov.commons.Functionary.class);
		addRelatedEntity("employeeType",org.egov.pims.model.EmployeeStatusMaster.class);
		
			
	}
	
	public void prepare() 
	{
		super.prepare();
		addDropdownData("designationMasterList", getPersistenceService().findAllBy("from DesignationMaster order by designationName"));
		addDropdownData("DepartmentImplList",getEisService().getDeptsForUser());
		addDropdownData("EgwStatusList", getPersistenceService().findAllBy("from EgwStatus egw where upper(egw.moduletype) ='EMPLOYEE'"));
		setupDropdownDataExcluding("deptId","employeeStatus");
		
		
	}
	
	public String search() throws Exception
	{
		setMode(mode);
		Integer deptId=Integer.valueOf(0);
		Integer desgId=Integer.valueOf(0);
		Integer functionaryId = Integer.valueOf(0);
		Integer statusId = Integer.valueOf(0);
		Integer typeId = Integer.valueOf(0);
		String empCode="";
		String empName="";
		if(empview.getDeptId()!=null && empview.getDeptId().getId()!=null && empview.getDeptId().getId().intValue()!=-1)
		{
			deptId=empview.getDeptId().getId();
		}
		if(getDesignationId()!=null && getDesignationId().intValue()!=-1)
		{
			desgId = getDesignationId();
		}
		if(empview.getFunctionary()!=null && empview.getFunctionary().getId()!=null &&  empview.getFunctionary().getId()!=-1)
		{
			functionaryId =  empview.getFunctionary().getId();
		}
		if(empview.getEmployeeCode()!=null && !"".equals(empview.getEmployeeCode()))
		{
			empCode=empview.getEmployeeCode();
		}
		if(empview.getEmployeeName()!=null && !"".equals(empview.getEmployeeName()))
		{
			empName=empview.getEmployeeName();
		}
		if(empview.getEmployeeStatus()!=null && empview.getEmployeeStatus().getId()!=null && empview.getEmployeeStatus().getId().intValue()!=-1)
		{
			statusId = empview.getEmployeeStatus().getId();
		}
		if(empview.getEmployeeType()!=null && empview.getEmployeeType().getId()!=null && empview.getEmployeeType().getId().intValue()!=-1)
		{
			typeId = empview.getEmployeeType().getId();
		}
		if(null!=getMode() && getMode().equalsIgnoreCase("view"))
		{
			employeeList =employeeService.searchEmployeeForNominees(deptId,desgId,functionaryId,empCode, empName,statusId, typeId);
		}
		else
		{	
			employeeList =employeeService.searchEmployee(deptId,desgId,functionaryId,empCode, empName,statusId, typeId);
		}	
		return NEW;
	}
	public Integer getDesignationId() {
		return designationId;
	}
	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}
	
	
	public void setEmployeeList(List<EmployeeView> employeeList) {
		this.employeeList = employeeList;
	}
	public List<EmployeeView> getEmployeeList() {
		return employeeList;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public void setEmpview(EmployeeView empview) {
		this.empview = empview;
	}
	 
	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	public EisUtilService getEisService() {
		return eisService;
	}
	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}
	
	
	}
