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
 * Created on Jan 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly=true)
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
	@Transactional
	public void insert() throws SQLException,TaskFailedException
	{
		EGovernCommon commommethods = new EGovernCommon();				
		created = commommethods.getCurrentDate();
		Query statement=null;
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
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug(insertQuery);
			statement = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
			statement.executeUpdate();
   		}
		catch(Exception e){
			LOGGER.error("Exp in insert :"+e.getMessage());
			throw taskExc;
		}
		
	}
	@Transactional
	public void update () throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{
			EGovernCommon commommethods = new EGovernCommon();
			created = commommethods.getCurrentDate();
			Query statement = null;
			try
	   		{
				created = formatter.format(sdf.parse(created));
				setLastModified(created);
				narration = commommethods.formatString(narration);
				updateQuery = updateQuery.substring(0,updateQuery.length()-1);
				updateQuery = updateQuery + " WHERE id = " + id;
				if(LOGGER.isDebugEnabled())     LOGGER.debug(updateQuery);
				statement = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
				statement.executeUpdate();
	   		}
			catch(Exception e){
				LOGGER.error("Exp in update :"+e.getMessage());
				throw taskExc;
			}
			updateQuery="UPDATE billcollector SET";
		}
	}
}
