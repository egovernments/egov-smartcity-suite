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

package org.egov.ptis.service.dashboard;

import static java.lang.String.format;
import static org.egov.ptis.constants.PropertyTaxConstants.BIGDECIMAL_100;
import static org.egov.ptis.constants.PropertyTaxConstants.COLLECION_BILLING_SERVICE_PT;
import static org.egov.ptis.constants.PropertyTaxConstants.COLLECION_BILLING_SERVICE_WTMS;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_PROPERTY_TYPE_COURTCASES;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CFinancialYearService;
import org.egov.infra.admin.master.entity.es.CityIndex;
import org.egov.infra.admin.master.service.es.CityIndexService;
import org.egov.infra.rest.client.SimpleRestClient;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.utils.WebUtils;
import org.egov.ptis.bean.dashboard.CollReceiptDetails;
import org.egov.ptis.bean.dashboard.CollTableData;
import org.egov.ptis.bean.dashboard.CollectionDetails;
import org.egov.ptis.bean.dashboard.CollectionDetailsRequest;
import org.egov.ptis.bean.dashboard.CollectionStats;
import org.egov.ptis.bean.dashboard.CollectionTrend;
import org.egov.ptis.bean.dashboard.PropertyTaxDefaultersRequest;
import org.egov.ptis.bean.dashboard.ReceiptTableData;
import org.egov.ptis.bean.dashboard.ReceiptsTrend;
import org.egov.ptis.bean.dashboard.StateCityInfo;
import org.egov.ptis.bean.dashboard.TaxDefaulters;
import org.egov.ptis.bean.dashboard.TaxPayerResponseDetails;
import org.egov.ptis.bean.dashboard.TotalCollectionStats;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.service.es.CollectionIndexElasticSearchService;
import org.egov.ptis.service.es.PropertyTaxElasticSearchIndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to provide APIs for CM Dashboard
 */

