/**
 * 
 */
package org.egov.services.report;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.model.report.FundFlowBean;
import org.egov.utils.Constants;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

/**
 * @author mani
 *
 */
@SuppressWarnings("unchecked")
public class FundFlowService extends PersistenceService {
	private static Logger LOGGER=Logger.getLogger(FundFlowService.class);
	SimpleDateFormat sqlformat=new SimpleDateFormat("dd-MMM-yyyy");
	private GenericHibernateDaoFactory genericDao;
	/**
	 * All amounts is in lakhs
	 */
	public List<FundFlowBean> getOutStandingPayments(Date asPerDate,Long fundId) {
		String voucherDate=sqlformat.format(asPerDate);
		List<AppConfigValues> appConfig = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"VOUCHER_STATUS_TO_CHECK_BANK_BALANCE");
		if(appConfig == null || appConfig.isEmpty())
			throw new ValidationException("","VOUCHER_STATUS_TO_CHECK_BANK_BALANCE is not defined in AppConfig");
		String voucherStatus = appConfig.get(0).getValue();
		
		//get BPVs for the cuurent date which are in the workflow
		StringBuffer outstandingPaymentQryStr=new StringBuffer(500);
		outstandingPaymentQryStr=
		outstandingPaymentQryStr.
		append	("SELECT DISTINCT( ba.accountnumber)      AS accountNumber ,  ROUND(SUM(ph.paymentamount)/100000,2) AS outStandingBPV"+
				" FROM voucherheader vh,paymentheader ph,bankaccount ba,eg_wf_states state where ph.state_id     =state.id "+
				"	and vh.id =ph.voucherheaderid and  ba.id=ph.bankaccountnumberid");
				if(fundId!=null && fundId!=-1)
				{
					outstandingPaymentQryStr.append(" and vh.fundid ="+fundId);
					outstandingPaymentQryStr.append(" and ba.fundid ="+fundId );
				}
		outstandingPaymentQryStr.append(" and vh.voucherdate <='")  
		.append(voucherDate)
		.append("' and state.value like '")
		.append(voucherStatus)
		.append("' group by accountNumber  ");   
		LOGGER.debug("Out Standing Payment Query "+outstandingPaymentQryStr.toString());
		Query outstandingQry=getSession().createSQLQuery(outstandingPaymentQryStr.toString())
		.addScalar("accountNumber")
		.addScalar("outStandingBPV")
		.setResultTransformer(Transformers.aliasToBean(FundFlowBean.class));
		return outstandingQry.list();
	}
	
	public List<FundFlowBean> getConcurrancePayments(Date asPerDate,Long fundId) {
		String voucherDate=sqlformat.format(asPerDate);
		List<AppConfigValues> appConfig = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"PAYMENT_WF_STATUS_FOR_BANK_BALANCE_CHECK");
		if(appConfig == null || appConfig.isEmpty())
			throw new ValidationException("","PAYMENT_WF_STATUS_FOR_BANK_BALANCE_CHECK is not defined in AppConfig");
		String voucherStatus ="";
		StringBuffer values=new StringBuffer(200);
		for(AppConfigValues app:appConfig)
		{
			values.append("'");
			values.append(app.getValue());
			values.append("',");
		}
		//need to ommit the last comma
		voucherStatus= values.substring(0, values.length()-1);
		
		//get BPVs for the cuurent date which are in the workflow
		StringBuffer conCurrancePaymentQryStr=new StringBuffer(500);
		conCurrancePaymentQryStr=
			conCurrancePaymentQryStr.
		append	("SELECT DISTINCT( ba.accountnumber)      AS accountNumber ,  ROUND(SUM(ph.paymentamount)/100000,2) AS concurranceBPV"+
				" FROM voucherheader vh,paymentheader ph,bankaccount ba,eg_wf_states state where ph.state_id     =state.id "+
				"	and vh.id =ph.voucherheaderid and  ba.id=ph.bankaccountnumberid");
			
			if(fundId!=null && fundId!=-1)
			{
				conCurrancePaymentQryStr.append(" and vh.fundid ="+fundId);
				conCurrancePaymentQryStr.append(" and ba.fundid ="+fundId);
			}
		conCurrancePaymentQryStr.append(" and to_char(created_date,'dd-Mon-yyyy') ='")  
		.append(voucherDate)
		.append("' and ( state.value in (")
		.append(voucherStatus)
		.append(")OR vh.status=0 ) group by accountNumber  ");  
		
		LOGGER.debug("Concurrancey payment "+conCurrancePaymentQryStr.toString());
		Query conCurranceQry=getSession().createSQLQuery(conCurrancePaymentQryStr.toString())
		.addScalar("accountNumber")
		.addScalar("concurranceBPV")
		.setResultTransformer(Transformers.aliasToBean(FundFlowBean.class));
		return conCurranceQry.list();
	}
	/**
	 *  All Payment Bank Accounts
	 */
	public List<FundFlowBean> getAllpaymentAccounts(Long fundId) {
		StringBuffer allPaymentAccounts=new StringBuffer(500);
		 allPaymentAccounts.append("select ba.id as bankAccountId, ba.accountnumber as accountNumber, ba.glcodeid as codeId,b.name as bankName " +
		 		"from bankaccount ba left outer join bankbranch bb  on ba.branchid=bb.id left outer " +
		 		"join bank b on bb.bankid=b.id where  ba.isactive='1' and ba.type in ('PAYMENTS','RECEIPTS_PAYMENTS') " );
		 		if(fundId!=null && fundId!=-1)
		 		{
		 			 allPaymentAccounts.append("and ba.fundid="+fundId);
		 		}
		
		 Query allPaymentAccountsQry=getSession().createSQLQuery(allPaymentAccounts.toString())
		 .addScalar("bankAccountId")
		 .addScalar("accountNumber")
		 .addScalar("bankName")
		 .setResultTransformer(Transformers.aliasToBean(FundFlowBean.class));
		 return allPaymentAccountsQry.list();
	}
	/**
	 * get All   Receipt Bank Accounts for the selected Fund
	 * 
	 */
	public List<FundFlowBean> getAllReceiptAccounts( Long fundId) {
		StringBuffer allAccounts=new StringBuffer(500);
		 allAccounts.append("select ba.id as bankAccountId, ba.accountnumber as accountNumber, ba.glcodeId as codeId,b.name as bankName " +
		 		"from bankaccount ba left outer join bankbranch bb  on ba.branchid=bb.id left outer " +
		 		"join bank b on bb.bankid=b.id where  ba.isactive='1' and ba.type in ('RECEIPTS') " );
		 if(fundId!=null && fundId!=-1)
		 {
			 allAccounts.append(" and ba.fundid="+fundId);
		 }
		 Query allAccountsQry=getSession().createSQLQuery(allAccounts.toString())
		 .addScalar("bankAccountId")
		 .addScalar("accountNumber")
		 .addScalar("bankName")
		 .setResultTransformer(Transformers.aliasToBean(FundFlowBean.class));
		 return allAccountsQry.list();
	}
	/**
	 * @return
	 */
	public List<FundFlowBean> getContraReceiptsForTheDay(Date asPerDate,Long fundId ) {
		String voucherDate=sqlformat.format(asPerDate);
		StringBuffer temp=new StringBuffer(1000);
		    temp=temp.append(" SELECT gl.glcodeid as codeId, ba.accountnumber as accountNumber, b.name as bankName,round(SUM(DECODE(gl.debitamount,NULL,0,gl.debitamount))/100000,2) AS btbReceipt FROM" +
			 " contrajournalvoucher CV  , voucherheader vh ,  generalledger gl, bankaccount ba, bankbranch bb,bank b WHERE "); 
			 if(fundId!=null && fundId!=-1)
			 {
				 temp.append(" vh.fundid ="+fundId+" AND ba.fundid         ="+fundId +" and"); 
			 }
			 temp.append(" vh.voucherdate    ='"+voucherDate+"'and gl.voucherheaderid= vh.id "+
			" and cv.voucherheaderid=vh.id and ba.id= cv.tobankaccountid and ba.glcodeid= gl.glcodeid AND vh.status =0 AND  ba.type  "+
			" IN ('PAYMENTS','RECEIPTS_PAYMENTS') and bb.bankid= b.id and ba.branchid=bb.id GROUP BY GL.GLCODEID,ba.accountnumber,b.name");
		    
		    List<FundFlowBean> tempPayList; 
			 Query tempQry=getSession().createSQLQuery(temp.toString())
			 .addScalar("accountNumber")       
			 .addScalar("bankName")
			 .addScalar("btbReceipt")
			 .setResultTransformer(Transformers.aliasToBean(FundFlowBean.class));
			 tempPayList=tempQry.list();
		return tempPayList;
	}
	/**
	 * get Receipt Bank Accounts  of selected Fund which has Contra payment for current day 
	 * @param voucherDate
	 * @return
	 */
	public List<FundFlowBean> getContraPaymentsForTheDay(Date asPerDate,Long fundId) {
		String voucherDate=sqlformat.format(asPerDate);
		StringBuffer qry=new StringBuffer(1000);
	    qry=qry.append(" SELECT gl.glcodeid as codeId, ba.accountnumber as accountNumber, b.name as bankName, round(SUM(DECODE(gl.creditamount,NULL,0,gl.creditamount))/100000,2) AS btbPayment FROM" +
		 " contrajournalvoucher CV  , voucherheader vh ,  generalledger gl, bankaccount ba, bankbranch bb,bank b WHERE ");
		    if(fundId!=null && fundId!=-1)
		    {
		    	qry.append(" vh.fundid ="+fundId+" AND ba.fundid         ="+fundId +" and "); 
		    }
	    	qry.append("vh.voucherdate    ='"+voucherDate+"'and gl.voucherheaderid= vh.id "+
		" and cv.voucherheaderid=vh.id and ba.id= cv.frombankaccountid and ba.glcodeid= gl.glcodeid AND vh.status =0 AND  ba.type  "+
		" IN ('RECEIPTS') and bb.bankid= b.id and ba.branchid=bb.id GROUP BY gl.glcodeId,ba.accountnumber,b.name");
	    List<FundFlowBean> tempList;                         
		 Query q=getSession().createSQLQuery(qry.toString())
		 .addScalar("accountNumber")       
		 .addScalar("bankName")
		 .addScalar("btbPayment")
		 .setResultTransformer(Transformers.aliasToBean(FundFlowBean.class));
		 tempList=q.list();
		 LOGGER.debug("account containg transactions ------"+tempList.size());
		return tempList;
	}
	
	/**
	 * get Receipt Bank Accounts  of selected Fund which has Contra payment for current day 
	 * @param voucherDate
	 * @return
	 */
	public List<FundFlowBean> getContraPaymentsForTheDayFromPaymentBanks(Date asPerDate,Long fundId) {
		String voucherDate=sqlformat.format(asPerDate);
		StringBuffer qry=new StringBuffer(1000);
	    qry=qry.append(" SELECT gl.glcodeid as codeId, ba.accountnumber as accountNumber, b.name as bankName, round(SUM(DECODE(gl.creditamount,NULL,0,gl.creditamount))/100000,2) AS btbPayment FROM" +
		 " contrajournalvoucher CV  , voucherheader vh ,  generalledger gl, bankaccount ba, bankbranch bb,bank b WHERE  "); 
		 if(fundId!=null && fundId!=-1)
		 {
			 qry=qry.append(" vh.fundid ="+fundId+" AND ba.fundid ="+fundId +" and ");
		 }
		 qry=qry.append(" vh.voucherdate    ='"+voucherDate+"'and gl.voucherheaderid= vh.id "+
		" and cv.voucherheaderid=vh.id and ba.id= cv.frombankaccountid and ba.glcodeid= gl.glcodeid AND vh.status =0 AND  ba.type  "+
		" IN ('RECEIPTS_PAYMENTS') and bb.bankid= b.id and ba.branchid=bb.id GROUP BY gl.glcodeId,ba.accountnumber,b.name");
	    List<FundFlowBean> tempList;                         
		 Query q=getSession().createSQLQuery(qry.toString())
		 .addScalar("accountNumber")       
		 .addScalar("bankName")
		 .addScalar("btbPayment")
		 .setResultTransformer(Transformers.aliasToBean(FundFlowBean.class));
		 tempList=q.list();
		 LOGGER.debug("account containg transactions ------"+tempList.size());
		return tempList;
	}
	public GenericHibernateDaoFactory getGenericDao() {
		return genericDao;
	}
	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}	
	
	
