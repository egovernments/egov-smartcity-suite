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
 * Created on Jul 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.util.Locale;
/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SchemeMasterBean 
{
	private String schemecode=null;
	private String schemename=null;
	private String description=null;
	private String startDate=null;
	private String endDate=null;
	private String isActive=null;
	private final static Logger LOGGER=Logger.getLogger(SchemeMasterBean.class);
	private static TaskFailedException taskExc;
	private SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	private SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the endDate.
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return Returns the isActive.
	 */
	public String getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive The isActive to set.
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	/**
	 * @return Returns the schemecode.
	 */
	public String getSchemecode() {
		return schemecode;
	}
	/**
	 * @param schemecode The schemecode to set.
	 */
	public void setSchemecode(String schemecode) {
		this.schemecode = schemecode;
	}
	/**
	 * @return Returns the schemename.
	 */
	public String getSchemename() {
		return schemename;
	}
	/**
	 * @param schemename The schemename to set.
	 */
	public void setSchemename(String schemename) {
		this.schemename = schemename;
	}
	/**
	 * @return Returns the startDate.
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	 public void insert(Connection con,DataCollection dc) throws TaskFailedException
	 {
	 	boolean schemeExists=false;
		PreparedStatement pst=null;
		try
		{
			String endDate=dc.getValue("endDate");
			String startDate=dc.getValue("startDate");
            endDate = formatter1.format(sdf.parse(endDate));
            startDate = formatter1.format(sdf.parse(startDate));
			long schemeMaxId=0;
			schemeMaxId=getId("SCHEME");
			dc.addValue("schemeId",schemeMaxId);
			String mainScheme="";
			schemeExists=recordExists(dc.getValue("schemecode"),"scheme",null,con,dc.getValue("fund_id"));
			if(LOGGER.isInfoEnabled())     LOGGER.info("scheme exists :"+schemeExists);
			mainScheme="insert into scheme (ID,CODE,NAME,VALIDFROM,VALIDTO,ISACTIVE,DESCRIPTION,FUNDID) values (?, ?, ?, ?, ?, ?, ?, ?)";
			
			pst=con.prepareStatement(mainScheme);
			pst.setLong(1, schemeMaxId);
			pst.setString(2, dc.getValue("schemecode"));
			pst.setString(3, dc.getValue("schemename"));
			pst.setString(4, startDate);
			pst.setString(5, endDate);
			pst.setString(6, dc.getValue("isActive"));
			pst.setString(7, dc.getValue("description"));
			pst.setString(8, dc.getValue("fund_id"));
			pst.executeUpdate();
		}
		catch(Exception e)
		{
			if (schemeExists)
				dc.addMessage("exilRPError","Scheme Already Exists with this Code and fund");
			else
				dc.addMessage("exilRPError","Error in Insert "+e.toString());
			throw taskExc;
		}finally{
			try{
				pst.close();
			}catch(Exception e){LOGGER.error("Inside the finally block in insert");}
		}
	 }
	 public void modify(Connection con,DataCollection dc) throws TaskFailedException
	 {
		PreparedStatement pst=null;
		try
		{
			String endDate=dc.getValue("endDate");
			String startDate=dc.getValue("startDate");
            endDate = formatter1.format(sdf.parse(endDate));
            startDate = formatter1.format(sdf.parse(startDate));
			String mainScheme="";
			mainScheme="update scheme set VALIDFROM= ?, VALIDTO= ?, ISACTIVE= ?, DESCRIPTION= ?, FUNDID= ? where code= ?";
			pst=con.prepareStatement(mainScheme);
			pst.setString(1, startDate);
			pst.setString(2, endDate);
			pst.setString(3, dc.getValue("isActive"));
			pst.setString(4, dc.getValue("description"));
			pst.setString(5, dc.getValue("fund_id"));
			pst.setString(6, dc.getValue("schemecode"));
			pst.executeUpdate();
		}
		catch(Exception e)
		{
			dc.addMessage("exilRPError","Error in Update "+e.toString());
			throw taskExc;
		}finally{
			try{
				pst.close();
			}catch(Exception e){LOGGER.error("Inside the finally block in insert",e);}
		}
	 }
	 	
		private boolean recordExists(String code,String tableName,String schemeId,Connection con, String fundId) throws TaskFailedException
		{
			PreparedStatement pst=null;
			ResultSet rs= null;
			boolean flag=false;
			try
			{
				String query="";
				if (schemeId!=null){
					query="select count(*) from "+tableName+" where code=? and schemeid= ?";
					pst=con.prepareStatement(query);
					pst.setString(1, code);
					pst.setString(2, schemeId);					
				}				
				else{
					query="select count(*) from "+tableName+" where code=? and fundid= ?";
					pst=con.prepareStatement(query);
					pst.setString(1, code);
					pst.setString(2, fundId);
				}
				if(LOGGER.isInfoEnabled())     LOGGER.info("record exists query "+query);
				rs=pst.executeQuery();
				if (rs!=null && rs.next())
				{
					if (rs.getString(1)==null)
						flag=false;
					else
					{
						if (rs.getInt(1)>0){
							flag=true;
							return flag;
						}
						else
							flag=false;
					}
				}
				else
					flag=false;
			}
			catch(Exception e)
			{
				throw taskExc;
			}finally{
				try{
					rs.close();
					pst.close();
				}catch(Exception e){LOGGER.error("Inside the finally block in insert");}
			}
			return flag;
		}
		private long getId(String tableName)
		{
			return PrimaryKeyGenerator.getNextKey(tableName);
		}
}
