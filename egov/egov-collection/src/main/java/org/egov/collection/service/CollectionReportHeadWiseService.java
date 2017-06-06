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
            final String paymentMode, final String source, final String glCode, final int status,final Integer branchId) {
        final SimpleDateFormat fromDateFormatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        final SimpleDateFormat toDateFormatter = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        final StringBuilder defaultQueryStr = new StringBuilder(500);
        final StringBuilder queryStr = new StringBuilder(500);
        queryStr.append("SELECT  (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cash' THEN count(*) END) AS cashCount,  ")
                .append("(CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cheque' THEN count(*) WHEN EGF_INSTRUMENTTYPE.TYPE='dd' THEN count(*) END) AS chequeddCount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE= 'online' THEN count(*) END) AS onlineCount, ")
                .append(" EGCL_COLLECTIONHEADER.SOURCE AS source, EG_LOCATION.NAME AS counterName, EG_USER.NAME AS employeeName,GLCODE,")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cash' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS cashAmount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cheque' THEN SUM(EGCL_COLLECTIONDETAILS.CRAMOUNT) WHEN EGF_INSTRUMENTTYPE.TYPE='dd' THEN SUM(EGCL_COLLECTIONDETAILS.CRAMOUNT) END) AS chequeddAmount,")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='card' THEN count(*) END) AS cardCount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='card' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS cardAmount, ")
                .append(" count(*) as totalReceiptCount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE= 'online' THEN SUM(EGCL_COLLECTIONDETAILS.CRAMOUNT) END) AS onlineAmount, EG_USER.ID AS USERID FROM")
                .append(" EGCL_COLLECTIONHEADER EGCL_COLLECTIONHEADER INNER JOIN EGCL_COLLECTIONINSTRUMENT EGCL_COLLECTIONINSTRUMENT ON EGCL_COLLECTIONHEADER.ID = EGCL_COLLECTIONINSTRUMENT.COLLECTIONHEADER ")
                .append(" INNER JOIN EGF_INSTRUMENTHEADER EGF_INSTRUMENTHEADER ON EGCL_COLLECTIONINSTRUMENT.INSTRUMENTHEADER = EGF_INSTRUMENTHEADER.ID ")
                .append(" INNER JOIN EGW_STATUS EGW_STATUS ON EGCL_COLLECTIONHEADER.STATUS = EGW_STATUS.ID ")
                .append(" INNER JOIN EG_LOCATION EG_LOCATION ON EGCL_COLLECTIONHEADER.LOCATION = EG_LOCATION.ID ")
                .append(" INNER JOIN EGF_INSTRUMENTTYPE EGF_INSTRUMENTTYPE ON EGF_INSTRUMENTHEADER.INSTRUMENTTYPE = EGF_INSTRUMENTTYPE.ID ")
                .append(" INNER JOIN EGCL_COLLECTIONMIS EGCL_COLLECTIONMIS ON EGCL_COLLECTIONHEADER.ID = EGCL_COLLECTIONMIS.COLLECTIONHEADER ")
                .append(" INNER JOIN EG_USER EG_USER ON EGCL_COLLECTIONHEADER.CREATEDBY = EG_USER.ID ")
                .append(" INNER JOIN EGEIS_EMPLOYEE EG_EMPLOYEE ON EG_USER.ID = EG_EMPLOYEE.ID ")
                .append(" INNER JOIN EGEIS_ASSIGNMENT EGEIS_ASSIGNMENT ON EGEIS_ASSIGNMENT.EMPLOYEE = EG_EMPLOYEE.ID ")
                .append("INNER JOIN EGCL_COLLECTIONDETAILS EGCL_COLLECTIONDETAILS ON EGCL_COLLECTIONHEADER.ID = EGCL_COLLECTIONDETAILS.COLLECTIONHEADER ")
                .append(" INNER JOIN CHARTOFACCOUNTS CAO ON CAO.ID = EGCL_COLLECTIONDETAILS.CHARTOFACCOUNT WHERE")
                .append(" EGW_STATUS.DESCRIPTION != 'Cancelled' AND EGCL_COLLECTIONDETAILS.CRAMOUNT>0");

        final StringBuilder onlineQueryStr = new StringBuilder();
        onlineQueryStr
                .append("SELECT  (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cash' THEN count(*) END) AS cashCount,  ")
                .append("(CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cheque' THEN count(*) WHEN EGF_INSTRUMENTTYPE.TYPE='dd' THEN count(*) END) AS chequeddCount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE= 'online' THEN count(*) END) AS onlineCount, ")
                .append(" EGCL_COLLECTIONHEADER.SOURCE AS source, '' AS counterName, '' AS employeeName,CAO.NAME || '-' || CAO.GLCODE AS GLCODE,")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cash' THEN SUM(EGCL_COLLECTIONDETAILS.CRAMOUNT) END) AS cashAmount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cheque' THEN SUM(EGCL_COLLECTIONDETAILS.CRAMOUNT) WHEN EGF_INSTRUMENTTYPE.TYPE='dd' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS chequeddAmount,")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='card' THEN count(*) END) AS cardCount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='card' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS cardAmount, ")
                .append(" count(*) as totalReceiptCount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE= 'online' THEN SUM(EGCL_COLLECTIONDETAILS.CRAMOUNT) END) AS onlineAmount, 0 AS USERID FROM ")
                .append(" EGCL_COLLECTIONHEADER EGCL_COLLECTIONHEADER INNER JOIN EGCL_COLLECTIONINSTRUMENT EGCL_COLLECTIONINSTRUMENT ON EGCL_COLLECTIONHEADER.ID = EGCL_COLLECTIONINSTRUMENT.COLLECTIONHEADER ")
                .append(" INNER JOIN EGF_INSTRUMENTHEADER EGF_INSTRUMENTHEADER ON EGCL_COLLECTIONINSTRUMENT.INSTRUMENTHEADER = EGF_INSTRUMENTHEADER.ID ")
                .append(" INNER JOIN EGW_STATUS EGW_STATUS ON EGCL_COLLECTIONHEADER.STATUS = EGW_STATUS.ID")
                .append(" INNER JOIN EGF_INSTRUMENTTYPE EGF_INSTRUMENTTYPE ON EGF_INSTRUMENTHEADER.INSTRUMENTTYPE = EGF_INSTRUMENTTYPE.ID")
                .append(" INNER JOIN EGCL_COLLECTIONMIS EGCL_COLLECTIONMIS ON EGCL_COLLECTIONHEADER.ID = EGCL_COLLECTIONMIS.COLLECTIONHEADER ")
                .append("INNER JOIN EGCL_COLLECTIONDETAILS EGCL_COLLECTIONDETAILS ON EGCL_COLLECTIONHEADER.ID = EGCL_COLLECTIONDETAILS.COLLECTIONHEADER ")
                .append(" INNER JOIN CHARTOFACCOUNTS CAO ON CAO.ID = EGCL_COLLECTIONDETAILS.CHARTOFACCOUNT WHERE")
                .append(" EGW_STATUS.DESCRIPTION != 'Cancelled' AND EGCL_COLLECTIONDETAILS.CRAMOUNT>0");
        final StringBuilder queryStrGroup = new StringBuilder();
        queryStrGroup
                .append(" GROUP BY  source, counterName, employeeName, USERID,CAO.NAME,CAO.GLCODE,EGF_INSTRUMENTTYPE.TYPE");

        if (fromDate != null && toDate != null) {
            queryStr.append(" AND EGCL_COLLECTIONHEADER.RECEIPTDATE between to_timestamp('"
                    + fromDateFormatter.format(fromDate) + "', 'YYYY-MM-DD HH24:MI:SS') and " + " to_timestamp('"
                    + toDateFormatter.format(toDate) + "', 'YYYY-MM-DD HH24:MI:SS') ");
            onlineQueryStr.append(" AND EGCL_COLLECTIONHEADER.RECEIPTDATE between to_timestamp('"
                    + fromDateFormatter.format(fromDate) + "', 'YYYY-MM-DD HH24:MI:SS') and " + " to_timestamp('"
                    + toDateFormatter.format(toDate) + "', 'YYYY-MM-DD HH24:MI:SS') ");
        }

        if (!source.isEmpty() && !source.equals(CollectionConstants.ALL)) {
            queryStr.append(" AND EGCL_COLLECTIONHEADER.SOURCE=:source");
            onlineQueryStr.append(" AND EGCL_COLLECTIONHEADER.SOURCE=:source");
        } else {
            queryStr.setLength(0);
            queryStr.append(onlineQueryStr);
        }

        if (glCode != null) {
            queryStr.append(" AND CAO.GLCODE =:glCode");
            onlineQueryStr.append(" AND CAO.GLCODE =:glCode");
        } else {
            queryStr.setLength(0);
            queryStr.append(onlineQueryStr);
        }

        if(branchId != null && branchId != -1)
        {
            queryStr.append(" AND EGCL_COLLECTIONMIS.DEPOSITEDBRANCH=:branchId");
            onlineQueryStr.append(" AND EGCL_COLLECTIONMIS.DEPOSITEDBRANCH=:branchId");
        }
        if (status != -1) {
            queryStr.append(" AND EGCL_COLLECTIONHEADER.STATUS =:searchStatus");
            onlineQueryStr.append(" AND EGCL_COLLECTIONHEADER.STATUS =:searchStatus");
        }

        if (StringUtils.isNotBlank(paymentMode) && !paymentMode.equals(CollectionConstants.ALL)) {
            if (paymentMode.equals(CollectionConstants.INSTRUMENTTYPE_ONLINE)) {
                queryStr.setLength(0);
                onlineQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE in (:paymentMode)");
                queryStr.append(onlineQueryStr);
                queryStr.append(queryStrGroup);
            } else {
                queryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE in (:paymentMode)");
                queryStr.append(queryStrGroup);
                onlineQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE in (:paymentMode)");
            }
        } else {
            defaultQueryStr.append(queryStr);
            defaultQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE = 'cash'");
            defaultQueryStr.append(queryStrGroup);
            defaultQueryStr.append(" union ");
            defaultQueryStr.append(queryStr);
            defaultQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE in ('cheque', 'dd')");
            defaultQueryStr.append(queryStrGroup);
            defaultQueryStr.append(" union ");
            defaultQueryStr.append(queryStr);
            defaultQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE = 'card'");
            defaultQueryStr.append(queryStrGroup);
            defaultQueryStr.append(" union ");
            defaultQueryStr.append(onlineQueryStr);
            defaultQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE = 'online'");
            defaultQueryStr.append(queryStrGroup);
            queryStr.setLength(0);
            queryStr.append(defaultQueryStr);
        }
        final StringBuilder aggregateQueryStr = new StringBuilder();
        aggregateQueryStr.append(onlineQueryStr);
        aggregateQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE = 'cash'");
        aggregateQueryStr.append(queryStrGroup);
        aggregateQueryStr.append(" union ");
        aggregateQueryStr.append(onlineQueryStr);
        aggregateQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE in( 'cheque','dd')");
        aggregateQueryStr.append(queryStrGroup);
        aggregateQueryStr.append(" union ");
        aggregateQueryStr.append(onlineQueryStr);
        aggregateQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE = 'card'");
        aggregateQueryStr.append(queryStrGroup);
        aggregateQueryStr.append(" union ");
        aggregateQueryStr.append(onlineQueryStr);
        aggregateQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE = 'online'");
        aggregateQueryStr.append(queryStrGroup);

        final StringBuilder finalQueryStr = new StringBuilder();
        finalQueryStr
                .append("SELECT cast(sum(cashCount) AS NUMERIC) AS cashCount,cast(sum(chequeddCount) AS NUMERIC) AS chequeddCount,cast(sum(onlineCount) AS NUMERIC) AS onlineCount,SOURCE,counterName,employeeName,glCode,cast(sum(cashAmount) AS DOUBLE PRECISION) AS cashAmount, cast(sum(chequeddAmount) AS DOUBLE PRECISION) AS chequeddAmount,  "
                        + " cast(sum(cardCount) AS NUMERIC) AS cardCount,cast(sum(cardAmount) AS DOUBLE PRECISION) AS cardAmount ,  "
                        + " cast(sum(totalReceiptCount) AS NUMERIC) as totalReceiptCount, cast(sum(onlineAmount) AS DOUBLE PRECISION) AS onlineAmount ,USERID FROM (");
        finalQueryStr
                .append(queryStr)
                .append(" ) AS RESULT GROUP BY RESULT.SOURCE,RESULT.counterName,RESULT.employeeName,RESULT.USERID,RESULT.glCode order by SOURCE,employeeName,glCode");

        final StringBuilder finalAggregateQryStr = new StringBuilder();
        finalAggregateQryStr
                .append("SELECT sum(cashCount) AS cashCount,sum(chequeddCount) AS chequeddCount,sum(onlineCount) AS onlineCount,SOURCE,counterName,employeeName,glCode,sum(cashAmount) AS cashAmount, sum(chequeddAmount) AS chequeddAmount,  "
                        + "  sum(cardCount) AS cardCount, sum(cardAmount) AS cardAmount, cast(sum(totalReceiptCount) AS NUMERIC) as totalReceiptCount , sum(onlineAmount) AS onlineAmount ,USERID FROM (");
        finalAggregateQryStr
                .append(aggregateQueryStr)
                .append(" ) AS RESULT GROUP BY RESULT.SOURCE,RESULT.counterName,RESULT.employeeName,RESULT.USERID,RESULT.glCode order by source,employeeName, glCode");

        final SQLQuery query = (SQLQuery) getCurrentSession().createSQLQuery(finalQueryStr.toString())
                .addScalar("cashCount", org.hibernate.type.StringType.INSTANCE).addScalar("cashAmount", DoubleType.INSTANCE)
                .addScalar("chequeddCount", org.hibernate.type.StringType.INSTANCE)
                .addScalar("chequeddAmount", DoubleType.INSTANCE)
                .addScalar("onlineCount", org.hibernate.type.StringType.INSTANCE).addScalar("onlineAmount", DoubleType.INSTANCE)
                .addScalar("source", org.hibernate.type.StringType.INSTANCE)
                .addScalar("counterName", org.hibernate.type.StringType.INSTANCE)
                .addScalar("employeeName", org.hibernate.type.StringType.INSTANCE)
                .addScalar("glCode", org.hibernate.type.StringType.INSTANCE)
                .addScalar("cardAmount", DoubleType.INSTANCE).addScalar("cardCount", org.hibernate.type.StringType.INSTANCE)
                .addScalar("totalReceiptCount", org.hibernate.type.StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(CollectionSummaryHeadWiseReport.class));

        final SQLQuery aggrQuery = (SQLQuery) getCurrentSession().createSQLQuery(finalAggregateQryStr.toString())
                .addScalar("cashCount", org.hibernate.type.StringType.INSTANCE).addScalar("cashAmount", DoubleType.INSTANCE)
                .addScalar("chequeddCount", org.hibernate.type.StringType.INSTANCE)
                .addScalar("chequeddAmount", DoubleType.INSTANCE)
                .addScalar("onlineCount", org.hibernate.type.StringType.INSTANCE).addScalar("onlineAmount", DoubleType.INSTANCE)
                .addScalar("source", org.hibernate.type.StringType.INSTANCE)
                .addScalar("counterName", org.hibernate.type.StringType.INSTANCE)
                .addScalar("employeeName", org.hibernate.type.StringType.INSTANCE)
                .addScalar("glCode", org.hibernate.type.StringType.INSTANCE)
                .addScalar("cardAmount", DoubleType.INSTANCE).addScalar("cardCount", org.hibernate.type.StringType.INSTANCE)
                .addScalar("totalReceiptCount", org.hibernate.type.StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(CollectionSummaryHeadWiseReport.class));

        if (!source.isEmpty() && !source.equals(CollectionConstants.ALL)) {
            query.setString("source", source);
            aggrQuery.setString("source", source);
        }
        if (glCode != null) {
            query.setString("glCode", glCode);
            aggrQuery.setString("glCode", glCode);
        }
        if (status != -1) {
            query.setLong("searchStatus", status);
            aggrQuery.setLong("searchStatus", status);
        }

        if (StringUtils.isNotBlank(paymentMode) && !paymentMode.equals(CollectionConstants.ALL))
            if (paymentMode.equals(CollectionConstants.INSTRUMENTTYPE_CHEQUEORDD)) {
                query.setParameterList("paymentMode", new ArrayList<>(Arrays.asList("cheque", "dd")));
                aggrQuery.setParameterList("paymentMode", new ArrayList<>(Arrays.asList("cheque", "dd")));
            } else {
                query.setString("paymentMode", paymentMode);
                aggrQuery.setString("paymentMode", paymentMode);
            }
        if(branchId != null && branchId != -1)
        {
            query.setInteger("branchId",branchId);
            aggrQuery.setInteger("branchId",branchId);
        }
        final List<CollectionSummaryHeadWiseReport> reportResults = populateQueryResults(query.list());
        final List<CollectionSummaryHeadWiseReport> aggrReportResults = populateQueryResults(aggrQuery.list());
        final CollectionSummaryHeadWiseReportResult collResult = new CollectionSummaryHeadWiseReportResult();
        collResult.setCollectionSummaryReportList(reportResults);
        collResult.setAggrCollectionSummaryReportList(aggrReportResults);
        return collResult;
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