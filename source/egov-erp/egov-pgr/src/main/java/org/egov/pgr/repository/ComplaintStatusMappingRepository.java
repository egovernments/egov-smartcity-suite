package org.egov.pgr.repository;

import java.util.List;

import org.egov.infra.persistence.service.HibernateRepository;
import org.egov.lib.rjbac.role.Role;
import org.egov.pgr.entity.ComplaintStatus;
import org.egov.pgr.entity.ComplaintStatusMapping;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ComplaintStatusMappingRepository extends HibernateRepository<ComplaintStatusMapping> {

	@Autowired
	public ComplaintStatusMappingRepository(SessionFactory sessionFactory) {
		super(sessionFactory, ComplaintStatusMapping.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<ComplaintStatus> getStatusByRoleAndCurrentStatus(List<Role> role, ComplaintStatus status) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ComplaintStatusMapping.class,"complaintMapping");
		criteria.add(Restrictions.eq("complaintMapping.currentStatus", status)).
		add(Restrictions.in("complaintMapping.role", role)).
		addOrder(Order.asc("complaintMapping.orderNo"));
		criteria.setProjection(Projections.property("complaintMapping.showStatus"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();

	}

}
