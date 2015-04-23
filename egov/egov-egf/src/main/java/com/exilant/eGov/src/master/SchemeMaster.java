/*
 * Created on Jul 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.master;
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.database.utils.EgovDatabaseManager;

import com.exilant.eGov.src.domain.SchemeMasterBean;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SchemeMaster extends AbstractTask{
	private final static Logger LOGGER=Logger.getLogger(SchemeMaster.class);
	
	public void execute(String taskName,String gridName,DataCollection dc,Connection conn,
			boolean errorOnNoData,boolean gridHasColumnHeading, String prefix) throws TaskFailedException{
		if(LOGGER.isInfoEnabled())     LOGGER.info("inside scheme master");
		SchemeMasterBean schemeMasterBean=new SchemeMasterBean();
		try{
			if (dc.getValue("modeOfExec").equalsIgnoreCase("modify")){
				schemeMasterBean.modify(conn,dc);
				dc.addMessage("eGovSuccess","Scheme-Update");
			}else{
				schemeMasterBean.insert(conn,dc);
				dc.addMessage("eGovSuccess","Scheme-Create");
			}
			return;
		}catch(Exception e){
//			dc.addMessage("exilRPError",e.getMessage());
			throw new TaskFailedException(e.toString());
		}finally{
			EgovDatabaseManager.releaseConnection(conn,null);
		}
	}
	
}
