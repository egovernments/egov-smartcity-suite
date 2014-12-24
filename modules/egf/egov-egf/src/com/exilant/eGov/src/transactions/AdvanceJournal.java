/**
 * Created on Jan 13, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.transactions;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;
import com.exilant.GLEngine.TransaxtionParameter;
import com.exilant.eGov.src.common.DataValidator;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.*;
import com.exilant.exility.common.*;
import com.exilant.eGov.src.common.GlReverser;
import org.egov.infstr.utils.EGovConfig;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.math.BigDecimal;
import org.apache.log4j.Logger;
import org.egov.utils.FinancialConstants;

public class AdvanceJournal extends AbstractTask{
    private final static Logger LOGGER=Logger.getLogger(AdvanceJournal.class);
	private Connection connection;
	private Statement statement;
	private ResultSet resultset;
	private int bankId=0, branchId=0, contractorId=0, supplierId=0;
	private BigDecimal accountBalance=new BigDecimal("0.00");  //for reconciliation
	private TaskFailedException taskExc;
	String voucherNumber;
	public String fiscalPid;
		public String vType;
		public String egVoucher;
	public String cgNum;
	public String fId;
	public String newVcNo;
	public double modifiedAmt;
	private int voucherHeaderId;
	DataCollection dataCol=null;
	private static int vouchertypelength=Integer.parseInt(FinancialConstants.VOUCHERNO_TYPE_LENGTH);
	ArrayList transactions = new ArrayList();
	EGovernCommon cm = new EGovernCommon();
	CommonMethodsI cmImpl=new CommonMethodsImpl();

	//PreDefinedAccCodes codes = new PreDefinedAccCodes();
	String interFundPayment=null;
	String interFundJV=null;
	String refCGN=null;
	/*	  this method is called by Exility	*/
	
	public void execute(String taskName,
							String gridName,
							DataCollection dc,
							Connection conn,
							boolean erroOrNoData,
							boolean gridHasColumnHeading, String prefix) throws TaskFailedException{
		
		// need to resolve this later
		/*

		dataCol=dc;
		connection = conn;
		taskExc = new TaskFailedException();
		ArrayList transactions = new ArrayList();
		//If the funds are different then the Debit codes will differ.
		if(dc.getValue("showGlEntries").equalsIgnoreCase("show")&& dc.getValue("showJVGlEntries").equalsIgnoreCase("no") )
		{
			String code1="",name1="";
			StringBuffer str=new StringBuffer(";");
			Fund fundPaymentobj=new Fund();
			try{
				//Get the Interfund transfer account for Work order Fund
				interFundPayment=fundPaymentobj.getInterfundCode(dc.getValue("fund_id"),conn);
			}catch(Exception e)
			{
				LOGGER.debug("Exp="+e.getMessage());
				throw taskExc;
			}
			if(interFundPayment==null)
				throw new TaskFailedException("Inter Fund Transfer account is not defined for Work order Fund.");
			LOGGER.debug("Intefund transfer -payment is:"+interFundPayment);
			//If different the debit code will be Interfund transfer account for Work order Fund
			if(!dc.getValue("fund_id").equals(dc.getValue("fund1_id")))
			{
				String accName="",glcodeIF="";
				if(interFundPayment!=null)
				{
					String accInfo=getAccountById(interFundPayment);
					if(accInfo!=null){
						StringTokenizer str1=new StringTokenizer(accInfo,"~!");
						while (str1.hasMoreTokens()) {
							 accName=str1.nextToken();
							 glcodeIF=str1.nextToken();
						}
					}
				}
				code1=glcodeIF;
				str=str.append("debit"+"^");
				str=str.append(code1+"^");
				name1 = accName;
				str=str.append(name1+";");
			}
			//if funds are same it will be Advance a/c
			else{
				if("contractor".equals(((String)dc.getValue("typeOfPayment")).toLowerCase())) {
				//	code1=EGovConfig.getProperty("egf_config.xml","ConAdvCode","","ContractorCodes");
					code1=EGovConfig.getProperty("egf_config.xml","ConAdvCode","","ContractorCodes");
					str=str.append("debit"+"^");
					str=str.append(code1+"^");
					name1 = cm.getCodeName(code1,conn);
					str=str.append(name1+";");
					}else{
					//code1=EGovConfig.getProperty("egf_config.xml","SupAdvCode","","SupplierCodes");
					code1=EGovConfig.getProperty("egf_config.xml","SupAdvCode","","SupplierCodes");
					str=str.append("debit"+"^");
					str=str.append(code1+"^");
					name1 = cm.getCodeName(code1,conn);
					str=str.append(name1+";");
			   	}


			}
			ChartOfAccounts cOfAcc = ChartOfAccounts.getInstance();
			String code2 = cOfAcc.getGLCode("bankAccount_id",dc.getValue("accountNumberId"),connection);
			str=str.append("credit"+"^");
			str=str.append(code2+"^");
			String name2 = cm.getCodeName(code2,conn);
			str=str.append(name2+";");
			String outp=str.toString();
			dc.addValue("glCodeName",outp);
			return;
		}
		//This is for Show JV Entry
		if(dc.getValue("showGlEntries").equalsIgnoreCase("show")&& dc.getValue("showJVGlEntries").equalsIgnoreCase("yes") )
		{
			String code1="",name1="";
			StringBuffer str=new StringBuffer(";");
			Fund fundPaymentobj=new Fund();

//			Now we need to get the debit code of JV
			if("contractor".equals(((String)dc.getValue("typeOfPayment")).toLowerCase())) {
				code1=EGovConfig.getProperty("egf_config.xml","ConAdvCode","","ContractorCodes");
				str=str.append("debit"+"^");
				str=str.append(code1+"^");
				name1 = cm.getCodeName(code1,conn);
				str=str.append(name1+";");
				}else{
				code1=EGovConfig.getProperty("egf_config.xml","SupAdvCode","","SupplierCodes");
				str=str.append("debit"+"^");
				str=str.append(code1+"^");
				name1 = cm.getCodeName(code1,conn);
				str=str.append(name1+";");
		   	}

			//Now we need to get the Credit code for JV
			try{
				//Get the Interfund transfer account for Work order Fund
				interFundJV=fundPaymentobj.getInterfundCode(dc.getValue("fund1_id"),conn);
			}catch(Exception e)
			{
				LOGGER.debug("Exp="+e.getMessage());
				throw taskExc;
			}
			if(interFundJV==null)
				throw new TaskFailedException("Inter Fund Transfer account is not defined for Bank Fund.");
			LOGGER.debug("Intefund transfer -JV is:"+interFundJV);
			String accName="",glcodeIF="";
			if(interFundJV!=null)
			{
				String accInfo=getAccountById(interFundJV);
				if(accInfo!=null){
					StringTokenizer str1=new StringTokenizer(accInfo,"~!");
					while (str1.hasMoreTokens()) {
						 accName=str1.nextToken();
						 glcodeIF=str1.nextToken();
					}
				}
			}
			code1=glcodeIF;
			str=str.append("credit"+"^");
			str=str.append(code1+"^");
			name1 = accName;
			str=str.append(name1+";");
			String outp=str.toString();
			dc.addValue("glCodeName",outp);
			return;
		}
		fId=dc.getValue("fund_id").toString();
		if(dc.getValue("modeOfExec").equalsIgnoreCase("modify"))
		{
				reverse(dc,conn);
				return;
	   }
		else if(dc.getValue("modeOfExec").equalsIgnoreCase("edit")){
	  	 	editVoucher(dc,conn);
			return;
		 }

		try	{
			if(!validate(dc)) {
   				dc.addMessage("exilRPError","Server Validation Failed..Check The Data Sent");
   				throw  taskExc;
   			} 
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			Date dt=new Date();
			String vdt=(String)dc.getValue("voucherHeader_voucherDate");
			dt = sdf.parse( vdt );
			String voucherHeader_voucherDate = formatter.format(dt);
			String bankAccId=dc.getValue("accountGroup_accountNumberId");
			String chequeNo=dc.getValue("chequeDetail_chequeNumber");
			int CurdateCheck=1;
			 try{
			 	CurdateCheck=cm.isCurDate(dc,conn,vdt);
			}
			catch(Exception e){throw new TaskFailedException(e.getMessage());}
			if(CurdateCheck==0)
				return;
// Checks for Bank Balance Sufficiency
	//		double balAvailable =cm.getAccountBalance(dc,connection,dc.getValue("accountGroup_accountNumberId")) ;
   			accountBalance = cm.getAccountBalance(dc,connection,dc.getValue("accountGroup_accountNumberId")).subtract(new BigDecimal(dc.getValue("contracotrBillDetail_advanceAmount")));
   			accountBalance =accountBalance.setScale(2, BigDecimal.ROUND_HALF_UP);
//if insufficient balance, throw the TaskFailedException
            *//** Commenting for Time-being **//*
   			if(accountBalance.doubleValue() < 0)
   			{
   				dc.addMessage("exilError","Insufficient Balace: " + "Balance available After Transaction is " + accountBalance);
   				throw taskExc;
   			}
   			//fiscalPid=getFiscalPeriod(dc.getValue("voucherHeader_voucherDate"));
			fiscalPid=cm.getFiscalPeriod(voucherHeader_voucherDate);
				We need to check if the Work order fund and the Bank Fund is same.
			 * If same then we need to pass a payment voucher.
			 * If the funds are same then we need to pass a Journal voucher and a payment voucher
			 
			String bankFundId=dc.getValue("fund1_id");
			LOGGER.debug("Bank Fundid is :"+bankFundId+" Work order fund id is :"+fId);
			//If Funds are different
			if(!bankFundId.equals(fId))
			{
				//pass jv and get the cgn
			//	FinancialTransactions ftxn=new FinancialTransactions();
				Voucherheader vobj=new Voucherheader();
				vobj=passInterFundJV(dc,conn);
				refCGN=vobj.getCgn();

				//Set the debit code for payment voucher.
				//It will Interfund Transfer account of Work order fund
				Fund fundPaymentobj=new Fund();

				interFundPayment=fundPaymentobj.getInterfundCode(dc.getValue("fund_id"),conn);
				if(interFundPayment==null)
					throw new TaskFailedException("Inter Fund Transfer account is not defined for Work order Fund.");
				LOGGER.debug("Intefund transfer -payment is:"+interFundPayment);
			}

			//voucherNumber=cm.vNumber(dc, conn,bankFundId,dc.getValue("voucherHeader_voucherNumber"));
			voucherNumber = cm.getVoucherNumber(bankFundId, FinancialConstants.PAYMENT_VOUCHERNO_TYPE, dc.getValue("voucherHeader_voucherNumber"), conn);
			vType=voucherNumber.substring(0,vouchertypelength);

			egVoucher=cm.getEg_Voucher(vType,fiscalPid,conn);
			for(int i=egVoucher.length();i<5;i++)
			{
				 egVoucher="0"+egVoucher;
			}
			cgNum=vType+egVoucher;

			VoucherTypeForULB voucherType=new VoucherTypeForULB();
			String vTypeForULB=(String)voucherType.readVoucherTypes("Payment");
			if(vTypeForULB.equalsIgnoreCase("Auto"))
			{
				voucherNumber=cgNum;
			}
			if(!cm.isUniqueVN(dc, conn,voucherNumber))
				throw new TaskFailedException();
			if(!cm.isUniqueChequeNo(chequeNo,bankAccId,dc, conn))
				throw new TaskFailedException();
			if(!cm.isChqNoWithinRange(chequeNo,bankAccId,dc, conn))
				throw new TaskFailedException();
			postInVoucherHeader(dc,conn);
			int chequeDetailId=postInChequeDetail(dc);
			if(chequeDetailId==0){
				dc.addMessage("Cheque Detail Insertion Failed");
				throw taskExc;
			}
			if("contractor".equals( (dc.getValue("subLedgerPaymentHeader_typeOfPayment")).toLowerCase())){
				contractorId = getContraSupplierId(dc);
			}else{
				supplierId = getContraSupplierId(dc);
			}
			postInSubLedgerPaymentHeader(dc,chequeDetailId);
            postInBankReconciliation(dc);
			postInVoucherDetail(dc, transactions,conn);
			updateInWorksDetail(dc);
			added the necessary data to each of objects
    		transactions=addRequiredDataToList(transactions,dc);

    		if( validation by chartofaccountengine returns true)
    		post the data in voucher header
    		ChartOfAccounts engine=ChartOfAccounts.getInstance();
    		Transaxtion txnList[]=new Transaxtion[transactions.size()];
    		txnList=(Transaxtion[])transactions.toArray(txnList);
    		if(!engine.postTransaxtions(txnList, connection, dc)){
    			//dc.addMessage("exilRPError","Engine Validation Failed");
    			throw taskExc;
    		}
    		cm.UpdateVoucherStatus(dc,"Payment",conn,voucherHeaderId);
    		LOGGER.debug("Insert vouchermis...");
    		updateVoucherMIS("I");
    		dc.addValue("voucherHeader_voucherNumber", voucherNumber);
    		dc.addMessage("eGovSuccess","Advance Payment : CGNumber is "+cgNum+" and VoucherNumber is "+voucherNumber);
    		}catch(Exception sqlex){
    		dc.addMessage("exilRPError","Transaction Failed :"+sqlex.getMessage());
    		throw taskExc;
		}
	*/}

	/*private void getResultSet(String tableName, String condition, DataCollection dc) throws TaskFailedException{
		try{
			statement = connection.createStatement();
			resultset = statement.executeQuery("select * from " + tableName + " where 1=1 " + condition);
			resultset.next();
		}
		catch(SQLException sqlex){
			dc.addMessage("exilNoData","select * from " + tableName + " where 1=1 " + condition);
			throw taskExc;
		}
	}*/

	public boolean validate(DataCollection dc) throws TaskFailedException,SQLException{
   		DataValidator valid = new DataValidator();
   		if(!valid.checkFundSourceId(dc.getValue("fundSource_id"), connection)){
   			dc.addMessage("exilInvalidID", dc.getValue("fundSource_id"));
   			return false;
   		}
   		if(!valid.checkFundId(dc.getValue("fund_id"), connection)){
   			dc.addMessage("exilInvalidID", dc.getValue("fund_id"));
   			return false;
   		}
   		if(!valid.checkAccount(dc.getValue("accountGroup_accountNumberId"), connection)){
   			dc.addMessage("exilInvalidID", dc.getValue("accountGroup_accountNumberId"));
   			return false;
   		}
   		Statement st=null;
   		ResultSet rset=null;
   		try{
   			st=connection.createStatement();
   			rset=st.executeQuery("select advancepayable from worksDetail where id="+dc.getValue("worksDetail_id"));
   			if(rset.next()){
   				double maxAdvance=ExilPrecision.convertToDouble(rset.getString(1),2);
   				double inputAdvance=ExilPrecision.convertToDouble(dc.getValue("contracotrBillDetail_advanceAmount"), 2);
   				if(inputAdvance>maxAdvance){
   					dc.addMessage("Advance given is greater than max Advance");
   					throw new TaskFailedException();
   				}
   			}
   		}catch(Exception e){
   			throw new TaskFailedException(e.toString());
   		}
   		finally{
   			rset.close();
   			st.close();
   		}
   		return true;
   	}

	private int getContraSupplierId(DataCollection dc) throws SQLException{
		int id=0;
		statement = connection.createStatement();
		resultset = statement.executeQuery("SELECT id FROM relation  where code='" + dc.getValue("contractor_supplier_code") + "'");
		resultset.next();
		id = resultset.getInt("ID");
		resultset.close();
		statement.close();
		return id;
	}

	private void postInVoucherHeader(DataCollection dc,Connection con) throws SQLException,TaskFailedException	{
		VoucherHeader vh = new VoucherHeader();
		vh.setCgn((String)dc.getValue("voucherHeader_cgn"));
		String voucherHeader_voucherDate="";
   		try
   		{

   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			String vdt=(String)dc.getValue("voucherHeader_voucherDate");
			voucherHeader_voucherDate = formatter.format(sdf.parse( vdt ));
   		}
   		catch(Exception e){throw new TaskFailedException(e.getMessage());}

		//vh.setVoucherDate((String)dc.getValue("voucherHeader_voucherDate"));
   		vh.setVoucherDate(voucherHeader_voucherDate);
		vh.setName("Advance Payment");
		vh.setType("Payment");
		vh.setVoucherNumber(voucherNumber);
		vh.setFundId(dc.getValue("fund1_id").toString());
		vh.setFundSourceId(dc.getValue("fundSource_id").toString());
		vh.setDepartmentId(dc.getValue("department_id").toString());
		vh.setDescription((String)dc.getValue("voucherHeader_narration"));
		vh.setFunctionId((String)dc.getValue("department_id"));
		vh.setFundSourceId((String)dc.getValue("fundSource_id"));
		vh.setCreatedby(dc.getValue("current_UserID"));
		vh.setFiscalPeriodId(cm.getFiscalPeriod(voucherHeader_voucherDate));
		vh.setCgvn(cgNum);
		if(refCGN!=null)
			vh.setRefCgn(refCGN);
		vh.insert(connection);
		voucherHeaderId = vh.getId();
	}

	 /**
	 * This function will update the voucher header in the modify option
	 * @param dc
	 * @param transactions
	 * @throws SQLException
	 * @throws TaskFailedException
	 * @author rashmi
	 * @serialData 15-Feb-2006
	 */

	private void updateInVoucherHeader(DataCollection dc,Connection con) throws SQLException,TaskFailedException{

		//First we need to see if the Fund is changing to check if
		//the new voucher number and cgvn number to be generated.
		VoucherHeader VH = new VoucherHeader();
		String oldFundid=cm.getFund(dc,con);
		String voucherHeader_voucherDate="";
		String oldVoucherDate="";
		try{
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			Date dt=new Date();
			String vdt=(String)dc.getValue("voucherHeader_voucherDate");
			dt = sdf.parse( vdt );
			voucherHeader_voucherDate = formatter.format(dt);
			String oldVdt=(String)dc.getValue("oldVoucherDate");
						dt = sdf.parse( oldVdt );
			oldVoucherDate = formatter.format(dt);
			}
			catch(Exception e)
			{
				LOGGER.error("Exp in date formating: "+e);
				throw new TaskFailedException();
			}
	//		if Fund is same then we can use the same voucher number and cgnumber
			LOGGER.debug("..voucherNumber.."+voucherNumber+"  .VoucherHeader_voucherNumber. "+dc.getValue("voucherHeader_voucherNumber"));
			LOGGER.debug("voucherHeader_voucherDate:  "+voucherHeader_voucherDate+"  oldVoucherDate:  "+oldVoucherDate);
			LOGGER.debug("oldFundid:  "+oldFundid+"  dc.getValue(fund_id):  "+dc.getValue("fund_id"));
		if((oldFundid.equals(dc.getValue("fund1_id").toString())) && (cm.getFiscalPeriod(voucherHeader_voucherDate).equalsIgnoreCase(cm.getFiscalPeriod(oldVoucherDate))))
		{
			LOGGER.debug("Funds are same.. and same fiscalperiod id");
			VH.setCgvn(dc.getValue("voucherHeader_cgn").toString());
			VH.setVoucherNumber(dc.getValue("voucherHeader_voucherNumber").toString());
			cgNum=dc.getValue("voucherHeader_cgn").toString();
			//voucherNumber=dc.getValue("voucherHeader_voucherNumber").toString();
		}
		else
		{
			try
			{
				fiscalPid=cm.getFiscalPeriod(voucherHeader_voucherDate);
				if(!(oldFundid.equals(dc.getValue("fund1_id").toString())))
				{
					//String voucherNum=dc.getValue("voucherHeader_voucherNumber").toString();
					String voucherNum=voucherNumber;
					voucherNum=voucherNum.substring(vouchertypelength);
					//voucherNumber=cm.vNumber(dc, con,dc.getValue("fund1_id").toString(),voucherNum);
					voucherNumber = cm.getVoucherNumber(dc.getValue("fund1_id").toString(), FinancialConstants.PAYMENT_VOUCHERNO_TYPE, voucherNum, con);
					vType=voucherNumber.substring(0,vouchertypelength);
					egVoucher=cm.getEg_Voucher(vType,fiscalPid,con);
					for(int i=egVoucher.length();i<5;i++)
					{
						 egVoucher="0"+egVoucher;
					}
					cgNum=vType+egVoucher;
					VoucherTypeForULB voucherType=new VoucherTypeForULB();
					String vTypeForULB=(String)voucherType.readVoucherTypes("Payment");
					if(vTypeForULB.equalsIgnoreCase("Auto"))
					{
						voucherNumber=cgNum;
					}    
					
					if(!cm.isUniqueVN(dc,con,voucherNumber))
					{
						throw new TaskFailedException();
					}
				}
				   
				VH.setCgvn(cgNum);
				VH.setVoucherNumber(voucherNumber);
			}
			catch(Exception e)
			{
				LOGGER.info("Exp in getting new VNo. "+e);
				dc.addMessage("exilRPError","Transaction Failed :"+e.getMessage());
				throw new TaskFailedException();
			}

		}
		VH.setCgn(dc.getValue("cgnNum").toString());
		VH.setDepartmentId(dc.getValue("department_id"));
		VH.setDescription((String)dc.getValue("voucherHeader_narration"));
		VH.setFundId(dc.getValue("fund1_id"));
		VH.setFundSourceId(dc.getValue("fundSource_id"));
		VH.setFunctionId((String)dc.getValue("department_id"));
		VH.setId((String)dc.getValue("voucherHeader_id"));
		VH.setVoucherDate(voucherHeader_voucherDate);
		//VH.setFunctionId((String)dc.getValue("department_name"));
		VH.setFiscalPeriodId(cm.getFiscalPeriod(voucherHeader_voucherDate));
		VH.setLastModifiedBy(dc.getValue("current_UserID"));
		VH.setName("Advance Payment");
		VH.setType("Payment");
		if(refCGN!=null)
			VH.setRefCgn(refCGN);
		else
			VH.setRefCgn("");
		VH.update(con);
		voucherHeaderId = VH.getId();

		//Insert Record in the egf_record_status table
		cm.UpdateVoucherStatus(dc,"Payment",con,voucherHeaderId);
		}

	/**
	 * need to fix thos later.
	 */
	
	/*private int postInChequeDetail(DataCollection dc) throws SQLException,TaskFailedException	{
		int cqId=0;
		ChequeDetail cd = new ChequeDetail();
		cd.setAccountNumberId(dc.getValue("accountGroup_accountNumberId").toString());
		cd.setAmount(ExilPrecision.convertToString(dc.getValue("contracotrBillDetail_advanceAmount"), 2));
		String bankAndBranch[] = (dc.getValue("bank_id").trim()).split("-");
		bankId = Integer.valueOf(bankAndBranch[0]).intValue();
		cd.setBankId( bankId + "" );
		branchId = Integer.valueOf(bankAndBranch[1]).intValue();
		cd.setBranchId( branchId + "" );
		String voucherHeader_voucherDate="";
		String chequeDetail_chequeDate="";
		try
   		{

   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			String cdt=(String)dc.getValue("chequeDetail_chequeDate");
			chequeDetail_chequeDate = formatter.format(sdf.parse(cdt));
			String vdt=(String)dc.getValue("voucherHeader_voucherDate");
			voucherHeader_voucherDate = formatter.format(sdf.parse(vdt));

   		}
   		catch(Exception e){throw new TaskFailedException(e.getMessage());}
		//cd.setChequeDate((String)dc.getValue("chequeDetail_chequeDate"));
   		cd.setChequeDate(chequeDetail_chequeDate);
		cd.setChequeNumber((String)dc.getValue("chequeDetail_chequeNumber"));
		cd.setNarration((String)dc.getValue("voucherHeader_narration"));
		//cd.setReceiptDate((String)dc.getValue("voucherHeader_voucherDate"));
		cd.setReceiptDate(voucherHeader_voucherDate);
		cd.setIsDeposited("1");
		cd.setIsPayCheque("1");
		cd.setChequeType("C");
		cd.setVoucherHeaderId(String.valueOf(voucherHeaderId));
		cd.insert(connection);
		cqId=cd.getId();
		return cqId;
	}
*/
	/**
	 * Update ChequeDetail
	 * @param dc
	 * @return
	 * @throws SQLException
	 */
	
	/**
	 * need to fix this issue later. 
	 */
	/*private int UpdateChequeDetail(DataCollection dc) throws SQLException,TaskFailedException	{
		int cqId=0;
		statement = connection.createStatement();
		ChequeDetail cd = new ChequeDetail();
		cd.setAccountNumberId(dc.getValue("accountGroup_accountNumberId").toString());
		cd.setAmount(ExilPrecision.convertToString(dc.getValue("contracotrBillDetail_advanceAmount"), 2));
		String bankAndBranch[] = (dc.getValue("bank_id").trim()).split("-");
		bankId = Integer.valueOf(bankAndBranch[0]).intValue();
		cd.setBankId( bankId + "" );
		branchId = Integer.valueOf(bankAndBranch[1]).intValue();
		cd.setBranchId( branchId + "" );
		String voucherHeader_voucherDate="";
		String chequeDetail_chequeDate="";
		try
   		{
   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			String cdt=(String)dc.getValue("chequeDetail_chequeDate");
			chequeDetail_chequeDate = formatter.format(sdf.parse(cdt));
			String vdt=(String)dc.getValue("voucherHeader_voucherDate");
			voucherHeader_voucherDate = formatter.format(sdf.parse(vdt));
   		}
   		catch(Exception e){throw new TaskFailedException(e.getMessage());}
		//cd.setChequeDate((String)dc.getValue("chequeDetail_chequeDate"));
   		cd.setChequeDate(chequeDetail_chequeDate);
		cd.setChequeNumber((String)dc.getValue("chequeDetail_chequeNumber"));
		cd.setNarration((String)dc.getValue("voucherHeader_narration"));
		//cd.setReceiptDate((String)dc.getValue("voucherHeader_voucherDate"));
		cd.setReceiptDate(voucherHeader_voucherDate);
		cd.setIsDeposited("1");
		cd.setIsPayCheque("1");
		String vhid=String.valueOf(voucherHeaderId);
		cd.setVoucherHeaderId(vhid);
		cd.setChequeType("C");

		resultset = statement.executeQuery("SELECT id from CHEQUEDETAIL where voucherheaderid = " + voucherHeaderId);
		if(resultset.next()){
			cd.setId(resultset.getInt("id") + "");
			cd.update(connection);
		}else{
			cd.insert(connection);
		}

		resultset.close();
   		statement.close();
		cqId=cd.getId();
		return cqId;
	}*/
	   private void postInBankReconciliation(DataCollection dc) throws TaskFailedException
	   {
	   		BankReconciliation rec = new BankReconciliation();
	   		rec.setAmount(dc.getValue("contracotrBillDetail_advanceAmount"));
	   		rec.setBankAccountId(dc.getValue("accountGroup_accountNumberId"));
	   		String chequeDetail_chequeDate="";
	   		try
	   		{
	   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				String vdt=(String)dc.getValue("chequeDetail_chequeDate");
				chequeDetail_chequeDate = formatter.format(sdf.parse(vdt));
	   		}
	   		catch(Exception e){throw new TaskFailedException(e.getMessage());}
	   		rec.setChequeDate(chequeDetail_chequeDate);
	   		rec.setChequeNumber(dc.getValue("chequeDetail_chequeNumber"));
	   		rec.setVoucherHeaderId(String.valueOf(voucherHeaderId));
	   		rec.setIsReconciled("0");
	   		rec.setTransactionBalance(accountBalance + "");
	   		rec.setTransactionType("Cr");
	   		rec.setType("C");
	   		try{
	   			rec.insert(connection);
	   		}catch(SQLException sqlEx){
	   			dc.addMessage("exilError","insertion failed in BankReconciliation");
				throw taskExc;
	   		}
	   }
	   private void UpdateBankReconciliation(DataCollection dc) throws TaskFailedException
	   {
		   // double balAvailable = cm.getAccountBalance(dc,connection,dc.getValue("accountGroup_accountNumberId"));
	   		BankReconciliation rec = new BankReconciliation();
	   		rec.setAmount(dc.getValue("contracotrBillDetail_advanceAmount"));
	   		rec.setBankAccountId(dc.getValue("accountGroup_accountNumberId"));
	   		String chequeDetail_chequeDate="";
	   		try
	   		{
	   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				String vdt=(String)dc.getValue("chequeDetail_chequeDate");
				chequeDetail_chequeDate = formatter.format(sdf.parse(vdt));
	   		}
	   		catch(Exception e){throw new TaskFailedException(e.getMessage());}
	   		rec.setChequeDate(chequeDetail_chequeDate);
	   		rec.setChequeNumber(dc.getValue("chequeDetail_chequeNumber"));
	   		rec.setVoucherHeaderId(String.valueOf(voucherHeaderId));
	   		rec.setIsReconciled("0");
	   		rec.setTransactionBalance(accountBalance + "");
	   		rec.setTransactionType("Cr");
	   		rec.setType("C");
	   		try{
	   			statement = connection.createStatement();
	   			ResultSet rs = statement.executeQuery("SELECT id from BankReconciliation where voucherheaderid = " + voucherHeaderId);
	   		if(rs.next()){
	   			rec.setId(rs.getInt("id") + "");
	   			rec.update(connection);
	   		}else{
	   			rec.insert(connection);
	   		}

	   		}
	   		catch(Exception e){
	   			throw new TaskFailedException(e.getMessage());
	   		}
	   }

	private void postInVoucherDetail(DataCollection dc,ArrayList transactions,Connection con) throws TaskFailedException, SQLException {
		/****  Supplier/Contractor account (Debited)  ****/
		double debitAmount=0, creditAmount=0;
		String glCode, accName;

		VoucherDetail vd = new VoucherDetail();
		int lineId=1;

		Transaxtion transaction = new Transaxtion();

		if("contractor".equals(((String)dc.getValue("subLedgerPaymentHeader_typeOfPayment")).toLowerCase())) {
			//glCode = codes.getConAdvPayCode();
			glCode=EGovConfig.getProperty("egf_config.xml","ConAdvCode","","ContractorCodes");
			//glCode=codes.getConPayCode();
			accName = cm.getCodeName(glCode,con);
		}else{
			//glCode = codes.getSupAdvPayCode();
			glCode=EGovConfig.getProperty("egf_config.xml","SupAdvCode","","SupplierCodes");
			//glCode=codes.getSupPayCode();
			accName = cm.getCodeName(glCode,con);
		}
		if(interFundPayment!=null)
		{
			String accInfo=getAccountById(interFundPayment);
			if(accInfo!=null){
				String glcodeIF=null;
				StringTokenizer str=new StringTokenizer(accInfo,"~!");
				while (str.hasMoreTokens()) {
					accName=str.nextToken();
					glcodeIF=str.nextToken();
					vd.setAccountName(accName);
					vd.setGLCode(glcodeIF);
					transaction.setGlCode(glcodeIF);
					transaction.setGlName(accName);

					}
			}
		}
		else{
		vd.setGLCode(glCode);
		vd.setAccountName(accName);
		transaction.setGlCode(glCode);
		transaction.setGlName(accName);
		}
		vd.setVoucherHeaderID(voucherHeaderId + "");

		vd.setCreditAmount("0");
		vd.setDebitAmount(ExilPrecision.convertToString(dc.getValue("contracotrBillDetail_advanceAmount"), 2));
		vd.setLineID(lineId + "");
		vd.setNarration((String)dc.getValue("voucherHeader_narration"));
		vd.insert(connection);
		lineId++;


		transaction.setCrAmount("0");
		transaction.setDrAmount(ExilPrecision.convertToString(dc.getValue("contracotrBillDetail_advanceAmount"), 2));
		transaction.setVoucherLineId(String.valueOf(vd.getId()));
		transaction.setVoucherHeaderId(String.valueOf(voucherHeaderId));
		transaction.setNarration(dc.getValue("voucherHeader_narration"));
		transactions.add(transaction);
		debitAmount += ExilPrecision.convertToDouble(dc.getValue("contracotrBillDetail_advanceAmount"), 2);

		/****   Paid(bank) account (Credited)  ****/
		ChartOfAccounts cOfAcc = ChartOfAccounts.getInstance();
		glCode = cOfAcc.getGLCode("bankAccount_id",dc.getValue("accountGroup_accountNumberId"),connection);
		accName = cm.getCodeName(glCode,con);
		vd.setGLCode(glCode);
		vd.setAccountName(accName);
		vd.setCreditAmount(ExilPrecision.convertToString(dc.getValue("contracotrBillDetail_advanceAmount"), 2));
		vd.setDebitAmount("0");
		vd.setLineID(lineId + "");
		vd.setNarration((String)dc.getValue("voucherHeader_narration"));
		lineId++;
		vd.insert(connection);

		Transaxtion transactionBank = new Transaxtion();
		transactionBank.setGlCode(glCode);
		transactionBank.setGlName(accName);
		transactionBank.setCrAmount(ExilPrecision.convertToString(dc.getValue("contracotrBillDetail_advanceAmount"), 2));
		transactionBank.setDrAmount("0");
		transactionBank.setVoucherLineId(String.valueOf(vd.getId()));
		transactionBank.setVoucherHeaderId(String.valueOf(voucherHeaderId));
		transactionBank.setNarration(dc.getValue("voucherHeader_narration"));
		transactions.add(transactionBank);

		creditAmount += ExilPrecision.convertToDouble(dc.getValue("contracotrBillDetail_advanceAmount"), 2);

		if((Math.abs(debitAmount - creditAmount))>0.00000001){
			dc.addMessage("exilError","Debit amount mismatches Credit amount,  Transaction canceled...");
			throw taskExc;
		}
	}

