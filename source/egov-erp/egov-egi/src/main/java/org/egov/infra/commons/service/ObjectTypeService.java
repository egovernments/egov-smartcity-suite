/**
 * 
 */
package org.egov.infra.commons.service;

import org.egov.commons.ObjectType;
import org.egov.infra.commons.repository.ObjectTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vaibhav.K
 *
 */
@Service
@Transactional(readOnly=true)
public class ObjectTypeService {

    private ObjectTypeRepository objectTypeRepository;
    
    public ObjectTypeService(final ObjectTypeRepository objectTypeRepository) {
        this.objectTypeRepository = objectTypeRepository;
    }
    
    @Transactional
    public void create(final ObjectType objectType) {
        objectTypeRepository.save(objectType);
    }
    
    @Transactional
    public void update(final ObjectType objectType) {
        objectTypeRepository.save(objectType);
    }
    
    @Transactional
    public void delete(final ObjectType objectType) {
        objectTypeRepository.delete(objectType);
    }
    
    public ObjectType getObjectTypeByName(String name) {
        return objectTypeRepository.findByType(name);
    }
    
}
