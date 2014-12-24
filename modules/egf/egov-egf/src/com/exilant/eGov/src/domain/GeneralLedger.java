/*
 * Created on Feb 14, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.domain;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.ReturningWork;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;


public class GeneralLedger
{
	private String id = null;
	private String voucherLineId = null;
	private String effectiveDate = "1-Jan-1900";
	private String glCodeId = null;
	private String glCode = null;
	private String debitAmount = "0";
	private String creditAmount = "0";
	private String[] accountDetail=null ;
	private String description = null;
	private String voucherHeaderId =null;
	private String created = "1-Jan-1900";
	private String functionId=null;
	private String  updateQuery="UPDATE generalledger SET";
	private boolean isId=false, isField=false;
	private static final Logger LOGGER=Logger.getLogger(GeneralLedger.class);
	private static TaskFailedException taskExc;
	
	public void setId(String aId){ id = aId; isId=true;}
	public void setvoucherLineId(String avoucherLineId){ voucherLineId = avoucherLineId;updateQuery = updateQuery + " voucherlineid='" + voucherLineId + "',"; isField = true; }
	public void seteffectiveDate(String aeffectiveDate){ effectiveDate = aeffectiveDate; updateQuery = updateQuery + " effectiveDate='" + effectiveDate + "',"; isField = true;}
	public void setglCodeId(String aglCodeId){ glCodeId = aglCodeId; updateQuery = updateQuery + " glCodeId='" + glCodeId + "',"; isField = true;}
	public void setglCode(String aglCode){ glCode = aglCode; updateQuery = updateQuery + " glCode='" + glCode + "',"; isField = true;}
	public void setdebitAmount(String adebitAmount){ debitAmount = adebitAmount; updateQuery = updateQuery + " debitAmount='" + debitAmount + "',"; isField = true;}
	public void setcreditAmount(String acreditAmount){ creditAmount = acreditAmount;updateQuery = updateQuery + " creditAmount='" + creditAmount + "',"; isField = true; }
	public void setaccountDetail(String aaccountDetail,int index){ accountDetail[index-1] = aaccountDetail; }
	public void setdescription(String adescription){ description = adescription; updateQuery = updateQuery + " description='" + description + "',"; isField = true;}
	public void setvoucherHeaderId(String avoucherHeaderId){ voucherHeaderId = avoucherHeaderId; updateQuery = updateQuery + " voucherHeaderId='" + voucherHeaderId + "',"; isField = true;}
	public void setfunctionId(String afunctionId){ functionId = afunctionId; updateQuery = updateQuery + " functionId='" + functionId + "',"; isField = true;}
	public void setAccountDetailSize(int length){
		if(accountDetail!=null)return;
		accountDetail=new String[length];
		for(int i=0;i<length;i++){
			accountDetail[i]="0";
		}
	}

	public int getId() {return Integer.valueOf(id).intValue(); }
	public String getvoucherLineId(){return voucherLineId;}
	public String geteffectiveDate(){return effectiveDate;}
	public String getglCodeId(){return glCodeId;}
	public String getglCode(){return glCode;}
	public String getdebitAmount(){return debitAmount;}
	public String getcreditAmount(){return creditAmount;}
	public String getdescription(){return description;}
	public String getvoucherHeaderId(){return voucherHeaderId;}
	public String getfunctionId(){return functionId;}

	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		EGovernCommon commommethods = new EGovernCommon();
		Statement statement = null;
		try{
			effectiveDate = String.valueOf(commommethods.getCurrentDate(connection));
			Date dt=new Date();
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			dt = sdf.parse( effectiveDate );
			effectiveDate = formatter.format(dt);
		
			description = commommethods.formatString(description);
			setId( String.valueOf(PrimaryKeyGenerator.getNextKey("GeneralLedger")) );
	
			if( functionId==null ||functionId.equals("")) functionId=null;
			String insertQuery;
			 insertQuery = "INSERT INTO generalledger (id, voucherLineID, effectiveDate, glCodeID, " +
									"glCode, debitAmount, creditAmount,";
			 insertQuery+="description,voucherHeaderId,functionId) VALUES (" + id + ", " + voucherLineId + ", '" + effectiveDate + "', "
									+ glCodeId + ", '" + glCode + "', " + debitAmount + ", " + creditAmount
									+ ", " ;
			 insertQuery+="'" + description + "', "+ voucherHeaderId +", " + functionId +")";
	
	
			LOGGER.info(insertQuery);
		      statement = connection.createStatement();
	
		      statement.executeUpdate(insertQuery);
		}
		catch(Exception e){throw taskExc;}
		finally{
	      statement.close();
		}

	}
	/**
	 * Fucntion for update generalledger
	 * @param connection
	 * @throws SQLException
	 */
	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{
			EGovernCommon commommethods = new EGovernCommon();
			created = commommethods.getCurrentDate(connection);
			try
	   		{
	   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				created = formatter.format(sdf.parse( created ));
	   		}catch(Exception e){throw taskExc;}

			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;
			Statement statement = connection.createStatement();
			LOGGER.info(updateQuery);
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery="UPDATE generalledger SET";
		}

	}


	/**
	 * Function to get all the recoveries not in fund
	 * @param ACCOUNTDETAILTYPE
	 * @param ACCOUNTDETAILKEY
	 * @param FUND
	 * @param date
	 * @param status
	 * @return HashMap with account code as the key and the total pending recovery amount for that account code.
	 * @throws SQLException
	 * @throws TaskFailedException
	 */
	public HashMap getRecoveryForSubLedgerNotInFund(final Integer ACCOUNTDETAILTYPE,final Integer ACCOUNTDETAILKEY, 
			final Integer FUND, final Date date,final int status)throws SQLException, TaskFailedException
	{
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<HashMap>() {

			@Override
			public HashMap execute(Connection connection) throws SQLException {
				HashMap <String,BigDecimal> hmA = new HashMap <String,BigDecimal> ();   
				HashMap <String,BigDecimal> hmB = new HashMap <String,BigDecimal> ();
				HashMap <String,BigDecimal> hmFinal = new HashMap <String,BigDecimal> ();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				String vDate = formatter.format(date);
				
				Statement statement =null;
				ResultSet rs= null;
				try{
					
					statement = connection.createStatement();

						// Query1 - to get the sum of credit amount glcode wise
						String selQuery="SELECT GL.GLCODE as ACCOUNTCODE,SUM(GLD.AMOUNT) AS CREDITAMOUNT FROM VOUCHERHEADER VH,GENERALLEDGER GL,GENERALLEDGERDETAIL GLD " +
						" WHERE VH.FUNDID NOT IN ("+FUND+") AND GLD.DETAILTYPEID="+ACCOUNTDETAILTYPE+" AND DETAILKEYID="+ACCOUNTDETAILKEY+" AND VH.STATUS="+status+" AND GL.CREDITAMOUNT>0 " +
						" AND VH.ID=GL.VOUCHERHEADERID AND GL.ID=GLD.GENERALLEDGERID AND VH.VOUCHERDATE<='"+vDate+"' GROUP BY GL.GLCODE";
						LOGGER.debug("query (CreditAmount)--> "+selQuery);
						
						rs=statement.executeQuery(selQuery);
						while(rs.next())
							hmA.put(rs.getString("ACCOUNTCODE"),rs.getBigDecimal("CREDITAMOUNT"));
						LOGGER.debug("map size -------> "+hmA.size());
						
						// Query2 - to get the sum of debit amount glcode wise
						selQuery="SELECT GL.GLCODE AS GLCODE ,SUM(GLD.AMOUNT) AS DEBITAMOUNT FROM VOUCHERHEADER VH,GENERALLEDGER GL,GENERALLEDGERDETAIL GLD  " +
								" WHERE VH.FUNDID NOT IN ("+FUND+")	AND GLD.DETAILTYPEID="+ACCOUNTDETAILTYPE+" AND DETAILKEYID="+ACCOUNTDETAILKEY+" AND VH.STATUS="+status+" AND GL.DEBITAMOUNT>0 AND  " +
								" VH.ID=GL.VOUCHERHEADERID AND GL.ID=GLD.GENERALLEDGERID AND VH.VOUCHERDATE<='"+vDate+"' GROUP BY GL.GLCODE";
						LOGGER.debug("query (DebitAmount)--> "+selQuery);  
						
						rs=statement.executeQuery(selQuery);
						while(rs.next())
							hmB.put(rs.getString("GLCODE"),rs.getBigDecimal("DEBITAMOUNT"));
						LOGGER.debug("map size -------> "+hmB.size()); 
						
						if(hmA.size()==0) 
							return hmB;
						else if(hmB.size()==0)
						{
							Set<Map.Entry <String,BigDecimal>> setA = hmA.entrySet();
							for (Map.Entry <String,BigDecimal>   meA : setA){
								hmFinal.put(meA.getKey(), meA.getValue().multiply(new BigDecimal(-1)));  
							}
							return hmFinal;  
						}
						
						//Calculating the recovery amount as:
						//	Recoveryamount=DEBITAMOUNT(query 2)- CREDITAMOUNT(query 1)

						hmFinal=hmB;
						Set<Map.Entry <String,BigDecimal>> setA = hmA.entrySet();
						for (Map.Entry <String,BigDecimal>   meA : setA){
							if(hmFinal.containsKey(meA.getKey())){
								BigDecimal iC =  hmFinal.get(meA.getKey()).subtract(meA.getValue());
								hmFinal.put(meA.getKey(), iC); 
							}
							else{
								hmFinal.put(meA.getKey(), meA.getValue().multiply(new BigDecimal(-1)));  
							}
						}
						LOGGER.debug("hmCopy------>"+hmFinal);	
					}
				catch(Exception e)
				{
					  LOGGER.error("Exception in getRecoveryForSubLedgerNotInFund():"+e);
		                 
				}finally{
					rs.close();
					statement.close();
				}
				return hmFinal;
			}
		});
		
		
	} 


	/**
	 * Function to get all the recoveries for a particular fund
	 * @param ACCOUNTDETAILTYPE
	 * @param ACCOUNTDETAILKEY
	 * @param FUND
	 * @param date
	 * @param status
	 * @return HashMap with account code as the key and the total pending recovery amount for that account code.
	 * @throws SQLException
	 * @throws TaskFailedException
	 */
	public HashMap getRecoveryForSubLedger(final Integer ACCOUNTDETAILTYPE, final Integer ACCOUNTDETAILKEY,final Integer FUND, 
			final Date date,final int status)throws SQLException, TaskFailedException
	{
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<HashMap>() {

			@Override
			public HashMap execute(Connection connection) throws  SQLException {
				HashMap <String,BigDecimal> hmA = new HashMap <String,BigDecimal> (); 
				HashMap <String,BigDecimal> hmB = new HashMap <String,BigDecimal> ();
				HashMap <String,BigDecimal> hmFinal = new HashMap <String,BigDecimal> ();
				
				Statement statement = null;
				ResultSet rs= null;
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");    
				String vDate = formatter.format(date);
				try{
					
					statement = connection.createStatement();  
					
						// Query1 - to get the sum of credit amount glcode wise
						String selQuery=" SELECT GL.GLCODE as ACCOUNTCODE, SUM(GLD.AMOUNT) as CREDITAMOUNT FROM VOUCHERHEADER VH,GENERALLEDGER GL,GENERALLEDGERDETAIL GLD " +
								" WHERE VH.FUNDID="+FUND+"	AND GLD.DETAILTYPEID="+ACCOUNTDETAILTYPE+" AND DETAILKEYID="+ACCOUNTDETAILKEY+" AND VH.STATUS="+status+" AND GL.CREDITAMOUNT>0 " +
								" AND VH.ID=GL.VOUCHERHEADERID AND GL.ID=GLD.GENERALLEDGERID AND VH.VOUCHERDATE<='"+vDate+"' GROUP BY GL.GLCODE" ;
						LOGGER.debug("query (CreditAmount)--> "+selQuery);
						
						rs=statement.executeQuery(selQuery);
						while(rs.next())
							hmA.put(rs.getString("ACCOUNTCODE"),rs.getBigDecimal("CREDITAMOUNT"));
						LOGGER.debug("map size -------> "+hmA.size());
						
						// Query2 - to get the sum of debit amount glcode wise
						selQuery="SELECT GL.GLCODE as GLCODE, SUM(GLD.AMOUNT) as DEBITAMOUNT FROM VOUCHERHEADER VH,GENERALLEDGER GL,GENERALLEDGERDETAIL GLD  " +
								"WHERE VH.FUNDID="+FUND+" AND GLD.DETAILTYPEID="+ACCOUNTDETAILTYPE+" AND DETAILKEYID="+ACCOUNTDETAILKEY+" AND VH.STATUS="+status+" AND GL.DEBITAMOUNT>0 AND " +
								"VH.ID=GL.VOUCHERHEADERID AND GL.ID=GLD.GENERALLEDGERID AND VH.VOUCHERDATE<='"+vDate+"' GROUP BY GL.GLCODE";
						LOGGER.debug("query (DebitAmount)--> "+selQuery);  
						
						rs=statement.executeQuery(selQuery); 
						while(rs.next())
							hmB.put(rs.getString("GLCODE"),rs.getBigDecimal("DEBITAMOUNT"));
						LOGGER.debug("map size -------> "+hmB.size());   
						
						if(hmA.size()==0) 
							return hmB;
						else if(hmB.size()==0) 
						{
							Set<Map.Entry <String,BigDecimal>> setA = hmA.entrySet();
							for (Map.Entry <String,BigDecimal>   meA : setA){
								hmFinal.put(meA.getKey(), meA.getValue().multiply(new BigDecimal(-1)));  
							}
							return hmFinal;     
						}
						
						hmFinal=hmB;
						Set<Map.Entry <String,BigDecimal>> setA = hmA.entrySet();
						for (Map.Entry <String,BigDecimal>   meA : setA){
							if(hmFinal.containsKey(meA.getKey())){   
								BigDecimal iC = hmFinal.get(meA.getKey()).subtract(meA.getValue());
								hmFinal.put(meA.getKey(), iC);    
							}
							else{
								hmFinal.put(meA.getKey(),meA.getValue().multiply(new BigDecimal(-1))); 
							}
						}
						LOGGER.debug("hmCopy------>"+hmFinal);  	 
					}
				catch(Exception e)
				{
					  LOGGER.error("Exception in getRecoveryForSubLedger():"+e);
		             
				}finally{
					rs.close();
					statement.close();
				}
				return hmFinal;
			}
		});
		
		
   }
}