public BigDecimal	getBankBalance(Long bankaccountId,Date asPerDate)
	{
	 	
	try {
		asPerDate=sqlformat.parse(sqlformat.format(asPerDate));
		final Calendar calfrom = Calendar.getInstance();
		calfrom.setTime(asPerDate);
		calfrom.set(Calendar.HOUR, 0);
		calfrom.set(Calendar.MINUTE, 0);
		calfrom.set(Calendar.SECOND, 0);
		calfrom.set(Calendar.AM_PM, Calendar.AM);
		asPerDate= calfrom.getTime();
	
	} catch (Exception e) {
		throw new ValidationException(Arrays.asList(new ValidationError("cannot.format.date","Failed during date Formatting ")));
	}
	if(bankaccountId==null)
	{
		throw new ValidationException(Arrays.asList(new ValidationError("bankaccount.id.is.null","BankAccountId is not provided")));
	}
	setType(FundFlowBean.class);
	 FundFlowBean fundFlowBean=(FundFlowBean) this.find("from FundFlowBean where bankAccountId=? and to_date(reportDate)=?",BigDecimal.valueOf(bankaccountId),asPerDate);
	//Means Report is not Generated
	 if(fundFlowBean==null)
	 {
		throw new ValidationException(Arrays.asList(new ValidationError("fund.flow.report.not.generated.for.the.day","Fund Flow Report is not Generated Balance check Failed")));
	 }
		  BigDecimal bankBalance = (fundFlowBean.getOpeningBalance().add(fundFlowBean.getCurrentReceipt()));//since all amounts in lakh multiply by lakh and return
		  bankBalance=bankBalance.multiply(new BigDecimal(100000));
		  LOGGER.debug(" From Fund Flow report Openig Balance+Receipt for "+bankaccountId+" is "+bankBalance); 
		  bankBalance= bankBalance.subtract(getContraPayment(bankaccountId,asPerDate));
		  bankBalance= bankBalance.add(getContraReceipt(bankaccountId,asPerDate));
		  bankBalance= bankBalance.subtract(getOutStandingPayment(bankaccountId,asPerDate));
		  LOGGER.debug(" BankBalance for "+bankaccountId+" is "+bankBalance);
		  return bankBalance;
	}
