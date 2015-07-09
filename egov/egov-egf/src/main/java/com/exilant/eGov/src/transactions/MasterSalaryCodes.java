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
/* Created on July 17, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.transactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.EGSalaryCodes;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author Iliyaraja
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */

public class MasterSalaryCodes extends AbstractTask {
	private final static Logger LOGGER = Logger
			.getLogger(MasterSalaryCodes.class);
	private DataCollection dc;
	private Connection con = null;
	private ResultSet rs = null;
	PreparedStatement pstmt = null;
	String today;
	private int id;

	EGovernCommon commonmethod = new EGovernCommon();

	public MasterSalaryCodes() {
	}

	public void execute(String nameTask, String dataTask,
			DataCollection dataCollection, Connection conn,
			boolean errorOnNoData, boolean gridHasColumnHeading, String prefix)
			throws TaskFailedException {

		this.dc = dataCollection;
		this.con = conn;
		boolean temp = false; /* This value is to Check the completion of Function */

		int flag = 0;
		try {
			if(LOGGER.isDebugEnabled())     LOGGER.debug("MODE OF EXECUTION VALUE IS:"
					+ dc.getValue("modeOfExec"));
			// TEST START
			String[][] salaryCodes2 = (String[][]) dc
					.getGrid("salaryCodesTable");

			if(LOGGER.isInfoEnabled())     LOGGER.info("salaryCodes2 Length is:" + salaryCodes2.length);

			for (int i = 0; i < salaryCodes2.length; i++) {
				if(LOGGER.isInfoEnabled())     LOGGER.info("salaryCodes i value is :" + i);

				if(LOGGER.isInfoEnabled())     LOGGER.info("salaryCodes2[i][0]" + salaryCodes2[i][0]);// salary_CodeId
				if(LOGGER.isInfoEnabled())     LOGGER.info("salaryCodes2[i][1]" + salaryCodes2[i][1]);// salary_accountHead
				if(LOGGER.isInfoEnabled())     LOGGER.info("salaryCodes2[i][2]" + salaryCodes2[i][2]);// glCode_Id
				if(LOGGER.isInfoEnabled())     LOGGER.info("salaryCodes2[i][3]" + salaryCodes2[i][3]);// salary_Type

			}

			// TEST END

			if (dc.getValue("modeOfExec").equalsIgnoreCase("new")) {
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Create Mode------------------>");

				flag = 1;
				temp = postInEGSalaryCodes();
				if (temp != true && flag == 1) {
					dc.addMessage("userMessege",
							"New Salary Codes Insertion Failed");
					throw new TaskFailedException(
							"postInEGSalaryCodes Function Fails");
				}

			} // new mode
			else if (dc.getValue("modeOfExec").equalsIgnoreCase("modify")) {
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Modify Mode------------------>");
				flag = 2;
				temp = updateEGSalaryCodes();
				if (temp != true && flag == 2) {
					dc
							.addMessage("userMessege",
									"Salary Codes Updation Failed");
					throw new TaskFailedException(
							"updateEGSalaryCodes Function Fails");
				}
			} // modify mode

			if (flag == 1) {
				dc.addMessage("masterInsertUpdate");
			}
			if (flag == 2) {
				dc.addMessage("masterInsertUpdate");
			}

		} catch (Exception ex) {
			LOGGER.error("error in execute" + ex.getMessage(), ex);
			throw new TaskFailedException(ex);
		} finally {
			try {
			} catch (Exception close) {
				LOGGER.error("error inside Finally.." + close.getMessage(),
						close);
			}
		}
	} // execute method end

	private boolean postInEGSalaryCodes() throws Exception {
		if(LOGGER.isInfoEnabled())     LOGGER.info("Inside postInEGSalaryCodes");
		EGSalaryCodes salcode = new EGSalaryCodes();
		boolean answer = false;

		String[][] salaryCodesGrid = (String[][]) dc
				.getGrid("salaryCodesTable");

		if(LOGGER.isInfoEnabled())     LOGGER.info("salaryCodesGrid Length is:" + salaryCodesGrid.length);

		for (int j = 0; j < salaryCodesGrid.length; j++) {
			// 0. salary_CodeId 1. salary_accountHead 2. glCode_Id 3.
			// salary_Type

			if (salaryCodesGrid[j][2] != "") {
				salcode = new EGSalaryCodes();

				String aHead = salaryCodesGrid[j][1];
				String aGlcodeId = salaryCodesGrid[j][2];

				int answer1 = checkHeadUniqueInsert(aHead);
				if(LOGGER.isDebugEnabled())     LOGGER.debug("ANSWER1:" + answer1);

				try {
					if (answer1 != 1) {
						dc.addMessage("userFailure",
								"Duplicate Salary Head Not Allowed");
						throw new TaskFailedException(
								"checkHeadUniqueInsert Function Failed");
					}

				} catch (Exception ex) {
					LOGGER.error("error in postInEGSalaryCodes()"
							+ ex.getMessage(), ex);
					throw new TaskFailedException(ex);
				}

				int answer2 = checkGlcodeIdUniqueInsert(aGlcodeId);
				if(LOGGER.isDebugEnabled())     LOGGER.debug("ANSWER2:" + answer2);
				try {
					if (answer2 != 1) {
						dc.addMessage("userFailure",
								"Duplicate GLCode Not Allowed");
						throw new TaskFailedException(
								"checkGlcodeIdUniqueInsert Function Failed");
					}

				} catch (Exception ex) {
					LOGGER.error("error inside postInEGSalaryCodes()"
							+ ex.getMessage(), ex);
					throw new TaskFailedException(ex);
				}

				salcode.setHead(salaryCodesGrid[j][1]);
				salcode.setGlcodeId(salaryCodesGrid[j][2]);
				salcode.setSalType(salaryCodesGrid[j][3]);

				salcode.setCreatedby(dc.getValue("egUser_id"));
				today = commonmethod.getCurrentDateTime();
				salcode
						.setCreatedDate(commonmethod
								.getSQLDateTimeFormat(today));

				if (answer1 != 0 && answer2 != 0) {

					salcode.insert();
					answer = true;
				} else {
					answer = false;

				}
			} // main if
		} // for j
		return answer;
	} // End postInEGSalaryCode

