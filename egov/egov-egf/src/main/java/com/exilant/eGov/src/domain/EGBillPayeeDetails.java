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
package com.exilant.eGov.src.domain;


import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
@Transactional(readOnly=true)
public class EGBillPayeeDetails  {
	private static final Logger LOGGER=Logger.getLogger(EGBillPayeeDetails.class);

	String id;
	String egBilldetailsId;
	String accountDetailTypeId;
	String accountDetailKeyId;
	String debitAmount;
	String creditAmount;
	String lastUpdatedTime;
	String tdsId;
	private String updateQuery="UPDATE EG_BILLPAYEEDETAILS SET";
	private boolean isId=false,isField=false;

	@Transactional
	public void insert() throws SQLException,TaskFailedException
	{
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("EG_BILLPAYEEDETAILS")));
		String insertQuery = "INSERT INTO EG_BILLPAYEEDETAILS (Id, BilldetailId, accountDetailTypeId,accountDetailKeyId, " +
						"debitamount, creditAmount,lastUpdatedTime,tdsId) " +
						"VALUES (" + id + ", " + egBilldetailsId + ", " +accountDetailTypeId+", " + accountDetailKeyId +" , " + debitAmount +" ," + creditAmount + ",to_date('"+this.lastUpdatedTime+"','dd-Mon-yyyy HH24:MI:SS'),"+tdsId+")";
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);   
		Query statement = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
		statement.executeUpdate();
	}
	@Transactional
	public void update () throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;

			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			Query statement = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
			statement.executeUpdate();
			updateQuery="UPDATE EG_BILLPAYEEDETAILS SET";
		}
	}


	@Transactional
public void delete()throws SQLException,TaskFailedException
{
	String delQuery="delete from EG_BILLPAYEEDETAILS where id="+getId() ;
	if(LOGGER.isInfoEnabled())     LOGGER.info(delQuery);
	Query statement = HibernateUtil.getCurrentSession().createSQLQuery(delQuery);
	statement.executeUpdate();

}
	@Transactional(readOnly=true)
public void deleteByBillDetailId()throws SQLException,TaskFailedException
{
	String delQuery="delete from EG_BILLPAYEEDETAILS where BilldetailId ="+egBilldetailsId;
	if(LOGGER.isInfoEnabled())     LOGGER.info(delQuery);
	Query statement = HibernateUtil.getCurrentSession().createSQLQuery(delQuery);
	statement.executeUpdate();

}
public void setId(String aId){ id = aId; isId=true;isField = true;}
public int getId() {return Integer.valueOf(id).intValue(); }
public void setEgBilldetailsId(String aBillId){ egBilldetailsId = aBillId; updateQuery = updateQuery + " BilldetailsId=" + egBilldetailsId + ","; isField = true;}
public void setAccountDetailTypeId(String aAccountDetailTypeId){accountDetailTypeId=aAccountDetailTypeId;updateQuery=updateQuery+"accountDetailTypeId="+accountDetailTypeId+" , ";isField=true;}
public void setAccountDetailKeyId(String aAccountDetailKeyId){accountDetailKeyId=aAccountDetailKeyId;updateQuery=updateQuery+"accountDetailKeyId="+accountDetailKeyId+" , ";isField=true;}
public void setDebitAmount(String aDebitAmount){ debitAmount=aDebitAmount;updateQuery=updateQuery+" debitAmount ="+debitAmount+" ,"; isField=true;}
public void setCreditAmount(String aCreditAmount){ creditAmount=aCreditAmount;updateQuery=updateQuery+" creditAmount ="+creditAmount+" ,"; isField=true;}
public void setLastUpdatedTime(String aLastModifiedDate){ lastUpdatedTime = aLastModifiedDate; updateQuery = updateQuery + " lastUpdateTime=to_date('" + lastUpdatedTime + "','dd-Mon-yyyy HH24:MI:SS')"+","; isField = true;}
public void setTdsId(String tId){tdsId=tId;updateQuery=updateQuery+"tdsId="+tdsId+",";isField=true;}
public int getTdsId() {return Integer.valueOf(tdsId).intValue(); }


}
