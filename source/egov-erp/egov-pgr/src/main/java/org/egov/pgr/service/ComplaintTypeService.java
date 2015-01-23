package org.egov.pgr.service;

import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.repository.ComplaintTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ComplaintTypeService {

    private ComplaintTypeRepository complaintTypeRepository;

    @Autowired
    public ComplaintTypeService(ComplaintTypeRepository complaintTypeRepository) {
        this.complaintTypeRepository = complaintTypeRepository;
    }

    public void createComplaintType(ComplaintType complaintType) {
        complaintTypeRepository.create(complaintType);
    }
}