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
package com.exilant.eGov.src.domain;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
@Transactional(readOnly=true)
public class Asset
{
	private final static Logger LOGGER=Logger.getLogger(Asset.class);
	private TaskFailedException taskExc;
	private final static String NULLSTRING="null";
	private String id = "";
	private String code = "";
	private String name = "";
	private String parentId = NULLSTRING;
	private String status = "0";
	private String typeId = NULLSTRING;
	private String description = "";
	private String assetModeId = NULLSTRING;
	private String departmentId = NULLSTRING;
	private String initVal = "0";
	private String commDate = "";
	//private String isActive = "1";
	private String maxShifts = "0";
	private String location = NULLSTRING;
	private String hasDocs = "0";
	private String surveyNo = "";
	private String dimensions = "";
	private String landId = NULLSTRING;
	private String used = "0";
	private String updateQuery="UPDATE Asset SET";
	private boolean isId=false, isField=false;


	public void setId(String aId){ id = aId;  isId=true; }
	public int getId() {return Integer.valueOf(id).intValue(); }
	public void setCode(String aCode){ code = aCode;  updateQuery = updateQuery + " code = '" + code + "',"; isField = true; }
	public void setName(String aName){ name = aName;  updateQuery = updateQuery + " name = '" + name + "',"; isField = true; }
	public void setParentId(String aParentId){ parentId = aParentId;  updateQuery = updateQuery + " ParentId = " + aParentId + ","; isField = true; }
	public void setStatus(String aStatus){ status = aStatus;  updateQuery = updateQuery + " status = " + status + ","; isField = true; }
	public void setTypeId(String aAssetType){ typeId = aAssetType;  updateQuery = updateQuery + " TypeId = " + aAssetType + ","; isField = true; }
	public void setDescription(String aDescription){ description = aDescription;  updateQuery = updateQuery + " description = '" + description + "',"; isField = true; }
	public void setAssetModeId(String aModeId){ assetModeId = aModeId;  updateQuery = updateQuery + " assetModeId = " + assetModeId + ","; isField = true; }
	public void setDepartmentId(String aDepartmentId){ departmentId = aDepartmentId; updateQuery = updateQuery + " DepartmentId = " + aDepartmentId + ","; isField = true; }
	public void setLocation(String aLocation){ location = aLocation;  updateQuery = updateQuery + " Location = " + aLocation + ","; isField = true; }
	public void setInitVal(String aInitVal){ initVal = aInitVal;  updateQuery = updateQuery + " initVal = " + initVal + ","; isField = true; }
	public void setCommencingDate(String aCommDate){ commDate = aCommDate;  updateQuery = updateQuery + " commDate = '" + aCommDate + "',"; isField = true; }
	public void setMaxShifts(String aMaxShifts){ maxShifts = aMaxShifts;  updateQuery = updateQuery + " maxShifts = " + maxShifts + ","; isField = true; }
	public void setHasDocs(String aHasDocs){ hasDocs = aHasDocs;  updateQuery = updateQuery + " hasDocs = " + hasDocs + ","; isField = true; }
	public void setSurveyNo(String aSurveyNo){ surveyNo = aSurveyNo;  updateQuery = updateQuery + " surveyNo = '" + surveyNo + "',"; isField = true; }
	public void setDimensions(String aDimensions){ dimensions = aDimensions;  updateQuery = updateQuery + " dimensions = '" + dimensions + "',"; isField = true; }
	public void setLandId(String aLandId){ landId = aLandId;  updateQuery = updateQuery + " landId = " + landId + ","; isField = true; }
	public void setUsed(String aUsed){ used = aUsed;  updateQuery = updateQuery + " used = " + used + ","; isField = true; }
	@Transactional
	public void insert() throws SQLException,TaskFailedException
	{
		Query statement = null;
		try{
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("Asset")) );

		String insertQuery = "INSERT INTO Asset (id, parentId,typeid, code, name, description, " +
								"status,landId ,assetmodeid, departmentid, location, initVal, " +
								"commDate, maxShifts, hasDocs, surveyNo, dimensions,used) " +
								"VALUES (" + id + ", " + parentId + ", " +  typeId + ", '" 
								+ code + "', '" + name + "', '" + description + "', " + status + ", " 
								+ landId + ", " + assetModeId + ", " + departmentId + ", " 
								+ location + ", " + initVal + ", '" + commDate + "', " 
								+ maxShifts + ", " + hasDocs + ", '" + surveyNo + "', '" 
								+ dimensions   + "',"+used+")";
		if(LOGGER.isDebugEnabled())     LOGGER.debug(insertQuery);
		
		statement = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
		statement.executeUpdate();
		}catch(Exception e)
		{
			LOGGER.error("Exception in insert:"+e.getMessage());
			throw taskExc;
		}
	
		
	}
	@Transactional
	public void update () throws SQLException,TaskFailedException
	{
		Query statement =null;
		try{
		if(isId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;
			if(LOGGER.isDebugEnabled())     LOGGER.debug(updateQuery);
			statement = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
			statement.executeUpdate();
			updateQuery="UPDATE Asset SET";
			if(LOGGER.isDebugEnabled())     LOGGER.debug(updateQuery);
		}
	}catch(Exception e)
	{
		LOGGER.error("Exception in update:"+e.getMessage());
		throw taskExc;
	}

	}
}

