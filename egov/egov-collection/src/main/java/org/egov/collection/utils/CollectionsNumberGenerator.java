/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
package org.egov.collection.utils;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.egov.collection.entity.Challan;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.commons.CFinancialYear;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.utils.DBSequenceGenerator;
import org.egov.infra.persistence.utils.SequenceNumberGenerator;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.utils.DateUtils;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class CollectionsNumberGenerator {
    @Autowired
    private ScriptService scriptService;
    private CollectionsUtil collectionsUtil;

    @Autowired
    private DBSequenceGenerator dbSequenceGenerator;

    @Autowired
    private SequenceNumberGenerator sequenceNumberGenerator;

    /**
     * This method generates the receipt number for the given receipt header
     *
     * @param receiptHeader an instance of <code>ReceiptHeader</code>
     * @return a <code>String</code> representing the receipt number
     */
    public String generateReceiptNumber(final ReceiptHeader receiptHeader) {
        final CFinancialYear financialYear = collectionsUtil.getFinancialYearforDate(new Date());
        final SimpleDateFormat sdf = new SimpleDateFormat("MM");
        final String formattedDate = sdf.format(receiptHeader.getReceiptdate());
        final String strObj = "SQ_RECEIPTHEADER_" + financialYear.getFinYearRange();
        final String result = formattedDate + '/' + financialYear.getFinYearRange() + '/'
                + sequenceNumberGenerator.getNextSequence(strObj);
        return result;
    }

    /**
     * This method generates the challan number for the given receipt header
     *
     * @param challan an instance of <code>Challan</code>
     * @return a <code>String</code> representing the challan number
     */
    public String generateChallanNumber(final Challan challan, final CFinancialYear financialYear) {

        final String APP_NUMBER_SEQ_PREFIX = "SQ_CHALLAN%s";
        final SimpleDateFormat sdf = new SimpleDateFormat("MM");
        final String formattedDate = sdf.format(new Date());

        final String currentYear = DateUtils.currentDateToYearFormat();
        final String sequenceName = String.format(APP_NUMBER_SEQ_PREFIX, currentYear);
        Serializable sequenceNumber;
        try {
            try {
                sequenceNumber = sequenceNumberGenerator.getNextSequence(sequenceName);
            } catch (final SQLGrammarException e) {
                sequenceNumber = dbSequenceGenerator.createAndGetNextSequence(sequenceName);
            }
        } catch (final SQLException e) {
            throw new ApplicationRuntimeException("Error occurred while generating Application Number", e);
        }

        final String result = formattedDate + "/" + financialYear.getFinYearRange() + "/" + sequenceNumber;
        return result;
    }

    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }
}
