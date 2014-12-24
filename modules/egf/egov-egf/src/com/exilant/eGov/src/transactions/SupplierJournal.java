/**
 * Created on Jan 5, 2005
 * @author pushpendra.singh
 */
 
package com.exilant.eGov.src.transactions;

import com.exilant.GLEngine.AccountDetailType;
import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;
import com.exilant.GLEngine.TransaxtionParameter;
import com.exilant.eGov.src.common.*;
import com.exilant.eGov.src.domain.*;
import com.exilant.exility.common.*;

import org.egov.infstr.utils.EGovConfig;
import com.exilant.exility.dataservice.DataExtractor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.text.ParseException;
import org.apache.log4j.Logger;

public class SupplierJournal extends AbstractTask{
	private static final  Logger LOGGER=Logger.getLogger(SupplierJournal.class);
	private Connection connection;
	private Statement statement;
	private ResultSet resultset;
	private DataCollection dc;
	private TaskFailedException taskExc;
	private int supplierId = 0, voucherHeaderId=0;
	private HashMap detailTypes;
	private ArrayList requiredList;
	public double OtherRecoveries=0.0;
	String voucherNumber;
	public String fiscalPid;
	public String vType;
	public String eg_voucher;
	public String cgNum;
	public String newVcNo;
	public String fId;
	public String fuctid="";
	int VoucherHeaderId;
	ArrayList transactions = new ArrayList();

	EGovernCommon cm = new EGovernCommon();
	CommonMethodsI cmImpl=new CommonMethodsImpl();
	public SupplierJournal(){}

	public void execute(String taskName,
							String gridName,
							DataCollection dc,
							Connection conn,
							boolean erroOrNoData,
							boolean gridHasColumnHeading, String prefix) throws TaskFailedException {
		this.dc = dc;
		this.connection = conn;
		String supplierGrid[][] = (String[][])dc.getGrid("gridSupplierJournal");
		// 5-functionid 0-glcode
		if(!cm.checkDuplicatesForFunction_AccountCode(dc,supplierGrid, 5,0))throw new TaskFailedException();
		if(dc.getValue("showGlEntries").equalsIgnoreCase("show"))
		{
			StringBuffer str=new StringBuffer(";");
			StringBuffer stradv=new StringBuffer("");
			str=str.append("SupPayCode"+"^");
			String code1=EGovConfig.getProperty("egf_config.xml","SupPayCode","","SupplierCodes");
			str=str.append(code1+"^");
			String name1=cm.getCodeName(code1,connection);
			str=str.append(name1+";");
			String outp=str.toString();
			
			String code2=EGovConfig.getProperty("egf_config.xml","SupAdvCode","","SupplierCodes");
			if(!code1.equalsIgnoreCase(code2))
			{      
				stradv=stradv.append("SupAdvCode"+"^");
				stradv=stradv.append(code2+"^");
				String name2=cm.getCodeName(code2,conn);
				stradv=stradv.append(name2+";");
				outp=outp+stradv.toString();
			}
			/*str=str.append("SupAdvCode"+"^");
			String code2=EGovConfig.getProperty("SupAdvCode","","SupplierCodes");
			str=str.append(code2+"^");
			String name2=getCodeName(code2);
			str=str.append(name2+";");*/
			//String outp=str.toString();
			dc.addValue("glCodeName",outp);
			LOGGER.debug(" xxxxxxxxxx  "+outp);
			return;
		}
		try{
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			Date dt=new Date();
			String vdt=(String)dc.getValue("voucherHeader_voucherDate");
			dt = sdf.parse( vdt );
			String voucherHeader_voucherDate = formatter.format(dt);
			fiscalPid=cm.getFiscalPeriod(voucherHeader_voucherDate);
		}
		catch(Exception e){throw new TaskFailedException(e.getMessage());}
		fId=dc.getValue("fund_id").toString();
		if(dc.getValue("modeOfExec").equalsIgnoreCase("modify")){
			try{
				String vdt=(String)dc.getValue("voucherHeader_effDate");
				String vt=(String)dc.getValue("voucherHeader_voucherDate");
				int dateCheck=1;
				int CurdateCheck=1;
				dateCheck=cm.isValidDate(dc, conn,vt,vdt);
				if(dateCheck==0)
					return;
				CurdateCheck=cm.isCurDate(dc,conn,vdt);
				if(CurdateCheck==0)
					return;
				newVcNo=cm.rvNumber(dc,conn,fId);
				vType=newVcNo.substring(0,2);
				eg_voucher=cm.getEg_Voucher(vType,fiscalPid,conn);
			}catch(Exception e){
				dc.addMessage("exilRPError","Contractor Journal error : " + e.toString());
		    	throw taskExc;
			}
			for(int i=eg_voucher.length();i<5;i++)
			{
				 eg_voucher="0"+eg_voucher;
			}
			cgNum=vType+eg_voucher;
			reverse();
			dc.addMessage("eGovSuccess","for Reversing Supplier Journal : CGNumber is "+cgNum+" and VoucherNumber is "+newVcNo);
			return;
		 }
		else if(dc.getValue("modeOfExec").equalsIgnoreCase("edit")){
	  	 	editVoucher(dc,conn);
			return;
		 }
		ArrayList transactions = new ArrayList();
		taskExc=new TaskFailedException();
		try{
			String vdt=(String)dc.getValue("voucherHeader_voucherDate");
			int CurdateCheck=1;
			
			CurdateCheck=cm.isCurDate(dc,conn,vdt);
			if(CurdateCheck==0)
				return;
			voucherNumber=cm.vNumber(dc, conn,fId,dc.getValue("voucherHeader_voucherNumber"));
			vType=voucherNumber.substring(0,2);
			eg_voucher=cm.getEg_Voucher(vType,fiscalPid,conn);
			for(int i=eg_voucher.length();i<5;i++)
			{
				 eg_voucher="0"+eg_voucher;
			}
			cgNum=vType+eg_voucher;
			if(!cm.isUniqueVN(dc, conn,voucherNumber))
				throw new TaskFailedException();

			if(!isValidAmount(dc)){
				dc.addMessage("exilRPError","Server Validation Failed..Check The Data Sent");
   				throw  taskExc;
			}

			//getRequiredData(dc);

			try{
 				postInVoucherHeader(dc);
 				cm.updateVoucherMIS("I",conn,voucherHeaderId,(String)dc.getValue("field_id"),(String)dc.getValue("fundSource_id"),(String)dc.getValue("scheme"),(String)dc.getValue("subscheme"),dc.getValue("voucherHeader_voucherDate"));
				postInVoucherDetail(dc,transactions,conn);
				postInSupplierBillDetail(dc);
				updateBillStatus(dc);
				postInWorksDetail(dc);
			}catch(Exception e){
				dc.addMessage("exilRPError",e.toString());
				throw new TaskFailedException();
			}

			/*added the necessary data to each of objects*/
    		//transactions=addRequiredDataToList(transactions,dc);
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
    		cm.UpdateVoucherStatus(dc,"Journal Voucher",conn,voucherHeaderId);
    		dc.addMessage("eGovSuccess","Supplier Journal : CGNumber is "+cgNum+" and VoucherNumber is "+voucherNumber);
    	}catch(Exception sqlex){
    		dc.addMessage("exilRPError","Transaction Failed :"+sqlex.getMessage());
    		throw taskExc;
		}
	}
	private void updateBillStatus(DataCollection dc)throws TaskFailedException
	{
		String qryString = "";
		qryString = "Update EG_BILLREGISTER set ISACTIVE = 1, BILLAPPROVALSTATUS ='"+"PASSED"+"' where ID ="+dc.getValue("supplierBillDetail_billNumber");
		try
		{
		Statement statement = connection.createStatement();
		statement.executeQuery(qryString);
		statement.close();
		}
		catch(SQLException sqlE){throw taskExc;}
		try
		 {
		    statement.close();
		 }
		 catch(Exception close){throw new TaskFailedException();}
	}
	private void getResultSet(String tableName, String condition, DataCollection dc) throws TaskFailedException{
		try{
			resultset = statement.executeQuery("select * from " + tableName + " where 1=1 " + condition);
			if(!resultset.next()) {
				dc.addMessage("exilError","no Data");
				throw  taskExc;
			}
		}
		catch(SQLException sqlex){
			dc.addMessage("exilNoData","select * from " + tableName + " where 1=1 " + condition);
			throw  taskExc;
		}
	}

