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
package org.egov.collection.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.CollectionSummaryHeadWiseReport;
import org.egov.collection.entity.CollectionSummaryHeadWiseReportResult;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.springframework.stereotype.Service;

@Service
public class CollectionReportHeadWiseService {

    @PersistenceContext
    EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public CollectionSummaryHeadWiseReportResult getCollectionSummaryReport(final Date fromDate, final Date toDate,
            final String paymentMode, final String source, final String glCode, final int status, final Integer branchId) {
        final SimpleDateFormat fromDateFormatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        final SimpleDateFormat toDateFormatter = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        final StringBuilder defaultQueryStr = new StringBuilder(500);
        final StringBuilder aggregateQueryStr = new StringBuilder();
        StringBuilder rebateQueryStr = new StringBuilder("");
        StringBuilder revenueHeadQueryStr = new StringBuilder("");

        final StringBuilder selectQueryStr = new StringBuilder(
                "SELECT  (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cash' THEN count(distinct(EGCL_COLLECTIONHEADER.id)) END) AS cashCount,  "
                        +
                        " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cheque' THEN count(distinct(EGCL_COLLECTIONHEADER.id)) WHEN EGF_INSTRUMENTTYPE.TYPE='dd' THEN count(distinct(EGCL_COLLECTIONHEADER.id)) END) AS chequeddCount, "
                        +
                        " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE= 'online' THEN count(distinct(EGCL_COLLECTIONHEADER.id)) END) AS onlineCount, "
                        +
                        " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='card' THEN count(distinct(EGCL_COLLECTIONHEADER.id)) END) AS cardCount, "
                        +
                        " count(*) as totalReceiptCount, " +
                        " EGCL_COLLECTIONHEADER.SOURCE AS source,CAO.NAME || '-' || CAO.GLCODE AS GLCODE,");
        final StringBuilder revSelectQueryStr = new StringBuilder(selectQueryStr).append(
                " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cash' THEN SUM(EGCL_COLLECTIONDETAILS.CRAMOUNT) END) AS cashAmount, " +
                        " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cheque' THEN SUM(EGCL_COLLECTIONDETAILS.CRAMOUNT) WHEN EGF_INSTRUMENTTYPE.TYPE='dd' THEN SUM(EGCL_COLLECTIONDETAILS.CRAMOUNT) END) AS chequeddAmount,"
                        +
                        " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='card' THEN SUM(EGCL_COLLECTIONDETAILS.CRAMOUNT) END) AS cardAmount, "
                        +
                        " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE= 'online' THEN SUM(EGCL_COLLECTIONDETAILS.CRAMOUNT) END) AS onlineAmount");
        final StringBuilder rebateSelectQueryStr = new StringBuilder(selectQueryStr).append(
                " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cash' THEN SUM(EGCL_COLLECTIONDETAILS.DRAMOUNT) END) AS cashAmount, " +
                        " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cheque' THEN SUM(EGCL_COLLECTIONDETAILS.DRAMOUNT) WHEN EGF_INSTRUMENTTYPE.TYPE='dd' THEN SUM(EGCL_COLLECTIONDETAILS.DRAMOUNT) END) AS chequeddAmount,"
                        +
                        " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='card' THEN SUM(EGCL_COLLECTIONDETAILS.DRAMOUNT) END) AS cardAmount, "
                        +
                        " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE= 'online' THEN SUM(EGCL_COLLECTIONDETAILS.DRAMOUNT) END) AS onlineAmount");
        final StringBuilder fromQueryStr = new StringBuilder(" FROM "
                + " EGCL_COLLECTIONHEADER EGCL_COLLECTIONHEADER INNER JOIN EGCL_COLLECTIONINSTRUMENT EGCL_COLLECTIONINSTRUMENT ON EGCL_COLLECTIONHEADER.ID = EGCL_COLLECTIONINSTRUMENT.COLLECTIONHEADER "
                + " INNER JOIN EGF_INSTRUMENTHEADER EGF_INSTRUMENTHEADER ON EGCL_COLLECTIONINSTRUMENT.INSTRUMENTHEADER = EGF_INSTRUMENTHEADER.ID "
                + " INNER JOIN EGW_STATUS EGW_STATUS ON EGCL_COLLECTIONHEADER.STATUS = EGW_STATUS.ID"
                + " INNER JOIN EGF_INSTRUMENTTYPE EGF_INSTRUMENTTYPE ON EGF_INSTRUMENTHEADER.INSTRUMENTTYPE = EGF_INSTRUMENTTYPE.ID"
                + " INNER JOIN EGCL_COLLECTIONMIS EGCL_COLLECTIONMIS ON EGCL_COLLECTIONHEADER.ID = EGCL_COLLECTIONMIS.COLLECTIONHEADER "
                + " INNER JOIN EGCL_COLLECTIONDETAILS EGCL_COLLECTIONDETAILS ON EGCL_COLLECTIONHEADER.ID = EGCL_COLLECTIONDETAILS.COLLECTIONHEADER "
                + " INNER JOIN CHARTOFACCOUNTS CAO ON CAO.ID = EGCL_COLLECTIONDETAILS.CHARTOFACCOUNT ");
        StringBuilder whereQueryStr = new StringBuilder(" WHERE EGW_STATUS.DESCRIPTION != 'Cancelled' ");
        StringBuilder creditWhereQueryStr = new StringBuilder("  AND EGCL_COLLECTIONDETAILS.CRAMOUNT>0 ");
        StringBuilder debitWhereQueryStr = new StringBuilder(
                "  AND EGCL_COLLECTIONDETAILS.DRAMOUNT>0 AND CAO.type ='I' AND (CAO.purposeid is null or CAO.purposeid not in (select id from EGF_ACCOUNTCODE_PURPOSE where name in ('"
                        + CollectionConstants.PURPOSE_NAME_CASH_IN_HAND + "','" + CollectionConstants.PURPOSE_NAME_CHEQUE_IN_HAND
                        + "','"
                        + CollectionConstants.PURPOSE_NAME_CASH_IN_TRANSIT + "','" + CollectionConstants.PURPOSE_NAME_CREDIT_CARD
                        + "','"
                        + CollectionConstants.PURPOSE_NAME_ATM_ACCOUNTCODE + "','"
                        + CollectionConstants.PURPOSE_NAME_INTERUNITACCOUNT + "')))");
        final StringBuilder queryStrGroup = new StringBuilder(" GROUP BY source,CAO.NAME,CAO.GLCODE,EGF_INSTRUMENTTYPE.TYPE ");
        final StringBuilder finalSelectQueryStr = new StringBuilder(
                "SELECT sum(cashCount) AS cashCount,sum(chequeddCount) AS chequeddCount,sum(onlineCount) AS onlineCount,SOURCE,glCode,sum(cashAmount) AS cashAmount, sum(chequeddAmount) AS chequeddAmount,  "
                        + "  sum(cardCount) AS cardCount, sum(cardAmount) AS cardAmount, cast(sum(totalReceiptCount) AS NUMERIC) as totalReceiptCount,sum(onlineAmount) AS onlineAmount  FROM (");
        final StringBuilder finalGroupQuery = new StringBuilder(
                " ) AS RESULT GROUP BY RESULT.SOURCE,RESULT.glCode order by source, glCode");

        if (fromDate != null && toDate != null) {
            whereQueryStr.append(" AND EGCL_COLLECTIONHEADER.RECEIPTDATE between to_timestamp('"
                    + fromDateFormatter.format(fromDate) + "', 'YYYY-MM-DD HH24:MI:SS') and " + " to_timestamp('"
                    + toDateFormatter.format(toDate) + "', 'YYYY-MM-DD HH24:MI:SS') ");
        }
        if (!source.isEmpty() && !source.equals(CollectionConstants.ALL)) {
            whereQueryStr.append(" AND EGCL_COLLECTIONHEADER.SOURCE=:source");
        }
        if (glCode != null) {
            whereQueryStr.append(" AND CAO.GLCODE =:glCode");
        }
        if (branchId != null && branchId != -1) {
            whereQueryStr.append(" AND EGCL_COLLECTIONMIS.DEPOSITEDBRANCH=:branchId");
        }
        if (status != -1) {
            whereQueryStr.append(" AND EGCL_COLLECTIONHEADER.STATUS =:searchStatus");
        }
        if (StringUtils.isNotBlank(paymentMode) && !paymentMode.equals(CollectionConstants.ALL)) {
            whereQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE in (:paymentMode)");
            revenueHeadQueryStr.append(revSelectQueryStr).append(fromQueryStr).append(whereQueryStr)
                    .append(creditWhereQueryStr).append(queryStrGroup);
            rebateQueryStr.append(rebateSelectQueryStr).append(fromQueryStr).append(whereQueryStr)
                    .append(debitWhereQueryStr).append(queryStrGroup);
        } else {
            revenueHeadQueryStr.append(revSelectQueryStr).append(fromQueryStr).append(whereQueryStr)
                    .append(creditWhereQueryStr).append(" AND EGF_INSTRUMENTTYPE.TYPE = 'cash'").append(queryStrGroup);
            revenueHeadQueryStr.append(" union ");
            revenueHeadQueryStr.append(revSelectQueryStr).append(fromQueryStr).append(whereQueryStr)
                    .append(creditWhereQueryStr).append(" AND EGF_INSTRUMENTTYPE.TYPE  in( 'cheque','dd') ")
                    .append(queryStrGroup);
            revenueHeadQueryStr.append(" union ");
            revenueHeadQueryStr.append(revSelectQueryStr).append(fromQueryStr).append(whereQueryStr)
                    .append(creditWhereQueryStr).append(" AND EGF_INSTRUMENTTYPE.TYPE = 'card'").append(queryStrGroup);
            revenueHeadQueryStr.append(" union ");
            revenueHeadQueryStr.append(revSelectQueryStr).append(fromQueryStr).append(whereQueryStr)
                    .append(creditWhereQueryStr).append(" AND EGF_INSTRUMENTTYPE.TYPE = 'online'").append(queryStrGroup);

            rebateQueryStr.append(rebateSelectQueryStr).append(fromQueryStr).append(whereQueryStr)
                    .append(debitWhereQueryStr).append(" AND EGF_INSTRUMENTTYPE.TYPE = 'cash'").append(queryStrGroup);
            rebateQueryStr.append(" union ");
            rebateQueryStr.append(rebateSelectQueryStr).append(fromQueryStr).append(whereQueryStr)
                    .append(debitWhereQueryStr).append(" AND EGF_INSTRUMENTTYPE.TYPE  in( 'cheque','dd') ").append(queryStrGroup);
            rebateQueryStr.append(" union ");
            rebateQueryStr.append(rebateSelectQueryStr).append(fromQueryStr).append(whereQueryStr)
                    .append(debitWhereQueryStr).append(" AND EGF_INSTRUMENTTYPE.TYPE = 'card'").append(queryStrGroup);
            rebateQueryStr.append(" union ");
            rebateQueryStr.append(rebateSelectQueryStr).append(fromQueryStr).append(whereQueryStr)
                    .append(debitWhereQueryStr).append(" AND EGF_INSTRUMENTTYPE.TYPE = 'online'").append(queryStrGroup);
        }

        final StringBuilder finalRevQueryStr = new StringBuilder(finalSelectQueryStr).append(revenueHeadQueryStr)
                .append(finalGroupQuery);
        final StringBuilder finalRebateQueryStr = new StringBuilder(finalSelectQueryStr).append(rebateQueryStr)
                .append(finalGroupQuery);

        final SQLQuery aggrQuery = (SQLQuery) getCurrentSession().createSQLQuery(finalRevQueryStr.toString())
                .addScalar("cashCount", org.hibernate.type.StringType.INSTANCE).addScalar("cashAmount", DoubleType.INSTANCE)
                .addScalar("chequeddCount", org.hibernate.type.StringType.INSTANCE)
                .addScalar("chequeddAmount", DoubleType.INSTANCE)
                .addScalar("onlineCount", org.hibernate.type.StringType.INSTANCE).addScalar("onlineAmount", DoubleType.INSTANCE)
                .addScalar("source", org.hibernate.type.StringType.INSTANCE)
                .addScalar("glCode", org.hibernate.type.StringType.INSTANCE)
                .addScalar("cardAmount", DoubleType.INSTANCE).addScalar("cardCount", org.hibernate.type.StringType.INSTANCE)
                .addScalar("totalReceiptCount", org.hibernate.type.StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(CollectionSummaryHeadWiseReport.class));

        final SQLQuery rebateQuery = (SQLQuery) getCurrentSession().createSQLQuery(finalRebateQueryStr.toString())
                .addScalar("cashCount", org.hibernate.type.StringType.INSTANCE).addScalar("cashAmount", DoubleType.INSTANCE)
                .addScalar("chequeddCount", org.hibernate.type.StringType.INSTANCE)
                .addScalar("chequeddAmount", DoubleType.INSTANCE)
                .addScalar("onlineCount", org.hibernate.type.StringType.INSTANCE).addScalar("onlineAmount", DoubleType.INSTANCE)
                .addScalar("source", org.hibernate.type.StringType.INSTANCE)
                .addScalar("glCode", org.hibernate.type.StringType.INSTANCE)
                .addScalar("cardAmount", DoubleType.INSTANCE).addScalar("cardCount", org.hibernate.type.StringType.INSTANCE)
                .addScalar("totalReceiptCount", org.hibernate.type.StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(CollectionSummaryHeadWiseReport.class));
        if (!source.isEmpty() && !source.equals(CollectionConstants.ALL)) {
            aggrQuery.setString("source", source);
            rebateQuery.setString("source", source);
        }
        if (glCode != null) {
            aggrQuery.setString("glCode", glCode);
            rebateQuery.setString("glCode", glCode);
        }
        if (status != -1) {
            aggrQuery.setLong("searchStatus", status);
            rebateQuery.setLong("searchStatus", status);
        }

        if (StringUtils.isNotBlank(paymentMode) && !paymentMode.equals(CollectionConstants.ALL))
            if (paymentMode.equals(CollectionConstants.INSTRUMENTTYPE_CHEQUEORDD)) {
                aggrQuery.setParameterList("paymentMode", new ArrayList<>(Arrays.asList("cheque", "dd")));
                rebateQuery.setParameterList("paymentMode", new ArrayList<>(Arrays.asList("cheque", "dd")));
            } else {
                aggrQuery.setString("paymentMode", paymentMode);
                rebateQuery.setString("paymentMode", paymentMode);
            }
        if (branchId != null && branchId != -1) {
            aggrQuery.setInteger("branchId", branchId);
            rebateQuery.setInteger("branchId", branchId);
        }
        final List<CollectionSummaryHeadWiseReport> rebateReportResultList = populateQueryResults(rebateQuery.list());
        final List<CollectionSummaryHeadWiseReport> aggrReportResults = populateQueryResults(aggrQuery.list());
        final CollectionSummaryHeadWiseReportResult collResult = new CollectionSummaryHeadWiseReportResult();
        if (!aggrReportResults.isEmpty()) {
            rebateTotal(aggrReportResults.get(0), rebateReportResultList);
        }
        collResult.setAggrCollectionSummaryReportList(aggrReportResults);
        collResult.setRebateCollectionSummaryReportList(rebateReportResultList);
        return collResult;
    }

