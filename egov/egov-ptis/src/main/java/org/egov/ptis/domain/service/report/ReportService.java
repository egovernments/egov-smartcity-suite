/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.ptis.domain.service.report;

import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.ROLE_COLLECTION_OPERATOR;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.RegionalHeirarchy;
import org.egov.commons.RegionalHeirarchyType;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.service.RegionalHeirarchyService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.search.elastic.entity.CollectionIndex;
import org.egov.infra.utils.DateUtils;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.BaseRegisterResult;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.BillCollectorDailyCollectionReportResult;
import org.egov.ptis.domain.entity.property.CurrentInstDCBReportResult;
import org.egov.ptis.domain.entity.property.DailyCollectionReportResult;
import org.egov.ptis.domain.entity.property.FloorDetailsView;
import org.egov.ptis.domain.entity.property.InstDmdCollMaterializeView;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class ReportService {

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
    private PersistenceService propPerServ;
    final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private RegionalHeirarchyService regionalHeirarchyService;

    @Autowired
    private UserService userService;

    private @Autowired FinancialYearDAO financialYearDAO;

    /**
     * Method gives List of properties with current and arrear individual demand
     * details
     * 
     * @param ward
     * @param block
     * @return
     */
    public List<BaseRegisterResult> getPropertyByWardAndBlock(final String ward, final String block) {

        final StringBuilder queryStr = new StringBuilder(500);
        queryStr.append("select distinct pmv from PropertyMaterlizeView pmv where pmv.isActive = true ");

        if (StringUtils.isNotBlank(ward))
            queryStr.append(" and pmv.ward.id=:ward ");
        if (StringUtils.isNotBlank(block))
            queryStr.append(" and pmv.block.id=:block ");
        queryStr.append(" order by pmv.propertyId, pmv.ward");
        final Query query = propPerServ.getSession().createQuery(queryStr.toString());
        if (StringUtils.isNotBlank(ward))
            query.setLong("ward", Long.valueOf(ward));
        if (StringUtils.isNotBlank(block))
            query.setLong("block", Long.valueOf(block));

        List<PropertyMaterlizeView> properties = query.list();
        List<BaseRegisterResult> baseRegisterResultList = new LinkedList<BaseRegisterResult>();

        for (PropertyMaterlizeView propMatView : properties) {
            List<FloorDetailsView> floorDetails = new LinkedList<FloorDetailsView>(propMatView.getFloorDetails());
            if (floorDetails.size() > 1) {
                addMultipleFloors(baseRegisterResultList, propMatView, floorDetails);
            } else {
                BaseRegisterResult baseRegisterResultObj = new BaseRegisterResult();
                baseRegisterResultObj = addSingleFloor(baseRegisterResultObj, propMatView);
                for (FloorDetailsView floor : floorDetails) {
                    baseRegisterResultObj.setPlinthArea(floor.getPlinthArea());
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
        baseRegisterResultObj.setOwnerName(propMatView.getOwnerName().contains(",") ? propMatView.getOwnerName()
                .replace(",", " & ") : propMatView.getOwnerName());
        baseRegisterResultObj.setIsExempted(propMatView.getIsExempted() ? "Yes" : "No");
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
        List<InstDmdCollMaterializeView> instDemandCollList = new LinkedList<InstDmdCollMaterializeView>(
                propMatView.getInstDmdColl());
        for (InstDmdCollMaterializeView instDmdCollObj : instDemandCollList) {
            if (instDmdCollObj.getInstallment().equals(propertyTaxUtil.getCurrentInstallment())) {
                if (propertyType.getCode().equals(OWNERSHIP_TYPE_VAC_LAND)) {
                    baseRegisterResultObj.setPropertyTax(instDmdCollObj.getVacantLandTax());
                } else {
                    baseRegisterResultObj.setPropertyTax(instDmdCollObj.getGeneralTax());
                }
                baseRegisterResultObj.setEduCessTax(instDmdCollObj.getEduCessTax());
                baseRegisterResultObj.setLibraryCessTax(instDmdCollObj.getLibCessTax());
                baseRegisterResultObj.setPenaltyFines(instDmdCollObj.getPenaltyFinesTax());
                baseRegisterResultObj.setCurrTotal(instDmdCollObj.getGeneralTax().add(instDmdCollObj.getEduCessTax())
                        .add(instDmdCollObj.getLibCessTax()));
            } else {
                if (propertyType.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
                    totalArrearPropertyTax = totalArrearPropertyTax.add(instDmdCollObj.getVacantLandTax());
                else
                    totalArrearPropertyTax = totalArrearPropertyTax.add(instDmdCollObj.getGeneralTax());
                totalArrearEduCess = totalArrearEduCess.add(instDmdCollObj.getEduCessTax());
                totalArreaLibCess = totalArreaLibCess.add(instDmdCollObj.getLibCessTax());
                arrearPenaltyFine = arrearPenaltyFine.add(instDmdCollObj.getPenaltyFinesTax());
            }
        }

        String arrearPerFrom = "";
        String arrearPerTo = "";
        if (instDemandCollList.size() > 1) {
            arrearPerTo = dateFormatter.format(instDemandCollList.get(instDemandCollList.size() - 2).getInstallment()
                    .getToDate());
            arrearPerFrom = dateFormatter.format(instDemandCollList.get(0).getInstallment().getFromDate());
            baseRegisterResultObj.setArrearPeriod(arrearPerFrom + "-" + arrearPerTo);
        } else {
            baseRegisterResultObj.setArrearPeriod("N/A");
        }

        baseRegisterResultObj.setArrearTotal(totalArrearPropertyTax.add(totalArrearEduCess).add(totalArreaLibCess));
        baseRegisterResultObj.setArrearPropertyTax(totalArrearPropertyTax);
        baseRegisterResultObj.setArrearLibraryTax(totalArreaLibCess);
        baseRegisterResultObj.setArrearEduCess(totalArrearEduCess);
        baseRegisterResultObj.setArrearPenaltyFines(arrearPenaltyFine);
        baseRegisterResultObj.setPropertyType(propertyType.getCode());
        return baseRegisterResultObj;
    }

    private void addMultipleFloors(List<BaseRegisterResult> baseRegisterResultList, PropertyMaterlizeView propMatView,
            List<FloorDetailsView> floorDetails) {
        BaseRegisterResult baseRegisterResultObj = null;
        int count = 0;
        for (FloorDetailsView floorview : floorDetails) {
            if (count == 0) {
                baseRegisterResultObj = new BaseRegisterResult();
                baseRegisterResultObj = addSingleFloor(baseRegisterResultObj, propMatView);
                baseRegisterResultObj.setPlinthArea(floorview.getPlinthArea());
                baseRegisterResultObj.setPropertyUsage(floorview.getPropertyUsage().contains(",") ? floorview
                        .getPropertyUsage().replace(",", " & ") : floorview.getPropertyUsage());
                baseRegisterResultObj.setClassificationOfBuilding(floorview.getClassification());
                count++;
            } else {
                baseRegisterResultObj = new BaseRegisterResult();
                baseRegisterResultObj.setAssessmentNo("");
                baseRegisterResultObj.setOwnerName("");
                baseRegisterResultObj.setDoorNO("");
                baseRegisterResultObj.setCourtCase("");
                baseRegisterResultObj.setArrearPeriod("");
                baseRegisterResultObj.setPlinthArea(floorview.getPlinthArea());
                baseRegisterResultObj.setPropertyUsage(floorview.getPropertyUsage().contains(",") ? floorview
                        .getPropertyUsage().replace(",", " & ") : floorview.getPropertyUsage());
                baseRegisterResultObj.setClassificationOfBuilding(floorview.getClassification());
            }
            baseRegisterResultList.add(baseRegisterResultObj);
        }

    }

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

        for (Object objects : objectList) {
            final Object[] object = (Object[]) objects;
            CollectionIndex collectionIndex = (CollectionIndex) object[0];
            BasicPropertyImpl basicProperty = (BasicPropertyImpl) object[1];
            arrLibCess = BigDecimal.ZERO;
            currLibCess = BigDecimal.ZERO;
            result = new DailyCollectionReportResult();
            result.setReceiptNumber(collectionIndex.getReceiptNumber());
            result.setReceiptDate(collectionIndex.getReceiptDate());
            result.setAssessmentNumber(collectionIndex.getConsumerCode());
            result.setOwnerName(collectionIndex.getPayeeName());
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
            result.setTotalLibCess(arrLibCess.add(currLibCess));
            result.setTotalPenalty(collectionIndex.getLatePaymentCharges());
            result.setFromInstallment(collectionIndex.getInstallmentFrom());
            result.setToInstallment(collectionIndex.getInstallmentTo());
            dailyCollectionReportList.add(result);
        }

        return dailyCollectionReportList;
    }

    public List<CurrentInstDCBReportResult> getCurrentInstallmentDCB(String ward) {
        final StringBuilder queryStr = new StringBuilder(500);

        queryStr.append("select ward.name as \"wardName\", cast(count(*) as integer) as \"noOfProperties\", cast(sum(pi.aggregate_current_demand) as numeric) as \"currDemand\", cast(sum(pi.current_collection) as numeric) as \"currCollection\", cast(sum(pi.aggregate_arrear_demand) as numeric) as \"arrearDemand\",cast(sum(pi.arrearcollection) as numeric) as \"arrearCollection\" from egpt_mv_propertyinfo pi,"
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

    public List<BillCollectorDailyCollectionReportResult> getBillCollectorWiseDailyCollection(Date date,
            BillCollectorDailyCollectionReportResult bcDailyCollectionReportResult) {
        boolean whereConditionAdded = false;
        List<BillCollectorDailyCollectionReportResult> listBcPayment = new ArrayList<BillCollectorDailyCollectionReportResult>(
                0);
        int noofDays = 0;
        final StringBuilder queryBuilder = new StringBuilder(
                " select distinct district,ulbname  \"ulbName\" ,ulbcode \"ulbCode\" ,collectorname,mobilenumber,sum(target_arrears_demand) \"target_arrears_demand\",sum(target_current_demand) \"target_current_demand\",sum(today_arrears_collection) \"today_arrears_collection\",sum(today_currentyear_collection) \"today_currentyear_collection\", "
                        + " sum(cummulative_arrears_collection) \"cummulative_arrears_collection\",sum(cummulative_currentyear_collection) \"cummulative_currentyear_collection\",sum(lastyear_collection) \"lastyear_collection\",sum(lastyear_cummulative_collection) \"lastyear_cummulative_collection\"   "
                        + " from public.billColl_DialyCollection_view  ");

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
                noofDays = DateUtils.noOfDays(new Date(), currentFinancialYear.getEndingDate());
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

    public List<BillCollectorDailyCollectionReportResult> getUlbWiseDailyCollection(Date date) {

        List<BillCollectorDailyCollectionReportResult> listBcPayment = new ArrayList<BillCollectorDailyCollectionReportResult>(
                0);
        int noofDays = 0;
        final StringBuilder queryBuilder = new StringBuilder(

                " select distinct district,ulbname \"ulbName\" ,ulbcode \"ulbCode\"  ,  collectorname \"collectorname\" ,mobilenumber \"mobilenumber\",  "
                        + "target_arrears_demand,target_current_demand,today_arrears_collection,today_currentyear_collection,   "
                        + "cummulative_arrears_collection,cummulative_currentyear_collection,lastyear_collection,lastyear_cummulative_collection  "
                        + "from  public.ulbWise_DialyCollection_view  order by district,ulbname ");
        final Query query = propPerServ.getSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(BillCollectorDailyCollectionReportResult.class));

        listBcPayment = (List<BillCollectorDailyCollectionReportResult>) query.list();

        if (financialYearDAO != null && listBcPayment.size() > 0) {

            CFinancialYear currentFinancialYear = financialYearDAO.getFinancialYearByDate(new Date());
            if (currentFinancialYear != null)
                noofDays = DateUtils.noOfDays(new Date(), currentFinancialYear.getEndingDate());
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
        result = BigDecimal.valueOf(amt / 100000).setScale(2, BigDecimal.ROUND_HALF_UP);

        return result;
    }

    public List<BillCollectorDailyCollectionReportResult> getUlbWiseDcbCollection(Date date,
            BillCollectorDailyCollectionReportResult bcDailyCollectionReportResult) {

        boolean whereConditionAdded = false;
        List<BillCollectorDailyCollectionReportResult> listBcPayment = new ArrayList<BillCollectorDailyCollectionReportResult>(
                0);
        int noofDays = 0;
        final StringBuilder queryBuilder = new StringBuilder(
                " select distinct district,ulbname  \"ulbName\" ,ulbcode \"ulbCode\",collectorname,mobilenumber ,sum(totalaccessments) \"totalaccessments\" , sum(current_demand) \"current_demand\", sum(arrears_demand) \"arrears_demand\", sum(current_demand_collection) \"current_demand_collection\" ,sum(arrears_demand_collection) \"arrears_demand_collection\" , sum(current_penalty) \"current_penalty\", sum(arrears_penalty) \"arrears_penalty\"  , sum(current_penalty_collection) \"current_penalty_collection\"  , sum(arrears_penalty_collection) \"arrears_penalty_collection\"  "
                        + " from public.ulbWise_DCBCollection_view  ");

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
            bcResult.setCummulative_total_CollectionPercentage(BigDecimal.valueOf((bcResult.getCummulative_total_Collection()*100)/bcResult.getTarget_total_demand()));
            bcResult.setCummulative_total_CollectionInterestPercentage(BigDecimal.valueOf((bcResult.getCummulative_total_CollectionInterest()*100)/bcResult.getTarget_total_demandInterest()));

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
            bcResult.setCummulative_total_CollectionPercentage(bcResult.getCummulative_total_CollectionPercentage().setScale(0, BigDecimal.ROUND_HALF_EVEN));
            bcResult.setCummulative_total_CollectionInterestPercentage(bcResult.getCummulative_total_CollectionInterestPercentage().setScale(0, BigDecimal.ROUND_HALF_EVEN));
        } 

    }

}
