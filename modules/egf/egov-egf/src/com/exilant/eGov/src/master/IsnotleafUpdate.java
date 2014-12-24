package com.exilant.eGov.src.master;
import java.sql.Connection;
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



	public void execute(String taskName,
						String gridName,
						DataCollection datacollection,
						Connection conn,
						boolean errorOnNoData,
						boolean gridHasColumnHeading, String prefix) throws TaskFailedException{

			dataCollection=datacollection;
			this.connection=conn;
		try{
			isnotleafUpdateF();
		  	}
			catch(SQLException sqlex ){
				LOGGER.debug("ERROR IN POSTING : " + sqlex.toString());
				dataCollection.addMessage("eGovFailure"," ");
				throw new TaskFailedException(sqlex);
			}
		}

		public void isnotleafUpdateF()throws SQLException{
			final Statement statement;
			statement=connection.createStatement();
			String qryString = "";
			final String tName=dataCollection.getValue("tableName");

					qryString = "Update "+tName+" set isnotleaf = 0";

					statement.executeQuery(qryString);


					qryString = "Update "+tName+" set isnotleaf = 1 where ID in (Select unique parentID from "+tName+")";

					statement.executeQuery(qryString);
					statement.close();



	}// end of Isnotleaf
}


//end of class
