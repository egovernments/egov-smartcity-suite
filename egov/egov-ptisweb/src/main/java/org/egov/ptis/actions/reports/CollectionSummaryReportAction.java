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

package org.egov.ptis.actions.reports;

import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.property.PropertyUsageDAO;
import org.egov.ptis.domain.entity.property.CollectionSummary;
import org.egov.ptis.domain.entity.property.CollectionSummaryDetails;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ZERO;
import static org.egov.infra.web.struts.actions.BaseFormAction.VIEW;
import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.ptis.constants.PropertyTaxConstants.COLL_MODES_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
@Results({ @Result(name = VIEW, location = "collectionSummaryReport-view.jsp") })
public class CollectionSummaryReportAction extends BaseFormAction {
    /**
     *
     */
    private static final long serialVersionUID = -3560529685172919434L;
    private final Logger LOGGER = Logger.getLogger(getClass());
    
    private Map<Long, String> zoneBndryMap;
    private Map<Long, String> wardBndryMap;
    private Map<Long, String> blockBndryMap;
    private Map<Long, String> localityBndryMap;
    private Map<Character, String> collectionModesMap;
    private List<Map<String, Object>> resultList;
    private List<PropertyUsage> propUsageList;
    
    @Autowired
    public PropertyTaxUtil propertyTaxUtil;
    @Autowired
    public FinancialYearDAO financialYearDAO;
    @Autowired
    public PropertyUsageDAO propertyUsageDAO;
    @Autowired
    private BoundaryService boundaryService;
    
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    public static final String ZONEWISE = "zoneWise";
    public static final String WARDWISE = "wardWise";
    public static final String BLOCKWISE = "blockWise";
    public static final String LOCALITYWISE = "localityWise";
    public static final String USAGEWISE = "usageWise";
    private static final String CURR_DATE = "currentDate";
    
    private Long zoneId;
    private Long wardId;
    private Long blockId;
    
    private String propTypeCategoryId;
    private String finYearStartDate;
    private String dateSelected;
    private String fromDate;
    private String toDate;
    private String boundaryId;
    private String collMode;
    private String transMode;
    private String mode;

    BigDecimal taxAmount = ZERO, totTaxAmt = ZERO, arrearTaxAmount = ZERO, totArrearTaxAmt = ZERO, penaltyAmount = ZERO,
            totPenaltyAmt = ZERO;
    BigDecimal arrearPenaltyAmount = ZERO, totArrearPenaltyAmt = ZERO, libCessAmount = ZERO, totLibCessAmt = ZERO,
            arrearLibCessAmount = ZERO;
    BigDecimal totArrearLibCessAmt = ZERO, grandTotal = ZERO;
    Long prevZone = null, prevWard = null, prevBlock = null, prevLocality = null;
    String prevPropertyType = null;

