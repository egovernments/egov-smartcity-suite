
package com.exilant.exility.common;

import java.sql.Connection;

import com.exilant.exility.dataservice.*;
import com.exilant.exility.updateservice.*;

/**
 * @author raghu.bhandi Exilant Consulting
 *
 * Abstract Class that is the base class for all tasks
 */
public abstract class AbstractTask {

	public AbstractTask() {
		super();
	}
	
	abstract public void execute (String taskName,
							String gridName,
							DataCollection dc,
							Connection con,
							boolean errorOnNoData,
							boolean gridHasColumnHeading, String prefix) throws TaskFailedException ;	


	// executes an SQL statement and extracts the results into the dc.
	protected void extractData( String sql,
				String gridName,
				DataCollection dc,
				Connection con,
				boolean errorOnNoData,
				boolean gridHasColumnHeading, String prefix) throws TaskFailedException
	{
		DataExtractor extractor = DataExtractor.getExtractor();
		extractor.extract(sql,gridName,dc,con,errorOnNoData,gridHasColumnHeading, prefix);
	}
	
	public void update( String sql,
			Connection con,
			DataCollection dc,
			boolean errorOnNoData) throws TaskFailedException
		{
			DataUpdater updator = DataUpdater.getUpdater();
			updator.update(sql,con,dc,errorOnNoData);
		}

}
