package com.exilant.exility.updateservice;

import java.sql.*;

import org.apache.log4j.Logger;


import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author raghu.bhandi, Exilant Consulting
 *
 * Executes a SQL statement and extracs the results into a dataCollection
 * This is a singleton
 */

public class DataUpdater {
	private static final Logger LOGGER = Logger.getLogger(DataUpdater.class);
	private static DataUpdater singletonInstance;


	public static DataUpdater getUpdater (){
		if  (singletonInstance == null)singletonInstance = new DataUpdater();
		return singletonInstance;
	}


	private DataUpdater() {
		super();
	}


	public int update(String sql,
						Connection con,
						DataCollection dc,
						boolean errorOnNoUpdate) throws TaskFailedException {
		if(sql == null || sql.length() == 0 )return 0;
//temp code to test when database is not available
		int i = dc.getInt("sqlCount");
		i++;
		dc.addValue("sqlCount", i);
		/* commented in egf
		 		dc.addValue("sql" + i, sql);
		*/
		if (null == con)return 0;
// end of temp patch. You should remove this patch when going to production
		int noOfRowsEffected = 0;
		try{
			Statement statement = con.createStatement();
			noOfRowsEffected = statement.executeUpdate(sql);
LOGGER.debug("SSSSSSSS>>>>>>>>>>>>>>>>>>>>>>>>" + sql);
			if(noOfRowsEffected == 0 && errorOnNoUpdate){
				throw new TaskFailedException();
			}
			else
			{
				dc.addMessage("masterInsertUpdate");
			}
			statement.close();
		}
		catch(SQLException e){
			LOGGER.debug("err Message  "+e.getErrorCode());
			if(e.getErrorCode() == 1)
				dc.addMessage("exilDuplicate");
			else
			dc.addMessage("exilSQLException", e.getMessage());
			throw new TaskFailedException();
		}
		return noOfRowsEffected;
	}
}
