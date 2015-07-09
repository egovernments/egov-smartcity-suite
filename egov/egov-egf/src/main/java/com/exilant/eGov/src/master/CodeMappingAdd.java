/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
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
		try {
			CM.insert();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	
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
		CM.update();	
		//if(LOGGER.isDebugEnabled())     LOGGER.debug("aftr CM.update(connection)))))))))))))))))");
		
	}
		
}
	
//end of class



	
