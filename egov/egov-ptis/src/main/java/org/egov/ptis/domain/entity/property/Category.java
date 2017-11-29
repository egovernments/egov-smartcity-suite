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



package org.egov.ptis.domain.entity.property;

import org.egov.infstr.models.BaseModel;

import java.util.Date;
import java.util.Set;

/**
 * Category class is Primarily used in the Property Tax and is used to calculate
 * the base demand Normally for the boundary, the ULB specifies the category to
 * which it belongs and the rate of tax for per unit of Measurement. eg: Ashok
 * Nagar Ward may belong to Category A. The amount per sq. feet for the ward
 * might be specified as 500 Rs. per sq. Feet. The Category class encapsulates
 * the above information.
 * 
 * @author Manu Srivastava
 * @version 1.00
 * @see
 * @see
 * @since 1.00
 */
public class Category extends BaseModel {

    private String categoryName;
    private Double categoryAmount;
    private Set<BoundaryCategory> catBoundaries;
    private Character isHistory;
    private PropertyUsage propUsage;
    private Date fromDate;
    private Date toDate;
    private StructureClassification structureClass;
    private boolean isActive;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Double getCategoryAmount() {
        return categoryAmount;
    }

    public void setCategoryAmount(Double categoryAmount) {
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

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        StringBuilder objStr = new StringBuilder();

        objStr.append("Id: ").append(getId()).append("|");
        objStr.append("Category: ").append(getCategoryName()).append("|Amount: ").append(getCategoryAmount())
                .append("|Usage: ").append(getPropUsage()).append("|Classification: ").append(getStructureClass());

        return objStr.toString();
    }
}
