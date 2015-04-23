  /*
   * EGPSalaryBillDetails.java Created on June 27, 2007
   *
   * Copyright 2005 eGovernments Foundation. All rights reserved.
   * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
  package com.exilant.eGov.src.domain;

  import java.sql.*;
  import java.sql.SQLException;
  import java.sql.ResultSet;
  import java.sql.PreparedStatement;
  import org.apache.log4j.Logger;
  import com.exilant.exility.updateservice.PrimaryKeyGenerator;
  import com.exilant.exility.common.TaskFailedException;

  /**
  * @author Iliyaraja
  *
  * TODO To change the template for this generated type comment go to
  * Window - Preferences - Java - Code Style - Code Templates
  */

  public class EGPSalaryBillDetails
  {
	private static final Logger LOGGER=Logger.getLogger(EGPSalaryBillDetails.class);
	private PreparedStatement pstmt=null;
	private String id=null;
	ResultSet rs=null;
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

	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
	//	EGovernCommon commommethods = new EGovernCommon();
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("EGP_SALARY_BILLDETAILS")));

		String insertQuery = "INSERT INTO EGP_SALARY_BILLDETAILS (Id, BillId, HeadId,GlcodeId, " +
						"SalType, Amount,LastModifiedDate) values(?,?,?,?,?,?,?)";
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		pstmt=connection.prepareStatement(insertQuery);
		pstmt.setString(1,id);
		pstmt.setString(2,billId);
		pstmt.setString(3,headId);
		pstmt.setString(4,glcodeId);
		pstmt.setString(5,salType);
		pstmt.setString(6,amount);
		pstmt.setString(7,this.lastModifiedDate);
		rs=pstmt.executeQuery();
		pstmt.close();
	}

	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = ?";
			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			pstmt=connection.prepareStatement(updateQuery);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			pstmt.close();
			updateQuery="UPDATE EGP_SALARY_BILLDETAILS SET";
		}
	}

	public void delete(Connection con)throws SQLException,TaskFailedException
		{
			String delQuery="delete from EGP_SALARY_BILLDETAILS where id= ?";
			if(LOGGER.isInfoEnabled())     LOGGER.info(delQuery);
			pstmt=con.prepareStatement(delQuery);
			pstmt.setInt(1, getId());
			rs=pstmt.executeQuery();
			pstmt.close();	
		}


}