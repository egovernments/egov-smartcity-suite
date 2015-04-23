package org.egov.ptis.domain.dao.property;

import java.util.List;

import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.infstr.utils.DateUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryDAO;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class CategoryHibDaoTest extends EgovHibernateTest {
	PropertyUsageDAO propUsagDao;
	BoundaryDAO boundaryDao;
	CategoryDao catDao;
	Boundary boundary;

	// getAllCategoriesbyHistory()
	public void testGetAllCategoriesbyHistory() {
		catDao = PropertyDAOFactory.getDAOFactory().getCategoryDao();
		List catList = catDao.getAllCategoriesbyHistory();
		assertFalse(catList.isEmpty());
	}

	// end of getAllCategoriesbyHistory()

	// getCategoryAmount()
	public void testGetCategoryAmountInputNull() {
		catDao = PropertyDAOFactory.getDAOFactory().getCategoryDao();
		Float catAmt = catDao.getCategoryAmount(null, null);
		assertEquals(null, catAmt);
	}

	public void testGetCategoryAmountInputUsageIdAndBndryId() {
		catDao = PropertyDAOFactory.getDAOFactory().getCategoryDao();
		propUsagDao = PropertyDAOFactory.getDAOFactory().getPropertyUsageDAO();
		PropertyUsage propUsage = (PropertyUsage) propUsagDao.getPropertyUsage("NONRESD");
		boundaryDao = new BoundaryDAO();
		boundary = boundaryDao.getBoundary(7612);
		Float catAmt = catDao.getCategoryAmount(propUsage.getId().intValue(), boundary.getId());
		assertEquals("4.5", catAmt.toString());
	}

	// end of getCategoryAmount()

	// getCategoryAmountByUsageAndBndryAndDate()
	public void testGetCategoryAmountByUsageAndBndryAndDateInputNull() {
		catDao = PropertyDAOFactory.getDAOFactory().getCategoryDao();
		Float catAmt = catDao.getCategoryAmountByUsageAndBndryAndDate(null, null, null);
		assertEquals(null, catAmt);
	}

	public void testGetCategoryAmountByUsageAndBndryAndDateInputUsageIdAndBndryId() {
		catDao = PropertyDAOFactory.getDAOFactory().getCategoryDao();
		propUsagDao = PropertyDAOFactory.getDAOFactory().getPropertyUsageDAO();
		PropertyUsage propUsage = (PropertyUsage) propUsagDao.getPropertyUsage("NONRESD");
		boundaryDao = new BoundaryDAO();
		boundary = boundaryDao.getBoundary(7612);
		Float catAmt = catDao.getCategoryAmountByUsageAndBndryAndDate(propUsage.getId().intValue(),
				boundary.getId(), DateUtils.getLastFinYearStartDate());
		assertEquals("4.5", catAmt.toString());
	}

	// end of getCategoryAmountByUsageAndBndryAndDate()

	// getCategoryByCategoryNameAndUsage()
	public void testGetCategoryByCategoryNameAndUsageWithInputNull() {
		catDao = PropertyDAOFactory.getDAOFactory().getCategoryDao();
		Category category = catDao.getCategoryByCategoryNameAndUsage(null);
		assertNull(category);
	}

	public void testGetCategoryByCategoryNameAndUsageWithWrongCategoryName() {
		PropertyUsageDAO propDao = PropertyDAOFactory.getDAOFactory().getPropertyUsageDAO();
		PropertyUsage propUsage = propDao.getPropertyUsage("RESD");
		catDao = PropertyDAOFactory.getDAOFactory().getCategoryDao();
		Category category = catDao.getCategoryByCategoryNameAndUsage(Restrictions.and(Restrictions
				.eq("propUsage", propUsage), Restrictions.eq("categoryName", "Wrong Category Name")));
		assertNull(category);
	}

	public void testGetCategoryByCategoryNameAndUsageWithCorrectCategoryName() {
		PropertyUsageDAO propDao = PropertyDAOFactory.getDAOFactory().getPropertyUsageDAO();
		PropertyUsage propUsage = propDao.getPropertyUsage("RESD");
		catDao = PropertyDAOFactory.getDAOFactory().getCategoryDao();
		Category category = catDao.getCategoryByCategoryNameAndUsage(Restrictions.and(Restrictions
				.eq("propUsage", propUsage), Restrictions.eq("categoryName", "B1")));
		assertNotNull(category);
	}

	// end of getCategoryByCategoryNameAndUsage()

	// getCategoryByCatAmtAndUsage()
	public void testGetCategoryByCatAmtAndUsageWithInputNull() {
		catDao = PropertyDAOFactory.getDAOFactory().getCategoryDao();
		List categoryList = catDao.getCategoryByCatAmtAndUsage(null);
		assertNotNull(categoryList.size() < 1);
	}

	public void testGetCategoryByCatAmtAndUsage() {
		catDao = PropertyDAOFactory.getDAOFactory().getCategoryDao();
		propUsagDao = PropertyDAOFactory.getDAOFactory().getPropertyUsageDAO();
		PropertyUsage propUsage = (PropertyUsage) propUsagDao.getPropertyUsage("NONRESD");
		Criterion criterion = Restrictions.and(Restrictions.eq("categoryAmount", Float
				.valueOf("4.5")), Restrictions.eq("propUsage", propUsage));
		List categoryList = catDao.getCategoryByCatAmtAndUsage(criterion);
		assertNotNull(categoryList.size() > 1);
	}
	// end of getCategoryByCatAmtAndUsage()
}