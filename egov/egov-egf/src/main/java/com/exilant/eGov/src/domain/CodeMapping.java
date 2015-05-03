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
 * Created on Mar 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.exilant.eGov.src.domain;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author Rashmi MN
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly=true)
public class CodeMapping {

	private String eg_BoundaryID = null;
	private String cashInHand = null;
	private String chequeInHand = null;

	private String ID = "0";

	private String insertQuery;
	private boolean isId = false, isField = false;
	private static final Logger LOGGER = Logger.getLogger(CodeMapping.class);
	private TaskFailedException taskExc;

	public void setID(String aID) {
		ID = aID;
		isId = true;
	}

	public void setegBoundaryID(String xegBoundaryID) {
		eg_BoundaryID = xegBoundaryID;
		insertQuery = insertQuery + " egBoundaryID=" + xegBoundaryID + ",";
		isField = true;
	}

	public void setcashInHand(String xcashInHand) {
		cashInHand = xcashInHand;
	}

	public void setchequeInHand(String xchequeInHand) {
		chequeInHand = xchequeInHand;
	}
	@Transactional
	public void insert() throws Exception {
		setID(String.valueOf(PrimaryKeyGenerator.getNextKey("CodeMapping")));
		insertQuery = "insert into CodeMapping (id,eg_BoundaryID,cashInHand,chequeInHand) values(?,?,?,?)";
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		// Statement statement = connection.createStatement();
		Query pstmt = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
		pstmt.setString(1, ID);
		pstmt.setString(2, eg_BoundaryID);
		pstmt.setString(3, cashInHand);
		pstmt.setString(4, chequeInHand);
		pstmt.executeUpdate();

	}
	@Transactional
	public void update() throws SQLException {
		try {
			newUpdate();
		} catch (TaskFailedException e) {
			LOGGER.error("error inside update" + e.getMessage(),e);
		}
	}
	@Transactional
	public void newUpdate() throws TaskFailedException,
			SQLException {
		Query pstmt = null;
		StringBuilder query = new StringBuilder(500);
		query.append("update billcollector set ");
		if (cashInHand != null)
			query.append("CASHINHAND=?,");
		if (chequeInHand != null)
			query.append("CHEQUEINHAND=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where EG_BOUNDARYID=?");
		try {
			int i = 1;
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query.toString());
			if (cashInHand != null)
				pstmt.setString(i++, cashInHand);
			if (chequeInHand != null)
				pstmt.setString(i++, chequeInHand);
			pstmt.setString(i++, eg_BoundaryID);

			pstmt.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exp in update: " + e.getMessage(),e);
			throw taskExc;
		} 
	}

}
