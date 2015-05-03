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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.eGov.src.domain.Bank;
import com.exilant.eGov.src.domain.BankAccount;
import com.exilant.eGov.src.domain.BankBranch;
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
@Transactional(readOnly=true)
public class BankBranchAdd extends AbstractTask {

	private DataCollection dataCollection;
	private Connection con = null;
	private Query pstmt = null;
	private List<Object[]> rs = null;
	private String bankName;
	private int id;
	private static final Logger LOGGER = Logger.getLogger(BankBranchAdd.class);
	private static final String EGUSERID = "egUser_id";
	private static TaskFailedException taskExc;
	private BankBranchModify bankModify = new BankBranchModify();

	public BankBranchAdd() {
		super();
	}

	public void execute(String nameTask, String dataTask,
			DataCollection dataCollection, Connection conn,
			boolean errorOnNoData, boolean gridHasColumnHeading, String prefix)
			throws TaskFailedException {

		this.dataCollection = dataCollection;

		con = conn;
		// The below line is to Get DataBase Date and asigning that to Global
		// Variable dbDate;
		String[][] accountGrid = (String[][]) dataCollection
				.getGrid("bankAccountGrid");
		try {
			if (dataCollection.getValue("new_bank").equalsIgnoreCase("1"))
				postInBank(con);
			// else
			// throw new TaskFailedException("Invalid Data");
		    try{
		    	bankModify.verifyMICRUniqueness(con, dataCollection.getValue("bankBranch_branchMICR"), id);
		    }
		    catch(Exception ex){
		    	dataCollection.addMessage("userFailure",": "+dataCollection.getValue("bankBranch_branchMICR")+" already exist");
				throw taskExc;
		    }
			postInBankBranch();
			bankModify.verifyUniqueness(con, id, accountGrid, 2);
			if(dataCollection.getValue("bankBranch_addAccount").equals("1")){
				postInBankAcount();
			}
			dataCollection.addMessage("userSuccess", dataCollection.getValue(
					"bankBranch_branchName").toString(),
					"Branch Inserted Successfully");
			ChartOfAccounts.getInstance().reLoadAccountData();
		} catch (Exception ex) {
			LOGGER.error("inside execute" + ex.getMessage(), ex);
			dataCollection
					.addMessage("userFailure", "Branch Insertion Failure");
			throw taskExc;
		}
	}

	private void postInBankBranch() throws TaskFailedException, Exception {
		/*
		 * This Function Selecte the Id from Bank table for given BankCode And
		 * Also Insert The table BankBranch with data entered in the Screen Bank
		 * Branch - Add
		 */
		String strQry = "SELECT ID,NAME FROM Bank WHERE Code=?";
		BankBranch table = new BankBranch();
		// st=con.createStatement();
		String bank_code = dataCollection.getValue("bank_code").toString();
		if (bank_code == null || bank_code.length() == 0) {
			dataCollection.addMessage("userMessege",
					"Bank Code is must required field");
			throw new TaskFailedException("Bank Code cannot be empty.");
		}
		pstmt = HibernateUtil.getCurrentSession().createSQLQuery(strQry);
		pstmt.setString(1, bank_code);
		rs = pstmt.list();
		if(rs == null || rs.size() == 0){
			dataCollection.addMessage("userMessege", "Invalid Bank Code");
			throw new TaskFailedException("BankId Not Found");
		}
		String bankId = "";
		for(Object[] element : rs){
			bankId = element[0].toString();
		bankName = element[1].toString();
		}
		String isActive = dataCollection.getValue("bankBranch_isActive")
				.toString();
		if (isActive != null
				&& (isActive.compareToIgnoreCase("ON") == 0 || isActive
						.equals("1")))
			isActive = "1";
		else
			isActive = "0";

		String bankBranch_branchCode = dataCollection.getValue(
				"bankBranch_branchCode").toString();
		if (bankBranch_branchCode != null
				&& bankBranch_branchCode.length() != 0) {
			table.setBranchCode(bankBranch_branchCode);
		}

		else {
			dataCollection.addMessage("userMessege",
					"Please Fill the Branch Code");
			throw new TaskFailedException("Branch Code cannot be empty.");
		}
		String bankBranch_branchName = dataCollection.getValue(
				"bankBranch_branchName").toString();
		if (bankBranch_branchName != null
				&& bankBranch_branchName.length() != 0) {
			table.setBranchName(bankBranch_branchName);
		}

		else {
			dataCollection.addMessage("userMessege",
					"Please Fill the Branch Name");
			throw new TaskFailedException("Bank Name cannot be empty.");
		}
		String bankBranch_branchAddress1 = dataCollection.getValue(
				"bankBranch_branchAddress1").toString();
		if (bankBranch_branchAddress1 != null
				&& bankBranch_branchAddress1.length() != 0) {
			table.setBranchAddress1(bankBranch_branchAddress1);
		}

		else {
			dataCollection.addMessage("userMessege",
					"Please Fill the Branch Address");
			throw new TaskFailedException("Bank Address cannot be empty.");
		}

		String bankBranch_branchAddress2 = dataCollection.getValue(
				"bankBranch_branchAddress2").toString();
		if (bankBranch_branchAddress2 != null
				&& bankBranch_branchAddress2.length() != 0)
			table.setBranchAddress2(bankBranch_branchAddress2);

		String bankBranch_branchCity = dataCollection.getValue(
				"bankBranch_branchCity").toString();
		bankBranch_branchCity = formatString(bankBranch_branchCity);
		if (bankBranch_branchCity != null
				&& bankBranch_branchCity.length() != 0)
			table.setBranchCity(bankBranch_branchCity);

		String bankBranch_branchPin = dataCollection.getValue(
				"bankBranch_branchPin").toString();
		if (bankBranch_branchPin != null && bankBranch_branchPin.length() != 0)
			table.setBranchPin(bankBranch_branchPin);
		
		String bankBranch_branchMICR=dataCollection.getValue("bankBranch_branchMICR").toString();
		if(bankBranch_branchMICR!=null && bankBranch_branchMICR.length()!=0)
			table.setBranchMICR(bankBranch_branchMICR);

		String bankBranch_branchPhone = dataCollection.getValue(
				"bankBranch_branchPhone").toString();
		if (bankBranch_branchPhone != null
				&& bankBranch_branchPhone.length() != 0)
			table.setBranchPhone(bankBranch_branchPhone);

		String bankBranch_branchFax = dataCollection.getValue(
				"bankBranch_branchFax").toString();
		if (bankBranch_branchFax != null && bankBranch_branchFax.length() != 0)
			table.setBranchFax(bankBranch_branchFax);

		table.setBankId(bankId);
		String bankBranch_contactPerson = dataCollection.getValue(
				"bankBranch_contactPerson").toString();
		bankBranch_contactPerson = formatString(bankBranch_contactPerson);
		if (bankBranch_contactPerson != null
				&& bankBranch_contactPerson.length() != 0)
			table.setContactPerson(bankBranch_contactPerson);

		String bankBranch_narration = dataCollection.getValue(
				"bankBranch_narration").toString();
		bankBranch_narration = formatString(bankBranch_narration);
		if (bankBranch_narration != null && bankBranch_narration.length() != 0)
			table.setNarration(bankBranch_narration);

		table.setIsActive(isActive);
		table.setModifiedBy(dataCollection.getValue(EGUSERID));

		table.insert();
		id = Integer.parseInt(table.getId());
		dataCollection.addValue("bankBranch_branchId", Integer.toString(id));

	}

