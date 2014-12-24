/*
 * Created on Mar 4, 2005
 * @author pushpendra.singh 
 */

package com.exilant.eGov.src.domain;

import com.exilant.eGov.src.common.*;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import com.exilant.exility.common.TaskFailedException;

public class ChartOfAccountDetail {
	private String id = null;
	private String glCodeId =null;
	private String detailTypeId =null;
	private static final Logger LOGGER=Logger.getLogger(ChartOfAccountDetail.class);
	private TaskFailedException taskExc;	
	private String updateQuery="UPDATE ChartOfAccountDetail SET";
	private boolean isId=false, isField=false;
	EGovernCommon cm = new EGovernCommon();	
	
	public void setId(String aId){ id = aId; isId=true;}
	public int getId() {return Integer.valueOf(id).intValue(); }
	public void setGLCodeId(String aGLCodeId){ glCodeId = aGLCodeId; updateQuery = updateQuery + " glCodeId=" + glCodeId + ","; isField = true;}
	public void setDetailTypeId(String aDetailTypeId){ detailTypeId = aDetailTypeId; updateQuery = updateQuery + " detailTypeId=" + detailTypeId + ","; isField = true;}
	
	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		Statement statement =null;
		try{		
			setId( String.valueOf(PrimaryKeyGenerator.getNextKey("ChartOfAccountDetail")) );			
			String insertQuery = "INSERT INTO ChartOfAccountDetail (id, glCodeId, detailTypeId)" + 
									"VALUES(" + id + ", " + glCodeId + ", " + detailTypeId + ")";			 
		    statement = connection.createStatement();
		    LOGGER.debug(insertQuery);	    
		    statement.executeUpdate(insertQuery);
		}catch(Exception e){
			LOGGER.error("Exp in insert:"+e.getMessage());
			throw taskExc;			
		}finally{
			statement.close();
		}
	    
	}
	
	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{	
			Statement statement =null;
			try{
				updateQuery = updateQuery.substring(0,updateQuery.length()-1);
				updateQuery = updateQuery + " WHERE id = " + id;
				LOGGER.debug(updateQuery);
				statement = connection.createStatement();
				statement.executeUpdate(updateQuery);
			}catch(Exception e){
				LOGGER.error("Exp in update:"+e.getMessage());
				throw taskExc;			
			}finally{
				statement.close();
			}
			updateQuery="UPDATE ChartOfAccountDetail SET";
			
		}
	}
}