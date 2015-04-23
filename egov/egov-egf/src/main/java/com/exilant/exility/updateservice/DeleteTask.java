package com.exilant.exility.updateservice;

import java.sql.Connection;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

/**
 */
public class DeleteTask extends AbstractTask
{
	static private DeleteTask singletonInstance;
	public static DeleteTask getTask()
	{
		if (singletonInstance == null)singletonInstance = new DeleteTask();
				return singletonInstance;
	}
	private DeleteTask()
	{
		super();
	}
	public void execute(String  tableName,
							String gridName,
							DataCollection dc,
							Connection con,
							boolean errorOnNoData,
							boolean gridHasColumnHeading, String prefix) throws TaskFailedException
		{
			TableDefinition tableDefinition = Tables.getTable(tableName); 
			String sql = tableDefinition.getDeleteSQL(dc);
			DataUpdater dataUpdater = DataUpdater.getUpdater();
			dataUpdater.update(sql,con,dc, errorOnNoData);
		}
	public static void main(String[] args)
	{
	}
}