@Service
public class PropTaxDashboardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropTaxDashboardService.class);

    @PersistenceContext
    private EntityManager entityManager;
    
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
        Long startTime = System.currentTimeMillis();
        CollectionStats consolidatedData = new CollectionStats();
        Map<String, BigDecimal> consolidatedColl = collectionIndexElasticSearchService
                .getFinYearsCollByService(COLLECION_BILLING_SERVICE_PT);
        Long timeTaken = System.currentTimeMillis() - startTime;
        CFinancialYear currFinYear = cFinancialYearService.getFinancialYearByDate(new Date());

        LOGGER.debug("Time taken by getFinYearsCollByService() for Property Tax is : " + timeTaken + " (millisecs) ");
        if (!consolidatedColl.isEmpty()) {
            consolidatedData.setCytdColl(consolidatedColl.get("cytdColln"));
            consolidatedData.setLytdColl(consolidatedColl.get("lytdColln"));
        }
        startTime = System.currentTimeMillis();
        BigDecimal totalDmd = propertyTaxElasticSearchIndexService.getTotalDemand();
        timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by Property Tax getTotalDemand() is : " + timeTaken + " (millisecs) ");
        int noOfMonths = DateUtils
                .noOfMonths(DateUtils.startOfDay(currFinYear.getStartingDate()), new Date()) + 1;
        consolidatedData.setTotalDmd(totalDmd.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(noOfMonths)));
        consolidatedData.setPerformance((consolidatedData.getCytdColl().multiply(BIGDECIMAL_100))
                .divide(consolidatedData.getTotalDmd(), 1, BigDecimal.ROUND_HALF_UP));
        consolidatedData.setLyVar(
                (consolidatedData.getCytdColl().subtract(consolidatedData.getLytdColl()).multiply(BIGDECIMAL_100))
                        .divide(consolidatedData.getLytdColl(), 1, BigDecimal.ROUND_HALF_UP));
        consolidatedCollectionDetails.setPropertyTax(consolidatedData);

        // For Water Tax collections
        consolidatedData = new CollectionStats();
        startTime = System.currentTimeMillis();
        consolidatedColl = collectionIndexElasticSearchService.getFinYearsCollByService(COLLECION_BILLING_SERVICE_WTMS);
        timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by getFinYearsCollByService() for Water Tax is : " + timeTaken + " (millisecs) ");
        if (!consolidatedColl.isEmpty()) {
            consolidatedData.setCytdColl(consolidatedColl.get("cytdColln"));
            consolidatedData.setLytdColl(consolidatedColl.get("lytdColln"));
        }
        startTime = System.currentTimeMillis();
        consolidatedData.setTotalDmd(getWaterChargeTotalDemand(request));
        timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by Water Tax getTotalDemand() is : " + timeTaken + " (millisecs) ");
        consolidatedData.setPerformance((consolidatedData.getCytdColl().multiply(BIGDECIMAL_100))
                .divide(consolidatedData.getTotalDmd(), 1, BigDecimal.ROUND_HALF_UP));
        consolidatedData.setLyVar(
                (consolidatedData.getCytdColl().subtract(consolidatedData.getLytdColl()).multiply(BIGDECIMAL_100))
                        .divide(consolidatedData.getCytdColl(), 1, BigDecimal.ROUND_HALF_UP));
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

    /**
     * Gives the total demand for Water Charges
     * 
     * @param request
     * @return BigDecimal
     */
    public BigDecimal getWaterChargeTotalDemand(final HttpServletRequest request) {
        final String wtmsRestURL = format(PropertyTaxConstants.WTMS_TOTALDEMAND_RESTURL,
                WebUtils.extractRequestDomainURL(request, false));
        final HashMap<String, Object> waterTaxInfo = simpleRestClient.getRESTResponseAsMap(wtmsRestURL);
        return waterTaxInfo.get("currentDemand") == null ? BigDecimal.ZERO
                : new BigDecimal(Double.valueOf((Double) waterTaxInfo.get("currentDemand")));
    }

    /**
     * Gives the Collection Index details across all ULBs
     * 
     * @param collectionDetailsRequest
     * @return CollectionIndexDetails
     */
    public CollectionDetails getCollectionIndexDetails(CollectionDetailsRequest collectionDetailsRequest) {
        CollectionDetails collectionIndexDetails = new CollectionDetails();
        List<CollTableData> collIndexData;
        List<CollectionTrend> collectionTrends = new ArrayList<>();
        collectionIndexElasticSearchService.getCompleteCollectionIndexDetails(collectionDetailsRequest,
                collectionIndexDetails);
        propertyTaxElasticSearchIndexService.getConsolidatedDemandInfo(collectionDetailsRequest,
                collectionIndexDetails);
        if (!DASHBOARD_PROPERTY_TYPE_COURTCASES.equalsIgnoreCase(collectionDetailsRequest.getPropertyType())){
            collectionTrends = collectionIndexElasticSearchService
                    .getMonthwiseCollectionDetails(collectionDetailsRequest);
        }
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType()) && collectionDetailsRequest.getType()
                .equalsIgnoreCase(PropertyTaxConstants.DASHBOARD_GROUPING_BILLCOLLECTORWISE))
            collIndexData = collectionIndexElasticSearchService
                    .getResponseTableDataForBillCollector(collectionDetailsRequest);
        else
            collIndexData = collectionIndexElasticSearchService.getResponseTableData(collectionDetailsRequest);
        collectionIndexDetails.setCollTrends(collectionTrends);
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
        return propertyTaxElasticSearchIndexService.getTopTenTaxPerformers(collectionDetailsRequest);

    }

    /**
     * Returns Bottom Ten ULB's Tax Producers
     * 
     * @param collectionDetailsRequest
     * @return
     */
    public TaxPayerResponseDetails getBottomTenTaxProducers(CollectionDetailsRequest collectionDetailsRequest) {
        return propertyTaxElasticSearchIndexService.getBottomTenTaxPerformers(collectionDetailsRequest);

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

}