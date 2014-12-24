package org.egov.ptis.domain.dao.property;

import org.egov.DuplicateElementException;
import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class BoundaryCategoryHibDaoTest extends EgovHibernateTest {

	BoundaryCategoryDao boundaryCatDao;
	PropertyUsageDAO propUsagDao = PropertyDAOFactory.getDAOFactory().getPropertyUsageDAO();
	BoundaryDAO boundaryDao;
	Category category;
	CategoryDao categoryDao;
	Boundary boundary;
	BoundaryCategory bndryCategory;

	public void testGetCategoryByBoundryAndUsageInputNull() throws Exception,
			DuplicateElementException {
		boundaryCatDao = PropertyDAOFactory.getDAOFactory().getBoundaryCategoryDao();
		category = boundaryCatDao.getCategoryByBoundryAndUsage(null, null);
		assertNull(category);
	}

	public void testGetCategoryByBoundryAndUsageInputBoundaryAndUsage() throws Exception,
			DuplicateElementException {
		boundaryCatDao = PropertyDAOFactory.getDAOFactory().getBoundaryCategoryDao();
		boundaryDao = new BoundaryDAO();
		propUsagDao = PropertyDAOFactory.getDAOFactory().getPropertyUsageDAO();
		PropertyUsage propUsage = (PropertyUsage) propUsagDao.getPropertyUsage("RESD");
		boundary = boundaryDao.getBoundary(7612);
		category = boundaryCatDao.getCategoryByBoundryAndUsage(boundary, propUsage);
		assertNotNull(category);
	}

	public void testGetCategoryByBoundryAndUsageAndDateInputNull() throws Exception,
			DuplicateElementException {
		boundaryCatDao = PropertyDAOFactory.getDAOFactory().getBoundaryCategoryDao();
		category = boundaryCatDao.getCategoryByBoundryAndUsageAndDate(null, null, null);
		assertNull(category);
	}

	public void testGetCategoryByBoundryAndUsageAndDateInputBoundaryAndUsage() throws Exception,
			DuplicateElementException {
		boundaryCatDao = PropertyDAOFactory.getDAOFactory().getBoundaryCategoryDao();
		boundaryDao = new BoundaryDAO();
		propUsagDao = PropertyDAOFactory.getDAOFactory().getPropertyUsageDAO();
		PropertyUsage propUsage = (PropertyUsage) propUsagDao.getPropertyUsage("RESD");
		boundary = boundaryDao.getBoundary(7612);
		category = boundaryCatDao.getCategoryByBoundryAndUsageAndDate(boundary, propUsage,
				DateUtils.getLastFinYearStartDate());
		assertNotNull(category);
	}

	public void testGetBoundaryCategoryByBoundaryAndCategoryWithInputNull() {
		boundaryCatDao = PropertyDAOFactory.getDAOFactory().getBoundaryCategoryDao();
		bndryCategory = boundaryCatDao.getBoundaryCategoryByBoundaryAndCategory(null);
		assertNull(bndryCategory);
	}

	public void testGetBoundaryCategoryByBoundaryAndCategory() {
		boundaryCatDao = PropertyDAOFactory.getDAOFactory().getBoundaryCategoryDao();
		boundaryDao = new BoundaryDAO();
		boundary = boundaryDao.getBoundary(7612);
		categoryDao = PropertyDAOFactory.getDAOFactory().getCategoryDao();
		category = new Category();
		category = (Category) categoryDao.findById(Long.valueOf(1), true);
		Criterion criterion = Restrictions.and(Restrictions.eq("bndry", boundary), Restrictions.eq(
				"category", category));
		bndryCategory = boundaryCatDao.getBoundaryCategoryByBoundaryAndCategory(criterion);
		assertNotNull(bndryCategory);
	}
}
