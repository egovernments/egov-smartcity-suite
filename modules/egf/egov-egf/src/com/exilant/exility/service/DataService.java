package com.exilant.exility.service;

/*
 * Gets a "read-only" connection to the database, and calls the job srvice
 * Programmers/designer have to ensure that the jobsteps in the job being called
 * use the connection only for read operaion. (or select operations)
 *
 * This class is either invoked direclty by the ServieAgent from the Web-Tier or it can be called
 *  by the corresponding SLSB
 */
import java.sql.Connection;
import java.sql.SQLException;

import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.Work;

import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.ExilServiceInterface;
import com.exilant.exility.dataservice.JobService;

public class DataService implements ExilServiceInterface{
	private static DataService singletonInstance;

	public static DataService getService(){
		if(singletonInstance == null)singletonInstance = new DataService();
		return singletonInstance;
	}

	private DataService(){
		super();
	}


	public void doService(final DataCollection dc)
	{
		HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection con) throws SQLException {
			
				try{
					JobService jobService = JobService.getInstance();
					
					//con.setReadOnly(true);
					jobService.doService(dc,con);

				}catch(Exception e){
					dc.addMessage("exilDBError", e.toString());
				}
			}
		});
		
		
		

		
	}

/*
	public static void main(String[] args)
	{
		DataService d = new DataService();
		DataCollection dc = new DataCollection();
		dc.addValue("serviceID","job1");
		d.doService(dc);
	}

*/
}
