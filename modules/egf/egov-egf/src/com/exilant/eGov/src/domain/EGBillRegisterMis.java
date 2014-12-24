  /*
   * EGBillRegisterMis.java Created on June 26, 2007
   *
   * Copyright 2005 eGovernments Foundation. All rights reserved.
   * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
  package com.exilant.eGov.src.domain;

  import java.sql.*;
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
						"VALUES (" + id + "," + billId + "," + fundId + "," + segmentId + "," + subSegmentId + "," + fieldId + "," + subFieldId + "," + functionaryId+ ",'" + sanctionedBy + "',to_date('"+this.sanctionDate+"','dd-Mon-yyyy HH24:MI:SS'),'" + sanctionDetail + "','" + narration + "',to_date('"+this.lastUpdatedTime+"','dd-Mon-yyyy HH24:MI:SS'),'" + disBursementType
						+ "'," + escalation + "," + advancePayments + "," + securedAdvances + "," + deductAmountWitheld + "," + departmentId + "," + month + "," + financialYearId + ")";

		LOGGER.info(insertQuery);
		Statement statement = connection.createStatement();
		statement.executeUpdate(insertQuery);
		statement.close();
	}

public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isBillId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE billId = " + billId;

			LOGGER.info(updateQuery);
			Statement statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery="UPDATE EG_BILLREGISTERMIS SET";
		}
	}


	}