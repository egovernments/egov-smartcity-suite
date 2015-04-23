/*
 * Created on Apr 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.master;
import java.sql.*;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.domain.*;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.common.DataCollection;

/**
 * @author rashmi.mahale
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FinancialYearAdd extends AbstractTask{
	private static final Logger LOGGER = Logger.getLogger(FinancialYearAdd.class);
	private DataCollection dc;
	private Connection connection=null;
	int fid;
	String status;

	public void execute (String taskName,
			String gridName,
			DataCollection dataCollection,
			Connection conn,
			boolean errorOnNoData,
			boolean gridHasColumnHeading, String prefix) throws TaskFailedException {

		dc=dataCollection;

		//if(LOGGER.isDebugEnabled())     LOGGER.debug("financialYear_financialYear :" + dataCollection.getValue("financialYear_financialYear"));
		this.connection=conn;
		try{
			postInFinancialYear();
			postInFiscalPeriod();

			dc.addMessage("eGovSuccess","Fiscal Year");
			}catch(Exception ex){
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Error : " + ex.toString());
				dc.addMessage("userFailure"," Insertion Failure");
				throw new TaskFailedException(ex);
			}
	}

	private void postInFinancialYear()throws SQLException,TaskFailedException{
		FinancialYear FY=new FinancialYear();
		FY.setFinancialYear(dc.getValue("financialYear_financialYear"));
		FY.setStartingDate(dc.getValue("financialYear_startingDate"));
		FY.setEndingDate(dc.getValue("financialYear_endingDate"));
		FY.setModifiedBy(dc.getValue("egUser_id"));
		FY.setIsActive("1");
		FY.insert(connection);
		fid=FY.getId();
	}
  private void postInFiscalPeriod()throws SQLException,TaskFailedException{
  		ResultSet rs = null;
  		FiscalPeriod FP =new FiscalPeriod();
		status=dc.getValue("fiscalPeriod");
		FP.setFinancialYearId(fid + "");
		String fGrid[][]=(String [][])dc.getGrid("fiscalPeriodGrid");


  			for(int i=0;i<fGrid.length;i++){
  				//if(LOGGER.isDebugEnabled())     LOGGER.debug("values : " + fGrid[i][1] + "  " + fGrid[i][2] + "  " + fGrid[i][3]);
	  			FP.setName(fGrid[i][1]);
				FP.setStartingDate(fGrid[i][2]);
				FP.setEndingDate(fGrid[i][3]);
				FP.setModifiedBy(dc.getValue("egUser_id"));
				FP.insert(connection);
  			}


  }		//end of post
}//end of class
