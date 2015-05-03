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
 * @author pushpendra.singh 
 */

package com.exilant.eGov.src.domain;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
@Transactional(readOnly=true)
public class ChartOfAccountDetail {
	private String id = null;
	private String glCodeId = null;
	private String detailTypeId = null;
	private static final Logger LOGGER = Logger
			.getLogger(ChartOfAccountDetail.class);
	private TaskFailedException taskExc;
	private String updateQuery = "UPDATE ChartOfAccountDetail SET";
	private boolean isId = false, isField = false;
	EGovernCommon cm = new EGovernCommon();

	public void setId(String aId) {
		id = aId;
		isId = true;
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}

	public void setGLCodeId(String aGLCodeId) {
		glCodeId = aGLCodeId;
		isField = true;
	}

	public void setDetailTypeId(String aDetailTypeId) {
		detailTypeId = aDetailTypeId;
		isField = true;
	}
	@Transactional
	public void insert() throws SQLException,
			TaskFailedException {
		Query pst = null;
		try {
			setId(String.valueOf(PrimaryKeyGenerator
					.getNextKey("ChartOfAccountDetail")));
			String insertQuery = "INSERT INTO ChartOfAccountDetail (id, glCodeId, detailTypeId)"
					+ "VALUES( ?, ?, ?)";
			pst = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
			pst.setString(1, id);
			pst.setString(2, glCodeId);
			pst.setString(3, detailTypeId);
			if(LOGGER.isDebugEnabled())     LOGGER.debug(insertQuery);
			pst.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exp in insert:" + e.getMessage(),e);
			throw taskExc;
		} 

	}
	@Transactional
	public void update() throws SQLException,
			TaskFailedException {
		if (isId && isField) {
			Query pst = null;
			try {

				updateQuery = "UPDATE ChartOfAccountDetail SET glCodeId=? , detailTypeId=?  WHERE id =?";
				if(LOGGER.isDebugEnabled())     LOGGER.debug(updateQuery);
				pst = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
				pst.setString(1, glCodeId);
				pst.setString(2, detailTypeId);
				pst.setString(3, id);
				pst.executeUpdate();
			} catch (Exception e) {
				LOGGER.error("Exp in update:" + e.getMessage(),e);
				throw taskExc;
			} 

		}
	}
}