/**
 * it is for single bankaccount 
 * @param bankaccountId
 * @param asPerDate
 */
private BigDecimal getContraPayment(Long bankaccountId, Date asPerDate) {
	StringBuffer qry=new StringBuffer(100);
		qry.append(" select decode(SUM(DECODE(gl.creditamount,NULL,0,gl.creditamount)),null,0,SUM(DECODE(gl.creditamount,NULL,0,gl.creditamount))) as payment from contrajournalvoucher cjv,"+
			 " voucherheader vh, generalledger gl where vh.id=gl.voucherheaderid and cjv.voucherheaderid= vh.id "+
			 " and vh.voucherdate='"+sqlformat.format(asPerDate)+"' and cjv.frombankaccountid="+bankaccountId+" and vh.status =0");
		List list = getSession().createSQLQuery(qry.toString()).list();
		BigDecimal contraPayment=(BigDecimal)list.get(0); 
		LOGGER.debug("Contra Payments For BankId "+bankaccountId +" And Date "+sqlformat.format(asPerDate)+" is : "+ contraPayment);
		return contraPayment;
}

/**
 * it is for single bankaccount 
 * @param bankaccountId
 * @param asPerDate
 * 
 */
private BigDecimal getContraReceipt(Long bankaccountId, Date asPerDate) {
	StringBuffer qry=new StringBuffer(100);
		qry.append(" select Decode(SUM(DECODE(gl.debitamount,NULL,0,gl.debitamount)),null,0,SUM(DECODE(gl.debitamount,NULL,0,gl.debitamount))) as receipt from contrajournalvoucher cjv,"+
			 " voucherheader vh, generalledger gl where vh.id=gl.voucherheaderid and cjv.voucherheaderid= vh.id "+
			 " and vh.voucherdate='"+sqlformat.format(asPerDate)+"' and cjv.tobankaccountid="+bankaccountId+" and vh.status =0");
		List list = getSession().createSQLQuery(qry.toString()).list();
		 BigDecimal contraReceipt	=	(BigDecimal)list.get(0);  
		LOGGER.debug("Contra Receipt For BankId "+bankaccountId +" And Date "+sqlformat.format(asPerDate)+" is : "+ contraReceipt);
		return contraReceipt;
}                  
	  
