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
import org.egov.billsaccounting.client.WorksBillDelegate;
import org.egov.billsaccounting.client.WorksBillForm;
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
import com.exilant.eGov.src.domain.ContractorBillDetail;
import com.exilant.eGov.src.domain.EgfStatusChange;
import com.exilant.eGov.src.domain.VoucherDetail;
import com.exilant.eGov.src.domain.VoucherHeader;
import com.exilant.eGov.src.domain.WorksDetail;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.dataservice.DataExtractor;

public class ContractorJournal extends AbstractTask{
	private static final Logger LOGGER=Logger.getLogger(ContractorJournal.class);
	private UserService userService;
	private BillsService billsService;
	private Connection connection;
	private Statement statement;
	private ResultSet resultset;
	private DataCollection dc;
	private HashMap detailTypes;
	private TaskFailedException taskExc;
	private int contractorId = 0, voucherHeaderId=0;
	private ArrayList requiredList;
	public double OtherRecoveries=0.0;
	public String voucherNumber;
	public String fiscalPid;
	public ArrayList defReqData;
	public String vType;
	public String eg_voucher;
	public String cgNum;
	public String fId;
	public String newVcNo;
	public String fuctid="";
	int VoucherHeaderId;
	String todayTime;
	ArrayList transactions = new ArrayList();

	EGovernCommon cm = new EGovernCommon();
	CommonMethodsI cmImpl=new CommonMethodsImpl();
	//no argument Constructor
	public ContractorJournal(){}
	
	public void execute(String taskName,
							String gridName,
							DataCollection dc,
							Connection conn,
							boolean erroOrNoData,
							boolean gridHasColumnHeading, String prefix) throws TaskFailedException{

		this.dc = dc;
		this.connection = conn;
		taskExc = new TaskFailedException();
		String contractorGrid[][] = (String[][])dc.getGrid("gridContractorJournal");
		// 4-functionid 0-glcode
		if(!cm.checkDuplicatesForFunction_AccountCode(dc,contractorGrid, 4,0))throw new TaskFailedException();
		if(dc.getValue("showGlEntries").equalsIgnoreCase("show"))
		{
		StringBuffer str=new StringBuffer(";");   
		StringBuffer stradv=new StringBuffer("");
		str=str.append("ConPayCode"+"^");
		String code1=EGovConfig.getProperty("egf_config.xml","ConPayCode","","ContractorCodes");
		str=str.append(code1+"^");
		String name1=getCodeName(code1);
		str=str.append(name1+";");
		String outp=str.toString();
		String advCode=EGovConfig.getProperty("egf_config.xml","ConAdvCode","","ContractorCodes");
		if(!code1.equalsIgnoreCase(advCode))
		{  
			stradv=stradv.append("ConAdvCode"+"^");
			stradv=stradv.append(advCode+"^");
			String name2=getCodeName(advCode);
			stradv=stradv.append(name2+";");
			outp=outp+stradv.toString();
		}
	
		dc.addValue("glCodeName",outp);
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
				
		        
			newVcNo=cm.getVoucherNumber(fId, FinancialConstants.WORKSBILL_VOUCHERNO_TYPE, dc.getValue("voucherHeader_newVcNo"), conn);
			//rvNumber(dc,conn,fId);
			vType=newVcNo.substring(0,Integer.parseInt(FinancialConstants.VOUCHERNO_TYPE_LENGTH));
			eg_voucher=cm.getEg_Voucher(vType,fiscalPid,conn);
			// Genarate new VoucherNumber For Reversal if Voucher generation Type is 'Auto'
			//Use the logic used to create Receipt
			VoucherTypeForULB voucherType=new VoucherTypeForULB();
			String vTypeForULB=(String)voucherType.readVoucherTypes("Journal");
	        if(vTypeForULB.equalsIgnoreCase("Auto"))
	        	{
	        	CommonMethodsImpl cmImpl = new CommonMethodsImpl();
	        	newVcNo=cmImpl.getTxnNumber(fId, FinancialConstants.WORKSBILL_VOUCHERNO_TYPE, vt, conn);
	        	}	
			}
			catch(Exception e)
			{
				dc.addMessage("exilRPError","Contractor Journal error : " + e.toString());
	    		throw taskExc;
			}
			for(int i=eg_voucher.length();i<10;i++)
			{
				 eg_voucher="0"+eg_voucher;
			}
			cgNum=vType+eg_voucher;
			
			reverse();
			
			dc.addMessage("eGovSuccess","for Reversing Contractor Journal  : CGNumber is "+cgNum+" and VoucherNumber is "+newVcNo);
			return;
		 }

			else if(dc.getValue("modeOfExec").equalsIgnoreCase("edit")){
		  	 	try{
		  	 		editVoucher(dc,conn);
		  	 	}catch(Exception e){throw new TaskFailedException(e.getMessage());}
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
		}
			if(!cm.isUniqueVN(dc, conn,voucherNumber))
				throw new TaskFailedException();

			/*added in egf*/
			//checkValidCodes(dc);
			/*added in egf*/

			if(!isValidAmount(dc)){
				dc.addMessage("exilRPError","Server Validation Failed..Check The Data Sent");
   				throw  taskExc;
			}

			getRequiredData(dc,null,null);

			postInVoucherHeader(dc,conn);
			cm.updateVoucherMIS("I",conn,voucherHeaderId,(String)dc.getValue("field_id"), (String)dc.getValue("fundSource_id"),(String)dc.getValue("scheme"),(String)dc.getValue("subscheme"),dc.getValue("voucherHeader_voucherDate"));
			if(!dc.getValue("billid").toString().equals("")){
			com.exilant.eGov.src.domain.BillRegisterBean b=new com.exilant.eGov.src.domain.BillRegisterBean();
			b.updateStatus("PASSED",conn,dc.getValue("billid"));
			}
			postInVoucherDetail(dc,transactions);
			postInAssetStatus(dc);
			postInContractorBillDetail(dc);
			postInWorksDetail(dc);

