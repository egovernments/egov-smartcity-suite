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
package org.egov.wtms.web.controller.reports;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.WATER_TAX_INDEX_NAME;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.es.CollectionDocument;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.wtms.application.entity.DailyWTCollectionReportSearch;
import org.egov.wtms.entity.es.WaterChargeDocument;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/report/dailyWTCollectionReport/search/")
public class DailyWTCollectionReportController {

    @Autowired
    public EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    public AssignmentService assignmentService;
    
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public AppConfigValueService appConfigValueService;

    @Autowired
    private CityService cityService;
    @Autowired
    private BoundaryService boundaryService;

    @ModelAttribute
    public void reportModel(final Model model) {
        final DailyWTCollectionReportSearch dailyCollectionReportResut = new DailyWTCollectionReportSearch();
        model.addAttribute("dailyWTCollectionReport", dailyCollectionReportResut);
    }

    @ModelAttribute("collectionMode")
    public Map<String, String> loadInstrumentTypes() {
        final Map<String, String> collectionModeMap = new LinkedHashMap<String, String>(0);
        collectionModeMap.put(Source.ESEVA.toString(), Source.ESEVA.toString());
        collectionModeMap.put(Source.MEESEVA.toString(), Source.MEESEVA.toString());
        collectionModeMap.put(Source.APONLINE.toString(), Source.APONLINE.toString());
        collectionModeMap.put(Source.SOFTTECH.toString(), Source.SOFTTECH.toString());
        collectionModeMap.put(Source.SYSTEM.toString(), Source.SYSTEM.toString());
        return collectionModeMap;
    }

    @ModelAttribute("operators")
    public Set<User> loadCollectionOperators() {
        final String operatorDesignation = appConfigValueService.getAppConfigValueByDate(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.COLLECTION_DESIGNATIONFORCSCOPERATOR, new Date()).getValue();
        return assignmentService.getUsersByDesignations(operatorDesignation.split(","));
    }

    @ModelAttribute("status")
    public List<EgwStatus> loadStatus() {

        return egwStatusHibernateDAO.getStatusByModule(CollectionConstants.MODULE_NAME_RECEIPTHEADER);
    }

