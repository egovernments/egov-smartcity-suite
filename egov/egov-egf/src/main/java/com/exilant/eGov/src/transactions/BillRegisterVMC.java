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
/* Created on Jan, 2006
*
* TODO To change the template for this generated file go to
* Window - Preferences - Java - Code Style - Code Templates
*/
package com.exilant.eGov.src.transactions;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.utils.FinancialConstants;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.BillRegisterBean;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

/**
* @author Iliyaraja
*
* TODO To change the template for this generated type comment go to
* Window - Preferences - Java - Code Style - Code Templates
*/
//This class updates data of BankBranch - New screen to BillRegister table. and Account datails to Bank Acoount
public class BillRegisterVMC extends AbstractTask {
	private final static Logger LOGGER=Logger.getLogger(BillRegisterVMC.class);
	private DataCollection dc;
	private Connection con=null;
	PreparedStatement pstmt=null;
	String today;
//	private int id;
	private TaskFailedException taskExc;
	public BillRegisterVMC(){	}



	public void execute (String nameTask,
						String dataTask,
						DataCollection dataCollection,
						Connection conn,
						boolean errorOnNoData,
						boolean gridHasColumnHeading, String prefix) throws TaskFailedException {

		this.dc=dataCollection;
		this.con=conn;
		try{
			if(LOGGER.isInfoEnabled())     LOGGER.info("MODE OF EXECUTION VALUE IS:"+dc.getValue("modeOfExec"));
			 if(dc.getValue("modeOfExec").equalsIgnoreCase("new")){
				postInBillRegister();
				dc.addMessage("userSuccess",dc.getValue("bill_Number").toString(),"BillRegister Inserted Successfully");
			 }

			 else if(dc.getValue("modeOfExec").equalsIgnoreCase("modify")){
				updateBillRegister();
				dc.addMessage("userSuccess",dc.getValue("bill_Number").toString(),"BillRegister Updated Successfully");
			 }
		}catch(Exception ex){
			LOGGER.error("error inside execute"+ex.getMessage(), ex);
			dc.addMessage("exilRPError","Transaction Failed :"+ex.getMessage());
    		throw taskExc;
		}

	} // execute method end

	/**
	 * This function will insert a record to the bill register
	 * @throws TaskFailedException
	 */
	private void postInBillRegister() throws TaskFailedException,Exception
	{
		EGovernCommon commonmethod = new EGovernCommon();
		CommonMethodsImpl cmImpl = new CommonMethodsImpl();

		today=commonmethod.getCurrentDateTime();
	//	boolean answer=false;
		BillRegisterBean billBean = new BillRegisterBean();
		//billBean.setId(String.valueOf(PrimaryKeyGenerator.getNextKey("EG_BILLREGISTER")));
		//billBean.setBillNumber(dc.getValue("bill_Number"));
		billBean.setBillNumber(cmImpl.getTxnNumber("BILL",dc.getValue("bill_Date")));

      	billBean.setBillDate(commonmethod.getSQLDateFormat(dc.getValue("bill_Date")));
      	billBean.setBillAmount(Double.parseDouble(dc.getValue("bill_Amount")));
		if(!dc.getValue("field_Name").equals(""))
			billBean.setFieldId(dc.getValue("field_Name"));
		billBean.setExpenditureType(dc.getValue("expenditure_Type"));
		if(!dc.getValue("expenditure_Type").equalsIgnoreCase(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT) && !dc.getValue("WPCodeList").equals(""))
			billBean.setWorksDetailId(dc.getValue("WPCodeList"));
	    if(!dc.getValue("advance_Adjusted").equals("") && !dc.getValue("expenditure_Type").equals(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT)){
	    	checkAdvanceAdjusted();
	    	if(LOGGER.isInfoEnabled())     LOGGER.info(" Inside set advance "+dc.getValue("advance_Adjusted")+"   "+dc.getValue("expenditure_Type"));
	  		billBean.setAdvanceAdjusted(Double.parseDouble(dc.getValue("advance_Adjusted")));
	    }
		billBean.setBillStatus("PENDING");
		if(!dc.getValue("bill_Narration").equals(""))
			billBean.setBillNarration(dc.getValue("bill_Narration"));
	    billBean.setBillType(dc.getValue("bill_Type"));
		billBean.setPassedAmount(Double.parseDouble(dc.getValue("passed_Amount")));
		billBean.setCreatedby(Integer.parseInt(dc.getValue("egUser_id")));
		billBean.setCreatedDate(commonmethod.getSQLDateTimeFormat(today));

		/*if(!checkBillNumberUniqueInsert())
		{
				dc.addMessage("userFailure","Duplicate BillNumber Not Allowed");
				throw new TaskFailedException();
		}*/
		billBean.insert(con);

		String createdBillNo=billBean.getBillNumber();
		if(LOGGER.isInfoEnabled())     LOGGER.info("CREATED BILL NO IS---->"+createdBillNo);
 		dc.addValue("bill_Number",createdBillNo);

	}  // Insert method ended.

