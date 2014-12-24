/*
 * Created on Feb 28, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author Elzan Mathew
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class worksmis {

	private static final Logger LOGGER=Logger.getLogger(WorksDetail.class);
	private String id = null;
	private String worksdetailid = null;
	private String fieldid = null;
	private String schemeid = null;
	private String subschemeid = null;
	private String isFixedAsset = null;
	private String lastModifiedDate = "1-Jan-1900"; 
	EGovernCommon commonmethods = new EGovernCommon(); 
	private static TaskFailedException taskExc;
	private String updateQuery="UPDATE EGW_WORKS_MIS SET";
	public void setId(String aId){ id = aId;  }
	public int getId() {return Integer.valueOf(id).intValue(); }
	
	public void setIsFixedAsset(String isFixedAsset) {
		if(isFixedAsset != null && !("".equals(isFixedAsset))){
			this.isFixedAsset = isFixedAsset;
			updateQuery = updateQuery + " isfixedasset = " + this.isFixedAsset + ",";
		}
	}
	public void setWorksdetailid(String aRelationId){ worksdetailid = aRelationId;  updateQuery = updateQuery + " worksdetailid = " + worksdetailid + ",";  }
	public void setFieldid(String afieldid) {
		if(afieldid!=null && !afieldid.trim().equals(""))   	fieldid = afieldid;
	   	//LOGGER.info("Inside worksmis ..length of fieldid : "+fieldid.length());
	   //	if(fieldid==0) fieldid=null;
	   		updateQuery = updateQuery + " fieldid=" + fieldid + ",";
	   		 	}
	public void setSchemeid(String aschemeid) {
		
		if(aschemeid!=null && !aschemeid.trim().equals(""))   	schemeid = aschemeid;
		updateQuery = updateQuery + " schemeid=" + schemeid + ",";
	   	
	   	}
	public void setSubschemeid(String asubschemeid) {
		if(asubschemeid!=null && !asubschemeid.trim().equals(""))   	subschemeid = asubschemeid;
	  	updateQuery = updateQuery + " subschemeid=" + subschemeid + ",";
		
	}
	public void setLastModifiedDate(String aLastModifiedDate) {lastModifiedDate = aLastModifiedDate; updateQuery = updateQuery + " lastmodifieddate=to_date('"+lastModifiedDate+"','dd-Mon-yyyy HH24:MI:SS'),"; }
	
	/**
	 * Function to insert to egw_works_mis
	 * @param connection
	 * @throws TaskFailedException
	 */
	 public void insert(Connection connection)  throws TaskFailedException
	   {  
	 		Statement statement = null;
	   		lastModifiedDate = commonmethods.getCurrentDateTime(connection);
	   		try{
		   		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
				lastModifiedDate = formatter.format(sdf.parse(lastModifiedDate));
				setLastModifiedDate(lastModifiedDate);
	   	  
		   		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("egw_works_mis")) );
		   	
		   		String insertQuery = "INSERT INTO egw_works_mis(Id, worksdetailid, fieldid,lastmodifieddate,schemeid,subschemeid,isfixedasset) " +
				"VALUES ('"+id+"','"+worksdetailid+"',"+fieldid+",to_date('" + lastModifiedDate + "','dd-Mon-yyyy HH24:MI:SS'),"+schemeid+","+subschemeid+","+isFixedAsset+")";
	
		   		LOGGER.info(insertQuery);
		   		statement = connection.createStatement();
	   			statement.executeUpdate(insertQuery);
	   		}catch(Exception e){LOGGER.error(e.getMessage());
	   			throw taskExc;
	   		}finally{
	   			try{statement.close();
	   		}catch(Exception e){LOGGER.error(e.getMessage());
	   			}
	   		}
	   }
	/**
	 * Function to Update egw_works_mis
	 * @param connection
	 * @throws TaskFailedException
	 */
	 public void update (Connection connection) throws TaskFailedException
	   {
	   		lastModifiedDate = commonmethods.getCurrentDateTime(connection);
	   		Statement statement = null;
	   		try{
		   		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
				lastModifiedDate = formatter.format(sdf.parse(lastModifiedDate));
				setLastModifiedDate(lastModifiedDate);
				
				updateQuery = updateQuery.substring(0,updateQuery.length()-1);
				updateQuery = updateQuery + " WHERE worksdetailid = " + worksdetailid;
				LOGGER.info(updateQuery);
				statement = connection.createStatement();
				statement.executeUpdate(updateQuery);
				
				updateQuery="UPDATE voucherheader SET";
				
		   	}catch(Exception e){
				LOGGER.error("Error in update: "+e);
				throw taskExc;
			}finally{
	   			try{statement.close();
		   		}catch(Exception e){LOGGER.error(e.getMessage());
		   			}
		   		}
	   }
	
}
