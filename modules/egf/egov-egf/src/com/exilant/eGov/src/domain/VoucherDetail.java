/*
 * Created on Jan 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VoucherDetail
{
	private String id = null;
	private String lineId = null;
	private String voucherHeaderId =null;
	private String glCode = null;
	private String accountName = null;
	private String debitAmount = "0";
	private String creditAmount = "0";
	private String narration = "";
	private static final Logger LOGGER = Logger.getLogger(VoucherDetail.class);
	private String updateQuery="UPDATE voucherdetail SET";
	private boolean isId=false, isField=false;

	public VoucherDetail(){}

	public void setId(String aId){id=aId; isId=true;}
	public void setLineID(String aLineId){lineId=aLineId; updateQuery = updateQuery + " lineId=" + lineId + ","; isField = true;}
	public void setVoucherHeaderID(String aVoucherHeaderId){voucherHeaderId = aVoucherHeaderId; updateQuery = updateQuery + " voucherHeaderId=" + voucherHeaderId + ","; isField = true;}
	public void setGLCode(String aGLCode){glCode=aGLCode; updateQuery = updateQuery + " glCode='" + glCode + "',"; isField = true;}
	public void setAccountName(String aAccountName){accountName=aAccountName; updateQuery = updateQuery + " accountName='" + accountName + "',"; isField = true;}
	public void setDebitAmount(String aDebitAmount){debitAmount=aDebitAmount; updateQuery = updateQuery + " debitAmount=" + debitAmount + ","; isField = true;}
	public void setCreditAmount(String aCreditAmount){creditAmount=aCreditAmount; updateQuery = updateQuery + " creditAmount=" + creditAmount + ","; isField = true;}
	public void setNarration(String aNarration){narration=aNarration; updateQuery = updateQuery + " narration='" + narration + "',"; isField = true;}

	public int getId() {return Integer.valueOf(id).intValue();}
	public String getLineID(){return lineId;}
	public String getVoucherHeaderID(){return voucherHeaderId;}
	public String getGLCode(){return glCode;}
	public String getAccountName(){return accountName;}
	public String getDebitAmount(){return debitAmount;}
	public String getCreditAmount(){return creditAmount;}
	public String getNarration(){return narration;}



	/* inserts the Value in VoucherDetail Table  */
	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		EGovernCommon commonmethods = new EGovernCommon();
		LOGGER.info("Inside VoucherDetail Narration is"+narration);
		if(narration!=null && narration.length()!=0)
		narration = commonmethods.formatString(narration);
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("VoucherDetail")) );

		String insertQuery = "INSERT INTO voucherdetail (ID, LineID, VoucherHeaderID, GLCode, AccountName, " +
						"DebitAmount, CreditAmount, Narration) " +
						"VALUES(" + id + ", " + lineId + ", " + voucherHeaderId + ", '" + glCode + "', '"
						+ accountName + "', " + debitAmount + ", " + creditAmount + ", '" + narration + "')";
		LOGGER.info(insertQuery);
	   	Statement statement = connection.createStatement();
	   	statement.executeUpdate(insertQuery);
	   	updateQuery="UPDATE voucherdetail SET";
	   	statement.close();
	}

	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{
			EGovernCommon commonmethods = new EGovernCommon();
		LOGGER.info("Inside VoucherDetail Narration is"+narration);
		if(narration!=null && narration.length()!=0)
		narration = commonmethods.formatString(narration);
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;

			LOGGER.info(updateQuery);
			Statement statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery="UPDATE voucherdetail SET";
		}
	}
}
