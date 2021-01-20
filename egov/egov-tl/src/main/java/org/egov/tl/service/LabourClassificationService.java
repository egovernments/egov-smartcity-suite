package org.egov.tl.service;

import java.util.List;

import org.egov.tl.entity.LabourClassification;
import org.egov.tl.repository.LabourClassificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LabourClassificationService {
	
private final LabourClassificationRepository labourClassificationRepository;
	
	@Autowired
	public LabourClassificationService(final LabourClassificationRepository labourClassificationRepository) {
		this.labourClassificationRepository = labourClassificationRepository;
	}
	
	public List<LabourClassification> getLabourClassification() {
        return labourClassificationRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}
