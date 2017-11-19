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
package org.egov.assets.model;

import org.egov.commons.CFinancialYear;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * DepreciationMetaData entity.
 *
 * @author Nilesh
 */

public class DepreciationMetaData extends BaseModel {

    // Constructors

    /**
     *
     */
    private static final long serialVersionUID = -4404379086769814828L;

    /** default constructor */
    public DepreciationMetaData() {
    }

    // Fields
    @Required(message = "depmetadata.rate.null")
    @Min(value = 0, message = "depmetadata.rate.not.negative")
    private Float depreciationRate;

    @Required(message = "depmetadata.financialyear.null")
    private CFinancialYear financialYear;

    private AssetCategory assetCategory;

    public Float getDepreciationRate() {
        return depreciationRate;
    }

    public void setDepreciationRate(final Float depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public CFinancialYear getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(final CFinancialYear financialYear) {
        this.financialYear = financialYear;
    }

    public AssetCategory getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(final AssetCategory assetCategory) {
        this.assetCategory = assetCategory;
    }

    @Override
    public String toString() {
        final StringBuilder objString = new StringBuilder();
        final String NEW_LINE = System.getProperty("line.separator");

        objString.append(this.getClass().getName() + " Object {" + NEW_LINE);
        objString.append(" Id: " + id + NEW_LINE);
        objString.append(" Dep Rate: " + depreciationRate + NEW_LINE);
        objString.append(" Year: " + (financialYear == null ? "null" : financialYear.getId()) + NEW_LINE);
        objString.append(" Cat: " + (assetCategory == null ? "null" : assetCategory.getId()) + NEW_LINE);
        objString.append("}");

        return objString.toString();
    }

    @Override
    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        if (depreciationRate <= 0.0)
            validationErrors.add(new ValidationError("deprate", "depmetadata.rate.percentage_greater_than_0"));
        if (depreciationRate > 100.0)
            validationErrors.add(new ValidationError("deprate", "depmetadata.rate.percentage_less_than_100"));
        if (financialYear == null || financialYear.getId() == null)
            validationErrors.add(new ValidationError("financialyear", "depmetadata.financialyear.required"));
        return validationErrors;
    }
}