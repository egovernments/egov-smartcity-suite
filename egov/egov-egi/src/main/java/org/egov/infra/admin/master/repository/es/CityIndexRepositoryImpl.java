package org.egov.infra.admin.master.repository.es;

import java.util.List;

import org.egov.infra.admin.master.entity.es.CityIndex;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

public class CityIndexRepositoryImpl implements CityIndexCustomRepository {
	
	@Autowired
	private ElasticsearchTemplate elasticSearchTemplate;

	//Can be used preferably for fetching only one record where  
	//district code id fetched during aggregation
	@Override
	public CityIndex findOneByDistrictCode(String districtCode) {
		
		SearchQuery query = new NativeSearchQueryBuilder().withIndices("city")
				            .withQuery(QueryBuilders.matchQuery("districtcode", districtCode))
				            .build();
		
		List<CityIndex> cityList = elasticSearchTemplate.queryForList(query, CityIndex.class);
		
		//Used for returning one record based on aggregated result district code
		return cityList.get(0);
	}

	//Can be used preferably for fetching only one record where  
	//city code id fetched during aggregation
	public CityIndex findOneByCityCode(String cityCode){
		SearchQuery query = new NativeSearchQueryBuilder().withIndices("city")
	            .withQuery(QueryBuilders.matchQuery("citycode", cityCode))
	            .build();
		
		List<CityIndex> cityList = elasticSearchTemplate.queryForList(query, CityIndex.class);
		
		//Used for returning one record based on aggregated result city code
		return cityList.get(0);
	}
	
}