    public void rebateTotal(CollectionSummaryHeadWiseReport collectionSummaryHeadWiseReport,
            List<CollectionSummaryHeadWiseReport> rebateResultList) {
        for (final CollectionSummaryHeadWiseReport rebate : rebateResultList) {
            if (!rebate.getCashAmount().equals(new Double(0.0)))
                collectionSummaryHeadWiseReport.setTotalCashRebateAmount(
                        Double.sum(collectionSummaryHeadWiseReport.getTotalCashRebateAmount(), rebate.getCashAmount()));
            if (!rebate.getChequeddAmount().equals(new Double(0.0)))
                collectionSummaryHeadWiseReport.setTotalChequeddRebateAmount(
                        Double.sum(collectionSummaryHeadWiseReport.getTotalChequeddRebateAmount(), rebate.getChequeddAmount()));
            if (!rebate.getCardAmount().equals(new Double(0.0)))
                collectionSummaryHeadWiseReport.setTotalCardRebateAmount(
                        Double.sum(collectionSummaryHeadWiseReport.getTotalCardRebateAmount(), rebate.getCardAmount()));
            if (!rebate.getOnlineAmount().equals(new Double(0.0)))
                collectionSummaryHeadWiseReport.setTotalOnlineRebateAmount(
                        Double.sum(collectionSummaryHeadWiseReport.getTotalOnlineRebateAmount(), rebate.getOnlineAmount()));
            if (!rebate.getTotalAmount().equals(new Double(0.0)))
                collectionSummaryHeadWiseReport.setTotalRebateAmount(
                        Double.sum(collectionSummaryHeadWiseReport.getTotalRebateAmount(), rebate.getTotalAmount()));
        }
    }