	private void postInVoucherHeader(DataCollection dc) throws SQLException	,ParseException,TaskFailedException{
		VoucherHeader vh = new VoucherHeader();
		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		//Date dt=new Date();
		String vdt=(String)dc.getValue("voucherHeader_voucherDate");
		String dateformat = formatter.format(sdf.parse(vdt));
		vh.setCgn(dc.getValue("voucherHeader_cgn").toString());
		vh.setVoucherDate(dateformat);
		vh.setName("Supplier Journal");
		vh.setType("Journal Voucher");
		vh.setVoucherNumber(voucherNumber);
		vh.setFundId(dc.getValue("fund_id"));
		vh.setFundSourceId(dc.getValue("fundSource_id"));
		vh.setDepartmentId(dc.getValue("department_id"));
		vh.setDescription(dc.getValue("voucherHeader_narration"));
		vh.setIsConfirmed("1");
		vh.setFunctionId(dc.getValue("department_id"));
		vh.setFiscalPeriodId(cm.getFiscalPeriod(dateformat));
		//vchHead.setFundSourceId((String)dc.getValue("fundSource_id"));
		vh.setCreatedby(dc.getValue("current_UserID"));
		vh.setCgvn(cgNum);
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
		try
		{
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			String vdt=(String)dc.getValue("voucherHeader_voucherDate");
			voucherHeader_voucherDate = formatter.format(sdf.parse(vdt));
			String oldVdt=(String)dc.getValue("oldVoucherDate");
			oldVoucherDate = formatter.format(sdf.parse(oldVdt));
		}
		catch(Exception e)
		{
			LOGGER.error("Exp in date formating: "+e);
			throw new TaskFailedException(e.getMessage());
		}
//		if Fund is same then we can use the same voucher number and cgnumber
		LOGGER.info("..voucherNumber.."+voucherNumber+"  .VoucherHeader_voucherNumber. "+dc.getValue("voucherHeader_voucherNumber"));
		LOGGER.info("voucherHeader_voucherDate:  "+voucherHeader_voucherDate+"  oldVoucherDate:  "+oldVoucherDate);
		LOGGER.info("oldFundid:  "+oldFundid+"  dc.getValue(fund_id):  "+dc.getValue("fund_id"));
		if((oldFundid.equals(dc.getValue("fund_id").toString())) && (cm.getFiscalPeriod(voucherHeader_voucherDate).equalsIgnoreCase(cm.getFiscalPeriod(oldVoucherDate))))
		{
			LOGGER.debug("Funds are same.. and same fiscalperiod id");
			VH.setCgvn(dc.getValue("voucherHeader_cgn").toString());
			VH.setVoucherNumber(dc.getValue("voucherHeader_voucherNumber").toString());
			cgNum=dc.getValue("voucherHeader_cgn").toString();
			voucherNumber=dc.getValue("voucherHeader_voucherNumber").toString();
		}
		else
		{
			try
			{
				fiscalPid=cm.getFiscalPeriod(voucherHeader_voucherDate);
				if(!(oldFundid.equals(dc.getValue("fund_id").toString())))
				{
					String voucherNum=dc.getValue("voucherHeader_voucherNumber").toString();
					voucherNum=voucherNum.substring(2);
					voucherNumber=cm.vNumber(dc, con,fId,voucherNum);
					if(!cm.isUniqueVN(dc, con,voucherNumber))	throw new TaskFailedException();
				}
					vType=voucherNumber.substring(0,2);
					eg_voucher=cm.getEg_Voucher(vType,fiscalPid,con);
					for(int i=eg_voucher.length();i<5;i++)
							eg_voucher="0"+eg_voucher;
					cgNum=vType+eg_voucher;
					VH.setCgvn(cgNum);
					VH.setVoucherNumber(voucherNumber);
			}
			catch(Exception e)
			{
				LOGGER.error("Exp in getting new VNo. "+e);
				dc.addMessage("exilRPError","Transaction Failed :"+e.getMessage());
				throw new TaskFailedException(e.getMessage());
			}

		}
		VH.setCgn(dc.getValue("cgnNum").toString());
		VH.setDepartmentId(dc.getValue("department_id"));
		VH.setDescription((String)dc.getValue("voucherHeader_narration"));
		VH.setFundId(dc.getValue("fund_id"));
		VH.setFundSourceId(dc.getValue("fundSource_id"));
		VH.setId((String)dc.getValue("voucherHeader_id"));
		VH.setVoucherDate(voucherHeader_voucherDate);
		VH.setFunctionId(dc.getValue("department_id"));
		VH.setFiscalPeriodId(cm.getFiscalPeriod(voucherHeader_voucherDate));
		//VH.setLastModifiedBy(dc.getValue("current_UserID"));
		VH.setCreatedby(dc.getValue("current_UserID"));
		VH.setName("Supplier Journal");
		VH.setType("Journal Voucher");
		VH.update(con);
		VoucherHeaderId = VH.getId();
		
		//Insert Record in the egf_record_status table
		cm.UpdateVoucherStatus(dc,"Journal Voucher",con,VoucherHeaderId);
		}

