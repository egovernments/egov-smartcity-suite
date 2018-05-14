package org.egov.eventnotification.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.eventnotification.constants.EventnotificationConstant;
import org.egov.eventnotification.entity.CategoryParameters;
import org.egov.eventnotification.entity.ModuleCategory;
import org.egov.eventnotification.entity.NotificationDrafts;
import org.egov.eventnotification.entity.TemplateModule;
import org.egov.eventnotification.repository.DraftRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DraftService {
	private static final Logger LOGGER = Logger.getLogger(DraftService.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private TemplateModuleService templateModuleService;

	@Autowired
	private ModuleCategoryService moduleCategoryService;

	@Autowired
	private DraftRepository draftRepository;

	@Autowired
	private CategoryParametersService categoryParametersService;

	public Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	public List<TemplateModule> getAllModules() {
		LOGGER.info("Service Request to get all Modules");
		return templateModuleService.findAll();
	}

	public List<ModuleCategory> getAllCategories() {
		LOGGER.info("Service Request to get all Categories");
		return moduleCategoryService.findAll();
	}

	public List<CategoryParameters> getAllCategoryParameters() {
		LOGGER.info("Service Request to get All Category Parameters");
		return categoryParametersService.findAll();
	}

	public List<NotificationDrafts> getAllNotificationDrafts() {
		LOGGER.info("Service Request to get all Notification Drafts");
		return draftRepository.findAll();
	}

	public List<ModuleCategory> getCategoriesForModule(Long moduleId) {
		LOGGER.info("Service Request to get Categories for Module");
		return moduleCategoryService.getCategoriesForModule(moduleId);
	}

	public List<CategoryParameters> getParametersForCategory(Long categoryId) {
		LOGGER.info("Service Request to get Parameters for Category");
		return categoryParametersService.getParametersForCategory(categoryId);
	}

	@Transactional
	public NotificationDrafts persist(NotificationDrafts draft){
		LOGGER.info("Service Request to persist the draft details");
		return draftRepository.save(draft);
	}
	
    @SuppressWarnings("unchecked")
	public List<NotificationDrafts> searchDraft(NotificationDrafts draftObj) {
    	LOGGER.info("Service Request to Search Drafts");
        Criteria criteria = getCurrentSession().createCriteria(NotificationDrafts.class);
        if (StringUtils.isNotBlank(draftObj.getType()))
            criteria.add(Restrictions.ilike(EventnotificationConstant.DRAFT_NOTIFICATION_TYPE, draftObj.getType() + "%"));
        if (StringUtils.isNotBlank(draftObj.getName()))
            criteria.add(Restrictions.ilike(EventnotificationConstant.DRAFT_NAME, draftObj.getName() + "%"));
        criteria.addOrder(Order.desc(EventnotificationConstant.DRAFT_ID));
        return criteria.list();
    }
    
    /**
     * Fetch all the draft
     * @return List<Notificationschedule>
     */
    public List<NotificationDrafts> findAllDrafts() {
        return draftRepository.findAll();
    }

    /**
     * Fetch notification draft by id
     * @param id
     * @return NotificationDrafts
     */
    public NotificationDrafts findDraftById(Long id) {
        return draftRepository.findOne(id);
    }
    
    @Transactional
    public NotificationDrafts updateDraftById(Long id, NotificationDrafts updatedDraft) { 
    	NotificationDrafts existingDraft = draftRepository.findOne(id);
    	if(null != existingDraft) { 
    		existingDraft.setName(updatedDraft.getName());
    		existingDraft.setType(updatedDraft.getType());
    		existingDraft.setMessage(updatedDraft.getMessage());
    		existingDraft.setModule(updatedDraft.getModule());
    		existingDraft.setCategory(updatedDraft.getCategory());
    		existingDraft.setUpdatedDate(new Date().getTime());
    	}
    	return existingDraft;
    }

}
