/* eGov suite of products aim to improve the internal efficiency,transparency,
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

package org.egov.collection.service.dashboard;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

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
import org.egov.infstr.models.ServiceDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to provide APIs for CM Dashboard
 */

@Service
public class CollectionDashboardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionDashboardService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CollectionDocumentElasticSearchService collectionDocumentElasticSearchService;

    /**
     * Provides State-wise Collection Statistics for Property Tax, Water Charges
     * and Others
     *
     * @return CollectionStats
     */
    public TotalCollectionDashBoardStats getTotalCollectionStats(final HttpServletRequest request) {
        final TotalCollectionDashBoardStats consolidatedCollectionDetails = new TotalCollectionDashBoardStats();
        BigDecimal variance = BigDecimal.ZERO;
        Long startTime = System.currentTimeMillis();
        CollectionDashBoardStats consolidatedData;
        final List<CollectionDashBoardStats> totalStatistics = new ArrayList<>();
        final Query qry = entityManager.createNamedQuery(CollectionConstants.DISTINCT_SERVICE_DETAILS,
                ServiceDetails.class);
        final List<ServiceDetails> serviceDetails = qry.getResultList();
        Map<String, BigDecimal> consolidatedColl = new HashMap<>();
        for (final ServiceDetails serviceDetail : serviceDetails) {
            consolidatedData = new CollectionDashBoardStats();
            consolidatedColl = collectionDocumentElasticSearchService.getFinYearsCollByService(serviceDetail.getName());
            Long timeTaken = System.currentTimeMillis() - startTime;
            LOGGER.debug(
                    "Time taken by getFinYearsCollByService() for Property Tax is : " + timeTaken + " (millisecs) ");
            if (!consolidatedColl.isEmpty()) {
                consolidatedData.setCytdColl(consolidatedColl.get("cytdColln"));
                consolidatedData.setLytdColl(consolidatedColl.get("lytdColln"));
            }
            consolidatedData.setServiceName(serviceDetail.getName());

            startTime = System.currentTimeMillis();
            timeTaken = System.currentTimeMillis() - startTime;
            LOGGER.debug("Time taken by Property Tax getTotalDemand() is : " + timeTaken + " (millisecs) ");
            if (consolidatedData.getLytdColl().compareTo(BigDecimal.ZERO) == 0)
                variance = CollectionConstants.BIGDECIMAL_100;
            else
                variance = consolidatedData.getCytdColl().subtract(consolidatedData.getLytdColl())
                        .multiply(CollectionConstants.BIGDECIMAL_100)
                        .divide(consolidatedData.getLytdColl(), 1, BigDecimal.ROUND_HALF_UP);
            consolidatedData.setLyVar(variance);
            totalStatistics.add(consolidatedData);
        }
        consolidatedCollectionDetails.setCollection(totalStatistics);
        consolidatedColl = collectionDocumentElasticSearchService.getFinYearsCollByService(null);
        if (!consolidatedColl.isEmpty()) {
            consolidatedCollectionDetails.setTotalCurrentCollection(consolidatedColl.get("cytdColln"));
            consolidatedCollectionDetails.setTotalLastYearCollection(consolidatedColl.get("lytdColln"));
        }
        return consolidatedCollectionDetails;
    }

    /**
     * Gives the Collection Index details across all ULBs
     *
     * @param collectionDashBoardRequest
     * @return CollectionIndexDetails
     */
    public List<TotalCollectionStatistics> getCollectionIndexDetails(
            final CollectionDashBoardRequest collectionDashBoardRequest) {
        final List<CollectionDocumentDetails> collectionDocumentDetails = new ArrayList<>();
        List<CollectionTableData> collIndexData = new ArrayList<>();
        final List<TotalCollectionStatistics> totalStats = new ArrayList<>();
        TotalCollectionStatistics ts = null;
        final Query qry = entityManager.createNamedQuery(CollectionConstants.DISTINCT_SERVICE_DETAILS,
                ServiceDetails.class);
        final List<ServiceDetails> serviceDetails = qry.getResultList();
        for (final ServiceDetails serviceDetail : serviceDetails) {
            CollectionDocumentDetails cds = new CollectionDocumentDetails();
            ts = new TotalCollectionStatistics();
            cds = collectionDocumentElasticSearchService.getCompleteCollectionIndexDetails(collectionDashBoardRequest,
                    serviceDetail.getName());
            final List<CollectionDashBoardTrend> collectionTrends = collectionDocumentElasticSearchService
                    .getMonthwiseCollectionDetails(collectionDashBoardRequest, serviceDetail.getName());
            collIndexData = collectionDocumentElasticSearchService.getResponseTableData(collectionDashBoardRequest,
                    serviceDetail.getName());
            cds.setCollTrends(collectionTrends);
            cds.setResponseDetails(collIndexData);
            cds.setServiceName(serviceDetail.getName());
            collectionDocumentDetails.add(cds);
            ts.setCollectionDashBoardStats(collectionDocumentDetails);

        }
        totalStats.add(ts);

        return totalStats;
    }

    /**
     * Returns Top Ten ULB's Tax Producers
     *
     * @param collectionDashBoardRequest
     * @return
     */
    public TaxPayerDashBoardResponseDetails getTopTenTaxProducers(
            final CollectionDashBoardRequest collectionDashBoardRequest) {
        return collectionDocumentElasticSearchService.getTopTenTaxPerformers(collectionDashBoardRequest);
    }

    /**
     * Returns Bottom Ten ULB's Tax Producers
     *
     * @param collectionDashBoardRequest
     * @return
     */
    public TaxPayerDashBoardResponseDetails getBottomTenTaxProducers(
            final CollectionDashBoardRequest collectionDashBoardRequest) {

        return collectionDocumentElasticSearchService.getBottomTenTaxPerformers(collectionDashBoardRequest);
    }

}