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
   * EGPSalaryBillDetails.java Created on June 27, 2007
   *
   * Copyright 2005 eGovernments Foundation. All rights reserved.
   * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
  package com.exilant.eGov.src.domain;

  import java.sql.SQLException;
import java.util.List;

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
  public class EGPSalaryBillDetails
  {
	private static final Logger LOGGER=Logger.getLogger(EGPSalaryBillDetails.class);
	private Query pstmt=null;
	private String id=null;
	List<Object[]> rs=null;
	private String billId=null;
	private String headId=null;
	private String glcodeId=null;
	private String salType=null;
	private String amount=null;
	private String lastModifiedDate="";

	private String updateQuery="UPDATE EGP_SALARY_BILLDETAILS SET";
	private boolean isId=false,isField=false;


	public EGPSalaryBillDetails() {}

	public void setId(String aId){ id = aId; isId=true;isField = true;}
	public int getId() {return Integer.valueOf(id).intValue(); }

	public void setBillId(String aBillId){ billId = aBillId; updateQuery = updateQuery + " billId=" + billId + ","; isField = true;}
	public void setHeadId(String aHeadId){ headId = aHeadId; updateQuery = updateQuery + " headId=" + headId + ","; isField = true;}
	public void setGlcodeId(String aGlcodeId){glcodeId = aGlcodeId; updateQuery = updateQuery + " glcodeId=" + glcodeId + ","; isField = true;}
	public void setSalType(String aSalType){ salType = aSalType; updateQuery = updateQuery +" salType='" + salType + "',"; isField = true;}
	public void setAmount(String aAmount){ amount = aAmount; updateQuery = updateQuery + " amount=" + amount + ","; isField = true;}
	public void setLastModifiedDate(String aLastModifiedDate){ lastModifiedDate = aLastModifiedDate; updateQuery = updateQuery + " lastModifiedDate=to_date('" + lastModifiedDate + "','dd-Mon-yyyy HH24:MI:SS')"+","; isField = true;}
	@Transactional
	public void insert() throws SQLException,TaskFailedException
	{
	//	EGovernCommon commommethods = new EGovernCommon();
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("EGP_SALARY_BILLDETAILS")));

		String insertQuery = "INSERT INTO EGP_SALARY_BILLDETAILS (Id, BillId, HeadId,GlcodeId, " +
						"SalType, Amount,LastModifiedDate) values(?,?,?,?,?,?,?)";
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		pstmt=HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
		pstmt.setString(1,id);
		pstmt.setString(2,billId);
		pstmt.setString(3,headId);
		pstmt.setString(4,glcodeId);
		pstmt.setString(5,salType);
		pstmt.setString(6,amount);
		pstmt.setString(7,this.lastModifiedDate);
		pstmt.executeUpdate();
	}
	@Transactional
	public void update () throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = ?";
			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			pstmt=HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			updateQuery="UPDATE EGP_SALARY_BILLDETAILS SET";
		}
	}
	@Transactional
	public void delete()throws SQLException,TaskFailedException
		{
			String delQuery="delete from EGP_SALARY_BILLDETAILS where id= ?";
			if(LOGGER.isInfoEnabled())     LOGGER.info(delQuery);
			pstmt=HibernateUtil.getCurrentSession().createSQLQuery(delQuery);
			pstmt.setInteger(1, getId());
			pstmt.executeUpdate();
		}


}
