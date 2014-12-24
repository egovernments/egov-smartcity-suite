
package com.exilant.exility.updateservice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.ReturningWork;


public class PrimaryKeyGenerator
{
	private static final Logger LOGGER = Logger.getLogger(PrimaryKeyGenerator.class);
	private static PrimaryKeyGenerator singletonInstance;

	//private static HashMap nextKeys = new HashMap();
	static
	{
		singletonInstance = new PrimaryKeyGenerator();
	}

	public static PrimaryKeyGenerator getKeyGenerator(){
		//if(singletonInstance == null)singletonInstance = new PrimaryKeyGenerator();
		return singletonInstance;
	}

	private PrimaryKeyGenerator()
	{
		super();
	}

	public static Long getNextKey(final String tableName)
	{
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<Long>() {

			@Override
			public Long execute(Connection con) throws SQLException {
				long key = 0;
				String sql = "select SEQ_"+tableName+".nextval from dual";
				try
				{
					
					Statement stat = con.createStatement();
					ResultSet rs = stat.executeQuery(sql);
					if(rs.next())
					key = rs.getLong(1);
					else
					throw new Exception();
					rs.close();
					stat.close();
				}
				catch(Exception e)
				{
					LOGGER.debug("Exp="+e.getMessage());
					LOGGER.debug("Error getting value from sequence "+e.toString());
				}
				
				LOGGER.debug("PK for "+tableName+" is "+key);
				return key;
			}
		});
		
	}



}
