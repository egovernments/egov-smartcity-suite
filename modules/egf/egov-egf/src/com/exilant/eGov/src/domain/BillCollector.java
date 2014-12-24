/*
 * Created on Jan 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;
import java.util.Locale;
/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class BillCollector
{
	private String id = null;
	private String code = null;
	private String name = null;
	private String departmentId = null;
	private String cashInHand = null;
	private String chequeInHand =null;
	private String type = null;
	private String zoneId = null;
	private String regionId =null;
	private String narration = null;
	private String isActive = "1";
	private String lastModified = "1-Jan-1900";
	private String created = "1-Jan-1900";
	private String modifiedBy = null;
	private static final Logger LOGGER=Logger.getLogger(BillCollector.class);
	private String updateQuery="UPDATE billcollector SET";
	private boolean isId=false, isField=false;
	private SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
	private TaskFailedException taskExc;
	
	public void setId(String aId){ id = aId; isId=true;}
	public int getId() {return Integer.valueOf(id).intValue();}
	public void setCode(String aCode){ code = aCode; updateQuery = updateQuery + " code='" + code + "',"; isField = true;}
	public void setName(String aName){ name = aName; updateQuery = updateQuery + " name='" + name + "',"; isField = true;}
	public void setDepartmentId(String aDepartmentId){ departmentId = aDepartmentId; updateQuery = updateQuery + " departmentId=" + departmentId + ","; isField = true;}
	public void setCashInHand(String aCashInHand){ cashInHand = aCashInHand; updateQuery = updateQuery + " cashInHand=" + cashInHand + ","; isField = true;}
	public void setChequeInHand(String aChequeInHand){ chequeInHand = aChequeInHand; updateQuery = updateQuery + " chequeInHand=" + chequeInHand + ","; isField = true;}
	public void setType(String aType){ type = aType; updateQuery = updateQuery + " type='" + type + "',"; isField = true;}
	public void setZoneId(String aZoneId){ zoneId = aZoneId; updateQuery = updateQuery + " zoneId=" + zoneId + ","; isField = true;}
	public void setRegionId(String aRegionId){ regionId = aRegionId; updateQuery = updateQuery + " regionId=" + regionId + ","; isField = true;}
	public void setNarration(String aNarration){ narration = aNarration; updateQuery = updateQuery + " narration='" + narration + "',"; isField = true;}
	public void setIsActive(String aIsActive){ isActive = aIsActive; updateQuery = updateQuery + " isActive=" + isActive + ","; isField = true;}
	public void setLastModified(String aLastModified){ lastModified = aLastModified; updateQuery = updateQuery + " lastModified='" + lastModified + "',"; isField = true;}
	public void setCreated(String aCreated){ created = aCreated; updateQuery = updateQuery + " created='" + created + "',"; isField = true;}
	public void setModifiedBy(String aModifiedBy){ modifiedBy = aModifiedBy; updateQuery = updateQuery + " modifiedBy=" + modifiedBy + ","; isField = true;}

	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		EGovernCommon commommethods = new EGovernCommon();				
		created = commommethods.getCurrentDate(connection);
		Statement statement=null;
		try
   		{
			created = formatter.format(sdf.parse(created));   			
			setCreated(created);
			setLastModified(created);				
			narration = commommethods.formatString(narration);
			setId( String.valueOf(PrimaryKeyGenerator.getNextKey("BillCollector")) );
			
			String insertQuery = "INSERT INTO billCollector (Id, Code, Name, DepartmentId, CashInHand, ChequeInHand, Type,ZoneId, RegionId, Narration, IsActive, LastModified, Created, ModifiedBy) "
							+ " values (" + id + ", '" + code + "', '" + name + "', " + departmentId + ", " 
							+ cashInHand + ", " + chequeInHand + ", '" + type + "', " + zoneId + ", " 
							+ regionId + ", '" + narration + "', " + isActive + ", '" + lastModified + "', '" 
							+ created + "', " + modifiedBy + ")";
			
			LOGGER.debug(insertQuery);
			statement = connection.createStatement();
			statement.executeUpdate(insertQuery);
   		}
		catch(Exception e){
			LOGGER.error("Exp in insert :"+e.getMessage());
			throw taskExc;
		}finally{
			statement.close();
		}
		
	}
	
	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{
			EGovernCommon commommethods = new EGovernCommon();
			created = commommethods.getCurrentDate(connection);
			Statement statement = null;
			try
	   		{
				created = formatter.format(sdf.parse(created));
				setLastModified(created);
				narration = commommethods.formatString(narration);
				updateQuery = updateQuery.substring(0,updateQuery.length()-1);
				updateQuery = updateQuery + " WHERE id = " + id;
				LOGGER.debug(updateQuery);
				statement = connection.createStatement();
				statement.executeUpdate(updateQuery);
	   		}
			catch(Exception e){
				LOGGER.error("Exp in update :"+e.getMessage());
				throw taskExc;
			}finally{
				statement.close();
			}
			updateQuery="UPDATE billcollector SET";
		}
	}
}
