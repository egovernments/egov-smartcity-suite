package org.egov.portal.service;

import java.util.List;

import org.egov.infra.admin.master.entity.Module;
import org.egov.portal.entity.PortalServiceType;
import org.egov.portal.repository.PortalServiceTypeRepository;
import org.egov.portal.repository.specs.SearchPortalServiceTypeSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PortalServiceTypeService {

    @Autowired
    private PortalServiceTypeRepository portalServiceTypeRepository;

    public PortalServiceType getPortalServiceTypeById(final Long id) {
        return portalServiceTypeRepository.findById(id);
    }

    public List<Module> getAllModules() {
        return portalServiceTypeRepository.findAllModules();
    }

    public List<String> findAllServiceTypes(final Long moduleId) {
        return portalServiceTypeRepository.findAllServiceTypes(moduleId);
    }

    public List<PortalServiceType> searchPortalServiceType(final Long module, final String name) {
        return portalServiceTypeRepository
                .findAll(SearchPortalServiceTypeSpec.searchPortalServiceType(module, name));
    }

    @Transactional
    public void update(final PortalServiceType portalService) {
        portalServiceTypeRepository.save(portalService);
    }
}
