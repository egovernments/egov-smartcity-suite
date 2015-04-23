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