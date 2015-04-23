package com.exilant.exility.service;

import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.ExilServiceInterface;
import com.exilant.exility.dataservice.*;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.database.utils.EgovDatabaseManager;

import java.sql.Connection;

public class ListService implements ExilServiceInterface{
	private static final Logger LOGGER = Logger.getLogger(ListService.class);
	private static ListService singletonInstance;

	public static ListService getService(){
		if (singletonInstance == null){
			singletonInstance = new ListService();
		}
		return singletonInstance;
	}

	private ListService(){
		super();
	}


	public void doService(DataCollection dc)
	{
		String[] services = dc.getValueList("serviceID");
		if (null == services || services.length == 0 )
		{
			String serviceID = dc.getValue("serviceID");
			if (null == serviceID || serviceID.length() == 0)
			{
				dc.addMessage("exilNoServiceID");
				return;
			}
			services = new String[1];
			services[0] = serviceID;
		}
		Connection con = null;
		SQLTask task = SQLTask.getTask();


		try
		{
			con = EgovDatabaseManager.openConnection();
			for (int i=0; i<services.length; i++)
			{
			//con.setReadOnly(true);
				task.execute(services[i], services[i], dc, con, false, false, "");
if(LOGGER.isDebugEnabled())     LOGGER.debug(" tasks executed in " + i + " for service id = ????????  " + services[i]);
			}
		}
		catch(Exception e)
		{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Exp="+e.getMessage());
			//if(LOGGER.isDebugEnabled())     LOGGER.debug("DataBase error in ListService"));
			dc.addMessage("exilDBError", e.getMessage());
		}finally{
			EgovDatabaseManager.releaseConnection(con,null);
		}

		/*try
		{
			//con.close();
		//	DBHandler.returnConnection(con,null);
		}
		catch (Exception e) {}*/


	}
	public static void main(String[] args)
	{
		ListService ls = new ListService();
		DataCollection dc = new DataCollection();
		dc.addValue("serviceID","SQLDefinition1");
		//dc.addValue("empno","100");
		//dc.addValue("doj","08-dec-04");
		ls.doService(dc);
		//if(LOGGER.isDebugEnabled())     LOGGER.debug("ReSULT :"+res);

	}
}
