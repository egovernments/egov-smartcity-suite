/*
 * @(#)ComplaintDetailService.java 3.0, 23 Jul, 2013 3:29:43 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.displaytag.pagination.PaginatedList;
import org.egov.pgr.domain.entities.ComplaintDetails;
import org.egov.pgr.services.persistence.EntityService;

public interface ComplaintDetailService extends EntityService<ComplaintDetails, Long> {

	public ComplaintDetails createNewComplaint(ComplaintDetails complaint);

	public PaginatedList searchMyComplaints(Integer userId, String mode, int page, int pagesize);

	public PaginatedList searchComplaintsByName(HashMap<String, Object> hashMap, int page, int pagesize);

	public List<ComplaintDetails> getComplaintDetails(Map<String, Object> queryMapParam);

	public List<ComplaintDetails> getComplaintsEligibleForEscalation();

	public Date getComplaintExpiryDate(Long complaintType, Date escaDate);
}
