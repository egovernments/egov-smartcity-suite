package org.egov.billsaccounting.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import org.egov.exceptions.RBACException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
//import org.jboss.cache.TreeCacheMBean;

import com.exilant.GLEngine.GLAccount;
import com.exilant.exility.dataservice.DataExtractor;


public class PurchaseBillAction extends DispatchAction {
	public final static  Logger LOGGER=Logger.getLogger(WorksBillAction.class);
	private UserService userService;
	
	public ActionForward beforeCreate(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res) throws IOException,ServletException
	{
		String target="success";	
		String beforeCreateExpMsg="Exception in beforeCreate ...";
		try {
			PurchaseBillForm sbForm=(PurchaseBillForm ) form;
			sbForm.reset(mapping, req);
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			Date billdate=new Date();
			String billDatestr=sdf.format(billdate);
			LOGGER.info(billDatestr);  
			sbForm.setBillDate(billDatestr);
			req.setAttribute("SupplierBillForm",sbForm);
			int userId=((Integer)req.getSession().getAttribute("com.egov.user.LoginUserId")).intValue();
			sbForm.setUserId(userId);
				User user=(User)userService.getUserByID(userId);
				 String  username=user.getUserName();
				 LOGGER.info(username);
				 sbForm.setUserName(username);
			
			
			PurchaseBillDelegate sbDelegate=new PurchaseBillDelegate();
			List deptList=sbDelegate.getAllActiveDepartments();    
			List netPayList=sbDelegate.getNetPayList();
			req.setAttribute("netPayList", netPayList);
			req.setAttribute("deptList", deptList);
			req.setAttribute("mode", "create");
		} catch (EGOVRuntimeException e) {
			target="error";
		 	LOGGER.error(beforeCreateExpMsg +""+e.getMessage());
		 	HibernateUtil.rollbackTransaction();
		 	throw e;
		
		} 
		
		
			
		
		
		return mapping.findForward(target);   
	}
	public ActionForward getTdsAndotherdtls(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res) throws IOException,ServletException
	{
		String target="";	
		String getTdsAndotherdtlsExpMsg="Exception in getTdsAndotherdtls ...";
		//get Contractor or Supplier name and tds List
		try {
			PurchaseBillForm sbForm=(PurchaseBillForm)form;
			PurchaseBillDelegate sbDelegate=new PurchaseBillDelegate(); 
			sbForm=sbDelegate.getTds(sbForm); 
			req.setAttribute("tdsList",sbForm.getTdsList());
			req.setAttribute("SupplierBillForm", sbForm);
			List netPayList=sbDelegate.getNetPayList();
			req.setAttribute("netPayList", netPayList);
			List deptList=sbDelegate.getAllActiveDepartments();    
			req.setAttribute("deptList", deptList);
			req.setAttribute("mode", req.getParameter("mode"));  
			target="success";
		} catch (EGOVRuntimeException e) {
			
			target="error";  
		 	LOGGER.error(getTdsAndotherdtlsExpMsg+""+e.getMessage());
		 	HibernateUtil.rollbackTransaction();
		 	throw e;
		 	
		}	
		
		return mapping.findForward(target);   
	}
	public ActionForward create(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";   
		String	alertMessage=null;
		String billCreation=null;
		List deptList = null;
		List netPayList=null;
	
		try  
		{
			LOGGER.info("Inside search");
			req.setAttribute("mode","create");
			PurchaseBillForm sbForm=(PurchaseBillForm ) form;
			//String buttonType=(String)req.getParameter("buttonType");
			String buttonType=sbForm.getButtonType();
			req.setAttribute("buttonType", buttonType);
			PurchaseBillDelegate sbDelegate=new PurchaseBillDelegate();
			deptList=sbDelegate.getAllActiveDepartments(); 
			netPayList=sbDelegate.getNetPayList();
			int userId=((Integer)req.getSession().getAttribute("com.egov.user.LoginUserId")).intValue();
			sbForm.setUserId(userId);
			LOGGER.info("CodeLists"+sbForm.codeList);
			int result=sbDelegate.postInEgBillRegister(sbForm);  
			req.setAttribute("SupplierBillForm", sbForm);
			if(result==1)
			{
			alertMessage="Bill cannot be Created Bill Amount Exceeds The Total WorkOrder Amount ";
			billCreation="failed";
			}
			else
			{		
			alertMessage="Bill "+sbForm.getBillNo() +"  Succesfully created";
			billCreation="success";
			}
			    
			req.setAttribute("billCreation",billCreation);
			req.setAttribute("alertMessage",alertMessage);
			req.setAttribute("netPayList", netPayList);  
			
			req.setAttribute("deptList", deptList);
			target="success";
		}catch(RBACException e)
		{
			target="success";
			alertMessage=e.getMessage();
			req.setAttribute("deptList", deptList);
			req.setAttribute("alertMessage",alertMessage);
			LOGGER.error("Error While Creating the Bill");
			LOGGER.debug("Exp="+e.getMessage());
			HibernateUtil.rollbackTransaction();
		}
		catch(Exception ex)   
		{
			target="success";
			alertMessage=ex.getMessage();
			req.setAttribute("deptList", deptList);
			req.setAttribute("alertMessage",alertMessage);
			LOGGER.error("Error While Creating the Bill");
			HibernateUtil.rollbackTransaction();
		}
		return mapping.findForward(target);
	}
	public ActionForward beforeViewModify(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "success";    
	
		try  
		{
			LOGGER.info("Inside BeforeModify");
			String mode="modify";  
			 mode=req.getParameter("mode");
			 
			req.setAttribute("mode",mode);
			PurchaseBillForm sbForm=(PurchaseBillForm ) form;
			//billNumber=1040&showMode=modify&expType=Works
			String billRegId=req.getParameter("billId");
			LOGGER.info("billRegId"+billRegId);
			sbForm.setBillId(billRegId);
			PurchaseBillDelegate sbDelegate=new PurchaseBillDelegate();
			int userId=((Integer)req.getSession().getAttribute("com.egov.user.LoginUserId")).intValue();
			sbForm.setUserId(userId);
			LOGGER.info("CodeLists"+sbForm.codeList);
			sbForm=	sbDelegate.getEgBillRegister(sbForm);      
			req.setAttribute("SupplierBillForm",sbForm);
		
			req.setAttribute("tdsList",sbForm.getTdsList());
			req.setAttribute("SupplierBillForm", sbForm);
			List netPayList=sbDelegate.getNetPayList();
			req.setAttribute("netPayList", netPayList);
			List deptList=sbDelegate.getAllActiveDepartments();    
			req.setAttribute("deptList", deptList);
			 
			target="success";  
			  
		}
		catch(Exception ex)   
		{
			target = "error";
			LOGGER.error("Exception Encountered!!!"+ex.getMessage());
			HibernateUtil.rollbackTransaction();
			//throw new EGOVRuntimeException(ex.getMessage(),ex);
		
		}
		return mapping.findForward(target);
	}
	
