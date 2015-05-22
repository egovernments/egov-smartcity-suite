package org.egov.collection.service;

import java.util.List;

import org.egov.collection.repository.ServiceCategoryRepository;
import org.egov.infstr.models.ServiceCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ServiceCategoryService{
	private final ServiceCategoryRepository serviceCategoryRepository;
	  
    @Autowired
    public ServiceCategoryService(final ServiceCategoryRepository serviceCategoryRepository) {

        this.serviceCategoryRepository = serviceCategoryRepository;
    }
    
    @Transactional
    public void create(final ServiceCategory serviceCategory) {
        serviceCategoryRepository.save(serviceCategory);
    }

    @Transactional
    public void update(final ServiceCategory serviceCategory) {
        serviceCategoryRepository.save(serviceCategory);
    }
    
    public List<ServiceCategory> getAllServiceCategoriesOrderByCode(){
		return serviceCategoryRepository.findAllByOrderByCodeAsc();
	}
    
    public ServiceCategory findByCode(String code) {
    	return serviceCategoryRepository.findByCode(code);
    }
    
    public List<ServiceCategory> getAllActiveServiceCategories() {
    	return serviceCategoryRepository.findAllActiveServiceCategories();    	
    }
}
