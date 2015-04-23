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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
public class FiscalPeriodAdd extends AbstractTask{
	private static final Logger LOGGER = Logger.getLogger(FiscalPeriodAdd.class);
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
			try{
			postInFinancialYear();
			}
			catch(Exception ex)
			{
				//if(LOGGER.isDebugEnabled())     LOGGER.debug("Errorin the postInFinancialYear();>>>>>>>>>>>  : " + ex.toString());
				throw new TaskFailedException("Errorin the postInFinancialYear();>>>>>>>>>>>  :  : "+ex);

			}
			try{
			postInFiscalPeriod();
			}
			catch(Exception ex)
			{
				//if(LOGGER.isDebugEnabled())     LOGGER.debug("Errorin the postInFiscalPeriod();.......................  : " + ex.toString());
				throw new TaskFailedException("Errorin the postInFiscalPeriod();.......................  : "+ex);
			}


			dc.addMessage("eGovSuccess","Fiscal Year");
			}catch(Exception ex){
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Error : " + ex.toString());
				dc.addMessage("userFailure"," Insertion Failure");
				throw new TaskFailedException(ex);
			}
	}

	private void postInFinancialYear()throws SQLException,TaskFailedException{
		FinancialYear FY=new FinancialYear();
		String open=dc.getValue("isActiveForPosting");
		FY.setFinancialYear(dc.getValue("financialYear_financialYear"));
		try
   		{
			//if(LOGGER.isDebugEnabled())     LOGGER.debug("inside try catch");
   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
			Date dt=new Date();
			String financialYear_startingDate=dc.getValue("financialYear_startingDate");
			dt = sdf.parse( financialYear_startingDate );
			financialYear_startingDate = formatter.format(dt);

			String financialYear_endingDate=dc.getValue("financialYear_endingDate");
			dt = sdf.parse( financialYear_endingDate );
			financialYear_endingDate = formatter.format(dt);

			FY.setStartingDate(financialYear_startingDate);
			FY.setEndingDate(financialYear_endingDate);

   		}
   		catch(Exception e){
   			throw new TaskFailedException(e.getMessage());
   		}
		//FY.setStartingDate(dc.getValue("financialYear_startingDate"));
		//FY.setEndingDate(dc.getValue("financialYear_endingDate"));
		FY.setModifiedBy(dc.getValue("egUser_id"));
		if(open!=null && open.length()>=1){
			FY.setIsActiveForPosting(open);
		}
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
	  			try
	  	   		{
	  				//if(LOGGER.isDebugEnabled())     LOGGER.debug("inside try catch");
	  	   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
	  				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
	  				Date dt=new Date();
	  				String FiscalPeriod_startingDate=fGrid[i][2];
	  				dt = sdf.parse( FiscalPeriod_startingDate );
	  				FiscalPeriod_startingDate = formatter.format(dt);

	  				String FiscalPeriod_endingDate=fGrid[i][3];
	  				dt = sdf.parse( FiscalPeriod_endingDate );
	  				FiscalPeriod_endingDate = formatter.format(dt);

	  				FP.setStartingDate(FiscalPeriod_startingDate);
	  				FP.setEndingDate(FiscalPeriod_endingDate);

	  	   		}
	  	   		catch(Exception e){
	  	   			throw new TaskFailedException(e.getMessage());
	  	   		}
				//FP.setStartingDate(fGrid[i][2]);
				//FP.setEndingDate(fGrid[i][3]);
				FP.setModifiedBy(dc.getValue("egUser_id"));
				FP.insert(connection);
  			}


  }		//end of post
}//end of class
