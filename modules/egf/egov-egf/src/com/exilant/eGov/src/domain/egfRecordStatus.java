/**
 * @author Raja
 * @Domain Object for egfRecordStatus table
 *
 */
package com.exilant.eGov.src.domain;

import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;

public class egfRecordStatus
{
	private static final Logger LOGGER = Logger.getLogger(egfRecordStatus.class);
	private String id = null;
	private String  updateQuery="UPDATE egf_record_status SET";
	private boolean isId=false, isField=false;
	private String voucherheaderId = null;
	private String record_Type = null;
	private String status = null;
	private String updateTime = null;
	private String userId = null;
	private String voucherHeader_voucherDate=null;
	private String effectiveDate = "1-Jan-1900";
	private String currentUserId=null;

	public void setId(String aId){ id = aId; }
	public void setVoucherheaderId(String avoucherheaderId){ voucherheaderId = avoucherheaderId;updateQuery = updateQuery + " voucherheaderId='" + voucherheaderId + "',"; isField = true; isId=true;}
	public void setEffectiveDate(String aeffectiveDate){ effectiveDate = aeffectiveDate; updateQuery = updateQuery + " updatedtime=to_date('" + effectiveDate + "','dd-Mon-yyyy HH24:MI:SS'),"; isField = true;}
	public void setRecord_Type(String arecord_Type){ record_Type = arecord_Type;updateQuery = updateQuery + " record_Type='" + record_Type + "',"; isField = true; }
	public void setStatus(String astatus){ status = astatus;updateQuery = updateQuery + " status='" + status + "',"; isField = true; }
	public void setUserId(String auserId){ userId = auserId;updateQuery = updateQuery + " userId='" + userId + "',"; isField = true; }
	public void setVoucherHeader_voucherDate(String avoucherHeader_voucherDate){ voucherHeader_voucherDate = avoucherHeader_voucherDate;updateQuery = updateQuery + " voucherHeader_voucherDate='" + voucherHeader_voucherDate + "',"; isField = true; }
	public void setCurrentUserId(String acurrentUserId){ currentUserId = acurrentUserId;updateQuery = updateQuery + " currentUserId='" + currentUserId + "',"; isField = true; }

	public int getId() {return Integer.valueOf(id).intValue(); }
	public String getvoucherheaderId(){return voucherheaderId;}
	public String getrecord_Type(){return record_Type;}
	public String getstatus(){return status;}
	public String getupdateTime(){return updateTime;}
	public String getuserId(){return userId;}
	public String geteffectiveDate(){return effectiveDate;}

	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("EGF_RECORD_STATUS")) );
		String insertQuery = "Insert into EGF_RECORD_STATUS(ID,voucherheaderid,RECORD_TYPE,STATUS,updatedtime," +
			"USERID) values("+id+","+voucherheaderId+",'"+record_Type+"',"+status+ ",to_date('" +effectiveDate+"','dd-Mon-yyyy HH24:MI:SS'),"+userId+")";
		LOGGER.info(insertQuery);
		Statement statement = connection.createStatement();
		statement.executeUpdate(insertQuery);
		statement.close();
	}

	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE voucherheaderid = " + voucherheaderId;
			LOGGER.info(updateQuery);
			Statement statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery="UPDATE EGF_RECORD_STATUS SET";
		}
	}
}

