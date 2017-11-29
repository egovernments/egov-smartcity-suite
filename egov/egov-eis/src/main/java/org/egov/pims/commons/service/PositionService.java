/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.pims.commons.service;

import org.egov.eis.entity.Assignment;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

public class PositionService extends PersistenceService<Position, Integer> {  
	
    @PersistenceContext
	private EntityManager entityManager;

	public PositionService() {
	    super(Position.class);
    }

    public PositionService(Class<Position> type) {
        super(type);
    }
	    
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
			criteria.add(Restrictions.eq("position.deptDesig.designation.id", designationMasterId));
		}
			
		criteria.add(Subqueries.propertyNotIn("position.id", detachAssignment));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.asc("position.name"));
		return criteria;
	}
	

}
