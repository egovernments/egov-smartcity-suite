package org.egov.web.actions.brs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.egf.model.ReconcileBean;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
@Service
public class ManualReconcileHelper {

	private static  final Logger LOGGER = Logger.getLogger(ManualReconcileHelper.class);

	@Autowired
	private AppConfigValueService appConfigValueService;
	@Autowired
	@Qualifier("persistenceService")
	private PersistenceService persistenceService;
	public String getUnReconciledDrCr(Long bankAccId,Date fromDate,Date toDate)  
	{

		//String  ="decode(iv.voucherHeaderId,null,0,ih.instrumentAmount)";
		String instrumentsForBrsEntryTotal="case when br.voucherHeaderId is null then ih.instrumentAmount else 0 end";
		//String instrumentsForOtherTotal="decode(br.voucherHeaderId,null,ih.instrumentAmount,0)";
		String voucherExcludeStatuses=getExcludeStatuses();
		
		String totalQuery="SELECT (sum(CASE WHEN ih.ispaycheque='1' then ih.instrumentAmount else 0 end ))  AS \"brs_creditTotal\", "
			+" (sum(CASE WHEN ih.ispaycheque = '0' then  ih.instrumentAmount else 0 end)) AS \"brs_debitTotal\" "
			+" FROM egf_instrumentheader ih 	WHERE   ih.bankAccountId =:bankAccountId "
			+" AND IH.INSTRUMENTDATE >= :fromDate" 
			+" AND IH.INSTRUMENTDATE <= :toDate"
			+" AND  ( (ih.ispaycheque='0' and  ih.id_status=(select id from egw_status where moduletype='Instrument' "
			+ " and description='Deposited'))or (ih.ispaycheque='1' and  ih.id_status=(select id from egw_status where "
			+ " moduletype='Instrument'  and description='New'))) "
			+" and ih.instrumentnumber is not null";
	//see u might need to exclude brs entries here 
		
		String otherTotalQuery=" SELECT (sum(case when ih.ispaycheque='1' then ih.instrumentAmount else 0 end))  AS \"brs_creditTotalOthers\", "
			+" (sum(case when ih.ispaycheque= '0' then ih.instrumentAmount else 0 end))  AS \"brs_debitTotalOthers\" "
			+" FROM  egf_instrumentheader ih	WHERE   ih.bankAccountId =:bankAccountId"
			+" AND IH.transactiondate >= :fromDate"
			+" AND IH.transactiondate <= :toDate  "
			+" AND ( (ih.ispaycheque='0' and ih.id_status=(select id from egw_status where moduletype='Instrument'"
			+ "  and description='Deposited'))or (ih.ispaycheque='1' and  ih.id_status=(select id from egw_status where"
			+ " moduletype='Instrument'  and description='New'))) "
			+" AND ih.transactionnumber is not null";
		
		String brsEntryQuery=" SELECT (sum(case when ih.ispaycheque= '1' then "+instrumentsForBrsEntryTotal+" else 0 end ))  AS \"brs_creditTotalBrsEntry\", "
		+" (sum(case when ih.ispaycheque= '0' then "+instrumentsForBrsEntryTotal+" else 0 end))  AS \"brs_debitTotalBrsEntry\" "
		+" FROM egf_instrumentheader ih, bankentries br	WHERE   ih.bankAccountId = :bankAccountId"
		+" AND IH.transactiondate >= :fromDate  "
		+" AND IH.transactiondate <= :toDate "
		+" AND ( (ih.ispaycheque='0' and ih.id_status=(select id from egw_status where moduletype='Instrument' "
		+ " and description='Deposited')) or (ih.ispaycheque='1' and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='New'))) "
		+" AND br.instrumentHeaderid=ih.id and ih.transactionnumber is not null"	;
	
		if(LOGGER.isInfoEnabled())     LOGGER.info("  query  for  total : "+totalQuery);
		
		
		
		String unReconciledDrCr="";
		
		
		String creditTotal=null;
		String creditOthertotal=null;
		String debitTotal=null;
		String debitOtherTotal=null;
		String creditTotalBrsEntry=null;
		String debitTotalBrsEntry=null;
		
		try
		{
			SQLQuery totalSQLQuery = persistenceService.getSession().createSQLQuery(totalQuery);
			totalSQLQuery.setLong("bankAccountId",bankAccId);
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

			if(LOGGER.isInfoEnabled())     LOGGER.info("  query  for other than cheque/DD: "+otherTotalQuery);
			totalSQLQuery = persistenceService.getSession().createSQLQuery(otherTotalQuery);
			totalSQLQuery.setLong("bankAccountId",bankAccId);
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
			if(LOGGER.isInfoEnabled())     LOGGER.info("  query  for bankEntries: "+brsEntryQuery);

			totalSQLQuery = persistenceService.getSession().createSQLQuery(brsEntryQuery);
			totalSQLQuery.setLong("bankAccountId",bankAccId);
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
			
		}
		return unReconciledDrCr;
	}
	
