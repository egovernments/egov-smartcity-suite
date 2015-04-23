/**
 * Created on Jul 01, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.transactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class AccountEntity extends AbstractTask{
	private static final Logger LOGGER = Logger.getLogger(AccountEntity.class);
	private Connection connection;
	private ResultSet resultset;
	private PreparedStatement pstmt=null;
	private DataCollection dc;
	private TaskFailedException taskExc;

	/*	  this method is called by Exility	*/
	public void execute(String taskName,
							String gridName,
							DataCollection dc,
							Connection conn,
							boolean erroOrNoData,
							boolean gridHasColumnHeading, String prefix) throws TaskFailedException{

		connection = conn;
		taskExc = new TaskFailedException();
		this.dc = dc;
		String accDetailTypeId = dc.getValue("accDetailTypeId");
		if(accDetailTypeId != null && !accDetailTypeId.equalsIgnoreCase("")){
			String accDetailKey = dc.getValue("accDetailKey");
			String filter = " AND accountDetailTypeId="+accDetailTypeId+" AND accountDetailKey="+accDetailKey;			
			getData(filter);
			return;
		}
		String accEntityId = getAccEntityID(dc.getValue("glCodeId"));
			if(dc.getValue("setOrUpdate").equalsIgnoreCase("update")||dc.getValue("setOrUpdate").equalsIgnoreCase("set")){				
				if(!getData("")){					
					if(LOGGER.isDebugEnabled())     LOGGER.debug("ERROR AccountEntity: get data for update");
					dc.addMessage("eGovFailure","get data for update");
					throw taskExc;
				}
			}
	//	}
		if(!accEntityId.equalsIgnoreCase("")){
			String tableName = getTableName(accEntityId);
			String query="";			
			if(tableName.equalsIgnoreCase("accountentitymaster"))
				query="SELECT id, name FROM ? WHERE detailTypeId = ? ORDER BY name";
			else
				query="SELECT id, name FROM ? ORDER BY name";
			dc.addValue("accEntityId", accEntityId);
			if(LOGGER.isDebugEnabled())     LOGGER.debug("query "+query);
			if(!fillDC(query,tableName,accEntityId)){				
				if(LOGGER.isDebugEnabled())     LOGGER.debug("ERROR AccountEntity->fillDC()");
				dc.addMessage("eGovFailure","getting accEntity Failed : AccountEntity->fillDC()");
				throw taskExc;
			}
		}
	}
	private boolean getData(String filter){
		boolean status=false;
		String fundId = dc.getValue("fundId");
		String fundSourceId = dc.getValue("fundSourceId");
		String fyId = dc.getValue("fyId");
		String glCodeId = dc.getValue("glCodeId");
		String fundsourceCondition="";
		String departmentId = dc.getValue("deptId");
		String query="";
		PreparedStatement pstmt=null;
		try{
		if(fundSourceId.trim().length()==0)	{	
		 query="SELECT id, openingDebitBalance AS \"dr\", openingCreditBalance AS \"cr\" , accountdetailkey AS \"accDetailKey\" , narration as \"txnNarration\" " +
						"FROM transactionSummary WHERE glCodeId=? AND fundId= ? AND fundSourceId is null AND financialYearId= ? and departmentId=?";
		if(!filter.equalsIgnoreCase(""))
			query = query + filter;
		pstmt=connection.prepareStatement(query);
		pstmt.setString(1,glCodeId);
		pstmt.setString(2,fundId);
		pstmt.setString(3,fyId);
		pstmt.setString(4,departmentId);
		}
		else{
		 query="SELECT id, openingDebitBalance AS \"dr\", openingCreditBalance AS \"cr\" , accountdetailkey AS \"accDetailKey\" , narration as \"txnNarration\" " +
			"FROM transactionSummary WHERE glCodeId= ? AND fundId=? AND fundSourceId=? AND financialYearId=? and departmentId=?";	
		 if(!filter.equalsIgnoreCase(""))
			 query = query + filter;
			pstmt=connection.prepareStatement(query);
			pstmt.setString(1,glCodeId);
			pstmt.setString(2,fundId);
			pstmt.setString(3,fundSourceId);
			pstmt.setString(4,fyId);
			pstmt.setString(5,departmentId);
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Execute get data ::::"+query);
		if(!filter.equalsIgnoreCase(""))
			query = query + filter;

			resultset = pstmt.executeQuery();
			if(resultset.next()){
				dc.addValue("txnId", resultset.getString("id"));
				dc.addValue("drAmt", resultset.getString("dr"));
				dc.addValue("crAmt", resultset.getString("cr"));
				dc.addValue("accEnt", resultset.getString("accDetailKey"));
				dc.addValue("txnNarration", resultset.getString("txnNarration"));
			}
			resultset.close();
			status = true;
		}catch(SQLException ex){
			if(LOGGER.isDebugEnabled())     LOGGER.debug("ERROR AccountEntity->getAccEntityID()"+ex,ex);
			dc.addMessage("eGovFailure","getting accEntityId Failed");
		}
		return status;
	}
	private String getAccEntityID(String glCodeId){
		String accEntityId="";
		PreparedStatement pstmt=null;
		String dtlQry="SELECT detailTypeId FROM chartOfAccountDetail WHERE glCodeId=?";
		try{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("SELECT detailTypeId FROM chartOfAccountDetail WHERE glCodeId="+glCodeId);
			pstmt=connection.prepareStatement(dtlQry);
			pstmt.setString(1,glCodeId);
			resultset = pstmt.executeQuery();
			if(resultset.next()) accEntityId = resultset.getString(1);
			resultset.close();
			pstmt.close();
		}catch(SQLException ex){			
			LOGGER.error("ERROR AccountEntity->getAccEntityID()",ex);
			dc.addMessage("eGovFailure","getting accEntityId Failed");
		}
		return accEntityId;
	}
	private boolean fillDC(String query,String tablName,String accEntityId) throws TaskFailedException{
		String data="";
		PreparedStatement pstmt=null;
		boolean status = false;
		try{
			if(tablName.equalsIgnoreCase("accountentitymaster")){
				pstmt=connection.prepareStatement(query);
				pstmt.setString(1,tablName);
				pstmt.setString(2,accEntityId);
				resultset = pstmt.executeQuery();
			}
			else{
				pstmt=connection.prepareStatement(query);
				pstmt.setString(1,tablName);
				resultset = pstmt.executeQuery();
			}
			while(resultset.next()){
				data = data.concat(resultset.getString("id").concat(",".concat(resultset.getString("name").concat(";")))) ;
			}
			resultset.close();
		}catch(SQLException ex){
			LOGGER.error("ERROR AccountEntity->fillDC()" + ex.toString(),ex);
			status = false;
		}
		String rows[] = data.split(";");
		String gridData[][] = new String[rows.length+1][2];
		gridData[0][0] = "0";
		gridData[0][1] = "";
		for(int i=1; i<=rows.length; i++){
			gridData[i] = rows[i-1].split(",");
		}
		dc.addGrid("accEntityList",gridData);
		status = true;
		return status;
	}           
	private String getTableName(String accEntityId) throws TaskFailedException{
		String tableName="";
		PreparedStatement pstmt=null;
		String tabQry="SELECT tableName FROM accountdetailtype WHERE id = ?";
		try{
			pstmt=connection.prepareStatement(tabQry);
			pstmt.setString(1,accEntityId);
			resultset = pstmt.executeQuery();
			if(resultset.next()) tableName = resultset.getString("tableName");
			resultset.close();
			pstmt.close();
		}catch(SQLException ex){
			LOGGER.error("ERROR AccountEntity->getTableName()",ex);			
			dc.addMessage("eGovFailure","getting accEntity Failed");
			throw taskExc;
		}
		return tableName;
	}
}
