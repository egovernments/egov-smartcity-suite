/*
 * Created on Jan 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import org.egov.infstr.utils.EGovConfig;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;

/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VoucherHeader
{
	private static final Logger LOGGER = Logger.getLogger(VoucherHeader.class);
	private String id = null;
	   /* CG number - unique identifier for the project	  */
	   private String cgn = null;
	   /* The system date when the transaction was created   */
	   private String cgDate = "1-Jan-1900";
	   private String name = null;
	   private String type = null;
	   private String description =null;
	   private String effectiveDate = "1-Jan-1900";
	   private String voucherNumber = null;
	   private String voucherDate = "1-Jan-1900";
	   private String departmentId = null;
	   private String functionId = null;
	   private String fundSourceId = null;
	   private String fundId = null;
	   private String fiscalPeriodId=null;
	   private String status="0";
	   private String originalVcId=null;
	   private String isConfirmed="1";
	   private String createdby=null;
	   private String refCgno="";
	   private String cgvn=null;
	   private String lastModifiedBy=null;
	   private String lastModifiedDate="1-Jan-1900";
	   private String moduleId=null;
	   private String updateQuery="UPDATE voucherheader SET";
	   private boolean isId=false, isField=false;
	   private static TaskFailedException taskExc;
	   public VoucherHeader() {}

	   public void setId(String aId) {id = aId; isId=true;}
	   public void setCgn(String aCgn){cgn = aCgn; updateQuery = updateQuery + " cgn='" + cgn + "',"; isField = true;}
	   public void setRefCgn(String aRefCgn){refCgno = aRefCgn; updateQuery = updateQuery + " refcgno='" + refCgno + "',"; isField = true;}
	   public void setCgDate(String aCgDate){cgDate = aCgDate; updateQuery = updateQuery + " cgDate='" + cgDate + "',"; isField = true;}
	   public void setName(String aName) {name = aName; updateQuery = updateQuery + " name='" + name + "',"; isField = true;}
	   public void setType(String aType) {type = aType; updateQuery = updateQuery + " type='" + type + "',"; isField = true;}
	   public void setDescription(String aDescription) {
		   LOGGER.debug("voucherheader Description Before:"+aDescription);
	   		description = aDescription!=null?commonmethods.formatString(aDescription):"";
	   		LOGGER.debug("voucherheader Description After:"+description);
	   		if(description.length()==0)description="";
	   		updateQuery = updateQuery + " description='" + description + "',";
	   		isField = true;
	   	}
	   public void setEffectiveDate(String aEffectiveDate) {effectiveDate = aEffectiveDate; updateQuery = updateQuery + " effectiveDate='" + effectiveDate + "',"; isField = true;}
	   public void setVoucherNumber(String aVoucherNumber) {voucherNumber = aVoucherNumber; updateQuery = updateQuery + " voucherNumber='" + voucherNumber.toUpperCase() + "',"; isField = true;}
	   public void setVoucherDate(String aVoucherDate) {voucherDate = aVoucherDate; updateQuery = updateQuery + " voucherDate='" + voucherDate + "',"; isField = true;}
	   public void setDepartmentId(String aDepartmentId) {
	   	departmentId = aDepartmentId;
	   	if(departmentId.length()==0)departmentId=null;
	   	updateQuery = updateQuery + " departmentId=" + departmentId + ",";
	   	isField = true;
	   	}
	   public void setFunctionId(String aFunctionId) {
	   	functionId = aFunctionId;
	   	if(functionId.length()==0)functionId=null;
	   	updateQuery = updateQuery + " functionId=" + functionId + ",";
	   	isField = true;
	   	}
	   public void setCreatedby(String aCreatedBy) {
	   	createdby= aCreatedBy;
	   	isField = true;
	  }
	   	 public void setFundSourceId(String aFundSourceId) {
			   	fundSourceId = aFundSourceId;
			   	if(aFundSourceId != null)
			   	{
				   	if(fundSourceId.length()==0 || (fundSourceId.trim()).equalsIgnoreCase(""))fundSourceId="null";
				   	updateQuery = updateQuery + " fundSourceid=" + fundSourceId + ",";
				   	isField = true;
			   	}
	   	}
	   	public void setLastModifiedBy(String aLastModifiedBy) {
	   		lastModifiedBy= aLastModifiedBy;
	   		updateQuery = updateQuery + " lastmodifiedby=" + lastModifiedBy + ",";
		   	isField = true;
		}

	   public void setLastModifiedDate(String aLastModifiedDate) {lastModifiedDate = aLastModifiedDate; updateQuery = updateQuery + " lastmodifieddate=to_date('"+lastModifiedDate+"','dd-Mon-yyyy HH24:MI:SS'),"; isField = true;}

	   public void setFundId(String aFundId) {fundId = aFundId; updateQuery = updateQuery + " fundId=" + fundId + ","; isField = true;}
	   public void setFiscalPeriodId(String afiscalPeriodId) {fiscalPeriodId = afiscalPeriodId; updateQuery = updateQuery + " fiscalPeriodId=" + fiscalPeriodId + ","; isField = true;}
	   public void setStatus(String aStatus) { status =aStatus;updateQuery = updateQuery + " status=" + status + ","; isField = true;}
	   public void setOriginalVcId(String aOriginalVcId) {originalVcId =aOriginalVcId;updateQuery = updateQuery + " originalVcId=" + originalVcId + ","; isField = true;}
	   public void setIsConfirmed(String aIsConfirmed) {isConfirmed =aIsConfirmed;updateQuery = updateQuery + " isConfirmed=" + isConfirmed + ","; isField = true;}
	   public void setCgvn(String aCgvn){cgvn = aCgvn; updateQuery = updateQuery + " cgvn='" + cgvn + "',"; isField = true;}
	   public void setModuleId(String aModuleid){moduleId = aModuleid; updateQuery = updateQuery + " moduleid='" + moduleId + "',"; isField = true;}


	   public int getId() {return Integer.valueOf(id).intValue();}
	   public String getCgn(){return cgn;}
	   public String getRefCgn(){return refCgno;}
	   public String getCgDate(){return cgDate;}
	   public String getName() {return name;}
	   public String getType() {return type;}
	   public String getDescription() {return description;}
	   public String getEffectiveDate() {return effectiveDate;}
	   public String getVoucherNumber() {return voucherNumber;}
	   public String getVoucherDate() {return voucherDate ;}
	   public String getDepartmentId() {return departmentId ;}
	   public String getFunctionId() {return functionId ;}
	   public String getFundId() {return fundId ;}
	   public String getFundSourceId() {return fundSourceId ;}
	   public String getFiscalPeriodId() {return fiscalPeriodId ;}
	   public String getStatus() {return status ;}
	   public String getOriginalVcId() {return originalVcId ;}
	   public String getCreatedby() {return createdby;}
	   public String getCgvn(){return cgvn;}
	   public String getlastModifiedBy() {return lastModifiedBy;}
	   public String getlastModifiedDate(){return lastModifiedDate; }
	   public String getModuleId() {return moduleId;}

	   EGovernCommon commonmethods = new EGovernCommon();

	   public void insert(Connection connection) throws SQLException,TaskFailedException
	   {
	   	    String code=EGovConfig.getProperty("egf_config.xml","confirmoncreate","","JournalVoucher");// get from the config file
	   	    //If its N we need to confirm on create.
			if(!this.getStatus().equals("2"))
			{
				LOGGER.info("status   "+getStatus());
				if(code.equalsIgnoreCase("N"))
					isConfirmed = "1";
				else
					isConfirmed = "0";
			}

			LOGGER.info("Inside vh departmentId   "+departmentId);
			LOGGER.info("Inside vh functionId   "+functionId);
			LOGGER.info("Inside vh fundId   "+fundId);

			LOGGER.info("Inside vh fundSourceId   "+fundSourceId);
			LOGGER.info("Inside vh fiscalPeriodId   "+fiscalPeriodId);
			LOGGER.info("Inside vh originalVcId  "+originalVcId);
			LOGGER.info("Inside vh moduleId  "+moduleId);

	   		if(departmentId==null || departmentId.length()==0)
	   			departmentId=null;

	   		if(functionId==null || functionId.length()==0)
	   			functionId=null;

	   		if(fundId==null || fundId.length()==0)
	   			fundId=null;

	   		//if(fundSourceId != null && fundSourceId.length()==0)
	   		if(fundSourceId == null || fundSourceId.length()==0)
	   			fundSourceId=null;

	   		if(fiscalPeriodId == null || fiscalPeriodId.length()==0)
	   			fiscalPeriodId=null;

	   		if(originalVcId == null || originalVcId.length()==0)
	   			originalVcId=null;

	   		if(moduleId == null || moduleId.length()==0)
	   			moduleId=null;


	   		effectiveDate = commonmethods.getCurrentDateTime(connection);

	   		try{
		   		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
				effectiveDate = formatter.format(sdf.parse( effectiveDate));
		   		setCgDate(effectiveDate);
	   		}catch(Exception e){
				LOGGER.error("Exp in insert"+e.getMessage());
				throw taskExc;
			}

	   		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("VoucherHeader")) );

	   		description = commonmethods.formatString(description);
	   		name = commonmethods.formatString(name);
	   		Statement statement = null;
	   		try
	   		{
		   		lastModifiedDate = commonmethods.getCurrentDateTime(connection);
		   		Date dt=new Date();
		   		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
				dt = sdf.parse(lastModifiedDate);
				lastModifiedDate = formatter.format(dt);

	   		
			String insertQuery = "INSERT INTO voucherheader (Id, CGN, CGDate, Name, Type, Description, " +
			"EffectiveDate,VoucherNumber,VoucherDate,DepartmentId, FundId,fiscalPeriodId,status,originalVcId,fundSourceid, isConfirmed,createdby, FunctionId,refcgno,cgvn,moduleid,LASTMODIFIEDDATE) " +
			"VALUES (" + id + ", '" + cgn + "', to_date('" + cgDate + "','dd-Mon-yyyy HH24:MI:SS'),'" + name + "','"
			+ type + "','" + description + "',to_date('" + effectiveDate + "','dd-Mon-yyyy HH24:MI:SS'),'" + voucherNumber.toUpperCase()
			+ "','" + voucherDate + "'," + departmentId + "," + fundId+","+fiscalPeriodId+","+status+","+ originalVcId+","+ fundSourceId+ ","+isConfirmed+","+createdby+","+functionId+",'"+refCgno+"','"+cgvn+"',"+moduleId+",to_date('"+lastModifiedDate+"','dd-Mon-yyyy HH24:MI:SS'))";

	   		LOGGER.info(insertQuery);
	   		statement = connection.createStatement();
			statement.executeUpdate(insertQuery);
	   		}catch(Exception e){throw taskExc;}
	   		finally{
	   			statement.close();
	   		}
			
	   }

	   public void update (Connection connection) throws SQLException,TaskFailedException
	   {
	   	lastModifiedDate = commonmethods.getCurrentDateTime(connection);

	   	try{
	   		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			lastModifiedDate = formatter.format(sdf.parse(lastModifiedDate));
			setLastModifiedDate(lastModifiedDate);
   		}catch(Exception e){
   			throw taskExc;
		}

		if(isId && isField)
		{
			description = commonmethods.formatString(description);
			name = commonmethods.formatString(name);

			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;
			LOGGER.info(updateQuery);
			Statement statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery="UPDATE voucherheader SET";
		}
	}

	public void cancelVouchers(String cgvn,Connection conn) throws Exception
	{


	        Statement statement = conn.createStatement();
			String query="select id from voucherheader where cgvn='"+cgvn+"'";
			LOGGER.info(query);
			ResultSet resultset=statement.executeQuery(query);
			String vid="";
			if(resultset.next())
				vid=resultset.getString(1);
			else
				throw new Exception("Voucher does not exist.");
			resultset.close();
			statement.close();

	        PreparedStatement ps=null;
			ResultSet rs=null;
			String today;
			VoucherHeader vh = new VoucherHeader();
			String getRefVoucher="SELECT a.id,a.vouchernumber,a.cgn FROM voucherheader a,voucherheader b "+
						"WHERE a.CGN=b.REFCGNO AND b.id=?";

				LOGGER.info("getRefVoucher   "+getRefVoucher);
				ps=conn.prepareStatement(getRefVoucher);
				vh.setId(vid);
				egfRecordStatus egfstatus= new egfRecordStatus();
				SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				EGovernCommon cm=new EGovernCommon();
				today=cm.getCurrentDate(conn);
				LOGGER.info("Update the egf_record_status table of original voucher");
				egfstatus.setEffectiveDate(formatter.format(sdf.parse(today)));
				egfstatus.setStatus("4");
				egfstatus.setVoucherheaderId(vid);
				egfstatus.update(conn);
				LOGGER.info("Update the original voucher");
				vh.setStatus(""+4);
				vh.update(conn);

				//Check if there is any related vouchers
				ps.clearParameters();
				ps.setString(1,vid);
				rs=ps.executeQuery();
				//LOGGER.info("if any related vouchers exist then we need to  that also.");
				if(rs.next())
				{
					egfRecordStatus egfstatusRef= new egfRecordStatus();
					String refVhid=rs.getString(1);
					vh.setId(refVhid);
					egfstatusRef.setEffectiveDate(formatter.format(sdf.parse(today)));
					egfstatusRef.setStatus("4");
					egfstatusRef.setVoucherheaderId(refVhid);
					egfstatusRef.update(conn);
					vh.setStatus(""+4); LOGGER.info("before voucher update");
					vh.update(conn);
				}
	}

}