	/**
	 * This function will update a bill reguster
	 * @throws TaskFailedException
	 */
	private void updateBillRegister() throws TaskFailedException
	{
		EGovernCommon commonmethod = new EGovernCommon();
		CommonMethodsImpl cmImpl = new CommonMethodsImpl();
		today=commonmethod.getCurrentDateTime();
		if(LOGGER.isInfoEnabled())     LOGGER.info("Update ID Value is:"+(dc.getValue("bill_Id")));
//		boolean answer=false;
		BillRegisterBean billBean = new BillRegisterBean();
		billBean.setId(dc.getValue("bill_Id"));

		//billBean.setBillNumber(dc.getValue("bill_Number"));

		// In Modify mode if may change Bill Date-validation start
		try
		{
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				String newBillDate=dc.getValue("bill_Date");
				String oldBillDate=dc.getValue("oldbill_Date");
				Date d1;
				Date d2;

				 d1 = df.parse(newBillDate);
				 d2 = df.parse(oldBillDate);

				 if (d1.equals(d2))
				 {
				 	if(LOGGER.isInfoEnabled())     LOGGER.info("BOTH DATE ARE EQUAL");
		         billBean.setBillNumber(dc.getValue("bill_Number"));
			 	 } // main if
				 else
				 {
					 	if(LOGGER.isInfoEnabled())     LOGGER.info("BOTH DATE ARE NOT EQUAL");
					 	String newDate=formatter.format(d1);
					 	String oldDate=formatter.format(d2);

					 	String fisPeriodId1=cmImpl.getFiscalPeriod(newDate,con);
					 	String fisPeriodId2=cmImpl.getFiscalPeriod(oldDate,con);

					 	if(!fisPeriodId1.equals(fisPeriodId2))
					 	{
					 		if(LOGGER.isInfoEnabled())     LOGGER.info("If Fiscal period is different --->Generate new Bill Number");

					 		billBean.setBillNumber(cmImpl.getTxnNumber("BILL",dc.getValue("bill_Date")));

					 		String billNoNew=billBean.getBillNumber();
					 		dc.addValue("bill_Number",billNoNew);

					 	}
					 	if(fisPeriodId1.equals(fisPeriodId2))
					 	{

					 		String monthNew[] = newBillDate.split("/");
					 		if(LOGGER.isInfoEnabled())     LOGGER.info(" The monthNew :"+monthNew[1]);

					 		String monthOld[] = oldBillDate.split("/");
					 		if(LOGGER.isInfoEnabled())     LOGGER.info(" The monthOld :"+monthOld[1]);

							 		if(monthNew[1].equals(monthOld[1]))
							 		{
							 			if(LOGGER.isInfoEnabled())     LOGGER.info("If BOTH DATE ARE NOT EQUAL BUT Fiscal period and Month are Same--->Same Bill Number");
							 			billBean.setBillNumber(dc.getValue("bill_Number"));
							 		}
							 		else
							 		{
							 			if(LOGGER.isInfoEnabled())     LOGGER.info("If Fiscal period is Same but Month is different--->Reformat Bill Number");

							 			String billNo=dc.getValue("bill_Number");
							 			String splitArray[]=billNo.split("/");
							 			if(LOGGER.isInfoEnabled())     LOGGER.info(" txnType splitArray[0]:"+splitArray[0]);
							 			if(LOGGER.isInfoEnabled())     LOGGER.info(" FinYear splitArray[2]:"+splitArray[2]);

							 			String reFormatBillNo=splitArray[0]+"/"+monthNew[1]+"/"+splitArray[2];
							 			billBean.setBillNumber(reFormatBillNo);

							 			dc.addValue("bill_Number",reFormatBillNo);
							 		}
					 	}

				 } // main else
		}
		catch(Exception e)
		{
			LOGGER.error("Exp="+e.getMessage(), e);
			throw new TaskFailedException(e.getMessage());
		}//validation End

        billBean.setBillDate(commonmethod.getSQLDateFormat(dc.getValue("bill_Date")));
		billBean.setBillAmount(Double.parseDouble(dc.getValue("bill_Amount")));
		if(!dc.getValue("field_Name").equals(""))
			billBean.setFieldId(dc.getValue("field_Name"));
		billBean.setExpenditureType(dc.getValue("expenditure_Type"));
		if(!dc.getValue("expenditure_Type").equals(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT) && !dc.getValue("WPCodeList").equals(""))
			billBean.setWorksDetailId(dc.getValue("WPCodeList"));
    	billBean.setBillStatus("PENDING");
		if(!dc.getValue("bill_Narration").equals(""))
			billBean.setBillNarration(dc.getValue("bill_Narration"));
		billBean.setBillType(dc.getValue("bill_Type"));
		billBean.setPassedAmount(Double.parseDouble(dc.getValue("passed_Amount")));
		if(!dc.getValue("advance_Adjusted").equals("") && !dc.getValue("expenditure_Type").equals(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT)){
			checkAdvanceAdjusted();
	    	if(LOGGER.isDebugEnabled())     LOGGER.debug(" Inside set advance "+dc.getValue("advance_Adjusted")+"   "+dc.getValue("expenditure_Type"));
	  		billBean.setAdvanceAdjusted(Double.parseDouble(dc.getValue("advance_Adjusted")));
	    }
	    billBean.setLastModifiedBy(Integer.parseInt(dc.getValue("egUser_id")));
		billBean.setLastModifiedDate(commonmethod.getSQLDateTimeFormat(today));
		if(!checkBillNumberUniqueUpdate())
		{
			dc.addMessage("userFailure","Duplicate BillNumber Not Allowed");
			throw new TaskFailedException("checkBillNumberUniqueUpdate Function Failed");
		}

		billBean.update(con);

	} // Update method ended.


