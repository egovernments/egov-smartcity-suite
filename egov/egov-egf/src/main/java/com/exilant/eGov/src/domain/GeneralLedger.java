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
 * Created on Feb 14, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.domain;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
@Transactional(readOnly=true)
public class GeneralLedger {
	private String id = null;
	private String voucherLineId = null;
	private String effectiveDate = "1-Jan-1900";
	private String glCodeId = null;
	private String glCode = null;
	private String debitAmount = "0";
	private String creditAmount = "0";
	private String[] accountDetail = null;
	private String description = null;
	private String voucherHeaderId = null;
	private String created = "1-Jan-1900";
	private String functionId = null;
	private String updateQuery = "UPDATE generalledger SET";
	private boolean isId = false, isField = false;
	private static final Logger LOGGER = Logger.getLogger(GeneralLedger.class);
	private static TaskFailedException taskExc;

	public void setId(String aId) {
		id = aId;
		isId = true;
	}

	public void setAccountDetailSize(int length) {
		if (accountDetail != null)
			return;
		accountDetail = new String[length];
		for (int i = 0; i < length; i++) {
			accountDetail[i] = "0";
		}
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}
	@Transactional
	public void insert() throws SQLException,
			TaskFailedException {
		EGovernCommon commommethods = new EGovernCommon();
		Query pst = null;
		try {
			effectiveDate = String.valueOf(commommethods
					.getCurrentDate());
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			dt = sdf.parse(effectiveDate);
			effectiveDate = formatter.format(dt);

			description = commommethods.formatString(description);
			setId(String.valueOf(PrimaryKeyGenerator
					.getNextKey("GeneralLedger")));

			if (functionId == null || functionId.equals(""))
				functionId = null;
			String insertQuery;
			insertQuery = "INSERT INTO generalledger (id, voucherLineID, effectiveDate, glCodeID, "
					+ "glCode, debitAmount, creditAmount,";
			insertQuery += "description,voucherHeaderId,functionId) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
			pst = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
			pst.setString(1, id);
			pst.setString(2, voucherLineId);
			pst.setString(3, effectiveDate);
			pst.setString(4, glCodeId);
			pst.setString(5, glCode);
			pst.setString(6, debitAmount);
			pst.setString(7, creditAmount);
			pst.setString(8, description);
			pst.setString(9, voucherHeaderId);
			pst.setString(10, functionId);
			pst.executeUpdate();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw taskExc;
		} finally {
		}

	}

