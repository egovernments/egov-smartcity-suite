/*
 * Created on Apr 19, 2005 
 * @author pushpendra.singh 
 */

package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import org.apache.log4j.Logger;

public class TransactionSummary
{
	private String id =null;
	private String financialYearId =null;
	private String glCodeId =null;
	private String openingDebitBalance = "";
	private String openingCreditBalance = "";
	private String debitAmount = "";
	private String creditAmount = "";
	private String accountDetailTypeId =null;
	private String accountDetailKey = null;
	private String fundId = null;

	private String updateQuery="UPDATE TransactionSummary SET";
	private boolean isId=false, isField=false;
	private static final Logger LOGGER = Logger.getLogger(SupplierBillDetail.class);

	public void setId(String aId){ id = aId;  isId=true; }
	public int getId() {return Integer.valueOf(id).intValue(); }
	public void setFinancialYearId(String aFinancialYearId){ financialYearId = aFinancialYearId;  updateQuery = updateQuery + " financialYearId = " + financialYearId + ","; isField = true; }
	public void setGLCodeId(String aGLCodeId){ glCodeId = aGLCodeId;  updateQuery = updateQuery + " glCodeId = " + glCodeId + ","; isField = true; }
	public void setOpeningDebitBalance(String aOpeningDebitBalance){ openingDebitBalance = aOpeningDebitBalance;  updateQuery = updateQuery + " openingDebitBalance = " + openingDebitBalance + ","; isField = true; }
	public void setOpeningCreditBalance(String aOpeningCreditBalance){ openingCreditBalance = aOpeningCreditBalance;  updateQuery = updateQuery + " openingCreditBalance = " + openingCreditBalance + ","; isField = true; }
	public void setDebitAmount(String aDebitAmount){ debitAmount = aDebitAmount;  updateQuery = updateQuery + " debitAmount = " + debitAmount + ","; isField = true; }
	public void setCreditAmount(String aCreditAmount){ creditAmount = aCreditAmount;  updateQuery = updateQuery + " creditAmount = " + creditAmount + ","; isField = true; }
	public void setAccountDetailTypeId(String aAccountDetailTypeId){ accountDetailTypeId = aAccountDetailTypeId;  updateQuery = updateQuery + " accountDetailTypeId = " + accountDetailTypeId + ","; isField = true; }
	public void setAccountDetailKey(String aAccountDetailKey){ accountDetailKey = aAccountDetailKey;  updateQuery = updateQuery + " accountDetailKey = " + accountDetailKey + ","; isField = true; }
	public void setFundId(String aFundId){ fundId = aFundId;  updateQuery = updateQuery + " fundId = " + fundId + ","; isField = true; }

	public void insert(Connection connection) throws SQLException
	{
	//	EGovernCommon commommethods = new EGovernCommon();	
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("TransactionSummary")) );

		String insertQuery = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid, " + 
								"openingdebitbalance, openingcreditbalance, debitamount, " +
								"creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId) " +
								"VALUES (" + id + ", " + financialYearId + ", " + glCodeId + ", " 
								+ openingDebitBalance + ", " + openingCreditBalance + ", " 
								+ debitAmount + ", " + creditAmount + ", " + accountDetailTypeId + ", " 
								+ accountDetailKey + ", " + fundId + ")";
		LOGGER.info(insertQuery);
		Statement statement = connection.createStatement();
		statement.executeUpdate(insertQuery);
		statement.close();
	}

	public void update (Connection connection) throws SQLException
	{
		if(isId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;
			LOGGER.info(updateQuery);
			Statement statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery="UPDATE TransactionSummary SET";
		}
	}
}

