/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
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
