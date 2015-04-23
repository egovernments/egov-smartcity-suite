/*
 * Created on Jan 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

public class SurrenderChequeDetail
{
	private String id = null;
	private String BankAccountId = null;
	private String chequeNumber ="";
	private String chequeDate = "1-Jan-1900";
	private String voucherHeaderId =null;
	private String lastModified = "1-Jan-1900";
	private String created = "1-Jan-1900";
	private static TaskFailedException taskExc;
	private String updateQuery="insert into eg_surrendered_cheques values ";
	EGovernCommon cm = new EGovernCommon();
	private final static Logger LOGGER=Logger.getLogger(SurrenderChequeDetail.class);
	Statement statement;
	public void setId(String aId){ id = aId;}
	public int getId() {return Integer.valueOf(id).intValue();}
	public void setChequeDate(String aChequeDate){ chequeDate = aChequeDate; updateQuery = updateQuery + " ChequeDate='" + chequeDate + "',";}
	public void setChequeNumber(String aChequeNumber){ chequeNumber = aChequeNumber; updateQuery = updateQuery + " ChequeNumber='" + chequeNumber + "',"; }
	public void setBankAccountId(String aBankAccountId){ BankAccountId = aBankAccountId; updateQuery = updateQuery + " BankAccountId=" + BankAccountId + ","; }
	public void setLastModified(String aLastModified){ lastModified = aLastModified; updateQuery = updateQuery + " LastModified='" + lastModified + "',"; }
	public void setCreated(String aCreated){ created = aCreated; /* not said for updation */}
	public void setVoucherHeaderId(String avoucherHeaderId){ voucherHeaderId = avoucherHeaderId;  updateQuery = updateQuery + " voucherHeaderId='" + voucherHeaderId + "',";}
	
	
	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		if(LOGGER.isInfoEnabled())     LOGGER.info("insert inside");
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate(connection);
		try
   		{
   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			created = formatter.format(sdf.parse( created ));
   		}
		catch(Exception e){throw taskExc;}
		setCreated(created);
		setLastModified(created);
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("eg_surrendered_cheques")) );

		String insertQuery = "INSERT INTO eg_surrendered_cheques (Id,BankAccountId, ChequeNumber,ChequeDate,VHID,LastModifiedDate) " +
		" VALUES(" + id + ","+ BankAccountId +", '" + chequeNumber + "', '" + chequeDate + "','"+voucherHeaderId+"', '"+ lastModified +"')";
		Statement statement = connection.createStatement();
        if(LOGGER.isDebugEnabled())     LOGGER.debug("insertQuery:"+insertQuery);
		statement.executeUpdate(insertQuery);
		statement.close();

	}
	
}
