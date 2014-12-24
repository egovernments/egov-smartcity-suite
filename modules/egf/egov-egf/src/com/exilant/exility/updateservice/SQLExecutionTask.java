package com.exilant.exility.updateservice;

import com.exilant.exility.common.*;
import com.exilant.exility.dataservice.*;
import java.sql.Connection;


public class SQLExecutionTask extends AbstractTask
{
	static private SQLExecutionTask singletonInstance;
	public static SQLExecutionTask getTask() 
	{
		if (singletonInstance == null)singletonInstance = new SQLExecutionTask();
		return singletonInstance;
	}
	private SQLExecutionTask()
	{
		super();
	}
	public void execute(String  serviceID,
						String gridName,
						DataCollection dc,
						Connection con,
						boolean errorOnNoData,
						boolean gridHasColumnHeading, String prefix) throws TaskFailedException
	{
		SQLTask task = SQLTask.getTask();
		String sql = task.getSQL(serviceID, dc, con);
		super.update(sql,con,dc,errorOnNoData);
	}
	
	public static void main(String[] args)
	{
		
	}
}
