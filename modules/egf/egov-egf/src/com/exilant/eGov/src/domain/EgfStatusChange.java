/*
 * Created on Jun 20, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.*;
import org.apache.log4j.Logger;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author Sapna
 * @version 1.0
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EgfStatusChange {
	private static final Logger LOGGER=Logger.getLogger(EgfStatusChange.class);
	private String id=null;
	private String moduletype=null;
	private String moduleid=null;
	private String fromstatus=null;
	private String tostatus=null;
	private String createdby=null;
	private String lastmodifieddate;
	private String updateQuery="UPDATE EGW_SATUSCHANGE SET";
	private boolean isModuleid=false,isField=false;

	/**
		 * @return Returns the createdby.
		 */
		public String getCreatedby() {
			return createdby;
		}
		/**
		 * @param createdby The createdby to set.
		 */
		public void setCreatedby(String createdby) {
			this.createdby = createdby;
			updateQuery = updateQuery + " createdby=" + createdby + ","; isField = true;
		}
		/**
		 * @return Returns the fromstatus.
		 */
		public String getFromstatus() {
			return fromstatus;
		}
		/**
		 * @param fromstatus The fromstatus to set.
		 */
		public void setFromstatus(String fromstatus) {
			this.fromstatus = fromstatus;
			updateQuery = updateQuery + " fromstatus=" + fromstatus + ","; isField = true;
		}
		/**
		 * @return Returns the id.
		 */
		public String getId() {
			return id;
		}
		/**
		 * @param id The id to set.
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * @return Returns the lastmodifieddate.
		 */
		public String getLastmodifieddate() {
			return lastmodifieddate;
		}
		/**
		 * @param lastmodifieddate The lastmodifieddate to set.
		 */
		public void setLastmodifieddate(String lastmodifieddate) {
			this.lastmodifieddate = lastmodifieddate;
			updateQuery = updateQuery + " lastmodifieddate=to_date('" + lastmodifieddate + "','dd-Mon-yyyy HH24:MI:SS')"+","; isField = true;
		}
		/**
		 * @return Returns the moduleid.
		 */
		public String getModuleid() {
			return moduleid;
		}
		/**
		 * @param moduleid The moduleid to set.
		 */
		public void setModuleid(String moduleid) {
			this.moduleid = moduleid;
			updateQuery = updateQuery + " moduleid=" + moduleid + ","; isModuleid = true;

		}
		/**
		 * @return Returns the moduletype.
		 */
		public String getModuletype() {
			return moduletype;
		}
		/**
		 * @param moduletype The moduletype to set.
		 */
		public void setModuletype(String moduletype) {
			this.moduletype = moduletype;
			updateQuery = updateQuery +" moduletype='" + moduletype + "',"; isField = true;
		}
		/**
		 * @return Returns the tostatus.
		 */
		public String getTostatus() {
			return tostatus;
		}
		/**
		 * @param tostatus The tostatus to set.
		 */
		public void setTostatus(String tostatus) {
			this.tostatus = tostatus;
			updateQuery = updateQuery + " tostatus=" + tostatus + ","; isField = true;
		}

		/**
		 * This method inserts the record in the EGW_SATUSCHANGE table
		 * @param connection
		 * @return
		 */
		public void insert(Connection connection) throws SQLException
		{
			String insertQuery="";
			setId(String.valueOf(PrimaryKeyGenerator.getNextKey("EGW_SATUSCHANGE")) );
			LOGGER.info("getID()---"+ getId());
			insertQuery = "INSERT INTO EGW_SATUSCHANGE (ID, MODULETYPE, MODULEID, FROMSTATUS,TOSTATUS,CREATEDBY,LASTMODIFIEDDATE)"
				+ "VALUES("+this.id+",'"+this.moduletype+"',"+this.moduleid+","+this.fromstatus +","+this.tostatus +","+this.createdby+", to_date('"+this.lastmodifieddate+"','dd-Mon-yyyy HH24:MI:SS'))";
			LOGGER.info("insertQuery: "+ insertQuery);
			Statement statement = connection.createStatement();
		    statement.executeUpdate(insertQuery);
		  //LOGGER.info("INSERT QUERY IS:"+insertQuery);
		}

		public void update (Connection connection) throws SQLException
		{
			if(isModuleid && isField)
			{
				updateQuery = updateQuery.substring(0,updateQuery.length()-1);
				updateQuery = updateQuery + " WHERE moduleid = " + moduleid;

				LOGGER.info(updateQuery);
				Statement statement = connection.createStatement();
				statement.executeUpdate(updateQuery);
				statement.close();
				updateQuery="UPDATE EGW_SATUSCHANGE SET";
			}
		}
}
