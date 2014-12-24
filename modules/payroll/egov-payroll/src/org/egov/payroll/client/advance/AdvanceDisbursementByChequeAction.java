/*
 * AdvanceDisbursementByChequeAction.java Created on Oct 25, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.payroll.client.advance;

/*import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
import org.egov.commons.Accountdetailtype;
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
import org.egov.masters.services.MastersService;
import org.egov.payroll.model.Advance;
import org.egov.payroll.services.advance.AdvanceService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.utils.GetEgfManagers;

import com.exilant.eGov.src.common.EGovernCommon;

import com.exilant.eGov.src.reports.CommnFunctions;
import com.exilant.eGov.src.transactions.CommonMethodsI;
import com.exilant.eGov.src.transactions.CommonMethodsImpl;
import com.exilant.eGov.src.transactions.FinancialTransactions;*/

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish
 * @version 1.00 
 */

/*public class AdvanceDisbursementByChequeAction extends DispatchAction
{
	public static final Logger logger = Logger.getLogger(AdvanceDisbursementByChequeAction.class);	
	Timestamp curDate = new Timestamp(System.currentTimeMillis());
	SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	SimpleDateFormat format =new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
	private static final String MODE="mode";
	private static final String CREATEDISBURSEMENT="createDisbursement";
	private static final String ERROR= "error";
	private static final String VHID="vhId";
	private static final String YES="yes";
	private static final String LOGINUSERID="com.egov.user.LoginUserId";
	EGovernCommon egc = new EGovernCommon();
	
    public ActionForward beforeCreateDisbursement(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
     throws IOException,ServletException
    {
        String target ="";                
        ArrayList advSanctionEmpList=null;
        try
        {
        	logger.debug(" inside beforeCreateDisbursement");
        	AdvanceManager sam = PayrollManagersUtill.getSalaryadvanceManager();	
        	CommonsManager cm = PayrollManagersUtill.getCommonsManager();
        	EgwStatus status = cm.getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_SANCTION);
        	advSanctionEmpList=(ArrayList)sam.getSalAdvancesTroChequeByStatus(status);        	
        	req.getSession().setAttribute("advSanctionEmpList", advSanctionEmpList);
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
       ArrayList advSanctionEmpList=null;
       ArrayList bankBranchList=null;
       ArrayList cashierList=null;
       try
       {
			logger.debug(" inside searchAdvanceDetails");
			AdvanceManager sam = PayrollManagersUtill.getSalaryadvanceManager();	
			CommonsManager cm = PayrollManagersUtill.getCommonsManager();
			EisManager em = PayrollManagersUtill.getEisManager();			
			AdvanceDisbursementByChequeForm adf = (AdvanceDisbursementByChequeForm)form;
			EgwStatus status = cm.getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_SANCTION);			
			advSanctionEmpList=(ArrayList)sam.getSalAdvancesTroChequeByStatus(status);				
			advSanctionList=(ArrayList)sam.getSalAdvancesTroChequeFilterBy(Integer.parseInt(adf.getEmployee()),status);				
			Assignment assignment=em.getLatestAssignmentForEmployee(Integer.parseInt(adf.getEmployee()));
			String fundId=null, fundName=null;  
			if(assignment!=null)
			{
				fundId=assignment.getFundId().getId().toString();
				fundName=assignment.getFundId().getName();
			}			
			adf.setFundId(fundId);
			adf.setFundName(fundName);
			if(assignment!=null)
			{
				bankBranchList=(ArrayList)egc.getBankAndBranch(HibernateUtil.getCurrentSession().connection(),fundId);
			}
			cashierList=(ArrayList)egc.getCashierNameList(HibernateUtil.getCurrentSession().connection());
			
			Date date = new Date();
			String currDate = sdf.format(date);
			req.getSession().setAttribute("currentDate", currDate);
			req.getSession().setAttribute("advSanctionList", advSanctionList);
			req.getSession().setAttribute("advSanctionEmpList", advSanctionEmpList);
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
   
   public ActionForward createDisursementByCheque(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target ="";
		String alertMessage=null;
		try
		{
			MastersManager masterManager = GetEgfManagers.getMastersManager();
			logger.debug(" inside createDisursementByCheque");
			AdvanceDisbursementByChequeForm adf = (AdvanceDisbursementByChequeForm)form;			
			CommonsManager cmnMngr = PayrollManagersUtill.getCommonsManager();
			AdvanceManager sam = PayrollManagersUtill.getSalaryadvanceManager();	
			int fundId=Integer.parseInt(adf.getFundId());			
	   		CommonMethodsI cm=new CommonMethodsImpl();
			Voucher v=new Voucher();
	   		Voucherheader vh=new Voucherheader();
	   		Mode mode=new Mode();
	   		MisDataCollection mdc=new MisDataCollection();
	   		MisData mi=new MisData();
	   		Instruments cqs=new Instruments();
	   		Instrument cq[]=new Instrument[1];
	   		GeneralLedgerEnteries glentry=new GeneralLedgerEnteries();
	   		
	   		int glSize=0;
	   		if(adf.getId()!=null && adf.getId().length>0)
	   		{
	   			for(int i=0;i<adf.getId().length; i++)
	   			{	
	   				if(adf.getPay()[i].equalsIgnoreCase(YES))
	   				{
	   					glSize=i+1;
	   				}
	   			}
	   		}	   		
	   		glSize=glSize+1;
	   		GeneralLedgerPosting gl[]=new GeneralLedgerPosting[glSize];
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
			mdc.setPaidTo(adf.getEmployeeName());
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
				{
					throw new EGOVException("Error: Duplicate Cheque Number");
				}
				if(!egc.isChqNoWithinRange(chqNo,adf.getBankAccount(),HibernateUtil.getCurrentSession().connection()))
				{
					throw new EGOVException("Error: Invalid Cheque Number");
			}
			}

			cq[0]=new Instrument();
			cq[0].setInstrumentnumber(chqNo);
			cq[0].setInstrumentdate(chqDate);
			cq[0].setInstrumentamount(Double.parseDouble(adf.getTotalAmt()));
			Accountdetailtype accountDetailType = masterManager.getAccountdetailtypeByName(PayrollConstants.EMPLOYEE_MODULE);
			cq[0].setInstrumentdetailTypeId(accountDetailType.getId()); //FIXME: fixed use getAccountdetailtypeAttributename(Connection, String) where name='Employee'
			cq[0].setInstrumentdetailKeyId(Integer.parseInt(adf.getEmployee()));			
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
			if(adf.getId()!=null && adf.getId().length>0)
	   		{
	   			for(int i=0;i<adf.getId().length; i++)
	   			{
	   				if(adf.getPay()[i].equalsIgnoreCase(YES))
	   				{			
	   					Advance salAdvance=sam.getSalaryadvanceById(Integer.parseInt(adf.getId()[i]));
	   					gl[calGlSize]=new GeneralLedgerPosting();
						gl[calGlSize].setCredit(0);
						gl[calGlSize].setDebit(Double.parseDouble(salAdvance.getAdvanceAmt().toString()));
						gl[calGlSize].setGlcode(adf.getGlCode()[i]);
						int detailTypeId=cmnMngr.getDetailTypeId(adf.getGlCode()[i],HibernateUtil.getCurrentSession().connection());
						if(detailTypeId!=0)
						{														
							glDet[calGlSize]=new GlDetailEntry();
							glDet[calGlSize].setDetailGlcode(adf.getGlCode()[i]);
							glDet[calGlSize].setDetailTypeId(detailTypeId);
							glDet[calGlSize].setDetailKeyId(Integer.parseInt(adf.getEmployee()));
							glDet[calGlSize].setDetailKeyAmount(Double.parseDouble(salAdvance.getAdvanceAmt().toString()));
							gl[calGlSize].setGlDetailEntry(glDet);				
		   				}
						calGlSize++;						
		       		}
				}
			}
			
			gl[calGlSize]=new GeneralLedgerPosting();
			gl[calGlSize].setCredit(Double.parseDouble(adf.getTotalAmt()));
			gl[calGlSize].setDebit(0);
			gl[calGlSize].setGlcode(bankGlCode);
			CChartOfAccounts coa=cmnMngr.getCChartOfAccountsByGlCode(bankGlCode);
			int detailTypeId=cmnMngr.getDetailTypeId(bankGlCode,HibernateUtil.getCurrentSession().connection());
			if(detailTypeId!=0)
			{				
				glDet[calGlSize]=new GlDetailEntry();
				glDet[calGlSize].setDetailGlcode(coa.getGlcode());						
				glDet[calGlSize].setDetailTypeId(detailTypeId);
				glDet[calGlSize].setDetailKeyId(Integer.parseInt(adf.getEmployee()));
				glDet[calGlSize].setDetailKeyAmount(Double.parseDouble(adf.getTotalAmt()));
				gl[calGlSize].setGlDetailEntry(glDet);
				calGlSize++;					
			}

			glentry.setGeneralLedgerPosting(gl);
			v.setGeneralLedgerEnteries(glentry);
			FinancialTransactions ft=new FinancialTransactions();
			Voucherheader voucherHeader=ft.postTransaction(v,HibernateUtil.getCurrentSession().connection());

			if(adf.getId()!=null && adf.getId().length>0)
	   		{
	   			for(int i=0;i<adf.getId().length; i++)
	   			{	   				
	   				if(adf.getPay()[i].equalsIgnoreCase(YES))
	   				{
	   					Advance salAdvance=sam.getSalaryadvanceById(Integer.parseInt(adf.getId()[i]));
	   					EgwStatus status=null;
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
	   		}
			
			req.setAttribute(MODE,"search");
			req.setAttribute("buttonType",req.getParameter("button"));
			req.setAttribute(VHID,voucherHeader.getVoucherHeaderId());	
			HibernateUtil.getCurrentSession().flush();	
			
			if(autoGenerateChqNo.equalsIgnoreCase("Y"))
			{
				alertMessage="Executed successfully. Cheque No. used is "+chqNo;
			}
			else
			{
				alertMessage="Executed successfully";
			}
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
      
   public ActionForward beforePrintChequeDisbursement(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target ="";		
		try
		{
			logger.debug(" inside beforePrintChequeDisbursement");
			String vhId=req.getParameter(VHID);
			logger.debug("vhId-->>"+vhId);
			AdvanceDisbursementByChequeForm adf = (AdvanceDisbursementByChequeForm)form;			
			CommonsManager cmnMngr = PayrollManagersUtill.getCommonsManager();
			AdvanceManager sam = PayrollManagersUtill.getSalaryadvanceManager();			
			CommnFunctions cf=new CommnFunctions();
			CVoucherHeader vchrHdr=cmnMngr.findVoucherHeaderById(Long.parseLong(vhId));		   
			ArrayList disbursementList=(ArrayList)sam.getSalAdvancesByVoucherHeader(vchrHdr);
			Fund fund=cmnMngr.getFundById(vchrHdr.getFundId().getId());
			adf.setFundName(fund.getName());	        
	        adf.setVoucherNo(vchrHdr.getVoucherNumber());
	        adf.setVoucherDate(sdf.format(vchrHdr.getVoucherDate()));
	        
	        String emplCode=null,emplName=null;
	        String[] advanceType=null;
			String[] sanctionNo=null;
			String[] sanctionedBy=null;
			String[] sanctionAmount=null;			
			int count=0;			
			BigDecimal totalAmt=new BigDecimal(0);
			Advance salAdvance=null;
			if(disbursementList !=null && !disbursementList.isEmpty())
			{	
				advanceType=new String[disbursementList.size()];
				sanctionNo=new String[disbursementList.size()];
				sanctionedBy=new String[disbursementList.size()];
				sanctionAmount=new String[disbursementList.size()];
				for(Iterator it = disbursementList.iterator(); it.hasNext(); ) 
				{								
					salAdvance=(Advance)it.next();	
					advanceType[count]=salAdvance.getSalaryCodes().getHead();
					sanctionNo[count]=salAdvance.getSanctionNum();
					sanctionedBy[count]=salAdvance.getSanctionedBy().getUserName();
					sanctionAmount[count]=cf.numberToString(salAdvance.getAdvanceAmt().toString()).toString();
					totalAmt=totalAmt.add(salAdvance.getAdvanceAmt());
					emplCode=salAdvance.getEmployee().getEmployeeCode().toString();
					emplName=salAdvance.getEmployee().getEmployeeName();
					count++;
				}
			}			       
			adf.setAdvanceType(advanceType);
			adf.setSanctionNo(sanctionNo);
			adf.setSanctionedBy(sanctionedBy);
			adf.setSanctionAmount(sanctionAmount);
			adf.setTotalAmt(cf.numberToString(totalAmt.toString()).toString());   		
   		
			adf.setEmployee(emplCode);
			adf.setEmployeeName(emplName);
				   		
	   		ArrayList chequedetailList=(ArrayList)cmnMngr.getChequedetailByVoucherheader(vchrHdr);
	   		for (Iterator cdItr = chequedetailList.iterator(); cdItr.hasNext(); )
            {
	   			Chequedetail chequedetail=(Chequedetail)cdItr.next();
	   			if(chequedetail.getBankbranch()!=null)
	   			{
	   				adf.setBank(chequedetail.getBankbranch().getBranchname()+" - "+chequedetail.getBank().getName());
	   			}
	   			if(chequedetail.getBankaccount()!=null)
	   			{
	   				adf.setBankAccount(chequedetail.getBankaccount().getAccountnumber());
	   			}
	   			adf.setChequeNo(chequedetail.getChequenumber());    			
	   			adf.setChequeDate(sdf.format(chequedetail.getChequedate()));  
	   			if(chequedetail.getPaidbyid()!=null)
	   			{
	   				String paidBy=egc.getBillCollectorName(HibernateUtil.getCurrentSession().connection(), chequedetail.getPaidbyid().toString());
	   				adf.setPaidBy(paidBy);
	   			}
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
   
   public ActionForward beforeChequeDisbursementList(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target ="";
		ArrayList advDisbEmpList=null;
		try
		{
			logger.debug(" inside beforeChequeDisbursementList");
			AdvanceManager sam = PayrollManagersUtill.getSalaryadvanceManager();
			CommonsManager cmnMngr = PayrollManagersUtill.getCommonsManager();
			EgwStatus status = cmnMngr.getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_DISBURSE);
        	advDisbEmpList=(ArrayList)sam.getSalAdvancesTroChequeByStatus(status);        	
        	req.getSession().setAttribute("advDisbEmpList", advDisbEmpList);			
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
   
   public ActionForward getChequeDisbursementList(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target ="";
		ArrayList<Advance> chequeDisbursementList=null;
		ArrayList advDisbEmpList=null;
		try
		{
			logger.debug(" insidec getChequeDisbursementList");
			AdvanceDisbursementByChequeForm adf = (AdvanceDisbursementByChequeForm)form;	
			AdvanceManager sam = PayrollManagersUtill.getSalaryadvanceManager();
			CommonsManager cmnMngr = PayrollManagersUtill.getCommonsManager();		
			EgwStatus status = cmnMngr.getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_DISBURSE);
        	advDisbEmpList=(ArrayList)sam.getSalAdvancesTroChequeByStatus(status);   
			if(adf.getEmployee()!=null & !adf.getEmployee().equals("0"))
			{
				chequeDisbursementList=(ArrayList)sam.getSalAdvancesTroChequeFilterBy(Integer.parseInt(adf.getEmployee()),status);
			}
			else
			{
				chequeDisbursementList=(ArrayList)sam.getSalAdvancesTroChequeFilterBy(null,status);
			}
			
			req.getSession().setAttribute("advDisbEmpList", advDisbEmpList);
			req.setAttribute("chequeDisbursementList",chequeDisbursementList);
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
   
   public ActionForward beforeViewAndModifyAdvDisbByCheque(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target ="";
		ArrayList advSanctionEmpList=null;
		ArrayList bankBranchList=null;
		ArrayList cashierList=null;
		
		try
		{
			logger.debug(" inside beforeViewAndModifyAdvDisbByCheque");
			AdvanceDisbursementByChequeForm adf = (AdvanceDisbursementByChequeForm)form;	
			AdvanceManager sam = PayrollManagersUtill.getSalaryadvanceManager();
			EisManager em = PayrollManagersUtill.getEisManager();
			CommonsManager cmnMngr = PayrollManagersUtill.getCommonsManager();		
			String vhId=req.getParameter(VHID);
			String emplId=req.getParameter("emplId");
			logger.debug("vhId-->>"+vhId);
			
			EgwStatus status = cmnMngr.getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_DISBURSE);
			advSanctionEmpList=(ArrayList)sam.getSalAdvancesTroChequeByStatus(status); 
			PersonalInformation empl=em.getEmloyeeById(Integer.parseInt(emplId));
			adf.setEmployee(empl.getIdPersonalInformation().toString());
			adf.setEmployeeName(empl.getEmployeeName());
			
			CVoucherHeader vchrHdr=cmnMngr.findVoucherHeaderById(Long.parseLong(vhId));		   
			ArrayList advSanctionList=(ArrayList)sam.getSalAdvancesByVoucherHeader(vchrHdr);
			Fund fund=cmnMngr.getFundById(vchrHdr.getFundId().getId());
			adf.setFundName(fund.getName());
			adf.setFundId(fund.getId().toString());
	        adf.setVoucherNo(vchrHdr.getVoucherNumber());
	        adf.setVoucherDate(sdf.format(vchrHdr.getVoucherDate()));	
			bankBranchList=(ArrayList)egc.getBankAndBranch(HibernateUtil.getCurrentSession().connection(),fund.getId().toString());
		
			cashierList=(ArrayList)egc.getCashierNameList(HibernateUtil.getCurrentSession().connection());
						
	   		ArrayList chequedetailList=(ArrayList)cmnMngr.getChequedetailByVoucherheader(vchrHdr);
	   		for (Iterator cdItr = chequedetailList.iterator(); cdItr.hasNext(); )
            {
	   			Chequedetail chequedetail=(Chequedetail)cdItr.next();
	   			if(chequedetail.getBankbranch()!=null)
	   			{
	   				adf.setBank(chequedetail.getBankbranch().getId().toString());
	   			}
	   			if(chequedetail.getBankaccount()!=null)
	   			{
	   				adf.setBankAccount(chequedetail.getBankaccount().getId().toString());
	   			}
	   			adf.setChequeNo(chequedetail.getChequenumber());    			
	   			adf.setChequeDate(sdf.format(chequedetail.getChequedate()));  
	   			if(chequedetail.getPaidbyid()!=null)
	   			{	   				
	   				adf.setPaidBy(chequedetail.getPaidbyid().toString());
	   			}
	   			BigDecimal balance=egc.getAccountBalance(getDateString(sdf.format(vchrHdr.getVoucherDate())), HibernateUtil.getCurrentSession().connection(),chequedetail.getBankaccount().getId().toString());
	   			adf.setBalance(balance.toString());
            }  
	   		Date date = new Date();
			String currDate = sdf.format(date);
			req.getSession().setAttribute("currentDate", currDate);
			req.getSession().setAttribute("advSanctionList", advSanctionList);
			req.getSession().setAttribute("advSanctionEmpList", advSanctionEmpList);
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
   
   public ActionForward modifyDisbursementByCheque(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target ="";
		String alertMessage=null;
		try
		{
			logger.debug(" inside modifyDisbursementByCheque");
			AdvanceDisbursementByChequeForm adf = (AdvanceDisbursementByChequeForm)form;			
			CommonsManager cmnMngr = PayrollManagersUtill.getCommonsManager();			
			AdvanceManager sam = PayrollManagersUtill.getSalaryadvanceManager();	
			String vhId=req.getParameter(VHID);
			logger.debug("vhId-->> "+vhId);
			CVoucherHeader voucherheader=cmnMngr.findVoucherHeaderById(Long.parseLong(vhId));
			int fundId=Integer.parseInt(adf.getFundId());			
	   		CommonMethodsI cm=new CommonMethodsImpl();
			Voucher v=new Voucher();
	   		Voucherheader vh=new Voucherheader();
	   		Mode mode=new Mode();
	   		MisDataCollection mdc=new MisDataCollection();
	   		MisData mi=new MisData();
	   		Instruments cqs=new Instruments();
	   		Instrument cq[]=new Instrument[1];
	   		GeneralLedgerEnteries glentry=new GeneralLedgerEnteries();
	   		
	   		int glSize=0;
	   		if(adf.getId()!=null && adf.getId().length>0)
	   		{
	   			for(int i=0;i<adf.getId().length; i++)
	   			{	
	   				if(adf.getPay()[i].equalsIgnoreCase(YES))
	   				{
	   					glSize=i+1;
	   				}
	   			}
	   		}
	   		glSize=glSize+1;
	   		GeneralLedgerPosting gl[]=new GeneralLedgerPosting[glSize];
	   		GlDetailEntry glDet[]=new GlDetailEntry[glSize];
	   		vh.setVoucherdate(getDateString(adf.getVoucherDate()));	   			   		
	   		String vhNum=adf.getVoucherNoPrefix()+adf.getVoucherNo();   						
			vh.setCgvn(voucherheader.getCgvn());
			if(!vhNum.equals(voucherheader.getVoucherNumber())&& !egc.isUniqueVN(vhNum,vh.getVoucherdate(),HibernateUtil.getCurrentSession().connection()))
				
				{
				throw new EGOVException("Error: Duplicate Voucher Number");
				}
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
			mdc.setPaidTo(adf.getEmployeeName());
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
			if(autoGenerateChqNo.equalsIgnoreCase("Y") && adf.getIsChqSurrendered())
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
			else if(!autoGenerateChqNo.equalsIgnoreCase("Y") && adf.getIsChqSurrendered())
			{
				chqNo=adf.getNewChequeNo();
				chqDate=adf.getNewChequeDate();

				if(!egc.isUniqueChequeNo(chqNo,adf.getBankAccount(),HibernateUtil.getCurrentSession().connection()))
					
					{
					throw new EGOVException("Error: Duplicate Cheque Number");
					}

				if(!egc.isChqNoWithinRange(chqNo,adf.getBankAccount(),HibernateUtil.getCurrentSession().connection()))
					
					{
					throw new EGOVException("Error: Invalid Cheque Number");				
					}
			}
			else			
			{
				chqNo=adf.getChequeNo();
				chqDate=adf.getChequeDate();
			}

			cq[0]=new Instrument();
			cq[0].setInstrumentnumber(chqNo);
			cq[0].setInstrumentdate(chqDate);
			cq[0].setInstrumentamount(Double.parseDouble(adf.getTotalAmt()));
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
			if(adf.getId()!=null && adf.getId().length>0)
	   		{
	   			for(int i=0;i<adf.getId().length; i++)
	   			{
	   				if(adf.getPay()[i].equalsIgnoreCase(YES))
	   				{  					
	   					Advance salAdvance=sam.getSalaryadvanceById(Integer.parseInt(adf.getId()[i]));
	   					gl[calGlSize]=new GeneralLedgerPosting();
						gl[calGlSize].setCredit(0);
						gl[calGlSize].setDebit(Double.parseDouble(salAdvance.getAdvanceAmt().toString()));
						gl[calGlSize].setGlcode(adf.getGlCode()[i]);
						int detailTypeId=cmnMngr.getDetailTypeId(adf.getGlCode()[i],HibernateUtil.getCurrentSession().connection());
						if(detailTypeId!=0)
						{														
							glDet[calGlSize]=new GlDetailEntry();
							glDet[calGlSize].setDetailGlcode(adf.getGlCode()[i]);
							glDet[calGlSize].setDetailTypeId(detailTypeId);
							glDet[calGlSize].setDetailKeyId(Integer.parseInt(adf.getEmployee()));
							glDet[calGlSize].setDetailKeyAmount(Double.parseDouble(salAdvance.getAdvanceAmt().toString()));
							gl[calGlSize].setGlDetailEntry(glDet);				
		   				}
						calGlSize++;						
		       		}
				}
			}
			
			gl[calGlSize]=new GeneralLedgerPosting();
			gl[calGlSize].setCredit(Double.parseDouble(adf.getTotalAmt()));
			gl[calGlSize].setDebit(0);
			gl[calGlSize].setGlcode(bankGlCode);
			CChartOfAccounts coa=cmnMngr.getCChartOfAccountsByGlCode(bankGlCode);
			int detailTypeId=cmnMngr.getDetailTypeId(bankGlCode,HibernateUtil.getCurrentSession().connection());
			if(detailTypeId!=0)
			{				
				glDet[calGlSize]=new GlDetailEntry();
				glDet[calGlSize].setDetailGlcode(coa.getGlcode());						
				glDet[calGlSize].setDetailTypeId(detailTypeId);
				glDet[calGlSize].setDetailKeyId(Integer.parseInt(adf.getEmployee()));
				glDet[calGlSize].setDetailKeyAmount(Double.parseDouble(adf.getTotalAmt()));
				gl[calGlSize].setGlDetailEntry(glDet);
				calGlSize++;					
			}

			glentry.setGeneralLedgerPosting(gl);
			v.setGeneralLedgerEnteries(glentry);
			FinancialTransactions ft=new FinancialTransactions();
			Voucherheader voucherHeader=ft.postTransaction(v,HibernateUtil.getCurrentSession().connection());

			if(adf.getId()!=null && adf.getId().length>0)
	   		{
	   			for(int i=0;i<adf.getId().length; i++)
	   			{	   				
	   				if(adf.getPay()[i].equalsIgnoreCase(YES))
	   				{
	   					Advance salAdvance=sam.getSalaryadvanceById(Integer.parseInt(adf.getId()[i]));						
						CVoucherHeader vchrHdr=cmnMngr.findVoucherHeaderById(Long.parseLong(voucherHeader.getVoucherHeaderId()));
						salAdvance.setVoucherheader(vchrHdr);
						salAdvance.setLastmodifiedby(Long.parseLong(req.getSession().getAttribute(LOGINUSERID).toString()));
						salAdvance.setLastmodifieddate(curDate);
						sam.updateSalaryadvance(salAdvance);						
	   				}
	   			}
	   		}		
			req.setAttribute("buttonType",req.getParameter("button"));
			req.setAttribute(VHID,voucherHeader.getVoucherHeaderId());	
			HibernateUtil.getCurrentSession().flush();	
			
			if(autoGenerateChqNo.equalsIgnoreCase("Y") && adf.getIsChqSurrendered())
				
				{
				alertMessage="Executed successfully. Cheque No. used is "+chqNo;
				}
			else
				{
				alertMessage="Executed successfully";
				}
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
		if(!("").equals(dateString))
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
}
*/