	private String getExcludeStatuses() {
		
		List<AppConfigValues> configValuesByModuleAndKey = appConfigValueService.getConfigValuesByModuleAndKey("EGF","statusexcludeReport");
		final String statusExclude = configValuesByModuleAndKey.get(0).getValue();
		return statusExclude;
		
	}
	
	public List<ReconcileBean> getUnReconciledCheques(Long bankAccId,Date recDate) 
	{
		List<ReconcileBean> list=new ArrayList<ReconcileBean>();
		try{
		String voucherExcludeStatuses=getExcludeStatuses();
        String query=" select case when ih.instrumentNumber is null then 'Direct' else ih.instrumentNumber  end as \"chequeNumber\", " +
		" to_char(ih.instrumentdate,'dd/mm/yyyy') as \"chequeDate\" ,ih.instrumentAmount as \"chequeAmount\",rec.transactiontype as \"txnType\" , "
		+ " case when rec.transactionType='Cr' then  'P' else 'R' end as \"type\" " +" FROM BANKRECONCILIATION rec, BANKACCOUNT BANK,"
		+" VOUCHERHEADER v ,egf_instrumentheader ih, egf_instrumentotherdetails io, egf_instrumentVoucher iv	WHERE "
		+ "  ih.bankAccountId = BANK.ID AND bank.id =:bankAccId   AND IH.INSTRUMENTDATE <= :toDate  "
		+" AND v.ID= iv.voucherheaderid  and v.STATUS not in  ("+voucherExcludeStatuses+") AND "
		+" ((ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited'))or (ih.ispaycheque='1' and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='New'))) "
		+" AND rec.instrumentHeaderId=ih.id	 and iv.instrumentHeaderid=ih.id and io.instrumentheaderid=ih.id and ih.instrumentNumber is not null union  "
		+" select case when ih.transactionnumber is null then 'Direct' else ih.transactionnumber end as \"chequeNumber\", " +
		" to_char(ih.transactiondate,'dd/mm/yyyy') as \"chequedate\" ,ih.instrumentAmount as \"chequeamount\",rec.transactiontype as \"txnType\", case when rec.transactionType= 'Cr' then 'P' else 'R' end    as \"type\" " +
		" FROM BANKRECONCILIATION rec, BANKACCOUNT BANK,"
		+" VOUCHERHEADER v ,egf_instrumentheader ih, egf_instrumentotherdetails io, egf_instrumentVoucher iv	WHERE   ih.bankAccountId = BANK.ID AND bank.id = :bankAccId "
		+"   AND IH.INSTRUMENTDATE <= :toDate "
		+" AND v.ID= iv.voucherheaderid and v.STATUS not in  ("+voucherExcludeStatuses+") AND ((ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited'))or (ih.ispaycheque='1' and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='New'))) "
		+" AND rec.instrumentHeaderId=ih.id	 and iv.instrumentHeaderid=ih.id and io.instrumentheaderid=ih.id and ih.transactionnumber is not null"
		;
		
       // if(LOGGER.isInfoEnabled())    
        LOGGER.info("  query  for getUnReconciledCheques: "+query);
		/*String query=" SELECT decode(rec.chequeNumber, null, 'Direct', rec.chequeNumber) as \"chequeNumber\",rec.chequedate as \"chequedate\" ,amount as \"chequeamount\",transactiontype as \"txnType\" ,rec.type as \"type\" from bankreconciliation rec, bankAccount bank, voucherheader vh "
			+" where  rec.bankAccountId = bank.id AND bank.id ="+bankAccId+" and  rec.isReversed = 0 AND (rec.reconciliationDate > to_date('"+recDate+"'  || ' 23:59:59','DD-MON-YYYY HH24:MI:SS') "
			+" OR (rec.isReconciled = 0)) AND vh.VOUCHERDATE <= to_date('"+recDate+"'  || ' 23:59:59','DD-MON-YYYY HH24:MI:SS') and vh.id=rec.VOUCHERHEADERID and vh.STATUS<>4"
			+" union "
			+" select refno as \"chequeNumber\", txndate as \"chequedate\", txnamount as \"chequeamount\", decode(type,'R','Dr','Cr') as \"txnType\", "
			+" type as \"type\" from bankentries be,bankAccount bank where  be.bankAccountId = bank.id and bank.id ="+bankAccId+"  "
			+" and txndate<= to_date('"+recDate+"'  || ' 23:59:59','DD-MON-YYYY HH24:MI:SS') and voucherheaderid is null ";
*/
        
        
		SQLQuery createSQLQuery = persistenceService.getSession().createSQLQuery(query);
		createSQLQuery.setLong("bankAccId", bankAccId);
		createSQLQuery.setDate("toDate", recDate);
		createSQLQuery.setResultTransformer(Transformers.aliasToBean(ReconcileBean.class));
	    list = (List<ReconcileBean>)createSQLQuery.list();
         
		}
		catch(Exception e)
		{
			LOGGER.error("Exp in getUnReconciledCheques:"+e.getMessage());
			new ApplicationRuntimeException(e.getMessage());
		}
		
		return list;
	}
	
}
