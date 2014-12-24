package com.exilant.eGov.src.master;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.domain.*;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.common.DataCollection;


public class AccountDtlKeyAdd extends AbstractTask {
	private final static Logger LOGGER=Logger.getLogger(AccountDtlKeyAdd.class); 
	private transient Connection connection;
	//private ResultSet resultset;
	//private  boolean autoCommit=false;
	//private String currentDate="";
	private DataCollection dataCollection;



	public void execute(String taskName,
						String gridName,
						DataCollection datacollection,
						Connection conn,
						boolean errorOnNoData,
						boolean gridHasColumnHeading, String prefix) throws TaskFailedException{

			dataCollection=datacollection;
			this.connection=conn;
		try{
			postInFundAdd();
		  	}
			catch(SQLException sqlex ){
				LOGGER.debug("ERROR IN POSTING : " + sqlex.toString());
				dataCollection.addMessage("eGovFailure"," ");
				throw new TaskFailedException(sqlex);
			}
		}

		public void postInFundAdd()throws SQLException,TaskFailedException{
			 
			Statement statement=connection.createStatement();
			int cnt=1;
			String cashCode = "";
			String chequeCode = "";
			String tName=dataCollection.getValue("tableName");
			String cde=tName+"_code";
			String code=dataCollection.getValue(cde);
	//		boolean bothPresent = false;
			String desc = "";
			String attrName = "";
			ResultSet rs = null;
			ResultSet rst = null;
			String qryString = "";
			String type = "cash";
			String dtlKey="";
			String aid;

			if(tName.equals("organizationStructure")){
				desc = dataCollection.getValue("organizationStructure_description");
				cashCode =dataCollection.getValue("organizationStructure_cashInHand");
				chequeCode=dataCollection.getValue("organizationStructure_chequeInHand");

				if (cashCode.length() != 0 && chequeCode.length()!=0){
				//	bothPresent = true;
					cnt++;
				}
			}

			for(int i=1;i<=cnt;i++){
				AccountDtlKey ADk = new AccountDtlKey();
				if(!desc.equalsIgnoreCase("zone")){
					if (tName.equalsIgnoreCase("organizationstructure")){
						if(desc.equalsIgnoreCase("region")){
							if (cashCode!=null && type.equalsIgnoreCase("cash")){
								attrName = "regionCash_id";
								ADk.setglCodeID(cashCode);
								type = "cheque";
							} else if(chequeCode != null){
								attrName = "regionCheque_id";
								ADk.setglCodeID(chequeCode);
							}
						} else if(desc.equalsIgnoreCase("ward")){
							attrName = "organizationStructure_id";
						}
						qryString = "Select ID, attributeName from AccountDetailType where attributename='"+attrName+"'";
					} // end of orgstructure
			//	} // end of orgstructure
					else if(tName.equals("accountEntityMaster")){
						qryString = "Select ID, attributeName from AccountDetailType where id="+dataCollection.getValue("accountEntityMaster_detailTypeId");
						LOGGER.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+qryString);
					}else {
						qryString = "Select ID, attributeName from AccountDetailType where tablename='"+tName+"'";
					}

					rst=statement.executeQuery(qryString);
					while(rst.next()) {
						ADk.setdetailTypeId(rst.getString(1));
						ADk.setdetailName(rst.getString(2));
					}

					//select detail key for the code
					if(tName.equalsIgnoreCase("financialYear")){
						String fYear=dataCollection.getValue("financialYear_financialYear");
						//LOGGER.debug("*****************"+fYear+"*********");
						qryString = "Select ID from "+tName+" Where financialYear='"+fYear+"'";
					}else{
						qryString="Select ID from "+tName+" where code='"+code+"'";
					}
					rs=statement.executeQuery(qryString);
					while(rs.next()){
						dtlKey=rs.getString(1);
						ADk.setdetailKey(dtlKey);
					}
					rs=statement.executeQuery("Select Id from AccountDetailKey where detailkey='"+dtlKey+"' and detailName='"+attrName+"'");
					//LOGGER.debug("******************************************************************");
					if(rs.next()){
						//LOGGER.debug("********************************");
						aid=rs.getString(1);
						//LOGGER.debug(aid);
						ADk.setID(aid);
						ADk.update(connection);
						//LOGGER.debug("<<<<<<<<<<<<<<<<update service");
					}else{
						ADk.insert(connection);
					}
				}

		}// end of for

	}// end of post
}


//end of class




