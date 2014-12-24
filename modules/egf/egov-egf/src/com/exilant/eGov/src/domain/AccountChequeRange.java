/*
 * Created on Jan 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.lib.rjbac.user.User;

import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class AccountChequeRange
{
	private String id = "";
	private String bankAccountID = "";
	private String fromChequeNumber = "";
	private String toChequeNumber = "";
	private String receivedDate = "1-Jan-1900";
	private String isExhausted = "0";
	private String nextChqNo = "";
	private String isAllottedTo = "1";
	private String createdBy;
	
	public String getCreatedBy() {
		return createdBy;
	}
	
	public String getCreatedDate() {
		return createdDate;
	}
	
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	

	private String createdDate;
	private String lastModifiedBy;
	private String lastModifiedDate;
	
	private TaskFailedException taskExc;
	
	private static final Logger LOGGER = Logger.getLogger(AccountChequeRange.class);
	private String updateQuery="UPDATE bankbranch SET";

	public void setId(final String aId){ id = aId; }
	public int getId() {return Integer.valueOf(id).intValue(); }
	public void setBankAccountID(final String aBankAccountID){ bankAccountID = aBankAccountID; updateQuery = updateQuery + " BankAccountID='" + bankAccountID + "',"; }
	public void setFromChequeNumber(final String aFromChequeNumber){ fromChequeNumber = aFromChequeNumber; updateQuery = updateQuery + " FromChequeNumber='" + fromChequeNumber + "',"; }
	public void setToChequeNumber(final String aToChequeNumber){ toChequeNumber = aToChequeNumber; updateQuery = updateQuery + " branchAddress1='" + toChequeNumber + "',"; }
	public void setReceivedDate(final String aReceivedDate){ receivedDate = aReceivedDate; updateQuery = updateQuery + " ReceivedDate='" + receivedDate + "',"; }
	public void setIsExhausted(final String aisExhausted){ isExhausted = aisExhausted; updateQuery = updateQuery + " isExhausted=" + isExhausted + ",,"; }
	public void setNextChqNo(final String anextChqNo){ nextChqNo = anextChqNo; updateQuery = updateQuery + " nextChqNo='" + nextChqNo + "',"; }
	public void setIsAllotteTo(final String aIsAllottedTo){ isAllottedTo = aIsAllottedTo; updateQuery = updateQuery + " isAllottedTo=" + isAllottedTo + ",,"; }
	public void setCreatedBy(String acreatedBy) {createdBy = acreatedBy; updateQuery = updateQuery + " createdBy=" + createdBy + ",,"; }
	public void setCreatedDate(String acreatedDate) {createdDate = acreatedDate; updateQuery = updateQuery + " createdDate=" + createdDate + ",,";}
	public void setLastModifiedBy(String alastModifiedBy) {lastModifiedBy = alastModifiedBy; updateQuery = updateQuery + " lastModifiedBy=" + lastModifiedBy + ",,";}
	public void setLastModifiedDate(String alastModifiedDate) {lastModifiedDate = alastModifiedDate;updateQuery = updateQuery + " lastModifiedDate=" + lastModifiedDate + ",,";}
	
	public void insert(final Connection connection) throws SQLException,TaskFailedException
	{
		Statement statement =null;
		try{
			setId( String.valueOf(PrimaryKeyGenerator.getNextKey("EGF_ACCOUNT_CHEQUES")) );
		
		String insertQuery = "INSERT INTO EGF_ACCOUNT_CHEQUES (Id, BankAccountID, FromChequeNumber, ToChequeNumber, ReceivedDate,isExhausted,nextChqNo,AllotedTo,createdBy,createdDate,lastModifiedBy,lastModifiedDate) " + 
						"VALUES (" + id + ", '" + bankAccountID + "', '" + fromChequeNumber + "', '" + toChequeNumber + "', '" 
						+ receivedDate + "'," + isExhausted + ",'" + nextChqNo + "'," + isAllottedTo +"," + createdBy + ",'" + createdDate + "'," + lastModifiedBy +  ",'" + lastModifiedDate +  "' )";
		
		LOGGER.info(insertQuery);
		statement = connection.createStatement();
		statement.executeUpdate(insertQuery);
		}catch(Exception e)
		{
			LOGGER.error("Exception in insert:"+e.getMessage());
			throw taskExc;
		}finally{
			try{
				statement.close();
			}catch(Exception e){LOGGER.error("Inside finally block");}
		}
		
		
	}
	
}

