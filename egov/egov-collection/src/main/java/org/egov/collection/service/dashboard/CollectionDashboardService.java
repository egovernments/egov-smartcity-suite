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

package org.egov.collection.service.dashboard;

import org.egov.collection.bean.dashboard.CollectionDashBoardRequest;
import org.egov.collection.bean.dashboard.CollectionDashBoardStats;
import org.egov.collection.bean.dashboard.CollectionDashBoardTrend;
import org.egov.collection.bean.dashboard.CollectionDocumentDetails;
import org.egov.collection.bean.dashboard.CollectionTableData;
import org.egov.collection.bean.dashboard.TaxPayerDashBoardResponseDetails;
import org.egov.collection.bean.dashboard.TotalCollectionDashBoardStats;
import org.egov.collection.bean.dashboard.TotalCollectionStatistics;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.service.elasticsearch.CollectionDocumentElasticSearchService;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CFinancialYearService;
import org.egov.infstr.models.ServiceDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service to provide APIs for CM Dashboard
 */

@Service
public class CollectionDashboardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionDashboardService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CFinancialYearService cFinancialYearService;

    @Autowired
    private CollectionDocumentElasticSearchService collectionDocumentElasticSearchService;
    private static final String MILLISECS = " (millisecs) ";

    /**
     * Provides State-wise Collection Statistics for Property Tax, Water Charges
     * and Others
     *
     * @return CollectionStats
     */
    public TotalCollectionDashBoardStats getTotalCollectionStats(
            final CollectionDashBoardRequest collectionDashBoardRequest) {
        final TotalCollectionDashBoardStats consolidatedCollectionDetails = new TotalCollectionDashBoardStats();
        final List<CollectionDashBoardStats> totalStatistics = new ArrayList<>();
        final List<String> toBeExcluded = new ArrayList<>();
        final CFinancialYear currFinYear = cFinancialYearService.getFinancialYearByDate(new Date());
        for (final String service : collectionDashBoardRequest.getIncludeServices()) {
            if (!service.equalsIgnoreCase(CollectionConstants.DASHBOARD_OTHERS))
                setConsolidatedData(totalStatistics, new ArrayList<String>(Arrays.asList(service)), service,
                        currFinYear);
            toBeExcluded.add(service);
        }
        final List<String> serviceList = getServiceList(collectionDashBoardRequest.getExcludeServices(), toBeExcluded);
        if (!serviceList.isEmpty())
            setConsolidatedData(totalStatistics, serviceList, CollectionConstants.DASHBOARD_OTHERS, currFinYear);
        consolidatedCollectionDetails.setCollection(totalStatistics);
        BigDecimal currColl = BigDecimal.ZERO;
        BigDecimal lastColl = BigDecimal.ZERO;
        if (!totalStatistics.isEmpty())
            for (final CollectionDashBoardStats stat : totalStatistics) {
                currColl = currColl.add(stat.getCytdColl());
                lastColl = lastColl.add(stat.getLytdColl());
            }
        consolidatedCollectionDetails.setTotalCurrentCollection(currColl);
        consolidatedCollectionDetails.setTotalLastYearCollection(lastColl);
        return consolidatedCollectionDetails;
    }

    private void setConsolidatedData(final List<CollectionDashBoardStats> totalStatistics,
            final List<String> serviceList, final String service, final CFinancialYear financialYear) {
        Long startTime = System.currentTimeMillis();
        BigDecimal variance;
        final CollectionDashBoardStats consolidatedData = new CollectionDashBoardStats();
        final Map<String, BigDecimal> consolidatedColl = collectionDocumentElasticSearchService
                .getFinYearsCollByService(serviceList, financialYear);
        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken by getFinYearsCollByService() for Property Tax is : " + timeTaken + MILLISECS);
        if (!consolidatedColl.isEmpty()) {
            consolidatedData.setCytdColl(consolidatedColl.get("cytdColln"));
            consolidatedData.setLytdColl(consolidatedColl.get("lytdColln"));
        }
        consolidatedData.setServiceName(service);

        startTime = System.currentTimeMillis();
        timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken by Property Tax getTotalDemand() is : " + timeTaken + MILLISECS);
        if (consolidatedData.getLytdColl().compareTo(BigDecimal.ZERO) == 0)
            variance = CollectionConstants.BIGDECIMAL_100;
        else
            variance = consolidatedData.getCytdColl().subtract(consolidatedData.getLytdColl())
                    .multiply(CollectionConstants.BIGDECIMAL_100)
                    .divide(consolidatedData.getLytdColl(), 1, BigDecimal.ROUND_HALF_UP);
        consolidatedData.setLyVar(variance);
        totalStatistics.add(consolidatedData);
    }

    /**
     * Gives the Collection Index details across all ULBs
     *
     * @param collectionDashBoardRequest
     * @return CollectionIndexDetails
     */
    public List<TotalCollectionStatistics> getCollectionIndexDetails(
            final CollectionDashBoardRequest collectionDashBoardRequest) {
        final List<TotalCollectionStatistics> totalStats = new ArrayList<>();
        final List<String> toBeExcluded = new ArrayList<>();
        final CFinancialYear currFinYear = cFinancialYearService.getFinancialYearByDate(new Date());
        for (final String service : collectionDashBoardRequest.getIncludeServices()) {
            if (!service.equalsIgnoreCase(CollectionConstants.DASHBOARD_OTHERS))
                setCollectionIndexStats(collectionDashBoardRequest, totalStats,
                        new ArrayList<String>(Arrays.asList(service)), service, currFinYear);
            toBeExcluded.add(service);
        }
        final List<String> serviceList = getServiceList(collectionDashBoardRequest.getExcludeServices(), toBeExcluded);
        if (!serviceList.isEmpty())
            setCollectionIndexStats(collectionDashBoardRequest, totalStats, serviceList,
                    CollectionConstants.DASHBOARD_OTHERS, currFinYear);
        return totalStats;
    }

    private void setCollectionIndexStats(final CollectionDashBoardRequest collectionDashBoardRequest,
            final List<TotalCollectionStatistics> totalStats, final List<String> serviceList, final String service,
            final CFinancialYear financialYear) {
        final List<CollectionDocumentDetails> collectionDocumentDetails = new ArrayList<>();
        final TotalCollectionStatistics totalCollStat = new TotalCollectionStatistics();
        final CollectionDocumentDetails collectionDocDetails = collectionDocumentElasticSearchService
                .getCompleteCollectionIndexDetails(collectionDashBoardRequest, serviceList, financialYear);
        final List<CollectionDashBoardTrend> collectionTrends = collectionDocumentElasticSearchService
                .getMonthwiseCollectionDetails(collectionDashBoardRequest, serviceList, financialYear);
        final List<CollectionTableData> collTableData = collectionDocumentElasticSearchService.getResponseTableData(
                collectionDashBoardRequest, serviceList, financialYear);
        collectionDocDetails.setCollTrends(collectionTrends);
        collectionDocDetails.setResponseDetails(collTableData);
        collectionDocDetails.setServiceName(service);
        collectionDocumentDetails.add(collectionDocDetails);
        totalCollStat.setCollectionDashBoardStats(collectionDocumentDetails);
        totalStats.add(totalCollStat);
    }

    /**
     * Returns Top Ten ULB's Tax Producers
     *
     * @param collectionDashBoardRequest
     * @return
     */
    public List<TaxPayerDashBoardResponseDetails> getTopTenTaxProducers(
            final CollectionDashBoardRequest collectionDashBoardRequest) {
        final List<TaxPayerDashBoardResponseDetails> topTenCollectionsList = new ArrayList<>();
        final List<String> toBeExcluded = new ArrayList<>();
        TaxPayerDashBoardResponseDetails topTenResponse;
        for (final String service : collectionDashBoardRequest.getIncludeServices()) {
            if (!service.equalsIgnoreCase(CollectionConstants.DASHBOARD_OTHERS)) {
                topTenResponse = collectionDocumentElasticSearchService.getTopTenTaxPerformers(
                        collectionDashBoardRequest, new ArrayList<String>(Arrays.asList(service)));
                topTenResponse.setServiceName(service);
                topTenCollectionsList.add(topTenResponse);
            }
            toBeExcluded.add(service);
        }
        final List<String> serviceList = getServiceList(collectionDashBoardRequest.getExcludeServices(), toBeExcluded);
        if (!serviceList.isEmpty()) {
            topTenResponse = collectionDocumentElasticSearchService.getTopTenTaxPerformers(collectionDashBoardRequest,
                    serviceList);
            topTenResponse.setServiceName(CollectionConstants.DASHBOARD_OTHERS);
            topTenCollectionsList.add(topTenResponse);
        }
        return topTenCollectionsList;
    }

    /**
     * Returns Bottom Ten ULB's Tax Producers
     *
     * @param collectionDashBoardRequest
     * @return
     */
    public List<TaxPayerDashBoardResponseDetails> getBottomTenTaxProducers(
            final CollectionDashBoardRequest collectionDashBoardRequest) {
        final List<TaxPayerDashBoardResponseDetails> bottomTenCollectionsList = new ArrayList<>();
        final List<String> toBeExcluded = new ArrayList<>();
        TaxPayerDashBoardResponseDetails bottomTenResponse;
        for (final String service : collectionDashBoardRequest.getIncludeServices()) {
            if (!service.equalsIgnoreCase(CollectionConstants.DASHBOARD_OTHERS)) {
                bottomTenResponse = collectionDocumentElasticSearchService.getBottomTenTaxPerformers(
                        collectionDashBoardRequest, new ArrayList<String>(Arrays.asList(service)));
                bottomTenResponse.setServiceName(service);
                bottomTenCollectionsList.add(bottomTenResponse);
            }
            toBeExcluded.add(service);
        }
        final List<String> serviceList = getServiceList(collectionDashBoardRequest.getExcludeServices(), toBeExcluded);
        if (!serviceList.isEmpty()) {
            bottomTenResponse = collectionDocumentElasticSearchService.getBottomTenTaxPerformers(
                    collectionDashBoardRequest, serviceList);
            bottomTenResponse.setServiceName(CollectionConstants.DASHBOARD_OTHERS);
            bottomTenCollectionsList.add(bottomTenResponse);
        }
        return bottomTenCollectionsList;
    }

    @SuppressWarnings("unchecked")
    private List<String> getServiceList(final List<String> excludeServices, final List<String> toBeExcluded) {
        final List<String> serviceList = new ArrayList<>();
        if (!excludeServices.contains(CollectionConstants.DASHBOARD_OTHERS)) {
            final Query qry = entityManager.createNamedQuery(CollectionConstants.DISTINCT_SERVICE_DETAILS,
                    ServiceDetails.class);
            final List<ServiceDetails> serviceDetails = qry.getResultList();
            for (final ServiceDetails sd : serviceDetails)
                serviceList.add(sd.getName());
            serviceList.removeAll(toBeExcluded);
            serviceList.removeAll(excludeServices);
        }
        return serviceList;
    }

}