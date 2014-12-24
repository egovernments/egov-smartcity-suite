/**
 * Created on Jan 10, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.transactions;    

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.client.PurchaseBillDelegate;
import org.egov.billsaccounting.client.PurchaseBillForm;
import org.egov.billsaccounting.model.Worksdetail;
import org.egov.billsaccounting.services.WorksBillService;
import org.egov.infstr.utils.EGovConfig;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.model.bills.EgBillregister;
import org.egov.services.bills.BillsService;
import org.egov.utils.FinancialConstants;
import org.egov.utils.GetEgfManagers;

import com.exilant.GLEngine.AccountDetailType;
import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;
import com.exilant.GLEngine.TransaxtionParameter;
import com.exilant.eGov.src.common.DataValidator;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.common.GlReverser;
import com.exilant.eGov.src.domain.ActionDetails;
import com.exilant.eGov.src.domain.BillRegisterBean;
import com.exilant.eGov.src.domain.EgfStatusChange;
import com.exilant.eGov.src.domain.SupplierBillDetail;
import com.exilant.eGov.src.domain.VoucherDetail;
import com.exilant.eGov.src.domain.VoucherHeader;
import com.exilant.eGov.src.domain.WorksDetail;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.dataservice.DataExtractor;

public class SupplierJournal_vmc extends AbstractTask{
	private static final Logger LOGGER=Logger.getLogger(SupplierJournal_vmc.class);
	private UserService userService;
	private BillsService billsService;
	private Connection connection;
	private Statement statement;
	private ResultSet resultset;
	private DataCollection dc;
	private HashMap detailTypes;
	private TaskFailedException taskExc;
	private int supplierId = 0, voucherHeaderId=0, supplierBillId=0;
	private ArrayList requiredList;
	public double OtherRecoveries=0.0;
	public String voucherNumber;
	public ArrayList defReqData;
	public String fiscalPid;
	public String vType;
	public String eg_voucher;
	public String cgNum;
	public String fId;
	public String newVcNo;
	public String fuctid="";
	int VoucherHeaderId;
	String todayTime;
	ArrayList transactions = new ArrayList();
	CommonMethodsI cmImpl=new CommonMethodsImpl();
	EGovernCommon cm = new EGovernCommon();
	//no argument Constructor
	public SupplierJournal_vmc(){}

	public void execute(String taskName,
							String gridName,
							DataCollection dc,
							Connection conn,
							boolean erroOrNoData,
							boolean gridHasColumnHeading, String prefix) throws TaskFailedException{

		this.dc = dc;
		this.connection = conn;
		taskExc = new TaskFailedException();

		if(dc.getValue("showGlEntries").equalsIgnoreCase("show"))
		{
			StringBuffer str=new StringBuffer(";");
			StringBuffer stradv=new StringBuffer("");
			str=str.append("SupPayCode"+"^");
			String code1=EGovConfig.getProperty("egf_config.xml","SupPayCode","","SupplierCodes");
			str=str.append(code1+"^");
			String name1=cm.getCodeName(code1,conn);   
			str=str.append(name1+";");
			String outp=str.toString();
			String advCode=EGovConfig.getProperty("egf_config.xml","SupAdvCode","","SupplierCodes");
			if(!code1.equalsIgnoreCase(advCode))
			{      
				stradv=stradv.append("SupAdvCode"+"^");
				stradv=stradv.append(advCode+"^");
				String name2=cm.getCodeName(advCode,conn);
				stradv=stradv.append(name2+";");
				outp=outp+stradv.toString();
			}
			dc.addValue("glCodeName",outp);
			
			//LOGGER.debug(" xxxxxxxxxx  "+outp);
			return;
		}
		fId=dc.getValue("fund_id").toString();
		if(dc.getValue("modeOfExec").equalsIgnoreCase("modify")){
			try{
				SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				Date dt=new Date();
				String vdt=(String)dc.getValue("voucherHeader_effDate");
				dt = sdf.parse( vdt );
				String voucherHeader_effDate = formatter.format(dt);
				String vt=(String)dc.getValue("voucherHeader_voucherDate");
				int dateCheck=1;
				int CurdateCheck=1;
				dateCheck=cm.isValidDate(dc, conn,vt,vdt);
				if(dateCheck==0)
					return;
				CurdateCheck=cm.isCurDate(dc,conn,vdt);
				if(CurdateCheck==0)
					return;
				//fiscalPid=getFiscalPeriod(dc.getValue("voucherHeader_effDate"));
				fiscalPid=cm.getFiscalPeriod(voucherHeader_effDate);

				newVcNo=cm.getVoucherNumber(fId, FinancialConstants.PURCHBILL_VOUCHERNO_TYPE, dc.getValue("voucherHeader_newVcNo"), conn);
				//rvNumber(dc,conn,fId);
				vType=newVcNo.substring(0,Integer.parseInt(FinancialConstants.VOUCHERNO_TYPE_LENGTH));
				eg_voucher=cm.getEg_Voucher(vType,fiscalPid,conn);
				//c Genarate new VoucherNumber For Reversal if Voucher generation Type is 'Auto'
				//Use the logic used to create Receipt
				VoucherTypeForULB voucherType=new VoucherTypeForULB();
				String vTypeForULB=(String)voucherType.readVoucherTypes("Journal");
		        if(vTypeForULB.equalsIgnoreCase("Auto"))
		        	{
		        	CommonMethodsImpl cmImpl = new CommonMethodsImpl();
		        	newVcNo=cmImpl.getTxnNumber(fId, FinancialConstants.PURCHBILL_VOUCHERNO_TYPE, vt, conn);
		        	
		        	}	
			}
			catch(Exception e)
			{
				dc.addMessage("exilRPError","Supplier Journal error : " + e.toString());
	    		throw taskExc;
			}
			for(int i=eg_voucher.length();i<10;i++)
			{
				 eg_voucher="0"+eg_voucher;
			}
			cgNum=vType+eg_voucher;
			reverse();
			dc.addMessage("eGovSuccess","for Reversing Supplier Journal  : CGNumber is "+cgNum+" and VoucherNumber is "+newVcNo);
			return;
		 }

			else if(dc.getValue("modeOfExec").equalsIgnoreCase("edit")){
		  	 	editVoucher(dc,conn);
				return;
			 }

		try{

			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			Date dt=new Date();
			String vdt=(String)dc.getValue("voucherHeader_voucherDate");
			dt = sdf.parse( vdt );
			String voucherHeader_voucherDate = formatter.format(dt);
			int CurdateCheck=1;
			CurdateCheck=cm.isCurDate(dc,conn,vdt);
			if(CurdateCheck==0)
				return;
			//fiscalPid=getFiscalPeriod(dc.getValue("voucherHeader_voucherDate"));
			fiscalPid=cm.getFiscalPeriod(voucherHeader_voucherDate);
			voucherNumber=cm.vNumber(dc, conn,fId,dc.getValue("voucherHeader_voucherNumber"));
			vType=voucherNumber.substring(0,2);
			eg_voucher=cm.getEg_Voucher(vType,fiscalPid,conn);
			for(int i=eg_voucher.length();i<5;i++)
			{
				 eg_voucher="0"+eg_voucher;
			}
			cgNum=vType+eg_voucher;


			VoucherTypeForULB voucherType=new VoucherTypeForULB();//check for vouchernumber generation Type if Not Auto throw Exception 
		//Else genarate new one for reversed Bill by appending '/noOftimesVoucherCreated'
		String vTypeForULB=(String)voucherType.readVoucherTypes("Journal");
		if(vTypeForULB.equalsIgnoreCase("Auto"))
		{
		LOGGER.info("Inside Auto Mode");
		String vNumRptsql="select  count(*) from voucherheader where vouchernumber like '"+voucherNumber+"%'";
		Statement vNumRptst=connection.createStatement();
		ResultSet vNumRptrs=vNumRptst.executeQuery(vNumRptsql);
		int noOftimesVoucherCreated=0;
		if(vNumRptrs.next())
			noOftimesVoucherCreated=vNumRptrs.getInt(1);
		if(noOftimesVoucherCreated!=0)
		voucherNumber=voucherNumber+"/"+(noOftimesVoucherCreated);
		LOGGER.debug("VoucherNumber"+voucherNumber);
		vNumRptst.close();
		}
			if(!cm.isUniqueVN(dc, conn,voucherNumber))
				throw new TaskFailedException();

			if(!isValidAmount(dc)){
				dc.addMessage("exilRPError","Server Validation Failed..Check The Data Sent");
   				throw  taskExc;
			}

			//getRequiredData(dc);
			postInVoucherHeader(dc,conn);
			LOGGER.info("the scheme and subscheme are"+dc.getValue("scheme")+"   :"+dc.getValue("subscheme"));
			cm.updateVoucherMIS("I",conn,voucherHeaderId,(String)dc.getValue("field_id"),dc.getValue("fundSource_id"),(String)dc.getValue("scheme"),(String)dc.getValue("subscheme"),dc.getValue("voucherHeader_voucherDate"));
			com.exilant.eGov.src.domain.BillRegisterBean b=new com.exilant.eGov.src.domain.BillRegisterBean();
			if(!dc.getValue("billid").equals(""))
			{
				b.updateStatus("PASSED",conn,dc.getValue("billid"));}
			postInVoucherDetail(dc,transactions,conn);
			postInAssetStatus(dc);
			postInSupplierBillDetail(dc);
			postInWorksDetail(dc);

     		/*if( validation by chartofaccountengine returns true)
    		post the data in voucher header*/
			ChartOfAccounts engine=ChartOfAccounts.getInstance();
    		Transaxtion txnList[]=new Transaxtion[transactions.size()];
    		txnList=(Transaxtion[])transactions.toArray(txnList);
    		if(!engine.postTransaxtions(txnList, connection, dc)){
    			dc.addMessage("exilRPError","Engine Validation Failed");
    			throw taskExc;
    		}
    		LOGGER.debug("before engine");
    		cm.UpdateVoucherStatus(dc,"Journal Voucher",conn,voucherHeaderId);
    		dc.addValue("voucherHeader_voucherNumber",voucherNumber);
    		dc.addMessage("eGovSuccess","Supplier Journal : CGNumber is "+cgNum+" and VoucherNumber is "+voucherNumber);
    		
    	}catch(Exception sqlex){
    		LOGGER.error("Exp="+sqlex.getMessage());
    		dc.addMessage("exilRPError","Transaction Failed :"+sqlex.getMessage());
    		throw taskExc;
		}
	}

	private void getResultSet(String tableName, String condition, DataCollection dc) throws TaskFailedException{
		try{
			resultset = statement.executeQuery("select * from " + tableName + " where 1=1 " + condition);
			resultset.next();
		}
		catch(SQLException sqlex){
			dc.addMessage("exilNoData","select * from " + tableName + " where 1=1 " + condition);
			throw  taskExc;
		}
	}

	private void postInVoucherHeader(DataCollection dc,Connection conn) throws SQLException,TaskFailedException{
		statement = connection.createStatement();
		String voucherHeader_voucherDate="";
		try{
		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String vdt=(String)dc.getValue("voucherHeader_voucherDate");
		voucherHeader_voucherDate = formatter.format(sdf.parse(vdt));
		}
		catch(Exception e){throw new TaskFailedException(e.getMessage());}

		VoucherHeader vh = new VoucherHeader();
		vh.setCgn(dc.getValue("voucherHeader_cgn"));
		//vh.setVoucherDate((String)dc.getValue("voucherHeader_voucherDate"));
		vh.setVoucherDate(voucherHeader_voucherDate);
		vh.setName("Supplier Journal");
		vh.setType("Journal Voucher");
		vh.setVoucherNumber(voucherNumber);
		vh.setFundId(dc.getValue("fund_id"));
		vh.setDepartmentId(dc.getValue("department_id"));
		vh.setFundSourceId(dc.getValue("fundSource_id"));
		vh.setDescription(dc.getValue("voucherHeader_narration"));
		vh.setIsConfirmed("1");
		//vh.setFiscalPeriodId(getFiscalPeriod(dc.getValue("voucherHeader_voucherDate")));
		vh.setFiscalPeriodId(cm.getFiscalPeriod(voucherHeader_voucherDate));
	//	vh.setFunctionId((String)dc.getValue("function_id"));
		vh.setCreatedby(dc.getValue("current_UserID"));
		vh.setCgvn(cgNum);
		statement.close();
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
			throw new TaskFailedException();
		}

		//		if Fund is same then we can use the same voucher number and cgnumber
		if((oldFundid.equals(dc.getValue("fund_id").toString())) && (cm.getFiscalPeriod(voucherHeader_voucherDate).equalsIgnoreCase(cm.getFiscalPeriod(oldVoucherDate))))
		{
			LOGGER.info("Funds are same.. and same fiscalperiod id");
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
					LOGGER.info("Funds are different");
					String voucherNum=dc.getValue("voucherHeader_voucherNumber").toString();
					voucherNum=voucherNum.substring(2);
					voucherNumber=cm.vNumber(dc, con,fId,voucherNum);
					if(!cm.isUniqueVN(dc, con,voucherNumber))	throw new TaskFailedException();
				}
				vType=voucherNumber.substring(0,2);
				eg_voucher=cm.getEg_Voucher(vType,fiscalPid,con);
				for(int i=eg_voucher.length();i<10;i++)
				 eg_voucher="0"+eg_voucher;
				cgNum=vType+eg_voucher;

				VH.setCgvn(cgNum);
				VH.setVoucherNumber(voucherNumber);
			}
			catch(Exception e)
			{
				LOGGER.error("Exp in getting new VNo. "+e);
				dc.addMessage("exilRPError","Transaction Failed :"+e.getMessage());
				throw new TaskFailedException();
			}

		}
		VH.setCgn(dc.getValue("cgnNum").toString());
		VH.setDepartmentId(dc.getValue("department_id"));
		VH.setDescription((String)dc.getValue("voucherHeader_narration"));
		VH.setFundId(dc.getValue("fund_id"));
		VH.setFundSourceId(dc.getValue("fundSource_id"));
		VH.setId((String)dc.getValue("voucherHeader_id"));
		VH.setVoucherDate(voucherHeader_voucherDate);
		//VH.setFunctionId((String)dc.getValue("department_name"));
		VH.setFiscalPeriodId(cm.getFiscalPeriod(voucherHeader_voucherDate));
		VH.setLastModifiedBy(dc.getValue("current_UserID"));
		VH.setName("Supplier Journal");
		VH.setType("Journal Voucher");
	//	VH.setIsConfirmed("0");
		VH.update(con);
		VoucherHeaderId = VH.getId();
		//Insert Record in the egf_record_status table
		cm.UpdateVoucherStatus(dc,"Journal Voucher",con,VoucherHeaderId);
	}

	private void postInSupplierBillDetail(DataCollection dc) throws TaskFailedException, SQLException{
				statement = connection.createStatement();
				SupplierBillDetail cbd = new SupplierBillDetail();
				String supBillDetail_billDate="";
				statement = connection.createStatement();
				try{
							SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
							SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
							String vdt=(String)dc.getValue("supplierBillDetail_billDate");
							supBillDetail_billDate = formatter.format(sdf.parse(vdt));
							LOGGER.debug("billdate1:*******"+supBillDetail_billDate);
					}
				catch(Exception e){LOGGER.error("date conversion error");}

				getResultSet("relation", "and code='" + dc.getValue("supplier_code") + "'", dc);
				supplierId = resultset.getInt("ID");
				cbd.setSupplierId(supplierId + "");
				resultset.close();
				statement.close();
				cbd.setWorksDetailId(dc.getValue("worksDetail_id"));
				cbd.setBillAmount(ExilPrecision.convertToString(dc.getValue("supplierBillDetail_billAmount"),2));
				cbd.setbilldate(supBillDetail_billDate);
				cbd.setBillNumber(dc.getValue("billNumber"));
				double passedAmt=ExilPrecision.convertToDouble(dc.getValue("supplierBillDetail_passedAmount"),2);
			//	double adjAmt= ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2);
				cbd.setPassedAmount(ExilPrecision.convertToString((passedAmt),2));
				cbd.setApprovedBy(dc.getValue("supplierBillDetail_approvedBy"));
				cbd.setNarration(dc.getValue("voucherHeader_narration"));
				cbd.setVoucherHeaderId(voucherHeaderId+"");
				cbd.setTdsAmount(ExilPrecision.convertToString((dc.getDouble("deductTotal")-OtherRecoveries),2));
				cbd.setTdsPaidToIt("0");
				cbd.setPaidAmount("0");
				cbd.setAdvAdjAmount(ExilPrecision.convertToString(dc.getValue("worksDetail_adjustmentAmount"),2));
				cbd.setOtherRecoveries(""+OtherRecoveries);
				if(!dc.getValue("asset_name").equalsIgnoreCase(""))
					cbd.setAssetId(dc.getValue("asset_name"));
				else
					cbd.setAssetId("null");
				cbd.setBillId(dc.getValue("billid"));
				cbd.insert(connection);
				supplierBillId = cbd.getId();
		}

		private void UpdateInSupplierBillDetail(DataCollection dc) throws TaskFailedException, SQLException{
				statement = connection.createStatement();
				SupplierBillDetail cbd = new SupplierBillDetail();
				String supBillDetail_billDate="";
				statement = connection.createStatement();
				try{
					SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
					String vdt=(String)dc.getValue("supplierBillDetail_billDate");
					supBillDetail_billDate = formatter.format(sdf.parse(vdt));
					LOGGER.debug("billdate1:*******"+supBillDetail_billDate);
			}
					catch(Exception e){throw new TaskFailedException(e.getMessage());}
				getResultSet("relation", "and code='" + dc.getValue("supplier_code") + "'", dc);
				supplierId = resultset.getInt("ID");
				cbd.setSupplierId(supplierId + "");

				cbd.setWorksDetailId(dc.getValue("worksDetail_id"));
				cbd.setBillNumber(dc.getValue("billNumber"));
				cbd.setbilldate(supBillDetail_billDate);
				cbd.setBillAmount(ExilPrecision.convertToString(dc.getValue("supplierBillDetail_billAmount"),2));
				double passedAmt=ExilPrecision.convertToDouble(dc.getValue("supplierBillDetail_passedAmount"),2);
				cbd.setPassedAmount(ExilPrecision.convertToString((passedAmt),2));
				cbd.setApprovedBy(dc.getValue("supplierBillDetail_approvedBy"));
				cbd.setNarration(dc.getValue("voucherHeader_narration"));
				cbd.setVoucherHeaderId(String.valueOf(VoucherHeaderId));
				cbd.setTdsAmount(ExilPrecision.convertToString((dc.getDouble("deductTotal")-OtherRecoveries),2));
				cbd.setTdsPaidToIt("0");
				cbd.setPaidAmount("0");
				cbd.setAdvAdjAmount(ExilPrecision.convertToString(dc.getValue("worksDetail_adjustmentAmount"),2));
				cbd.setOtherRecoveries(""+OtherRecoveries);
				cbd.setBillId(dc.getValue("billid"));
				if(!dc.getValue("asset_name").equalsIgnoreCase(""))
				cbd.setAssetId(dc.getValue("asset_name"));

				LOGGER.debug("SELECT id from supplierbilldetail where voucherheaderid = " + VoucherHeaderId);
				ResultSet rs = statement.executeQuery("SELECT id from supplierbilldetail where voucherheaderid = " + VoucherHeaderId);
				if(rs.next()){
					cbd.setId(rs.getInt("id") + "");
					cbd.update(connection);
				}
				else
					cbd.insert(connection);
				supplierBillId = cbd.getId();
				resultset.close();
				statement.close();
	}

	private void postInVoucherDetail(DataCollection dc, ArrayList transactions,Connection conn) throws TaskFailedException, SQLException{
		double debitAmount=0, creditAmount=0;
		String glCode, accName;
		String supplierGrid[][] = (String[][])dc.getGrid("gridSupplierJournal");
		LOGGER.debug("length of the grid+++++++"+supplierGrid.length);
		VoucherDetail vd = new VoucherDetail();
//		boolean isSupplierPaybleAccount = false;

		for(int i=0; i < supplierGrid.length; i++){
			if(supplierGrid[i][0].equalsIgnoreCase("")) continue;
			//PreDefinedAccCodes codes = new PreDefinedAccCodes();
			/* checking for duplicate account code*/
			for(int j=i+1; j < supplierGrid.length; j++){
				if(supplierGrid[i][0].equals(supplierGrid[j][0])){
					dc.addMessage("exilDuplicateGLCode", supplierGrid[i][0].toString());
					throw  taskExc;
				}
			}
			if(supplierGrid[i][0].equals(EGovConfig.getProperty("egf_config.xml","SupPayCode","","SupplierCodes")) || supplierGrid[i][0].equals(EGovConfig.getProperty("egf_config.xml","SupAdvCode","","SupplierCodes"))){
				dc.addMessage("exilError","Internal Codes Used  : transaction cancelled.");
				throw  taskExc;
			}

			debitAmount += ExilPrecision.convertToDouble(supplierGrid[i][2], 2);
		}
		if( !(debitAmount> 0.0) ){
			dc.addMessage("exilError","no transaction involved.");
			throw  taskExc;
		}

		int lineID=1;

		for(int i=0; i < supplierGrid.length; i++){
			if(supplierGrid[i][0].equalsIgnoreCase("")) continue;
			//0. accountCode     1. accountHead     2. debitAmount   3. creditAmount    4. narration
			vd.setLineID(String.valueOf(lineID));
			lineID++;
			vd.setVoucherHeaderID(voucherHeaderId + "");
			glCode = supplierGrid[i][0];
			accName = cm.getCodeName(glCode,conn);
			vd.setGLCode(glCode);
			vd.setAccountName(accName);
			LOGGER.debug("accountName is"+accName);
			vd.setDebitAmount(ExilPrecision.convertToString(supplierGrid[i][2],2));
			vd.setCreditAmount("0");
			vd.setNarration(supplierGrid[i][4]);
			vd.insert(connection);

			Transaxtion transaction = new Transaxtion();
			transaction.setGlCode(glCode);
			transaction.setGlName(accName); 
			transaction.setDrAmount(ExilPrecision.convertToString(supplierGrid[i][2],2));
			transaction.setCrAmount("0");
			transaction.setVoucherLineId(String.valueOf(vd.getId()));
			transaction.setVoucherHeaderId(String.valueOf(voucherHeaderId));
			transaction.setNarration(supplierGrid[i][3]);
			//transaction.setTransaxtionParam(getRequiredData(dc,i+1,vd)); added below
			 fuctid=supplierGrid[i][4];
			if(fuctid.length()!=0 || fuctid!=null) transaction.setFunctionId(supplierGrid[i][4]);
			transactions.add(transaction);
		}
		transactions=addRequiredDataToList(transactions,dc,0,"entities_grid");
		lineID=transactions.size()+1;
		int start=transactions.size();
		/*to add the default supplier payable and tds codes;add any extra recoveries in that function */
		//OtherRecoveries=creditAmount;
		addSupplierAndTDSAcc(dc,transactions,creditAmount,conn);
		postDefaultsInVoucherDtl(dc,transactions,lineID,start,conn);
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
			String delQuery="DELETE FROM voucherdetail WHERE voucherheaderid="+VoucherHeaderId+" AND ID NOT IN (SELECT VOUCHERLINEID FROM GENERALLEDGER WHERE voucherheaderid="+VoucherHeaderId+")";
			statement.executeUpdate(delQuery);
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
	private void updateVoucherDetail(DataCollection dc, ArrayList transactions,Connection con) throws SQLException, TaskFailedException {
		double debitAmount=0, creditAmount=0;
		VoucherDetail VD =new VoucherDetail();
		String[][] voucherDetail = (String[][])dc.getGrid("gridSupplierJournal");
		ArrayList vDetailId=new ArrayList();
		ArrayList vDetailLineId=new ArrayList();
		ArrayList vAccountName=new ArrayList();
		ArrayList vDetailGlcode=new ArrayList();
		boolean isSupplierPaybleAccount = false;

		for(int i=0; i < voucherDetail.length; i++){

			if(voucherDetail[i][0].equalsIgnoreCase("")) continue;
		//	PreDefinedAccCodes codes = new PreDefinedAccCodes();
			/* checking for duplicate account code*/
			for(int j=i+1; j < voucherDetail.length; j++){
				if(voucherDetail[i][0].equals(voucherDetail[j][0])){
					dc.addMessage("exilDuplicateGLCode", voucherDetail[i][0].toString());
					throw  taskExc;
				}
			}
			if(voucherDetail[i][0].equals(EGovConfig.getProperty("egf_config.xml","SupPayCode","","SupplierCodes")) || voucherDetail[i][0].equals(EGovConfig.getProperty("egf_config.xml","SupAdvCode","","SupplierCodes"))){
				dc.addMessage("exilError","Internal Codes Used  : transaction cancelled.");
				throw  taskExc;
			}

			debitAmount += ExilPrecision.convertToDouble(voucherDetail[i][2], 2);
			//creditAmount += ExilPrecision.convertToDouble(voucherDetail[i][3], 2);
		}
		if( !(debitAmount+creditAmount > 0.0) ){
			dc.addMessage("exilError","no transaction involved.");
			throw  taskExc;
		}
		//Get the count of line items present.update the exsting and insert the new.
		statement = connection.createStatement();
	LOGGER.debug("select id ,LineId ,ACCOUNTNAME,glCode from VoucherDetail where voucherheaderid="+VoucherHeaderId+" order by LineId");
		resultset = statement.executeQuery("select id ,LineId,ACCOUNTNAME,glCode from VoucherDetail where voucherheaderid="+VoucherHeaderId+" order by LineId");
		int c=0;
		while(resultset.next()){
			vDetailLineId.add(c,(resultset.getString("LineId")));
			vDetailId.add(c,(resultset.getString("id")));
			vAccountName.add(c,(resultset.getString("ACCOUNTNAME")));
			vDetailGlcode.add(c,(resultset.getString("glCode")));
			c++;
		}


		voucherDetail = (String[][])dc.getGrid("gridSupplierJournal");
		//LOGGER.debug("Exisitng count is: "+vDetailId.size()+" Grid Length :"+voucherDetail.length +" accountname length :"+vAccountName.size());

		int i=0;

		for(i=0;i<voucherDetail.length;i++){

				if(voucherDetail[i][0].equalsIgnoreCase("")) continue;
				VD.setVoucherHeaderID(String.valueOf(VoucherHeaderId));
				VD.setGLCode(voucherDetail[i][0]);
				VD.setAccountName(cm.getCodeName(voucherDetail[i][0], con));
				if(voucherDetail[i][2].length()>0)
					VD.setDebitAmount(ExilPrecision.convertToString(voucherDetail[i][2],2));
				else
					VD.setDebitAmount("0");
				VD.setCreditAmount("0");
				VD.setNarration(voucherDetail[i][3]);
				VD.setLineID((i+1) + "");
				if(voucherDetail[i][0].length()>0)
					VD.insert(connection);
				Transaxtion transaction = new Transaxtion();
				transaction.setGlCode(voucherDetail[i][0]);
				transaction.setGlName(voucherDetail[i][1]);

				if(voucherDetail[i][2].length()>0)
					transaction.setDrAmount(ExilPrecision.convertToString(voucherDetail[i][2],2));
				else
					transaction.setDrAmount("0");
				transaction.setCrAmount("0");
				transaction.setVoucherLineId(String.valueOf(VD.getId()));
				transaction.setVoucherHeaderId(String.valueOf(VoucherHeaderId));
				transaction.setNarration(voucherDetail[i][3]);
			//	transaction.setTransaxtionParam(getRequiredData(dc,i+1,VD));
				 fuctid=voucherDetail[i][4];
					if(fuctid.length()!=0 || fuctid!=null) transaction.setFunctionId(voucherDetail[i][4]);
				transactions.add(transaction);
			}
		transactions=addRequiredDataToList(transactions,dc,0,"entities_grid");
		int lineID=transactions.size()+1;
		//int lineID=vDetailLineId.get(i+1);
		int start=transactions.size();
		/*to add the default supplier payable and tds codes;add any extra recoveries in that function */
		//OtherRecoveries=creditAmount;
		addSupplierAndTDSAcc(dc,transactions,creditAmount,con);
		//postDefaultsInVoucherDtl(dc,transactions,lineID,start);
		String glCode="";

		//VoucherDetail vd = new VoucherDetail();
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
			vd.setAccountName(cm.getCodeName(glCode, con));
			vd.setDebitAmount(trn.getDrAmount());
			vd.setCreditAmount(trn.getCrAmount());
			vd.setNarration(dc.getValue("voucherHeader_narration"));


			statement = connection.createStatement();
			LOGGER.debug("select id from VoucherDetail where voucherheaderid="+VoucherHeaderId+" glcode="+glCode);

			resultset = statement.executeQuery("select id from VoucherDetail where voucherheaderid="+VoucherHeaderId+" and glcode="+glCode);
			if(resultset.next()){
				//LOGGER.debug("id is present ie inside the update function");
				vd.setId(resultset.getInt("id") + "");
				vd.update(connection);
			}
			else
				vd.insert(connection);
			resultset.close();
			statement.close();
			//vd.setId(vDetailId.get(id).toString());
			//vd.update(connection);
			trn.setVoucherLineId(String.valueOf(vd.getId()));
			trn.setVoucherHeaderId(String.valueOf(VoucherHeaderId));
			trn.setNarration(dc.getValue("voucherHeader_narration"));
		}


	}
	private void postDefaultsInVoucherDtl(DataCollection dc, ArrayList transactions,int lineID,int start,Connection con)
																					throws SQLException, TaskFailedException	{
		 		String glCode="";

		VoucherDetail vd = new VoucherDetail();
		for(int i=start; i < transactions.size(); i++){
		LOGGER.debug("inside the postDefaultsInVoucherDtl");
			Transaxtion trn=(Transaxtion)transactions.get(i);
			if(trn.getGlCode().length()==0) continue;
			//0. accountCode     1. accountHead     2. debitAmount   3. creditAmount    4. narration
			vd.setLineID(String.valueOf(lineID));
			lineID++;
			LOGGER.debug("trn.getGlCode()"+trn.getGlCode());
			glCode = trn.getGlCode();
			vd.setVoucherHeaderID(voucherHeaderId + "");
			vd.setGLCode(glCode);
			LOGGER.debug("glcode in defaults"+glCode);
			vd.setAccountName(cm.getCodeName(glCode,con));
			vd.setDebitAmount(trn.getDrAmount());
			vd.setCreditAmount(trn.getCrAmount());
			vd.setNarration(dc.getValue("voucherHeader_narration"));
			try{
			vd.insert(connection);
			trn.setVoucherLineId(String.valueOf(vd.getId()));
			}catch(Exception e){throw  taskExc;}
			trn.setVoucherHeaderId(String.valueOf(voucherHeaderId));
			trn.setNarration(dc.getValue("voucherHeader_narration"));
			//trn.setFunctionId(fuctid);
			//LOGGER.debug("111111111111111fuctid-in defaultsvd------"+fuctid);
		}
	}

	public boolean validate(DataCollection dc){
   		DataValidator valid = new DataValidator();
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
		for(int i=1;i<entityGrid.length;i++){
			//search entities only for the rowindex sent
			//entityGrid[i][0]-dettypeid entityGrid[i][1]-detkey  entityGrid[i][2]-detvalue entityGrid[i][3]-rowIndex
			if(entityGrid[i][3].equals(String.valueOf(rowIdx))){
				AccountDetailType accType=(AccountDetailType)detailTypes.get(entityGrid[i][0]);
				if(accType!=null){
					TransaxtionParameter reqData=new TransaxtionParameter();
					try {
						reqData.setGlcodeId(cm.getCodeNameById(vd.getGLCode(), connection)+"");
					} catch (Exception e) {
						throw new TaskFailedException();
					}
					reqData.setDetailName(accType.getAttributeName());
					reqData.setDetailKey(entityGrid[i][1]);
					reqData.setDetailAmt(vd.getDebitAmount());
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

	private int postInWorksDetail(DataCollection dc)throws SQLException,TaskFailedException{
		int worksID=0;
		WorksDetail wDet=new WorksDetail();
		wDet.setId(dc.getValue("worksDetail_id"));
		double passAmount=ExilPrecision.convertToDouble(dc.getValue("supplierBillDetail_passedAmount"),2);
		double adjAmt=ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2);
		//double advAmt=ExilPrecision.convertToDouble(dc.getValue("worksDetail_advanceAmount"),2);
		//wDet.setAdvanceAmount(ExilPrecision.convertToString(adjAmt,2));
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
	private int updateWorksDetail(DataCollection dc,Connection con)throws SQLException,TaskFailedException{
		int worksID=0;
		WorksDetail wDet=new WorksDetail();
		Statement st=con.createStatement();
		double oldPassAmount=0;
		double oldadjAmt=0;
		String oldWorksId="";

		String newWorksID=dc.getValue("worksDetail_id");
		double passAmount=ExilPrecision.convertToDouble(dc.getValue("supplierBillDetail_passedAmount"),2);
		//double TrxnPassAmount=ExilPrecision.convertToDouble(dc.getValue("supplierBillDetail_passedAmount"),2);
		double adjAmt=ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2);
	//	double advAmt=ExilPrecision.convertToDouble(dc.getValue("worksDetail_advanceAmount"),2);
		//wDet.setAdvanceAmount(ExilPrecision.convertToString(adjAmt,2));
		//Getting Old PassedAmount and Old AdjustedAmount
		ResultSet rs=st.executeQuery("select advadjamt,passedamount,worksDetailId from supplierbilldetail where voucherheaderid="+dc.getValue("voucherHeader_id"));
		LOGGER.debug("select advadjamt,passedamount,worksDetailId from supplierbilldetail where voucherheaderid="+dc.getValue("voucherHeader_id"));
		if(rs.next())
		{
			oldadjAmt=rs.getDouble(1);
			oldPassAmount=rs.getDouble(2);
			oldWorksId=rs.getString(3);
			//Only if the work orders are same
			if(newWorksID.equalsIgnoreCase(oldWorksId))
			{
				adjAmt=adjAmt-oldadjAmt;
				passAmount=passAmount-oldPassAmount;
			}
		}
		LOGGER.debug("Latest adjAmt:"+adjAmt+" passAmount="+passAmount);
		//If the work orders are different, we need to deduct the passed amount and adjustment amount
		if(!newWorksID.equalsIgnoreCase(oldWorksId))
		{
			wDet.setId(oldWorksId);
			wDet.setAdvanceAdjustment("-"+ExilPrecision.convertToString(oldadjAmt,2));
			wDet.setPassedAmount("-"+ExilPrecision.convertToString(oldPassAmount,2));
			wDet.update(connection);
		}
		wDet.setId(dc.getValue("worksDetail_id"));
		wDet.setAdvanceAdjustment(ExilPrecision.convertToString(adjAmt,2));
		wDet.setPassedAmount(ExilPrecision.convertToString(passAmount,2));
		wDet.update(connection);
		return worksID;
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
			return false;
		}
			if((billAmount+totalPassedAmount)>totalValue){
			dc.addMessage("exilRPError","Bill amount cannot exceeded total amount");
			return false;
		}
	/*	if((passedAmount+adjustmentAmount)>billAmount){
			dc.addMessage("exilRPError","Passed amount and adjustment amount exceeded bill amount:");
			return false;
		}*/
		if(adjustmentAmount>(advanceAmount-advanceAdjAmount)){
			dc.addMessage("exilRPError","Adjustment amount cannot exceed advance amount:");
			return false;
		}

		if(((totalValue-totalPassedAmount)-(passedAmount-adjustmentAmount))<((advanceAmount-advanceAdjAmount)-adjustmentAmount)){
			dc.addMessage("exilRPError","Adjust advance amount and passed amount ");
			return false;
	    }
		/*if(	passedAmount<=0 &&
				((totalValue-totalPassedAmount)!=(advanceAmount-advanceAdjAmount))){
			    dc.addMessage("exilRPError","Passed Amount cannot be zero ");
			    return false;
		}*/
		return true;
	}
	private void addSupplierAndTDSAcc(DataCollection dc,ArrayList transactions,double creditAmount,Connection con)throws TaskFailedException{
		String dedfuct="";
		double passAmount=ExilPrecision.convertToDouble(dc.getValue("supplierBillDetail_passedAmount"),2);
		double adjustAmt=ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2);
		double tdsAmount=0;
		//String fuctid="";
		String supplierDedGrid[][] = (String[][])dc.getGrid("gridSupplierDeduction");
		for(int i=0; i < supplierDedGrid.length; i++)
		{
			 if(supplierDedGrid[i][2].equalsIgnoreCase("") || supplierDedGrid[i][0].equalsIgnoreCase("") ) continue;
				if(supplierDedGrid[i][0].equals(EGovConfig.getProperty("egf_config.xml","SupPayCode","","SupplierCodes")) || supplierDedGrid[i][0].equals(EGovConfig.getProperty("egf_config.xml","SupAdvCode","","SupplierCodes")) || supplierDedGrid[i][0].equals(EGovConfig.getProperty("egf_config.xml","ConPayCode","","ContractorCodes")) || supplierDedGrid[i][0].equals(EGovConfig.getProperty("egf_config.xml","ConAdvCode","","ContractorCodes")))
				{
					dc.addMessage("exilError","Internal Codes Used  : transaction cancelled.");
					throw  taskExc;
				}
		} 
		for(int i=0; i < supplierDedGrid.length; i++)
		{
		 if(supplierDedGrid[i][2].equalsIgnoreCase("") || supplierDedGrid[i][0].equalsIgnoreCase("") ) continue;
		 /* checking for duplicate account code*/
		/*	for(int j=i+1; j < supplierDedGrid.length; j++)
			{
				if(supplierDedGrid[i][0].equals(supplierDedGrid[j][0]))
				{
				dc.addMessage("exilDuplicateGLCode", supplierDedGrid[i][0].toString());
				throw  taskExc;
				}
			}*/
			Transaxtion trn=new Transaxtion();
			trn.setGlCode(supplierDedGrid[i][0]);
			trn.setGlName(cm.getCodeName(supplierDedGrid[i][0],con));
			trn.setDrAmount("0");
			trn.setCrAmount(ExilPrecision.convertToString(supplierDedGrid[i][2],2));
			tdsAmount += ExilPrecision.convertToDouble(supplierDedGrid[i][2], 2);
			if(supplierDedGrid[i][4].equalsIgnoreCase(""))
				OtherRecoveries=OtherRecoveries+ExilPrecision.convertToDouble(supplierDedGrid[i][2], 2);
			trn.setNarration(dc.getValue("voucherHeader_narration"));
			trn.setTransaxtionParam(getRequiredData(dc,trn,supplierDedGrid[i][4]));//Set tdsid for Eg_remittance_gldtl table
			dedfuct=supplierDedGrid[i][3];
			if(dedfuct.length()!=0 || dedfuct!=null) trn.setFunctionId(dedfuct);
			transactions.add(trn);
		}
		
		String supPayable=EGovConfig.getProperty("egf_config.xml","SupPayCode","","SupplierCodes");
		String supAdvance=EGovConfig.getProperty("egf_config.xml","SupAdvCode","","SupplierCodes"); 
		
		
	/*	Transaxtion trn=new Transaxtion();
		if(ExilPrecision.convertToDouble((passAmount-creditAmount)-tdsAmount,2)>0){
			trn.setGlCode(supPayable);
			LOGGER.debug("The glcode is "+trn.getGlCode());
			trn.setGlName(cm.getCodeName(supPayable,con));
			trn.setDrAmount("0");
			trn.setCrAmount(ExilPrecision.convertToString((passAmount-creditAmount)-tdsAmount,2));
			trn.setNarration(dc.getValue("voucherHeader_narration"));
			trn.setTransaxtionParam(getRequiredData(dc,trn,null));
			transactions.add(trn);
		}
		//advance  */
		//if Contractors Payable and Contractor advance Payable are not mapped to same code make two different entries
		//else make single entry
		
		if(!supPayable.equalsIgnoreCase(supAdvance))
		{
		Transaxtion trn=new Transaxtion();
		if(ExilPrecision.convertToDouble((passAmount-creditAmount)-tdsAmount-adjustAmt,2)>0){
			trn.setGlCode(supPayable);
			trn.setGlName(cm.getCodeName(supPayable,con));
			trn.setDrAmount("0");
			trn.setCrAmount(ExilPrecision.convertToString((passAmount-creditAmount)-tdsAmount-adjustAmt,2));
			trn.setNarration(dc.getValue("voucherHeader_narration"));
			trn.setTransaxtionParam(getRequiredData(dc,trn,null));
			transactions.add(trn);
		}
		//advance
		Transaxtion trnadv=new Transaxtion();
		if(adjustAmt>0)
		{
			trnadv.setGlCode(supAdvance);
			trnadv.setGlName(cm.getCodeName(supAdvance,con));
			trnadv.setDrAmount("0");
			trnadv.setCrAmount(ExilPrecision.convertToString(adjustAmt,2));
			trnadv.setNarration(dc.getValue("voucherHeader_narration"));
			trnadv.setTransaxtionParam(getRequiredData(dc,trnadv,null));
			transactions.add(trnadv);
		}
		}
		else
		{
			Transaxtion trn=new Transaxtion();
			if(ExilPrecision.convertToDouble((passAmount-creditAmount)-tdsAmount,2)>0){
				trn.setGlCode(supPayable);
				trn.setGlName(cm.getCodeName(supPayable,con));
				trn.setDrAmount("0");
				trn.setCrAmount(ExilPrecision.convertToString((passAmount-creditAmount)-tdsAmount,2));
				trn.setNarration(dc.getValue("voucherHeader_narration"));
				trn.setTransaxtionParam(getRequiredData(dc,trn,null));
				transactions.add(trn);
			}
		}
		String adjAmt=dc.getValue("worksDetail_adjustmentAmount");
		try{
			if(Double.isNaN(Double.parseDouble(adjAmt)))return;
		}catch(Exception e){
			throw new TaskFailedException(e.getMessage());
		}
	}
	public ArrayList getRequiredData(DataCollection dc,Transaxtion trn,String tdsId) throws TaskFailedException{
		requiredList=new ArrayList();
		ArrayList formList=new ArrayList();
		/*formList.add("fund_id");
		formList.add("relation_id"\);
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
		String formControlName;
		for(int i=0;i<formList.size();i++){
			formControlName=(String)formList.get(i);
			TransaxtionParameter transParam = new TransaxtionParameter();
			if(trn!=null)
			{
				try {
					LOGGER.debug("###### getGLCode() is"+trn.getGlCode());
					transParam.setGlcodeId(cmImpl.getGlCodeId(trn.getGlCode(),connection));
					} catch (Exception e) {
					LOGGER.debug("Exp="+e.getMessage());
					throw new TaskFailedException();
				}
				//transParam.setDetailAmt(vd.getDebitAmount()!="0"?vd.getDebitAmount():vd.getCreditAmount());
			transParam.setDetailAmt(!trn.getDrAmount().equals("0")?trn.getDrAmount():trn.getCrAmount());
			
			}else
			{
		
			transParam.setGlcodeId(EGovConfig.getProperty("egf_config.xml","SupPayCode","SupplierCodes"));
		
			transParam.setDetailAmt(calculateNetAmt(dc));
			}
		//	transParam.setDetailKey((String)dc.getValue(formControlName));
			//transParam.setDetailName((String)dc.getValue(formControlName));
			String code=(String)dc.getValue("supplier_code");
			String acKey=(String)dc.getValue("supplier_id");
			transParam.setDetailKey(acKey);
			transParam.setTdsId(tdsId);
			LOGGER.debug(" AccountDetailKey is "+transParam.getDetailKey());
			LOGGER.debug(" dc.getValue(formControlName)  "+dc.getValue(formControlName));
			LOGGER.debug("TDS Id -->"+tdsId);
			
			transParam.setDetailName(formControlName);
			requiredList.add(transParam);
		}
		return requiredList;
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
	int status=0;
	//ArrayList transactions=new ArrayList();
	 Statement st=null;
     ResultSet rset=null;
     voucherNumber=dc.getValue("voucherHeader_voucherNumber");
     String vhID=dc.getValue("voucherHeader_id");
     String voucherNum="";
     String subVN="";
     LOGGER.info("voucherNumber:"+voucherNumber);
     try
     {
         st=con.createStatement();
         LOGGER.info("select vouchernumber from voucherheader where id="+vhID);
         rset=st.executeQuery("select vouchernumber from voucherheader where id="+vhID);
         if(rset.next())
         {
             voucherNum=rset.getString(1);
             subVN=voucherNum.substring(0,2);
         }
         rset.close();
         voucherNumber=subVN+voucherNumber;
         dc.addValue("voucherHeader_voucherNumber",voucherNumber);
         LOGGER.info("voucherNumber:"+voucherNumber);
         if(!voucherNum.equals(voucherNumber))
         {
             LOGGER.info("voucherNum NOt Equal");

             try{
             	if(!cm.isUniqueVN(dc, con,voucherNumber))
                     throw  new TaskFailedException();
             	}catch(Exception e){throw new TaskFailedException(e.getMessage());}
         }
     }catch(SQLException sq){throw new TaskFailedException(sq.getMessage());}
	try{

		String vdt=(String)dc.getValue("voucherHeader_voucherDate");
		int CurdateCheck=1;
		CurdateCheck=cm.isCurDate(dc,con,vdt);
		if(CurdateCheck==0)
			return;

		if(!isValidAmount(dc)){
			dc.addMessage("exilRPError","Server Validation Failed..Check The Data Sent");
				throw  taskExc;
		}

		//getRequiredData(dc);
		updateInVoucherHeader(dc,con);
		cm.updateVoucherMIS("U",con,VoucherHeaderId,(String)dc.getValue("field_id"),dc.getValue("fundSource_id"),(String)dc.getValue("scheme"),(String)dc.getValue("subscheme"),dc.getValue("voucherHeader_voucherDate"));
		com.exilant.eGov.src.domain.BillRegisterBean b=new com.exilant.eGov.src.domain.BillRegisterBean();
		if(!dc.getValue("billid").equals(""))
		{
			if(!(dc.getValue("billid").trim().equals(dc.getValue("oldBillid").trim())))
			{
			b.updateStatus("PASSED",con,dc.getValue("billid"));
			b.updateStatus("PENDING",con,dc.getValue("oldBillid"));
			}
		}

		updateVoucherDetail(dc, transactions,con);
		updateWorksDetail(dc,con);
		LOGGER.debug("after updateWorksDetail");
		//Update SupplierBillDetail Table After Updating WorkDetail Since it extracts values from SupplierBillDetail table
		updateInAssetStatus(dc);
		UpdateInSupplierBillDetail(dc);
		//updateBillStatus(dc);
		ChartOfAccounts engine = ChartOfAccounts.getInstance();
		Transaxtion txnList[] = new Transaxtion[transactions.size()];
		txnList=(Transaxtion[])transactions.toArray(txnList);
		if(!engine.postTransaxtions(txnList, con, dc))throw taskExc;
		//updateEGF_MappedTDS(dc);
		deleteFromVoucherDetail();
		dc.addValue("voucherHeader_voucherNumber",voucherNumber);
			dc.addMessage("eGovSuccess", "CGNumber is "+cgNum+" and VoucherNumber is "+voucherNumber);
	}catch(Exception e){
		dc.addMessage("exilRPError","Error in update :"+e.getMessage());
		throw new TaskFailedException();
	}
}

	public void reverse() throws TaskFailedException{
		GlReverser glr=new GlReverser();
		SupplierBillDetail cbd=new SupplierBillDetail();
		WorksDetail wDet=new WorksDetail();
		ResultSet rset;
		double passedAmt=ExilPrecision.convertToDouble(dc.getValue("supplierBillDetail_passedAmount"),2);
		double adjAmt= ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2);
		double pdAmount=0;
	//	double pAmount=(passedAmt+adjAmt);
		String vhId=null;
		String sphId=null;
		String billRegId=null;

		try{
			Statement st=connection.createStatement();
		//	String cgn=dc.getValue("cgnNum");
			rset=st.executeQuery("select id from voucherHeader where cgn='"+dc.getValue("cgnNum")+"'");
			while(rset.next()){
				vhId=rset.getString(1);
			}

			rset=st.executeQuery("Select id from subledgerPaymentHeader where voucherHeaderId="+vhId);
			while(rset.next()){
				sphId=rset.getString(1);
			}
				if(sphId==null){
					 rset=st.executeQuery("select id,billid from supplierBillDetail where voucherHeaderid="+vhId);
					 while(rset.next()){
						cbd.setId(rset.getString(1));
						billRegId=rset.getString(2);
					}
					rset.close();
					st.close();

					SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
					String vdt=(String)dc.getValue("voucherHeader_effDate");
					String voucherHeader_effDate = formatter.format(sdf.parse(vdt));
					glr.reverse(connection,dc.getValue("cgnNum"),newVcNo,voucherHeader_effDate,cgNum,dc);
					cbd.reverse(connection);
					//reverse asset stauts in egf_asset
					reverseInAssetStatus(dc);
					wDet.setId(dc.getValue("worksDetail_id"));
					wDet.reverseNegative(connection,pdAmount,adjAmt,passedAmt,0);
					if(billRegId!=null)
					{
						reverseBillRegister(dc,connection,billRegId);
						createNewWorkFlow( billRegId);//so that bill again available for the approve mode
					}
					}else{
						dc.addMessage("Supplier is already paid");
					}
				st.close();
			}catch(Exception e){
				dc.addMessage("exilRPError",e.getMessage());
				throw new TaskFailedException();
			}
	}
	private void createNewWorkFlow(String billRegId)throws TaskFailedException {
		PurchaseBillDelegate sDelegate=new PurchaseBillDelegate();
		PurchaseBillForm sbForm=new PurchaseBillForm();
		 
		sbForm.setBillId(billRegId);
		
		try{
			
			WorksBillService worksBillService=(WorksBillService)GetEgfManagers.getWorksBillService();
			EgBillregister billReg=billsService.getBillRegisterById(Integer.valueOf(billRegId));
			sbForm=(PurchaseBillForm)sDelegate.getEgBillRegister(sbForm);
			String wid=billReg.getWorksdetailId();
			Worksdetail wd =(Worksdetail)worksBillService.getWorksDetailById(Integer.valueOf(wid));
			sbForm.CSId=wd.getRelation().getId().toString();
			int userId=billReg.getCreatedBy().getId();//add the user who created Bill(not the Current User) 
			sbForm.setUserId(userId);
			try{  
				User user=(User)userService.getUserByID(userId);
				 String  username=user.getUserName();
				 LOGGER.info(username);
				 sbForm.setUserName(username);
			}
			catch(Exception e)
			{
				LOGGER.error("Exp="+e.getMessage());
				throw new TaskFailedException(e.getMessage());
			}
			sDelegate.createWorkFlow(sbForm);
		}catch(Exception e)
		{
			LOGGER.error("Exp="+e.getMessage());
			throw new TaskFailedException(e.getMessage());
		}
		
		
		// TODO Auto-generated method stub
		
	}
	private void postInAssetStatus(DataCollection dc)throws SQLException,TaskFailedException{

		ResultSet rs;
		String assetId=dc.getValue("asset_name");
		String worksId=dc.getValue("worksDetail_id");
		Statement st=connection.createStatement();
		LOGGER.info("updation into asset status started with assetId:"+assetId);
		//AssetStaus astStatus=new AssetStaus();
		//if(assetId==null) LOGGER.debug("assetId null ");
	//	if(!assetId.equalsIgnoreCase("")){} else LOGGER.debug("empty assetId ");
		if(!assetId.equalsIgnoreCase(""))
		{
			rs=st.executeQuery("SELECT assetId FROM supplierbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and worksdetailid='"+worksId+"' and assetid='"+assetId+"'");
			LOGGER.info("SELECT assetId FROM supplierbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and worksdetailid='"+worksId+"' and assetid='"+assetId+"'");

			if(!rs.next())
			{
				LOGGER.debug("This is a first bill for the assetId:"+assetId);
				st.executeQuery("update eg_asset set statusId="+cm.getEGWStatusId(connection, "ASSET", "CWIP")+" where assetId="+assetId+" and statusid=1");
				LOGGER.info("updated assetStaus:update eg_asset set statusId="+cm.getEGWStatusId(connection, "ASSET", "CWIP")+" where assetId="+assetId+" and statusid=1");
			}
		}
		st.close();

	}

	private void updateInAssetStatus(DataCollection dc)throws SQLException,TaskFailedException{

		ResultSet rs;
		String assetId=dc.getValue("asset_name");
		String vhId=dc.getValue("voucherHeader_id");
		String oldAssetId="";
		String worksId=dc.getValue("worksDetail_id");
		Statement st=connection.createStatement();
		LOGGER.debug("updateInAssetStatus started:");
		rs=st.executeQuery("SELECT assetid FROM supplierbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and cbd.voucherheaderid='"+vhId+"'" );
		if(rs.next())
		{	oldAssetId=rs.getString(1);
		 	rs.close();
		 }
		if(oldAssetId==null) oldAssetId="";
		if(assetId==null) oldAssetId="";
		if(!oldAssetId.equalsIgnoreCase(assetId))
		{
			LOGGER.info("assetId are different");
			rs=st.executeQuery("SELECT assetid FROM supplierbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and cbd.voucherheaderid<>'"+vhId+"'" );
			LOGGER.info("SELECT assetid FROM supplierbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and cbd.voucherheaderid<>'"+vhId+"'" );
			if(!rs.next())
			{
				rs=st.executeQuery("update eg_asset set statusId="+cm.getEGWStatusId(connection, "ASSET", "Created")+" where assetId="+oldAssetId);
				LOGGER.info("updated assetStaus:update eg_asset set statusId="+cm.getEGWStatusId(connection, "ASSET", "Created")+" where assetId="+oldAssetId);
			}
		}

		//AssetStaus astStatus=new AssetStaus();
		//if(assetId==null) LOGGER.debug("assetId null ");
	//	if(!assetId.equalsIgnoreCase("")){} else LOGGER.debug("empty assetId ");

		if(assetId!=null && !assetId.equalsIgnoreCase(""))
		{
			rs=st.executeQuery("SELECT assetId FROM supplierbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and worksdetailid='"+worksId+"' and assetid='"+assetId+"'");
			LOGGER.info("SELECT assetId FROM supplierbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and worksdetailid='"+worksId+"' and assetid='"+assetId+"'");

			if(!rs.next())
			{
				LOGGER.info("This is first bill for the assetId:"+assetId);
				st.executeQuery("update eg_asset set statusId="+cm.getEGWStatusId(connection, "ASSET", "CWIP")+" where assetId="+assetId);
				LOGGER.info("updated assetStaus:update eg_asset set statusId="+cm.getEGWStatusId(connection, "ASSET", "CWIP")+" where assetId="+assetId);
			}
		}
		st.close();
		LOGGER.debug("updateInAssetStatus end");
	}

	private void reverseInAssetStatus(DataCollection dc)throws SQLException,TaskFailedException{

		ResultSet rs;
		String assetId=dc.getValue("asset_name");
		String vhId=dc.getValue("voucherHeader_id");
		Statement st=connection.createStatement();
		LOGGER.info("updation into asset status started with assetId:"+assetId);
		//AssetStaus astStatus=new AssetStaus();
		//if(assetId==null) LOGGER.debug("assetId null ");
	//	if(!assetId.equalsIgnoreCase("")){} else LOGGER.debug("empty assetId ");
		if(!assetId.equalsIgnoreCase(""))
		{
			rs=st.executeQuery("SELECT assetId FROM supplierbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and cbd.voucherheaderid <>'"+vhId+"' and assetid='"+assetId+"'");
			LOGGER.info("SELECT assetId FROM supplierbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and cbd.voucherheaderid <>'"+vhId+"' and assetid='"+assetId+"'");

			if(!rs.next())
			{
				LOGGER.debug("This is a first bill for the assetId:"+assetId);
				st.executeQuery("update eg_asset set statusId="+cm.getEGWStatusId(connection, "ASSET", "Created")+" where assetId="+assetId);
				LOGGER.debug("updated assetStaus:update eg_asset set statusId="+cm.getEGWStatusId(connection, "ASSET", "Created")+" where assetId="+assetId);
			}
		}
		st.close();
	}
	public String calculateNetAmt(DataCollection dc)
	{
		//BigDecimal netAmt=new BigDecimal("0.00");
		//Add the net payable to the advance adjusted because both gets accounted to the same code -Contractor Payable
		BigDecimal netAmt=new BigDecimal(ExilPrecision.convertToDouble(dc.getValue("worksDetail_NetAmount"),2)).add (new BigDecimal(ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2)));
		LOGGER.info("Net Amount being credited to Internal Code is :"+netAmt);
		return netAmt.toString();
	}
	
	
	/**
		 * 
		 * @param dc
		 * @param cn
		 * @param billRegId
		 * @throws TaskFailedException
		 * @throws SQLException
		 * this Method will Set The StatusId to 29 and status to approve and inserts one record 
		 * into egw_statusChange and eg_actiondetails
		 */
		private void reverseBillRegister(DataCollection dc,Connection cn,String billRegId)throws TaskFailedException,SQLException
		{
					String todayTime=cm.getCurrentDateTime(cn);
			  		BillRegisterBean bBean=new BillRegisterBean();
			  		bBean.setBillStatus("PENDING");
			  		//bBean.setBillStatusId("37");
			  		String pendingStatusId = cm.getEGWStatusId(cn, "PURCHBILL", "Pending");
			  		bBean.setBillStatusId(pendingStatusId);
			  		bBean.setId(billRegId);
			  		bBean.setLastModifiedDate(cm.getSQLDateTimeFormat(todayTime));
			  		bBean.update(cn);
			  		postInEGActionDetails(dc,billRegId,"Reverse Supplier Journal",cn);//insert into eg_actiondetails 
			  		//postInEGWStatusChange(dc,billRegId,"38","37",cn);//insert into egw_satuschange no applied since it is not updating status id
			  		postInEGWStatusChange(dc,billRegId,cm.getEGWStatusId(cn,"PURCHBILL","Passed"),pendingStatusId,cn);//insert into egw_satuschange no applied since it is not updating status id
		    }
		  
		    
		  
		/**
			 * This function will insert the egw_satuschange for the
			 * @param dc
			 * @throws SQLException
			 * @throws TaskFailedException
	*/

//
	public boolean postInEGActionDetails(DataCollection dc,String abillId,String aType,Connection connection)throws TaskFailedException{
	LOGGER.debug("Inside postInEGActionDetails");
			boolean result=false;
			ActionDetails acdetail = new ActionDetails();
			todayTime=cm.getCurrentDateTime(connection);

			acdetail.setModuletype("PURCHBILL");
		acdetail.setModuleid(abillId);
		acdetail.setActionDoneBy(dc.getValue("egUser_id"));

		acdetail.setActionDoneOn(cm.getSQLDateTimeFormat(todayTime));
		acdetail.setLastmodifieddate(cm.getSQLDateTimeFormat(todayTime));

		if(!dc.getValue("reviewDetail_comments").equals(""))
		acdetail.setActionDetcomments(dc.getValue("reviewDetail_comments"));

		//acdetail.setActionType("Reject Salary Bill");
		acdetail.setActionType(aType);
		acdetail.setCreatedby(dc.getValue("egUser_id"));

		try	{
			acdetail.insert(connection);
				result=true;
			}catch(Exception e)	{
				throw new TaskFailedException(e.getMessage());
			}
			return result;
	}  

	/**
		 * This function will update the EG_BillRegister for the
		 * @param dc
		 * @throws SQLException
		 * @throws TaskFailedException
	*/
	public boolean postInEGWStatusChange(DataCollection dc,String abillId,String afromstatus,String atostatus,Connection connection)throws TaskFailedException{
	LOGGER.debug("Inside postInEGWStatusChange");
		boolean result=false;
		EgfStatusChange egfstatus = new EgfStatusChange();
		todayTime=cm.getCurrentDateTime(connection);

	egfstatus.setModuletype("PURCHBILL");
	egfstatus.setModuleid(abillId);
	egfstatus.setFromstatus(afromstatus);
	egfstatus.setTostatus(atostatus);

	egfstatus.setCreatedby(dc.getValue("egUser_id"));
	egfstatus.setLastmodifieddate(cm.getSQLDateTimeFormat(todayTime));

		try	{
		egfstatus.insert(connection);
			result=true;
		}catch(Exception e)	{
			LOGGER.debug("Exp="+e.getMessage());
			throw new TaskFailedException(e.getMessage());
		}
		return result;
	}

	/**
	 * 
	 * @param transactions Arraylist containg glcodes and other details for generalledger table
	 * @param dc
	 * @param startIndex Position of glcodes Starting in the arraylist 
	 * @param gridName entity containg the subledger datas 
	 * @return
	 * @throws TaskFailedException
	 * Call this method once for one earning and once for deduction for setting the subledgercodes to the list of glcodes 
	 * Added by mani for new subledgerscreen this method can be used get values from entitygrids 
	 * from Starting index to end of the arraylist it adds transaction parameter to the arraylist 
	 * which is read from entityGrids 
	 */	
		public ArrayList addRequiredDataToList(ArrayList transactions, DataCollection dc,int startIndex,String gridName) throws TaskFailedException{
			int k=1;
			for(int i=startIndex;i<transactions.size();i++){
				ArrayList reqData=getRequiredData(transactions,dc,startIndex,k,gridName);
				if(defReqData!=null){
					for(int a=0;a<defReqData.size();a++){
						reqData.add(defReqData.get(a));
					}
				}
				Transaxtion trnDetail=(Transaxtion)transactions.get(i);
				trnDetail.setTransaxtionParam(reqData);
				k++;
			}
			return transactions;
		}
	/**
	 * @param transactions
	 * @param dc
	 * @param txnPos
	 * @param rowIdx
	 * @param gridName
	 * @return
	 * @throws TaskFailedException
	 * Added by mani for new subledgerscreen this method can be used get values from entitygrids 
	 * it needs the position of transaction object inthe transactions ArrayList 
	 * 
	 */
		public ArrayList getRequiredData(ArrayList transactions,DataCollection dc,int txnPos, int rowIdx,String gridName) throws TaskFailedException{
			DataExtractor de=DataExtractor.getExtractor();
			String sql="select ID as \"ID\",name as  \"name\",tableName as \"tableName\","+
			"description as \"description\",columnName as \"columnName\",attributeName as \"attributeName\""+
			",nbrOfLevels as  \"nbrOfLevels\" from accountdetailtype";
			if(detailTypes==null){
				detailTypes=de.extractIntoMap(sql,"ID",AccountDetailType.class);
			}
			ArrayList reqParams=new ArrayList();
			String[][] entityGrid=cm.insertBlankRow(dc.getGrid(gridName));
			for(int i=1;i<entityGrid.length;i++){
				//search entities only for the rowindex sent
				//entityGrid[i][0]-dettypeid entityGrid[i][1]-detkey  entityGrid[i][2]-detvalue entityGrid[i][3]-rowIndex 
				//entityGrid[i][4]- grid_amount entityGrid[i][5]- grid_Code  
				if(entityGrid[i][3]!=null && ! entityGrid[i][3].equalsIgnoreCase(""))
				{
					if(entityGrid[i][3].equals(String.valueOf(rowIdx))){
						AccountDetailType accType=(AccountDetailType)detailTypes.get(entityGrid[i][0]);
						if(accType!=null){
							TransaxtionParameter reqData=new TransaxtionParameter();
							try {
								LOGGER.debug("trnpos"+txnPos+"Roeindex"+rowIdx);
								LOGGER.debug("glcode:"+((Transaxtion)transactions.get(txnPos+rowIdx-1)).getGlCode());
								reqData.setGlcodeId(cmImpl.getGlCodeId(((Transaxtion)transactions.get(txnPos+rowIdx-1)).getGlCode(), connection)+"");
								} catch (Exception e) {
									LOGGER.error("Error Occured while while adding Subledger Values");
								throw new TaskFailedException(e.getMessage());
								}

							reqData.setDetailName(accType.getAttributeName());
							reqData.setDetailKey(entityGrid[i][1]);
							//String detailAmt=((Transaxtion)transactions.get(txnPos+rowIdx-1)).getDrAmount();
							/*if(detailAmt==null || detailAmt.trim().length()==0 || Double.parseDouble(detailAmt)== 0 )
							detailAmt=((Transaxtion)transactions.get(txnPos+rowIdx-1)).getCrAmount();*/

							reqData.setDetailAmt(entityGrid[i][4]);
							LOGGER.debug("The Contes of transaction parameters");
							LOGGER.debug(reqData.getGlcodeId());
							LOGGER.debug(reqData.getDetailName());
							LOGGER.debug(reqData.getDetailKey());
							LOGGER.debug(reqData.getDetailAmt());
							reqParams.add(reqData);
						}
					}
				}
			}
			return reqParams;
		}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setBillsService(BillsService billsService) {
		this.billsService = billsService;
	}
		
}