			LOGGER.debug("before ChartOfAccounts");
    		ChartOfAccounts engine=ChartOfAccounts.getInstance();
    		LOGGER.debug("after ChartOfAccounts");
    		Transaxtion txnList[]=new Transaxtion[transactions.size()];
    		txnList=(Transaxtion[])transactions.toArray(txnList);
    		LOGGER.debug("before txn");
    		if(!engine.postTransaxtions(txnList, connection, dc)){
    			 //dc.addMessage("exilRPError","Engine Validation Failed");
    			
    			throw taskExc;
    		}
    		LOGGER.debug("before engine");
    		//postInEGF_MappedTDS(dc);
    		/* End of GLPosting */
//			status(dc);//Added for entering status in EGF_RECORD_STATUS
    		//cm.UpdateVoucherStatus(dc,"Contractor Journal",conn,voucherHeaderId);
    		cm.UpdateVoucherStatus(dc,"Journal Voucher",conn,voucherHeaderId);
    		dc.addValue("voucherHeader_voucherNumber", voucherNumber);
    		dc.addMessage("eGovSuccess","Contractor Journal : CGNumber is "+cgNum+" and VoucherNumber is "+voucherNumber);
    	}catch(Exception sqlex){
    		dc.addMessage("exilRPError","Transaction Failed :"+sqlex.getMessage());
    		LOGGER.error("Exp="+sqlex.getMessage());
    		throw taskExc;
		}
	}

	String getCodeName(String code) throws TaskFailedException
	{
		String name="";
		try{
			Statement st=connection.createStatement();
			ResultSet rset=st.executeQuery("select name from chartofaccounts where glcode='"+code+"'");
			if(rset.next())
				name=rset.getString(1);
			st.close();
		}
		catch(Exception e)
		{
			LOGGER.info("error  "+ e.toString());
    		throw taskExc;
		}
		return name;
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
		vh.setVoucherDate(voucherHeader_voucherDate);
		vh.setName("Contractor Journal");
		vh.setType("Journal Voucher");
		vh.setVoucherNumber(voucherNumber);
		vh.setFundId(dc.getValue("fund_id"));
		vh.setDepartmentId(dc.getValue("department_id"));
		vh.setFundSourceId(dc.getValue("fundSource_id"));
		vh.setDescription(dc.getValue("voucherHeader_narration"));
		vh.setIsConfirmed("1");
		vh.setFiscalPeriodId(cm.getFiscalPeriod(voucherHeader_voucherDate));
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
		LOGGER.debug("..voucherNumber.."+voucherNumber+"  .VoucherHeader_voucherNumber. "+dc.getValue("voucherHeader_voucherNumber"));
		LOGGER.debug("voucherHeader_voucherDate:  "+voucherHeader_voucherDate+"  oldVoucherDate:  "+oldVoucherDate);
		LOGGER.debug("oldFundid:  "+oldFundid+"  dc.getValue(fund_id):  "+dc.getValue("fund_id"));
		if((oldFundid.equals(dc.getValue("fund_id").toString())) && (cm.getFiscalPeriod(voucherHeader_voucherDate).equalsIgnoreCase(cm.getFiscalPeriod(oldVoucherDate))))
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
				if(!(oldFundid.equals(dc.getValue("fund_id").toString())))
				{
					LOGGER.debug("Funds are different");
					//String voucherNum=dc.getValue("voucherHeader_voucherNumber").toString();
					String voucherNum=voucherNumber;
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
				LOGGER.info("Exp in getting new VNo. "+e);
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
		VH.setName("Contractor Journal");
		VH.setType("Journal Voucher");
		VH.update(con);
		VoucherHeaderId = VH.getId();

		//Insert Record in the egf_record_status table
		cm.UpdateVoucherStatus(dc,"Journal Voucher",con,VoucherHeaderId);
}

	private void postInContractorBillDetail(DataCollection dc) throws TaskFailedException, SQLException{
				statement = connection.createStatement();
				ContractorBillDetail cbd = new ContractorBillDetail();
				String contBillDetail_billDate="";
				statement = connection.createStatement();
				try{
					SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
					String vdt=(String)dc.getValue("contBillDetail_billDate");
					contBillDetail_billDate = formatter.format(sdf.parse(vdt));
					LOGGER.debug("billdate1:*******"+contBillDetail_billDate);
				}catch(Exception e){
					LOGGER.error("date conversion error");
					throw new TaskFailedException(e.getMessage());
				}

				getResultSet("relation", "and code='" + dc.getValue("contractor_code") + "'", dc);
				contractorId = resultset.getInt("ID");
				cbd.setContractorId(contractorId + "");
				resultset.close();
				statement.close();
				cbd.setWorksDetailId(dc.getValue("worksDetail_id"));
				cbd.setBillAmount(ExilPrecision.convertToString(dc.getValue("contractorBillDetail_billAmount"),2));
				cbd.setBilldate(contBillDetail_billDate);
				cbd.setBillNumber(dc.getValue("contBillDetail_billNumber"));
				double passedAmt=ExilPrecision.convertToDouble(dc.getValue("contractorBillDetail_passedAmount"),2);
			//	double adjAmt= ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2);
				cbd.setPassedAmount(ExilPrecision.convertToString((passedAmt),2));
				cbd.setApprovedBy(dc.getValue("contractorBillDetail_approvedBy"));
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
				if(!dc.getValue("billid").equalsIgnoreCase(""))
					cbd.setBillId(dc.getValue("billid"));
				cbd.insert(connection);
			//	contractorBillId = cbd.getId();
		}

		private void UpdateInContractorBillDetail(DataCollection dc) throws TaskFailedException, SQLException{
				statement = connection.createStatement();
				ContractorBillDetail cbd = new ContractorBillDetail();
				String contBillDetail_billDate="";
				statement = connection.createStatement();
				try{
					SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
					String vdt=(String)dc.getValue("contBillDetail_billDate");
					contBillDetail_billDate = formatter.format(sdf.parse(vdt));
					LOGGER.debug("billdate1:*******"+contBillDetail_billDate);
				}catch(Exception e){
					LOGGER.error("date conversion error");
					throw new TaskFailedException(e.getMessage());
				}
				getResultSet("relation", "and code='" + dc.getValue("contractor_code") + "'", dc);
				contractorId = resultset.getInt("ID");
				cbd.setContractorId(contractorId + "");

				cbd.setWorksDetailId(dc.getValue("worksDetail_id"));
				cbd.setBillNumber(dc.getValue("contBillDetail_billNumber"));
				cbd.setBilldate(contBillDetail_billDate);
				cbd.setBillAmount(ExilPrecision.convertToString(dc.getValue("contractorBillDetail_billAmount"),2));
				double passedAmt=ExilPrecision.convertToDouble(dc.getValue("contractorBillDetail_passedAmount"),2);
				cbd.setPassedAmount(ExilPrecision.convertToString((passedAmt),2));
				cbd.setApprovedBy(dc.getValue("contractorBillDetail_approvedBy"));
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
				
				LOGGER.info("SELECT id from contractorbilldetail where voucherheaderid = " + VoucherHeaderId);
				ResultSet rs = statement.executeQuery("SELECT id from contractorbilldetail where voucherheaderid = " + VoucherHeaderId);
				if(rs.next()){
					cbd.setId(rs.getInt("id") + "");
					cbd.update(connection);
				}
				else {
					cbd.insert(connection);
				}

		//		contractorBillId = cbd.getId();
				resultset.close();
				statement.close();
	}

	private void postInVoucherDetail(DataCollection dc, ArrayList transactions) throws TaskFailedException, SQLException{
		double debitAmount=0, creditAmount=0;
		String glCode, accName;
		String contractorGrid[][] = (String[][])dc.getGrid("gridContractorJournal");
		VoucherDetail vd = new VoucherDetail();
		
		for(int i=0; i < contractorGrid.length; i++){
			if(contractorGrid[i][0].equalsIgnoreCase("")) continue;
			//PreDefinedAccCodes codes = new PreDefinedAccCodes();
			
			if(contractorGrid[i][0].equals(EGovConfig.getProperty("egf_config.xml","ConPayCode","","ContractorCodes")) || contractorGrid[i][0].equals(EGovConfig.getProperty("egf_config.xml","ConAdvCode","","ContractorCodes"))){
				dc.addMessage("exilError","Internal Codes Used  : transaction cancelled.");
				throw  taskExc;
			}
			debitAmount += ExilPrecision.convertToDouble(contractorGrid[i][2], 2);
		}
		if( !(debitAmount> 0.0) ){
			dc.addMessage("exilError","no transaction involved.");
			throw  taskExc;
		}

		int lineID=1;

		for(int i=0; i < contractorGrid.length; i++){
			if(contractorGrid[i][0].equalsIgnoreCase("")) continue;
			//0. accountCode     1. accountHead     2. debitAmount   3. creditAmount    4. narration 5.tdsId
			vd.setLineID(String.valueOf(lineID));
			lineID++;
			vd.setVoucherHeaderID(voucherHeaderId + "");
			glCode = contractorGrid[i][0];
			accName = getAccountName(glCode, dc);
			vd.setGLCode(glCode);
			vd.setAccountName(accName);
			vd.setDebitAmount(ExilPrecision.convertToString(contractorGrid[i][2],2));
			//vd.setCreditAmount(ExilPrecision.convertToString(contractorGrid[i][3],2));
			vd.setCreditAmount("0");
			vd.setNarration(contractorGrid[i][4]);
			vd.insert(connection);

			Transaxtion transaction = new Transaxtion();
			transaction.setGlCode(glCode);
			transaction.setGlName(accName);
			transaction.setDrAmount(ExilPrecision.convertToString(contractorGrid[i][2],2));
			transaction.setCrAmount("0");
			transaction.setVoucherLineId(String.valueOf(vd.getId()));
			transaction.setVoucherHeaderId(String.valueOf(voucherHeaderId));
			transaction.setNarration(contractorGrid[i][3]);
			
			LOGGER.info("transactions size:"+transactions.size());
			//transaction.setTransaxtionParam(getRequiredData(dc,i+1,vd));
			 fuctid=contractorGrid[i][4];
			if(fuctid!=null || fuctid.length()!=0 ) transaction.setFunctionId(contractorGrid[i][4]);
			transactions.add(transaction);
		}
		transactions=addRequiredDataToList(transactions, dc,0,"entities_grid");
		lineID=transactions.size()+1;
		int start=transactions.size();
		/*to add the default contractor payable and tds codes;add any extra recoveries in that function */
		//OtherRecoveries=creditAmount;
		addContractorAndTDSAcc(dc,transactions,creditAmount);
	postDefaultsInVoucherDtl(dc,transactions,lineID,start);
	}

private void  deleteFromVoucherDetail ()throws SQLException,Exception
{
	statement = connection.createStatement();
		LOGGER.info("select LineId  from VoucherDetail where voucherheaderid="+VoucherHeaderId+" order by LineId");

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
	private void updateVoucherDetail(DataCollection dc, ArrayList transactions) throws SQLException, TaskFailedException {
		double debitAmount=0, creditAmount=0;
		VoucherDetail VD =new VoucherDetail();
		String[][] voucherDetail = (String[][])dc.getGrid("gridContractorJournal");
		ArrayList vDetailId=new ArrayList();
		ArrayList vDetailLineId=new ArrayList();
		ArrayList vAccountName=new ArrayList();
		ArrayList vDetailGlcode=new ArrayList();
	
		for(int i=0; i < voucherDetail.length; i++){

			if(voucherDetail[i][0].equalsIgnoreCase("")) continue;
		//	PreDefinedAccCodes codes = new PreDefinedAccCodes();
			if(voucherDetail[i][0].equals(EGovConfig.getProperty("egf_config.xml","ConPayCode","","ContractorCodes")) || voucherDetail[i][0].equals(EGovConfig.getProperty("egf_config.xml","ConAdvCode","","ContractorCodes"))){
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

		voucherDetail = (String[][])dc.getGrid("gridContractorJournal");
		int i=0;

		for(i=0;i<voucherDetail.length;i++){
				if(voucherDetail[i][0].equalsIgnoreCase("")) continue;
				VD.setVoucherHeaderID(String.valueOf(VoucherHeaderId));
				VD.setGLCode(voucherDetail[i][0]);
				VD.setAccountName(getAccountName(voucherDetail[i][0], dc));
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
				transaction.setTransaxtionParam(getRequiredData(dc,i+1,VD));
					if(fuctid!=null ||fuctid.length()!=0 ) transaction.setFunctionId(voucherDetail[i][4]);
				transactions.add(transaction);
			}
		transactions=addRequiredDataToList(transactions,dc,0,"entities_grid");
		int lineID=transactions.size()+1;
		int start=transactions.size();
		addContractorAndTDSAcc(dc,transactions,creditAmount);
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
			
			vd.setAccountName(getAccountName(glCode, dc));
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
	private void postDefaultsInVoucherDtl(DataCollection dc, ArrayList transactions,int lineID,int start) throws SQLException, TaskFailedException	{
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
			vd.setAccountName(getAccountName(glCode, dc));
			vd.setDebitAmount(trn.getDrAmount());
			vd.setCreditAmount(trn.getCrAmount());
			vd.setNarration(dc.getValue("voucherHeader_narration"));
			vd.insert(connection);
			trn.setVoucherLineId(String.valueOf(vd.getId()));
			trn.setVoucherHeaderId(String.valueOf(voucherHeaderId));
			trn.setNarration(dc.getValue("voucherHeader_narration"));
			}
	}
	private String getAccountName(String aGLCode, DataCollection dc) throws TaskFailedException{
		String accountName="";
		try{
			statement = connection.createStatement();
			resultset = statement.executeQuery("SELECT name FROM chartofaccounts where glcode='" + aGLCode + "'");
			resultset.next();
			accountName = resultset.getString("Name");
			resultset.close();
			statement.close();
		}catch(SQLException sqlex){
			dc.addMessage("exilInvalidGLCode", aGLCode + sqlex.toString());
			throw  taskExc;
		}
		return accountName;
	}

	public boolean validate(DataCollection dc)throws TaskFailedException{
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
						reqData.setGlcodeId(cmImpl.getGlCodeId(vd.getGLCode(), connection)+"");
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
		double passAmount=ExilPrecision.convertToDouble(dc.getValue("contractorBillDetail_passedAmount"),2);
		double adjAmt=ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2);
	//	double advAmt=ExilPrecision.convertToDouble(dc.getValue("worksDetail_advanceAmount"),2);
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
		double passAmount=ExilPrecision.convertToDouble(dc.getValue("contractorBillDetail_passedAmount"),2);
		//double TrxnPassAmount=ExilPrecision.convertToDouble(dc.getValue("contractorBillDetail_passedAmount"),2);
		double adjAmt=ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2);
	//	double advAmt=ExilPrecision.convertToDouble(dc.getValue("worksDetail_advanceAmount"),2);
		//wDet.setAdvanceAmount(ExilPrecision.convertToString(adjAmt,2));
		//Getting Old PassedAmount and Old AdjustedAmount
		ResultSet rs=st.executeQuery("select advadjamt,passedamount,worksDetailId from contractorbilldetail where voucherheaderid="+dc.getValue("voucherHeader_id"));
		LOGGER.info("select advadjamt,passedamount,worksDetailId from contractorbilldetail where voucherheaderid="+dc.getValue("voucherHeader_id"));
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
		double passedAmount=ExilPrecision.convertToDouble(dc.getValue("contractorBillDetail_passedAmount"),2);
		double adjustmentAmount=ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2);
		double billAmount=ExilPrecision.convertToDouble(dc.getValue("contractorBillDetail_billAmount"),2);
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
                ResultSet rs=st.executeQuery("select advadjamt,passedamount from contractorbilldetail where voucherheaderid="+dc.getValue("voucherHeader_id"));
                LOGGER.info("select advadjamt,passedamount from contractorbilldetail where voucherheaderid="+dc.getValue("voucherHeader_id"));
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
		LOGGER.debug(billAmount+" "+totalPassedAmount+" "+totalValue);
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
	/**
	 * 
	 * @param dc
	 * @param transactions
	 * @param debitAmount
	 * @param creditAmount
	 * @throws TaskFailedException
	 * This method adds the voucherdetail from deduction grid 
	 * if any code is subledger code contractor id is set as detail key
	 */
	private void addContractorAndTDSAcc(DataCollection dc,ArrayList transactions,double creditAmount)throws TaskFailedException{
		String dedfuct="";
		//PreDefinedAccCodes codes=new PreDefinedAccCodes();
		//LOGGER.info("inside the addContractorAndTDSAcc ");
		//ArrayList reqList=getRequiredData(dc);
		//contractor payable
		double passAmount=ExilPrecision.convertToDouble(dc.getValue("contractorBillDetail_passedAmount"),2);
		double adjustAmt=ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2);
		double tdsAmount=0;
		//String fuctid="";
		String contractorDedGrid[][] = (String[][])dc.getGrid("gridContractorDeduction");
		for(int i=0; i < contractorDedGrid.length; i++)
		{
			 if(contractorDedGrid[i][2].equalsIgnoreCase("") || contractorDedGrid[i][0].equalsIgnoreCase("") ) continue;
			 if(contractorDedGrid[i][0].equals(EGovConfig.getProperty("egf_config.xml","ConPayCode","","ContractorCodes")) || contractorDedGrid[i][0].equals(EGovConfig.getProperty("egf_config.xml","ConAdvCode","","ContractorCodes")) || contractorDedGrid[i][0].equals(EGovConfig.getProperty("egf_config.xml","SupPayCode","","SupplierCodes")) || contractorDedGrid[i][0].equals(EGovConfig.getProperty("egf_config.xml","SupAdvCode","","SupplierCodes")) )
			 {
					dc.addMessage("exilError","Internal Codes Used  : transaction cancelled.");
					throw  taskExc;
				}
		} 
		for(int i=0; i < contractorDedGrid.length; i++)
		{
		 if(contractorDedGrid[i][2].equalsIgnoreCase("") || contractorDedGrid[i][0].equalsIgnoreCase("") ) continue;
		 /* checking for duplicate account code*/
		/*	for(int j=i+1; j < contractorDedGrid.length; j++)
			{
				if(contractorDedGrid[i][0].equals(contractorDedGrid[j][0]))
				{
				dc.addMessage("exilDuplicateGLCode", contractorDedGrid[i][0].toString());
				throw  taskExc;
				}
			}*/
			for(int i1=0; i1 < contractorDedGrid.length; i1++)
			{
				for(int j=0; j < contractorDedGrid[i1].length; j++){
				}
			}
			Transaxtion trn=new Transaxtion();
			if(contractorDedGrid[i][0].contains("`-`")){
					String glcode[]=contractorDedGrid[i][0].split("`-`");
					contractorDedGrid[i][0]=glcode[0];
					trn.setGlCode(glcode[0]);
				}
			else
				trn.setGlCode(contractorDedGrid[i][0]);
			LOGGER.debug("trn.getGlCode():"+trn.getGlCode());
			trn.setGlName(getAccountName(contractorDedGrid[i][0],dc));
			trn.setDrAmount("0");
			trn.setCrAmount(ExilPrecision.convertToString(contractorDedGrid[i][2],2));
			tdsAmount += ExilPrecision.convertToDouble(contractorDedGrid[i][2], 2);
			if(contractorDedGrid[i][4].equalsIgnoreCase(""))
				OtherRecoveries=OtherRecoveries+ExilPrecision.convertToDouble(contractorDedGrid[i][2], 2);
			trn.setNarration(dc.getValue("voucherHeader_narration"));
			//trn.setTransaxtionParam(requiredList);
			VoucherDetail vd=new VoucherDetail();//used for transfering data to getRequiredData function
			vd.setGLCode(trn.getGlCode());	vd.setDebitAmount(trn.getCrAmount());
			LOGGER.debug("TDS ID is -->"+contractorDedGrid[i][4]);
			trn.setTransaxtionParam(getRequiredData(dc,vd,contractorDedGrid[i][4]));//Set tdsid for Eg_remittance_gldtl table
			dedfuct=contractorDedGrid[i][3];
			if(dedfuct!=null && dedfuct.length()!=0)
				trn.setFunctionId(dedfuct);
			transactions.add(trn);
		}
		String conPayable=EGovConfig.getProperty("egf_config.xml","ConPayCode","","ContractorCodes");
		String conAdvance=EGovConfig.getProperty("egf_config.xml","ConAdvCode","","ContractorCodes");
		
		//if Contractors Payable and Contractor advance Payable are not mapped to same code make two different entries
		//else make single entry
		
		if(!conPayable.equalsIgnoreCase(conAdvance))
		{
		Transaxtion trn=new Transaxtion();
		if(ExilPrecision.convertToDouble((passAmount-creditAmount)-tdsAmount-adjustAmt,2)>0){
			trn.setGlCode(conPayable);
			trn.setGlName(getAccountName(conPayable,dc));
			trn.setDrAmount("0");
			trn.setCrAmount(ExilPrecision.convertToString((passAmount-creditAmount)-tdsAmount-adjustAmt,2));
			trn.setNarration(dc.getValue("voucherHeader_narration"));
			//trn.setTransaxtionParam(requiredList);
			VoucherDetail vd=new VoucherDetail();//used for transfering data to getRequiredData function
			vd.setGLCode(trn.getGlCode());	
			vd.setCreditAmount(trn.getCrAmount());
			trn.setTransaxtionParam(getRequiredData(dc,vd,null));
			transactions.add(trn);
		}
		//advance
		Transaxtion trnadv=new Transaxtion();
		if(adjustAmt>0)
		{
			trnadv.setGlCode(conAdvance);
			trnadv.setGlName(getAccountName(conAdvance,dc));
			trnadv.setDrAmount("0");
			trnadv.setCrAmount(ExilPrecision.convertToString(adjustAmt,2));
			trnadv.setNarration(dc.getValue("voucherHeader_narration"));
			//trn.setTransaxtionParam(requiredList);
			VoucherDetail vd=new VoucherDetail();//used for transfering data to getRequiredData function
			vd.setGLCode(trnadv.getGlCode());	
			vd.setDebitAmount(trnadv.getCrAmount());
			trnadv.setTransaxtionParam(getRequiredData(dc,vd,null));
			transactions.add(trnadv);
		}
		}
		else
		{
			Transaxtion trn=new Transaxtion();
			if(ExilPrecision.convertToDouble((passAmount-creditAmount)-tdsAmount,2)>0){
				trn.setGlCode(conPayable);
				trn.setGlName(getAccountName(conPayable,dc));
				trn.setDrAmount("0");
				trn.setCrAmount(ExilPrecision.convertToString((passAmount-creditAmount)-tdsAmount,2));
				trn.setNarration(dc.getValue("voucherHeader_narration"));
				//trn.setTransaxtionParam(requiredList);
				VoucherDetail vd=new VoucherDetail();//used for transfering data to getRequiredData function
				vd.setGLCode(trn.getGlCode());	
				vd.setDebitAmount(trn.getCrAmount());
				trn.setTransaxtionParam(getRequiredData(dc,vd,null));
				transactions.add(trn);
			}
		}
		
		String adjAmt=dc.getValue("worksDetail_adjustmentAmount");
	
		if(adjAmt!=null && !adjAmt.trim().equalsIgnoreCase("") && Double.isNaN(Double.parseDouble(adjAmt)))return;
	}

	public ArrayList getRequiredData(DataCollection dc,VoucherDetail vd,String tdsId) throws TaskFailedException{
		requiredList=new ArrayList();
		ArrayList formList=new ArrayList();
		/*formList.add("fund_id");
		formList.add("relation_id"\);
		formList.add("worksDetail_id");*/
		try{
			Statement st=connection.createStatement();
			ResultSet rset=st.executeQuery("select controlname from screencontrols where screenname='contractorjournal'");
			while(rset.next()){
				formList.add(rset.getString(1));
			}
			rset.close();
			st.close();
		}catch(Exception e){
			throw new TaskFailedException(e.getMessage());
		}
		String formControlName;
		for(int i=0;i<formList.size();i++){
			formControlName=(String)formList.get(i);

			TransaxtionParameter transParam = new TransaxtionParameter();
			if(vd!=null)
			{
				try {
					LOGGER.debug("###### getGLCode() is"+vd.getGLCode());
					transParam.setGlcodeId(cmImpl.getGlCodeId(vd.getGLCode(),connection));

				} catch (Exception e) {
					LOGGER.debug("Exp="+e.getMessage());
					throw new TaskFailedException();
				}
				transParam.setDetailAmt(!vd.getDebitAmount().equals("0")?vd.getDebitAmount():vd.getCreditAmount());
			}else{
				transParam.setGlcodeId(EGovConfig.getProperty("egf_config.xml","ConPayCode","","ContractorCodes"));
				transParam.setDetailAmt(calculateNetAmt(dc));
			}
			transParam.setDetailName((String)dc.getValue(formControlName));
		//	String code=(String)dc.getValue("contractor_code");
			String acKey=(String)dc.getValue("contractor_id");
			transParam.setDetailKey(acKey);	
			LOGGER.debug(" AccountDetailKey is "+transParam.getDetailKey());
			transParam.setDetailName(formControlName);
			transParam.setTdsId(tdsId);
		/*	try{
				LOGGER.info(" i am in CommonsManagerHome");
				commh = (CommonsManagerHome)sl.getLocalHome("CommonsManagerHome");
				comm = commh.create();
			String detid_name=comm.getAccountdetailtypeAttributename(connection, "Creditor");  
			String[] ids=detid_name.split("#");
			transParam.setDetailTypeId(ids[0]);
			}catch(Exception e)
			{
				LOGGER.error("Error While Getting Detail Type");
				LOGGER.debug("Exp="+e.getMessage());
				throw new TaskFailedException(e.getMessage());
				
			}*/
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
public void editVoucher(DataCollection dc,Connection con) throws Exception{
//	int status=0;
	//ArrayList transactions=new ArrayList();
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
         LOGGER.debug("voucherNumber:"+voucherNumber);
         if(!voucherNum.equals(voucherNumber))
         {
             LOGGER.debug("voucherNum NOt Equal");
             if(!cm.isUniqueVN(dc, con,voucherNumber)){
                 throw  new TaskFailedException();
             }
         }
     }
     catch(SQLException sq){throw new TaskFailedException(sq.getMessage());}
	try{

		String vdt=(String)dc.getValue("voucherHeader_voucherDate");
		int CurdateCheck=1;
		
		CurdateCheck=cm.isCurDate(dc,con,vdt);
		
		if(CurdateCheck==0)
			return;
		/*added in egf*/
		//checkValidCodes(dc);
		/*added in egf*/

		if(!isValidAmount(dc)){
			dc.addMessage("exilRPError","Server Validation Failed..Check The Data Sent");
				throw  taskExc;
		}
		getRequiredData(dc,null,null);
		updateInVoucherHeader(dc,con);
		cm.updateVoucherMIS("U",con,VoucherHeaderId,(String)dc.getValue("field_id"), (String)dc.getValue("fundSource_id"),(String)dc.getValue("s"),(String)dc.getValue("subscheme"),dc.getValue("voucherHeader_voucherDate"));
		com.exilant.eGov.src.domain.BillRegisterBean b=new com.exilant.eGov.src.domain.BillRegisterBean();
		if(dc.getValue("billid")!=null && dc.getValue("oldBillid")!=null)
		{
			if(!(dc.getValue("billid").trim().equals(dc.getValue("oldBillid").trim())))
			{
				b.updateStatus("PASSED",con,dc.getValue("billid"));
				b.updateStatus("PENDING",con,dc.getValue("oldBillid"));
			}
		}
		if(dc.getValue("billid")!=null && dc.getValue("billid").trim().length()>0)
		{
			b.updateStatus("PASSED",con,dc.getValue("billid"));
		}
		updateVoucherDetail(dc, transactions);
		updateWorksDetail(dc,con);
		LOGGER.debug("after updateWorksDetail");
		//Update ContractorBillDetail Table After Updating WorkDetail Since it extracts values from ContractorBillDetail table
		updateInAssetStatus(dc);
		LOGGER.debug("after AssetStatus");
		UpdateInContractorBillDetail(dc);
		LOGGER.debug("after UpdateInContractorBillDetail");
//		String supplierBillNumber= dc.getValue("supplierBillDetail_billNumber");
	//	if(supplierBillNumber!=null && !supplierBillNumber.trim().equalsIgnoreCase(""))	updateBillStatus(dc);
		//LOGGER.debug("after updateBillStatus");

		ChartOfAccounts engine = ChartOfAccounts.getInstance();
		Transaxtion txnList[] = new Transaxtion[transactions.size()];
		txnList=(Transaxtion[])transactions.toArray(txnList);
		LOGGER.info("after txnList ######### ----"+txnList.length);
		if(!engine.postTransaxtions(txnList, con, dc))throw taskExc;
		LOGGER.info("after txnList #########12 ----"+txnList.length);
		//updateEGF_MappedTDS(dc);
		deleteFromVoucherDetail(); 
			dc.addValue("voucherHeader_voucherNumber", voucherNumber);
			dc.addMessage("eGovSuccess", "CGNumber is "+cgNum+" and VoucherNumber is "+voucherNumber);
	}catch(Exception e){
		LOGGER.error("Exp="+e.getMessage());
		dc.addMessage("exilRPError","Error in update :"+e.getMessage());
		throw new TaskFailedException();
	}
}
	public void reverse() throws TaskFailedException{
		GlReverser glr=new GlReverser();
		ContractorBillDetail cbd=new ContractorBillDetail();
		WorksDetail wDet=new WorksDetail();
		ResultSet rset;
		double passedAmt=ExilPrecision.convertToDouble(dc.getValue("contractorBillDetail_passedAmount"),2);
		double adjAmt= ExilPrecision.convertToDouble(dc.getValue("worksDetail_adjustmentAmount"),2);
		double pdAmount=0;
		//double pAmount=(passedAmt+adjAmt);
		String vhId=null;
		String sphId=null;
		String billRegId=null;
		try{
			Statement st=connection.createStatement();
			//String cgn=dc.getValue("cgnNum");
			rset=st.executeQuery("select id from voucherHeader where cgn='"+dc.getValue("cgnNum")+"'");
			while(rset.next()){
				vhId=rset.getString(1);
			}
			rset=st.executeQuery("Select id from subledgerPaymentHeader where voucherHeaderId="+vhId);
			while(rset.next()){
				sphId=rset.getString(1);
			}
				if(sphId==null){
					 rset=st.executeQuery("select id,billid from contractorBillDetail where voucherHeaderid="+vhId);
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
					//glr.reverse(connection,dc.getValue("cgnNum"),newVcNo,dc.getValue("voucherHeader_effDate"),cgNum,dc);
					glr.reverse(connection,dc.getValue("cgnNum"),newVcNo,voucherHeader_effDate,cgNum,dc);
					cbd.reverse(connection);
					//reverse asset stauts in egf_asset
					reverseInAssetStatus(dc);
					wDet.setId(dc.getValue("worksDetail_id"));
					wDet.reverseNegative(connection,pdAmount,adjAmt,passedAmt,0);
					if(billRegId!=null){
						reverseBillRegister(dc,connection,billRegId);
						createNewWorkFlow(billRegId);
					}
				}else{
						dc.addMessage("Contractor is already paid");
				}
				st.close();
			}catch(Exception e){
				dc.addMessage("exilRPError",e.getMessage());
				throw new TaskFailedException();
			}
	}
	private void createNewWorkFlow(String billRegId) throws TaskFailedException {
		WorksBillDelegate wDelegate=new WorksBillDelegate();
		WorksBillForm wbForm=new WorksBillForm();
		 
		wbForm.setBillId(billRegId);
		
		try{
			
			LOGGER.info(" i am in CommonsManagerHome");
			WorksBillService worksBillService=(WorksBillService)GetEgfManagers.getWorksBillService();
			EgBillregister billReg=billsService.getBillRegisterById(Integer.valueOf(billRegId));
			wbForm=(WorksBillForm)wDelegate.getEgBillRegister(wbForm);
			String wid=billReg.getWorksdetailId();
			Worksdetail wd =(Worksdetail)worksBillService.getWorksDetailById(Integer.valueOf(wid));
			wbForm.CSId=wd.getRelation().getId().toString();
			int userId=billReg.getCreatedBy().getId();
			wbForm.setUserId(userId);
			try{  
				User user=(User)userService.getUserByID(userId);
				 String  username=user.getUserName();
				 LOGGER.info(username);
				 wbForm.setUserName(username);
			}
			catch(Exception e)
			{
				LOGGER.error("Exp="+e.getMessage());
				throw new TaskFailedException(e.getMessage());
			}
			wDelegate.createWorkFlow(wbForm);
		}catch(Exception e)
		{
			LOGGER.error("Exception in createNewWorkFlow "+e.getMessage());
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
		//if(assetId==null) LOGGER.info("assetId null ");
	//	if(!assetId.equalsIgnoreCase("")){} else LOGGER.info("empty assetId ");
		if(!assetId.equalsIgnoreCase(""))
		{
		rs=st.executeQuery("SELECT assetId FROM contractorbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and worksdetailid='"+worksId+"' and assetid='"+assetId+"'");
		LOGGER.info("SELECT assetId FROM contractorbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and worksdetailid='"+worksId+"' and assetid='"+assetId+"'");

		if(!rs.next())
		{
			LOGGER.debug("This is a first bill for the assetId:"+assetId);
			try{
				st.executeQuery("update eg_asset set statusId="+cm.getEGWStatusId(connection, "ASSET", "CWIP")+" where assetId="+assetId+" and statusid=1");
				LOGGER.debug("updated assetStaus:update eg_asset set statusId="+cm.getEGWStatusId(connection, "ASSET", "CWIP")+" where assetId="+assetId+" and statusid=1");
			}
			catch(SQLException e){ LOGGER.error("updation into asset status failed");
			throw new TaskFailedException();}
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
		LOGGER.debug("updateInAssetStatus started:.....");
		rs=st.executeQuery("SELECT assetid FROM contractorbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and cbd.voucherheaderid='"+vhId+"'" );
		if(rs.next())
		{	oldAssetId=rs.getString(1);
		 	//rs.close();
		 }
		if(oldAssetId==null) oldAssetId="";
		if(assetId==null) oldAssetId="";
		if(!oldAssetId.equalsIgnoreCase(assetId))
		{
			LOGGER.debug("assetId are different");
			rs=st.executeQuery("SELECT assetid FROM contractorbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and cbd.voucherheaderid<>'"+vhId+"'" );
			LOGGER.debug("SELECT assetid FROM contractorbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and cbd.voucherheaderid<>'"+vhId+"'" );
			if(!rs.next())
			{
				try{
					rs=st.executeQuery("update eg_asset set statusId="+cm.getEGWStatusId(connection, "ASSET", "Created")+" where assetId="+oldAssetId);
					LOGGER.debug("updated assetStaus:update eg_asset set statusId="+cm.getEGWStatusId(connection, "ASSET", "Created")+" where assetId="+oldAssetId);
				}
				catch(SQLException e){
					LOGGER.error("updation into asset status bearing assetId "+oldAssetId+" failed");
					throw new TaskFailedException();}
			}
		}

		//AssetStaus astStatus=new AssetStaus();
		//if(assetId==null) LOGGER.info("assetId null ");
	//	if(!assetId.equalsIgnoreCase("")){} else LOGGER.info("empty assetId ");

		if(assetId!=null && !assetId.equalsIgnoreCase(""))
		{
		rs=st.executeQuery("SELECT assetId FROM contractorbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and worksdetailid='"+worksId+"' and assetid='"+assetId+"'");
		LOGGER.info("SELECT assetId FROM contractorbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and worksdetailid='"+worksId+"' and assetid='"+assetId+"'");

		if(!rs.next())
		{
			LOGGER.debug("This is first bill for the assetId:"+assetId);
			try{
				rs=st.executeQuery("update eg_asset set statusId="+cm.getEGWStatusId(connection, "ASSET", "CWIP")+" where assetId="+assetId);
				LOGGER.debug("updated assetStaus:update eg_asset set statusId="+cm.getEGWStatusId(connection, "ASSET", "CWIP")+" where assetId="+assetId);
			}
			catch(SQLException e){ LOGGER.error("updation into asset status failed"); 
			throw new TaskFailedException();}
		}
	}
		rs.close();
		st.close();
		LOGGER.debug("updateInAssetStatus end");
	}

	private void reverseInAssetStatus(DataCollection dc)throws SQLException,TaskFailedException{

		ResultSet rs;
		String assetId=dc.getValue("asset_name");
		String vhId=dc.getValue("voucherHeader_id");
		Statement st=connection.createStatement();
		LOGGER.debug("updation into asset status started with assetId:"+assetId);
		//AssetStaus astStatus=new AssetStaus();
		//if(assetId==null) LOGGER.info("assetId null ");
	//	if(!assetId.equalsIgnoreCase("")){} else LOGGER.info("empty assetId ");
		if(!assetId.equalsIgnoreCase(""))
		{
		rs=st.executeQuery("SELECT assetId FROM contractorbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and cbd.voucherheaderid <>'"+vhId+"' and assetid='"+assetId+"'");
		LOGGER.info("SELECT assetId FROM contractorbilldetail cbd,voucherheader vh  WHERE vh.id=cbd.voucherheaderid AND vh.status=0 and cbd.voucherheaderid <>'"+vhId+"' and assetid='"+assetId+"'");

		if(!rs.next())
		{
			LOGGER.debug("This is a first bill for the assetId:"+assetId);
			try{
				st.executeQuery("update eg_asset set statusId="+cm.getEGWStatusId(connection, "ASSET", "Created")+" where assetId="+assetId);
				LOGGER.debug("updated assetStaus:update eg_asset set statusId="+cm.getEGWStatusId(connection, "ASSET", "Created")+" where assetId="+assetId);
			}
			catch(SQLException e){ LOGGER.error("reverseing into asset status failed"); 
			throw new TaskFailedException();}
		}
	}
		st.close();
	}
	public String calculateNetAmt(DataCollection dc)
	{
		// netAmt=new BigDecimal("0.00");
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
		  		String pendingStatusId = cm.getEGWStatusId(cn, "WORKSBILL","Pending");
		  		bBean.setBillStatusId(pendingStatusId);
		  		bBean.setId(billRegId);
		  		bBean.setLastModifiedDate(cm.getSQLDateTimeFormat(todayTime));
		  		bBean.update(cn);
		  		postInEGActionDetails(dc,billRegId,"Reverse Contractor Journal",cn);//insert into eg_actiondetails 
		  		//postInEGWStatusChange(dc,billRegId,"38","37",cn);//insert into egw_satuschange no applied since it is not updating status id
		  		postInEGWStatusChange(dc,billRegId,cm.getEGWStatusId(cn, "WORKSBILL", "Passed"),pendingStatusId,cn);//insert into egw_satuschange no applied since it is not updating status id
	    }
	  
	    
	  
	/**
		 * This function will insert the egw_satuschange for the
		 * @param dc
		 * @throws SQLException
		 * @throws TaskFailedException
*/

//public boolean postInEGActionDetails(DataCollection dc,String abillId){
public boolean postInEGActionDetails(DataCollection dc,String abillId,String aType,Connection connection)throws TaskFailedException{
LOGGER.debug("Inside postInEGActionDetails");
		boolean result=false;
		ActionDetails acdetail = new ActionDetails();
		todayTime=cm.getCurrentDateTime(connection);

		acdetail.setModuletype("WORKSBILL");
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

egfstatus.setModuletype("WORKSBILL");
egfstatus.setModuleid(abillId);
egfstatus.setFromstatus(afromstatus);
egfstatus.setTostatus(atostatus);

egfstatus.setCreatedby(dc.getValue("egUser_id"));
egfstatus.setLastmodifieddate(cm.getSQLDateTimeFormat(todayTime));

	try	{
	egfstatus.insert(connection);
		result=true;
	}catch(Exception e)	{
		LOGGER.error("Exp="+e.getMessage());
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

	public  void setBillsService(BillsService billsService) {
		this.billsService = billsService;
	}

	
}
