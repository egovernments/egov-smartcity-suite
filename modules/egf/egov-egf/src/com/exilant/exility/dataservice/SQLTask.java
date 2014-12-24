
package com.exilant.exility.dataservice;

import com.exilant.exility.common.*;

import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;

import org.egov.infstr.utils.EGovConfig;

public class SQLTask extends AbstractTask {
	static private SQLTask singletonInstance;
	
	//declaration for XML Loader, field not used anywhere
	protected SQLTemplate sqlTemplate;
	public HashMap sqlTemplates;
	
	//singleton 
	public static SQLTask getTask()
	{
		if (singletonInstance == null)singletonInstance = new SQLTask();
		return singletonInstance;
	}


	private SQLTask()
	{
		super();
		//load all statements ... may be this is too many for a project.
		// let us go with this approach at this time...
		XMLLoader xmlLoader = new XMLLoader();
		URL url = EGovConfig.class.getClassLoader().getResource("config/resource/sqlTemplates.xml");
		//LOGGER.debug("url in SQLTask=================="+url);
		xmlLoader.load(url.toString(),this);
	}
	
	public String getSQL(String serviceID, DataCollection dc, Connection con) throws TaskFailedException
	{
		//locate the statement fromHashMap
		SQLTemplate template = (SQLTemplate)this.sqlTemplates.get(serviceID);//Get based on the Id from the hashmap.
		if (template == null)
		{
			dc.addMessage("exilNoSQLTemplate", serviceID);
			throw new TaskFailedException() ;	
		}
		return template.getSQL(dc);
		
	}
	
	public void execute(String  serviceID,
						String gridName,
						DataCollection dc,
						Connection con,
						boolean erronNoData,
						boolean gridHasColumnHeading, 
						String prefix) throws TaskFailedException
						{
		//locate the statement fromHashMap
		SQLTemplate template = (SQLTemplate)this.sqlTemplates.get(serviceID);//Get based on the Id from the hashmap.
		if (template == null)
		{
			dc.addMessage("exilNoSQLTemplate", serviceID);
			throw new TaskFailedException() ;	
		}
		String sql = this.getSQL(serviceID, dc, con);		
		this.extractData(sql, gridName, dc, con, erronNoData, gridHasColumnHeading, prefix);	}
}
