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
package org.egov.works.web.actions.reports;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.egov.commons.EgwTypeOfWork;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.gis.model.GeoLatLong;
import org.egov.infra.gis.model.GeoLocation;
import org.egov.infra.gis.service.GeoLocationConstants;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.utils.NumberUtil;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.masters.NatureOfWork;
import org.egov.works.models.tender.OfflineStatus;
import org.egov.works.models.tender.TenderEstimate;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.models.tender.TenderResponseActivity;
import org.egov.works.models.tender.WorksPackageDetails;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;

public class WorksGISReportAction extends BaseFormAction {

    private static final long serialVersionUID = -6987141628565169146L;
    private static final Logger LOGGER = Logger.getLogger(WorksGISReportAction.class);
    private final String AFTER_SEARCH = "afterSearch";
    private final String GMAP = "gmap";
    private Integer zoneId;
    private Integer wardId;
    private Long parentCategory;
    private Long category;
    private Long expenditureType;
    private Long contractorId;
    private WorkOrderService workOrderService;
    private List<GeoLocation> locationList;
    private final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private String resultStatus = "beforeSearch";
    private String estimatenumber;
    private WorksService worksService;
    private List<String> tenderTypeList = null;;

    @PersistenceContext
    private EntityManager entityManager;

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(final Long contractorId) {
        this.contractorId = contractorId;
    }

    @Override
    public Object getModel() {
        return null;
    }

    @Override
    public void prepare() {
        super.prepare();
        final List<Boundary> zoneList = entityManager.createQuery(
                "from Boundary BI  where upper(BI.boundaryType.name) = 'ZONE' order by BI.id", Boundary.class)
                .getResultList();
        addDropdownData("zoneList", zoneList);
        addDropdownData("wardList", Collections.emptyList());
        addDropdownData("typeList", entityManager.createQuery("from NatureOfWork dt", NatureOfWork.class).getResultList());
        addDropdownData("parentCategoryList",
                entityManager.createQuery("from EgwTypeOfWork etw1 where etw1.parentid is null", EgwTypeOfWork.class)
                        .getResultList());
        addDropdownData("categoryList", Collections.emptyList());
    }

    public String beforeSearch() {
        return GMAP;
    }

