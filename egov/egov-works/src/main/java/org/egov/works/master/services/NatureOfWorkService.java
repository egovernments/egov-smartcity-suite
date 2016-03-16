package org.egov.works.master.services;

import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.masters.NatureOfWork;
import org.springframework.stereotype.Service;

@Service
public class NatureOfWorkService extends PersistenceService<NatureOfWork, Integer> {
    
    public NatureOfWorkService(final Class<NatureOfWork> natureofwork) {
        this.type = natureofwork;
    }
}
