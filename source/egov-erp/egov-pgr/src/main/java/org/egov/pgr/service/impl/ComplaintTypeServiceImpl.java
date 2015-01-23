package org.egov.pgr.service.impl;

import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.repository.ComplaintTypeRepository;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly=true)
public class ComplaintTypeServiceImpl implements ComplaintTypeService {

	private ComplaintTypeRepository complaintTypeRepository;

	public ComplaintTypeServiceImpl() {
	}

	//	@Autowired
	public ComplaintTypeServiceImpl(ComplaintTypeRepository complaintTypeRepository) {
		this.complaintTypeRepository = complaintTypeRepository;
	}

	@Override
	public List<ComplaintType> getAllComplaintTypes() {
		return null;
//		return complaintTypeRepository.findAll(new Sort(Direction.ASC, "name"));
	}

	@Override
	public List<ComplaintType> getAllComplaintTypeByNameLike(String nameLike) {
		return complaintTypeRepository.findByNameContainingIgnoreCaseOrderByNameAsc(nameLike);
	}

	@Override
	public ComplaintType getComplaintTypeById(Long id) {
		return null;
//		return complaintTypeRepository.findOne(id);
	}

	@Override
	public List<ComplaintType> getAllComplaintTypeByDepartment(Long id) {
		return complaintTypeRepository.findByDepartmentId(id);
	}

	@Override
	public void createComplaintType(ComplaintType complaintType) {

	}
}