package org.egov.infra.admin.master.service;

import org.egov.infra.admin.master.entity.CityWebsite;
import org.egov.infra.admin.master.repository.CityWebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CityWebsiteService {
    
    private CityWebsiteRepository cityWebsiteRepository;
    
    @Autowired
    public CityWebsiteService(CityWebsiteRepository cityWebsiteRepository) {
        this.cityWebsiteRepository = cityWebsiteRepository;
    }

    @Transactional
    public CityWebsite createCityWebsite(CityWebsite cityWebsite) {
        return cityWebsiteRepository.save(cityWebsite);
    }
    
    @Transactional
    public CityWebsite updateCityWebsite(CityWebsite cityWebsite) {
        return cityWebsiteRepository.save(cityWebsite);
    }
    
    @Transactional
    public void deleteCityWebsite(CityWebsite cityWebsite) {
        cityWebsiteRepository.delete(cityWebsite);
    }
    
    @Transactional
    public CityWebsite getCityWebSiteByURL(String url) {
        return cityWebsiteRepository.findByURL(url);
    }
    
    @Transactional
    public CityWebsite getCityWebsiteByCityName(String cityName) {
        return cityWebsiteRepository.findByCityName(cityName);   
    }
}
