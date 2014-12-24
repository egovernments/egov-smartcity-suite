/*
 * Created on Dec 31, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.master;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
//This class updates data of BankBranch - New screen to BankBranch table. and Account datails to Bank Acoount
public class BankBranchModify extends AbstractTask {

	private DataCollection dc;
	private Connection con=null;
	private Statement st=null;
	private ResultSet rs=null;
	private String bankName,accDetailTypeId,oldGlCodeID="",oldParentID="";
	private int glId;
	private String bankAccountId=null;
	private static final  Logger LOGGER=Logger.getLogger(BankBranchModify.class);
	private static final String BANKBRANCH_BRANCHID="bankBranch_branchId";
	private static final String BANKBRANCH_BRANCHNAME= "bankBranch_branchName";
	private static final String EGUSER_ID="egUser_id";
	private static final String USERMESSAGE ="userMessege";
	private static final String EXILERROR="exilError";
	private static TaskFailedException taskExc;
	
	//private int id;
	public BankBranchModify(){

	}
	public void execute (String nameTask,String dataTask,
						DataCollection dataCollection,
						Connection conn,
						boolean errorOnNoData,
						boolean gridHasColumnHeading, String prefix) throws TaskFailedException {
        LOGGER.info("coming here..execute");
		dc=dataCollection;
		con=conn;
		boolean temp=false;	/*This value is to Check the complition of Function*/
        String[][] accountGrid =(String[][])dc.getGrid("bankAccountGrid");
        String chequetableGrid[][];
        int id=dc.getInt(BANKBRANCH_BRANCHID);
 		/*
		 * The below line is to Get DataBase Date and asigning that to Global Variable dbDate;
		 */
		
		try{

			temp=postInBankBranch();
			if(temp!=true){
				dc.addMessage(USERMESSAGE,"Branch Modification Failed");
				throw new TaskFailedException("postInBankBranch Function Fails");
			}

			temp=false;
            if(accountGrid[0][0]==null || accountGrid[0][0].equalsIgnoreCase(""))
            {
                verifyUniqueness(con,id,accountGrid,2);
            }
            
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
//			adding method for adding chequeno range--rashmi
			/*temp=false; // commented by msahoo - removed cheque details from bank modify.
            chequetableGrid=(String [][])dc.getGrid("chequetableGrid");
            if(!(chequetableGrid[0][1]==null || chequetableGrid[0][1].equalsIgnoreCase("") ||chequetableGrid[0][0].length()==0))
            {
    			temp=postInAccountCheques();
    			if(!temp)
    				return;
            }*/
			dc.addMessage("userSuccess",dc.getValue(BANKBRANCH_BRANCHNAME).toString(),"Branch Modified Successfully");
			ChartOfAccounts.getInstance().reLoadAccountData();
		}catch(Exception ex){
			dc.addMessage("userFailure","Modification Failure");
			throw taskExc;
		}
	}
	
	private boolean postInBankBranch() throws Exception{
		/*
		 * This Function Selecte the Id from Bank table for given BankCode
		 * And Also Insert The table BankBranch with data entered in the Screen
		 * Bank Branch - Add
		 */
		BankBranch table = new BankBranch();
		st=con.createStatement();
		String bank_code=dc.getValue("bank_code").toString();
		if(bank_code ==null || bank_code.length()==0){
			dc.addMessage(USERMESSAGE,"Bank Code is Must Required Field");
			return false;
		}

		rs=st.executeQuery("SELECT ID,NAME FROM Bank WHERE Code='"+bank_code+"'");
		if(!rs.next()){
			dc.addMessage(USERMESSAGE,"Invalid Bank code");
			throw new TaskFailedException("BankId Not Found");
		}

		String bankId=Integer.toString(rs.getInt(1));
		bankName=rs.getString(2); /*egf*/
		rs.close();
		st.close();


		String bankBranch_branchId=dc.getValue(BANKBRANCH_BRANCHID);
		if(bankBranch_branchId!=null && bankBranch_branchId.length()!=0)
			table.setId(bankBranch_branchId);
		else{
			dc.addMessage(USERMESSAGE,"BranchId is must required field");
			return false;
		}

		String bankBranch_branchCode=dc.getValue("bankBranch_branchCode").toString();
		if(bankBranch_branchCode!=null && bankBranch_branchCode.length()!=0)
			table.setBranchCode(bankBranch_branchCode);
		else{
			dc.addMessage(USERMESSAGE,"Please Fill the Branch Code");
			return false;
		}

		String bankBranch_branchName=dc.getValue(BANKBRANCH_BRANCHNAME).toString();
		if(bankBranch_branchName!=null && bankBranch_branchName.length()!=0)
			table.setBranchName(bankBranch_branchName);
		else{
			dc.addMessage(USERMESSAGE,"Please Fill the Branch Name");
			return false;
		}

		String bankBranch_branchAddress1=dc.getValue("bankBranch_branchAddress1").toString();
		if(bankBranch_branchAddress1!=null && bankBranch_branchAddress1.length()!=0)
			table.setBranchAddress1(bankBranch_branchAddress1);
		else{
			dc.addMessage(USERMESSAGE,"Please Fill the Branch Address");
			return false;
		}

		String bankBranch_branchAddress2=dc.getValue("bankBranch_branchAddress2").toString();
		if(bankBranch_branchAddress2!=null && bankBranch_branchAddress2.length()!=0)
			table.setBranchAddress2(bankBranch_branchAddress2);
		else	table.setBranchAddress2(" ");


		String bankBranch_branchCity=dc.getValue("bankBranch_branchCity").toString();
		bankBranch_branchCity=formatString(bankBranch_branchCity);
		if(bankBranch_branchCity!=null && bankBranch_branchCity.length()!=0)
			table.setBranchCity(bankBranch_branchCity);
		else	table.setBranchCity(" ");


		String bankBranch_branchPin=dc.getValue("bankBranch_branchPin").toString();
		if(bankBranch_branchPin!=null && bankBranch_branchPin.length()!=0)
			table.setBranchPin(bankBranch_branchPin);
		else	table.setBranchPin(" ");

		String bankBranch_branchPhone=dc.getValue("bankBranch_branchPhone").toString();
		if(bankBranch_branchPhone!=null && bankBranch_branchPhone.length()!=0)
			table.setBranchPhone(bankBranch_branchPhone);
		else	table.setBranchPhone(" ");

		String bankBranch_branchFax=dc.getValue("bankBranch_branchFax").toString();
		if(bankBranch_branchFax!=null && bankBranch_branchFax.length()!=0)
			table.setBranchFax(bankBranch_branchFax);
		else	table.setBranchFax(" ");

		table.setBankId(bankId);

		String bankBranch_contactPerson=dc.getValue("bankBranch_contactPerson").toString();
		bankBranch_contactPerson=formatString(bankBranch_contactPerson);
		if(bankBranch_contactPerson!=null && bankBranch_contactPerson.length()!=0)
			table.setContactPerson(bankBranch_contactPerson);
		else	table.setContactPerson(" ");

		String bankBranch_narration=dc.getValue("bankBranch_narration").toString();
		bankBranch_narration=formatString(bankBranch_narration);
		if(bankBranch_narration!=null && bankBranch_narration.length()!=0)
			table.setNarration(bankBranch_narration);
		else	table.setNarration(" ");

		String isActive=dc.getValue("bankBranch_isActive").toString();
		if(isActive!=null &&(isActive.compareToIgnoreCase("ON")==0 || isActive.equals("1")))	isActive="1";
		else	isActive="0";
		table.setIsActive(isActive);
		table.setModifiedBy(dc.getValue(EGUSER_ID));

		table.update(con);

		return true;
	}
