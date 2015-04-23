package com.exilant.eGov.src.master;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;



public class IsnotleafUpdate extends AbstractTask {
	private static final Logger LOGGER = Logger.getLogger(IsnotleafUpdate.class);
	private transient Connection connection;
	private DataCollection dataCollection;
	private PreparedStatement pstmt = null;


	public void execute(String taskName,
						String gridName,
						DataCollection datacollection,
						Connection conn,
						boolean errorOnNoData,
						boolean gridHasColumnHeading, String prefix) throws TaskFailedException{

			dataCollection=datacollection;
			this.connection=conn;
			isnotleafUpdateF();
		}

		public void isnotleafUpdateF(){
			final Statement statement;
			ResultSet rs=null;
			String qryString = "";
			final String tName=dataCollection.getValue("tableName");
			try{	
					qryString = "Update ?  set isnotleaf = ?";
					pstmt = connection.prepareStatement(qryString);
					pstmt.setString(1,tName);
					pstmt.setInt(2,0);
					if(LOGGER.isInfoEnabled())     LOGGER.info(qryString);
					
					
					rs = pstmt.executeQuery();

					String subquery="Select unique parentID from ?" ;
					pstmt = connection.prepareStatement(qryString);
					pstmt.setString(1,qryString);
					qryString = "Update ? set isnotleaf = ? where ID in ?";
					pstmt = connection.prepareStatement(qryString);
					pstmt.setString(1,tName);
					pstmt.setInt(2,1);
					if(LOGGER.isInfoEnabled())     LOGGER.info(qryString);
					rs = pstmt.executeQuery();
					rs.close();
				}
			catch(SQLException sqlex ){
				sqlex.printStackTrace();
				if(LOGGER.isDebugEnabled())     LOGGER.debug("ERROR IN POSTING : " + sqlex.toString());
				sqlex.getMessage();
			}
	}// end of Isnotleaf
}


//end of class
