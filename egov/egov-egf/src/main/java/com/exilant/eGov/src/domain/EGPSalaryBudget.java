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
   * EGPSalaryBudget.java Created on June 27, 2007
   *
   * Copyright 2005 eGovernments Foundation. All rights reserved.
   * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
  package com.exilant.eGov.src.domain;

  import java.sql.*;
  import java.sql.PreparedStatement;
  import java.sql.SQLException;
  import java.sql.ResultSet;
  import org.apache.log4j.Logger;
  import com.exilant.exility.common.TaskFailedException;
  import com.exilant.exility.updateservice.PrimaryKeyGenerator;


  /**
  * @author Iliyaraja
  *
  * TODO To change the template for this generated type comment go to
  * Window - Preferences - Java - Code Style - Code Templates
  */

  public class EGPSalaryBudget
  {

	private static final Logger LOGGER=Logger.getLogger(EGPSalaryBudget.class);
	private PreparedStatement pstmt=null;
	ResultSet rs=null;
	private String id=null;
	private String billId=null;
	private String budgetCodeId=null;
	private String functionId=null;
	private String lastModifiedDate="";
	private String updateQuery="UPDATE EGP_SALARY_BUDGET SET";
	private boolean isBillId=false,isField=false;


	public EGPSalaryBudget() {}

	public void setId(String aId){ id = aId; isField = true;}
	public int getId() {return Integer.valueOf(id).intValue(); }

	public void setBillId(String aBillId){ billId = aBillId; updateQuery = updateQuery + " billId=" + billId + ","; isBillId = true;isField = true;}
	public void setBudgetCodeId(String aBudgetCodeId){ budgetCodeId = aBudgetCodeId; updateQuery = updateQuery + " budgetCodeId=" + budgetCodeId + ","; isField = true;}
	public void setFunctionId(String aFunctionId){functionId = aFunctionId; updateQuery = updateQuery + " functionId=" + functionId + ","; isField = true;}
	public void setLastModifiedDate(String aLastModifiedDate){ lastModifiedDate = aLastModifiedDate; updateQuery = updateQuery + " lastModifiedDate=to_date('" + lastModifiedDate + "','dd-Mon-yyyy HH24:MI:SS')"+","; isField = true;}

	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
	//	EGovernCommon commommethods = new EGovernCommon();
		//Date lastmodi=to_date('"+this.lastModifiedDate+"','dd-Mon-yyyy HH24:MI:SS');
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("EGP_SALARY_BUDGET")));

		String insertQuery = "INSERT INTO EGP_SALARY_BUDGET (Id, BillId, BudgetCodeId,FunctionId,LastModifiedDate) " +
						"values (?,?,?,?,?)";
						//"VALUES (" + id + ", " + billId + ", " + budgetCodeId + ", " + functionId + ",to_date('"+this.lastModifiedDate+"','dd-Mon-yyyy HH24:MI:SS'))";
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		pstmt=connection.prepareStatement(insertQuery);
		pstmt.setString(1, id);
		pstmt.setString(2, billId);
		pstmt.setString(1, budgetCodeId);
		pstmt.setString(1, functionId);
		pstmt.setString(1, this.lastModifiedDate);
		rs=pstmt.executeQuery();
		pstmt.close();
		
	}

	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isBillId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE billId = ?";

			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			pstmt=connection.prepareStatement(updateQuery);
			pstmt.setString(1, billId);
			ResultSet rs = pstmt.executeQuery();
			pstmt.close();
			updateQuery="UPDATE EGP_SALARY_BUDGET SET";
		}
	}


	}
