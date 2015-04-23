/*
 * Created on Jan 18, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.exilant.exility.updateservice;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author deepthi.kollipara
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class InsertTask extends AbstractTask
{
	private static final Logger LOGGER = Logger.getLogger(InsertTask.class);
	static private InsertTask singletonInstance;
	public static InsertTask getTask()
	{
		if (singletonInstance == null)singletonInstance = new InsertTask();
				return singletonInstance;
	}
	private InsertTask()
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
			String sql = tableDefinition.getInsertSQL(dc);
if(LOGGER.isDebugEnabled())     LOGGER.debug("Insert SQL>>>>>>>>>>>>" + sql);			
			DataUpdater dataUpdater = DataUpdater.getUpdater();
			dataUpdater.update(sql,con,dc, false);
		}
	public static void main(String[] args)
	{
	}
}
