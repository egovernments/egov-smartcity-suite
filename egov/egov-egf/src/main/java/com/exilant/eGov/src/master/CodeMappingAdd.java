package com.exilant.eGov.src.master;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.domain.*;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.common.DataCollection;


public class CodeMappingAdd extends AbstractTask {
	private static final Logger LOGGER = Logger.getLogger(CodeMappingAdd.class);
	private Connection connection;
	private DataCollection dc;
	
	
	
	public void execute(String taskName,
						String gridName,
						DataCollection datacollection, 
						Connection conn,
						boolean errorOnNoData,
						boolean gridHasColumnHeading, String prefix) throws TaskFailedException{
		
			dc=datacollection;
			this.connection=conn;	
		try{
				if(dc.getValue("modeOfExec").equalsIgnoreCase("Add"))
					{
						postCashChequeDetails();
						dc.addMessage("eGovSuccess", "Adding CodeMapping.");
					}
				
				else if(dc.getValue("modeOfExec").equalsIgnoreCase("Modify"))
					{
						updateCashChequeDetails();
						dc.addMessage("eGovSuccess", "Updating CodeMapping.");
					}
			}
			catch(SQLException sqlex ){
				if(LOGGER.isDebugEnabled())     LOGGER.debug("ERROR IN POSTING : " + sqlex.toString());
				dc.addMessage("exilSQLError",sqlex.getMessage());
				throw new TaskFailedException(sqlex);
			}
		}
	
	private void postCashChequeDetails() throws SQLException
	{
		
		CodeMapping CM= new CodeMapping();
		//CM.setID((String)dc.getValue("CodeMapping_ID"));
		CM.setegBoundaryID((String)dc.getValue("AdminBoundaryId"));
		CM.setcashInHand((String)dc.getValue("cashInHandId"));
		CM.setchequeInHand((String)dc.getValue("ChequeInHandId"));	
		CM.insert(connection);	
	
	}
	private void updateCashChequeDetails() throws SQLException
	{
		//if(LOGGER.isDebugEnabled())     LOGGER.debug("inside the updateCashChequeDetails codemappingadd))))))))))))))))");
		CodeMapping CM= new CodeMapping();
		//if(LOGGER.isDebugEnabled())     LOGGER.debug("aftr codemapping class))))))))))))))))");
		CM.setID((String)dc.getValue("ID"));
		CM.setegBoundaryID((String)dc.getValue("AdminBoundaryId"));
		CM.setcashInHand((String)dc.getValue("cashInHandId"));
		CM.setchequeInHand((String)dc.getValue("ChequeInHandId"));	
		CM.update(connection);	
		//if(LOGGER.isDebugEnabled())     LOGGER.debug("aftr CM.update(connection)))))))))))))))))");
		
	}
		
}
	
//end of class



	
