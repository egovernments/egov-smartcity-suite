package com.exilant.exility.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.Work;

import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.ExilServiceInterface;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.dataservice.SQLTask;

public class TreeService implements ExilServiceInterface{
	private static TreeService singletonInstance;

	public static TreeService getService(){
		if (singletonInstance == null){
			singletonInstance = new TreeService();
		}
		return singletonInstance;
	}

	private TreeService(){
		super();
	}


	public void doService(final DataCollection dc)
	{
		
		HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection con) throws SQLException {

				String treeName = dc.getValue("treeName");
				if (treeName == null)
				{
					dc.addMessage("exilNoKeyValue");
					return;
				}
				
				SQLTask task = SQLTask.getTask();
				try
				{
					task.execute(treeName, treeName, dc, con, true, true,"");
				}
				catch(TaskFailedException e)
				{
					// message is already put by the task;
				}
				catch(Exception e)
				{
					dc.addMessage("exilServerError",e.getMessage());
				}
				
			}
		});
		
	}
}
