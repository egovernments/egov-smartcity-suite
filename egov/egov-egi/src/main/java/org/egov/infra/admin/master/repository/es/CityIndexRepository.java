package org.egov.infra.admin.master.repository.es;

import java.util.List;

import org.egov.infra.admin.master.entity.es.CityIndex;
import org.egov.pims.commons.Designation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CityIndexRepository extends ElasticsearchRepository<CityIndex, String>,CityIndexCustomRepository {

	public CityIndex findByCitycode(String code); 
	
	public List<CityIndex> findByDistrictcode(String districtCode);
}
