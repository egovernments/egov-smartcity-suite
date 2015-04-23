package com.exilant.eGov.src.domain;

import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.*;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;

public class EgfCessMaster
{
	private String id = null;
	private String financialYearId = null;
	private String taxCodeId = null;
	private String glCodeId = null;
	private String isActive = "1";
	private String percentage="0";
	private String updateQuery="UPDATE EGF_CESS_MASTER SET";	
	private boolean isId=false, isField=false;
	private static final Logger LOGGER=Logger.getLogger(EgfCessMaster.class);
	
	public void setId(String aId){ id = aId; isId=true;}
	public int getId() {return Integer.valueOf(id).intValue(); }
	public void setFinancialYearId(String aFinancialYearId){ financialYearId = aFinancialYearId; updateQuery = updateQuery + " financialYearId='" + financialYearId + "',"; isField = true;}
	public void setTaxCodeId(String aTaxCodeId){ taxCodeId = aTaxCodeId; updateQuery = updateQuery + " taxCodeId=" + taxCodeId + ","; isField = true;}
	public void setGlCodeId(String aGlCodeId){ glCodeId = aGlCodeId; updateQuery = updateQuery + " glCodeId=" + glCodeId + ","; isField = true;}
	public void setIsActive(String aIsActive){ isActive = aIsActive; updateQuery = updateQuery + " isActive=" + isActive + ","; isField = true;}
	public void setPercentage(String aPercentage){ percentage = aPercentage; updateQuery = updateQuery + " percentage=" + percentage + ","; isField = true;}
	
	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("EGF_CESS_MASTER")) );		
		String insertQuery = "INSERT INTO EGF_CESS_MASTER (Id, financialyearid, taxcodeid, glcodeid, isactive ,percentage)" + 
						"VALUES (" + id + ", '" +  financialYearId+ "', " + taxCodeId + ", " + glCodeId + ", " + isActive+","+percentage+")";
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		Statement statement = connection.createStatement();
		statement.executeUpdate(insertQuery);
		statement.close();
	}
	
	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;
			Statement statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			statement.close();
			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			updateQuery="UPDATE EGF_CESS_MASTER SET";			
		}
	}
	
	 
	}

