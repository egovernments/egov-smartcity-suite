
package com.exilant.eGov.src.domain;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.exilant.exility.updateservice.PrimaryKeyGenerator;

public class Eg_Numbers
{
	   private String id =null;
	   private String voucherNumber =null;
	   private String voucherType = null;
	   private String fiscialPeriodId=null;
	   private String updateQuery="UPDATE eg_numbers SET";
	   private boolean isId=false, isField=false;
	   private static final Logger LOGGER=Logger.getLogger(Eg_Numbers.class);
	   
	   public Eg_Numbers() {}
	   public void setId(String aId) {id = aId; isId=true;}
	   public void setVoucherNumber(String aVoucherNumber) {voucherNumber = aVoucherNumber; updateQuery = updateQuery + " voucherNumber='" + voucherNumber + "',"; isField = true;}
	   public void setVoucherType(String vType) {voucherType = vType; updateQuery = updateQuery + " voucherType='" + voucherType + "',"; isField = true;}
	   public void setFiscialPeriodId(String afiscialPeriodId) {fiscialPeriodId = afiscialPeriodId; updateQuery = updateQuery + " fiscialPeriodId=" + fiscialPeriodId + ","; isField = true;}


	   public int getId() {return Integer.valueOf(id).intValue();}
	   public String getVoucherNumber() {return voucherNumber;}
	   public String getVoucherType() {return voucherType;}
	   public String getFiscialPeriodId() {return fiscialPeriodId ;}


	   public void insert(Connection connection) throws SQLException
	   {
	   		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("eg_numbers")) );
	   		String insertQuery = "INSERT INTO eg_numbers (Id, VoucherNumber,VoucherType,fiscialPeriodId) " +
							"VALUES (" + id + ", " +  voucherNumber + ",'" + voucherType + "'," + fiscialPeriodId + " )";
	   		LOGGER.info(insertQuery);
	   		Statement statement = connection.createStatement();
			statement.executeUpdate(insertQuery);
			statement.close();
		}

	   public void update (final Connection connection) throws SQLException
		{
			if(isId && isField)
			{
				updateQuery = updateQuery.substring(0,updateQuery.length()-1);
				updateQuery = updateQuery + " WHERE id = " + id;
				LOGGER.info(updateQuery);
				Statement statement = connection.createStatement();
				statement.executeUpdate(updateQuery);
				statement.close();
				updateQuery="UPDATE eg_numbers SET";
			}
		}

}
