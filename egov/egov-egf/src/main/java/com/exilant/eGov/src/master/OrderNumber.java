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
package com.exilant.eGov.src.master;
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly=true)
public class OrderNumber
{
	private final static Logger LOGGER=Logger.getLogger(OrderNumber.class);
	private  int id;
	private char orderType;
	private int orderNumber;
	private  int financialYearId;

	/**
	 *
	 */
	public OrderNumber() {

	}


	/**
	 * @return Returns the financialYearId.
	 */
	public int getFinancialYearId() {
		return financialYearId;
	}
	/**
	 * @param financialYearId The financialYearId to set.
	 */
	public void setFinancialYearId(int financialYearId) {
		this.financialYearId = financialYearId;
	}
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Returns the orderNumber.
	 */
	public int getOrderNumber() {
		return orderNumber;
	}
	/**
	 * @param orderNumber The orderNumber to set.
	 */
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	/**
	 * @return Returns the orderType.
	 */
	public char getOrderType() {
		return orderType;
	}
	/**
	 * @param orderType The orderType to set.
	 */
	public void setOrderType(char orderType) {
		this.orderType = orderType;
	}
	@Transactional
	public boolean insertOrderNumber()
	{

		String insertQuery="Insert into EG_ORDERNO(ID, ORDERTYPE,ORDERNO,FINANCIALYEARID)Values("+
		id+",'"+orderType+"',"+orderNumber+","+financialYearId+")";
		if(LOGGER.isDebugEnabled())     LOGGER.debug("insertQuery:"+insertQuery);
		try
		{
		Query statement = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
	    statement.executeUpdate();
	    if(LOGGER.isDebugEnabled())     LOGGER.debug("insertQuery:"+insertQuery);
	   // if(LOGGER.isDebugEnabled())     LOGGER.debug("======**************************======");
		}
		catch(Exception insert){if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception in inserting DEPARTMENT:"+insert);return false;}
		return true;
	}
	@Transactional
	public boolean update(Connection connection,String code)
	{

		String updateQuery = "UPDATE EG_ORDERNO SET ORDERTYPE = '"+ orderType +"',ORDERNO="+orderNumber+",FINANCIALYEARID = "+ financialYearId + "where id ="+code;

		if(LOGGER.isDebugEnabled())     LOGGER.debug("updateQuery:"+updateQuery);
		try{
		Query statement = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
	    statement.executeUpdate();
		}
	   // if(LOGGER.isDebugEnabled())     LOGGER.debug("updateQuery:"+updateQuery);

		catch(Exception insert){if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception in Updating COSTCENTRE:"+insert);return false;}
		return true;
	}

}
