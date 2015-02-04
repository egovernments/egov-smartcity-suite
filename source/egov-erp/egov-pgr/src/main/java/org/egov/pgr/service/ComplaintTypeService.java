package org.egov.pgr.service;

import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.repository.ComplaintTypeRepository;
import org.egov.search.Index;
import org.egov.search.ResourceType;
import org.egov.search.index.domain.Document;
import org.egov.search.index.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ComplaintTypeService {

    private ComplaintTypeRepository complaintTypeRepository;
    private IndexService indexService;

    @Autowired
    public ComplaintTypeService(ComplaintTypeRepository complaintTypeRepository, IndexService indexService) {
        this.complaintTypeRepository = complaintTypeRepository;
        this.indexService = indexService;
    }

    @Transactional(readOnly = true)
    public ComplaintType findBy(Long complaintId) {
        return complaintTypeRepository.get(complaintId);
    }

    public void createComplaintType(ComplaintType complaintType) {
        complaintTypeRepository.create(complaintType);

        Document complaintTypeDocument = new Document(complaintType.getId().toString(), complaintType.toJson());
        indexService.index(Index.PGR, ResourceType.COMPLAINT_TYPE, complaintTypeDocument);
    }

    public void updateComplaintType(ComplaintType complaintType) {
        complaintTypeRepository.save(complaintType);
    }

    public List<ComplaintType> findAll() {
        return complaintTypeRepository.findAll();
    }

}