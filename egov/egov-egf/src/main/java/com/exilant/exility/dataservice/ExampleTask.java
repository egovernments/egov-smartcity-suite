package com.exilant.exility.dataservice;

import java.sql.Connection;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author raghu.bhandi, Exilant Consulting
 *
 */
public class ExampleTask extends AbstractTask {

	/**
	 * 
	 */
	public ExampleTask() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.exilant.exility.dataservice.AbstractTask#execute(java.lang.String, java.lang.String, com.exilant.exility.common.DataCollection, java.sql.Connection)
	 */
	public void execute(
		String taskName,
		String gridName,
		DataCollection dc,
		Connection con,
		boolean errorOnData,
		boolean gridHasColumnHeading, String prefix)
		throws TaskFailedException {
		dc.addValue("field1FromExample", "Valu1 from Example Task");
		dc.addValue("field2FromExample", "Some value from example task");
		String[][] grid = {{"valu11", "value12", "value13"},
							{"valu21", "value22", "value23"} };
		dc.addGrid("grdiFromExample", grid);
		//throw new TaskFailedException(); // this will stop further executions
	}

	public static void main(String[] args) {
	}
}
