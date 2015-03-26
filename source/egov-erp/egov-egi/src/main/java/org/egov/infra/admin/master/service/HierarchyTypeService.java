package org.egov.infra.admin.master.service;

import java.util.List;

import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.repository.HierarchyTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for the HierarchyType
 * 
 * @author nayeem
 *
 */
@Service
@Transactional(readOnly = true)
public class HierarchyTypeService {
    
    private HierarchyTypeRepository hierarchyTypeRepository;
    
    @Autowired
    public HierarchyTypeService(HierarchyTypeRepository hierarchyTypeRepository) {
        this.hierarchyTypeRepository = hierarchyTypeRepository;
    }
    
    @Transactional
    public void createHierarchyType(HierarchyType hierarchyType) {
        hierarchyTypeRepository.save(hierarchyType);
    }
    
    @Transactional
    public void updateHierarchyType(HierarchyType hierarchyType) {
        hierarchyTypeRepository.save(hierarchyType);
    }
    
    public HierarchyType getHierarchyTypeById(Long id) {
        return hierarchyTypeRepository.findOne(id);
    }

    public HierarchyType getHierarchyTypeByName(String name) {
        return hierarchyTypeRepository.findByName(name);
    }
    
    public List<HierarchyType> getAllHierarchyTypes() {
        return hierarchyTypeRepository.findAll();
    }
}
