/*
 * Created on Apr 5, 2005
 * @author pushpendra.singh
 */
 
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import com.exilant.exility.common.TaskFailedException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

public class WorksDetail
{
	private static final Logger LOGGER=Logger.getLogger(WorksDetail.class);
	private String id =null;
	private String relationId =null;
	private String code =null;
	private String orderDate = "1-Jan-1900";
	private String totalValue = "0";
	private String paidAmount = "0";
	private String advancePayable = "0";
	private String advanceAmount = "0";
	private String advanceAdjAmount="0";
	private String isActive = "0";
	private String created = "1-Jan-1900";
	private String lastmodified = "1-Jan-1900";
	private String modifiedBy = null;
	private String name =null;
	private String authorizedBy = null;
	private String levelOfWork =null;
	private String wardId = null;
	private String fundingFrom = null;
	private String tdsId = null;
	private String securityDeposit =null;
	private String retention =null;
	private String bankGuarantee ="";
	private String glCodeId =null;
	private String passedAmount = "0";
	private String type = null;

    private String fundId=null;
    private String fundSourceId=null;
    private String sanctionNo=null;
    private String worksTds=null;
    private String remarks="";
    private String sanctionDate="1-Jan-1900";
    private String schemeId=null;
    private String workCategory=null;
    private String subCategory=null;
    private String execdeptid=null;
    EGovernCommon cm = new EGovernCommon();
    private static TaskFailedException taskExc;
	private String updateQuery="UPDATE WorksDetail SET";
	private boolean isId=false, isField=false;
	
	public void setId(String aId){ id = aId;isId=true;  }
	public int getId() {return Integer.valueOf(id).intValue(); }
	public void setRelationId(String aRelationId){ relationId = aRelationId;  updateQuery = updateQuery + " relationid = " + relationId + ","; isField = true; }
	public void setCode(String aCode){ code = aCode;  updateQuery = updateQuery + " code = '" + code + "',"; isField = true; }
	public void setType(String aType){ type = aType;  updateQuery = updateQuery + " type = '" + aType + "',"; isField = true; }
	public void setOrderDate(String aOrderDate){ orderDate = aOrderDate;  updateQuery = updateQuery + " orderdate = '" + orderDate + "',"; isField = true; }
	public void setTotalValue(String aTotalValue){ totalValue = aTotalValue;  updateQuery = updateQuery + " totalvalue = " + totalValue + ","; isField = true; }
	public void setPaidAmount(String aPaidAmount){ paidAmount = aPaidAmount;  updateQuery = updateQuery + " paidamount = paidAmount+" + paidAmount + ","; isField = true; }
	public void setAdvancePayable(String aAdvancePayable){ advancePayable = aAdvancePayable;  updateQuery = updateQuery + " advancepayable = " + advancePayable + ","; isField = true; }
	public void setAdvanceAmount(String aAdvanceAmount){ advanceAmount = aAdvanceAmount;  updateQuery = updateQuery + " advanceamount =advanceamount- " + advanceAmount + ","; isField = true; }
	public void setAdvancePaidAmount(String aAdvanceAmount){ advanceAmount = aAdvanceAmount;  updateQuery = updateQuery + " advanceamount = " + advanceAmount + ","; isField = true; }
	public void setAdvanceAdjustment(String aAdvanceAdjAmount){ advanceAdjAmount = aAdvanceAdjAmount;  updateQuery = updateQuery + " advanceadj =advanceadj+ " + advanceAdjAmount + ","; isField = true; }
	public void setActualAdvanceAmount(String aAdvanceAmount){ advanceAmount = aAdvanceAmount;  updateQuery = updateQuery + " advanceamount = " + advanceAmount + ","; isField = true; }
	public void setIsActive(String aIsActive){ isActive = aIsActive;  updateQuery = updateQuery + " isactive = " + isActive + ","; isField = true; }
	public void setCreated(String aCreated){ created = aCreated;  updateQuery = updateQuery + " created = '" + created + "',"; isField = true; }
	public void setLastmodified(String aLastmodified){ lastmodified = aLastmodified;  updateQuery = updateQuery + " lastmodified = '" + lastmodified + "',"; isField = true; }
	public void setModifiedBy(String aModifiedBy){ modifiedBy = aModifiedBy;  updateQuery = updateQuery + " modifiedby = " + modifiedBy + ","; isField = true; }
	public void setName(String aname){ name = aname;  updateQuery = updateQuery + " name = '" + name + "',"; isField = true; }
	public void setAuthorizedBy(String aAuthorizedBy){ authorizedBy = aAuthorizedBy;  updateQuery = updateQuery + " authorizedby = '" + authorizedBy + "',"; isField = true; }
	public void setLevelOfWork(String aLevelOfWork){ levelOfWork = aLevelOfWork;  updateQuery = updateQuery + " levelofwork = '" + levelOfWork + "',"; isField = true; }
	public void setWardId(String aWardId){ wardId = aWardId;  updateQuery = updateQuery + " wardid = " + wardId + ","; isField = true; }
	public void setFundingFrom(String aFundingFrom){ fundingFrom = aFundingFrom;  updateQuery = updateQuery + " fundingfrom = " + fundingFrom + ","; isField = true; }
	public void seTtdsId(String aTdsId){ tdsId = aTdsId;  updateQuery = updateQuery + " tdsid = " + tdsId + ","; isField = true; }
	public void setSecurityDeposit(String aSecurityDeposit){ securityDeposit = aSecurityDeposit;  updateQuery = updateQuery + " securitydeposit = '" + securityDeposit + "',"; isField = true; }
	public void setRetention(String aRetention){ retention = aRetention;  updateQuery = updateQuery + " retention = " + retention + ","; isField = true; }
	public void setBankGuarantee(String aBankGuarantee){ bankGuarantee = aBankGuarantee;  updateQuery = updateQuery + " bankguarantee = '" + bankGuarantee + "',"; isField = true; }
	public void setGLCodeId(String aGLCodeId){ glCodeId = aGLCodeId;  updateQuery = updateQuery + " glcodeid = " + glCodeId + ","; isField = true; }
	public void setPassedAmount(String aPassedAmount){ passedAmount = aPassedAmount;  updateQuery = updateQuery + " passedamount =passedAmount+ " + passedAmount + ","; isField = true; }

