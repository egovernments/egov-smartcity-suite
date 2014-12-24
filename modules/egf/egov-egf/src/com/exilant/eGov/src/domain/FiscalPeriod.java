/*
 * Created on Apr 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

public class FiscalPeriod
{
	private String id =null;
	private String type = null;
	private String name =null;
	private String startingDate = "1-Jan-1900";
	private String endingDate = "1-Jan-1900";
	private String parentId = null;
	private String isActiveForPosting = "0";
	private String isActive = "0";
	private String modifiedBy =null;
	private String lastModified = "";
	private String created = "1-Jan-1900";
	private String financialYearId =null;
	private static TaskFailedException taskExc;
	private String updateQuery="UPDATE FiscalPeriod SET";
	private boolean isId=false, isField=false;
	private static final Logger LOGGER=Logger.getLogger(FiscalPeriod.class);

	public void setId(String aId){ id = aId;  isId=true; }
	public int getId() {return Integer.valueOf(id).intValue(); }
	public void setType(String aType){ type = aType;  updateQuery = updateQuery + " type = " + type + ","; isField = true; }
	public void setName(String aName){ name = aName;  updateQuery = updateQuery + " name = '" + name + "',"; isField = true; }
	public void setStartingDate(String aStartingDate){ startingDate = aStartingDate;  updateQuery = updateQuery + " startingdate = '" + startingDate + "',"; isField = true; }
	public void setEndingDate(String aEndingDate){ endingDate = aEndingDate;  updateQuery = updateQuery + " endingdate = '" + endingDate + "',"; isField = true; }
	public void setParentid(String aParentId){ parentId = aParentId;  updateQuery = updateQuery + " parentid = " + parentId + ","; isField = true; }
	public void setIsActiveForPosting(String aIsActiveForPosting){ isActiveForPosting = aIsActiveForPosting;  updateQuery = updateQuery + " isactiveforposting = " + isActiveForPosting + ","; isField = true; }
	public void setIsActive(String aIsActive){ isActive = aIsActive;  updateQuery = updateQuery + " isactive = " + isActive + ","; isField = true; }
	public void setModifiedBy(String aModifiedBy){ modifiedBy = aModifiedBy;  updateQuery = updateQuery + " modifiedby = " + modifiedBy + ","; isField = true; }
	public void setLastModified(String aLastModified){ lastModified = aLastModified;  updateQuery = updateQuery + " lastmodified = '" + lastModified + "',"; isField = true; }
	public void setCreated(String aCreated){ created = aCreated;  updateQuery = updateQuery + " created = '" + created + "',"; isField = true; }
	public void setFinancialYearId(String aFinancialYearId){ financialYearId = aFinancialYearId;  updateQuery = updateQuery + " financialyearid = " + financialYearId + ","; isField = true; }

	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate(connection);
		try
   		{
   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			created = formatter.format(sdf.parse(created));
   		}catch(Exception e){throw taskExc;}
		setCreated(created);
		setLastModified(created);
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("FiscalPeriod")) );

		String insertQuery = "INSERT INTO FiscalPeriod (id, type, name, startingdate, endingdate, parentid, " +
								"isactiveforposting, isactive, modifiedby, lastmodified, created, FINANCIALYEARID) " +
								"VALUES (" + id + ", " + type + ", '" + name + "', '" + startingDate + "', '"
								+ endingDate + "', " + parentId + ", " + isActiveForPosting + ", "
								+ isActive + ", " + modifiedBy + ", '" + lastModified + "', '"
								+ created + "', " + financialYearId + ")";

		LOGGER.info("before : " + insertQuery);
		Statement statement = connection.createStatement();
		statement.executeUpdate(insertQuery);
		statement.close();
	}

	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{
			EGovernCommon commommethods = new EGovernCommon();
			created = commommethods.getCurrentDate(connection);
			try
	   		{
	   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				created = formatter.format(sdf.parse( created ));
	   		}catch(Exception e){LOGGER.error("Exp in update fiscalperiod: "+e.getMessage());
	   			throw taskExc;
			}
			setLastModified(created);
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;
			LOGGER.info(updateQuery);
			Statement statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery="UPDATE FiscalPeriod SET";
		}
	}
}

