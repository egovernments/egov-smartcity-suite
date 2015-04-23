/*
 * Created on Jan 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.master;
import com.exilant.eGov.src.domain.*;
import com.exilant.exility.common.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;




public class BillCollectorNew extends AbstractTask {
	private Connection connection;
	private Statement statement;
	private ResultSet resultset;
	private boolean autoCommit=false;
	private DataCollection dc;
	private int billCollectorId=0;
	public BillCollectorNew()
	{   }
	
	public void execute (String taskName,
			String gridName,
			DataCollection dc,
			Connection con,
			boolean errorOnNoData,
			boolean gridHasColumnHeading, String prefix) throws TaskFailedException
	{
		this.dc = dc;
		this.connection = con;
		
		try {

			postInBillCollector();
			postInBillCollectorDetail();
			dc.addMessage("eGovSuccess", "BillCollectorNew");

		}
		catch(SQLException sqlex){			
			dc.addMessage("eGovFailure","BillCollectorNew");
			throw new TaskFailedException(sqlex);
			
		}
	}
	
	
		
		private void postInBillCollector() throws SQLException,TaskFailedException
		{
			
			BillCollector BC= new BillCollector();
			BC.setId((String)dc.getValue("billCollector_ID"));
			BC.setCode((String)dc.getValue("billCollector_code"));
			BC.setName((String)dc.getValue("billCollector_name"));
			BC.setDepartmentId((String)dc.getValue("billCollector_departmentId"));
			if(dc.getValue("billCollector_cashInHand").length()>0)
				BC.setCashInHand((String)dc.getValue("billCollector_cashInHand"));
			if(dc.getValue("billCollector_chequeInHand").length()>0)
				BC.setChequeInHand((String)dc.getValue("billCollector_chequeInHand"));
			BC.setType((String)dc.getValue("billCollector_type"));
			if(dc.getValue("billCollector_zoneId").length()>0)
				BC.setZoneId((String)dc.getValue("billCollector_zoneId"));
			if(dc.getValue("billCollector_regionId").length()>0)
				BC.setRegionId((String)dc.getValue("billCollector_regionId"));
			BC.setNarration((String)dc.getValue("billCollector_narration"));
			String isActive=dc.getValue("billCollector_isActive");
			isActive = (isActive != null) ? isActive : "0";
			BC.setIsActive(isActive);
			BC.setModifiedBy(dc.getValue("egUser_id"));
			
			BC.insert(connection);
			billCollectorId = BC.getId();
		}
		
		private void postInBillCollectorDetail() throws SQLException,TaskFailedException
		{

			BillCollectorDetail BCD = new BillCollectorDetail();
			String billColId=(String)dc.getValue("billCollector_ID");
			BCD.setBillCollectorID(billColId);
			if(dc.getValue("finalWardList").length()>0)
		//	String wardList=(String)dc.getValue("finalWardList");
		//	String[] wards=wardList.split(",");
		//	for(int i=0;i<wards.length;i++){
				BCD.setWardId((String)dc.getValue("finalWardList"));
				BCD.setBillCollectorID(billCollectorId + "");
				BCD.insert(connection);
		//	}

			
		}
		
	}