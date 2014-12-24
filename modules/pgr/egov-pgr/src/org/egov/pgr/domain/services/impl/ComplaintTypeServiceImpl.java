/*
 * @(#)ComplaintTypeServiceImpl.java 3.0, 23 Jul, 2013 3:29:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.egov.infstr.utils.HibernateUtil;
import org.egov.pgr.domain.entities.ComplaintTypes;
import org.egov.pgr.domain.services.ComplaintTypeService;
import org.egov.pgr.services.persistence.EntityServiceImpl;

public class ComplaintTypeServiceImpl extends EntityServiceImpl<ComplaintTypes, Long> implements ComplaintTypeService {

	public ComplaintTypeServiceImpl() {
		super(ComplaintTypes.class);

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ComplaintTypes> getTop5Complaints() {

		final List<ComplaintTypes> top5Complaints = new ArrayList<ComplaintTypes>();

		final StringBuffer query = new StringBuffer();

		query.append("select * from (select COMPLAINTTYPE,count(COMPLAINTTYPE) as count from EGGR_COMPLAINTDETAILS ").append(" group by  COMPLAINTTYPE having COMPLAINTTYPE").append("  is not null order by count desc ) where rownum <= 5");

		final List<Object[]> list = HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
		for (final Object[] objects : list) {

			final ComplaintTypes complaintTypes = findById(Long.valueOf(objects[0].toString()), false);
			top5Complaints.add(complaintTypes);
		}
		return top5Complaints;
	}
}
