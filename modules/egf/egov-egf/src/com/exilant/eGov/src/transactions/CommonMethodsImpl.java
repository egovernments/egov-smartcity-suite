/*
 * MCDMethods.java  Created on Mar 28, 2006
 *
 *  Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions;

/**
 * This class defines common methods used for integration
 * @author Tilak
 *
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.ReturningWork;

import com.exilant.eGov.src.domain.Eg_Numbers;
import com.exilant.exility.common.TaskFailedException;

public class CommonMethodsImpl implements CommonMethodsI {
	Statement statement;
	Connection connection;
	ResultSet rset;
	private static  final Logger LOGGER = Logger.getLogger(CommonMethodsImpl.class);

	/**
	 * Get the cash in hand code account for the boundary
	 */
	public String getCashInHand(int BoundaryId,Connection connection)  throws Exception {
	  	String cashinHandCode="";
	  	try{
	  		statement=connection.createStatement();
			rset=statement.executeQuery(" SELECT a.glcode FROM CHARTOFACCOUNTS a,EG_BOUNDARY b,eg_boundary_type c "+
										" WHERE id=(SELECT cashinhand FROM CODEMAPPING WHERE EG_BOUNDARYID="+BoundaryId+" )  and b.ID_BNDRY_TYPE=c.ID_BNDRY_TYPE and b.ID_BNDRY="+BoundaryId);
			if(rset.next())	cashinHandCode=rset.getString(1);
			else throw new Exception();
			LOGGER.info(">>>cashinHandCode "+cashinHandCode);
	  	}
	  	catch(Exception e){
	  		LOGGER.error(" Glcode for cashinhand not found "+e.toString());
	  		throw new Exception(e.toString());
	  	}
	  	finally{
	  		try{
	  			rset.close();
	  			statement.close();
	  		}
	  		catch(Exception e)
	  		{
	  			LOGGER.error(e.toString());
	  		}
	  	}
	  	return cashinHandCode;
	  }
	/**
	 * Get the Cheque in hand code account for the boundary
	 */
	  public String getChequeInHand(int BoundaryId,Connection connection)  throws Exception {
	  	String chequeinHandCode="";
	  	try{
	   		statement=connection.createStatement();
			rset=statement.executeQuery(" SELECT a.glcode FROM CHARTOFACCOUNTS a,EG_BOUNDARY b,eg_boundary_type c "+
										" WHERE id=(SELECT chequeinhand FROM CODEMAPPING WHERE EG_BOUNDARYID="+BoundaryId+" )  and b.ID_BNDRY_TYPE=c.ID_BNDRY_TYPE and b.ID_BNDRY="+BoundaryId);
			if(rset.next()) chequeinHandCode=rset.getString(1);
			else throw new Exception("Chequeinhand Code not Found");
			LOGGER.info(">>>chequeinHandCode "+chequeinHandCode);
	  	}
	  	catch(Exception e){
	  		LOGGER.error(" Glcode for chequeinHandCode not found "+e.toString());
	  		LOGGER.debug("Exp="+e.getMessage());
	  		throw new Exception(e.toString());
	  	}
	  	finally{
	  		try{
	  			rset.close();
	  			statement.close();
	  		}
	  		catch(Exception e)
	  		{
	  			LOGGER.error(e.toString());
	  		}
	  	}

	  	return chequeinHandCode;
	  }

	  /**
	   * Get the property tax code for the year
	   */
	  public String getPTCode(String forYear,Connection connection) throws Exception
	  {
	  	String ptCodeAndName="";
	  	try{
	  		if(!forYear.equalsIgnoreCase(""))
			{
	  			LOGGER.info("  forYear  "+forYear);
	  			statement=connection.createStatement();
				rset=statement.executeQuery("select financialyear from financialyear  where '"+forYear+"' between startingdate and endingdate");
				String fId="",isOld="";
				if(rset.next())
				{
					LOGGER.info("   inside 1    ");
					fId=rset.getString(1);
				}
				else
				{
					rset.close();
					rset=statement.executeQuery("select a.glcode,a.name from chartofaccounts a,egf_tax_account_mapping b where b.glcodeid=a.id and upper(b.financialyear)=upper('old')");
					if(rset.next())
					{
						ptCodeAndName=rset.getString(1);
						ptCodeAndName=ptCodeAndName+"#"+rset.getString(2);
						LOGGER.info(">>>ptCodeAndName "+ptCodeAndName);
					}
				}
				if(!fId.equalsIgnoreCase(""))
				{
					rset=statement.executeQuery("select a.isold from egf_tax_account_mapping a,egf_tax_code b,financialyear c where a.taxcodeid=b.id and b.code='PT' and a.financialyear=c.financialyear and c.financialyear='"+fId+"'");
					if(rset.next())
					{
						LOGGER.info("   inside 2    ");
						isOld=rset.getString(1);
					}
					if(isOld != null && isOld.equals("1"))
					{
								LOGGER.info("   inside 4    ");
								rset=statement.executeQuery("select a.glcode,a.name from chartofaccounts a,egf_tax_account_mapping b where b.glcodeid=a.id and upper(b.financialyear)=upper('old')");
								if(rset.next())
								{
									ptCodeAndName=rset.getString(1);
									ptCodeAndName=ptCodeAndName+"#"+rset.getString(2);
									LOGGER.info(">>>ptCodeAndName** "+ptCodeAndName);
								}
					}
					else
					{
								LOGGER.info("   inside 5   ");
								rset=statement.executeQuery("select a.glcode,a.name from chartofaccounts a,egf_tax_account_mapping b,egf_tax_code c,financialyear d where b.taxcodeid=c.id and c.code='PT' and b.glcodeid=a.id and b.financialyear=d.financialyear and d.financialyear='"+fId+"'");
								if(rset.next())
								{
									ptCodeAndName=rset.getString(1);
									ptCodeAndName=ptCodeAndName+"#"+rset.getString(2);
									LOGGER.info(">>>ptCodeAndName "+ptCodeAndName);
								}
								else
									throw new Exception("Property Tax code not Found for "+forYear);

					}
				}
			}
			else
			{
				//if foryear is not given, then use Sespense code
				statement=connection.createStatement();
				rset=statement.executeQuery("select a.glcode, a.name from chartofaccounts a,egf_accountcode_purpose b where a.purposeid=b.id and upper(b.name)=upper('SuspenseCode')");
				if(rset.next()){
					ptCodeAndName=rset.getString(1);
					ptCodeAndName=ptCodeAndName+"#"+rset.getString(2);
					LOGGER.info(">>>ptCodeAndName1 "+ptCodeAndName);
				}
				else throw new Exception("Property Tax code not Found for "+forYear);
			}
	  	}
	  	catch(Exception e)
	  	{
	  		LOGGER.error(" PT code not found "+e.toString());
	  		throw new Exception(e.toString());
	  	}
	  	finally{
	  		try{
	  			rset.close();
	  			statement.close();
	  		}
	  		catch(Exception e)
	  		{
	  			LOGGER.error(e.toString());
	  		}
	  	}
	  	return ptCodeAndName;
	  }

	/**
	 * 	Get the account code for the bank account
	 */
	  @Deprecated
	  public String getBankCode(int bankAccountId,Connection connection) throws Exception
	  {
	  	String bankCodeAndName="";
	  	try{
	  		statement=connection.createStatement();
			rset=statement.executeQuery("select glcode,name from chartofaccounts where id=(select glcodeid from bankaccount where id="+bankAccountId+")");
			if(rset.next()){
				bankCodeAndName=rset.getString(1);
				bankCodeAndName=bankCodeAndName+"#"+rset.getString(2);
				LOGGER.info(">>>bankCodeAndName "+bankCodeAndName);
			}
			else throw new Exception("BAnk Code Not Found");
	  	}
	  	catch(Exception e){
	  		LOGGER.error(" Bank code not found "+e.toString());
	  		throw new Exception(e.toString());
	  	}
	  	finally{
	  		try{
	  			rset.close();
	  			statement.close();
	  		}
	  		catch(Exception e)
	  		{
	  			LOGGER.error(e.toString());
	  		}
	  	}
	  	return bankCodeAndName;
	  }
	  
	  public String getBankCode(final int bankAccountId) throws Exception
	  {
		  
		  return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection connection) throws SQLException {
				

			  	String bankCodeAndName="";
			  	try{
			  		statement=connection.createStatement();
					rset=statement.executeQuery("select glcode,name from chartofaccounts where id=(select glcodeid from bankaccount where id="+bankAccountId+")");
					if(rset.next()){
						bankCodeAndName=rset.getString(1);
						bankCodeAndName=bankCodeAndName+"#"+rset.getString(2);
						LOGGER.info(">>>bankCodeAndName "+bankCodeAndName);
					}
					else throw new Exception("BAnk Code Not Found");
			  	}
			  	catch(Exception e){
			  		LOGGER.error(" Bank code not found "+e.toString());
			  		
			  	}
			  	finally{
			  		try{
			  			rset.close();
			  			statement.close();
			  		}
			  		catch(Exception e)
			  		{
			  			LOGGER.error(e.toString());
			  		}
			  	}
			  	return bankCodeAndName;
			  
			}
		});
		  
	  }

	  /**
	   * Get the fiscal period id for the date passed
	   */
	  @Deprecated
	  public String getFiscalPeriod(String vDate,Connection connection) throws TaskFailedException,Exception
	  {
	  	String fiscalPeriodID="null";
		try
		{
			statement =connection.createStatement();
			rset=statement.executeQuery("select id from fiscalperiod  where '"+vDate+"' between startingdate and endingdate");
			if(rset.next()){
				fiscalPeriodID=rset.getString(1);
				LOGGER.info(">>>fiscalPeriodID "+fiscalPeriodID);
			}
			else 
				throw new TaskFailedException("fiscal Period Not Found");
		}
		catch(TaskFailedException e)
		{
			LOGGER.error("fiscal Period Not Found="+e.getMessage());
			throw new TaskFailedException("fiscal Period Not Found");
		}
		catch(Exception e)
		{
			LOGGER.error("failed to get fiscalperiodId "+e.toString());
			throw new Exception(e.toString());
		}
		finally{
	  		try{
	  			rset.close();
	  			statement.close();
	  		}
	  		catch(Exception e)
	  		{
	  			LOGGER.error(e.toString());
	  		}
	  	}
		return fiscalPeriodID;
	  }
	  
	  public String getFiscalPeriod(final String vDate) throws TaskFailedException,Exception
	  {
		  return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection connection) throws SQLException {
			

			  	String fiscalPeriodID="null";
				try
				{
					statement =connection.createStatement();
					rset=statement.executeQuery("select id from fiscalperiod  where '"+vDate+"' between startingdate and endingdate");
					if(rset.next()){
						fiscalPeriodID=rset.getString(1);
						LOGGER.info(">>>fiscalPeriodID "+fiscalPeriodID);
					}
					else 
						throw new TaskFailedException("fiscal Period Not Found");
				}
				catch(TaskFailedException e)
				{
					LOGGER.error("fiscal Period Not Found="+e.getMessage());
					
				}
				catch(Exception e)
				{
					LOGGER.error("failed to get fiscalperiodId "+e.toString());
					
				}
				finally{
			  		try{
			  			rset.close();
			  			statement.close();
			  		}
			  		catch(Exception e)
			  		{
			  			LOGGER.error(e.toString());
			  		}
			  	}
				return fiscalPeriodID;
			  
			}
		});
		  
	  }


	  /**
	   * Get the bank and bank branch of the bank acocunt
	   */
	  public String getBankId(int bankAccountId,Connection connection) throws Exception
	  {
	  	String bankAndBranchId="null";
		try{
			statement=connection.createStatement();
  	  	rset=statement.executeQuery("select b.id,c.id from bankaccount a,bankbranch b,bank c "+
  									" where a.branchid=b.id and b.bankid=c.id and a.id="+bankAccountId);
  	  		if(rset.next()){
  	  			bankAndBranchId=rset.getString(1);
  	  			bankAndBranchId=bankAndBranchId+"#"+rset.getString(2);
  	  			LOGGER.info(">>>bankAndBranchId "+bankAndBranchId);
  	  		}
  	  		else throw new Exception("Bank Code Not Found");
		}
		catch(Exception e){
			LOGGER.error(" Bank Id not found "+e.toString());
			throw new Exception(e.toString());
		}
		finally{
  		try{
  			rset.close();
  			statement.close();
  		}
  		catch(Exception e)
  		{
  			LOGGER.error(e.toString());
  		}
		}
  		return bankAndBranchId;
	}

	  /**
	   * Get the bank balance for the bank account
	   */
	  public double getAccountBalance(int bankAccountId,String vcDate,Connection connection) throws Exception
	  {
	  	double opeAvailable=0,totalAvailable=0;
	  	try{

		  	statement = connection.createStatement();

		  	String str="SELECT decode(sum(openingDebitBalance),null,0,sum(openingDebitBalance))- decode(sum(openingCreditBalance),null,0,sum(openingCreditBalance)) AS \"openingBalance\" "+
				"FROM transactionSummary WHERE financialYearId=( SELECT id FROM financialYear WHERE startingDate <='"+vcDate+"'" +
			   	"AND endingDate >='"+vcDate+"')  AND glCodeId =(select glcodeid from bankaccount where id="+bankAccountId+")";
		  	LOGGER.info(str);
		  	rset = statement.executeQuery(str);
		  	if(rset.next())
		  	 	opeAvailable = rset.getDouble("openingBalance");
			LOGGER.info("opening balance  "+opeAvailable);
		    rset.close();

	   		String str1="SELECT (decode(sum(gl.debitAmount),null,0,sum(gl.debitAmount)) - decode(sum(gl.creditAmount),null,0,sum(gl.creditAmount))) + " +opeAvailable+""+
				" as \"totalAmount\" FROM   generalLedger gl, voucherHeader vh WHERE vh.id = gl.voucherHeaderId AND gl.glCodeid = (select glcodeid from bankaccount where id="+bankAccountId+") AND  "+
				" vh.voucherDate >=( SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') FROM financialYear WHERE startingDate <= '"+vcDate+"' AND endingDate >= '"+vcDate+"') AND vh.voucherDate <= '"+vcDate+"'";
	   		LOGGER.info(str1);

	   		rset = statement.executeQuery(str1);
	 		if(rset.next()){
		   		 totalAvailable = rset.getDouble("totalAmount");
		   		LOGGER.info("total balance  "+totalAvailable);
	 		}

	  	}catch(Exception e){
	  		LOGGER.error(" could not get Bankbalance  "+e.toString());
	  		throw new Exception(e.toString());
		}
		finally{
  		try{
  			rset.close();
  			statement.close();
  		}
  		catch(Exception e)
  		{
  			LOGGER.error(e.toString());
  		}
		}
		return totalAvailable;
	}

	  /**
	   * Get the acccount code and name for the account with the purposeid
	   */
	public String getCodeName(String purposeId,Connection connection) throws Exception
	{
		String codeAndName="null";
		try{

	  	statement=connection.createStatement();
  	  	rset=statement.executeQuery("select a.glcode, a.name from chartofaccounts a,egf_accountcode_purpose b where a.purposeid=b.id and b.id="+purposeId);
  	  //	for(int i=0;rset.next();i++){
  	  		while(rset.next()){
  	  			codeAndName=rset.getString(1);
  	  			codeAndName=codeAndName+"#"+rset.getString(2);
  	  		}

		}
		catch(Exception e){
			LOGGER.error(" code not found for purpose id "+e.toString());
			throw new Exception(e.toString());
		}
		finally{
  		try{
  			rset.close();
  			statement.close();
  		}
  		catch(Exception e)
  		{
  			LOGGER.error(e.toString());

  		}
		}
  		return codeAndName;
	}

	/**
	 * Get the name of the	accountcode passed
	 */
	public String getNameFromCode(String glcode,Connection connection) throws Exception
	{

		String codeName="null";
		try{
				String query="select name from chartofaccounts where glcode='"+glcode+"'";
				LOGGER.info("  query   "+query);
			  	statement=connection.createStatement();
		  	  	rset=statement.executeQuery("select name from chartofaccounts where glcode='"+glcode+"'");
		  	  	if(rset.next())
		  	  	{
		  	  		codeName=rset.getString(1);
		  	  		LOGGER.info("  codeName   "+codeName);
		  	  	}
		  	  	else
		  	  		throw new Exception("code not found");
		}
				catch(Exception e){
					LOGGER.error(" code not found "+e.toString());
					throw new Exception(e.toString());
				}
				finally{
		  		try{
		  			rset.close();
		  			statement.close();
		  		}
		  		catch(Exception e)
		  		{
		  			LOGGER.error(e.toString());

		  		}
				}
  		return codeName;
	}

	/**
	 * Get the accountcode of the is paased.
	 */
	public String getGlCode(final String glCodeId) throws Exception
	{
		
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {

			@Override
			public String execute(Connection connection) throws SQLException {
				

				String glCode="null";
				try{
						String query="select glcode from chartofaccounts where id="+glCodeId+"";
						LOGGER.info("  query   "+query);
					  	statement=connection.createStatement();
				  	  	rset=statement.executeQuery(query);
				  	  	if(rset.next())
				  	  	{
				  	  		glCode=rset.getString(1);
				  	  		LOGGER.info("  glCode   "+glCode);
				  	  	}
				  	  	else
				  	  		throw new Exception("id not found");
				}
						catch(Exception e){
							LOGGER.error(" id not found "+e.toString());
							
						}
						finally{
				  		try{
				  			rset.close();
				  			statement.close();
				  		}
				  		catch(Exception e)
				  		{
				  			LOGGER.error(e.toString());

				  		}
						}
		  		return glCode;
			
				
			}
		});
	}

	@Deprecated
	public String getGlCode(String glCodeId,Connection connection) throws Exception
	{
		String glCode="null";
		try{
				String query="select glcode from chartofaccounts where id="+glCodeId+"";
				LOGGER.info("  query   "+query);
			  	statement=connection.createStatement();
		  	  	rset=statement.executeQuery(query);
		  	  	if(rset.next())
		  	  	{
		  	  		glCode=rset.getString(1);
		  	  		LOGGER.info("  glCode   "+glCode);
		  	  	}
		  	  	else
		  	  		throw new Exception("id not found");
		}
				catch(Exception e){
					LOGGER.error(" id not found "+e.toString());
					throw new Exception(e.toString());
				}
				finally{
		  		try{
		  			rset.close();
		  			statement.close();
		  		}
		  		catch(Exception e)
		  		{
		  			LOGGER.error(e.toString());

		  		}
				}
  		return glCode;
	}

	/**
	 * This is to check if a record already exist in integrationlog
	 */
	public String checkRecordIdInLog(String recordId,int userId,Connection connection) throws Exception
	{
		String cgn=null;
		try
		{
				String query="SELECT VOUCHERNUMBER FROM integrationlog WHERE RECORDID='"+recordId+"' and USERID="+userId+" order by id desc";
				LOGGER.info("  query   "+query);
			  	statement=connection.createStatement();
		  	  	rset=statement.executeQuery(query);
		  	  	if(rset.next())
		  	  	{
		  	  		cgn=rset.getString(1);
		  	  		LOGGER.info("  cgn in log  "+cgn);
		  	  	}
		}
  	  	catch(Exception e)
  	  	{
			LOGGER.debug("Exp="+e.getMessage());
			throw new Exception(e.toString());
		}
		return cgn;
	}

	/**
	 * Get the division code of the id passed
	 */
	public String getDivisionCode(Integer divid,Connection connection) throws Exception
	{
		 String divCode=null;
		 String sql="SELECT distinct BNDRY_NUM FROM EG_BOUNDARY where ID_BNDRY='"+divid+"'";
		 LOGGER.debug("Divisio code query-->>>>>>>> "+ sql);
		try{

			statement=connection.createStatement();
			rset=statement.executeQuery(sql);
			while(rset.next())
			{
				divCode=rset.getString(1);
				LOGGER.debug("divCode >>>>>>>"+divCode);
			}

		}
		catch(Exception e)
		{
			LOGGER.debug("Exp="+e.getMessage());
			throw new Exception(e.toString());
		}
		return divCode;
	}

	/**
	 * Get the division id for the sub field passed
	 */
	public Integer getDivisionId(Integer fieldId,Connection connection) throws Exception
	{
		Integer divId=null;
		 //String sql="SELECT distinct BNDRY_NUM FROM EG_BOUNDARY where ID_BNDRY='"+divid+"'";
		 String sql="Select PARENT from EG_BOUNDARY where ID_BNDRY='"+fieldId+"'";
		 LOGGER.debug("Division id query-->>>>>>>> "+ sql);
		try{

			statement=connection.createStatement();
			rset=statement.executeQuery(sql);
			if(rset.next())
				divId=rset.getInt(1);
			LOGGER.debug("Division id is >>>>>>>"+divId);
		}
		catch(Exception e)
		{
			LOGGER.debug("Exp="+e.getMessage());
			throw new Exception(e.toString());
		}
		return divId;
	}

	/**
	 * Get the financial year for the data passed
	 */
	public String getFinacialYear(String vDate,Connection connection) throws Exception
	{
	  String finYear="null";
	  String sql="select FINANCIALYEAR from FINANCIALYEAR  where '"+vDate+"' between startingdate and endingdate";
	  try
	  {
	  	statement=connection.createStatement();
		rset=statement.executeQuery(sql);
		while(rset.next())
		{
	       finYear=rset.getString(1);
	       LOGGER.debug("finYear id>>>>>>>"+finYear);
	    }

	  }
	  catch(Exception e)
	  {
	  	LOGGER.debug("Exp="+e.getMessage());
		throw new Exception(e.toString());
	  }
	  return finYear;
	}
	/**
	 * This method gets the GlCodeId by passing GLCODE as parameter
	 * added by Sapna
	 */
	public String getGlCodeId(String glCode,Connection connection) throws Exception
	{
		String glCodeId="null";
		try{
				String query="select id from chartofaccounts where glCode like '"+glCode+"'";
				LOGGER.info("  query   "+query);
			  	statement=connection.createStatement();
		  	  	rset=statement.executeQuery(query);
		  	  	if(rset.next())
		  	  	{
		  	  		glCodeId=rset.getString(1);
		  	  		LOGGER.debug("  glCodeId   "+glCodeId);
		  	  	}
		  	  	else
		  	  		throw new Exception("id not found");
		}
				catch(Exception e){
					LOGGER.error(" id not found "+e.toString());
					throw new Exception(e.toString());
				}
				finally{
		  		try{
		  			rset.close();
		  			statement.close();
		  		}
		  		catch(Exception e)
		  		{
		  			LOGGER.error(e.toString());

		  		}
				}
  		return glCodeId;
	}

	/**
	 * This API will return the transaction no for any type of txn.
	 * Input :Type,transaction date and connection
	 * Output :Transaction number in the format txnType+number+/+month+/+year
	 */
	public String getTxnNumber(String txnType,String vDate,Connection con) throws Exception
	{
	  String finYear="";
	  String fiscalPeriod="";
	  String runningNumber="";
	  String retVal="";
	  Eg_Numbers en=new Eg_Numbers();

	  String month[] = vDate.split("/");
		LOGGER.info(" The month :"+month[1]);
		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	//	Date dt=new Date();
		//dt = sdf.parse( vDate );
		String txndate = formatter.format(sdf.parse( vDate ));

	  String sql="select a.FINANCIALYEAR,b.id from FINANCIALYEAR a,fiscalperiod b  where a.id=b.financialyearid AND '"+txndate+"' between b.startingdate and b.endingdate";
	  LOGGER.info(sql);
	  try
	  {
	  	statement=con.createStatement();
		rset=statement.executeQuery(sql);
		if(rset.next())
		{
	       finYear=rset.getString(1);
	       fiscalPeriod=rset.getString(2);
	       LOGGER.info("finYear id>>>>>>>"+finYear +" fiscalPeriod :"+fiscalPeriod);
	       rset.close();
	    }
		else
			throw new Exception("Year is not defined in the system");



		String year = finYear.substring(2,4)+finYear.substring((finYear.length()-2),(finYear.length()));
		LOGGER.info(" The year String :"+year);

		rset = statement.executeQuery("select id as \"key\", vouchernumber  as \"vouchernumber\" from " +
						"eg_numbers where fiscialperiodid="+fiscalPeriod+" and vouchertype='"+txnType+"'");

		if(rset.next())
		{
			runningNumber = rset.getString("vouchernumber");
			String idno=rset.getString("key");
			en.setId(idno);
			LOGGER.info("runningNumber  "+runningNumber);
			runningNumber=String.valueOf(Integer.parseInt(runningNumber)+1);
			en.setVoucherType(txnType);
			en.setVoucherNumber(runningNumber);
			en.update(con);
		}
		else
		{
			runningNumber="1";
			runningNumber=String.valueOf(Integer.parseInt(runningNumber));
			en.setVoucherNumber(runningNumber);
			en.setVoucherType(txnType);
			en.setFiscialPeriodId(fiscalPeriod);
			en.insert(con);
		}


		retVal=txnType+runningNumber+"/"+month[1]+"/"+year;
		LOGGER.info("Return value is :"+retVal);
	  }
	  catch(Exception e)
	  {
	  	LOGGER.debug("Exp="+e.getMessage());
		throw new Exception(e.toString());
	  }
	  finally
	  {
	  	rset.close();
	  	statement.close();
	  }
	  return retVal;
	}


	/**
		 * added by Iliyaraja
		 * This API will return the generated Voucher number.
		 * Input :Fund Id,txnType,transaction date and connection
		 * Output :Transaction number in the format fundType+txnType+/+number+/+month+/+year
	*/
		public String getTxnNumber(String fundId,String txnType,String vDate,Connection con) throws Exception
		{
			if(txnType==null || txnType.equals(""))
				throw new Exception("Configuration setting for voucher numbering is not done");

		  String fType="";
		  String finYear="";
		  String fiscalPeriod="";
		  String runningNumber="";
		  String retVal="";
		  Eg_Numbers en=new Eg_Numbers();

		  String month[] = vDate.split("/");
			LOGGER.info(" The month :"+month[1]);
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			String txndate = formatter.format(sdf.parse( vDate ));

		  String sql="select a.FINANCIALYEAR,b.id from FINANCIALYEAR a,fiscalperiod b  where a.id=b.financialyearid AND '"+txndate+"' between b.startingdate and b.endingdate";
		  LOGGER.info(sql);
		  try
		  {
		  	statement=con.createStatement();

			// This is for getting fund type based on the fund id.
			rset = statement.executeQuery("SELECT identifier as \"fund_identi\" from fund where id="+fundId);
			if(rset.next()) {
			 		fType = rset.getString("fund_identi");

			 		LOGGER.info("Fund Id--->"+fundId +" Fund Type---->"+fType);
			rset.close();
			}
			else
				throw new Exception("Fund is not defined in the system");


			rset=statement.executeQuery(sql);
			if(rset.next())
			{
		       finYear=rset.getString(1);
		       fiscalPeriod=rset.getString(2);
		       LOGGER.info("finYear id>>>>>>>"+finYear +" fiscalPeriod :"+fiscalPeriod);
		       rset.close();
		    }
			else
				throw new Exception("Year is not defined in the system");
			
			String year = finYear.substring(2,4)+finYear.substring((finYear.length()-2),(finYear.length()));
			LOGGER.info(" The year String :"+year);
			txnType=fType.concat(txnType);
				
			
			rset = statement.executeQuery("select id as \"key\", vouchernumber  as \"vouchernumber\" from " +
							"eg_numbers where fiscialperiodid="+fiscalPeriod+" and vouchertype='"+txnType+"'");
			String runNum = null;
			
			if(rset.next())
			{
				
				runningNumber = rset.getString("vouchernumber");
				runNum = runningNumber;
				String idno=rset.getString("key");
				en.setId(idno);
				LOGGER.info("runningNumber  "+runningNumber);
				runningNumber=String.valueOf(Integer.parseInt(runningNumber)+1);
				en.setVoucherType(txnType);
				en.setVoucherNumber(runningNumber);
				en.update(con);
			}
			else
			{
				runningNumber="1";
				runningNumber=String.valueOf(Integer.parseInt(runningNumber));
				runNum = runningNumber;
				en.setVoucherNumber(runningNumber);
				en.setVoucherType(txnType);
				en.setFiscialPeriodId(fiscalPeriod);
				en.insert(con);
			}
			retVal=txnType+"/"+runNum+"/"+month[1]+"/"+year;

			
			LOGGER.info("Return value is :"+retVal);
		  }
		  catch(Exception e)
		  {
		  	LOGGER.debug("Exp="+e.getMessage());
			throw new Exception(e.toString());
		  }
		  finally
		  {
		  	rset.close();
		  	statement.close();
		  }
		  return retVal;
	}
		/*
		 * This API will return the running number.
		 * Parameter  : Fund Id,txnType,transaction date and connection
		 * return     : String transaction number.
		 */
		public String getTransRunningNumber(String fundId,String txnType,String vDate,Connection con) throws Exception {
			
			  if(txnType==null || txnType.equals(""))
				throw new Exception("Configuration setting for voucher numbering is not done");

			  String fType="";
			  String finYear="";
			  String fiscalPeriod="";
			  String runningNumber="1";
			  String retVal="";
			  Eg_Numbers en=new Eg_Numbers();

			  SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			  SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			  String txndate = formatter.format(sdf.parse( vDate ));
	
			  String sql="select a.FINANCIALYEAR,b.id from FINANCIALYEAR a,fiscalperiod b  where a.id=b.financialyearid AND '"+txndate+"' between b.startingdate and b.endingdate";
			  LOGGER.debug("sql in getTransRunningNumber() :"+sql);
			  try
			  {
			  	statement=con.createStatement();
	
				// This is for getting fund type based on the fund id.
				rset = statement.executeQuery("SELECT identifier as \"fund_identi\" from fund where id="+fundId);
				if(rset.next()) {
					fType = rset.getString("fund_identi");
					LOGGER.info("Fund Id  :--->"+fundId +" Fund Type  :---->"+fType);
					rset.close();
				}
				else
					throw new Exception("Fund is not defined in the system");

				rset=statement.executeQuery(sql);
				if(rset.next())
				{
			       finYear=rset.getString(1);
			       fiscalPeriod=rset.getString(2);
			       LOGGER.debug("finYear id>>>>>>>"+finYear +" fiscalPeriod :"+fiscalPeriod);
			       rset.close();
			    }
				else
					throw new Exception("Year is not defined in the system");
				
				txnType = fType.concat(txnType);
				PreparedStatement stmt = con.prepareStatement("SELECT id as \"key\", vouchernumber  as \"vouchernumber\" from " +
						"eg_numbers where fiscialperiodid=? and vouchertype=? ");
					       
				stmt.setInt(1, Integer.parseInt(fiscalPeriod));
				stmt.setString(2, txnType);
				rset = stmt.executeQuery();
		
				/*rset = stmt.executeQuery("select id as \"key\", vouchernumber  as \"vouchernumber\" from " +
								"eg_numbers where fiscialperiodid="+fiscalPeriod+" and vouchertype='"+txnType+"'"+" for update of vouchernumber");*/
			//	String runNum = null;
				
				if(rset.next())
				{
					runningNumber = rset.getString("vouchernumber");
					String idno=rset.getString("key");
					en.setId(idno);
					LOGGER.debug("runningNumber  "+runningNumber);
					runningNumber=String.valueOf(Integer.parseInt(runningNumber)+1);
					//runNum = runningNumber;
					en.setVoucherType(txnType);
					en.setVoucherNumber(runningNumber);
					en.update(con);
				}
				else
				{
					//runningNumber="1";
					runningNumber=String.valueOf(Integer.parseInt(runningNumber));
				//	runNum = runningNumber;
					en.setVoucherNumber(runningNumber);
					en.setVoucherType(txnType);
					en.setFiscialPeriodId(fiscalPeriod);
					en.insert(con);
				}
				retVal = runningNumber;
				LOGGER.info("Return value is in getTransRunningNumber() :"+retVal);
			  }
			  catch(Exception e) {
			  	LOGGER.debug("Exp occured in getTransRunningNumber() :"+e.getMessage());
				throw new Exception(e.toString());
			  }
			  finally
			  {
			  	rset.close();
			  	statement.close();
			  }
			  return retVal;
		}
		
		/**
		 * This API will return the sub feild id when the boundary num is passed.
		 * We are ordering by id_bndry_type desc so that sub field value will come first.
		 */
		public Integer getDivisionIdFromCode(String divisionCode,Connection connection) throws Exception
		{
			Integer divId=null;
	   	    String sql="Select id_bndry from EG_BOUNDARY where BNDRY_NUM='"+divisionCode+"' and is_bndry_active=1 order by id_bndry_type desc";
			LOGGER.info("Sub Field id query-->>>>>>>> "+ sql);
			statement=connection.createStatement();
			rset=statement.executeQuery(sql);
			if(rset.next())
				divId=rset.getInt(1);
			LOGGER.info("Sub Feild id is >>>>>>>"+divId);
			return divId;
		}

}