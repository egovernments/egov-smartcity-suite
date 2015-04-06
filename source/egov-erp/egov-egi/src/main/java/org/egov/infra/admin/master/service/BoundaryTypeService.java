package org.egov.infra.admin.master.service;

import java.util.List;

import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.repository.BoundaryTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for the BoundaryType
 * 
 * @author Manasa Niranjan
 *
 */
@Service
@Transactional(readOnly = true)
public class BoundaryTypeService {

	private final BoundaryTypeRepository boundaryTypeRepository;
	
	@Autowired
    public BoundaryTypeService(final BoundaryTypeRepository boundaryTypeRepository) {
        this.boundaryTypeRepository = boundaryTypeRepository;
    }
    
    @Transactional
    public void createBoundaryType(final BoundaryType boundaryType) {
    	boundaryTypeRepository.save(boundaryType);
    }
    
    @Transactional
    public void updateBoundaryType(final BoundaryType boundaryType) {
    	boundaryTypeRepository.save(boundaryType);
    }
    
    public BoundaryType getBoundaryTypeById(Long id) {
        return boundaryTypeRepository.findOne(id);
    }

    public BoundaryType getBoundaryTypeByName(String name) {
        return boundaryTypeRepository.findByName(name);
    }
    
    public List<BoundaryType> getAllBoundaryTypes() {
        return boundaryTypeRepository.findAll();
    }
    
    public BoundaryType getBoundaryTypeByHierarchyTypeNameAndLevel(String name,Integer hierarchyLevel) {
    	return boundaryTypeRepository.findByHierarchyTypeNameAndLevel(name,hierarchyLevel);
    }
}
