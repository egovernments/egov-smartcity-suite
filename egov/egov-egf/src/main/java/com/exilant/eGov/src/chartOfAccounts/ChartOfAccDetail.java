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
 * Created on Mar 4, 2005
 *
 * To change the template for this generated file go to
 * Window - Prences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.chartOfAccounts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.eGov.src.domain.ChartOfAccountDetail;
import com.exilant.eGov.src.domain.ChartOfAccts;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
@Transactional(readOnly=true)
public class ChartOfAccDetail extends AbstractTask  {

	private final static Logger LOGGER = Logger
			.getLogger(ChartOfAccDetail.class);
	private Connection connection;
	HashSet hs = new HashSet();
	int coaId;
	public Integer roleId = null;
	public Integer actionId = null;
	private static final String EXILRPRRROR = "exilRPError";
	private static final String CHARTOFACCGLCODE = "chartOfAccounts_glCode";
	private static final String CHARTOFACCTYPE = "chartOfAccounts_accType1";
	private static final String CHARTOFACCID = "chartOfAccounts_ID";

	public String setQuotes(String str) {
		return str.replaceAll("'", "''");

	}

	public void execute(String taskName, String gridName, DataCollection dc,
			Connection conn, boolean erroOrNoData,
			boolean gridHasColumnHeading, String prefix)
			throws TaskFailedException {

		ChartOfAccts chart = new ChartOfAccts();
		this.connection = conn;
		String glcode = dc.getValue(CHARTOFACCGLCODE);
		hs.add(glcode);
		try {
			// Session session =HibernateUtil.getCurrentSession();
			//.HibernateUtil.beginTransaction();
			if(LOGGER.isDebugEnabled())     LOGGER.debug("glcode>>>>>>>   " + hs);
			String sql = "select b.id_role from eg_user a,eg_roles b,eg_userrole c where a.id_user=c.id_user and b.id_role=c.id_role and a.id_user= ?";
			PreparedStatement pst = connection.prepareStatement(sql);
			pst.setString(1, dc.getValue("egUser_id"));
			ResultSet rset = pst.executeQuery();
			if (rset.next()) {
				roleId = Integer.parseInt(rset.getString(1));

			}
			// ChartOfAccDetail coa=new ChartOfAccDetail();
			actionId = Integer.valueOf((Integer.parseInt(dc
					.getValue("actionId"))));
			dc.addValue("actionId", actionId);
			
			if(LOGGER.isInfoEnabled())     LOGGER.info("Connection in RBAC..:" + conn.isClosed());
		} catch (Exception e) {
			 
			LOGGER.error("Exp=" + e.getMessage(),e);
			dc.addMessage(EXILRPRRROR, "Invalid Action : " + e.getMessage());
			throw new TaskFailedException();
		}

		// Get the connection from current session as its closed above.

		chart.setGLCode(dc.getValue(CHARTOFACCGLCODE));
		chart.setName(setQuotes(dc.getValue("chartOfAccounts_name")));
		chart.setDescription(setQuotes(dc
				.getValue("chartOfAccounts_description")));
		chart.setIsActiveForPosting(dc.getValue("coa_isActiveForPosting"));
		chart.setClassification(dc.getValue("chartOfAccounts_classification"));
		// String purpose=(String)dc.getValue("chartOfAccounts_purpose");
		if (dc.getValue("chartOfAccounts_purpose").length() > 0) {
			if(LOGGER.isInfoEnabled())     LOGGER.info("inside if purpose id");
			chart.setPurposeId(dc.getValue("chartOfAccounts_purpose"));
		} else {
			chart.setPurposeId(null);
		}

		chart.setType(dc.getValue("chartOfAccounts_type"));
		chart.setFunctionReqd(dc.getValue("chartOfAccounts_funcReqd"));
		chart.setParentId(dc.getValue("chartOfAccounts_parentID"));
		chart.setModifiedBy(dc.getValue("egUser_id"));

		String budgetCheck = (String) dc.getValue("budgetCheckReqd");
		// Check glcode is of detail code then check for budget check required
		// or not
		boolean detailAccountCode = isDetailAccountCode(glcode, dc); // if(LOGGER.isDebugEnabled())     LOGGER.debug("detailAccountCode:"+detailAccountCode);
		if (budgetCheck.equalsIgnoreCase("1") && detailAccountCode) {
			chart.setBudgetCheckReqd(budgetCheck);
			if(LOGGER.isDebugEnabled())     LOGGER.debug("inside if maxlength ");
		} else if (budgetCheck.equalsIgnoreCase("1") && !detailAccountCode) {
			dc.addMessage(EXILRPRRROR,
					"\"Budget Check Reqd\" cannot be checked for non detail Account code: "
							+ glcode);
			throw new TaskFailedException();
		} else if (!budgetCheck.equalsIgnoreCase("1") && detailAccountCode) {
			chart.setBudgetCheckReqd("0");
		}

		if (dc.getValue(CHARTOFACCID).equalsIgnoreCase("")) {

			if (!isMaxLength(glcode, dc)) {
				throw new TaskFailedException();
			}
			try {
				if(LOGGER.isInfoEnabled())     LOGGER.info("Connection before isUniqueGL:" + conn.isClosed());
			} catch (Exception e) {
				LOGGER.error("Exp in check conn" + e,e);
			}
			if (!isUniqueGL(glcode, dc,conn)) {
				throw new TaskFailedException();
			}
			try {
				chart.insert(conn);
				coaId =Integer.parseInt(chart.getId());
				dc.addValue(CHARTOFACCID, String.valueOf(chart.getId()));
				String str1 = (String) dc.getValue(CHARTOFACCTYPE);
				if (!str1.equals("0") && str1.length() != 0 && str1 != null) {
					ChartOfAccountDetail chartDetail = new ChartOfAccountDetail();
					chartDetail.setGLCodeId(String.valueOf(coaId));
					chartDetail.setDetailTypeId(dc.getValue(CHARTOFACCTYPE));
					chartDetail.insert();

				}
				String str2 = (String) dc.getValue("chartOfAccounts_funcReqd");
				if ("1".equals(str2)) {
					String chartofaccGlcode = "'"
							+ dc.getValue(CHARTOFACCGLCODE) + "%'";
					String query = "UPDATE chartofaccounts SET functionreqd=1 where glcode LIKE ?";
					PreparedStatement pst = conn.prepareStatement(query);
					pst.setString(1, chartofaccGlcode);
					int i = pst.executeUpdate();
					if(LOGGER.isDebugEnabled())     LOGGER.debug("number of rows updated " + i);

				}

				ChartOfAccounts.getInstance().reLoadAccountData();
				dc.addMessage("eGovSuccess", "Insertion of Account code");
			} catch (Exception e) {
				LOGGER.error(" Error in Insertion in chartofaccounts "
						+ e.toString(),e);
				dc.addMessage("eGovFailure", "Adding Glcode");
				LOGGER.error("Exp=" + e.getMessage(),e);
				throw new TaskFailedException();
			}
		} else {
			chart.setId(dc.getValue(CHARTOFACCID));
			try {
				String glCode = "";
				String sql = "select glcode as \"code\" from chartofaccounts where id= ?";
				PreparedStatement pst = conn.prepareStatement(sql);
				pst.setString(1, dc.getValue(CHARTOFACCID));
				ResultSet rs = null;
				pst = conn.prepareCall(sql);
				pst.setString(1, dc.getValue(CHARTOFACCID));
				rs = pst.executeQuery();
				if (rs.next()) {
					glCode = rs.getString("code");
				}
				pst.close();

				chart.update();
				String delsql = "DELETE FROM chartofaccountdetail WHERE glcodeid= ?";
				pst = conn.prepareStatement(delsql);
				pst.setString(1, dc.getValue(CHARTOFACCID));
				pst.execute();
				pst.close();
				String str1 = (String) dc.getValue(CHARTOFACCTYPE);
				if (!str1.equals("0") && str1.length() != 0 && str1 != null) {
					ChartOfAccountDetail chartDetail = new ChartOfAccountDetail();
					chartDetail.setGLCodeId(dc.getValue(CHARTOFACCID));
					chartDetail.setDetailTypeId(dc.getValue(CHARTOFACCTYPE));
					chartDetail.insert();

				}

				String query = "UPDATE chartofaccounts SET functionreqd=0 where glcode LIKE ?";
				pst = conn.prepareStatement(query);
				pst.setString(1, glCode + "%");
				pst.executeUpdate();
				pst.close();
				String str2 = (String) dc.getValue("chartOfAccounts_funcReqd");
				if ("1".equals(str2)) {
					// Statement stmt=conn.createStatement();
					// String
					// query1="UPDATE chartofaccounts SET functionreqd=1 where glcode LIKE '"+dc.getValue(CHARTOFACCGLCODE)+"%'";
					// int i=stmt.executeUpdate(query1);

				}
				ChartOfAccounts.getInstance().reLoadAccountData();
			} catch (Exception e) {
				LOGGER.error(" Error in updation " + e.toString(),e);
				dc.addMessage("eGovFailure", "Adding Glcode");
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Exp=" + e.getMessage());
				throw new TaskFailedException();
			}

		}

	}

