package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.exilant.exility.updateservice.PrimaryKeyGenerator;

public class Eg_Numbers 
{
	private String id = null;
	private String voucherNumber = null;
	private String voucherType = null;
	private String fiscialPeriodId = null;
	private String updateQuery = "UPDATE eg_numbers SET";
	private boolean isId = false, isField = false;
	private static final Logger LOGGER = Logger.getLogger(Eg_Numbers.class);

	public Eg_Numbers() {
	}

	public void setId(String aId) {
		id = aId;
		isId =true;
	}

	public void setVoucherNumber(String aVoucherNumber) {
		voucherNumber = aVoucherNumber;
		updateQuery = updateQuery + " voucherNumber='" + voucherNumber + "',";
		isField = true;
	}

	public void setVoucherType(String vType) {
		voucherType = vType;
		updateQuery = updateQuery + " voucherType='" + voucherType + "',";
		isField = true;
	}

	public void setFiscialPeriodId(String afiscialPeriodId) {
		fiscialPeriodId = afiscialPeriodId;
		updateQuery = updateQuery + " fiscialPeriodId=" + fiscialPeriodId + ",";
		isField = true;
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}

	public String getVoucherNumber() {
		return voucherNumber;
	}

	public String getVoucherType() {
		return voucherType;
	}

	public String getFiscialPeriodId() {
		return fiscialPeriodId;
	}

	public void insert(Connection connection) throws SQLException {
		PreparedStatement psmt = null;
		setId(String.valueOf(PrimaryKeyGenerator.getNextKey("eg_numbers")));
		String insertQuery = "INSERT INTO eg_numbers (Id, VoucherNumber,VoucherType,fiscialPeriodId) values (?,?,?,?)";
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		psmt = connection.prepareStatement(insertQuery);
		psmt.setString(1, id);
		psmt.setString(2, voucherNumber);
		psmt.setString(3, voucherType);
		psmt.setString(4, fiscialPeriodId);
		psmt.executeQuery();
		psmt.close();
	}

	public void update (final Connection connection) throws SQLException
		{
		    PreparedStatement psmt=null;
			if(isId && isField)
			{
				updateQuery = updateQuery.substring(0,updateQuery.length()-1);
				updateQuery = updateQuery + " WHERE id = ?";
				if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
				psmt=connection.prepareStatement(updateQuery);
				psmt.setString(1,id);
				psmt.executeUpdate();
				
				psmt.close();
				updateQuery="UPDATE eg_numbers SET";
			}
		}
}
