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