	public Set getIeList() {
		return hs;
	}

	public void setIeList(Set ieList) {

	}

	public boolean isUniqueGL(String glcode, DataCollection dc, Connection conn) {
		boolean isUnique = false;
		try {
			String sql = "SELECT id FROM CHARTOFACCOUNTS WHERE glcode =:glcode";
			if(LOGGER.isDebugEnabled())     LOGGER.debug("SELECT id FROM CHARTOFACCOUNTS WHERE glcode = '"
					+ glcode + "'");
			List<Object[]> rs =HibernateUtil.getCurrentSession().createSQLQuery(sql).setString("glcode", glcode).list();
			if (rs!=null && rs.size()>0) {
				dc.addMessage(EXILRPRRROR, "Duplicate Glcode ");
			} else {
				isUnique = true;
			}

		} catch (HibernateException e) {
			LOGGER.error("Exception occured while getting the data  "+e.getMessage(),new HibernateException(e.getMessage()));
		}catch (Exception e) {
			LOGGER.error("Exception occured while getting the data  "+e.getMessage(),new Exception(e.getMessage()));
		}
		return isUnique;
	}

	public boolean isMaxLength(String glcode, DataCollection dc) {
		boolean isMax = false;
		try {
			String glcodelength = EGovConfig.getProperty("egf_config.xml",
					"glcodeMaxLength", "", "AccountCode");
			int gl = glcode.length();
			Integer.parseInt(glcodelength);
			if(LOGGER.isDebugEnabled())     LOGGER.debug("glcodelength ===" + gl + "glcodelength from xml=="
					+ Integer.parseInt(glcodelength));
			if (gl > Integer.parseInt(glcodelength)) {
				dc.addMessage(EXILRPRRROR,
						"Glcode length should be Less than or Equal to "
								+ Integer.parseInt(glcodelength));
			} else {
				isMax = true;
			}

		} catch (Exception ex) {

			LOGGER.error("EXP=" + ex.getMessage(),ex);
		}
		return isMax;
	}

	public boolean isDetailAccountCode(String glcode, DataCollection dc) {
		boolean isDetailCode = false;
		try {
			if(LOGGER.isDebugEnabled())     LOGGER.debug("glcodelength === " + glcode);
			String glcodelength = EGovConfig.getProperty("egf_config.xml",
					"glcodeMaxLength", "", "AccountCode");
			int gl = glcode.length();
			if(LOGGER.isDebugEnabled())     LOGGER.debug("glcodelength ===" + gl + "glcodelength from xml=="
					+ Integer.parseInt(glcodelength));
			if (gl != Integer.parseInt(glcodelength)) {
				isDetailCode = false;
			} else {
				isDetailCode = true;
			}

		} catch (Exception ex) {

			LOGGER.error("EXP=" + ex.getMessage(),ex);
		}
		return isDetailCode;
	}

}
