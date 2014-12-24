package org.egov.payroll.reports;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.model.BatchFailureDetails;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.utils.PayrollManagersUtill;

public class SearchAction extends Action {
	private static final Logger logger = Logger.getLogger(SearchAction.class);
	
	public ActionForward execute(ActionMapping actionMapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		try{	
			Integer finyr = null;
			Integer month = null;
			Integer deptid = null;
			Integer empid = null;
			Integer type = null;
			Integer functionaryId=0;
			Integer errPay=0;
			Integer billNoId=null;
			SearchForm searchform = null;
			String mode = request.getParameter("mode");
			searchform = (SearchForm)form;
			logger.info("Mode-----"+mode);						
			if("resolve".equals(mode)){
				logger.info("Mode-----"+mode);	
			}
			else{
				searchform.setFinYr((String)request.getSession().getAttribute("finYearR"));
				searchform.setMonth((String)request.getSession().getAttribute("monthR"));
				searchform.setEmpid((String)request.getSession().getAttribute("empidR"));
				searchform.setDeptid((String)request.getSession().getAttribute("deptidR"));		
				searchform.setBillNumberId((Integer)request.getSession().getAttribute("billno"));
			}
			finyr=(searchform.getFinYr()!=null && !searchform.getFinYr().trim().equals("")&&!searchform.getFinYr().equals("-1"))?Integer.parseInt(searchform.getFinYr()):null;
			month=(searchform.getMonth()!=null && !searchform.getMonth().trim().equals("")&&!searchform.getMonth().equals("-1"))?Integer.parseInt(searchform.getMonth()):null;
			deptid=(searchform.getDeptid()!=null && !searchform.getDeptid().trim().equals("")&&!searchform.getDeptid().equals("-1"))?Integer.parseInt(searchform.getDeptid()):null;
			empid=(searchform.getEmpid()!=null && !( searchform.getEmpid().trim().equals("")||searchform.getEmpid().equals("-1")))?Integer.parseInt(searchform.getEmpid()):null;
			type=(searchform.getType()!=null && !searchform.getType().trim().equals("")&&!searchform.getType().equals("-1"))?Integer.parseInt(searchform.getType()):null;
			functionaryId=(searchform.getFunctionaryId()!=null && !searchform.getFunctionaryId().trim().equals("")&&!searchform.getFunctionaryId().equals("-1"))?Integer.parseInt(searchform.getFunctionaryId()):null;
			billNoId=(searchform).getBillNumberId()!=null && !searchform.getBillNumberId().equals("-1")?searchform.getBillNumberId():null;
			
			if(searchform.getErrorPay()==null || searchform.getErrorPay().toCharArray()[0]=='N')
			{
				errPay = 0;
			}
			else if(searchform.getErrorPay().toCharArray()[0]=='Y')
			{
				errPay = 1;
			}
			PayRollService payRollService = PayrollManagersUtill.getPayRollService();
			ArrayList<BatchFailureDetails> BatchFailureList=(ArrayList)payRollService.getPendingPaySlipsList(finyr,month,deptid,empid,type,functionaryId,billNoId,errPay);
			int i=0;
			String[] deptids=new String[BatchFailureList.size()];
			String[] deptnames=new String[BatchFailureList.size()];
			String[] empids=new String[BatchFailureList.size()];
			String[] empcodes=new String[BatchFailureList.size()];
			String[] empnames=new String[BatchFailureList.size()];
			String[] fromdates=new String[BatchFailureList.size()];
			String[] todates=new String[BatchFailureList.size()];
			String[] types=new String[BatchFailureList.size()];
			String[] remarks=new String[BatchFailureList.size()];
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			for(BatchFailureDetails batchfail:BatchFailureList )
			{
				empids[i]=batchfail.getEmployee().getIdPersonalInformation().toString();
				empnames[i]=batchfail.getEmployee().getEmployeeName();
				fromdates[i]=formatter.format(batchfail.getFromDate());
				todates[i]=formatter.format(batchfail.getToDate());
				remarks[i]=batchfail.getRemarks();
				int paytype=batchfail.getPayType().getId().intValue();
				types[i]=paytype+"";
				deptids[i]=batchfail.getDepartment().getId().toString();	
				empcodes[i]=batchfail.getEmployee().getEmployeeCode().toString();
				deptnames[i]=batchfail.getDepartment().getDeptName().toString();
				i++;
			}
			
			searchform.setEmpids(empids);
			searchform.setEmpnames(empnames);
			searchform.setFromdates(fromdates);
			searchform.setTodates(todates);
			searchform.setDeptids(deptids);
			searchform.setDeptnames(deptnames);
			searchform.setPaytypes(types);
			searchform.setRemarks(remarks);
			searchform.setEmpcodes(empcodes);			
			request.getSession().setAttribute("finYearR", searchform.getFinYr());
			request.getSession().setAttribute("monthR", searchform.getMonth());
			request.getSession().setAttribute("empidR", searchform.getEmpid());
			request.getSession().setAttribute("deptidR", searchform.getDeptid());
			return actionMapping.findForward("success");	
	
		}catch(Exception e)
		{
			logger.error(e.getMessage());
			
			HibernateUtil.rollbackTransaction();
			return actionMapping.findForward("error");
		}
	//	return actionMapping.findForward("failure");
	}

}