	private void postInSupplierBillDetail(DataCollection dc) throws SQLException, TaskFailedException ,ParseException{
		SupplierBillDetail sbd = new SupplierBillDetail();
		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		statement = connection.createStatement();
		getResultSet("relation", "and code='" + dc.getValue("supplier_code") + "'", dc);
		supplierId = resultset.getInt("ID");
		sbd.setSupplierId(supplierId + "");
		resultset.close();
		statement.close();
		sbd.setWorksDetailId(dc.getValue("worksDetail_id"));
		sbd.setBillNumber(dc.getValue("supplierBillDetail_billNumber"));

		String sdt=(String)dc.getValue("supplierBillDetail_billDate");
		if(sdt==null || sdt.equalsIgnoreCase("")){
			sbd.setbilldate(sdt);
		}
		else
			{ 	String supbilldate = formatter.format(sdf.parse(sdt));
				sbd.setbilldate(supbilldate);
			}
		sbd.setMRNNumber(dc.getValue("supplierBillDetail_mrnNumber"));
		
		String mrndt=(String)dc.getValue("supplierBillDetail_mrnDate");
		if(mrndt==null || mrndt.equalsIgnoreCase(""))
			sbd.setMRNDate(mrndt);
		else
		{
			String sup_mrndate = formatter.format(sdf.parse(mrndt));
			sbd.setMRNDate(sup_mrndate);
		}
		sbd.setBillAmount(ExilPrecision.convertToString(dc.getValue("supplierBillDetail_billAmount"),2));
		double passedAmt=ExilPrecision.convertToDouble(dc.getValue("supplierBillDetail_passedAmount"),2);
		//double adjAmt= ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2);
		sbd.setPassedAmount(ExilPrecision.convertToString((passedAmt),2));
		sbd.setApprovedBy(dc.getValue("supplierBillDetail_approvedBy"));
		sbd.setNarration(dc.getValue("voucherHeader_narration"));
		sbd.setVoucherHeaderId(voucherHeaderId+"");
		//sbd.setTdsAmount(ExilPrecision.convertToString(dc.getValue("worksDetail_tdsAmount"),2));;
		//sbd.setTdsPaidToIt("0");
		sbd.setPaidAmount("0");
		sbd.setAdvAdjAmount(ExilPrecision.convertToString(dc.getValue("worksDetail_adjustmentAmount"),2));
		sbd.setOtherRecoveries(""+OtherRecoveries);
		if(!dc.getValue("asset_name").equalsIgnoreCase(""))
			sbd.setAssetId(dc.getValue("asset_name"));
		String mrnid = dc.getValue("mrnNumber");
		if(!mrnid.equalsIgnoreCase(""))
		{
			sbd.setMrnId(mrnid);
			/* 
			 * We need to change the bill received status to 1 in egf_mrnheader table
			 */
			updateBillReceivedMRN(mrnid,1);
		}		
		sbd.insert(connection);
		//supplierBillDetailId = sbd.getId();
	}

	private void UpdateInSupplierBillDetail(DataCollection dc) throws TaskFailedException, SQLException,ParseException{
		SupplierBillDetail sbd = new SupplierBillDetail();
		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	//	Date dt=new Date();

		statement = connection.createStatement();
		getResultSet("relation", "and code='" + dc.getValue("supplier_code") + "'", dc);
		supplierId = resultset.getInt("ID");
		sbd.setSupplierId(supplierId + "");
		resultset.close();
		statement.close();
		sbd.setWorksDetailId(dc.getValue("worksDetail_id"));
		sbd.setBillNumber(dc.getValue("supplierBillDetail_billNumber"));

		String sdt=(String)dc.getValue("supplierBillDetail_billDate");
		LOGGER.debug("billdate"+sdt);
		if(sdt==null || sdt.equalsIgnoreCase(""))
						sbd.setbilldate(sdt);
			else
			{
				String supbilldate = formatter.format(sdf.parse(sdt));
				//sbd.setbilldate(dc.getValue("supplierBillDetail_billDate"));
				sbd.setbilldate(supbilldate);
			}
		sbd.setMRNNumber(dc.getValue("supplierBillDetail_mrnNumber"));
		String mrnid = dc.getValue("mrnNumber");
		if(!mrnid.equalsIgnoreCase(""))
		{
			sbd.setMrnId(mrnid);
		}	

		String mrndt=(String)dc.getValue("supplierBillDetail_mrnDate");
		if(mrndt==null || mrndt.equalsIgnoreCase(""))
					sbd.setMRNDate(mrndt);
		else
		{
			String sup_mrndate = formatter.format(sdf.parse(mrndt));
			sbd.setMRNDate(sup_mrndate);
		}
		sbd.setBillAmount(ExilPrecision.convertToString(dc.getValue("supplierBillDetail_billAmount"),2));
		double passedAmt=ExilPrecision.convertToDouble(dc.getValue("supplierBillDetail_passedAmount"),2);
		sbd.setPassedAmount(ExilPrecision.convertToString(passedAmt,2));
		sbd.setApprovedBy(dc.getValue("supplierBillDetail_approvedBy"));
		sbd.setNarration(dc.getValue("voucherHeader_narration"));
		sbd.setVoucherHeaderId(String.valueOf(VoucherHeaderId));
		sbd.setPaidAmount("0");
		sbd.setAdvAdjAmount(ExilPrecision.convertToString(dc.getValue("worksDetail_adjustmentAmount"),2));
		sbd.setOtherRecoveries(""+OtherRecoveries);
		if(!dc.getValue("asset_name").equalsIgnoreCase(""))
			sbd.setAssetId(dc.getValue("asset_name"));
		try{
			statement = connection.createStatement();
			LOGGER.info("SELECT id from supplierbilldetail where voucherheaderid = " + VoucherHeaderId);
		ResultSet rs = statement.executeQuery("SELECT id from supplierbilldetail where voucherheaderid = " + VoucherHeaderId);
		if(rs.next()){
			sbd.setId(rs.getInt("id") + "");	
			int id = rs.getInt("id");
			/*
			 * Check whether MRN number is changed or not. If it is changed then we need to set the old mrn number billreceived status to 0 in egf_mrnheader table
			 */
			if(!mrnid.equalsIgnoreCase(""))
			{
				String oldMrnId = getOldMrnId(id); 
				if(!oldMrnId.equalsIgnoreCase(mrnid))
				{
					updateBillReceivedMRN(oldMrnId,0);
					updateBillReceivedMRN(mrnid,1);
				}					
			}	
			
			sbd.update(connection);
			
		}
		else 
			sbd.insert(connection);
		}catch(Exception e){
			LOGGER.error("error in getting the id ---"+e);
			throw new TaskFailedException(e.getMessage());
		}
		supplierId = sbd.getId();
		resultset.close();
		statement.close();
	}
	
	private void updateBillReceivedMRN(String mrnId,int status)throws TaskFailedException
	{
		String qryString = "";
		qryString = "Update egf_mrnheader set billreceived = "+status+" where id ="+mrnId;
		LOGGER.debug("qryString --> "+qryString);
		try
		{
		Statement statement = connection.createStatement();
		statement.executeQuery(qryString);
		statement.close();		
		}
		catch(SQLException e)
		{
			throw new TaskFailedException(e.getMessage());
		}
	}	