	public ActionForward modify(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException
	{
		String target="";
		String	alertMessage=null;
		String billCreation=null;
		try{
			LOGGER.info("Inside BeforeModify");
			String mode="modify";  
			mode=req.getParameter("mode");
			req.setAttribute("mode",mode);
			int userId=((Integer)req.getSession().getAttribute("com.egov.user.LoginUserId")).intValue();
			
			PurchaseBillForm sbForm=(PurchaseBillForm ) form;	
			sbForm.setUserId(userId);
			PurchaseBillDelegate delgate=new PurchaseBillDelegate();
			int result=delgate.modify(sbForm);
			target="success";
			if(result==1)     
			{
			alertMessage="Bill cannot be Created Bill Amount Exceeds The Total WorkOrder Amount ";
			billCreation="failed";
			}
			else
			{		
			alertMessage=" Bill  "+sbForm.getBillNo() +"  Succesfully Modified";
			billCreation="success";
		//	HibernateUtil.commitTransaction();
			}
			req.setAttribute("billCreation",billCreation);
			req.setAttribute("alertMessage",alertMessage);
			req.setAttribute("tdsList",sbForm.getTdsList());
			req.setAttribute("SupplierBillForm", sbForm);
			List deptList=delgate.getAllActiveDepartments();   
			List netPayList=delgate.getNetPayList();
			req.setAttribute("netPayList", netPayList);
			req.setAttribute("deptList", deptList);
			req.setAttribute("mode", "modify");
			String buttonType=sbForm.getButtonType();
			req.setAttribute("buttonType", buttonType);
			
			
		}catch(Exception e) 
		{
			target="error";
			alertMessage="Exception while Modifying the  Bill :bill modification  failed";
			LOGGER.error("Error While Modifying the Bill");
			LOGGER.debug("Exp="+e.getMessage());
			HibernateUtil.rollbackTransaction();
			//throw new EGOVRuntimeException(e.getMessage(),e);
		
		}
		LOGGER.info("just before forward");
		return mapping.findForward(target);
		  
	}
	public ActionForward approve(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException
	{
		String target="";
		try{
			LOGGER.info("Inside Modify"); 
			String mode="modify";  
			mode=req.getParameter("mode");
			req.setAttribute("mode",mode);
			int userId=((Integer)req.getSession().getAttribute("com.egov.user.LoginUserId")).intValue();
			
			PurchaseBillForm sbForm=(PurchaseBillForm ) form;	
			sbForm.setUserId(userId);
			PurchaseBillDelegate delgate=new PurchaseBillDelegate();
		//	delgate.modify(sbForm); 
			LOGGER.info("BIll Modified Succesfully");
			LOGGER.info("Before Vopucher Creation");
			List netPayList=delgate.getNetPayList();
			delgate.gnerateVoucher(Integer.parseInt(sbForm.getBillId()),userId,sbForm.getVoucherHeader_narration(),sbForm.getBillAprvalDate());
			
			target="success";
			String	alertMessage="Bill Succesfully Modified And Approved VoucherNumber ";
			req.setAttribute("alertMessage",alertMessage);
			req.setAttribute("tdsList",sbForm.getTdsList());
			req.setAttribute("SupplierBillForm", sbForm);
			List deptList=delgate.getAllActiveDepartments(); 
			//netPayList=delgate.getNetPayList();
			req.setAttribute("netPayList", netPayList);
			req.setAttribute("deptList", deptList);
			req.setAttribute("buttonType", "close");
			req.setAttribute("mode", "approve");
			
		}catch(Exception e)
		{
			target="error";
			LOGGER.error("Error While Modifying the Bill");
			LOGGER.debug("Exp="+e.getMessage());
			HibernateUtil.rollbackTransaction();
		
		}

		return mapping.findForward(target);
		
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	
}
