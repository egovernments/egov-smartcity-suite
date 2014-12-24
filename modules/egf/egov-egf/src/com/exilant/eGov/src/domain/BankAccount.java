/*
 * Created on Jan 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.apache.log4j.Logger;

/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class BankAccount
{
	private String id = null;
	private String branchId =null;
	private String accountNumber =null;
	private String accountType = null;
	private String narration = null;
	private String isActive = "1";
	private String created = "1-Jan-1900";
	private String modifiedBy = null;
	private String lastModified = "1-Jan-1900";
	private String glcodeID=null;
	private String fundID=null;
	private String payTo="";
	private TaskFailedException taskExc;
	private String currentBalance = "0";
	private String type;
	private String updateQuery="UPDATE bankaccount SET";
	private boolean isId=false, isField=false;
	private static final Logger LOGGER=Logger.getLogger(BankAccount.class); 
	private SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
	public void setId(String aId){ id = aId; isId=true;}
	public int getId() {return Integer.valueOf(id).intValue();}
	public void setBranchId(String aBranchId){ branchId = aBranchId; updateQuery = updateQuery + " branchId=" + branchId + ","; isField = true;}
	public void setAccountNumber(String aAccountNumber){ accountNumber = aAccountNumber; updateQuery = updateQuery + " accountNumber='" + accountNumber + "',"; isField = true;}
	public void setAccountType(String aAccountType){ accountType = aAccountType; updateQuery = updateQuery + " accountType='" + accountType + "',"; isField = true;}
	public void setNarration(String aNarration){ narration = aNarration; updateQuery = updateQuery + " narration='" + narration + "',"; isField = true;}
	public void setPayTo(String apayTo){ payTo = apayTo; updateQuery = updateQuery + " payTo='" + payTo + "',"; isField = true;}
	public void setIsActive(String aIsActive){ isActive = aIsActive; updateQuery = updateQuery + " isActive=" + isActive + ","; isField = true;}
	public void setCreated(String aCreated){ created = aCreated; updateQuery = updateQuery + " created='" + created + "',"; isField = true;}
	public void setModifiedBy(String aModifiedBy){ modifiedBy = aModifiedBy; updateQuery = updateQuery + " modifiedBy=" + modifiedBy + ","; isField = true;}
	public void setLastModified(String aLastModified){ lastModified = aLastModified; updateQuery = updateQuery + " lastModified='" + lastModified + "',"; isField = true;}
	public void setGlcodeId(String aGlcodeID){ glcodeID = aGlcodeID; updateQuery = updateQuery + " glcodeid=" + glcodeID + ","; isField = true;}
	public void setCurrentBalance(String aCurrentBalance){ currentBalance = aCurrentBalance; updateQuery = updateQuery + " currentBalance=" + currentBalance + ","; isField = true;}
	public void setFundID(String aFundID){ fundID = aFundID; updateQuery = updateQuery + "fundid=" + fundID + ","; isField = true;}
	public void setType(String aType){ type = aType; updateQuery = updateQuery + "type='" + aType + "',"; isField = true;}

	public void insert(Connection connection) throws TaskFailedException, SQLException
	{
		EGovernCommon commonmethods = new EGovernCommon();
		created = commonmethods.getCurrentDate(connection);
		Statement statement=null;
		try
   		{
   			created = formatter.format(sdf.parse( created ));  		 
   			setCreated(created);
   			setLastModified(created);
   			narration = commonmethods.formatString(narration);
   			setId( String.valueOf(PrimaryKeyGenerator.getNextKey("BankAccount")) );
   			String insertQuery = "INSERT INTO bankaccount (Id, BranchId, AccountNumber, AccountType, Narration,PayTo, IsActive, " +
			"Created, ModifiedBy, LastModified,glcodeid, CurrentBalance,fundid,type) " +
			"values (" + id + ", " + branchId + ", '" + accountNumber + "', '" + accountType + "', '"
			+ narration + "', '" + payTo + "', " + isActive + ", '" + created + "', " + modifiedBy + ", '"
			+ lastModified + "'," + glcodeID + ", " + currentBalance +","+fundID +",'"+type+"')";

   			LOGGER.debug(insertQuery);
   			statement = connection.createStatement();
   			statement.executeUpdate(insertQuery); 
   		}catch(Exception e){
   			LOGGER.error("Exp in insert"+e.getMessage());
   			throw taskExc;}
		finally{
			try{
				statement.close();
			}catch(Exception e){LOGGER.error("Inside finally block");}
		}
		
	}

	public void update (Connection connection) throws TaskFailedException, SQLException
	{
		if(isId && isField)
		{
			EGovernCommon commonmethods = new EGovernCommon();
			created = commonmethods.getCurrentDate(connection);
			Statement statement = null;
			try
	   		{
	   			created = formatter.format(sdf.parse( created ));
				setLastModified(created);
				narration = commonmethods.formatString(narration);
				updateQuery = updateQuery.substring(0,updateQuery.length()-1);
				updateQuery = updateQuery + " WHERE id = " + id;
				LOGGER.debug(updateQuery);
				statement = connection.createStatement();
				statement.executeUpdate(updateQuery);
				updateQuery="UPDATE bankaccount SET";
	   	}catch(Exception e){
	   		LOGGER.error("Exp in update:"+e.getMessage());
	   		throw taskExc;}
		finally{
			try{
				statement.close();
			}catch(Exception e){LOGGER.error("Inside finally block");}
		}	
			}
	}
 }


