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
package com.exilant.eGov.src.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.domain.AccountDtlKey;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class AccountDtlKeyAdd extends AbstractTask {
	private final static Logger LOGGER = Logger
			.getLogger(AccountDtlKeyAdd.class);
	private transient Connection connection;
	private PreparedStatement pstmt = null;
	private ResultSet resultset;
	// private boolean autoCommit=false;
	// private String currentDate="";
	private DataCollection dataCollection;

	public void execute(String taskName, String gridName,
			DataCollection datacollection, Connection conn,
			boolean errorOnNoData, boolean gridHasColumnHeading, String prefix)
			throws TaskFailedException {

		dataCollection = datacollection;
		this.connection = conn;
		try {
			postInFundAdd();
		} catch (SQLException sqlex) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug("ERROR IN POSTING : " + sqlex.toString(),sqlex);
			dataCollection.addMessage("eGovFailure", " ");
			throw new TaskFailedException(sqlex);
		}
	}

	public void postInFundAdd() throws SQLException, TaskFailedException {

		int cnt = 1;
		String cashCode = "";
		String chequeCode = "";
		String tName = dataCollection.getValue("tableName");
		String cde = tName + "_code";
		String code = dataCollection.getValue(cde);
		// boolean bothPresent = false;
		String desc = "";
		String attrName = "";
		ResultSet rs = null;
		ResultSet rst = null;
		String qryString = "";
		String type = "cash";
		String dtlKey = "";
		String aid;

		if (tName.equals("organizationStructure")) {
			desc = dataCollection.getValue("organizationStructure_description");
			cashCode = dataCollection
					.getValue("organizationStructure_cashInHand");
			chequeCode = dataCollection
					.getValue("organizationStructure_chequeInHand");

			if (cashCode.length() != 0 && chequeCode.length() != 0) {
				// bothPresent = true;
				cnt++;
			}
		}

		for (int i = 1; i <= cnt; i++) {
			AccountDtlKey ADk = new AccountDtlKey();
			if (!desc.equalsIgnoreCase("zone")) {
				if (tName.equalsIgnoreCase("organizationstructure")) {
					if (desc.equalsIgnoreCase("region")) {
						if (cashCode != null && type.equalsIgnoreCase("cash")) {
							attrName = "regionCash_id";
							ADk.setglCodeID(cashCode);
							type = "cheque";
						} else if (chequeCode != null) {
							attrName = "regionCheque_id";
							ADk.setglCodeID(chequeCode);
						}
					} else if (desc.equalsIgnoreCase("ward")) {
						attrName = "organizationStructure_id";
					}
					qryString = "Select ID, attributeName from AccountDetailType where attributename = ?";
					pstmt = connection.prepareStatement(qryString);
					pstmt.setString(1, attrName);

				} // end of orgstructure
				// } // end of orgstructure
				else if (tName.equals("accountEntityMaster")) {
					String str = dataCollection
							.getValue("accountEntityMaster_detailTypeId");
					qryString = "Select ID, attributeName from AccountDetailType where id= ? ";
					if(LOGGER.isDebugEnabled())     LOGGER.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + qryString);
					pstmt = connection.prepareStatement(qryString);
					pstmt.setString(1, str);
				} else {
					qryString = "Select ID, attributeName from AccountDetailType where tablename= ? ";
					pstmt = connection.prepareStatement(qryString);
					pstmt.setString(1, tName);
				}

				rst = pstmt.executeQuery();
				while (rst.next()) {
					ADk.setdetailTypeId(rst.getString(1));
					ADk.setdetailName(rst.getString(2));
				}

				// select detail key for the code
				if (tName.equalsIgnoreCase("financialYear")) {
					String fYear = dataCollection
							.getValue("financialYear_financialYear");
					// if(LOGGER.isDebugEnabled())     LOGGER.debug("*****************"+fYear+"*********");
					qryString = "Select ID from "+tName+" Where financialYear=?";
					pstmt = connection.prepareStatement(qryString);
					pstmt.setString(1, fYear);
				} else {
					qryString = "Select ID from "+tName+" where code= ? ";
					pstmt = connection.prepareStatement(qryString);
					pstmt.setString(1, code);
				}
				rs = pstmt.executeQuery();
				while (rs.next()) {
					dtlKey = rs.getString(1);
					ADk.setdetailKey(dtlKey);
				}
				String qry=null;
				if(attrName==""){
					qry = "Select Id from AccountDetailKey where detailkey= ? and detailName=''";
				}else{
					qry = "Select Id from AccountDetailKey where detailkey= ? and detailName= ? ";
				}
				if(LOGGER.isDebugEnabled())     LOGGER.debug("*********Before prepare Statement*****" + qry);
				pstmt = connection.prepareStatement(qry);
				if(attrName==""){
					pstmt.setString(1, dtlKey);
				}else{
					pstmt.setString(1, dtlKey);
					pstmt.setString(2, attrName);
				}
				rs = pstmt.executeQuery();
				if (rs.next()) {
					aid = rs.getString(1);
					if(LOGGER.isDebugEnabled())     LOGGER.debug(aid);
					ADk.setID(aid);
					ADk.update();
					// if(LOGGER.isDebugEnabled())     LOGGER.debug("<<<<<<<<<<<<<<<<update service");
				} else {
					ADk.insert();
				}
			}

		}// end of for

	}// end of post
}

// end of class

