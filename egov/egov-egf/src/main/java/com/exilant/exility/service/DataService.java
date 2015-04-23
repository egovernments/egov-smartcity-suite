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

import org.apache.log4j.Logger;
import org.egov.infstr.utils.database.utils.EgovDatabaseManager;

import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.ExilServiceInterface;
import com.exilant.exility.dataservice.JobService;

public class DataService implements ExilServiceInterface{
	private static DataService singletonInstance;
	private static final Logger LOGGER = Logger.getLogger(DataService.class);
	public static DataService getService(){
		if(singletonInstance == null)singletonInstance = new DataService();
		return singletonInstance;
	}

	private DataService(){
		super();
	}


	public void doService(DataCollection dc)
	{
		Connection con = null;
		try{
			JobService jobService = JobService.getInstance();
			con = EgovDatabaseManager.openConnection();
			//con.setReadOnly(true);
			jobService.doService(dc,con);

		}catch(Exception e){
			LOGGER.error("Inside doService"+e.getMessage(),e);
			dc.addMessage("exilDBError", e.toString());
		}finally{
			EgovDatabaseManager.releaseConnection(con,null);
		}

		/*try{
			con.close();
		}catch (Exception e){
		}*/
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
