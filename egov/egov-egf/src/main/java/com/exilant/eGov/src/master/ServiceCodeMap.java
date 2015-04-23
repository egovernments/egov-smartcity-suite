package com.exilant.eGov.src.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.domain.CodeServiceMap;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class ServiceCodeMap extends AbstractTask {
	PreparedStatement pstmt=null; 
	ResultSet rset=null;
	private static final Logger LOGGER = Logger.getLogger(ServiceCodeMap.class);
	public void execute(String taskName, String gridName, DataCollection dc,
			Connection conn, boolean errorOnNoData,
			boolean gridHasColumnHeading, String prefix)
			throws TaskFailedException {
		try {
			postInCodeServiceMap(dc, conn);
			dc.addMessage("eGovSuccess", "Mapping");
		} catch (Exception sqlex) {
			LOGGER.error("eGovFailure"+sqlex.getMessage(),sqlex);
			dc.addMessage("eGovFailure", "Mapping failed");
			throw new TaskFailedException();
		}
	}

	public void postInCodeServiceMap(DataCollection dc, Connection conn)
			throws TaskFailedException {

		final String srvId = dc.getValue("ftService_id");
		String codeList[] = dc.getValueList("ftService_toCode");
		if (codeList == null) {
			codeList = new String[1];
			codeList[0] = dc.getValue("ftService_toCode");
		}
		try{
		String delQuery="delete FROM codeservicemap WHERE serviceid=?";
		pstmt=conn.prepareStatement(delQuery);
		pstmt.setString(1,srvId);
		rset=pstmt.executeQuery();
		CodeServiceMap csm = new CodeServiceMap();
		csm.setServiceId(srvId);
		for (int i = 0; i < codeList.length; i++) {
			csm.setGlCodeId(codeList[i]);
			csm.insert(conn);
		}
		rset.close();
		pstmt.close();
		}
		catch(SQLException ex){ 
			LOGGER.error("ERROR"+ex.getMessage(),ex);
		}
		
	}

}
