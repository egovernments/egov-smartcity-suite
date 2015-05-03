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
 * Created on Jun 20, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author Sapna
 * @version 1.0
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly=true)
public class ActionDetails {
		private static final Logger LOGGER=Logger.getLogger(ActionDetails.class);

		// Fields

		private String id;
		private String moduletype;
		private String moduleid;

		private String actionDoneBy;
		private String actionDoneOn;
		private String actionDetcomments="";
		private String createdby;

		private String lastmodifieddate;
		private String actionType ;

		/**
		 * @return Returns the actionDetcomments.
		 */
		public String getActionDetcomments() {
			return actionDetcomments;
		}
		/**
		 * @param actionDetcomments The actionDetcomments to set.
		 */
		public void setActionDetcomments(final String actionDetcomments) {
			this.actionDetcomments = actionDetcomments;
		}
		/**
		 * @return Returns the actionDoneBy.
		 */
		public String getActionDoneBy() {
			return actionDoneBy;
		}
		/**
		 * @param actionDoneBy The actionDoneBy to set.
		 */
		public void setActionDoneBy(final String actionDoneBy) {
			this.actionDoneBy = actionDoneBy;
		}
		/**
		 * @return Returns the actionDoneOn.
		 */
		public String getActionDoneOn() {
			return actionDoneOn;
		}
		/**
		 * @param actionDoneOn The actionDoneOn to set.
		 */
		public void setActionDoneOn(final String actionDoneOn) {
			this.actionDoneOn = actionDoneOn;
		}
		/**
		 * @return Returns the actionType.
		 */
		public String getActionType() {
			return actionType;
		}
		/**
		 * @param actionType The actionType to set.
		 */
		public void setActionType(final String actionType) {
			this.actionType = actionType;
		}
		/**
		 * @return Returns the created by.
		 */
		public String getCreatedby() {
			return createdby;
		}
		/**
		 * @param createdby The createdby to set.
		 */
		public void setCreatedby(final String createdby) {
			this.createdby = createdby;
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
		public void setModuleid(final String moduleid) {
			this.moduleid = moduleid;
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
		public void setModuletype(final String moduletype) {
			this.moduletype = moduletype;
		}
		/**
		 * This method inserts the record in the EGW_SATUSCHANGE table
		 * @param connection
		 * @return
		 */
		@Transactional
				public boolean insert()
					{
						String insertQuery="";
						Query pstmt=null;
						setId(String.valueOf(PrimaryKeyGenerator.getNextKey("eg_actiondetails")) );
						if(LOGGER.isDebugEnabled())     LOGGER.debug("getID()---"+ getId());
						insertQuery = "INSERT INTO EG_ACTIONDETAILS (ID, MODULETYPE, MODULEID, ACTIONDONEBY,ACTIONDONEON,COMMENTS,CREATEDBY,LASTMODIFIEDDATE,ACTIONTYPE)"
							+ "VALUES(?,?,?,?,?,?,?,?,?)";
						if(LOGGER.isDebugEnabled())     LOGGER.debug("insertQuery: "+ insertQuery);
						try
						{
						 pstmt=HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
						 pstmt.setString(1,this.id);
						 pstmt.setString(2,this.moduletype);
						 pstmt.setString(3,this.moduleid);
						 pstmt.setString(4,this.actionDoneBy);
						 pstmt.setString(5,this.actionDoneOn);
						 pstmt.setString(6,this.actionDetcomments);
						 pstmt.setString(7,this.createdby);
						 pstmt.setString(8,this.lastmodifieddate);
						 pstmt.setString(9,this.actionType);
						 pstmt.executeUpdate();
						}
						catch(Exception insert){
							LOGGER.error("Exception in inserting EGW_SATUSCHANGE :"+insert);
							return false;
							}
						
						return true;
			}
}
