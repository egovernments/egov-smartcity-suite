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
package org.egov.ptis.domain.service.report;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.ROLE_COLLECTION_OPERATOR;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.RegionalHeirarchy;
import org.egov.commons.RegionalHeirarchyType;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.service.RegionalHeirarchyService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.EnvironmentSettings;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.BillCollectorDailyCollectionReportResult;
import org.egov.ptis.domain.entity.property.CollectionSummary;
import org.egov.ptis.domain.entity.property.CurrentInstDCBReportResult;
import org.egov.ptis.domain.entity.property.DefaultersInfo;
import org.egov.ptis.domain.entity.property.InstDmdCollMaterializeView;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.master.service.PropertyUsageService;
import org.egov.ptis.report.bean.ApartmentDCBReportResult;
import org.egov.ptis.report.bean.NatureOfUsageResult;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class ReportService {

    private static final String AND_PI_PROPTYMASTER_IN = " and pi.proptymaster in (";
    private static final String BLOCK = "block";
    public static final String CURRENTYEAR_FIRST_HALF = "Current 1st Half";
    public static final String CURRENTYEAR_SECOND_HALF = "Current 2nd Half";
    private static final String COURTCASE = "COURTCASE";
    private static final String CENTRAL_GOVT_33_5 = "CENTRAL_GOVT_33.5";
    private static final String CENTRAL_GOVT_75 = "CENTRAL_GOVT_75";
    private static final String CENTRAL_GOVT_50 = "CENTRAL_GOVT_50";
    private static final String COURTCASE_CENTRAL_GOVT_33_5 = "COURTCASE-CENTRAL_GOVT_33.5";
    private static final String COURTCASE_CENTRAL_GOVT_75 = "COURTCASE-CENTRAL_GOVT_75";
    private static final String COURTCASE_CENTRAL_GOVT_50 = "COURTCASE-CENTRAL_GOVT_50";
    private static final String CENTRAL_GOVT = "CENTRAL_GOVT";
    private static final String STATE_GOVT = "STATE_GOVT";
    private static final String COURTCASE_STATE_GOVT = "COURTCASE-STATE_GOVT";
    private static final String PRIVATE_EXCLUDE_COURTCASE = "PRIVATE_EXCLUDE_COURTCASE";
    private static final String COURTCASE_PRIVATE = "COURTCASE-PRIVATE";
    private static final String COURTCASE_EWSHS = "COURTCASE-EWSHS";
    private static final String EWSHS = "EWSHS";
    private static final String PRIVATE = "PRIVATE";
    private static final String ABOVE_FIVE_YEARS = "Above 5 Years";
    private PersistenceService propPerServ;

    @Autowired
    private RegionalHeirarchyService regionalHeirarchyService;
    @Autowired
    private UserService userService;
    @Autowired
    private FinancialYearDAO financialYearDAO;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private EnvironmentSettings environmentSettings;
    @Autowired
    private PropertyUsageService propertyUsageService;
    @Autowired
    private BoundaryService boundaryService;

    @ReadOnly
    public List<CurrentInstDCBReportResult> getCurrentInstallmentDCB(final String ward) {
        final StringBuilder queryStr = new StringBuilder(500);

        queryStr.append(
                "select ward.name as \"wardName\", cast(count(*) as integer) as \"noOfProperties\", cast(sum(pi.aggregate_current_firsthalf_demand+pi.aggregate_current_secondhalf_demand) as numeric) as \"currDemand\", cast(sum(pi.current_firsthalf_collection+pi.current_secondhalf_collection) as numeric) as \"currCollection\", cast(sum(pi.aggregate_arrear_demand) as numeric) as \"arrearDemand\",cast(sum(pi.arrearcollection) as numeric) as \"arrearCollection\" from egpt_mv_propertyinfo pi,"
                        + " eg_boundary ward where ward.id = pi.wardid and pi.isexempted = false and pi.isactive=true and ward.boundarytype = (select id from eg_boundary_type where name='Ward' and hierarchytype = (select id from eg_hierarchy_type where name= 'REVENUE')) ");

        if (StringUtils.isNotBlank(ward))
            queryStr.append(" and pi.wardid=:ward ");

        queryStr.append("group by ward.name order by ward.name ");
        final Query query = propPerServ.getSession().createSQLQuery(queryStr.toString());
        if (StringUtils.isNotBlank(ward))
            query.setLong("ward", Long.valueOf(ward));

        query.setResultTransformer(new AliasToBeanResultTransformer(CurrentInstDCBReportResult.class));
        return query.list();
    }

    public Set<User> getCollectionOperators() {
        return userService.getUsersByRoleName(ROLE_COLLECTION_OPERATOR);
    }

    public PersistenceService getPropPerServ() {
        return propPerServ;
    }

    public void setPropPerServ(final PersistenceService propPerServ) {
        this.propPerServ = propPerServ;
    }

    @ReadOnly
    public List<BillCollectorDailyCollectionReportResult> getBillCollectorWiseDailyCollection(final Date date,
            final BillCollectorDailyCollectionReportResult bcDailyCollectionReportResult) {
        boolean whereConditionAdded = false;
        List<BillCollectorDailyCollectionReportResult> listBcPayment = new ArrayList<BillCollectorDailyCollectionReportResult>(
                0);
        int noofDays = 0;
        final StringBuilder queryBuilder = new StringBuilder(
                " select distinct district,ulbname  \"ulbName\" ,ulbcode \"ulbCode\" ,collectorname,mobilenumber,sum(target_arrears_demand) \"target_arrears_demand\",sum(target_current_demand) \"target_current_demand\",sum(today_arrears_collection) \"today_arrears_collection\",sum(today_currentyear_collection) \"today_currentyear_collection\", "
                        + " sum(cummulative_arrears_collection) \"cummulative_arrears_collection\",sum(cummulative_currentyear_collection) \"cummulative_currentyear_collection\",sum(lastyear_collection) \"lastyear_collection\",sum(lastyear_cummulative_collection) \"lastyear_cummulative_collection\"   "
                        + "from " + environmentSettings.statewideSchemaName() + ".billColl_DialyCollection_view ");
        final String value_ALL = "ALL";

        if (bcDailyCollectionReportResult != null) {
            if (bcDailyCollectionReportResult.getCity() != null && !bcDailyCollectionReportResult.getCity().equals("")
                    && !bcDailyCollectionReportResult.getCity().equalsIgnoreCase(value_ALL)) {
                whereConditionAdded = addWhereCondition(whereConditionAdded, queryBuilder);
                queryBuilder.append("  lower(ulbname)=:cityName  ");
            } else if (bcDailyCollectionReportResult.getDistrict() != null
                    && !bcDailyCollectionReportResult.getDistrict().equals("")
                    && !bcDailyCollectionReportResult.getDistrict().equalsIgnoreCase(value_ALL)) {
                if (whereConditionAdded)
                    queryBuilder.append(" and  lower(district)=:districtName ");
                else {
                    whereConditionAdded = addWhereCondition(whereConditionAdded, queryBuilder);
                    queryBuilder.append("  lower(district)=:districtName  ");
                }
            } else if (bcDailyCollectionReportResult.getRegion() != null
                    && !bcDailyCollectionReportResult.getRegion().equals("")
                    && !bcDailyCollectionReportResult.getRegion().equalsIgnoreCase(value_ALL))
                if (whereConditionAdded)
                    queryBuilder.append(" and  lower(district) in (:districtNames) ");
                else {
                    whereConditionAdded = addWhereCondition(whereConditionAdded, queryBuilder);
                    queryBuilder.append("   lower(district) in (:districtNames) ");
                }

            if (bcDailyCollectionReportResult.getType() != null && !bcDailyCollectionReportResult.getType().equals("")
                    && !bcDailyCollectionReportResult.getType().equalsIgnoreCase(value_ALL))
                if (whereConditionAdded)
                    queryBuilder.append(" and type =:typeOfSearch ");
                else {
                    whereConditionAdded = addWhereCondition(whereConditionAdded, queryBuilder);
                    queryBuilder.append(" type =:typeOfSearch ");
                }

        }
        queryBuilder
                .append(" group by district,ulbname ,ulbcode  ,collectorname,mobilenumber  order by district,ulbname,collectorname ");
        final Query query = propPerServ.getSession().createSQLQuery(queryBuilder.toString());
        if (bcDailyCollectionReportResult != null) {
            if (bcDailyCollectionReportResult.getCity() != null && !bcDailyCollectionReportResult.getCity().equals("")
                    && !bcDailyCollectionReportResult.getCity().equalsIgnoreCase(value_ALL))
                query.setString("cityName", bcDailyCollectionReportResult.getCity().toLowerCase());
            else if (bcDailyCollectionReportResult.getDistrict() != null
                    && !bcDailyCollectionReportResult.getDistrict().equals("")
                    && !bcDailyCollectionReportResult.getDistrict().equalsIgnoreCase(value_ALL))
                query.setString("districtName", bcDailyCollectionReportResult.getDistrict().toLowerCase());
            else if (bcDailyCollectionReportResult.getRegion() != null
                    && !bcDailyCollectionReportResult.getRegion().equals("")
                    && !bcDailyCollectionReportResult.getRegion().equalsIgnoreCase(value_ALL)) {
                final LinkedList<String> districtlist = new LinkedList<>();
                if (regionalHeirarchyService != null) {
                    final List<RegionalHeirarchy> regions = regionalHeirarchyService
                            .getActiveChildRegionHeirarchyByPassingParentNameAndType(RegionalHeirarchyType.DISTRICT,
                                    bcDailyCollectionReportResult.getRegion());
                    if (regions != null && !regions.isEmpty()) {
                        for (final RegionalHeirarchy regiion : regions)
                            districtlist.add(regiion.getName().toLowerCase());
                        query.setParameterList("districtNames", districtlist);
                    }

                }

            }

            if (bcDailyCollectionReportResult.getType() != null && !bcDailyCollectionReportResult.getType().equals("")
                    && !bcDailyCollectionReportResult.getType().equalsIgnoreCase(value_ALL))
                query.setString("typeOfSearch", bcDailyCollectionReportResult.getType());
        }

        query.setResultTransformer(new AliasToBeanResultTransformer(BillCollectorDailyCollectionReportResult.class));

        listBcPayment = query.list();

        if (financialYearDAO != null && !listBcPayment.isEmpty()) {

            final CFinancialYear currentFinancialYear = financialYearDAO.getFinancialYearByDate(new Date());
            if (currentFinancialYear != null)
                noofDays = DateUtils.daysBetween(new Date(), currentFinancialYear.getEndingDate());
        }
        buildCollectionReport(listBcPayment, noofDays);
        return listBcPayment;

    }

    private boolean addWhereCondition(boolean conditionTocheckAlreadyAdded, final StringBuilder queryBuilder) {
        if (!conditionTocheckAlreadyAdded) {
            queryBuilder.append(" where ");
            conditionTocheckAlreadyAdded = true;
        }
        return conditionTocheckAlreadyAdded;
    }

    @ReadOnly
    public List<BillCollectorDailyCollectionReportResult> getUlbWiseDailyCollection(final Date date) {

        List<BillCollectorDailyCollectionReportResult> listBcPayment = new ArrayList<BillCollectorDailyCollectionReportResult>(
                0);
        int noofDays = 0;
        final StringBuilder queryBuilder = new StringBuilder(

                " select distinct district,ulbname \"ulbName\" ,ulbcode \"ulbCode\"  ,  collectorname \"collectorname\" ,mobilenumber \"mobilenumber\",  "
                        + "target_arrears_demand,target_current_demand,today_arrears_collection,today_currentyear_collection,   "
                        + "cummulative_arrears_collection,cummulative_currentyear_collection,lastyear_collection,lastyear_cummulative_collection  "
                        + "from " + environmentSettings.statewideSchemaName()
                        + ".ulbWise_DialyCollection_view  order by district,ulbname ");
        final Query query = propPerServ.getSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(BillCollectorDailyCollectionReportResult.class));

        listBcPayment = query.list();

        if (financialYearDAO != null && !listBcPayment.isEmpty()) {

            final CFinancialYear currentFinancialYear = financialYearDAO.getFinancialYearByDate(new Date());
            if (currentFinancialYear != null)
                noofDays = DateUtils.daysBetween(new Date(), currentFinancialYear.getEndingDate());
        }
        buildCollectionReport(listBcPayment, noofDays);
        return listBcPayment;

    }

    private void buildCollectionReport(final List<BillCollectorDailyCollectionReportResult> listBcPayment, final int noofDays) {
        for (final BillCollectorDailyCollectionReportResult bcResult : listBcPayment) {

            if (bcResult.getTarget_arrears_demand() == null)
                bcResult.setTarget_arrears_demand(0.0);
            if (bcResult.getTarget_current_demand() == null)
                bcResult.setTarget_current_demand(0.0);

            bcResult.setTarget_total_demand(bcResult.getTarget_arrears_demand() + bcResult.getTarget_current_demand());

            if (bcResult.getToday_arrears_collection() == null)
                bcResult.setToday_arrears_collection(0.0);
            if (bcResult.getToday_currentyear_collection() == null)
                bcResult.setToday_currentyear_collection(0.0);

            bcResult.setToday_total_collection(bcResult.getToday_arrears_collection()
                    + bcResult.getToday_currentyear_collection());

            if (bcResult.getCummulative_arrears_collection() == null)
                bcResult.setCummulative_arrears_collection(0.0);
            if (bcResult.getCummulative_currentyear_collection() == null)
                bcResult.setCummulative_currentyear_collection(0.0);

            bcResult.setCummulative_total_Collection(bcResult.getCummulative_arrears_collection()
                    + bcResult.getCummulative_currentyear_collection());

            if (noofDays > 0)
                bcResult.setDay_target(BigDecimal
                        .valueOf(bcResult.getTarget_total_demand() - bcResult.getCummulative_total_Collection())
                        .divide(BigDecimal.valueOf(noofDays), 4, RoundingMode.HALF_UP)
                        .setScale(2, RoundingMode.HALF_UP));
            else
                bcResult.setDay_target(ZERO);

            if (bcResult.getCummulative_total_Collection() > 0)
                bcResult.setCummulative_currentYear_Percentage(BigDecimal.valueOf(bcResult
                        .getCummulative_total_Collection()).divide(
                                BigDecimal.valueOf(bcResult.getTarget_total_demand()), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP));

            if (bcResult.getLastyear_collection() == null)
                bcResult.setLastyear_collection(0.0);
            else
                bcResult.setLastyear_collection((double) Math.round(bcResult.getLastyear_collection()));

            if (bcResult.getLastyear_cummulative_collection() == null)
                bcResult.setLastyear_cummulative_collection(0.0);
            else
                bcResult.setLastyear_cummulative_collection((double) Math.round(bcResult
                        .getLastyear_cummulative_collection()));
            bcResult.setPercentage_compareWithLastYear(bcResult.getCummulative_total_Collection()
                    - bcResult.getLastyear_cummulative_collection());

            if (bcResult.getLastyear_cummulative_collection() > 0)
                bcResult.setGrowth(BigDecimal.valueOf(bcResult.getCummulative_total_Collection()
                        - bcResult.getLastyear_cummulative_collection()).divide(
                                BigDecimal.valueOf(bcResult.getLastyear_cummulative_collection()), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP));
            else
                bcResult.setGrowth(ZERO);
        }

        for (final BillCollectorDailyCollectionReportResult bcResult : listBcPayment) {

            bcResult.setTarget_arrears_demand(formatAmt(bcResult.getTarget_arrears_demand()).doubleValue());
            bcResult.setTarget_current_demand(formatAmt(bcResult.getTarget_current_demand()).doubleValue());
            bcResult.setTarget_total_demand(formatAmt(bcResult.getTarget_total_demand()).doubleValue());
            bcResult.setDay_target(formatAmt(bcResult.getDay_target().doubleValue()));
            bcResult.setToday_total_collection(formatAmt(bcResult.getToday_total_collection()).doubleValue());
            bcResult.setCummulative_arrears_collection(formatAmt(bcResult.getCummulative_arrears_collection())
                    .doubleValue());
            bcResult.setCummulative_currentyear_collection(formatAmt(bcResult.getCummulative_currentyear_collection())
                    .doubleValue());
            bcResult.setCummulative_total_Collection(formatAmt(bcResult.getCummulative_total_Collection())
                    .doubleValue());
            bcResult.setPercentage_compareWithLastYear(formatAmt(bcResult.getPercentage_compareWithLastYear())
                    .doubleValue());
            bcResult.setLastyear_collection(formatAmt(bcResult.getLastyear_collection()).doubleValue());
            bcResult.setLastyear_cummulative_collection(formatAmt(bcResult.getLastyear_cummulative_collection())
                    .doubleValue());
        }
    }

    public BigDecimal formatAmt(final double amt) {
        return BigDecimal.valueOf(amt / 1000).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @ReadOnly
    public List<BillCollectorDailyCollectionReportResult> getUlbWiseDcbCollection(final Date date,
            final BillCollectorDailyCollectionReportResult bcDailyCollectionReportResult) {

        boolean whereConditionAdded = false;
        List<BillCollectorDailyCollectionReportResult> listBcPayment = new ArrayList<BillCollectorDailyCollectionReportResult>(
                0);
        final StringBuilder queryBuilder = new StringBuilder(
                " select distinct district,ulbname  \"ulbName\" ,ulbcode \"ulbCode\",collectorname,mobilenumber ,sum(totalaccessments) \"totalaccessments\" , sum(current_demand) \"current_demand\", sum(arrears_demand) \"arrears_demand\", sum(current_demand_collection) \"current_demand_collection\" ,sum(arrears_demand_collection) \"arrears_demand_collection\" , sum(current_penalty) \"current_penalty\", sum(arrears_penalty) \"arrears_penalty\"  , sum(current_penalty_collection) \"current_penalty_collection\"  , sum(arrears_penalty_collection) \"arrears_penalty_collection\"  "
                        + "from " + environmentSettings.statewideSchemaName() + ".ulbWise_DCBCollection_view ");

        final String valueAll = "ALL";

        if (bcDailyCollectionReportResult != null) {
            if (bcDailyCollectionReportResult.getCity() != null && !bcDailyCollectionReportResult.getCity().equals("")
                    && !bcDailyCollectionReportResult.getCity().equalsIgnoreCase(valueAll)) {
                whereConditionAdded = addWhereCondition(whereConditionAdded, queryBuilder);
                queryBuilder.append("  lower(ulbname)=:cityName  ");
            } else if (bcDailyCollectionReportResult.getDistrict() != null
                    && !bcDailyCollectionReportResult.getDistrict().equals("")
                    && !bcDailyCollectionReportResult.getDistrict().equalsIgnoreCase(valueAll)) {
                if (whereConditionAdded)
                    queryBuilder.append(" and  lower(district)=:districtName ");
                else {
                    whereConditionAdded = addWhereCondition(whereConditionAdded, queryBuilder);
                    queryBuilder.append("  lower(district)=:districtName  ");
                }
            } else if (bcDailyCollectionReportResult.getRegion() != null
                    && !bcDailyCollectionReportResult.getRegion().equals("")
                    && !bcDailyCollectionReportResult.getRegion().equalsIgnoreCase(valueAll))
                if (whereConditionAdded)
                    queryBuilder.append(" and  lower(district) in (:districtNames) ");
                else {
                    whereConditionAdded = addWhereCondition(whereConditionAdded, queryBuilder);
                    queryBuilder.append("   lower(district) in (:districtNames) ");
                }

            if (bcDailyCollectionReportResult.getType() != null && !bcDailyCollectionReportResult.getType().equals("")
                    && !bcDailyCollectionReportResult.getType().equalsIgnoreCase(valueAll))
                if (whereConditionAdded)
                    queryBuilder.append(" and category in (:typeOfSearch) ");
                else {
                    whereConditionAdded = addWhereCondition(whereConditionAdded, queryBuilder);
                    queryBuilder.append(" category in (:typeOfSearch) ");
                }

        }
        queryBuilder
                .append(" group by district,ulbname ,ulbcode  ,collectorname,mobilenumber  order by district,ulbname,collectorname ");
        final Query query = propPerServ.getSession().createSQLQuery(queryBuilder.toString());
        if (bcDailyCollectionReportResult != null) {
            if (bcDailyCollectionReportResult.getCity() != null && !bcDailyCollectionReportResult.getCity().equals("")
                    && !bcDailyCollectionReportResult.getCity().equalsIgnoreCase(valueAll))
                query.setString("cityName", bcDailyCollectionReportResult.getCity().toLowerCase());
            else if (bcDailyCollectionReportResult.getDistrict() != null
                    && !bcDailyCollectionReportResult.getDistrict().equals("")
                    && !bcDailyCollectionReportResult.getDistrict().equalsIgnoreCase(valueAll))
                query.setString("districtName", bcDailyCollectionReportResult.getDistrict().toLowerCase());
            else if (bcDailyCollectionReportResult.getRegion() != null
                    && !bcDailyCollectionReportResult.getRegion().equals("")
                    && !bcDailyCollectionReportResult.getRegion().equalsIgnoreCase(valueAll)) {
                final LinkedList<String> districtlist = new LinkedList<>();
                if (regionalHeirarchyService != null) {
                    final List<RegionalHeirarchy> regions = regionalHeirarchyService
                            .getActiveChildRegionHeirarchyByPassingParentNameAndType(RegionalHeirarchyType.DISTRICT,
                                    bcDailyCollectionReportResult.getRegion());
                    if (regions != null && !regions.isEmpty()) {
                        for (final RegionalHeirarchy regiion : regions)
                            districtlist.add(regiion.getName().toLowerCase());
                        query.setParameterList("districtNames", districtlist);
                    }

                }

            }

            if (bcDailyCollectionReportResult.getType() != null && !bcDailyCollectionReportResult.getType().equals("")
                    && !bcDailyCollectionReportResult.getType().equalsIgnoreCase(valueAll))
                query.setParameterList("typeOfSearch", prepareTypeOfSearch(bcDailyCollectionReportResult.getType()));
        }

        query.setResultTransformer(new AliasToBeanResultTransformer(BillCollectorDailyCollectionReportResult.class));

        listBcPayment = query.list();

        buildCollectionReportForUlbWiseDCb(listBcPayment);
        return listBcPayment;

    }

    private List<String> prepareTypeOfSearch(final String type) {
        final List<String> types = new ArrayList<>();
        if (PRIVATE.equals(type)) {
            types.add(PRIVATE);
            types.add(EWSHS);
            types.add(COURTCASE_PRIVATE);
            types.add(COURTCASE_EWSHS);
        } else if (PRIVATE_EXCLUDE_COURTCASE.equals(type)) {
            types.add(PRIVATE);
            types.add(EWSHS);
        } else if (CENTRAL_GOVT.equals(type)) {
            types.add(CENTRAL_GOVT_50);
            types.add(CENTRAL_GOVT_75);
            types.add(CENTRAL_GOVT_33_5);
            types.add(COURTCASE_CENTRAL_GOVT_50);
            types.add(COURTCASE_CENTRAL_GOVT_75);
            types.add(COURTCASE_CENTRAL_GOVT_33_5);
        } else if (STATE_GOVT.equals(type)) {
            types.add(STATE_GOVT);
            types.add(COURTCASE_STATE_GOVT);
        } else if (COURTCASE.equals(type)) {
            types.add(COURTCASE_PRIVATE);
            types.add(COURTCASE_EWSHS);
            types.add(COURTCASE_CENTRAL_GOVT_50);
            types.add(COURTCASE_CENTRAL_GOVT_75);
            types.add(COURTCASE_CENTRAL_GOVT_33_5);
            types.add(COURTCASE_STATE_GOVT);
        }
        return types;
    }

    private void buildCollectionReportForUlbWiseDCb(final List<BillCollectorDailyCollectionReportResult> listBcPayment) {
        Double percentage = 0.0;
        for (final BillCollectorDailyCollectionReportResult bcResult : listBcPayment) {

            if (bcResult.getArrears_demand() == null)
                bcResult.setArrears_demand(0.0);
            if (bcResult.getCurrent_demand() == null)
                bcResult.setCurrent_demand(0.0);
            if (bcResult.getArrears_penalty() == null)
                bcResult.setArrears_penalty(0.0);
            if (bcResult.getCurrent_penalty() == null)
                bcResult.setCurrent_penalty(0.0);
            if (bcResult.getTotalaccessments() == null)
                bcResult.setTotalaccessments(BigDecimal.valueOf(0));

            bcResult.setTarget_total_demand(bcResult.getArrears_demand() + bcResult.getCurrent_demand());
            bcResult.setTarget_total_demandInterest(bcResult.getArrears_penalty() + bcResult.getCurrent_penalty());

            if (bcResult.getCurrent_demand_collection() == null)
                bcResult.setCurrent_demand_collection(0.0);
            if (bcResult.getArrears_demand_collection() == null)
                bcResult.setArrears_demand_collection(0.0);
            if (bcResult.getCurrent_penalty_collection() == null)
                bcResult.setCurrent_penalty_collection(0.0);
            if (bcResult.getArrears_penalty_collection() == null)
                bcResult.setArrears_penalty_collection(0.0);

            bcResult.setCummulative_total_Collection(bcResult.getCurrent_demand_collection()
                    + bcResult.getArrears_demand_collection());
            bcResult.setCummulative_total_CollectionInterest(bcResult.getCurrent_penalty_collection()
                    + bcResult.getArrears_penalty_collection());

            if (bcResult.getTarget_total_demand() != 0.0) {
                percentage = bcResult.getCummulative_total_Collection() * 100 / bcResult.getTarget_total_demand();
                bcResult.setCummulative_total_CollectionPercentage(BigDecimal.valueOf(percentage.isNaN() ? 0.0 : percentage));
                percentage = (bcResult.getCummulative_total_Collection() + bcResult.getCummulative_total_CollectionInterest())
                        * 100 / bcResult.getTarget_total_demand();
                bcResult.setCummulative_total_CollectionInterestPercentage(
                        BigDecimal.valueOf(percentage.isNaN() ? 0.0 : percentage));
            } else {
                bcResult.setCummulative_total_CollectionPercentage(ZERO);
                bcResult.setCummulative_total_CollectionInterestPercentage(ZERO);
            }

            bcResult.setBalance_arrearTax(bcResult.getArrears_demand() - bcResult.getArrears_demand_collection());
            bcResult.setBalance_arrearInterest(bcResult.getArrears_penalty() - bcResult.getArrears_penalty_collection());
            bcResult.setBalance_currentTax(bcResult.getCurrent_demand() - bcResult.getCurrent_demand_collection());
            bcResult.setBalance_currentInterest(bcResult.getCurrent_penalty()
                    - bcResult.getCurrent_penalty_collection());
            bcResult.setBalance_total(bcResult.getTarget_total_demand() - bcResult.getCummulative_total_Collection());
            bcResult.setBalance_totalInterest(
                    bcResult.getTarget_total_demandInterest() - bcResult.getCummulative_total_CollectionInterest());
        }

        for (final BillCollectorDailyCollectionReportResult bcResult : listBcPayment) {

            bcResult.setBalance_arrearTax(formatAmt(bcResult.getBalance_arrearTax()).doubleValue());
            bcResult.setBalance_arrearInterest(formatAmt(bcResult.getBalance_arrearInterest()).doubleValue());
            bcResult.setBalance_currentTax(formatAmt(bcResult.getBalance_currentTax()).doubleValue());
            bcResult.setBalance_currentInterest(formatAmt(bcResult.getBalance_currentInterest()).doubleValue());
            bcResult.setBalance_total(formatAmt(bcResult.getBalance_total()).doubleValue());
            bcResult.setBalance_totalInterest(formatAmt(bcResult.getBalance_totalInterest()).doubleValue());

            bcResult.setArrears_demand(formatAmt(bcResult.getArrears_demand()).doubleValue());
            bcResult.setArrears_demand_collection(formatAmt(bcResult.getArrears_demand_collection()).doubleValue());
            bcResult.setArrears_penalty(formatAmt(bcResult.getArrears_penalty()).doubleValue());
            bcResult.setArrears_penalty_collection(formatAmt(bcResult.getArrears_penalty_collection()).doubleValue());

            bcResult.setCurrent_demand(formatAmt(bcResult.getCurrent_demand()).doubleValue());
            bcResult.setCurrent_demand_collection(formatAmt(bcResult.getCurrent_demand_collection()).doubleValue());
            bcResult.setCurrent_penalty(formatAmt(bcResult.getCurrent_penalty()).doubleValue());
            bcResult.setCurrent_penalty_collection(formatAmt(bcResult.getCurrent_penalty_collection()).doubleValue());
            bcResult.setTarget_total_demand(formatAmt(bcResult.getTarget_total_demand()).doubleValue());
            bcResult.setCummulative_total_Collection(formatAmt(bcResult.getCummulative_total_Collection())
                    .doubleValue());
            bcResult.setTarget_total_demandInterest(formatAmt(bcResult.getTarget_total_demandInterest()).doubleValue());
            bcResult.setCummulative_total_CollectionInterest(formatAmt(bcResult.getCummulative_total_CollectionInterest())
                    .doubleValue());
            bcResult.setCummulative_total_CollectionPercentage(
                    bcResult.getCummulative_total_CollectionPercentage().setScale(2, BigDecimal.ROUND_HALF_EVEN));
            bcResult.setCummulative_total_CollectionInterestPercentage(
                    bcResult.getCummulative_total_CollectionInterestPercentage().setScale(2, BigDecimal.ROUND_HALF_EVEN));
        }

    }

    /**
     * @param boundaryId, mode, courtCase, propertyTypes
     * @return @ Description - Returns query that retrieves zone/ward/block/propertywise Arrear, Current Demand and Collection
     * Details
     */
    @ReadOnly
    public SQLQuery prepareQueryForDCBReport(final Long boundaryId, final String mode, final Boolean courtCase,
            final List<String> propertyTypes) {

        final String WARDWISE = "ward";
        final String BLOCKWISE = BLOCK;
        final String PROPERTY = "property";

        final StringBuffer queryStr = new StringBuffer("");
        String commonFromQry = "", finalCommonQry = "", finalSelectQry = "", finalGrpQry = "", boundaryQry = "", whereQry = "",
                propertyTypeIds = "", courtCaseTable = "", courtCaseQry = "";
        Long param = null;

        if (propertyTypes != null && !propertyTypes.isEmpty()) {
            propertyTypeIds = propertyTypes.get(0);
            for (int i = 1; i < propertyTypes.size(); i++)
                propertyTypeIds += "," + propertyTypes.get(i);
        }

        if (courtCase) {
            courtCaseTable = ",egpt_courtcases cc ";
            courtCaseQry = " and cc.assessmentno = pi.upicno";
        } else
            courtCaseQry = " and not exists (select 1 from egpt_courtcases cc where pi.upicno = cc.assessmentno )";

        if (boundaryId != -1 && boundaryId != null)
            param = boundaryId;

        commonFromQry = " from egpt_mv_propertyinfo pi ";
        if (!mode.equalsIgnoreCase(PROPERTY))
            commonFromQry = commonFromQry + ", eg_boundary boundary ";
        commonFromQry = commonFromQry + courtCaseTable + " where pi.isactive = true and pi.isexempted = false " + courtCaseQry;

        finalCommonQry = " cast(sum(pi.waivedoff_amount) as numeric) as \"waivedOffPT\","
                + "cast(sum(pi.ARREAR_DEMAND) as numeric) as \"dmnd_arrearPT\","
                + " cast(sum(pi.pen_aggr_arrear_demand) AS numeric) as \"dmnd_arrearPFT\", cast(sum(pi.annualdemand) AS numeric) as \"dmnd_currentPT\", "
                + " cast(sum(pi.pen_aggr_current_firsthalf_demand) + sum(pi.pen_aggr_current_secondhalf_demand) AS numeric) as \"dmnd_currentPFT\","
                + " cast(sum(pi.ARREAR_COLLECTION) AS numeric) as \"clctn_arrearPT\", cast(sum(pi.pen_aggr_arr_coll) AS numeric) as \"clctn_arrearPFT\","
                + " cast(sum(pi.annualcoll) AS numeric) as \"clctn_currentPT\","
                + " cast(sum(pi.pen_aggr_current_firsthalf_coll) + sum(pi.pen_aggr_current_secondhalf_coll) AS numeric) as \"clctn_currentPFT\"  ";

        // Conditions to Retrieve data based on selected boundary types
        if (!mode.equalsIgnoreCase(PROPERTY)) {
            finalSelectQry = "select count(distinct pi.upicno) as \"assessmentCount\",cast(boundary.id as integer) as \"boundaryId\",boundary.name as \"boundaryName\", ";
            finalGrpQry = " group by boundary.id,boundary.name order by boundary.name";
        }
        if (propertyTypes == null)
            whereQry = whereQry
                    + " and pi.proptymaster not in (select id from egpt_property_type_master where code = 'VAC_LAND') ";
        if (mode.equalsIgnoreCase(WARDWISE)) {
            if (param != 0)
                whereQry = whereQry + " and pi.WARDID = " + param;
            if (propertyTypes != null && !propertyTypes.isEmpty())
                whereQry = whereQry + AND_PI_PROPTYMASTER_IN + propertyTypeIds + ") ";
            boundaryQry = " and pi.wardid=boundary.id ";
        } else if (mode.equalsIgnoreCase(BLOCKWISE)) {
            whereQry = whereQry + " and pi.wardid = " + param;
            if (propertyTypes != null && !propertyTypes.isEmpty())
                whereQry = whereQry + AND_PI_PROPTYMASTER_IN + propertyTypeIds + ") ";
            boundaryQry = " and pi.blockid=boundary.id and pi.wardid = boundary.parent ";
        } else if (mode.equalsIgnoreCase(PROPERTY)) {
            finalSelectQry = "select distinct pi.upicno as \"assessmentNo\", pi.houseno as \"houseNo\", pi.ownersname as \"ownerName\", ";
            whereQry = whereQry + " and pi.blockid = " + param;
            if (propertyTypes != null && !propertyTypes.isEmpty())
                whereQry = whereQry + AND_PI_PROPTYMASTER_IN + propertyTypeIds + ") ";
            boundaryQry = " and pi.wardid = ( select parent from eg_boundary where id = " + param + " ) ";
            finalGrpQry = " group by pi.upicno, pi.houseno, pi.ownersname order by pi.upicno ";
        }

        // Final Query : Retrieves arrear and current data for the selected boundary.
        queryStr.append(finalSelectQry).append(finalCommonQry).append(commonFromQry).append(whereQry)
                .append(boundaryQry).append(finalGrpQry);

        return propPerServ.getSession().createSQLQuery(queryStr.toString());
    }

    /**
     * This method gives the defaulters information
     * @param propertyViewList
     * @return list
     */
    @ReadOnly
    public List<DefaultersInfo> getDefaultersInformation(final Query query, final String noofyrs, final Integer limit) {
        final List<DefaultersInfo> defaultersList = new ArrayList<>();
        final List<DefaultersInfo> defaultersListForYrs = new ArrayList<>();
        DefaultersInfo defaultersInfo;
        BigDecimal totalDue;
        BigDecimal currPenalty;
        BigDecimal currPenaltyColl;
        int count = 1;

        int reqyr = 0;
        final List<PropertyMaterlizeView> propertyViewList = query.list();

        for (final PropertyMaterlizeView propView : propertyViewList) {

            if (isCountInLimit(limit, count))
                break;

            defaultersInfo = getInstDmdInfo(propView);

            defaultersInfo.setSlNo(count);
            defaultersInfo.setAssessmentNo(propView.getPropertyId());
            defaultersInfo.setOwnerName(getOwerName(propView));
            defaultersInfo.setWardName(propView.getWard().getName());
            defaultersInfo.setHouseNo(propView.getHouseNo());
            defaultersInfo.setLocality(getLocality(propView));
            defaultersInfo.setMobileNumber(getMobileNo(propView));
            defaultersInfo.setArrearsDue(propView.getAggrArrDmd().subtract(propView.getAggrArrColl()
                    .add(propView.getArrearCourtVerdictAmount()).add(propView.getArrearWriteOffAmount())));
            defaultersInfo.setCurrentDue(propView.getAggrCurrFirstHalfDmd().add(propView.getAggrCurrSecondHalfDmd())
                    .subtract(propView.getAggrCurrFirstHalfColl().add(propView.getAggrCurrSecondHalfColl())
                            .add(propView.getCurrentFirstHalfCourtVerdictAmount())
                            .add(propView.getCurrentSecondHalfCourtVerdictAmount())
                            .add(propView.getCurrentFirstHalfWriteOffAmount())
                            .add(propView.getCurrentSecondHalfWriteOffAmount())));
            defaultersInfo.setAggrArrearPenalyDue(getAggArrPenaltyDue(propView)
                    .subtract(propView.getArrearPenaltyCourtVerdictAmount().add(propView.getArrearPenaltyWriteOffAmount())));
            currPenalty = getAggCurrFirstHalfPenalty(propView)
                    .add(getAggCurrSecHalfPenalty(propView));
            currPenaltyColl = getAggCurrFirstHalfPenColl(propView)
                    .add(getAggCurrSecHalfPenColl(propView));
            defaultersInfo.setAggrCurrPenalyDue(currPenalty.subtract(currPenaltyColl
                    .add(propView.getCurrentPenaltyCourtVerdictAmount()).add(propView.getCurrentPenaltyWriteOffAmount())));
            totalDue = defaultersInfo.getArrearsDue().add(defaultersInfo.getCurrentDue())
                    .add(defaultersInfo.getAggrArrearPenalyDue()).add(defaultersInfo.getAggrCurrPenalyDue());
            defaultersInfo.setTotalDue(totalDue);
            int yrs = 0;

            if (isNotMoreThanFiveYrs(noofyrs)) {
                reqyr = Integer.parseInt(noofyrs.substring(0, 1));
                yrs = propertyTaxUtil.getNoOfYears(defaultersInfo.getMinDate(), defaultersInfo.getMaxDate());

            }
            if (isNotMoreThanFiveYrs(noofyrs) && reqyr >= yrs) {
                defaultersListForYrs.add(defaultersInfo);
                count++;
            } else if (noofyrs == null || noofyrs != null && ABOVE_FIVE_YEARS.equalsIgnoreCase(noofyrs)) {
                defaultersList.add(defaultersInfo);
                count++;
            }
        }

        return defaultersListForYrs.isEmpty() ? defaultersList : defaultersListForYrs;
    }

    @ReadOnly
    private DefaultersInfo getInstDmdInfo(final PropertyMaterlizeView propView) {
        final DefaultersInfo defaultersInfo = new DefaultersInfo();

        Iterator itr;
        InstDmdCollMaterializeView idc;
        if (!propView.getInstDmdColl().isEmpty()) {
            itr = propView.getInstDmdColl().iterator();
            Installment minInstallment = null;
            Installment maxInstallment = null;
            while (itr.hasNext()) {
                BigDecimal dmdtot;
                BigDecimal colltot;
                idc = (InstDmdCollMaterializeView) itr.next();
                dmdtot = getGenTax(idc).add(getEduCess(idc)).add(getLibCess(idc)).add(getPenaltyFines(idc))
                        .add(getPubSerCharge(idc)).add(getSewTax(idc)).add(getUnaPenalty(idc)).add(getVacLandTax(idc));

                colltot = getGenTaxColl(idc).add(getEduCessColl(idc)).add(getLibCessColl(idc)).add(getPenaltyFineColl(idc))
                        .add(getPubServiceColl(idc)).add(getSewColl(idc)).add(getUnauthPenColl(idc)).add(getVacLColl(idc));

                minInstallment = getMinInstallment(minInstallment, idc, dmdtot, colltot);
                maxInstallment = getMaxInstallment(maxInstallment, idc, dmdtot, colltot);

            }
            if (minInstallment != null) {
                defaultersInfo.setMinDate(minInstallment.getFromDate());
                defaultersInfo.setArrearsFrmInstallment(minInstallment.getDescription());
            }
            if (maxInstallment != null) {
                defaultersInfo.setMaxDate(maxInstallment.getFromDate());
                defaultersInfo.setArrearsToInstallment(maxInstallment.getDescription());
            }
        }
        return defaultersInfo;
    }

    /**
     * @param zoneId
     * @param wardId
     * @param areaId
     * @param localityId
     * @return
     */
    @ReadOnly
    public List<PropertyMaterlizeView> prepareQueryforArrearRegisterReport(final Long zoneId, final Long wardId,
            final Long areaId, final Long localityId) {
        // Get current installment
        final Installment currentInst = propertyTaxCommonUtils.getCurrentInstallment();
        final StringBuffer query = new StringBuffer(300);
        // Query that retrieves all the properties that has arrears.
        query.append("select distinct pmv from PropertyMaterlizeView pmv,InstDmdCollMaterializeView idc where "
                + "pmv.basicPropertyID = idc.propMatView.basicPropertyID and pmv.isActive = true and idc.installment.fromDate not between  ('"
                + currentInst.getFromDate() + "') and ('" + currentInst.getToDate() + "') ");
        if (propertyTaxUtil.isWard(localityId))
            query.append(" and pmv.locality.id= :localityId ");
        if (propertyTaxUtil.isWard(zoneId))
            query.append(" and pmv.zone.id= :zoneId ");
        if (propertyTaxUtil.isWard(wardId))
            query.append("  and pmv.ward.id= :wardId ");
        if (propertyTaxUtil.isWard(areaId))
            query.append("  and pmv.block.id= :areaId ");
        query.append(" order by pmv.basicPropertyID ");
        final Query qry = propPerServ.getSession().createQuery(query.toString());
        if (propertyTaxUtil.isWard(localityId))
            qry.setParameter("localityId", localityId);
        if (propertyTaxUtil.isWard(zoneId))
            qry.setParameter("zoneId", zoneId);
        if (propertyTaxUtil.isWard(wardId))
            qry.setParameter("wardId", wardId);
        if (propertyTaxUtil.isWard(areaId))
            qry.setParameter("areaId", areaId);
        @SuppressWarnings("unchecked")
        final List<PropertyMaterlizeView> propertyViewList = qry.setResultTransformer(
                CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();
        return propertyViewList;
    }

    @SuppressWarnings("unchecked")
    @ReadOnly
    public List<CollectionSummary> getCollectionSummaryList(final String fromDate, final String toDate,
            final String collMode, final String transMode, final String mode, final String boundaryId,
            final String propTypeCategoryId, final Long zoneId, final Long wardId, final Long blockId) {
        try {
            final Query query = propertyTaxUtil.prepareQueryforCollectionSummaryReport(fromDate, toDate, collMode, transMode,
                    mode,
                    boundaryId,
                    propTypeCategoryId, zoneId, wardId, blockId);
            return query.list();
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Error occured in Class : CollectionSummaryReportAction  Method : list "
                    + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @ReadOnly
    public List<PropertyMutation> getTitleTransferReportList(final Query query) {
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @ReadOnly
    public List<NatureOfUsageResult> getNatureOfUsageReportList(final HttpServletRequest request) {
        final StringBuilder query = new StringBuilder();
        query.append(
                "select distinct pi.upicno \"assessmentNumber\", pi.ownersname \"ownerName\", pi.mobileno \"mobileNumber\", pi.houseno \"doorNumber\", pi.address \"address\", cast((pi.AGGREGATE_CURRENT_FIRSTHALF_DEMAND + pi.AGGREGATE_CURRENT_SECONDHALF_DEMAND) as numeric) \"fullYearTax\", fd.natureofusage \"usageName\" from egpt_mv_propertyInfo pi ");
        final StringBuilder whereQuery = new StringBuilder(" where pi.upicno is not null and pi.isactive = true ");
        final String natureOfUsage = request.getParameter("natureOfUsage");
        final String ward = request.getParameter("ward");
        final String block = request.getParameter(BLOCK);
        final StringBuilder srchCriteria = new StringBuilder("Total number of properties with");
        final Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(natureOfUsage) && !"-1".equals(natureOfUsage)) {
            final PropertyUsage propertyUsage = propertyUsageService.findById(Long.valueOf(natureOfUsage));
            srchCriteria.append(" Nature of usage : " + propertyUsage.getUsageName());
            query.append(",EGPT_MV_CURRENT_FLOOR_DETAIL fd ");
            whereQuery.append(" and fd.basicpropertyid = pi.basicpropertyid and fd.natureofusage = :natureOfUsage");
            params.put("natureOfUsage", propertyUsage.getUsageName());
        }
        else {
        	query.append(",EGPT_MV_CURRENT_FLOOR_DETAIL fd ");
            whereQuery.append(" and fd.basicpropertyid = pi.basicpropertyid");
        }
        if (StringUtils.isNotBlank(ward) && !"-1".equals(ward)) {
            final Boundary wardBndry = boundaryService.getBoundaryById(Long.valueOf(ward));
            srchCriteria.append(" Ward : " + wardBndry.getName());
            whereQuery.append(" and pi.wardid = :ward");
            params.put("ward", Long.valueOf(ward));
        }
        if (StringUtils.isNotBlank(block) && !"-1".equals(block)) {
            final Boundary blockBndry = boundaryService.getBoundaryById(Long.valueOf(block));
            srchCriteria.append(" Block : " + blockBndry.getName());
            whereQuery.append(" and pi.blockid = :block");
            params.put(BLOCK, Long.valueOf(block));
        }
        final SQLQuery sqlQuery = propertyTaxCommonUtils.getSession().createSQLQuery(
                query.append(whereQuery).toString());
        for (final String key : params.keySet())
            sqlQuery.setParameter(key, params.get(key));
        sqlQuery.setResultTransformer(Transformers.aliasToBean(NatureOfUsageResult.class));
        final List<NatureOfUsageResult> results = sqlQuery.list();
        srchCriteria.append(" are : " + results.size());
        return results;
    }

    private BigDecimal getAggCurrSecHalfPenColl(final PropertyMaterlizeView propView) {
        return propView.getAggrCurrSecondHalfPenalyColl() != null ? propView
                .getAggrCurrSecondHalfPenalyColl() : ZERO;
    }

    private BigDecimal getAggCurrFirstHalfPenColl(final PropertyMaterlizeView propView) {
        return propView.getAggrCurrFirstHalfPenalyColl() != null ? propView
                .getAggrCurrFirstHalfPenalyColl() : ZERO;
    }

    private BigDecimal getAggCurrSecHalfPenalty(final PropertyMaterlizeView propView) {
        return propView.getAggrCurrSecondHalfPenaly() != null ? propView.getAggrCurrSecondHalfPenaly() : ZERO;
    }

    private BigDecimal getAggCurrFirstHalfPenalty(final PropertyMaterlizeView propView) {
        return propView.getAggrCurrFirstHalfPenaly() != null ? propView.getAggrCurrFirstHalfPenaly() : ZERO;
    }

    private BigDecimal getAggArrPenaltyDue(final PropertyMaterlizeView propView) {
        return (propView.getAggrArrearPenaly() != null ? propView
                .getAggrArrearPenaly() : ZERO).subtract(propView.getAggrArrearPenalyColl() != null ? propView
                        .getAggrArrearPenalyColl() : ZERO);
    }

    private String getOwerName(final PropertyMaterlizeView propView) {
        return propView.getOwnerName() != null ? propView.getOwnerName().contains(",") ? propView.getOwnerName().replace(",",
                " & ") : propView.getOwnerName() : "NA";
    }

    private String getLocality(final PropertyMaterlizeView propView) {
        return propView.getLocality() != null ? propView.getLocality().getName()
                : "NA";
    }

    private String getMobileNo(final PropertyMaterlizeView propView) {
        return StringUtils.isNotBlank(propView.getMobileNumber()) ? propView
                .getMobileNumber() : "NA";
    }

    private boolean isNotMoreThanFiveYrs(final String noofyrs) {
        return noofyrs != null && !ABOVE_FIVE_YEARS.equalsIgnoreCase(noofyrs);
    }

    private boolean isCountInLimit(final Integer limit, final int count) {
        return limit != null && limit != -1 && count - 1 == limit;
    }

    public Installment getMinInstallment(final Installment minInstallment, final InstDmdCollMaterializeView idc,
            final BigDecimal dmdtot, final BigDecimal colltot) {
        Installment inst = null;
        if(dmdtot.compareTo(colltot) > 0){
        if (minInstallment == null)
            return idc.getInstallment();
        else if (minInstallment.getFromDate().after(idc.getInstallment().getFromDate()))
            inst = idc.getInstallment();
        }
        return inst == null ? minInstallment : inst;
    }

    public Installment getMaxInstallment(final Installment maxInstallment, final InstDmdCollMaterializeView idc,
            final BigDecimal dmdtot, final BigDecimal colltot) {
        Installment inst = null;
        if (maxInstallment == null)
            return idc.getInstallment();
        else if (maxInstallment.getFromDate().before(idc.getInstallment().getFromDate()) && dmdtot.compareTo(colltot) > 0)
            inst = idc.getInstallment();
        return inst == null ? maxInstallment : inst;
    }

    @SuppressWarnings("unchecked")
    @ReadOnly
    public List<ApartmentDCBReportResult> prepareQueryForApartmentDCBReport(final Long boundaryId, final String mode,
            final Long apartmentId) {
        final String PROPERTY = "property";
        final StringBuilder queryStr = new StringBuilder();
        final StringBuilder commonFromQry = new StringBuilder();
        final StringBuilder finalCommonQry = new StringBuilder();
        final StringBuilder finalSelectQry = new StringBuilder();
        final StringBuilder finalGrpQry = new StringBuilder();
        final StringBuilder boundaryQry = new StringBuilder();
        final StringBuilder whereQry = new StringBuilder();
        whereQry.append(" where ");
        whereQry.append(
                " pd.apartment=a.id and p.id=pd.id_property and pi.basicpropertyid=p.id_basic_property  and pi.isexempted=false and p.status in ('A','I')  and pi.isactive = true and pi.isexempted = false ");
        commonFromQry.append(" from egpt_mv_propertyinfo pi , egpt_apartment a,egpt_property_detail pd,egpt_property p ");
        if (boundaryId != -1 && boundaryId != null && boundaryId != 0)
            boundaryQry.append(" and pi.wardid = " + boundaryId);
        if (apartmentId != -1 && apartmentId != null && apartmentId != 0)
            whereQry.append(" and pd.apartment = " + apartmentId);
        finalCommonQry.append(" cast(COALESCE(sum(pi.waivedoff_amount),0) as numeric) as \"waivedOffPT\",");
        finalCommonQry.append(" cast(COALESCE(sum(pi.ARREAR_DEMAND),0) as numeric) as \"dmndArrearPT\",");
        finalCommonQry.append(
                " cast(COALESCE(sum(pi.pen_aggr_arrear_demand),0) AS numeric) as \"dmndArrearPFT\", cast(COALESCE(sum(pi.annualdemand),0) AS numeric) as \"dmndCurrentPT\", ");
        finalCommonQry.append(
                " cast(COALESCE(sum(pi.pen_aggr_current_firsthalf_demand),0)+COALESCE(sum(pi.pen_aggr_current_secondhalf_demand),0) AS numeric) as \"dmndCurrentPFT\",");
        finalCommonQry.append(
                " cast(COALESCE(sum(pi.ARREAR_COLLECTION),0) AS numeric) as \"clctnArrearPT\", cast(COALESCE(sum(pi.pen_aggr_arr_coll),0) AS numeric) as \"clctnArrearPFT\",");
        finalCommonQry.append(" cast(COALESCE(sum(pi.annualcoll),0) AS numeric) as \"clctnCurrentPT\",");
        finalCommonQry.append(
                " cast(COALESCE(sum(pi.pen_aggr_current_firsthalf_coll),0)+COALESCE(sum(pi.pen_aggr_current_secondhalf_coll),0) AS numeric) as \"clctnCurrentPFT\"  ");
        if (!mode.equalsIgnoreCase(PROPERTY)) {
            finalSelectQry.append(
                    "select count(distinct pi.upicno) as \"assessmentCount\",cast(a.id as integer) as \"apartmentId\",a.name as \"apartmentName\", ");
            finalGrpQry.append(" group by a.id,a.name order by a.name");
        } else if (mode.equalsIgnoreCase(PROPERTY)) {
            finalSelectQry.append(
                    "select distinct pi.upicno as \"assessmentNo\", pi.houseno as \"houseNo\", pi.ownersname as \"ownerName\", ");
            finalGrpQry.append(" group by pi.upicno, pi.houseno, pi.ownersname order by pi.upicno ");
        }
        queryStr.append(finalSelectQry).append(finalCommonQry).append(commonFromQry).append(whereQry)
                .append(boundaryQry).append(finalGrpQry);
        final SQLQuery sqlQuery = propertyTaxCommonUtils.getSession().createSQLQuery(queryStr.toString());
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ApartmentDCBReportResult.class));
        return sqlQuery.list();
    }

    private BigDecimal getVacLandTax(final InstDmdCollMaterializeView idc) {
        return idc.getVacantLandTax() != null ? idc.getVacantLandTax() : ZERO;
    }

    private BigDecimal getUnaPenalty(final InstDmdCollMaterializeView idc) {
        return idc.getUnauthPenaltyTax() != null ? idc.getUnauthPenaltyTax() : ZERO;
    }

    private BigDecimal getSewTax(final InstDmdCollMaterializeView idc) {
        return idc.getSewTax() != null ? idc.getSewTax() : ZERO;
    }

    private BigDecimal getPubSerCharge(final InstDmdCollMaterializeView idc) {
        return idc.getPubSerChrgTax() != null ? idc.getPubSerChrgTax() : ZERO;
    }

    private BigDecimal getPenaltyFines(final InstDmdCollMaterializeView idc) {
        return idc.getPenaltyFinesTax() != null ? idc.getPenaltyFinesTax() : ZERO;
    }

    private BigDecimal getLibCess(final InstDmdCollMaterializeView idc) {
        return idc.getLibCessTax() != null ? idc.getLibCessTax() : ZERO;
    }

    private BigDecimal getEduCess(final InstDmdCollMaterializeView idc) {
        return idc.getEduCessTax() != null ? idc.getEduCessTax() : ZERO;
    }

    private BigDecimal getGenTax(final InstDmdCollMaterializeView idc) {
        return idc.getGeneralTax() != null ? idc.getGeneralTax() : ZERO;
    }

    private BigDecimal getVacLColl(final InstDmdCollMaterializeView idc) {
        return idc.getVacantLandTaxColl() != null ? idc.getVacantLandTaxColl() : ZERO;
    }

    private BigDecimal getUnauthPenColl(final InstDmdCollMaterializeView idc) {
        return idc.getUnauthPenaltyTaxColl() != null ? idc.getUnauthPenaltyTaxColl() : ZERO;
    }

    private BigDecimal getSewColl(final InstDmdCollMaterializeView idc) {
        return idc.getSewTaxColl() != null ? idc.getSewTaxColl() : ZERO;
    }

    private BigDecimal getPubServiceColl(final InstDmdCollMaterializeView idc) {
        return idc.getPubSerChrgTaxColl() != null ? idc.getPubSerChrgTaxColl() : ZERO;
    }

    private BigDecimal getPenaltyFineColl(final InstDmdCollMaterializeView idc) {
        return idc.getPenaltyFinesTaxColl() != null ? idc.getPenaltyFinesTaxColl() : ZERO;
    }

    private BigDecimal getLibCessColl(final InstDmdCollMaterializeView idc) {
        return idc.getLibCessTaxColl() != null ? idc.getLibCessTaxColl() : ZERO;
    }

    private BigDecimal getEduCessColl(final InstDmdCollMaterializeView idc) {
        return idc.getEduCessTaxColl() != null ? idc.getEduCessTaxColl() : ZERO;
    }

    private BigDecimal getGenTaxColl(final InstDmdCollMaterializeView idc) {
        return idc.getGeneralTaxColl() != null ? idc.getGeneralTaxColl() : ZERO;
    }

}
