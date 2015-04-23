/*
 * Created on Jan 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import org.apache.log4j.Logger;
/**
 * @author vijaykumar.b
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class ContraJournalVoucher
{
	private String id = null;
	private String voucherHeaderID = null;
	private String type = "";
	private String fromBankId = null;
	private String fromBankBranchId = null;
	private String fromBankAccountId = null;
	private String fromChequeNumber = "";
	private String fromChequeDate = "1-Jan-1900";
	private String toBankId = null;
	private String toBankBranchId = null;
	private String toBankAccountId = null;
	private String narration = "";
	private String toCashNameId = null;
	private String toChequeNameId = null;
	private String fromCashNameId = null;
	private String fromFundID = null;
	private String toFundID = null;
	private String isReversed = "0";
	private String updateQuery="UPDATE ContraJournalVoucher SET";
	
	private static final Logger LOGGER=Logger.getLogger(ContraJournalVoucher.class);
	private boolean isId=false, isField=false;

	private Statement statement;

	public void setId(String aID){ id = aID; isId=true;}
	public void setVoucherHeaderID(String aVoucherHeaderID){ voucherHeaderID = aVoucherHeaderID;updateQuery = updateQuery + " voucherHeaderID='" + voucherHeaderID + "',"; isField = true;}
	public void setType(String aType){ type = aType; updateQuery = updateQuery + " type='" + type + "',"; isField = true;}
	public void setFromBankId(String aFromBankId){	fromBankId = aFromBankId; updateQuery = updateQuery + " fromBankId='" + fromBankId + "',"; isField = true;}
	public void setFromBankBranchId(String aFromBankBranchId){ fromBankBranchId = aFromBankBranchId; updateQuery = updateQuery + " fromBankBranchId='" + fromBankBranchId + "',"; isField = true;}
	public void setFromBankAccountId(String aFromBankAccountId){ fromBankAccountId = aFromBankAccountId; updateQuery = updateQuery + " fromBankAccountId='" + fromBankAccountId + "',"; isField = true;}
	public void setFromChequeNumber(String aFromChequeNumber){ fromChequeNumber = aFromChequeNumber; updateQuery = updateQuery + " fromChequeNumber='" + fromChequeNumber + "',"; isField = true;}
	public void setFromChequeDate(String aFromChequeDate){ fromChequeDate = aFromChequeDate; updateQuery = updateQuery + " fromChequeDate='" + fromChequeDate + "',"; isField = true;}
	public void setToBankId(String aToBankId){ toBankId = aToBankId; updateQuery = updateQuery + " toBankId='" + toBankId + "',"; isField = true;}
	public void setToBankBranchId(String aToBankBranchId){ toBankBranchId = aToBankBranchId; updateQuery = updateQuery + " toBankBranchId='" + toBankBranchId + "',"; isField = true;}
	public void setToBankAccountId(String aToBankAccountId){ toBankAccountId = aToBankAccountId; updateQuery = updateQuery + " toBankAccountId='" + toBankAccountId + "',"; isField = true;}
	public void setNarration(String aNarration){ narration = aNarration; updateQuery = updateQuery + " narration='" + narration + "',"; isField = true;}
	public void setToCashNameId(String aToCashNameId){ toCashNameId = aToCashNameId; updateQuery = updateQuery + " toCashNameId='" + toCashNameId + "',"; isField = true;}
	public void setFromCashNameId(String aFromCashNameId){ fromCashNameId = aFromCashNameId; updateQuery = updateQuery + " fromCashNameId='" + fromCashNameId + "',"; isField = true;}
	public void setFromFundID(String aFromFundID){ fromFundID = aFromFundID; updateQuery = updateQuery + " fromFundID='" + fromFundID + "',"; isField = true;}
	public void setToFundID(String aToFundID){ toFundID = aToFundID; updateQuery = updateQuery + " toFundID='" + toFundID + "',"; isField = true;}
	public void setToChequeNameId(String aToChequeNameId){ toChequeNameId = aToChequeNameId; updateQuery = updateQuery + " toChequeNameId='" + toChequeNameId + "',"; isField = true;}
	public void setIsReversed(String aIsReversed){ isReversed = aIsReversed; updateQuery = updateQuery + " isReversed='" + isReversed + "',"; isField = true;}

	public void insert(Connection connection) throws SQLException
	{
		EGovernCommon commommethods = new EGovernCommon();
		narration = commommethods.formatString(narration);

		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("ContraJournalVoucher")) );
		String insertQuery = "INSERT INTO ContraJournalVoucher (Id, VoucherHeaderID, Type, FromBankId, " +
								"FromBankBranchId, FromBankAccountId, FromChequeNumber, FromChequeDate, " +
								"ToBankId, ToBankBranchId, ToBankAccountId, Narration, ToCashNameId, " +
								"FromCashNameId, FromFundID, ToFundID,tochequenameid,isReversed) " +
								"VALUES (" + id + ", " + voucherHeaderID + ", '" + type + "', " + fromBankId + ", " +
								fromBankBranchId + ", " + fromBankAccountId + ", '" + fromChequeNumber + "', '" +
								fromChequeDate + "', " + toBankId + ", " + toBankBranchId + ", " +
								toBankAccountId + ", '" + narration + "', " + toCashNameId + ", " +
								fromCashNameId + ", " + fromFundID + ", " + toFundID +","+ toChequeNameId+","+isReversed+")";

		Statement statement = connection.createStatement();
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		statement.executeUpdate(insertQuery);
		statement.close();
	}
	
	public void reverse(Connection connection)throws SQLException
	{
		//	EGovernCommon commommethods=new EGovernCommon();
			Statement statement=connection.createStatement();
			String reverseQuery="UPDATE contraJournalvoucher SET IsReversed=1 WHERE id="+id;
			if(LOGGER.isInfoEnabled())     LOGGER.info(reverseQuery);
			statement.executeQuery(reverseQuery);
			statement.close();
	}

	//added by rashmi
	public void update (Connection connection) throws SQLException
	{
		if(isId && isField)
		{
		//	EGovernCommon commommethods = new EGovernCommon();
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;
			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery="UPDATE ContraJournalVoucher SET";
		}
	}


}