	private boolean updateEGSalaryCodes() throws Exception {
		if(LOGGER.isInfoEnabled())     LOGGER.info("Inside updateEGSalaryCodes()");

		EGSalaryCodes salcode = new EGSalaryCodes();
		boolean answer = false;

		String[][] salaryCodesGrid = (String[][]) dc
				.getGrid("salaryCodesTable");

		if(LOGGER.isInfoEnabled())     LOGGER.info("salaryCodesGrid Length is:" + salaryCodesGrid.length);

		for (int j = 0; j < salaryCodesGrid.length; j++) {
			// 0. salary_CodeId 1. salary_accountHead 2. glCode_Id 3.
			// salary_Type

			if (salaryCodesGrid[j][0] != "" && salaryCodesGrid[j][2] != "") {
				LOGGER
						.info("MODIFY MODE------------>Inside salaryCodesGrid UPDATE");
				salcode = new EGSalaryCodes();

				String aHeadId = salaryCodesGrid[j][0];
				String aHead = salaryCodesGrid[j][1];
				String aGlcodeId = salaryCodesGrid[j][2];

				int answer1 = checkHeadUniqueUpdate(aHeadId, aHead);
				if(LOGGER.isDebugEnabled())     LOGGER.debug("ANSWER1:" + answer1);

				try {
					if (answer1 != 1) {
						dc.addMessage("userFailure",
								"Duplicate Salary Head Not Allowed");
						throw new TaskFailedException(
								"checkHeadUniqueUpdate Function Failed");
					}

				} catch (Exception ex) {
					LOGGER.error("error inside updateEGSalaryCodes()"
							+ ex.getMessage(), ex);
					throw new TaskFailedException(ex);
				}

				int answer2 = checkGlcodeIdUniqueUpdate(aHeadId, aGlcodeId);
				if(LOGGER.isDebugEnabled())     LOGGER.debug("ANSWER2:" + answer2);
				try {
					if (answer2 != 1) {
						dc.addMessage("userFailure",
								"Duplicate GLCode Not Allowed");
						throw new TaskFailedException(
								"checkGlcodeIdUniqueUpdate Function Failed");
					}

				} catch (Exception ex) {
					LOGGER.error("error inside updateEGSalaryCodes"
							+ ex.getMessage(), ex);
					throw new TaskFailedException(ex);
				}

				salcode.setId(salaryCodesGrid[j][0]);
				salcode.setHead(salaryCodesGrid[j][1]);
				salcode.setGlcodeId(salaryCodesGrid[j][2]);
				salcode.setSalType(salaryCodesGrid[j][3]);

				salcode.setLastModifiedBy(dc.getValue("egUser_id"));
				today = commonmethod.getCurrentDateTime();
				salcode.setLastModifiedDate(commonmethod
						.getSQLDateTimeFormat(today));

				if (answer1 != 0 && answer2 != 0) {

					salcode.update();
					answer = true;
				} else {
					answer = false;

				}

			} // main if for UPDATE

			if (salaryCodesGrid[j][0] == "" && salaryCodesGrid[j][2] != "") {
				LOGGER
						.info("MODIFY MODE------------>Inside salaryCodesGrid INSERT");
				salcode = new EGSalaryCodes();

				String aHead = salaryCodesGrid[j][1];
				String aGlcodeId = salaryCodesGrid[j][2];

				int answer1 = checkHeadUniqueInsert(aHead);
				if(LOGGER.isDebugEnabled())     LOGGER.debug("ANSWER3:" + answer1);

				try {
					if (answer1 != 1) {
						dc.addMessage("userFailure",
								"Duplicate Salary Head Not Allowed");
						throw new TaskFailedException(
								"checkHeadUniqueInsert Function Failed");
					}

				} catch (Exception ex) {
					LOGGER.error("error inside updateEGSalaryCodes"
							+ ex.getMessage(), ex);
					throw new TaskFailedException(ex);
				}

				int answer2 = checkGlcodeIdUniqueInsert(aGlcodeId);
				if(LOGGER.isDebugEnabled())     LOGGER.debug("ANSWER4:" + answer2);
				try {
					if (answer2 != 1) {
						dc.addMessage("userFailure",
								"Duplicate GLCode Not Allowed");
						throw new TaskFailedException(
								"checkGlcodeIdUniqueInsert Function Failed");
					}

				} catch (Exception ex) {
					LOGGER.error("error inside updateEGSalaryCodes"
							+ ex.getMessage(), ex);
					throw new TaskFailedException(ex);
				}

				salcode.setHead(salaryCodesGrid[j][1]);
				salcode.setGlcodeId(salaryCodesGrid[j][2]);
				salcode.setSalType(salaryCodesGrid[j][3]);

				salcode.setCreatedby(dc.getValue("egUser_id"));
				today = commonmethod.getCurrentDateTime();
				salcode
						.setCreatedDate(commonmethod
								.getSQLDateTimeFormat(today));

				if (answer1 != 0 && answer2 != 0) {
					salcode.insert();
					answer = true;
				} else {
					answer = false;

				}

			} // main if for INSERT
		} // for j
		return answer;
	} // updateEGSalaryCodes method

