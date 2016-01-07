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
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly = true)
public class AccountDtlKey {
    private final static Logger LOGGER = Logger.getLogger(AccountDtlKey.class);
    private String id = "";
    private String groupID = "1";
    private String glCodeID = null;
    private String detailTypeId = null;
    private String detailName = "";
    private String detailKey = null;
    private TaskFailedException taskExc;

    // private String insertQuery;
    private boolean isId = false, isField = false;

    public void setID(final String aID) {
        id = aID;
        isId = true;
    }

    public void setgroupID(final String agroupID) {
        groupID = agroupID;
        isField = true;
    }

    public void setglCodeID(final String aglCodeID) {
        glCodeID = aglCodeID;
        isField = true;
    }

    public void setdetailTypeId(final String adetailTypeId) {
        detailTypeId = adetailTypeId;
        isField = true;
    }

    public void setdetailName(final String adetailName) {
        detailName = adetailName;
        isField = true;
    }

    public void setdetailKey(final String adetailKey) {
        detailKey = adetailKey;
        isField = true;
    }

    @Transactional
    public void insert() throws SQLException,
    TaskFailedException {
        Query pst = null;
        try {

            setID(String.valueOf(PrimaryKeyGenerator
                    .getNextKey("AccountDetailKey")));
            final String insertQuery = "insert into AccountDetailKey (id,groupID, glCodeID, detailTypeId, detailName, detailKey)"
                    + " values ( ?, ?, ?, ?, ?, ?)";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(insertQuery);
            pst = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
            pst.setString(1, id);
            pst.setString(2, groupID);
            pst.setString(3, glCodeID);
            pst.setString(4, detailTypeId);
            pst.setString(5, detailName);
            pst.setString(6, detailKey);
            final int ret = pst.executeUpdate();
            // if(LOGGER.isDebugEnabled()) LOGGER.debug(insertQuery);
            if (ret == 1)
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("SUCCESS INSERTING INTO ADK");
        } catch (final Exception e) {
            LOGGER.error("Exception in insert:" + e.getMessage(), e);
            throw taskExc;
        }
    }

    @Transactional
    public void update() throws SQLException,
    TaskFailedException {
        try {

            if (isId && isField)
                newUpdate();
        } catch (final Exception e) {
            LOGGER.error("Exception in update:" + e.getMessage(), e);
            throw taskExc;
        }
    }

    public void newUpdate() throws TaskFailedException,
    SQLException {

        Query pstmt = null;
        final StringBuilder query = new StringBuilder(500);
        query.append("update accountdetailkey set ");
        if (groupID != null)
            query.append("groupID=?,");
        if (glCodeID != null)
            query.append("glCodeID=?,");
        if (detailTypeId != null)
            query.append("detailTypeId=?,");
        if (detailName != null)
            query.append("detailName=?,");
        if (detailKey != null)
            query.append("detailKey=?,");
        final int lastIndexOfComma = query.lastIndexOf(",");
        query.deleteCharAt(lastIndexOfComma);
        query.append(" where id=?");
        try {
            int i = 1;
            pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query.toString());
            if (groupID != null)
                pstmt.setString(i++, groupID);
            if (glCodeID != null)
                pstmt.setString(i++, glCodeID);
            if (detailTypeId != null)
                pstmt.setString(i++, detailTypeId);
            if (detailName != null)
                pstmt.setString(i++, detailName);
            if (detailKey != null)
                pstmt.setString(i++, detailKey);
            pstmt.setString(i++, id);

            pstmt.executeUpdate();
        } catch (final Exception e) {
            LOGGER.error("Exp in update: " + e.getMessage(), e);
            throw taskExc;
        }
    }

}
