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
				EgovDatabaseManager.releaseConnection(conn,null);
			}*/
	}
	
}
