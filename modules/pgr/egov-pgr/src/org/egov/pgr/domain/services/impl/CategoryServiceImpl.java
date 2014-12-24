/*
 * @(#)ComplaintStatusServiceImpl.java 3.0, 23 Jul, 2013 3:29:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.services.impl;

import org.egov.pgr.domain.entities.Category;
import org.egov.pgr.domain.services.CategoryService;
import org.egov.pgr.services.persistence.EntityServiceImpl;

public class CategoryServiceImpl extends EntityServiceImpl<Category, Long> implements CategoryService {

	public CategoryServiceImpl() {
		super(Category.class);
	}
}
