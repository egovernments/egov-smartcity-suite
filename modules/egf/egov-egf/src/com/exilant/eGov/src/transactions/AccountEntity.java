/**
 * Created on Jul 01, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.transactions;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class AccountEntity extends AbstractTask{
	private static final Logger LOGGER = Logger.getLogger(AccountEntity.class);
	private Connection connection;
	private Statement statement;
	private ResultSet resultset;
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
		try{
			statement = connection.createStatement();
		}catch(SQLException ex){
			LOGGER.debug("ERROR AccountEntity: createStatement");
			dc.addMessage("eGovFailure","createStatemant Failed");
			throw taskExc;
		}

		String accDetailTypeId = dc.getValue("accDetailTypeId");
		if(accDetailTypeId != null && !accDetailTypeId.equalsIgnoreCase("")){
			String accDetailKey = dc.getValue("accDetailKey");
			String filter = " AND accountDetailTypeId="+accDetailTypeId+" AND accountDetailKey="+accDetailKey;			
			getData(filter);
			return;
		}
		String accEntityId = getAccEntityID(dc.getValue("glCodeId"));
	//	if(accEntityId.equalsIgnoreCase("")){
			/*String gridData[][] = new String[1][2];
			gridData[0][0] = "0";
			gridData[0][1] = "";
			dc.addGrid("accEntityList",gridData);*/
			if(dc.getValue("setOrUpdate").equalsIgnoreCase("update")||dc.getValue("setOrUpdate").equalsIgnoreCase("set")){				
				if(!getData("")){					
					LOGGER.debug("ERROR AccountEntity: get data for update");
					dc.addMessage("eGovFailure","get data for update");
					throw taskExc;
				}
			}
	//	}
		if(!accEntityId.equalsIgnoreCase("")){
			String tableName = getTableName(accEntityId);
			String query="";			
			if(tableName.equalsIgnoreCase("accountentitymaster"))
				query="SELECT id, name FROM "+tableName+" WHERE detailTypeId = "+accEntityId+" ORDER BY name";
			else
				query="SELECT id, name FROM "+tableName+" ORDER BY name";

			dc.addValue("accEntityId", accEntityId);
			LOGGER.debug("query "+query);
			if(!fillDC(query)){				
				LOGGER.debug("ERROR AccountEntity->fillDC()");
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
		if (fundSourceId.trim().length()==0)
			fundsourceCondition=" AND fundSourceId is null";
		else
			fundsourceCondition=" AND fundSourceId="+ fundSourceId;
				
		String query="SELECT id, openingDebitBalance AS \"dr\", openingCreditBalance AS \"cr\" , accountdetailkey AS \"accDetailKey\" , narration as \"txnNarration\" " +
						"FROM transactionSummary WHERE glCodeId="+glCodeId+" " +
						"AND fundId="+ fundId + fundsourceCondition+" AND financialYearId="+fyId;
		LOGGER.debug("Execute get data ::::"+query);
		if(!filter.equalsIgnoreCase(""))
			query = query + filter;

		try{
			resultset = statement.executeQuery(query);
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
			LOGGER.debug("ERROR AccountEntity->getAccEntityID()"+ex);
			dc.addMessage("eGovFailure","getting accEntityId Failed");
		}

		return status;
	}
	private String getAccEntityID(String glCodeId){
		String accEntityId="";
		try{
			LOGGER.debug("SELECT detailTypeId FROM chartOfAccountDetail WHERE glCodeId="+glCodeId);
			resultset = statement.executeQuery("SELECT detailTypeId FROM chartOfAccountDetail WHERE glCodeId="+glCodeId);
			if(resultset.next()) accEntityId = resultset.getString(1);
			resultset.close();
		}catch(SQLException ex){			
			LOGGER.debug("ERROR AccountEntity->getAccEntityID()");
			dc.addMessage("eGovFailure","getting accEntityId Failed");
		}
		return accEntityId;
	}
	private boolean fillDC(String query) throws TaskFailedException{
		String data="";
		boolean status = false;
		try{
			resultset = statement.executeQuery(query);
			while(resultset.next()){
				data = data.concat(resultset.getString("id").concat(",".concat(resultset.getString("name").concat(";")))) ;
			}
			resultset.close();
		}catch(SQLException ex){
			LOGGER.debug("ERROR AccountEntity->fillDC()" + ex.toString());
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
		try{
			resultset = statement.executeQuery("SELECT tableName FROM accountdetailtype WHERE id = "+accEntityId);
			if(resultset.next()) tableName = resultset.getString("tableName");
			resultset.close();
		}catch(SQLException ex){
			LOGGER.debug("ERROR AccountEntity->getTableName()");			
			dc.addMessage("eGovFailure","getting accEntity Failed");
			throw taskExc;
		}
		return tableName;
	}
}
