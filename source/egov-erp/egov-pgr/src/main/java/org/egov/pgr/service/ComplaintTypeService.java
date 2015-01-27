package org.egov.pgr.service;

import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.repository.ComplaintTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ComplaintTypeService {

    private ComplaintTypeRepository complaintTypeRepository;

    @Autowired
    public ComplaintTypeService(ComplaintTypeRepository complaintTypeRepository) {
        this.complaintTypeRepository = complaintTypeRepository;
    }

    public ComplaintType findBy(Long complaintId) {
        return complaintTypeRepository.get(complaintId);
    }

    public void createComplaintType(ComplaintType complaintType) {
        complaintTypeRepository.create(complaintType);
    }

    public void updateComplaintType(ComplaintType complaintType) {
        complaintTypeRepository.save(complaintType);
    }

    public List<ComplaintType> findAll() {
        return complaintTypeRepository.query(ComplaintType.QRY_ALL_COMPLAINT_TYPES).list();
    }
}