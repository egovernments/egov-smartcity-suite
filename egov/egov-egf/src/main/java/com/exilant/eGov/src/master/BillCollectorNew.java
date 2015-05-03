/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
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
			
			BC.insert();
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
				BCD.insert();
		//	}

			
		}
		
	}
