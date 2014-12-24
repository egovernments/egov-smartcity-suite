package org.egov.pims.client;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.pims.model.GradeMaster;
import org.egov.pims.service.EmployeeService;


public class AfterGradeMasterAction extends DispatchAction 
{
	public final static Logger LOGGER = Logger.getLogger(AfterGradeMasterAction.class);
	private EmployeeService employeeService;
   public ActionForward createGradeMaster(ActionMapping mapping,ActionForm form, HttpServletRequest req,HttpServletResponse res)
   {
	   
	   String target = "";
	   String alertMessage="";
	   String gradeName = "";
	   String startDate = "";
	   String endDate = "";
	   String age="";
	   int orderNo=0;
	   String mode="create";
	   try {
		    gradeMstrForm gradeForm = (gradeMstrForm)form;
		   
		   gradeName=gradeForm.getGradeVal();
		   startDate=gradeForm.getStartDate();
		   endDate=gradeForm.getEndDate();
		   age=gradeForm.getGradeAge();
		   orderNo = gradeForm.getOrderNo();
		   
		   GradeMaster gradeMstr = new GradeMaster();
		   	
		   if(!StringUtils.trimToEmpty(gradeName).equals(""))
		   {
			   gradeMstr.setName(gradeName);
		   }
		   if(!StringUtils.trimToEmpty(startDate).equals(""))
		   {
			   gradeMstr.setFromDate(getDateString(startDate));
		   }
		   if(!StringUtils.trimToEmpty(endDate).equals(""))
		   {
			   gradeMstr.setToDate(getDateString(endDate));
		   }
		   if(!StringUtils.trimToEmpty(age).equals(""))
		   {
			   gradeMstr.setAge(Integer.valueOf(age));
		   }
		   gradeMstr.setOrderNo(orderNo);
		   
		   employeeService.createGradeMstr(gradeMstr);
		   EgovMasterDataCaching.getInstance().removeFromCache("egEmp-GradeMaster");
		   req.getSession().setAttribute(modeForward, mode);
		   target = "success";
		   alertMessage = "Executed successfully";
		   
	} catch (Exception ex) {
           target = error;
		   throw new EGOVRuntimeException(ex.getMessage(),ex);
	}
	req.getSession().setAttribute("alertMessage", alertMessage);
	return mapping.findForward(target);
		   
	 }
   private java.util.Date getDateString(String dateString)
	{

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		java.util.Date d =null;
		try
		{
			d = dateFormat.parse(dateString);

		} catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return d;
}
   
   public ActionForward viewBasedOnGradeId(ActionMapping mapping,ActionForm form, HttpServletRequest req,HttpServletResponse res)
   {
	   String target="";
	   String gradeId="";
	   
	   try {
			   gradeId=req.getParameter("grades");
			   String mode=req.getParameter("viewMode");
			   GradeMaster grade=employeeService.getGradeMstrById(Integer.valueOf(gradeId));
			   req.setAttribute("gradeMstrDet", grade);
			   req.setAttribute(modeForward, mode);
			   
			   target="viewGradeDetails";
	   } catch (NumberFormatException e) {
		   target = error;
		   throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
	} catch (Exception e) {
			target = error;
		   throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
	}
	   return mapping.findForward(target);
   }
   public ActionForward afterModify(ActionMapping mapping,ActionForm form, HttpServletRequest req,HttpServletResponse res)
   {
	   String target="";
	   String alertMessage="";
	   String gradeId="";
	   String gradeName = "";
	   String startDate = "";
	   String endDate = "";
	   String age="";
	   int orderNo=0;
	   String mode="create";
	   gradeMstrForm gradeForm = (gradeMstrForm)form;
	   
	   try {
		   
		   req.setAttribute(modeForward, mode);
		   gradeId=gradeForm.getGradeId();
		   gradeName=gradeForm.getGradeVal();
		   startDate=gradeForm.getStartDate();
		   endDate=gradeForm.getEndDate();
		   age=gradeForm.getGradeAge();
		   orderNo = gradeForm.getOrderNo();
		   GradeMaster gradeMstr=employeeService.getGradeMstrById(Integer.valueOf(gradeId));
		   if(!StringUtils.trimToEmpty(gradeName).equals(""))
		   {
			   gradeMstr.setName(gradeName);
		   }
		   if(!StringUtils.trimToEmpty(startDate).equals(""))
		   {
			   gradeMstr.setFromDate(getDateString(startDate));
		   }
		   if(!StringUtils.trimToEmpty(endDate).equals(""))
		   {
			   gradeMstr.setToDate(getDateString(endDate));
		   }
		   if(!StringUtils.trimToEmpty(age).equals(""))
		   {
			   gradeMstr.setAge(Integer.valueOf(age));
		   }
		   gradeMstr.setOrderNo(orderNo);
		   employeeService.updateGradeMstr(gradeMstr);
		   EgovMasterDataCaching.getInstance().removeFromCache("egEmp-GradeMaster");
		   target = "successModify";
		   alertMessage = "Executed successfully";
		   
	} catch (NumberFormatException e) {
			target = error;
		   throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
	} catch (Exception e) {
			target = error;
		   throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
	}
	   req.setAttribute("alertMessage", alertMessage);
	   return mapping.findForward(target);
   }
   
   public ActionForward afterDelete(ActionMapping mapping,ActionForm form, HttpServletRequest req,HttpServletResponse res) throws Exception
   {
	   String target="";
	   String alertMessage="";
	   String gradeId="";
	   String mode="create";
	   String strErrorMsg="";
	   
	   gradeMstrForm gradeForm = (gradeMstrForm)form;
	   
	   try {
		   GradeMaster gradeMstr=null;
		   List empList=null;
		   req.setAttribute(modeForward, mode);
		   gradeId=gradeForm.getGradeId();
		   if(gradeId!=null)
		   {
			   empList= employeeService.getAllEmpByGrade(Integer.valueOf(gradeId));
			   if(empList!=null && !empList.isEmpty())
			   {
				   throw new TooManyValuesException("The Selected Grade Master is in use.Cannot be deleted!!");
			   }
			   else
			   {
				    gradeMstr=employeeService.getGradeMstrById(Integer.valueOf(gradeId));
				    employeeService.removeGradeMstr(gradeMstr);
				    EgovMasterDataCaching.getInstance().removeFromCache("egEmp-GradeMaster");
			   }
		   }
		   target = "successDelete";
		   alertMessage = "Executed successfully";
		   
	} 
	   catch(TooManyValuesException invalidExp)
		{
		    strErrorMsg = invalidExp.getMessage();
		    target="empPage";
			alertMessage=strErrorMsg;
			
		}
	
		
	   
	   req.setAttribute("alertMessage", alertMessage);
	   return mapping.findForward(target);
   }
   
   
public EmployeeService getEmployeeService() {
	return employeeService;
}
public void setEmployeeService(EmployeeService employeeService) {
	this.employeeService = employeeService;
}



private static final String modeForward = "mode";
private static final String error = "error";
private static final String STR_EXCEPTION= "Exception:";
}
