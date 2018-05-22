package org.egov.eventnotification.service;

import java.util.List;

import org.egov.eventnotification.entity.CategoryParameters;
import org.egov.eventnotification.entity.ModuleCategory;
import org.egov.eventnotification.repository.CategoryParametersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CategoryParametersService {

    @Autowired
    private CategoryParametersRepository categoryParametersRepository;

    public List<CategoryParameters> findAll() {
        return categoryParametersRepository.findAll();
    }

    public List<CategoryParameters> getParametersForCategory(Long categoryId) {
        ModuleCategory moduleCategory = new ModuleCategory();
        moduleCategory.setId(categoryId);
        return categoryParametersRepository.findByModuleCategory(moduleCategory);
    }

}
