package org.egov.payroll.client.bulkRuleUpdation;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.model.PayGenUpdationRule;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.services.payhead.PayheadService;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.model.EmployeeGroupMaster;
import org.egov.pims.utils.EisManagersUtill;
import org.hibernate.exception.ConstraintViolationException;
import org.egov.infstr.services.PersistenceService;

public class BulkUpdateMasterAction extends DispatchAction 
{
	public static final Logger logger = Logger.getLogger(BulkUpdateMasterAction.class.getClass());
	PayrollExternalInterface payrollExternalInterface;
	private PayheadService payheadService;
	private PayRollService payRollService; 
	private static final String MODE="mode";
	private static final String ERROR="error";
	protected transient PersistenceService<EmployeeGroupMaster, Integer> persistenceService;
	
	
	
	public ActionForward beforeLoad(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{	
		
		String target = "";
		BulkMasterForm bulkForm = (BulkMasterForm)form;
		String viewMode="";
		
		try {
			if(req.getParameter(MODE)!=null)
			{
				viewMode = req.getParameter(MODE);
				req.setAttribute(MODE,viewMode);
			}
			List<SalaryCodes> allSalaryCodes= getPayheadService().getAllSalaryCodes();
			req.setAttribute("allSalaryCodes", allSalaryCodes);
			populate(req,bulkForm);
			target = "salaryCode";
		} 
		catch(EGOVRuntimeException ex)
        {
            logger.error("EGOVRuntimeException Encountered!!!"+ ex.getMessage());
            target = ERROR;
            HibernateUtil.rollbackTransaction();
            
        }catch(Exception ex)
        {
            logger.error("EGOVRuntimeException Encountered!!!"+ ex.getMessage());
            target = ERROR;
            HibernateUtil.rollbackTransaction();
            
        }
        return mapping.findForward(target);
	}
	private void populate(HttpServletRequest req,BulkMasterForm bulkForm)
	{

		try {
			/*
			 * To get all active financial year
			 */
			List fYMasterList=payrollExternalInterface.getAllActiveFinancialYearList();
			req.getSession().setAttribute("finMap",getFinMap(fYMasterList));
			//current month
			int currentmonth=Calendar.getInstance().get(Calendar.MONTH) + 1;
			Map mMap =EisManagersUtill.getMonthsStrVsDaysMap();
			req.getSession().setAttribute("monthMap", mMap);
			//set current month's id
			bulkForm.setMonthId(Integer.toString(currentmonth));
			//current financial year
			String  currentfinYear=payrollExternalInterface.getCurrYearFiscalId();
			bulkForm.setFinYear(currentfinYear);
			ArrayList<EmployeeGroupMaster> grpMstrList = (ArrayList<EmployeeGroupMaster>)EgovMasterDataCaching.getInstance().get("egEmp-EmployeeGroupMaster");
			req.getSession().setAttribute("empGroupMastersList", grpMstrList);
			
			
		} catch (EGOVRuntimeException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			throw new EGOVRuntimeException( e.getMessage(),e);
		}
		
		
		
	}
	
	
	public Map getFinMap(List list)
	{
		Map<Long,String> finMap = new TreeMap<Long,String>();
		try {
			for(Iterator iter = list.iterator();iter.hasNext();)
			{
				CFinancialYear cFinancialYear = (CFinancialYear)iter.next();
				finMap.put(cFinancialYear.getId(), cFinancialYear.getFinYearRange());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
		return finMap;
	}
	
	public  ActionForward saveRule(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	{
		String target="";
		BulkMasterForm bulkForm = (BulkMasterForm)form;
		
		String mode=(String)req.getParameter(MODE);
		if(mode!=null)
			
			{
			req.getSession().setAttribute(MODE, mode);
			}
		String payHead=null;
		String percentage=null;
		String amount=null;
		String monthId=null;
		String finYr=null;
		String empGroup = null;
		
		empGroup=bulkForm.getEmpGrpMstr();
		payHead=bulkForm.getPayHead();
		percentage=bulkForm.getPercentage();
		amount=bulkForm.getAmount();
		monthId=bulkForm.getMonthId();
		finYr=bulkForm.getFinYear();
		
		logger.debug("payHead>>"+payHead);
		logger.debug("percentage>>"+percentage);
		logger.debug("amount>>"+amount);
		logger.debug("monthId>>"+monthId);
		logger.debug("finYr>>"+finYr);
		try
		{
		
				PayGenUpdationRule updationRule = new PayGenUpdationRule();
				if(empGroup!=null && !StringUtils.trimToEmpty(empGroup).equals(""))
				{
					persistenceService.setType(EmployeeGroupMaster.class);
					EmployeeGroupMaster empGrp  = (EmployeeGroupMaster) persistenceService.findById(Integer.valueOf(empGroup), false);
					updationRule.setEmpGroupMstrs(empGrp);
				}
				if(payHead!=null && !payHead.equals(""))
				{
					SalaryCodes salaryCodes=PayrollManagersUtill.getPayRollService().getSalaryCodesById(new Integer(payHead));
					updationRule.setSalaryCodes(salaryCodes);
				}
				if(percentage!=null && !percentage.equals(""))
				{
					updationRule.setPercentage(new BigDecimal(percentage));
				}
				if(!StringUtils.trimToEmpty(amount).equals(""))
				{
					updationRule.setMonthlyAmt(new BigDecimal(amount));
				}
				if(!StringUtils.trimToEmpty(monthId).equals(""))
				{
					updationRule.setMonth(new BigDecimal(monthId));
				}
				if(!StringUtils.trimToEmpty(finYr).equals(""))
				{
					CFinancialYear cfinancial  =payrollExternalInterface.findFinancialYearById(new Long(finYr));
					updationRule.setFinancialyear(cfinancial);
				}
				if(!StringUtils.trimToEmpty(monthId).equals("") && !StringUtils.trimToEmpty(finYr).equals(""))
				{
					Date effDate = payRollService.getStartDateOfMonthByMonthAndFinYear((new Integer(monthId)).intValue(), (new Integer(finYr)).intValue());
					logger.info("monthId="+monthId+", finYr="+finYr+", effDate ="+effDate);
					updationRule.setEffectivedate(effDate);
				}
				getPayheadService().createRule(updationRule);
				req.getSession().setAttribute("ruleMstrId", updationRule.getId());
				target="success";
		
		}
		catch(ConstraintViolationException sqe)
	    {	  
			target = ERROR;
			logger.info("ConstraintViolationException Encountered!!!"+sqe.getMessage());
			
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException((new StringBuilder("ConstraintViolationException:")).append(sqe.getMessage()).toString(), sqe);	       
	    }catch(Exception ex)
		{
		   target = ERROR;
		   logger.info(ex.getMessage());
		   
		   throw new EGOVRuntimeException(ex.getMessage(),ex);
		}	
		return mapping.findForward(target);
	}
	
	public ActionForward viewRule(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	{
		String target="";
		String alertMessage="";
		String mode="";
		BulkMasterForm bulkForm = (BulkMasterForm)form;
		Integer ruleId=(Integer)req.getSession().getAttribute("ruleMstrId");
		mode=(String)req.getSession().getAttribute(MODE);
		if(ruleId!=null)
		{
			try 
			{
				PayGenUpdationRule ruleUpdation=getPayheadService().getRuleMstrById(Integer.valueOf(ruleId.intValue()));
				List<SalaryCodes> allSalaryCodes= getPayheadService().getAllSalaryCodes();
				//TODO getit from session,since its already in the session
				ArrayList<EmployeeGroupMaster> grpMstrList = (ArrayList<EmployeeGroupMaster>)EgovMasterDataCaching.getInstance().get("egEmp-EmployeeGroupMaster");
				req.setAttribute("empGrps", grpMstrList);
				req.setAttribute("allSalaryCodes", allSalaryCodes);
				
				if(ruleUpdation!=null)
				{
				bulkForm.setRuleUpdation(ruleUpdation);
				}
				if("create".equals(mode))
					
					{
					alertMessage=successCreateMsg;
					}
				else if("modify".equals(mode))
					
					{
					alertMessage=successModifyMsg;
					}
				req.setAttribute("alertMessage", alertMessage);
				target = "view";
			}catch(Exception ex)
			{
			   target = ERROR;
			   logger.info(ex.getMessage());
			  
			   //HibernateUtil.rollbackTransaction() has to be removed?
			   
			   throw new EGOVRuntimeException(ex.getMessage(),ex);
			}	
		}
		return mapping.findForward(target);
	}
	
	//FIXME: to be rewritten
	public ActionForward searchBeforeLoad(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	{
		String target="";
		BulkMasterForm bulkForm = (BulkMasterForm)form;
		
		String viewMode ="";
		if(req.getParameter(MODE)!=null)
		{
			viewMode = req.getParameter(MODE);
			req.setAttribute(MODE,viewMode);
		}
		try {
					populate(req,bulkForm);
					target = "searchRule";
					
		} catch (Exception e) {
			
			 target = ERROR;
			   logger.info(e.getMessage());
			  
			   throw new EGOVRuntimeException(e.getMessage(),e);
		}
		return mapping.findForward(target);
	}
	
	
	
	
	public ActionForward viewRuleMaster(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	{
		String target="";
		String alertMessage="";
		BulkMasterForm bulkForm = (BulkMasterForm)form;
		
		String monthId=null;
		String finYr=null;
		
		try {
			
			monthId=bulkForm.getMonthId();
			finYr=bulkForm.getFinYear();
			//FIXME: get mode from request and set it back in request
			String mode=(String)req.getParameter(MODE);
			if(mode!=null)
				
				{
				req.setAttribute(MODE, mode);
				}
			if(!StringUtils.trimToEmpty(monthId).equals("") && !StringUtils.trimToEmpty(finYr).equals(""))
			{
				List payGenRuleList=getPayheadService().getRulemasterByMonFnYr(new Integer(monthId),new Integer(finYr));
				if(payGenRuleList.isEmpty())
				{
					target="NoResult";
					alertMessage = strNoRuleSetUp;
					req.setAttribute("alertMessage", alertMessage);
				}
				else
				{
					target="showList";
					req.setAttribute("payGenRuleList", payGenRuleList);
					
				}
			}
		}  catch (Exception e)
		{
			   target = ERROR;
			   logger.info(e.getMessage());
			   
			   HibernateUtil.rollbackTransaction();
			  
		}
		
		
		return mapping.findForward(target);
	}
	
	public ActionForward setIdForMasterDetails(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	{
		String target="";
		BulkMasterForm bulkForm = (BulkMasterForm)form;
		String payHead=null;
		String mode=(String)req.getParameter(MODE);
		if(mode!=null)
			{
			req.setAttribute(MODE, mode);
			}
		
		try {
			payHead=req.getParameter("ruleId");
			if(!StringUtils.trimToEmpty(payHead).equals(""))
			{
				PayGenUpdationRule payGenMstrRule=getPayheadService().getRuleMstrById(new Integer(payHead));
				if(payGenMstrRule!=null)
				{
					target="viewResult";
					List<SalaryCodes> allSalaryCodes= getPayheadService().getAllSalaryCodes();
					req.setAttribute("allSalaryCodes", allSalaryCodes);
					bulkForm.setRuleUpdation(payGenMstrRule);
					req.getSession().setAttribute("monthMap",getMonthMap());
				}
			}
		}  catch (Exception e)
		{
			   target = ERROR;
			   logger.info(e.getMessage());
			   HibernateUtil.rollbackTransaction();
			   
		}
		
		
		return mapping.findForward(target);
	}
	private Map getMonthMap() {
		Map<Integer,String>monthMap=new HashMap<Integer,String>();
		monthMap.put(Integer.valueOf(1),"Jan");
		monthMap.put(Integer.valueOf(2),"Feb");
		monthMap.put(Integer.valueOf(3),"Mar");
		monthMap.put(Integer.valueOf(4),"Apr");
		monthMap.put(Integer.valueOf(5),"May");
		monthMap.put(Integer.valueOf(6),"June");
		monthMap.put(Integer.valueOf(7),"July");
		monthMap.put(Integer.valueOf(8),"Aug");
		monthMap.put(Integer.valueOf(9),"Sep");
		monthMap.put(Integer.valueOf(10),"Oct");
		monthMap.put(Integer.valueOf(11),"Nov");
		monthMap.put(Integer.valueOf(12),"Dec");
		return monthMap;
	}
	
	public ActionForward modifyRule(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	{
		String target="";
		BulkMasterForm bulkForm = (BulkMasterForm)form;
		String mode=(String)req.getParameter(MODE);
		if(mode!=null)
			{
			req.getSession().setAttribute(MODE, mode);
			}
		String ruleId=null;
		
		String percentage=null;
		String amount=null;
		String monthId=null;
		String finYr=null;
		String empGroup = null;
		try {
				
			ruleId=bulkForm.getRuleId();
			percentage=bulkForm.getPercentage();
			amount=bulkForm.getAmount();
			monthId=bulkForm.getMonthId();
			finYr=bulkForm.getFinYear();
			empGroup=bulkForm.getEmpGrpMstr();
			if(!StringUtils.trimToEmpty(ruleId).equals(""))
			{
				PayGenUpdationRule updationRule=getPayheadService().getRuleMstrById(new Integer(ruleId));
				if(percentage==null || percentage.equals(""))
					{
					updationRule.setPercentage(null);
					}
				
				else
					{
					updationRule.setPercentage(new BigDecimal(percentage));
					}
				
				if(StringUtils.trimToEmpty(amount).equals(""))
					{
					updationRule.setMonthlyAmt(null);
					}
				else
					{
					updationRule.setMonthlyAmt(new BigDecimal(amount));
					}
				
				//FIXME : should check that month + fin year >= current month
				if(!StringUtils.trimToEmpty(monthId).equals(""))
					{
					updationRule.setMonth(new BigDecimal(monthId));
					}
				
				if(!StringUtils.trimToEmpty(finYr).equals(""))
				{
					CFinancialYear cfinancial  =payrollExternalInterface.findFinancialYearById(new Long(finYr));
					updationRule.setFinancialyear(cfinancial);
				}
				if(!StringUtils.trimToEmpty(monthId).equals("") && !StringUtils.trimToEmpty(finYr).equals(""))
				{
					Date effDate = payRollService.getStartDateOfMonthByMonthAndFinYear((new Integer(monthId)).intValue(), (new Integer(finYr)).intValue());
					logger.info("monthId="+monthId+", finYr="+finYr+", effDate ="+effDate);
					updationRule.setEffectivedate(effDate);
				}
				if(empGroup!=null && !empGroup.equals(""))
				{
					persistenceService.setType(EmployeeGroupMaster.class);
					EmployeeGroupMaster empGrp = (EmployeeGroupMaster) persistenceService.findById(Integer.valueOf(empGroup), false);
					updationRule.setEmpGroupMstrs(empGrp);
				}
				getPayheadService().updateRule(updationRule);
				req.getSession().setAttribute("ruleMstrId", new Integer(ruleId));
				target="success";
			}
			
			
		} catch (Exception e)
		{
			   target = ERROR;
			   logger.info(e.getMessage());
			   
			   
		}
		
		
		return mapping.findForward(target);
	}
	
	private static final String strNoRuleSetUp="Rule has not been set up For this given period";
	
	private static final String successCreateMsg="Successfully created the rule setup";
	
	private static final String successModifyMsg="Successfully Modified the rule setup";


	public void setPersistenceService(
			PersistenceService<EmployeeGroupMaster, Integer> persistenceService) {
		this.persistenceService = persistenceService;
	}
	public PayrollExternalInterface getPayrollExternalInterface() {
		return payrollExternalInterface;
	}
	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}
	public PayheadService getPayheadService() {
		return payheadService;
	}
	public void setPayheadService(PayheadService payheadService) {
		this.payheadService = payheadService;
	}
	public PayRollService getPayRollService() {
		return payRollService;
	}
	public void setPayRollService(PayRollService payRollService) {
		this.payRollService = payRollService;
	}
	
}