    @Override
    public Object getModel() {

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void prepare() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into prepare method");
        super.prepare();
        setZoneBndryMap(CommonServices.getFormattedBndryMap(boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Zone", REVENUE_HIERARCHY_TYPE)));
        setWardBndryMap(CommonServices.getFormattedBndryMap(boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Ward", REVENUE_HIERARCHY_TYPE)));
        setBlockBndryMap(CommonServices.getFormattedBndryMap(boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Block", REVENUE_HIERARCHY_TYPE)));
        setLocalityBndryMap(CommonServices.getFormattedBndryMap(boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Locality", LOCATION_HIERARCHY_TYPE)));
        addDropdownData("instrumentTypeList", propertyTaxUtil.prepareInstrumentTypeList());
        setCollectionModesMap(COLL_MODES_MAP);
        final CFinancialYear finyear = financialYearDAO.getFinancialYearByDate(new Date());
        if (finyear != null)
            finYearStartDate = sdf.format(finyear.getStartingDate());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from prepare method");

        super.prepare();
        final List<Boundary> zoneList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Zone",
        		REVENUE_HIERARCHY_TYPE);
        addDropdownData("zoneList", zoneList);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Zone id : " + zoneId + ", " + "Ward id : " + wardId);
        if (wardId == null || wardId.equals(-1))
            addDropdownData("blockList", Collections.EMPTY_LIST);
        prepareBlockDropDownData(wardId != null, blockId != null);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from prepare method");
        propUsageList = propertyUsageDAO.getPropUsageAscOrder();
        addDropdownData("propUsageList", propUsageList);
    }

    /**
     * Loads block based on selected ward
     * @param wardExists
     * @param blockExists
     */
    @SuppressWarnings("unchecked")
    private void prepareBlockDropDownData(final boolean wardExists, final boolean blockExists) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into prepareBlockDropDownData method");
            LOGGER.debug("Ward Exists ? : " + wardExists + ", " + "Block Exists ? : " + blockExists);
        }
        if (wardExists && blockExists) {
            List<Boundary> blockList = new ArrayList<Boundary>();
            blockList = boundaryService.getActiveChildBoundariesByBoundaryId(getWardId());
            addDropdownData("blockList", blockList);
        } else
            addDropdownData("blockList", Collections.EMPTY_LIST);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from prepareWardDropDownData method");
    }

    /**
     * @return to Zonewise Collection Summary Search Screen
     */
    @SkipValidation
    @Action(value = "/reports/collectionSummaryReport-zoneWise")
    public String zoneWise() {
        fromDate = finYearStartDate;
        toDate = sdf.format(new Date());
        setMode("zoneWise");
        return VIEW;
    }

    /**
     * @return to Wardwise Collection Summary Search Screen
     */
    @SkipValidation
    @Action(value = "/reports/collectionSummaryReport-wardWise")
    public String wardWise() {
        fromDate = finYearStartDate;
        toDate = sdf.format(new Date());
        setMode("wardWise");
        return VIEW;
    }

    /**
     * @return to Blockwise Collection Summary Search Screen
     */
    @SkipValidation
    @Action(value = "/reports/collectionSummaryReport-blockWise")
    public String blockWise() {
        fromDate = finYearStartDate;
        toDate = sdf.format(new Date());
        setMode("blockWise");
        return VIEW;
    }

    /**
     * @return to Localitywise Collection Summary Search Screen
     */
    @SkipValidation
    @Action(value = "/reports/collectionSummaryReport-localityWise")
    public String localityWise() {
        fromDate = finYearStartDate;
        toDate = sdf.format(new Date());
        setMode("localityWise");
        return VIEW;
    }

    /**
     * @return to Property Usagewise Collection Summary Search Screen
     */
    @SkipValidation
    @Action(value = "/reports/collectionSummaryReport-usageWise")
    public String usageWise() {
        fromDate = finYearStartDate;
        toDate = sdf.format(new Date());
        setMode("usageWise");
        return VIEW;
    }

    /**
     * Invoked from Collection Summary screens to retrieve aggregated collection summary for selected zone / ward / block /
     * locality or usagetype
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    @ValidationErrorPage(value = "view")
    @Action(value = "/reports/collectionSummaryReport-list")
    public void list() throws ParseException, IOException {
        List<CollectionSummaryReportResult> resultList = new ArrayList<CollectionSummaryReportResult>();
        String result = null;
        final Query query = prepareQuery();
        resultList = prepareOutput(query.list());
        // for converting resultList to JSON objects.
        // Write back the JSON Response.
        result = new StringBuilder("{ \"data\":").append(toJSON(resultList, CollectionSummaryReportResult.class,
                CollectionSummaryReportHelperAdaptor.class)).append("}").toString();
        final HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(result, response.getWriter());
    }

    /**
     * @return
     */
    public Query prepareQuery() {
        try {
            final String currDate = sdf.format(new Date());
            if (currDate.equals(fromDate) || currDate.equals(toDate))
                dateSelected = CURR_DATE;
            return propertyTaxUtil.prepareQueryforCollectionSummaryReport(fromDate, toDate, collMode, transMode, mode,
                    boundaryId,
                    propTypeCategoryId, zoneId, wardId, blockId);
        } catch (final Exception e) {

            LOGGER.error("Error occured in Class : CollectionSummaryReportAction  Method : list", e);
            throw new ApplicationRuntimeException("Error occured in Class : CollectionSummaryReportAction  Method : list "
                    + e.getMessage());
        }
    }

    /**
     * @param collectionSummaryList
     * @return
     * @throws ParseException
     */
    private List<CollectionSummaryReportResult> prepareOutput(final List<CollectionSummary> collectionSummaryList)
            throws ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into prepareResultList method");
        final List<CollectionSummaryReportResult> csrFinalList = new LinkedList<CollectionSummaryReportResult>();

        try {
            if (collectionSummaryList != null && !collectionSummaryList.isEmpty()) {
                for (final CollectionSummary collSummary : collectionSummaryList) {
                    List<CollectionSummaryDetails> collDetails = new ArrayList<CollectionSummaryDetails>(collSummary.getCollectionDetails());
                    CollectionSummaryDetails summaryDetails = collDetails.get(0);
                    if (prevZone == null && prevWard == null && prevBlock == null && prevLocality == null
                            && prevPropertyType == null)
                        initializeReasonAmount(collSummary,summaryDetails);
                    else if (prevZone != null && prevZone.equals(collSummary.getZoneId().getId())
                            || prevWard != null && prevWard.equals(collSummary.getWardId().getId())
                            || prevBlock != null && prevBlock.equals(collSummary.getAreaId().getId())
                            || prevLocality != null && prevLocality.equals(collSummary.getLocalityId().getId())
                            || prevPropertyType != null &&
                            prevPropertyType.equalsIgnoreCase(collSummary.getProperty().getPropertyDetail().getCategoryType())) {
                        if (taxAmount != null)
                            taxAmount = summaryDetails.getCurrentTaxColl() != null ? taxAmount.add(summaryDetails.getCurrentTaxColl())
                                    : taxAmount;
                        else
                            taxAmount = summaryDetails.getCurrentTaxColl();
                        if (arrearTaxAmount != null)
                            arrearTaxAmount = summaryDetails.getArrearTaxColl() != null ? arrearTaxAmount.add(summaryDetails
                                    .getArrearTaxColl())
                                    : arrearTaxAmount;
                        else
                            arrearTaxAmount = summaryDetails.getArrearTaxColl();
                        if (penaltyAmount != null)
                            penaltyAmount = summaryDetails.getPenaltyColl() != null ? penaltyAmount.add(summaryDetails
                                    .getPenaltyColl()) : penaltyAmount;
                        else
                            penaltyAmount = summaryDetails.getPenaltyColl();
                        if (arrearPenaltyAmount != null)
                            arrearPenaltyAmount = summaryDetails.getArrearPenaltyColl() != null ? arrearPenaltyAmount
                                    .add(summaryDetails
                                            .getArrearPenaltyColl()) : arrearPenaltyAmount;
                        else
                            arrearPenaltyAmount = summaryDetails.getArrearPenaltyColl();
                        if (libCessAmount != null)
                            libCessAmount = summaryDetails.getLibCessColl() != null ? libCessAmount.add(summaryDetails
                                    .getLibCessColl()) : libCessAmount;
                        else
                            libCessAmount = summaryDetails.getLibCessColl();
                        if (arrearLibCessAmount != null)
                            arrearLibCessAmount = summaryDetails.getArrearLibCessColl() != null ? arrearLibCessAmount
                                    .add(summaryDetails
                                            .getArrearLibCessColl()) : arrearLibCessAmount;
                        else
                            arrearLibCessAmount = summaryDetails.getArrearLibCessColl();
                    } else {
                        csrFinalList.add(getCalculatedResultMap());
                        initializeReasonAmount(collSummary,summaryDetails);
                    }
                }
                // Last Row
                csrFinalList.add(getCalculatedResultMap());
            }
        } catch (final Exception e) {
            LOGGER.error("Exception in prepareBndryWiseResultList method : " + e.getMessage());

            throw new ApplicationRuntimeException("Exception in prepareBndryWiseResultList method : ", e);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from prepareResultList method");
        return csrFinalList;
    }

    /**
     * @param collSummary
     */
    private void initializeReasonAmount(final CollectionSummary collSummary,final CollectionSummaryDetails details) {
        if (mode.equals(ZONEWISE))
            prevZone = collSummary.getZoneId().getId();
        else if (mode.equals(WARDWISE))
            prevWard = collSummary.getWardId().getId();
        else if (mode.equals(BLOCKWISE))
            prevBlock = collSummary.getAreaId().getId();
        else if (mode.equals(LOCALITYWISE))
            prevLocality = collSummary.getLocalityId().getId();
        else if (mode.equals(USAGEWISE))
            prevPropertyType = collSummary.getProperty().getPropertyDetail().getCategoryType();
        taxAmount = details.getCurrentTaxColl();
        arrearTaxAmount = details.getArrearTaxColl();
        penaltyAmount = details.getPenaltyColl();
        arrearPenaltyAmount = details.getArrearPenaltyColl();
        libCessAmount = details.getLibCessColl();
        arrearLibCessAmount = details.getArrearLibCessColl();
    }

    /**
     * @return
     */
    private CollectionSummaryReportResult getCalculatedResultMap() {
        final CollectionSummaryReportResult result = new CollectionSummaryReportResult();

        if (mode.equals(ZONEWISE))
            result.setBoundaryName(boundaryService.getBoundaryById(prevZone).getName());
        else if (mode.equals(WARDWISE))
            result.setBoundaryName(boundaryService.getBoundaryById(prevWard).getName());
        else if (mode.equals(BLOCKWISE))
            result.setBoundaryName(boundaryService.getBoundaryById(prevBlock).getName());
        else if (mode.equals(LOCALITYWISE))
            result.setBoundaryName(boundaryService.getBoundaryById(prevLocality).getName());
        else if (mode.equals(USAGEWISE))
            result.setPropertyType(prevPropertyType);
        
        result.setArrearTaxAmount(arrearTaxAmount != null ? arrearTaxAmount : ZERO);
        totArrearTaxAmt = arrearTaxAmount != null ? totArrearTaxAmt.add(arrearTaxAmount) : totArrearTaxAmt
                .add(ZERO);
        result.setArrearLibraryCess(arrearLibCessAmount != null ? arrearLibCessAmount : ZERO);
        totArrearLibCessAmt = arrearLibCessAmount != null ? totArrearLibCessAmt.add(arrearLibCessAmount) : totArrearLibCessAmt
                .add(ZERO);
        result.setArrearTotal(totArrearTaxAmt.add(totArrearLibCessAmt));

        result.setTaxAmount(taxAmount);
        totTaxAmt = taxAmount != null ? totTaxAmt.add(taxAmount) : totTaxAmt.add(ZERO);
        result.setLibraryCess(libCessAmount != null ? libCessAmount : ZERO);
        totLibCessAmt = libCessAmount != null ? totLibCessAmt.add(libCessAmount) : totLibCessAmt
                .add(ZERO);
        result.setCurrentTotal(totTaxAmt.add(totLibCessAmt));

        result.setPenalty(penaltyAmount != null ? penaltyAmount : ZERO);
        totPenaltyAmt = penaltyAmount != null ? totPenaltyAmt.add(penaltyAmount) : totPenaltyAmt
                .add(ZERO);
        result.setArrearPenalty(arrearPenaltyAmount != null ? arrearPenaltyAmount : ZERO);
        totArrearPenaltyAmt = arrearPenaltyAmount != null ? totArrearPenaltyAmt.add(arrearPenaltyAmount) : totArrearPenaltyAmt
                .add(ZERO);
        result.setPenaltyTotal(totPenaltyAmt.add(totArrearPenaltyAmt));
        
        taxAmount = taxAmount != null ? taxAmount : ZERO;
        if (arrearTaxAmount != null)
            taxAmount = taxAmount.add(arrearTaxAmount);
        if (penaltyAmount != null)
            taxAmount = taxAmount.add(penaltyAmount);
        if (arrearPenaltyAmount != null)
            taxAmount = taxAmount.add(arrearPenaltyAmount);
        if (libCessAmount != null)
            taxAmount = taxAmount.add(libCessAmount);
        if (arrearLibCessAmount != null)
            taxAmount = taxAmount.add(arrearLibCessAmount);
        result.setTotal(taxAmount);
        return result;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public Map<Character, String> getCollectionModesMap() {
        return collectionModesMap;
    }

    public void setCollectionModesMap(final Map<Character, String> collectionModesMap) {
        this.collectionModesMap = collectionModesMap;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(final String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(final String toDate) {
        this.toDate = toDate;
    }

    public String getCollMode() {
        return collMode;
    }

    public void setCollMode(final String collMode) {
        this.collMode = collMode;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(final String transMode) {
        this.transMode = transMode;
    }

    public List<Map<String, Object>> getResultList() {
        return resultList;
    }

    public void setResultList(final List<Map<String, Object>> resultList) {
        this.resultList = resultList;
    }

    public String getDateSelected() {
        return dateSelected;
    }

    public void setDateSelected(final String dateSelected) {
        this.dateSelected = dateSelected;
    }

    public String getBoundaryId() {
        return boundaryId;
    }

    public void setBoundaryId(final String boundaryId) {
        this.boundaryId = boundaryId;
    }

    public Map<Long, String> getWardBndryMap() {
        return wardBndryMap;
    }

    public void setWardBndryMap(final Map<Long, String> wardBndryMap) {
        this.wardBndryMap = wardBndryMap;
    }

    public Map<Long, String> getBlockBndryMap() {
        return blockBndryMap;
    }

    public void setBlockBndryMap(final Map<Long, String> blockBndryMap) {
        this.blockBndryMap = blockBndryMap;
    }

    public Map<Long, String> getLocalityBndryMap() {
        return localityBndryMap;
    }

    public void setLocalityBndryMap(final Map<Long, String> localityBndryMap) {
        this.localityBndryMap = localityBndryMap;
    }

    public Map<Long, String> getZoneBndryMap() {
        return zoneBndryMap;
    }

    public void setZoneBndryMap(final Map<Long, String> zoneBndryMap) {
        this.zoneBndryMap = zoneBndryMap;
    }

    public List<PropertyUsage> getPropUsageList() {
        return propUsageList;
    }

    public void setPropUsageList(List<PropertyUsage> propUsageList) {
        this.propUsageList = propUsageList;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(final Long zoneId) {
        this.zoneId = zoneId;
    }

    public Long getWardId() {
        return wardId;
    }

    public void setWardId(final Long wardId) {
        this.wardId = wardId;
    }

    public String getPropTypeCategoryId() {
        return propTypeCategoryId;
    }

    public void setPropTypeCategoryId(final String propTypeCategoryId) {
        this.propTypeCategoryId = propTypeCategoryId;
    }

    public String getFinYearStartDate() {
        return finYearStartDate;
    }

    public void setFinYearStartDate(final String finYearStartDate) {
        this.finYearStartDate = finYearStartDate;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

}
