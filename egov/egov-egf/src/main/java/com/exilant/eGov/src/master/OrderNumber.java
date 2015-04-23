package com.exilant.eGov.src.master;
import java.sql.Connection;
import java.sql.Statement;

import org.apache.log4j.Logger;
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
	public boolean insertOrderNumber(Connection connection)
	{

		String insertQuery="Insert into EG_ORDERNO(ID, ORDERTYPE,ORDERNO,FINANCIALYEARID)Values("+
		id+",'"+orderType+"',"+orderNumber+","+financialYearId+")";
		if(LOGGER.isDebugEnabled())     LOGGER.debug("insertQuery:"+insertQuery);
		try
		{
		Statement statement = connection.createStatement();
	    statement.executeUpdate(insertQuery);
	    if(LOGGER.isDebugEnabled())     LOGGER.debug("insertQuery:"+insertQuery);
		    try
		    {
			    statement.close();
			    //if(LOGGER.isDebugEnabled())     LOGGER.debug("============");
		    }
		    catch(Exception close)
		    {
		    	return false;
		    }
	   // if(LOGGER.isDebugEnabled())     LOGGER.debug("======**************************======");
		}
		catch(Exception insert){if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception in inserting DEPARTMENT:"+insert);return false;}
		return true;
	}
	public boolean update(Connection connection,String code)
	{

		String updateQuery = "UPDATE EG_ORDERNO SET ORDERTYPE = '"+ orderType +"',ORDERNO="+orderNumber+",FINANCIALYEARID = "+ financialYearId + "where id ="+code;

		if(LOGGER.isDebugEnabled())     LOGGER.debug("updateQuery:"+updateQuery);
		try{
		Statement statement = connection.createStatement();
	    statement.executeUpdate(updateQuery);
	   // if(LOGGER.isDebugEnabled())     LOGGER.debug("updateQuery:"+updateQuery);
	    try{
	    statement.close();
	    }catch(Exception close){return false;}
	    //if(LOGGER.isDebugEnabled())     LOGGER.debug(updateQuery);
		}
		catch(Exception insert){if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception in Updating COSTCENTRE:"+insert);return false;}
		return true;
	}

}