	 public String getOldMrnId(int suppBillDetailId)throws TaskFailedException
		{
			Statement statement;
			ResultSet resultset;
			String oldMrnId=null;

			try
			{
				statement = connection.createStatement();
				resultset = statement.executeQuery("select mrnid from supplierbilldetail where id ="+suppBillDetailId );
				resultset.next();
				oldMrnId = resultset.getString(1);
				resultset.close();
				statement.close();
			}
			catch(SQLException e)
			{
				LOGGER.debug("Exp="+e.getMessage());
				throw new TaskFailedException(e.getMessage());
			}
			return oldMrnId;
		}
	private void postInVoucherDetail(DataCollection dc, ArrayList transactions,Connection conn) throws SQLException, TaskFailedException	{
		double debitAmount=0, creditAmount=0;
		String glCode, accName, fucid;
		String supplierGrid[][] = (String[][])dc.getGrid("gridSupplierJournal");
		VoucherDetail vd = new VoucherDetail();
		//boolean isSupplierPaybleAccount = false;
		for(int i=0; i < supplierGrid.length; i++) {
			if(supplierGrid[i][0].equalsIgnoreCase("")) continue;
			//PreDefinedAccCodes codes = new PreDefinedAccCodes();
			
			if(supplierGrid[i][0].equals(EGovConfig.getProperty("egf_config.xml","SupPayCode","","SupplierCodes")) || supplierGrid[i][0].equals(EGovConfig.getProperty("egf_config.xml","SupAdvCode","","SupplierCodes")) ){
				dc.addMessage("exilError","Internal Codes Used  : transaction cancelled.");
				throw  taskExc;
			}
			if(supplierGrid[i][0].equals(EGovConfig.getProperty("egf_config.xml","ConPayCode","","ContractorCodes"))
					|| supplierGrid[i][0].equals(EGovConfig.getProperty("egf_config.xml","ConAdvCode","","ContractorCodes"))){
				dc.addMessage("exilError","ContractorPayble/ContractorAdvance present : transaction cancelled.");
				throw  taskExc;
			}
			if(	ExilPrecision.convertToDouble(supplierGrid[i][2], 2) > 0
					&& ExilPrecision.convertToDouble(supplierGrid[i][3], 2) > 0){
				dc.addMessage("exilError","both debit and credit can not be provided for the same account.");
				throw  taskExc;
			}

			debitAmount += ExilPrecision.convertToDouble(supplierGrid[i][2], 2);
			creditAmount += ExilPrecision.convertToDouble(supplierGrid[i][3], 2);
		}

		if( !(debitAmount+creditAmount > 0.0) ){
			dc.addMessage("exilError","no transaction involved.");
			throw  taskExc;
		}

		/*to add the default supplier payable and tds codes;add any extra recoveries in that function */
		//addSupplierAndTDSAcc(dc,transactions,debitAmount,creditAmount);
		//postDefaultsInVoucherDtl(dc,transactions);
		//int lineID=transactions.size()+1;
		int lineID=1;
		for(int i=0; i < supplierGrid.length; i++){
			if(supplierGrid[i][0].equalsIgnoreCase("")) continue;
			//0. accountCode     1. accountHead     2. debitAmount   3. creditAmount    4. narration 5. function code
			vd.setLineID(String.valueOf(lineID));
			lineID++;
			glCode = supplierGrid[i][0];
			accName = cm.getCodeName(glCode, conn);
			fucid = supplierGrid[i][5];
			vd.setVoucherHeaderID(voucherHeaderId + "");
			vd.setGLCode(glCode);
			vd.setAccountName(accName);
			vd.setDebitAmount(ExilPrecision.convertToString(supplierGrid[i][2],2));
			vd.setCreditAmount(ExilPrecision.convertToString(supplierGrid[i][3],2));
			vd.setNarration(supplierGrid[i][4]);
			vd.insert(connection);

			Transaxtion transaction = new Transaxtion();
			transaction.setGlCode(glCode);
			transaction.setGlName(accName);
			transaction.setDrAmount(ExilPrecision.convertToString(supplierGrid[i][2],2));
			transaction.setCrAmount(ExilPrecision.convertToString(supplierGrid[i][3],2));
			transaction.setVoucherLineId(String.valueOf(vd.getId()));
			transaction.setVoucherHeaderId(String.valueOf(voucherHeaderId));
			transaction.setNarration(supplierGrid[i][4]);
			/*String detailAmt=ExilPrecision.convertToString(supplierGrid[i][2],2);
			if(detailAmt==null || detailAmt.trim().length()==0 || Double.parseDouble(detailAmt)== 0 )
				detailAmt=ExilPrecision.convertToString(supplierGrid[i][3],2);
			*/
			transaction.setTransaxtionParam(getRequiredData(dc,i+1,vd));
		/* Newly added for function code - March 27, 2006 */
			transaction.setFunctionId(fucid);
		/* End */
			transactions.add(transaction);
		}
		/*new*/
		lineID=transactions.size()+1;
		int start=transactions.size();
		/*to add the default contractor payable and tds codes;add any extra recoveries in that function */
		OtherRecoveries=creditAmount;
		addSupplierAndTDSAcc(dc,transactions,creditAmount,conn);
		postDefaultsInVoucherDtl(dc,transactions,lineID,start,conn);
		/*new*/
	}
private void  deleteFromVoucherDetail ()throws SQLException,Exception
		{
			statement = connection.createStatement();
				LOGGER.debug("select LineId  from VoucherDetail where voucherheaderid="+VoucherHeaderId+" order by LineId");

				resultset = statement.executeQuery("select LineId from VoucherDetail where voucherheaderid="+VoucherHeaderId+" order by LineId");
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
						String delQuery="DELETE FROM voucherdetail WHERE voucherheaderid="+VoucherHeaderId+" AND ID NOT IN (SELECT VOUCHERLINEID FROM GENERALLEDGER WHERE voucherheaderid="+VoucherHeaderId+")";
						LOGGER.debug(".....delQuery ........."+delQuery);
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
	/**
	 * This function will update the voucher detail for the
	 * existing records and then insert new records if any
	 * @param dc
	 * @param transactions
	 * @throws SQLException
	 * @throws TaskFailedException
	 */
	private void updateVoucherDetail(DataCollection dc, ArrayList transactions,Connection conn) throws SQLException, TaskFailedException {
		double debitAmount=0, creditAmount=0;
		VoucherDetail VD =new VoucherDetail();
		String[][] voucherDetail = (String[][])dc.getGrid("gridSupplierJournal");
		ArrayList vDetailId=new ArrayList();
		ArrayList vDetailLineId=new ArrayList();
		ArrayList vAccountName=new ArrayList();
		ArrayList vDetailGlcode=new ArrayList();
		boolean isContractorPaybleAccount = false;

		for(int i=0; i < voucherDetail.length; i++){
			if(voucherDetail[i][0].equalsIgnoreCase("")) continue;
		//	PreDefinedAccCodes codes = new PreDefinedAccCodes();
			
			if(voucherDetail[i][0].equals(EGovConfig.getProperty("egf_config.xml","ConPayCode","","ContractorCodes")) || voucherDetail[i][0].equals(EGovConfig.getProperty("egf_config.xml","ConAdvCode","","ContractorCodes"))){
				dc.addMessage("exilError","Internal Codes Used  : transaction cancelled.");
				throw  taskExc;
			}
			if(voucherDetail[i][0].equals(EGovConfig.getProperty("egf_config.xml","SupPayCode","","SupplierCodes"))
					|| voucherDetail[i][0].equals(EGovConfig.getProperty("egf_config.xml","SupAdvCode","","SupplierCodes"))){
				dc.addMessage("exilError","Supplier Payble/supplier Advance present : transaction cancelled.");
				throw  taskExc;
			}
			if(	ExilPrecision.convertToDouble(voucherDetail[i][2], 2) > 0
					&& ExilPrecision.convertToDouble(voucherDetail[i][3], 2) > 0){
				dc.addMessage("exilError","both debit and credit can not be provided for the same account.");
				throw  taskExc;
			}

			debitAmount += ExilPrecision.convertToDouble(voucherDetail[i][2], 2);
			creditAmount += ExilPrecision.convertToDouble(voucherDetail[i][3], 2);
		}
		if( !(debitAmount+creditAmount > 0.0) ){
			dc.addMessage("exilError","no transaction involved.");
			throw  taskExc;
		}
		//Get the count of line items present.update the exsting and insert the new.
		statement = connection.createStatement();
		LOGGER.info("select id ,LineId ,ACCOUNTNAME,glCode from VoucherDetail where voucherheaderid="+VoucherHeaderId+" order by LineId");

		resultset = statement.executeQuery("select id ,LineId,ACCOUNTNAME,glCode from VoucherDetail where voucherheaderid="+VoucherHeaderId+" order by LineId");
		int c=0;
		while(resultset.next()){
			vDetailLineId.add(c,(resultset.getString("LineId")));
			vDetailId.add(c,(resultset.getString("id")));
			vAccountName.add(c,(resultset.getString("ACCOUNTNAME")));
			vDetailGlcode.add(c,(resultset.getString("glCode")));

			c++;
		}
		//int count=vDetailLineId.size();

		resultset.close();
		statement.close();
		voucherDetail = (String[][])dc.getGrid("gridSupplierJournal");
		LOGGER.info("Exisitng count is: "+vDetailId.size()+" Grid Length :"+voucherDetail.length +" accountname length :"+vAccountName.size());

		int i=0;

		for(i=0;i<voucherDetail.length;i++){
			//String VAN=vAccountName.get(i).toString();
				if(voucherDetail[i][0].equalsIgnoreCase("")) continue;
				VD.setVoucherHeaderID(String.valueOf(VoucherHeaderId));
				VD.setGLCode(voucherDetail[i][0]);
				VD.setAccountName(cm.getCodeName(voucherDetail[i][0], conn));
				if(voucherDetail[i][2].length()>0)
					VD.setDebitAmount(ExilPrecision.convertToString(voucherDetail[i][2],2));
				else
					VD.setDebitAmount("0");
				if(voucherDetail[i][3].length()>0)
					VD.setCreditAmount(ExilPrecision.convertToString(voucherDetail[i][3],2));
				else
					VD.setCreditAmount("0");
				VD.setNarration(voucherDetail[i][4]);
				VD.setLineID((i+1) + "");
				if(voucherDetail[i][0].length()>0)
					VD.insert(connection);
			//	}
				Transaxtion transaction = new Transaxtion();
				transaction.setGlCode(voucherDetail[i][0]);
				transaction.setGlName(voucherDetail[i][1]);

				if(voucherDetail[i][2].length()>0)
					transaction.setDrAmount(ExilPrecision.convertToString(voucherDetail[i][2],2));
				else
					transaction.setDrAmount("0");
				if(voucherDetail[i][3].length()>0)
					transaction.setCrAmount(ExilPrecision.convertToString(voucherDetail[i][3],2));
				else
					transaction.setCrAmount("0");
				transaction.setVoucherLineId(String.valueOf(VD.getId()));
				transaction.setVoucherHeaderId(String.valueOf(VoucherHeaderId));
				transaction.setNarration(voucherDetail[i][4]);
			/*	String detailAmt=ExilPrecision.convertToString(voucherDetail[i][2],2);
				if(detailAmt==null || detailAmt.trim().length()==0 || Double.parseDouble(detailAmt)== 0 )
					detailAmt=ExilPrecision.convertToString(voucherDetail[i][3],2);
			*/	transaction.setTransaxtionParam(getRequiredData(dc,i+1,VD));
				 fuctid=voucherDetail[i][5];
					transaction.setFunctionId(voucherDetail[i][5]);
				transactions.add(transaction);
			}
		int lineID=transactions.size()+1;
		int start=transactions.size();
		/*to add the default contractor payable and tds codes;add any extra recoveries in that function */
		OtherRecoveries=creditAmount;
		addSupplierAndTDSAcc(dc,transactions,creditAmount,conn);
		//postDefaultsInVoucherDtl(dc,transactions,lineID,start);
		String glCode="";

		
		for(int k=start; k < transactions.size(); k++){
			VoucherDetail vd = new VoucherDetail();
			Transaxtion trn=(Transaxtion)transactions.get(k);
			if(trn.getGlCode().length()==0) continue;
			//0. accountCode     1. accountHead     2. debitAmount   3. creditAmount    4. narration
			vd.setLineID(String.valueOf(lineID));
			lineID++;
			glCode = trn.getGlCode();
			vd.setVoucherHeaderID(String.valueOf(VoucherHeaderId));
			vd.setGLCode(glCode);
			LOGGER.debug("getAccountName(glCode, dc)    "+cm.getCodeName(glCode, conn)+"trn.getDrAmount()   "+trn.getDrAmount()+"trn.getCrAmount()   "+trn.getCrAmount());
			vd.setAccountName(cm.getCodeName(glCode, conn));
			vd.setDebitAmount(trn.getDrAmount());
			vd.setCreditAmount(trn.getCrAmount());
			vd.setNarration(dc.getValue("voucherHeader_narration"));
			statement = connection.createStatement();
			LOGGER.info("select id from VoucherDetail where voucherheaderid="+VoucherHeaderId+" glcode="+glCode);

			resultset = statement.executeQuery("select id from VoucherDetail where voucherheaderid="+VoucherHeaderId+" and glcode="+glCode);
			if(resultset.next()){
				vd.setId(resultset.getInt("id") + "");
				vd.update(connection);
			}
			else{
				vd.insert(connection);
			}
			resultset.close();
			statement.close();
			trn.setVoucherLineId(String.valueOf(vd.getId()));
			trn.setVoucherHeaderId(String.valueOf(VoucherHeaderId));
			trn.setNarration(dc.getValue("voucherHeader_narration"));
		}
	}
	private void postDefaultsInVoucherDtl(DataCollection dc, ArrayList transactions,int lineID,int start,Connection conn) throws SQLException, TaskFailedException	{
 		String glCode="";
 		VoucherDetail vd = new VoucherDetail();
		for(int i=start; i < transactions.size(); i++){
			Transaxtion trn=(Transaxtion)transactions.get(i);
			if(trn.getGlCode().length()==0) continue;
			//0. accountCode     1. accountHead     2. debitAmount   3. creditAmount    4. narration
			vd.setLineID(String.valueOf(lineID));
			lineID++;
			glCode = trn.getGlCode();
			vd.setVoucherHeaderID(voucherHeaderId + "");
			vd.setGLCode(glCode);
			vd.setAccountName(cm.getCodeName(glCode, conn));
			vd.setDebitAmount(trn.getDrAmount());
			vd.setCreditAmount(trn.getCrAmount());
			vd.setNarration(dc.getValue("voucherHeader_narration"));
			vd.insert(connection);
			trn.setVoucherLineId(String.valueOf(vd.getId()));
			trn.setVoucherHeaderId(String.valueOf(voucherHeaderId));
			trn.setNarration(dc.getValue("voucherHeader_narration"));
		}
	}

	
	public boolean validate(DataCollection dc)throws TaskFailedException{
   		DataValidator valid = new DataValidator();
   /*		if(!valid.checkDepartmentId(dc.getValue("department_id"), connection)){
   			dc.addMessage("exilInvalidID", dc.getValue("department_id"));
   			return false;
   		} */
   		if(!valid.checkFundSourceId(dc.getValue("fundSource_id"), connection)){
   			dc.addMessage("exilInvalidID", dc.getValue("fundSource_id"));
   			return false;
   		}
   		if(!valid.checkFundId(dc.getValue("fund_id"), connection)){
   			dc.addMessage("exilInvalidID", dc.getValue("fund_id"));
   			return false;
   		}
   		return true;
   	}

	public ArrayList getRequiredData(DataCollection dc,Transaxtion trn)throws TaskFailedException{
		requiredList=new ArrayList();
		ArrayList formList=new ArrayList();
		/*formList.add("fund_id");
		formList.add("department_id");
		formList.add("relation_id");
		formList.add("worksDetail_id");*/
		try{
			Statement st=connection.createStatement();
			ResultSet rset=st.executeQuery("select controlname from screencontrols where screenname='supplierjournal'");
			while(rset.next()){
				formList.add(rset.getString(1));
			}
			rset.close();
			st.close();
		}catch(Exception e){
			throw new TaskFailedException(e.toString());
		}
		/*
		 * getting amount for adding to transactionparam object
		 */
	//	String supplierGrid[][] = (String[][])dc.getGrid("gridSupplierJournal");
//		String detailAmt=ExilPrecision.convertToString(supplierGrid[0][2],2);
		//if(detailAmt==null || detailAmt.trim().length()==0 || Double.parseDouble(detailAmt)== 0 )
			//detailAmt=ExilPrecision.convertToString(supplierGrid[0][3],2);
		
		String formControlName;
		for(int i=0;i<formList.size();i++)
		{
			formControlName=(String)formList.get(i);
			TransaxtionParameter transParam = new TransaxtionParameter();
			if(trn!=null)
			{
				try 
				{
					LOGGER.debug("###### getGLCode() is"+trn.getGlCode());
					transParam.setGlcodeId(cmImpl.getGlCodeId(trn.getGlCode(),connection));
				} catch (Exception e) {
					LOGGER.debug("Exp="+e.getMessage());
					throw new TaskFailedException();
				}
				transParam.setDetailAmt(!trn.getDrAmount().equals("0")?trn.getDrAmount():trn.getCrAmount());
			}
			else
			{
				transParam.setGlcodeId(EGovConfig.getProperty("egf_config.xml","SupPayCode","SupplierCodes"));
				transParam.setDetailAmt(calculateNetAmt(dc));
			}
		//	String code=(String)dc.getValue("supplier_code");
			String acKey=(String)dc.getValue("relation_id");
			transParam.setDetailKey(acKey);
			
			LOGGER.debug(" AccountDetailKey is "+transParam.getDetailKey());
			LOGGER.debug(" dc.getValue(formControlName)  "+dc.getValue(formControlName));
			
			transParam.setDetailName(formControlName);
			requiredList.add(transParam);
		}
		return requiredList;
	}
	public String calculateNetAmt(DataCollection dc)
	{
	 	//Add the net payable to the advance adjusted because both gets accounted to the same code -Contractor Payable
	  BigDecimal netAmt=new BigDecimal(ExilPrecision.convertToDouble(dc.getValue("worksDetail_NetAmount"),2)).add (new BigDecimal(ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2)));
		LOGGER.debug("Net Amount being credited to Internal Code is :"+netAmt);
		return netAmt.toString();
	}
	private int postInWorksDetail(DataCollection dc)throws SQLException,ParseException,TaskFailedException{
		int worksID=0;
		WorksDetail wDet=new WorksDetail();
		wDet.setId(dc.getValue("worksDetail_id"));
		double passAmount=ExilPrecision.convertToDouble(dc.getValue("supplierBillDetail_passedAmount"),2);
		double adjAmt=ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2);
	//	double advAmt=ExilPrecision.convertToDouble(dc.getValue("worksDetail_advanceAmount"),2);
		wDet.setAdvanceAdjustment(ExilPrecision.convertToString(adjAmt,2));
		wDet.setPassedAmount(ExilPrecision.convertToString(passAmount,2));
		wDet.update(connection);
		return worksID;
	}
	/**
	 * This function is called on Modify.
	 * This Function updates the values in worksdetail table
	 * @param dc
	 * @param con
	 * @author Lakshmi
	 * @throws TaskFailedException
	 */
	private int updateWorksDetail(DataCollection dc,Connection con)throws SQLException,ParseException,TaskFailedException
	{
		int worksID=0;
		WorksDetail wDet=new WorksDetail();
		Statement st=con.createStatement();
		double oldPassAmount=0;
		double oldadjAmt=0;
		String oldWorksId="";
		String newWorksID=dc.getValue("worksDetail_id");
		
		double passAmount=ExilPrecision.convertToDouble(dc.getValue("supplierBillDetail_passedAmount"),2);
		double adjAmt=ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2);
	//	double advAmt=ExilPrecision.convertToDouble(dc.getValue("worksDetail_advanceAmount"),2);
		double TrxnPassAmount=passAmount;
		double TrxnAdjAmt=adjAmt;
