/*
 * Created on Apr 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.master;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.domain.FinancialYear;
import com.exilant.eGov.src.domain.FiscalPeriod;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author rashmi.mahale
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FinancialYearMod extends AbstractTask{
	private final static Logger LOGGER=Logger.getLogger(FinancialYearMod.class);
	private DataCollection dc;
	private Connection connection=null;
	private int fid;
	private String status;

	public void execute (String taskName,
			String gridName,
			DataCollection dataCollection,
			Connection conn,
			boolean errorOnNoData,
			boolean gridHasColumnHeading, String prefix) throws TaskFailedException {

		dc=dataCollection;

		//LOGGER.debug("financialYear_financialYear :" + dataCollection.getValue("financialYear_financialYear"));
		this.connection=conn;
		try{
			postInFinancialYear();
			postInFiscalPeriod();
			dc.addMessage("eGovSuccess","Fiscal Year");
			}catch(Exception ex){
				LOGGER.debug("Error : " + ex.toString());
				dc.addMessage("userFailure"," Insertion Failure");
				throw new TaskFailedException(ex);
			}
	}

	private void postInFinancialYear()throws SQLException,TaskFailedException{
		FinancialYear FY=new FinancialYear();
		FY.setId(dc.getValue("financialYear_ID"));
		FY.setFinancialYear(dc.getValue("financialYear_financialYear"));
		FY.setStartingDate(dc.getValue("financialYear_startingDate"));
		FY.setEndingDate(dc.getValue("financialYear_endingDate"));
		FY.setModifiedBy(dc.getValue("egUser_id"));
		FY.update(connection);
		fid=FY.getId();
	}
  private void postInFiscalPeriod()throws SQLException,TaskFailedException{
  		FiscalPeriod FP =new FiscalPeriod();
		status=dc.getValue("fiscalPeriod");
		FP.setFinancialYearId(fid + "");
		String fGrid[][]=(String [][])dc.getGrid("fiscalPeriodGrid");


  			for(int i=0;i<fGrid.length;i++){
  				if(fGrid[i][1].equalsIgnoreCase("")) continue;
	  			FP.setName(fGrid[i][1]);
				FP.setStartingDate(fGrid[i][2]);
				FP.setEndingDate(fGrid[i][3]);
				FP.setModifiedBy(dc.getValue("egUser_id"));

				if(fGrid[i][0].equals("")){
					FP.insert(connection);
				}
				else{
					FP.setId(fGrid[i][0]);
					FP.update(connection);
				}

  			}

  }		//end of post
}//end of class
