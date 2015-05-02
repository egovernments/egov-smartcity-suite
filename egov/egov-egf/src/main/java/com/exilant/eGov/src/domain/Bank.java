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
 * Created on Jul 13, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EgovMasterDataCaching;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author Lakshmi
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class Bank {
	private String id = null;
	private String code = null;
	private String name = null;
	private String narration = null;
	private String isActive = "1";
	private String lastModified = null;
	private String created = null;
	private String modifiedBy = null;
	private String type = null;
	private static final Logger LOGGER = Logger.getLogger(Bank.class);
	private TaskFailedException taskExc;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale
			.getDefault());
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",
			Locale.getDefault());
	private String insertQuery;
	private boolean isId = false, isField = false;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public SimpleDateFormat getFormatter() {
		return formatter;
	}

	public void setFormatter(SimpleDateFormat formatter) {
		this.formatter = formatter;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String insert(Connection con) throws TaskFailedException,
			SQLException {
		setId(String.valueOf(PrimaryKeyGenerator.getNextKey("Bank")));

		EGovernCommon common = new EGovernCommon();
		try {
			PreparedStatement pstmt = null;

			created = common.getCurrentDate();
			created = formatter.format(sdf.parse(created));
			lastModified = created;
			EgovMasterDataCaching.getInstance().removeFromCache("egf-bank");

			setCreated(created);
			setLastModified(lastModified);
			setCode(id);
			narration=common.formatString(narration);
			insertQuery = "insert into Bank (id,code,Name,Narration,isActive,LastModified,Created,ModifiedBy,Type)"
					+ "values(?,?,?,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(insertQuery);
			pstmt.setString(1, removeSingleQuotes(id));
			pstmt.setString(2, removeSingleQuotes(code));
			pstmt.setString(3, removeSingleQuotes(name));
			pstmt.setString(4, removeSingleQuotes(narration));
			pstmt.setString(5, removeSingleQuotes(isActive));
			pstmt.setString(6, removeSingleQuotes(lastModified));
			pstmt.setString(7, removeSingleQuotes(created));
			pstmt.setString(8, removeSingleQuotes(modifiedBy));
			pstmt.setString(9, removeSingleQuotes(type));
			if(LOGGER.isDebugEnabled())     LOGGER.debug("B4 insertion: " + insertQuery);
			pstmt.executeUpdate();
			pstmt.close();
			if(LOGGER.isDebugEnabled())     LOGGER.debug(insertQuery);
		} catch (Exception e) {
			LOGGER.error("Exp in insert" + e.getMessage(), e);
			throw taskExc;
		} 
		return id;
	}

	private String removeSingleQuotes(String obj) {
		if (obj != null) {
			obj = obj.replaceAll("'", "");
		}
		return obj;

	}

	public void update(Connection con) throws TaskFailedException, SQLException {
		newUpdate(con);
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate();
		PreparedStatement pstmt = null;
		try {
			created = formatter.format(sdf.parse(created));
		} catch (ParseException parseExp) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug(parseExp.getMessage(), parseExp);
		}
		setCreated(created);
		setLastModified(created);
		StringBuilder query = new StringBuilder(500);
		query.append("update Bank set ");
		if (code != null)
			query.append("code=?,");
		if (name != null)
			query.append("name=?,");
		if (narration != null)
			query.append("narration=?,");
		if (isActive != null)
			query.append("isActive=?,");
		if (lastModified != null)
			query.append("lastModified=?,");
		if (created != null)
			query.append("created=?,");
		if (modifiedBy != null)
			query.append("modifiedBy=?,");
		if (type != null)
			query.append("type=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		try {
			int i = 1;
			pstmt = con.prepareStatement(query.toString());
			if (code != null)
				pstmt.setString(i++, code);
			if (name != null)
				pstmt.setString(i++, name);
			if (narration != null)
				pstmt.setString(i++, narration);
			if (isActive != null)
				pstmt.setString(i++, isActive);
			if (lastModified != null)
				pstmt.setString(i++, lastModified);
			if (created != null)
				pstmt.setString(i++, created);
			if (modifiedBy != null)
				pstmt.setString(i++, modifiedBy);
			if (type != null)
				pstmt.setString(i++, type);
			pstmt.setString(i++, id);

			pstmt.executeQuery();
		} catch (Exception e) {
			LOGGER.error("Exp in update: " + e.getMessage());
			throw taskExc;
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				LOGGER.error("Inside finally block of update");
			}
		}
	}

}
