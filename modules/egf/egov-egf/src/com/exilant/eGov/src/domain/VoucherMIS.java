/*
 * Created on Feb22,2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;

/**
 * @author Elzan Mathew
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
	public class VoucherMIS 
	{
	   private static final Logger LOGGER = Logger.getLogger(VoucherMIS.class);
	   private String id = "";
	   private static TaskFailedException taskExc;
	   private String voucherheaderid=null;
	   private String divisionId=null;
	   private String createTimeStamp="1-Jan-1900";
	   private String updateQuery="UPDATE vouchermis SET";
	   private String fundsourceid=null;
	   private String segmentId=null;
	   private String subSegmentId=null;
	   private String departmentId=null; //same as functionary
	   private String scheme=null;
	   private String subscheme=null;
	   private String functionary=null;
	   private String sourcePath=null;
	   private String budgetaryAppnumber=null;
	   EGovernCommon commonmethods = new EGovernCommon();
	   
	   public VoucherMIS() {}
	
	   public void setId(String aId) {id = aId; }
	   public void setVoucherheaderid(String vhid) { 	voucherheaderid = vhid; updateQuery = updateQuery + " voucherheaderid='" + voucherheaderid + "',"; }
	   public void setFundsourceid(String fsid) {
	   	fundsourceid = fsid; 
	   	
	   	if(fundsourceid != null && !fundsourceid.trim().equals(""))
	   		{ LOGGER.info(" inside if fundsourceid:"+fundsourceid);
	   		updateQuery = updateQuery + " fundsourceid=" + fundsourceid + ",";
	   		
	   		}else { LOGGER.info(" inside else fundsourceid:"+fundsourceid); fundsourceid=null; }
	   	}
	   
	   public void setDivisionId(String adivisionId) 
	   {
	   	divisionId = adivisionId;
	   	if(divisionId != null && divisionId.length()==0)
	   		divisionId=null;
	   		updateQuery = updateQuery + " divisionId=" + divisionId + ",";
	   		
	   }
	   
	   public void setSegmentId(String aSegmentId) 
	   {
	   	segmentId = aSegmentId;
	   	if(segmentId != null && segmentId.length()==0)
	   		segmentId=null;
	   		updateQuery = updateQuery + " segmentid=" + segmentId + ",";
	   		
	   }
	   
	   public void setSubSegmentId(String aSubSegmentId) 
	   {
	   	subSegmentId = aSubSegmentId;
	   	if(subSegmentId != null && subSegmentId.length()==0)
	   		subSegmentId=null;
	   		updateQuery = updateQuery + " sub_segmentid=" + subSegmentId + ",";
	   		
	   }
	   
	   public void setDepartmentId(String aDepartmentId) 
	   {
	   	departmentId = aDepartmentId;
	   	if(departmentId != null && departmentId.length()==0)
	   		departmentId=null;
	   		updateQuery = updateQuery + " DEPARTMENTID=" + departmentId + ",";
	   		
	   }
	   public void setScheme(String aScheme) 
	   {
		   scheme= aScheme;
		   if(scheme != null && scheme.length()==0)
			   scheme=null;
		   updateQuery = updateQuery + " schemeid = " + scheme + ","; 
		    
	   }
	   public void setSubscheme(String aSubscheme) 
	   { 
		   subscheme= aSubscheme;
		   if(subscheme != null && subscheme.length()==0)
			   subscheme=null;
		   updateQuery = updateQuery + " subschemeid = " + subscheme + ","; 
		    
	   }
	   public void setFunctionary(String aFunctionary) 
	   { 
		   functionary= aFunctionary;
		   if(functionary != null && functionary.length()==0)
			   functionary=null;
		   updateQuery = updateQuery + " functionaryId = " + functionary + ","; 
		    
	   }
	   public void setSourcePath(String source) 
	   { 
		   sourcePath= source;
		   if(sourcePath != null && sourcePath.length()==0)
			   sourcePath=null;
		   updateQuery = updateQuery + " sourcePath = '" + sourcePath + "',"; 
		    
	   }
	   
	   public void setBudgetaryAppnumber(String appnumber) 
	   { 
		   budgetaryAppnumber= appnumber;
		   if(budgetaryAppnumber != null && budgetaryAppnumber.length()==0)
			   sourcePath=null;
		   updateQuery = updateQuery + " budgetary_appnumber = '" + budgetaryAppnumber + "',"; 
		    
	   }
	   public void setCreateTimeStamp(String acreateTimeStamp) {createTimeStamp = acreateTimeStamp; updateQuery = updateQuery + " CREATETIMESTAMP=to_date('"+createTimeStamp+"','dd-Mon-yyyy HH24:MI:SS'),"; }
	
	 
	   public int getId() {return Integer.valueOf(id).intValue();}
	   public String getVoucherheaderid() {return voucherheaderid ;}
	   public String getDivisionId() {return divisionId;}
	   public String getFundsourceid() {return fundsourceid ;}
	   public String getSegmentId() {return segmentId ;}
	   public String getSubSegmentId() {return subSegmentId ;}
	   public String getDepartmentId() {return departmentId ;}
	   public String getCreateTimeStamp(){return createTimeStamp; }
	   public String getScheme() {	return scheme;	}
	   public String getSubscheme() {return subscheme;}
	   public String getFunctionary() {return functionary;}
	   public String getSourcePath(){return sourcePath;}
	   public String getBudgetaryAppnumber(){return budgetaryAppnumber;}
		
		/**
		 * This functon is to Insert the data to VoucherMIS table
		 * @param connection
		 * @throws TaskFailedException
		 */	   
	   public void insert(Connection connection)  throws TaskFailedException
	   {
	   		Statement statement = null;
	   		createTimeStamp = commonmethods.getCurrentDateTime(connection);
	   		try{
		   		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
				createTimeStamp = formatter.format(sdf.parse(createTimeStamp));
				setCreateTimeStamp(createTimeStamp);
	   		}catch(Exception e){
				LOGGER.error("error in the date formate "+e);
				throw taskExc;
			}
	   	  	setId( String.valueOf(PrimaryKeyGenerator.getNextKey("vouchermis")) );
	   	
	   		String insertQuery = "INSERT INTO vouchermis (Id, voucherheaderid, divisionId,CREATETIMESTAMP,fundsourceid,segmentid,sub_segmentid,DEPARTMENTID,schemeid,subschemeid,functionaryid,sourcepath,budgetary_appnumber) " +
			"VALUES ('"+id+"','"+voucherheaderid+"',"+divisionId+",to_date('" + createTimeStamp + "','dd-Mon-yyyy HH24:MI:SS'),"+ fundsourceid +","+segmentId+","+subSegmentId+","+departmentId+","+scheme+","+subscheme+","+functionary+",'"+sourcePath+"','"+budgetaryAppnumber+"')";

	   		LOGGER.info(insertQuery);
	   		try{statement = connection.createStatement();
	   			statement.executeUpdate(insertQuery);
	   			statement.close();
	   		}catch(Exception e){LOGGER.error(e.getMessage());
	   			throw taskExc;
	   		}
	   }

	   /**
	    * This function is to update the data to VoucherMIS table.
	    * @param connection
	    * @throws TaskFailedException
	    */
	   public void update (Connection connection) throws TaskFailedException
	   {
	   	createTimeStamp = commonmethods.getCurrentDateTime(connection);

	   		try{
		   		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
				createTimeStamp = formatter.format(sdf.parse(createTimeStamp));
				setCreateTimeStamp(createTimeStamp);
				updateQuery = updateQuery.substring(0,updateQuery.length()-1);
				updateQuery = updateQuery + " WHERE voucherheaderid = " + voucherheaderid;
				LOGGER.info(updateQuery);
				Statement statement = connection.createStatement();
				statement.executeUpdate(updateQuery);
				statement.close();
				updateQuery="UPDATE voucherheader SET";
				
		   	}catch(Exception e){
				LOGGER.error("Error in update: "+e);
				throw taskExc;
			}
	   }

	}
