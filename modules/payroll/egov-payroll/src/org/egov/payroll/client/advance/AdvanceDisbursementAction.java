/*
 * AdvanceDisbursementAction.java Created on Oct 31, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.payroll.client.advance;

/*import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVException;
import org.egov.commons.Bankaccount;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Chequedetail;
import org.egov.commons.EgSurrenderedCheques;
import org.egov.commons.EgwSatuschange;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.transaction.integration.dataobjects.GeneralLedgerEnteries;
import org.egov.infstr.transaction.integration.dataobjects.GeneralLedgerPosting;
import org.egov.infstr.transaction.integration.dataobjects.GlDetailEntry;
import org.egov.infstr.transaction.integration.dataobjects.Instrument;
import org.egov.infstr.transaction.integration.dataobjects.Instruments;
import org.egov.infstr.transaction.integration.dataobjects.MisData;
import org.egov.infstr.transaction.integration.dataobjects.MisDataCollection;
import org.egov.infstr.transaction.integration.dataobjects.Mode;
import org.egov.infstr.transaction.integration.dataobjects.Voucher;
import org.egov.infstr.transaction.integration.dataobjects.Voucherheader;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentManager;
import org.egov.lib.rjbac.utils.GetRjbacManagers;
import org.egov.payroll.model.Advance;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.services.advance.AdvanceService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollManagersUtill;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.reports.CommnFunctions;
import com.exilant.eGov.src.transactions.CommonMethodsI;
import com.exilant.eGov.src.transactions.CommonMethodsImpl;
import com.exilant.eGov.src.transactions.FinancialTransactions;
*/
/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish
 * @version 1.00

 */