private void  deleteFromVoucherDetail ()throws SQLException,TaskFailedException
{
	statement = connection.createStatement();
		LOGGER.info("select LineId  from VoucherDetail where voucherheaderid="+voucherHeaderId+" order by LineId");
		resultset = statement.executeQuery("select LineId from VoucherDetail where voucherheaderid="+voucherHeaderId+" order by LineId");
		int c=0;
		ArrayList vDetailLineId=new ArrayList();
		while(resultset.next())
		{
			vDetailLineId.add(c,(resultset.getString(1)));
			c++;
		}
		int count=vDetailLineId.size();
		if(count>0)
		{
			try
			{
				String delQuery="DELETE FROM voucherdetail WHERE voucherheaderid="+voucherHeaderId+" AND ID NOT IN (SELECT VOUCHERLINEID FROM GENERALLEDGER WHERE voucherheaderid="+voucherHeaderId+")";
				statement.executeUpdate(delQuery);

			}
			catch(Exception e)
			{
				throw new TaskFailedException("Exp in reading from generalledgerdetail: "+e);
			}
		}
		resultset.close();
		statement.close();



}
	private void UpdateVoucherDetail(DataCollection dc,ArrayList transactions,Connection con) throws TaskFailedException, SQLException {
			/****  Supplier/Contractor account (Debited)  ****/
			double debitAmount=0, creditAmount=0;
			String glCode, accName;
			//ArrayList vDetailId=new ArrayList();
			VoucherDetail vd = new VoucherDetail();
			int lineId=1;

			Transaxtion transaction = new Transaxtion();

			if("contractor".equals(((String)dc.getValue("subLedgerPaymentHeader_typeOfPayment")).toLowerCase())) {
				glCode=EGovConfig.getProperty("egf_config.xml","ConAdvCode","","ContractorCodes");
		//	glCode = codes.getConPayCode();
			accName = cm.getCodeName(glCode,con);
			}else{
				glCode=EGovConfig.getProperty("egf_config.xml","SupAdvCode","","SupplierCodes");
				//glCode = codes.getSupPayCode();
				accName = cm.getCodeName(glCode,con);
			}

			if(interFundPayment!=null)
			{
				String accInfo=getAccountById(interFundPayment);
				if(accInfo!=null){
					String glcodeIF=null;
					StringTokenizer str=new StringTokenizer(accInfo,"~!");
					while (str.hasMoreTokens()) {
						accName=str.nextToken();
						glcodeIF=str.nextToken();
						vd.setAccountName(accName);
						vd.setGLCode(glcodeIF);
						transaction.setGlCode(glcodeIF);
						transaction.setGlName(accName);

						}
				}
			}
			else{
			vd.setGLCode(glCode);
			vd.setAccountName(accName);
			transaction.setGlCode(glCode);
			transaction.setGlName(accName);
			}

			vd.setVoucherHeaderID(String.valueOf(voucherHeaderId));
		//	vd.setGLCode(glCode);
		//	vd.setAccountName(accName);
			vd.setCreditAmount("0");
			vd.setDebitAmount(ExilPrecision.convertToString(dc.getValue("contracotrBillDetail_advanceAmount"), 2));
			vd.setLineID(lineId + "");
			vd.setNarration((String)dc.getValue("voucherHeader_narration"));
			vd.insert(connection);
			lineId++;

		//	transaction.setGlCode(glCode);
		//	transaction.setGlName(accName);
			transaction.setCrAmount("0");
			transaction.setDrAmount(ExilPrecision.convertToString(dc.getValue("contracotrBillDetail_advanceAmount"), 2));
			transaction.setVoucherLineId(String.valueOf(vd.getId()));
			transaction.setVoucherHeaderId(String.valueOf(voucherHeaderId));
			transaction.setNarration(dc.getValue("voucherHeader_narration"));
			transactions.add(transaction);
			debitAmount += ExilPrecision.convertToDouble(dc.getValue("contracotrBillDetail_advanceAmount"), 2);

			/****   Paid(bank) account (Credited)  ****/
			ChartOfAccounts cOfAcc = ChartOfAccounts.getInstance();
			glCode = cOfAcc.getGLCode("bankAccount_id",dc.getValue("accountGroup_accountNumberId"),connection);
			accName = cm.getCodeName(glCode,con);
			vd.setGLCode(glCode);
			vd.setAccountName(accName);
			vd.setCreditAmount(ExilPrecision.convertToString(dc.getValue("contracotrBillDetail_advanceAmount"), 2));
			vd.setDebitAmount("0");
			vd.setLineID(lineId + "");
			vd.setNarration((String)dc.getValue("voucherHeader_narration"));
			lineId++;
			vd.insert(connection);

			Transaxtion transactionBank = new Transaxtion();
			transactionBank.setGlCode(glCode);
			transactionBank.setGlName(accName);
			transactionBank.setCrAmount(ExilPrecision.convertToString(dc.getValue("contracotrBillDetail_advanceAmount"), 2));
			transactionBank.setDrAmount("0");
			transactionBank.setVoucherLineId(String.valueOf(vd.getId()));
			transactionBank.setVoucherHeaderId(String.valueOf(voucherHeaderId));
			transactionBank.setNarration(dc.getValue("voucherHeader_narration"));
			transactions.add(transactionBank);

			creditAmount += ExilPrecision.convertToDouble(dc.getValue("contracotrBillDetail_advanceAmount"), 2);

		//	if(debitAmount != creditAmount){
			if(Math.abs(debitAmount-creditAmount)>0.0000001){
			dc.addMessage("exilError","Debit amount mismatches Credit amount,  Transaction canceled...");
			throw taskExc;
			}
}
	/**
	 * need to fix this issue later. 
	 */
	
	/*private void postInSubLedgerPaymentHeader(DataCollection dc,int chequeDetailId) throws TaskFailedException, SQLException {
		SubLedgerPaymentHeader slph = new SubLedgerPaymentHeader();
		slph.setBankAccountId(dc.getValue("accountGroup_accountNumberId"));
		slph.setChequeId(String.valueOf(chequeDetailId));
		slph.setContractorId(contractorId==0?null:contractorId+"");
		slph.setNarration((String)dc.getValue("voucherHeader_narration"));
		slph.setPaidAmount(ExilPrecision.convertToString(dc.getValue("contracotrBillDetail_advanceAmount"), 2));
		slph.setSupplierId(supplierId==0?null:supplierId+"");
		slph.setType("Advance");
		slph.setTypeOfPayment((String)dc.getValue("subLedgerPaymentHeader_typeOfPayment"));
		slph.setVoucherHeaderId(voucherHeaderId + "");
		slph.setWorksDetailID((String)dc.getValue("worksDetail_id"));
		slph.insert(connection);
	}*/
	/**
	 * SubLedgerPaymentHeader update function
	 * **/
	
	/**
	 * need to fix this issue later. 
	 */
