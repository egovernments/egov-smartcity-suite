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
 * Created on Feb22,2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author Elzan Mathew
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly=true)
public class VoucherMIS {
	private static final Logger LOGGER = Logger.getLogger(VoucherMIS.class);
	private String id = "";
	private static TaskFailedException taskExc;
	private String voucherheaderid = null;
	private String divisionId = null;
	private String createTimeStamp = "1-Jan-1900";
	private String updateQuery = "UPDATE vouchermis SET";
	private String fundsourceid = null;
	private String segmentId = null;
	private String subSegmentId = null;
	private String departmentId = null; // same as functionary
	private String scheme = null;
	private String subscheme = null;
	private String functionary = null;
	private String function = null;
	private String sourcePath = null;
	private String budgetaryAppnumber = null;
	EGovernCommon commonmethods = new EGovernCommon();

	public VoucherMIS() {
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}
	
	public void setId(String aId) {
		id = aId;
	}

	/**
	 * This functon is to Insert the data to VoucherMIS table
	 * 
	 * @param connection
	 * @throws TaskFailedException
	 */
	@Transactional
	public void insert() throws TaskFailedException {
		Query pst = null;
		createTimeStamp = commonmethods.getCurrentDateTime();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat formatter = new SimpleDateFormat(
					"dd-MMM-yyyy HH:mm:ss");
			createTimeStamp = formatter.format(sdf.parse(createTimeStamp));
			setCreateTimeStamp(createTimeStamp);
		} catch (Exception e) {
			LOGGER.error("error in the date formate " + e);
			throw taskExc;
		}
		setId(String.valueOf(PrimaryKeyGenerator.getNextKey("vouchermis")));

		String insertQuery = "INSERT INTO vouchermis (Id, voucherheaderid, divisionId,CREATETIMESTAMP,fundsourceid,segmentid,sub_segmentid,DEPARTMENTID,schemeid,subschemeid,functionaryid,sourcepath,budgetary_appnumber,FUNCTIONID) "
				+ "VALUES ( ?, ?, ?,to_date(?,'dd-Mon-yyyy HH24:MI:SS'), ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		try {
			pst = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
			pst.setString(1, id);
			pst.setString(2, voucherheaderid);
			pst.setString(3, divisionId);
			pst.setString(4, createTimeStamp);
			pst.setString(5, fundsourceid);
			pst.setString(6, segmentId);
			pst.setString(7, subSegmentId);
			pst.setString(8, departmentId);
			pst.setString(9, scheme);
			pst.setString(10, subscheme);
			pst.setString(11, functionary);
			pst.setString(12, sourcePath);
			pst.setString(13, budgetaryAppnumber);
			pst.setString(14, function);
			pst.executeUpdate();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw taskExc;
		}
	}

	/**
	 * This function is to update the data to VoucherMIS table.
	 * 
	 * @param connection
	 * @throws TaskFailedException
	 */
	@Transactional
	public void update() throws TaskFailedException {
		createTimeStamp = commonmethods.getCurrentDateTime();

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat formatter = new SimpleDateFormat(
					"dd-MMM-yyyy HH:mm:ss");
			createTimeStamp = formatter.format(sdf.parse(createTimeStamp));
			setCreateTimeStamp(createTimeStamp);

			newUpdate();

		} catch (Exception e) {
			LOGGER.error("Error in update: " + e);
			throw taskExc;
		}
	}

	public void newUpdate() throws TaskFailedException,
			SQLException {
		Query pstmt = null;
		StringBuilder query = new StringBuilder(500);
		query.append("update vouchermis set ");
		if (voucherheaderid != null)
			query.append("voucherheaderid=?,");
		if (divisionId != null)
			query.append("divisionId=?,");
		if (createTimeStamp != null)
			query.append("createTimeStamp=?,");
		if (fundsourceid != null)
			query.append("fundsourceid=?,");
		if (segmentId != null)
			query.append("segmentId=?,");
		if (subSegmentId != null)
			query.append("sub_segmentid=?,");
		if (departmentId != null)
			query.append("departmentId=?,");
		if (scheme != null)
			query.append("schemeid=?,");
		if (subscheme != null)
			query.append("subschemeid=?,");
		if (functionary != null)
			query.append("functionaryid=?,");
		if (sourcePath != null)
			query.append("sourcePath=?,");
		if (budgetaryAppnumber != null)
			query.append("budgetary_appnumber=?,");
		if (function != null)
			query.append("functionid=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		try {
			int i = 1;
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query.toString());
			if (voucherheaderid != null)
				pstmt.setString(i++, voucherheaderid);
			if (divisionId != null)
				pstmt.setString(i++, divisionId);
			if (createTimeStamp != null)
				pstmt.setString(i++, createTimeStamp);
			if (fundsourceid != null)
				pstmt.setString(i++, fundsourceid);
			if (segmentId != null)
				pstmt.setString(i++, segmentId);
			if (subSegmentId != null)
				pstmt.setString(i++, subSegmentId);
			if (departmentId != null)
				pstmt.setString(i++, departmentId);
			if (scheme != null)
				pstmt.setString(i++, scheme);
			if (subscheme != null)
				pstmt.setString(i++, subscheme);
			if (functionary != null)
				pstmt.setString(i++, functionary);
			if (sourcePath != null)
				pstmt.setString(i++, sourcePath);
			if (budgetaryAppnumber != null)
				pstmt.setString(i++, budgetaryAppnumber);
			if (function != null)
				pstmt.setString(i++, function);
			pstmt.setString(i++, id);

			pstmt.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exp in update: " + e.getMessage());
			throw taskExc;
		}

	}

	public String getVoucherheaderid() {
		return voucherheaderid;
	}

	public void setVoucherheaderid(String voucherheaderid) {
		this.voucherheaderid = voucherheaderid;
	}

	public String getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public String getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(String createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public String getFundsourceid() {
		return fundsourceid;
	}

	public void setFundsourceid(String fundsourceid) {
		this.fundsourceid = fundsourceid;
	}

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getSubSegmentId() {
		return subSegmentId;
	}

	public void setSubSegmentId(String subSegmentId) {
		this.subSegmentId = subSegmentId;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getSubscheme() {
		return subscheme;
	}

	public void setSubscheme(String subscheme) {
		this.subscheme = subscheme;
	}

	public String getFunctionary() {
		return functionary;
	}

	public void setFunctionary(String functionary) {
		this.functionary = functionary;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getBudgetaryAppnumber() {
		return budgetaryAppnumber;
	}

	public void setBudgetaryAppnumber(String budgetaryAppnumber) {
		this.budgetaryAppnumber = budgetaryAppnumber;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

}
