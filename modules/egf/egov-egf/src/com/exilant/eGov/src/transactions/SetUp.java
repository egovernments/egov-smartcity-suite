/*
 * Created on Apr 20, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.transactions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.ClosedPeriods;
import com.exilant.eGov.src.domain.FinancialYear;
//import com.exilant.eGov.src.reports.Fund;
import com.exilant.eGov.src.reports.TransferBalance;
//import com.exilant.eGov.src.reports.BalanceSheetReport;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import org.apache.log4j.Logger;


class FY{
	String id;
	String sDate;
	String eDate;
	public FY(){};
	public void setId(String id){this.id = id;}
	public void setSDate(String sDate){this.sDate = sDate;}
	public void setEDate(String eDate){	this.eDate = eDate;	}
	public String getId(){return id;}
	public String getSDate(){return sDate;}
	public String getEDate(){return eDate;}
}
public class SetUp extends AbstractTask{

       private Connection connection;
       private Statement statement;
       private ResultSet resultset;
       private static  final Logger LOGGER = Logger.getLogger(SetUp.class);
       private String effectiveDate;

       public void execute(String taskName,
                                                       String gridName,
                                                       DataCollection dc,
                                                       Connection con,
                                                       boolean errorOnNoData,
                                                       boolean gridHasColumnHeading,
                                                       String prefix) throws TaskFailedException {
               this.connection = con;
               boolean transferred;
               EGovernCommon cm = new EGovernCommon();
               effectiveDate = cm.getCurrentDateTime(connection);
               try{
			   		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
					Date dt=new Date();
					dt = sdf.parse( effectiveDate );
					effectiveDate = formatter.format(dt);
		   		}catch(Exception e){
		   			LOGGER.debug("Exp="+e.getMessage());
		   			throw new TaskFailedException();
		   		}

               try{
                       if(dc.getValue("activity").equalsIgnoreCase("OFY"))
                               openFY(dc); //Open Financial Year
                       else if(dc.getValue("activity").equalsIgnoreCase("CFY")){
                       	String fyId=dc.getValue("financialYear_id");
                       	if(isPreToFYOpen(fyId)){
                            dc.addMessage("exilError","Previos Financial Year is Open, it can not be closed");
                            throw new TaskFailedException();
                       		}

                               transferred=checkForTransferClosingBalance(con,dc); //Check for TransferClosingBalance
                               if(transferred==false){
                                       dc.addMessage("userFailure"," Cannot Close this financial year as Transfer Closing Balance not done");
                                       throw new TaskFailedException();
                               }

                               closeFY(dc); //Close Financial Year
                       }
                       else if(dc.getValue("activity").equalsIgnoreCase("OP"))
                               openDateRange(dc); //Open Periods
                       else if(dc.getValue("activity").equalsIgnoreCase("CP")){
                       	String fyId=dc.getValue("financialYear_id");
                       	if(isPreToFYOpen(fyId)){
                            dc.addMessage("exilError","Previos Financial Year is Open, it can not be closed");
                            throw new TaskFailedException();
                       		}

                               closeDateRange(dc); //Close Data Range

                       }
                       else if(dc.getValue("activity").equalsIgnoreCase("CB")){
                       		   String fyId = dc.getValue("financialYear_id");
                               calcClosingBalance(dc); //Calculate Closing Balance
                               updateTransferClosingBalance(con,dc);//Updating TransferClosingBalance in FinancialYear

                       }
                       else if(dc.getValue("activity").equalsIgnoreCase("CFNEW")){
                       	String fyId=dc.getValue("financialYear_id");
                       	if(isPreToFYOpen(fyId)){
                            dc.addMessage("exilError","Previos Financial Year is Open, it can not be closed");
                            throw new TaskFailedException();
                       		}
                                       calcClosingBalance(dc); //Calculate Closing Balance
                                       closeFY(dc); //Close Financial Year


                       }
                       dc.addMessage("eGovSuccess","SetUp");
               }catch(SQLException ex){
                       dc.addMessage("eGovFailure","SetUp");
                       throw new TaskFailedException();
               }
               /*catch(Exception ex){
                       throw new TaskFailedException();
               }*/
       }
       private void updateTransferClosingBalance(Connection connection, DataCollection dc)throws TaskFailedException{
               String fyId = dc.getValue("financialYear_id");
               int n=0;
               try{

               statement = connection.createStatement();
               n = statement.executeUpdate("update financialYear set TRANSFERCLOSINGBALANCE = 1 where id='"+fyId+"'");
           		if(n==1)LOGGER.debug("executed successfully n value "+n);
               }
               catch(Exception ex){
                               LOGGER.debug("Error : " + ex.toString());
                               dc.addMessage("eGovFailure","Unable to update the TRANSFERCLOSINGBALANCE in Financial Year" );
                               throw new TaskFailedException(ex);
                               }
       }

       private boolean checkForTransferClosingBalance(Connection connection, DataCollection dc) throws TaskFailedException{
               String fyId = dc.getValue("financialYear_id");
               int chk=0;
               try{
                       statement = connection.createStatement();
                       resultset = statement.executeQuery("select TRANSFERCLOSINGBALANCE from financialYear where id='"+fyId+"'and TRANSFERCLOSINGBALANCE=0");

                       if(resultset.next()){
                               return false;
                       }
                       return true;
               }
               catch(Exception ex){
                               LOGGER.debug("Error : " + ex.toString());
                               throw new TaskFailedException(ex);
                               }
       }

      private void openFY(DataCollection dc) throws TaskFailedException{
               String fyId = dc.getValue("financialYear_id");
               try{
                       statement = connection.createStatement();
                       resultset = statement.executeQuery("SELECT id FROM financialYear " +
                                       "WHERE startingDate > (SELECT endingDate FROM financialYear WHERE id = "+fyId+") " +
                                                       "AND isClosed = 1");
                       if(resultset.next()){
                               dc.addMessage("exilError","This Financial Year can not be opened, later is closed");
                               throw new TaskFailedException();
                       }
                       resultset.close();
                       statement.close();

                       FinancialYear fy = new FinancialYear();
                       fy.setId(fyId);
                       fy.setIsActiveForPosting("1");
                       fy.update(connection);
               }catch(SQLException ex){
                       LOGGER.debug("Error (SetUp->openFY): " + ex.toString());
                       dc.addMessage("exilError", "SetUp->openFY failed");
                       throw new TaskFailedException();
               }
       }
       private void closeFY(DataCollection dc) throws TaskFailedException, SQLException {
               String fyId = dc.getValue("financialYear_id");
               boolean calcClBal = dc.getValue("calcClBal").equalsIgnoreCase("1")?true:false;
               if(!isFYOpen(fyId)){
                       dc.addMessage("exilError","Financial Year is already closed");
                       throw new TaskFailedException();
               }

              /* if(isPreToFYOpen(fyId)){
                       dc.addMessage("exilError","Previos Financial Year is Open, it can not be closed");
                       throw new TaskFailedException();
               }*/
               FinancialYear fy = new FinancialYear();
               fy.setId(fyId);
               fy.setIsActiveForPosting("0");
               fy.setIsClosed("1");

               fy.setTransferClosingBalance("1");
               try{
                       fy.update(connection);
               }catch(SQLException ex){
                       dc.addMessage("exilError", "SetUp->closeFY failed");
                       throw new TaskFailedException();
               }

               //Closing Balance
               if(calcClBal && !calcClosingBalance(fyId,dc.getValue("current_UserID"))){
                       dc.addMessage("exilError","Closing Balance Failed");
                       throw new TaskFailedException();
               }
       }

       /**
        * This function is called to calculate the closing balance for any financial year
        * @param dc
        * @throws TaskFailedException
        */
       private void calcClosingBalance(DataCollection dc) throws TaskFailedException{
       	LOGGER.debug("Calculate cloasing balance is called...");
		String fyId = dc.getValue("financialYear_id");
		if(!calcClosingBalance(fyId,dc.getValue("current_UserID"))){
			dc.addMessage("exilError","Closing Balance Failed");
			throw new TaskFailedException();
		}
	}
       /**
        * This function will do the closing balance calculation
        * @param fyId
        * @return
        */
       	private boolean calcClosingBalance(String fyId,String userId)throws TaskFailedException{
		boolean isDone = false;
		FY fy = getFinancialYear(fyId);
		String sDate=fy.getSDate();
		String eDate=fy.getEDate();
		String nextFYId = getNextFYId(fyId);
		LOGGER.info("Current Year is .."+fyId+"  Next Year for "+fyId+" is :"+nextFYId);
		String clBalQuery = "";
		try{

			statement = connection.createStatement();
			//Closing Balance For glCode-Fund-FY from transaction table
			clBalQuery = "SELECT gl.glcodeId, vh.fundId, sum(debitAmount) AS \"dr\", " +
							"sum(creditAmount) AS \"cr\", (sum(debitAmount) - sum(creditAmount)) AS \"balance\" " +
							" FROM voucherHeader vh, chartOfAccounts coa,CHARTOFACCOUNTDETAIL coad, generalledger gl " +
							"WHERE vh.id = gl.voucherHeaderId AND gl.glCode=coa.glcode " +
							"AND voucherDate >= '"+sDate+"' AND voucherDate <= '"+eDate+"'AND vh.status<>4 " +
							" AND coa.type in('A','L') AND coad.GLCODEID (+)=coa.id GROUP BY gl.glcodeId,vh.fundId";
			LOGGER.info(clBalQuery);
			resultset = statement.executeQuery(clBalQuery);

			String id="",query="";
			double bal=0, opDr=0, opCr=0, dr=0, cr=0;
			Statement stBatch = connection.createStatement();
			if(nextFYId.equalsIgnoreCase("")){
				LOGGER.info("Next FY not present ***************************");
				return false;
			}
			int i=0;
			int isControlCode=0;
			while(resultset.next()){
				id = String.valueOf(PrimaryKeyGenerator.getNextKey("TransactionSummary"));
				bal = resultset.getDouble("balance");
				opDr = bal>0?bal:0;
				opCr = bal<0?Math.abs(bal):0;
				query = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid, " +
							"openingdebitbalance, openingcreditbalance, debitamount, " +
							"creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId,lastmodifiedby,lastmodifieddate) " +
							"VALUES (" + id + ", " + nextFYId + ", " + resultset.getInt("glCodeId") + ", " +
							opDr + ", " + opCr + ", " + dr + ", " + cr + ", null, null, " + resultset.getInt("fundId") + ","+userId+",to_date('" + effectiveDate + "','dd-Mon-yyyy HH24:MI:SS'))";
				stBatch.addBatch(query);
				LOGGER.info(query);
			}
resultset.close();
			//Delete all the opening balances present for the new financial year. This is because we are going to recalculate from last year
			statement.executeUpdate("DELETE from transactionSummary WHERE financialYearId="+nextFYId);

			int updateCounts[] = stBatch.executeBatch();

			/*for(int i=0; i<updateCounts.length;i++)
			if(updateCounts[i] == 0);
			*/

			//Closing Balance For glCode-Fund-FY-accountEntity from transaction table
			String glCodeAndDetailType = "select coa.glCode AS \"glCode\", cod.detailTypeId AS \"detailTypeId\" " +
					"FROM chartOfAccounts coa, chartOfAccountDetail cod " +
					"WHERE coa.id = cod.glCodeId  and coa.type in ('A','L')";
			LOGGER.info("glCodeAndDetailType: " + glCodeAndDetailType);
			resultset = null;
			resultset = statement.executeQuery(glCodeAndDetailType);
			String codeAndDetailCondition = "";
			String glCode="";
			if(resultset.next())
				codeAndDetailCondition = " (coa.glCode = '"+resultset.getString("glCode")+"' AND gld.detailTypeId = "+resultset.getString("detailTypeId")+") ";
			while(resultset.next())
				codeAndDetailCondition = codeAndDetailCondition + " OR (coa.glCode = '"+resultset.getString("glCode")+"' AND gld.detailTypeId = "+resultset.getString("detailTypeId")+")";
			if(codeAndDetailCondition.trim().length()>0)
			{
			clBalQuery = "SELECT gl.glcodeId, vh.fundId, gld.detailTypeId, gld.detailKeyId, sum(debitAmount) AS \"dr\", " +
					"sum(creditAmount) AS \"cr\", (sum(debitAmount) - sum(creditAmount)) AS \"balance\" " +
					"FROM voucherHeader vh, chartOfAccounts coa, generalledger gl, generalLedgerDetail gld  " +
					"WHERE vh.id = gl.voucherHeaderId AND gl.glCode=coa.glcode AND gl.id = gld.generalLedgerId AND (" + codeAndDetailCondition +")" +
					"AND voucherDate >= '"+sDate+"' AND voucherDate <= '"+eDate+"' AND coa.type in('A','L') AND vh.status<>4" +
					"GROUP BY gl.glcodeId, vh.fundId, gld.detailTypeId, gld.detailKeyId";

			resultset = null;
			LOGGER.info("clBalQuery  ..."+clBalQuery);
			resultset = statement.executeQuery(clBalQuery);
			}
			String txnSumBatch="", txnSumBatchDetail="", detailTypeId="", detailKeyId="";
			String txnDetailId="";
			stBatch.clearBatch();
			while(resultset.next()){
				id = String.valueOf(PrimaryKeyGenerator.getNextKey("TransactionSummary"));
				//txnDetailId = String.valueOf(PrimaryKeyGenerator.getNextKey("transactionSummaryDetails"));

				bal = resultset.getDouble("balance");
				opDr = bal>0?bal:0;
				opCr = bal<0?Math.abs(bal):0;
				detailTypeId=resultset.getString("detailTypeId");
				detailKeyId=resultset.getString("detailKeyId");
				txnSumBatch = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid, " +
							"openingdebitbalance, openingcreditbalance, debitamount, " +
							"creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId,lastmodifiedby,lastmodifieddate) " +
							"VALUES (" + id + ", " + nextFYId + ", " + resultset.getInt("glCodeId") + ", " +
							opDr + ", " + opCr + ", " + dr + ", " + cr + ", " + detailTypeId + ", " + detailKeyId + ", " +
							resultset.getInt("fundId") +","+userId+",to_date('" + effectiveDate + "','dd-Mon-yyyy HH24:MI:SS'))";
				LOGGER.info(txnSumBatch);
				stBatch.addBatch(txnSumBatch);
			}
			resultset.close();

			//if(!txnDetailId.equalsIgnoreCase(""))
				updateCounts = stBatch.executeBatch();

			//LOGGER.debug("*****************Calculation of Closing Balance Done");
			/** Calculating current year openingBalance+ClosingBalance for next year's OpeningBalance **/
			/** ********************************* **/
		//	resultset = null;
			double bal1=0, opDr1=0, opCr1=0, dr1=0, cr1=0;
			String qryTxns="SELECT cl.id AS \"clId\", op.id AS \"opId\", " +
			"cl.openingDebitBalance+op.openingDebitBalance AS \"dr\", " +
			"cl.openingCreditBalance+op.openingCreditBalance AS \"cr\" " +
			", (cl.openingDebitBalance+op.openingDebitBalance) - (cl.openingCreditBalance+op.openingCreditBalance) AS \"balance\" "+
			"FROM transactionSummary cl, transactionSummary op " +
			"WHERE cl.financialYearId = "+fyId+" AND op.financialYearId = "+nextFYId+" " +
			"AND cl.glCodeId = op.glCodeId AND cl.fundId = op.fundId " +
			"AND cl.accountDetailTypeId = op.accountDetailTypeId " +
			"AND cl.accountDetailKey = op.accountDetailKey " +
			" union "+
			"SELECT cl.id AS \"clId\", op.id AS \"opId\", " +
			"cl.openingDebitBalance+op.openingDebitBalance AS \"dr\", " +
			"cl.openingCreditBalance+op.openingCreditBalance AS \"cr\" " +
			", (cl.openingDebitBalance+op.openingDebitBalance) - (cl.openingCreditBalance+op.openingCreditBalance) AS \"balance\" "+
			"FROM transactionSummary cl, transactionSummary op " +
			"WHERE cl.financialYearId = "+fyId+" AND op.financialYearId = "+nextFYId+" " +
			"AND cl.glCodeId = op.glCodeId AND cl.fundId = op.fundId " +
			" and cl.accountdetailtypeid is  null and op.accountdetailtypeid is  null  and cl.accountdetailkey is  null and op.accountdetailkey is  null ";

			LOGGER.info("Transaction summary:"+qryTxns);

			resultset = statement.executeQuery(qryTxns);

			stBatch.clearBatch();
			String clIds="";
			while(resultset.next()){
				bal1 = resultset.getDouble("balance");
				opDr1 = bal1>0?bal1:0;
				opCr1 = bal1<0?Math.abs(bal1):0;
				clIds = clIds + resultset.getString("clId") + ",";
				//Get the opening balance and then find the net opening debit or credit.
				query = "UPDATE transactionSummary SET openingDebitBalance = "+opDr1+", " +
						"openingCreditBalance = "+opCr1+" WHERE id = "+resultset.getString("opId");
				stBatch.addBatch(query);
				LOGGER.info(query);
			}
			if(clIds.length()>0)
				clIds = clIds.substring(0, clIds.length()-1);
			else
				clIds = "0";

			resultset = null;
			//taking previous year opb
			String qry1="SELECT id, glCodeId, openingDebitBalance AS \"dr\", " +
			"openingCreditBalance AS \"cr\", debitAmount, creditAmount,accountDetailTypeId, " +
			"accountDetailKey, financialYearId, fundId,(openingDebitBalance-openingCreditBalance)as \"balance\" FROM transactionSummary " +
			"WHERE financialYearId = " + fyId + " AND id NOT IN ("+clIds+")";
			LOGGER.info("qry1..."+qry1);
			resultset = statement.executeQuery(qry1);
			while(resultset.next()){
				ArrayList list= null;
				HashMap hm=null;
				Boolean isExist=false;
				bal=opDr=opCr=0;
				bal=resultset.getDouble("balance");
				opDr = bal>0?bal:0;
				opCr = bal<0?Math.abs(bal):0;
				id = String.valueOf(PrimaryKeyGenerator.getNextKey("transactionSummary"));
				query = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid, " +
							"openingdebitbalance, openingcreditbalance, debitamount, " +
							"creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId,lastmodifiedby,lastmodifieddate) " +
							"VALUES (" + id + ", " + nextFYId + ", " + resultset.getInt("glCodeId") + ", " +
							opDr + ", " + opCr + ", " +
							resultset.getDouble("debitAmount") + ", " + resultset.getDouble("creditAmount") + ", " +
							resultset.getString("accountDetailTypeId") + ", " + resultset.getString("accountDetailKey") + ", " +
							resultset.getInt("fundId") +","+userId+",to_date('" + effectiveDate + "','dd-Mon-yyyy HH24:MI:SS'))";

				stBatch.addBatch(query);
				LOGGER.info(query);
			}
			updateCounts = stBatch.executeBatch();
			stBatch.close();
			resultset.close();
			statement.close();
			//This function will get the income over expense and transfer to the Fund Balance
			getIncomeOverExpense(sDate,eDate,nextFYId);
//			Recursive Closing Balace for subsequent years
		//	if(nextYear(nextFYId)) calcClosingBalance(nextFYId);
			LOGGER.info("**************************** Closing Balance Done");
			isDone = true;
		}catch(Exception ex){
			LOGGER.error("Exp="+ex.getMessage());
			LOGGER.error("Error SetUp->ClosingBalance(): " + ex.toString());
			throw new TaskFailedException(ex.getMessage());
		}
		return isDone;
	}

       	/**
       	 * Create the FY object when a financial year id is passed
       	 * @param fyId
       	 * @return
       	 * @throws TaskFailedException
       	 */
       	private FY getFinancialYear(String fyId)throws TaskFailedException{
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
			LOGGER.error("Error SetUp->getFinancialYear: " + ex.toString());
			throw new TaskFailedException(ex.getMessage());
		}
		return fy;
	}

       	/**
       	 * This function will return the next financial year of the financial id passed to the API
       	 * @param fyId
       	 * @return
       	 */
       	private String getNextFYId(String fyId)throws TaskFailedException{
		String nextFYId="";
		try{
			statement = connection.createStatement();
			String nxtYearStr="SELECT id FROM financialYear " +
			"WHERE startingDate = (SELECT endingDate+1 FROM financialYear WHERE id = " + fyId+")";
			LOGGER.info("Query for next year :"+nxtYearStr);
			resultset = statement.executeQuery(nxtYearStr);
			if(resultset.next())
				nextFYId = resultset.getString("id");
			resultset.close();
			statement.close();
		}catch(SQLException ex){
			LOGGER.error("Error SetUp->getNextFYId: " + ex.toString());
			throw new TaskFailedException(ex.getMessage());
		}
		return nextFYId;
	}

	private boolean isPreToFYOpen (String fyId)throws TaskFailedException{
		boolean isOpen=false;
		try{
			statement = connection.createStatement();
			resultset = statement.executeQuery("SELECT id FROM financialYear " +
					"WHERE endingDate < (SELECT startingDate FROM financialYear WHERE id = " + fyId+") " +
							"AND isClosed=0");
			if(resultset.next()) isOpen = true;
			resultset.close();
			statement.close();
		}catch(SQLException ex){
			isOpen = false;
			LOGGER.error("Error SetUp->getNextFYId: " + ex.toString());
			throw new TaskFailedException(ex.getMessage());
		}
		return isOpen;
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
			LOGGER.error("Exp="+ex.getMessage());
			LOGGER.error("Error SetUp->nextYear: " + ex);
			throw new TaskFailedException(ex.getMessage());
		}
		return nextYear;
	}
	private void openDateRange(DataCollection dc) throws TaskFailedException{
		//LOGGER.debug("openDateRange");
		LOGGER.debug(dc.getValue("closedPeriods_id"));
		String sDate="", eDate="";
		try{
			statement = connection.createStatement();
			resultset = statement.executeQuery("SELECT to_char(startingDate, 'DD-Mon-yyyy') AS \"startingDate\", to_char(endingDate, 'DD-Mon-yyyy') AS \"endingDate\" " +
													"FROM closedPeriods " +
													"WHERE id=" + dc.getValue("closedPeriods_id"));
			if(resultset.next()){
				sDate = resultset.getString("startingDate");
				eDate = resultset.getString("endingDate");
			}
			resultset.close();
			statement.close();
		}catch(SQLException ex){
			LOGGER.error("Error SetUp->openDateRange() InValid Id: " + ex.toString());
			throw new TaskFailedException(ex.getMessage());
		}

		if(!isFYOpen(sDate, eDate)){
			dc.addMessage("exilError","Financial Year is Closed");
			throw new TaskFailedException("Financial Year is Closed");
		}
		if(priorToHardClosed(sDate, eDate)){
			dc.addMessage("exilError","date range can not be opened prior to hard closed date range");
			throw new TaskFailedException("Date range can not be opened prior to hard closed date range");
		}
		try{
			statement = connection.createStatement();
			resultset = statement.executeQuery("DELETE from closedPeriods WHERE id=" + dc.getValue("closedPeriods_id"));
			resultset.close();
			statement.close();
		}catch(SQLException ex){
			LOGGER.error("Error SetUp->openDateRange(): " + ex.toString());
			throw new TaskFailedException(ex.getMessage());
		}
	}
	private void closeDateRange(DataCollection dc) throws TaskFailedException{
		String clstartingDate="";
		String clendingDate="";
		try
   		{
			//LOGGER.debug("inside try catch");
   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			Date dt=new Date();
			clstartingDate=dc.getValue("closedPeriods_startingDate");
			dt = sdf.parse( clstartingDate );
			clstartingDate = formatter.format(dt);
			clendingDate=dc.getValue("closedPeriods_endingDate");
			dt = sdf.parse( clendingDate );
			clendingDate = formatter.format(dt);
			//LOGGER.debug("within try transactionDate"+clendingDate);


   		}
   		catch(Exception e){
   			throw new TaskFailedException(e.getMessage());
   		}
		//String sDate = dc.getValue("closedPeriods_startingDate");
		//String eDate = dc.getValue("closedPeriods_endingDate");

   		String sDate = clstartingDate;
   		String eDate = clendingDate;
		String hardClose = dc.getValue("hardClose");


		if(!isFYOpen(sDate, eDate)){
			dc.addMessage("exilError","Financial Year is Closed Or date range out of Financial Year");
			throw new TaskFailedException();
		}
		//String fyEndingDate = isFYEndingDate(eDate);
		if(isFYEndingDate(eDate)){
			dc.addMessage("exilError","last date of a Financial Year can not be closed");
			throw new TaskFailedException();
		}
		if(hardClose.equalsIgnoreCase("1") && isHardClosed(sDate, eDate)){
			dc.addMessage("exilError","date range already hard closed");
			throw new TaskFailedException();
		}
		int cpId = isSoftClosed(sDate, eDate);
		if(cpId > 0){
			if(hardClose.equalsIgnoreCase("0")){
				dc.addMessage("exilError","date range already soft closed");
				throw new TaskFailedException();
			}
			//closePeriod(sDate, eDate, hardClose, cpId);
			if(canBeHardClosed(sDate))
				closePeriod(sDate, eDate, hardClose, cpId);
			else{
				dc.addMessage("exilError","can not be hard closed, all previous ranges must be hard closed");
				throw new TaskFailedException();
			}
			return;
		}
		if(rangeOverlaps(sDate, eDate)){
			dc.addMessage("exilError","date range overlaps with previously closed date range");
			throw new TaskFailedException();
		}

		if(hardClose.equalsIgnoreCase("0")){
			if(priorToHardClosed(sDate, eDate)){
				dc.addMessage("exilError","Soft Closing can not be performed prior to hard closed date range");
				throw new TaskFailedException();
			}
			closePeriod(sDate, eDate, hardClose, 0);
			return;
		}

		if(canBeHardClosed(sDate))
			closePeriod(sDate, eDate, hardClose, 0);
		else{
			dc.addMessage("exilError","can not be hard closed, all previous ranges must be hard closed");
			throw new TaskFailedException();
		}
	}
	private boolean closePeriod(String sDate,
									String eDate,
									String hardClose,
									int id) throws TaskFailedException{
		boolean success = false;
		ClosedPeriods cp = new ClosedPeriods();
		cp.setStartingDate(sDate);
		cp.setEndingDate(eDate);
		cp.setIsClosed(hardClose);
		try{
			if(id==0) cp.insert(connection);
			else {
				cp.setId(id + "");
				cp.update(connection);
			}
			success = true;
		}catch(SQLException ex){
			LOGGER.error("Error SetUp->closePeriod(): " + ex.toString());
			throw new TaskFailedException(ex.getMessage());
		}
		return success;
	}


	private boolean rangeOverlaps(String sDate,
									String eDate)throws TaskFailedException{
		boolean overlap = false;
		String messege = "";
		try{
			statement = connection.createStatement();
			resultset = statement.executeQuery("SELECT id FROM closedPeriods " +
							"WHERE (startingDate <= '"+sDate+"' AND endingDate >= '"+sDate+"') " +
							"OR (startingDate <= '"+eDate+"' AND endingDate >= '"+eDate+"') " +
							"OR (startingDate >= '"+sDate+"' AND endingDate <= '"+eDate+"') " +
							"OR (startingDate >= '"+sDate+"' AND startingDate <= '"+eDate+"') " +
							"OR (endingDate > '"+sDate+"' AND endingDate < '"+sDate+"')");

			if(resultset.next()) overlap = true;
			resultset.close();
			statement.close();
		}catch(SQLException ex){
			LOGGER.error("Error SetUp->rangeOverlaps(): " + ex.toString());
			throw new TaskFailedException(ex.getMessage());
		}
		return overlap;
	}

	private boolean isHardClosed(String sDate,
									String eDate)throws TaskFailedException{
		boolean hardClosed = false;
		try{
			statement = connection.createStatement();
			resultset = statement.executeQuery("SELECT id FROM closedPeriods " +
							"WHERE startingDate = '"+sDate+"' AND endingDate = '"+eDate+"' AND isClosed=1");
			if(resultset.next()) hardClosed = true;

			resultset.close();
			statement.close();
		}catch(SQLException ex){
			LOGGER.error("Error SetUp->isHardClosed(): " + ex.toString());
			throw new TaskFailedException(ex.getMessage());
		}
		return hardClosed;
	}
	private int isSoftClosed(String sDate,
								String eDate)throws TaskFailedException{
		int id=0;
		try{
			statement = connection.createStatement();
			LOGGER.debug("SELECT id AS \"id\" FROM closedPeriods " +
					"WHERE startingDate = '"+sDate+"' AND endingDate = '"+eDate+"' AND isClosed=0");
			resultset = statement.executeQuery("SELECT id AS \"id\" FROM closedPeriods " +
							"WHERE startingDate = '"+sDate+"' AND endingDate = '"+eDate+"' AND isClosed=0");
			if(resultset.next()) id = resultset.getInt("id");
			resultset.close();
			statement.close();
		}catch(SQLException ex){
			LOGGER.error("Error SetUp->isSoftClosed(): " + ex.toString());
			throw new TaskFailedException(ex.getMessage());
		}
		return id;
	}
	private boolean isFYEndingDate(String eDate)throws TaskFailedException{
		boolean isEndDate=false;
		try{
			statement = connection.createStatement();
			resultset = statement.executeQuery("SELECT id FROM financialYear " +
							"WHERE endingDate = '"+eDate+"'");
			if(resultset.next()) isEndDate = true;
			resultset.close();
			statement.close();
		}catch(SQLException ex){
			isEndDate = true;
			LOGGER.error("Error SetUp->isFYEndingDate: " + ex.toString());
			throw new TaskFailedException(ex.getMessage());
		}
		return isEndDate;
	}

       private boolean priorToHardClosed(String sDate,String eDate)throws TaskFailedException{
               boolean isPrior=false;
               try{
                       statement = connection.createStatement();
                       resultset = statement.executeQuery("SELECT id FROM closedPeriods " +
                                                       "WHERE (endingDate>='" + sDate + "' OR endingDate>='" + eDate + "') " +
                                                       "AND isClosed = 1");
                       if(resultset.next()) isPrior = true;
                       resultset.close();
                       statement.close();
               }catch(SQLException ex){
                       LOGGER.error("Error SetUp->priorToHardClosed(): " + ex.toString());
                       throw new TaskFailedException(ex.getMessage());
               }
               return isPrior;
       }

       private boolean canBeHardClosed(String sDate)throws TaskFailedException{
               boolean canBeHardClosed=false;
               try{
                               statement = connection.createStatement();
                               resultset = statement.executeQuery("SELECT id FROM financialYear WHERE isClosed=1");
                               if(!resultset.next()) {
                                       resultset.close();
                                       statement.close();
                                       statement = connection.createStatement();
                                       resultset = statement.executeQuery("SELECT id FROM financialYear " +
                                                       "WHERE '"+sDate+"' = (SELECT min(startingDate) FROM financialYear)");
                                       if(resultset.next()) canBeHardClosed = true;
                                       return canBeHardClosed;
                               }
                               resultset.close();
                               statement.close();

                               if(!canBeHardClosed){
                                       statement = connection.createStatement();
                                       resultset = statement.executeQuery("SELECT id FROM closedPeriods WHERE (endingDate+1 = '"+sDate+"' " +
                                                                       "AND endingDate = (SELECT max(endingDate) FROM closedPeriods WHERE isClosed = 1))");
                                       if(resultset.next()) canBeHardClosed = true;
                                       resultset.close();
                                       statement.close();
                               }

                               if(!canBeHardClosed){
                                       statement = connection.createStatement();
                                       resultset = statement.executeQuery("SELECT id FROM financialYear WHERE startingDate = '"+sDate+"' " +
                                                                               "AND startingDate > (SELECT max(endingDate) FROM financialYear " +
                                                                               "WHERE isClosed=1) AND startingDate = (SELECT min(startingDate) " +
                                                                               "FROM financialYear WHERE isClosed=0)");
                                       if(resultset.next()) canBeHardClosed = true;
                                       resultset.close();
                                       statement.close();
                               }
               }catch(SQLException ex){
                       canBeHardClosed = false;
                       LOGGER.error("Error SetUp->canBeHardClosed(): " + ex.toString());
                       throw new TaskFailedException(ex.getMessage());
               }
               return canBeHardClosed;
       }
       private boolean isFYOpen(String sDate, String eDate)throws TaskFailedException{
               boolean isOpen=false;
               try{
                       statement = connection.createStatement();
                       resultset = statement.executeQuery("SELECT id FROM financialYear " +
                                                                       "WHERE startingDate <= '"+sDate+"' " +
                                                                       "AND endingDate >= '"+eDate+"' " +
                                                                       "AND isActiveForPosting=1");
                       if(resultset.next()) isOpen = true;
                       resultset.close();
                       statement.close();
               }catch(SQLException ex){
                       isOpen = false;
                       LOGGER.error("Error SetUp->isFYOpen(): " + ex.toString());
                       throw new TaskFailedException(ex.getMessage());
               }
               return isOpen;
       }

       private boolean isFYOpen(String fyId)throws TaskFailedException{
               boolean isOpen=false;
               try{
                       statement = connection.createStatement();
                       resultset = statement.executeQuery("SELECT id FROM financialYear WHERE isClosed=1 AND id=" + fyId);
                       if(!resultset.next()) isOpen = true;
                       resultset.close();
                       statement.close();
               }catch(SQLException ex){
                       isOpen = false;
                       LOGGER.error("Error SetUp->isFYOpen(): " + ex.toString());
                       throw new TaskFailedException(ex.getMessage());
               }
               return isOpen;
       }

    private void getIncomeOverExpense(String sDate,String eDate,String nextFYId) throws Exception
   	{
   		TransferBalance tb=null;
   		Statement stmt1=null;
   		Statement stmt2=null;
   		ResultSet rs1=null;
   		ResultSet rs2=null;
   		int isClosed=0;
   		int glcodeid=0;
   		//BalanceSheetReport bsobj=new BalanceSheetReport();
   		String reqFundId[]=getFundList(sDate,eDate);

		statement = connection.createStatement();
		stmt2 = connection.createStatement();
		resultset=statement.executeQuery(" select a.id from chartofaccounts a where purposeid=7");
		if(resultset.next())
			glcodeid=resultset.getInt(1);
		else
			throw new Exception("Account Code not mapped for Excess IE.");

   		for(int i=0;i<reqFundId.length;i++)
		{
			String fundCondition=reqFundId[i];
			double balance=0,opDr=0,opCr=0;
			double optxndb=0,optxncr=0;
			tb=new TransferBalance();
			String	query1 = "SELECT decode(sum(gl.creditAmount)-sum(gl.debitamount),null,0,sum(gl.creditAmount)-sum(gl.debitamount)) FROM chartofaccounts  coa,generalledger gl,"
			+" voucherHeader vh WHERE coa.TYPE = 'I' and vh.ID =  gl.VOUCHERHEADERID AND  gl.glcode=coa.glcode "
			+" AND vh.VOUCHERDATE >= '"+sDate+"' AND vh.VOUCHERDATE <= '"+eDate+"' AND vh.status<>4 and vh.fundid="+fundCondition ;

			 String ifExistQry="Select glcodeid,openingdebitbalance,openingcreditbalance from transactionsummary where fundid="+fundCondition+
			 " AND financialyearid="+nextFYId +"and glcodeid="+glcodeid;
			 LOGGER.info("******"+ifExistQry);

			 boolean ifExist=false;
			 rs2=stmt2.executeQuery(ifExistQry);
			 if(rs2.next()){
			 	ifExist=true;
			 	optxndb=rs2.getDouble("openingdebitbalance");
			 	optxncr=rs2.getDouble("openingcreditbalance");
			 	LOGGER.info("optxndb  "+optxndb+"  optxncr  "+optxncr+ "  Glcode :"+rs2.getString("glcodeid"));
			 }
			 rs2.close();
			try
			{
				statement = connection.createStatement();
				LOGGER.info("Get Net Balance of All Income:" +query1);
		 		resultset = statement.executeQuery(query1);
		 		if(resultset.next())
		 		{
		 			balance= resultset.getDouble(1);
		 		}
		 		LOGGER.info("Total of all Incomes :"+balance);
		 		resultset.close();
		 		String	query2 = "SELECT decode(sum(gl.debitAmount)-sum(gl.creditamount),null,0,sum(gl.debitAmount)-sum(gl.creditamount)) FROM chartofaccounts  coa,generalledger gl,"
				+" voucherHeader vh WHERE coa.TYPE = 'E' and vh.ID =  gl.VOUCHERHEADERID AND  gl.glcode=coa.glcode "
				+" AND vh.VOUCHERDATE >= '"+sDate+"' AND vh.VOUCHERDATE <= '"+eDate+"' AND vh.status<>4 and vh.fundid="+fundCondition ;

		 		LOGGER.info("Get Net Balance of All Expenses:" +query2);

			 	resultset = statement.executeQuery(query2);
			 	if(resultset.next())
			 	{
			 		balance= balance-resultset.getDouble(1);
			 	}
			 	LOGGER.info("Net of Expense and Income "+balance);
			 	resultset.close();

				String query="";
				String id = String.valueOf(PrimaryKeyGenerator.getNextKey("TransactionSummary"));
				if(balance!=0){
					if(ifExist){
						//Records would have already there for the transactions for last year. That case update
						if(optxndb>0)
							balance=balance-optxndb;
						else
							balance=balance+optxncr;
						opDr = balance>0?0:Math.abs(balance);
						opCr = balance<0?0:Math.abs(balance);
						query ="update TransactionSummary set openingdebitbalance="+opDr+",openingcreditbalance="+
						opCr+" Where glcodeid="+glcodeid+" And fundId="+fundCondition+" AND financialYearId="+nextFYId;
					}
					else{
						//If there are no trnsactions for this glcode last year there wont be any records. So Insert
						if(optxndb>0)
							balance=balance-optxndb;
						else
							balance=balance+optxncr;
						opDr = balance>0?0:Math.abs(balance);
						opCr = balance<0?0:Math.abs(balance);
				 	 query = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid,openingdebitbalance, openingcreditbalance, debitamount, " +
				 	 		"creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId) " +
				 	 		"VALUES (" + id + ", " + nextFYId + ", " + glcodeid + ", " +opDr + ", " + opCr + ", 0, 0, null, null, " + fundCondition + ")";
					}

			 	LOGGER.info("Excess of Income over Expense :"+query);
			 	int isUpdated=statement.executeUpdate(query);

				}

			}
			catch(Exception ex)
			{

				LOGGER.error("Exp="+ex.getMessage());
				throw new Exception();
			}

		}
   	}


    private String[] getFundList(String startDate,String endDate) throws Exception
	{
		String fundCondition="";
		String fundCondition1="";
		String fundId="";
		String reqFundId[];
		if(!fundId.equalsIgnoreCase(""))
		{
			fundCondition="AND vh.fundId="+fundId+" ";
			fundCondition1="WHERE transactionsummary.fundId="+fundId+" ";
		}
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY );
			String	query = " select f.id,f.name from fund f where (f.id in(SELECT unique vh.fundId FROM chartofaccounts  coa,generalledger gl, "
			  +" voucherHeader vh WHERE (coa.TYPE = 'A' OR coa.TYPE = 'L')  and vh.ID =  gl.VOUCHERHEADERID AND  gl.glcode=coa.glcode "
			  +"  AND vh.status<>4  AND vh.VOUCHERDATE >= '"+startDate+"' AND vh.VOUCHERDATE <= '"+endDate+"' "+fundCondition+") or  f.id in(select unique fundid from transactionsummary "+fundCondition1+")) and f.isactive='1' and f.isnotleaf!='1' "
			  +" order by f.id ";

	 		resultset = statement.executeQuery(query);
	 		int resSize=0,i=0;
	 		if(resultset.last())
	 		resSize=resultset.getRow();
	 		reqFundId=new String[resSize];
	 //	 	Fund fu=null;
	 		resultset.beforeFirst();
	 		while(resultset.next())
	 		{
	 			reqFundId[i]=resultset.getString(1);
	 			i++;
			}
	 	}
		catch(Exception  ex)
		{
			LOGGER.error("Exp="+ex.getMessage());
			throw new Exception();
		}
		return reqFundId;
	}
}