//		Getting Old PassedAmount and Old AdjustedAmount
        LOGGER.debug("query:select advadjamt,passedamount from supplierbilldetail where voucherheaderid="+dc.getValue("voucherHeader_id"));
		ResultSet rs=st.executeQuery("select advadjamt,passedamount,worksDetailId from supplierbilldetail where voucherheaderid="+dc.getValue("voucherHeader_id"));
		if(rs.next())
		{
			oldadjAmt=rs.getDouble(1);
			oldPassAmount=rs.getDouble(2);
			oldWorksId=rs.getString(3);
			adjAmt=adjAmt-oldadjAmt;
			passAmount=passAmount-oldPassAmount;
            LOGGER.debug("Inside oldadjAmt "+oldadjAmt+"oldPassAmount: "+oldPassAmount);
		}
        LOGGER.debug("adjAmt "+adjAmt+"passAmount: "+passAmount);
        if(!newWorksID.equalsIgnoreCase(oldWorksId))
		{ 
        	
        	wDet.setId(oldWorksId);
			wDet.setAdvanceAdjustment("-"+ExilPrecision.convertToString(oldadjAmt,2));
			wDet.setPassedAmount("-"+ExilPrecision.convertToString(oldPassAmount,2));
			wDet.update(connection);
			wDet.setId(dc.getValue("worksDetail_id"));
			LOGGER.debug("passedAmount1:"+TrxnPassAmount);
			LOGGER.debug("passedAmount:"+ExilPrecision.convertToString(TrxnPassAmount,2));
			wDet.setAdvanceAdjustment(ExilPrecision.convertToString(TrxnAdjAmt,2));
			wDet.setPassedAmount(ExilPrecision.convertToString(TrxnPassAmount,2));
			wDet.update(connection);
		}else
		{
			wDet.setId(dc.getValue("worksDetail_id"));
			wDet.setAdvanceAdjustment(ExilPrecision.convertToString(adjAmt,2));
			wDet.setPassedAmount(ExilPrecision.convertToString(passAmount,2));
			wDet.update(connection);
		}
        
	/*	wDet.setId(dc.getValue("worksDetail_id"));
		wDet.setAdvanceAdjustment(ExilPrecision.convertToString(adjAmt,2));
		wDet.setPassedAmount(ExilPrecision.convertToString(passAmount,2));
		wDet.update(connection);*/
		return worksID;
	}
	private void addSupplierAndTDSAcc(DataCollection dc,ArrayList transactions,double creditAmount,Connection conn)throws TaskFailedException{
	//	PreDefinedAccCodes codes=new PreDefinedAccCodes();
		//supplier payable
		double passAmount=ExilPrecision.convertToDouble(dc.getValue("supplierBillDetail_passedAmount"),2); 
        double adjustAmt=ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2);
		double tdsAmount=ExilPrecision.convertToDouble(dc.getValue("worksDetail_tdsAmount"),2);
		//Transaxtion trn=new Transaxtion();
			//advance
		String adjAmt=dc.getValue("worksDetail_adjustmentAmount"); 
		try{
			if(Double.isNaN(Double.parseDouble(adjAmt)))return;
		}catch(Exception e){
			throw new TaskFailedException(e.getMessage());
		}
		
		//if Supplers Payable and Suppliers advance Payable are not mapped to same code make two different entries
		//else make single entry

		String supPayable=EGovConfig.getProperty("egf_config.xml","SupPayCode","","SupplierCodes");
		String supAdvance=EGovConfig.getProperty("egf_config.xml","SupAdvCode","","SupplierCodes");

		if(!supPayable.equalsIgnoreCase(supAdvance))
		{
			Transaxtion trn=new Transaxtion();
			if(ExilPrecision.convertToDouble((passAmount-creditAmount)-tdsAmount-adjustAmt,2)>0){
				trn.setGlCode(supPayable);
				trn.setGlName(cm.getCodeName(supPayable,conn));
				trn.setDrAmount("0");
				trn.setCrAmount(ExilPrecision.convertToString((passAmount-creditAmount)-tdsAmount-adjustAmt,2));
				trn.setNarration(dc.getValue("voucherHeader_narration"));
				trn.setTransaxtionParam(getRequiredData(dc,trn));
				transactions.add(trn);
			}
			//advance
			Transaxtion trnadv=new Transaxtion();
			if(adjustAmt>0)
			{
				trnadv.setGlCode(supAdvance);
				trnadv.setGlName(cm.getCodeName(supAdvance,conn));
				trnadv.setDrAmount("0");
				trnadv.setCrAmount(ExilPrecision.convertToString(adjustAmt,2));
				trnadv.setNarration(dc.getValue("voucherHeader_narration"));
				trnadv.setTransaxtionParam(getRequiredData(dc,trnadv));
				transactions.add(trnadv);
			}
		}
		else
		{
			Transaxtion trn=new Transaxtion();
			if(ExilPrecision.convertToDouble((passAmount-creditAmount)-tdsAmount,2)>0){
				trn.setGlCode(supPayable);
				trn.setGlName(cm.getCodeName(supPayable,conn));
				trn.setDrAmount("0");
				trn.setCrAmount(ExilPrecision.convertToString((passAmount-creditAmount)-tdsAmount,2));
				trn.setNarration(dc.getValue("voucherHeader_narration"));
				trn.setTransaxtionParam(getRequiredData(dc,trn));
				transactions.add(trn);
			}
		}
		
	
	}
	private boolean isValidAmount(DataCollection dc)throws TaskFailedException{
		double passedAmount=ExilPrecision.convertToDouble(dc.getValue("supplierBillDetail_passedAmount"),2);
		double adjustmentAmount=ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2);
		double billAmount=ExilPrecision.convertToDouble(dc.getValue("supplierBillDetail_billAmount"),2);
		//retreive from server
        double oldPassAmount=0;
        double oldadjAmt=0;
		double totalValue=0;
		double totalPassedAmount=0;
		double advanceAmount=0;
		double advanceAdjAmount=0;
		try{
			Statement st=connection.createStatement();
			ResultSet rset=st.executeQuery("select totalvalue,advanceamount,passedamount,advanceadj from worksdetail where id ="+dc.getValue("worksDetail_id"));
			while(rset.next()){
				totalValue=ExilPrecision.convertToDouble(rset.getString(1),2);
				advanceAmount=ExilPrecision.convertToDouble(rset.getString(2),2);
				totalPassedAmount=ExilPrecision.convertToDouble(rset.getString(3),2);
				advanceAdjAmount=ExilPrecision.convertToDouble(rset.getString(4),2);
			}
			rset.close();
            if(dc.getValue("modeOfExec").equalsIgnoreCase("edit"))
            {
                ResultSet rs=st.executeQuery("select advadjamt,passedamount from supplierbilldetail where voucherheaderid="+dc.getValue("voucherHeader_id"));
                LOGGER.info("select advadjamt,passedamount from supplierbilldetail where voucherheaderid="+dc.getValue("voucherHeader_id"));
                if(rs.next())
                {
                    oldadjAmt=rs.getDouble(1);
                    oldPassAmount=rs.getDouble(2);
                    advanceAdjAmount=advanceAdjAmount-oldadjAmt;
                    totalPassedAmount=totalPassedAmount-oldPassAmount;
                }
            }
			st.close();
		}catch(Exception e){
			dc.addMessage("exilRPError",e.toString());
			throw new TaskFailedException(e.getMessage());
		}

		if((billAmount+totalPassedAmount)>totalValue){
			dc.addMessage("exilRPError","Bill amount cannot exceeded total amount");
			return false;
		}
	
		if(adjustmentAmount>(advanceAmount-advanceAdjAmount)){
			dc.addMessage("exilRPError","Adjustment amount cannot exceed advance amount:");
			return false;
		}
		if(((totalValue-totalPassedAmount)-(passedAmount-adjustmentAmount))<((advanceAmount-advanceAdjAmount)-adjustmentAmount)){
				dc.addMessage("exilRPError","Adjust advance amount and passed amount ");
				return false;
		}
		
		return true;
	}

	public ArrayList getRequiredData(DataCollection dc,int rowIdx,VoucherDetail vd) throws TaskFailedException{
		DataExtractor de=DataExtractor.getExtractor();
		String sql="select ID as \"ID\",name as  \"name\",tableName as \"tableName\","+
		"description as \"description\",columnName as \"columnName\",attributeName as \"attributeName\""+
		",nbrOfLevels as  \"nbrOfLevels\" from accountdetailtype";
		if(detailTypes==null){
			detailTypes=de.extractIntoMap(sql,"ID",AccountDetailType.class);
		}
		ArrayList reqParams=new ArrayList();
		String[][] entityGrid=dc.getGrid("entities_grid");
		for(int i=1;i<entityGrid.length;i++)
		{
			//search entities only for the rowindex sent
			//entityGrid[i][0]-dettypeid entityGrid[i][1]-detkey  entityGrid[i][2]-detvalue entityGrid[i][3]-rowIndex
			if(entityGrid[i][3].equals(String.valueOf(rowIdx))){
				AccountDetailType accType=(AccountDetailType)detailTypes.get(entityGrid[i][0]);
				if(accType!=null){
					TransaxtionParameter reqData=new TransaxtionParameter();
					try {
						reqData.setGlcodeId(cmImpl.getGlCodeId(vd.getGLCode(), connection)+"");
					} catch (Exception e) {
						throw new TaskFailedException();
					}
					reqData.setDetailName(accType.getAttributeName());
					reqData.setDetailKey(entityGrid[i][1]);
					reqData.setDetailAmt(vd.getDebitAmount().equalsIgnoreCase("0.0")?vd.getCreditAmount():vd.getDebitAmount());
					reqParams.add(reqData);
				}
			}
		}
		if(requiredList!=null){
			for(int a=0;a<requiredList.size();a++){
				reqParams.add(requiredList.get(a));
			}
		}
		return reqParams;
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
public void editVoucher(DataCollection dc,Connection con) throws TaskFailedException{
	//int status=0;
	 Statement st=null;
	ResultSet rset=null;
	voucherNumber=dc.getValue("voucherHeader_voucherNumber");
	String vhID=dc.getValue("voucherHeader_id");
	String voucherNum="";
	String subVN="";
	LOGGER.debug("voucherNumber:"+voucherNumber);
	try
	{
		st=con.createStatement();
		LOGGER.debug("select vouchernumber from voucherheader where id="+vhID);
		rset=st.executeQuery("select vouchernumber from voucherheader where id="+vhID);
		if(rset.next())
		{
			voucherNum=rset.getString(1);
			subVN=voucherNum.substring(0,2);
		}
		rset.close();
		voucherNumber=subVN+voucherNumber;
		dc.addValue("voucherHeader_voucherNumber",voucherNumber);
		LOGGER.debug("voucherNumber:"+voucherNumber);
		if(!voucherNum.equals(voucherNumber))
		{
			LOGGER.debug("voucherNum NOt Equal");
			try{
				if(!cm.isUniqueVN(dc, con,voucherNumber))			
					throw  new TaskFailedException();
			}catch(Exception e){
				throw new TaskFailedException(e.getMessage()); 
			}
		}
	}
        catch(SQLException sq){throw new TaskFailedException(sq.getMessage());}

	//ArrayList transactions=new ArrayList();
	try{
		/*added in egf*/
	//	checkValidCodes(dc);
		/*added in egf*/
		if(!isValidAmount(dc)){
			dc.addMessage("exilRPError","Server Validation Failed..Check The Data Sent");
				throw  taskExc;
		}

		//getRequiredData(dc);
		updateInVoucherHeader(dc,con);
		cm.updateVoucherMIS("U",con,VoucherHeaderId,(String)dc.getValue("field_id"),(String)dc.getValue("fundSource_id"),(String)dc.getValue("scheme"),(String)dc.getValue("subscheme"),dc.getValue("voucherHeader_voucherDate"));
		updateVoucherDetail(dc, transactions,con);
		updateWorksDetail(dc,con);
		//Update SupplierBillDetail Table After Updating WorkDetail Since it extracts values from SupplierBillDetail table
//		Update SupplierBillDetail Table After Updating WorkDetail Since it extracts values from SupplierBillDetail table
		UpdateInSupplierBillDetail(dc);
		updateBillStatus(dc);
		ChartOfAccounts engine = ChartOfAccounts.getInstance();
		Transaxtion txnList[] = new Transaxtion[transactions.size()];
		txnList=(Transaxtion[])transactions.toArray(txnList);
		LOGGER.debug("after txnList ######### ----"+txnList.length);
		if(!engine.postTransaxtions(txnList, con, dc))throw taskExc;
		deleteFromVoucherDetail();
			dc.addMessage("eGovSuccess", "CGNumber is "+cgNum+" and VoucherNumber is "+voucherNumber);
	}catch(Exception e){
		LOGGER.error("Exception in update."+e);
		dc.addMessage("exilRPError","Error in update :"+e.getMessage());
		throw new TaskFailedException();
	}
}
	public void reverse() throws TaskFailedException{
		GlReverser glr=new GlReverser();
		SupplierBillDetail sbd=new SupplierBillDetail();
		WorksDetail wDet=new WorksDetail();
		ResultSet rset;
		double passedAmt=ExilPrecision.convertToDouble(dc.getValue("supplierBillDetail_passedAmount"),2);
		double adjAmt= ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2);
		double pdAmount=0;
		//double pAmount=(passedAmt+adjAmt);
		String vhId=null;
		String sphId=null;
		try{
			Statement st=connection.createStatement();
	//		String cgn=dc.getValue("cgnNum");
			rset=st.executeQuery("select id from voucherHeader where cgn='"+dc.getValue("cgnNum")+"'");
			st.close();
			while(rset.next()){
				vhId=rset.getString(1);
			}

			rset=st.executeQuery("Select id from subledgerPaymentHeader where voucherHeaderId="+vhId);
			while(rset.next()){
				sphId=rset.getString(1);
			}
				if(sphId==null){
					 rset=st.executeQuery("select id from supplierBillDetail where voucherHeaderid="+vhId);
					 while(rset.next()){
						sbd.setId(rset.getString(1));
					}
					rset.close();
					st.close();

					SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
					//Date dt=new Date();
					String vdt=(String)dc.getValue("voucherHeader_effDate");
					String voucherHeader_effDate = formatter.format(sdf.parse(vdt));

					glr.reverse(connection,dc.getValue("cgnNum"),newVcNo,voucherHeader_effDate,cgNum,dc);
					sbd.reverse(connection);

					wDet.setId(dc.getValue("worksDetail_id"));
					wDet.reverseNegative(connection,pdAmount,adjAmt,passedAmt,0);
					}else{
						dc.addMessage("Supplier is already paid");
					}
			}catch(Exception e){
				dc.addMessage("exilRPError",e.getMessage());
				throw new TaskFailedException();
			}

	}
}