    /**
     * Return values in the order - estimate id[0], lat[1], lon[2], estimate Number[3], name[4], work
     * value[5],overheads[6],wpno[7] tender document released date[8] , type of work[9], workSubType[10], projcode.id[11] In case
     * contractor is selected in search criteria, the following work order related values are returned from this query: work order
     * date[12], contract period[13], contractor name[14], work commenced date[15]
     *
     * @return
     */
    private Map<String, Object> generateQuery() {
        final Map<String, Object> queryMap = new HashMap<>();
        final StringBuffer query = new StringBuffer(1024);
        final Map<String, Object> params = new HashMap<>();
        final StringBuffer columnsToShow = new StringBuffer(
                " absEst.id, absEst.lat, absEst.lon, absEst.estimateNumber,absEst.name,")
                        .append(" coalesce(absEst.workValue,0), (select sum(coalesce(ovr.amount,0)) from OverheadValue ovr")
                        .append(" where ovr.abstractEstimate=absEst), (select wpd.worksPackage.wpNumber from WorksPackageDetails wpd")
                        .append(" where wpd.estimate=absEst and wpd.worksPackage.egwStatus.code not in (:wpStatus)), ")
                        .append(" (select to_char(offLineStatus.statusDate,'dd/MM/YYYY') from OfflineStatus offLineStatus")
                        .append(" where offLineStatus.objectId = ( select wpd.worksPackage.id from WorksPackageDetails wpd")
                        .append(" where wpd.estimate = absEst and wpd.worksPackage.egwStatus.code not in (:wpStatus))")
                        .append(" and offLineStatus.egwStatus.code = :olsStatus and objectType='WorksPackage' ), ")
                        .append(" absEst.parentCategory.description, workSubType.description, projcode.id ");

        params.put("wpStatus", Arrays.asList(WorksConstants.NEW, WorksConstants.CANCELLED_STATUS));
        params.put("olsStatus", WorksConstants.TENDER_DOCUMENT_RELEASED);

        final StringBuffer columnsToShowWhenWOEIsJoined = new StringBuffer(
                " absEst.id, absEst.lat, absEst.lon, absEst.estimateNumber,absEst.name, coalesce(absEst.workValue,0),")
                        .append(" (select sum(coalesce(ovr.amount,0)) from OverheadValue ovr where ovr.abstractEstimate=absEst)  ,")
                        .append(" (select wpd.worksPackage.wpNumber from WorksPackageDetails wpd where wpd.estimate=absEst")
                        .append(" and wpd.worksPackage.egwStatus.code not in (:wpStatus)), ")
                        .append(" (select to_char(offLineStatus.statusDate,'dd/MM/YYYY') from OfflineStatus offLineStatus")
                        .append(" where offLineStatus.objectId = ( select wpd.worksPackage.id from WorksPackageDetails wpd")
                        .append(" where wpd.estimate=absEst and wpd.worksPackage.egwStatus.code not in (:wpStatus))")
                        .append(" and offLineStatus.egwStatus.code = :olsStatus and objectType='WorksPackage' ), ")
                        .append(" absEst.parentCategory.description, workSubType.description , projcode.id,  ")
                        .append(" to_char(wo.workOrderDate,'dd/MM/YYYY'), wo.contractPeriod, wo.contractor.name, ")
                        .append(" (select to_char(offLineStatus.statusDate,'dd/MM/YYYY') from OfflineStatus offLineStatus")
                        .append(" where offLineStatus.objectId= wo.id and offLineStatus.egwStatus.code = :olsStatusCommenced")
                        .append(" and objectType='WorkOrder') ");

        params.put("olsStatusCommenced", WorksConstants.WO_STATUS_WOCOMMENCED);

        if (contractorId != null && contractorId != -1) {
            query.append("select ").append(columnsToShowWhenWOEIsJoined)
                    .append(" from AbstractEstimate absEst left join absEst.category workSubType left join absEst.projectCode projcode,")
                    .append(" WorkOrder wo, WorkOrderEstimate woe ")
                    .append(" where  absEst.id=woe.estimate.id and wo.id=woe.workOrder.id  and wo.contractor.id = :contractorId")
                    .append(" and absEst.parent is null ");
            params.put("contractorId", contractorId);
        } else
            query.append("select ").append(columnsToShow)
                    .append(" from AbstractEstimate absEst left join absEst.category workSubType left join absEst.projectCode projcode ")
                    .append(" where absEst.parent is null ");
        // Consider only estimates which have lat long
        query.append(" and absEst.lat is not null and absEst.lon is not null and absEst.egwStatus.code not in (:wpStatus) ");
        if (zoneId != null && zoneId != -1) {
            query.append(" and absEst.ward.parent.id = :zoneId");
            params.put("zoneId", zoneId);
        }
        if (wardId != null && wardId != -1) {
            query.append(" and absEst.ward.id= :wardId");
            params.put("wardId", wardId);
        }
        if (category != null && category != -1) {
            query.append(" and absEst.category.id = :category");
            params.put("category", category);
        } else if (parentCategory != null && parentCategory != -1) {
            query.append(" and absEst.parentCategory.id = :parentCategory");
            params.put("parentCategory", parentCategory);
        }
        if (expenditureType != null && expenditureType != -1) {
            query.append(" and absEst.type.id = :expenditureType");
            params.put("expenditureType", expenditureType);
        }
        if (StringUtils.isNotBlank(estimatenumber)) {
            query.append(" and UPPER(absEst.estimateNumber) like :estimateNumber");
            params.put("estimateNumber", "%" + estimatenumber.toUpperCase() + "%");
        }
        query.append(" order by absEst.id ");
        queryMap.put("query", query);
        queryMap.put("params", params);
        return queryMap;
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public String search() {
        List<Object[]> findAll = null;
        locationList = new ArrayList<>();
        GeoLatLong latlong = new GeoLatLong();
        Map<String, Object> markerdata = null;
        final Map<String, Object> queryMap = generateQuery();
        final String queryString = (String) queryMap.get("query");
        final Map<String, Object> params = (Map<String, Object>) queryMap.get("params");
        try {
            LOGGER.info("HQl query=" + queryString.toString());
            final Query query = entityManager.createQuery(queryString);
            params.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
            findAll = query.getResultList();
            LOGGER.info("HQl query RESULT " + findAll.size());
            Long estId, projcodeId, contractPeriod;
            BigDecimal estLat, estLon;
            String estNumber, estWorkName, wpno, tenderDocReleasedDt, typeOfWork, subTypeOfWork, workOrderDate, contractorName,
                    workCommencedDt;
            Money workValue;
            Double ovrheads;
            // Start format and push the data
            GeoLocation geoLocation = null;
            for (final Object[] columnOutput : findAll) {
                // Reset the values
                estId = projcodeId = contractPeriod = null;
                estLat = estLon = null;
                estNumber = estWorkName = wpno = tenderDocReleasedDt = typeOfWork = subTypeOfWork = workOrderDate = contractorName = workCommencedDt = null;
                workValue = null;
                ovrheads = null;

                // Set the values
                estId = (Long) columnOutput[0];
                estLat = (BigDecimal) columnOutput[1];
                estLon = (BigDecimal) columnOutput[2];
                estNumber = (String) columnOutput[3];
                estWorkName = (String) columnOutput[4];
                workValue = (Money) columnOutput[5];
                ovrheads = (Double) columnOutput[6];
                wpno = (String) columnOutput[7];
                tenderDocReleasedDt = (String) columnOutput[8];
                typeOfWork = (String) columnOutput[9];
                subTypeOfWork = (String) columnOutput[10];
                projcodeId = (Long) columnOutput[11];

                geoLocation = new GeoLocation();
                if (null != estLat && null != estLon) {
                    latlong = new GeoLatLong();
                    latlong.setLatitude(estLat);
                    latlong.setLongitude(estLat);
                }
                geoLocation.setGeoLatLong(latlong);
                // **geoLocation.setUseNiceInfoWindow(true);
                geoLocation.setUrlRedirect("../estimate/abstractEstimate!edit.action?id=" + estId
                        + "&sourcepage=search~" + estNumber);
                String max50CharName = "";
                if (null != estWorkName && estWorkName.length() >= 50)
                    max50CharName = estWorkName.substring(0, 50) + "...";
                else
                    max50CharName = estWorkName;
                geoLocation.setInfo2("Work Name=" + max50CharName);
                if (workValue != null || ovrheads != null) {
                    BigDecimal amt = workValue != null ? new BigDecimal(workValue.getValue()) : BigDecimal.ZERO;
                    amt = amt.add(ovrheads != null ? new BigDecimal(ovrheads) : BigDecimal.ZERO);
                    geoLocation.setInfo3("Estimate Value(Rs)="
                            + NumberUtil.formatNumber(amt, NumberUtil.NumberFormatStyle.CRORES));
                } else
                    geoLocation.setInfo3("Estimate Value(Rs)=0.00");
                if (wpno != null)
                    geoLocation.setInfo4("Works Package Number=" + wpno);
                markerdata = new HashMap<>();
                if (typeOfWork != null) {
                    geoLocation.appendToInfo5("Type of Work=" + typeOfWork);
                    if (typeOfWork.equalsIgnoreCase(WorksConstants.TYPE_OF_WORK_BRIDGES))
                        markerdata.put("icon", GeoLocationConstants.MARKEROPTION_ICON_PURPLE);
                    else if (typeOfWork.equalsIgnoreCase(WorksConstants.TYPE_OF_WORK_BUILDINGS))
                        markerdata.put("icon", GeoLocationConstants.MARKEROPTION_ICON_YELLOW);
                    else if (typeOfWork.equalsIgnoreCase(WorksConstants.TYPE_OF_WORK_ELECTRICAL))
                        markerdata.put("icon", GeoLocationConstants.MARKEROPTION_ICON_GREEN);
                    else if (typeOfWork.equalsIgnoreCase(WorksConstants.TYPE_OF_WORK_ROADS))
                        markerdata.put("icon", GeoLocationConstants.MARKEROPTION_ICON_ORANGE);
                    else if (typeOfWork.equalsIgnoreCase(WorksConstants.TYPE_OF_WORK_STORMWATER_DRAIN))
                        markerdata.put("icon", GeoLocationConstants.MARKEROPTION_ICON_BLUE);
                }
                if (subTypeOfWork != null)
                    geoLocation.appendToInfo5("Subtype of Work=" + subTypeOfWork);
                if (tenderDocReleasedDt != null)
                    geoLocation.appendToInfo5("Tender Document Released Date=" + tenderDocReleasedDt);
                final String tenderAmount = getTenderAmount(estId);
                if (StringUtils.isNotBlank(tenderAmount))
                    geoLocation.appendToInfo5("Tender Finalized Value(Rs)=" + tenderAmount);
                if (contractorId != null && contractorId != -1) {
                    workOrderDate = (String) columnOutput[12];
                    if (columnOutput[13] != null)
                        contractPeriod = Long.parseLong(columnOutput[13].toString());
                    contractorName = (String) columnOutput[14];
                    workCommencedDt = (String) columnOutput[15];
                    if (workOrderDate != null)
                        geoLocation.appendToInfo5("Work Order Date=" + workOrderDate);
                    if (workCommencedDt != null)
                        geoLocation.appendToInfo5("Work Commenced Date=" + workCommencedDt);
                    if (contractPeriod != null)
                        geoLocation.appendToInfo5("Contract Period In Days=" + contractPeriod);
                    if (workCommencedDt != null && contractPeriod != null) {
                        final Date woDate = new Date(workCommencedDt);
                        final Calendar cal = Calendar.getInstance();
                        cal.setTime(woDate);
                        cal.add(Calendar.DATE, new Long(contractPeriod).intValue());
                        geoLocation.appendToInfo5("Expected Date Of Completion =" + sdf.format(cal.getTime()));
                    }
                    if (projcodeId != null) {
                        final String paymentAmt = getPaymentAmount(projcodeId);
                        if (paymentAmt != null)
                            geoLocation.appendToInfo5("Payment Released(Rs)=" + paymentAmt);
                    }
                    if (contractorName != null)
                        geoLocation.appendToInfo5("Contractor Name=" + contractorName);
                } else {
                    final List<WorkOrder> woList = entityManager.createQuery(
                            new StringBuffer("select woe.workOrder from WorkOrderEstimate woe")
                                    .append(" where woe.estimate.id = :estimateId and upper(woe.workOrder.egwStatus.code) not in (:woStatus) ")
                                    .toString(),
                            WorkOrder.class)
                            .setParameter("estimateId", estId)
                            .setParameter("woStatus", Arrays.asList(WorksConstants.NEW, WorksConstants.CANCELLED_STATUS))
                            .getResultList();
                    if (woList != null && woList.size() > 0) {
                        final StringBuffer workOrderDatesBuf = new StringBuffer("");
                        final StringBuffer workCommencedDateBuf = new StringBuffer("");
                        final StringBuffer contractorNameBuf = new StringBuffer("");
                        final StringBuffer workExpectedCompletionDateBuf = new StringBuffer("");
                        final StringBuffer workOrderContractPeriodBuf = new StringBuffer("");
                        for (final WorkOrder wo : woList) {
                            Long workOrderContractPeriod = null;
                            workOrderDatesBuf.append(sdf.format(wo.getWorkOrderDate()) + ",");
                            contractorNameBuf.append(wo.getContractor().getName() + ",");
                            if (wo.getContractPeriod() != null) {
                                workOrderContractPeriod = Long.valueOf(wo.getContractPeriod());
                                workOrderContractPeriodBuf.append(wo.getContractPeriod() + ",");
                            } else
                                workOrderContractPeriodBuf.append("NA,");
                            boolean workCommencedPresent = false;
                            boolean workExpectedCompletedPresent = false;
                            for (final OfflineStatus ss : wo.getOfflineStatuses())
                                if (ss.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.WO_STATUS_WOCOMMENCED)) {
                                    workCommencedDateBuf.append(sdf.format(ss.getStatusDate()) + ",");
                                    workCommencedPresent = true;
                                    if (workOrderContractPeriod != null) {
                                        final Calendar cal = Calendar.getInstance();
                                        cal.setTime(wo.getWorkOrderDate());
                                        cal.add(Calendar.DATE, workOrderContractPeriod.intValue());
                                        workExpectedCompletionDateBuf.append(sdf.format(cal.getTime()) + ",");
                                        workExpectedCompletedPresent = true;
                                    }
                                }
                            if (!workCommencedPresent)
                                workCommencedDateBuf.append("NA,");
                            if (!workExpectedCompletedPresent)
                                workExpectedCompletionDateBuf.append("NA,");
                        }
                        geoLocation.appendToInfo5("Work Order Date="
                                + workOrderDatesBuf.deleteCharAt(workOrderDatesBuf.lastIndexOf(",")).toString());
                        if (StringUtils.isNotBlank(workCommencedDateBuf.toString()))
                            geoLocation.appendToInfo5("Work Commenced Date="
                                    + workCommencedDateBuf.deleteCharAt(workCommencedDateBuf.lastIndexOf(","))
                                            .toString());
                        if (StringUtils.isNotBlank(workOrderContractPeriodBuf.toString()))
                            geoLocation.appendToInfo5("Contract Period In Days="
                                    + workOrderContractPeriodBuf.deleteCharAt(
                                            workOrderContractPeriodBuf.lastIndexOf(",")).toString());
                        if (StringUtils.isNotBlank(workExpectedCompletionDateBuf.toString()))
                            geoLocation.appendToInfo5("Expected Date Of Completion="
                                    + workExpectedCompletionDateBuf.deleteCharAt(
                                            workExpectedCompletionDateBuf.lastIndexOf(",")).toString());
                        if (projcodeId != null) {
                            final String paymentAmt = getPaymentAmount(projcodeId);
                            if (paymentAmt != null)
                                geoLocation.appendToInfo5("Payment Released(Rs)=" + paymentAmt);
                        }
                        geoLocation.appendToInfo5("Contractor Name="
                                + contractorNameBuf.deleteCharAt(contractorNameBuf.lastIndexOf(",")).toString());
                    }
                }
                geoLocation.setMarkerOptionData(markerdata);
                locationList.add(geoLocation);
            }
            ServletActionContext.getRequest().setAttribute("kmlfilename", "coczone");
            ServletActionContext.getRequest()
                    .setAttribute(GeoLocationConstants.GEOLOCATIONLIST_ATTRIBUTE, locationList);
            resultStatus = AFTER_SEARCH;
            if (locationList != null && locationList.size() >= 1)
                return GMAP;
        } catch (final Exception e) {
            LOGGER.error(e, e);
        }
        return GMAP;
    }

