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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly=true)
public class SubSchemeBean 
{
	private String schemecode=null;
	private String schemename=null;
	private String description=null;
	private String startDate=null;
	private String endDate=null;
	private String isActive=null;
	private final static Logger LOGGER=Logger.getLogger(SubSchemeBean.class);
	private SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	private SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
	private TaskFailedException taskExc;
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
	@Transactional
	 public void insert(DataCollection dc,String schemeCode) throws TaskFailedException
	 {
		//int [] result=new int[10];
		Query st=null;
		try
		{
			EGovernCommon egov = new EGovernCommon();
			String[][] schemedetail = (String[][])dc.getGrid("AddScheme_Grid");			
			Date dt=new Date();
			String modifiedDate=egov.getCurrentDate();
			dt = sdf.parse(modifiedDate);
			modifiedDate = formatter1.format(dt);
			
			long subSchemeMaxId=0;
			String subStartDate="";
			String subEndDate="",subScheme="";
	
			for(int j=0;j<schemedetail.length;j++ ) 
			{
				if (!schemedetail[j][0].trim().equals(""))
				{
					dt = sdf.parse(schemedetail[j][2]);
					subStartDate = formatter1.format(dt);
					dt = sdf.parse(schemedetail[j][3]);
					subEndDate = formatter1.format(dt);					
					subSchemeMaxId=getId("SUB_SCHEME");
					subScheme="insert into sub_scheme (ID,CODE,NAME,VALIDFROM,VALIDTO,ISACTIVE,schemeid,lastmodifieddate) values ("+subSchemeMaxId+",'"+schemedetail[j][0]+"','"+schemedetail[j][1]+"','"+subStartDate+"','"+subEndDate+"','"+schemedetail[j][4]+"',"+schemeCode+",'"+modifiedDate+"')";
					if(LOGGER.isInfoEnabled())     LOGGER.info("sub scheme "+subScheme);
					st=HibernateUtil.getCurrentSession().createSQLQuery(subScheme);
					st.executeUpdate();
				}
			}
			dc.addMessage("eGovSuccess","Scheme Created");
		}
		catch(Exception e)
		{
			dc.addMessage("exilRPError","Error in Insert"+e.toString());
			LOGGER.error("Inside insert....."+e.getMessage());
			throw taskExc;
		}
	 }
	@Transactional
	 public void modify(DataCollection dc) throws TaskFailedException
	 {
	 	//int [] result=new int[10];
		Query st=null;
		try
		{
			EGovernCommon egov = new EGovernCommon();
			String[][] schemedetail = (String[][])dc.getGrid("AddScheme_Grid");			
			Date dt=new Date();
			String modifiedDate=egov.getCurrentDate();
			dt = sdf.parse(modifiedDate);
			modifiedDate = formatter1.format(dt);
			
			long subSchemeMaxId=0;
			String subStartDate="";
			String subEndDate="",subScheme="";
			String code="('";
			String schemeId=dc.getValue("codeofscheme");
			for(int j=0;j<schemedetail.length;j++ ) 
			{
				if (!schemedetail[j][0].trim().equals(""))
				{
					dt = sdf.parse(schemedetail[j][2]);
					subStartDate = formatter1.format(dt);
					dt = sdf.parse(schemedetail[j][3]);
					subEndDate = formatter1.format(dt);
						if (j==schemedetail.length-1)
							code=code+schemedetail[j][0]+"')";
						else
							code=code+schemedetail[j][0]+"','";
						
						if (recordExists(schemedetail[j][0],"sub_scheme",schemeId))
							subScheme="update sub_scheme set VALIDFROM='"+subStartDate+"',VALIDTO='"+subEndDate+"',ISACTIVE='"+schemedetail[j][4]+"',lastmodifieddate='"+modifiedDate+"' where schemeid in (select id from scheme where code='"+dc.getValue("schemecode")+"') and code='"+schemedetail[j][0]+"'";
						else
						{
							subSchemeMaxId=getId("SUB_SCHEME");
							subScheme="insert into sub_scheme (ID,CODE,NAME,VALIDFROM,VALIDTO,ISACTIVE,schemeid,lastmodifieddate) values ("+subSchemeMaxId+",'"+schemedetail[j][0]+"','"+schemedetail[j][1]+"','"+subStartDate+"','"+subEndDate+"','"+schemedetail[j][4]+"',"+schemeId+",'"+modifiedDate+"')";
							if(LOGGER.isInfoEnabled())     LOGGER.info("sub scheme "+subScheme);
						}
						st=HibernateUtil.getCurrentSession().createSQLQuery(subScheme);
					st.executeUpdate();
				}
			}
			dc.addMessage("eGovSuccess","Scheme Updated");
			deleteRecords(code,schemeId);
		}
		catch(Exception e)
		{
			dc.addMessage("exilRPError","Error in Modify "+e.toString());
			LOGGER.error("Inside modify....."+e.getMessage());
			throw taskExc;
		}
	 }
		private boolean recordExists(String code,String tableName,String schemeId) throws TaskFailedException
		{
			Query st=null;
			boolean flag=false;
			List<Object[]> rs= null;
			try
			{
				
				String query="";
				if (schemeId==null)
					query="select count(*) from "+tableName+" where code='"+code+"'";
				else
					query="select count(*) from "+tableName+" where code='"+code+"' and schemeid="+Integer.parseInt(schemeId);
				if(LOGGER.isInfoEnabled())     LOGGER.info("record exists query "+query);
				st=HibernateUtil.getCurrentSession().createSQLQuery(query);
				rs=st.list();
				for(Object[] element : rs){
					if (element[0].toString()==null)
						flag=false;
					else
					{
						if (Integer.parseInt(element[0].toString())>0)
							flag=true;
						else
							flag=false;
					}
				}
				if(rs == null || rs.size() == 0) 
					flag=false;
			}
			catch(Exception e)
			{
				LOGGER.error("Inside recordexist....."+e.getMessage());
				throw taskExc;
			}
			return flag;
		}
		@Transactional
		private void deleteRecords(String codes,String schemeId) throws TaskFailedException
		{
			Query st=null;
			try
			{
				
				String query="delete from sub_scheme where code not in "+codes+" and schemeid="+schemeId;
				if(LOGGER.isInfoEnabled())     LOGGER.info("delete query "+query);
				st=HibernateUtil.getCurrentSession().createSQLQuery(query);
				st.executeUpdate();
			}
			catch(Exception e)
			{
				LOGGER.error("Inside delete....."+e.getMessage());
				throw taskExc;
			}
		}
		private long getId(String tableName)
		{
			return PrimaryKeyGenerator.getNextKey(tableName);
		}
}
