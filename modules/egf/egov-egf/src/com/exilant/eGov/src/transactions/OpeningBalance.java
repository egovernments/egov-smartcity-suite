/**
 * Created on Jan 16 , 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.transactions;

import com.exilant.exility.common.*;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import com.exilant.eGov.src.common.*;
import org.apache.log4j.Logger;

public class OpeningBalance extends AbstractTask{
	 private Connection connection;
	 private Statement statement;
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
    				statement  = connection.createStatement();
    				String query="select glcode as \"glcode\",classification as \"classification\",isactiveforposting as \"isactiveforposting\",type as \"type\" from chartofaccounts where glcode = '"+gridOpeningBalance[i][1]+"' and type='"+serviceType+"'";
    				LOGGER.debug(query);
    				resultset = statement.executeQuery(query);
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
				LOGGER.info("mode:"+mode);
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
		
	 	String fundId = dc.getValue("fund_id");
		String financialYearId = dc.getValue("financialYear_id");
		String detailTypeId = "0", detailKey = "0";
		String id="", query="", dr="0", cr="0";
		String gridOpeningBalance[][] = dc.getGrid("gridOpeningBalance");
		extraMessage = "";
		if(!isFYOpen(financialYearId)){
			dc.addMessage("exilError","Financial Year is Closed, Opening Balance can not be updated");
			throw taskExc;
		}
		try{
			statement  = connection.createStatement();
			Statement stBatch = connection.createStatement();
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
				chkEntry = "SELECT id FROM transactionSummary WHERE glCodeId="+gridOpeningBalance[i][0]+" " +
								"AND fundId="+fundId+" AND financialYearId="+financialYearId + " " +
									"AND accountDetailTypeId="+detailTypeId+" AND accountDetailKey=" +detailKey+fsCond;
									

				resultset = null;
				LOGGER.debug("chkEntry  "+chkEntry);
				resultset = statement.executeQuery(chkEntry);
				if(resultset.next())
					extraMessage = extraMessage + gridOpeningBalance[i][1] + ", ";	
				else{
					/** Checking the Bank account 
					 * If its an asset code then only we need to check
					 * */
					if(dc.getValue("ftService_type").equals("A"))
					{
						LOGGER.debug("Need to check bank accounts.");
						ResultSet rs=statement.executeQuery("select fundid from bankaccount where glcodeid="+gridOpeningBalance[i][0]);
						if(rs.next())
						{
							//Check if the funds are same
							String bankFund=rs.getString(1);
							LOGGER.debug("Need to check bank accounts.");
							if(!bankFund.equalsIgnoreCase(fundId)){
								dc.addMessage("exilError","Fund associated to the Bank Account is incorrect");
								throw new TaskFailedException("Fund associated to the Bank Account is incorrect,");
							}
						}
					}
					
								
					id = String.valueOf(PrimaryKeyGenerator.getNextKey("TransactionSummary"));
					query = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid, " +
								"openingdebitbalance, openingcreditbalance, debitamount, " +
								"creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId,fundsourceid,LASTMODIFIEDBY,LASTMODIFIEDDATE,NARRATION) " +
								"VALUES (" + id + ", " + financialYearId + ", " + gridOpeningBalance[i][0] + ", " +
								gridOpeningBalance[i][3] + ", " + gridOpeningBalance[i][4] + ", " + dr + ", " + cr +
								", "+detailTypeId+", "+detailKey+", " + fundId + ","+fundSourceId+", "+dc.getValue("current_UserID")+", " +lastModifiedDate+", '" + gridOpeningBalance[i][8]+"')";
					LOGGER.info("query  "+query);
					stBatch.addBatch(query);
				}
				/** if executeBatch Fails throw exception **/
				int updateCounts[] = stBatch.executeBatch();
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
			statement.close();
			stBatch.close();
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
			statement  = connection.createStatement();
			Statement stBatch = connection.createStatement();
			String updateQuery="";
			String glCodes="";
			int i=index;
				gridOpeningBalance[i][3] = gridOpeningBalance[i][3].equalsIgnoreCase("")?"0":gridOpeningBalance[i][3];
				gridOpeningBalance[i][4] = gridOpeningBalance[i][4].equalsIgnoreCase("")?"0":gridOpeningBalance[i][4];
				glCodes = glCodes + "'" + gridOpeningBalance[i][1] + "',";
				/*chkEntry = "SELECT id FROM transactionSummary WHERE glCodeId="+gridOpeningBalance[i][0]+" " +
								"AND fundId="+fundId+" AND financialYearId="+financialYearId + accEntityCondition;
							*/
				updateQuery = "UPDATE transactionSummary SET openingdebitbalance="+gridOpeningBalance[i][3]+", " +
									"openingcreditbalance="+gridOpeningBalance[i][4]+", "+
									" LASTMODIFIEDBY="+dc.getValue("current_UserID")+", "+
									" LASTMODIFIEDDATE="+ lastModifiedDate +", "+
									" NARRATION='"+ gridOpeningBalance[i][8] +"'" +
									" WHERE id="+gridOpeningBalance[i][7];

				LOGGER.info("updateQuery  "+updateQuery);
				stBatch.addBatch(updateQuery);

			/** if executeBatch Fails throw exception **/
			int updateCounts[] = stBatch.executeBatch();
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
			statement = connection.createStatement();
			//Closing Balance For glCode-Fund-FY
			clBalQuery = "SELECT gl.glcodeId, vh.fundId, sum(debitAmount) AS \"dr\", " +
							"sum(creditAmount) AS \"cr\", (sum(debitAmount) - sum(creditAmount)) AS \"balance\" " +
							"FROM voucherHeader vh, chartOfAccounts coa, generalledger gl " +
							"WHERE vh.id = gl.voucherHeaderId AND gl.glCode=coa.glcode " +
							"AND voucherDate >= '"+sDate+"' AND voucherDate <= '"+eDate+"' " +
							"AND vh.fundId="+fundId+" AND coa.id IN ("+glCodeIds+") " +
							"GROUP BY gl.glcodeId,vh.fundId";

			resultset = statement.executeQuery(clBalQuery);

			String id="",query="";
			double bal=0, opDr=0, opCr=0, dr=0, cr=0;
			Statement stBatch = connection.createStatement();
			if(nextFYId.equalsIgnoreCase("")){
				return true;
			}
			int i=0;
			while(resultset.next()){
				id = String.valueOf(PrimaryKeyGenerator.getNextKey("TransactionSummary"));
				bal = resultset.getDouble("balance");
				opDr = bal>0?bal:0;
				opCr = bal<0?Math.abs(bal):0;
				query = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid, " +
							"openingdebitbalance, openingcreditbalance, debitamount, " +
							"creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId) " +
							"VALUES (" + id + ", " + nextFYId + ", " + resultset.getInt("glCodeId") + ", " +
							opDr + ", " + opCr + ", " + dr + ", " + cr + ", 0, 0, " + resultset.getInt("fundId") + ")";
				stBatch.addBatch(query);
			}

			statement.executeUpdate("DELETE transactionSummary WHERE financialYearId="+nextFYId+" " +
							"AND fundId="+fundId+" AND glCodeId IN ("+glCodeIds+")");
			int updateCounts[] = stBatch.executeBatch();
			for(i=0; i<updateCounts.length;i++){
				if(updateCounts[i] == 0){
					dc.addMessage("exilError","Batch Execute Failed");
					throw taskExc;
				}
			}

			/** Calculating current year openingBalance+ClosingBalance for next year's OpeningBalance **/
			/** ********************************* **/
			resultset = null;
			resultset = statement.executeQuery("SELECT cl.id AS \"clId\", op.id AS \"opId\", " +
					"cl.openingDebitBalance+op.openingDebitBalance AS \"dr\", " +
					"cl.openingCreditBalance+op.openingCreditBalance AS \"cr\" " +
					"FROM transactionSummary cl, transactionSummary op " +
					"WHERE cl.financialYearId = "+fyId+" AND op.financialYearId = "+nextFYId+" " +
					"AND cl.glCodeId = op.glCodeId AND cl.fundId = op.fundId " +
					"AND cl.accountDetailTypeId = op.accountDetailTypeId " +
					"AND cl.accountDetailKey = op.accountDetailKey " +
					"AND cl.fundId="+fundId+" AND cl.glCodeId IN ("+glCodeIds+")");
			stBatch.clearBatch();
			String clIds="";
			while(resultset.next()){
				clIds = clIds + resultset.getString("clId") + ",";
				dr = resultset.getDouble("dr");
				cr = resultset.getDouble("cr");
				bal = dr-cr;
				dr = bal>0?bal:0;
				cr = bal<0?Math.abs(bal):0;

				query = "UPDATE transactionSummary SET openingDebitBalance = "+dr+", " +
						"openingCreditBalance = "+cr+" WHERE id = "+resultset.getString("opId");
				stBatch.addBatch(query);
			}

			if(clIds.length()>0)
				clIds = clIds.substring(0, clIds.length()-1);
			else
				clIds = "0";

			resultset = null;

			resultset = statement.executeQuery("SELECT id, glCodeId, openingDebitBalance AS \"dr\", " +
					"openingCreditBalance AS \"cr\", debitAmount, creditAmount,accountDetailTypeId, " +
					"accountDetailKey, financialYearId, fundId FROM transactionSummary " +
					"WHERE financialYearId = " + fyId + " AND fundId="+fundId+" " +
							"AND glCodeId IN ("+glCodeIds+") AND id NOT IN ("+clIds+")");
			while(resultset.next()){
				id = String.valueOf(PrimaryKeyGenerator.getNextKey("transactionSummary"));
				query = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid, " +
							"openingdebitbalance, openingcreditbalance, debitamount, " +
							"creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId) " +
							"VALUES (" + id + ", " + nextFYId + ", " + resultset.getInt("glCodeId") + ", " +
							resultset.getInt("dr") + ", " + resultset.getInt("cr") + ", " +
							resultset.getInt("debitAmount") + ", " + resultset.getInt("creditAmount") + ", " +
							resultset.getInt("accountDetailTypeId") + ", " + resultset.getInt("accountDetailKey") + ", " +
							resultset.getInt("fundId") + ")";
				stBatch.addBatch(query);
			}
			stBatch.executeBatch();
			/*for(int i=0; i<updateCounts.length;i++)
			if(updateCounts[i] == 0);
			*/
			/** ********************************* **/

			stBatch.close();
			resultset.close();
			statement.close();

			//Recursive Closing Balace for subsequent years
			if(nextYear(fyId)) calcClosingBalance(nextFYId, fundId, glCodeIds);
			isDone = true;
		}catch(SQLException ex){
			LOGGER.debug("Error SetUp->ClosingBalance(): " + ex.toString());
			throw new TaskFailedException(ex.getMessage());
		}
		return isDone;
	}
	 private boolean nextYear(String fyId)throws TaskFailedException{
		boolean nextYear=false;
		try{
			statement = connection.createStatement();
			resultset = statement.executeQuery("SELECT id FROM financialYear " +
					"WHERE startingDate > (SELECT endingDate FROM financialYear WHERE id = " + fyId +")");
			if(resultset.next()) nextYear = true;
			resultset.close();
			statement.close();
		}catch(SQLException ex){
			LOGGER.debug("Error SetUp->nextYear: " + ex.toString());
		}
		return nextYear;
	}
	 private FY getFinancialYear(String fyId){
		FY fy = new FY();
		try{
			statement = connection.createStatement();
			resultset = statement.executeQuery("SELECT to_char(startingDate, 'DD-Mon-yyyy') AS \"startingDate\", to_char(endingDate, 'DD-Mon-yyyy') AS \"endingDate\" " +
					"FROM financialYear WHERE id="+fyId);
			if(resultset.next()){
				fy.setId(fyId);
				fy.setSDate(resultset.getString("startingDate"));
				fy.setEDate(resultset.getString("endingDate"));
			}
			resultset.close();
			statement.close();
		}catch(SQLException ex){
			LOGGER.debug("Error SetUp->getFinancialYear: " + ex.toString());
		}
		return fy;
	}
	 private boolean isFYOpen(String fyId){
		boolean isOpen=false;
		try{
			statement = connection.createStatement();
			resultset = statement.executeQuery("SELECT id FROM financialYear WHERE isClosed=1 AND id=" + fyId);
			if(!resultset.next()) isOpen = true;
			resultset.close();
			statement.close();
		}catch(SQLException ex){
			isOpen = false;
			LOGGER.debug("Error SetUp->isFYOpen(): " + ex.toString());
		}
		return isOpen;
	 }
	 private String getNextFYId(String fyId){
		String nextFYId="";
		try{
			statement = connection.createStatement();
			resultset = statement.executeQuery("SELECT id FROM financialYear " +
					"WHERE startingDate = (SELECT endingDate+1 FROM financialYear WHERE id = " + fyId+")");
			if(resultset.next()) nextFYId = resultset.getString("id");
			resultset.close();
			statement.close();
		}catch(SQLException ex){
			LOGGER.debug("Error SetUp->getNextFYId: " + ex.toString());
		}
		return nextFYId;
	 }

	 private String isOpeniningBalanceSet(Connection con,DataCollection dc, Integer index) throws TaskFailedException
	{
		try {
			String fundId = dc.getValue("fund_id");
			String fyId =  dc.getValue("financialYear_id");
			statement = con.createStatement();
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
				if(accountDetailKey!=null && !(accountDetailKey.trim().equalsIgnoreCase("") || accountDetailKey.equalsIgnoreCase("0")) )
					resultset = statement.executeQuery("select glcodeid as \"glcodeId\" from transactionsummary "+
							" where glcodeid="+glCodeId+ " AND fundId="+ fundId + " AND ACCOUNTDETAILKEY="+accountDetailKey +" AND accountdetailtypeid="+accountDetailTypeId+ " AND financialYearId="+fyId+ fundsourceCondition);
			}else
				resultset = statement.executeQuery("select glcodeid as \"glcodeId\" from transactionsummary "+
					" where glcodeid="+glCodeId+ " AND fundId="+ fundId +" AND financialYearId="+fyId+ fundsourceCondition);
			
			if(resultset.next())
				return "update";
			else
				return "set";
		} catch (SQLException e) {
			LOGGER.debug("Exp="+e.getMessage()); throw taskExc;
		}
	}

}
