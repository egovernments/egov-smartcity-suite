package com.exilant.eGov.src.domain;

import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.*;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;
public class EgfTaxCodeMapping
{
	private String id = null;
	private String financialYear = null;
	private String financialYearId = null;
	private String taxCodeId = null;
	private String glCodeId = null;
	private String isActive = "1";
	private String isOld=null;
	private String updateQuery="UPDATE EGF_TAX_ACCOUNT_MAPPING SET";
	private boolean isId=false, isField=false;
	private static final Logger LOGGER = Logger.getLogger(EgfTaxCodeMapping.class);
	public void setId(String aId){ id = aId; isId=true;}
	public int getId() {return Integer.valueOf(id).intValue(); }
	public void setFinancialYear(String aFinancialYear){ financialYear = aFinancialYear; updateQuery = updateQuery + " financialYear='" + financialYear + "',"; isField = true;}
	public void setFinancialYearId(String aFinancialYearId){ financialYearId = aFinancialYearId; updateQuery = updateQuery + " financialYearId='" + financialYearId + "',"; isField = true;}
	public void setTaxCodeId(String aTaxCodeId){ taxCodeId = aTaxCodeId; updateQuery = updateQuery + " taxCodeId=" + taxCodeId + ","; isField = true;}
	public void setGlCodeId(String aGlCodeId){ glCodeId = aGlCodeId; updateQuery = updateQuery + " glCodeId=" + glCodeId + ","; isField = true;}
	public void setIsActive(String aIsActive){ isActive = aIsActive; updateQuery = updateQuery + " isActive=" + isActive + ","; isField = true;}
	public void setIsOld(String aIsOld){ isOld = aIsOld; updateQuery = updateQuery + " isOld=" + isOld + ","; isField = true;}

	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		if(LOGGER.isInfoEnabled())     LOGGER.info("financialYearId  "+financialYearId);
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("EGF_TAX_ACCOUNT_MAPPING")) );
		String insertQuery = "INSERT INTO EGF_TAX_ACCOUNT_MAPPING (Id, financialyear, taxcodeid, glcodeid, isactive,FINANCIALYEARID,ISOLD )" +
						"VALUES (" + id + ",'" +  financialYear+ "'," + taxCodeId + ", " + glCodeId + ", " + isActive	+","+financialYearId+","+isOld+")";
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
			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery="UPDATE EGF_TAX_ACCOUNT_MAPPING SET";
		}
	}


	}

