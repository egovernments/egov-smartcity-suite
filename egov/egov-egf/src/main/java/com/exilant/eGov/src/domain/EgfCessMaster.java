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
	@Transactional
	public void insert() throws SQLException,TaskFailedException
	{
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("EGF_CESS_MASTER")) );		
		String insertQuery = "INSERT INTO EGF_CESS_MASTER (Id, financialyearid, taxcodeid, glcodeid, isactive ,percentage)" + 
						"VALUES (" + id + ", '" +  financialYearId+ "', " + taxCodeId + ", " + glCodeId + ", " + isActive+","+percentage+")";
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		Query statement = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
		statement.executeUpdate();
	}
	@Transactional
	public void update () throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;
			Query statement = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
			statement.executeUpdate();
			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			updateQuery="UPDATE EGF_CESS_MASTER SET";			
		}
	}
	
	 
	}