    public void setFundid(String aFundId){ fundId = aFundId;  updateQuery = updateQuery + " fundid = " + fundId + ","; isField = true; }
    public void setFundSourceId(String aFundSourceId){ fundSourceId = aFundSourceId;  updateQuery = updateQuery + " fundSourceId = " + fundSourceId + ","; isField = true; }
    public void setSanctionNo(String aSanctionNo){ sanctionNo = aSanctionNo;  updateQuery = updateQuery + " sanctionNo = '" + sanctionNo + "',"; isField = true; }
    public void setWorksTds(String aWorksTds){ worksTds = aWorksTds;  updateQuery = updateQuery + " worksTds = '" + worksTds + "',"; isField = true; }
    public void setRemarks(String aRemarks){ remarks = aRemarks;  updateQuery = updateQuery + " remarks = '" + remarks + "',"; isField = true; }
    public void setSanctionDate(String aSanctionDate){ sanctionDate = aSanctionDate;  updateQuery = updateQuery + " sanctionDate = '" + sanctionDate + "',"; isField = true; }
    public void setSchemeId(String aSchemeId){ schemeId = aSchemeId;  updateQuery = updateQuery + " schemeId = " + schemeId + ","; isField = true; }
	public void setWorkCategory(String workCategory) {this.workCategory = cm.assignValue(workCategory, this.workCategory);	updateQuery = updateQuery + " workCategory = " + this.workCategory + ","; isField = true; }
	public void setSubCategory(String subCategory) {this.subCategory = cm.assignValue(subCategory, this.subCategory);	updateQuery = updateQuery + " subCategory = " + this.subCategory + ","; isField = true; }
	public void setExecdeptid(String execdeptid) {
		this.execdeptid = execdeptid;
		updateQuery = updateQuery + " execdeptid = " + execdeptid + ",";
		isField = true;
	}
	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("WorksDetail")) );
		created = cm.getCurrentDate(connection);
		Statement statement =null;
		try{
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			String currentdate = formatter.format(sdf.parse(created));
			setCreated(currentdate);
			setLastmodified(currentdate);
			String insertQuery = "INSERT INTO WorksDetail (id, relationid, code, orderdate, totalvalue, " +
						"paidamount, advancepayable, advanceamount, isactive, created, lastmodified, " +
						"modifiedby, name, authorizedby, levelofwork, wardid, schemeid, tdsid, " +
						"securitydeposit, retention, bankguarantee, GLCODEID,PASSEDAMOUNT,type,advanceadj,fundid,"+
	                    "fundsourceid,sanctionno,workstds,remarks,sanctiondate,workcategory,subcategory,execdeptid) " +
						"VALUES (" + id + ", " + relationId + ", '" + code + "', '" + orderDate + "', "
						+ totalValue + ", " + paidAmount + ", " + advancePayable + ", " + advanceAmount + ", "
						+ isActive + ", '" + created + "', '" + lastmodified + "', " + modifiedBy + ", '"
						+ name + "', '" + authorizedBy + "', '" + levelOfWork + "', " + wardId + ", "
						+ schemeId + ", " + tdsId + ", '" + securityDeposit + "', " + retention + ", '"
						+ bankGuarantee + "', " + glCodeId + ","+passedAmount+",'"+type+"',"+advanceAdjAmount+","+fundId
	                    +","+fundSourceId+",'"+sanctionNo+"',"+worksTds+",'"+remarks+"',"+"'"+sanctionDate+"',"+workCategory+","+subCategory+","+execdeptid+")";
	        LOGGER.debug(insertQuery);
			statement = connection.createStatement();
			statement.executeUpdate(insertQuery);
		}catch(Exception e){
			LOGGER.error("Error in insert: "+e);
			throw taskExc;
		}finally{
   			try{statement.close();
	   		}catch(Exception e){LOGGER.error(e.getMessage());
   			}
   		}
		
	}

	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		created = cm.getCurrentDate(connection);
		String currentdate="";
		Statement statement = null;
		try{
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			currentdate = formatter.format(sdf.parse(created));

		setLastmodified(currentdate);

		if(isId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;
            LOGGER.info(updateQuery);
			statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			updateQuery="UPDATE WorksDetail SET";
		}
		}catch(Exception e){
			LOGGER.error("Error in update: "+e);
			throw taskExc;
		}finally{
   			try{statement.close();
	   		}catch(Exception e){LOGGER.error(e.getMessage());
	   			}
	   		}
	}

	public void reversePositive (Connection connection,double paidAmount ,double adjAmount,double passedAmount,double advanceAmount)
																			throws SQLException,TaskFailedException
	{
		Statement statement=null;
		if(isId){
			try{
				statement=connection.createStatement();
				created = cm.getCurrentDate(connection);
				
		   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
					created = formatter.format(sdf.parse(created));
		 
				setLastmodified(created);
				String reversePositive="UPDATE worksDetail SET paidAmount=paidAmount+"+paidAmount+
				", advanceAdj=advanceAdj+"+adjAmount+", passedAmount=passedAmount+"+passedAmount+
				", advanceAmount=advanceAmount+"+advanceAmount+"WHERE id="+ id;
				LOGGER.info(reversePositive);
				statement.executeQuery(reversePositive);
		}catch(Exception e){
			LOGGER.error("Error in reversepostitive: "+e);
			throw taskExc;
		}finally{
			try{statement.close();
		 		}catch(Exception e){LOGGER.error(e.getMessage());
				}
			}
		}
	}

	public void reverseNegative (Connection connection,double paidAmount ,double adjAmount,double passedAmount,double advanceAmount)
																						throws SQLException,TaskFailedException
	{	
		Statement statement=null;
			if(isId){
			try
		   	{
				statement=connection.createStatement();
				created = cm.getCurrentDate(connection);
	   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				created = formatter.format(sdf.parse( created ));
				setLastmodified(created);
				setLastmodified(cm.getCurrentDate(connection));
				String reverseNegative="UPDATE worksDetail SET paidAmount=paidAmount-"+paidAmount+
				", advanceAdj=advanceAdj-"+adjAmount+", passedAmount=passedAmount-"+passedAmount+
				", advanceAmount=advanceAmount-"+advanceAmount+" WHERE id="+ id;
				LOGGER.info(reverseNegative);
				statement.executeQuery(reverseNegative);
			}catch(Exception e){
				LOGGER.error("Error in reversenegative: "+e);
				throw taskExc;
			}finally{
	   			try{statement.close();
		   		}catch(Exception e){LOGGER.error(e.getMessage());
	   			}
	   		}
		}
	}

	public Map getContractorSupplier(Connection con) throws Exception
	{
		Statement statement =null;
		ResultSet rs=null;
	
		String query="SELECT id , name  FROM relation WHERE isActive='1' order by name";
		LOGGER.info("  query   "+query);
		Map hm=new LinkedHashMap();
		try
		{
			statement = con.createStatement();
			rs=statement.executeQuery(query);
			while (rs.next())
			{
				hm.put(rs.getString(1),rs.getString(2));
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Exp in getContractorSupplier"+e.getMessage());
			throw taskExc;
		}finally{
   			try{
   				rs.close();
   				statement.close();
	   		}catch(Exception e){LOGGER.error(e.getMessage());
   			}
   		}

		return hm;
	}
}