/*	private void UpdateSubLedgerPaymentHeader(DataCollection dc,int chequeDetailId) throws TaskFailedException, SQLException {
		SubLedgerPaymentHeader slph = new SubLedgerPaymentHeader();
		statement = connection.createStatement();
		slph.setBankAccountId(dc.getValue("accountGroup_accountNumberId"));
		slph.setChequeId(String.valueOf(chequeDetailId));
		slph.setContractorId(contractorId==0?null:contractorId+"");
		slph.setNarration((String)dc.getValue("voucherHeader_narration"));
		slph.setPaidAmount(ExilPrecision.convertToString(dc.getValue("contracotrBillDetail_advanceAmount"), 2));
		slph.setSupplierId(supplierId==0?null:supplierId+"");
		slph.setType("Advance");
		slph.setTypeOfPayment((String)dc.getValue("subLedgerPaymentHeader_typeOfPayment"));
		String vhid=String.valueOf(voucherHeaderId);
		slph.setVoucherHeaderId(vhid);
		slph.setWorksDetailID((String)dc.getValue("worksDetail_id"));

		resultset = statement.executeQuery("SELECT id from SubLedgerPaymentHeader where voucherheaderid = " + voucherHeaderId);
		if(resultset.next()){
			slph.setId(resultset.getInt("id") + "");
			slph.update(connection);
		}else{
			slph.insert(connection);
		}

		resultset.close();
		statement.close();
	}*/
	private void updateInWorksDetail(DataCollection dc)throws SQLException,TaskFailedException
	{
		if(dc.getValue("modeOfExec").equalsIgnoreCase("edit"))
		{
			double oldAmount=0.0;
			double dbAmount=0.0;
			WorksDetail wd=new WorksDetail();
			wd.setId((String)dc.getValue("worksDetail_oldWorksdetailId"));
			statement = connection.createStatement();
			resultset = statement.executeQuery("select  advanceAmount from worksDetail where id="+dc.getValue("worksDetail_oldWorksdetailId"));
			if(resultset.next())
			{
				oldAmount=resultset.getDouble(1);
			}
			double newAmount = oldAmount-(ExilPrecision.convertToDouble(dc.getValue("worksDetail_oldAmount"),2));
			wd.setAdvancePaidAmount(""+newAmount);
			wd.update(connection);
			resultset = statement.executeQuery("select  advanceAmount from worksDetail where id="+dc.getValue("worksDetail_id"));
			if(resultset.next())
			{
				dbAmount=resultset.getDouble(1);
			}
			wd.setId((String)dc.getValue("worksDetail_id"));
			wd.setAdvancePaidAmount(""+(ExilPrecision.convertToDouble(dc.getValue("contracotrBillDetail_advanceAmount"),2)+dbAmount));
			wd.update(connection);
		}
		else
		{
			double maxAmount=0.0;
			double dbAmount=0.0;
			statement = connection.createStatement();
			resultset = statement.executeQuery("select  advancepayable,advanceAmount from worksDetail where id="+dc.getValue("worksDetail_id"));
			if(resultset.next())
			{
				maxAmount=resultset.getDouble(1);
				dbAmount=resultset.getDouble(2);
			}
			LOGGER.debug("dbAmount  "+dbAmount+"  maxAmount  "+maxAmount);
			WorksDetail wd=new WorksDetail();
			wd.setId((String)dc.getValue("worksDetail_id"));
			wd.setAdvancePaidAmount(""+(ExilPrecision.convertToDouble(dc.getValue("contracotrBillDetail_advanceAmount"),2)+dbAmount));
			wd.update(connection);
		}
	}

	/**
	 * This function returns the name+"~!"+code when passed the glcodeid
	 * @param aGLCodeid
	 * @param dc
	 * @return
	 * @throws TaskFailedException
	 */
	private String getAccountById(String aGLCodeid) throws TaskFailedException{
		String accountInfo="";
		resultset=null;
		statement=null;
		try{
			statement = connection.createStatement();
			resultset = statement.executeQuery("SELECT name,glcode FROM chartofaccounts where id='" + aGLCodeid + "'");
			resultset.next();
			accountInfo = resultset.getString("name")+"~!"+resultset.getString("glcode");
			resultset.close();
			statement.close();
		}catch(SQLException sqlex){
			throw taskExc;
		}
		return accountInfo;
	}

	public ArrayList addRequiredDataToList(ArrayList transactions,
				DataCollection dc)throws TaskFailedException{
		ArrayList reqData=getRequiredData(dc,transactions,1);
		for(int i=0;i<transactions.size();i++){
		Transaxtion transaxtion = (Transaxtion)transactions.get(i);
		transaxtion.setTransaxtionParam(reqData);
		}
		return transactions;
	}

	public ArrayList getRequiredData(DataCollection dc, ArrayList transactions,int rowIdx)throws TaskFailedException{
		ArrayList requiredList=new ArrayList();
		ArrayList formList=new ArrayList();
		formList.add("fund_id");
	//	formList.add("department_id");
		formList.add("fundSource_id");
		formList.add("accountGroup_accountNumberId");
		formList.add("relation_id");
		formList.add("worksDetail_id");
		String formControlName;
		for(int i=0;i<formList.size();i++){
			formControlName=(String)formList.get(i);
			TransaxtionParameter transParam = new TransaxtionParameter();
			try {
				transParam.setGlcodeId(cmImpl.getGlCodeId(((Transaxtion)transactions.get(rowIdx-1)).getGlCode(), connection)+"");
			} catch (Exception e) {
				throw new TaskFailedException();
			}
			transParam.setDetailKey((String)dc.getValue(formControlName));
			if(formControlName.equalsIgnoreCase("accountGroup_accountNumberId"))formControlName="bankAccount_id";
			transParam.setDetailName(formControlName);
			String detailAmt=((Transaxtion)transactions.get(rowIdx-1)).getDrAmount();
			if(detailAmt==null || detailAmt.trim().length()==0 || Double.parseDouble(detailAmt)== 0 )
				detailAmt=((Transaxtion)transactions.get(rowIdx-1)).getCrAmount();
			transParam.setDetailAmt(detailAmt);
			requiredList.add(transParam);
		}
		return requiredList;
	}

	private void checkAmount(DataCollection dc) throws TaskFailedException,SQLException
	{

		double dbAmount=0.0;
		double maxAmount=0.0;
		double oldAmount=ExilPrecision.convertToDouble(dc.getValue("oldAmount"),2);
		double newAmount=ExilPrecision.convertToDouble(dc.getValue("contracotrBillDetail_advanceAmount"),2);
		statement = connection.createStatement();
		resultset = statement.executeQuery("select  advancepayable,advanceAmount from worksDetail where id="+dc.getValue("worksDetail_id"));
		LOGGER.info("select  advancepayable,advanceAmount from worksDetail where id="+dc.getValue("worksDetail_id"));
		if(resultset.next())
	   	{
			maxAmount=resultset.getDouble(1);
			dbAmount=resultset.getDouble(2);
		}
		LOGGER.debug("dbAmount  "+dbAmount+"  maxAmount  "+maxAmount+"  oldAmount "+oldAmount+" newAmount "+newAmount);
		if((dbAmount-oldAmount)+newAmount >maxAmount)
		{
			dc.addMessage("exilRPError","Amount Crossed Max Limit "+maxAmount);
			throw new TaskFailedException();
		}
		modifiedAmt=(dbAmount-oldAmount)+newAmount;
	}
	/**
	 * This function is called on Modify.
	 * 1.We need to update the voucher header along with the modified by and modified date.
	 * 2.Update the voucherdetail for the existing records and insert the new records.
	 * 3.update the generalledger for the existing records and insert the new records.
	 * @param dc
	 * @param con
	 * @throws TaskFailedException
	 */
	/**
	 * need to fix this issue later. 
	 */
	
	/*public void editVoucher(DataCollection dc,Connection con) throws TaskFailedException{

        Statement st=null;
        ResultSet rset=null;
        voucherNumber=dc.getValue("voucherHeader_voucherNumber");
        String vhID=dc.getValue("voucherHeader_id");
        String voucherNum="";
        String subVN="",refernceJVNo="",bankFundId=null;
        bankFundId=dc.getValue("fund1_id");
        LOGGER.debug("voucherNumber:"+voucherNumber);
        try
        {
            st=con.createStatement();
            LOGGER.info("select vouchernumber,refcgno from voucherheader where id="+vhID);
            rset=st.executeQuery("select vouchernumber,refcgno from voucherheader where id="+vhID);
            if(rset.next())
            {
                voucherNum=rset.getString(1);
                subVN=voucherNum.substring(0,vouchertypelength);
                refernceJVNo=rset.getString(2);
            }
            rset.close();
            voucherNumber=subVN+voucherNumber;
            dc.addValue("voucherHeader_voucherNumber",voucherNumber);
            LOGGER.debug("voucherNumber:"+voucherNumber);
            if(!voucherNum.equals(voucherNumber))
            {
                LOGGER.debug("voucherNum NOt Equal");
                if(!cm.isUniqueVN(dc, connection,voucherNumber)){
                    throw  taskExc;
                }
            }
            //Delete the Interfund JV and create a new JV if its an Interfund Transfer
            LOGGER.debug("refernceJVNo is :"+refernceJVNo);

           	if(refernceJVNo!=null && refernceJVNo.trim().equals("")) {
            	//We need to delete the JV
           		EGovernCommon eGovernCommon=new EGovernCommon();
           		eGovernCommon.deleteJournal(refernceJVNo,con);
            }

            	//Check if this is an Intefund Transfer. If so pass the new JV
            	LOGGER.debug("Bank Fundid is :"+bankFundId+" Work order fund id is :"+dc.getValue("fund_id"));
    			//If Funds are different
    			if(!bankFundId.equals(dc.getValue("fund_id")))
    			{
    				//pass jv and get the cgn
    			//	FinancialTransactions ftxn=new FinancialTransactions();
    				Voucherheader vobj=new Voucherheader();
    				vobj=passInterFundJV(dc,con);
    				refCGN=vobj.getCgn();

    				//Set the debit code for payment voucher.
    				//It will Interfund Transfer account of Work order fund
    				Fund fundPaymentobj=new Fund();
    				interFundPayment=fundPaymentobj.getInterfundCode(dc.getValue("fund_id"),con);
    				if(interFundPayment==null)
    					throw new TaskFailedException("Inter Fund Transfer account is not defined for Work order Fund.");
    				LOGGER.debug("Intefund transfer -payment is:"+interFundPayment);
    			}
         }catch(Exception sq){
        	LOGGER.error("Exp="+sq.getMessage());
        	throw taskExc;
        }

    	try
		{
    		checkAmount(dc);
    		String bankAccId=dc.getValue("accountGroup_accountNumberId");
    		String chequeNo=dc.getValue("chequeDetail_chequeNumber");
			int CurdateCheck=1;
			String vdt=(String)dc.getValue("voucherHeader_voucherDate");
		 	CurdateCheck=cm.isCurDate(dc,con,vdt);
			if(CurdateCheck==0)
				return;

            *//**Checks for Bank Balance Sufficiency
             * Commenting for Time-being **//*
	   		accountBalance=cm.getAccountBalanceInModify(dc,connection,bankAccId,dc.getValue("contracotrBillDetail_advanceAmount"),Integer.parseInt(dc.getValue("voucherHeader_id")));
			statement = connection.createStatement();
			updateInVoucherHeader(dc,con);

	   	    resultset = statement.executeQuery("SELECT chequenumber from chequedetail where voucherheaderid = " + voucherHeaderId);
	   		if(resultset.next())
	   		{
	   			String oldChequeNo=resultset.getInt("chequenumber")+"";
	   			if(!oldChequeNo.equals(chequeNo))
	   			{
	   				if(!cm.isUniqueChequeNo(chequeNo,bankAccId,dc, con))
	   					throw new TaskFailedException();
	   				if(!cm.isChqNoWithinRange(chequeNo,bankAccId,dc, con))
	   				throw new TaskFailedException();
	   			}
	   		}

	   		int chequeDetailId=UpdateChequeDetail(dc);
			if(chequeDetailId==0)
			{
				dc.addMessage("Cheque Detail Updation Failed");
				throw taskExc;
			}

			if("contractor".equals( (dc.getValue("subLedgerPaymentHeader_typeOfPayment")).toLowerCase())){
				contractorId = getContraSupplierId(dc);
			}else{
				supplierId = getContraSupplierId(dc);
			}
			UpdateSubLedgerPaymentHeader(dc,chequeDetailId);
			UpdateBankReconciliation(dc);
            UpdateVoucherDetail(dc, transactions,con);
			updateInWorksDetail(dc);
			LOGGER.debug("update vouchermis...");
			//cm.updateVoucherMIS("U",con,voucherHeaderId,(String)dc.getValue("field_name"), (String)dc.getValue("fundSource_id"));
			updateVoucherMIS("U");
			transactions=addRequiredDataToList(transactions,dc);
			ChartOfAccounts engine = ChartOfAccounts.getInstance();
			Transaxtion txnList[] = new Transaxtion[transactions.size()];
			txnList=(Transaxtion[])transactions.toArray(txnList);
			if(!engine.postTransaxtions(txnList, con, dc))throw taskExc;
				deleteFromVoucherDetail();
				dc.addValue("voucherHeader_voucherNumber",voucherNumber);
			dc.addMessage("eGovSuccess", "CGNumber is "+cgNum+" and VoucherNumber is "+voucherNumber);
		}catch(Exception e){
			LOGGER.debug("Exp="+e.getMessage());
			dc.addMessage("exilRPError","Error in update :"+e.getMessage());
			throw new TaskFailedException();
		}
	}*/

	/**
	 * need to fix this issue later. 
	 */
	
	/*private void reverse(DataCollection dc,Connection conn) throws TaskFailedException
		{
			GlReverser glr=new GlReverser();
		try{
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			Date dt=new Date();
			String vdt=(String)dc.getValue("voucherHeader_effDate");
			dt = sdf.parse( vdt );
			String voucherHeader_effDate = formatter.format(dt);
			String vt=(String)dc.getValue("voucherHeader_voucherDate");
			dt=null; dt = sdf.parse( vt );
			String vhdate=formatter.format(dt);
			int dateCheck=1;
			int CurdateCheck=1;

			dateCheck=cm.isValidDate(dc, conn,vt,vdt);
			if(dateCheck==0)
				return;
			CurdateCheck=cm.isCurDate(dc,conn,vdt);
			if(CurdateCheck==0)
				return;
			String vhid="",refcgn="",jvcgn="";
			fiscalPid=cm.getFiscalPeriod(voucherHeader_effDate);
			String originalFiscalPid=cm.getFiscalPeriod(vhdate);

			//Reverse the journal first
			LOGGER.debug("Original Journal Voucher number is:"+dc.getValue("jv_voucherNumber"));
			LOGGER.debug("Journal VN length :"+dc.getValue("jv_voucherNumber").trim().length());
			if(dc.getValue("jv_voucherNumber").trim().length()>0)
			{
				///newVcNo=cm.getCgvnNo(conn,dc.getValue("fund_id"),dc.getValue("voucherHeader_newJVcNo"),"J");
				newVcNo = cm.getVoucherNumber(dc.getValue("fund_id"), FinancialConstants.JOURNAL_VOUCHERNO_TYPE, dc.getValue("voucherHeader_newJVcNo"), conn);
				
				vType=newVcNo.substring(0,vouchertypelength);
				LOGGER.debug("Reverse Journal no:"+newVcNo);
				egVoucher=cm.getEg_Voucher(vType,fiscalPid,conn);
				for(int i=egVoucher.length();i<5;i++)
				{
					 egVoucher="0"+egVoucher;
				}
				cgNum=vType+egVoucher;
				LOGGER.debug("Reverse Journal cgno:"+cgNum);
				VoucherTypeForULB voucherType=new VoucherTypeForULB();
				String vTypeForULB=(String)voucherType.readVoucherTypes("Payment");
				if(vTypeForULB.equalsIgnoreCase("Auto"))
				{
					newVcNo=cgNum;
				}    
				
				
				
				//We need to find the JV
				String getJV="Select id,cgn from voucherheader where vouchernumber='"+dc.getValue("jv_voucherNumber")+"' and status!=4 and fiscalperiodid='"+originalFiscalPid+"'";
				LOGGER.debug("getJV **"+getJV);
				Statement stmt1=conn.createStatement();
				ResultSet rs1=stmt1.executeQuery(getJV);
				if(rs1.next()){
					 vhid=rs1.getString(1);
					 jvcgn=rs1.getString(2);
				}
				LOGGER.debug("vhid is :"+vhid+"***jvcgn"+jvcgn);
				rs1.close();
				if(vhid.length()>0)
				{
					//We need to reverse that JV
					LOGGER.debug("Before calling the Journal reverse.");
					glr.reverse(conn,jvcgn,newVcNo,voucherHeader_effDate,cgNum,dc);
					LOGGER.debug("After calling the Journal reverse.");
					//get the cgn of the new voucher and set it as the refcgn for the new payment voucher.
					String getnewvhcgn="SELECT vh.cgn FROM voucherheader vh WHERE vh.originalvcid='"+vhid+"'";
					LOGGER.info(getnewvhcgn);

					rs1=stmt1.executeQuery(getnewvhcgn);
					//if there is a referncecgn then we need to set it for the payment voucher
					if(rs1.next())
						refcgn=rs1.getString(1);
					LOGGER.debug("New cgnNo for JV is :"+refcgn);
					if(refcgn.length()>0)
						glr.refcgvn=refcgn;
					LOGGER.debug("After setting refernce.");
				}
			}
			//fiscalPid=getFiscalPeriod(dc.getValue("voucherHeader_effDate"));
			LOGGER.debug("Bank Fund :"+dc.getValue("fund1_id"));
			//newVcNo=cm.rvNumber(dc,conn,dc.getValue("fund1_id"));
			newVcNo = cm.getVoucherNumber(dc.getValue("fund1_id"), FinancialConstants.PAYMENT_VOUCHERNO_TYPE, dc.getValue("voucherHeader_newVcNo"), conn);
			LOGGER.debug("Rceipt Reverse Voucher num:"+newVcNo);
			vType=newVcNo.substring(0,vouchertypelength);
			egVoucher=cm.getEg_Voucher(vType,fiscalPid,conn);
			}
			catch(Exception e)
			{
				dc.addMessage("exilRPError","Contractor Journal error : " + e.toString());
				throw taskExc;
			}
			for(int i=egVoucher.length();i<5;i++)
			{
				 egVoucher="0"+egVoucher;
			}
			cgNum=vType+egVoucher;
			VoucherTypeForULB voucherType=new VoucherTypeForULB();
			String vTypeForULB=(String)voucherType.readVoucherTypes("Payment");
			if(vTypeForULB.equalsIgnoreCase("Auto"))
			{
				newVcNo=cgNum;
			}                             
			SubLedgerPaymentHeader slph=new SubLedgerPaymentHeader();
			WorksDetail wd=new WorksDetail();
			Fund fu=new Fund();
			BankAccount ba=new BankAccount();
			BankReconciliation br=new BankReconciliation();
			ChequeDetail cd= new ChequeDetail();
			wd.setId((String)dc.getValue("worksDetail_id"));
			fu.setId(dc.getValue("fund_id"));
			ba.setId(dc.getValue("accountGroup_accountNumberId"));
			double advanceAmt=ExilPrecision.convertToDouble(dc.getValue("contracotrBillDetail_advanceAmount"), 2);
			try{
	//			Statement statement = connection.createStatement();
				SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				String vdt=(String)dc.getValue("voucherHeader_effDate");
				String voucherHeader_effDate = formatter.format(sdf.parse(vdt));
				//glr.reverseSubLedger(connection,dc.getValue("cgnNum"),newVcNo,dc.getValue("voucherHeader_effDate"),cgNum,dc);
				glr.reverseSubLedger(connection,dc.getValue("cgnNum"),newVcNo,voucherHeader_effDate,cgNum,dc);
				slph.reverse(connection,dc.getValue("cgnNum"));
				wd.reverseNegative(connection,0,0,0,advanceAmt);
				br.reverse(connection,dc.getValue("cgnNum"));
				cd.reverse(connection,dc.getValue("cgnNum"),"subledger");
			}
			catch(Exception e)
			{    LOGGER.error("Exp="+e.getMessage());
				dc.addMessage("exilRPError",e.getMessage());
				throw new TaskFailedException();
			}
			dc.addMessage("eGovSuccess","for Reversing Advance Journal  : CGNumber is "+cgNum+" and VoucherNumber is "+newVcNo);
	}
*/
	/**
	 * This function will pass the JV if there is an Inter fund transfer involved
	 * @param dc
	 * @param con
	 * @return
	 * @throws Exception
	 */
	/**
	 * need to fix this issue later. 
	 */
	/*private Voucherheader passInterFundJV(DataCollection dc,Connection con) throws TaskFailedException,SQLException,ParseException,Exception
	{
		EGovernCommon ecm = new EGovernCommon();
		CommonMethodsI cm=new CommonMethodsImpl();
		Voucher v=new Voucher();
		Voucherheader vh=new Voucherheader();

		MisDataCollection mdc=new MisDataCollection();
		MisData mi=new MisData();

		GeneralLedgerEnteries glentry=new GeneralLedgerEnteries();
		GeneralLedgerPosting gl[]=new GeneralLedgerPosting[2];
		GlDetailEntry glDet[]= new GlDetailEntry[2];
		String voucherHeader_voucherDate="",glCode="",accName="";
		String glcodeIF=null;
		Fund fundPaymentobj=new Fund();
		int detailTypeid=0;
		Statement stmt=null;
		ResultSet rs=null;

		interFundJV=fundPaymentobj.getInterfundCode(dc.getValue("fund1_id"),con);
		if(interFundJV==null)
			throw new TaskFailedException("Inter Fund Transfer account is not defined for Bank Fund.");
		LOGGER.debug("Intefund transfer -Journal is:"+interFundJV);
		//If the Interfund Transfer account is defined we need to set it as the Credot glcode for JV.
		//Debit code will be always Advance to Contractors.
		if(interFundJV!=null)
		{
			String accInfo=getAccountById(interFundJV);
			if(accInfo!=null){
				StringTokenizer str=new StringTokenizer(accInfo,"~!");
				while (str.hasMoreTokens()) {
					accName=str.nextToken();
					glcodeIF=str.nextToken();
				}
			}
		}
		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String vdt=(String)dc.getValue("voucherHeader_voucherDate");
		voucherHeader_voucherDate = formatter.format(sdf.parse(vdt));

		vh.setVoucherdate(voucherHeader_voucherDate);
		vh.setFund(Integer.parseInt(dc.getValue("fund_id")));
		LOGGER.info("dcvalue:fundsourceid:"+dc.getValue("fundSource_id"));
		if(!dc.getValue("fundSource_id").trim().equals("")){
			LOGGER.info("fundsource id:interfund transfer");
			mi.setFundSource(Integer.parseInt(dc.getValue("fundSource_id")));
		}
		mi.setField((dc.getValue("field_name").equals("")) ? null : Integer.valueOf(dc.getValue("field_name")));
		if(dc.getValue("departmentId")!=null && !dc.getValue("departmentId").equals("") )
		mi.setDepartment(Integer.valueOf(dc.getValue("departmentId")));
		mdc.setMisData(mi);
		v.setMisDataCollection(mdc);

		vh.setDescription("Inter Fund Transfer from advance Payment");
		vh.setName("Advance Payment");
		vh.setType("Journal Voucher");
		String fiscalPeriodId=cm.getFiscalPeriod(voucherHeader_voucherDate,con);
		//Get the Journal voucher number
		//String vNum=ecm.vNumberCess(dc,con,dc.getValue("fund_id"));
		String vNum = ecm.getVoucherNumber(fId, FinancialConstants.JOURNAL_VOUCHERNO_TYPE, dc.getValue("jv_voucherNumber"), con);
		
		String vType=vNum.substring(0,vouchertypelength);
		String cgvn=ecm.getEg_Voucher(vType,fiscalPeriodId,con);
		for(int i=cgvn.length();i<5;i++){
			cgvn="0"+cgvn;	}
		cgvn=vType+cgvn;
		VoucherTypeForULB voucherType=new VoucherTypeForULB();
		String vTypeForULB=(String)voucherType.readVoucherTypes("Payment");
		if(vTypeForULB.equalsIgnoreCase("Auto"))
		{
			vNum=cgvn;
		}
		
		LOGGER.debug("Journal Voucher number :"+vNum);
		if(!ecm.isUniqueVN(vNum,vh.getVoucherdate(),con))
		{
			throw new Exception("Duplicate Voucher Number");
		}
		vh.setCgn("JVG"+ecm.getCGNumber());
		vh.setCgvn(cgvn);
		vh.setUserId(Integer.parseInt(dc.getValue("current_UserID")));
		vh.setVouchernumber(vNum);
		v.setVoucherheader(vh);

		if("contractor".equals(((String)dc.getValue("subLedgerPaymentHeader_typeOfPayment")).toLowerCase())) {
			glCode = EGovConfig.getProperty("egf_config.xml","ConAdvCode","","ContractorCodes");
			accName = cm.getCodeName(glCode,con);
		}else{
			glCode =EGovConfig.getProperty("egf_config.xml","SupAdvCode","","SupplierCodes");
			accName = cm.getCodeName(glCode,con);
		}
		//Get the DetailTypeId for the control code.
		String qryDetailType="Select detailtypeid from chartofaccountdetail where glcodeid=(select id from chartofaccounts where glcode='"+glCode+"')";
		stmt=con.createStatement();
		rs=stmt.executeQuery(qryDetailType);
		if(rs.next())
			detailTypeid=rs.getInt(1);

		LOGGER.debug("glCode is :"+glCode+" accName :"+accName+" Amount :"+dc.getValue("contracotrBillDetail_advanceAmount")+" detailTypeid :"+detailTypeid);
		gl[0]=new GeneralLedgerPosting();
		glDet[0]=new GlDetailEntry();
		gl[0].setCredit(0);
		gl[0].setDebit(Double.parseDouble(dc.getValue("contracotrBillDetail_advanceAmount")));
		gl[0].setGlcode(glCode);
		//gl[0].setDetailKeyId(Integer.parseInt(dc.getValue("relation_id")));
		glDet[0].setDetailKeyId(Integer.parseInt(dc.getValue("relation_id")));
		if(detailTypeid!=0)
			glDet[0].setDetailTypeId(detailTypeid);
		//glDet[0].setDetailKeyAmount(Double.parseDouble(dc.getValue("contracotrBillDetail_advanceAmount")));
		glDet[0].setDetailKeyAmount(Integer.parseInt(dc.getValue("contracotrBillDetail_advanceAmount")));
		glDet[0].setDetailGlcode(glCode);
		gl[0].setGlDetailEntry(glDet);

		gl[1]=new GeneralLedgerPosting();
		gl[1].setCredit(Double.parseDouble(dc.getValue("contracotrBillDetail_advanceAmount")));
		gl[1].setDebit(0);
		gl[1].setGlcode(glcodeIF);

		glentry.setGeneralLedgerPosting(gl);
		v.setGeneralLedgerEnteries(glentry);
		FinancialTransactions ft=new FinancialTransactions();
		Voucherheader voucherHeader=ft.postTransaction(v,con,null);
		return voucherHeader;
	}*/
	private void updateVoucherMIS(String action) throws TaskFailedException {
		try{
		VoucherMIS misobj=new VoucherMIS();
		misobj.setVoucherheaderid(String.valueOf(voucherHeaderId));
	 	if(!((String)dataCol.getValue("scheme")).equals(""))
	 		cm.validateScheme((String)dataCol.getValue("voucherHeader_voucherDate"),(String)dataCol.getValue("scheme"),connection);
		misobj.setScheme(dataCol.getValue("scheme").toString());
		if(!((String)dataCol.getValue("subscheme")).equals("") )
			cm.validatesubScheme((String)dataCol.getValue("voucherHeader_voucherDate"),(String)dataCol.getValue("subscheme").toString(),connection);
		misobj.setSubscheme((String)dataCol.getValue("subscheme"));
		misobj.setFundsourceid((String)dataCol.getValue("fundSource_id"));
		misobj.setDepartmentId((String)dataCol.getValue("departmentId"));
		misobj.setFunctionary((String)dataCol.getValue("functionaryId"));
		misobj.setDivisionId((String)dataCol.getValue("field_name"));
		if(action.equalsIgnoreCase("I"))
		{
			misobj.setSourcePath("/EGF/HTML/VMC/AdvanceJournal_VMC.jsp?drillDownCgn="+(String)dataCol.getValue("voucherHeader_cgn")+"&showMode=view");
			misobj.insert(connection);
		}
		else
		{
			misobj.setSourcePath("/EGF/HTML/VMC/AdvanceJournal_VMC.jsp?drillDownCgn="+dataCol.getValue("cgnNum").toString()+"&showMode=view");
			misobj.update(connection);
		}
	}catch(Exception e)
	{
	
		LOGGER.error("Error While Inserting into Vouchermis");
		throw new TaskFailedException(e.getMessage());
	}
	
	}	
}
