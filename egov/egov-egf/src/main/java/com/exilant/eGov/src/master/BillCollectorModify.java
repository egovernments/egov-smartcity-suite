/*
 * Created on Feb 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.master;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.exilant.eGov.src.domain.BillCollector;
import com.exilant.eGov.src.domain.BillCollectorDetail;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class BillCollectorModify extends AbstractTask {
	private Connection connection;
	private DataCollection dc;
	PreparedStatement pstmt=null; 
	
	
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
		dc.addMessage("eGovSuccess", "BillCollectorModify");
		}
		catch(SQLException sqlex){
			dc.addMessage("eGovFailure","BillCollectorModify");
			throw new TaskFailedException(sqlex);
	}
}

	private void postInBillCollector() throws SQLException,TaskFailedException
	{
		BillCollector BC = new BillCollector();
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
		if(dc.getValue("OS_regionName").length()>0)
			BC.setRegionId((String)dc.getValue("OS_regionName"));
		BC.setNarration((String)dc.getValue("billCollector_narration"));
		String isActive=dc.getValue("billCollector_isActive");
		isActive = isActive != null ? isActive : "0";		
		BC.setIsActive(isActive);
		BC.setModifiedBy(dc.getValue("egUser_id"));

		BC.update(connection);		
	}
	
	private void postInBillCollectorDetail() throws SQLException,TaskFailedException
	{
		ResultSet rset=null;
		
		BillCollectorDetail BCD = new BillCollectorDetail();
		String billColId=(String)dc.getValue("billCollector_ID");
		BCD.setBillCollectorID(billColId);
		String selQuery="SELECT ID FROM billCollectorDetail WHERE billCollectorId=?";
		pstmt=connection.prepareStatement(selQuery);
		pstmt.setString(1,billColId);
		rset=pstmt.executeQuery();
		//ResultSet rset=statement.executeQuery("SELECT ID FROM billCollectorDetail WHERE billCollectorId="+billColId);
		String bId="";
		ArrayList idList=new ArrayList();		
		try{
			while(rset.next()){
				bId=rset.getString(1);
				idList.add(bId);
			}
			for(int i=0;i<idList.size();i++){
				bId=(String)idList.get(i);
				pstmt=connection.prepareStatement("DELETE FROM billCollectorDetail WHERE id=?");
				pstmt.setString(1,bId);
				rset=pstmt.executeQuery();
				//statement.execute("DELETE FROM billCollectorDetail WHERE id="+bId);
			}			
		}catch(Exception e){
			throw new TaskFailedException(e.getMessage());		
		}	
		//statement.close();
		if(dc.getValue("finalWardList").length()>0)
			BCD.setWardId((String)dc.getValue("finalWardList"));
		BCD.insert(connection);
	}	
}