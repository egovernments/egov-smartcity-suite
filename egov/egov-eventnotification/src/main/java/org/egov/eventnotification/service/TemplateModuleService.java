package org.egov.eventnotification.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.eventnotification.entity.TemplateModule;
import org.egov.eventnotification.repository.TemplateModuleRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TemplateModuleService {

	private static final Logger LOGGER = Logger.getLogger(TemplateModuleService.class);
    private final TemplateModuleRepository templateModuleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TemplateModuleService(final TemplateModuleRepository templateModuleRepository) {
        this.templateModuleRepository = templateModuleRepository; 

    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<TemplateModule> findAll() {
    	LOGGER.info("Service Request to find all Modules");
    	return templateModuleRepository.findAll();
    }
}
