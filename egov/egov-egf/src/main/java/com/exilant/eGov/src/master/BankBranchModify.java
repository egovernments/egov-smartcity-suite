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
 * Created on Dec 31, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.master;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.AccountChequeRange;
import com.exilant.eGov.src.domain.BankAccount;
import com.exilant.eGov.src.domain.BankBranch;
import com.exilant.eGov.src.domain.ChartOfAccountDetail;
import com.exilant.eGov.src.domain.ChartOfAccts;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author nagaraj.bhat
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
// This class updates data of BankBranch - New screen to BankBranch table. and
// Account datails to Bank Acoount

public class BankBranchModify extends AbstractTask {

	private DataCollection dc;
	private Connection con = null;
	private ResultSet rs = null;
	private String bankName, accDetailTypeId, oldGlCodeID = "",
			oldParentID = "";
	private int glId;
	private String bankAccountId = null;
	private static final Logger LOGGER = Logger
			.getLogger(BankBranchModify.class);
	private static final String BANKBRANCH_BRANCHID = "bankBranch_branchId";
	private static final String BANKBRANCH_BRANCHNAME = "bankBranch_branchName";
	private static final String EGUSER_ID = "egUser_id";
	private static final String USERMESSAGE = "userMessege";
	private static final String EXILERROR = "exilError";
	private static TaskFailedException taskExc;

	// private int id;
	public BankBranchModify() {
	}

	public void execute(String nameTask, String dataTask,
			DataCollection dataCollection, Connection conn,
			boolean errorOnNoData, boolean gridHasColumnHeading, String prefix)
			throws TaskFailedException {
		if(LOGGER.isInfoEnabled())     LOGGER.info("coming here..execute");
		dc = dataCollection;
		con = conn;
		boolean temp = false; /* This value is to Check the complition of Function */
		String[][] accountGrid = (String[][]) dc.getGrid("bankAccountGrid");
		String chequetableGrid[][];
		int id = dc.getInt(BANKBRANCH_BRANCHID);
		/*
		 * The below line is to Get DataBase Date and asigning that to Global
		 * Variable dbDate;
		 */

		try {
			verifyMICRUniqueness(con, dataCollection.getValue("bankBranch_branchMICR"), id);

			temp = postInBankBranch();
			if (temp != true) {
				dc.addMessage(USERMESSAGE, "Branch Modification Failed");
				throw new TaskFailedException("postInBankBranch Function Fails");
			}

			temp = false;
			if (accountGrid[0][0] == null
					|| accountGrid[0][0].equalsIgnoreCase("")) {
				verifyUniqueness(con, id, accountGrid, 2);
			}

            if(dc.getValue("bankBranch_modifyAccount").equals("1")){
                try {
                	temp=postInBankAcount();
    			} catch (Exception e) {
    				dc.addMessage(USERMESSAGE,e.getMessage());
    				throw new TaskFailedException("postInBankAcount Function Fails");
    			}
    			if(temp!=true){
    				dc.addMessage(USERMESSAGE,"Account Modification Failed");
    				throw new TaskFailedException("postInBankAcount Function Fails");
    			}
            }
			// adding method for adding chequeno range--rashmi
			/*
			 * temp=false; // commented by msahoo - removed cheque details from
			 * bank modify. chequetableGrid=(String
			 * [][])dc.getGrid("chequetableGrid");
			 * if(!(chequetableGrid[0][1]==null ||
			 * chequetableGrid[0][1].equalsIgnoreCase("")
			 * ||chequetableGrid[0][0].length()==0)) {
			 * temp=postInAccountCheques(); if(!temp) return; }
			 */
			dc.addMessage("userSuccess", dc.getValue(BANKBRANCH_BRANCHNAME)
					.toString(), "Branch Modified Successfully");
			ChartOfAccounts.getInstance().reLoadAccountData();
		} catch (Exception ex) {
			LOGGER.error("error in the date formate " + ex);
			dc.addMessage("userFailure", "Modification Failure");
			throw taskExc;
		}
	}

