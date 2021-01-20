package org.egov.tl.service;

import java.util.List;

import org.egov.tl.entity.LabourEmployer;
import org.egov.tl.repository.LabourEmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LabourEmployeeService {

	private final LabourEmployerRepository labourEmployerRepository;
	
	@Autowired
	public LabourEmployeeService(final LabourEmployerRepository labourEmployerRepository) {
		this.labourEmployerRepository = labourEmployerRepository;
	}
	
	public List<LabourEmployer> getLabourEmployer() {
        return labourEmployerRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}
