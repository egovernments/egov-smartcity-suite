/*
 * Created on Jul 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.log4j.Logger;
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
	 public void insert(Connection con,DataCollection dc,String schemeCode) throws TaskFailedException
	 {
		//int [] result=new int[10];
		Statement st=null;
		try
		{
			EGovernCommon egov = new EGovernCommon();
			String[][] schemedetail = (String[][])dc.getGrid("AddScheme_Grid");			
			Date dt=new Date();
			String modifiedDate=egov.getCurrentDate(con);
			dt = sdf.parse(modifiedDate);
			modifiedDate = formatter1.format(dt);
			st=con.createStatement();
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
					st.addBatch(subScheme);
				}
			}
			st.executeBatch();
			dc.addMessage("eGovSuccess","Scheme Created");
		}
		catch(Exception e)
		{
			dc.addMessage("exilRPError","Error in Insert"+e.toString());
			LOGGER.error("Inside insert....."+e.getMessage());
			throw taskExc;
		}finally{
			try{
				st.close();
			}catch(Exception e){LOGGER.error("Inside finally.....");}
		}
	 }
	 public void modify(Connection con,DataCollection dc) throws TaskFailedException
	 {
	 	//int [] result=new int[10];
		Statement st=null;
		try
		{
			EGovernCommon egov = new EGovernCommon();
			String[][] schemedetail = (String[][])dc.getGrid("AddScheme_Grid");			
			Date dt=new Date();
			String modifiedDate=egov.getCurrentDate(con);
			dt = sdf.parse(modifiedDate);
			modifiedDate = formatter1.format(dt);
			st=con.createStatement();
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
						
						if (recordExists(schemedetail[j][0],"sub_scheme",schemeId,con))
							subScheme="update sub_scheme set VALIDFROM='"+subStartDate+"',VALIDTO='"+subEndDate+"',ISACTIVE='"+schemedetail[j][4]+"',lastmodifieddate='"+modifiedDate+"' where schemeid in (select id from scheme where code='"+dc.getValue("schemecode")+"') and code='"+schemedetail[j][0]+"'";
						else
						{
							subSchemeMaxId=getId("SUB_SCHEME");
							subScheme="insert into sub_scheme (ID,CODE,NAME,VALIDFROM,VALIDTO,ISACTIVE,schemeid,lastmodifieddate) values ("+subSchemeMaxId+",'"+schemedetail[j][0]+"','"+schemedetail[j][1]+"','"+subStartDate+"','"+subEndDate+"','"+schemedetail[j][4]+"',"+schemeId+",'"+modifiedDate+"')";
							if(LOGGER.isInfoEnabled())     LOGGER.info("sub scheme "+subScheme);
						}
					st.addBatch(subScheme);
				}
			}
			st.executeBatch();
			dc.addMessage("eGovSuccess","Scheme Updated");
			deleteRecords(code,schemeId,con);
		}
		catch(Exception e)
		{
			dc.addMessage("exilRPError","Error in Modify "+e.toString());
			LOGGER.error("Inside modify....."+e.getMessage());
			throw taskExc;
		}finally{
			try{
				st.close();
			}catch(Exception e){LOGGER.error("Inside finally.modify...");}
		}
	 }

		private boolean recordExists(String code,String tableName,String schemeId,Connection con) throws TaskFailedException
		{
			Statement st=null;
			boolean flag=false;
			ResultSet rs= null;
			try
			{
				st=con.createStatement();
				String query="";
				if (schemeId==null)
					query="select count(*) from "+tableName+" where code='"+code+"'";
				else
					query="select count(*) from "+tableName+" where code='"+code+"' and schemeid="+Integer.parseInt(schemeId);
				if(LOGGER.isInfoEnabled())     LOGGER.info("record exists query "+query);
				rs=st.executeQuery(query);
				if (rs!=null && rs.next())
				{
					if (rs.getString(1)==null)
						flag=false;
					else
					{
						if (rs.getInt(1)>0)
							flag=true;
						else
							flag=false;
					}
				}
				else
					flag=false;
			}
			catch(Exception e)
			{
				LOGGER.error("Inside recordexist....."+e.getMessage());
				throw taskExc;
			}finally{
				try{
					rs.close();
					st.close();
				}catch(Exception e){LOGGER.error("Inside finally.....");}
			}
			return flag;
		}
		private void deleteRecords(String codes,String schemeId,Connection con) throws TaskFailedException
		{
			Statement st=null;
			try
			{
				st=con.createStatement();
				String query="delete from sub_scheme where code not in "+codes+" and schemeid="+schemeId;
				if(LOGGER.isInfoEnabled())     LOGGER.info("delete query "+query);
				st.executeUpdate(query);
			}
			catch(Exception e)
			{
				LOGGER.error("Inside delete....."+e.getMessage());
				throw taskExc;
			}finally{
				try{
					st.close();
				}catch(Exception e){LOGGER.error("Inside finally.....");}
			}
		}
		private long getId(String tableName)
		{
			return PrimaryKeyGenerator.getNextKey(tableName);
		}
}