	// ----------These Methods and Queries are used for Only Validation
	// purpose-------------

	private int checkHeadUniqueInsert(String aHead) throws TaskFailedException {
		int result1 = 0;
		String sql1 = "select esal.HEAD from  eg_salarycodes esal where  upper(esal.HEAD) like upper(?)";
		try {
			pstmt = con.prepareStatement(sql1);
			pstmt.setString(1, aHead);
			ResultSet rset2 = pstmt.executeQuery();
			if(LOGGER.isDebugEnabled())     LOGGER.debug("sql1:" + sql1);
			if (rset2.next())
				result1 = 0;
			else
				result1 = 1;
		} catch (Exception ex) {
			LOGGER.error(
					"error inside checkHeadUniqueInsert" + ex.getMessage(), ex);
			dc.addMessage("userFailure", "Duplicate Salary Head Not Allowed");
			throw new TaskFailedException(ex);
		}
		return result1;
	}

	private int checkHeadUniqueUpdate(String aHeadId, String aHead)
			throws TaskFailedException {
		int result1 = 0;
		String sql1 = "select esal.HEAD from  eg_salarycodes esal where  esal.id !=? and upper(esal.HEAD) like upper(?)";
		try {
			pstmt = con.prepareStatement(sql1);
			pstmt.setString(1, aHeadId);
			pstmt.setString(2, aHead);
			ResultSet rset2 = pstmt.executeQuery();
			if(LOGGER.isDebugEnabled())     LOGGER.debug("sql1:" + sql1);
			if (rset2.next())
				result1 = 0;
			else
				result1 = 1;
		} catch (Exception ex) {
			LOGGER.error(
					"error inside checkHeadUniqueUpdate" + ex.getMessage(), ex);
			dc.addMessage("userFailure", "Duplicate Salary Head Not Allowed");
			throw new TaskFailedException(ex);
		}
		return result1;
	}

	private int checkGlcodeIdUniqueInsert(String aGlcodeId)
			throws TaskFailedException {
		int result2 = 0;
		String sql2 = "select esal.GLCODEID from  eg_salarycodes esal where  esal.GLCODEID=?";
		try {
			pstmt = con.prepareStatement(sql2);
			pstmt.setString(1, aGlcodeId);
			ResultSet rset2 = pstmt.executeQuery();
			if(LOGGER.isDebugEnabled())     LOGGER.debug("sql2:" + sql2);
			if (rset2.next())
				result2 = 0;
			else
				result2 = 1;
		} catch (Exception ex) {
			LOGGER.error("error inside checkGlcodeIdUniqueInsert"
					+ ex.getMessage(), ex);
			dc.addMessage("userFailure", "Duplicate Salary Head Not Allowed");
			throw new TaskFailedException(ex);
		}
		return result2;
	}

	private int checkGlcodeIdUniqueUpdate(String aHeadId, String aGlcodeId)
			throws TaskFailedException {
		int result2 = 0;
		String sql2 = "select esal.GLCODEID from  eg_salarycodes esal where  esal.id !=? and esal.GLCODEID=?";
		try {
			pstmt = con.prepareStatement(sql2);
			pstmt.setString(1, aHeadId);
			pstmt.setString(2, aGlcodeId);
			ResultSet rset2 = pstmt.executeQuery();
			if(LOGGER.isDebugEnabled())     LOGGER.debug("sql2:" + sql2);
			if (rset2.next())
				result2 = 0;
			else
				result2 = 1;
		} catch (Exception ex) {
			LOGGER.error("error inside checkGlcodeIdUniqueUpdate"
					+ ex.getMessage(), ex);
			dc.addMessage("userFailure", "Duplicate Salary Head Not Allowed");
			throw new TaskFailedException(ex);
		}
		return result2;
	}

} // class
