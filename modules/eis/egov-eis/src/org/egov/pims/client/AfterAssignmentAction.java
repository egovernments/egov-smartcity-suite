package org.egov.pims.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.utils.EisConstants;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AfterAssignmentAction extends  DispatchAction {
	
	private static  final Logger LOGGER = Logger.getLogger(AfterAssignmentAction.class);
	private EmployeeService employeeService;
	  
	public ActionForward executeAssignment(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = null;
		try
	    {
			PIMSForm pIMSForm =(PIMSForm)form;
			String  fromdate=(String)pIMSForm.getDateInput();
			if(fromdate !=null)
			{
				GregorianCalendar fromdate1=new GregorianCalendar(Integer.parseInt(fromdate.split("/")[2]),Integer.parseInt(fromdate.split("/")[1])-1,Integer.parseInt(fromdate.split("/")[0]));
				//Date d1 = new Date(fromdate);
				LOGGER.info(">>>>>>>>>>>>>>>date<<<<<<<<<<<<<<"+fromdate1.getTime().toString());
				
				List employeeAssignList=employeeService.getListOfEmployeeWithoutAssignment(fromdate1.getTime());
				List<PersonalInformation> employeeList = employeeService.getListOfPersonalInformationByEmpIdsList(employeeAssignList);
				
				//To remove the retired employees from the list.
				List<PersonalInformation> tmpRetiredEmployeeList= new ArrayList<PersonalInformation>();
				for(PersonalInformation pi : employeeList)
				{
					if(pi.getStatusMaster()!=null && pi.getStatusMaster().getDescription().equals(EisConstants.STATUS_TYPE_RETIRED))
					{
						tmpRetiredEmployeeList.add(pi);
					}
				}
				employeeList.removeAll(tmpRetiredEmployeeList);
				
				req.setAttribute("employeeList",employeeList);
	
				target = "searchEmployee";
			}
	    }
		catch(Exception e)
		{
			target = "error";
			LOGGER.error("Exception Encountered!!!"+e.getMessage());
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		return mapping.findForward(target);
	}
	
	
	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	

}