	private boolean postInBankBranch() throws Exception {
		/*
		 * This Function Selecte the Id from Bank table for given BankCode And
		 * Also Insert The table BankBranch with data entered in the Screen Bank
		 * Branch - Add
		 */
		PreparedStatement pstmt = null;
		BankBranch table = new BankBranch();
		String bank_code = dc.getValue("bank_code").toString();
		if (bank_code == null || bank_code.length() == 0) {
			dc.addMessage(USERMESSAGE, "Bank Code is Must Required Field");
			return false;
		}
		pstmt = con.prepareStatement("SELECT ID,NAME FROM Bank WHERE Code=?");
		pstmt.setString(1, bank_code);
		rs = pstmt.executeQuery();
		if (!rs.next()) {
			dc.addMessage(USERMESSAGE, "Invalid Bank code");
			throw new TaskFailedException("BankId Not Found");
		}

		String bankId = Integer.toString(rs.getInt(1));
		bankName = rs.getString(2); /* egf */
		rs.close();
		pstmt.close();

		String bankBranch_branchId = dc.getValue(BANKBRANCH_BRANCHID);
		if (bankBranch_branchId != null && bankBranch_branchId.length() != 0)
			table.setId(bankBranch_branchId);
		else {
			dc.addMessage(USERMESSAGE, "BranchId is must required field");
			return false;
		}

		String bankBranch_branchCode = dc.getValue("bankBranch_branchCode")
				.toString();
		if (bankBranch_branchCode != null
				&& bankBranch_branchCode.length() != 0)
			table.setBranchCode(bankBranch_branchCode);
		else {
			dc.addMessage(USERMESSAGE, "Please Fill the Branch Code");
			return false;
		}

		String bankBranch_branchName = dc.getValue(BANKBRANCH_BRANCHNAME)
				.toString();
		if (bankBranch_branchName != null
				&& bankBranch_branchName.length() != 0)
			table.setBranchName(bankBranch_branchName);
		else {
			dc.addMessage(USERMESSAGE, "Please Fill the Branch Name");
			return false;
		}

		String bankBranch_branchAddress1 = dc.getValue(
				"bankBranch_branchAddress1").toString();
		if (bankBranch_branchAddress1 != null
				&& bankBranch_branchAddress1.length() != 0)
			table.setBranchAddress1(bankBranch_branchAddress1);
		else {
			dc.addMessage(USERMESSAGE, "Please Fill the Branch Address");
			return false;
		}

		String bankBranch_branchAddress2 = dc.getValue(
				"bankBranch_branchAddress2").toString();
		if (bankBranch_branchAddress2 != null
				&& bankBranch_branchAddress2.length() != 0)
			table.setBranchAddress2(bankBranch_branchAddress2);
		else
			table.setBranchAddress2(" ");

		String bankBranch_branchCity = dc.getValue("bankBranch_branchCity")
				.toString();
		bankBranch_branchCity = formatString(bankBranch_branchCity);
		if (bankBranch_branchCity != null
				&& bankBranch_branchCity.length() != 0)
			table.setBranchCity(bankBranch_branchCity);
		else
			table.setBranchCity(" ");

		String bankBranch_branchPin = dc.getValue("bankBranch_branchPin")
				.toString();
		if (bankBranch_branchPin != null && bankBranch_branchPin.length() != 0)
			table.setBranchPin(bankBranch_branchPin);
		else
			table.setBranchPin(" ");
		
		String bankBranch_branchMICR=dc.getValue("bankBranch_branchMICR").toString();
		if(bankBranch_branchMICR!=null && bankBranch_branchMICR.length()!=0)
			table.setBranchMICR(bankBranch_branchMICR);

		String bankBranch_branchPhone = dc.getValue("bankBranch_branchPhone")
				.toString();
		if (bankBranch_branchPhone != null
				&& bankBranch_branchPhone.length() != 0)
			table.setBranchPhone(bankBranch_branchPhone);
		else
			table.setBranchPhone(" ");

		String bankBranch_branchFax = dc.getValue("bankBranch_branchFax")
				.toString();
		if (bankBranch_branchFax != null && bankBranch_branchFax.length() != 0)
			table.setBranchFax(bankBranch_branchFax);
		else
			table.setBranchFax(" ");

		table.setBankId(bankId);

		String bankBranch_contactPerson = dc.getValue(
				"bankBranch_contactPerson").toString();
		bankBranch_contactPerson = formatString(bankBranch_contactPerson);
		if (bankBranch_contactPerson != null
				&& bankBranch_contactPerson.length() != 0)
			table.setContactPerson(bankBranch_contactPerson);
		else
			table.setContactPerson(" ");

		String bankBranch_narration = dc.getValue("bankBranch_narration")
				.toString();
		bankBranch_narration = formatString(bankBranch_narration);
		if (bankBranch_narration != null && bankBranch_narration.length() != 0)
			table.setNarration(bankBranch_narration);
		else
			table.setNarration(" ");

		String isActive = dc.getValue("bankBranch_isActive").toString();
		if (isActive != null
				&& (isActive.compareToIgnoreCase("ON") == 0 || isActive
						.equals("1")))
			isActive = "1";
		else
			isActive = "0";
		table.setIsActive(isActive);
		table.setModifiedBy(dc.getValue(EGUSER_ID));

		table.update();

		return true;
	}

