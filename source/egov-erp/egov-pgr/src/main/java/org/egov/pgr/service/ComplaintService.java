package org.egov.pgr.service;

import org.egov.pgr.entity.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface ComplaintService {
	Complaint createComplaint(Complaint complaint);
	
	String generateComplaintID();
	
	Page<Complaint> findAllCurrentUserComplaints(Pageable pageable);
	
	Page<Complaint> getComplaintByComplaintID(String complaintID,Pageable pageable);
	
	Page<Complaint> getComplaintsLike(Complaint complaint, Date fromDate, Date toDate, long totalComplaints, Pageable pageable);
	
	Complaint getComplaintByComplaintID(String complaintID);
}