	/**
	 * Fucntion for update generalledger
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	@Transactional
	public void update() throws SQLException,
			TaskFailedException {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			created = formatter.format(sdf.parse(created));
			newUpdate();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw taskExc;
		}
	}

	public void newUpdate() throws TaskFailedException,
			SQLException {
		Query pstmt = null;
		StringBuilder query = new StringBuilder(500);
		query.append("update generalledger set ");
		if (voucherLineId != null)
			query.append("VOUCHERLINEID=?,");
		if (effectiveDate != null)
			query.append("EFFECTIVEDATE=?,");
		if (glCodeId != null)
			query.append("GLCODEID=?,");
		if (glCode != null)
			query.append("GLCODE=?,");
		if (debitAmount != null)
			query.append("DEBITAMOUNT=?,");
		if (creditAmount != null)
			query.append("CREDITAMOUNT=?,");
		if (description != null)
			query.append("DESCRIPTION=?,");
		if (voucherHeaderId != null)
			query.append("VOUCHERHEADERID=?,");
		if (functionId != null)
			query.append("FUNCTIONID=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		try {
			int i = 1;
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query.toString());
			if (voucherLineId != null)
				pstmt.setString(i++, voucherLineId);
			if (effectiveDate != null)
				pstmt.setString(i++, effectiveDate);
			if (glCodeId != null)
				pstmt.setString(i++, glCodeId);
			if (glCode != null)
				pstmt.setString(i++, glCode);
			if (debitAmount != null)
				pstmt.setString(i++, debitAmount);
			if (creditAmount != null)
				pstmt.setString(i++, creditAmount);
			if (description != null)
				pstmt.setString(i++, description);
			if (voucherHeaderId != null)
				pstmt.setString(i++, voucherHeaderId);
			if (functionId != null)
				pstmt.setString(i++, functionId);
			pstmt.setString(i++, id);

			pstmt.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exp in update: " + e.getMessage());
			throw taskExc;
		}
	}

	/**
	 * Function to get all the recoveries not in fund
	 * 
	 * @param ACCOUNTDETAILTYPE
	 * @param ACCOUNTDETAILKEY
	 * @param FUND
	 * @param date
	 * @param status
	 * @return HashMap with account code as the key and the total pending
	 *         recovery amount for that account code.
	 * @throws SQLException
	 * @throws TaskFailedException
	 */
	public HashMap getRecoveryForSubLedgerNotInFund(Integer ACCOUNTDETAILTYPE,
			Integer ACCOUNTDETAILKEY, Integer FUND, Date date, int status)
			throws SQLException, TaskFailedException {
		HashMap<String, BigDecimal> hmA = new HashMap<String, BigDecimal>();
		HashMap<String, BigDecimal> hmB = new HashMap<String, BigDecimal>();
		HashMap<String, BigDecimal> hmFinal = new HashMap<String, BigDecimal>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String vDate = formatter.format(date);
		Query pst = null;
		List<Object[]> rs = null;
		try {

			// Query1 - to get the sum of credit amount glcode wise
			String selQuery = "SELECT GL.GLCODE as ACCOUNTCODE,SUM(GLD.AMOUNT) AS CREDITAMOUNT FROM VOUCHERHEADER VH,GENERALLEDGER GL,GENERALLEDGERDETAIL GLD "
					+ " WHERE VH.FUNDID NOT IN (?) AND GLD.DETAILTYPEID= ? AND DETAILKEYID= ? AND VH.STATUS= ? AND GL.CREDITAMOUNT>0 "
					+ " AND VH.ID=GL.VOUCHERHEADERID AND GL.ID=GLD.GENERALLEDGERID AND VH.VOUCHERDATE<= ? GROUP BY GL.GLCODE";
			if(LOGGER.isDebugEnabled())     LOGGER.debug("query (CreditAmount)--> " + selQuery);
			pst = HibernateUtil.getCurrentSession().createSQLQuery(selQuery);
			pst.setInteger(1, FUND);
			pst.setInteger(2, ACCOUNTDETAILTYPE);
			pst.setInteger(3, ACCOUNTDETAILKEY);
			pst.setInteger(4, status);
			pst.setString(5, vDate);
			rs = pst.list();
			for(Object[] element : rs){
				hmA.put(element[0].toString(), new BigDecimal(element[1].toString()));
			}
			if(LOGGER.isDebugEnabled())     LOGGER.debug("map size -------> " + hmA.size());

			// Query2 - to get the sum of debit amount glcode wise
			selQuery = "SELECT GL.GLCODE AS GLCODE ,SUM(GLD.AMOUNT) AS DEBITAMOUNT FROM VOUCHERHEADER VH,GENERALLEDGER GL,GENERALLEDGERDETAIL GLD  "
					+ " WHERE VH.FUNDID NOT IN (?)	AND GLD.DETAILTYPEID= ? AND DETAILKEYID= ? AND VH.STATUS= ? AND GL.DEBITAMOUNT>0 AND  "
					+ " VH.ID=GL.VOUCHERHEADERID AND GL.ID=GLD.GENERALLEDGERID AND VH.VOUCHERDATE<= ? GROUP BY GL.GLCODE";
			if(LOGGER.isDebugEnabled())     LOGGER.debug("query (DebitAmount)--> " + selQuery);
			pst = HibernateUtil.getCurrentSession().createSQLQuery(selQuery);
			pst.setInteger(1, FUND);
			pst.setInteger(2, ACCOUNTDETAILTYPE);
			pst.setInteger(3, ACCOUNTDETAILKEY);
			pst.setInteger(4, status);
			pst.setString(5, vDate);
			rs = pst.list();
			for(Object[] elementB : rs){
				hmB.put(elementB[0].toString(), new BigDecimal(elementB[1].toString()));
			}
			if(LOGGER.isDebugEnabled())     LOGGER.debug("map size -------> " + hmB.size());

			if (hmA.size() == 0)
				return hmB;
			else if (hmB.size() == 0) {
				Set<Map.Entry<String, BigDecimal>> setA = hmA.entrySet();
				for (Map.Entry<String, BigDecimal> meA : setA) {
					hmFinal.put(meA.getKey(), meA.getValue().multiply(
							new BigDecimal(-1)));
				}
				return hmFinal;
			}

			// Calculating the recovery amount as:
			// Recoveryamount=DEBITAMOUNT(query 2)- CREDITAMOUNT(query 1)

			hmFinal = hmB;
			Set<Map.Entry<String, BigDecimal>> setA = hmA.entrySet();
			for (Map.Entry<String, BigDecimal> meA : setA) {
				if (hmFinal.containsKey(meA.getKey())) {
					BigDecimal iC = hmFinal.get(meA.getKey()).subtract(
							meA.getValue());
					hmFinal.put(meA.getKey(), iC);
				} else {
					hmFinal.put(meA.getKey(), meA.getValue().multiply(
							new BigDecimal(-1)));
				}
			}
			if(LOGGER.isDebugEnabled())     LOGGER.debug("hmCopy------>" + hmFinal);
		} catch (Exception e) {
			LOGGER
					.error("Exception in getRecoveryForSubLedgerNotInFund():"
							+ e);
			throw taskExc;
		} finally {
		}
		return hmFinal;
	}

