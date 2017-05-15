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
        final StringBuilder defaultQueryStr = new StringBuilder(500);
        final StringBuilder queryStr = new StringBuilder(500);
        queryStr.append("SELECT  (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cash' THEN count(*) END) AS cashCount,  ")
                .append("(CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cheque' THEN count(*) WHEN EGF_INSTRUMENTTYPE.TYPE='dd' THEN count(*) END) AS chequeddCount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE= 'online' THEN count(*) END) AS onlineCount, ")
                .append(" EGCL_COLLECTIONHEADER.SOURCE AS source, EG_LOCATION.NAME AS counterName, EG_USER.NAME AS employeeName,SER.NAME AS serviceName,")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cash' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS cashAmount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cheque' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) WHEN EGF_INSTRUMENTTYPE.TYPE='dd' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS chequeddAmount,")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE= 'online' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS onlineAmount, EG_USER.ID AS USERID, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='bankchallan' THEN count(*) END) AS bankCount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='bankchallan' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS bankAmount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='card' THEN count(*) END) AS cardCount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='card' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS cardAmount, ")
                .append(" count(*) as totalReceiptCount ")
                .append(" FROM EGCL_COLLECTIONHEADER EGCL_COLLECTIONHEADER INNER JOIN EGCL_COLLECTIONINSTRUMENT EGCL_COLLECTIONINSTRUMENT ON EGCL_COLLECTIONHEADER.ID = EGCL_COLLECTIONINSTRUMENT.COLLECTIONHEADER")
                .append(" INNER JOIN EGF_INSTRUMENTHEADER EGF_INSTRUMENTHEADER ON EGCL_COLLECTIONINSTRUMENT.INSTRUMENTHEADER = EGF_INSTRUMENTHEADER.ID")
                .append(" INNER JOIN EGW_STATUS EGW_STATUS ON EGCL_COLLECTIONHEADER.STATUS = EGW_STATUS.ID")
                .append(" INNER JOIN EG_LOCATION EG_LOCATION ON EGCL_COLLECTIONHEADER.LOCATION = EG_LOCATION.ID")
                .append(" INNER JOIN EGF_INSTRUMENTTYPE EGF_INSTRUMENTTYPE ON EGF_INSTRUMENTHEADER.INSTRUMENTTYPE = EGF_INSTRUMENTTYPE.ID")
                .append(" INNER JOIN EGCL_COLLECTIONMIS EGCL_COLLECTIONMIS ON EGCL_COLLECTIONHEADER.ID = EGCL_COLLECTIONMIS.COLLECTIONHEADER")
                .append(" INNER JOIN EG_USER EG_USER ON EGCL_COLLECTIONHEADER.CREATEDBY = EG_USER.ID")
                .append(" INNER JOIN EGEIS_EMPLOYEE EG_EMPLOYEE ON EG_USER.ID = EG_EMPLOYEE.ID")
                .append(" INNER JOIN EGEIS_ASSIGNMENT EGEIS_ASSIGNMENT ON EGEIS_ASSIGNMENT.EMPLOYEE = EG_EMPLOYEE.ID")
                .append(" INNER JOIN  EGCL_SERVICEDETAILS SER ON SER.ID = EGCL_COLLECTIONHEADER.SERVICEDETAILS WHERE")
                .append(" EGW_STATUS.DESCRIPTION != 'Cancelled' AND EGEIS_ASSIGNMENT.ISPRIMARY=true");

        final StringBuilder onlineQueryStr = new StringBuilder();
        onlineQueryStr
                .append("SELECT  (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cash' THEN count(*) END) AS cashCount,  ")
                .append("(CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cheque' THEN count(distinct(EGCL_COLLECTIONHEADER.RECEIPTNUMBER)) WHEN EGF_INSTRUMENTTYPE.TYPE='dd' THEN count(distinct(EGCL_COLLECTIONHEADER.RECEIPTNUMBER)) END) AS chequeddCount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE= 'online' THEN count(*) END) AS onlineCount, ")
                .append(" EGCL_COLLECTIONHEADER.source AS source, '' AS counterName, '' AS employeeName,SER.NAME AS serviceName,")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cash' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS cashAmount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='cheque' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) WHEN EGF_INSTRUMENTTYPE.TYPE='dd' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS chequeddAmount,")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE= 'online' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS onlineAmount, 0 AS USERID, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='bankchallan' THEN count(*) END) AS bankCount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='bankchallan' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS bankAmount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='card' THEN count(*) END) AS cardCount, ")
                .append(" (CASE WHEN EGF_INSTRUMENTTYPE.TYPE='card' THEN SUM(EGF_INSTRUMENTHEADER.INSTRUMENTAMOUNT) END) AS cardAmount, ")
                .append(" count(*) as totalReceiptCount")
                .append(" FROM EGCL_COLLECTIONHEADER EGCL_COLLECTIONHEADER INNER JOIN EGCL_COLLECTIONINSTRUMENT EGCL_COLLECTIONINSTRUMENT ON EGCL_COLLECTIONHEADER.ID = EGCL_COLLECTIONINSTRUMENT.COLLECTIONHEADER")
                .append(" INNER JOIN EGF_INSTRUMENTHEADER EGF_INSTRUMENTHEADER ON EGCL_COLLECTIONINSTRUMENT.INSTRUMENTHEADER = EGF_INSTRUMENTHEADER.ID")
                .append(" INNER JOIN EGW_STATUS EGW_STATUS ON EGCL_COLLECTIONHEADER.STATUS = EGW_STATUS.ID")
                .append(" INNER JOIN EGF_INSTRUMENTTYPE EGF_INSTRUMENTTYPE ON EGF_INSTRUMENTHEADER.INSTRUMENTTYPE = EGF_INSTRUMENTTYPE.ID")
                .append(" INNER JOIN EGCL_COLLECTIONMIS EGCL_COLLECTIONMIS ON EGCL_COLLECTIONHEADER.ID = EGCL_COLLECTIONMIS.COLLECTIONHEADER")
                .append(" INNER JOIN  EGCL_SERVICEDETAILS SER ON SER.ID = EGCL_COLLECTIONHEADER.SERVICEDETAILS WHERE")
                .append(" EGW_STATUS.DESCRIPTION != 'Cancelled' ");
        final StringBuilder queryStrGroup = new StringBuilder();
        queryStrGroup
                .append(" GROUP BY  source, counterName, employeeName, USERID,serviceName, EGF_INSTRUMENTTYPE.TYPE");

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

        if (serviceId != null && serviceId != -1) {
            queryStr.append(" AND EGCL_COLLECTIONHEADER.SERVICEDETAILS =:serviceId");
            onlineQueryStr.append(" AND EGCL_COLLECTIONHEADER.SERVICEDETAILS =:serviceId");
        }

        if (status != -1) {
            queryStr.append(" AND EGCL_COLLECTIONHEADER.STATUS =:searchStatus");
            onlineQueryStr.append(" AND EGCL_COLLECTIONHEADER.STATUS =:searchStatus");
        }

        if (!serviceType.equals(CollectionConstants.ALL)) {
            queryStr.append(" AND SER.SERVICETYPE =:serviceType");
            onlineQueryStr.append(" AND SER.SERVICETYPE =:serviceType");
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
            defaultQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE = 'bankchallan'");
            defaultQueryStr.append(queryStrGroup);
            defaultQueryStr.append(" union ");
            defaultQueryStr.append(queryStr);
            defaultQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE in ('cheque', 'dd')");
            defaultQueryStr.append(queryStrGroup);
            defaultQueryStr.append(" union ");
            defaultQueryStr.append(onlineQueryStr);
            defaultQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE = 'online'");
            defaultQueryStr.append(queryStrGroup);
            defaultQueryStr.append(" union ");
            defaultQueryStr.append(queryStr);
            defaultQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE = 'card'");
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
        aggregateQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE = 'bankchallan'");
        aggregateQueryStr.append(queryStrGroup);
        aggregateQueryStr.append(" union ");
        aggregateQueryStr.append(onlineQueryStr);
        aggregateQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE in( 'cheque','dd')");
        aggregateQueryStr.append(queryStrGroup);
        aggregateQueryStr.append(" union ");
        aggregateQueryStr.append(onlineQueryStr);
        aggregateQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE = 'online'");
        aggregateQueryStr.append(queryStrGroup);
        aggregateQueryStr.append(" union ");
        aggregateQueryStr.append(onlineQueryStr);
        aggregateQueryStr.append(" AND EGF_INSTRUMENTTYPE.TYPE  = 'card' ");
        aggregateQueryStr.append(queryStrGroup);

        final StringBuilder finalQueryStr = new StringBuilder();
        finalQueryStr
                .append("SELECT cast(sum(cashCount) AS NUMERIC) AS cashCount,cast(sum(chequeddCount) AS NUMERIC) AS chequeddCount,cast(sum(onlineCount) AS NUMERIC) AS onlineCount,source,counterName,employeeName,serviceName,cast(sum(cashAmount) AS NUMERIC) AS cashAmount, cast(sum(chequeddAmount) AS NUMERIC) AS chequeddAmount, cast(sum(onlineAmount) AS NUMERIC) AS onlineAmount ,USERID,cast(sum(bankCount) AS NUMERIC) AS bankCount, cast(sum(bankAmount) AS NUMERIC) AS bankAmount, "
                        + "  cast(sum(cardCount) AS NUMERIC) AS cardCount, cast(sum(cardAmount) AS NUMERIC) AS cardAmount, cast(sum(totalReceiptCount) AS NUMERIC) as totalReceiptCount  FROM (");
        finalQueryStr
                .append(queryStr)
                .append(" ) AS RESULT GROUP BY RESULT.source,RESULT.counterName,RESULT.employeeName,RESULT.USERID,RESULT.serviceName order by source,employeeName, serviceName ");

        final StringBuilder finalAggregateQryStr = new StringBuilder();
        finalAggregateQryStr
                .append("SELECT sum(cashCount) AS cashCount,sum(chequeddCount) AS chequeddCount,sum(onlineCount) AS onlineCount,source,counterName,employeeName,serviceName,sum(cashAmount) AS cashAmount, sum(chequeddAmount) AS chequeddAmount, sum(onlineAmount) AS onlineAmount ,USERID, sum(bankCount) AS bankCount, sum(bankAmount) AS bankAmount, "
                        + " sum(cardCount) AS cardCount, sum(cardAmount) AS cardAmount,  cast(sum(totalReceiptCount) AS NUMERIC) as totalReceiptCount FROM (");
        finalAggregateQryStr
                .append(aggregateQueryStr)
                .append(" ) AS RESULT GROUP BY RESULT.source,RESULT.counterName,RESULT.employeeName,RESULT.USERID,RESULT.serviceName order by source,employeeName, serviceName ");

        final SQLQuery query = (SQLQuery) getCurrentSession().createSQLQuery(finalQueryStr.toString())
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

        final SQLQuery aggrQuery = (SQLQuery) getCurrentSession().createSQLQuery(finalAggregateQryStr.toString())
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
        if (!source.isEmpty() && !source.equals(CollectionConstants.ALL)) {
            query.setString("source", source);
            aggrQuery.setString("source", source);
        }
        if (serviceId != null && serviceId != -1) {
            query.setLong("serviceId", serviceId);
            aggrQuery.setLong("serviceId", serviceId);
        }
        if (status != -1) {
            query.setLong("searchStatus", status);
            aggrQuery.setLong("searchStatus", status);
        }

        if (!serviceType.equals(CollectionConstants.ALL)) {
            query.setString("serviceType", serviceType);
            aggrQuery.setString("serviceType", serviceType);
        }

        if (StringUtils.isNotBlank(paymentMode) && !paymentMode.equals(CollectionConstants.ALL))
            if (paymentMode.equals(CollectionConstants.INSTRUMENTTYPE_CHEQUEORDD)) {
                query.setParameterList("paymentMode", new ArrayList<>(Arrays.asList("cheque", "dd")));
                aggrQuery.setParameterList("paymentMode", new ArrayList<>(Arrays.asList("cheque", "dd")));
            } else {
                query.setString("paymentMode", paymentMode);
                aggrQuery.setString("paymentMode", paymentMode);
            }
        final List<CollectionSummaryReport> reportResults = populateQueryResults(query.list());

        final List<CollectionSummaryReport> aggrReportResults = populateQueryResults(aggrQuery.list());
        final CollectionSummaryReportResult collResult = new CollectionSummaryReportResult();
        collResult.setCollectionSummaryReportList(reportResults);
        collResult.setAggrCollectionSummaryReportList(aggrReportResults);
        return collResult;
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