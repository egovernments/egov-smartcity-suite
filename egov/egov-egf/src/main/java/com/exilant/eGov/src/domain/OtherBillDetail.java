/*
 * Created on Mar 8, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

/**
 * @author Elzan Mathew
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.Connection;
import java.sql.Statement;
import org.apache.log4j.Logger;

public class OtherBillDetail {

	private static final Logger LOGGER = Logger
			.getLogger(OtherBillDetail.class);
	private String id = null;
	private String voucherheaderid = null;
	private String billid = null;
	private String PayVhId = null;
	private static TaskFailedException taskExc;
	private String updateQuery = "UPDATE otherbilldetail SET";

	public OtherBillDetail() {
	}

	public void setId(String aId) {
		id = aId;
	}

	public void setVoucherheaderid(String vhid) {
		voucherheaderid = vhid;
		updateQuery = updateQuery + " voucherheaderid='" + voucherheaderid
				+ "',";
	}

	public void setFieldid(String afieldid) {
		billid = afieldid;
		updateQuery = updateQuery + " billid=" + billid + ",";
	}

	public void setPayVhId(String phId) {
		PayVhId = phId;
		updateQuery = updateQuery + " PayVhId=" + PayVhId + ",";
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}

	public String getVoucherheaderid() {
		return voucherheaderid;
	}

	public String getBillid() {
		return billid;
	}

	public String getPayVhId() {
		return PayVhId;
	}

	/**
	 * Function to Insert to otherbilldetail table
	 * 
	 * @param connection
	 * @throws TaskFailedException
	 */
	public void insert(Connection connection) throws TaskFailedException {
		Statement statement = null;

		setId(String.valueOf(PrimaryKeyGenerator.getNextKey("otherbilldetail")));

		String insertQuery = "INSERT INTO otherbilldetail (Id, voucherheaderid, billid,PayVhId) "
				+ "VALUES ("
				+ id
				+ ","
				+ voucherheaderid
				+ ","
				+ billid
				+ ","
				+ PayVhId + ")";

		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);

		try {
			statement = connection.createStatement();
			statement.executeUpdate(insertQuery);
			statement.close();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			throw taskExc;
		}
	}

	/**
	 * This function is to update the data to otherbilldetail table.
	 * 
	 * @param connection
	 * @throws TaskFailedException
	 */

	public void updateUsingBillId(Connection connection)
			throws TaskFailedException {
		try {
			updateQuery = updateQuery.substring(0, updateQuery.length() - 1);
			updateQuery = updateQuery + " WHERE billid = " + billid;
			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			Statement statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery = "UPDATE otherbilldetail SET";

		} catch (Exception e) {
			LOGGER.error("Error in update: " + e,e);
			throw taskExc;
		}
	}

}
