package org.egov.pgr.service;

import java.util.List;

import org.egov.pgr.entity.ComplaintType;

public interface ComplaintTypeService {

	List<ComplaintType> getAllComplaintTypes();
	List<ComplaintType> getAllComplaintTypeByNameLike(String nameLike);
	ComplaintType getComplaintTypeById(Long id);
	List<ComplaintType> getAllComplaintTypeByDepartment(Long id);
}
