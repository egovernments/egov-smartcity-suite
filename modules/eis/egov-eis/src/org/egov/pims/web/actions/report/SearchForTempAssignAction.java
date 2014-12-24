package org.egov.pims.web.actions.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.models.validator.CheckDateFormat;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.validator.annotations.Validation;

@ParentPackage("egov")
@Validation()
public class SearchForTempAssignAction extends BaseFormAction
{
	/**
	 * 
	 */
	
	
	private static final long serialVersionUID = 1L;
	//private EmployeeView EMPVIEW = new EmployeeView();
	
	private final Assignment EMPVIEW = new Assignment();
	DateFormat smt  = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	private EmployeeService employeeService;
	private List<Assignment> empTempList=new LinkedList<Assignment>(); 
	private List<Assignment> empPrimaryList=new LinkedList<Assignment>(); 
	private final List<TempAssignDto> showList = new LinkedList<TempAssignDto>();
	@CheckDateFormat(message="invalid.fieldvalue.positionID")
	public Date fromDate;
	public String employeeCode;
	public String positionSearch;
	public Integer positionID;
	public Object getModel() {
		
		return EMPVIEW;
	}
	
	@SkipValidation
	public String createOrModify()
	{
		return NEW;
	}
	public SearchForTempAssignAction() 
	{
		addRelatedEntity("position", Position.class);
		
	}
	public String search()//This for search employee for temporary Assignment
	{
		
		if(getPosition()==null && fromDate==null && "".equals(employeeCode) )
		{
			addActionError("Please Enter any one of the field");
		}
		else
		{
			
			if(getPosition()==null)
			{
				
				empTempList=employeeService.getEmpTempAssignment(employeeCode,fromDate,Integer.valueOf(0));
				if(empTempList!=null && empTempList.isEmpty())
				{
					addActionMessage("Nothing Found to Display");
				}
				else
				{
					for(Assignment ass : empTempList)
					{
						TempAssignDto temp = new TempAssignDto();
						temp.setEmpCode(ass.getAssignmentPrd().getEmployeeId().getEmployeeCode().toString());
						temp.setEmpFirstName(ass.getAssignmentPrd().getEmployeeId().getEmployeeFirstName());
						temp.setEmpDesgName(ass.getDesigId().getDesignationName());
						temp.setDeptName(ass.getDeptId().getDeptName());
						temp.setFromDate(smt.format(ass.getAssignmentPrd().getFromDate()));
						temp.setToDate(smt.format(ass.getAssignmentPrd().getToDate()));
						temp.setTempPos(ass.getPosition().getName());
						if(ass!=null && ass.getAssignmentPrd().getEmployeeId().getIdPersonalInformation()!=null)
						{
							Assignment position = null;
							if(fromDate==null)
							{
								 position=employeeService.getAssignmentByEmpAndDate(new Date(),ass.getAssignmentPrd().getEmployeeId().getIdPersonalInformation());
							}
							else
							{
							   position=employeeService.getAssignmentByEmpAndDate(fromDate,ass.getAssignmentPrd().getEmployeeId().getIdPersonalInformation());
							}
							if(position==null)
							{
								
								
								
								temp.setHoldingPriPos("Not Assigned");
							}
							else
							{
								temp.setHoldingPriPos(position.getPosition().getName());
							}
						}
						showList.add(temp);
					}
					
					addActionMessage("Successfully Found the Result");
				}
			}
			else
			{
				empTempList=employeeService.getEmpTempAssignment(employeeCode,fromDate,Integer.valueOf(getPosition()));
				//This Api Is called only when we search with position. For the selected position we need to show both temp and emp with primary 
				//information
				empPrimaryList=employeeService.getEmpPrimaryAssignment(employeeCode,fromDate,Integer.valueOf(getPosition()));
				
				if(empTempList!=null && empTempList.isEmpty())
				{
					addActionMessage("Nothing Found to Display");
				}
				else
				{
					for(Assignment ass : empTempList)
					{
						
						TempAssignDto temp = new TempAssignDto();
						temp.setEmpCode(ass.getAssignmentPrd().getEmployeeId().getEmployeeCode().toString());
						temp.setEmpFirstName(ass.getAssignmentPrd().getEmployeeId().getEmployeeFirstName());
						temp.setEmpDesgName(ass.getDesigId().getDesignationName());
						temp.setDeptName(ass.getDeptId().getDeptName());
						temp.setFromDate(smt.format(ass.getAssignmentPrd().getFromDate()));
						temp.setToDate(smt.format(ass.getAssignmentPrd().getToDate()));
						temp.setTempPos(ass.getPosition().getName());
						if(ass!=null && ass.getAssignmentPrd().getEmployeeId().getIdPersonalInformation()!=null)
						{
							Assignment position = null;
							if(fromDate==null)
							{
								 position=employeeService.getAssignmentByEmpAndDate(new Date(),ass.getAssignmentPrd().getEmployeeId().getIdPersonalInformation());
							}
							else
							{
							   position=employeeService.getAssignmentByEmpAndDate(fromDate,ass.getAssignmentPrd().getEmployeeId().getIdPersonalInformation());
							}
							
							if(position==null)
							{
								
								temp.setHoldingPriPos("Not Assigned");
							}
							else
							{
								temp.setHoldingPriPos(position.getPosition().getName());
							}
						}
						showList.add(temp);
					}
					
					if(empPrimaryList!=null && !empPrimaryList.isEmpty())
					{
							for(Assignment assPrimary : empPrimaryList)
							{
								
								TempAssignDto temp = new TempAssignDto();
								temp.setEmpCode(assPrimary.getAssignmentPrd().getEmployeeId().getEmployeeCode().toString());
								temp.setEmpFirstName(assPrimary.getAssignmentPrd().getEmployeeId().getEmployeeFirstName());
								temp.setEmpDesgName(assPrimary.getDesigId().getDesignationName());
								temp.setDeptName(assPrimary.getDeptId().getDeptName());
								temp.setFromDate(smt.format(assPrimary.getAssignmentPrd().getFromDate()));
								temp.setToDate(smt.format(assPrimary.getAssignmentPrd().getToDate()));
								temp.setHoldingPriPos(assPrimary.getPosition().getName());
								temp.setTempPos("----");
								showList.add(temp);
							}
					}
					addActionMessage("Successfully Found the Result");
				}
			}
		}
		
		return NEW;
	}


	
	public void setEmployeeService(EmployeeService employeeService){
		this.employeeService = employeeService;
	}

	public List<Assignment> getEmpTempList() {
		return empTempList;
	}

	

	public String getPositionSearch() {
		return positionSearch;
	}

	public void setPositionSearch(String positionSearch) {
		this.positionSearch = positionSearch;
	}

	public Integer getPosition() {
		return positionID;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public void setPositionID(Integer positionID) {
		this.positionID = positionID;
	}

	public List<TempAssignDto> getShowList() {
		return showList;
	}


	
}
