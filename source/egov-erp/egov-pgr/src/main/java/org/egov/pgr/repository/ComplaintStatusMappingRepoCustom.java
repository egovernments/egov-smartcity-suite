package org.egov.pgr.repository;

import java.util.List;

import org.egov.lib.rjbac.role.Role;
import org.egov.pgr.entity.ComplaintStatus;

public interface ComplaintStatusMappingRepoCustom {

    List<ComplaintStatus> getStatusByRoleAndCurrentStatus(List<Role> role, ComplaintStatus status);
}
