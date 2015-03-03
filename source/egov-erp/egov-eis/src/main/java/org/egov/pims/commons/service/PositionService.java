package org.egov.pims.commons.service;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class PositionService extends PersistenceService<Position, Integer> {  
	
	  @PersistenceContext
		private EntityManager entityManager;
	    
		public Session  getCurrentSession() {
			return entityManager.unwrap(Session.class);
		}
	/**
	 * gives vacant positions for given date range and designation 
	 * @param fromDate
	 * @param toDate
	 * @param designationMasterId
	 * @return
	 */
	public Criteria getVacantPositionCriteria(Date fromDate,Date toDate,Integer designationMasterId)
	{
		DetachedCriteria detachAssignmentPrd=DetachedCriteria.forClass(Assignment.class, "assignment");
		detachAssignmentPrd.add(Restrictions.and(Restrictions.le("assignment.fromDate", fromDate),
				Restrictions.or(Restrictions.ge("assignment.toDate", toDate), Restrictions.isNull("assignment.toDate")))).
				setProjection(Projections.property("assignment.id"));
		
		DetachedCriteria detachAssignment=DetachedCriteria.forClass(Assignment.class, "assignment");
		detachAssignment.add(Subqueries.propertyIn("assignment.id", detachAssignmentPrd));
		detachAssignment.add(Restrictions.eq("assignment.isPrimary", 'Y'));
		detachAssignment.setProjection(Projections.distinct(Projections.property("assignment.position.id")));
		
		Criteria criteria=getCurrentSession().createCriteria(Position.class, "position");
		if(designationMasterId!=null && !designationMasterId.equals("0")) 
		{
			criteria.add(Restrictions.eq("position.deptDesigId.desigId.designationId", designationMasterId));
		}
			
		criteria.add(Subqueries.propertyNotIn("position.id", detachAssignment));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.asc("position.name"));
		return criteria;
	}
	

}
