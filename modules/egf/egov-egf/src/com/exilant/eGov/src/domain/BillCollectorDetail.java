/*
 * Created on Jan 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import org.apache.log4j.Logger;
/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class BillCollectorDetail
{
	private String id = null;
	private String billCollectorId = null;
	private String wardId = null;
	private String updateQuery="UPDATE billcollectordetail SET";
	private boolean isId=false, isField=false;
	private static final Logger LOGGER=Logger.getLogger(BillCollectorDetail.class);
	private TaskFailedException taskExc;
	public void setId(String aId){ id = aId; isId=true;}
	public int getId() {return Integer.valueOf(id).intValue();}
	public void setBillCollectorID(String aBillCollectorID){ billCollectorId = aBillCollectorID; updateQuery = updateQuery + " billCollectorId=" + billCollectorId + ","; isField = true;}
	public void setWardId(String aWardId){ wardId = aWardId; updateQuery = updateQuery + " wardId=" + wardId + ","; isField = true;}

	public void insert(Connection connection) throws SQLException,TaskFailedException
	{		
		
		Statement statement = null;
		try{
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("BillCollectorDetail")) );
		
		String insertQuery = "INSERT INTO billcollectorDetail (Id, BillCollectorID, WardId) values (" + id + ", " + billCollectorId + ", " + wardId + ")";
		LOGGER.debug(insertQuery);
		statement = connection.createStatement();
		statement.executeUpdate(insertQuery);
		}
		catch(Exception e){
			LOGGER.error("Exp in insert :"+e.getMessage());
			throw taskExc;
		}finally{
			statement.close();
		}
	}
	
	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{	
			Statement statement = null;
			try{
				updateQuery = updateQuery.substring(0,updateQuery.length()-1);
				updateQuery = updateQuery + " WHERE id = " + id;
				LOGGER.debug(updateQuery);
				
				statement = connection.createStatement();
				statement.executeUpdate(updateQuery);
			}
			catch(Exception e){
				LOGGER.error("Exp in insert :"+e.getMessage());
				throw taskExc;
			}finally{
				statement.close();
			}
			updateQuery="UPDATE billcollectordetail SET";
		}
	}
}

