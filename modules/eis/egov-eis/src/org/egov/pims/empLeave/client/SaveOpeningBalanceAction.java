package org.egov.pims.empLeave.client;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.utils.EisManagersUtill;
import org.hibernate.SQLQuery;



public class SaveOpeningBalanceAction extends DispatchAction
{
	public final static Logger LOGGER = Logger.getLogger(SaveOpeningBalanceAction.class.getClass());
	
	@SuppressWarnings("deprecation")
	public ActionForward executeSearch(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException, SQLException
	 {
		
		String target =null;
		String alertMessage=null;
		
		 String queryInsert  = "INSERT INTO EGEIS_LEAVE_OPENING_BALANCE(ID,EMP_ID,LEAVE_TYPE,LEAVES_AVAILABLE,FINANCIALYEAR,CALENDARYEAR) VALUES (LEAVE_OPENING_BALANCE_SEQ.nextval,?,?,?,?,?)";
		 String queryLeaveUpDate = "UPDATE EGEIS_LEAVE_OPENING_BALANCE SET LEAVES_AVAILABLE = ? where ID = ? ";
		 SQLQuery stmLeaveBalanceCreate  = HibernateUtil.getCurrentSession().createSQLQuery(queryInsert);
		 SQLQuery stmLeaveBalanceUpdate  = HibernateUtil.getCurrentSession().createSQLQuery(queryLeaveUpDate);
		try
		{
			SimpleDateFormat sqlUtil = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
			LeaveBalanceForm leavebalanceform =(LeaveBalanceForm)form;
			
			String[] availableLeaves=leavebalanceform.getAvailableLeaves();
   			String[] leaveOpenBalanceId=leavebalanceform.getLeaveid(); 
   			
   			String typeOfLeaveMstr=(String)req.getSession().getAttribute("typeOfLeaveMstr");
   			
   			String finYear=EisManagersUtill.getCommonsService().getCurrYearFiscalId();
   		    Date currentDate = new Date();
   		    String calYear=EisManagersUtill.getEmpLeaveService().getYearIdByGivenDate(sqlUtil.format(currentDate));
   			String[] empId=leavebalanceform.getEmpId();
   			
   			/*
   			 * for update
   			 */
   			
   			for(int i=0;i<leaveOpenBalanceId.length;i++)
			{	
   					
   				if(!leaveOpenBalanceId[i].equalsIgnoreCase("null"))
   				{
   					if(!availableLeaves[i].equals(""))
   					{ 
	   					stmLeaveBalanceUpdate.setFloat(0,new Float(availableLeaves[i]));
	   					stmLeaveBalanceUpdate.setInteger(1, Integer.valueOf(leaveOpenBalanceId[i]));
	   					stmLeaveBalanceUpdate.executeUpdate();
   					}
   					
   				}	
   				else
   				{   	//create leave open balance for an employee 				
   					if(!availableLeaves[i].equals(""))
   					{  	
   						stmLeaveBalanceCreate.setInteger(0,Integer.valueOf(empId[i]));
   						stmLeaveBalanceCreate.setInteger(1,Integer.valueOf(typeOfLeaveMstr));
   						stmLeaveBalanceCreate.setFloat(2,Float.valueOf(availableLeaves[i]));
   						stmLeaveBalanceCreate.setInteger(3,Integer.valueOf(finYear));
   						stmLeaveBalanceCreate.setInteger(4,Integer.valueOf(calYear));
   						stmLeaveBalanceCreate.executeUpdate();
   					}
   				}
			}
   			target = "createScreenLeaveBalance";
			alertMessage="Created/Modified Leave Opening Balance successfully";
		} 
		catch(Exception e) {
			target = "error";
			LOGGER.error("Exception Encountered!!!"+e.getMessage());
			
		   throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
	    }
		 
		req.setAttribute("alertMessage", alertMessage); 
		return mapping.findForward(target);
	 }
	
	private final static String STR_EXCEPTION = "Exception:";
}