	/**
	 * This function will validate the billnumber is Modify mode
	 * @return
	 * @throws TaskFailedException
	 */
	private boolean checkBillNumberUniqueUpdate()throws TaskFailedException{
		boolean result=true;
		String billid=dc.getValue("bill_Id");
		String billno=dc.getValue("bill_Number");
		String sql2="select eg.BILLNUMBER from  EG_BILLREGISTER eg where  eg.id !=? and upper(eg.BILLNUMBER) like upper(?)";
	    try{
	    	pstmt=con.prepareStatement(sql2);
	    	pstmt.setString(1,billid);
	    	pstmt.setString(2,billno);
	    	ResultSet rset2=pstmt.executeQuery();
			if(LOGGER.isInfoEnabled())     LOGGER.info("sql2:"+sql2);
			if(rset2.next())
				result=false;
			pstmt.close();
		    }catch(Exception ex){
		    	LOGGER.error("Exp in checkBillNumberUniqueUpdate"+ex.getMessage(), ex);
		    	throw new TaskFailedException(ex);
			}
		return result;
	}

	/**
	 * This function will check if the advance adjustes is within the limit
	 * @throws TaskFailedException
	 */
  private void checkAdvanceAdjusted()throws TaskFailedException{
//	int result2=0;
	double advanceAdj=0;
	if(!dc.getValue("advance_Adjusted").equals(""))
	advanceAdj=Double.parseDouble(dc.getValue("advance_Adjusted"));
	double advAmtPaid=0.0,advAmtAdj=0.0;
	String wpcode=dc.getValue("WPCodeList");
	String sql1="SELECT wd.ADVANCEAMOUNT,wd.ADVANCEADJ FROM WORKSDETAIL wd WHERE  wd.ID=?";
	if(LOGGER.isInfoEnabled())     LOGGER.info("sql1:"+sql1);
	try
	{
		pstmt=con.prepareStatement(sql1);
		pstmt.setString(1,wpcode);
		ResultSet rset=pstmt.executeQuery();
		if(rset.next())
		{
			advAmtPaid=rset.getDouble("advanceamount");
			advAmtAdj=rset.getDouble("advanceadj");
		}
		pstmt.close();
		if(advAmtPaid>0 && advanceAdj>0){
			dc.addMessage("userFailure","There is no advance Paid for you to adjust");
			throw new TaskFailedException();}
		if((advAmtAdj+advanceAdj)>advAmtPaid){
			dc.addMessage("userFailure","AdvanceAdjusted Must be less than/equal to the Advance outstanding");
			throw new TaskFailedException();}
	}catch(Exception ex){
		LOGGER.error("Exp in checkAdvanceAdjusted"+ex.getMessage(), ex);
		throw new TaskFailedException(ex);}
  }



}