/*public class AdvanceDisbursementAction extends DispatchAction
{
	public static final Logger logger = Logger.getLogger(AdvanceDisbursementAction.class);	
	Timestamp curDate = new Timestamp(System.currentTimeMillis());
    static	final String DATEFORMATSLASH="dd/MM/yyyy";
    static final String DATEFORMATHYPHEN="dd-MMM-yyyy";
    static final String CREATEDISBURSEMENT="createDisbursement";
    static final String DEDUCTIONADVANCE="Deduction-Advance";
    static final String MODE="mode";
    static final String SALCODESLIST="salCodesList";
    private static final String VHID="vhId";
    private static final String ERROR= "error";
    private static final String LOGINUSERID="com.egov.user.LoginUserId";
	SimpleDateFormat sdf =new SimpleDateFormat(DATEFORMATSLASH,Locale.getDefault());
	SimpleDateFormat format =new SimpleDateFormat(DATEFORMATHYPHEN,Locale.getDefault());
	NumberFormat nf=new DecimalFormat("##############.00");

	EGovernCommon egc = new EGovernCommon();
	
    public ActionForward beforeCreateDisbursement(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
     throws IOException,ServletException
    {
        String target ="";                
        ArrayList salCodesList=null;
        try
        {
        	logger.debug(" inside beforeCreateDisbursement");
        	AdvanceManager sam = PayrollManagersUtill.getSalaryadvanceManager();      	
        	salCodesList = (ArrayList)sam.getSalaryCodesByCategoryName(DEDUCTIONADVANCE);        	 	
        	req.getSession().setAttribute(SALCODESLIST, salCodesList);
        	req.setAttribute(MODE,"search");
			target=CREATEDISBURSEMENT;
        }
        catch(Exception ex)
		{
		    target = ERROR;
		    logger.error(ex.getMessage());
		    HibernateUtil.rollbackTransaction();
		}
		return mapping.findForward(target);
    }
    
    public ActionForward searchAdvanceDetails(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
    throws IOException,ServletException
   {
       String target ="";                
       ArrayList advSanctionList=null;     
       ArrayList bankBranchList=null;
       ArrayList cashierList=null;
       ArrayList salCodesList=null;
       try
       {
			logger.debug(" inside searchAdvanceDetails");
			AdvanceManager sam = PayrollManagersUtill.getSalaryadvanceManager();	
			CommonsManager cm = PayrollManagersUtill.getCommonsManager();		
			DepartmentManager dmag	=	GetRjbacManagers.getDepartmentManager();
			AdvanceDisbursementForm adf = (AdvanceDisbursementForm)form;
			EgwStatus status = cm.getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_SANCTION);
			String paymentType=null;
			SalaryCodes salCode=null;
			Department dept=null;
			Fund fund=null;
			if(adf.getDisbMethod()!=null && !adf.getDisbMethod().equals("0"))
			{
				paymentType=adf.getDisbMethod();
			}
			if(adf.getAdvanceType()!=null && !adf.getAdvanceType().equals("0"))
			{
				salCode=sam.getSalaryCodesById(Integer.parseInt(adf.getAdvanceType()));
			}
			if(adf.getDepartment()!=null && !adf.getDepartment().equals("0"))
			{
				dept=dmag.getDepartment(Integer.parseInt(adf.getDepartment()));
			}
			if(adf.getFund()!=null && !adf.getFund().equals("0"))
			{
				fund=cm.getFundById(Integer.parseInt(adf.getFund()));
			}
			advSanctionList=(ArrayList)sam.getSalAdvancesFilterBy(paymentType, dept, salCode, fund, status);
			      		
			BigDecimal totalSancAmt=new BigDecimal(0);
			Advance salAdvance=null;
			if(advSanctionList !=null && advSanctionList.size()>0)
			{				
				for(Iterator it = advSanctionList.iterator(); it.hasNext(); ) 
				{								
					salAdvance=(Advance)it.next();					
					totalSancAmt=totalSancAmt.add(salAdvance.getAdvanceAmt());
				}
			}
					
			adf.setTotalAmount(nf.format(totalSancAmt));
			bankBranchList=(ArrayList)egc.getBankAndBranch(HibernateUtil.getCurrentSession().connection(),adf.getFund());			
			cashierList=(ArrayList)egc.getCashierNameList(HibernateUtil.getCurrentSession().connection());
			Date date = new Date();
			String currDate = sdf.format(date);
			req.getSession().setAttribute("currentDate", currDate);
			salCodesList = (ArrayList)sam.getSalaryCodesByCategoryName(DEDUCTIONADVANCE);    	 	
        	req.getSession().setAttribute(SALCODESLIST, salCodesList);
			req.getSession().setAttribute("advSanctionList", advSanctionList);			
			req.getSession().setAttribute("bankBranchList", bankBranchList);
			req.getSession().setAttribute("cashierList", cashierList);
			req.setAttribute(MODE,"create");
			target=CREATEDISBURSEMENT;
       }
       catch(Exception ex)
		{
		    target = ERROR;
		    logger.error(ex.getMessage());
		    HibernateUtil.rollbackTransaction();
		}
		return mapping.findForward(target);
   }
    
    public ActionForward getEmployeeDetails(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
    throws IOException,ServletException
    {
       String target ="";                
       ArrayList advSanctionList=null;    
       try
       {
			logger.debug(" inside getEmployeeDetails");
			AdvanceManager sam = PayrollManagersUtill.getSalaryadvanceManager();	
			CommonsManager cm = PayrollManagersUtill.getCommonsManager();		
			DepartmentManager dmag	=	GetRjbacManagers.getDepartmentManager();
			AdvanceDisbursementForm adf = (AdvanceDisbursementForm)form;
			EgwStatus status=null;
			if(req.getParameter(MODE).equalsIgnoreCase("create"))
			{
				status = cm.getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_SANCTION);
			}
			else
			{
				status = cm.getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_DISBURSE);
			}
			String disbMethod=req.getParameter("disbMethod");
			String department=req.getParameter("department");
			String advanceType=req.getParameter("advanceType");
			String fundId=req.getParameter("fund");
			String paymentType=null;
			SalaryCodes salCode=null;
			Department dept=null;
			Fund fund=null;
			if(disbMethod!=null && !disbMethod.equals("0"))
			{
				paymentType=disbMethod;
			}
			if(advanceType!=null && !advanceType.equals("0"))
			{
				salCode=sam.getSalaryCodesById(Integer.parseInt(advanceType));
			}
			if(department!=null && !department.equals("0"))
			{
				dept=dmag.getDepartment(Integer.parseInt(department));
			}
			if(fundId!=null && !fundId.equals("0"))
			{
				fund=cm.getFundById(Integer.parseInt(adf.getFund()));
			}
			
			advSanctionList=(ArrayList)sam.getSalAdvancesFilterBy(paymentType, dept, salCode, fund, status);				    
			req.getSession().setAttribute("advSanctionList", advSanctionList);			
			target="getEmployeeDetails";
       }
       catch(Exception ex)
		{
		    target = ERROR;
		    logger.error(ex.getMessage());
		    HibernateUtil.rollbackTransaction();
		}
		return mapping.findForward(target);
   }
    
    public ActionForward createDisursement(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target ="";
		String alertMessage=null;
		try
		{
			logger.debug(" inside createDisursement");
			AdvanceDisbursementForm adf = (AdvanceDisbursementForm)form;			
			CommonsManager cmnMngr = PayrollManagersUtill.getCommonsManager();			
			AdvanceManager sam = PayrollManagersUtill.getSalaryadvanceManager();	
			DepartmentManager dmag	=	GetRjbacManagers.getDepartmentManager();
			int fundId=Integer.parseInt(adf.getFund());			
	   		CommonMethodsI cm=new CommonMethodsImpl();
			Voucher v=new Voucher();
	   		Voucherheader vh=new Voucherheader();
	   		Mode mode=new Mode();
	   		MisDataCollection mdc=new MisDataCollection();
	   		MisData mi=new MisData();
	   		Instruments cqs=new Instruments();
	   		Instrument cq[]=new Instrument[1];
	   		GeneralLedgerEnteries glentry=new GeneralLedgerEnteries();	   			   	
	   		GeneralLedgerPosting gl[]=new GeneralLedgerPosting[2];	   		
	   		int glSize=0;
	   		EgwStatus status = cmnMngr.getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_SANCTION);
			String paymentType=null;
			SalaryCodes salCode=null;
			Department dept=null;
			Fund fund=null;
			if(adf.getDisbMethod()!=null && !adf.getDisbMethod().equals("0"))
			{
				paymentType=adf.getDisbMethod();
			}
			if(adf.getAdvanceType()!=null && !adf.getAdvanceType().equals("0"))
			{
				salCode=sam.getSalaryCodesById(Integer.parseInt(adf.getAdvanceType()));
			}
			if(adf.getDepartment()!=null && !adf.getDepartment().equals("0"))
			{
				dept=dmag.getDepartment(Integer.parseInt(adf.getDepartment()));
			}
			if(adf.getFund()!=null && !adf.getFund().equals("0"))
			{
				fund=cmnMngr.getFundById(Integer.parseInt(adf.getFund()));
			}
			ArrayList advSanctionList =(ArrayList)sam.getSalAdvancesFilterBy(paymentType, dept, salCode, fund, status);
	   		if(advSanctionList!=null && advSanctionList.size()>0)
	   		{
	   			glSize=advSanctionList.size();
	   		}
	   		
	   		GlDetailEntry glDet[]=new GlDetailEntry[glSize];
	   		vh.setVoucherdate(getDateString(adf.getVoucherDate()));
	   		String fiscalPeriodId=cm.getFiscalPeriod(vh.getVoucherdate(),HibernateUtil.getCurrentSession().connection());
	   		String vhNum=null;
	   		String eg_voucher;
	   		String cgvn;
	   		vhNum=egc.vNumber(HibernateUtil.getCurrentSession().connection(),Integer.toString(fundId),adf.getVoucherNo());
  			String vType=vhNum.substring(0,2);
			eg_voucher=egc.getEg_Voucher(vType,fiscalPeriodId,HibernateUtil.getCurrentSession().connection());
			for(int i=eg_voucher.length();i<5;i++)
			{
				 eg_voucher="0"+eg_voucher;
			}
			cgvn=vType+eg_voucher;
			vh.setCgvn(cgvn);
			if(!egc.isUniqueVN(vhNum,vh.getVoucherdate(),HibernateUtil.getCurrentSession().connection()))
			{
				throw new EGOVException("Error: Duplicate Voucher Number");
			}
	   		vh.setVouchernumber(vhNum);
	   		vh.setCgn("DBP"+egc.getCGNumber());
	   		vh.setFund(fundId);
	   		vh.setDescription("Advance Disbursement");
			vh.setName("Bank Payment");
			vh.setType("Payment");
			vh.setModuleId(7);//Module Id=7 for Payroll
			
			int userId=((Integer)req.getSession().getAttribute(LOGINUSERID)).intValue();
			vh.setUserId(userId);
			v.setVoucherheader(vh);

			mode.setBankAccountId(Integer.parseInt(adf.getBankAccount()));
			if(adf.getPaidTo()==null || "".equals(adf.getPaidTo()))
			{
				mdc.setPaidTo(" ");
			}
			else
			{
				mdc.setPaidTo(adf.getPaidTo());
			}
			if(adf.getDepartment()!=null && !adf.getDepartment().equals("0"))
			{
				mi.setDepartment(Integer.parseInt(adf.getDepartment()));
			}
			mdc.setMisData(mi);

			String chqNo=null, chqDate=null;
			String autoGenerateChqNo= EGovConfig.getProperty("egf_config.xml","autoGenerateChqNo","","autoGenerateCheck");
			//if Auto Generate Cheque No Configuration is Yes then Auto Generate the Cheque Number
			if(autoGenerateChqNo.equalsIgnoreCase("Y"))
			{
				String availChqNoList[]= egc.getNextChequeNo(HibernateUtil.getCurrentSession().connection(),adf.getBankAccount(),1,1);
				logger.debug("Auto Generated availChqNoList[]-->"+availChqNoList);			
				if(availChqNoList[1]==null)
				{
					throw new EGOVException("Error: No Cheques are available for this account");					
				}

				chqNo=availChqNoList[1];
				chqDate=sdf.format(curDate);
				logger.debug("Auto Generated Cheque No1-->"+chqNo);
			}
			else
			{
				chqNo=adf.getChequeNo();
				chqDate=adf.getChequeDate();

				if(!egc.isUniqueChequeNo(chqNo,adf.getBankAccount(),HibernateUtil.getCurrentSession().connection()))
					throw new EGOVException("Error: Duplicate Cheque Number");

				if(!egc.isChqNoWithinRange(chqNo,adf.getBankAccount(),HibernateUtil.getCurrentSession().connection()))
					throw new EGOVException("Error: Invalid Cheque Number");
			}

			cq[0]=new Instrument();
			cq[0].setInstrumentnumber(chqNo);
			cq[0].setInstrumentdate(chqDate);
			cq[0].setInstrumentamount(Double.parseDouble(adf.getTotalAmount()));
			cqs.setInstrument(cq);
			mode.setInstruments(cqs);
			if("0".equals(adf.getPaidBy()))
			{
				mdc.setPaidBy(null);
			}
			else
			{
			mdc.setPaidBy(adf.getPaidBy());
			}
			mdc.setMode(mode);
			v.setMisDataCollection(mdc);

			String bankAccCodeAndName=cm.getBankCode(Integer.parseInt(adf.getBankAccount()),HibernateUtil.getCurrentSession().connection());
			String bankAccCodeName[]=bankAccCodeAndName.split("#");
			String bankGlCode=bankAccCodeName[0];

			int calGlSize=0;			
			gl[0]=new GeneralLedgerPosting();
			gl[0].setCredit(0);
			gl[0].setDebit(Double.parseDouble(adf.getTotalAmount()));
			gl[0].setGlcode(salCode.getChartofaccounts().getGlcode());
			
			int detailTypeId=cmnMngr.getDetailTypeId(salCode.getChartofaccounts().getGlcode(),HibernateUtil.getCurrentSession().connection());
			if(detailTypeId!=0)
			{														
				if(advSanctionList!=null && advSanctionList.size()>0)
		   		{
					for(Iterator it = advSanctionList.iterator(); it.hasNext(); ) 
					{					
						Advance salAdvance=(Advance)it.next();	  
						glDet[calGlSize]=new GlDetailEntry();
						glDet[calGlSize].setDetailGlcode(salCode.getChartofaccounts().getGlcode());
						glDet[calGlSize].setDetailTypeId(detailTypeId);
						glDet[calGlSize].setDetailKeyId(salAdvance.getEmployee().getIdPersonalInformation());
						glDet[calGlSize].setDetailKeyAmount(Double.parseDouble(salAdvance.getAdvanceAmt().toString()));
						gl[0].setGlDetailEntry(glDet);
						calGlSize++;     
					}
		   		}
			}
				
			gl[1]=new GeneralLedgerPosting();
			gl[1].setCredit(Double.parseDouble(adf.getTotalAmount()));
			gl[1].setDebit(0);
			gl[1].setGlcode(bankGlCode);
			CChartOfAccounts coa=cmnMngr.getCChartOfAccountsByGlCode(bankGlCode);
			detailTypeId=cmnMngr.getDetailTypeId(bankGlCode,HibernateUtil.getCurrentSession().connection());
			if(detailTypeId!=0)
			{				
				if(advSanctionList!=null && advSanctionList.size()>0)
		   		{
					for(Iterator it = advSanctionList.iterator(); it.hasNext(); ) 
					{					
						Advance salAdvance=(Advance)it.next();	  
						glDet[calGlSize]=new GlDetailEntry();
						glDet[calGlSize].setDetailGlcode(coa.getGlcode());						
						glDet[calGlSize].setDetailTypeId(detailTypeId);
						glDet[calGlSize].setDetailKeyId(salAdvance.getEmployee().getIdPersonalInformation());
						glDet[calGlSize].setDetailKeyAmount(Double.parseDouble(salAdvance.getAdvanceAmt().toString()));
						gl[1].setGlDetailEntry(glDet);
						calGlSize++;	
					}
		   		}
			}

			glentry.setGeneralLedgerPosting(gl);
			v.setGeneralLedgerEnteries(glentry);
			FinancialTransactions ft=new FinancialTransactions();
			Voucherheader voucherHeader=ft.postTransaction(v,HibernateUtil.getCurrentSession().connection());

			if(advSanctionList!=null && advSanctionList.size()>0)
	   		{
				for(Iterator it = advSanctionList.iterator(); it.hasNext(); ) 
				{
					Advance salAdvance=(Advance)it.next();
   					status=null;
					status = cmnMngr.getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_DISBURSE);						
					salAdvance.setStatus(status); 
					CVoucherHeader vchrHdr=cmnMngr.findVoucherHeaderById(Long.parseLong(voucherHeader.getVoucherHeaderId()));
					salAdvance.setVoucherheader(vchrHdr);
					salAdvance.setLastmodifiedby(Long.parseLong(req.getSession().getAttribute(LOGINUSERID).toString()));
					salAdvance.setLastmodifieddate(curDate);
					sam.updateSalaryadvance(salAdvance);
					
					EgwSatuschange egwSatuschange=new EgwSatuschange();
					egwSatuschange.setModuletype("Salaryadvance");
					egwSatuschange.setModuleid(salAdvance.getId());
					status=cmnMngr.getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_SANCTION);
					egwSatuschange.setFromstatus(status.getId());
					status=cmnMngr.getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_DISBURSE);
					egwSatuschange.setTostatus(status.getId());
					egwSatuschange.setCreatedby((Integer)req.getSession().getAttribute(LOGINUSERID));
					cmnMngr.createEgwSatuschange(egwSatuschange);   				
	   			}
	   		}
			
			req.setAttribute(MODE,"search");
			req.setAttribute("buttonType",req.getParameter("button"));
			req.setAttribute(VHID,voucherHeader.getVoucherHeaderId());	
			HibernateUtil.getCurrentSession().flush();	
			
			if(autoGenerateChqNo.equalsIgnoreCase("Y"))
				alertMessage="Executed successfully. Cheque No. used is "+chqNo;
			else
				alertMessage="Executed successfully";
			target=CREATEDISBURSEMENT;
		}
		catch(EGOVException ex)
       {
           target = CREATEDISBURSEMENT;
           alertMessage=ex.getMessage();
           logger.error("excpetion message "+ex.getMessage());
           HibernateUtil.rollbackTransaction();
       }
		catch(Exception ex)
		{
		    target = ERROR;
		    logger.error(ex.getMessage());
		    HibernateUtil.rollbackTransaction();
		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
   }
    
    public ActionForward beforePrintDisbursement(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target ="";		
		try
		{
			logger.debug(" inside beforePrintDisbursement");
			String vhId=req.getParameter(VHID);
			logger.debug("vhId-->>"+vhId);
			AdvanceDisbursementForm adf = (AdvanceDisbursementForm)form;			
			CommonsManager cmnMngr = PayrollManagersUtill.getCommonsManager();
			AdvanceManager sam = PayrollManagersUtill.getSalaryadvanceManager();			
			DepartmentManager dmag	=	GetRjbacManagers.getDepartmentManager();
			CommnFunctions cf=new CommnFunctions();
			CVoucherHeader vchrHdr=cmnMngr.findVoucherHeaderById(Long.parseLong(vhId));		   
			ArrayList disbursementList=(ArrayList)sam.getSalAdvancesByVoucherHeader(vchrHdr);
			Fund fund=cmnMngr.getFundById(vchrHdr.getFundId().getId());
			adf.setFundName(fund.getName());	        
	        adf.setVoucherNo(vchrHdr.getVoucherNumber());
	        adf.setVoucherDate(sdf.format(vchrHdr.getVoucherDate()));
	        String deptId=null;	        
	        deptId=egc.getDeptIdFromVoucherMis(HibernateUtil.getCurrentSession().connection(), vchrHdr.getId().toString());
	        if(deptId!=null)
	        {
	        	Department dept = dmag.getDepartment(Integer.parseInt(adf.getDepartment()));
	        	adf.setDepartment(dept.getDeptName());
	        }
	        String disbMethod=null,dept=null,advanceType=null;
	        BigDecimal totalSancAmt=new BigDecimal(0);
			Advance salAdvance=null;
			if(disbursementList !=null && disbursementList.size()>0)
			{	
				for(Iterator it = disbursementList.iterator(); it.hasNext(); ) 
				{								
					salAdvance=(Advance)it.next();	
					disbMethod=salAdvance.getPaymentType();
					advanceType=salAdvance.getSalaryCodes().getHead();
					totalSancAmt=totalSancAmt.add(salAdvance.getAdvanceAmt());					
				}
			}			       
		
			adf.setTotalAmount(cf.numberToString(totalSancAmt.toString()).toString());	
			if(dept!=null)
				adf.setDepartment(dept);
			adf.setAdvanceType(advanceType);
			if(disbMethod.equalsIgnoreCase("cash"))
				adf.setDisbMethod("Cash");
			else if(disbMethod.equalsIgnoreCase("dbt"))
				adf.setDisbMethod("Direct Bank Transfer");
			
	   		ArrayList chequedetailList=(ArrayList)cmnMngr.getChequedetailByVoucherheader(vchrHdr);
	   		for (Iterator cdItr = chequedetailList.iterator(); cdItr.hasNext(); )
            {
	   			Chequedetail chequedetail=(Chequedetail)cdItr.next();
	   			if(chequedetail.getBankbranch()!=null)
	   				adf.setBank(chequedetail.getBankbranch().getBranchname()+" - "+chequedetail.getBank().getName());
	   			if(chequedetail.getBankaccount()!=null)
	   				adf.setBankAccount(chequedetail.getBankaccount().getAccountnumber());
	   			adf.setChequeNo(chequedetail.getChequenumber());    			
	   			adf.setChequeDate(sdf.format(chequedetail.getChequedate()));  
	   			if(chequedetail.getPaidbyid()!=null)
	   			{
	   				String paidBy=egc.getBillCollectorName(HibernateUtil.getCurrentSession().connection(), chequedetail.getPaidbyid().toString());
	   				adf.setPaidBy(paidBy);
	   			}
	   			if(chequedetail.getPayto()!=null)
	   				adf.setPaidTo(chequedetail.getPayto());
            }    		
			req.getSession().setAttribute(MODE,"print");
			target="printDetails";
		}
		catch(Exception ex)
		{
		    target = ERROR;
		    logger.error(ex.getMessage());
		    HibernateUtil.rollbackTransaction();
		}
		return mapping.findForward(target);
   }
    
    public ActionForward beforeDisbursementList(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target ="";
		 ArrayList salCodesList=null;
		try
		{
			logger.debug(" inside beforeDisbursementList");
			AdvanceManager sam = PayrollManagersUtill.getSalaryadvanceManager();
			salCodesList = (ArrayList)sam.getSalaryCodesByCategoryName(DEDUCTIONADVANCE); 	
        	req.getSession().setAttribute(SALCODESLIST, salCodesList);			
			req.setAttribute(MODE,req.getParameter(MODE));
			target="disbursementList";
		}
		catch(Exception ex)
		{
		    target = ERROR;
		    logger.error(ex.getMessage());
		    HibernateUtil.rollbackTransaction();
		}
		return mapping.findForward(target);
   }
   
    public ActionForward getDisbursementList(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target ="";
		ArrayList<Advance> disbursementList=null;
		ArrayList salCodesList=null;
		try
		{
			logger.debug(" insidec getDisbursementList");
			AdvanceDisbursementForm adf = (AdvanceDisbursementForm)form;	
			AdvanceManager sam = PayrollManagersUtill.getSalaryadvanceManager();
			CommonsManager cmnMngr = PayrollManagersUtill.getCommonsManager();		
			EgwStatus status = cmnMngr.getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_DISBURSE);	       
	       	salCodesList = (ArrayList)sam.getSalaryCodesByCategoryName(DEDUCTIONADVANCE);	       	
	       	String paymentType=null;
			SalaryCodes salCode=null;			
			Fund fund=null;
			if(adf.getDisbMethod()!=null && !adf.getDisbMethod().equals("0"))
				paymentType=adf.getDisbMethod();
			if(adf.getAdvanceType()!=null && !adf.getAdvanceType().equals("0"))
			{
				salCode=sam.getSalaryCodesById(Integer.parseInt(adf.getAdvanceType()));
			}			
			if(adf.getFund()!=null && !adf.getFund().equals("0"))
			{
				fund=cmnMngr.getFundById(Integer.parseInt(adf.getFund()));
			}
			disbursementList=(ArrayList)sam.getSalAdvancesFilterBy(paymentType, null, salCode, fund, status);	
						
			req.getSession().setAttribute(SALCODESLIST, salCodesList);
			req.setAttribute("disbursementList",disbursementList);
			String mode=req.getParameter(MODE);			
			req.setAttribute(MODE,mode);
			target="disbursementList";
		}
		catch(Exception ex)
		{
			target = ERROR;
			logger.error(ex.getMessage());
			HibernateUtil.rollbackTransaction();
		}
		return mapping.findForward(target);
	}
    
    public ActionForward beforeViewAndModifyAdvDisb(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target ="";
		ArrayList advSanctionList=null;     
		ArrayList bankBranchList=null;
		ArrayList cashierList=null;
		ArrayList salCodesList=null;
		
		try
		{
			logger.debug(" inside beforeViewAndModifyAdvDisb");
			AdvanceDisbursementForm adf = (AdvanceDisbursementForm)form;	
			AdvanceManager sam = PayrollManagersUtill.getSalaryadvanceManager();			
			CommonsManager cmnMngr = PayrollManagersUtill.getCommonsManager();		
			String vhId=req.getParameter(VHID);			
			logger.debug("vhId-->>"+vhId);
						
			CVoucherHeader vchrHdr=cmnMngr.findVoucherHeaderById(Long.parseLong(vhId));		   
			advSanctionList=(ArrayList)sam.getSalAdvancesByVoucherHeader(vchrHdr);
			Fund fund=cmnMngr.getFundById(vchrHdr.getFundId().getId());			
			adf.setFund(fund.getId().toString());
	        adf.setVoucherNo(vchrHdr.getVoucherNumber());
	        adf.setVoucherDate(sdf.format(vchrHdr.getVoucherDate()));	        
	        String deptId=null;	        
	        deptId=egc.getDeptIdFromVoucherMis(HibernateUtil.getCurrentSession().connection(), vchrHdr.getId().toString());
	        if(deptId!=null)
	        	adf.setDepartment(deptId);
	        
	        BigDecimal totalSancAmt=new BigDecimal(0);
	        String advType=null, disbMethod=null;
			Advance salAdvance=null;
			if(advSanctionList !=null && advSanctionList.size()>0)
			{
				for(Iterator it = advSanctionList.iterator(); it.hasNext(); ) 
				{								
					salAdvance=(Advance)it.next();					
					advType=salAdvance.getSalaryCodes().getId().toString();
					disbMethod=salAdvance.getPaymentType();
					totalSancAmt=totalSancAmt.add(salAdvance.getAdvanceAmt());					
				}
			}			
			
			adf.setAdvanceType(advType);
			adf.setDisbMethod(disbMethod);
			adf.setTotalAmount(nf.format(totalSancAmt));			
	        
			bankBranchList=(ArrayList)egc.getBankAndBranch(HibernateUtil.getCurrentSession().connection(),fund.getId().toString());		
			cashierList=(ArrayList)egc.getCashierNameList(HibernateUtil.getCurrentSession().connection());
			
	   		ArrayList chequedetailList=(ArrayList)cmnMngr.getChequedetailByVoucherheader(vchrHdr);
	   		for (Iterator cdItr = chequedetailList.iterator(); cdItr.hasNext(); )
            {
	   			Chequedetail chequedetail=(Chequedetail)cdItr.next();
	   			if(chequedetail.getBankbranch()!=null)
	   				adf.setBank(chequedetail.getBankbranch().getId().toString());
	   			if(chequedetail.getBankaccount()!=null)
	   				adf.setBankAccount(chequedetail.getBankaccount().getId().toString());
	   			adf.setChequeNo(chequedetail.getChequenumber());    			
	   			adf.setChequeDate(sdf.format(chequedetail.getChequedate()));  
	   			if(chequedetail.getPaidbyid()!=null)
	   			{	   				
	   				adf.setPaidBy(chequedetail.getPaidbyid().toString());
	   			}
	   			if(chequedetail.getPayto()!=null)
	   				adf.setPaidTo(chequedetail.getPayto());
	   			BigDecimal balance=egc.getAccountBalance(getDateString(sdf.format(vchrHdr.getVoucherDate())), HibernateUtil.getCurrentSession().connection(),chequedetail.getBankaccount().getId().toString());
	   			adf.setBalance(balance.toString());
            } 
	   		salCodesList = (ArrayList)sam.getSalaryCodesByCategoryName(DEDUCTIONADVANCE);  
	   		
	   		Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMATSLASH,Locale.getDefault());
			String currDate = sdf.format(date);
			req.getSession().setAttribute("currentDate", currDate);
	   		req.getSession().setAttribute(SALCODESLIST, salCodesList);		
			req.getSession().setAttribute("advSanctionList", advSanctionList);			
			req.getSession().setAttribute("bankBranchList", bankBranchList);
			req.getSession().setAttribute("cashierList", cashierList);
			req.setAttribute(MODE,req.getParameter(MODE));
			target="viewModifyDisbursement";
		}
		catch(Exception ex)
		{
		    target = ERROR;
		    logger.error(ex.getMessage());
		    HibernateUtil.rollbackTransaction();
		}
		return mapping.findForward(target);
	}
    
    public ActionForward modifyDisbursement(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target ="";
		String alertMessage=null;
		try
		{
			logger.debug(" inside modifyDisbursement");
			AdvanceDisbursementForm adf = (AdvanceDisbursementForm)form;			
			CommonsManager cmnMngr = PayrollManagersUtill.getCommonsManager();			
			AdvanceManager sam = PayrollManagersUtill.getSalaryadvanceManager();	
			String vhId=req.getParameter(VHID);
			logger.debug("vhId-->> "+vhId);
			CVoucherHeader voucherheader=cmnMngr.findVoucherHeaderById(Long.parseLong(vhId));
			int fundId=Integer.parseInt(adf.getFund());			
	   		CommonMethodsI cm=new CommonMethodsImpl();
			Voucher v=new Voucher();
	   		Voucherheader vh=new Voucherheader();
	   		Mode mode=new Mode();
	   		MisDataCollection mdc=new MisDataCollection();
	   		MisData mi=new MisData();
	   		Instruments cqs=new Instruments();
	   		Instrument cq[]=new Instrument[1];
	   		GeneralLedgerEnteries glentry=new GeneralLedgerEnteries();	   		
	   		GeneralLedgerPosting gl[]=new GeneralLedgerPosting[2];	   		
	   		int glSize=0;	
	   					
	   		ArrayList advSanctionList = (ArrayList)sam.getSalAdvancesByVoucherHeader(voucherheader);
	   		if(advSanctionList!=null && advSanctionList.size()>0)
	   		{
	   			glSize=advSanctionList.size();
	   		}
	   		GlDetailEntry glDet[]=new GlDetailEntry[glSize];
	   		vh.setVoucherdate(getDateString(adf.getVoucherDate()));	   			   		
	   		String vhNum=adf.getVoucherNoPrefix()+adf.getVoucherNo();   						
			vh.setCgvn(voucherheader.getCgvn());
			if(!vhNum.equals(voucherheader.getVoucherNumber())&& !egc.isUniqueVN(vhNum,vh.getVoucherdate(),HibernateUtil.getCurrentSession().connection()))
				throw new EGOVException("Error: Duplicate Voucher Number");
	   		vh.setVouchernumber(vhNum);	   		
	   		vh.setOldVoucherId(voucherheader.getId().toString());	   	
	   		vh.setCgn(voucherheader.getCgn());
	   		vh.setFund(fundId);
	   		vh.setDescription("Advance Disbursement");
			vh.setName("Bank Payment");
			vh.setType("Payment");
			vh.setModuleId(7);//Module Id=7 for Payroll
			int userId=((Integer)req.getSession().getAttribute(LOGINUSERID)).intValue();
			vh.setUserId(userId);
			v.setVoucherheader(vh);

			mode.setBankAccountId(Integer.parseInt(adf.getBankAccount()));
			if(adf.getPaidTo()==null || "".equals(adf.getPaidTo()))
				mdc.setPaidTo(" ");
			else
				mdc.setPaidTo(adf.getPaidTo());
			if(adf.getDepartment()!=null && !adf.getDepartment().equals("0"))
				mi.setDepartment(Integer.parseInt(adf.getDepartment()));
			mdc.setMisData(mi);

			//If Cheque is surrendered then insert into EG_SURRENDRED_CHEQUES table 
			if(adf.getIsChqSurrendered())
			{
				EgSurrenderedCheques egSurrenderedCheques = new EgSurrenderedCheques();				
				Bankaccount bankAccount = (Bankaccount)cmnMngr.getBankaccountById(Integer.parseInt(adf.getBankAccount()));
				egSurrenderedCheques.setBankaccount(bankAccount);
				egSurrenderedCheques.setVoucherheader(voucherheader);
				egSurrenderedCheques.setChequenumber(adf.getChequeNo());
				egSurrenderedCheques.setChequedate(getDate(adf.getChequeDate()));
				egSurrenderedCheques.setLastmodifieddate(curDate);
				cmnMngr.createEgSurrenderedCheques(egSurrenderedCheques);
			}
			String chqNo=null, chqDate=null;
			String autoGenerateChqNo= EGovConfig.getProperty("egf_config.xml","autoGenerateChqNo","","autoGenerateCheck");
			//if Auto Generate Cheque No Configuration is Yes and Cheque Is Surrendered then Auto Generate the Cheque Number
			if(autoGenerateChqNo.equalsIgnoreCase("Y") && adf.getIsChqSurrendered()==true)
			{
				String availChqNoList[]= egc.getNextChequeNo(HibernateUtil.getCurrentSession().connection(),adf.getBankAccount(),1,1);
				logger.debug("Auto Generated availChqNoList[]-->"+availChqNoList);			
				if(availChqNoList[1]==null)
				{
					throw new EGOVException("Error: No Cheques are available for this account");					
				}

				chqNo=availChqNoList[1];
				chqDate=sdf.format(curDate);
				logger.debug("Auto Generated Cheque No1-->"+chqNo);
			}			
			else if(!autoGenerateChqNo.equalsIgnoreCase("Y") && adf.getIsChqSurrendered()==true)
			{
				chqNo=adf.getNewChequeNo();
				chqDate=adf.getNewChequeDate();

				if(!egc.isUniqueChequeNo(chqNo,adf.getBankAccount(),HibernateUtil.getCurrentSession().connection()))
					throw new EGOVException("Error: Duplicate Cheque Number");

				if(!egc.isChqNoWithinRange(chqNo,adf.getBankAccount(),HibernateUtil.getCurrentSession().connection()))
					throw new EGOVException("Error: Invalid Cheque Number");				
			}
			else			
			{
				chqNo=adf.getChequeNo();
				chqDate=adf.getChequeDate();
			}

			cq[0]=new Instrument();
			cq[0].setInstrumentnumber(chqNo);
			cq[0].setInstrumentdate(chqDate);
			cq[0].setInstrumentamount(Double.parseDouble(adf.getTotalAmount()));
			cqs.setInstrument(cq);
			mode.setInstruments(cqs);
			if("0".equals(adf.getPaidBy()))
				mdc.setPaidBy(null);
			else
				mdc.setPaidBy(adf.getPaidBy());
			mdc.setMode(mode);
			v.setMisDataCollection(mdc);

			String bankAccCodeAndName=cm.getBankCode(Integer.parseInt(adf.getBankAccount()),HibernateUtil.getCurrentSession().connection());
			String bankAccCodeName[]=bankAccCodeAndName.split("#");
			String bankGlCode=bankAccCodeName[0];
			SalaryCodes salCode=sam.getSalaryCodesById(Integer.parseInt(adf.getAdvanceType()));
			int calGlSize=0;			
			gl[0]=new GeneralLedgerPosting();
			gl[0].setCredit(0);
			gl[0].setDebit(Double.parseDouble(adf.getTotalAmount()));
			gl[0].setGlcode(salCode.getChartofaccounts().getGlcode());		
			
			int detailTypeId=cmnMngr.getDetailTypeId(salCode.getChartofaccounts().getGlcode(),HibernateUtil.getCurrentSession().connection());			 
			if(detailTypeId!=0)
			{														
				if(advSanctionList!=null && advSanctionList.size()>0)
		   		{
					for(Iterator it = advSanctionList.iterator(); it.hasNext(); ) 
					{					
						Advance salAdvance=(Advance)it.next();	  
						glDet[calGlSize]=new GlDetailEntry();
						glDet[calGlSize].setDetailGlcode(salCode.getChartofaccounts().getGlcode());
						glDet[calGlSize].setDetailTypeId(detailTypeId);
						glDet[calGlSize].setDetailKeyId(salAdvance.getEmployee().getIdPersonalInformation());
						glDet[calGlSize].setDetailKeyAmount(Double.parseDouble(salAdvance.getAdvanceAmt().toString()));
						gl[0].setGlDetailEntry(glDet);
						calGlSize++;     
					}
		   		}
			}
				
			gl[1]=new GeneralLedgerPosting();
			gl[1].setCredit(Double.parseDouble(adf.getTotalAmount()));
			gl[1].setDebit(0);
			gl[1].setGlcode(bankGlCode);
			CChartOfAccounts coa=cmnMngr.getCChartOfAccountsByGlCode(bankGlCode);
			detailTypeId=cmnMngr.getDetailTypeId(bankGlCode,HibernateUtil.getCurrentSession().connection());
			if(detailTypeId!=0)
			{				
				if(advSanctionList!=null && advSanctionList.size()>0)
		   		{
					for(Iterator it = advSanctionList.iterator(); it.hasNext(); ) 
					{					
						Advance salAdvance=(Advance)it.next();	  
						glDet[calGlSize]=new GlDetailEntry();
						glDet[calGlSize].setDetailGlcode(coa.getGlcode());						
						glDet[calGlSize].setDetailTypeId(detailTypeId);
						glDet[calGlSize].setDetailKeyId(salAdvance.getEmployee().getIdPersonalInformation());
						glDet[calGlSize].setDetailKeyAmount(Double.parseDouble(salAdvance.getAdvanceAmt().toString()));
						gl[1].setGlDetailEntry(glDet);
						calGlSize++;	
					}
		   		}
			}

			glentry.setGeneralLedgerPosting(gl);
			v.setGeneralLedgerEnteries(glentry);
			FinancialTransactions ft=new FinancialTransactions();
			Voucherheader voucherHeader=ft.postTransaction(v,HibernateUtil.getCurrentSession().connection());

			if(advSanctionList!=null && advSanctionList.size()>0)
	   		{
				for(Iterator it = advSanctionList.iterator(); it.hasNext(); ) 
				{   					
					Advance salAdvance=(Advance)it.next();   					
					CVoucherHeader vchrHdr=cmnMngr.findVoucherHeaderById(Long.parseLong(voucherHeader.getVoucherHeaderId()));
					salAdvance.setVoucherheader(vchrHdr);
					salAdvance.setLastmodifiedby(Long.parseLong(req.getSession().getAttribute(LOGINUSERID).toString()));
					salAdvance.setLastmodifieddate(curDate);
					sam.updateSalaryadvance(salAdvance);									
	   			}
	   		}
					
			req.setAttribute("buttonType",req.getParameter("button"));
			req.setAttribute(VHID,voucherHeader.getVoucherHeaderId());	
			HibernateUtil.getCurrentSession().flush();	
			
			if(autoGenerateChqNo.equalsIgnoreCase("Y") && adf.getIsChqSurrendered())
				alertMessage="Executed successfully. Cheque No. used is "+chqNo;
			else
				alertMessage="Executed successfully";
			target="viewModifyDisbursement";		
		}
		catch(EGOVException ex)
       {
			req.setAttribute("buttonType",req.getParameter("button"));
			target = "viewModifyDisbursement";
           alertMessage=ex.getMessage();
           logger.error("excpetion message "+ex.getMessage());
           HibernateUtil.rollbackTransaction();
       }
		catch(Exception ex)
		{
		    target = ERROR;
		    logger.error(ex.getMessage());
		    HibernateUtil.rollbackTransaction();
		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
   }
    
    private String getDateString(String dateString) throws Exception
	{
		String retValue="";
		if(!"".equals(dateString))
		{
			Date dt=new Date();
			dt = sdf.parse(dateString);
			retValue=format.format(dt);
		}
		return retValue;
	}
    
    private java.util.Date getDate(String dateString)throws Exception
	{
		java.util.Date d = new java.util.Date();
		try
		{
			d = sdf.parse(dateString);

		} catch (Exception e)
		{
			logger.error(e.getMessage());
			throw e;
		}
		return d;
	}
	
}*/

