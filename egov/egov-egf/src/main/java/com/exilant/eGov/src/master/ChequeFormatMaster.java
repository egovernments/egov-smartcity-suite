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
/*
 * Created on Jul 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.master;
import java.sql.Connection;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.domain.ChequeFormatMasterBean;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
/**
 * @author Mani
 *
 * This Class Creates Cheque Printing Format for Specific Bank.
 *  
 */
public class ChequeFormatMaster extends AbstractTask 
{
	private final static Logger LOGGER=Logger.getLogger(ChequeFormatMaster.class);
	public void execute(String taskName,
			String gridName,
			DataCollection dc,
			Connection conn,
			boolean errorOnNoData,
			boolean gridHasColumnHeading, String prefix) throws TaskFailedException
			{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("inside ChequeFormat master");
			ChequeFormatMasterBean chequeFormatMasterBean=new ChequeFormatMasterBean();
			try
			{
				if (dc.getValue("modeOfExec").equalsIgnoreCase("modify"))
				{
					chequeFormatMasterBean.modify(conn,dc);
					dc.addMessage("eGovSuccess","CheckFormats update");	
				}
				else
				{
					chequeFormatMasterBean.insert(conn,dc);
					dc.addMessage("eGovSuccess","CheckFormats insert");	
				}
			
				return;
			}
			catch(Exception e)
			{
				dc.addMessage("exilRPError",e.getMessage());
				throw new TaskFailedException(e.toString());
			}
		/*	finally
			{
				//This fix is for Phoenix Migration.EgovDatabaseManager.releaseConnection(conn,null);
			}*/
	}
	
}
