/*
 * BoundaryCategory.java Created on Dec 15, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.entity.property;

import java.util.Date;

import org.egov.infstr.models.BaseModel;
import org.egov.lib.admbndry.Boundary;

/**
 * Link table between Category and the Boundary. Boundary might belong belong to
 * a category. In a particular ULB, the category would normally be assigned to a
 * boundary level. Example in Delhi, Colanies are assigned to a category. In
 * Nirmala Nagara, wards are assigned a category.
 * 
 * @author Manu Srivastava
 * @version 1.00
 * @see org.egov.ptis.domain.entity.property.Category
 * @see org.egov.lib.admbndry.Boundary
 * @see org.egov.lib.admbndry.BoundaryType
 * @since 1.00
 */
public class BoundaryCategory extends BaseModel {
	private Boundary bndry;
	private Category category;
	private Date fromDate;
	private Date toDate;

	public Boundary getBndry() {
		return bndry;
	}

	public void setBndry(Boundary bndry) {
		this.bndry = bndry;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	@Override
	public String toString() {

		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getId()).append("|");
		objStr = (getBndry() != null) ? objStr.append("Boundary: ").append(getBndry().getName()) : objStr.append("");
		objStr.append("|Category: ").append(getCategory());

		return objStr.toString();
	}
}