/**
 * it is for single bankaccount 
 * @param bankaccountId
 * @param asPerDate
 * @return
 */
private BigDecimal getOutStandingPayment(Long bankaccountId, Date asPerDate)
{
	List<AppConfigValues> appConfig = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"VOUCHER_STATUS_TO_CHECK_BANK_BALANCE");
	if(appConfig == null || appConfig.isEmpty())
		throw new ValidationException("","VOUCHER_STATUS_TO_CHECK_BANK_BALANCE is not defined in AppConfig");
	String voucherStatus = appConfig.get(0).getValue();
	StringBuffer outstandingPaymentQryStr=new StringBuffer(500);
	outstandingPaymentQryStr=
	outstandingPaymentQryStr.
	append	("SELECT Decode(SUM(Decode(ph.paymentamount,null,0,ph.paymentamount)),null,0,SUM(Decode(ph.paymentamount,null,0,ph.paymentamount))) AS concurranceBPV"+
			" FROM voucherheader vh,paymentheader ph,bankaccount ba,eg_wf_states state where ph.state_id     =state.id "+
			"	and vh.id =ph.voucherheaderid and  ba.id=ph.bankaccountnumberid and ba.id="+bankaccountId+"")
		.append(" and vh.voucherdate ='")  
		.append(sqlformat.format(asPerDate))
		.append("' and state.value like '")
		.append(voucherStatus)   
		.append("'");   
		
	List list = getSession().createSQLQuery(outstandingPaymentQryStr.toString()).list();
	BigDecimal outStandingPayment=(BigDecimal)list.get(0);
	LOGGER.debug("OutStanding payments for BankId "+bankaccountId +" And Date "+sqlformat.format(asPerDate)+" is : "+ outStandingPayment);
	return outStandingPayment;
}

}
