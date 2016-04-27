package com.exilant.eGov.src.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
@Component
public class BankReconciliationSummary {

	private static final Logger LOGGER = Logger.getLogger(BankReconciliationSummary.class);
	@Autowired
	@Qualifier("persistenceService")
	protected PersistenceService persistenceService;
	@Autowired
    private AppConfigValueService appConfigValuesService;
	

	String defaultStatusExclude=null;
	
	
	SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	
	
	
	public String getUnReconciledDrCr(Integer bankAccId,Date fromDate,Date toDate) throws Exception
	{

		String instrumentsForTotal="case when iv.voucherHeaderId is null then 0 else ih.instrumentAmount end)";
		String instrumentsForBrsEntryTotal="(case when br.voucherHeaderId is null then ih.instrumentAmount else 0 end)";
		
		
		String totalQuery="SELECT (sum(case when ih.ispaycheque='1' then ih.instrumentAmount else 0 end))  AS \"brs_creditTotal\", "
			+" (sum( case when ih.ispaycheque= '0' then ih.instrumentAmount else 0 end) ) AS \"brs_debitTotal\" "
			+" FROM egf_instrumentheader ih 	WHERE   ih.bankAccountId =:bankAccountId "
			+" AND IH.INSTRUMENTDATE >= :fromDate" 
			+" AND IH.INSTRUMENTDATE <= :toDate"
			+" AND  ( (ih.ispaycheque='0' and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited'))or (ih.ispaycheque='1' and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='New'))) "
			+" and ih.instrumentnumber is not null";
	//see u might need to exclude brs entries here 
		
		String otherTotalQuery=" SELECT (sum(case when ih.ispaycheque='1' then ih.instrumentAmount else 0 end ))  AS \"brs_creditTotalOthers\", "
			+" (sum(case when ih.ispaycheque='0' then ih.instrumentAmount else 0 end ) ) AS \"brs_debitTotalOthers\" "
			+" FROM  egf_instrumentheader ih	WHERE   ih.bankAccountId =:bankAccountId"
			+" AND IH.transactiondate >= :fromDate"
			+" AND IH.transactiondate <= :toDate  "
			+" AND ( (ih.ispaycheque='0' and ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited'))or (ih.ispaycheque='1' and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='New'))) "
			+" AND ih.transactionnumber is not null";
		
		
		
		String brsEntryQuery="select (sum(case when be.type='Receipt' then (case when be.voucherheaderid is null then be.txnamount else 0 end) else 0 end))AS \"brs_creditTotalBrsEntry\","
				+"(sum(case when be.type='Payment' then (case when be.voucherheaderid is null then be.txnamount else 0 end) else 0 end))AS \"brs_debitTotalBrsEntry\""
				+"FROM  bankentries be WHERE   be.bankAccountId = :bankAccountId and be.voucherheaderid is null AND be.txndate >=:fromDate   AND be.txndate <= :toDate";


		
		
	
		if(LOGGER.isInfoEnabled())     LOGGER.info("  query  for  total : "+totalQuery);
		if(LOGGER.isInfoEnabled())     LOGGER.info("  query  for other than cheque/DD: "+otherTotalQuery);
		if(LOGGER.isInfoEnabled())     LOGGER.info("  query  for bankEntries: "+brsEntryQuery);
		
		String unReconciledDrCr="";
		
		
		String creditTotal=null;
		String creditOthertotal=null;
		String debitTotal=null;
		String debitOtherTotal=null;
		String creditTotalBrsEntry=null;
		String debitTotalBrsEntry=null;
		
		try
		{
			SQLQuery totalSQLQuery =  persistenceService.getSession().createSQLQuery(totalQuery);
			totalSQLQuery.setInteger("bankAccountId",bankAccId);
			totalSQLQuery.setDate("fromDate",fromDate);
			totalSQLQuery.setDate("toDate",toDate);
			
			List list = totalSQLQuery.list();
			if (list.size()>0)
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug(list.get(0));
				Object [] my = (Object[])list.get(0);
				creditTotal=my[0]!=null?my[0].toString():null;
				debitTotal=my[1]!=null?my[1].toString():null;
			}

			totalSQLQuery = persistenceService.getSession().createSQLQuery(otherTotalQuery);
			totalSQLQuery.setInteger("bankAccountId",bankAccId);
			totalSQLQuery.setDate("fromDate",fromDate);
			totalSQLQuery.setDate("toDate",toDate);
			list = totalSQLQuery.list();
			if (list.size()>0)
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug(list.get(0));
				Object [] my = (Object[])list.get(0);
				creditOthertotal=my[0]!=null?my[0].toString():null;
				debitOtherTotal=my[1]!=null?my[1].toString():null;
			}

			totalSQLQuery = persistenceService.getSession().createSQLQuery(brsEntryQuery);
			totalSQLQuery.setInteger("bankAccountId",bankAccId);
			totalSQLQuery.setDate("fromDate",fromDate);
			totalSQLQuery.setDate("toDate",toDate);
			list = totalSQLQuery.list();
			if (list.size()>0)
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug(list.get(0));
				Object [] my = (Object[])list.get(0);
				creditTotalBrsEntry=my[0]!=null?my[0].toString():null;
				debitTotalBrsEntry=my[1]!=null?my[1].toString():null;
			}


		unReconciledDrCr=(creditTotal != null ? creditTotal : "0" )+"/"+(creditOthertotal!= null ? creditOthertotal : "0")
		+"/"+(debitTotal!= null ? debitTotal : "0") +"/"+( debitOtherTotal!= null ? debitOtherTotal : "0")+""+
		"/"+(creditTotalBrsEntry!= null ? creditTotalBrsEntry : "0") +"/"+( debitTotalBrsEntry!= null ? debitTotalBrsEntry : "0")+"";
		}
		catch(Exception e)
		{
			LOGGER.error("Exp in getUnReconciledDrCr"+e.getMessage());
			throw e;
		}
		return unReconciledDrCr;
	}
	
	
	
	
	
}
