package org.egov.payroll.reports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.payroll.model.BatchFailureDetails;
import org.egov.payroll.services.payslip.IPayslipProcess;

public class PayslipExceptionReportAction extends Action {
	private static final Logger logger = Logger.getLogger(PayslipExceptionReportAction.class);
	private IPayslipProcess payslipProcess;
	
	public IPayslipProcess getPayslipProcess() {
		return payslipProcess;
	}

	public void setPayslipProcess(IPayslipProcess payslipProcess) {
		this.payslipProcess = payslipProcess;
	}

	public ActionForward execute(ActionMapping actionMapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		try{				
			Integer deptid = null;			
			Integer functionaryId=null;			
			SearchForm searchform = null;					
			searchform = (SearchForm)form;
			String userName =(String) request.getSession().getAttribute("com.egov.user.LoginUserName");
			String fromdate = searchform.getFromdate();
			String todate = searchform.getTodate();			
			String billNumberStr =(String)request.getParameter("billNumberStr");		
			if(null != billNumberStr && billNumberStr.lastIndexOf(',') == (billNumberStr.length()-1))
				billNumberStr=billNumberStr.substring(0, billNumberStr.length()-1);		
			String biNumberArr[] =billNumberStr.split(",");				
			logger.info("fromdate="+fromdate+"todate="+todate+"");			
			GregorianCalendar fromDate1 = new GregorianCalendar();
			fromDate1.set(Integer.parseInt(fromdate.split("/")[2]),Integer.parseInt(fromdate.split("/")[1])-1,Integer.parseInt(fromdate.split("/")[0]));
			GregorianCalendar toDate1 = new GregorianCalendar();
			toDate1.set(Integer.parseInt(todate.split("/")[2]),Integer.parseInt(todate.split("/")[1])-1,Integer.parseInt(todate.split("/")[0]));
			deptid=(searchform.getDeptid()!=null && !searchform.getDeptid().trim().equals("")&&!searchform.getDeptid().equals(""))?Integer.parseInt(searchform.getDeptid()):null;
			functionaryId=(searchform.getFunctionaryId()!=null && !searchform.getFunctionaryId().trim().equals("")&&!searchform.getFunctionaryId().equals(""))?Integer.parseInt(searchform.getFunctionaryId()):null;
			List<BatchFailureDetails> listBatchFailure = new ArrayList<BatchFailureDetails>();
			for(int i=0; i<biNumberArr.length; i++)
			{	
				if(null != biNumberArr[i] && ""!= biNumberArr[i] && Integer.valueOf(biNumberArr[i]).intValue() > 0)								
				{
					Integer billNumberId=Integer.valueOf(biNumberArr[i]);
					listBatchFailure.addAll(payslipProcess.generateBatchPayslips(fromDate1, toDate1, deptid, userName, functionaryId,false,null,billNumberId));
				}
			}						
			//ArrayList<BatchFailureDetails> BatchFailureList=(ArrayList)payrollmgr.getPendingPaySlipsList(finyr,month,deptid,empid,type,functionaryId);
			int i=0;	
			String[] empids=new String[listBatchFailure.size()];
			String[] empcodes=new String[listBatchFailure.size()];
			String[] empnames=new String[listBatchFailure.size()];
			String[] fromdates=new String[listBatchFailure.size()];
			String[] todates=new String[listBatchFailure.size()];
			String[] types=new String[listBatchFailure.size()];
			String[] remarks=new String[listBatchFailure.size()];
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			for(BatchFailureDetails batchfail:listBatchFailure ){
				empids[i]=batchfail.getEmployee().getIdPersonalInformation().toString();
				empnames[i]=batchfail.getEmployee().getEmployeeName();
				fromdates[i]=formatter.format(batchfail.getFromDate());
				todates[i]=formatter.format(batchfail.getToDate());
				remarks[i]=batchfail.getRemarks();
				int paytype=batchfail.getPayType().getId().intValue();
				types[i]=paytype+"";			
				empcodes[i++]=batchfail.getEmployee().getEmployeeCode().toString();			
			}
			searchform.setEmpids(empids);
			searchform.setEmpnames(empnames);
			searchform.setFromdates(fromdates);
			searchform.setTodates(todates);
			searchform.setPaytypes(types);
			searchform.setRemarks(remarks);
			searchform.setEmpcodes(empcodes);			
			request.getSession().setAttribute("finYearR", searchform.getFinYr());
			request.getSession().setAttribute("monthR", searchform.getMonth());
			request.getSession().setAttribute("empidR", searchform.getEmpid());
			request.getSession().setAttribute("deptidR", searchform.getDeptid());
			return actionMapping.findForward("success");		
		}catch(Exception e){
			logger.error(e.getMessage());
			//HibernateUtil.rollbackTransaction();
			throw e;
		}	
	}

	
}
