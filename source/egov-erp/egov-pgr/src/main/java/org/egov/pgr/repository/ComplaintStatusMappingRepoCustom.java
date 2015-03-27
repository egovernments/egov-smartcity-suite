package org.egov.pgr.repository;

import java.util.List;
import java.util.Set;

import org.egov.infra.admin.master.entity.Role;
import org.egov.pgr.entity.ComplaintStatus;

public interface ComplaintStatusMappingRepoCustom {

    List<ComplaintStatus> getStatusByRoleAndCurrentStatus(Set<Role> role, ComplaintStatus status);
}
