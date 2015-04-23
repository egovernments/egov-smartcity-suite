 /*
 * BankReconciliationAction.java Created on Aug 7, 2006
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions.brs;
/**
 *
 * @author Tilak
 * @version 1.00
 */

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentOtherDetails;
import org.egov.services.instrument.InstrumentService;
import org.egov.utils.FinancialConstants;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.BankBranch;
import com.exilant.eGov.src.domain.BankReconciliation;
public class BankReconciliationAction extends DispatchAction {
	public BankReconciliationAction() {
		super();

	}
	private static  final Logger LOGGER = Logger.getLogger(BankReconciliationAction.class);
	String target = "";
	String alertMessage=null;
	SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	Date dt;
	EGovernCommon cm = new EGovernCommon();
	InstrumentService instrumentService;
	PersistenceService persistenceService;
	/**
	 * @param persistenceService the persistenceService to set
	 */
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	/**
	 * @param instrumentService the instrumentService to set
	 */
	public void setInstrumentService(InstrumentService instrumentService) {
		this.instrumentService = instrumentService;
	}
	/*
	  get list of bankbranches
	*/
	public ActionForward toLoad(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws Exception
	{

		try{
				if(LOGGER.isInfoEnabled())     LOGGER.info(">>> inside toLoad");
				BankBranch bb=new BankBranch();
				//This fix is for Phoenix Migration.
				Map hm=null;//bb.getBankBranch(null);
				//This fix is for Phoenix Migration.HibernateUtil.getCurrentSession().setAttribute("bankBranchList", hm);
				if(LOGGER.isDebugEnabled())     LOGGER.debug(">>> before ending BankReconciliationAction");
				target = "success";

		   }
		catch(Exception ex)
		{
		   target = "error";
		   LOGGER.error("Exception Encountered!!!"+ex.getMessage(),ex);
		    
   		}
		return mapping.findForward(target);

	}
	/*
		  get list of accountnumber for given bankbranch
	*/
	public ActionForward getAccountNumbers(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
		throws Exception
		{

			try{
					if(LOGGER.isInfoEnabled())     LOGGER.info(">>> inside getAccountNumbers");
					org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm)form;
					BankBranch bb=new BankBranch();
					if(LOGGER.isDebugEnabled())     LOGGER.debug("bank id  "+bankRecForm.get("bankId"));
					//This fix is for Phoenix Migration.
					HashMap hm=null;//bb.getAccNumber(((String)bankRecForm.get("bankId")),null);
					if(LOGGER.isDebugEnabled())     LOGGER.debug(">>> after list creation BankReconciliationAction");
					//This fix is for Phoenix Migration.reqHibernateUtil.getCurrentSession().setAttribute("accNumberList1", hm);
					if(LOGGER.isInfoEnabled())     LOGGER.info(">>> before ending BankReconciliationAction");
					target = "success";

			   }
			catch(Exception ex)
			{
			   target = "error";
			   LOGGER.error("Exception Encountered!!!"+ex.getMessage(),ex);
			    
	   		}
			return mapping.findForward(target);

	}
	/*
			  get list of accountnumber for given bankbranch(used in brs report screen)
	*/
	public ActionForward getAccountNumbersForRS(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws Exception
	{

		try{
				if(LOGGER.isInfoEnabled())     LOGGER.info(">>> inside getAccountNumbers");
				org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm)form;
				BankBranch bb=new BankBranch();
				if(LOGGER.isInfoEnabled())     LOGGER.info("bank id  "+bankRecForm.get("bankId"));
				//This fix is for Phoenix Migration.
				HashMap hm=null;//bb.getAccNumber(((String)bankRecForm.get("bankId")),null);
				if(LOGGER.isDebugEnabled())     LOGGER.debug(">>> after list creation BankReconciliationAction");
				//This fix is for Phoenix Migration.reqHibernateUtil.getCurrentSession().setAttribute("accNumberList3", hm);
				if(LOGGER.isDebugEnabled())     LOGGER.debug(">>> before ending BankReconciliationAction");
				target = "success";

		   }
		catch(Exception ex)
		{
		   target = "error";
		   LOGGER.error("Exception Encountered!!!"+ex.getMessage(),ex);
		    
		}
		return mapping.findForward(target);

	}
	/*
	 get list of unreconciled cheques, bankbranch balance and sum of unreconciled receipts&payments amount
	*/
	public ActionForward showBrsDetails(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws Exception
	{

		try{
				if(LOGGER.isInfoEnabled())     LOGGER.info(">>> inside showBrsDetails");
				org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm)form;
			//	BigDecimal accountBalance=new BigDecimal("0.00");
				BankReconciliation br=new BankReconciliation();
				if(LOGGER.isInfoEnabled())     LOGGER.info("bankacc id  "+bankRecForm.get("accId"));
				dt=new Date();
				dt = sdf.parse((String)bankRecForm.get("bankStatementDate"));
				String recToDate = formatter.format(dt);
				Date toDate = sdf.parse((String)bankRecForm.get("recToDate"));
				if(LOGGER.isInfoEnabled())     LOGGER.info("recToDate  "+recToDate);
				String recFromDate = (String)bankRecForm.get("recFromDate");
				Date fromDate = sdf.parse((String)bankRecForm.get("recFromDate"));
				recFromDate=StringUtils.isNotBlank(recFromDate)?formatter.format(sdf.parse((String)bankRecForm.get("recFromDate"))):"";
				if(LOGGER.isInfoEnabled())     LOGGER.info("recFromDate  "+recFromDate);
				//This fix is for Phoenix Migration.
				ArrayList al=null;//br.getRecordsToReconcile((String)bankRecForm.get("accId"),recFromDate,recToDate,null);
				if(LOGGER.isDebugEnabled())     LOGGER.debug(">>> after list creation BankReconciliationAction");
				req.setAttribute("brsDetails", al);
				//This fix is for Phoenix Migration.
				BigDecimal accountBalance = null;//cm.getAccountBalance(recToDate,null,(String)bankRecForm.get("accId")).setScale(2, BigDecimal.ROUND_HALF_UP);
				req.setAttribute("accountBalance", ""+accountBalance);

				String bankAccIdStr= (String)bankRecForm.get("accId");
				Integer bankAccountId=0;
				if(bankAccIdStr!=null)
					bankAccountId=Integer.parseInt(bankAccIdStr);
				//String unReconciledDrCr=br.getUnReconciledDrCr((String)bankRecForm.get("accId"),recFromDate,recToDate,null);
				String unReconciledDrCr=br.getUnReconciledDrCr(bankAccountId,fromDate,toDate);   
				String drcrValues[]=unReconciledDrCr.split("/");
					if(LOGGER.isInfoEnabled())     LOGGER.info("  unReconciledDrCr   "+unReconciledDrCr);
					if(LOGGER.isInfoEnabled())     LOGGER.info(drcrValues[0]+"  "+drcrValues[3]+"  "+drcrValues[1]+"   "+drcrValues[2]);
					//double unReconciledCr=(Double.parseDouble(drcrValues[0]))+(Double.parseDouble(drcrValues[3]));
					//double unReconciledDr=(Double.parseDouble(drcrValues[1]))+(Double.parseDouble(drcrValues[2]));
					double unReconciledCr=Double.parseDouble(drcrValues[0]);
					double unReconciledCrOthers=Double.parseDouble(drcrValues[1]);
					double unReconciledDr=Double.parseDouble(drcrValues[2]);
					double unReconciledDrOthers=Double.parseDouble(drcrValues[3]);
					double unReconciledCrBrsEntry=Double.parseDouble(drcrValues[4]);
					double unReconciledDrBrsEntry=Double.parseDouble(drcrValues[5]);
					req.setAttribute("unReconciledCr", ""+unReconciledCr);
					req.setAttribute("unReconciledCrOthers", ""+unReconciledCrOthers);
					req.setAttribute("unReconciledDr", ""+unReconciledDr);
					req.setAttribute("unReconciledDrOthers", ""+unReconciledDrOthers);
					req.setAttribute("unReconciledCrBrsEntries", ""+unReconciledCrBrsEntry);
					req.setAttribute("unReconciledDrBrsEntries", ""+unReconciledDrBrsEntry);
					
					
					req.setAttribute("balanceAsPerStatement", ""+Double.parseDouble((String)bankRecForm.get("balAsPerStatement")));

				if(LOGGER.isDebugEnabled())     LOGGER.debug(">>> before ending BankReconciliationAction");
				target = "success";

		   }
		catch(Exception ex)
		{
		   target = "error";
		   LOGGER.error("Exception Encountered!!!"+ex.getMessage(),ex);
		    
   		}
		return mapping.findForward(target);

	}
	/*
		get list of unreconciled cheques, bankbranch balance(used in Brs report screen)
	*/
	public ActionForward brsSummary(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
		throws Exception
	{

			try{
					if(LOGGER.isInfoEnabled())     LOGGER.info(">>> inside brsSummary");
					org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm)form;
					// accountBalance=new BigDecimal("0.00");
					BankReconciliation br=new BankReconciliation();
					if(LOGGER.isInfoEnabled())     LOGGER.info("bankacc id  "+bankRecForm.get("accId"));
					dt=new Date();
					dt = sdf.parse((String)bankRecForm.get("bankStatementDate"));
					FinancialYearHibernateDAO financialYearDAO=new FinancialYearHibernateDAO(CFinancialYear.class,HibernateUtil.getCurrentSession());
					
					CFinancialYear finYearByDate = financialYearDAO.getFinYearByDate(dt);
					String recDate = formatter.format(dt);
					Date fromDate = finYearByDate.getStartingDate();
					if(LOGGER.isInfoEnabled())     LOGGER.info("recDate  "+recDate);
					//This fix is for Phoenix Migration.
					BigDecimal accountBalance = null;//cm.getAccountBalance(recDate,null,(String)bankRecForm.get("accId")).setScale(2, BigDecimal.ROUND_HALF_UP);
					req.setAttribute("accountBalance", ""+accountBalance);

					String bankAccIdStr= (String)bankRecForm.get("accId");
					Integer bankAccountId=0;
					if(bankAccIdStr!=null)
						bankAccountId=Integer.parseInt(bankAccIdStr);
					String unReconciledDrCr=br.getUnReconciledDrCr(bankAccountId,fromDate,dt);
					String drcrValues[]=unReconciledDrCr.split("/");
						if(LOGGER.isInfoEnabled())     LOGGER.info("  unReconciledDrCr   "+unReconciledDrCr);
						if(LOGGER.isInfoEnabled())     LOGGER.info("  drcrValues[]   "+drcrValues[1]);
						if(LOGGER.isInfoEnabled())     LOGGER.info("  drcrValues   "+drcrValues.length);
						if(LOGGER.isInfoEnabled())     LOGGER.info(drcrValues[0]+"  "+drcrValues[1]+"  "+drcrValues[2]+"   "+drcrValues[3]);
						double unReconciledCr=Double.parseDouble(drcrValues[0]);
						double unReconciledCrOthers=Double.parseDouble(drcrValues[1]);
						double unReconciledDr=Double.parseDouble(drcrValues[2]);
						double unReconciledDrOthers=Double.parseDouble(drcrValues[3]);
						double unReconciledCrBrsEntry=Double.parseDouble(drcrValues[4]);
						double unReconciledDrBrsEntry=Double.parseDouble(drcrValues[5]);
						req.setAttribute("unReconciledCr", ""+unReconciledCr);
						req.setAttribute("unReconciledCrOthers", ""+unReconciledCrOthers);
						req.setAttribute("unReconciledDr", ""+unReconciledDr);
						req.setAttribute("unReconciledDrOthers", ""+unReconciledDrOthers);
						req.setAttribute("unReconciledCrBrsEntries", ""+unReconciledCrBrsEntry);
						req.setAttribute("unReconciledDrBrsEntries", ""+unReconciledDrBrsEntry);
					//commenting for time being to load system faster
						//ArrayList al=br.getUnReconciledCheques((String)bankRecForm.get("accId"),recDate,null);
					
						ArrayList al=new ArrayList();
						if(LOGGER.isDebugEnabled())     LOGGER.debug(">>>>>>>>>>  al   ....."+al);
					req.setAttribute("unreconciledCheques", al);
					req.setAttribute("balanceAsPerStatement", ""+Double.parseDouble((String)bankRecForm.get("balAsPerStatement")));

					if(LOGGER.isDebugEnabled())     LOGGER.debug(">>> before ending ReconciliationSummaryAction");
					target = "success";

			   }
			catch(Exception ex)
			{
			   target = "error";
			   LOGGER.error("Exception Encountered!!!"+ex.getMessage(),ex);
			  //  
	   		}
			return mapping.findForward(target);

	}
	/*
		To reconcile cheques
	*/
	public ActionForward reconcile(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
		throws Exception
		{

			try{
					if(LOGGER.isInfoEnabled())     LOGGER.info(">>> inside reconcile");
					org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm)form;
					String[] recId = (String[])bankRecForm.get("recID");
					String[] instrumentId = (String[])bankRecForm.get("ihId");
					String[] chqDate = (String[])bankRecForm.get("bankStatementChqDate");
					dt=new Date();
					dt = sdf.parse((String)bankRecForm.get("recToDate"));
					Date reconcileDate=dt;
					String reconcileDateStr=formatter.format(dt);
					BankReconciliation br = new BankReconciliation();
					instrumentService=new InstrumentService();
					persistenceService=new PersistenceService();
					//persistenceService.setSessionFactory(new SessionFactory());
					instrumentService.setPersistenceService( persistenceService);
					
					PersistenceService<InstrumentHeader, Long> iHeaderService= new PersistenceService<InstrumentHeader, Long>();
					iHeaderService.setType(InstrumentHeader.class);
					//iHeaderService.setSessionFactory(new SessionFactory());
					instrumentService.setInstrumentHeaderService(iHeaderService);
					PersistenceService<InstrumentOtherDetails, Long> iOtherDetailsService= new PersistenceService<InstrumentOtherDetails, Long>();
					iOtherDetailsService.setType(InstrumentOtherDetails.class);
					//iOtherDetailsService.setSessionFactory(new SessionFactory());
					instrumentService.setInstrumentOtherDetailsService(iOtherDetailsService);
					
					for(int i=0;i<instrumentId.length;i++)
					{
						if(chqDate[i] == null || chqDate[i].equalsIgnoreCase(""))
						continue;
						dt=new Date();
						dt = sdf.parse(chqDate[i]);
						if(LOGGER.isInfoEnabled())     LOGGER.info("formatter.format(dt)   "+ formatter.format(dt));
						
						InstrumentHeader ih=instrumentService.instrumentHeaderService.find("from InstrumentHeader where id=?",Long.valueOf(instrumentId[i]));
						ih.setStatusId(instrumentService.getStatusId(FinancialConstants.INSTRUMENT_RECONCILED_STATUS));
						instrumentService.instrumentHeaderService.persist(ih);
						InstrumentOtherDetails io = instrumentService.instrumentOtherDetailsService.find("from InstrumentOtherDetails where instrumentHeaderId=?",ih);
						if(io==null)
						{
							io=new InstrumentOtherDetails();
							io.setInstrumentStatusDate(dt);
							io.setReconciledAmount(ih.getInstrumentAmount());
							io.setInstrumentHeaderId(ih);
							
						}
						io.setInstrumentStatusDate(dt);
						io.setReconciledAmount(ih.getInstrumentAmount());
						io.setReconciledOn(reconcileDate);
						instrumentService.instrumentOtherDetailsService.persist(io);
						
					}
					
					/*for(int i=0; i < recId.length; i++)
					{
						if(chqDate[i] == null || chqDate[i].equalsIgnoreCase(""))
						continue;
						if(LOGGER.isInfoEnabled())     LOGGER.info("chqDate[i]  "+chqDate[i]+"   recId[i]   "+ recId[i]);
						dt=new Date();
						dt = sdf.parse(chqDate[i]);
						if(LOGGER.isInfoEnabled())     LOGGER.info("formatter.format(dt)   "+ formatter.format(dt));
						br.setId(recId[i]);
						
						
						br.setIsReconciled("1");
						br.setReconciliationDate(reconcileDate);
						br.setRecChequeDate(formatter.format(dt));
						br.update(null);
					}*/
					if(LOGGER.isDebugEnabled())     LOGGER.debug(">>> before ending reconcile");
					alertMessage="Executed successfully";
					req.setAttribute("alertMessage", alertMessage);
					target = "success";

				}
			catch(Exception ex)
			{
			   target = "error";
			   LOGGER.error("Exception Encountered!!!"+ex.getMessage(),ex);
			 //   
			}
			return mapping.findForward(target);
		}
}
