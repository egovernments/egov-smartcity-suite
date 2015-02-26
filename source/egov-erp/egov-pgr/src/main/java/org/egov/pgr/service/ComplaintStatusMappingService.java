package org.egov.pgr.service;

import java.util.List;

import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.role.RoleImpl;
import org.egov.lib.rjbac.role.dao.RoleDAO;
import org.egov.pgr.entity.ComplaintStatus;
import org.egov.pgr.repository.ComplaintStatusMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintStatusMappingService {
	@Autowired
	private ComplaintStatusMappingRepository complaintStatusMappingRepository;

	@Autowired
	RoleDAO roleDAO;

	public List<ComplaintStatus> getStatusByRoleAndCurrentStatus(List<Role> role, ComplaintStatus status) {
		return complaintStatusMappingRepository.getStatusByRoleAndCurrentStatus(role, status);
	}

}
