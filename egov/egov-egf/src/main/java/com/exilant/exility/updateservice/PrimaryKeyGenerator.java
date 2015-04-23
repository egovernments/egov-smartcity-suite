
package com.exilant.exility.updateservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;



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

	public static long getNextKey(String tableName)
	{
		Connection con = null;
		long key = 0;
		String sql = "select SEQ_"+tableName+".nextval from dual";
		try
		{
			con = null;//This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if(rs.next())
			key = rs.getLong(1);
			else
			throw new Exception();
			rs.close();
			pst.close();
		}
		catch(Exception e)
		{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Exp="+e.getMessage());
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Error getting value from sequence "+e.toString());
		}
		finally
		{
			//This fix is for Phoenix Migration.EgovDatabaseManager.releaseConnection(con,null);
		}

		if(LOGGER.isDebugEnabled())     LOGGER.debug("PK for "+tableName+" is "+key);
		return key;
	}



}
