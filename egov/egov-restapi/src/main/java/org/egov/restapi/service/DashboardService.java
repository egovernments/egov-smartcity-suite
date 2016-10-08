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

package org.egov.restapi.service;

import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.ptis.bean.dashboard.CollIndexTableData;
import org.egov.ptis.bean.dashboard.CollReceiptDetails;
import org.egov.ptis.bean.dashboard.CollectionDetailsRequest;
import org.egov.ptis.bean.dashboard.CollectionIndexDetails;
import org.egov.ptis.bean.dashboard.CollectionTrend;
import org.egov.ptis.bean.dashboard.ConsolidatedCollDetails;
import org.egov.ptis.bean.dashboard.ConsolidatedCollectionDetails;
import org.egov.ptis.bean.dashboard.ReceiptTableData;
import org.egov.ptis.bean.dashboard.ReceiptsTrend;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.service.elasticsearch.CollectionIndexElasticSearchService;
import org.egov.ptis.service.elasticsearch.PropertyTaxElasticSearchIndexService;
import org.egov.ptis.service.elasticsearch.WaterTaxElasticSearchIndexService;
import org.egov.restapi.model.StateCityInfo;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to provide apis for CM Dashboard
 */

@Service
public class DashboardService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionIndexElasticSearchService.class);
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Autowired
	private CollectionIndexElasticSearchService collectionIndexElasticSearchService;
	
	@Autowired
	private PropertyTaxElasticSearchIndexService propertyTaxElasticSearchIndexService;
	
	@Autowired
	private WaterTaxElasticSearchIndexService waterTaxElasticSearchIndexService;
	
	/**
	 * Gives the State-City information across all ULBs
	 * @return List
	 */
	public List<StateCityInfo> getStateCityDetails(){
		String query = "select regionname as region, districtname as district, city as city, grade as grade, citycode as ulbcode from public.statecityinfo order by city ";
		SQLQuery sqlQuery = entityManager.unwrap(Session.class).createSQLQuery(query);
		sqlQuery.addScalar("region", StandardBasicTypes.STRING);
		sqlQuery.addScalar("district", StandardBasicTypes.STRING);
		sqlQuery.addScalar("city", StandardBasicTypes.STRING);
		sqlQuery.addScalar("grade", StandardBasicTypes.STRING);
		sqlQuery.addScalar("ulbCode", StandardBasicTypes.STRING);
		sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(StateCityInfo.class));
		List<StateCityInfo> stateCityDetails = sqlQuery.list();
		return stateCityDetails;
	}
	
	/**
	 * Provides State-wise Collection Statistics for Property Tax, Water Charges and Others
	 * @return ConsolidatedCollDetails
	 */
	public ConsolidatedCollectionDetails getConsolidatedCollectionDetails(){
		ConsolidatedCollectionDetails consolidatedCollectionDetails = new ConsolidatedCollectionDetails();
		//For Property Tax collections
		ConsolidatedCollDetails consolidatedData = new ConsolidatedCollDetails();
		Map<String, BigDecimal> consolidatedColl = collectionIndexElasticSearchService.getConsolidatedCollection(PropertyTaxConstants.COLLECION_BILLING_SERVICE_PT);		
		if(!consolidatedColl.isEmpty()){
			consolidatedData.setCytdColl(consolidatedColl.get("cytdColln"));
			consolidatedData.setLytdColl(consolidatedColl.get("lytdColln"));
		}
		consolidatedData.setTotalDmd(propertyTaxElasticSearchIndexService.getTotalDemand());
		consolidatedCollectionDetails.setPropertyTax(consolidatedData);
		
		//For Water Tax collections
		consolidatedData = new ConsolidatedCollDetails();
		consolidatedColl = collectionIndexElasticSearchService.getConsolidatedCollection(PropertyTaxConstants.COLLECION_BILLING_SERVICE_WTMS);		
		if(!consolidatedColl.isEmpty()){
			consolidatedData.setCytdColl(consolidatedColl.get("cytdColln"));
			consolidatedData.setLytdColl(consolidatedColl.get("lytdColln"));
		}
		consolidatedData.setTotalDmd(waterTaxElasticSearchIndexService.getTotalDemand());
		consolidatedCollectionDetails.setWaterTax(consolidatedData);
		
		//Other collections - temporarily set to 0
		consolidatedData = new ConsolidatedCollDetails();
		consolidatedData.setCytdColl(BigDecimal.ZERO);
		consolidatedData.setTotalDmd(BigDecimal.ZERO);
		consolidatedData.setLytdColl(BigDecimal.ZERO);
		consolidatedCollectionDetails.setOthers(consolidatedData);
		
		return consolidatedCollectionDetails;
	}
	
	/**
	 * Gives the Collection Index details across all ULBs
	 * @param collectionDetailsRequest
	 * @return CollectionIndexDetails
	 */
	public CollectionIndexDetails getCollectionIndexDetails(CollectionDetailsRequest collectionDetailsRequest){
		LOGGER.info("CollectionDetailsRequest input ----> ");
		LOGGER.info("regionName = "+collectionDetailsRequest.getRegionName()+", districtName = "+ collectionDetailsRequest.getDistrictName()+
				", ulbGrade = "+collectionDetailsRequest.getUlbGrade()+", ulbCode = "+collectionDetailsRequest.getUlbCode()+
				", fromDate = "+collectionDetailsRequest.getFromDate()+", toDate = "+collectionDetailsRequest.getToDate());
		CollectionIndexDetails collectionIndexDetails = new CollectionIndexDetails(); 
		collectionIndexElasticSearchService.getCompleteCollectionIndexDetails(collectionDetailsRequest,collectionIndexDetails);
		propertyTaxElasticSearchIndexService.getConsolidatedDemandInfo(collectionDetailsRequest, collectionIndexDetails);
		List<CollectionTrend> collectionTrends = collectionIndexElasticSearchService.getMonthwiseCollectionDetails(collectionDetailsRequest);
		List<CollIndexTableData> collIndexData = collectionIndexElasticSearchService.getResponseTableData(collectionDetailsRequest);
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
	 * @param collectionDetailsRequest
	 * @return CollReceiptDetails
	 */
	public CollReceiptDetails getReceiptDetails(CollectionDetailsRequest collectionDetailsRequest){
		LOGGER.info("CollectionDetailsRequest input ----> ");
		LOGGER.info("regionName = "+collectionDetailsRequest.getRegionName()+", districtName = "+ collectionDetailsRequest.getDistrictName()+
				", ulbGrade = "+collectionDetailsRequest.getUlbGrade()+", ulbCode = "+collectionDetailsRequest.getUlbCode()+
				", fromDate = "+collectionDetailsRequest.getFromDate()+", toDate = "+collectionDetailsRequest.getToDate());
		CollReceiptDetails receiptDetails = new CollReceiptDetails(); 
		collectionIndexElasticSearchService.getCummulativeReceiptsCount(collectionDetailsRequest,receiptDetails);
		List<ReceiptsTrend> receiptTrends = collectionIndexElasticSearchService.getMonthwiseReceiptsTrend(collectionDetailsRequest);
		List<ReceiptTableData> receiptTableData = collectionIndexElasticSearchService.getReceiptTableData(collectionDetailsRequest);
		receiptDetails.setReceiptDetails(receiptTableData);
		receiptDetails.setReceiptsTrends(receiptTrends);
		ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_SUCCESS);
        errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_SUCCESS);
        receiptDetails.setErrorDetails(errorDetails);
		return receiptDetails;
	}
}