	// added by rashmi
	private boolean postInAccountCheques() throws Exception {
		AccountChequeRange acr = new AccountChequeRange();
		String chequetableGrid[][];
		chequetableGrid = (String[][]) dc.getGrid("chequetableGrid");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("after chequetableGrid+++" + chequetableGrid.length);
		if (chequetableGrid[0][0] == null
				|| chequetableGrid[0][0].equalsIgnoreCase("")
				|| chequetableGrid[0][0].length() == 0) {
			return false;
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("delete EGF_ACCOUNT_CHEQUES where bankaccountid="
				+ bankAccountId);
		for (int i = 0; i < chequetableGrid.length; i++) {
			if ((chequetableGrid[i][0].length() > 0 && chequetableGrid[i][1]
					.length() == 0)
					|| (chequetableGrid[i][0].length() == 0 && chequetableGrid[i][1]
							.length() > 0)) {
				dc.addMessage(EXILERROR,
						"Enter the from cheque number and to cheque number.");
				return false;
			}
			if (chequetableGrid[i][0].length() > 0
					&& chequetableGrid[i][1].length() > 0) {
				if (Integer.parseInt(chequetableGrid[i][0]) > Integer
						.parseInt(chequetableGrid[i][1])) {
					dc
							.addMessage(EXILERROR,
									" From cheque number should be less than To cheque number.");
					return false;
				}
			}
			acr.setBankAccountID(bankAccountId);
			acr.setFromChequeNumber(chequetableGrid[i][0]);
			acr.setToChequeNumber(chequetableGrid[i][1]);
			if (!chequetableGrid[i][3].equals(""))
				acr.setIsExhausted(chequetableGrid[i][3]);
			else
				acr.setIsExhausted("0");
			acr.setNextChqNo(chequetableGrid[i][4]);

			/* hardcoding to 1 for timebeing as only cash branch is present */
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Department received for row number " + i + 1 + "is "
					+ chequetableGrid[i][5]);
			if (!chequetableGrid[i][5].equals(""))
				acr.setIsAllotteTo(chequetableGrid[i][5]);
			else if (chequetableGrid[i][0].length() > 0
					&& chequetableGrid[i][1].length() > 0) {
				dc.addMessage(EXILERROR, " Please select a department");
				return false;
			}

			if (chequetableGrid[i][2].length() > 0) {
				try {
					String ReceivedDate = "";
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",
							Locale.getDefault());
					SimpleDateFormat formatter = new SimpleDateFormat(
							"dd-MMM-yyyy", Locale.getDefault());
					String vdt = (String) chequetableGrid[i][2];
					ReceivedDate = formatter.format(sdf.parse(vdt));
					acr.setReceivedDate(ReceivedDate);
					acr.setCreatedDate(formatter.format(new Date()));
					acr.setLastModifiedDate(formatter.format(new Date()));

				} catch (Exception e) {
					LOGGER.error("error in the date formate " + e);
					throw taskExc;
				}
			} else {
				EGovernCommon commonmethods = new EGovernCommon();
				String created = commonmethods.getCurrentDate();
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",
							Locale.getDefault());
					SimpleDateFormat formatter = new SimpleDateFormat(
							"dd-MMM-yyyy", Locale.getDefault());
					created = formatter.format(sdf.parse(created));
					acr.setReceivedDate(created);
					acr.setCreatedDate(formatter.format(new Date()));
					acr.setLastModifiedDate(formatter.format(new Date()));

				} catch (Exception e) {
					LOGGER.error("error in the date formate " + e);
					throw taskExc;
				}
			}
			String fromcheno = chequetableGrid[i][0];
			String tocheno = chequetableGrid[i][1];
			acr.setCreatedBy(dc.getValue("egUser_id"));
			acr.setLastModifiedBy(dc.getValue("egUser_id"));
			/*
			 * if(!isUniqueChequeRange(dc,
			 * con,bankAccountId,fromcheno,tocheno)){ throw taskExc; }
			 */
			try {
				if (fromcheno.length() > 0 && tocheno.length() > 0)

					acr.insert();
			} catch (SQLException sqlEx) {
				LOGGER.error("postInAccountCheques failed" + sqlEx);
				dc.addMessage(EXILERROR,
						"insertion failed in AccountChequeRange");
				throw taskExc;
			}
		}
		return true;
	}

	private boolean postInBankAcount() throws Exception {
		/*
		 * This Function Selects ID from bankBranch Table for the Given BankCode
		 * Inserts that ID as BranchId in BankAccount table along with Data
		 * Enter in Bank Branch - New; Grid(accountGrid)
		 */
		String newGLCode = "", coaID = "";
		String accType = "", glCode = "", oldFundId = "";
		int fundId, glcodeId;
		PreparedStatement pstmt = null;
		String str = "SELECT ID,ACCOUNTTYPE,GLCODEID,FUNDID,ACCOUNTNUMBER,PAYTO FROM BankAccount WHERE ID=?";
		BankAccount table = new BankAccount();
		String[][] accountGrid = (String[][]) dc.getGrid("bankAccountGrid");
		for (int i = 0; i < accountGrid.length; i++) {
			String accountStatus = "0";
			if (accountGrid[i][0] != null && accountGrid[i][0].length() != 0) {
				oldParentID = "";
				oldGlCodeID = "";
				/* egf */
				/*** Commenting for time being-Elzan ******/
				// if(isAlreadyPosted(accountGrid[i][0])){
				// throw new TaskFailedException();
				// }
				/* egf */

				// New
				try {
					pstmt = con.prepareStatement(str);
					pstmt.setString(1, accountGrid[i][0]);
					rs = pstmt.executeQuery();
					if (!rs.next()) {
						dc.addMessage(USERMESSAGE, "Invalid Bank code");
						throw new TaskFailedException("BankId Not Found");
					}
					accType = rs.getString(2);
					glcodeId = rs.getInt(3);
					fundId = rs.getInt(4);
					oldFundId = rs.getString(4);
					glId = glcodeId;
				} catch (Exception e) {
					LOGGER.error("Exception in postInBankAcount"
							+ e.getMessage());
					return false;
				} finally {
					try {
						rs.close();
						pstmt.close();
					} catch (Exception e) {
						LOGGER.error("Exception in postInBankAcount in finally"
								+ e.getMessage());
					}

				}
				if (accountGrid[i][0] != null
						&& accountGrid[i][0].length() != 0) {
					if ((!accountGrid[i][7].equals(oldFundId))
							|| (!accountGrid[i][2].equals(accType))) {
						if (isAlreadyPosted(accountGrid[i][0])) {
							throw new TaskFailedException();
						}
					}
				}

				try {

					pstmt = con
							.prepareStatement("SELECT GLCODE FROM CHARTOFACCOUNTS WHERE ID= ?");
					pstmt.setInt(1, glcodeId);
					LOGGER
							.debug("SELECT GLCODE FROM CHARTOFACCOUNTS WHERE ID='"
									+ glcodeId + "'");
					rs = pstmt.executeQuery();
					if (!rs.next()) {
						dc.addMessage(USERMESSAGE, "Invalid Bank code");
						throw new TaskFailedException("BankId Not Found");
					}
					glCode = rs.getString(1);
				} catch (Exception e) {
					LOGGER.error("Exception in postInBankAcount"
							+ e.getMessage());
					return false;
				} finally {
					try {
						rs.close();
						pstmt.close();
					} catch (Exception e) {
						LOGGER.error("Exception in postInBankAcount finallay"
								+ e.getMessage());
					}

				}
				// New End
				/* egf */
				if (accountGrid[i][1] != null
						&& accountGrid[i][1].length() != 0
						&& accountGrid[i][2] != null
						&& accountGrid[i][2].length() != 0
						&& accountGrid[i][4] != null
						&& accountGrid[i][4].length() != 0) {
					/* egf */
					newGLCode = "";
					coaID = "";
					if (!oldParentID.equalsIgnoreCase(accountGrid[i][5])) {
						if (!((accountGrid[i][2].equalsIgnoreCase(accType)) && (accountGrid[i][7]
								.equalsIgnoreCase(Integer.toString(fundId))))) {
							String code = EGovConfig.getProperty(
									"egf_config.xml", "glcodeMaxLength", "",
									"AccountCode");
							newGLCode = prepareBankAccCode(con,
									accountGrid[i][5], code);
							pstmt = con
									.prepareStatement("SELECT * FROM chartofaccounts WHERE glcode= ?");
							pstmt.setString(1, newGLCode);
							rs = pstmt.executeQuery();
							if (rs.next()) {
								throw new Exception(
										"bank account modification failed,can not  create account code,please contact adminstrator at egov.");
							}
							removeAccFromCOA(glcodeId);
						} else {
							newGLCode = glCode;

						}
						// removeAccFromCOA(glcodeId);
						coaID = postInChartOfAccounts(newGLCode,
								accountGrid[i][5], accountGrid[i][1]);
					} else {
						coaID = oldGlCodeID;
					}
					/* egf */
					String bankBranch_branchId = dc
							.getValue(BANKBRANCH_BRANCHID);
					if (bankBranch_branchId != null
							&& bankBranch_branchId.length() != 0)
						table.setBranchId(bankBranch_branchId);
					table.setAccountNumber(accountGrid[i][1]);
					table.setAccountType(accountGrid[i][2]);
					table.setNarration(formatString(accountGrid[i][3]));
					if (accountGrid[i][4].compareToIgnoreCase("ON") == 0
							|| accountGrid[i][4].equals("1"))
						accountStatus = "1";
					table.setIsActive(accountStatus);
					table.setModifiedBy(dc.getValue(EGUSER_ID));
					table.setId(accountGrid[i][0]);
					table.setGlcodeID(coaID);
					table.setFundID(accountGrid[i][7]);
					if(LOGGER.isInfoEnabled())     LOGGER.info("PayTo-------->" + accountGrid[i][8]);
					table.setPayTo(accountGrid[i][8]);
					table.setType(accountGrid[i][9]);
					try {
						table.update();

					} catch (Exception e) {
						LOGGER.error("error in update" + e.getMessage());
						dc.addMessage("exilRPError",
								"Error in update bankaccount" + e.getMessage());
					}
					String name = bankName + " "
							+ dc.getValue(BANKBRANCH_BRANCHNAME).toString()
							+ " " + accountGrid[0][1];
					updateVoucherDetail(con, name, glId);
					bankAccountId = String.valueOf(table.getId());
					// String detailKey=accountGrid[i][0];
				} else
					continue;
			} else {
				if (accountGrid[i][1] != null
						&& accountGrid[i][1].length() != 0
						&& accountGrid[i][2] != null
						&& accountGrid[i][2].length() != 0
						&& accountGrid[i][4] != null
						&& accountGrid[i][4].length() != 0) {
					// if(LOGGER.isDebugEnabled())     LOGGER.debug("TwoglCode:"+glCode);
					String code = EGovConfig.getProperty("egf_config.xml",
							"glcodeMaxLength", "", "AccountCode");
					if(LOGGER.isDebugEnabled())     LOGGER.debug("code:" + code);
					newGLCode = prepareBankAccCode(con, accountGrid[i][5], code);
					coaID = postInChartOfAccounts(newGLCode, accountGrid[i][5],
							accountGrid[i][1]);
					if (accountGrid[i][4].compareToIgnoreCase("ON") == 0
							|| accountGrid[i][4].equals("1"))
						accountStatus = "1";

					String bankBranch_branchId = dc
							.getValue(BANKBRANCH_BRANCHID);
					if (bankBranch_branchId != null
							&& bankBranch_branchId.length() != 0)
						table.setBranchId(bankBranch_branchId);

					table.setAccountNumber(accountGrid[i][1]);
					table.setAccountType(accountGrid[i][2]);
					String accountGridnarration = formatString(accountGrid[i][3]);
					table.setNarration(accountGridnarration);
					table.setIsActive(accountStatus);
					table.setModifiedBy(dc.getValue(EGUSER_ID));
					table.setFundID(accountGrid[i][7]);
					table.setPayTo(accountGrid[i][8]);
					table.setType(accountGrid[i][9]);
					if (coaID != null && coaID.length() > 0)
						table.setGlcodeID(coaID);
					else
						table.setGlcodeID("0");

					table.insert();
					bankAccountId = String.valueOf(table.getId());
				} else
					continue;
			}
		}
		return true;
	}

	public String formatString(String data) {
		String formattedData = data.replaceAll("'", "''");
		if(LOGGER.isDebugEnabled())     LOGGER.debug(formattedData);
		return formattedData;
	}

	/**
	 * This function will check if the account details can be modified. If
	 * opening balance>0 then it should restrict modification Restrict
	 * modification if valid vouchers are present.
	 * 
	 * @param bankAccID
	 * @return
	 * @throws Exception
	 */
	public boolean isAlreadyPosted(String bankAccID) throws Exception {
		boolean isPosted = false;
		PreparedStatement pstmt = null;
		BigDecimal netAmt = BigDecimal.ZERO;
		pstmt = con
				.prepareStatement("select gl.glcode,gl.glcodeid from bankaccount ba,generalledger gl, VOUCHERHEADER vh  where ba.glcodeid=gl.glcodeid and ba.id=? AND gl.voucherheaderid=vh.id and vh.STATUS<>4");
		pstmt.setString(1, bankAccID);
		ResultSet rset = pstmt.executeQuery();
		if (rset.next()) { // if it is posted get1 old glcodeid from
							// generalledger table as they might have changed in
							// modify
			dc.addMessage("exilRPError", "Code :" + rset.getString(1)
					+ " Is Used For Posting Cant Modify");
			oldGlCodeID = rset.getString(2);
			isPosted = true;
		} else { // if it is not posted
			// if(LOGGER.isDebugEnabled())     LOGGER.debug("inside the isAlreadyPosted else cond");
			rset.close();
			pstmt = con
					.prepareStatement("SELECT SUM(openingdebitbalance)+SUM(openingcreditbalance),ba.accountnumber as accno FROM bankaccount ba,transactionSummary ts WHERE ba.glcodeid=ts.glcodeid AND ba.id=? GROUP BY ba.accountnumber");
			pstmt.setString(1, bankAccID);
			rset = pstmt.executeQuery();
			if (rset.next())
				netAmt = rset.getBigDecimal(1);
			if (netAmt.compareTo(BigDecimal.ZERO) == 1) { // if it is posted
															// find out whether
															// OB has been set
				dc
						.addMessage("exilRPError", "Cannot Modify A/C "
								+ rset.getString(2)
								+ " as opening balance is present.");
				isPosted = true;
			} else {// if it is neither posted nor set Opening Balance get old
					// glcodeid from bankaccount table as they might have
					// changed in modify
				rset.close();
				pstmt = con
						.prepareStatement("select ba.glcodeid,ca.parentid from bankaccount ba,chartofaccounts ca  where ba.glcodeid=ca.id and ba.id=?");
				pstmt.setString(1, bankAccID);
				rset = pstmt.executeQuery();
				if (rset.next()) {
					oldGlCodeID = rset.getString(1);
					oldParentID = rset.getString(2);
				}
			}
		}
		pstmt.close();
		return isPosted;
	}

	public void removeFromCOA(String glCodeID, String detTypeID, String defKeyID)
			throws Exception {
		PreparedStatement pstm = null;
		PreparedStatement pstm1 = null;
		String qry1 = "delete chartofaccountdetail where glcodeid=? and detailtypeid=?";
		String qry2 = "delete chartofaccounts where id=?";
		pstm = con.prepareStatement(qry1);
		pstm.setString(1, glCodeID);
		pstm.setString(2, detTypeID);
		pstm.executeQuery(qry1);

		pstm1 = con.prepareStatement(qry2);
		pstm1 = con.prepareStatement(qry2);
		pstm1.setString(1, glCodeID);
		pstm1.close();
		pstm.close();
	}

	public void removeAccFromCOA(int id) throws TaskFailedException {
		PreparedStatement psmt = null;
		try {
			String qry = "delete chartofaccounts where id=?";
			psmt = con.prepareStatement(qry);
			psmt.setInt(1, id);
			psmt.execute();
		} catch (Exception e) {
			LOGGER.error("Error in removeAccFromCOA :" + e.getMessage());
			throw taskExc;
		} finally {
			try {
				rs.close();
				psmt.close();
			} catch (Exception e) {
				LOGGER.error("Error in removeAccFromCOA finally"
						+ e.getMessage());
			}

		}
	}

	public String prepareBankAccCode(Connection con, String accID, String code)
			throws Exception {
		String glCode = "";
		Long glcode;
		Long tempCode = 0L;
		PreparedStatement pstmt = null;
		pstmt = con
				.prepareStatement("select glcode from chartofaccounts where glcode like (select glcode from chartofaccounts where id=?) order by glcode desc");
		pstmt.setString(1, accID);
		ResultSet rset = pstmt.executeQuery();
		while (rset.next()) {
			glCode = rset.getString(1);
		}
		String subminorvalue = EGovConfig.getProperty("egf_config.xml",
				"subminorvalue", "", "AccountCode");
		glCode = glCode.substring(0, Integer.parseInt(subminorvalue));
		pstmt = con
				.prepareStatement("select glcode from chartofaccounts where glcode like ? || '%' order by glcode desc");
		pstmt.setString(1, glCode);
		rset = pstmt.executeQuery();
		if (rset.next()) {
			glCode = rset.getString(1);
		}
		String zero = EGovConfig.getProperty("egf_config.xml", "zerofill", "",
				"AccountCode");
		if (glCode.length() == Integer.parseInt(code)) {
			glcode = Long.parseLong(glCode);
			tempCode = glcode + 1;
		} else {
			glCode = glCode + zero;
			glcode = Long.parseLong(glCode);
			tempCode = glcode + 1;
		}
		glCode = Long.toString(tempCode);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("glCode ----->" + glCode);
		return glCode;
	}

	public String postInChartOfAccounts(String glCode, String parentId,
			String accNumber) throws Exception {
		PreparedStatement pstmt = null;
		ChartOfAccts chart = new ChartOfAccts();
		chart.setGLCode(glCode);
		chart.setName(bankName + " " + dc.getValue(BANKBRANCH_BRANCHNAME) + " "
				+ accNumber);
		chart.setDescription(bankName + " "
				+ dc.getValue(BANKBRANCH_BRANCHNAME) + " " + accNumber);
		chart.setIsActiveForPosting("1");
		chart.setParentId(parentId);
		chart.setType("A");
		chart.setClass("5"); // This is the leaf level number.
		chart.setClassification("4");
		chart.setModifiedBy(dc.getValue(EGUSER_ID));
		pstmt = con
				.prepareStatement("SELECT * FROM chartofaccounts WHERE glcode= ?");
		pstmt.setString(1, glCode);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			// if(LOGGER.isDebugEnabled())     LOGGER.debug("id is present ie inside the update function");
			chart.setId(rs.getInt("id") + "");
			chart.update();
			// throw new
			// Exception("bank account modification failed,can not  create account code,please contact adminstrator at egov.");
		} else {
			// if(LOGGER.isDebugEnabled())     LOGGER.debug("id is not present ie inside the insert function");
			chart.insert(con);
		}
		// chart.insert(con);
		return String.valueOf(chart.getId());
	}

	public void postInCOADetail(String coaID, String defaultID, String type)
			throws Exception {
		ChartOfAccountDetail cDet = new ChartOfAccountDetail();
		cDet.setDetailTypeId(accDetailTypeId);
		cDet.setGLCodeId(coaID);
		if (type.equals("insert")) {
			cDet.insert();
		} else {
			cDet.setId(getCOADetID(coaID, accDetailTypeId, defaultID));
			cDet.update();
		}
	}

	// check for uniqueness --by rashmi
	public boolean isUniqueChequeRange(DataCollection dc, Connection conn,
			String bankaccid, String fromcheno, String tocheno)
			throws TaskFailedException {
		PreparedStatement pstmt = null;
		boolean isUnique = false;
		String strQry = "SELECT id FROM EGF_ACCOUNT_CHEQUES WHERE BANKACCOUNTID = ? AND ((FROMCHEQUENUMBER<= ?   AND ? <= TOCHEQUENUMBER) or (FROMCHEQUENUMBER<= ?  AND ? <= TOCHEQUENUMBER)) AND length(FROMCHEQUENUMBER)=length(?) and length(TOCHEQUENUMBER)=length(?)";
		try {
			LOGGER
					.debug("SELECT id FROM EGF_ACCOUNT_CHEQUES WHERE BANKACCOUNTID = '"
							+ bankaccid
							+ "' AND ((FROMCHEQUENUMBER<='"
							+ fromcheno
							+ "' AND '"
							+ fromcheno
							+ "'<= TOCHEQUENUMBER) or ( (FROMCHEQUENUMBER<='"
							+ tocheno
							+ "' AND '"
							+ tocheno
							+ "'<= TOCHEQUENUMBER)");
			pstmt = con.prepareStatement(strQry);
			pstmt.setString(1, bankaccid);
			pstmt.setString(2, fromcheno);
			pstmt.setString(3, fromcheno);
			pstmt.setString(4, tocheno);
			pstmt.setString(5, tocheno);
			pstmt.setString(6, fromcheno);
			pstmt.setString(7, fromcheno);

			ResultSet rs = pstmt.executeQuery();
			// if(LOGGER.isDebugEnabled())     LOGGER.debug("after the query--isUniqueChequeRange");
			if (rs.next())
				dc.addMessage("exilRPError", "duplicate ChequeRange");

			else
				isUnique = true;
			rs.close();
			pstmt.close();
		} catch (SQLException ex) {
			LOGGER.error("isUniqueChequeRange  failed" + ex);
			dc.addMessage("exilRPError",
					"DataBase Error(isUniqueChequeRange) : " + ex.toString());
			throw taskExc;
		}
		return isUnique;
	}

	public String getCOADetID(String coaID, String accDetailTypeId,
			String defaultID) throws Exception {
		PreparedStatement pstmt = null;
		pstmt = con
				.prepareStatement("select id from chartofaccountdetail where glcodeid=? and detailtypeid=?");
		pstmt.setString(1, coaID);
		pstmt.setString(2, accDetailTypeId);
		ResultSet rset = pstmt.executeQuery();
		if (rset.next()) { // if it is posted get old glcodeid from
							// generalledger table as they might have changed in
							// modify
			pstmt.close();
			return rset.getString(1);
		}
		pstmt.close();
		return null;
	}

	public void verifyUniqueness(Connection con, int field1, String field2[][],
			int type) throws TaskFailedException, SQLException {
		if(LOGGER.isInfoEnabled())     LOGGER.info("verifyUniqueness:");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String accNo = "";
		String brchName = "";
		if (type == 1) {
			pstmt = con
					.prepareStatement("select BranchName from bankbranch where branchid= ?");
			pstmt.setInt(1, field1);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				brchName = rs.getString(1);
				if (brchName.equalsIgnoreCase(field2[0][0])) {
					dc.addMessage(EXILERROR, ": Duplicate Branch Name :"
							+ field2[0][0]);
					throw new TaskFailedException();
				}
			}
		}
		if (type == 2) {
			pstmt = con
					.prepareStatement("select accountNumber from bankaccount where branchid=?");
			pstmt.setInt(1, field1);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				accNo = rs.getString(1);
				for (int i = 0; i < field2.length; i++) {
					if(LOGGER.isInfoEnabled())     LOGGER.info("accountNumber:" + accNo + "ACC: "
							+ field2[i][1]);
					if (accNo.equalsIgnoreCase(field2[i][1])) {
						dc.addMessage(EXILERROR, ": " + field2[i][1]
								+ " is a Duplicate Account Number");
						throw new TaskFailedException();
					}
				}
			}
			rs.close();
		}
	}
	
    public void verifyMICRUniqueness(Connection con, String micr, int id)throws TaskFailedException,SQLException
    {
     if(LOGGER.isInfoEnabled())     LOGGER.info("verifyMICRUniqueness:");
   if(micr!=null && !micr.isEmpty())
   {
     int abc = id;
        PreparedStatement pst=null;
        ResultSet rs=null;
        if(id == 0){
        	String qry = "select MICR from bankbranch where MICR="+micr;
        	pst=con.prepareStatement(qry);
       	 	rs=pst.executeQuery();
        }
        else{
        	String qry = "select MICR from bankbranch where MICR="+micr+" and id<>"+id;
        	pst = con.prepareStatement(qry);
        	rs=pst.executeQuery();
        }
        if(rs.next()){
        	if(rs.getString(1)!=null )
        	{
            dc.addMessage(EXILERROR,": "+micr+" already exist");
            throw new TaskFailedException();
        	}
        }
        rs.close();
   }
     }  

	public void updateVoucherDetail(Connection con, String name, int glId)
			throws TaskFailedException, SQLException {
		try {
			PreparedStatement pstmt = null;

			String query = "update voucherdetail set accountname=? where glcode=(select glcode from chartofaccounts where id=?)";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setInt(2, glId);
			if(LOGGER.isDebugEnabled())     LOGGER.debug("voucherdetail query:" + query);
			pstmt.executeUpdate();

		} catch (SQLException ex) {
			LOGGER.error("Voucherdetail updation failed" + ex);
			dc.addMessage(EXILERROR, ":Voucherdetail updation failed");
			throw taskExc;
		}
	}

}