    private String getTenderAmount(final Object estimateId) {
        final List<WorksPackageDetails> wpdDetailsList = entityManager.createQuery(
                "select wpd from WorksPackageDetails wpd where wpd.estimate.id = :estimateId", WorksPackageDetails.class)
                .setParameter("estimateId", estimateId)
                .getResultList();
        if (tenderTypeList == null || tenderTypeList.size() == 0)
            tenderTypeList = worksService.getTendertypeList();
        double totalAmt = 0;
        if (wpdDetailsList != null)
            for (final WorksPackageDetails wpd : wpdDetailsList)
                for (final TenderEstimate te : wpd.getWorksPackage().getTenderEstimateSet())
                    for (final TenderResponse tr : te.getTenderResponseSet())
                        // Consider only approved Tender Response
                        if (WorksConstants.APPROVED.equals(tr.getEgwStatus().getCode())) {
                            final List<TenderResponseActivity> trAct = entityManager.createQuery(
                                    new StringBuffer("from TenderResponseActivity trAct")
                                            .append(" where trAct.activity.abstractEstimate.id = :estimateId")
                                            .append(" and trAct.tenderResponse.id = :tenderResponseId")
                                            .append(" and trAct.tenderResponse.egwStatus.code = :tenderResponseStatus")
                                            .toString(),
                                    TenderResponseActivity.class)
                                    .setParameter("estimateId", wpd.getEstimate().getId())
                                    .setParameter("tenderResponseId", tr.getId())
                                    .setParameter("tenderResponseStatus", WorksConstants.APPROVED)
                                    .getResultList();

                            for (final TenderResponseActivity act : trAct)
                                if (tr.getTenderEstimate().getTenderType().equals(tenderTypeList.get(1)))
                                    totalAmt += act.getNegotiatedQuantity() * act.getNegotiatedRate()
                                            * act.getActivity().getConversionFactor();

                            if (tr.getTenderEstimate().getTenderType().equals(tenderTypeList.get(0)))
                                totalAmt += wpd.getEstimate().getWorkValue()
                                        + wpd.getEstimate().getWorkValue()
                                                * tr.getPercNegotiatedAmountRate() / 100;
                            break;
                        }
        if (totalAmt != 0)
            return NumberUtil.formatNumber(new BigDecimal(totalAmt), NumberUtil.NumberFormatStyle.CRORES);
        else
            return null;
    }

