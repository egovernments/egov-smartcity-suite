/*
 * Created on Apr 5, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.transactions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.exilant.GLEngine.AccountDetailType;
import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;
import com.exilant.GLEngine.TransaxtionParameter;
import com.exilant.eGov.src.common.DataValidator;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.BankAccount;
import com.exilant.eGov.src.domain.BankReconciliation;
import com.exilant.eGov.src.domain.VoucherDetail;
import com.exilant.eGov.src.domain.VoucherHeader;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.dataservice.DataExtractor;

public class BankEntry extends AbstractTask{
	private static final Logger LOGGER = Logger.getLogger(BankEntry.class);
	private Connection connection;
	private Statement statement;
	private ResultSet resultset;
	private TaskFailedException taskExc;

	private int voucherHeaderId=0;
	private double accountBalance[];
	private HashMap detailTypes;
	public BankEntry(){}
	String voucherNumber;
	public String fiscalPid;
		public String vType;
		public String egVoucher;
	public String cgNum;
	EGovernCommon cm = new EGovernCommon();
	public void execute(String taskName,
							String gridName,
							DataCollection dc,
							Connection conn,
							boolean erroOrNoData,
							boolean gridHasColumnHeading, String prefix) throws TaskFailedException{
		this.connection = conn;
		ArrayList transactions = new ArrayList();

		//LOGGER.debug("**************************************************************");
		//LOGGER.debug("CG Number : " + dc.getValue("voucherHeader_cgn"));
		//LOGGER.debug("voucher Number : " + dc.getValue("voucherHeader_voucherNumber"));
		//LOGGER.debug("voucher Date : " + dc.getValue("voucherHeader_voucherDate"));
		//LOGGER.debug("fund Id : " + dc.getValue("fund_id"));
		//LOGGER.debug("department Id : " + dc.getValue("department_id"));
		//LOGGER.debug("voucher Number : " + dc.getValue("voucherHeader_description"));
		//LOGGER.debug("**************************************************************");
		String gridBankEntry[][] = dc.getGrid("gridBankEntry");
		for(int i=0; i<gridBankEntry.length; i++){
			////LOGGER.debug(gridBankEntry[i][0] + "  " + gridBankEntry[i][1] + "  " + gridBankEntry[i][2] + "  " + gridBankEntry[i][3] + "  " + gridBankEntry[i][4]);
		}
		//LOGGER.debug("**************************************************************");
		//LOGGER.debug("bank_id : " + dc.getValue("bank_id"));
		//LOGGER.debug("bankAccount_id : " + dc.getValue("bankAccount_id"));
		//LOGGER.debug("bankGLCode : " + dc.getValue("bankGLCode"));


		/*	if(!validate(dc)) {
			dc.addMessage("exilRPError","Server Validation Failed..Check The Data Sent");
			throw  taskExc;
		}	*/




		try{
			String voucherHeader_voucherDate;
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		//	Date dt=new Date();
			String vdt=(String)dc.getValue("voucherHeader_voucherDate");
		//	dt = sdf.parse( vdt );
			voucherHeader_voucherDate = formatter.format( sdf.parse( vdt ));

			//fiscalPid=getFiscalPeriod(dc.getValue("voucherHeader_voucherDate"));
			fiscalPid=cm.getFiscalPeriod(voucherHeader_voucherDate);
			String fId=dc.getValue("fund_id").toString();
			voucherNumber=cm.vNumber(dc, conn,fId,dc.getValue("voucherHeader_voucherNumber"));
			vType=voucherNumber.substring(0,2);
			//LOGGER.debug("aaaaaaaaa  "+vType+"   bbbbbbbb"+voucherNumber);
			egVoucher=cm.getEg_Voucher(vType,fiscalPid,conn);
			for(int i=egVoucher.length();i<5;i++)
			{
				 egVoucher="0"+egVoucher;
			}
			cgNum=vType+egVoucher;
			//LOGGER.debug("aaaaaaaaa  "+cgNum);
			if(!cm.isUniqueVN(dc, conn,voucherNumber))
			throw new TaskFailedException();
			postInVoucherHeader(dc,conn);
			postInBankAccount(dc);
			postInBankReconciliation(dc);
			postInVoucherDetail(dc, transactions,conn);
			/*added the necessary data to each of objects*/
    		transactions=addRequiredDataToList(transactions,dc);

    		/*if( validation by chartofaccountengine returns true)
    		post the data in voucher header*/
    		ChartOfAccounts engine=ChartOfAccounts.getInstance();
    		Transaxtion txnList[]=new Transaxtion[transactions.size()];
    		txnList=(Transaxtion[])transactions.toArray(txnList);
    		if(!engine.postTransaxtions(txnList, connection, dc)){
    			//dc.addMessage("exilRPError","Engine Validation Failed");
    			throw taskExc;
    		}
    		/* End of GLPosting */
    		dc.addMessage("eGovSuccess","Bank Entry : CGNumber is "+cgNum+" and VoucherNumber is "+voucherNumber);
    	}catch(Exception sqlex){
    		dc.addMessage("exilRPError","Transaction Failed :"+sqlex.getMessage());
    		throw taskExc;
		}
	}

	private void postInVoucherHeader(DataCollection dc,Connection conn) throws SQLException,TaskFailedException	{
		VoucherHeader vh = new VoucherHeader();
		vh.setCgn(dc.getValue("voucherHeader_cgn").toString());
		String voucherHeader_voucherDate="";
   		try
   		{

   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		//	Date dt=new Date();
			String vdt=(String)dc.getValue("voucherHeader_voucherDate");
		//	dt = sdf.parse( vdt );
			voucherHeader_voucherDate = formatter.format(sdf.parse( vdt ));


   		}
   		catch(Exception e){throw new TaskFailedException(e.getMessage());}
		//vh.setVoucherDate(dc.getValue("voucherHeader_voucherDate"));
   		vh.setVoucherDate(voucherHeader_voucherDate);
		vh.setName("Bank Entry");
		vh.setType("Journal Voucher");
		vh.setFundId(dc.getValue("fund_id"));
		vh.setDepartmentId(dc.getValue("department_id"));
		vh.setVoucherNumber(voucherNumber);
		vh.setDescription(dc.getValue("voucherHeader_description"));
		//vh.setFiscalPeriodId(getFiscalPeriod(dc.getValue("voucherHeader_voucherDate")));
		vh.setFiscalPeriodId(cm.getFiscalPeriod(voucherHeader_voucherDate));
		vh.setCgvn(cgNum);
	//	vh.setFundId((String)dc.getValue("fund_id"));
	//	vh.setFunctionId((String)dc.getValue("function_id"));
	//	vh.setFundSourceId((String)dc.getValue("fundSource_id"));
		vh.setCreatedby(dc.getValue("current_UserID"));
		vh.insert(connection);
		voucherHeaderId = vh.getId();
	}

	private void postInBankAccount (DataCollection dc) throws TaskFailedException
	{
		BankAccount ba = new BankAccount();
	   	try{
	   		statement = connection.createStatement();
	   		resultset = statement.executeQuery("select currentBalance from bankAccount where id = " + dc.getValue("bankAccount_id") + " for update");
	   		resultset.next();
	   		double balAvailable = resultset.getDouble("currentBalance");
	   		resultset = null;

	   		String gridBankEntry[][] = dc.getGrid("gridBankEntry");
	   		//String bankGLCode = dc.getValue("bankGLCode");
	   		accountBalance = new double[gridBankEntry.length];
	   		//LOGGER.debug("********************** BANK ACCOUNT ****************************");
	   		//LOGGER.debug("balAvailable: "+balAvailable);
	   		for(int i=0; i<gridBankEntry.length; i++){
	   			if(gridBankEntry[i][0].equalsIgnoreCase("")) continue;
	   			gridBankEntry[i][2] = gridBankEntry[i][2].equalsIgnoreCase("")?"0":gridBankEntry[i][2];
	   			gridBankEntry[i][3] = gridBankEntry[i][3].equalsIgnoreCase("")?"0":gridBankEntry[i][3];

	   			if(!gridBankEntry[i][2].equalsIgnoreCase("0"))
	   				balAvailable = ExilPrecision.convertToDouble(balAvailable - Double.parseDouble(gridBankEntry[i][2]),2);
	   			if(!gridBankEntry[i][3].equalsIgnoreCase("0"))
	   				balAvailable = ExilPrecision.convertToDouble(balAvailable + Double.parseDouble(gridBankEntry[i][3]),2);
	   			LOGGER.debug("bal at " + i + ": " + balAvailable + "    " + "dr: " + gridBankEntry[i][2]+"   cr: " + gridBankEntry[i][3] );
	   			accountBalance[i] = balAvailable;
	   		}

	   		//LOGGER.debug("********************** BANK ACCOUNT ****************************");
	   		//if insufficient balance, throw the TaskFailedException
   			if(balAvailable < 0){
   				dc.addMessage("exilError","Insufficient Balace: " + "balance available is " + balAvailable);
   				throw taskExc;
   			}

	   		ba.setId(dc.getValue("bankAccount_id"));
	   		ba.setCurrentBalance(balAvailable + "");
	   		ba.update(connection);
	   	}catch(SQLException sqlEx){
	   		dc.addMessage("exilError","no record for account id " + dc.getValue("bankAccount_id") + " in Bankaccount");
			throw taskExc;
	   	}
	}

	private void postInBankReconciliation(DataCollection dc) throws TaskFailedException
	{
		BankReconciliation rec = new BankReconciliation();
		//rec.setVoucherHeaderId(voucherHeaderId+"");
		rec.setBankAccountId(dc.getValue("bankAccount_id"));
		//rec.setIsReconciled("1");
		//rec.setChequeNumber("bank entry");

		String voucherHeader_voucherDate="";
   		try
   		{

   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			Date dt=new Date();
			String vdt=(String)dc.getValue("voucherHeader_voucherDate");
			dt = sdf.parse( vdt );
			voucherHeader_voucherDate = formatter.format(dt);
 		}catch(Exception e){throw new TaskFailedException(e.getMessage());}

		//rec.setReconciliationDate(dc.getValue("voucherHeader_voucherDate"));
   		//rec.setReconciliationDate(voucherHeader_voucherDate);
		String gridBankEntry[][] = dc.getGrid("gridBankEntry");
		String amount="", txnType="";
   		for(int i=0; i<gridBankEntry.length; i++){
   			if(gridBankEntry[i][0].equalsIgnoreCase("")) continue;
   			gridBankEntry[i][2] = gridBankEntry[i][2].equalsIgnoreCase("")?"0":gridBankEntry[i][2];
   			gridBankEntry[i][3] = gridBankEntry[i][3].equalsIgnoreCase("")?"0":gridBankEntry[i][3];

   			if(ExilPrecision.convertToDouble(gridBankEntry[i][2],2)>0){
   				amount = gridBankEntry[i][2];
   				txnType = "Cr";  //bank account will be credited
   			}else{
   				amount = gridBankEntry[i][3];
   				txnType = "Dr";  //bank account will be debited
   			}
   			rec.setAmount(amount);
   			//rec.setTransactionBalance(accountBalance[i] + ""); removed added to instrument
   			rec.setTransactionType(txnType);
   			

   			try{
   				rec.insert(connection);
   			}catch(SQLException sqlEx){
   				dc.addMessage("exilError","insertion failed in BankReconciliation");
   				throw taskExc;
   		   	}
   		}
	}

	private void postInVoucherDetail(DataCollection dc,
										ArrayList transactions,Connection conn) throws TaskFailedException, SQLException{

		/***********************************************
		* if Bank Balance > Account Balance then Credit: Account Head, Debit: Bank Account
		* if Bank Balance < Account Balance then Debit: Account Head, Credit: Bank Account
		***********************************************/

		VoucherDetail vd = new VoucherDetail();
		vd.setVoucherHeaderID(voucherHeaderId + "");

		String gridBankEntry[][] = dc.getGrid("gridBankEntry");
   		for(int i=0; i<gridBankEntry.length; i++){
   			if(gridBankEntry[i][0].equalsIgnoreCase("")) continue;
   			gridBankEntry[i][2] = gridBankEntry[i][2].equalsIgnoreCase("")?"0":gridBankEntry[i][2];
   			gridBankEntry[i][3] = gridBankEntry[i][3].equalsIgnoreCase("")?"0":gridBankEntry[i][3];

   			/******************* account code entry ********************/
   			vd.setGLCode(gridBankEntry[i][0]);
   			vd.setAccountName(gridBankEntry[i][1]);
   			vd.setDebitAmount(gridBankEntry[i][2]);
   			vd.setCreditAmount(gridBankEntry[i][3]);
   			vd.setNarration(gridBankEntry[i][4]);
   			statement = connection.createStatement();
   			resultset = statement.executeQuery("select max(LineId)+1 as LineId from VoucherDetail");
   			resultset.next();
   			vd.setLineID(resultset.getInt("LineId") + "");
   			resultset = null;
   			vd.insert(connection);

   			Transaxtion transaction = new Transaxtion();
   			transaction.setGlCode(gridBankEntry[i][0]);
   			transaction.setGlName(gridBankEntry[i][1]);
   			transaction.setDrAmount(gridBankEntry[i][2]);
   			transaction.setCrAmount(gridBankEntry[i][3]);
   			transaction.setNarration(gridBankEntry[i][4]);
   			//transaction.setTransaxtionParam();
   			transaction.setVoucherLineId(String.valueOf(vd.getId()));
   			transaction.setVoucherHeaderId(voucherHeaderId+"");
   			transactions.add(transaction);
   			/***********************************************************/

   			/******************* bank account entry ********************/
   			vd.setGLCode(dc.getValue("bankGLCode"));
   			vd.setAccountName(cm.getCodeName(dc.getValue("bankGLCode"), conn));
   			vd.setDebitAmount(gridBankEntry[i][3]);   //debit to bank account
   			vd.setCreditAmount(gridBankEntry[i][2]);  //credit to bank account
   			vd.setNarration(gridBankEntry[i][4]);
   			statement = connection.createStatement();
   			resultset = statement.executeQuery("select max(LineId)+1 as LineId from VoucherDetail");
   			resultset.next();
   			vd.setLineID(resultset.getInt("LineId") + "");
   			resultset = null;
   			vd.insert(connection);

   			Transaxtion transaction1 = new Transaxtion();
   			transaction1.setGlCode(dc.getValue("bankGLCode"));
   			transaction1.setGlName(cm.getCodeName(dc.getValue("bankGLCode"), conn));
   			transaction1.setDrAmount(gridBankEntry[i][3]);
   			transaction1.setCrAmount(gridBankEntry[i][2]);
   			transaction1.setNarration(gridBankEntry[i][4]);
   			//transaction.setTransaxtionParam();
   			transaction1.setVoucherLineId(String.valueOf(vd.getId()));
   			transaction1.setVoucherHeaderId(voucherHeaderId+"");
   			transactions.add(transaction1);
   			/***********************************************************/
   		}

		if(transactions.size()<=0){
			dc.addMessage("exilRPError","No Records To Insert. Fill the Data");
			throw taskExc;
		}
	}

	public boolean validate(DataCollection dc){
   		DataValidator valid = new DataValidator();
   		if(!valid.checkAccount(dc.getValue("bankAccount_id"), connection)){
   			dc.addMessage("exilInvalidID", dc.getValue("bankAccount_id"));
   			return false;
   		}
   		if(!valid.checkDepartmentId(dc.getValue("department_id"), connection)){
   			dc.addMessage("exilInvalidID", dc.getValue("department_id"));
   			return false;
   		}
   		if(!valid.checkFundId(dc.getValue("fund_id"), connection)){
   			dc.addMessage("exilInvalidID", dc.getValue("fund_id"));
   			return false;
   		}
   		return true;
   	}

	
	public ArrayList addRequiredDataToList(ArrayList transactions, DataCollection dc) throws TaskFailedException{
		for(int i=0;i<transactions.size();i++){
			ArrayList reqData=getRequiredData(dc,i+1);
			Transaxtion trnDetail=(Transaxtion)transactions.get(i);
			trnDetail.setTransaxtionParam(reqData);
		}
		return transactions;
	}
 
	public ArrayList getRequiredData(DataCollection dc,int rowIdx) throws TaskFailedException{
		DataExtractor de=DataExtractor.getExtractor();
		String sql="select ID as \"ID\",name as  \"name\",tableName as \"tableName\","+
		"description as \"description\",columnName as \"columnName\",attributeName as \"attributeName\""+
		",nbrOfLevels as  \"nbrOfLevels\" from accountdetailtype";
		if(detailTypes==null){
			detailTypes=de.extractIntoMap(sql,"ID",AccountDetailType.class);
		}
		ArrayList reqParams=new ArrayList();
		String[][] entityGrid=dc.getGrid("entities_grid");
		for(int i=1;i<entityGrid.length;i++){
			//search entities only for the rowindex sent
			//entityGrid[i][0]-dettypeid entityGrid[i][1]-detkey  entityGrid[i][2]-detvalue entityGrid[i][3]-rowIndex
			if(entityGrid[i][3].equals(String.valueOf(rowIdx))){
				AccountDetailType accType=(AccountDetailType)detailTypes.get(entityGrid[i][0]);
				if(accType!=null){
					TransaxtionParameter reqData=new TransaxtionParameter();
					reqData.setDetailName(accType.getAttributeName());
					reqData.setDetailKey(entityGrid[i][1]);
					reqParams.add(reqData);
				}
			}
		}
		return reqParams;
	}
}