package com.exilant.exility.service;


import com.exilant.exility.common.*;
import com.exilant.exility.dataservice.*;
import org.egov.infstr.utils.database.utils.EgovDatabaseManager;
import org.apache.log4j.Logger;
import java.sql.Connection;


public class DescriptionService implements ExilServiceInterface
{
//Static instance of the class to follow Singleton pattern
	static DescriptionService singletonInstance;
	private static final Logger LOGGER = Logger.getLogger(DescriptionService.class);
	private static final String SERVICE_ID = "serviceID";
	private static final String KEY_VALUE = "keyValue";

	public static DescriptionService getService(){
		if(singletonInstance == null){
			singletonInstance = new DescriptionService();
		}
		return singletonInstance;
	}

//Singleton pattern
	private DescriptionService()
	{
		super();
	}

	public void doService(DataCollection dc)
	{
		String[] serviceID = dc.getValueList("serviceID");
		String[] keyValue = dc.getValueList("keyValue");
		if (dc.hasList(SERVICE_ID))
		{
			serviceID = dc.getValueList(SERVICE_ID);
		}
		else if(dc.hasName(SERVICE_ID))
		{
			serviceID = new String[1];
			serviceID[0] = dc.getValue(SERVICE_ID);
		}
		else
		{
			dc.addMessage("exilNoServiceID");
			return;
		}

		if (dc.hasList(KEY_VALUE))
		{
			keyValue = dc.getValueList(KEY_VALUE);
		}
		else if(dc.hasName(KEY_VALUE))
		{
			keyValue = new String[1];
			keyValue[0] = dc.getValue(KEY_VALUE);
		}
		else
		{
			dc.addMessage("exilNoKeyValue");
			return;
		}
		int kount = serviceID.length;
		if (keyValue.length != kount)
		{
			dc.addMessage("exilKeyValueMismatch", " "+kount, " "+keyValue.length);
			return;
		}
		Connection con = null;
		con = EgovDatabaseManager.openConnection();
		//con.setReadOnly(true);
		SQLTask task = SQLTask.getTask();
		try
		{
			for(int i = 0;i < kount; i++)
			{
				dc.addValue(KEY_VALUE,keyValue[i]);
				task.execute(serviceID[i],serviceID[i]+"_"+keyValue[i],dc,con,false,false,"");
			}

		}
		catch(Exception e)
		{
			LOGGER.error("SQLTask failed "+e.getMessage(),e);
			dc.addMessage("exilDBError", e.getMessage());
		}finally{
			EgovDatabaseManager.releaseConnection(con,null);
		}
	}
	/*public static void main(String[] args)
	{
//		String[] arr = {"id1"};
//		String[] a = {"p1001"};
//		DescriptionService ds = DescriptionService.getInstance();
//		if(LOGGER.isDebugEnabled())     LOGGER.debug(ds.get(arr,a));

	}*/
}
