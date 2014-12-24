package com.exilant.exility.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.Work;

import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.ExilServiceInterface;
import com.exilant.exility.dataservice.SQLTask;

public class ListService implements ExilServiceInterface{
	private static final Logger LOGGER = Logger.getLogger(ListService.class);
	private static ListService singletonInstance;

	public static ListService getService(){
		if (singletonInstance == null){
			singletonInstance = new ListService();
		}
		return singletonInstance;
	}

	private ListService(){
		super();
	}


	public void doService(final DataCollection dc)
	{
		
		HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection con) throws SQLException {
			
				String[] services = dc.getValueList("serviceID");
				if (null == services || services.length == 0 )
				{
					String serviceID = dc.getValue("serviceID");
					if (null == serviceID || serviceID.length() == 0)
					{
						dc.addMessage("exilNoServiceID");
						return;
					}
					services = new String[1];
					services[0] = serviceID;
				}
				
				SQLTask task = SQLTask.getTask();


				try
				{
					
					for (int i=0; i<services.length; i++)
					{
					//con.setReadOnly(true);
						task.execute(services[i], services[i], dc, con, false, false, "");
		LOGGER.debug(" tasks executed in " + i + " for service id = ????????  " + services[i]);
					}
				}
				catch(Exception e)
				{
					LOGGER.debug("Exp="+e.getMessage());
					//LOGGER.debug("DataBase error in ListService"));
					dc.addMessage("exilDBError", e.getMessage());
				}
			}
		});
		
	}
	public static void main(String[] args)
	{
		ListService ls = new ListService();
		DataCollection dc = new DataCollection();
		dc.addValue("serviceID","SQLDefinition1");
		//dc.addValue("empno","100");
		//dc.addValue("doj","08-dec-04");
		ls.doService(dc);
		//LOGGER.debug("ReSULT :"+res);

	}
}
