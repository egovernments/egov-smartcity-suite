/*
 * Created on Mar 4, 2005
 * @author pushpendra.singh 
 */

package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

public class ChartOfAccountDetail {
	private String id = null;
	private String glCodeId = null;
	private String detailTypeId = null;
	private static final Logger LOGGER = Logger
			.getLogger(ChartOfAccountDetail.class);
	private TaskFailedException taskExc;
	private String updateQuery = "UPDATE ChartOfAccountDetail SET";
	private boolean isId = false, isField = false;
	EGovernCommon cm = new EGovernCommon();

	public void setId(String aId) {
		id = aId;
		isId = true;
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}

	public void setGLCodeId(String aGLCodeId) {
		glCodeId = aGLCodeId;
		isField = true;
	}

	public void setDetailTypeId(String aDetailTypeId) {
		detailTypeId = aDetailTypeId;
		isField = true;
	}

	public void insert(Connection connection) throws SQLException,
			TaskFailedException {
		PreparedStatement pst = null;
		try {
			setId(String.valueOf(PrimaryKeyGenerator
					.getNextKey("ChartOfAccountDetail")));
			String insertQuery = "INSERT INTO ChartOfAccountDetail (id, glCodeId, detailTypeId)"
					+ "VALUES( ?, ?, ?)";
			pst = connection.prepareStatement(insertQuery);
			pst.setString(1, id);
			pst.setString(2, glCodeId);
			pst.setString(3, detailTypeId);
			if(LOGGER.isDebugEnabled())     LOGGER.debug(insertQuery);
			pst.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exp in insert:" + e.getMessage(),e);
			throw taskExc;
		} finally {
			pst.close();
		}

	}

	public void update(Connection connection) throws SQLException,
			TaskFailedException {
		if (isId && isField) {
			PreparedStatement pst = null;
			try {

				updateQuery = "UPDATE ChartOfAccountDetail SET glCodeId=? , detailTypeId=?  WHERE id =?";
				if(LOGGER.isDebugEnabled())     LOGGER.debug(updateQuery);
				pst = connection.prepareStatement(updateQuery);
				pst.setString(1, glCodeId);
				pst.setString(2, detailTypeId);
				pst.setString(3, id);
				pst.executeUpdate();
			} catch (Exception e) {
				LOGGER.error("Exp in update:" + e.getMessage(),e);
				throw taskExc;
			} finally {
				pst.close();
			}

		}
	}
}