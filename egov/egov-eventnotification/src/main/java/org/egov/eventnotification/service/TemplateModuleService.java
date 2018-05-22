package org.egov.eventnotification.service;

import java.util.List;

import org.egov.eventnotification.entity.TemplateModule;
import org.egov.eventnotification.repository.TemplateModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TemplateModuleService {

    @Autowired
    private TemplateModuleRepository templateModuleRepository;

    public List<TemplateModule> findAll() {
        return templateModuleRepository.findAll();
    }
}
