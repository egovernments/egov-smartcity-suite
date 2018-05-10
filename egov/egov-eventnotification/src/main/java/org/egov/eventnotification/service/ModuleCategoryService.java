package org.egov.eventnotification.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.eventnotification.entity.ModuleCategory;
import org.egov.eventnotification.repository.ModuleCategoryRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ModuleCategoryService {

	private static final Logger LOGGER = Logger.getLogger(ModuleCategoryService.class);
    private final ModuleCategoryRepository moduleCategoryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ModuleCategoryService(final ModuleCategoryRepository moduleCategoryRepository) {
        this.moduleCategoryRepository = moduleCategoryRepository; 

    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<ModuleCategory> findAll() {
    	LOGGER.info("Repository Request to Find All Module Categories");
    	return moduleCategoryRepository.findAll();
    }
    
    public List<ModuleCategory> getCategoriesForModule(Long moduleId) {
    	return moduleCategoryRepository.getCategoriesForModule(moduleId);
    }
}