    public List<CollectionSummaryHeadWiseReport> populateQueryResults(final List<CollectionSummaryHeadWiseReport> queryResults) {
        for (final CollectionSummaryHeadWiseReport collectionSummaryHeadWiseReport : queryResults) {
            if (collectionSummaryHeadWiseReport.getCashCount() == null)
                collectionSummaryHeadWiseReport.setCashCount("");
            if (collectionSummaryHeadWiseReport.getChequeddCount() == null)
                collectionSummaryHeadWiseReport.setChequeddCount("");
            if (collectionSummaryHeadWiseReport.getOnlineCount() == null)
                collectionSummaryHeadWiseReport.setOnlineCount("");
            if (collectionSummaryHeadWiseReport.getCardCount() == null)
                collectionSummaryHeadWiseReport.setCardCount("");
            if (collectionSummaryHeadWiseReport.getTotalReceiptCount() == null)
                collectionSummaryHeadWiseReport.setTotalReceiptCount("");
            if (collectionSummaryHeadWiseReport.getCashAmount() == null)
                collectionSummaryHeadWiseReport.setCashAmount(0.0);
            if (collectionSummaryHeadWiseReport.getChequeddAmount() == null)
                collectionSummaryHeadWiseReport.setChequeddAmount(0.0);
            if (collectionSummaryHeadWiseReport.getOnlineAmount() == null)
                collectionSummaryHeadWiseReport.setOnlineAmount(0.0);
            if (collectionSummaryHeadWiseReport.getCardAmount() == null)
                collectionSummaryHeadWiseReport.setCardAmount(0.0);
            collectionSummaryHeadWiseReport.getOnlineAmount();
            collectionSummaryHeadWiseReport.setTotalAmount(Double.sum(collectionSummaryHeadWiseReport.getCardAmount(),
                    Double.sum(collectionSummaryHeadWiseReport.getChequeddAmount(),
                            collectionSummaryHeadWiseReport.getCashAmount())));
        }
        return queryResults;
    }
}