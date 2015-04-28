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
 * EgRemittanceGldtl.java Created on Oct 5, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish
 * @version 1.1  
 */

public class EgRemittanceGldtl
{
	EGovernCommon cm = new EGovernCommon();
	private String id = null;
	private String gldtlId = null;	
	private String gldtlAmt = "0";
	private String lastModifiedDate = "1-Jan-1900";
	private String remittedAmt = null;
	private String tdsId = null;
	private static final Logger LOGGER=Logger.getLogger(EgRemittanceGldtl.class);
	private static TaskFailedException taskExc;
	
	public void setId(String aId){ id = aId; }
	public void setGldtlId(String aGldtlId){ gldtlId = aGldtlId; }	
	public void setGldtlAmt(String aGldtlAmt){ gldtlAmt = aGldtlAmt; }
	public void setLastModifiedDate(String aLastModifiedDate){ lastModifiedDate = aLastModifiedDate; }
	public void setRemittedAmt(String aRemittedAmt){ remittedAmt = aRemittedAmt; }
	public void setTdsId(String aTdsId){ tdsId = aTdsId; }
	
	public int getId() {return Integer.valueOf(id).intValue(); }
	public String getGldtlId(){ return gldtlId ;}
	public String getGldtlAmt(){return gldtlAmt ; }
	public String getLastModifiedDate(){ return lastModifiedDate; }
	public String getRemittedAmt(){ return remittedAmt; }
	public String getTdsId(){ return tdsId; }
	
	public void insert(Connection connection) throws SQLException,TaskFailedException
	{						
		Query pstmt=null;
		EGovernCommon egc = new EGovernCommon();	
		lastModifiedDate = egc.getCurrentDateTime(connection);		
	   	try
	   	{	   		
	   		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			lastModifiedDate = formatter.format(sdf.parse(lastModifiedDate));
			setLastModifiedDate(lastModifiedDate);
	   	}
		catch(Exception e)
		{
			LOGGER.error("Exp in insert to remittance detail"+e.getMessage(),e);
			throw taskExc;
		}
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("eg_remittance_gldtl")) );
		
		String insertQuery = "INSERT INTO eg_remittance_gldtl (id, gldtlid, gldtlamt, lastmodifieddate, remittedamt, tdsid) " +		 
								"VALUES (?,?,?, to_date(?,'dd-Mon-yyyy HH24:MI:SS'),?,?)";		
		
		 pstmt =HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
		 pstmt.setString(1, id);
		 pstmt.setString(2, gldtlId);
		 pstmt.setString(3, gldtlAmt);
		 pstmt.setString(4, lastModifiedDate);
		 pstmt.setString(5, remittedAmt);
		 pstmt.setString(6, tdsId);
/*		 pstmt.executeQuery();
	     pstmt.close();*/
	     pstmt.executeUpdate();
	     
	}
}

