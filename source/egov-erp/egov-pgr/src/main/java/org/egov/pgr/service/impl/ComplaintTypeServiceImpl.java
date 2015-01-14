package org.egov.pgr.service.impl;

import java.util.List;

import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.repository.ComplaintTypeRepository;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class ComplaintTypeServiceImpl implements ComplaintTypeService {

	@Autowired
	ComplaintTypeRepository complaintTypeRepository;

	@Override
	public List<ComplaintType> getAllComplaintTypes() {
		return complaintTypeRepository.findAll(new Sort(Direction.ASC, "name"));
	}

	@Override
	public List<ComplaintType> getAllComplaintTypeByNameLike(String nameLike) {
		return complaintTypeRepository.findByNameContainingIgnoreCaseOrderByNameAsc(nameLike);
	}

	@Override
	public ComplaintType getComplaintTypeById(Long id) {
		return complaintTypeRepository.findOne(id);
	}

	@Override
	public List<ComplaintType> getAllComplaintTypeByDepartment(Long id) {
		return complaintTypeRepository.findByDepartmentId(id);
	}
	
	
	
}