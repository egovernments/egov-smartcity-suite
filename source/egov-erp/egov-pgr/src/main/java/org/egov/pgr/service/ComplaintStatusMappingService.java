package org.egov.pgr.service;

import java.util.List;
import java.util.Set;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.pgr.entity.ComplaintStatus;
import org.egov.pgr.repository.ComplaintStatusMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ComplaintStatusMappingService {
    @Autowired
    private ComplaintStatusMappingRepository complaintStatusMappingRepository;

    @Autowired
    RoleService roleService;

    public List<ComplaintStatus> getStatusByRoleAndCurrentStatus(final Set<Role> role, final ComplaintStatus status) {
        return complaintStatusMappingRepository.getStatusByRoleAndCurrentStatus(role, status);
    }

}
