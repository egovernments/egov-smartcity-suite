package com.exilant.exility.service;

/*
 * This is identical to
 */
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.Work;

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

	public void doService(final DataCollection dc){

		HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection con) throws SQLException {
				
				try{
					JobService jobService = JobService.getInstance();
				
					//con.setAutoCommit(false);
					jobService.doService(dc,con);
					//con.commit();
					HibernateUtil.commitTransaction();
				}catch(Exception e){
					LOGGER.debug("Exp="+e.getMessage());
					//dc.addMessage("DBError", e.getMessage());
					try{
						//con.rollback();
						HibernateUtil.rollbackTransaction();
					} catch (Exception se)
					{
						LOGGER.debug("Exp="+se.getMessage());
					}
				}
				
			}
		});
		
		
	}


	public static void main(String[] args){
	}
}
