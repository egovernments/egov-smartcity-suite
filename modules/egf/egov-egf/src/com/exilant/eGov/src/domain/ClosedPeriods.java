/*
 * Created on Jun 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;

/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class ClosedPeriods
{
	private String id =null;
	private String startingDate = "";
	private String endingDate = "";
	private String isClosed = null;
	
	private String updateQuery="UPDATE closedPeriods SET ";
	private boolean isId=false, isField=false;
	private static final Logger LOGGER=Logger.getLogger(ClosedPeriods.class);
	
	public void setId(String aId){ id = aId; isId=true;}
	public int getId() {return Integer.valueOf(id).intValue(); }
	public void setStartingDate(String aStartingDate){ startingDate = aStartingDate; updateQuery = updateQuery + " startingDate='" + startingDate + "',"; isField = true;}
	public void setEndingDate(String aEndingDate){ endingDate = aEndingDate; updateQuery = updateQuery + " endingDate='" + endingDate + "',"; isField = true;}
	public void setIsClosed(String aIsClosed){ isClosed = aIsClosed; updateQuery = updateQuery + " isClosed='" + isClosed + "',"; isField = true;}
	
	public void insert(Connection connection) throws SQLException{	
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("ClosedPeriods")) );		
		String insertQuery = "INSERT INTO closedPeriods (id, startingDate, endingDate, isClosed) " +						 
						"VALUES (" + id + ", '" + startingDate + "', '" + endingDate + "', " + isClosed + ")";
		
		LOGGER.info(insertQuery);
		Statement statement = connection.createStatement();
		statement.executeUpdate(insertQuery);
		statement.close();
	}
	
	public void update (Connection connection) throws SQLException
	{
		if(isId && isField)
		{	
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;
			Statement statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			statement.close();
			LOGGER.info(updateQuery);
			updateQuery="UPDATE ClosedPeriods SET ";			
		}
	}
	
	static public boolean isClosedForPosting(String date, Connection conn)throws TaskFailedException{
		boolean isClosed = true;
		try{
			Statement st = conn.createStatement();
			LOGGER.debug("SELECT id FROM financialYear " +
					"WHERE startingDate<='"+date+"' AND endingDate>='"+date+"' AND isActiveForPosting=1");
			
			ResultSet rs = st.executeQuery("SELECT id FROM financialYear " +
					"WHERE startingDate<='"+date+"' AND endingDate>='"+date+"' AND isActiveForPosting=1");			
			if(rs.next()) isClosed = false;
			
			if(!isClosed){
				rs=null;
				LOGGER.debug("SELECT id FROM closedPeriods " +
						"WHERE to_char(startingDate, 'DD-MON-YYYY')<='"+date+"' " +
								"AND endingDate>='"+date+"'");
				
				rs = st.executeQuery("SELECT id FROM closedPeriods " +
						"WHERE to_char(startingDate, 'DD-MON-YYYY')<='"+date+"' " +
								"AND endingDate>='"+date+"'");
				if(!rs.next()) isClosed = false;
			}
						
			rs.close();
			st.close();
		}catch(SQLException ex){
			isClosed = true;
			LOGGER.error("EXP="+ex.getMessage());
			throw new TaskFailedException(ex.getMessage());
		}		
		return isClosed;
	}
}

