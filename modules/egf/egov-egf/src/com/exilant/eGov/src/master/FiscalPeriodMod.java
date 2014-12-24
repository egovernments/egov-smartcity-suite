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
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

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
public class FiscalPeriodMod extends AbstractTask {
	private static final Logger LOGGER = Logger
			.getLogger(FiscalPeriodMod.class);
	private DataCollection dc;
	private Connection connection = null;
	private Statement statement;
	int fid;
	String status;

	public void execute(String taskName, String gridName,
			DataCollection dataCollection, Connection conn,
			boolean errorOnNoData, boolean gridHasColumnHeading, String prefix)
			throws TaskFailedException {

		dc = dataCollection;

		this.connection = conn;
		try {
			checkFinancialYear();
		} catch (Exception e) {
			LOGGER.debug("Exp="+e.getMessage());
			dc.addMessage("userFailure", " Modification Failure");
			throw new TaskFailedException(e);
		}
		try {
			postInFinancialYear();
			postInFiscalPeriod();
			dc.addMessage("eGovSuccess", "Fiscal Year");
		} catch (Exception ex) {
			LOGGER.debug("Error : " + ex.toString());
			dc.addMessage("userFailure", " Modification Failure");
			throw new TaskFailedException(ex);
		}

	}

	private void checkFinancialYear() throws Exception {
	//	FinancialYear FY = new FinancialYear();
		statement = connection.createStatement();
		ResultSet resultSet = null;
		//String fid=dc.getValue("financialYear_ID");
		//LOGGER.debug("fid : " +fid);
		resultSet = statement
				.executeQuery("select fiscalperiodid from voucherheader where fiscalperiodid in (select fp.id from fiscalperiod fp,financialyear fy where fy.id=fp.financialyearid and fy.id='"
						+ dc.getValue("financialYear_ID") + "')");

		if (resultSet.next()) {
			dc.addMessage("userFailure", " Cannot Modify this financial year "
					+ dc.getValue("financialYear_financialYear")
					+ " as records are there for this year.");
			throw new Exception();
		}
		statement.close();
		resultSet.close();
	}

	private void postInFinancialYear() throws SQLException, TaskFailedException {
		FinancialYear FY = new FinancialYear();
		String open = dc.getValue("isActiveForPosting");
		FY.setId(dc.getValue("financialYear_ID"));
		FY.setFinancialYear(dc.getValue("financialYear_financialYear"));
		try {
			//LOGGER.debug("inside try catch");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			Date dt = new Date();
			String financialYear_startingDate = dc
					.getValue("financialYear_startingDate");
			dt = sdf.parse(financialYear_startingDate);
			financialYear_startingDate = formatter.format(dt);

			String financialYear_endingDate = dc
					.getValue("financialYear_endingDate");
			dt = sdf.parse(financialYear_endingDate);
			financialYear_endingDate = formatter.format(dt);

			FY.setStartingDate(financialYear_startingDate);
			FY.setEndingDate(financialYear_endingDate);

		} catch (Exception e) {
			throw new TaskFailedException(e.getMessage());
		}
		//FY.setStartingDate(dc.getValue("financialYear_startingDate"));
		//FY.setEndingDate(dc.getValue("financialYear_endingDate"));
		FY.setModifiedBy(dc.getValue("egUser_id"));
		if (open != null && open.length() > 0) {
			FY.setIsActiveForPosting(open);
		}
		FY.update(connection);
		fid = FY.getId();
	}

	private void postInFiscalPeriod() throws SQLException, TaskFailedException {
		statement = connection.createStatement();
		ResultSet rs = null;
		FiscalPeriod FP = new FiscalPeriod();
		status = dc.getValue("fiscalPeriod");
		FP.setFinancialYearId(fid + "");
		String fGrid[][] = (String[][]) dc.getGrid("fiscalPeriodGrid");

		for (int i = 0; i < fGrid.length; i++) {
			if (fGrid[i][1].equalsIgnoreCase("")) {
				statement.executeUpdate("delete from fiscalPeriod where id="
						+ fGrid[i][0]);
			} else {
				FP.setName(fGrid[i][1]);
				try {
					//LOGGER.debug("inside try catch");
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat formatter = new SimpleDateFormat(
							"dd-MMM-yyyy");
					Date dt = new Date();
					String FiscalPeriod_startingDate = fGrid[i][2];
					dt = sdf.parse(FiscalPeriod_startingDate);
					FiscalPeriod_startingDate = formatter.format(dt);

					String FiscalPeriod_endingDate = fGrid[i][3];
					dt = sdf.parse(FiscalPeriod_endingDate);
					FiscalPeriod_endingDate = formatter.format(dt);

					FP.setStartingDate(FiscalPeriod_startingDate);
					FP.setEndingDate(FiscalPeriod_endingDate);
				} catch (Exception e) {
					throw new TaskFailedException(e.getMessage());
				}
				//FP.setStartingDate(fGrid[i][2]);
				//FP.setEndingDate(fGrid[i][3]);
				FP.setModifiedBy(dc.getValue("egUser_id"));
				if (fGrid[i][0].equals("")) {
					FP.insert(connection);
				} else {
					FP.setId(fGrid[i][0]);
					FP.update(connection);
				}

			}
		}
	} //end of post
}//end of class
