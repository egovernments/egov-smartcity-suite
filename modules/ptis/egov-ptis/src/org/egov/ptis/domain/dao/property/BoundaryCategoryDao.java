/*
 * BoundaryCategoryDao.java Created on Dec 15, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.util.Date;
import java.util.List;

import org.egov.infstr.dao.GenericDAO;
import org.egov.lib.admbndry.Boundary;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.hibernate.criterion.Criterion;

/**
 * TODO Brief Description of the purpose of the class/interface
 *
 * @author Manu 
 * @version 2.00
 */
public interface BoundaryCategoryDao extends GenericDAO
{
	public Category getCategoryForBoundary(Boundary bndry);
	public Category getCategoryForBoundaryAndDate(Boundary bndry,Date date);
	public BoundaryCategory getBoundaryCategoryByBoundry(Boundary bndry);
	public BoundaryCategory getBoundaryCategoryByBoundryAndDate(Boundary bndry,Date date);
	public Category getCategoryByBoundryAndUsage(Boundary bndry,PropertyUsage propertyUsage);
	public Category getCategoryByBoundryAndUsageAndDate(Boundary bndry,PropertyUsage propertyUsage,Date date);
	public BoundaryCategory getBoundaryCategoryByBoundaryAndCategory(Criterion criterion);
	public List<Category> getCategoriesByBoundry(Boundary bndry);
}
