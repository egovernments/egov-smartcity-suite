package org.egov.eventnotification.service;

import java.util.List;

import org.egov.eventnotification.entity.ModuleCategory;
import org.egov.eventnotification.repository.ModuleCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ModuleCategoryService {

    @Autowired
    private ModuleCategoryRepository moduleCategoryRepository;

    public List<ModuleCategory> findAll() {
        return moduleCategoryRepository.findAll();
    }

    public List<ModuleCategory> getCategoriesForModule(Long moduleId) {
        return moduleCategoryRepository.findByModuleId(moduleId);
    }
}
