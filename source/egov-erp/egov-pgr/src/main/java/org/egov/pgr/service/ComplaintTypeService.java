package org.egov.pgr.service;

import org.egov.pgr.entity.ComplaintType;

import java.util.List;

public interface ComplaintTypeService {

	List<ComplaintType> getAllComplaintTypes();
	List<ComplaintType> getAllComplaintTypeByNameLike(String nameLike);
	ComplaintType getComplaintTypeById(Long id);
	List<ComplaintType> getAllComplaintTypeByDepartment(Long id);

	void createComplaintType(ComplaintType  complaintType);
}
