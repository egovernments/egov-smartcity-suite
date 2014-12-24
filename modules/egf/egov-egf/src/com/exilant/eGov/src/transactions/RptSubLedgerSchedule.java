/*
 * Created on Oct 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.transactions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesHibernateDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.ReturningWork;

import com.exilant.GLEngine.GeneralLedgerBean;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */ 
public class RptSubLedgerSchedule 
{
	
	double totalDr=0.0,totalCr=0.0,totalOpgBal=0.0,totalClosingBal=0.0;
	ResultSet resultset;
	Statement statement;
	NumberFormat formatter;
	TaskFailedException taskExc;
	String glCode,accEntityId,fundId,fyId;
	String subLedgerTable;
	HashMap hm_opBal;
	LinkedList dataList = new LinkedList();
    private static final Logger LOGGER = Logger.getLogger(RptSubLedgerSchedule.class);
	public RptSubLedgerSchedule(){} 
	
	//code for SubLedger type
	public LinkedList getSubLedgerTypeSchedule(final GeneralLedgerBean reportBean)
	throws TaskFailedException
	{
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<LinkedList>() {

			@Override
			public LinkedList execute(Connection connection) throws SQLException {
			
			    try { statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);}catch(Exception e){LOGGER.info("Exception in creating statement:"+statement);}
			    formatter = new DecimalFormat();
			    formatter = new DecimalFormat("###############.00");
			    glCode = reportBean.getGlcode();
			    fundId = reportBean.getFund_id(); 
			    accEntityId = reportBean.getAccEntityId();
			    reportBean.setAccEntityId(accEntityId);
			    String startDate="";
			    String endDate="" ;
			    String formstartDate="";
			    String formendDate=""; 
			        
		        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
		        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
		        Date dt=new Date();        
		        try
		        {
		        endDate=(String)reportBean.getEndDate();
		        dt = sdf.parse(endDate );
		        formendDate = formatter1.format(dt);
		        }
		        catch(Exception e){
		            LOGGER.info("inside the try-startdate"+e);
		        }
		        try
		        {   startDate=(String)reportBean.getStartDate();
		            if(!startDate.equalsIgnoreCase("null")){
		               dt = sdf.parse(startDate);
		            formstartDate = formatter1.format(dt);
		            }
		        }

		        catch(Exception e){
		            LOGGER.error("inside the try-startdate"+e);
		        }            
		            startDate = formstartDate;
		        endDate = formendDate;
		        String startDateformat = startDate;
		        String startDateformat1 = "";
		        try{
		        dt = formatter1.parse(startDateformat);
		       // startDateformat1 = sdf.format(dt);
		        }
			        catch(Exception e){
			            LOGGER.info("Parse Exception"+e);
			        }               
			
			    fyId = getFYID(startDate);
			    try
			    {
			    getSubQuery(startDate, endDate);
			    formatSLTypeReport();
			    reportBean.setAccName(getAccountname(glCode));
			    }
			    catch(Exception exception)
				{
			    	LOGGER.error("Exp="+exception.getMessage());
				}
			   
			   return dataList;
				
			}
		});
		
		
	}
	
	private void getSubQuery(String startDate,String endDate) throws  Exception
	{
		String defaultStatusExclude=null;
		List<AppConfigValues> listAppConfVal=new AppConfigValuesHibernateDAO(AppConfigValues.class, HibernateUtil.getCurrentSession()).
		getConfigValuesByModuleAndKey("finance","statusexcludeReport");
		if(null!= listAppConfVal)
		{
			defaultStatusExclude = ((AppConfigValues)listAppConfVal.get(0)).getValue();
		}
		else
		{
			throw new EGOVRuntimeException("Exlcude statusses not  are not defined for Reports");
		}

		String query;
				
		query= "SELECT sltypeid,slid, SUM (opgcredit) AS \"OpgCreditBal\",SUM(opgdebit) AS \"OpgDebitBal\", SUM (previouscredit)AS \"PrevCredit\", "
				+" SUM(previousdebit) AS \"PrevDebit\", SUM (creditamount) AS \"Credit\",SUM(debitamount) AS \"Debit\" " 
				+" FROM (SELECT adk.detailtypeid sltypeid ,adk.detailkey slid,"
				+" NVL((SELECT openingcreditbalance FROM transactionsummary WHERE glcodeid = coa.ID AND openingcreditbalance > 0 AND adk.detailtypeid = accountdetailtypeid"
				+" AND adk.detailkey = accountdetailkey AND fundid = "+fundId+" AND financialyearid IN (SELECT ID FROM financialyear WHERE startingdate <= TO_DATE ('"+startDate+"')" 
				+" AND endingdate >= TO_DATE ('"+endDate+"'))),0) opgcredit,NVL((SELECT openingdebitbalance FROM transactionsummary "
				+" WHERE glcodeid = coa.ID AND openingdebitbalance > 0 AND adk.detailtypeid = accountdetailtypeid AND adk.detailkey = accountdetailkey"
				+" AND fundid = "+fundId+" AND financialyearid =(SELECT ID FROM financialyear WHERE startingdate <=TO_DATE('"+startDate+"')"
				+" AND endingdate >= TO_DATE (('"+endDate+"')))),0) opgdebit,NVL((SELECT SUM (gld.amount )FROM generalledgerdetail gld,generalledger gl,"
				+" voucherheader vh WHERE gld.detailtypeid = adk .detailtypeid AND gld.detailkeyid = adk.detailkey AND gld.generalledgerid = gl.ID"
				+" AND gl.glcodeid = coa.ID AND gl.creditamount > 0 AND gl.voucherheaderid = vh .ID AND vh.voucherdate >=(SELECT startingdate"
				+" FROM financialyear WHERE startingdate <= TO_DATE('"+startDate+"') AND endingdate >= TO_DATE ('"+endDate+"')) AND vh.voucherdate <="
				+" TO_DATE(TO_DATE ('"+startDate+"')-1)AND vh.fundid = "+fundId+" AND vh.status not in ("+defaultStatusExclude+")),0) previouscredit,NVL((SELECT SUM (gld.amount )"
				+" FROM generalledgerdetail gld,generalledger gl,voucherheader vh WHERE gld.detailtypeid = adk .detailtypeid AND gld.detailkeyid = adk .detailkey"
				+" AND gld.generalledgerid = gl .ID AND gl.glcodeid = coa .ID AND gl.debitamount > 0 AND gl.voucherheaderid = vh .ID AND vh.voucherdate >="
				+" (SELECT startingdate FROM financialyear WHERE startingdate <=TO_DATE('"+startDate+"') AND endingdate >= TO_DATE ('"+endDate+"')) AND vh.voucherdate <="
				+" TO_DATE(TO_DATE ('"+startDate+"') - 1) AND vh.fundid = "+fundId+" AND vh.status not in ("+defaultStatusExclude+")),0) previousdebit,NVL((SELECT SUM (gld.amount ) FROM generalledgerdetail gld,"
				+" generalledger gl,voucherheader vh WHERE gld.detailtypeid = adk .detailtypeid AND gld.detailkeyid = adk .detailkey AND gld.generalledgerid = gl.ID"
				+" AND gl.glcodeid = coa .ID AND gl.creditamount > 0 AND gl.voucherheaderid = vh .ID AND vh.voucherdate >= TO_DATE ('"+startDate+"') AND vh.voucherdate <= TO_DATE ('"+endDate+"')"
				+" AND vh.fundid = "+fundId+" AND vh.status not in ("+defaultStatusExclude+")), 0 ) creditamount,NVL((SELECT SUM (gld.amount ) FROM generalledgerdetail gld,generalledger gl,"
				+" voucherheader vh WHERE gld.detailtypeid = adk .detailtypeid AND gld.detailkeyid = adk .detailkey AND gld.generalledgerid = gl .ID" 
				+" AND gl.glcodeid = coa.ID AND gl.debitamount > 0 AND gl.voucherheaderid = vh .ID AND vh.voucherdate >= TO_DATE ('"+startDate+"')"
				+" AND vh.voucherdate <= TO_DATE ('"+endDate+"') AND vh.fundid = "+fundId+" AND vh.status not in ("+defaultStatusExclude+")),0 ) debitamount FROM chartofaccounts coa," 
				+" accountdetailkey adk,chartofaccountdetail cod WHERE coa.ID = cod .glcodeid AND cod.detailtypeid = adk .detailtypeid AND adk.detailtypeid = "+accEntityId+""
				+" AND coa.ID IN (SELECT ID FROM chartofaccounts WHERE glcode = '"+glCode+"')) GROUP BY sltypeid,slid HAVING SUM (opgcredit)+ SUM (opgdebit)"
				+" + SUM (previouscredit)+ SUM (previousdebit)+ SUM (creditamount)+ SUM (debitamount) > 0 ";
		LOGGER.info("QUERY..."+query); 
		try{
			GeneralLedgerBean gb=null;
	 		resultset = statement.executeQuery(query);
			PersistenceService ps = new PersistenceService();
			ps.setSessionFactory(new SessionFactory());
			Accountdetailtype accountdetailtype = (Accountdetailtype) ps.find(" from Accountdetailtype where id=?", Integer.valueOf(accEntityId));
			EntityType entity;
	 		while(resultset.next())
	 		{
	 			gb=new GeneralLedgerBean();  
	 				double openingBal=0.0;
	    			double closingBal=0.0;    	
	    			double opgCredit=0.0;
	    			double opgDebit=0.0;
	    			double prevDebit=0.0;
	    			double prevCredit=0.0;
	    			double debitamount=0.0;
	    			double creditamount=0.0; 			
	    			try
	    			{
	    				entity =  (EntityType) ps.find(" from "+accountdetailtype.getFullQualifiedName()+ " where id=? ", Integer.valueOf(resultset.getInt("slid")));
	    			}catch(Exception ee)
	    			{
	    				entity =  (EntityType) ps.find(" from "+accountdetailtype.getFullQualifiedName()+ " where id=? ", Long.valueOf(resultset.getLong("slid")));
	    			}
	    			gb.setCode(entity.getCode());
	    			gb.setName(entity.getName());
	    			gb.setAccEntityKey(resultset.getString("slid"));
	    			
	    			if(resultset.getString("Debit")!= null)
	    				debitamount=resultset.getDouble("Debit");
    	 			if(resultset.getString("Credit")!= null)
    	 				creditamount=resultset.getDouble("Credit");
    	 			if(resultset.getString("OpgCreditBal")!= null)
    	 				opgCredit=resultset.getDouble("OpgCreditBal");
    	 			if(resultset.getString("OpgDebitBal")!= null)
    	 				opgDebit=resultset.getDouble("OpgDebitBal");
    	 			if(resultset.getString("PrevDebit")!= null)
    	 				prevDebit=resultset.getDouble("PrevDebit");
    	 			if(resultset.getString("PrevCredit")!= null)
    	 				prevCredit=resultset.getDouble("PrevCredit");
    	 			
    	 			openingBal=opgCredit+prevCredit-(opgDebit+prevDebit);
    	 			LOGGER.debug("Hello............. "+openingBal+"==");
    	 			if(openingBal>0)
    	 			{
    	 				gb.setOpeningBal(""+numberToString(((Double)openingBal).toString())+" Cr");
    	 				totalOpgBal=totalOpgBal+openingBal;
    	 			}
    	 			else if(openingBal<0)
    	 			{
    	 				totalOpgBal=totalOpgBal+openingBal;    	 				
    	 				double openingBal1=openingBal*-1;
    	 				gb.setOpeningBal(""+numberToString(((Double)openingBal1).toString())+" Dr");    	
    	 			}
    	 			else
    	 				gb.setOpeningBal("&nbsp;");
    	 			
    	 			closingBal=openingBal+creditamount-debitamount;
    	 			if(closingBal>0)
    	 			{
    	 				gb.setClosingBal(""+numberToString(((Double)closingBal).toString())+" Cr");
    	 				
    	 			}    	 			
    	 			else if(closingBal<0)
    	 			{
    	 				double closingBal1=closingBal*-1;
    	 				gb.setClosingBal(""+numberToString(((Double)closingBal1).toString())+" Dr");    	 				
    	 			}
    	 			else
    	 				gb.setClosingBal("&nbsp;");    
    	 			
    	 			if(debitamount>0)
    	 			{    	 				
    	 				gb.setDebitamount(""+numberToString(((Double)debitamount).toString()));    	 				
    	 				totalDr=totalDr+debitamount;
    				}
    	 			else
    	 				gb.setDebitamount("&nbsp;");
    	 			if(creditamount>0)
    				{					
    					gb.setCreditamount(""+numberToString(((Double)creditamount).toString()));
    	 				totalCr=totalCr+(creditamount);
    				}
    	 			else
    	 				gb.setCreditamount("&nbsp;");
    				
    				totalClosingBal=totalOpgBal+totalCr-totalDr;
    				dataList.add(gb);
	 		}
		}
		catch(Exception e)
		{
			LOGGER.error("Error in getReport=="+e.getMessage());
			throw new Exception();
		}		
	}
	
	private void formatSLTypeReport()
	{
		GeneralLedgerBean gb=new GeneralLedgerBean();
		gb.setAccEntityKey("");
		gb.setCode("<hr noshade color=black size=1><b>Total:<hr noshade color=black size=1></b>");
		gb.setName("<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>"); 
		
		if(totalOpgBal>0)
		{
			gb.setOpeningBal("<hr noshade color=black size=1><b>"+numberToString(((Double)totalOpgBal).toString())+" Cr<hr noshade color=black size=1></b>");
			
		}
		else if(totalOpgBal<0)
		{    			
			totalOpgBal=totalOpgBal*-1;
			gb.setOpeningBal("<hr noshade color=black size=1><b>"+numberToString(((Double)totalOpgBal).toString())+" Dr<hr noshade color=black size=1></b>");
		}
		else if(totalOpgBal==0.0)
		{
			gb.setOpeningBal("<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>"); 	
		}
		if(totalClosingBal>0)
		{
			gb.setClosingBal("<hr noshade color=black size=1><b>"+numberToString(((Double)totalClosingBal).toString())+" Cr<hr noshade color=black size=1></b>");
		}
		else if(totalClosingBal<0)
		{
			totalClosingBal=totalClosingBal*-1;    		
			gb.setClosingBal("<hr noshade color=black size=1><b>"+numberToString(((Double)totalClosingBal).toString())+" Dr<hr noshade color=black size=1></b>");
		}
		else if(totalClosingBal==0.0)
		{
			gb.setClosingBal("<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>"); 	
		}
		gb.setDebitamount("<hr noshade color=black size=1><b>"+numberToString(((Double)totalDr).toString())+"<hr noshade color=black size=1></b>");
		gb.setCreditamount("<hr noshade color=black size=1><b>"+numberToString(((Double)totalCr).toString())+"<hr noshade color=black size=1></b>");  
		
		dataList.add(gb);
	}    
	
	private String getFYID(String sDate){
		String fyId="";
		try{
			ResultSet rset= statement.executeQuery("SELECT id FROM financialYear " +
					"WHERE startingDate<='"+sDate+"' AND endingDate>='"+sDate+"'");
			if(rset.next()) fyId = rset.getString("id");
			rset = null;
		}catch(SQLException ex){
			fyId="";
			LOGGER.info("Error GeneralLedger->getFYID(): " + ex.toString());
			LOGGER.error("Exp="+ex.getMessage());
		}
		return fyId;
	}	

	private String getAccountname(final String glCode){
		
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection connection) throws SQLException {
				
				String accName="";
				try{
					Statement st = connection.createStatement();
					ResultSet rset = st.executeQuery("select name from chartofaccounts where glCode='"+glCode+"'");
					rset.next();
					accName = rset.getString(1);
					rset.close();
					st.close();
				}catch(Exception sqlex){
					LOGGER.error("Exp="+sqlex.getMessage());
					return null;
				}
				return accName;
			}
		});
	}	
    
    public static StringBuffer numberToString(final String strNumberToConvert)
    {
        String strNumber="",signBit="";
        if(strNumberToConvert.startsWith("-"))
        {
            strNumber=""+strNumberToConvert.substring(1,strNumberToConvert.length());
            signBit="-";
        }
        else strNumber=""+strNumberToConvert;
        DecimalFormat dft = new DecimalFormat("##############0.00");
        String strtemp=""+dft.format(Double.parseDouble(strNumber));            
        StringBuffer strbNumber=new StringBuffer(strtemp);                       
        int intLen=strbNumber.length();
           
        for(int i=intLen-6;i>0;i=i-2)                      
        {            
            strbNumber.insert(i,',');               
        }                
       if(signBit.equals("-"))strbNumber=strbNumber.insert(0,"-");
        return strbNumber;
    }

}