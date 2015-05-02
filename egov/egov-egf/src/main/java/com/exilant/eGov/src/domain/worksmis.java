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
 * Created on Feb 28, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author Elzan Mathew
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class worksmis {

	private static final Logger LOGGER=Logger.getLogger(WorksDetail.class);
	private String id = null;
	private String worksdetailid = null;
	private String fieldid = null;
	private String schemeid = null;
	private String subschemeid = null;
	private String isFixedAsset = null;
	private String lastModifiedDate = "1-Jan-1900"; 
	EGovernCommon commonmethods = new EGovernCommon(); 
	private static TaskFailedException taskExc;
	private String updateQuery="UPDATE EGW_WORKS_MIS SET";
	public void setId(String aId){ id = aId;  }
	public int getId() {return Integer.valueOf(id).intValue(); }
	
	public void setIsFixedAsset(String isFixedAsset) {
		if(isFixedAsset != null && !("".equals(isFixedAsset))){
			this.isFixedAsset = isFixedAsset;
			updateQuery = updateQuery + " isfixedasset = " + this.isFixedAsset + ",";
		}
	}
	public void setWorksdetailid(String aRelationId){ worksdetailid = aRelationId;  updateQuery = updateQuery + " worksdetailid = " + worksdetailid + ",";  }
	public void setFieldid(String afieldid) {
		if(afieldid!=null && !afieldid.trim().equals(""))   	fieldid = afieldid;
	   	//if(LOGGER.isInfoEnabled())     LOGGER.info("Inside worksmis ..length of fieldid : "+fieldid.length());
	   //	if(fieldid==0) fieldid=null;
	   		updateQuery = updateQuery + " fieldid=" + fieldid + ",";
	   		 	}
	public void setSchemeid(String aschemeid) {
		
		if(aschemeid!=null && !aschemeid.trim().equals(""))   	schemeid = aschemeid;
		updateQuery = updateQuery + " schemeid=" + schemeid + ",";
	   	
	   	}
	public void setSubschemeid(String asubschemeid) {
		if(asubschemeid!=null && !asubschemeid.trim().equals(""))   	subschemeid = asubschemeid;
	  	updateQuery = updateQuery + " subschemeid=" + subschemeid + ",";
		
	}
	public void setLastModifiedDate(String aLastModifiedDate) {lastModifiedDate = aLastModifiedDate; updateQuery = updateQuery + " lastmodifieddate=to_date('"+lastModifiedDate+"','dd-Mon-yyyy HH24:MI:SS'),"; }
	
	/**
	 * Function to insert to egw_works_mis
	 * @param connection
	 * @throws TaskFailedException
	 */
	 public void insert(Connection connection)  throws TaskFailedException
	   {  
	 		PreparedStatement pst = null;
	   		lastModifiedDate = commonmethods.getCurrentDateTime();
	   		try{
		   		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
				lastModifiedDate = formatter.format(sdf.parse(lastModifiedDate));
				setLastModifiedDate(lastModifiedDate);
	   	  
		   		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("egw_works_mis")) );
		   	
		   		String insertQuery = "INSERT INTO egw_works_mis(Id, worksdetailid, fieldid,lastmodifieddate,schemeid,subschemeid,isfixedasset) " +
				"VALUES ( ?, ?, ?,to_date('" + lastModifiedDate + "','dd-Mon-yyyy HH24:MI:SS'), ?, ?, ?)";
	
		   		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		   		pst = connection.prepareStatement(insertQuery);
		   		pst = connection.prepareStatement(insertQuery);
				pst.setString(1, id);
				pst.setString(2, worksdetailid);
				pst.setString(3, fieldid);
				pst.setString(4, schemeid);
				pst.setString(5, subschemeid);
				pst.setString(6, isFixedAsset);
	   			pst.executeUpdate();
	   		}catch(Exception e){LOGGER.error(e.getMessage());
	   			throw taskExc;
	   		}finally{
	   			try{pst.close();
	   		}catch(Exception e){LOGGER.error(e.getMessage());
	   			}
	   		}
	   }
	/**
	 * Function to Update egw_works_mis
	 * @param connection
	 * @throws TaskFailedException
	 */
	 public void update (Connection connection) throws TaskFailedException
	   {
	   		lastModifiedDate = commonmethods.getCurrentDateTime();
	   		PreparedStatement pst = null;
	   		try{
		   		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
				lastModifiedDate = formatter.format(sdf.parse(lastModifiedDate));
				setLastModifiedDate(lastModifiedDate);
				
				updateQuery = updateQuery.substring(0,updateQuery.length()-1);
				updateQuery = updateQuery + " WHERE worksdetailid = ?";
				if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
				pst = connection.prepareStatement(updateQuery);
				pst.setString(1, worksdetailid);
				pst.executeUpdate();
				
				updateQuery="UPDATE voucherheader SET";
				
		   	}catch(Exception e){
				LOGGER.error("Error in update: "+e);
				throw taskExc;
			}finally{
	   			try{pst.close();
		   		}catch(Exception e){LOGGER.error(e.getMessage());
		   			}
		   		}
	   }
	
}
