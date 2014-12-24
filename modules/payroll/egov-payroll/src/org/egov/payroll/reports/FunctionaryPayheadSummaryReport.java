/**
 * PaySummaryReport.java created on 10 Aug, 2008 10:16:51 PM 
 */
package org.egov.payroll.reports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.service.EmployeeService;

/**
 * @author Surya
 *
 */
public class FunctionaryPayheadSummaryReport extends Action {

	private static final Logger logger = Logger.getLogger(SearchAction.class);
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
	
	
	public ActionForward execute(ActionMapping actionMapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		try{	
			SearchForm searchForm = (SearchForm)form;
			List<HashMap> payslipSet = new ArrayList<HashMap>();
			Integer month = Integer.parseInt(searchForm.getMonth());
			Integer year = Integer.parseInt(searchForm.getFinYr());
			Integer billNumberId = (searchForm.getBillNumberId());
			logger.info("month--"+month+"/year"+year);
			/*GregorianCalendar fromDate = new GregorianCalendar();
			fromDate.setTime(FORMATTER.parse(searchForm.getFromdate()));
			GregorianCalendar toDate=new GregorianCalendar();
			toDate.setTime(FORMATTER.parse(searchForm.getTodate()));*/
			Integer functionaryIds[] = new Integer[searchForm.getFunctionaryIds().length];
			int i = 0;
			for(String funcId : searchForm.getFunctionaryIds()){
				functionaryIds[i] = Integer.parseInt(funcId);
				i++;
			}
			payslipSet = PayrollManagersUtill.getPayRollService().getFunctionaryPayheadSummary(month, year, functionaryIds,billNumberId);
			request.setAttribute("payslipSet", payslipSet);
			request.setAttribute("billno",searchForm.getBillNumber());  
			return actionMapping.findForward("success");	
		}catch(Exception e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
			HibernateUtil.rollbackTransaction();
			return actionMapping.findForward("error");
		}
	
	}

	
}
