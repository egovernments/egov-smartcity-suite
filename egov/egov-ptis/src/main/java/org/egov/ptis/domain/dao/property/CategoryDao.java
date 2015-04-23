/*
 * CategoryDao.java Created on Dec 15, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.util.Date;
import java.util.List;

import org.egov.infstr.dao.GenericDAO;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.hibernate.criterion.Criterion;

/**
 * TODO Brief Description of the puprpose of the class/interface
 * 
 * @author Administrator
 * @version 2.00
 */
public interface CategoryDao extends GenericDAO {
	public List getAllCategoriesbyHistory();

	public Float getCategoryAmount(Integer usageId, Integer bndryId);

	public Float getCategoryAmountByUsageAndBndryAndDate(Integer usageId, Integer bndryId,
			Date fromDate);

	public Float getCatAmntByPropertyId(String pid);

	public Category getCategoryByCategoryNameAndUsage(Criterion criterion);

	public List<Category> getCategoryByCatAmtAndUsage(Criterion criterion);
	
	public List<Category> getCategoryByRateUsageAndStructClass(Criterion criterion);

}
