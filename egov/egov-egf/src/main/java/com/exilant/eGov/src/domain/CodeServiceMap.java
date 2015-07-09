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
package com.exilant.eGov.src.domain;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
@Transactional(readOnly=true)
public class CodeServiceMap {
	private String id = null;
	private String serviceId = null;
	private String glCodeId = null;
	private TaskFailedException taskExc;

	private static final Logger LOGGER = Logger.getLogger(CodeServiceMap.class);

	public void setId(String aId) {
		id = aId;
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}
	@Transactional
	public void insert() throws SQLException {
		// EGovernCommon commommethods = new EGovernCommon();
		setId(String.valueOf(PrimaryKeyGenerator.getNextKey("CodeServiceMap")));
		String insertQuery = "INSERT INTO CodeServiceMap (id, serviceid, GLCODEID) "
				+ "VALUES ( ?, ?, ?)";
		Query pst = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
		pst.setString(1, id);
		pst.setString(2, serviceId);
		pst.setString(3, glCodeId);
		pst.executeUpdate();
		if(LOGGER.isDebugEnabled())     LOGGER.debug(insertQuery);
	}
	@Transactional
	public void update() throws SQLException {
		try {
			newUpdate();
		} catch (TaskFailedException e) {
			LOGGER.error("Error inside update"+e.getMessage(),e);
		}
	}
	@Transactional
	public void newUpdate() throws TaskFailedException,
			SQLException {
		Query pstmt = null;
		StringBuilder query = new StringBuilder(500);
		query.append("update vouchermis set ");
		if (serviceId != null)
			query.append("voucherheaderid=?,");
		if (glCodeId != null)
			query.append("divisionId=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		try {
			int i = 1;
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query.toString());
			if (serviceId != null)
				pstmt.setString(i++, serviceId);
			if (glCodeId != null)
				pstmt.setString(i++, glCodeId);
			pstmt.setString(i++, id);

			pstmt.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exp in update: " + e.getMessage());
			throw taskExc;
		} 
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getGlCodeId() {
		return glCodeId;
	}

	public void setGlCodeId(String glCodeId) {
		this.glCodeId = glCodeId;
	}
}
