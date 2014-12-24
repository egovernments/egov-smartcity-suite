package org.egov.pims.web.actions.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.displaytag.pagination.PaginatedList;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.pims.reports.services.EisReportService;
import org.egov.web.actions.SearchFormAction;

public class PositionReportAction extends SearchFormAction{

	
	private EisReportService eisReportService;
	private DepartmentService departmentService;
	private  Long departmentId;
	private  Integer desgEnterId;
	private Integer posId;
	private transient Date givenDate;
	private PaginatedList positionsList; 
	private String designationName;
	private String positionName;
	private String deptName;
	private String department;

	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void prepare() {
		super.prepare();
		this.addDropdownData("departmentList", this.getPersistenceService().findAllBy("from DepartmentImpl order by deptName"));
	}
	
	
	public String search(){
		return NEW;
	}
   

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		Date userGivenDate=givenDate;
		List<Object> params=new ArrayList<Object>();
		if(posId != null)
		{
		    params.add(posId);
			params.add(userGivenDate);
			params.add(userGivenDate);
			params.add(userGivenDate);
			params.add(posId);
			params.add(userGivenDate);
			params.add(userGivenDate);
			params.add(userGivenDate);
		}
		else
		{
			params.add(departmentId);
			params.add(desgEnterId);
			params.add(userGivenDate);
			params.add(userGivenDate);
			params.add(userGivenDate);
			params.add(departmentId);
			params.add(desgEnterId);
			params.add(departmentId);
			params.add(desgEnterId);
			params.add(userGivenDate);
			params.add(userGivenDate);
			params.add(userGivenDate);
		}
		String countQuery=eisReportService.getcountEmpByDeptDesg(posId);
		SearchQuery searchquery= new SearchQuerySQL(eisReportService.getEmpDesigByDept(posId), countQuery, params );
		return searchquery;	
		
	}
	public String execute(){
				
		Department department=departmentService.getDepartmentById(departmentId);
		deptName=department.getDeptName();
	    setPageSize(20);
		super.search();
        setPositionsList(searchResult);
		return NEW;
	}
	
	public EisReportService getEisReportService() {
		return eisReportService;
	}
	public void setEisReportService(EisReportService eisReportService) {
		this.eisReportService = eisReportService;
	}
	
	
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	public Integer getDesgEnterId() {
		return desgEnterId;
	}
	public void setDesgEnterId(Integer desgEnterId) {
		this.desgEnterId = desgEnterId;
	}
	
	public Integer getPosId() {
		return posId;
	}
	public void setPosId(Integer posId) {
		this.posId = posId;
	}
	public Date getGivenDate() {
		return givenDate;
	}
	public void setGivenDate(Date givenDate) {
		this.givenDate = givenDate;
	}
	public PaginatedList getPositionsList() {
		return positionsList;
	}
	public void setPositionsList(PaginatedList positionsList) {
		this.positionsList = positionsList;
	}
	public String getDesignationName() {
		return designationName;
	}
	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public DepartmentService getDepartmentService() {
		return departmentService;
	}
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	
}
