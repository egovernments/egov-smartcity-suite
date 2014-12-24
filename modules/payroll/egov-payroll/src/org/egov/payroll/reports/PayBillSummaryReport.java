package org.egov.payroll.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.payroll.utils.PayrollManagersUtill;

/**
 * @author Jagadeesan
 *
 */
public class PayBillSummaryReport extends Action {

	private static final Logger LOGGER = Logger.getLogger(PayBillSummaryReport.class);

	public ActionForward execute(ActionMapping actionMapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		try{	
			HttpSession session = request.getSession();
			String userName =(String) session.getAttribute("com.egov.user.LoginUserName");
			SearchForm searchForm = (SearchForm)form;
			List<HashMap> payslipSet = new ArrayList<HashMap>();
			Integer month = Integer.parseInt(searchForm.getMonth());
			Integer year = Integer.parseInt(searchForm.getFinYr());
			Integer dept = Integer.parseInt(searchForm.getDeptid());
			Integer functionaryId = Integer.parseInt(searchForm.getFunctionaryId());
			
			payslipSet = PayrollManagersUtill.getPayRollService().getFunctionaryDeptWisePayBillSummary(month, year, functionaryId,dept);
			request.setAttribute("payslipSet", payslipSet);
			request.setAttribute("username",userName);
			
			return actionMapping.findForward("success");
			
		}catch(Exception e)
		{
			LOGGER.error(e.getMessage());
			return actionMapping.findForward("error");
		}
	
	}
}
