package com.exilant.exility.service;

/*
 * This is identical to
 */
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;


import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.ExilServiceInterface;
import com.exilant.exility.dataservice.JobService;
public class UpdateService implements ExilServiceInterface
{
	private final static Logger LOGGER=Logger.getLogger(UpdateService.class);
	private static UpdateService singletonInstance;

	public static UpdateService getService(){
		if (singletonInstance == null) singletonInstance = new UpdateService();
		return singletonInstance;
	}

	private UpdateService(){
		super();
	}

	public void doService(DataCollection dc){
		Connection con = null;
		try{
			JobService jobService = JobService.getInstance();
			con = null;//This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();
			//con.setAutoCommit(false);
			jobService.doService(dc,con);
			//con.commit();
			// 
		}catch(Exception e){
			LOGGER.error("Exp="+e.getMessage(),e);
			//dc.addMessage("DBError", e.getMessage());
			try{
				//con.rollback();
				 
			} catch (Exception se)
			{
				LOGGER.error("Exp="+se.getMessage(),se);
			}
		} finally {
			try{
				//con.close();
				//This fix is for Phoenix Migration.EgovDatabaseManager.releaseConnection(con,null);
			}catch (Exception e){
				LOGGER.error("EgovDatabaseManager release connection failed "+e.getMessage(),e);
			}
		}

	}


	public static void main(String[] args){
	}
}
