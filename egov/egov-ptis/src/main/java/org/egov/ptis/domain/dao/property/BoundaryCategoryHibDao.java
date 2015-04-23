/*
 * BoundaryCategoryHibDao.java Created on Dec 15, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;


/**
 * Hibernate implementation of BoundaryCategoryDao
 *
 * @author Manu
 * @version 2.00
 */
public class BoundaryCategoryHibDao extends GenericHibernateDAO implements BoundaryCategoryDao
{

	/**
	 * @param persistentClass
	 * @param session
	 */
	public BoundaryCategoryHibDao(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}

	public static final String BOUNDARY = "bndry";
	@Override
	public Category getCategoryForBoundary(Boundary bndry)
	{
		Category category = null;
		Query qry = null;
		if(bndry != null )
		{
			qry = getCurrentSession().createQuery("select C from Category C inner join C.catBoundaries BC where BC.bndry = :bndry AND (" +
					"(BC.toDate IS NULL AND BC.fromDate <= :currDate) " + "OR " +
			"(BC.fromDate <= :currDate AND BC.toDate >= :currDate)) ");
			qry.setEntity(BOUNDARY, bndry);
			qry.setDate("currDate", new Date());
			if(qry.list().size()==1)
				category = (Category)qry.uniqueResult();
		}
		return category;
	}

	@Override
	public Category getCategoryForBoundaryAndDate(Boundary bndry,Date date)
	{
		Category category = null;
		Query qry = null;
		if(bndry != null && date!=null)
		{
			qry = getCurrentSession().createQuery("select C from Category C inner join C.catBoundaries BC where BC.bndry = :bndry AND (" +
					"(BC.toDate IS NULL AND BC.fromDate <= :date) " + "OR " +
			"(BC.fromDate <= :date AND BC.toDate >= :date)) ");
			qry.setEntity(BOUNDARY, bndry);
			qry.setDate("date", date);
			if(qry.list().size()==1)
				category = (Category)qry.uniqueResult();
		}
		return category;
	}

	@Override
	public BoundaryCategory getBoundaryCategoryByBoundry(Boundary bndry) {
		BoundaryCategory bndryCategory = null;
		Query qry = null;
		if(bndry != null )
		{
			qry = getCurrentSession().createQuery("from BoundaryCategory BC where BC.bndry = :bndry AND (" +
					"(BC.toDate IS NULL AND BC.fromDate <= :currDate) " + "OR " +
			"(BC.fromDate <= :currDate AND BC.toDate >= :currDate)) ");
			qry.setEntity(BOUNDARY, bndry);
			qry.setDate("currDate", new Date());

			if(qry.list().size()==1)
				bndryCategory = (BoundaryCategory)qry.uniqueResult();
		}
		return bndryCategory;
	}

	@Override
	public BoundaryCategory getBoundaryCategoryByBoundryAndDate(Boundary bndry,Date date) {
		BoundaryCategory bndryCategory = null;
		Query qry = null;
		if(bndry != null && date!=null)
		{
			qry = getCurrentSession().createQuery("from BoundaryCategory BC where BC.bndry = :bndry AND (" +
					"(BC.toDate IS NULL AND BC.fromDate <= :date) " + "OR " +
			"(BC.fromDate <= :date AND BC.toDate >= :date)) ");
			qry.setEntity(BOUNDARY, bndry);
			qry.setDate("date", date);
			if(qry.list().size()==1)
				bndryCategory = (BoundaryCategory)qry.uniqueResult();
		}
		return bndryCategory;
	}

	@Override
	public Category getCategoryByBoundryAndUsage(Boundary bndry,PropertyUsage propertyUsage) {
		Category category = null;
		Query qry = null;
		if(bndry != null && propertyUsage != null)
		{
			qry = getCurrentSession().createQuery("select C from Category C inner join C.catBoundaries BC where BC.bndry = :bndry and C.propUsage = :propertyUsage AND (" +
					"(BC.toDate IS NULL AND BC.fromDate <= :currDate) " + "OR " +
			"(BC.fromDate <= :currDate AND BC.toDate >= :currDate)) ");
			qry.setEntity(BOUNDARY, bndry);
			qry.setEntity("propertyUsage", propertyUsage);
			qry.setDate("currDate", new Date());
			if(qry.list().size()==1)
				category = (Category)qry.uniqueResult();
		}
		return category;
	}

	@Override
	public Category getCategoryByBoundryAndUsageAndDate(Boundary bndry,PropertyUsage propertyUsage,Date date) {
		Category category = null;
		Query qry = null;
		if(bndry != null && propertyUsage != null && date!=null)
		{
			qry = getCurrentSession().createQuery("select C from Category C inner join C.catBoundaries BC where BC.bndry = :bndry and C.propUsage = :propertyUsage AND (" +
					"(BC.toDate IS NULL AND BC.fromDate <= :date) " + "OR " +
			"(BC.fromDate <= :date AND BC.toDate >= :date)) ");
			qry.setEntity(BOUNDARY, bndry);
			qry.setEntity("propertyUsage", propertyUsage);
			qry.setDate("date", date);
			if(qry.list().size()==1)
				category = (Category)qry.uniqueResult();
		}
		return category;
	}

	//get BoundaryCategory by boundary and category by passing criterion as parameter
	@Override
	public BoundaryCategory getBoundaryCategoryByBoundaryAndCategory(Criterion criterion)
	{
		Criteria criteria = getCurrentSession().createCriteria(BoundaryCategory.class);
		BoundaryCategory boundaryCategory=null;
		if(criterion!=null)
		{
			Criterion dateCondn1 = Restrictions.and(Restrictions.le("fromDate", new Date()),Restrictions.isNull("toDate"));
			Criterion dateCondn2 = Restrictions.and(Restrictions.le("fromDate", new Date()),Restrictions.ge("toDate", new Date()));
			Criterion dateCondn = Restrictions.or(dateCondn1, dateCondn2);

			criteria.add(criterion);
			criteria.add(dateCondn);
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			boundaryCategory=(BoundaryCategory)criteria.uniqueResult();
		}
		return boundaryCategory;
	}

	@Override
	public List<Category> getCategoriesByBoundry(Boundary bndry) {
		List<Category> list = new ArrayList<Category>();
		/*String query =  "SELECT cat.id_usage, " +
							"cat.id_struct_cl, " +
							"cat.category_amnt, " +
							"cat.category_name, " +
							"bcat.id_bndry " +
						"FROM egpt_mstr_category cat, " +
							"egpt_mstr_bndry_category bcat " +
						"WHERE cat.id_category = bcat.id_category " +
							"AND bcat.id_bndry =:bndryId ";	*/
		
		Query qry = HibernateUtil.getCurrentSession().createQuery(
				"select bc.category from BoundaryCategory bc where bc.bndry =:Boundary");
		qry.setEntity("Boundary", bndry);
		list = qry.list();
		return list;
	}
}
