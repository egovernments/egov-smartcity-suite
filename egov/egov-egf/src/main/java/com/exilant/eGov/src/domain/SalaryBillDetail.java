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
 * Created on Feb 4, 2005
  * @author pushpendra.singh
 */
package com.exilant.eGov.src.domain;


import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
@Transactional(readOnly=true)
public class SalaryBillDetail {
	private String id = null;
	private String voucherHeaderId = null;
	private String mmonth = null;
	private String grossPay = "0";
	private String totalDeductions = "0";
	private String netPay = "0";
	private String paidAmount = "0";
	private String narration = null;
	private String financialYearID =null;
	private String isReversed="0";
	private String billId=null;
	private String updateQuery="UPDATE salarybilldetail SET";
	private boolean isId=false, isField=false;
	private static final Logger LOGGER = Logger.getLogger(SalaryBillDetail.class);

	public void setId(String aId){ id = aId; isId=true;isField = true; }
	public void setvoucherHeaderId(String aVoucherHeaderId){  voucherHeaderId = aVoucherHeaderId;  updateQuery = updateQuery + " voucherheaderid = " + voucherHeaderId + ","; isField = true; }
	public void setmmonth(String ammonth){ mmonth = ammonth; updateQuery = updateQuery + " mmonth = " + mmonth + ","; isField = true; }
	public void setgrossPay(String agrossPay){ grossPay = agrossPay; updateQuery = updateQuery + " grossPay = " + grossPay + ","; isField = true;}
	public void settotalDeductions(String atotalDeductions){ totalDeductions = atotalDeductions; updateQuery = updateQuery + " totalDeductions = " + totalDeductions + ","; isField = true;}
	public void setnetPay(String anetPay){ netPay = anetPay; updateQuery = updateQuery + " netPay = " + netPay + ","; isField = true;}
	public void setpaidAmount(String apaidAmount){ paidAmount = apaidAmount; updateQuery = updateQuery + " paidAmount = paidAmount " + paidAmount + ","; isField = true;}
	public void setnarration(String anarration){ narration = anarration;updateQuery = updateQuery + " narration = '" + narration + "',"; isField = true; }
	public void setFinancialYearID(String aFinancialYearID){ financialYearID = aFinancialYearID; updateQuery = updateQuery + " financialYearID = " + financialYearID + ","; isField = true;}
	public void setisReversed(String aIsReversed) { isReversed = aIsReversed; updateQuery = updateQuery + " isReversed = " + isReversed + ","; isField = true;}
	public void setBillId(String abillId){ 
		billId = abillId;  
		if(billId!=null && !billId.trim().equals("")) 	
			updateQuery = updateQuery + " billId = '" + billId +"',"; 
		isField = true; }
	@Transactional
	public void insert() throws SQLException
	{
		EGovernCommon commonMethods = new EGovernCommon();
		narration = commonMethods.formatString(narration);
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("SalaryBillDetail")) );

		String insertQuery = "INSERT INTO salarybilldetail (Id, voucherHeaderId, mmonth, grossPay, " +
							"totalDeductions, netPay, paidAmount, narration,financialyearid,isreversed,BILLID) " +
							"VALUES (" + id + ", " + voucherHeaderId + ", '" + mmonth + "', "
							+ grossPay + ", " + totalDeductions + ", " + netPay + ", " + paidAmount
							+ ", '" + narration +"',"+ financialYearID +", " + isReversed +","+billId+")";

		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		Query statement = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
		statement.executeUpdate();
	}
	@Transactional
	public void reverse()throws SQLException
	{
		//	EGovernCommon commommethods=new EGovernCommon();
			
			String reverseQuery="UPDATE salaryBillDetail SET IsReversed=1 WHERE id="+id;
			Query statement=HibernateUtil.getCurrentSession().createSQLQuery(reverseQuery);
			if(LOGGER.isInfoEnabled())     LOGGER.info(reverseQuery);
			statement.executeUpdate();
	}
	@Transactional
	public void reverseCheck()throws SQLException,TaskFailedException
	{
				String paidAmount="";
				try{
				
				List<Object[]> rset;
				String reverseQuery="select paidamount as \"paidamount\" from salarybilldetail where id="+id;
				Query statement=HibernateUtil.getCurrentSession().createSQLQuery(reverseQuery);
				if(LOGGER.isInfoEnabled())     LOGGER.info(reverseQuery);
				rset  = statement.list();
				for(Object[] element : rset){
					 paidAmount=element[0].toString();
				}
				if(!paidAmount.equalsIgnoreCase("0")){
					throw new TaskFailedException("Salary Bill cannot be reversed unless Payments made for this bill is reversed");
					}
				}
				catch(Exception e){
					throw new TaskFailedException("Salary Bill cannot be reversed unless Payments made for this bill is reversed");
				}
		}
	@Transactional
	public void update () throws SQLException
	{
		if(isId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;
			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			Query statement = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
			statement.executeUpdate();
			updateQuery="UPDATE salarybilldetail SET";

		}
	}
	@Transactional
	public void reversePaid (double paidAmount )throws SQLException
	{
		if(isId){
				
				String reverseNegative="UPDATE salaryBillDetail SET paidAmount=paidAmount-"+paidAmount+" WHERE id="+ id;
				Query statement=HibernateUtil.getCurrentSession().createSQLQuery(reverseNegative);
				if(LOGGER.isInfoEnabled())     LOGGER.info(reverseNegative);
				statement.executeUpdate();
		}
	}
}
