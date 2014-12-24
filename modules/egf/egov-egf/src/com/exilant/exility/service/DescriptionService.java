package com.exilant.exility.service;


import java.sql.Connection;
import java.sql.SQLException;

import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.Work;

import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.ExilServiceInterface;
import com.exilant.exility.dataservice.SQLTask;


public class DescriptionService implements ExilServiceInterface
{
//Static instance of the class to follow Singleton pattern
	static DescriptionService singletonInstance;
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

	public void doService(final DataCollection dc)
	{
		
		
		HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection con) throws SQLException {
				
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
					dc.addMessage("exilDBError", e.getMessage());
				}
				
			}
		});
		
	}
	/*public static void main(String[] args)
	{
//		String[] arr = {"id1"};
//		String[] a = {"p1001"};
//		DescriptionService ds = DescriptionService.getInstance();
//		LOGGER.debug(ds.get(arr,a));

	}*/
}
