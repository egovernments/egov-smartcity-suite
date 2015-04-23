package com.exilant.eGov.src.master;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class MaxAmount extends AbstractTask {

	private static final Logger LOGGER = Logger.getLogger(MaxAmount.class);
	private DataCollection dc;



	public void execute(String taskName,
						String gridName,
						DataCollection datacollection,
						Connection conn,
						boolean errorOnNoData,
						boolean gridHasColumnHeading, String prefix) throws TaskFailedException{

			dc=datacollection;
		try{
				dc.addValue("MAXAmount",getMaxAmount());
		  	}
			catch(SQLException sqlex ){
				if(LOGGER.isDebugEnabled())     LOGGER.debug("ERROR IN POSTING : " + sqlex.toString());
				dc.addMessage("eGovFailure"," ");
				throw new TaskFailedException(sqlex);
			}
		}

		public String getMaxAmount()throws SQLException{
			String MAXCODELEN=EGovConfig.getProperty("egf_config.xml","Length","","MAXAmount");
			//if(LOGGER.isDebugEnabled())     LOGGER.debug("++++++++++++++++++++++++++++++++++"+MAXCODELEN);
			return MAXCODELEN;
		}







}


//end of class




