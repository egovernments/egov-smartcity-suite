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
package org.egov.ptis.domain.service.report;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.ROLE_COLLECTION_OPERATOR;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.RegionalHeirarchy;
import org.egov.commons.RegionalHeirarchyType;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.service.RegionalHeirarchyService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.infra.utils.DateUtils;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BaseRegisterResult;
import org.egov.ptis.domain.entity.property.BaseRegisterVLTResult;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BillCollectorDailyCollectionReportResult;
import org.egov.ptis.domain.entity.property.CurrentInstDCBReportResult;
import org.egov.ptis.domain.entity.property.DailyCollectionReportResult;
import org.egov.ptis.domain.entity.property.DefaultersInfo;
import org.egov.ptis.domain.entity.property.FloorDetailsView;
import org.egov.ptis.domain.entity.property.InstDmdCollMaterializeView;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class ReportService {

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
    private static final String PMV_QUERY = "select distinct pmv from PropertyMaterlizeView pmv where pmv.isActive = true ";
    private static final String ABOVE_FIVE_YEARS = "Above 5 Years";
    final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    private PersistenceService propPerServ;
    private Properties taxRateProps = null;
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
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     * Method gives List of properties with current and arrear individual demand
     * details
     *
     * @param ward
     * @param block
     * @return
     */
    @ReadOnly
    public List<BaseRegisterResult> getPropertyByWardAndBlock(final String ward, final String block, final boolean exemptedCase) {

        final StringBuilder queryStr = new StringBuilder(500);
        if(exemptedCase){
            queryStr.append(PMV_QUERY);
            queryStr.append(" and pmv.isExempted = true ");
        }
        else
            queryStr.append(PMV_QUERY);
        if (StringUtils.isNotBlank(ward))
            queryStr.append(" and pmv.ward.id=:ward ");
        if (StringUtils.isNotBlank(block))
            queryStr.append(" and pmv.block.id=:block ");
        queryStr.append("and pmv.propTypeMstrID.code<>'VAC_LAND'");
        queryStr.append(" order by pmv.propertyId, pmv.ward");
        final Query query = propPerServ.getSession().createQuery(queryStr.toString());
        if (StringUtils.isNotBlank(ward))
            query.setLong("ward", Long.valueOf(ward));
        if (StringUtils.isNotBlank(block))
            query.setLong("block", Long.valueOf(block));

        List<PropertyMaterlizeView> properties = query.list();
        List<BaseRegisterResult> baseRegisterResultList = new LinkedList<>();
        for (PropertyMaterlizeView propMatView : properties) {
            List<FloorDetailsView> floorDetails = new LinkedList<>(propMatView.getFloorDetails());
            if (floorDetails.size() > 1) {
                addMultipleFloors(baseRegisterResultList, propMatView, floorDetails);
            } else {
                BaseRegisterResult baseRegisterResultObj = new BaseRegisterResult();
                baseRegisterResultObj = addSingleFloor(baseRegisterResultObj, propMatView);
                for (FloorDetailsView floor : floorDetails) {
                    baseRegisterResultObj.setPlinthArea(floor.getBuiltUpArea());
                    baseRegisterResultObj.setPropertyUsage(floor.getPropertyUsage());
                    baseRegisterResultObj.setClassificationOfBuilding(floor.getClassification());
                }
                baseRegisterResultList.add(baseRegisterResultObj);
            }
        }
        return baseRegisterResultList;
    }

    private BaseRegisterResult addSingleFloor(BaseRegisterResult baseRegisterResultObj,
                                              PropertyMaterlizeView propMatView) {
        baseRegisterResultObj.setAssessmentNo(propMatView.getPropertyId());
        baseRegisterResultObj.setDoorNO(propMatView.getHouseNo());
        baseRegisterResultObj.setOwnerName(propMatView.getOwnerName() != null ? (propMatView.getOwnerName().contains(",") ? propMatView.getOwnerName()
                .replace(",", " & ") : propMatView.getOwnerName()) : "");
        baseRegisterResultObj.setIsExempted(propMatView.getIsExempted() != null ? (propMatView.getIsExempted() ? "Yes" : "No") : "No");
        baseRegisterResultObj.setCourtCase("No");

        PropertyTypeMaster propertyType = null;
        if (null != propMatView.getPropTypeMstrID()) {
            propertyType = (PropertyTypeMaster) getPropPerServ().find("from PropertyTypeMaster where id = ?",
                    propMatView.getPropTypeMstrID().getId());
        }

        BigDecimal totalArrearPropertyTax = BigDecimal.ZERO;
        BigDecimal totalArrearEduCess = BigDecimal.ZERO;
        BigDecimal totalArreaLibCess = BigDecimal.ZERO;
        BigDecimal arrearPenaltyFine = BigDecimal.ZERO;
        BigDecimal totalCurrPropertyTax = BigDecimal.ZERO;
        BigDecimal totalCurrEduCess = BigDecimal.ZERO;
        BigDecimal totalCurrLibCess = BigDecimal.ZERO;
        BigDecimal currPenaltyFine = BigDecimal.ZERO;
        BigDecimal arrColl;
        BigDecimal totalColl;
        BigDecimal currColl;

        List<InstDmdCollMaterializeView> instDemandCollList = new LinkedList<>(
                propMatView.getInstDmdColl());
        Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        for (InstDmdCollMaterializeView instDmdCollObj : instDemandCollList) {
            if (instDmdCollObj.getInstallment().equals(currYearInstMap.get(CURRENTYEAR_FIRST_HALF))) {
                totalCurrPropertyTax = totalCurrPropertyTax.add(instDmdCollObj.getGeneralTax() != null ? instDmdCollObj.getGeneralTax() : BigDecimal.ZERO);
                totalCurrEduCess = totalCurrEduCess.add(instDmdCollObj.getEduCessTax() != null ? instDmdCollObj.getEduCessTax() : BigDecimal.ZERO);
                totalCurrLibCess = totalCurrLibCess.add(instDmdCollObj.getLibCessTax() != null ? instDmdCollObj.getLibCessTax() : BigDecimal.ZERO);
                currPenaltyFine = currPenaltyFine.add(instDmdCollObj.getPenaltyFinesTax() != null ? instDmdCollObj.getPenaltyFinesTax() : BigDecimal.ZERO);
            } else if (instDmdCollObj.getInstallment().equals(currYearInstMap.get(CURRENTYEAR_SECOND_HALF))) {
                totalCurrPropertyTax = totalCurrPropertyTax.add(instDmdCollObj.getGeneralTax() != null ? instDmdCollObj.getGeneralTax() : BigDecimal.ZERO);
                totalCurrEduCess = totalCurrEduCess.add(instDmdCollObj.getEduCessTax() != null ? instDmdCollObj.getEduCessTax() : BigDecimal.ZERO);
                totalCurrLibCess = totalCurrLibCess.add(instDmdCollObj.getLibCessTax() != null ? instDmdCollObj.getLibCessTax() : BigDecimal.ZERO);
                currPenaltyFine = currPenaltyFine.add(instDmdCollObj.getPenaltyFinesTax() != null ? instDmdCollObj.getPenaltyFinesTax() : BigDecimal.ZERO);
            } else {
                totalArrearPropertyTax = totalArrearPropertyTax.add(instDmdCollObj.getGeneralTax() != null ? instDmdCollObj.getGeneralTax() : BigDecimal.ZERO);
                totalArrearEduCess = totalArrearEduCess.add(instDmdCollObj.getEduCessTax() != null ? instDmdCollObj.getEduCessTax() : BigDecimal.ZERO);
                totalArreaLibCess = totalArreaLibCess.add(instDmdCollObj.getLibCessTax() != null ? instDmdCollObj.getLibCessTax() : BigDecimal.ZERO);
                arrearPenaltyFine = arrearPenaltyFine.add(instDmdCollObj.getPenaltyFinesTax() != null ? instDmdCollObj.getPenaltyFinesTax() : BigDecimal.ZERO);
            }
        }

        String arrearPerFrom;
        String arrearPerTo;
        if (instDemandCollList.size() > 1) {
            arrearPerTo = dateFormatter.format(DateUtils.add(propertyTaxCommonUtils.getCurrentInstallment().getFromDate(), Calendar.DAY_OF_MONTH, -1));
            arrearPerFrom = dateFormatter.format(instDemandCollList.get(0).getInstallment().getFromDate());
            baseRegisterResultObj.setArrearPeriod(arrearPerFrom + "-" + arrearPerTo);
        } else {
            baseRegisterResultObj.setArrearPeriod("N/A");
        }

        baseRegisterResultObj.setPropertyTax(totalCurrPropertyTax);
        baseRegisterResultObj.setEduCessTax(totalCurrEduCess);
        baseRegisterResultObj.setLibraryCessTax(totalCurrLibCess);
        baseRegisterResultObj.setPenaltyFines(currPenaltyFine);
        baseRegisterResultObj.setCurrTotal(totalCurrPropertyTax.add(totalCurrEduCess).add(totalCurrLibCess));
        baseRegisterResultObj.setArrearTotal(totalArrearPropertyTax.add(totalArrearEduCess).add(totalArreaLibCess));
        baseRegisterResultObj.setArrearPropertyTax(totalArrearPropertyTax);
        baseRegisterResultObj.setArrearLibraryTax(totalArreaLibCess);
        baseRegisterResultObj.setArrearEduCess(totalArrearEduCess);
        baseRegisterResultObj.setArrearPenaltyFines(arrearPenaltyFine);
        baseRegisterResultObj.setPropertyType(propertyType.getCode());

        arrColl = propMatView.getAggrArrColl() != null ? propMatView.getAggrArrColl() : BigDecimal.ZERO;
        baseRegisterResultObj.setArrearColl(arrColl);

        totalColl = arrColl;
        currColl = (propMatView.getAggrCurrFirstHalfColl() != null ? propMatView.getAggrCurrFirstHalfColl() : BigDecimal.ZERO).add(propMatView.getAggrCurrSecondHalfColl() != null ? propMatView.getAggrCurrSecondHalfColl() : BigDecimal.ZERO);
        totalColl = totalColl.add(currColl);
        baseRegisterResultObj.setCurrentColl(currColl);
        baseRegisterResultObj.setTotalColl(totalColl);

        return baseRegisterResultObj;
    }

    private void addMultipleFloors(List<BaseRegisterResult> baseRegisterResultList, PropertyMaterlizeView propMatView,
                                   List<FloorDetailsView> floorDetails) {
        BaseRegisterResult baseRegisterResultObj;
        int count = 0;
        for (FloorDetailsView floorview : floorDetails) {
            if (count == 0) {
                baseRegisterResultObj = new BaseRegisterResult();
                baseRegisterResultObj = addSingleFloor(baseRegisterResultObj, propMatView);
                baseRegisterResultObj.setPlinthArea(floorview.getBuiltUpArea());
                baseRegisterResultObj.setPropertyUsage(floorview.getPropertyUsage() != null ? (floorview.getPropertyUsage().contains(",") ? floorview
                        .getPropertyUsage().replace(",", " & ") : floorview.getPropertyUsage()) : "");
                baseRegisterResultObj.setClassificationOfBuilding(floorview.getClassification());
                count++;
            } else {
                baseRegisterResultObj = new BaseRegisterResult();
                baseRegisterResultObj.setAssessmentNo("");
                baseRegisterResultObj.setOwnerName("");
                baseRegisterResultObj.setDoorNO("");
                baseRegisterResultObj.setCourtCase("");
                baseRegisterResultObj.setArrearPeriod("");
                baseRegisterResultObj.setPlinthArea(floorview.getBuiltUpArea());
                baseRegisterResultObj.setPropertyUsage(floorview.getPropertyUsage() != null ? (floorview.getPropertyUsage().contains(",") ? floorview
                        .getPropertyUsage().replace(",", " & ") : floorview.getPropertyUsage()) : "");
                baseRegisterResultObj.setClassificationOfBuilding(floorview.getClassification());
            }
            baseRegisterResultList.add(baseRegisterResultObj);
        }

    }
    
    @ReadOnly
    public List<DailyCollectionReportResult> getCollectionDetails(Date fromDate, Date toDate, String collectionMode,
                                                                  String collectionOperator, String status, String ward) throws ParseException {
        final StringBuilder queryStr = new StringBuilder(500);
        final SimpleDateFormat fromDateFormatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        final SimpleDateFormat toDateFormatter = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        queryStr.append(" from CollectionIndex collectionIndex,BasicPropertyImpl basicproperty where collectionIndex.billingService =:service and collectionIndex.consumerCode = basicproperty.upicNo and "
                + " (collectionIndex.receiptDate between to_timestamp('"
                + fromDateFormatter.format(fromDate)
                + "', 'YYYY-MM-DD HH24:MI:SS') and "
                + " to_timestamp('"
                + toDateFormatter.format(toDate)
                + "', 'YYYY-MM-DD HH24:MI:SS')) ");
        if (StringUtils.isNotBlank(collectionMode)) {
            queryStr.append(" and collectionIndex.channel =:mode ");
        }
        if (StringUtils.isNotBlank(collectionOperator)) {
            queryStr.append(" and collectionIndex.createdBy.id =:operator ");
        }
        if (StringUtils.isNotBlank(status)) {
            queryStr.append(" and collectionIndex.status =:status ");
        }
        if (StringUtils.isNotBlank(ward)) {
            queryStr.append(" and basicproperty.propertyID.ward.id =:ward");
        }
        queryStr.append(" order by collectionIndex.receiptDate ");
        final Query query = propPerServ.getSession().createQuery(queryStr.toString());
        query.setString("service", PTMODULENAME);
        if (StringUtils.isNotBlank(collectionMode)) {
            query.setString("mode", collectionMode);
        }
        if (StringUtils.isNotBlank(collectionOperator)) {
            query.setLong("operator", Long.valueOf(collectionOperator));
        }
        if (StringUtils.isNotBlank(status)) {
            query.setString("status", status);
        }
        if (StringUtils.isNotBlank(ward)) {
            query.setLong("ward", Long.valueOf(ward));
        }
        List<Object> objectList = query.list();
        List<DailyCollectionReportResult> dailyCollectionReportList = new ArrayList<DailyCollectionReportResult>();
        DailyCollectionReportResult result = null;
        BigDecimal arrLibCess = null;
        BigDecimal currLibCess = null;
        BigDecimal rebateAmount = null;

       /* for (Object objects : objectList) {
            final Object[] object = (Object[]) objects;
            CollectionIndex collectionIndex = (CollectionIndex) object[0];
            BasicPropertyImpl basicProperty = (BasicPropertyImpl) object[1];
            arrLibCess = BigDecimal.ZERO;
            currLibCess = BigDecimal.ZERO;
            result = new DailyCollectionReportResult();
            result.setReceiptNumber(collectionIndex.getReceiptNumber());
            result.setReceiptDate(collectionIndex.getReceiptDate());
            result.setAssessmentNumber(collectionIndex.getConsumerCode());
            result.setOwnerName(collectionIndex.getConsumerName());
            result.setPaidAt(collectionIndex.getChannel());
            result.setWard(basicProperty.getPropertyID().getWard().getName());

            String doorNo = basicProperty.getAddress().getHouseNoBldgApt();
            result.setTotalCollection(collectionIndex.getTotalAmount());
            if (doorNo != null && !doorNo.isEmpty())
                result.setDoorNumber(doorNo);
            else
                result.setDoorNumber("N/A");
            result.setStatus(collectionIndex.getStatus());

            result.setPaymentMode(collectionIndex.getPaymentMode());
            result.setArrearAmount(collectionIndex.getArrearAmount());
            result.setCurrentAmount(collectionIndex.getCurrentAmount());
            result.setArrearLibCess(collectionIndex.getArrearCess());
            result.setCurrentLibCess(collectionIndex.getCurrentCess());
            arrLibCess = collectionIndex.getCurrentCess() != null ? collectionIndex.getCurrentCess() : BigDecimal.ZERO;
            currLibCess = collectionIndex.getArrearCess() != null ? collectionIndex.getArrearCess() : BigDecimal.ZERO;
            rebateAmount = collectionIndex.getReductionAmount() != null ? collectionIndex.getReductionAmount() : BigDecimal.ZERO;
            result.setTotalLibCess(arrLibCess.add(currLibCess));
            result.setTotalPenalty(collectionIndex.getLatePaymentCharges());
            result.setTotalRebate(rebateAmount);
            result.setFromInstallment(collectionIndex.getInstallmentFrom());
            result.setToInstallment(collectionIndex.getInstallmentTo());
            dailyCollectionReportList.add(result);
        }*/

        return dailyCollectionReportList;
    }
    
    @ReadOnly
    public List<CurrentInstDCBReportResult> getCurrentInstallmentDCB(String ward) {
        final StringBuilder queryStr = new StringBuilder(500);

        queryStr.append("select ward.name as \"wardName\", cast(count(*) as integer) as \"noOfProperties\", cast(sum(pi.aggregate_current_firsthalf_demand+pi.aggregate_current_secondhalf_demand) as numeric) as \"currDemand\", cast(sum(pi.current_firsthalf_collection+pi.current_secondhalf_collection) as numeric) as \"currCollection\", cast(sum(pi.aggregate_arrear_demand) as numeric) as \"arrearDemand\",cast(sum(pi.arrearcollection) as numeric) as \"arrearCollection\" from egpt_mv_propertyinfo pi,"
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

    public void setPropPerServ(PersistenceService propPerServ) {
        this.propPerServ = propPerServ;
    }
    
    @ReadOnly
    public List<BillCollectorDailyCollectionReportResult> getBillCollectorWiseDailyCollection(Date date,
                                                                                              BillCollectorDailyCollectionReportResult bcDailyCollectionReportResult) {
        boolean whereConditionAdded = false;
        List<BillCollectorDailyCollectionReportResult> listBcPayment = new ArrayList<BillCollectorDailyCollectionReportResult>(
                0);
        int noofDays = 0;
        final StringBuilder queryBuilder = new StringBuilder(
                " select distinct district,ulbname  \"ulbName\" ,ulbcode \"ulbCode\" ,collectorname,mobilenumber,sum(target_arrears_demand) \"target_arrears_demand\",sum(target_current_demand) \"target_current_demand\",sum(today_arrears_collection) \"today_arrears_collection\",sum(today_currentyear_collection) \"today_currentyear_collection\", "
                        + " sum(cummulative_arrears_collection) \"cummulative_arrears_collection\",sum(cummulative_currentyear_collection) \"cummulative_currentyear_collection\",sum(lastyear_collection) \"lastyear_collection\",sum(lastyear_cummulative_collection) \"lastyear_cummulative_collection\"   "
                        + "from "+applicationProperties.statewideSchemaName()+".billColl_DialyCollection_view ");
        String value_ALL = "ALL";

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
                    && !bcDailyCollectionReportResult.getRegion().equalsIgnoreCase(value_ALL)) {
                if (whereConditionAdded)
                    queryBuilder.append(" and  lower(district) in (:districtNames) ");
                else {
                    whereConditionAdded = addWhereCondition(whereConditionAdded, queryBuilder);
                    queryBuilder.append("   lower(district) in (:districtNames) ");
                }
            }

            if (bcDailyCollectionReportResult.getType() != null && !bcDailyCollectionReportResult.getType().equals("")
                    && !bcDailyCollectionReportResult.getType().equalsIgnoreCase(value_ALL)) {

                if (whereConditionAdded)
                    queryBuilder.append(" and type =:typeOfSearch ");
                else {
                    whereConditionAdded = addWhereCondition(whereConditionAdded, queryBuilder);
                    queryBuilder.append(" type =:typeOfSearch ");
                }
            }

        }
        queryBuilder
                .append(" group by district,ulbname ,ulbcode  ,collectorname,mobilenumber  order by district,ulbname,collectorname ");
        final Query query = propPerServ.getSession().createSQLQuery(queryBuilder.toString());
        // query.setDate("collDate", date);

        if (bcDailyCollectionReportResult != null) {
            if (bcDailyCollectionReportResult.getCity() != null && !bcDailyCollectionReportResult.getCity().equals("")
                    && !bcDailyCollectionReportResult.getCity().equalsIgnoreCase(value_ALL)) {
                query.setString("cityName", bcDailyCollectionReportResult.getCity().toLowerCase());

            } else if (bcDailyCollectionReportResult.getDistrict() != null
                    && !bcDailyCollectionReportResult.getDistrict().equals("")
                    && !bcDailyCollectionReportResult.getDistrict().equalsIgnoreCase(value_ALL)) {
                query.setString("districtName", bcDailyCollectionReportResult.getDistrict().toLowerCase());

            } else if (bcDailyCollectionReportResult.getRegion() != null
                    && !bcDailyCollectionReportResult.getRegion().equals("")
                    && !bcDailyCollectionReportResult.getRegion().equalsIgnoreCase(value_ALL)) {
                LinkedList<String> districtlist = new LinkedList<String>();
                if (regionalHeirarchyService != null) {
                    List<RegionalHeirarchy> regions = regionalHeirarchyService
                            .getActiveChildRegionHeirarchyByPassingParentNameAndType(RegionalHeirarchyType.DISTRICT,
                                    bcDailyCollectionReportResult.getRegion());
                    if (regions != null && regions.size() > 0) {
                        for (RegionalHeirarchy regiion : regions) {
                            districtlist.add(regiion.getName().toLowerCase());
                        }
                        query.setParameterList("districtNames", districtlist);
                    }

                }

            }

            if (bcDailyCollectionReportResult.getType() != null && !bcDailyCollectionReportResult.getType().equals("")
                    && !bcDailyCollectionReportResult.getType().equalsIgnoreCase(value_ALL)) {
                query.setString("typeOfSearch", bcDailyCollectionReportResult.getType());
            }
        }

        query.setResultTransformer(new AliasToBeanResultTransformer(BillCollectorDailyCollectionReportResult.class));

        listBcPayment = (List<BillCollectorDailyCollectionReportResult>) query.list();

        if (financialYearDAO != null && listBcPayment.size() > 0) {

            CFinancialYear currentFinancialYear = financialYearDAO.getFinancialYearByDate(new Date());
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
    public List<BillCollectorDailyCollectionReportResult> getUlbWiseDailyCollection(Date date) {

        List<BillCollectorDailyCollectionReportResult> listBcPayment = new ArrayList<BillCollectorDailyCollectionReportResult>(
                0);
        int noofDays = 0;
        final StringBuilder queryBuilder = new StringBuilder(

                " select distinct district,ulbname \"ulbName\" ,ulbcode \"ulbCode\"  ,  collectorname \"collectorname\" ,mobilenumber \"mobilenumber\",  "
                        + "target_arrears_demand,target_current_demand,today_arrears_collection,today_currentyear_collection,   "
                        + "cummulative_arrears_collection,cummulative_currentyear_collection,lastyear_collection,lastyear_cummulative_collection  "
                        + "from "+applicationProperties.statewideSchemaName()+".ulbWise_DialyCollection_view  order by district,ulbname ");
        final Query query = propPerServ.getSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(BillCollectorDailyCollectionReportResult.class));

        listBcPayment = (List<BillCollectorDailyCollectionReportResult>) query.list();

        if (financialYearDAO != null && listBcPayment.size() > 0) {

            CFinancialYear currentFinancialYear = financialYearDAO.getFinancialYearByDate(new Date());
            if (currentFinancialYear != null)
                noofDays = DateUtils.daysBetween(new Date(), currentFinancialYear.getEndingDate());
        }
        buildCollectionReport(listBcPayment, noofDays);
        return listBcPayment;

    }

    private void buildCollectionReport(List<BillCollectorDailyCollectionReportResult> listBcPayment, int noofDays) {
        for (BillCollectorDailyCollectionReportResult bcResult : listBcPayment) {

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

            if (noofDays > 0) {
                bcResult.setDay_target(BigDecimal
                        .valueOf(bcResult.getTarget_total_demand() - bcResult.getCummulative_total_Collection())
                        .divide(BigDecimal.valueOf(noofDays), 4, RoundingMode.HALF_UP)
                        .setScale(2, RoundingMode.HALF_UP));
            } else
                bcResult.setDay_target(BigDecimal.ZERO);

            if (bcResult.getCummulative_total_Collection() > 0)
                bcResult.setCummulative_currentYear_Percentage(((BigDecimal.valueOf(bcResult
                        .getCummulative_total_Collection()).divide(
                        BigDecimal.valueOf(bcResult.getTarget_total_demand()), 4, RoundingMode.HALF_UP))
                        .multiply(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP));

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
                bcResult.setGrowth((BigDecimal.valueOf(bcResult.getCummulative_total_Collection()
                        - bcResult.getLastyear_cummulative_collection()).divide(
                        BigDecimal.valueOf(bcResult.getLastyear_cummulative_collection()), 4, RoundingMode.HALF_UP))
                        .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP));
            else
                bcResult.setGrowth((BigDecimal.ZERO));
        }

        for (BillCollectorDailyCollectionReportResult bcResult : listBcPayment) {

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

    public BigDecimal formatAmt(double amt) {
        BigDecimal result = new BigDecimal(0.000);
        result = BigDecimal.valueOf(amt / 1000).setScale(2, BigDecimal.ROUND_HALF_UP);

        return result;
    }
    
    @ReadOnly
    public List<BillCollectorDailyCollectionReportResult> getUlbWiseDcbCollection(Date date,
                                                                                  BillCollectorDailyCollectionReportResult bcDailyCollectionReportResult) {

        boolean whereConditionAdded = false;
        List<BillCollectorDailyCollectionReportResult> listBcPayment = new ArrayList<BillCollectorDailyCollectionReportResult>(
                0);
        int noofDays = 0;
        final StringBuilder queryBuilder = new StringBuilder(
                " select distinct district,ulbname  \"ulbName\" ,ulbcode \"ulbCode\",collectorname,mobilenumber ,sum(totalaccessments) \"totalaccessments\" , sum(current_demand) \"current_demand\", sum(arrears_demand) \"arrears_demand\", sum(current_demand_collection) \"current_demand_collection\" ,sum(arrears_demand_collection) \"arrears_demand_collection\" , sum(current_penalty) \"current_penalty\", sum(arrears_penalty) \"arrears_penalty\"  , sum(current_penalty_collection) \"current_penalty_collection\"  , sum(arrears_penalty_collection) \"arrears_penalty_collection\"  "
                        + "from "+applicationProperties.statewideSchemaName()+".ulbWise_DCBCollection_view ");

        String value_ALL = "ALL";

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
                    && !bcDailyCollectionReportResult.getRegion().equalsIgnoreCase(value_ALL)) {
                if (whereConditionAdded)
                    queryBuilder.append(" and  lower(district) in (:districtNames) ");
                else {
                    whereConditionAdded = addWhereCondition(whereConditionAdded, queryBuilder);
                    queryBuilder.append("   lower(district) in (:districtNames) ");
                }
            }

            if (bcDailyCollectionReportResult.getType() != null && !bcDailyCollectionReportResult.getType().equals("")
                    && !bcDailyCollectionReportResult.getType().equalsIgnoreCase(value_ALL)) {

                if (whereConditionAdded)
                    queryBuilder.append(" and category in (:typeOfSearch) ");
                else {
                    whereConditionAdded = addWhereCondition(whereConditionAdded, queryBuilder);
                    queryBuilder.append(" category in (:typeOfSearch) ");
                }
            }

        }
        queryBuilder
                .append(" group by district,ulbname ,ulbcode  ,collectorname,mobilenumber  order by district,ulbname,collectorname ");
        final Query query = propPerServ.getSession().createSQLQuery(queryBuilder.toString());
        // query.setDate("collDate", date);

        if (bcDailyCollectionReportResult != null) {
            if (bcDailyCollectionReportResult.getCity() != null && !bcDailyCollectionReportResult.getCity().equals("")
                    && !bcDailyCollectionReportResult.getCity().equalsIgnoreCase(value_ALL)) {
                query.setString("cityName", bcDailyCollectionReportResult.getCity().toLowerCase());

            } else if (bcDailyCollectionReportResult.getDistrict() != null
                    && !bcDailyCollectionReportResult.getDistrict().equals("")
                    && !bcDailyCollectionReportResult.getDistrict().equalsIgnoreCase(value_ALL)) {
                query.setString("districtName", bcDailyCollectionReportResult.getDistrict().toLowerCase());

            } else if (bcDailyCollectionReportResult.getRegion() != null
                    && !bcDailyCollectionReportResult.getRegion().equals("")
                    && !bcDailyCollectionReportResult.getRegion().equalsIgnoreCase(value_ALL)) {
                LinkedList<String> districtlist = new LinkedList<String>();
                if (regionalHeirarchyService != null) {
                    List<RegionalHeirarchy> regions = regionalHeirarchyService
                            .getActiveChildRegionHeirarchyByPassingParentNameAndType(RegionalHeirarchyType.DISTRICT,
                                    bcDailyCollectionReportResult.getRegion());
                    if (regions != null && regions.size() > 0) {
                        for (RegionalHeirarchy regiion : regions) {
                            districtlist.add(regiion.getName().toLowerCase());
                        }
                        query.setParameterList("districtNames", districtlist);
                    }

                }

            }

            if (bcDailyCollectionReportResult.getType() != null && !bcDailyCollectionReportResult.getType().equals("")
                    && !bcDailyCollectionReportResult.getType().equalsIgnoreCase(value_ALL)) {
                query.setParameterList("typeOfSearch", prepareTypeOfSearch(bcDailyCollectionReportResult.getType()));
            }
        }

        query.setResultTransformer(new AliasToBeanResultTransformer(BillCollectorDailyCollectionReportResult.class));

        listBcPayment = (List<BillCollectorDailyCollectionReportResult>) query.list();

        buildCollectionReportForUlbWiseDCb(listBcPayment);
        return listBcPayment;

    }

    private List<String> prepareTypeOfSearch(String type) {
        List<String> types = new ArrayList<String>();
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

    private void buildCollectionReportForUlbWiseDCb(List<BillCollectorDailyCollectionReportResult> listBcPayment) {
        Double percentage = 0.0;
        for (BillCollectorDailyCollectionReportResult bcResult : listBcPayment) {

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
                percentage = (bcResult.getCummulative_total_Collection() * 100) / bcResult.getTarget_total_demand();
                bcResult.setCummulative_total_CollectionPercentage(BigDecimal.valueOf(percentage.isNaN() ? 0.0 : percentage));
                percentage = ((bcResult.getCummulative_total_Collection() + bcResult.getCummulative_total_CollectionInterest()) * 100) / (bcResult.getTarget_total_demand());
                bcResult.setCummulative_total_CollectionInterestPercentage(BigDecimal.valueOf(percentage.isNaN() ? 0.0 : percentage));
            } else {
                bcResult.setCummulative_total_CollectionPercentage(BigDecimal.ZERO);
                bcResult.setCummulative_total_CollectionInterestPercentage(BigDecimal.ZERO);
            }

            bcResult.setBalance_arrearTax(bcResult.getArrears_demand() - bcResult.getArrears_demand_collection());
            bcResult.setBalance_arrearInterest(bcResult.getArrears_penalty() - bcResult.getArrears_penalty_collection());
            bcResult.setBalance_currentTax(bcResult.getCurrent_demand() - bcResult.getCurrent_demand_collection());
            bcResult.setBalance_currentInterest(bcResult.getCurrent_penalty()
                    - bcResult.getCurrent_penalty_collection());
            bcResult.setBalance_total(bcResult.getTarget_total_demand() - bcResult.getCummulative_total_Collection());
            bcResult.setBalance_totalInterest(bcResult.getTarget_total_demandInterest() - bcResult.getCummulative_total_CollectionInterest());
        }

        for (BillCollectorDailyCollectionReportResult bcResult : listBcPayment) {

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
            bcResult.setCummulative_total_CollectionPercentage(bcResult.getCummulative_total_CollectionPercentage().setScale(2, BigDecimal.ROUND_HALF_EVEN));
            bcResult.setCummulative_total_CollectionInterestPercentage(bcResult.getCummulative_total_CollectionInterestPercentage().setScale(2, BigDecimal.ROUND_HALF_EVEN));
        }

    }

    /**
     * @param boundaryId, mode, courtCase, propertyTypes
     * @return
     * @ Description - Returns query that retrieves zone/ward/block/propertywise
     * Arrear, Current Demand and Collection Details
     */
    public SQLQuery prepareQueryForDCBReport(final Long boundaryId, final String mode, final Boolean courtCase,
                                             final List<String> propertyTypes) {

        final String WARDWISE = "ward";
        final String BLOCKWISE = "block";
        final String PROPERTY = "property";

        final StringBuffer queryStr = new StringBuffer("");
        String commonFromQry = "", finalCommonQry = "", finalSelectQry = "", finalGrpQry = "", boundaryQry = "", whereQry = "",
                propertyTypeIds = "", courtCaseTable = "", courtCaseQry = "";
        Long param = null;

        if (propertyTypes != null && !propertyTypes.isEmpty()) {
            propertyTypeIds = propertyTypes.get(0);
            for (int i = 1; i < propertyTypes.size(); i++) {
                propertyTypeIds += "," + propertyTypes.get(i);
            }
        }

        if (courtCase) {
            courtCaseTable = ",egpt_courtcases cc ";
            courtCaseQry = " and cc.assessmentno = pi.upicno";
        } else {
            courtCaseQry = " and not exists (select 1 from egpt_courtcases cc where pi.upicno = cc.assessmentno )";
        }

        if (boundaryId != -1 && boundaryId != null)
            param = boundaryId;

        commonFromQry = " from egpt_mv_propertyinfo pi ";
        if (!mode.equalsIgnoreCase(PROPERTY)) {
            commonFromQry = commonFromQry + ", eg_boundary boundary ";
        }
        commonFromQry = commonFromQry + courtCaseTable + " where pi.isactive = true and pi.isexempted = false " + courtCaseQry;

        finalCommonQry = "cast(COALESCE(sum(pi.ARREAR_DEMAND),0) as numeric) as \"dmnd_arrearPT\","
                + " cast(COALESCE(sum(pi.pen_aggr_arrear_demand),0) AS numeric) as \"dmnd_arrearPFT\", cast(COALESCE(sum(pi.annualdemand),0) AS numeric) as \"dmnd_currentPT\", "
                + " cast(COALESCE(sum(pi.pen_aggr_current_firsthalf_demand),0)+COALESCE(sum(pi.pen_aggr_current_secondhalf_demand),0) AS numeric) as \"dmnd_currentPFT\","
                + " cast(COALESCE(sum(pi.ARREAR_COLLECTION),0) AS numeric) as \"clctn_arrearPT\", cast(COALESCE(sum(pi.pen_aggr_arr_coll),0) AS numeric) as \"clctn_arrearPFT\","
                + " cast(COALESCE(sum(pi.annualcoll),0) AS numeric) as \"clctn_currentPT\","
                + " cast(COALESCE(sum(pi.pen_aggr_current_firsthalf_coll),0)+COALESCE(sum(pi.pen_aggr_current_secondhalf_coll),0) AS numeric) as \"clctn_currentPFT\"  ";

        // Conditions to Retrieve data based on selected boundary types
        if (!mode.equalsIgnoreCase(PROPERTY)) {
            finalSelectQry = "select count(distinct pi.upicno) as \"assessmentCount\",cast(id as integer) as \"boundaryId\",boundary.name as \"boundaryName\", ";
            finalGrpQry = " group by boundary.id,boundary.name order by boundary.name";
        }
        if (propertyTypes == null)
            whereQry = whereQry + " and pi.proptymaster not in (select id from egpt_property_type_master where code = 'VAC_LAND') ";
        if (mode.equalsIgnoreCase(WARDWISE)) {
            if (param != 0)
                whereQry = whereQry + " and pi.WARDID = " + param;
            if (propertyTypes != null && !propertyTypes.isEmpty())
                whereQry = whereQry + " and pi.proptymaster in (" + propertyTypeIds + ") ";
            boundaryQry = " and pi.wardid=boundary.id ";
        } else if (mode.equalsIgnoreCase(BLOCKWISE)) {
            whereQry = whereQry + " and pi.wardid = " + param;
            if (propertyTypes != null && !propertyTypes.isEmpty())
                whereQry = whereQry + " and pi.proptymaster in (" + propertyTypeIds + ") ";
            boundaryQry = " and pi.blockid=boundary.id and pi.wardid = boundary.parent ";
        } else if (mode.equalsIgnoreCase(PROPERTY)) {
            finalSelectQry = "select distinct pi.upicno as \"assessmentNo\", pi.houseno as \"houseNo\", pi.ownersname as \"ownerName\", ";
            whereQry = whereQry + " and pi.blockid = " + param;
            if (propertyTypes != null && !propertyTypes.isEmpty())
                whereQry = whereQry + " and pi.proptymaster in (" + propertyTypeIds + ") ";
            boundaryQry = " and pi.wardid = ( select parent from eg_boundary where id = "+param+" ) ";
            finalGrpQry = " group by pi.upicno, pi.houseno, pi.ownersname order by pi.upicno ";
        }

        // Final Query : Retrieves arrear and current data for the selected boundary.
        queryStr.append(finalSelectQry).append(finalCommonQry).append(commonFromQry).append(whereQry)
                .append(boundaryQry).append(finalGrpQry);

        final SQLQuery query = propPerServ.getSession().createSQLQuery(queryStr.toString());
        return query;
    }

    /**
     * Method gives List of properties with current and arrear individual demand
     * details
     *
     * @param ward
     * @param block
     * @return
     */
    @ReadOnly
    public List<BaseRegisterVLTResult> getVLTPropertyByWardAndBlock(final String ward, final String block, final boolean exemptedCase) {
        BigDecimal taxRate = getTaxRate(PropertyTaxConstants.DEMANDRSN_CODE_VACANT_TAX);
        final StringBuilder queryStr = new StringBuilder(500);
        if(exemptedCase){
            queryStr.append(PMV_QUERY);
            queryStr.append(" and pmv.isExempted = true ");
        }
        else
            queryStr.append(PMV_QUERY);

        if (StringUtils.isNotBlank(ward))
            queryStr.append(" and pmv.ward.id=:ward ");
        if (StringUtils.isNotBlank(block))
            queryStr.append(" and pmv.block.id=:block ");
        queryStr.append("and pmv.propTypeMstrID.code='VAC_LAND'");
        queryStr.append(" order by pmv.propertyId, pmv.ward");
        final Query query = propPerServ.getSession().createQuery(queryStr.toString());
        if (StringUtils.isNotBlank(ward))
            query.setLong("ward", Long.valueOf(ward));
        if (StringUtils.isNotBlank(block))
            query.setLong("block", Long.valueOf(block));

        List<PropertyMaterlizeView> properties = query.list();
        List<BaseRegisterVLTResult> baseRegisterVLTResultList = new LinkedList<>();

        for (PropertyMaterlizeView propMatView : properties) {
            BigDecimal currFirstHalfLibCess = BigDecimal.ZERO;
            BigDecimal currSecondHalfLibCess = BigDecimal.ZERO;
            BigDecimal arrLibCess = BigDecimal.ZERO;
            BasicProperty basicProperty;
            BaseRegisterVLTResult baseRegisterVLTResultObj;
            baseRegisterVLTResultObj = new BaseRegisterVLTResult();
            baseRegisterVLTResultObj.setAssessmentNo(propMatView.getPropertyId());
            baseRegisterVLTResultObj.setWard(propMatView.getWard() != null ? (propMatView.getWard().getName() + " ," + propMatView.getWard().getBoundaryNum()) : "");
            baseRegisterVLTResultObj.setOwnerName(propMatView.getOwnerName() != null ? (propMatView.getOwnerName().contains(",") ? propMatView.getOwnerName()
                    .replace(",", " & ") : propMatView.getOwnerName()) : "");

            baseRegisterVLTResultObj.setSurveyNo(propMatView.getSurveyNo());
            baseRegisterVLTResultObj.setTaxationRate(taxRate);
            baseRegisterVLTResultObj.setMarketValue(propMatView.getMarketValue() != null ? propMatView.getMarketValue().setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
            baseRegisterVLTResultObj.setDocumentValue(propMatView.getCapitalValue() != null ? propMatView.getCapitalValue().setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
            basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(propMatView.getPropertyId());
            baseRegisterVLTResultObj.setOldAssessmentNo(basicProperty.getOldMuncipalNum());
            baseRegisterVLTResultObj.setSitalArea(propMatView.getSitalArea() != null ? (propMatView.getSitalArea()).setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
            if (propMatView.getMarketValue() != null && propMatView.getCapitalValue() != null)
                baseRegisterVLTResultObj.setHigherValueForImposedtax(propMatView.getMarketValue().compareTo(propMatView.getCapitalValue()) > 0 ? propMatView.getMarketValue().setScale(2, BigDecimal.ROUND_HALF_UP) : propMatView.getCapitalValue().setScale(2, BigDecimal.ROUND_HALF_UP));
            baseRegisterVLTResultObj.setIsExempted(propMatView.getIsExempted() != null ? (propMatView.getIsExempted() ? "Yes" : "No") : "");
            List<InstDmdCollMaterializeView> instDemandCollList = new LinkedList<>(
                    propMatView.getInstDmdColl());
            Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
            for (InstDmdCollMaterializeView instDmdCollObj : instDemandCollList) {
                if (instDmdCollObj.getInstallment().equals(currYearInstMap.get(CURRENTYEAR_FIRST_HALF))) {
                    currFirstHalfLibCess = instDmdCollObj.getLibCessTax() != null ? instDmdCollObj.getLibCessTax() : BigDecimal.ZERO;
                    baseRegisterVLTResultObj.setLibraryCessTaxFirstHlf(currFirstHalfLibCess);
                } else if (instDmdCollObj.getInstallment().equals(currYearInstMap.get(CURRENTYEAR_SECOND_HALF))) {
                    currSecondHalfLibCess = instDmdCollObj.getLibCessTax() != null ? instDmdCollObj.getLibCessTax() : BigDecimal.ZERO;
                    baseRegisterVLTResultObj.setLibraryCessTaxSecondHlf(currSecondHalfLibCess);
                } else {
                    arrLibCess = arrLibCess.add(instDmdCollObj.getLibCessTax() != null ? instDmdCollObj.getLibCessTax() : BigDecimal.ZERO);
                    baseRegisterVLTResultObj.setArrearLibraryTax(arrLibCess);
                }
            }
            baseRegisterVLTResultObj.setPropertyTaxFirstHlf((propMatView.getAggrCurrFirstHalfDmd() != null ? propMatView.getAggrCurrFirstHalfDmd() : BigDecimal.ZERO).subtract(currFirstHalfLibCess));
            baseRegisterVLTResultObj.setPropertyTaxSecondHlf((propMatView.getAggrCurrSecondHalfDmd() != null ? propMatView.getAggrCurrSecondHalfDmd() : BigDecimal.ZERO).subtract(currSecondHalfLibCess));
            baseRegisterVLTResultObj.setCurrTotal((propMatView.getAggrCurrFirstHalfDmd() != null ? propMatView.getAggrCurrFirstHalfDmd() : BigDecimal.ZERO).
                    add((propMatView.getAggrCurrSecondHalfDmd() != null ? propMatView.getAggrCurrSecondHalfDmd() : BigDecimal.ZERO)));
            BigDecimal currPenaltyFine = BigDecimal.ZERO;
            if (propMatView.getAggrCurrFirstHalfPenaly() != null) {
                currPenaltyFine = currPenaltyFine.add(propMatView.getAggrCurrFirstHalfPenaly());
            }
            if (propMatView.getAggrCurrSecondHalfPenaly() != null) {
                currPenaltyFine = currPenaltyFine.add(propMatView.getAggrCurrSecondHalfPenaly());
            }
            baseRegisterVLTResultObj.setPenaltyFines(currPenaltyFine);
            baseRegisterVLTResultObj.setArrearPropertyTax(propMatView.getAggrArrDmd() != null && propMatView.getAggrArrDmd().compareTo(BigDecimal.ZERO) >= 1 ? (propMatView.getAggrArrDmd()).subtract(arrLibCess) : BigDecimal.ZERO);
            baseRegisterVLTResultObj.setArrearPenaltyFines(propMatView.getAggrArrearPenaly() != null ? propMatView.getAggrArrearPenaly() : BigDecimal.ZERO);
            BigDecimal arrTotal;
            BigDecimal arrColl;
            BigDecimal totalColl;
            BigDecimal currColl;
            arrTotal = propMatView.getAggrArrDmd() != null ? propMatView.getAggrArrDmd() : BigDecimal.ZERO;
            baseRegisterVLTResultObj.setArrearTotal(arrTotal);

            arrColl = propMatView.getAggrArrColl() != null ? propMatView.getAggrArrColl() : BigDecimal.ZERO;
            baseRegisterVLTResultObj.setArrearColl(arrColl);

            totalColl = arrColl;
            currColl = (propMatView.getAggrCurrFirstHalfColl() != null ? propMatView.getAggrCurrFirstHalfColl() : BigDecimal.ZERO).add(propMatView.getAggrCurrSecondHalfColl() != null ? propMatView.getAggrCurrSecondHalfColl() : BigDecimal.ZERO);
            totalColl = totalColl.add(currColl);
            baseRegisterVLTResultObj.setCurrentColl(currColl);
            baseRegisterVLTResultObj.setTotalColl(totalColl);

            String arrearPerFrom;
            String arrearPerTo;
            if (instDemandCollList.size() > 1 && ((arrTotal.subtract(arrColl).compareTo(BigDecimal.ZERO)) > 1)) {
                arrearPerTo = dateFormatter.format(DateUtils.add(propertyTaxCommonUtils.getCurrentInstallment().getFromDate(), Calendar.DAY_OF_MONTH, -1));
                arrearPerFrom = dateFormatter.format(instDemandCollList.get(0).getInstallment().getFromDate());
                baseRegisterVLTResultObj.setArrearPeriod(arrearPerFrom + "-" + arrearPerTo);
            } else {
                baseRegisterVLTResultObj.setArrearPeriod("N/A");
            }

            baseRegisterVLTResultList.add(baseRegisterVLTResultObj);

        }
        return baseRegisterVLTResultList;
    }

    private BigDecimal getTaxRate(String taxHead) {
        taxRateProps = propertyTaxUtil.loadTaxRates();
        BigDecimal taxRate = BigDecimal.ZERO;
        if (taxRateProps != null) {
            taxRate = new BigDecimal(taxRateProps.getProperty(taxHead));
        }
        return taxRate;
    }
    
    /**
     * This method gives the defaulters information
     * @param propertyViewList
     * @return list
     */
    @ReadOnly
    public List<DefaultersInfo> getDefaultersInformation(List<PropertyMaterlizeView> propertyViewList,final String noofyrs,final Integer limit) {
        List<DefaultersInfo> defaultersList = new ArrayList<>();
        List<DefaultersInfo> defaultersListForYrs = new ArrayList<>();
        DefaultersInfo defaultersInfo;
        BigDecimal totalDue;
        BigDecimal currPenalty;
        BigDecimal currPenaltyColl;
        int count = 1;

        int reqyr = 0;
       
        
        for (final PropertyMaterlizeView propView : propertyViewList) {
            
           if (isCountInLimit(limit, count))
               break;
                
            
           defaultersInfo= getInstDmdInfo(propView);
         
            defaultersInfo.setSlNo(count);
            defaultersInfo.setAssessmentNo(propView.getPropertyId());
            defaultersInfo.setOwnerName(getOwerName(propView));
            defaultersInfo.setWardName(propView.getWard().getName());
            defaultersInfo.setHouseNo(propView.getHouseNo());
            defaultersInfo.setLocality(getLocality(propView));
            defaultersInfo.setMobileNumber(getMobileNo(propView));
            defaultersInfo.setArrearsDue(propView.getAggrArrDmd().subtract(propView.getAggrArrColl()));
            defaultersInfo.setCurrentDue((propView.getAggrCurrFirstHalfDmd().add(propView.getAggrCurrSecondHalfDmd()))
                    .subtract(propView.getAggrCurrFirstHalfColl().add(propView.getAggrCurrSecondHalfColl())));
            defaultersInfo.setAggrArrearPenalyDue(getAggArrPenaltyDue(propView));
            currPenalty = getAggCurrFirstHalfPenalty(propView)
                    .add(getAggCurrSecHalfPenalty(propView));
            currPenaltyColl = getAggCurrFirstHalfPenColl(propView)
                            .add(getAggCurrSecHalfPenColl(propView));
            defaultersInfo.setAggrCurrPenalyDue(currPenalty.subtract(currPenaltyColl));
            totalDue = defaultersInfo.getArrearsDue().add(defaultersInfo.getCurrentDue())
                    .add(defaultersInfo.getAggrArrearPenalyDue()).add(defaultersInfo.getAggrCurrPenalyDue());
            defaultersInfo.setTotalDue(totalDue);
            int yrs =0;
     
          
            if(isNotMoreThanFiveYrs(noofyrs)){
                reqyr=Integer.parseInt(noofyrs.substring(0,1));
                yrs=propertyTaxUtil.getNoOfYears(defaultersInfo.getMinDate(),defaultersInfo.getMaxDate());
                
            }
           if(isNotMoreThanFiveYrs(noofyrs) && reqyr >= yrs){
               defaultersListForYrs.add(defaultersInfo);
                count++;
            }else if(noofyrs ==null || (noofyrs!=null && ABOVE_FIVE_YEARS.equalsIgnoreCase(noofyrs))){
                defaultersList.add(defaultersInfo);
                count++;
           }
        }

        return defaultersListForYrs.isEmpty()?defaultersList:defaultersListForYrs;
    }
    
    @ReadOnly
    private DefaultersInfo getInstDmdInfo(PropertyMaterlizeView propView){
        DefaultersInfo defaultersInfo = new DefaultersInfo();

        Iterator itr;
        InstDmdCollMaterializeView idc;
        if (!propView.getInstDmdColl().isEmpty()) {
            itr = propView.getInstDmdColl().iterator();
            Installment minInstallment=null;
            Installment maxInstallment=null;
            while (itr.hasNext()) {
                BigDecimal dmdtot;
                BigDecimal colltot;
                idc = (InstDmdCollMaterializeView) itr.next();
                 dmdtot= getGenTax(idc).add 
                        (getEduCess(idc)).add 
                        (getLibCess(idc)).add 
                        (getPenaltyFines(idc)).add 
                        (getPubSerCharge(idc)).add 
                        (getSewTax(idc)).add 
                        (getUnaPenalty(idc)).add 
                        (getVacLandTax(idc));
                     
                 colltot= getGenTaxColl(idc).add 
                                ( getEduCessColl(idc)).add 
                                (getLibCessColl(idc)).add 
                                ( getPenaltyFineColl(idc)).add 
                                (getPubServiceColl(idc)).add 
                                (getSewColl(idc)).add 
                                (getUnauthPenColl(idc)).add 
                                (getVacLColl(idc));
               
                minInstallment=getMinInstallment(minInstallment,idc,dmdtot,colltot);
                maxInstallment=getMaxInstallment(maxInstallment,idc,dmdtot,colltot);
                     
            }
            if(minInstallment!=null){
            defaultersInfo.setMinDate(minInstallment.getFromDate());
            defaultersInfo.setArrearsFrmInstallment(minInstallment.getDescription());
            }
            if(maxInstallment!=null){
            defaultersInfo.setMaxDate(maxInstallment.getFromDate());
            defaultersInfo.setArrearsToInstallment(maxInstallment.getDescription());
            }
        }
        return defaultersInfo;
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
                " & ") : propView.getOwnerName():"NA";
    }

    private String getLocality(final PropertyMaterlizeView propView) {
        return (propView.getLocality()) != null ? propView.getLocality().getName()
                : "NA";
    }

    private String getMobileNo(final PropertyMaterlizeView propView) {
        return StringUtils.isNotBlank(propView.getMobileNumber()) ? propView
                .getMobileNumber() : "NA";
    }

    private boolean isNotMoreThanFiveYrs(final String noofyrs) {
        return noofyrs !=null && !ABOVE_FIVE_YEARS.equalsIgnoreCase(noofyrs);
    }

    private boolean isCountInLimit(final Integer limit, int count) {
        return limit != null && limit != -1 && count-1==limit;
    }

    public Installment getMinInstallment(Installment minInstallment,InstDmdCollMaterializeView idc,BigDecimal dmdtot,BigDecimal colltot){
        Installment inst=null;
        if(minInstallment==null){
            return idc.getInstallment();
        }else if (dmdtot.compareTo(colltot)>0 && minInstallment.getFromDate().after(idc.getInstallment().getFromDate())){
            inst=idc.getInstallment();
        }
        return inst==null?minInstallment:inst;
    }
    public Installment getMaxInstallment(Installment maxInstallment,InstDmdCollMaterializeView idc,BigDecimal dmdtot,BigDecimal colltot){
        Installment inst=null;
        if(maxInstallment==null){
            return idc.getInstallment();
        }else if(maxInstallment.getFromDate().before(idc.getInstallment().getFromDate()) && dmdtot.compareTo(colltot)>0){
            inst=idc.getInstallment();
        }
        return inst==null?maxInstallment:inst;
    }
    private BigDecimal getVacLandTax(InstDmdCollMaterializeView idc) {
        return idc.getVacantLandTax() != null ? idc.getVacantLandTax() : BigDecimal.ZERO;
    }

    private BigDecimal getUnaPenalty(InstDmdCollMaterializeView idc) {
        return idc.getUnauthPenaltyTax() != null ? idc.getUnauthPenaltyTax() : BigDecimal.ZERO;
    }

    private BigDecimal getSewTax(InstDmdCollMaterializeView idc) {
        return idc.getSewTax() != null ? idc.getSewTax() : BigDecimal.ZERO;
    }

    private BigDecimal getPubSerCharge(InstDmdCollMaterializeView idc) {
        return idc.getPubSerChrgTax() != null ? idc.getPubSerChrgTax() : BigDecimal.ZERO;
    }

    private BigDecimal getPenaltyFines(InstDmdCollMaterializeView idc) {
        return idc.getPenaltyFinesTax() != null ? idc.getPenaltyFinesTax() : BigDecimal.ZERO;
    }

    private BigDecimal getLibCess(InstDmdCollMaterializeView idc) {
        return idc.getLibCessTax() != null ? idc.getLibCessTax() : BigDecimal.ZERO;
    }

    private BigDecimal getEduCess(InstDmdCollMaterializeView idc) {
        return idc.getEduCessTax() != null ? idc.getEduCessTax() : BigDecimal.ZERO;
    }

    private BigDecimal getGenTax(InstDmdCollMaterializeView idc) {
        return idc.getGeneralTax() != null ? idc.getGeneralTax() : BigDecimal.ZERO;
    }

    private BigDecimal getVacLColl(InstDmdCollMaterializeView idc) {
        return idc.getVacantLandTaxColl() != null ? idc.getVacantLandTaxColl() : BigDecimal.ZERO;
    }

    private BigDecimal getUnauthPenColl(InstDmdCollMaterializeView idc) {
        return idc.getUnauthPenaltyTaxColl() != null ? idc.getUnauthPenaltyTaxColl() : BigDecimal.ZERO;
    }

    private BigDecimal getSewColl(InstDmdCollMaterializeView idc) {
        return idc.getSewTaxColl() != null ? idc.getSewTaxColl() : BigDecimal.ZERO;
    }

    private BigDecimal getPubServiceColl(InstDmdCollMaterializeView idc) {
        return idc.getPubSerChrgTaxColl() != null ? idc.getPubSerChrgTaxColl() : BigDecimal.ZERO;
    }

    private BigDecimal getPenaltyFineColl(InstDmdCollMaterializeView idc) {
        return idc.getPenaltyFinesTaxColl() != null ?  idc.getPenaltyFinesTaxColl() : BigDecimal.ZERO;
    }

    private BigDecimal getLibCessColl(InstDmdCollMaterializeView idc) {
        return idc.getLibCessTaxColl() != null ? idc.getLibCessTaxColl() : BigDecimal.ZERO;
    }

    private BigDecimal getEduCessColl(InstDmdCollMaterializeView idc) {
        return idc.getEduCessTaxColl() != null ?  idc.getEduCessTaxColl() : BigDecimal.ZERO;
    }

    private BigDecimal getGenTaxColl(InstDmdCollMaterializeView idc) {
        return idc.getGeneralTaxColl() != null ? idc.getGeneralTaxColl() : BigDecimal.ZERO;
    }
    
    
}
