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

package org.egov.ptis.service.dashboard;

import static java.lang.String.format;
import static org.egov.ptis.constants.PropertyTaxConstants.BIGDECIMAL_100;
import static org.egov.ptis.constants.PropertyTaxConstants.COLLECION_BILLING_SERVICE_PT;
import static org.egov.ptis.constants.PropertyTaxConstants.COLLECION_BILLING_SERVICE_WTMS;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_ALLWARDS;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_BILLCOLLECTORWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_REVENUEINSPECTORWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_REVENUEOFFICERWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CFinancialYearService;
import org.egov.infra.admin.master.entity.es.CityIndex;
import org.egov.infra.admin.master.service.es.CityIndexService;
import org.egov.infra.rest.client.SimpleRestClient;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.utils.WebUtils;
import org.egov.ptis.bean.dashboard.*;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.service.property.CollectionAchievementsService;
import org.egov.ptis.service.es.CollectionIndexElasticSearchService;
import org.egov.ptis.service.es.PropertyTaxElasticSearchIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to provide APIs for CM Dashboard
 */

@Service
public class PropTaxDashboardService {

    @Autowired
    private CFinancialYearService cFinancialYearService;

    @Autowired
    private CollectionIndexElasticSearchService collectionIndexElasticSearchService;

    @Autowired
    private PropertyTaxElasticSearchIndexService propertyTaxElasticSearchIndexService;

    @Autowired
    private SimpleRestClient simpleRestClient;

    @Autowired
    private CityIndexService cityIndexService;
    
    @Autowired
    private CollectionAchievementsService collectionAchievementsService;

    /**
     * Gives the State-City information across all ULBs
     * 
     * @return List
     */
    public List<StateCityInfo> getStateCityDetails() {
        List<StateCityInfo> stateCityDetails = new ArrayList<>();
        StateCityInfo cityInfo;
        Iterable<CityIndex> cities = cityIndexService.findAll();
        for (CityIndex city : cities) {
            cityInfo = new StateCityInfo();
            cityInfo.setRegion(city.getRegionname());
            cityInfo.setDistrict(city.getDistrictname());
            cityInfo.setCity(city.getName());
            cityInfo.setGrade(city.getCitygrade());
            cityInfo.setUlbCode(city.getCitycode());
            stateCityDetails.add(cityInfo);
        }
        return stateCityDetails;
    }

    /**
     * Provides State-wise Collection Statistics for Property Tax, Water Charges and Others
     * 
     * @return CollectionStats
     */
    public TotalCollectionStats getTotalCollectionStats(final HttpServletRequest request) {
        TotalCollectionStats consolidatedCollectionDetails = new TotalCollectionStats();
        // For Property Tax collections
        CollectionStats consolidatedData = new CollectionStats();
        Map<String, BigDecimal> consolidatedColl = collectionIndexElasticSearchService
                .getFinYearsCollByService(COLLECION_BILLING_SERVICE_PT);
        CFinancialYear currFinYear = getCurrentFinancialYear();

        if (!consolidatedColl.isEmpty()) {
            consolidatedData.setCytdColl(consolidatedColl.get("cytdColln"));
            consolidatedData.setLytdColl(consolidatedColl.get("lytdColln"));
        }
        BigDecimal totalDmd = propertyTaxElasticSearchIndexService.getTotalDemand();
        int noOfMonths = DateUtils
                .noOfMonthsBetween(DateUtils.startOfDay(currFinYear.getStartingDate()), new Date()) + 1;
        consolidatedData.setTotalDmd(totalDmd.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(noOfMonths)));
        if (consolidatedData.getTotalDmd().compareTo(BigDecimal.ZERO) > 0)
            consolidatedData.setPerformance(consolidatedData.getCytdColl().multiply(BIGDECIMAL_100)
                    .divide(consolidatedData.getTotalDmd(), 1, BigDecimal.ROUND_HALF_UP));
        if (consolidatedData.getLytdColl().compareTo(BigDecimal.ZERO) > 0)
            consolidatedData.setLyVar(
                    (consolidatedData.getCytdColl().subtract(consolidatedData.getLytdColl()).multiply(BIGDECIMAL_100))
                            .divide(consolidatedData.getLytdColl(), 1, BigDecimal.ROUND_HALF_UP));
        consolidatedCollectionDetails.setPropertyTax(consolidatedData);

