package org.egov.infra.admin.master.service.es;

import org.egov.infra.admin.master.entity.es.CityIndex;
import org.egov.infra.admin.master.repository.es.CityIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityIndexService {

	@Autowired
	private CityIndexRepository cityIndexRepository;
	
	public Iterable<CityIndex> findAll(){		
		return cityIndexRepository.findAll();
	}
	
	public CityIndex findByCitycode(String cityCode){
		return cityIndexRepository.findOneByCityCode(cityCode);
	}
	
	public CityIndex findByDistrictCode(String districtCode){
		return cityIndexRepository.findOneByDistrictCode(districtCode);
	}
}
