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

