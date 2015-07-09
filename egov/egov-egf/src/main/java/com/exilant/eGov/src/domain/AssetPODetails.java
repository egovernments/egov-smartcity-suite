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
 * Created on Jan 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.exilant.eGov.src.domain;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
@Transactional(readOnly=true)
public class AssetPODetails {
	private String id = null;
	private String assetid = null;
	private String workorderid = null;
	private String updateQuery = "UPDATE AssetPODetails SET";
	private boolean isId = false, isField = false;
	private final static Logger LOGGER = Logger.getLogger(AssetPODetails.class);
	private TaskFailedException taskExc;
   	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
	public void setId(String aId) {
		id = aId;
		isId = true;
	}
	
	public int getId() {
		return Integer.valueOf(id).intValue();
	}
	
	public void setAssetid(String aAssetid) {
		assetid = aAssetid;
		updateQuery = updateQuery + " assetid = " + assetid + ",";
		isField = true;
	}
	
	public void setWorkorderid(String aWorkorderid) {
		workorderid = aWorkorderid;
		updateQuery = updateQuery + " workorderid = " + workorderid + ",";
		isField = true;
	}
	@Transactional
	public void insert() throws TaskFailedException,
			SQLException {
		EGovernCommon commommethods = new EGovernCommon();
		Query statement=null;
		setId(String.valueOf(PrimaryKeyGenerator.getNextKey("assetPODetails")));
		try {
			String lastupdateddate = String.valueOf(commommethods.getCurrentDate());			
			lastupdateddate = formatter.format(sdf.parse(lastupdateddate));
			String insertQuery = "INSERT INTO eg_asset_PO (id, assetid, workorderid, lastupdateddate)VALUES ("+ id+ ", "+ assetid+ ", "+ workorderid+ ", '"	+ lastupdateddate + "', ')";
			statement = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
			if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
			statement.executeUpdate();
		
		} catch (Exception e) {
			LOGGER.error("Exp in insert:"+e.getMessage());
			throw taskExc;
		}
	}
	
	public void update() throws Exception{
		Query statement=null;
		if(isId && isField) {
			updateQuery = updateQuery.substring(0, updateQuery.length() - 1);
			updateQuery = updateQuery + " WHERE id = " + id;
			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			try{
			statement = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
			statement.executeUpdate();
			updateQuery = "UPDATE eg_asset_PO SET";
			}catch(Exception e)
			{
				LOGGER.error("Error while updating records to eg_asset_PO");
				throw e;
			}
		}
	}
	
	/**
	 * This function will accept the assetid and returns the Fundid related to
	 * that asset.
	 */
	public int getFundForAsset(int assetid) throws Exception {
		int fundid = 0;
		List<Object[]> rs = null;
		Query statement = null;
		try{
		String qry = "Select fundid AS \"fund\" from worksdetail a,eg_asset_PO b where a.id=b.workorderid and a.ISACTIVE=1 and b.assetid="
				+ assetid
				+ " UNION "
				+ " SELECT b.fundid AS \"fund\"  FROM  worksdetail a,egw_works_mis b,eg_asset_PO c WHERE b.worksdetailid =a.ID AND c.workorderid=a.ID AND c.assetid="
				+ assetid + " ORDER BY \"fund\" ";
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Getting the Fund" + qry);
		statement = HibernateUtil.getCurrentSession().createSQLQuery(qry);
		rs = statement.list();
		for(Object[] element : rs)
			{
			fundid = Integer.parseInt(element[0].toString());
			}
		}catch(Exception e)
		{
			LOGGER.error("Error while getFundForAsset"+e.getMessage());
			throw e;
		}
		return fundid;
	}
	
	/**
	 * This function will accept the assetid and returns the Fundsourceid
	 * related to that asset.
	 */
	public int getFundSourceForAsset(int assetid)
			throws Exception {
		int fundsourceid = 0;
		List<Object[]> rs = null;
		Query statement = null;
		try{
		String qry = "Select fundsourceid AS \"fundsourceid\" from worksdetail a,eg_asset_PO b where a.id=b.workorderid and a.ISACTIVE=1 and b.assetid="
				+ assetid
				+ " UNION"
				+ " SELECT financingsourceid AS \"fundsourceid\" FROM worksdetail a,EGW_WORKS_FINANCINGSOURCE b,eg_asset_PO c WHERE a.ID=b.worksdetailid  AND  a.ID=c.workorderid AND c.assetid="
				+ assetid + " ORDER BY \"fundsourceid\" ";
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Getting the Financing Source" + qry);
		statement = HibernateUtil.getCurrentSession().createSQLQuery(qry);
		rs = statement.list();
		for(Object[] element : rs)
			{
			fundsourceid = Integer.parseInt(element[0].toString());
			}
		}catch(Exception e)
		{
			LOGGER.error("Error while getFundSourceForAsset"+e.getMessage());
			throw e;
		}
			
		return fundsourceid;
	}
	
	/**
	 * To get list of worksDetailId(seperated by ',') if input parameter
	 * retValue=worksDetailId or list of worksDetailCode(seperated by ',') if
	 * input parameter retValue=worksDetailCode
	 * 
	 * @param assetid
	 * @param con
	 * @param fundId
	 * @param retValue
	 * @return
	 * @throws Exception
	 */
	public String getWorkOrderForAsset(int assetid,
			String fundId, String retValue) throws Exception {
		if(LOGGER.isInfoEnabled())     LOGGER.info("Inside the getWorkOrderForAsset API");
		String worksDetailId = "";
		String worksDetailCode = "";
		StringBuffer sbuffworksDetailId = new StringBuffer();
		StringBuffer sbuffworksDetailCode = new StringBuffer();
		String fundCheck = "";
		String returnVal = "";
		if(!fundId.equalsIgnoreCase(""))
			{
			fundCheck = " and a.fundId= " + fundId;
			}
		List<Object[]> rs = null;
		Query statement =null;
		try{
			String qry = "Select a.id,a.code from worksdetail a,eg_asset_PO b where a.id=b.workorderid "+ fundCheck
				+ " and a.ISACTIVE=1 and b.assetid="+ assetid
				+ " UNION SELECT b.ID,b.code FROM worksdetail b,egw_works_mis a,eg_asset_PO c WHERE b.ID=a.worksdetailid AND  b.ID=c.workorderid"+ fundCheck
				+ " AND b.STATUSID IN (SELECT id FROM egw_status WHERE moduletype='WO' AND description IN('Billed', 'Completion Certificate Issued')) and c.assetid="+ assetid;
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Query for getting Work Order" + qry);
			statement = HibernateUtil.getCurrentSession().createSQLQuery(qry);
			rs = statement.list();
			for(Object[] element : rs)
			{
				sbuffworksDetailId = sbuffworksDetailId .append(element[0].toString() .concat( ","));
				sbuffworksDetailCode = sbuffworksDetailCode.append(element[1].toString() .concat( ","));
			}
			worksDetailId=sbuffworksDetailId.toString();
			worksDetailCode=sbuffworksDetailCode.toString();
			
			if(retValue.equalsIgnoreCase("worksDetailId"))
			{
				returnVal = worksDetailId;
			}
			else
			{
				returnVal = worksDetailCode;
			}
		}catch(Exception e)
		{
			LOGGER.error("Error while getWorkOrderForAsset"+e.getMessage());
			throw e;
		}
		
		return returnVal;
		
	}
	
}
