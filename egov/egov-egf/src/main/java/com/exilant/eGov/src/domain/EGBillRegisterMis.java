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
   * EGBillRegisterMis.java Created on June 26, 2007
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
  import com.exilant.exility.updateservice.PrimaryKeyGenerator;
  import com.exilant.exility.common.TaskFailedException;

  /**
  * @author Iliyaraja
  *
  * TODO To change the template for this generated type comment go to
  * Window - Preferences - Java - Code Style - Code Templates
  */

  public class EGBillRegisterMis
  {

    private static final Logger LOGGER=Logger.getLogger(EGBillRegisterMis.class);
    private PreparedStatement pstmt=null;
    ResultSet rs=null;
	private String id=null;
	private String billId=null;
	private String fundId=null;
	private String segmentId=null;
	private String subSegmentId=null;
	private String fieldId=null;
	private String subFieldId=null;
	private String functionaryId=null;
	private String sanctionedBy=null;
	private String sanctionDate="";
	private String sanctionDetail=null;

	private String narration=null;
	private String lastUpdatedTime="";
	private String disBursementType=null;
	private String escalation=null;
	private String advancePayments=null;
	private String securedAdvances=null;
	private String deductAmountWitheld=null;
	private String departmentId=null;
	private String month=null;
	private String financialYearId=null;

	private String updateQuery="UPDATE EG_BILLREGISTERMIS SET";
	private boolean isBillId=false,isField=false;


	public EGBillRegisterMis() {}

	public void setId(String aId){ id = aId; isField = true;}
	public int getId() {return Integer.valueOf(id).intValue(); }
	public void setBillId(String aBillId){ billId = aBillId; updateQuery = updateQuery + " billId=" + billId + ","; isBillId = true;isField = true; }
	public void setFundId(String aFundId){ fundId = aFundId; updateQuery = updateQuery + " fundId=" + fundId + ","; isField = true;}
	public void setSegmentId(String aSegmentId){segmentId = aSegmentId; updateQuery = updateQuery + " segmentId=" + segmentId + ","; isField = true;}
	public void setSubSegmentId(String aSubSegmentId){ subSegmentId = aSubSegmentId; updateQuery = updateQuery + " subSegmentId=" + subSegmentId + ","; isField = true;}
	public void setFieldId(String aFieldId){ fieldId = aFieldId; updateQuery = updateQuery + " fieldId=" + fieldId + ","; isField = true;}
	public void setSubFieldId(String aSubFieldId){ subFieldId = aSubFieldId; updateQuery = updateQuery + " subFieldId=" + subFieldId + ","; isField = true;}
	public void setFunctionaryId(String aFunctionaryId){ functionaryId = aFunctionaryId; updateQuery = updateQuery + " functionaryId=" + functionaryId + ","; isField = true;}
	public void setSanctionedBy(String aSanctionedBy){ sanctionedBy = aSanctionedBy; updateQuery = updateQuery + " sanctionedBy='" + sanctionedBy + "',"; isField = true;}
	public void setSanctionDate(String aSanctionDate){ sanctionDate = aSanctionDate; updateQuery = updateQuery + " sanctionDate=to_date('" + sanctionDate + "','dd-Mon-yyyy HH24:MI:SS')"+",";isField = true;}

	public void setSanctionDetail(String aSanctionDetail){ sanctionDetail = aSanctionDetail; updateQuery = updateQuery +" sanctionDetail='" + sanctionDetail + "',"; isField = true;}
	public void setNarration(String aNarration){ narration = aNarration; updateQuery = updateQuery + " narration='" + narration + "',"; isField = true;}
	public void setLastUpdatedTime(String aLastUpdatedTime){ lastUpdatedTime = aLastUpdatedTime; updateQuery = updateQuery + " lastUpdatedTime=to_date('" + lastUpdatedTime + "','dd-Mon-yyyy HH24:MI:SS')"+","; isField = true;}
	public void setDisBursementType(String aDisBursementType){ disBursementType = aDisBursementType; updateQuery = updateQuery +" disBursementType='" + disBursementType + "',"; isField = true;}
	public void setEscalation(String aEscalation){ escalation = aEscalation; updateQuery = updateQuery + " escalation=" + escalation + ","; isField = true;}
	public void setAdvancePayments(String aAdvancePayments){ advancePayments = aAdvancePayments; updateQuery = updateQuery + " advancePayments=" + advancePayments + ","; isField = true;}
	public void setSecuredAdvances(String aSecuredAdvances){ securedAdvances = aSecuredAdvances; updateQuery = updateQuery + " securedAdvances=" + securedAdvances + ","; isField = true;}
	public void setDeductAmountWitheld(String aDeductAmountWitheld){ deductAmountWitheld = aDeductAmountWitheld; updateQuery = updateQuery + " deductAmountWitheld=" + deductAmountWitheld + ","; isField = true;}
	public void setDepartmentId(String aDepartmentId){ departmentId = aDepartmentId; updateQuery = updateQuery + " departmentId=" + departmentId + ","; isField = true;}
	public void setMonth(String aMonth){ month = aMonth; updateQuery = updateQuery + " month=" + month + ","; isField = true;}
	public void setFinancialYearId(String aFinancialYearId){ financialYearId = aFinancialYearId; updateQuery = updateQuery + " financialYearId=" + financialYearId + ","; isField = true;}


public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		//EGovernCommon commommethods = new EGovernCommon();
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("EG_BILLREGISTERMIS")));


		String insertQuery = "INSERT INTO EG_BILLREGISTERMIS (Id, BillId, FundId, SegmentId, " +
						"SubSegmentId, FieldId, SubFieldId, FunctionaryId, SanctionedBy, SanctionDate, SanctionDetail,Narration,LastUpdatedTime,DisBursementType,Escalation,AdvancePayments,SecuredAdvances,DeductAmountWitheld,DepartmentId,Month,FinancialYearId) " +
						"Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		pstmt=connection.prepareStatement(insertQuery);
		pstmt.setString(1,id);
		pstmt.setString(2,billId);
		pstmt.setString(3,fundId);
		pstmt.setString(4,segmentId);
		pstmt.setString(5,subSegmentId);
		pstmt.setString(6,fieldId);
		pstmt.setString(7,subFieldId);
		pstmt.setString(8,functionaryId);
		pstmt.setString(9,sanctionedBy);
		pstmt.setString(10,this.sanctionDate);
		pstmt.setString(11,sanctionDetail);
		pstmt.setString(12,narration);
		pstmt.setString(13,this.lastUpdatedTime);
		pstmt.setString(14,disBursementType);
		pstmt.setString(15,escalation);
		pstmt.setString(16,advancePayments);
		pstmt.setString(17,securedAdvances);
		pstmt.setString(18,deductAmountWitheld);
		pstmt.setString(19,departmentId);
		pstmt.setString(20,month);
		pstmt.setString(21,financialYearId);
		
		rs=pstmt.executeQuery();
		rs.close();
		pstmt.close();
	}

public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isBillId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = "UPDATE EG_BILLREGISTERMIS SET WHERE billId = ?";
			pstmt=connection.prepareStatement(updateQuery);
			pstmt.setString(1,billId);
			if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			rs=pstmt.executeQuery();
			updateQuery="UPDATE EG_BILLREGISTERMIS SET";
		}
	}


	}
