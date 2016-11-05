package org.egov.pgr.service.es;

import org.egov.pgr.entity.es.ComplaintIndex;
import org.egov.pgr.repository.es.ComplaintIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ComplaintIndexSearchService {
    @Autowired
    private ComplaintIndexRepository complaintIndexRepository;

    public List<ComplaintIndex> searchComplaintIndex(String searchQuery) {
        return Collections.emptyList();
    }
}
