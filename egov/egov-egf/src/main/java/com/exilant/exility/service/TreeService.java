package com.exilant.exility.service;

import com.exilant.exility.common.*;
import com.exilant.exility.dataservice.*;
import org.egov.infstr.utils.database.utils.EgovDatabaseManager;
import org.apache.log4j.Logger;
import java.sql.Connection;

public class TreeService implements ExilServiceInterface{
	private static TreeService singletonInstance;
	private static final Logger LOGGER = Logger.getLogger(TreeService.class);
	public static TreeService getService(){
		if (singletonInstance == null){
			singletonInstance = new TreeService();
		}
		return singletonInstance;
	}

	private TreeService(){
		super();
	}


	public void doService(DataCollection dc)
	{
		String treeName = dc.getValue("treeName");
		if (treeName == null)
		{
			dc.addMessage("exilNoKeyValue");
			return;
		}

		Connection con  = EgovDatabaseManager.openConnection();//Gets the DataBase Connection.
		SQLTask task = SQLTask.getTask();
		try
		{
			task.execute(treeName, treeName, dc, con, true, true,"");
		}
		catch(TaskFailedException e)
		{
			LOGGER.error("SQLTask execution failed"+e.getMessage(),e);
			// message is already put by the task;
		}
		catch(Exception e)
		{
			LOGGER.error("SQLTask execution failed"+e.getMessage(),e);
			dc.addMessage("exilServerError",e.getMessage());
		}finally{
			EgovDatabaseManager.releaseConnection(con,null);
		}

	}
}
