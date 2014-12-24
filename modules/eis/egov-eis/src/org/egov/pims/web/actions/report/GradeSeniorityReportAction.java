package org.egov.pims.web.actions.report;

import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.model.GradeMaster;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.Query;
import org.hibernate.criterion.Order;

@ParentPackage("egov")
public class GradeSeniorityReportAction extends BaseFormAction {
	
	private static final long serialVersionUID = -2290072157318284268L;
	public PersistenceService persistenceService;
	private Integer gradeId;
	private Integer deptId;
	private List<Object[]> resultEmpList;;

	@Override
	public Object getModel() {
		return null;
	}
	@Override
	public void prepare()
	{
		addDropdownData("gradeList", 
				persistenceService.getSession().createCriteria(GradeMaster.class).
				addOrder(Order.asc("name")).list());
		addDropdownData("departmentList", 
				persistenceService.getSession().createCriteria(DepartmentImpl.class).
				addOrder(Order.asc("deptName")).list());		
	}
	
	public String execute()
	{
		return SUCCESS;
	}
		
	
	public String getGradeDurationInfo()
	{
		StringBuffer activeEmpsQrySB=new StringBuffer(150);
		StringBuffer finalQrySB=new StringBuffer(300);
		
		activeEmpsQrySB.append("select innerEmpView.id from EmployeeView innerEmpView " +
				"where (:sysdate between innerEmpView.fromDate  and innerEmpView.toDate or (innerEmpView.toDate is null and innerEmpView.fromDate<=:sysdate))")
		.append("and upper(innerEmpView.isPrimary)='Y' ");
		if(deptId!=null && deptId!=-1)
		{
			activeEmpsQrySB.append(" and innerEmpView.deptId.id="+deptId);
		}
		
		
		Query finalQuery=persistenceService.getSession().createQuery(finalQrySB.append("select distinct  " +
				"empview.employeeCode,empview.employeeName,ROUND(SUM(" +
				" CASE WHEN empview.toDate is null or empview.toDate>sysdate THEN sysdate " +
				" else empview.toDate"+
				" end " +
				" - empview.fromDate)) " +						
				"from EmployeeView empview " +
				"left join empview.assignment as assignment where empview.id in ( "+activeEmpsQrySB+" )")
			.append(" and assignment.gradeId.id=:gradeId and empview.isPrimary='Y' group by empview.employeeCode,empview.employeeName order by 3 desc ").toString());
		finalQuery.setInteger("gradeId", gradeId);
		finalQuery.setDate("sysdate", DateUtils.today());
		
		 this.resultEmpList = finalQuery.list();
		return SUCCESS;
		
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	public List<Object[]> getResultEmpList() {
		return resultEmpList;
	}
	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	
	
	/**
	 * returns Date in words as year month and days for the given number of days
	 * @param numberOfDays
	 * @return 
	 */
	public   String getDateInWords(int numberOfDays)
	{
		String dateAsString="";
		int year,month=0;
		if(numberOfDays==0 || numberOfDays<0)
			return dateAsString;
		else
		{
			if(numberOfDays>365)
			{
				year=numberOfDays/365;
				if(year>0){
					dateAsString=dateAsString+year+(year==1?" Year ":" Years ");
				}
				numberOfDays=numberOfDays%365;
				
			}
			 if(numberOfDays>30)
			{
				month=numberOfDays/30;
				if(month>0){				
					dateAsString=dateAsString+month+(month==1?" Month ":" Months ");
				}
				numberOfDays=numberOfDays%30;
			}
			 if(numberOfDays>0)
			{				
				if(numberOfDays>0){					
					dateAsString=dateAsString+numberOfDays+(numberOfDays==1?" Day ":" Days ");
				}
			}
			
		}
		return dateAsString;
	}
	public Integer getGradeId() {
		return gradeId;
	}
	public Integer getDeptId() {
		return deptId;
	}
	
	
}
