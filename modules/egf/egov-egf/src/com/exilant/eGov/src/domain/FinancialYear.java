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
import com.exilant.exility.common.TaskFailedException;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import org.apache.log4j.Logger;
import org.egov.infstr.utils.EgovMasterDataCaching;

public class FinancialYear
{
	private String id =null;
	private String financialYear =null;
	private String startingDate = "1-Jan-1900";
	private String endingDate = "1-Jan-1900";
	private String isActive = "1";
	private String created = "1-Jan-1900";
	private String lastModified = "1-Jan-1900";
	private String modifiedBy =null;
	private String isActiveForPosting = "0";
	private String isClosed = "0";
	private String TransferClosingBalance = "0";
	private static final Logger LOGGER=Logger.getLogger(FinancialYear.class);
	private String updateQuery="UPDATE FinancialYear SET";
	private boolean isId=false, isField=false;

	public void setId(String aId){ id = aId;  isId=true; }
	public int getId() {return Integer.valueOf(id).intValue(); }
	public void setFinancialYear(String aFinancialYear){ financialYear = aFinancialYear;  updateQuery = updateQuery + " financialyear = '" + financialYear + "',"; isField = true; }
	public void setStartingDate(String aStartingDate){ startingDate= aStartingDate;  updateQuery = updateQuery + " startingdate = '" + startingDate + "',"; isField = true; }
	public void setEndingDate(String aEndingDate){ endingDate = aEndingDate;  updateQuery = updateQuery + " endingdate = '" + endingDate + "',"; isField = true; }
	public void setIsActive(String aIsActive){ isActive = aIsActive;  updateQuery = updateQuery + " isactive = " + isActive + ","; isField = true; }
	public void setCreated(String aCreated){ created = aCreated;  updateQuery = updateQuery + " created = '" + created + "',"; isField = true; }
	public void setLastModified(String aLastModified){ lastModified = aLastModified;  updateQuery = updateQuery + " lastmodified = '" + lastModified + "',"; isField = true; }
	public void setModifiedBy(String aModifiedBy){ modifiedBy = aModifiedBy;  updateQuery = updateQuery + " modifiedby = " + modifiedBy + ","; isField = true; }
	public void setIsActiveForPosting(String aIsActiveForPosting){ isActiveForPosting = aIsActiveForPosting;  updateQuery = updateQuery + " isActiveForPosting = " + isActiveForPosting + ","; isField = true; }
	public void setIsClosed(String aIsClosed){ isClosed = aIsClosed;  updateQuery = updateQuery + " isClosed = " + isClosed + ","; isField = true; }
	public void setTransferClosingBalance(String aTransferClosingBalance){TransferClosingBalance = aTransferClosingBalance;  updateQuery = updateQuery + " TransferClosingBalance = " + TransferClosingBalance + ","; isField = true; }

	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate(connection);
		try
   		{
   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			created = formatter.format(sdf.parse( created ));
			EgovMasterDataCaching.getInstance().removeFromCache("egi-activeFinYr");
   		}
   		catch(Exception e){
   			LOGGER.error("Exp in insert to financialyear: "+e.getMessage());
   			throw new TaskFailedException();
   		}
		setCreated(created);
		setLastModified(created);
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("FinancialYear")) );

		String insertQuery = "INSERT INTO FinancialYear (id, financialyear, startingdate, endingdate, " +
								"isactive, created, lastmodified, MODIFIEDBY, isActiveForPosting, isClosed, TransferClosingBalance) " +
								"VALUES (" + id + ", '" + financialYear + "', '" + startingDate + "', '"
								+ endingDate + "', " + isActive + ", '" + created + "', '"
								+ lastModified + "', " + modifiedBy + ", " + isActiveForPosting + ", " + isClosed + ", " + TransferClosingBalance + ")";

		Statement statement = connection.createStatement();
		LOGGER.info(insertQuery);
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
				EgovMasterDataCaching.getInstance().removeFromCache("egi-activeFinYr");
	   		}catch(Exception e){
	   			LOGGER.error("Exp in update financialyear: "+e.getMessage());
	   			throw new TaskFailedException();
	   		}
	   		setLastModified(created);
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;
			LOGGER.info(updateQuery);
			Statement statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery="UPDATE FinancialYear SET";

		}
	}
}

