/*
 * Created on Jan 4, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.master;

/**
 * @author Lakshmi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

import com.exilant.exility.common.AbstractTask;
import com.exilant.eGov.src.domain.ScheduleMapping;
import com.exilant.eGov.src.domain.ChartOfAccts;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class ScheduleMaster extends AbstractTask {
	private Connection con;
	private DataCollection dc;
	private static final Logger LOGGER = Logger.getLogger(ScheduleMaster.class);
	private static final String SCHNUMBER = "schNumber";
	private static final String REPTYPE = "repType";
	private static final String REPSUBTYPE = "repSubType";

	/* Abstract method of AbstractTask Class
	 */
	public void execute(String taskName, String gridName, DataCollection dc,
			Connection conn, boolean errorOnNoData,
			boolean gridHasColumnHeading, String prefix)
			throws TaskFailedException {
		this.con = conn;
		this.dc = dc;
		String mode = dc.getValue("mode");
		if (mode.equalsIgnoreCase("insert") && addSchedule()) {
			dc.addMessage("userSuccess", " Schedule No: "
					+ dc.getValue(SCHNUMBER), " Inserted Successfully");
		} else if (mode.equalsIgnoreCase("edit") && editSchedule()) {
			dc.addMessage("userSuccess", " Schedule No: "
					+ dc.getValue(SCHNUMBER), " updated Successfully");
		}
	}

	public boolean addSchedule() throws TaskFailedException {
		int schId = 0;
		Statement st = null;
		ResultSet rs = null;
		final String schNo = dc.getValue(SCHNUMBER);
		String query = "select id from schedulemapping where schedule='"
				+ schNo.toUpperCase() + "'";
		LOGGER.info(query);
		try {
			st = con.createStatement();
			rs = st.executeQuery(query);
			/** Checks whether given Schedule Number already Exists **/
			if (rs.next()) {
				dc.addMessage("exilError", ": Duplicate Schedule No.:" + schNo);
				throw new TaskFailedException();
			}
			rs.close();
			st.close();
			/** If doesnot exist insert into scheduleMapping table and update chartofaccounts table **/
			schId = postInSchedulemapping();
			if (schId == 0) {
				dc.addMessage("exilError", " : Error in Schedule Creation");
				throw new TaskFailedException();
			}
			updateChartofaccounts(schId);
		} catch (SQLException s) {
			dc.addMessage("exilError", " : Error in Schedule Creation");
			LOGGER.debug(s);
			throw new TaskFailedException();
		}
		return true;
	}

	public int postInSchedulemapping() throws TaskFailedException {
		int schId = 0;
		LOGGER.info("inside postInSchedulemapping");
		ScheduleMapping sch = new ScheduleMapping();
		sch.setSchedule(dc.getValue(SCHNUMBER));
		sch.setScheduleName(dc.getValue("schName"));
		sch.setReportType(dc.getValue(REPTYPE));
		sch.setCreatedBy(dc.getValue("egUser_id"));
		if ("RP".equalsIgnoreCase(dc.getValue(REPTYPE))) {
			sch.setRepSubType(dc.getValue(REPSUBTYPE));
			if ("POP".equalsIgnoreCase(dc.getValue(REPSUBTYPE)))
				sch.setIsRemission(dc.getValue("isRemission"));
		}

		try {
			sch.insert(con);
		} catch (SQLException s) {
			dc.addMessage("exilError", " : Error in Schedule Creation");
			throw new TaskFailedException();
		}
		schId = sch.getId();
		return schId;
	}

	public void updateChartofaccounts(int schMapId) throws TaskFailedException {
		ChartOfAccts coa = new ChartOfAccts();
		String[][] scheduleGrid = (String[][]) dc.getGrid("schLineItem");
		LOGGER.info("rep Type:" + dc.getValue(REPTYPE));
		LOGGER.info("schMapId:" + schMapId);
		if ("RP".equalsIgnoreCase(dc.getValue(REPTYPE))) {
			for (int i = 0; i < scheduleGrid.length; i++) {
				String accntCode = scheduleGrid[i][0];
				String glCode = null;
				StringTokenizer token = new StringTokenizer(accntCode, "  ~  ");
				if (token.hasMoreTokens()) {
					glCode = token.nextToken();
				}
				if ("ROP".equalsIgnoreCase(dc.getValue(REPSUBTYPE))
						|| "RNOP".equalsIgnoreCase(dc.getValue(REPSUBTYPE))) {
					coa.setReceiptscheduleid(schMapId + "");
					coa.setReceiptoperation(scheduleGrid[i][1]);
				} else if ("POP".equalsIgnoreCase(dc.getValue(REPSUBTYPE))
						|| "PNOP".equalsIgnoreCase(dc.getValue(REPSUBTYPE))) {
					coa.setPaymentscheduleid(schMapId + "");
					coa.setPaymentoperation(scheduleGrid[i][1]);
				}
				coa.setId(glCode);
				try {
					coa.update(con);
				} catch (SQLException s) {
					throw new TaskFailedException();
				}
			}
		} else {
			for (int i = 0; i < scheduleGrid.length; i++) {
				String accntCode = scheduleGrid[i][0];
				String glCode = null;
				StringTokenizer token = new StringTokenizer(accntCode, "  ~  ");
				if (token.hasMoreTokens()) {
					glCode = token.nextToken();
				}
				coa.setScheduleId(schMapId + "");
				coa.setOperation(scheduleGrid[i][1]);
				coa.setId(glCode);
				try {
					coa.update(con);
				} catch (SQLException s) {
					throw new TaskFailedException();
				}
			}
		}
	}

	public boolean editSchedule() throws TaskFailedException {
		String strSchId = dc.getValue("schId");
		int schId = Integer.parseInt(strSchId);
		try {
			/** update into scheduleMapping table and update chartofaccounts table **/
			if (schId != 0) {
				resetChartofAccounts(schId);
				updateSchedulemapping(schId);
				updateChartofaccounts(schId);
			}
		} catch (Exception s) {
			dc.addMessage("exilError", " : Error in Schedule Updation");
			throw new TaskFailedException();
		}
		return true;
	}

	public void updateSchedulemapping(int schMapId) throws TaskFailedException {
		LOGGER.info("schMapId:" + schMapId);
		ScheduleMapping sch = new ScheduleMapping();
		sch.setSchedule(dc.getValue(SCHNUMBER));
		sch.setScheduleName(dc.getValue("schName"));
		sch.setReportType(dc.getValue(REPTYPE));
		sch.setLastModifiedBy(dc.getValue("egUser_id"));
		if ("RP".equalsIgnoreCase(dc.getValue(REPTYPE))) {
			sch.setRepSubType(dc.getValue(REPSUBTYPE));
			if ("POP".equalsIgnoreCase(dc.getValue(REPSUBTYPE)))
				sch.setIsRemission(dc.getValue("isRemission"));
		}
		sch.setId(schMapId + "");
		try {
			sch.update(con);
		} catch (SQLException s) {
			dc.addMessage("exilError", " : Error in Schedule Updation");
			throw new TaskFailedException();
		}
	}

	public void resetChartofAccounts(int schMapId) throws TaskFailedException {
		Statement st = null;
		int rs = 0;
		String query = "update chartofaccounts set ";
		if ("RP".equalsIgnoreCase(dc.getValue(REPTYPE))) {
			StringBuffer sbuffer = new StringBuffer();
			if ("ROP".equalsIgnoreCase(dc.getValue(REPSUBTYPE))
					|| "RNOP".equalsIgnoreCase(dc.getValue(REPSUBTYPE))) {

				sbuffer
						.append(query)
						.append(
								" Receiptscheduleid=null,Receiptoperation=null where Receiptscheduleid=")
						.append(schMapId);
				//query=query+" Receiptscheduleid=null,Receiptoperation=null where Receiptscheduleid="+schMapId;
				query = sbuffer.toString();
			} else if ("POP".equalsIgnoreCase(dc.getValue(REPSUBTYPE))
					|| "PNOP".equalsIgnoreCase(dc.getValue(REPSUBTYPE))) {
				sbuffer
						.append(query)
						.append(
								" Paymentscheduleid=null,Paymentoperation=null where Paymentscheduleid=")
						.append(schMapId);

				query = sbuffer.toString();
				// query=query+" Paymentscheduleid=null,Paymentoperation=null where Paymentscheduleid="+schMapId; 
			}
		} else {
			query = query + " ScheduleId=null,Operation=null where ScheduleId="
					+ schMapId;
		}
		try {
			st = con.createStatement();
			rs = st.executeUpdate(query);
			LOGGER.info("updated " + rs + " rows..");
		} catch (SQLException s) {
			dc.addMessage("exilError", " : Error");
			throw new TaskFailedException();
		}
	}
}