//	added by rashmi
	private boolean postInAccountCheques() throws Exception{
		AccountChequeRange acr = new AccountChequeRange();
		String chequetableGrid[][];
		chequetableGrid=(String [][])dc.getGrid("chequetableGrid");
		LOGGER.debug("after chequetableGrid+++"+chequetableGrid.length);
		if(chequetableGrid[0][0]==null ||chequetableGrid[0][0].equalsIgnoreCase("") ||chequetableGrid[0][0].length()==0){
			return false;
		}
   		LOGGER.debug("delete EGF_ACCOUNT_CHEQUES where bankaccountid="+bankAccountId );
   		st=con.createStatement();
   		st.execute("delete EGF_ACCOUNT_CHEQUES where bankaccountid="+bankAccountId);
   		st.close();
		for(int i=0;i<chequetableGrid.length;i++){
			if((chequetableGrid[i][0].length()>0 && chequetableGrid[i][1].length()==0 )|| (chequetableGrid[i][0].length()==0 && chequetableGrid[i][1].length()> 0 )){
				dc.addMessage(EXILERROR,"Enter the from cheque number and to cheque number.");
				return false;
			}
			if(chequetableGrid[i][0].length()>0 && chequetableGrid[i][1].length()>0){
				if(Integer.parseInt(chequetableGrid[i][0])>Integer.parseInt(chequetableGrid[i][1])){
					dc.addMessage(EXILERROR," From cheque number should be less than To cheque number.");
					return false;
					}
			}
		acr.setBankAccountID(bankAccountId);
		acr.setFromChequeNumber(chequetableGrid[i][0]);
		acr.setToChequeNumber(chequetableGrid[i][1]);
		if(!chequetableGrid[i][3].equals(""))
			acr.setIsExhausted(chequetableGrid[i][3]);
		else
			acr.setIsExhausted("0");
		acr.setNextChqNo(chequetableGrid[i][4]);
		
		/*hardcoding to 1 for timebeing as only cash branch is present*/
		LOGGER.debug("Department received for row number "+ i+1 +"is "+ chequetableGrid[i][5]);
		if(!chequetableGrid[i][5].equals(""))
			acr.setIsAllotteTo(chequetableGrid[i][5]);
		else if(chequetableGrid[i][0].length()>0 && chequetableGrid[i][1].length()>0)
		{
			dc.addMessage(EXILERROR," Please select a department");
			return false;
		}

		if(chequetableGrid[i][2].length()>0){
	   		try
	   		{
	   			String ReceivedDate="";
	   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
				String vdt=(String)chequetableGrid[i][2];
				ReceivedDate = formatter.format(sdf.parse(vdt));
				acr.setReceivedDate(ReceivedDate);
				acr.setCreatedDate(formatter.format(new Date()));
				acr.setLastModifiedDate(formatter.format(new Date()));
	
	   		}
	   		catch(Exception e){
	   			LOGGER.error("error in the date formate "+e);
	   			throw taskExc;
	   		}
		}
		else{
			EGovernCommon commonmethods = new EGovernCommon();
			String created = commonmethods.getCurrentDate(con);
			try
			{
	   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
				created = formatter.format(sdf.parse(created));
				acr.setReceivedDate(created);
				acr.setCreatedDate(formatter.format(new Date()));
				acr.setLastModifiedDate(formatter.format(new Date()));
				
			}
			catch(Exception e){
				LOGGER.error("error in the date formate "+e);
				throw taskExc;
			}
		}
		String fromcheno=chequetableGrid[i][0];
		String tocheno=chequetableGrid[i][1];
		acr.setCreatedBy(dc.getValue("egUser_id"));
		acr.setLastModifiedBy(dc.getValue("egUser_id"));
		/*if(!isUniqueChequeRange(dc, con,bankAccountId,fromcheno,tocheno)){
			throw  taskExc;
	 	}*/
		try{
			if(fromcheno.length()>0 && tocheno.length()>0)
				
				acr.insert(con);
		}catch(SQLException sqlEx){
			dc.addMessage(EXILERROR,"insertion failed in AccountChequeRange");
			throw taskExc;
		}
	}
	return true;
}

	private boolean postInBankAcount() throws Exception {
		/*
		 * This Function Selects ID from bankBranch Table for the Given BankCode
		 * Inserts that ID as BranchId in BankAccount table along with Data Enter in
		 * Bank Branch - New; Grid(accountGrid)
		 */
		String newGLCode="",coaID="";
		String accType="",glCode="",oldFundId="";
		int fundId,glcodeId;
		BankAccount table=new BankAccount();
		String[][] accountGrid =(String[][])dc.getGrid("bankAccountGrid");
		for(int i=0;i<accountGrid.length;i++){
			String accountStatus="0";
			if(accountGrid[i][0]!=null && accountGrid[i][0].length()!=0){
				oldParentID="";oldGlCodeID="";
				/*egf*/
				/***Commenting for time being-Elzan******/
					//if(isAlreadyPosted(accountGrid[i][0])){
					//	throw new TaskFailedException();
					//}
				/*egf*/

				//New
				try{
					st=con.createStatement();

					rs=st.executeQuery("SELECT ID,ACCOUNTTYPE,GLCODEID,FUNDID,ACCOUNTNUMBER,PAYTO  " +
							   "FROM BankAccount " +
							   "WHERE ID='"+accountGrid[i][0]+"'");
					if(!rs.next()){

						dc.addMessage(USERMESSAGE,"Invalid Bank code");
						throw new TaskFailedException("BankId Not Found");
					}
					
					accType=rs.getString(2);
					glcodeId=rs.getInt(3);
					fundId=rs.getInt(4);
					oldFundId=rs.getString(4);
					glId=glcodeId;
				}
				catch(Exception e)
				{
					LOGGER.error("Exception in postInBankAcount"+e.getMessage());
					return false;
				}
				finally
				{
					try
					{
						rs.close();
						st.close();
					}
					catch(Exception e)
					{LOGGER.error("Exception in postInBankAcount in finally"+e.getMessage());}

				}
				if(accountGrid[i][0]!=null && accountGrid[i][0].length()!=0){
					if((!accountGrid[i][7].equals(oldFundId)) || (!accountGrid[i][2].equals(accType))){
						if(isAlreadyPosted(accountGrid[i][0])){
							throw new TaskFailedException();
						}
					}
				}

				try{
					st=con.createStatement();
					LOGGER.debug("SELECT GLCODE FROM CHARTOFACCOUNTS WHERE ID='"+glcodeId+"'");
					rs=st.executeQuery("SELECT GLCODE FROM CHARTOFACCOUNTS WHERE ID='"+glcodeId+"'");
					if(!rs.next()){
						dc.addMessage(USERMESSAGE,"Invalid Bank code");
						throw new TaskFailedException("BankId Not Found");
					}
					glCode=rs.getString(1);
					}
				catch(Exception e)
				{
					LOGGER.error("Exception in postInBankAcount"+e.getMessage());
					return false;
				}
				finally
				{
					try
					{
						rs.close();
						st.close();
					}
					catch(Exception e)
					{LOGGER.error("Exception in postInBankAcount finallay"+e.getMessage());}

				}
				//New End
				/*egf*/
					if(accountGrid[i][1]!=null && accountGrid[i][1].length()!=0 && accountGrid[i][2]!=null &&
						accountGrid[i][2].length()!=0 && accountGrid[i][4]!=null && accountGrid[i][4].length()!=0){
			/*egf*/
						newGLCode="";coaID="";
						if(!oldParentID.equalsIgnoreCase(accountGrid[i][5])){
							if(!((accountGrid[i][2].equalsIgnoreCase(accType)) && (accountGrid[i][7].equalsIgnoreCase(Integer.toString(fundId)))))
							{
								String code=EGovConfig.getProperty("egf_config.xml","glcodeMaxLength","","AccountCode");
								newGLCode=prepareBankAccCode(con,accountGrid[i][5],code);
								st = con.createStatement();
								rs = st.executeQuery("SELECT * FROM chartofaccounts WHERE glcode= '" + newGLCode+"'");
								if(rs.next()){
									
									throw new Exception("bank account modification failed,can not  create account code,please contact adminstrator at egov.");
								}
					   			removeAccFromCOA(glcodeId);
							}
							else
							{
								newGLCode = glCode;

							}
							//removeAccFromCOA(glcodeId);
							coaID=postInChartOfAccounts(newGLCode,accountGrid[i][5],accountGrid[i][1]);
						}else{
							coaID=oldGlCodeID;
						}
						/*egf*/
						String bankBranch_branchId=dc.getValue(BANKBRANCH_BRANCHID);
						if(bankBranch_branchId!=null && bankBranch_branchId.length()!=0)
							table.setBranchId(bankBranch_branchId);
						table.setAccountNumber(accountGrid[i][1]);
						table.setAccountType(accountGrid[i][2]);
						table.setNarration(formatString(accountGrid[i][3]));
						if(accountGrid[i][4].compareToIgnoreCase("ON")==0 || accountGrid[i][4].equals("1"))
							accountStatus="1";
						table.setIsActive(accountStatus);
						table.setModifiedBy(dc.getValue(EGUSER_ID));
						table.setId(accountGrid[i][0]);
						table.setGlcodeId(coaID);
						table.setFundID(accountGrid[i][7]);
						LOGGER.info("PayTo-------->"+accountGrid[i][8] );					
						table.setPayTo(accountGrid[i][8]);
						table.setType(accountGrid[i][9]);
						try{
						table.update(con);
						
						}
						catch(Exception e)
						{
							LOGGER.error("error in update"+e.getMessage());
							dc.addMessage("exilRPError","Error in update bankaccount"+e.getMessage());
						}
						String name=bankName+" "+dc.getValue(BANKBRANCH_BRANCHNAME).toString()+" "+accountGrid[0][1];
						updateVoucherDetail(con,name,glId);
						bankAccountId=String.valueOf(table.getId());
					//	String detailKey=accountGrid[i][0];
				}
				else continue;
			}else{
				if(accountGrid[i][1]!=null && accountGrid[i][1].length()!=0 && accountGrid[i][2]!=null &&
						accountGrid[i][2].length()!=0 && accountGrid[i][4]!=null && accountGrid[i][4].length()!=0){ 
					//LOGGER.debug("TwoglCode:"+glCode);
					String code=EGovConfig.getProperty("egf_config.xml","glcodeMaxLength","","AccountCode");
		   			LOGGER.debug("code:"+code);
		   			newGLCode=prepareBankAccCode(con,accountGrid[i][5],code);
					coaID=postInChartOfAccounts(newGLCode,accountGrid[i][5],accountGrid[i][1]);
					if(accountGrid[i][4].compareToIgnoreCase("ON")==0 || accountGrid[i][4].equals("1"))
						accountStatus="1";

						String bankBranch_branchId=dc.getValue(BANKBRANCH_BRANCHID);
						if(bankBranch_branchId!=null && bankBranch_branchId.length()!=0)
							table.setBranchId(bankBranch_branchId);

						table.setAccountNumber(accountGrid[i][1]);
						table.setAccountType(accountGrid[i][2]);
						String accountGridnarration=formatString(accountGrid[i][3]);
						table.setNarration(accountGridnarration);
						table.setIsActive(accountStatus);
						table.setModifiedBy(dc.getValue(EGUSER_ID));
						table.setFundID(accountGrid[i][7]);
						table.setPayTo(accountGrid[i][8]);
						table.setType(accountGrid[i][9]);
						if(coaID!=null && coaID.length()>0)
							table.setGlcodeId(coaID);
						else
							table.setGlcodeId("0");

						table.insert(con);
						bankAccountId=String.valueOf(table.getId());
				}
				else continue;
			}
		}
		return true;
	}

	public String formatString(String data){
		String formattedData=data.replaceAll("'","''");
		LOGGER.debug(formattedData);
		return formattedData;
	}

	/**
	 * This function will check if the account details can be modified.
	 * If opening balance>0 then it should restrict modification
	 * Restrict modification if valid vouchers are present.
	 * @param bankAccID
	 * @return
	 * @throws Exception
	 */
	public boolean isAlreadyPosted(String bankAccID) throws  Exception{
		boolean isPosted=false;
		Statement st=con.createStatement();
        BigDecimal netAmt=BigDecimal.ZERO;
       	ResultSet rset=st.executeQuery("select gl.glcode,gl.glcodeid from bankaccount ba,generalledger gl, VOUCHERHEADER vh  where ba.glcodeid=gl.glcodeid and ba.id="+bankAccID+" AND gl.voucherheaderid=vh.id and vh.STATUS<>4");
        if(rset.next()){ //if it is  posted get1 old glcodeid from generalledger table as they might have changed in modify
			dc.addMessage("exilRPError","Code :"+rset.getString(1)+" Is Used For Posting Cant Modify");
			oldGlCodeID=rset.getString(2);
			isPosted=true;
		}
        else{ //if it is not posted 
			//LOGGER.debug("inside the isAlreadyPosted else cond");
            rset.close();
            rset=st.executeQuery("SELECT SUM(openingdebitbalance)+SUM(openingcreditbalance),ba.accountnumber as accno FROM bankaccount ba,transactionSummary ts"+
                    " WHERE ba.glcodeid=ts.glcodeid AND ba.id="+bankAccID+" GROUP BY ba.accountnumber");
            if(rset.next())
                netAmt=rset.getBigDecimal(1);
            if(netAmt.compareTo(BigDecimal.ZERO)==1){ //if it is  posted find out whether OB has been set
                dc.addMessage("exilRPError","Cannot Modify A/C "+rset.getString(2)+" as opening balance is present.");
                isPosted=true;
            }
            else{//if it is neither posted nor set Opening Balance get old glcodeid from bankaccount table as they might have changed in modify
    			rset.close();
    			rset=st.executeQuery("select ba.glcodeid,ca.parentid from bankaccount ba,chartofaccounts ca  where ba.glcodeid=ca.id and ba.id="+bankAccID);
    			if(rset.next()){
    				oldGlCodeID=rset.getString(1);
    				oldParentID=rset.getString(2);
    			}
            }
		}
        st.close();
		return isPosted;
	}
	public void removeFromCOA(String glCodeID,String detTypeID,String defKeyID) throws Exception{
		Statement st=con.createStatement();
		st.execute("delete chartofaccountdetail where glcodeid="+glCodeID+" and detailtypeid="+detTypeID);
		st.execute("delete chartofaccounts where id="+glCodeID);
		st.close();
	}
	
	public void removeAccFromCOA(int id)throws TaskFailedException
	{
		try{
			st=con.createStatement();
			st.execute("delete chartofaccounts where id="+id);

			}
		catch(Exception e)
		{
			LOGGER.error("Error in removeAccFromCOA :"+e.getMessage());
			throw taskExc;
		}
		finally
		{
			try
			{
				rs.close();
				st.close();
			}
			catch(Exception e)
			{LOGGER.error("Error in removeAccFromCOA finally"+e.getMessage());}

		}
	}

	public String prepareBankAccCode(Connection con,String accID,String code) throws Exception{
		String glCode="";
		Long glcode;
         Long tempCode=0L;
		Statement st=con.createStatement();
		String query = "select glcode from chartofaccounts where glcode like (select glcode from chartofaccounts where id="+accID+") order by glcode desc";
		ResultSet rset=st.executeQuery(query);
		while(rset.next()){
			glCode=rset.getString(1);
			}
		String subminorvalue=EGovConfig.getProperty("egf_config.xml","subminorvalue","","AccountCode");
		glCode=glCode.substring(0,Integer.parseInt(subminorvalue));
		String query1= "select glcode from chartofaccounts where glcode like '"+glCode+"'|| '%' order by glcode desc";
		rset=st.executeQuery(query1);
		if(rset.next()){
			glCode=rset.getString(1);
		}
		String zero=EGovConfig.getProperty("egf_config.xml","zerofill","","AccountCode");
		if(glCode.length()== Integer.parseInt(code)){
		glcode = Long.parseLong(glCode);
		tempCode = glcode + 1;
		}
		else{
			glCode= glCode + zero ;
			glcode = Long.parseLong(glCode);
			tempCode = glcode + 1;
		}
		glCode =Long.toString(tempCode);
		LOGGER.debug("glCode ----->"+glCode);
		return glCode;
	}
	public String postInChartOfAccounts(String glCode,String parentId,String accNumber) throws Exception{
		ChartOfAccts chart = new ChartOfAccts();
		chart.setGLCode(glCode);
		chart.setName(bankName+" "+dc.getValue(BANKBRANCH_BRANCHNAME)+" "+accNumber);
		chart.setDescription(bankName+" "+dc.getValue(BANKBRANCH_BRANCHNAME)+" "+accNumber);
		chart.setIsActiveForPosting("1");
		chart.setParentId(parentId);
		chart.setType("A");
		chart.setClass("5"); //This is the leaf level number.
		chart.setClassification("4");
		chart.setModifiedBy(dc.getValue(EGUSER_ID));
		st = con.createStatement();
		rs = st.executeQuery("SELECT * FROM chartofaccounts WHERE glcode= '" + glCode+"'");
		if(rs.next()){
			//LOGGER.debug("id is present ie inside the update function");
			chart.setId(rs.getInt("id") + "");
			chart.update(con);
			//throw new Exception("bank account modification failed,can not  create account code,please contact adminstrator at egov.");
		}
		else {
			//LOGGER.debug("id is not present ie inside the insert function");
			chart.insert(con);
		}
		//chart.insert(con);
		return String.valueOf(chart.getId());
	}
	public void postInCOADetail(String coaID,String defaultID,String type)throws Exception{
		ChartOfAccountDetail cDet = new ChartOfAccountDetail();
		cDet.setDetailTypeId(accDetailTypeId);
		cDet.setGLCodeId(coaID);
		if(type.equals("insert")){
			cDet.insert(con);
		}else{
			cDet.setId(getCOADetID(coaID,accDetailTypeId,defaultID));
			cDet.update(con);
		}
	}

	// check for uniqueness --by rashmi
	public boolean isUniqueChequeRange(DataCollection dc,Connection conn,String bankaccid,String fromcheno, String tocheno )
							throws TaskFailedException{
		boolean isUnique = false;
		try{
			st = conn.createStatement();
			LOGGER.debug("SELECT id FROM EGF_ACCOUNT_CHEQUES WHERE BANKACCOUNTID = '" + bankaccid + "' AND ((FROMCHEQUENUMBER<='" + fromcheno + "' AND '"+ fromcheno + "'<= TOCHEQUENUMBER) or ( (FROMCHEQUENUMBER<='" + tocheno + "' AND '"+ tocheno + "'<= TOCHEQUENUMBER)");
			ResultSet rs = st.executeQuery("SELECT id FROM EGF_ACCOUNT_CHEQUES WHERE BANKACCOUNTID = '" + bankaccid + "' AND " +
					"((FROMCHEQUENUMBER<='" + fromcheno + "' AND '"+ fromcheno + "'<= TOCHEQUENUMBER) or (FROMCHEQUENUMBER<='" + tocheno 
					+ "' AND '"+ tocheno + "'<= TOCHEQUENUMBER)) AND length(FROMCHEQUENUMBER)=length('"+fromcheno+"') and length(TOCHEQUENUMBER)=length('"+fromcheno+"')");
			//LOGGER.debug("after the query--isUniqueChequeRange");
			if(rs.next()) dc.addMessage("exilRPError", "duplicate ChequeRange");

			else isUnique = true;
			rs.close();
			st.close();
		}catch(SQLException ex){
			dc.addMessage("exilRPError", "DataBase Error(isUniqueChequeRange) : " + ex.toString());
			throw taskExc;
		}
		return isUnique;
	}
	public String getCOADetID(String coaID,String accDetailTypeId,String defaultID) throws Exception{
		Statement st=con.createStatement();
		ResultSet rset=st.executeQuery("select id from chartofaccountdetail where glcodeid="+coaID+" and detailtypeid="+accDetailTypeId);
		if(rset.next()){ //if it is  posted get old glcodeid from generalledger table as they might have changed in modify
			st.close();
			return rset.getString(1);
		}
		st.close();
		return null;
	}
     public void verifyUniqueness(Connection con,int field1,String field2[][],int type)throws TaskFailedException,SQLException
        {
         LOGGER.info("verifyUniqueness:");
            Statement st=con.createStatement();
            ResultSet rs=null;
            String accNo="";
            String brchName="";
            if(type==1)
            {
                rs=st.executeQuery("select BranchName from bankbranch where branchid="+field1);
                if(rs.next())
                {
                    brchName=rs.getString(1);
                    if(brchName.equalsIgnoreCase(field2[0][0]))
                    {
                        dc.addMessage(EXILERROR,": Duplicate Branch Name :"+field2[0][0]);
                        throw new TaskFailedException();
                    }
                }
            }
            if(type==2)
            {
                rs=st.executeQuery("select accountNumber from bankaccount where branchid="+field1);
                while(rs.next())
                {
                        accNo=rs.getString(1);
                        for(int i=0;i<field2.length;i++)
                        {
                            LOGGER.info("accountNumber:"+accNo+"ACC: "+field2[i][1]);
                                if(accNo.equalsIgnoreCase(field2[i][1]))
                                {
                                    dc.addMessage(EXILERROR,": "+field2[i][1]+" is a Duplicate Account Number");
                                    throw new TaskFailedException();
                                }
                        }
                }
                rs.close();
            }
        }
     public void updateVoucherDetail(Connection con,String name,int glId) throws TaskFailedException,SQLException{
     	try{
     	Statement st=con.createStatement();
         String query="update voucherdetail set accountname='"+name+"' where glcode=(select glcode from chartofaccounts where id="+glId+")";
       LOGGER.debug("voucherdetail query:"+query);
        st.executeUpdate(query);
     	
     }catch(SQLException ex){
     	dc.addMessage(EXILERROR,":Voucherdetail updation failed");
     	 throw taskExc;
     }
    }
    
     
}
