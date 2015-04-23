/*
 * Category.java Created on Dec 14, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.entity.property;

import java.util.Date;
import java.util.Set;

import org.egov.infstr.models.BaseModel;

/**
 * Category class is Primarily used in the Property Tax and is used to calculate
 * the base demand Normally for the boundary, the ULB specifies the category to
 * which it belongs and the rate of tax for per unit of Measurement. eg: Ashok
 * Nagar Ward may belong to Category A. The amount per sq. feet for the ward
 * might be specified as 500 Rs. per sq. Feet.
 * 
 * The Category class encapsulates the above information.
 * 
 * @author Manu Srivastava
 * @version 1.00
 * @see
 * @see
 * @since 1.00
 */
public class Category extends BaseModel {

	private String categoryName;

	private Float categoryAmount;

	private Set<BoundaryCategory> catBoundaries;

	private Character isHistory;

	private PropertyUsage propUsage;

	private Date fromDate;

	private Date toDate;

	private StructureClassification structureClass;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Float getCategoryAmount() {
		return categoryAmount;
	}

	public void setCategoryAmount(Float categoryAmount) {
		this.categoryAmount = categoryAmount;
	}

	public Set<BoundaryCategory> getCatBoundaries() {
		return catBoundaries;
	}

	public void setCatBoundaries(Set<BoundaryCategory> catBoundaries) {
		this.catBoundaries = catBoundaries;
	}

	public Character getIsHistory() {
		return isHistory;
	}

	public void setIsHistory(Character isHistory) {
		this.isHistory = isHistory;
	}

	public PropertyUsage getPropUsage() {
		return propUsage;
	}

	public void setPropUsage(PropertyUsage propUsage) {
		this.propUsage = propUsage;
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

	public StructureClassification getStructureClass() {
		return structureClass;
	}

	public void setStructureClass(StructureClassification structureClass) {
		this.structureClass = structureClass;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getId()).append("|");
		objStr.append("Category: ").append(getCategoryName()).append("|Amount: ").append(getCategoryAmount()).append(
				"|Usage: ").append(getPropUsage()).append("|Classification: ").append(getStructureClass());

		return objStr.toString();
	}
}
