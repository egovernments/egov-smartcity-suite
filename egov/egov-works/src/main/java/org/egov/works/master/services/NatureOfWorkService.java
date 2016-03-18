package org.egov.works.master.services;

import java.util.List;

import org.egov.works.master.repository.NatureOfWorkRepository;
import org.egov.works.models.masters.NatureOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NatureOfWorkService {
    
    @Autowired
    private NatureOfWorkRepository natureOfWorkRepository;
    
    public List<NatureOfWork> findAll() {
        return natureOfWorkRepository.findAll();
    }
}
