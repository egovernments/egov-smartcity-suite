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
    
    public List<ServiceCategory> getAllServiceCategoriesByCodeLike(String code){
		return serviceCategoryRepository.findByCodeContainingIgnoreCase(code);
	}
    
    public ServiceCategory findById(Long id) {
    	return serviceCategoryRepository.findOne(id);
    }
}
