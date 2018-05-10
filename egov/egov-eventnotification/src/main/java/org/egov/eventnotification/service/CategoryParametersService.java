package org.egov.eventnotification.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.eventnotification.entity.CategoryParameters;
import org.egov.eventnotification.repository.CategoryParametersRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CategoryParametersService {
	
	private static final Logger LOGGER = Logger.getLogger(ModuleCategoryService.class);
    private final CategoryParametersRepository categoryParametersRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CategoryParametersService(final CategoryParametersRepository categoryParametersRepository) {
        this.categoryParametersRepository = categoryParametersRepository; 

    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<CategoryParameters> findAll() { 
    	LOGGER.info("Service Request to find all categories");
    	return categoryParametersRepository.findAll();
    }
    
    public List<CategoryParameters> getParametersForCategory(Long categoryId) { 
    	LOGGER.info("Service Request to get Parameters For Category");
    	return categoryParametersRepository.getParametersForCategory(categoryId);
    }

}
