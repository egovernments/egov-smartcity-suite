/*
 * Created on Mar 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class AccountDtlKey {
	private final static Logger LOGGER=Logger.getLogger(AccountDtlKey.class);
	private String id = "";
	private String groupID = "1";
	private String glCodeID = null;
	private String detailTypeId = null;
	private String detailName = "";
	private String detailKey = null;
	private TaskFailedException taskExc;

	//private String insertQuery;
	private boolean isId=false, isField=false;
	private String updateQuery="UPDATE accountdetailkey SET";
	
	public void setID(final String aID){ id = aID;isId=true; }
	public void setgroupID(final String agroupID){ groupID = agroupID; updateQuery = updateQuery + " groupId=" +groupID + ","; isField = true;}
	public void setglCodeID(final String aglCodeID){ glCodeID = aglCodeID; updateQuery = updateQuery + " glCodeID=" +glCodeID + ","; isField = true;}
	public void setdetailTypeId(final String adetailTypeId){ detailTypeId = adetailTypeId; updateQuery = updateQuery + " detailTypeId=" +detailTypeId + ","; isField = true;}
	public void setdetailName(final String adetailName){ detailName = adetailName; updateQuery = updateQuery + " detailName='" +detailName + "',"; isField = true;}
	public void setdetailKey(final String adetailKey){ detailKey = adetailKey; updateQuery = updateQuery + " detailKey=" +detailKey + ","; isField = true;}
	
	public void insert(final Connection connection) throws SQLException,TaskFailedException
	{
		Statement statement =null;
		try{
			
		setID( String.valueOf(PrimaryKeyGenerator.getNextKey("AccountDetailKey")) );		
		String insertQuery = "insert into AccountDetailKey (id,groupID, glCodeID, detailTypeId, detailName, detailKey)"+
		" values ("+ id +"," + groupID + ", " + glCodeID + ", " + detailTypeId + ", '" + 
			detailName + "', " + detailKey + ")";
		LOGGER.debug(insertQuery);		
		statement = connection.createStatement();
		int ret = statement.executeUpdate(insertQuery);
		//LOGGER.debug(insertQuery);
		if (ret == 1)
		{
			LOGGER.debug("SUCCESS INSERTING INTO ADK");
		}
		}catch(Exception e)
		{
			LOGGER.error("Exception in insert:"+e.getMessage());
			throw taskExc;
		}finally{
			try{
				statement.close();
			}catch(Exception e){LOGGER.error("Inside finally block");}
		}
	}
	
	public void update(final Connection connection) throws SQLException,TaskFailedException{
		Statement statement =null;
		try{
			
		if(isId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;
			statement = connection.createStatement();
			LOGGER.debug(updateQuery);
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery="UPDATE accountdetailkey SET";
		}
		}catch(Exception e)
		{
			LOGGER.error("Exception in update:"+e.getMessage());
			throw taskExc;
		}finally{
			try{
				statement.close();
			}catch(Exception e){LOGGER.error("Inside finally block");}
		}
	}
}
