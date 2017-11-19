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
package org.egov.ptis.domain.dao.property;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository(value = "boundaryCategoryDAO")
@Transactional(readOnly = true)
public class BoundaryCategoryHibDao implements BoundaryCategoryDao {

	@PersistenceContext
	private EntityManager entityManager;

	private Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	public static final String BOUNDARY = "bndry";

	@Override
	public Category getCategoryForBoundary(Boundary bndry) {
		Category category = null;
		Query qry = null;
		if (bndry != null) {
			qry = getCurrentSession().createQuery(
					"select C from Category C inner join C.catBoundaries BC where BC.bndry = :bndry AND ("
							+ "(BC.toDate IS NULL AND BC.fromDate <= :currDate) " + "OR "
							+ "(BC.fromDate <= :currDate AND BC.toDate >= :currDate)) ");
			qry.setEntity(BOUNDARY, bndry);
			qry.setDate("currDate", new Date());
			if (qry.list().size() == 1)
				category = (Category) qry.uniqueResult();
		}
		return category;
	}

	@Override
	public Category getCategoryForBoundaryAndDate(Boundary bndry, Date date) {
		Category category = null;
		Query qry = null;
		if (bndry != null && date != null) {
			qry = getCurrentSession().createQuery(
					"select C from Category C inner join C.catBoundaries BC where BC.bndry = :bndry AND ("
							+ "(BC.toDate IS NULL AND BC.fromDate <= :date) " + "OR "
							+ "(BC.fromDate <= :date AND BC.toDate >= :date)) ");
			qry.setEntity(BOUNDARY, bndry);
			qry.setDate("date", date);
			if (qry.list().size() == 1)
				category = (Category) qry.uniqueResult();
		}
		return category;
	}

	@Override
	public BoundaryCategory getBoundaryCategoryByBoundry(Boundary bndry) {
		BoundaryCategory bndryCategory = null;
		Query qry = null;
		if (bndry != null) {
			qry = getCurrentSession().createQuery(
					"from BoundaryCategory BC where BC.bndry = :bndry AND ("
							+ "(BC.toDate IS NULL AND BC.fromDate <= :currDate) " + "OR "
							+ "(BC.fromDate <= :currDate AND BC.toDate >= :currDate)) ");
			qry.setEntity(BOUNDARY, bndry);
			qry.setDate("currDate", new Date());

			if (qry.list().size() == 1)
				bndryCategory = (BoundaryCategory) qry.uniqueResult();
		}
		return bndryCategory;
	}

	@Override
	public BoundaryCategory getBoundaryCategoryByBoundryAndDate(Boundary bndry, Date date) {
		BoundaryCategory bndryCategory = null;
		Query qry = null;
		if (bndry != null && date != null) {
			qry = getCurrentSession().createQuery(
					"from BoundaryCategory BC where BC.bndry = :bndry AND ("
							+ "(BC.toDate IS NULL AND BC.fromDate <= :date) " + "OR "
							+ "(BC.fromDate <= :date AND BC.toDate >= :date)) ");
			qry.setEntity(BOUNDARY, bndry);
			qry.setDate("date", date);
			if (qry.list().size() == 1)
				bndryCategory = (BoundaryCategory) qry.uniqueResult();
		}
		return bndryCategory;
	}

	@Override
	public Category getCategoryByBoundryAndUsage(Boundary bndry, PropertyUsage propertyUsage) {
		Category category = null;
		Query qry = null;
		if (bndry != null && propertyUsage != null) {
			qry = getCurrentSession()
					.createQuery(
							"select C from Category C inner join C.catBoundaries BC where BC.bndry = :bndry and C.propUsage = :propertyUsage AND ("
									+ "(BC.toDate IS NULL AND BC.fromDate <= :currDate) "
									+ "OR "
									+ "(BC.fromDate <= :currDate AND BC.toDate >= :currDate)) ");
			qry.setEntity(BOUNDARY, bndry);
			qry.setEntity("propertyUsage", propertyUsage);
			qry.setDate("currDate", new Date());
			if (qry.list().size() == 1)
				category = (Category) qry.uniqueResult();
		}
		return category;
	}

	@Override
	public Category getCategoryByBoundryAndUsageAndDate(Boundary bndry,
			PropertyUsage propertyUsage, Date date) {
		Category category = null;
		Query qry = null;
		if (bndry != null && propertyUsage != null && date != null) {
			qry = getCurrentSession()
					.createQuery(
							"select C from Category C inner join C.catBoundaries BC where BC.bndry = :bndry and C.propUsage = :propertyUsage AND ("
									+ "(BC.toDate IS NULL AND BC.fromDate <= :date) "
									+ "OR "
									+ "(BC.fromDate <= :date AND BC.toDate >= :date)) ");
			qry.setEntity(BOUNDARY, bndry);
			qry.setEntity("propertyUsage", propertyUsage);
			qry.setDate("date", date);
			if (qry.list().size() == 1)
				category = (Category) qry.uniqueResult();
		}
		return category;
	}

	// get BoundaryCategory by boundary and category by passing criterion as
	// parameter

	@Override
	public BoundaryCategory getBoundaryCategoryByBoundaryAndCategory(Criterion criterion) {
		Criteria criteria = getCurrentSession().createCriteria(BoundaryCategory.class);
		BoundaryCategory boundaryCategory = null;
		if (criterion != null) {
			Criterion dateCondn1 = Restrictions.and(Restrictions.le("fromDate", new Date()),
					Restrictions.isNull("toDate"));
			Criterion dateCondn2 = Restrictions.and(Restrictions.le("fromDate", new Date()),
					Restrictions.ge("toDate", new Date()));
			Criterion dateCondn = Restrictions.or(dateCondn1, dateCondn2);

			criteria.add(criterion);
			criteria.add(dateCondn);
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			boundaryCategory = (BoundaryCategory) criteria.uniqueResult();
		}
		return boundaryCategory;
	}

	@Override
	public List<Category> getCategoriesByBoundry(Boundary bndry) {
		List<Category> list = new ArrayList<Category>();
		/*
		 * String query = "SELECT cat.id_usage, " + "cat.id_struct_cl, " +
		 * "cat.category_amnt, " + "cat.category_name, " + "bcat.id_bndry " +
		 * "FROM egpt_mstr_category cat, " + "egpt_mstr_bndry_category bcat " +
		 * "WHERE cat.id_category = bcat.id_category " +
		 * "AND bcat.id_bndry =:bndryId ";
		 */

		Query qry = getCurrentSession().createQuery(
				"select bc.category from BoundaryCategory bc where bc.bndry =:Boundary");
		qry.setEntity("Boundary", bndry);
		list = qry.list();
		return list;
	}

	@Override
	public BoundaryCategory findById(Integer id, boolean lock) {

		return null;
	}

	@Override
	public BoundaryCategory create(BoundaryCategory category) {

		return null;
	}

	@Override
	public void delete(BoundaryCategory category) {


	}

	@Transactional
	public BoundaryCategory update(BoundaryCategory category) {
		entityManager.persist(category);
		return category;
	}

	@Override
	public List<BoundaryCategory> findAll() {

		return null;
	}
}
