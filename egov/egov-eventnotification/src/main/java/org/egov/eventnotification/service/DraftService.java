package org.egov.eventnotification.service;

import java.util.List;

import org.egov.eventnotification.entity.CategoryParameters;
import org.egov.eventnotification.entity.ModuleCategory;
import org.egov.eventnotification.entity.NotificationDrafts;
import org.egov.eventnotification.entity.TemplateModule;
import org.egov.eventnotification.repository.DraftRepository;
import org.egov.eventnotification.repository.DraftRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DraftService {

    @Autowired
    private TemplateModuleService templateModuleService;

    @Autowired
    private ModuleCategoryService moduleCategoryService;

    @Autowired
    private DraftRepository draftRepository;

    @Autowired
    private CategoryParametersService categoryParametersService;

    @Autowired
    private DraftRepositoryImpl draftRepositoryImpl;

    public List<TemplateModule> getAllModules() {
        return templateModuleService.findAll();
    }

    public List<ModuleCategory> getAllCategories() {
        return moduleCategoryService.findAll();
    }

    public List<CategoryParameters> getAllCategoryParameters() {
        return categoryParametersService.findAll();
    }

    public List<NotificationDrafts> getAllNotificationDrafts() {
        return draftRepository.findAll();
    }

    public List<ModuleCategory> getCategoriesForModule(Long moduleId) {
        return moduleCategoryService.getCategoriesForModule(moduleId);
    }

    public List<CategoryParameters> getParametersForCategory(Long categoryId) {
        return categoryParametersService.getParametersForCategory(categoryId);
    }

    @Transactional
    public NotificationDrafts save(NotificationDrafts draft) {
        return draftRepository.save(draft);
    }

    public List<NotificationDrafts> searchDraft(NotificationDrafts draftObj) {
        return draftRepositoryImpl.searchDraft(draftObj);
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
    public NotificationDrafts updateDraft(NotificationDrafts updatedDraft) {
        return draftRepository.save(updatedDraft);
    }

    public List<CategoryParameters> getParameters() {
        return categoryParametersService.findAll();
    }

}
