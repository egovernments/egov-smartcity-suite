package com.exilant.exility.updateservice;

import java.sql.Connection;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class BulkTask extends AbstractTask
{
	static private BulkTask singletonInstance;
	
	private  final String BULK_ACTION = "bulkAction";
	private final String UPDATE_ACTION = "update";
	private final String INSERT_ACTION = "insert";
	private final String DELETE_ACTION = "delete";
	private final String NONE_ACTION = "none";

	public static BulkTask getTask()
	{
		if (singletonInstance == null)singletonInstance = new BulkTask();
				return singletonInstance;
	}
	private BulkTask()
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
			DataUpdater dataUpdater =DataUpdater.getUpdater();
			String sql = "";
			String nameInDC = tableDefinition.name +  "_" + BULK_ACTION;

			if (dc.hasName(nameInDC)) // single row insert/update case
			{
				String action = dc.getValue(nameInDC);

				if (action == NONE_ACTION) return; // no work
				
				if (action == INSERT_ACTION)
					sql = tableDefinition.getInsertSQL(dc);
				else if (action == UPDATE_ACTION)
					sql = tableDefinition.getUpdateSQL(dc); 
				else if (action == DELETE_ACTION)
					sql = tableDefinition.getDeleteSQL(dc); 
				else 
				{
					dc.addMessage("exilInvalidBulkAction", tableName, action);
					throw new TaskFailedException();
				}

				dataUpdater.update(sql, con, dc, errorOnNoData);
				return;
			}

			if (dc.hasList(nameInDC)) // multiple row.. Bulk insert/update
			{
				String[] actions = dc.getValueList(nameInDC);
				String action;

				for (int i=0; i<actions.length; i++)
				{
					action = actions[i];
					if (action == NONE_ACTION) continue; // no work
				
					if (action == INSERT_ACTION)
						sql = tableDefinition.getInsertSQL(dc, i);
					else if (action == UPDATE_ACTION)
						sql = tableDefinition.getUpdateSQL(dc, i); 
					else if (action == DELETE_ACTION)
						sql = tableDefinition.getDeleteSQL(dc, i); 
					else 
					{
						dc.addMessage("exilInvalidBulkAction", tableName, action);
						throw new TaskFailedException();
					}

					dataUpdater.update(sql, con, dc, errorOnNoData);
				}
				return;
			}
			//possibly multiple rows sent in dc as valueList

			dataUpdater.update(sql, con, dc, errorOnNoData);

		}
	public static void main(String[] args)
	{
	}
}
