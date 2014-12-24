
package com.exilant.exility.updateservice;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import org.egov.infstr.utils.EGovConfig;

import com.exilant.exility.common.XMLLoader;


public class Tables
{
	private static HashMap tableDefinitions;
	
	public static final String SURROGATE_KEY_NAME = "ID";
	public static final String MODIFIED_TIMESTAMP_NAME = "lastModified";
	public static final String MODIFIED_USER_NAME	 = "modifiedBy";
	public static final String CREATED_USER_NAME	 = "";
	public static final String CRAETED_TIMESTAMP_NAME = "created";
	public static final String ACTIVE_NAME = "isActive";
	
	
// it first checks in cache. Else loads into the cache before returning the iinstance
	public static TableDefinition getTable(String tableName){
		
		if (tableDefinitions == null) tableDefinitions = new HashMap();
		TableDefinition definition;
		Object obj = null;
		
		obj = tableDefinitions.get(tableName);
		if (obj == null){
			definition = new TableDefinition();
			XMLLoader xl = new XMLLoader();
			String fileName = "config/resource/table/" +tableName+".xml";
			URL url = EGovConfig.class.getClassLoader().getResource("config/resource/table/" +tableName+".xml");
			//LOGGER.debug("url in tables=================="+url);
			if(url!=null)
				fileName = url.getFile();
			File file = new File(fileName);
			if (file.isFile()){
				xl.load(fileName, definition);
				definition.optimize();
				tableDefinitions.put(tableName, definition);
			}
		}else definition = (TableDefinition)obj;
		
		return definition;

	}
	
	private Tables()
	{
		super();
	}
}
