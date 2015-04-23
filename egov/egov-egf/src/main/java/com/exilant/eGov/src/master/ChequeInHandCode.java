package com.exilant.eGov.src.master;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class ChequeInHandCode extends AbstractTask {
	private final static Logger LOGGER=Logger.getLogger(ChequeInHandCode.class);
	private DataCollection dc;



	public void execute(String taskName,
						String gridName,
						DataCollection datacollection,
						Connection conn,
						boolean errorOnNoData,
						boolean gridHasColumnHeading, String prefix) throws TaskFailedException{

			dc=datacollection;
		try{
				dc.addValue("purposeIdCq",getPurposeID());
		  	}
			catch(SQLException sqlex ){
				if(LOGGER.isDebugEnabled())     LOGGER.debug("ERROR IN POSTING : " + sqlex.toString());
				dc.addMessage("eGovFailure"," ");
				throw new TaskFailedException(sqlex);
			}
		}

		public String getPurposeID()throws SQLException{
			String purposeID=EGovConfig.getProperty("egf_config.xml","PURPOSEID","","ChequeInHand");
			if(LOGGER.isDebugEnabled())     LOGGER.debug("purposeID   "+purposeID);
			return purposeID;
		}







}


//end of class




