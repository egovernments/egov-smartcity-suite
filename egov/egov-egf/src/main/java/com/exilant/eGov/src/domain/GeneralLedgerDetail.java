/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
/*
 * Created on Feb 25, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.domain;

import java.math.BigDecimal;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.exility.updateservice.PrimaryKeyGenerator;

@Transactional(readOnly = true)
public class GeneralLedgerDetail {
    private static final Logger LOGGER = Logger.getLogger(GeneralLedgerDetail.class);
    private String id = null;
    private String glId = null;
    private String detailKeyId = null;
    private String detailTypeId = null;
    private String detailAmt = "0";

    @PersistenceContext
    private EntityManager entityManager;

    public int getId() {
        return Integer.valueOf(id).intValue();
    }

    public void setId(final String aId) {
        id = aId;
    }

    public String getGLId() {
        return glId;
    }

    public void setGLId(final String aGLId) {
        glId = aGLId;
    }

    public String getDetailKeyId() {
        return detailKeyId;
    }

    public void setDetailKeyId(final String aDetailKeyId) {
        detailKeyId = aDetailKeyId;
    }

    public String getDetailTypeId() {
        return detailTypeId;
    }

    public void setDetailTypeId(final String aDetailTypeId) {
        detailTypeId = aDetailTypeId;
    }

    public String getDetailAmt() {
        return detailAmt;
    }

    public void setDetailAmt(final String aDetailAmt) {
        detailAmt = aDetailAmt;
    }

    @Transactional
    public void insert() throws SQLException {
        setId(String.valueOf(PrimaryKeyGenerator.getNextKey("GeneralLedgerDetail")));

        final StringBuilder insertQuery = new StringBuilder(
                "INSERT INTO GeneralLedgerDetail(id, generalLedgerId, detailKeyId, detailTypeId, amount)")
                        .append(" VALUES ( :id, :generalLedgerId, :detailKeyId, :detailTypeId, :amount)");

        entityManager.createNativeQuery(insertQuery.toString())
                .setParameter("id", Long.valueOf(id))
                .setParameter("generalLedgerId", Long.valueOf(glId))
                .setParameter("detailKeyId", Long.valueOf(detailKeyId))
                .setParameter("detailTypeId", Long.valueOf(detailTypeId))
                .setParameter("amount", new BigDecimal(detailAmt))
                .executeUpdate();
        if (LOGGER.isInfoEnabled())
            LOGGER.info(insertQuery);
    }
}