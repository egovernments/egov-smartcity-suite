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

