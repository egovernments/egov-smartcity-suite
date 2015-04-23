package com.exilant.eGov.src.master;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.exilant.eGov.src.domain.*;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.common.DataCollection;

public class CashInHandCode extends AbstractTask {
	private final static Logger LOGGER=Logger.getLogger(CashInHandCode.class);
	
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
				dc.addValue("purposeIdCs",getPurposeID());
		  	}
			catch(SQLException sqlex ){
				if(LOGGER.isDebugEnabled())     LOGGER.debug("ERROR IN POSTING : " + sqlex.toString());
				dc.addMessage("eGovFailure"," ");
				throw new TaskFailedException(sqlex);
			}
		}
		
		public String getPurposeID()throws SQLException{
			String purposeID=EGovConfig.getProperty("egf_config.xml","PURPOSEID","","CashInHand");
			if(LOGGER.isDebugEnabled())     LOGGER.debug("++++++++++++++++++++++++++++++++++"+purposeID);
			return purposeID;
		}
					
				
				

	
			
	
}
				
	
//end of class