    private String getPaymentAmount(final Object object) {
        try {
            final BigDecimal amt = worksService.getTotalPaymentForProjectCode((Long) object);
            if (amt != null)
                return NumberUtil.formatNumber(amt, NumberUtil.NumberFormatStyle.CRORES);
            else
                return null;
        } catch (final ApplicationException e) {
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    public Map<String, Object> getContractorForApprovedWorkOrder() {
        final Map<String, Object> contractorsWithWOList = new HashMap<>();
        if (workOrderService.getContractorsWithWO() != null)
            for (final Contractor contractor : workOrderService.getContractorsWithWO())
                contractorsWithWOList.put(contractor.getId() + "", contractor.getName() + " - " + contractor.getCode());
        return contractorsWithWOList;
    }

    public void setWorkOrderService(final WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    public Integer getZoneId() {
        return zoneId;
    }

    public void setZoneId(final Integer zoneId) {
        this.zoneId = zoneId;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(final Integer wardId) {
        this.wardId = wardId;
    }

    public Long getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(final Long parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(final Long category) {
        this.category = category;
    }

    public Long getExpenditureType() {
        return expenditureType;
    }

    public void setExpenditureType(final Long expenditureType) {
        this.expenditureType = expenditureType;
    }

    public List<GeoLocation> getLocationList() {
        return locationList;
    }

    public void setLocationList(final List<GeoLocation> locationList) {
        this.locationList = locationList;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public String getEstimatenumber() {
        return estimatenumber;
    }

    public void setEstimatenumber(final String estimatenumber) {
        this.estimatenumber = estimatenumber;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }
}