	private void postInBankAcount() throws TaskFailedException, Exception {
		/*
		 * This Function Selects ID from bankBranch Table for the Given BankCode
		 * Inserts that ID as BranchId in BankAccount table along with Data
		 * Enter in Bank Branch - New; Grid(accountGrid)
		 */
		String newGLCode = "", coaID = "";
		BankAccount table = new BankAccount();
		String[][] accountGrid = (String[][]) dataCollection
				.getGrid("bankAccountGrid");
		for (int i = 0; i < accountGrid.length; i++) {
			if (accountGrid[i][4].length() > 0
					&& accountGrid[i][5].length() > 0) {
				String accountStatus = "0";
				newGLCode = "";
				coaID = "";
				String code = EGovConfig.getProperty("egf_config.xml",
						"glcodeMaxLength", "", "AccountCode");
				if(LOGGER.isInfoEnabled())     LOGGER.info("code:" + code);
				newGLCode = bankModify.prepareBankAccCode(con,
						accountGrid[i][4], code);
				if(LOGGER.isInfoEnabled())     LOGGER.info("newGLCode ---->" + newGLCode);
				coaID = postInChartOfAccounts(newGLCode, accountGrid[i][4],
						accountGrid[i][0]);
				if(LOGGER.isInfoEnabled())     LOGGER.info("coaID ---->" + coaID);

				if (accountGrid[i][0] != null
						&& accountGrid[i][0].length() != 0
						&& accountGrid[i][1] != null
						&& accountGrid[i][1].length() != 0) {
					if (accountGrid[i][3] != null
							&& (accountGrid[i][3].compareToIgnoreCase("ON") == 0 || accountGrid[i][3]
									.equals("1")))
						accountStatus = "1";
					table.setBranchId(Integer.toString(id));
					table.setAccountNumber(accountGrid[i][0]);
					table.setAccountType(accountGrid[i][1]);
					table.setNarration(formatString(accountGrid[i][2]));
					table.setIsActive(accountStatus);
					table.setModifiedBy(dataCollection.getValue(EGUSERID));
					table.setPayTo(accountGrid[i][6]);
					table.setFundID(accountGrid[i][5]);
					table.setType(accountGrid[i][7]);
					if (coaID != null && coaID.length() > 0)
						table.setGlcodeID(coaID);
					table.insert();
				}
			}
		}
	}

	public String formatString(String data) {
		return data.replaceAll("'", "''");

	}

	public String postInChartOfAccounts(String glCode, String parentId,
			String accNumber) throws Exception {
		ChartOfAccts chart = new ChartOfAccts();
		if(LOGGER.isInfoEnabled())     LOGGER.info("entrd postInChartOfAccounts----");
		chart.setGLCode(glCode);
		chart.setName(bankName + " "
				+ dataCollection.getValue("bankBranch_branchName") + " "
				+ accNumber);
		chart.setDescription(bankName + " "
				+ dataCollection.getValue("bankBranch_branchName") + " "
				+ accNumber);
		chart.setParentId(parentId);
		chart.setType("A");
		chart.setClass("5"); // This is the leaf level number.
		chart.setClassification("4");
		chart.setModifiedBy(dataCollection.getValue(EGUSERID));
		chart.setIsActiveForPosting("1");
		chart.insert(con);
		return String.valueOf(chart.getId());
	}

	public void postInBank(Connection con) throws TaskFailedException,
			SQLException {
		String code = "";
		Bank bnk = new Bank();
		bnk.setCode(dataCollection.getValue("bank_code"));
		bnk.setName(dataCollection.getValue("bank_name"));
		bnk.setNarration(dataCollection.getValue("bank_narration"));
		bnk.setIsActive(dataCollection.getValue("bank_active"));
		bnk.setModifiedBy(dataCollection.getValue(EGUSERID));
		code = bnk.insert();
		dataCollection.addValue("bank_code", code);
	}

}
