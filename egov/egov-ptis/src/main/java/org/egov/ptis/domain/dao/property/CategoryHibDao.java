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
import org.egov.ptis.domain.entity.property.Category;
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
import java.util.Date;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class CategoryHibDao implements CategoryDao {

	public static final String FROM_DATE = "fromDate";
	public static final String TO_DATE = "toDate";

	@PersistenceContext
	private EntityManager entityManager;

	private Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	@Override
	public List getAllCategoriesbyHistory() {
		Query qry = getCurrentSession().createQuery(
				"from Category C where " + "(C.toDate IS NULL AND C.fromDate <= :currDate) "
						+ "OR " + "(C.fromDate <= :currDate AND C.toDate >= :currDate)) ");
		qry.setDate("currDate", new Date());
		return qry.list();
	}

	@Override
	public Float getCategoryAmount(Integer usageId, Integer bndryId) {
		Query qry = null;
		Float catAmt = null;
		if (usageId != null && bndryId != null) {
			qry = getCurrentSession()
					.createQuery(
							"select C.categoryAmount from Category C left join C.catBoundaries cb left join cb.bndry cbndry where cbndry.id =:bndryId and C.propUsage =:usageId AND ("
									+ "(C.toDate IS NULL AND C.fromDate <= :currDate) "
									+ "OR "
									+ "(C.fromDate <= :currDate AND C.toDate >= :currDate)) ");
			qry.setInteger("bndryId", bndryId);
			qry.setInteger("usageId", usageId);
			qry.setDate("currDate", new Date());
			// qry.setInteger("instId",instId);
			catAmt = (Float) qry.uniqueResult();
		}
		return catAmt;
	}

	@Override
	public Float getCategoryAmountByUsageAndBndryAndDate(Integer usageId, Integer bndryId,
			Date fromDate) {
		Query qry = null;
		Float catAmt = null;
		if (usageId != null && bndryId != null && fromDate != null) {
			qry = getCurrentSession()
					.createQuery(
							"select C.categoryAmount from Category C left join C.catBoundaries cb left join cb.bndry cbndry where cbndry.id =:bndryId and C.propUsage =:usageId AND ("
									+ "(C.toDate IS NULL AND C.fromDate <= :fromDate) "
									+ "OR "
									+ "(C.fromDate <= :fromDate AND C.toDate >= :fromDate)) ");
			qry.setInteger("bndryId", bndryId);
			qry.setInteger("usageId", usageId);
			qry.setDate(FROM_DATE, fromDate);
			// qry.setInteger("instId",instId);
			catAmt = (Float) qry.uniqueResult();
		}
		return catAmt;
	}

	@Override
	public Float getCatAmntByPropertyId(String pid) {

		Query qry = getCurrentSession().createQuery(
				"select bp.boundary from BasicPropertyImpl bp where bp.upicNo like :pid");
		qry.setString("pid", pid);
		Boundary bndry = (Boundary) qry.uniqueResult();

		qry = getCurrentSession()
				.createQuery(
						"select cat.categoryAmount from BoundaryCategory bCat left join bCat.category cat where bCat.bndry like :bndry");
		qry.setEntity("bndry", bndry);

		Float catAmnt = (Float) qry.uniqueResult();

		return catAmnt;
	}

	/**
	 * Returns Category object by CategoryName and Usage by passing criterion as
	 * a parameter
	 * 
	 * @param criterion
	 * @return Category.
	 **/

	@Override
	public Category getCategoryByCategoryNameAndUsage(Criterion criterion) {
		Criteria criteria = getCurrentSession().createCriteria(Category.class);
		Category category = null;
		if (criterion != null) {
			Criterion dateCondn1 = Restrictions.and(Restrictions.le(FROM_DATE, new Date()),
					Restrictions.isNull(TO_DATE));
			Criterion dateCondn2 = Restrictions.and(Restrictions.le(FROM_DATE, new Date()),
					Restrictions.ge(TO_DATE, new Date()));
			Criterion dateCondn = Restrictions.or(dateCondn1, dateCondn2);

			criteria.add(criterion);
			criteria.add(dateCondn);
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			category = (Category) criteria.uniqueResult();
		}
		return category;
	}

	/**
	 * Returns ategory by category amount and usage id by passing criterion as a
	 * parameter
	 * 
	 * @param criterion
	 * @return List Category.
	 **/

	@Override
	public List<Category> getCategoryByCatAmtAndUsage(Criterion criterion) {
		Criteria criteria = getCurrentSession().createCriteria(Category.class);
		if (criterion != null) {
			Criterion dateCondn1 = Restrictions.and(Restrictions.le(FROM_DATE, new Date()),
					Restrictions.isNull(TO_DATE));
			Criterion dateCondn2 = Restrictions.and(Restrictions.le(FROM_DATE, new Date()),
					Restrictions.ge(TO_DATE, new Date()));
			Criterion dateCondn = Restrictions.or(dateCondn1, dateCondn2);

			criteria.add(criterion);
			criteria.add(dateCondn);
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		}
		return criteria.list();
	}

	/**
	 * 
	 * @param criterion
	 * @return List Category
	 */

	@Override
	public List<Category> getCategoryByRateUsageAndStructClass(Criterion criterion) {
		Criteria criteria = getCurrentSession().createCriteria(Category.class);
		if (criterion != null) {
			criteria.add(criterion);
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		}
		return criteria.list();
	}

	@Override
	public Category findById(Integer id, boolean lock) {

		return null;
	}

	@Override
	public List<Category> findAll() {

		return null;
	}

	@Override
	public Category create(Category category) {

		return null;
	}

	@Override
	public void delete(Category category) {


	}

	@Override
	public Category update(Category category) {

		return null;
	}
}
