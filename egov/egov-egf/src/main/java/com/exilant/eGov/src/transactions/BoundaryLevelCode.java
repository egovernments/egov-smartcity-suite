package com.exilant.eGov.src.transactions;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class BoundaryLevelCode extends AbstractTask {
	private static final Logger LOGGER = Logger.getLogger(BoundaryLevelCode.class);
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
				dc.addValue("boundaryTypeId",getBoundaryLevelId());
		  	}
			catch(SQLException sqlex ){
				LOGGER.error("ERROR IN POSTING : " + sqlex.toString(),sqlex);
				dc.addMessage("eGovFailure"," ");
				throw new TaskFailedException(sqlex);
			}
		}

		public String getBoundaryLevelId()throws SQLException,TaskFailedException
		{
			PreparedStatement pstmt=null;
			String boundaryTypeId="";
			try{
				String boundaryTypeval=EGovConfig.getProperty("egf_config.xml","city","","BoundaryType");
				String sql1="select distinct ID_BNDRY_TYPE from eg_boundary_type where lower(name)=lower(?)";
				pstmt=connection.prepareStatement(sql1);
				pstmt.setString(1,boundaryTypeval);
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Sql1=="+sql1);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next())
					boundaryTypeId = rs.getString(1);
				rs.close();
				pstmt.close();
			}
			catch(Exception e){
				dc.addMessage("eGovFailure","Exception while getting the boundary type id");
				LOGGER.error("Exp="+e.getMessage(),e);
				throw new TaskFailedException(e.getMessage());
			}
			return boundaryTypeId;
		}

}

