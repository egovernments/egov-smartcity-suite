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
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentOtherDetails;
import org.egov.services.instrument.InstrumentService;
import org.egov.utils.FinancialConstants;
import org.hibernate.jdbc.ReturningWork;

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
				LOGGER.info(">>> inside toLoad");
				BankBranch bb=new BankBranch();
				HashMap hm=bb.getBankBranch();
				req.getSession().setAttribute("bankBranchList", hm);
				LOGGER.debug(">>> before ending BankReconciliationAction");
				target = "success";

		   }
		catch(Exception ex)
		{
		   target = "error";
		   LOGGER.debug("Exception Encountered!!!"+ex.getMessage());
		   LOGGER.debug("EXP="+ex.getMessage());
		   HibernateUtil.rollbackTransaction();
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
					LOGGER.info(">>> inside getAccountNumbers");
					org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm)form;
					BankBranch bb=new BankBranch();
					LOGGER.debug("bank id  "+bankRecForm.get("bankId"));
					HashMap hm=bb.getAccNumber(((String)bankRecForm.get("bankId")));
					LOGGER.debug(">>> after list creation BankReconciliationAction");
					req.getSession().setAttribute("accNumberList1", hm);
					LOGGER.info(">>> before ending BankReconciliationAction");
					target = "success";

			   }
			catch(Exception ex)
			{
			   target = "error";
			   LOGGER.debug("Exception Encountered!!!"+ex.getMessage());
			   HibernateUtil.rollbackTransaction();
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
				LOGGER.info(">>> inside getAccountNumbers");
				org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm)form;
				BankBranch bb=new BankBranch();
				LOGGER.info("bank id  "+bankRecForm.get("bankId"));
				HashMap hm=bb.getAccNumber(((String)bankRecForm.get("bankId")));
				LOGGER.debug(">>> after list creation BankReconciliationAction");
				req.getSession().setAttribute("accNumberList3", hm);
				LOGGER.debug(">>> before ending BankReconciliationAction");
				target = "success";

		   }
		catch(Exception ex)
		{
		   target = "error";
		   LOGGER.debug("Exception Encountered!!!"+ex.getMessage());
		   HibernateUtil.rollbackTransaction();
		}
		return mapping.findForward(target);

	}
	/*
	 get list of unreconciled cheques, bankbranch balance and sum of unreconciled receipts&payments amount
	*/
	public ActionForward showBrsDetails(final ActionMapping mapping,final ActionForm form,final HttpServletRequest req,final HttpServletResponse res)
	throws Exception
	{
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<ActionForward>() {

			@Override
			public ActionForward execute(Connection connection) throws SQLException {

				try{
						LOGGER.info(">>> inside showBrsDetails");
						org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm)form;
					//	BigDecimal accountBalance=new BigDecimal("0.00");
						BankReconciliation br=new BankReconciliation();
						LOGGER.info("bankacc id  "+bankRecForm.get("accId"));
						dt=new Date();
						dt = sdf.parse((String)bankRecForm.get("bankStatementDate"));
						String recToDate = formatter.format(dt);
						LOGGER.info("recToDate  "+recToDate);
						String recFromDate = (String)bankRecForm.get("recFromDate");
						recFromDate=StringUtils.isNotBlank(recFromDate)?formatter.format(sdf.parse((String)bankRecForm.get("recFromDate"))):"";
						LOGGER.info("recFromDate  "+recFromDate);
						ArrayList al=br.getRecordsToReconcile((String)bankRecForm.get("accId"),recFromDate,recToDate,connection);
						LOGGER.debug(">>> after list creation BankReconciliationAction");
						req.setAttribute("brsDetails", al);

						BigDecimal accountBalance = cm.getAccountBalance(recToDate,connection,(String)bankRecForm.get("accId")).setScale(2, BigDecimal.ROUND_HALF_UP);
						req.setAttribute("accountBalance", ""+accountBalance);

						//String unReconciledDrCr=br.getUnReconciledDrCr((String)bankRecForm.get("accId"),recFromDate,recToDate,HibernateUtil.getCurrentSession().connection());
						String unReconciledDrCr=br.getUnReconciledDrCr((String)bankRecForm.get("accId"),recToDate,connection);
						String drcrValues[]=unReconciledDrCr.split("/");
							LOGGER.info("  unReconciledDrCr   "+unReconciledDrCr);
							LOGGER.info(drcrValues[0]+"  "+drcrValues[3]+"  "+drcrValues[1]+"   "+drcrValues[2]);
							double unReconciledCr=(Double.parseDouble(drcrValues[0]))+(Double.parseDouble(drcrValues[3]));
							double unReconciledDr=(Double.parseDouble(drcrValues[1]))+(Double.parseDouble(drcrValues[2]));
							req.setAttribute("unReconciledDr", ""+unReconciledDr);
							req.setAttribute("unReconciledCr", ""+unReconciledCr);
							req.setAttribute("balanceAsPerStatement", ""+Double.parseDouble((String)bankRecForm.get("balAsPerStatement")));

						LOGGER.debug(">>> before ending BankReconciliationAction");
						target = "success";

				   }
				catch(Exception ex)
				{
				   target = "error";
				   LOGGER.debug("Exception Encountered!!!"+ex.getMessage());
				   HibernateUtil.rollbackTransaction();
		   		}
				return mapping.findForward(target);

			
			}
		});
	}
	/*
		get list of unreconciled cheques, bankbranch balance(used in Brs report screen)
	*/
	public ActionForward brsSummary(final ActionMapping mapping,final ActionForm form,final HttpServletRequest req,final HttpServletResponse res)
		throws Exception
	{
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<ActionForward>() {

			@Override
			public ActionForward execute(Connection connection) throws SQLException {
				
				try{
						LOGGER.info(">>> inside brsSummary");
						org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm)form;
						// accountBalance=new BigDecimal("0.00");
						BankReconciliation br=new BankReconciliation();
						LOGGER.info("bankacc id  "+bankRecForm.get("accId"));
						dt=new Date();
						dt = sdf.parse((String)bankRecForm.get("bankStatementDate"));
						String recDate = formatter.format(dt);
						LOGGER.info("recDate  "+recDate);

						BigDecimal accountBalance = cm.getAccountBalance(recDate,connection,(String)bankRecForm.get("accId")).setScale(2, BigDecimal.ROUND_HALF_UP);
						req.setAttribute("accountBalance", ""+accountBalance);

						String unReconciledDrCr=br.getUnReconciledDrCr((String)bankRecForm.get("accId"),recDate,connection);
						String drcrValues[]=unReconciledDrCr.split("/");
							LOGGER.info("  unReconciledDrCr   "+unReconciledDrCr);
							LOGGER.info("  drcrValues[]   "+drcrValues[1]);
							LOGGER.info("  drcrValues   "+drcrValues.length);
							LOGGER.info(drcrValues[0]+"  "+drcrValues[1]+"  "+drcrValues[2]+"   "+drcrValues[3]);
							double unReconciledCr=Double.parseDouble(drcrValues[0]);
							double unReconciledCrOthers=Double.parseDouble(drcrValues[1]);
							double unReconciledDr=Double.parseDouble(drcrValues[2]);
							double unReconciledDrOthers=Double.parseDouble(drcrValues[3]);
							req.setAttribute("unReconciledCr", ""+unReconciledCr);
							req.setAttribute("unReconciledCrOthers", ""+unReconciledCrOthers);
							req.setAttribute("unReconciledDr", ""+unReconciledDr);
							req.setAttribute("unReconciledDrOthers", ""+unReconciledDrOthers);
						ArrayList al=br.getUnReconciledCheques((String)bankRecForm.get("accId"),recDate,connection);
						LOGGER.debug(">>>>>>>>>>  al   ....."+al);
						req.setAttribute("unreconciledCheques", al);
						req.setAttribute("balanceAsPerStatement", ""+Double.parseDouble((String)bankRecForm.get("balAsPerStatement")));

						LOGGER.debug(">>> before ending ReconciliationSummaryAction");
						target = "success";

				   }
				catch(Exception ex)
				{
				   target = "error";
				   LOGGER.error("Exception Encountered!!!"+ex.getMessage());
				  // HibernateUtil.rollbackTransaction();
		   		}
				return mapping.findForward(target);

		
			}
		});
		
		
	}
	/*
		To reconcile cheques
	*/
	public ActionForward reconcile(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
		throws Exception
		{

			try{
					LOGGER.info(">>> inside reconcile");
					org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm)form;
					String[] recId = (String[])bankRecForm.get("recID");
					String[] instrumentId = (String[])bankRecForm.get("ihId");
					String[] chqDate = (String[])bankRecForm.get("bankStatementChqDate");
					dt=new Date();
					dt = sdf.parse((String)bankRecForm.get("recToDate"));
					String reconcileDate=formatter.format(dt);
					BankReconciliation br = new BankReconciliation();
					instrumentService=new InstrumentService();
					persistenceService=new PersistenceService();
					persistenceService.setSessionFactory(new SessionFactory());
					instrumentService.setPersistenceService( persistenceService);
					
					PersistenceService<InstrumentHeader, Long> iHeaderService= new PersistenceService<InstrumentHeader, Long>();
					iHeaderService.setType(InstrumentHeader.class);
					iHeaderService.setSessionFactory(new SessionFactory());
					instrumentService.setInstrumentHeaderService(iHeaderService);
					PersistenceService<InstrumentOtherDetails, Long> iOtherDetailsService= new PersistenceService<InstrumentOtherDetails, Long>();
					iOtherDetailsService.setType(InstrumentOtherDetails.class);
					iOtherDetailsService.setSessionFactory(new SessionFactory());
					instrumentService.setInstrumentOtherDetailsService(iOtherDetailsService);
					
					for(int i=0;i<instrumentId.length;i++)
					{
						if(chqDate[i] == null || chqDate[i].equalsIgnoreCase(""))
						continue;
						dt=new Date();
						dt = sdf.parse(chqDate[i]);
						LOGGER.info("formatter.format(dt)   "+ formatter.format(dt));
						
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
						instrumentService.instrumentOtherDetailsService.persist(io);
						
					}
					
					/*for(int i=0; i < recId.length; i++)
					{
						if(chqDate[i] == null || chqDate[i].equalsIgnoreCase(""))
						continue;
						LOGGER.info("chqDate[i]  "+chqDate[i]+"   recId[i]   "+ recId[i]);
						dt=new Date();
						dt = sdf.parse(chqDate[i]);
						LOGGER.info("formatter.format(dt)   "+ formatter.format(dt));
						br.setId(recId[i]);
						
						
						br.setIsReconciled("1");
						br.setReconciliationDate(reconcileDate);
						br.setRecChequeDate(formatter.format(dt));
						br.update(HibernateUtil.getCurrentSession().connection());
					}*/
					LOGGER.debug(">>> before ending reconcile");
					alertMessage="Executed successfully";
					req.setAttribute("alertMessage", alertMessage);
					target = "success";

				}
			catch(Exception ex)
			{
			   target = "error";
			   LOGGER.error("Exception Encountered!!!"+ex.getMessage());
			 //  HibernateUtil.rollbackTransaction();
			}
			return mapping.findForward(target);
		}
}