        // For Water Tax collections
        consolidatedData = new CollectionStats();
        consolidatedColl = collectionIndexElasticSearchService.getFinYearsCollByService(COLLECION_BILLING_SERVICE_WTMS);
        if (!consolidatedColl.isEmpty()) {
            consolidatedData.setCytdColl(consolidatedColl.get("cytdColln"));
            consolidatedData.setLytdColl(consolidatedColl.get("lytdColln"));
        }
        BigDecimal totalDemandValue = getWaterChargeTotalDemand(request);
        int numberOfMonths = DateUtils.noOfMonthsBetween(DateUtils.startOfDay(currFinYear.getStartingDate()), new Date()) + 1;
        consolidatedData.setTotalDmd(totalDemandValue.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(numberOfMonths)));
        if (consolidatedData.getTotalDmd().compareTo(BigDecimal.ZERO) > 0)
            consolidatedData.setPerformance(consolidatedData.getCytdColl().multiply(BIGDECIMAL_100)
                    .divide(consolidatedData.getTotalDmd(), 1, BigDecimal.ROUND_HALF_UP));
        if (consolidatedData.getLytdColl().compareTo(BigDecimal.ZERO) > 0)
            consolidatedData.setLyVar(
                    (consolidatedData.getCytdColl().subtract(consolidatedData.getLytdColl()).multiply(BIGDECIMAL_100))
                            .divide(consolidatedData.getLytdColl(), 1, BigDecimal.ROUND_HALF_UP));
        consolidatedCollectionDetails.setWaterTax(consolidatedData);

        // Other collections - temporarily set to 0
        consolidatedData = new CollectionStats();
        consolidatedData.setCytdColl(BigDecimal.ZERO);
        consolidatedData.setTotalDmd(BigDecimal.ZERO);
        consolidatedData.setLytdColl(BigDecimal.ZERO);
        consolidatedData.setPerformance(BigDecimal.ZERO);
        consolidatedData.setLyVar(BigDecimal.ZERO);
        consolidatedCollectionDetails.setOthers(consolidatedData);

        return consolidatedCollectionDetails;
    }

    public CFinancialYear getCurrentFinancialYear() {
        return cFinancialYearService.getFinancialYearByDate(new Date());
    }

    /**
     * Gives the total demand for Water Charges
     * 
     * @param request
     * @return BigDecimal
     */
    public BigDecimal getWaterChargeTotalDemand(final HttpServletRequest request) {
        final String wtmsRestURL = format(PropertyTaxConstants.WTMS_TOTALDEMAND_RESTURL,
                WebUtils.extractRequestDomainURL(request, false));
        final Map<String, Object> waterTaxInfo = simpleRestClient.getRESTResponseAsMap(wtmsRestURL);
        return waterTaxInfo.get("currentDemand") == null ? BigDecimal.ZERO
                : new BigDecimal(Double.valueOf((Double) waterTaxInfo.get("currentDemand")));
    }

    /**
     * Gives the Collection Index details across all ULBs
     * 
     * @param collectionDetailsRequest
     * @return CollectionIndexDetails
     */
    public CollectionDetails getCollectionIndexDetails(CollectionDetailsRequest collectionDetailsRequest, boolean isForMISReports) {
        CollectionDetails collectionIndexDetails = new CollectionDetails();
        List<CollTableData> collIndexData;
        if (!DASHBOARD_GROUPING_ALLWARDS.equalsIgnoreCase(collectionDetailsRequest.getType())) {
            collectionIndexElasticSearchService.getCompleteCollectionIndexDetails(collectionDetailsRequest,
                    collectionIndexDetails);
            propertyTaxElasticSearchIndexService.getConsolidatedDemandInfo(collectionDetailsRequest,
                    collectionIndexDetails);
            List<CollectionTrend> collectionTrends = collectionIndexElasticSearchService
                    .getMonthwiseCollectionDetails(collectionDetailsRequest);
            collectionIndexDetails.setCollTrends(collectionTrends);
        }
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType()) && (collectionDetailsRequest.getType()
                .equalsIgnoreCase(DASHBOARD_GROUPING_BILLCOLLECTORWISE) 
                || DASHBOARD_GROUPING_REVENUEINSPECTORWISE.equalsIgnoreCase(collectionDetailsRequest.getType()) 
                || DASHBOARD_GROUPING_REVENUEOFFICERWISE.equalsIgnoreCase(collectionDetailsRequest.getType())))
            collIndexData = collectionIndexElasticSearchService
                    .getResponseTableDataForBillCollector(collectionDetailsRequest);
        else if (DASHBOARD_GROUPING_ALLWARDS.equalsIgnoreCase(collectionDetailsRequest.getType())) {
            Iterable<CityIndex> cities = cityIndexService.findAll();
            collIndexData = collectionIndexElasticSearchService.getWardWiseTableDataAcrossCities(collectionDetailsRequest,
                    cities);
        } else
            collIndexData = collectionIndexElasticSearchService.getResponseTableData(collectionDetailsRequest, isForMISReports);

        collectionIndexDetails.setResponseDetails(collIndexData);
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_SUCCESS);
        errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_SUCCESS);
        collectionIndexDetails.setErrorDetails(errorDetails);
        return collectionIndexDetails;
    }

    /**
     * Gives the receipts details across all ULBs
     * 
     * @param collectionDetailsRequest
     * @return CollReceiptDetails
     */
    public CollReceiptDetails getReceiptDetails(CollectionDetailsRequest collectionDetailsRequest) {
        CollReceiptDetails receiptDetails = new CollReceiptDetails();
        collectionIndexElasticSearchService.getTotalReceiptsCount(collectionDetailsRequest, receiptDetails);
        List<ReceiptsTrend> receiptTrends = collectionIndexElasticSearchService
                .getMonthwiseReceiptsTrend(collectionDetailsRequest);
        List<ReceiptTableData> receiptTableData = collectionIndexElasticSearchService
                .getReceiptTableData(collectionDetailsRequest);
        receiptDetails.setReceiptDetails(receiptTableData);
        receiptDetails.setReceiptsTrends(receiptTrends);
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_SUCCESS);
        errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_SUCCESS);
        receiptDetails.setErrorDetails(errorDetails);
        return receiptDetails;
    }

    /**
     * Returns Top Ten ULB's Tax Producers
     * 
     * @param collectionDetailsRequest
     * @return
     */
    public TaxPayerResponseDetails getTopTenTaxProducers(CollectionDetailsRequest collectionDetailsRequest) {
        return propertyTaxElasticSearchIndexService.getTopTenTaxPerformers(collectionDetailsRequest, getCurrentFinancialYear(), false);

    }

    /**
     * Returns Bottom Ten ULB's Tax Producers
     * 
     * @param collectionDetailsRequest
     * @return
     */
    public TaxPayerResponseDetails getBottomTenTaxProducers(CollectionDetailsRequest collectionDetailsRequest) {
        return propertyTaxElasticSearchIndexService.getBottomTenTaxPerformers(collectionDetailsRequest, getCurrentFinancialYear(), false);

    }

    /**
     * Returns Top 100 Defaulter's after filtering
     * 
     * @param propertyTaxDefaultersRequest
     * @return
     */
    public List<TaxDefaulters> getTaxDefaulters(PropertyTaxDefaultersRequest propertyTaxDefaultersRequest) {
        return propertyTaxElasticSearchIndexService.getTopDefaulters(propertyTaxDefaultersRequest);
    }

    /**
     * Provides city wise DCB details
     * @param collectionDetailsRequest
     * @return
     */
    public List<DCBDetails> getDCBDetails(CollectionDetailsRequest collectionDetailsRequest) {
        return propertyTaxElasticSearchIndexService.getDCBDetails(collectionDetailsRequest);
    }
    
    /**
     * Provides city wise collection details
     * @param collectionDetailsRequest
     * @param intervalType
     * @return list
     */
    public CollectionAnalysis getCollectionAnalysisData(CollectionDetailsRequest collectionDetailsRequest,
            String intervalType) {
        return collectionIndexElasticSearchService.getCollectionsForInterval(collectionDetailsRequest, intervalType);
    }

    /**
     * Provides week wise DCB details for all cities
     * @param collectionDetailsRequest
     * @param intervalType
     * @return list
     */
    public List<WeeklyDCB> getWeekwiseDCBDetails(CollectionDetailsRequest collectionDetailsRequest,
            String intervalType) {
        return collectionIndexElasticSearchService.getWeekwiseDCBDetailsAcrossCities(collectionDetailsRequest, intervalType);
    }
    
    /**
     * Provides month wise DCB details for all cities
     * @param collectionDetailsRequest
     * @param intervalType
     * @return
     */
    public List<MonthlyDCB> getMonthwiseDCBDetails(CollectionDetailsRequest collectionDetailsRequest,
            String intervalType) {
        return collectionIndexElasticSearchService.getMonthwiseDCBDetailsAcrossCities(collectionDetailsRequest, intervalType);
    }
    
    /**
     * API provides Daily Target information across all cities
     * @param collectionDetailsRequest
     * @return CollectionDetails
     */
    public CollectionDetails getDailyTarget(CollectionDetailsRequest collectionDetailsRequest) {
        CollectionDetails collectionIndexDetails = new CollectionDetails();
        collectionIndexDetails
                .setResponseDetails(collectionIndexElasticSearchService.getResponseTableData(collectionDetailsRequest, true));
        return collectionIndexDetails;
    }
    
    public List<DemandVariance> getDemandVariationDetails(CollectionDetailsRequest collectionDetailsRequest) {
        return propertyTaxElasticSearchIndexService.prepareDemandVariationDetails(collectionDetailsRequest);
    }
    
    /**
     * API is called from CollectionAchievementsJob to push BillCollector wise/RevenueInspector wise/RevenueOfficer wise data into
     * the CollectionAchievements index
     * @param cities
     * @param currFinYear
     */
    public void pushAchievements() {
        List<TaxPayerDetails> taxPayersList = prepareDataToLoadAchievementsIndex();
        for (TaxPayerDetails taxPayerDetails : taxPayersList)
            collectionAchievementsService.createAchievementsIndex(taxPayerDetails);
    }
    
    /**
     * API prepares the data to push into CollectionAchievements index
     * @param cities
     * @param currFinYear
     * @return list
     */
    public List<TaxPayerDetails> prepareDataToLoadAchievementsIndex() {
        Iterable<CityIndex> cities = cityIndexService.findAll();
        CFinancialYear currFinYear = getCurrentFinancialYear();

        CollectionDetailsRequest collectionDetailsRequest = new CollectionDetailsRequest();
        List<TaxPayerDetails> finalList = new ArrayList<>();
        List<TaxPayerDetails> taxPayerDetails;
        for (CityIndex city : cities) {
            // For each city, BillCollector wise, RevenueInspector wise and RevenueOfficer wise data will be pushed simultaneously
            collectionDetailsRequest.setUlbCode(city.getCitycode());
            collectionDetailsRequest.setType(DASHBOARD_GROUPING_BILLCOLLECTORWISE);
            taxPayerDetails = propertyTaxElasticSearchIndexService.prepareDataForAchievementsIndex(collectionDetailsRequest,
                    currFinYear, city);
            finalList.addAll(taxPayerDetails);
            
            collectionDetailsRequest.setType(DASHBOARD_GROUPING_REVENUEINSPECTORWISE);
            taxPayerDetails = propertyTaxElasticSearchIndexService.prepareDataForAchievementsIndex(collectionDetailsRequest,
                    currFinYear, city);
            finalList.addAll(taxPayerDetails);
            
            collectionDetailsRequest.setType(DASHBOARD_GROUPING_REVENUEOFFICERWISE);
            taxPayerDetails = propertyTaxElasticSearchIndexService.prepareDataForAchievementsIndex(collectionDetailsRequest,
                    currFinYear, city);
            finalList.addAll(taxPayerDetails);
        }
        return finalList;
    }

    /**
     * API to fetch top and bottom 10 achievers based on the type - billcollector/revenueinspector/revenueofficer
     * @param collectionDetailsRequest
     * @return map of top and bottom 10 achievers
     */
    public Map<String, List<TaxPayerDetails>> getCollectionRankings(CollectionDetailsRequest collectionDetailsRequest) {
        return propertyTaxElasticSearchIndexService.getCollectionRankings(collectionDetailsRequest);
    }
    
}