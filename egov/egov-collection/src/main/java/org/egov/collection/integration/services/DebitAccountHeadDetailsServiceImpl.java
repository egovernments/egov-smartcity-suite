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
package org.egov.collection.integration.services;

import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.integration.models.BillAccountDetails.PURPOSE;
import org.egov.commons.CChartOfAccounts;
import org.egov.infstr.services.PersistenceService;

import java.math.BigDecimal;

public class DebitAccountHeadDetailsServiceImpl implements DebitAccountHeadDetailsService {

    protected PersistenceService persistenceService;

    public ReceiptDetail addDebitAccountHeadDetails(final BigDecimal debitAmount, final ReceiptHeader receiptHeader,
            final BigDecimal chequeInstrumenttotal, final BigDecimal otherInstrumenttotal, final String instrumentType) {

        final ReceiptDetail newReceiptDetail = new ReceiptDetail();
        newReceiptDetail.setPurpose(PURPOSE.OTHERS.toString());
        if (chequeInstrumenttotal.toString() != null
                && !chequeInstrumenttotal.toString().trim().equals(CollectionConstants.ZERO_INT)
                && !chequeInstrumenttotal.toString().trim().equals(CollectionConstants.ZERO_DOUBLE)) {

            newReceiptDetail.setAccounthead((CChartOfAccounts) persistenceService.findByNamedQuery(
                    CollectionConstants.QUERY_CHARTOFACCOUNT_BY_INSTRTYPE, CollectionConstants.INSTRUMENTTYPE_CHEQUE));

            newReceiptDetail.setDramount(debitAmount);
            newReceiptDetail.setCramount(BigDecimal.valueOf(0));
            newReceiptDetail.setReceiptHeader(receiptHeader);
            newReceiptDetail.setFunction(receiptHeader.getReceiptDetails().iterator().next().getFunction());
        }

        if (otherInstrumenttotal.toString() != null
                && !otherInstrumenttotal.toString().trim().equals(CollectionConstants.ZERO_INT)
                && !otherInstrumenttotal.toString().trim().equals(CollectionConstants.ZERO_DOUBLE)) {
            if (instrumentType.equals(CollectionConstants.INSTRUMENTTYPE_CASH))
                newReceiptDetail
                .setAccounthead((CChartOfAccounts) persistenceService.findByNamedQuery(
                        CollectionConstants.QUERY_CHARTOFACCOUNT_BY_INSTRTYPE,
                        CollectionConstants.INSTRUMENTTYPE_CASH));
            else if (instrumentType.equals(CollectionConstants.INSTRUMENTTYPE_CARD))
                newReceiptDetail
                .setAccounthead((CChartOfAccounts) persistenceService.findByNamedQuery(
                        CollectionConstants.QUERY_CHARTOFACCOUNT_BY_INSTRTYPE,
                        CollectionConstants.INSTRUMENTTYPE_CARD));
            else if (instrumentType.equals(CollectionConstants.INSTRUMENTTYPE_BANK))
                newReceiptDetail.setAccounthead(receiptHeader.getReceiptInstrument().iterator().next()
                        .getBankAccountId().getChartofaccounts());
            else if (instrumentType.equals(CollectionConstants.INSTRUMENTTYPE_ONLINE))
                newReceiptDetail.setAccounthead((CChartOfAccounts) persistenceService.findByNamedQuery(
                        CollectionConstants.QUERY_CHARTOFACCOUNT_BY_INSTRTYPE_SERVICE,
                        CollectionConstants.INSTRUMENTTYPE_ONLINE, receiptHeader.getOnlinePayment().getService()
                        .getId()));
            newReceiptDetail.setDramount(debitAmount);
            newReceiptDetail.setCramount(BigDecimal.ZERO);
            newReceiptDetail.setReceiptHeader(receiptHeader);
            newReceiptDetail.setFunction(receiptHeader.getReceiptDetails().iterator().next().getFunction());
        }
        return newReceiptDetail;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }
}
