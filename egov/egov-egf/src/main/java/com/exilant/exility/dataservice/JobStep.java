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
package com.exilant.exility.dataservice;

import java.sql.Connection;
import com.exilant.exility.updateservice.*;
import com.exilant.exility.common.*;

public class JobStep{
	private static final String TYPE_USERTASK   = "userTask";
	private static final String TYPE_SQLTASK    = "sqlTask";
	private static final String TYPE_UPDATETASK = "updateTask";
	private static final String TYPE_INSERTTASK = "insertTask";
	private static final String TYPE_BULKTASK 	= "bulkTask";
	private static final String  TYPE_SQLEXECUTIONTASK = "sqlExecutionTask";

	public String name;
	public String type;
	public String gridName;
	public boolean errorOnNoData;
	public boolean gridHasColumnHeading;
	public String prefix = "";
	private Class userTaskClass; // caches the class created from the name

	public JobStep(){
		super();
	}
	
	public void execute(DataCollection dc, Connection con ) throws TaskFailedException{
		
		AbstractTask task;
		if (this.type.equals(JobStep.TYPE_USERTASK)){
			if (this.userTaskClass == null) { //this reflection happens only once..
				try {
					this.userTaskClass = Class.forName(this.name);
				} catch (ClassNotFoundException e) {
					dc.addMessage("exilNoClass", this.name);
					throw new TaskFailedException();
				}
			}
			try {
				task = (AbstractTask)this.userTaskClass.newInstance();
			} catch (Exception e) {
				dc.addMessage("exilNoInstance", this.name);
				throw new TaskFailedException();
			}
		}
		else if(this.type.equals(JobStep.TYPE_SQLTASK)){
			task = SQLTask.getTask();
		}
		else if(this.type.equals(JobStep.TYPE_SQLEXECUTIONTASK)){
			task = SQLExecutionTask.getTask();
		}
		else if(this.type.equals(JobStep.TYPE_UPDATETASK)){
			task = UpdateTask.getTask();
		}
		else if(this.type.equals(JobStep.TYPE_INSERTTASK)){
			task = InsertTask.getTask();
		}
		else if(this.type.equals(JobStep.TYPE_BULKTASK)){
			task = BulkTask.getTask();
		}
		else{
			dc.addMessage("exilNoTaskType", this.type);
			throw new TaskFailedException();
		}
		task.execute(this.name,this.gridName,dc,con, errorOnNoData,gridHasColumnHeading,prefix);
	}
}
