/* 
 * Created on Jan 19, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;

/**
 * @author Lakshmi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CommnFunctions 
{
    private static final Logger LOGGER=Logger.getLogger(CommnFunctions.class);
    
    private Statement statement;
    private ResultSet resultset;
    private static TaskFailedException taskExc;
    /**/
    public String reqFundId[];
    public String reqFundName[];
    /**
     * This method extracts all the fund ids and fund names.
     * @param conn          :reference of Connection
     * @param fundId        :The id of the fund for which the opening balances has to be calculated
     * @param startDate     :The start date of the financial year for which opening balance has to be calculated
     * @param endDate       :The end date of the financial year for which opening balance has to be calculated.
     * @param effFilter     :The Effective Date Filter
     * @param type1         :The type of account code (A,E,L,I)
     * @param type2         :The type of account code (A,E,L,I)
     * @throws Exception
     */
    @Deprecated
    public void getFundList(Connection conn,String fundId,String startDate, String endDate)throws Exception
    {
        String fundCondition="";
        //String fundCondition1="";
       
        if(!fundId.equalsIgnoreCase(""))
        {
            fundCondition=" AND Id="+fundId+" ";
            //fundCondition1="AND transactionsummary.fundId="+fundId+" ";
        }
        try
        { 
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY );
            /*String  query = " select f.id,f.name from fund f where (f.id in(SELECT unique vh.fundId FROM chartofaccounts  coa,generalledger gl, "
              +" voucherHeader vh WHERE  vh.ID =  gl.VOUCHERHEADERID AND  gl.glcode=coa.glcode "
              +" AND vh.VOUCHERDATE >= '"+startDate+"' AND vh.VOUCHERDATE <= '"+endDate+"' "+fundCondition+" ) or  f.id in(select unique fundid from transactionsummary WHERE financialyearid=(SELECT id FROM financialyear WHERE startingdate<='"+endDate+"' AND endingdate>='"+endDate+"')"+fundCondition1+")) and f.isactive=1 and f.isnotleaf!=1 "
              +" order by f.id ";*/
            String  query = " select id,name from fund where isactive='1' and isnotleaf!='1' "+fundCondition+" order by id";
            LOGGER.info("getFundList: "+query);
            resultset = statement.executeQuery(query);
            int resSize=0,i=0;
            if(resultset.last())
            resSize=resultset.getRow();
            reqFundId=new String[resSize];
            reqFundName=new String[resSize];
            LOGGER.info("resSize  "+resSize);
            resultset.beforeFirst();
            while(resultset.next())
            {
                reqFundId[i]=resultset.getString(1);
                reqFundName[i]=resultset.getString(2);
                i+=1;

            }
        }
        catch(Exception  ex)
        {
            LOGGER.error("Error in getting fund list");
            throw taskExc;
        }
    }
    
    
    public void getFundList(final String fundId,final String startDate,final  String endDate)throws Exception
    {
    	
    	HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection con) throws SQLException {
				 String fundCondition="";
			        //String fundCondition1="";
			       
			        if(!fundId.equalsIgnoreCase(""))
			        {
			            fundCondition=" AND Id="+fundId+" ";
			            //fundCondition1="AND transactionsummary.fundId="+fundId+" ";
			        }
			        try
			        { 
			            statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY );
			            /*String  query = " select f.id,f.name from fund f where (f.id in(SELECT unique vh.fundId FROM chartofaccounts  coa,generalledger gl, "
			              +" voucherHeader vh WHERE  vh.ID =  gl.VOUCHERHEADERID AND  gl.glcode=coa.glcode "
			              +" AND vh.VOUCHERDATE >= '"+startDate+"' AND vh.VOUCHERDATE <= '"+endDate+"' "+fundCondition+" ) or  f.id in(select unique fundid from transactionsummary WHERE financialyearid=(SELECT id FROM financialyear WHERE startingdate<='"+endDate+"' AND endingdate>='"+endDate+"')"+fundCondition1+")) and f.isactive=1 and f.isnotleaf!=1 "
			              +" order by f.id ";*/
			            String  query = " select id,name from fund where isactive='1' and isnotleaf!='1' "+fundCondition+" order by id";
			            LOGGER.info("getFundList: "+query);
			            resultset = statement.executeQuery(query);
			            int resSize=0,i=0;
			            if(resultset.last())
			            resSize=resultset.getRow();
			            reqFundId=new String[resSize];
			            reqFundName=new String[resSize];
			            LOGGER.info("resSize  "+resSize);
			            resultset.beforeFirst();
			            while(resultset.next())
			            {
			                reqFundId[i]=resultset.getString(1);
			                reqFundName[i]=resultset.getString(2);
			                i+=1;

			            }
			            HibernateUtil.release(statement, resultset);
			        }
			        catch(Exception  ex)
			        {
			            LOGGER.error("Error in getting fund list");
			         }
				
			}
		});
    	
       
    }
    
    
    /**
     * Get the opening balance for the list of account codes for a particular fund if fund is passed as a parameter 
     * for a particular financial year. Else get the opening balance of all the account codes irrespective of fund 
     * for a particular financial year
     * @param fundId        :The id of the fund for which the opening balances has to be calculated
     * @param type1         :The type of account code (A,I)
     * @param type2         :The type of account code (L,E)
     * @param substringVal  :The no of digits to pick from the glcode for grouping. (2 for major code, 3 for minor code)
     * @param startDate     :The start date of the financial year for which opening balance has to be calculated
     * @param endDate       :The end date of the financial year for which opening balance has to be calculated.
     * @param classification:Classification of the code for which opening balance has to be calculated
     * @param reqFundId1    :List of fundIds 
     * @param openingBal    :This parameter is called by reference so it  acts as input & output
     * @throws Exception
     */
   public void getOpeningBalance(Connection conn,String fundId, String type1,String type2,String substringVal, String startDate, String endDate, int classification,String reqFundId1[],HashMap openingBal)throws Exception
   {
       String fundCondition="";
        if(!fundId.equalsIgnoreCase(""))
        {
            fundCondition="AND f.Id="+fundId+" ";
        }
        String glcode="",fuId=""; 
        String query="SELECT substr(coa.glcode,0,"+substringVal+") as \"glcode\",ts.fundid as \"fundid\" ,"
                +" decode(coa.type,'"+type2+"',sum(ts.openingcreditbalance)-sum(ts.openingdebitbalance),sum(ts.openingdebitbalance)-sum(ts.openingcreditbalance)) as \"amount\" "
                +" FROM transactionsummary ts,  chartofaccounts coa,fund  f   WHERE (coa.TYPE = '"+type1+"' OR coa.TYPE = '"+type2+"') and coa.id = ts.glcodeid "
                +" AND financialyearid =(SELECT ID FROM financialyear WHERE startingdate <= '"+startDate+"' AND endingdate >= '"+endDate+"')  "+fundCondition+" and f.id=ts.fundid and f.isactive='1' and f.isnotleaf!='1' "
                +" GROUP BY substr(coa.glcode,0,"+substringVal+"), fundid ,coa.type ";
        LOGGER.info("query "+query);
        try
        {
            getFundList(fundId,startDate, endDate);
            statement = conn.createStatement();
            resultset = statement.executeQuery(query);
            Double opeBal = null;
            HashMap openingBalsubList = null;
            while(resultset.next())
            {
                glcode=resultset.getString("glcode");
                fuId=resultset.getString("fundid");
                opeBal=resultset.getDouble("amount");
               if(!openingBal.containsKey(glcode))
                {
                    openingBalsubList=new HashMap();
                    for(int i=0;i<reqFundId1.length;i++)
                    {
                        if(reqFundId1[i].equalsIgnoreCase(fuId))
                            openingBalsubList.put(reqFundId1[i],opeBal);
                        else
                            openingBalsubList.put(reqFundId1[i],new Double(0));
                    }
                    openingBal.put(glcode,openingBalsubList);
                }
                else
                {
                    ((HashMap)openingBal.get(glcode)).put(fuId,opeBal);
                }
 
            }
        }
        catch(Exception e)
        {
            LOGGER.info("Error in getOpeningBalance");
            throw taskExc;
        }

   }
   /**
    * Get the sum of debit amount minus the sum of credit amounts for the list of account codes for a particular fund 
    * from the GL if fund is passed as a parameter for a particular financial year. Else get the sum of debit amount 
    * minus the sum of credit amounts of all the account codes irrespective of fund for a particular financial year
    * @param fundid          :The id of the fund for which the opening balances has to be calculated
    * @param type1           :The type of account code (A,E,L,I)
    * @param type2           :The type of account code (A,E,L,I)
    * @param substringVal    :The no of digits to pick from the glcode for grouping. (2 for major code, 3 for minor code)
    * @param startDate       :The start date of the financial year for which opening balance has to be calculated
    * @param endDate         :The end date of the financial year for which opening balance has to be calculated
    * @param classification  :Classification of the code for which opening balance has to be calculated
    * @param effFilter       :The Effective Date Filter
    * @param txnBalancehasmap:Since HashMap is called by Reference same list will get effected in called method
    * @throws Exception     
    */
   public void   getTxnBalance(Connection conn,String fundid, String type1,String type2,String substringVal, String startDate, String endDate, int classification,String effFilter,HashMap txnBalancehasmap)throws Exception
   {
        String fundCondition=" ";
        if(fundid!=null && !fundid.equals(""))
        {
            fundCondition=" and vh.fundid="+fundid;
        }
        
        String  query1 = "SELECT SUBSTR(coa.GLCODE,1,2)as \"glCode\",vh.fundid as \"fundId\",decode(sum(gl.debitamount)-sum(gl.creditAmount),null,0,sum(gl.debitamount)-sum(gl.creditAmount))"
        +"as \"amount\" FROM chartofaccounts  coa,generalledger gl,"
        +" voucherHeader vh WHERE coa.TYPE = '"+type1+"' and vh.ID =  gl.VOUCHERHEADERID AND  gl.glcode=coa.glcode "
        +" AND vh.VOUCHERDATE >= '"+startDate+"' AND vh.VOUCHERDATE <= '"+endDate+"'"+fundCondition+"  " +effFilter;
        LOGGER.debug("getI: "+query1);
        try 
        {
            statement=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            resultset=statement.executeQuery(query1);
            while(resultset.next())
            {
                String accntCode=resultset.getString("glCode");
              //  String accntCode2=accntCode;
                String fund=resultset.getString("fundId");
                String amt=resultset.getString("amount");
                HashMap txnBalance=new HashMap();
                /** Storing fund and amount pairs of same GLCode into HashMap **/
                if(!txnBalancehasmap.containsKey(accntCode))//accntCode2.equalsIgnoreCase(accntCode))//Loops until GlCode changes
                {
                    for(int i=0;i<reqFundId.length;i++)
                    {
                        if(reqFundId[i].equalsIgnoreCase(fund))
                            txnBalance.put(fund,amt);//openingBalsubList.put(reqFundId1[i],opeBal);
                        else
                            txnBalance.put(reqFundId[i],new Double(0));
                    }
                    
                    txnBalancehasmap.put(accntCode,txnBalance);
                }
                else
                {
                    ((HashMap)txnBalancehasmap.get(accntCode)).put(fund,amt);
                }     
                if(!resultset.previous())
                    break;
                /** Storing GLCode and (fund-amount paired HashMap) pairs into HashMap **/
                txnBalancehasmap.put(accntCode,txnBalance);
            }
            resultset.close();
            statement.close();
            
        }
        catch(SQLException se)
        {
            LOGGER.error("Error in getschedulewiseOB");
            throw taskExc; 
        }
   }
   /**
    * 
    * @param fundid           :The id of the fund for which the opening balances has to be calculated
    * @param type1            :The type of account code (A,E,L,I)
    * @param type2            :The type of account code (A,E,L,I)
    * @param substringVal     :The no of digits to pick from the glcode for grouping. (2 for major code, 3 for minor code)
    * @param startDate        :The start date of the financial year for which opening balance has to be calculated
    * @param endDate          :The end date of the financial year for which opening balance has to be calculated
    * @param classification   :Classification of the code for which opening balance has to be calculated
    * @param txnCreditBalance :This parameter is called by reference so it  acts as input & output
    * @throws Exception
    */
   public void  getTxnCreditBalance(Connection conn,String fundId, String type1,String type2,String substringVal, String startDate, String endDate, int classification,HashMap txnCreditBalance ) throws Exception
   {
       String fundCondition="";
        if(!fundId.equalsIgnoreCase(""))
        {
            fundCondition="AND f.Id="+fundId+" ";
        }
        String glcode="",fuId="";
        String type="(coa.TYPE = '"+type1+"' OR coa.TYPE = '"+type2+"') and";
        if(type1==null || type1.trim().equals(""))
        	type="";
        String query="SELECT substr(coa.glcode,0,"+substringVal+") as \"glcode\",ts.fundid as \"fundid\" ,"
                +" sum(ts.openingcreditbalance) as \"amount\" "
                +" FROM transactionsummary ts,  chartofaccounts coa,fund  f   WHERE "+type+" coa.id = ts.glcodeid "
                +" AND financialyearid =(SELECT ID FROM financialyear WHERE startingdate <= '"+startDate+"' AND endingdate >= '"+endDate+"')  "+fundCondition+" and f.id=ts.fundid and f.isactive='1' and f.isnotleaf!='1' "
                +" GROUP BY substr(coa.glcode,0,"+substringVal+"), fundid ,coa.type";
        LOGGER.info("query "+query);
        try
        {
            statement = conn.createStatement();
            resultset = statement.executeQuery(query);
            Double opeBal = null;
            HashMap creditBalsubList = null;
            while(resultset.next())
            {
                glcode=resultset.getString("glcode");
                fuId=resultset.getString("fundid");
                opeBal=resultset.getDouble("amount");
                if(!txnCreditBalance.containsKey(glcode))
                {
                    creditBalsubList=new HashMap();
                    for(int i=0;i<reqFundId.length;i++)
                    {
                        if(reqFundId[i].equalsIgnoreCase(fuId))
                            creditBalsubList.put(reqFundId[i],opeBal);
                        else
                            creditBalsubList.put(reqFundId[i],new Double(0));
                    }
                    txnCreditBalance.put(glcode,creditBalsubList);
                }
                else
                {
                    ((HashMap)txnCreditBalance.get(glcode)).put(fuId,opeBal);
                }

            }
        }
        catch(Exception e)
        {
            LOGGER.error("Error in getCreditBalance");
            LOGGER.debug("Exp="+e.getMessage());
            throw new Exception();
        }
   }
   /**
    * 
    * @param fundId         :The id of the fund for which the opening balances has to be calculated
    * @param type1          :The type of account code (A,E,L,I)
    * @param type2          :The type of account code (A,E,L,I)
    * @param substringVal   :The no of digits to pick from the glcode for grouping. (2 for major code, 3 for minor code)
    * @param startDate      :The start date of the financial year for which opening balance has to be calculated
    * @param endDate        :The end date of the financial year for which opening balance has to be calculated
    * @param classification :classification of the code for which opening balance has to be calculated
    * @param txnDebitBalance:This parameter is called by reference so it  acts as input & output
    * @throws Exception
    */
   public void getTxnDebitBalance(Connection conn,String fundId, String type1,String type2,String substringVal, String startDate, String endDate, int classification,HashMap txnDebitBalance )throws Exception
   {
       String fundCondition="";
       if(!fundId.equalsIgnoreCase(""))
       {
           fundCondition="AND f.Id="+fundId+" ";
       }
       String glcode="",fuId="";
       String query="SELECT substr(coa.glcode,0,"+substringVal+") as \"glcode\",ts.fundid as \"fundid\" ,"
               +" sum(ts.openingdebitbalance) as \"amount\" "
               +" FROM transactionsummary ts,  chartofaccounts coa,fund  f   WHERE (coa.TYPE = '"+type1+"' OR coa.TYPE = '"+type2+"') and coa.id = ts.glcodeid "
               +" AND financialyearid =(SELECT ID FROM financialyear WHERE startingdate <= '"+startDate+"' AND endingdate >= '"+endDate+"')  "+fundCondition+" and f.id=ts.fundid and f.isactive='1' and f.isnotleaf!='1' "
               +" GROUP BY substr(coa.glcode,0,"+substringVal+"), fundid ,coa.type";
       LOGGER.info("query "+query);
       try
       {
           statement = conn.createStatement();
           resultset = statement.executeQuery(query);
           Double opeBal = null;
           HashMap debitBalsubList = null;
           while(resultset.next())
           {
               glcode=resultset.getString("glcode");
               fuId=resultset.getString("fundid");
               opeBal=resultset.getDouble("amount");
               if(!txnDebitBalance.containsKey(glcode))
               {
                   debitBalsubList=new HashMap();
                   for(int i=0;i<reqFundId.length;i++)
                   {
                       if(reqFundId[i].equalsIgnoreCase(fuId))
                           debitBalsubList.put(reqFundId[i],opeBal);
                       else
                           debitBalsubList.put(reqFundId[i],new Double(0));
                   }
                   txnDebitBalance.put(glcode,debitBalsubList);
               }
               else
               {
                   ((HashMap)txnDebitBalance.get(glcode)).put(fuId,opeBal);
               }

           }
       }
       catch(Exception e)
       {
           LOGGER.error("Error in getDebitBalance");
           throw taskExc;
       }
   }
  /** convert amount in rupeese to thousands or lakhs 
   * 
   * @param amt :amount to be converted
   * @param amt_In : to "thousands" or "lakhs"
   * @return
   */
  
   public String formatAmt(String amt,String amt_In)
   {   
       BigDecimal ammt= new BigDecimal(0.000);
       NumberFormat formatter;
       formatter = new DecimalFormat("##############0.00");
       int val=(amt_In.equalsIgnoreCase("thousand"))?1:amt_In.equalsIgnoreCase("lakhs")?2:3;
       switch(val)
           {
              case 1: 
               	     ammt=BigDecimal.valueOf(Double.parseDouble(amt)/1000);
               	     ammt=ammt.setScale(2,BigDecimal.ROUND_HALF_UP);
                 	 break;
                                 
               case 2 :  
               		ammt=BigDecimal.valueOf(Double.parseDouble(amt)/100000);
               		ammt=ammt.setScale(2,BigDecimal.ROUND_HALF_UP);
               		break;
               default: ammt=BigDecimal.valueOf(Double.valueOf(amt));
               BigDecimal tmpAmt= new BigDecimal(ammt.toBigInteger());
               tmpAmt=tmpAmt.add(BigDecimal.valueOf(0.5));
               if(ammt.doubleValue()>tmpAmt.doubleValue())
               			ammt=ammt.setScale(0,BigDecimal.ROUND_HALF_UP);
               			
               			
           }
        
       return formatter.format(Double.valueOf(ammt.toString()));
   }

   @Deprecated
   public String getStartDate(Connection con,int finYearId)throws TaskFailedException
   {
       String startDate="";
       String query="SELECT TO_CHAR(startingdate,'DD/MM/YYYY') FROM FINANCIALYEAR WHERE id="+finYearId;
       try
       {
           statement=con.createStatement();
           resultset=statement.executeQuery(query);
           if(resultset.next())
               startDate=resultset.getString(1);
       }
       catch(SQLException sql)
       {
    	   LOGGER.error("Exp in getStartDate :"+sql.getMessage());
    	   throw taskExc;    	   
       }
       return startDate;
   }
   
  
   public String getStartDate(final int finYearId)
   {
	   return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

		@Override
		public String execute(Connection con) throws SQLException {
			String startDate="";
		       String query="SELECT TO_CHAR(startingdate,'DD/MM/YYYY') FROM FINANCIALYEAR WHERE id="+finYearId;
		       try
		       {
		           statement=con.createStatement();
		           resultset=statement.executeQuery(query);
		           if(resultset.next())
		               startDate=resultset.getString(1);
		           HibernateUtil.release(statement, resultset);
		       }
		       catch(SQLException sql)
		       {
		    	   LOGGER.error("Exp in getStartDate :"+sql.getMessage());
		    	      	   
		       }
		       return startDate;
		}
	});
       
   }
   
   /** function to get financial year end Date
    * 
    * @param con
    * @param finYearId
    * @return
    */
   public String getEndDate(Connection con,int finYearId)throws TaskFailedException
   {
       String endDate="";
       String query="SELECT TO_CHAR(endingdate,'DD/MM/YYYY') FROM FINANCIALYEAR WHERE id="+finYearId;
       try
       {
           statement=con.createStatement();
           resultset=statement.executeQuery(query);
           if(resultset.next())
               endDate=resultset.getString(1);
       }
       catch(SQLException sql)
       {
    	   throw taskExc;    	   
       }
       return endDate;
   }
   /**function to format amount into to Indaian rupees format
    * 
    * @param strNumberToConvert
    * @return
    */
   public  StringBuffer numberToString(final String strNumberToConvert)
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
  
   /**function to get the financial year id 
    * @param sDate
    * @param connection
    * @return
    */
   @Deprecated
   public String getFYID(String sDate,Connection connection)throws TaskFailedException{
	String fyId="";
	try{
		//for accross the financial year
		statement=connection.createStatement();
		resultset = statement.executeQuery("SELECT id FROM financialYear " +
				"WHERE startingDate<='"+sDate+"' AND endingDate>='"+sDate+"'");
		if(resultset.next()) fyId = resultset.getString("id");
		resultset .close();
		statement.close();
	}catch(SQLException ex){
		fyId="";
		LOGGER.debug("Error GeneralLedger->getFYID(): " + ex.toString());
		throw taskExc;    	   
	}
	//LOGGER.debug("fyId: " + fyId);
	return fyId;
}
   
   public String getFYID(final String sDate){
	   
	   return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

		@Override
		public String execute(Connection connection) throws SQLException {
			String fyId="";
			try{
				//for accross the financial year
				statement=connection.createStatement();
				resultset = statement.executeQuery("SELECT id FROM financialYear " +
						"WHERE startingDate<='"+sDate+"' AND endingDate>='"+sDate+"'");
				if(resultset.next()) fyId = resultset.getString("id");
				resultset .close();
				statement.close();
			}catch(SQLException ex){
				fyId="";
				LOGGER.debug("Error GeneralLedger->getFYID(): " + ex.toString());
				    	   
			}
			//LOGGER.debug("fyId: " + fyId);
			return fyId;
		}
	});
	   
		
	}
   
 //used to format the report schedule
	/*
	 * tagName is the string[] containing <schedlue>lessGlcodes
	 */
	public String addToAddLessSubTotals(String glcode,BigDecimal amt,String[] tagName,int index,BigDecimal[] addSubtotals,BigDecimal[] lessSubtotals, String status)
	{
		String returnStatus="";
		
		for(int i=0;i<tagName.length;i++){
			if(tagName[i].equals(glcode)){
				if(lessSubtotals[index]==null){
					lessSubtotals[index]=BigDecimal.ZERO;
				}
				lessSubtotals[index]=lessSubtotals[index].add(amt);
			}
			else{
				if(addSubtotals[index]==null){
					addSubtotals[index]=BigDecimal.ZERO;
				}
				addSubtotals[index]=addSubtotals[index].add(amt);
			}
			//find when to insert addSubtotals & lessSubtotals
			if(tagName[0].equals(tagName[tagName.length-1]) && tagName[0].equals(glcode)){
				returnStatus="addBothSubtotals";
			}else if(tagName[0].equals(glcode) && !status.equals("added-addSubtotals")){
				returnStatus="addSubtotals";
			}else if(tagName[tagName.length-1].equals(glcode)){
				returnStatus="lessSubtotals";
			}
		}
		return returnStatus;
	}
	
}
