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
/**
 * Created on Jan 16 , 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.transactions;

import com.exilant.exility.common.*;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import com.exilant.eGov.src.common.*;
import org.apache.log4j.Logger;

public class OpeningBalance extends AbstractTask{
	 private Connection connection;
	 private PreparedStatement pst;
	 private ResultSet resultset;
	 private DataCollection dc;
	 private TaskFailedException taskExc;
	 private String extraMessage;
	 private static final Logger LOGGER=Logger.getLogger(OpeningBalance.class);
     EGovernCommon cm=new EGovernCommon();
     String lastModifiedDate;
	
	 public void execute(String taksName,
	  							String gridName,
								DataCollection dc,
								Connection conn,
								boolean erroOrNoData,
								boolean gridHasColumnHeading, String prefix) throws TaskFailedException{
	   		this.connection = conn;
	   		this.dc = dc;
			taskExc = new TaskFailedException();
			//String glCod = dc.getValue("chartOfAccounts_glCode");
            String gridOpeningBalance[][] = dc.getGrid("gridOpeningBalance");
			String serviceType = dc.getValue("ftService_type");
			
			 //set lastmodified date
            lastModifiedDate = cm.getCurrentDateTime(connection);
		   	try{
		   		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
				lastModifiedDate = formatter.format(sdf.parse(lastModifiedDate));
				lastModifiedDate=" to_date('"+lastModifiedDate+"','dd-Mon-yyyy HH24:MI:SS')";
			   	}catch(Exception e){
					LOGGER.error("ERROR in the date formate "+e);
					throw new TaskFailedException();
				}
			   	
            for(int i=0; i<gridOpeningBalance.length; i++){
                if(gridOpeningBalance[i][1].equalsIgnoreCase("")) continue;
    			try{
    				String query="select glcode as \"glcode\",classification as \"classification\",isactiveforposting as \"isactiveforposting\",type as \"type\" from chartofaccounts where glcode = ? and type= ?";
    				pst  = connection.prepareStatement(query);
    				pst.setString(1, gridOpeningBalance[i][1]);
    				pst.setString(2, serviceType);
    				if(LOGGER.isDebugEnabled())     LOGGER.debug(query);
    				resultset = pst.executeQuery();
    				if(resultset.next()){
    					if(!resultset.getString("classification").equalsIgnoreCase("4"))
    					{
    			   			dc.addMessage("exilError","Account code should be a Detailed Code");
    			   			throw new TaskFailedException();
    					}
    					else if(!resultset.getString("isactiveforposting").equalsIgnoreCase("1"))
    					{
    			   			dc.addMessage("exilError","Account code is not open for Posting");
    			   			throw new TaskFailedException();
    					}
    				}
    				else{
    					if(dc.getValue("ftService_type").equalsIgnoreCase("L") ||dc.getValue("ftService_type").equalsIgnoreCase("A"))
    					{
    						if(dc.getValue("ftService_type").equalsIgnoreCase("A"))
    							serviceType= "Asset Type";
    						if(dc.getValue("ftService_type").equalsIgnoreCase("L"))
    							serviceType= "Liability Type";
    			   			dc.addMessage("exilError","Account Code should be of "+serviceType);
    			   			throw new TaskFailedException();
    					}
    				}
    			}
    			catch(Exception e){
    				LOGGER.error("Exception while validating Glcode "+e);
    				throw new TaskFailedException("Invalid Account Code");
    			}
           
				String mode = isOpeniningBalanceSet(conn,dc,i);
				if(LOGGER.isInfoEnabled())     LOGGER.info("mode:"+mode);
				if(mode.equalsIgnoreCase("set")){
					if(!setOpeningBalance(dc,i))
						throw taskExc;
				}else if(mode.equalsIgnoreCase("update")){
					if(!updateOpeningBalance(i))
						throw taskExc;
				}
            }

			dc.addMessage("eGovSuccess","Opening Balance");
			//if(extraMessage.length()>1) dc.addMessage("eGovSuccess", extraMessage);
	 }
	 private boolean setOpeningBalance(DataCollection dc,int index) throws TaskFailedException{
	 	boolean status=false;
	 	String fundSourceId=dc.getValue("fundSourceId");
	 	
		if (fundSourceId.trim().equals(""))
			fundSourceId=null;
		if(LOGGER.isDebugEnabled())     LOGGER.debug("inside setOpeningBalance-------------------");
	 	String fundId = dc.getValue("fund_id");
		String financialYearId = dc.getValue("financialYear_id");
		String deptId = dc.getValue("dept_name");
		String detailTypeId = "0", detailKey = "0";
		String id="", query="", dr="0", cr="0";
		String gridOpeningBalance[][] = dc.getGrid("gridOpeningBalance");
		extraMessage = "";
		if(LOGGER.isDebugEnabled())     LOGGER.debug("<<<<<<<<<<<<<department object>>>>>>>"+deptId);
		if(!isFYOpen(financialYearId)){
			dc.addMessage("exilError","Financial Year is Closed, Opening Balance can not be updated");
			throw taskExc;
		}
		try{
			String chkEntry="";
			int i=index;
				gridOpeningBalance[i][3] = gridOpeningBalance[i][3].equalsIgnoreCase("")?"0":gridOpeningBalance[i][3];
				gridOpeningBalance[i][4] = gridOpeningBalance[i][4].equalsIgnoreCase("")?"0":gridOpeningBalance[i][4];
				detailTypeId = gridOpeningBalance[i][5];
				detailKey = gridOpeningBalance[i][6];

				if(detailTypeId.equalsIgnoreCase("")){
					detailTypeId = null;

				}
				if(detailKey.equalsIgnoreCase("")){
					detailKey = null;
				}
				String fsCond="";
				if(fundSourceId==null)
					fsCond=" AND fundSourceId is null";
				else
					fsCond=" AND fundSourceId="+ fundSourceId;
				chkEntry = "SELECT id FROM transactionSummary WHERE glCodeId= ?" +
								"AND fundId= ? AND financialYearId= ? " +
									"AND accountDetailTypeId= ? AND accountDetailKey= ?"+fsCond+
								" and DEPARTMENTID=?";
									
				pst  = connection.prepareStatement(chkEntry);
				pst.setString(1, gridOpeningBalance[i][0]);
				pst.setString(2, fundId);
				pst.setString(3, financialYearId);
				pst.setString(4, detailTypeId);
				pst.setString(5, detailKey);
				pst.setString(6, deptId);
				resultset = null;
				if(LOGGER.isDebugEnabled())     LOGGER.debug("chkEntry  "+chkEntry);
				resultset = pst.executeQuery();
				query = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid, " +
						"openingdebitbalance, openingcreditbalance, debitamount, " +
						"creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId,fundsourceid,DEPARTMENTID,LASTMODIFIEDBY,LASTMODIFIEDDATE,NARRATION) " +
						"VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, "+lastModifiedDate+", ?)";
				if(LOGGER.isInfoEnabled())     LOGGER.info("query  "+query);
				PreparedStatement pstBatch = connection.prepareStatement(query);
				if(resultset.next())
					extraMessage = extraMessage + gridOpeningBalance[i][1] + ", ";	
				else{
					/** Checking the Bank account 
					 * If its an asset code then only we need to check
					 * */
					if(dc.getValue("ftService_type").equals("A"))
					{
						if(LOGGER.isDebugEnabled())     LOGGER.debug("Need to check bank accounts.");
						String queryFundId = "select fundid from bankaccount where glcodeid= ?";
						pst = connection.prepareStatement(queryFundId);
						pst.setString(1, gridOpeningBalance[i][0]);
						ResultSet rs=pst.executeQuery();
						if(rs.next())
						{
							//Check if the funds are same
							String bankFund=rs.getString(1);
							if(LOGGER.isDebugEnabled())     LOGGER.debug("Need to check bank accounts.");
							if(!bankFund.equalsIgnoreCase(fundId)){
								dc.addMessage("exilError","Fund associated to the Bank Account is incorrect");
								throw new TaskFailedException("Fund associated to the Bank Account is incorrect,");
							}
						}
					}
					
								
					id = String.valueOf(PrimaryKeyGenerator.getNextKey("TransactionSummary"));
					pstBatch.setString(1, id);
					pstBatch.setString(2, financialYearId);
					pstBatch.setString(3, gridOpeningBalance[i][0]);
					pstBatch.setString(4, gridOpeningBalance[i][3]);
					pstBatch.setString(5, gridOpeningBalance[i][4]);
					pstBatch.setString(6, dr);
					pstBatch.setString(7, cr);
					pstBatch.setString(8, detailTypeId);
					pstBatch.setString(9, detailKey);
					pstBatch.setString(10, fundId);
					pstBatch.setString(11, fundSourceId);
					pstBatch.setString(12, deptId);
					pstBatch.setString(13, dc.getValue("current_UserID"));
					pstBatch.setString(14, gridOpeningBalance[i][8]);
					pstBatch.addBatch();
				}
				/** if executeBatch Fails throw exception **/
				int updateCounts[] = pstBatch.executeBatch();
				for(int cnt=0; cnt<updateCounts.length;cnt++)
					if(updateCounts[cnt] == 0)
					{
						dc.addMessage("eGovFailure","Updating Opening Balance (Execute Batch)");
						return false;
					}
				/** *********************************** **/

			if(extraMessage.length()>3)
				extraMessage = "Opening Balance Already set for Account Codes: " + extraMessage;

			resultset.close();
			pst.close();
			pstBatch.close();
			status = true;
		}catch(SQLException ex){
			LOGGER.error("ERROR OpeningBalance: " + ex.toString());
			status = false;
		}
	 	return status;
	 }
	 
	 
	 private boolean updateOpeningBalance(int index) throws TaskFailedException{
	 	boolean status=false;
	 	//String fundSourceId=dc.getValue("fundSourceId");
	 //	String fundId = dc.getValue("fund_id");
		String financialYearId = dc.getValue("financialYear_id"), nextFYId="";
		//String detailTypeId = "0", detailKey = "0";;
	//	String id="", query="", accEntityCondition="", dr="0", cr="0";
		String gridOpeningBalance[][] = dc.getGrid("gridOpeningBalance");
		extraMessage = "";
		if(!isFYOpen(financialYearId)){
			dc.addMessage("exilError","Financial Year is Closed, Opening Balance can not be updated");
			throw taskExc;
		}
		try{
			String updateQuery="";
			String glCodes="";
			int i=index;
				gridOpeningBalance[i][3] = gridOpeningBalance[i][3].equalsIgnoreCase("")?"0":gridOpeningBalance[i][3];
				gridOpeningBalance[i][4] = gridOpeningBalance[i][4].equalsIgnoreCase("")?"0":gridOpeningBalance[i][4];
				glCodes = glCodes + "'" + gridOpeningBalance[i][1] + "',";
				/*chkEntry = "SELECT id FROM transactionSummary WHERE glCodeId="+gridOpeningBalance[i][0]+" " +
								"AND fundId="+fundId+" AND financialYearId="+financialYearId + accEntityCondition;
							*/
				updateQuery = "UPDATE transactionSummary SET openingdebitbalance= ?, " +
									"openingcreditbalance= ?, "+
									" LASTMODIFIEDBY= ?, "+
									" LASTMODIFIEDDATE= "+lastModifiedDate+", "+
									" NARRATION= ?" +
									" WHERE id= ?";
				PreparedStatement pstBatch = connection.prepareStatement(updateQuery);

				if(LOGGER.isInfoEnabled())     LOGGER.info("updateQuery  "+updateQuery);
				pstBatch.setString(1, gridOpeningBalance[i][3]);
				pstBatch.setString(2, gridOpeningBalance[i][4]);
				pstBatch.setString(3, dc.getValue("current_UserID"));
				pstBatch.setString(4, gridOpeningBalance[i][8]);
				pstBatch.setString(5, gridOpeningBalance[i][7]);
				pstBatch.addBatch();

			/** if executeBatch Fails throw exception **/
			int updateCounts[] = pstBatch.executeBatch();
			for(int cnt=0; cnt<updateCounts.length;cnt++)
				if(updateCounts[cnt] == 0)
				{
					dc.addMessage("eGovFailure","Update Opening Balance (Execute Batch)");
					return false;
				}
			/** *********************************** **/
			glCodes = glCodes.substring(0, glCodes.length()-1);
			status = true;
		}catch(SQLException ex){
			LOGGER.error("ERROR OpeningBalance: " + ex.toString());
			dc.addMessage("eGovFailure","Opening Balance");
			status = false;
		}
	 	return status;
	 }
	 private boolean calcClosingBalance(String fyId, String fundId, String glCodeIds) throws TaskFailedException{
		boolean isDone = false;
		FY fy = getFinancialYear(fyId);
		String sDate=fy.getSDate();
		String eDate=fy.getEDate();
		String nextFYId = getNextFYId(fyId);

		String clBalQuery = "";
		try{
			//Closing Balance For glCode-Fund-FY
			clBalQuery = "SELECT gl.glcodeId, vh.fundId, sum(debitAmount) AS \"dr\", " +
							"sum(creditAmount) AS \"cr\", (sum(debitAmount) - sum(creditAmount)) AS \"balance\" " +
							"FROM voucherHeader vh, chartOfAccounts coa, generalledger gl " +
							"WHERE vh.id = gl.voucherHeaderId AND gl.glCode=coa.glcode " +
							"AND voucherDate >= '"+sDate+"' AND voucherDate <= '"+eDate+"' " +
							"AND vh.fundId= ? AND coa.id IN ("+glCodeIds+") " +
							"GROUP BY gl.glcodeId,vh.fundId";
			pst = connection.prepareStatement(clBalQuery);
			pst.setString(1, fundId);
			resultset = pst.executeQuery();

			String id="",query="";
			double bal=0, opDr=0, opCr=0, dr=0, cr=0;
			if(nextFYId.equalsIgnoreCase("")){
				return true;
			}
			int i=0;
			query = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid, " +
			"openingdebitbalance, openingcreditbalance, debitamount, " +
			"creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId) " +
			"VALUES ( ?, ?, ?, ?, ?, ?, ?, 0, 0, ?)";
			PreparedStatement pstBatch = connection.prepareStatement(query);
			while(resultset.next()){
				id = String.valueOf(PrimaryKeyGenerator.getNextKey("TransactionSummary"));
				bal = resultset.getDouble("balance");
				opDr = bal>0?bal:0;
				opCr = bal<0?Math.abs(bal):0;
				pstBatch.setString(1, id);
				pstBatch.setString(2, nextFYId);
				pstBatch.setInt(3, resultset.getInt("glCodeId"));
				pstBatch.setDouble(4, opDr);
				pstBatch.setDouble(5, opCr);
				pstBatch.setDouble(6, dr);
				pstBatch.setDouble(7, cr);
				pstBatch.setInt(8, resultset.getInt("fundId"));
				pstBatch.addBatch();
			}
			String sqlquery = "DELETE transactionSummary WHERE financialYearId= ? " +
			"AND fundId= ? AND glCodeId IN ("+glCodeIds+")";
			pst = connection.prepareStatement(sqlquery);
			pst.setString(1, nextFYId);
			pst.setString(2, fundId);
			pst.executeUpdate();
			int updateCounts[] = pstBatch.executeBatch();
			for(i=0; i<updateCounts.length;i++){
				if(updateCounts[i] == 0){
					dc.addMessage("exilError","Batch Execute Failed");
					throw taskExc;
				}
			}

			/** Calculating current year openingBalance+ClosingBalance for next year's OpeningBalance **/
			/** ********************************* **/
			resultset = null;
			String sqlQuery = "SELECT cl.id AS \"clId\", op.id AS \"opId\", " +
			"cl.openingDebitBalance+op.openingDebitBalance AS \"dr\", " +
			"cl.openingCreditBalance+op.openingCreditBalance AS \"cr\" " +
			"FROM transactionSummary cl, transactionSummary op " +
			"WHERE cl.financialYearId = ? AND op.financialYearId = ? " +
			"AND cl.glCodeId = op.glCodeId AND cl.fundId = op.fundId " +
			"AND cl.accountDetailTypeId = op.accountDetailTypeId " +
			"AND cl.accountDetailKey = op.accountDetailKey " +
			"AND cl.fundId= ? AND cl.glCodeId IN ("+glCodeIds+")";
			pst = connection.prepareStatement(sqlQuery);
			pst.setString(1, fyId);
			pst.setString(2, nextFYId);
			pst.setString(3, fundId);
			resultset = pst.executeQuery();
			pstBatch.clearBatch();
			String clIds="";
			query = "UPDATE transactionSummary SET openingDebitBalance = ?, " +
			"openingCreditBalance = ? WHERE id = ?";
			pstBatch = connection.prepareStatement(query);
			while(resultset.next()){
				clIds = clIds + resultset.getString("clId") + ",";
				dr = resultset.getDouble("dr");
				cr = resultset.getDouble("cr");
				bal = dr-cr;
				dr = bal>0?bal:0;
				cr = bal<0?Math.abs(bal):0;
				pstBatch.setDouble(1, dr);
				pstBatch.setDouble(2, cr);
				pstBatch.setString(3, resultset.getString("opId"));
				pstBatch.addBatch();
			}
			pstBatch.executeBatch();
			if(clIds.length()>0)
				clIds = clIds.substring(0, clIds.length()-1);
			else
				clIds = "0";

			resultset = null;
			String sqlQueryString = "SELECT id, glCodeId, openingDebitBalance AS \"dr\", " +
			"openingCreditBalance AS \"cr\", debitAmount, creditAmount,accountDetailTypeId, " +
			"accountDetailKey, financialYearId, fundId FROM transactionSummary " +
			"WHERE financialYearId = ? AND fundId= ? " +
					"AND glCodeId IN ("+glCodeIds+") AND id NOT IN ("+clIds+")";
			pst = connection.prepareStatement(sqlQueryString);
			pst.setString(1, fyId);
			pst.setString(2, fundId);
			//pst.setString(3, deptId);
			resultset = pst.executeQuery();
			query = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid, " +
			"openingdebitbalance, openingcreditbalance, debitamount, " +
			"creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId) " +
			"VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstBatch = connection.prepareStatement(query);
			while(resultset.next()){
				id = String.valueOf(PrimaryKeyGenerator.getNextKey("transactionSummary"));
				pstBatch.setString(1, id);
				pstBatch.setString(2, nextFYId);
				pstBatch.setInt(3, resultset.getInt("dr"));
				pstBatch.setInt(4, resultset.getInt("cr"));
				pstBatch.setInt(5, resultset.getInt("debitAmount"));
				pstBatch.setInt(6, resultset.getInt("creditAmount"));
				pstBatch.setInt(7, resultset.getInt("accountDetailTypeId"));
				pstBatch.setInt(8, resultset.getInt("accountDetailKey"));
				pstBatch.setInt(9, resultset.getInt("fundId"));
			//	pstBatch.setInt(10, resultset.getInt("dept_name"));
				pstBatch.addBatch();
			}
			pstBatch.executeBatch();
			/*for(int i=0; i<updateCounts.length;i++)
			if(updateCounts[i] == 0);
			*/
			/** ********************************* **/

			pstBatch.close();
			resultset.close();
			pst.close();

			//Recursive Closing Balace for subsequent years
			if(nextYear(fyId)) calcClosingBalance(nextFYId, fundId, glCodeIds);
			isDone = true;
		}catch(SQLException ex){
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Error SetUp->ClosingBalance(): " + ex.toString());
			throw new TaskFailedException(ex.getMessage());
		}
		return isDone;
	}
	 private boolean nextYear(String fyId)throws TaskFailedException{
		boolean nextYear=false;
		try{
			String query = "SELECT id FROM financialYear " +
			"WHERE startingDate > (SELECT endingDate FROM financialYear WHERE id = ?)";
			pst = connection.prepareStatement(query);
			pst.setString(1, fyId);
			resultset = pst.executeQuery();
			if(resultset.next()) nextYear = true;
			resultset.close();
			pst.close();
		}catch(SQLException ex){
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Error SetUp->nextYear: " + ex.toString());
		}
		return nextYear;
	}
	 private FY getFinancialYear(String fyId){
		FY fy = new FY();
		try{
			String query = "SELECT to_char(startingDate, 'DD-Mon-yyyy') AS \"startingDate\", to_char(endingDate, 'DD-Mon-yyyy') AS \"endingDate\" " +
			"FROM financialYear WHERE id= ?";
			pst = connection.prepareStatement(query);
			pst.setString(1, fyId);
			resultset = pst.executeQuery();
			if(resultset.next()){
				fy.setId(fyId);
				fy.setSDate(resultset.getString("startingDate"));
				fy.setEDate(resultset.getString("endingDate"));
			}
			resultset.close();
			pst.close();
		}catch(SQLException ex){
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Error SetUp->getFinancialYear: " + ex.toString());
		}
		return fy;
	}
	 private boolean isFYOpen(String fyId){
		boolean isOpen=false;
		try{
			String query = "SELECT id FROM financialYear WHERE isClosed=1 AND id= ?";
			pst = connection.prepareStatement(query);
			pst.setString(1, fyId);
			resultset = pst.executeQuery();
			if(!resultset.next()) isOpen = true;
			resultset.close();
			pst.close();
		}catch(SQLException ex){
			isOpen = false;
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Error SetUp->isFYOpen(): " + ex.toString());
		}
		return isOpen;
	 }
	 private String getNextFYId(String fyId){
		String nextFYId="";
		try{
			String query = "SELECT id FROM financialYear " +
			"WHERE startingDate = (SELECT endingDate+1 FROM financialYear WHERE id = ?)";
			pst = connection.prepareStatement(query);
			pst.setString(1, fyId);
			resultset = pst.executeQuery();
			if(resultset.next()) nextFYId = resultset.getString("id");
			resultset.close();
			pst.close();
		}catch(SQLException ex){
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Error SetUp->getNextFYId: " + ex.toString());
		}
		return nextFYId;
	 }

	 private String isOpeniningBalanceSet(Connection con,DataCollection dc, Integer index) throws TaskFailedException
	{
		try {
			String fundId = dc.getValue("fund_id");
			String fyId =  dc.getValue("financialYear_id");
			String deptId =  dc.getValue("dept_name");
			String gridOpeningBalance[][] = dc.getGrid("gridOpeningBalance");
			String accountDetailTypeId=gridOpeningBalance[index][5];
			String accountDetailKey=gridOpeningBalance[index][6];
			String glCodeId=gridOpeningBalance[index][0];
			String fundSourceId=dc.getValue("fundSourceId");
			String fundsourceCondition="";
			if(!(fundSourceId.equalsIgnoreCase("") ||  fundSourceId.equalsIgnoreCase("-1")||  fundSourceId.equalsIgnoreCase("0")))
				fundsourceCondition=" AND FUNDSOURCEID="+Integer.parseInt(fundSourceId);
			else
				fundsourceCondition=" AND FUNDSOURCEID IS NULL ";
								
			//check if entity key exists then check OB is set or not
			if(accountDetailTypeId!=null && !(accountDetailTypeId.trim().equalsIgnoreCase("") || accountDetailTypeId.equalsIgnoreCase("0")) )
			{
				String query = "select glcodeid as \"glcodeId\" from transactionsummary "+
				" where glcodeid= ? AND fundId= ? AND ACCOUNTDETAILKEY= ? AND accountdetailtypeid= ? AND financialYearId= ? "+ fundsourceCondition+
				" and departmentId=?";
				
				pst = con.prepareStatement(query);
				pst.setString(1, glCodeId);
				pst.setString(2, fundId);
				pst.setString(3, accountDetailKey);
				pst.setString(4, accountDetailTypeId);
				pst.setString(5, fyId);
				pst.setString(6, deptId);  
				if(accountDetailKey!=null && !(accountDetailKey.trim().equalsIgnoreCase("") || accountDetailKey.equalsIgnoreCase("0")) )
					resultset = pst.executeQuery();
			}else{
				String sql = "select glcodeid as \"glcodeId\" from transactionsummary "+
				" where glcodeid= ? AND fundId= ? AND financialYearId= ? "+ fundsourceCondition+
				" and departmentid=?";
				pst = con.prepareStatement(sql);
				pst.setString(1, glCodeId);
				pst.setString(2, fundId);
				pst.setString(3, fyId);
				pst.setString(4, deptId);
				resultset = pst.executeQuery();
			}
			
			if(resultset.next())
				return "update";
			else
				return "set";
		} catch (SQLException e) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Exp="+e.getMessage()); throw taskExc;
		}
	}

}
