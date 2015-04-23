 

package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

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

	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		Statement statement = null;
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
		statement = connection.createStatement();
		statement.executeUpdate(insertQuery);
		}catch(Exception e)
		{
			LOGGER.error("Exception in insert:"+e.getMessage());
			throw taskExc;
		}finally{
			try{
				statement.close();
			}catch(Exception e){LOGGER.error("Inside finally block");}
		}
	
		
	}

	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		Statement statement =null;
		try{
		if(isId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;
			if(LOGGER.isDebugEnabled())     LOGGER.debug(updateQuery);
			statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			updateQuery="UPDATE Asset SET";
			if(LOGGER.isDebugEnabled())     LOGGER.debug(updateQuery);
		}
	}catch(Exception e)
	{
		LOGGER.error("Exception in update:"+e.getMessage());
		throw taskExc;
	}finally{
		try{
			statement.close();
		}catch(Exception e){LOGGER.error("Inside finally block");}
	}

	}
}

