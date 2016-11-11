package org.egov.infra.admin.master.repository.es;

import org.egov.infra.admin.master.entity.es.CityIndex;

public interface CityIndexCustomRepository {
	
	public CityIndex findOneByDistrictCode(String districtCode);
	
	public CityIndex findOneByCityCode(String cityCode);

}
