/*
   * CancelBills.java Created on 28 Jan 2008
   *
   * Copyright 2005 eGovernments Foundation. All rights reserved.
   * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.BillRegisterBean;
import com.exilant.eGov.src.domain.EgfStatusChange;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
/**
 * 
 * @author Mani
 * Cancellation of bills Before  it goes to bills Accounting.
 * cancelled bills will not be available for bills accounting.
 * Selected Bills  Status will be changed "Cancelled"
 * And StatusIf=d will be updated to Corresponding id From egw_status Table
 *  
 */

public class CancelBills extends AbstractTask {	
    private final static Logger LOGGER=Logger.getLogger(CancelBills.class);

	private Connection connection;
	EGovernCommon cm=new EGovernCommon();
	public CancelBills(){}
	public void execute(String taskName, 
							String gridName, 
							DataCollection dc, 
							Connection conn,
							boolean erroOrNoData,
							boolean gridHasColumnHeading, String prefix) throws TaskFailedException{
		try{ 
		this.connection = conn;
		cancelBill(dc, conn);
		dc.addMessage("eGovSuccess", "Cancel Bills");
		} catch(Exception e) {
			LOGGER.error("Exp="+e.getMessage());			
			dc.addMessage("eGovFailure", "Cancel Bills");
			throw new TaskFailedException(e.getMessage());
		}
	}
	
	/**
	 * This function will cancel the Bills selected from the input screen.
	 * it will Update the BillStatus to 'CANCELLED'
	 * Also changes the StatsusId to Corresponding 'CANCELLED' number(ie StatusId) From egw_status table 
	 * @param dc
	 * @param conn
	 * @throws TaskFailedException
	 */
		private void cancelBill(DataCollection dc,Connection conn) throws TaskFailedException{
			this.connection = conn;
       		String BillSearchGrid[][] = (String[][])dc.getGrid("BillSearchGrid");
			/*     BillSearchGrid   0.billId  1.Selected or Not (1 -> selected 0-> Unselected)     */
			 
       		LOGGER.debug(" Inside CancelBills   ");
		
			try{
			for(int i=0; i < BillSearchGrid.length; i++)
			{
				if(BillSearchGrid[i][0].equalsIgnoreCase("") || BillSearchGrid[i][1].equalsIgnoreCase("0")) continue;
				LOGGER.info(BillSearchGrid[i][0]+"    "+BillSearchGrid[i][1]);
				String billId=BillSearchGrid[i][0];
				// Get ExpenditureType from Eg_BillRegister table for the bill id since statusids differ for different ExpenditureTypes
				Statement st=conn.createStatement();
				String expSql="Select ExpenditureType from eg_billRegister where id="+billId;
				LOGGER.debug("Get ExpeditureQuery is "+expSql);
				ResultSet expResultSet=st.executeQuery(expSql);
				String expType=null;
				if(expResultSet.next())	
				expType=expResultSet.getString(1);
				if(expType!=null)
				{
				if(expType.equalsIgnoreCase("Salary"))
					{
						//update BillRegister 28.created 32.cancelled
						LOGGER.debug("Salary Bill cancel Bill Id is"+billId);
						BillRegisterBean billBean=new BillRegisterBean();
						billBean.setBillStatus("CANCELLED");
						String cancelledStatusId = cm.getEGWStatusId(conn, "SALBILL", "Cancelled");
						//billBean.setBillStatusId("32");
						billBean.setBillStatusId(cancelledStatusId);
						billBean.setId(billId);
						billBean.update(connection);
						
						//UpdateEgfStatus
						EgfStatusChange status=new EgfStatusChange();
						status.setModuleid(billId);
						status.setModuletype("SALBILL");
						//status.setFromstatus("28");
						//status.setTostatus("32");
						status.setFromstatus(cm.getEGWStatusId(conn, "SALBILL", "Created"));
						status.setTostatus(cancelledStatusId);
						status.setLastmodifieddate(cm.getCurDateTime(connection));
						status.setCreatedby(dc.getValue("egUser_id"));
						status.update(connection);
					
					}
					
					else if(expType.equalsIgnoreCase("Purchase"))
					{
						LOGGER.debug("Salary Bill cancel Bill Id is"+billId);
//						UpdateEgfStatus
						EgfStatusChange status=new EgfStatusChange();
						status.setModuleid(billId);
						status.setModuletype("PURCHBILL");
						//status.setFromstatus("37");
						//status.setTostatus("39");
						String cacelledStatusId = cm.getEGWStatusId(conn, "PURCHBILL", "Cancelled");
						status.setFromstatus(cm.getEGWStatusId(conn, "PURCHBILL", "Pending"));
						status.setTostatus(cacelledStatusId);
						status.setLastmodifieddate(cm.getCurDateTime(connection));
						status.setCreatedby(dc.getValue("egUser_id"));
						status.update(connection);
						
						BillRegisterBean billBean=new BillRegisterBean();
						billBean.setBillStatus("CANCELLED");
						//billBean.setBillStatusId("39");
						billBean.setBillStatusId(cacelledStatusId);
						billBean.setId(billId);
						billBean.update(connection);
						
					}
					else if(expType.equalsIgnoreCase("Works"))
					{
						LOGGER.debug("Salary Bill cancel Bill Id is"+billId);
//						UpdateEgfStatus
						EgfStatusChange status=new EgfStatusChange();
						status.setModuleid(billId);
						status.setModuletype("WORKSBILL");
						//status.setFromstatus("33");
						//status.setTostatus("35");
						String cancelledStatusId = cm.getEGWStatusId(conn, "WORKSBILL", "Cancelled"); 
						status.setFromstatus(cm.getEGWStatusId(conn, "WORKSBILL", "Pending"));
						status.setTostatus(cancelledStatusId);
						status.setLastmodifieddate(cm.getCurDateTime(connection));
						status.setCreatedby(dc.getValue("egUser_id"));
						status.update(connection);
						
						BillRegisterBean billBean=new BillRegisterBean();
						billBean.setBillStatus("CANCELLED");
						//billBean.setBillStatusId("35");
						billBean.setBillStatusId(cancelledStatusId);
						billBean.setId(billId);
						billBean.update(connection);						
					}
				}
					else
					{
						LOGGER.error("Error in getting ExpenditureType For Bill");
						throw new TaskFailedException("Error in getting ExpenditureType For Bill");
					}
				
				expResultSet.close();
				st.close();
		 }
			}catch(Exception e){
				LOGGER.error("Exception in Cancellation of Bills:"+e);
				dc.addMessage("eGovFailure", e.getMessage());
				throw new TaskFailedException(e.getMessage());
				}
		}
		
		
}
