 
package com.exilant.eGov.src.master;


import java.sql.*;

import com.exilant.eGov.src.chartOfAccounts.CodeValidator;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
 

public class TaxFilterValidator extends AbstractTask{
	public void execute(String taskName,
			String gridName,
			DataCollection dc, 
			Connection conn,
			boolean errorOnNoData,
			boolean gridHasColumnHeading, String prefix) throws TaskFailedException{
		final CodeValidator cv=CodeValidator.getInstance();
		String accCode=dc.getValue("chartOfAccounts_code");
		//means it is in grid
		if(accCode==null || accCode.length()==0){
			final String[][] grid=dc.getGrid("accCodeGrid");
			for(int i=0;i<grid.length;i++){
				if(!cv.isValidGLCode("11",grid[i][2])){
					dc.addMessage("exilRPError",grid[i][2]+" is invalid ");
					throw new TaskFailedException();
				}
			}
		}else{
			if(!cv.isValidGLCode("11",accCode)){
				dc.addMessage("exilRPError",accCode+" is invalid ");
				throw new TaskFailedException();
			}
		}
			
	}
}
