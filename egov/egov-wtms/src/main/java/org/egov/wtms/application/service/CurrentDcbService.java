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
package org.egov.wtms.application.service;

import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.utils.DateUtils;
import org.egov.wtms.application.entity.DonationChargesDCBReportSearch;
import org.egov.wtms.application.entity.WaterChargesReceiptInfo;
import org.egov.wtms.reports.entity.DCBReportResult;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CurrentDcbService {

    public static final String ZONEWISE = "zone";
    public static final String WARDWISE = "ward";
    public static final String BLOCKWISE = "block";
    public static final String LOCALITYWISE = "locality";
    public static final String PROPERTY = "property";

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public DCBDisplayInfo getDcbDispInfo() {
        final DCBDisplayInfo dcbDispInfo = new DCBDisplayInfo();
        final List<String> reasonMasterCodes = new ArrayList<>(0);
        final List<String> reasonCategoryCodes = new ArrayList<>(0);
        reasonMasterCodes.add(WaterTaxConstants.WATERTAXREASONCODE);
        dcbDispInfo.setReasonCategoryCodes(reasonCategoryCodes);
        dcbDispInfo.setReasonMasterCodes(reasonMasterCodes);
        return dcbDispInfo;

    }

    @ReadOnly
    public List<WaterChargesReceiptInfo> getMigratedReceipttDetails(final String consumerNumber) throws ParseException {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append(
                "select distinct(i_bookno) as \"bookNumber\", cast(i_ctrrcptno as varchar) as \"receiptNumber\",dt_ctrrcptdt as \"receiptDate\",dt_paidfrmprddt as \"fromDate\",dt_paidtoprddt as \"toDate\","
                        + "d_crr+d_arr as \"receiptAmount\" from wt_wtchrgrcpt_tbl where i_csmrno =" + consumerNumber
                        + " order by dt_ctrrcptdt desc");
        final SQLQuery finalQuery = getCurrentSession().createSQLQuery(queryStr.toString());
        finalQuery.setResultTransformer(new AliasToBeanResultTransformer(WaterChargesReceiptInfo.class));
        return finalQuery.list();

    }

    public SQLQuery getMigratedReceiptDetails(final Long connectiondetails) throws ParseException {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append(
                "select distinct(booknumber) as \"bookNumber\", receiptnumber as \"receiptNumber\",receiptdate as \"receiptDate\",fromdate as \"fromDate\",todate as \"toDate\","
                        + "cast(amount as numeric(18,2)) as \"receiptAmount\" from egwtr_legacy_receipts where connectiondetails ="
                        + connectiondetails);
        final SQLQuery finalQuery = getCurrentSession().createSQLQuery(queryStr.toString());
        finalQuery.setResultTransformer(new AliasToBeanResultTransformer(WaterChargesReceiptInfo.class));
        return finalQuery;

    }

    @ReadOnly
    public List<DCBReportResult> getReportResult(final String paramList, final String connectionType, final String mode,
            final String reportType) {
        StringBuilder query;
        final StringBuilder selectQry1 = new StringBuilder();
        final StringBuilder selectQry2 = new StringBuilder();
        StringBuilder fromQry;
        StringBuilder whereQry = new StringBuilder();
        final StringBuilder groupByQry = new StringBuilder();
        selectQry2
                .append("  cast(SUM(arr_demand) as bigint) AS arr_demand,cast(SUM(curr_demand) as bigint) AS curr_demand,cast(SUM(arr_coll) as bigint) AS arr_coll,cast(SUM(curr_coll) as bigint) AS curr_coll,"
                        + "cast(SUM(arr_balance) as bigint) AS arr_balance,cast(SUM(curr_balance) as bigint) AS curr_balance ");
        fromQry = new StringBuilder(" from egwtr_mv_dcb_view dcbinfo,eg_boundary boundary ");
        if (ZONEWISE.equalsIgnoreCase(mode)) {
            selectQry1
                    .append("select  distinct cast(dcbinfo.zoneid as integer) as \"zoneid\",boundary.name as \"boundaryName\", count(hscno) as countofconsumerno,");
            groupByQry.append(" group by dcbinfo.zoneid,boundary.name order by boundary.name");
            whereQry.append(" where dcbinfo.zoneid=boundary.id ");
            if (paramList != null && !"".equalsIgnoreCase(paramList))
                whereQry = whereQry.append(" and dcbinfo.zoneid in (" + paramList + ")");
        } else if (WARDWISE.equalsIgnoreCase(mode)) {
            selectQry1
                    .append("select distinct cast(dcbinfo.wardid as integer) as \"wardid\",boundary.name as \"boundaryName\",count(hscno) as countofconsumerno, ");
            groupByQry.append(" group by dcbinfo.wardid,boundary.name order by boundary.name");
            whereQry.append(" where dcbinfo.wardid=boundary.id ");
            if (paramList != null && !paramList.equalsIgnoreCase("") && reportType.equalsIgnoreCase("wardWise"))
                whereQry = whereQry.append(" and dcbinfo.wardid in (" + paramList + ")");
            if (paramList != null && !paramList.equalsIgnoreCase("") && !reportType.equalsIgnoreCase("wardWise"))
                whereQry = whereQry.append(" and dcbinfo.zoneid in (" + paramList + ")");
        } else if (BLOCKWISE.equalsIgnoreCase(mode)) {
            selectQry1
                    .append("select  distinct cast(dcbinfo.block as integer) as \"wardid\",boundary.name as \"boundaryName\", count(hscno) as countofconsumerno,");
            groupByQry.append(" group by dcbinfo.block,boundary.name order by boundary.name");
            whereQry.append(" where dcbinfo.block=boundary.id ");
            if (paramList != null && !paramList.equalsIgnoreCase("") && reportType.equalsIgnoreCase("blockWise"))
                whereQry = whereQry.append(" and dcbinfo.block in (" + paramList + ")");
            if (paramList != null && !paramList.equalsIgnoreCase("") && !reportType.equalsIgnoreCase("blockWise"))
                whereQry = whereQry.append(" and dcbinfo.wardid in (" + paramList + ")");
        } else if (LOCALITYWISE.equalsIgnoreCase(mode)) {
            selectQry1
                    .append("select  distinct cast(dcbinfo.locality as integer) as \"locality\",boundary.name as \"boundaryName\",dcbinfo.username as \"username\", count(hscno) as countofconsumerno, ");
            groupByQry.append(" group by dcbinfo.locality,boundary.name,dcbinfo.username order by boundary.name");
            whereQry.append(" where dcbinfo.locality=boundary.id and dcbinfo.locality in (" + paramList + ")");
        } else if (PROPERTY.equalsIgnoreCase(mode)) {
            selectQry1
                    .append("select distinct dcbinfo.hscno as hscno,dcbinfo.propertyid as \"propertyid\" ,dcbinfo.username as \"username\", ");
            fromQry = new StringBuilder(" from egwtr_mv_dcb_view dcbinfo ");
            groupByQry.append("group by dcbinfo.hscno,dcbinfo.propertyid,dcbinfo.username ");
            whereQry.append(" where dcbinfo.hscno is not null  ");
            if (paramList != null && !paramList.equalsIgnoreCase("") && reportType.equalsIgnoreCase("localityWise"))
                whereQry = whereQry.append(" and dcbinfo.locality in (" + paramList + ")");
            else
                whereQry = whereQry.append(" and dcbinfo.block in (" + paramList + ")");
        }
        if (!"".equalsIgnoreCase(connectionType))
            whereQry.append(" and dcbinfo.connectiontype = '" + connectionType + "'");
        whereQry.append(" and dcbinfo.connectionstatus = 'ACTIVE'");
        query = selectQry1.append(selectQry2).append(fromQry).append(whereQry).append(groupByQry);
        final SQLQuery sqlQuery = entityManager.unwrap(Session.class).createSQLQuery(query.toString());
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(DCBReportResult.class));
        return sqlQuery.list();
    }

    @ReadOnly
    public List<Object[]> getDonationDCBReportDetails(final DonationChargesDCBReportSearch chargesDCBReportSearch) {
        StringBuilder selectQuery = new StringBuilder();
        StringBuilder fromQuery;
        final StringBuilder whereQuery = new StringBuilder();
        selectQuery.append(
                "select consumernumber, propertyid, username, mobileno, address, donation_demand, donation_coll, donation_balance");
        fromQuery = new StringBuilder(" from egwtr_mv_donation_dcb_view ");
        whereQuery.append(" where donation_demand is not null ");

        if (chargesDCBReportSearch.getFromDate() != null)
            whereQuery.append(" and applicationdate >= '" + chargesDCBReportSearch.getFromDate() + "' ");
        if (chargesDCBReportSearch.getToDate() != null)
            whereQuery.append(" and applicationdate <= '" + DateUtils.endOfDay(chargesDCBReportSearch.getToDate()) + "' ");
        if (chargesDCBReportSearch.getFromAmount() != null && !chargesDCBReportSearch.getFromAmount().equals(BigDecimal.ZERO))
            whereQuery.append(" and donation_demand >= " + chargesDCBReportSearch.getFromAmount());
        if (chargesDCBReportSearch.getToAmount() != null && !chargesDCBReportSearch.getToAmount().equals(BigDecimal.ZERO))
            whereQuery.append(" and donation_demand <= " + chargesDCBReportSearch.getToAmount());
        if (chargesDCBReportSearch.getPendingForPaymentOnly() != null && chargesDCBReportSearch.getPendingForPaymentOnly())
            whereQuery.append(" and (donation_demand-donation_coll) >0 ");
        else
            whereQuery.append(" and (donation_demand-donation_coll) >=0 ");
        selectQuery = selectQuery.append(fromQuery).append(whereQuery);
        final SQLQuery sqlQuery = entityManager.unwrap(Session.class).createSQLQuery(selectQuery.toString());
        return sqlQuery.list();
    }
}
