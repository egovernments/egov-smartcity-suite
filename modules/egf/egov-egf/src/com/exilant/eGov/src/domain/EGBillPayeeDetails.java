package com.exilant.eGov.src.domain;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

public class EGBillPayeeDetails  {
	private static final Logger LOGGER=Logger.getLogger(EGBillPayeeDetails.class);

	String id;
	String egBilldetailsId;
	String accountDetailTypeId;
	String accountDetailKeyId;
	String debitAmount;
	String creditAmount;
	String lastUpdatedTime;
	String tdsId;
	private String updateQuery="UPDATE EG_BILLPAYEEDETAILS SET";
	private boolean isId=false,isField=false;


	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("EG_BILLPAYEEDETAILS")));
		String insertQuery = "INSERT INTO EG_BILLPAYEEDETAILS (Id, BilldetailId, accountDetailTypeId,accountDetailKeyId, " +
						"debitamount, creditAmount,lastUpdatedTime,tdsId) " +
						"VALUES (" + id + ", " + egBilldetailsId + ", " +accountDetailTypeId+", " + accountDetailKeyId +" , " + debitAmount +" ," + creditAmount + ",to_date('"+this.lastUpdatedTime+"','dd-Mon-yyyy HH24:MI:SS'),"+tdsId+")";
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
			updateQuery = updateQuery + " WHERE id = " + id;

			LOGGER.info(updateQuery);
			Statement statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery="UPDATE EG_BILLPAYEEDETAILS SET";
		}
	}



public void delete(Connection con)throws SQLException,TaskFailedException
{
	String delQuery="delete from EG_BILLPAYEEDETAILS where id="+getId() ;
	LOGGER.info(delQuery);
	Statement statement = con.createStatement();
	statement.executeUpdate(delQuery);
	statement.close();

}
public void deleteByBillDetailId(Connection con)throws SQLException,TaskFailedException
{
	String delQuery="delete from EG_BILLPAYEEDETAILS where BilldetailId ="+egBilldetailsId;
	LOGGER.info(delQuery);
	Statement statement = con.createStatement();
	statement.executeUpdate(delQuery);
	statement.close();

}
public void setId(String aId){ id = aId; isId=true;isField = true;}
public int getId() {return Integer.valueOf(id).intValue(); }
public void setEgBilldetailsId(String aBillId){ egBilldetailsId = aBillId; updateQuery = updateQuery + " BilldetailsId=" + egBilldetailsId + ","; isField = true;}
public void setAccountDetailTypeId(String aAccountDetailTypeId){accountDetailTypeId=aAccountDetailTypeId;updateQuery=updateQuery+"accountDetailTypeId="+accountDetailTypeId+" , ";isField=true;}
public void setAccountDetailKeyId(String aAccountDetailKeyId){accountDetailKeyId=aAccountDetailKeyId;updateQuery=updateQuery+"accountDetailKeyId="+accountDetailKeyId+" , ";isField=true;}
public void setDebitAmount(String aDebitAmount){ debitAmount=aDebitAmount;updateQuery=updateQuery+" debitAmount ="+debitAmount+" ,"; isField=true;}
public void setCreditAmount(String aCreditAmount){ creditAmount=aCreditAmount;updateQuery=updateQuery+" creditAmount ="+creditAmount+" ,"; isField=true;}
public void setLastUpdatedTime(String aLastModifiedDate){ lastUpdatedTime = aLastModifiedDate; updateQuery = updateQuery + " lastUpdateTime=to_date('" + lastUpdatedTime + "','dd-Mon-yyyy HH24:MI:SS')"+","; isField = true;}
public void setTdsId(String tId){tdsId=tId;updateQuery=updateQuery+"tdsId="+tdsId+",";isField=true;}
public int getTdsId() {return Integer.valueOf(tdsId).intValue(); }


}