	/**
	 * Function to get all the recoveries for a particular fund
	 * 
	 * @param ACCOUNTDETAILTYPE
	 * @param ACCOUNTDETAILKEY
	 * @param FUND
	 * @param date
	 * @param status
	 * @return HashMap with account code as the key and the total pending
	 *         recovery amount for that account code.
	 * @throws SQLException
	 * @throws TaskFailedException
	 */
	public HashMap getRecoveryForSubLedger(Integer ACCOUNTDETAILTYPE,
			Integer ACCOUNTDETAILKEY, Integer FUND, Date date, int status)
			throws SQLException, TaskFailedException {
		HashMap<String, BigDecimal> hmA = new HashMap<String, BigDecimal>();
		HashMap<String, BigDecimal> hmB = new HashMap<String, BigDecimal>();
		HashMap<String, BigDecimal> hmFinal = new HashMap<String, BigDecimal>();
		Query pst = null;
		List<Object[]> rs = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String vDate = formatter.format(date);
		try {

			// Query1 - to get the sum of credit amount glcode wise
			String selQuery = " SELECT GL.GLCODE as ACCOUNTCODE, SUM(GLD.AMOUNT) as CREDITAMOUNT FROM VOUCHERHEADER VH,GENERALLEDGER GL,GENERALLEDGERDETAIL GLD "
					+ " WHERE VH.FUNDID= ?	AND GLD.DETAILTYPEID= ? AND DETAILKEYID= ? AND VH.STATUS= ? AND GL.CREDITAMOUNT>0 "
					+ " AND VH.ID=GL.VOUCHERHEADERID AND GL.ID=GLD.GENERALLEDGERID AND VH.VOUCHERDATE<= ? GROUP BY GL.GLCODE";
			if(LOGGER.isDebugEnabled())     LOGGER.debug("query (CreditAmount)--> " + selQuery);
			pst = HibernateUtil.getCurrentSession().createSQLQuery(selQuery);
			pst.setInteger(1, FUND);
			pst.setInteger(2, ACCOUNTDETAILTYPE);
			pst.setInteger(3, ACCOUNTDETAILKEY);
			pst.setInteger(4, status);
			pst.setString(5, vDate);
			rs = pst.list();
			for(Object[] element : rs){
				hmA.put(element[0].toString(), new BigDecimal(element[1].toString()));
			}
			if(LOGGER.isDebugEnabled())     LOGGER.debug("map size -------> " + hmA.size());

			// Query2 - to get the sum of debit amount glcode wise
			selQuery = "SELECT GL.GLCODE as GLCODE, SUM(GLD.AMOUNT) as DEBITAMOUNT FROM VOUCHERHEADER VH,GENERALLEDGER GL,GENERALLEDGERDETAIL GLD  "
					+ "WHERE VH.FUNDID= ? AND GLD.DETAILTYPEID= ? AND DETAILKEYID= ? AND VH.STATUS= ? AND GL.DEBITAMOUNT>0 AND "
					+ "VH.ID=GL.VOUCHERHEADERID AND GL.ID=GLD.GENERALLEDGERID AND VH.VOUCHERDATE<= ? GROUP BY GL.GLCODE";
			if(LOGGER.isDebugEnabled())     LOGGER.debug("query (DebitAmount)--> " + selQuery);
			pst = HibernateUtil.getCurrentSession().createSQLQuery(selQuery);
			pst.setInteger(1, FUND);
			pst.setInteger(2, ACCOUNTDETAILTYPE);
			pst.setInteger(3, ACCOUNTDETAILKEY);
			pst.setInteger(4, status);
			pst.setString(5, vDate);
			rs = pst.list();
			for(Object[] element : rs){
				hmB.put(element[0].toString(), new BigDecimal(element[1].toString()));
			}
			if(LOGGER.isDebugEnabled())     LOGGER.debug("map size -------> " + hmB.size());

			if (hmA.size() == 0)
				return hmB;
			else if (hmB.size() == 0) {
				Set<Map.Entry<String, BigDecimal>> setA = hmA.entrySet();
				for (Map.Entry<String, BigDecimal> meA : setA) {
					hmFinal.put(meA.getKey(), meA.getValue().multiply(
							new BigDecimal(-1)));
				}
				return hmFinal;
			}

			hmFinal = hmB;
			Set<Map.Entry<String, BigDecimal>> setA = hmA.entrySet();
			for (Map.Entry<String, BigDecimal> meA : setA) {
				if (hmFinal.containsKey(meA.getKey())) {
					BigDecimal iC = hmFinal.get(meA.getKey()).subtract(
							meA.getValue());
					hmFinal.put(meA.getKey(), iC);
				} else {
					hmFinal.put(meA.getKey(), meA.getValue().multiply(
							new BigDecimal(-1)));
				}
			}
			if(LOGGER.isDebugEnabled())     LOGGER.debug("hmCopy------>" + hmFinal);
		} catch (Exception e) {
			LOGGER.error("Exception in getRecoveryForSubLedger():" + e);
			throw taskExc;
		} finally {
		}
		return hmFinal;
	}

	public String getVoucherLineId() {
		return voucherLineId;
	}

	public void setVoucherLineId(String voucherLineId) {
		this.voucherLineId = voucherLineId;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getGlCodeId() {
		return glCodeId;
	}

	public void setGlCodeId(String glCodeId) {
		this.glCodeId = glCodeId;
	}

	public String getGlCode() {
		return glCode;
	}

	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}

	public String getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(String debitAmount) {
		this.debitAmount = debitAmount;
	}

	public String getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(String creditAmount) {
		this.creditAmount = creditAmount;
	}

	public String[] getAccountDetail() {
		return accountDetail;
	}

	public void setAccountDetail(String[] accountDetail) {
		this.accountDetail = accountDetail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVoucherHeaderId() {
		return voucherHeaderId;
	}

	public void setVoucherHeaderId(String voucherHeaderId) {
		this.voucherHeaderId = voucherHeaderId;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
}
