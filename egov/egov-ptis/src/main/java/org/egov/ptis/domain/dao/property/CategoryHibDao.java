/*
 * CategoryHibDao.java Created on Dec 15, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.util.Date;
import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.ptis.domain.entity.property.Category;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate implementation of CategoryDao
 * 
 * @author Administrator
 * @version 2.00
 */
public class CategoryHibDao extends GenericHibernateDAO implements CategoryDao {

	/**
	 * @param persistentClass
	 * @param session
	 */
	public static final String FROM_DATE = "fromDate";
	public static final String TO_DATE = "toDate";

	public CategoryHibDao(Class persistentClass, Session session) {
		super(persistentClass, session);
	}

	@Override
	public List getAllCategoriesbyHistory() {
		Query qry = getCurrentSession()
				.createQuery(
						"from Category C where "
								+ "(C.toDate IS NULL AND C.fromDate <= :currDate) "
								+ "OR "
								+ "(C.fromDate <= :currDate AND C.toDate >= :currDate)) ");
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
	public Float getCategoryAmountByUsageAndBndryAndDate(Integer usageId,
			Integer bndryId, Date fromDate) {
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

		Query qry = getCurrentSession()
				.createQuery(
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
			Criterion dateCondn1 = Restrictions.and(
					Restrictions.le(FROM_DATE, new Date()),
					Restrictions.isNull(TO_DATE));
			Criterion dateCondn2 = Restrictions.and(
					Restrictions.le(FROM_DATE, new Date()),
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
			Criterion dateCondn1 = Restrictions.and(
					Restrictions.le(FROM_DATE, new Date()),
					Restrictions.isNull(TO_DATE));
			Criterion dateCondn2 = Restrictions.and(
					Restrictions.le(FROM_DATE, new Date()),
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
	public List<Category> getCategoryByRateUsageAndStructClass(
			Criterion criterion) {
		Criteria criteria = getCurrentSession().createCriteria(Category.class);
		if (criterion != null) {
			criteria.add(criterion);
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		}
		return criteria.list();
	}
}