    @ModelAttribute("wards")
    public List<Boundary> wardBoundaries() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(PropertyTaxConstants.WARD,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @RequestMapping(method = GET)
    public String search(final Model model) {
        model.addAttribute("currentDate", new Date());
        return "dailyWTCollection-search";
    }
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
   public List<DailyWTCollectionReportSearch> searchCollection(@ModelAttribute final DailyWTCollectionReportSearch searchRequest) {

      List<DailyWTCollectionReportSearch>  collectionIndexSearchResult = null;
        final List<String> consumerCodes = new ArrayList<String>();
      final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
       searchRequest.setUlbName(cityWebsite.getName());

        if (StringUtils.isNotBlank(searchRequest.getRevenueWard())) {
         List<WaterChargeDocument>   waterChargeDocument = findAllWaterChargeIndexByFilter(searchRequest);
            for (final WaterChargeDocument consumerDocument : waterChargeDocument) {
              
                consumerCodes.add(consumerDocument.getConsumerCode());
            }
            searchRequest.setConsumerCode(consumerCodes);
            collectionIndexSearchResult = findAllCollectionIndexByFilter(searchRequest);
        } else
            collectionIndexSearchResult = findAllCollectionIndexByFilter(searchRequest);

        
        return collectionIndexSearchResult;
    }

 
    private BoolQueryBuilder getFilterQuery(final DailyWTCollectionReportSearch searchRequest) {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
          boolQuery=boolQuery.filter(QueryBuilders.termQuery("ulbName",searchRequest.getUlbName()));
        if (StringUtils.isNotBlank(searchRequest.getRevenueWard()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("ward", searchRequest.getRevenueWard()));
        if (isNotBlank(searchRequest.getFromDate()) &&
                isNotBlank(searchRequest.getToDate()))
            boolQuery = boolQuery.must(rangeQuery("createdDate")
                    .from(searchRequest.getFromDate())
                    .to(searchRequest.getToDate()));

        return boolQuery;
    }
    private BoolQueryBuilder getCollectionFilterQuery(final DailyWTCollectionReportSearch searchRequest) {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
       boolQuery=boolQuery.filter(QueryBuilders.termQuery("cityName",searchRequest.getUlbName()));
        boolQuery = boolQuery.filter(QueryBuilders.matchQuery("billingService", WaterTaxConstants.COLLECION_BILLING_SERVICE_WTMS));
        if (StringUtils.isNotBlank(searchRequest.getCollectionMode()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("channel", searchRequest.getCollectionMode()));
        if (StringUtils.isNotBlank(searchRequest.getCollectionOperator()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("receiptCreator", searchRequest.getCollectionOperator()));
        if (StringUtils.isNotBlank(searchRequest.getRevenueWard()))
         boolQuery = boolQuery.filter(QueryBuilders.matchQuery("revenueWard", searchRequest.getRevenueWard()));
        if (StringUtils.isNotBlank(searchRequest.getStatus()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("status", searchRequest.getStatus()));
       if (!searchRequest.getConsumerCode().isEmpty())
        {
           final String[] consumerCodesArray = searchRequest.getConsumerCode().toArray(new String[searchRequest.getConsumerCode().size()]);
           boolQuery = boolQuery.filter(QueryBuilders.matchQuery("consumerCode", consumerCodesArray)); 
        }
        if (isNotBlank(searchRequest.getFromDate()) &&
                isNotBlank(searchRequest.getToDate()))
            boolQuery = boolQuery.must(rangeQuery("createdDate")
                    .from(searchRequest.getFromDate())
                    .to(searchRequest.getToDate()));

        return boolQuery;
    }
    public List<WaterChargeDocument> findAllWaterChargeIndexByFilter(final DailyWTCollectionReportSearch searchRequest) {
        final BoolQueryBuilder query = getFilterQuery(searchRequest);
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(WATER_TAX_INDEX_NAME)
                .withQuery(query).build();
        final List<WaterChargeDocument> sampleEntities = elasticsearchTemplate.queryForList(searchQuery,
                WaterChargeDocument.class);
        return sampleEntities;
    }
    
    public List<DailyWTCollectionReportSearch> findAllCollectionIndexByFilter(final DailyWTCollectionReportSearch searchRequest) {
        List<DailyWTCollectionReportSearch> collectionIndexResult=new ArrayList<DailyWTCollectionReportSearch>();
        final BoolQueryBuilder query = getCollectionFilterQuery(searchRequest);
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(WaterTaxConstants.COLLECTION_INDEX_NAME)
                .withQuery(query).build();
        final List<CollectionDocument> sampleEntities = elasticsearchTemplate.queryForList(searchQuery,
                CollectionDocument.class);
        for(CollectionDocument temp:sampleEntities)
        {
            DailyWTCollectionReportSearch dailyRequestObj=new DailyWTCollectionReportSearch();
            dailyRequestObj.setReceiptnumber(temp.getReceiptNumber());
            dailyRequestObj.setConsumercode(temp.getConsumerCode());
            dailyRequestObj.setConsumername(temp.getConsumerName());
            dailyRequestObj.setChannel(temp.getChannel());
            dailyRequestObj.setPaymentmode(temp.getPaymentMode());
            dailyRequestObj.setInstallmentto(temp.getInstallmentTo());
            dailyRequestObj.setAdvanceamount(temp.getAdvanceAmount());
            dailyRequestObj.setArrearamount(temp.getArrearAmount());
            dailyRequestObj.setCurrentamount(temp.getCurrentAmount());
            dailyRequestObj.setTotalamount(temp.getTotalAmount());
            dailyRequestObj.setStatus(temp.getStatus());
            dailyRequestObj.setInstallmentfrom(temp.getInstallmentFrom());
            collectionIndexResult.add(dailyRequestObj);
        }
        return collectionIndexResult;
    }
}
