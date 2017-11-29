/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.egf.web.actions.brs;

import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.egf.model.ReconcileBean;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.services.instrument.InstrumentHeaderService;
import org.egov.services.instrument.InstrumentOtherDetailsService;
import org.egov.utils.FinancialConstants;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@Service
public class ManualReconcileHelper {

	private static  final Logger LOGGER = Logger.getLogger(ManualReconcileHelper.class);

	@Autowired
	private AppConfigValueService appConfigValueService;
	@Autowired
	@Qualifier("persistenceService")
	private PersistenceService persistenceService;

	@Autowired
	@Qualifier("instrumentOtherDetailsService")
	private InstrumentOtherDetailsService instrumentOtherDetailsService;
	@Autowired
	private EgwStatusHibernateDAO egwStatusHibernateDAO;
	
	@Autowired
	@Qualifier("instrumentHeaderService")
	private InstrumentHeaderService instrumentHeaderService;
	public Map<String,String> getUnReconciledDrCr(Long bankAccId,Date fromDate,Date toDate)  
	{
		Map<String,String> unreconMap=new LinkedHashMap<String,String>();
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

			
      /* ReconcileBean reconBean=new ReconcileBean();
       reconBean.setCreditAmount(BigDecimal.valueOf(creditTotal));
       reconBean.setDebitAmount(debitTotal);
       */
			creditTotal=creditTotal==null?"0":creditTotal;
			debitTotal=debitTotal==null?"0":debitTotal;
			creditOthertotal=creditOthertotal==null?"0":creditOthertotal;
			debitOtherTotal=debitOtherTotal==null?"0":debitOtherTotal;
			debitTotalBrsEntry=debitTotalBrsEntry==null?"0":debitTotalBrsEntry;
			
			unreconMap.put("Cheque/DD/Cash Payments",creditTotal);
			unreconMap.put("Cheque/DD/Cash Receipts",debitTotal);
			unreconMap.put("RTGS Payments",creditOthertotal);
			unreconMap.put("Other Receipts",debitOtherTotal);
			unreconMap.put("BRS Entry",debitTotalBrsEntry);
			
		/*//unReconciledDrCr="Cheque/DD/Cash Payments:"+(creditTotal != null ? creditTotal : "0" )+",RTGS Payments:"+(creditOthertotal!= null ? creditOthertotal : "0")
		+",Cheque/DD/Cash Receipts:"+(debitTotal!= null ? debitTotal : "0") +",Other Receipts:"+( debitOtherTotal!= null ? debitOtherTotal : "0")+""+
		"/"+(creditTotalBrsEntry!= null ? creditTotalBrsEntry : "0") +",Net:"+( debitTotalBrsEntry!= null ? debitTotalBrsEntry : "0")+"";*/
		}
		catch(Exception e)
		{
			LOGGER.error("Exp in getUnReconciledDrCr"+e.getMessage());
			
		}
		return unreconMap;
	}
	
	private String getExcludeStatuses() {
		
		List<AppConfigValues> configValuesByModuleAndKey = appConfigValueService.getConfigValuesByModuleAndKey("EGF","statusexcludeReport");
		final String statusExclude = configValuesByModuleAndKey.get(0).getValue();
		return statusExclude;
		
	}
	
	public List<ReconcileBean> getUnReconciledCheques(ReconcileBean reconBean) 
	{
		List<ReconcileBean> list=new ArrayList<ReconcileBean>();
		String instrumentCondition="";
		if(reconBean.getInstrumentNo()!=null && !reconBean.getInstrumentNo().isEmpty())
		{
			instrumentCondition="and (ih.instrumentNumber='"+reconBean.getInstrumentNo()+"' or ih.transactionnumber='"+reconBean.getInstrumentNo()+"' )";
		}
		try{
		String voucherExcludeStatuses=getExcludeStatuses();
        StringBuffer query=new StringBuffer().append(" select string_agg(distinct v.vouchernumber, ',') as \"voucherNumber\" ,ih.id as \"ihId\", case when ih.instrumentNumber is null then 'Direct' else ih.instrumentNumber  end as \"chequeNumber\", " +
		" to_char(ih.instrumentdate,'dd/mm/yyyy') as \"chequeDate\" ,ih.instrumentAmount as \"chequeAmount\",rec.transactiontype as \"txnType\" , "
		+ " case when rec.transactionType='Cr' then  'Payment' else 'Receipt' end as \"type\" " +" FROM BANKRECONCILIATION rec, BANKACCOUNT BANK,"
		+" VOUCHERHEADER v ,egf_instrumentheader ih, egf_instrumentotherdetails io, egf_instrumentVoucher iv	WHERE "
		+ "  ih.bankAccountId = BANK.ID AND bank.id =:bankAccId   AND IH.INSTRUMENTDATE <= :toDate  "
		+" AND v.ID= iv.voucherheaderid  and v.STATUS not in  ("+voucherExcludeStatuses+")  "  +instrumentCondition 
		+" AND ((ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited') and ih.ispaycheque='0') or (ih.ispaycheque='1' and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='New'))) "
		+" AND rec.instrumentHeaderId=ih.id	 and iv.instrumentHeaderid=ih.id and io.instrumentheaderid=ih.id and ih.instrumentNumber is not null"
		+ " group by ih.id,rec.transactiontype "
	
		+ " union  "
		
		+" select string_agg(distinct v.vouchernumber, ',') as \"voucherNumber\" , ih.id as \"ihId\", case when ih.transactionnumber is null then 'Direct' else ih.transactionnumber end as \"chequeNumber\", " +
		" to_char(ih.transactiondate,'dd/mm/yyyy') as \"chequedate\" ,ih.instrumentAmount as \"chequeamount\",rec.transactiontype as \"txnType\", case when rec.transactionType= 'Cr' then 'Payment' else 'Receipt' end    as \"type\" " +
		" FROM BANKRECONCILIATION rec, BANKACCOUNT BANK,"
		+" VOUCHERHEADER v ,egf_instrumentheader ih, egf_instrumentotherdetails io, egf_instrumentVoucher iv	WHERE   ih.bankAccountId = BANK.ID AND bank.id = :bankAccId "
		+"   AND IH.INSTRUMENTDATE <= :toDate " +instrumentCondition 
		+" AND v.ID= iv.voucherheaderid and v.STATUS not in  ("+voucherExcludeStatuses+") AND ((ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited') and ih.ispaycheque='0')or (ih.ispaycheque='1' and  ih.id_status=(select id from egw_status where moduletype='Instrument'  and description='New'))) "
		+" AND rec.instrumentHeaderId=ih.id	 and iv.instrumentHeaderid=ih.id and io.instrumentheaderid=ih.id and ih.transactionnumber is not null"
		+"   group by ih.id,rec.transactiontype order by 4 " );
       
        
        
        if(reconBean.getLimit()!=null & reconBean.getLimit()!=0)
        {
        	query.append(" limit "+reconBean.getLimit());
        }
		
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
        
        
		SQLQuery createSQLQuery = persistenceService.getSession().createSQLQuery(query.toString());
		createSQLQuery.setLong("bankAccId", reconBean.getAccountId());
		createSQLQuery.setDate("toDate", reconBean.getReconciliationDate());
		createSQLQuery.addScalar("voucherNumber",StringType.INSTANCE);
		createSQLQuery.addScalar("ihId",LongType.INSTANCE);
		createSQLQuery.addScalar("chequeDate",StringType.INSTANCE);
		createSQLQuery.addScalar("chequeNumber",StringType.INSTANCE);
		createSQLQuery.addScalar("chequeAmount",BigDecimalType.INSTANCE);
		createSQLQuery.addScalar("txnType",StringType.INSTANCE);
		createSQLQuery.addScalar("type",StringType.INSTANCE);
		createSQLQuery.setResultTransformer(Transformers.aliasToBean(ReconcileBean.class));
	    list = (List<ReconcileBean>)createSQLQuery.list();
         
		}
		catch(Exception e)
		{
			LOGGER.error("Exp in getUnReconciledCheques:"+e.getMessage());
			throw new ApplicationRuntimeException(e.getMessage());
		}
		
		return list;
	}

	@Transactional
	public void update(List<Date> reconDates, List<Long> instrumentHeaders) {
		int i=0;
		EgwStatus reconciledStatus = egwStatusHibernateDAO.getStatusByModuleAndCode(FinancialConstants.STATUS_MODULE_INSTRUMENT, FinancialConstants.INSTRUMENT_RECONCILED_STATUS);
		for(Date reconcileOn:reconDates)
		{
			if(reconcileOn!=null)
			{
				Long ihId = instrumentHeaders.get(i);
				InstrumentHeader ih = instrumentHeaderService.reconcile(reconcileOn, ihId,reconciledStatus ); 
				instrumentOtherDetailsService.reconcile(reconcileOn, ihId,ih.getInstrumentAmount());
			    
			}
			i++;
		}
		
		
	}
	
}
