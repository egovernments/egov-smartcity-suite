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

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.CollectionSummaryReport;
import org.egov.collection.entity.CollectionSummaryReportResult;
import org.egov.collection.entity.OnlinePaymentResult;
import org.egov.infra.config.properties.ApplicationProperties;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectionReportService {
    private static final Logger LOGGER = Logger.getLogger(CollectionReportService.class);

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ApplicationProperties applicationProperties;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public SQLQuery getOnlinePaymentReportData(final String districtName, final String ulbName, final String fromDate,
            final String toDate, final String transactionId) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        final StringBuilder queryStr = new StringBuilder(500);
        queryStr.append("select * from ").append(applicationProperties.statewideSchemaName())
                .append(".onlinepayment_view opv where 1=1");

        if (StringUtils.isNotBlank(districtName))
            queryStr.append(" and opv.districtName=:districtName ");
        if (StringUtils.isNotBlank(ulbName))
            queryStr.append(" and opv.ulbName=:ulbName ");
        if (StringUtils.isNotBlank(fromDate))
            queryStr.append(" and opv.transactiondate>=:fromDate ");
        if (StringUtils.isNotBlank(toDate))
            queryStr.append(" and opv.transactiondate<=:toDate ");
        if (StringUtils.isNotBlank(transactionId))
            queryStr.append(" and opv.transactionnumber like :transactionnumber ");
        queryStr.append(" order by receiptdate desc ");

        final SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());

        if (StringUtils.isNotBlank(districtName))
            query.setString("districtName", districtName);
        if (StringUtils.isNotBlank(ulbName))
            query.setString("ulbName", ulbName);
        try {
            if (StringUtils.isNotBlank(fromDate))
                query.setDate("fromDate", dateFormatter.parse(fromDate));
            if (StringUtils.isNotBlank(toDate))
                query.setDate("toDate", dateFormatter.parse(toDate));
        } catch (final ParseException e) {
            LOGGER.error("Exception parsing Date" + e.getMessage());
        }
        if (StringUtils.isNotBlank(transactionId))
            query.setString("transactionnumber", "%" + transactionId + "%");
        queryStr.append(" order by opv.receiptdate desc");
        query.setResultTransformer(new AliasToBeanResultTransformer(OnlinePaymentResult.class));
        return query;
    }

    public List<Object[]> getUlbNames(final String districtName) {
        final StringBuilder queryStr = new StringBuilder("select distinct ulbname from ").append(
                applicationProperties.statewideSchemaName()).append(".onlinepayment_view opv where 1=1");
        if (StringUtils.isNotBlank(districtName))
            queryStr.append(" and opv.districtName=:districtName ");
        final SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        if (StringUtils.isNotBlank(districtName))
            query.setString("districtName", districtName);
        return query.list();
    }

    public List<Object[]> getDistrictNames() {
        final StringBuilder queryStr = new StringBuilder("select distinct districtname from ").append(
                applicationProperties.statewideSchemaName()).append(".onlinepayment_view");
        final SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        return query.list();
    }

    public CollectionSummaryReportResult getCollectionSummaryReport(final Date fromDate, final Date toDate,
            final String paymentMode, final String source, final Long serviceId, final int status,
            final String serviceType) {
        final SimpleDateFormat fromDateFormatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        final SimpleDateFormat toDateFormatter = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        StringBuilder aggregateQuery = new StringBuilder();
        StringBuilder userwiseQuery = new StringBuilder();
        final StringBuilder finalUserwiseQuery = new StringBuilder();
        final StringBuilder finalAggregateQuery = new StringBuilder();
        final StringBuilder selectQuery = new StringBuilder(
                "SELECT (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cash' THEN count(distinct(EGCL_COLLECTIONHEADER.ID)) END) AS cashCount,  "
                        +
                        "(CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cheque' THEN count(distinct(EGCL_COLLECTIONHEADER.ID)) WHEN EGF_INSTRUMENTTYPE.TYPE='dd' THEN count(distinct(EGCL_COLLECTIONHEADER.ID)) END) AS chequeddCount, "
                        +
                        " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE= 'online' THEN count(distinct(EGCL_COLLECTIONHEADER.ID)) END) AS onlineCount, "
                        +
                        " EGCL_COLLECTIONHEADER.SOURCE AS source, SER.NAME AS serviceName," +
                        " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cash' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS cashAmount, "
                        +
                        " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cheque' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) WHEN EGF_INSTRUMENTTYPE.TYPE='dd' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS chequeddAmount,"
                        +
                        " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE= 'online' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS onlineAmount, "
                        +
                        " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='bankchallan' THEN count(distinct(EGCL_COLLECTIONHEADER.ID)) END) AS bankCount, "
                        +
                        " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='bankchallan' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS bankAmount, "
                        +
                        " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='card' THEN count(distinct(EGCL_COLLECTIONHEADER.ID)) END) AS cardCount, "
                        +
                        " (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='card' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS cardAmount, "
                        +
                        " count(distinct(EGCL_COLLECTIONHEADER.ID)) as totalReceiptCount ");
        final StringBuilder fromQuery = new StringBuilder(
                " FROM EGCL_COLLECTIONHEADER EGCL_COLLECTIONHEADER INNER JOIN EGCL_COLLECTIONINSTRUMENT EGCL_COLLECTIONINSTRUMENT ON EGCL_COLLECTIONHEADER.ID = EGCL_COLLECTIONINSTRUMENT.COLLECTIONHEADER"
                        +
                        " INNER JOIN EGF_INSTRUMENTHEADER EGF_INSTRUMENTHEADER ON EGCL_COLLECTIONINSTRUMENT.INSTRUMENTHEADER = EGF_INSTRUMENTHEADER.ID"
                        +
                        " INNER JOIN EGW_STATUS EGW_STATUS ON EGCL_COLLECTIONHEADER.STATUS = EGW_STATUS.ID" +
                        " INNER JOIN EGF_INSTRUMENTTYPE EGF_INSTRUMENTTYPE ON EGF_INSTRUMENTHEADER.INSTRUMENTTYPE = EGF_INSTRUMENTTYPE.ID"
                        +
                        " INNER JOIN EGCL_COLLECTIONMIS EGCL_COLLECTIONMIS ON EGCL_COLLECTIONHEADER.ID = EGCL_COLLECTIONMIS.COLLECTIONHEADER"
                        +
                        " INNER JOIN EGCL_SERVICEDETAILS SER ON SER.ID = EGCL_COLLECTIONHEADER.SERVICEDETAILS ");
        final StringBuilder whereQuery = new StringBuilder(" WHERE EGW_STATUS.DESCRIPTION != 'Cancelled'");
        final StringBuilder groupQuery = new StringBuilder(" GROUP BY  source, counterName, employeeName, USERID,serviceName, "
                + "EGF_INSTRUMENTTYPE.TYPE");

        aggregateQuery.append(selectQuery)
                .append(" , '' AS counterName, '' AS employeeName, 0 AS USERID ")
                .append(fromQuery);

        userwiseQuery.append(selectQuery)
                .append(" , EG_LOCATION.NAME AS counterName, EG_USER.NAME AS employeeName, EG_USER.ID AS USERID")
                .append(fromQuery)
                .append(" LEFT JOIN EG_LOCATION EG_LOCATION ON EGCL_COLLECTIONHEADER.LOCATION = EG_LOCATION.ID "
                        + " INNER JOIN EG_USER EG_USER ON EGCL_COLLECTIONHEADER.CREATEDBY = EG_USER.ID ");

        if (fromDate != null && toDate != null) {
            whereQuery.append(" AND EGCL_COLLECTIONHEADER.RECEIPTDATE between to_timestamp('"
                    + fromDateFormatter.format(fromDate) + "', 'YYYY-MM-DD HH24:MI:SS') and " + " to_timestamp('"
                    + toDateFormatter.format(toDate) + "', 'YYYY-MM-DD HH24:MI:SS') ");
        }

        if (!source.isEmpty() && !source.equals(CollectionConstants.ALL)) {
            whereQuery.append(" AND EGCL_COLLECTIONHEADER.SOURCE=:source");
        } else {
            userwiseQuery.setLength(0);
            userwiseQuery.append(aggregateQuery);
        }

        if (serviceId != null && serviceId != -1)
            whereQuery.append(" AND EGCL_COLLECTIONHEADER.SERVICEDETAILS =:serviceId");
        if (status != -1)
            whereQuery.append(" AND EGCL_COLLECTIONHEADER.STATUS =:searchStatus");
        if (!serviceType.equals(CollectionConstants.ALL))
            whereQuery.append(" AND SER.SERVICETYPE =:serviceType");
        if (StringUtils.isNotBlank(paymentMode) && !paymentMode.equals(CollectionConstants.ALL)) {
            whereQuery.append(" AND EGF_INSTRUMENTTYPE.TYPE in (:paymentMode)");
            if (paymentMode.equals(CollectionConstants.INSTRUMENTTYPE_ONLINE)) {
                userwiseQuery = aggregateQuery;
            }
            userwiseQuery.append(whereQuery).append(groupQuery);
            aggregateQuery.append(whereQuery).append(groupQuery);
        } else {
            userwiseQuery.append(whereQuery);
            aggregateQuery.append(whereQuery);
            userwiseQuery = prepareQueryForAllPaymentMode(userwiseQuery, groupQuery);
            aggregateQuery = prepareQueryForAllPaymentMode(aggregateQuery, groupQuery);
        }

        final StringBuilder finalSelectQuery = new StringBuilder(
                "SELECT cast(sum(cashCount) AS NUMERIC) AS cashCount,cast(sum(chequeddCount) AS NUMERIC) AS chequeddCount,cast(sum(onlineCount) AS NUMERIC) AS onlineCount,source,counterName,employeeName,serviceName,cast(sum(cashAmount) AS NUMERIC) AS cashAmount, cast(sum(chequeddAmount) AS NUMERIC) AS chequeddAmount, cast(sum(onlineAmount) AS NUMERIC) AS onlineAmount ,USERID,cast(sum(bankCount) AS NUMERIC) AS bankCount, cast(sum(bankAmount) AS NUMERIC) AS bankAmount, "
                        + "  cast(sum(cardCount) AS NUMERIC) AS cardCount, cast(sum(cardAmount) AS NUMERIC) AS cardAmount, cast(sum(totalReceiptCount) AS NUMERIC) as totalReceiptCount  FROM (");
        final StringBuilder finalGroupQuery = new StringBuilder(
                " ) AS RESULT GROUP BY RESULT.source,RESULT.counterName,RESULT.employeeName,RESULT.USERID,RESULT.serviceName order by source,employeeName, serviceName ");

        finalUserwiseQuery.append(finalSelectQuery).append(userwiseQuery).append(finalGroupQuery);
        finalAggregateQuery.append(finalSelectQuery).append(aggregateQuery).append(finalGroupQuery);

        final SQLQuery userwiseSqluery = createSQLQuery(finalUserwiseQuery.toString());
        final SQLQuery aggregateSqlQuery = createSQLQuery(finalAggregateQuery.toString());

        if (!source.isEmpty() && !source.equals(CollectionConstants.ALL)) {
            userwiseSqluery.setString("source", source);
            aggregateSqlQuery.setString("source", source);
        }
        if (serviceId != null && serviceId != -1) {
            userwiseSqluery.setLong("serviceId", serviceId);
            aggregateSqlQuery.setLong("serviceId", serviceId);
        }
        if (status != -1) {
            userwiseSqluery.setLong("searchStatus", status);
            aggregateSqlQuery.setLong("searchStatus", status);
        }

        if (!serviceType.equals(CollectionConstants.ALL)) {
            userwiseSqluery.setString("serviceType", serviceType);
            aggregateSqlQuery.setString("serviceType", serviceType);
        }

        if (StringUtils.isNotBlank(paymentMode) && !paymentMode.equals(CollectionConstants.ALL))
            if (paymentMode.equals(CollectionConstants.INSTRUMENTTYPE_CHEQUEORDD)) {
                userwiseSqluery.setParameterList("paymentMode", new ArrayList<>(Arrays.asList("cheque", "dd")));
                aggregateSqlQuery.setParameterList("paymentMode", new ArrayList<>(Arrays.asList("cheque", "dd")));
            } else {
                userwiseSqluery.setString("paymentMode", paymentMode);
                aggregateSqlQuery.setString("paymentMode", paymentMode);
            }
        final List<CollectionSummaryReport> reportResults = populateQueryResults(userwiseSqluery.list());
        final List<CollectionSummaryReport> aggrReportResults = populateQueryResults(aggregateSqlQuery.list());
        final CollectionSummaryReportResult collResult = new CollectionSummaryReportResult();
        collResult.setCollectionSummaryReportList(reportResults);
        collResult.setAggrCollectionSummaryReportList(aggrReportResults);
        return collResult;
    }

    public StringBuilder prepareQueryForAllPaymentMode(StringBuilder query, StringBuilder groupQuery) {
        String unionString = " union ";
        StringBuilder queryString = new StringBuilder();
        queryString.append(query);
        queryString.append(" AND EGF_INSTRUMENTTYPE.TYPE = 'cash'");
        queryString.append(groupQuery);
        queryString.append(unionString);
        queryString.append(query);
        queryString.append(" AND EGF_INSTRUMENTTYPE.TYPE = 'bankchallan'");
        queryString.append(groupQuery);
        queryString.append(unionString);
        queryString.append(query);
        queryString.append(" AND EGF_INSTRUMENTTYPE.TYPE in( 'cheque','dd')");
        queryString.append(groupQuery);
        queryString.append(unionString);
        queryString.append(query);
        queryString.append(" AND EGF_INSTRUMENTTYPE.TYPE = 'online'");
        queryString.append(groupQuery);
        queryString.append(unionString);
        queryString.append(query);
        queryString.append(" AND EGF_INSTRUMENTTYPE.TYPE  = 'card' ");
        queryString.append(groupQuery);
        return queryString;
    }

    public SQLQuery createSQLQuery(String query) {
        return (SQLQuery) getCurrentSession().createSQLQuery(query)
                .addScalar("cashCount", org.hibernate.type.StringType.INSTANCE)
                .addScalar("cashAmount", BigDecimalType.INSTANCE)
                .addScalar("chequeddCount", org.hibernate.type.StringType.INSTANCE)
                .addScalar("chequeddAmount", BigDecimalType.INSTANCE)
                .addScalar("onlineCount", org.hibernate.type.StringType.INSTANCE)
                .addScalar("onlineAmount", BigDecimalType.INSTANCE)
                .addScalar("source", org.hibernate.type.StringType.INSTANCE)
                .addScalar("serviceName", org.hibernate.type.StringType.INSTANCE)
                .addScalar("counterName", org.hibernate.type.StringType.INSTANCE)
                .addScalar("employeeName", org.hibernate.type.StringType.INSTANCE)
                .addScalar("bankCount", org.hibernate.type.StringType.INSTANCE)
                .addScalar("bankAmount", BigDecimalType.INSTANCE).addScalar("cardAmount", BigDecimalType.INSTANCE)
                .addScalar("cardCount", org.hibernate.type.StringType.INSTANCE)
                .addScalar("totalReceiptCount", org.hibernate.type.StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(CollectionSummaryReport.class));
    }

    public List<CollectionSummaryReport> populateQueryResults(final List<CollectionSummaryReport> queryResults) {
        for (final CollectionSummaryReport collectionSummaryReport : queryResults) {
            if (collectionSummaryReport.getCashCount() == null)
                collectionSummaryReport.setCashCount("");
            if (collectionSummaryReport.getChequeddCount() == null)
                collectionSummaryReport.setChequeddCount("");
            if (collectionSummaryReport.getOnlineCount() == null)
                collectionSummaryReport.setOnlineCount("");
            if (collectionSummaryReport.getBankCount() == null)
                collectionSummaryReport.setBankCount("");
            if (collectionSummaryReport.getCardCount() == null)
                collectionSummaryReport.setCardCount("");
            if (collectionSummaryReport.getTotalReceiptCount() == null)
                collectionSummaryReport.setTotalReceiptCount("");
            if (collectionSummaryReport.getCashAmount() == null)
                collectionSummaryReport.setCashAmount(BigDecimal.ZERO);
            if (collectionSummaryReport.getChequeddAmount() == null)
                collectionSummaryReport.setChequeddAmount(BigDecimal.ZERO);
            if (collectionSummaryReport.getOnlineAmount() == null)
                collectionSummaryReport.setOnlineAmount(BigDecimal.ZERO);
            if (collectionSummaryReport.getBankAmount() == null)
                collectionSummaryReport.setBankAmount(BigDecimal.ZERO);
            if (collectionSummaryReport.getCardAmount() == null)
                collectionSummaryReport.setCardAmount(BigDecimal.ZERO);
            collectionSummaryReport.setTotalAmount(collectionSummaryReport.getCardAmount()
                    .add(collectionSummaryReport.getBankAmount()).add(collectionSummaryReport.getOnlineAmount())
                    .add(collectionSummaryReport.getChequeddAmount()).add(collectionSummaryReport.getCashAmount()));

        }
        return queryResults;
    }
}