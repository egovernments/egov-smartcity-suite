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

package com.exilant.eGov.src.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class BankReconciliationSummary {

    private static final Logger LOGGER = Logger.getLogger(BankReconciliationSummary.class);

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public String getUnReconciledDrCr(Integer bankAccId, Date fromDate, Date toDate) {

        StringBuilder totalQuery = new StringBuilder(
                "SELECT (sum(case when ih.ispaycheque = '1' then ih.instrumentAmount else 0 end)) AS \"brs_creditTotal\",")
                        .append(" (sum( case when ih.ispaycheque = '0' then ih.instrumentAmount else 0 end)) AS \"brs_debitTotal\"")
                        .append(" FROM egf_instrumentheader ih WHERE ih.bankAccountId = :bankAccountId AND IH.INSTRUMENTDATE >= :fromDate")
                        .append(" AND IH.INSTRUMENTDATE <= :toDate AND ((ih.ispaycheque = '0' and ih.id_status =")
                        .append(" (select id from egw_status where moduletype = 'Instrument' and description = 'Deposited'))")
                        .append(" or (ih.ispaycheque = '1' and ih.id_status = (select id from egw_status")
                        .append(" where moduletype = 'Instrument' and description = 'New')))")
                        .append(" and ih.instrumentnumber is not null");
        // see u might need to exclude brs entries here

        StringBuilder otherTotalQuery = new StringBuilder(
                " SELECT (sum(case when ih.ispaycheque = '1' then ih.instrumentAmount else 0 end)) AS \"brs_creditTotalOthers\",")
                        .append(" (sum(case when ih.ispaycheque = '0' then ih.instrumentAmount else 0 end )) AS \"brs_debitTotalOthers\"")
                        .append(" FROM egf_instrumentheader ih WHERE ih.bankAccountId = :bankAccountId AND IH.transactiondate >= :fromDate")
                        .append(" AND IH.transactiondate <= :toDate AND ((ih.ispaycheque = '0' and ih.id_status =")
                        .append(" (select id from egw_status where moduletype = 'Instrument' and description = 'Deposited'))")
                        .append(" or (ih.ispaycheque = '1' and ih.id_status = (select id from egw_status where moduletype = 'Instrument'")
                        .append(" and description = 'New'))) AND ih.transactionnumber is not null");

        StringBuilder brsEntryQuery = new StringBuilder(
                " select (sum(case when be.type = 'Receipt' then (case when be.voucherheaderid is null then be.txnamount else 0 end)")
                        .append(" else 0 end)) AS \"brs_creditTotalBrsEntry\", (sum(case when be.type = 'Payment'")
                        .append(" then (case when be.voucherheaderid is null then be.txnamount else 0 end) else 0 end))")
                        .append(" AS \"brs_debitTotalBrsEntry\"")
                        .append(" FROM bankentries be")
                        .append(" WHERE be.bankAccountId = :bankAccountId and be.voucherheaderid is null AND be.txndate >= :fromDate")
                        .append(" AND be.txndate <= :toDate");

        if (LOGGER.isInfoEnabled())
            LOGGER.info("  query  for  total : " + totalQuery);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("  query  for other than cheque/DD: " + otherTotalQuery);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("  query  for bankEntries: " + brsEntryQuery);

        String unReconciledDrCr = "";
        String creditTotal = null;
        String creditOthertotal = null;
        String debitTotal = null;
        String debitOtherTotal = null;
        String creditTotalBrsEntry = null;
        String debitTotalBrsEntry = null;

        try {
            List<Object[]> list = entityManager.createNativeQuery(totalQuery.toString())
                    .setParameter("bankAccountId", bankAccId)
                    .setParameter("fromDate", fromDate, TemporalType.DATE)
                    .setParameter("toDate", toDate, TemporalType.DATE)
                    .getResultList();

            if (!list.isEmpty()) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(list.get(0));
                Object[] my = list.get(0);
                creditTotal = my[0] != null ? my[0].toString() : null;
                debitTotal = my[1] != null ? my[1].toString() : null;
            }

            list = entityManager.createNativeQuery(otherTotalQuery.toString())
                    .setParameter("bankAccountId", bankAccId)
                    .setParameter("fromDate", fromDate, TemporalType.DATE)
                    .setParameter("toDate", toDate, TemporalType.DATE)
                    .getResultList();
            if (!list.isEmpty()) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(list.get(0));
                Object[] my = list.get(0);
                creditOthertotal = my[0] != null ? my[0].toString() : null;
                debitOtherTotal = my[1] != null ? my[1].toString() : null;
            }

            list = entityManager.createNativeQuery(brsEntryQuery.toString())
                    .setParameter("bankAccountId", bankAccId)
                    .setParameter("fromDate", fromDate, TemporalType.DATE)
                    .setParameter("toDate", toDate, TemporalType.DATE)
                    .getResultList();
            if (list.size() > 0) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(list.get(0));
                Object[] my = list.get(0);
                creditTotalBrsEntry = my[0] != null ? my[0].toString() : null;
                debitTotalBrsEntry = my[1] != null ? my[1].toString() : null;
            }

            unReconciledDrCr = new StringBuilder(creditTotal != null ? creditTotal : "0").append("/")
                    .append(creditOthertotal != null ? creditOthertotal : "0")
                    .append("/").append(debitTotal != null ? debitTotal : "0").append("/")
                    .append(debitOtherTotal != null ? debitOtherTotal : "0").append("")
                    .append("/").append(creditTotalBrsEntry != null ? creditTotalBrsEntry : "0").append("/")
                    .append(debitTotalBrsEntry != null ? debitTotalBrsEntry : "0")
                    .append("").toString();
        } catch (Exception e) {
            LOGGER.error("Exp in getUnReconciledDrCr", e);
            throw e;
        }
        return unReconciledDrCr;
    }

}
