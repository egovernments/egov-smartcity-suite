/*
 * Created on Jan 18, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.exilant.exility.updateservice;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

import java.sql.Connection;

import org.apache.log4j.Logger;

/**
 * @author deepthi.kollipara
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UpdateTask extends AbstractTask
{
	private static final Logger LOGGER = Logger.getLogger(UpdateTask.class);
	static private UpdateTask singletonInstance;
	public static UpdateTask getTask() 
	{
		if (singletonInstance == null)singletonInstance = new UpdateTask();
		return singletonInstance;
	}
	private UpdateTask()
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
		String sql = tableDefinition.getUpdateSQL(dc);
LOGGER.debug("UPDATE SQL>>>>>>>>>>>>" + sql);		
		DataUpdater dataUpdater = DataUpdater.getUpdater();
		dataUpdater.update(sql,con,dc, false);
	}
	
	public static void main(String[] args)
	{
		
	}
}
