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

import java.sql.Statement;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
@Transactional(readOnly=true)
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
	@Transactional
	public void insert() throws TaskFailedException {
		Query statement = null;

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
			statement = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
			statement.executeUpdate();
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
	@Transactional
	public void updateUsingBillId()
			throws TaskFailedException {
		try {
			updateQuery = updateQuery.substring(0, updateQuery.length() - 1);
			updateQuery = updateQuery + " WHERE billid = " + billid;
			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			Query statement = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
			statement.executeUpdate();
			updateQuery = "UPDATE otherbilldetail SET";

		} catch (Exception e) {
			LOGGER.error("Error in update: " + e,e);
			throw taskExc;
		}
	}

}
