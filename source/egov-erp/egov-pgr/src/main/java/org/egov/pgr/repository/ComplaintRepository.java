package org.egov.pgr.repository;

import org.egov.lib.rjbac.user.User;
import org.egov.pgr.entity.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

	Page<Complaint> findByCreatedBy(User createdBy, Pageable pageable);
	Page<Complaint> findByComplaintID(String complaintID,Pageable pageable);	
	Complaint findByComplaintID(String complaintID);
}
