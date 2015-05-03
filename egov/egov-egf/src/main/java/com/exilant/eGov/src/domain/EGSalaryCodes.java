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
   * EGSalaryCodes.java Created on July 17, 2007
   *
   * Copyright 2005 eGovernments Foundation. All rights reserved.
   * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
  package com.exilant.eGov.src.domain;

  import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;


  /**
  * @author Iliyaraja
  *
  * TODO To change the template for this generated type comment go to
  * Window - Preferences - Java - Code Style - Code Templates
  */
  @Transactional(readOnly=true)
  public class EGSalaryCodes
  {

	  private static final Logger LOGGER=Logger.getLogger(EGSalaryCodes.class);

	private String id=null;
	private String head=null;
	private String glcodeId=null;
	private String salType=null;
	private String Createdby=null;
	private String createdDate="";
	private String lastModifiedBy=null;
	private String lastModifiedDate="";
	private String updateQuery="UPDATE EG_SALARYCODES SET";
	private boolean isId=false,isField=false;


	public EGSalaryCodes() {}

	public void setId(String aId){ id = aId; isId=true;isField = true;}
	public int getId() {return Integer.valueOf(id).intValue(); }

	public void setHead(String aHead){ head = aHead; updateQuery = updateQuery + " head='" + head + "',"; isField = true;}
	public void setGlcodeId(String aGlcodeId){glcodeId = aGlcodeId; updateQuery = updateQuery + " glcodeId=" + glcodeId + ","; isField = true;}
	public void setSalType(String aSalType){ salType = aSalType; updateQuery = updateQuery +" salType='" + salType + "',"; isField = true;}

	public void setCreatedby(String createdby) {Createdby = createdby;updateQuery = updateQuery + " CREATEDBY=" + Createdby + ","; isField = true;}
	public void setCreatedDate(String createdDate) {this.createdDate = createdDate;updateQuery = updateQuery + " createdDate='" + createdDate + "',"; isField = true;}

	public void setLastModifiedBy(String alastModifiedBy) {lastModifiedBy = alastModifiedBy;updateQuery = updateQuery + " lastModifiedBy=" + lastModifiedBy + ","; isField = true;}
	public void setLastModifiedDate(String aLastModifiedDate){ lastModifiedDate = aLastModifiedDate; updateQuery = updateQuery + " lastModifiedDate=to_date('" + lastModifiedDate + "','dd-Mon-yyyy HH24:MI:SS')"+","; isField = true;}
	@Transactional
	public void insert() throws SQLException,TaskFailedException
	{
	//	EGovernCommon commommethods = new EGovernCommon();
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("EG_SALARYCODES")));

		String insertQuery = "INSERT INTO EG_SALARYCODES (Id, head,GlcodeId, " +
						"SalType, Createdby,createdDate) " +
						"VALUES (" + id + ", '" + head + "'," + glcodeId + ",'" + salType + "'," + Createdby + ",to_date('"+this.createdDate+"','dd-Mon-yyyy HH24:MI:SS'))";
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

			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			Query statement = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
			statement.executeUpdate();
			updateQuery="UPDATE EG_SALARYCODES SET";
		}
	}

}
