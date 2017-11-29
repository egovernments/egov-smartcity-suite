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

import org.egov.assets.util.AssetIdentifier;
import org.egov.infstr.models.BaseModel;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.Date;

public class AssetActivities extends BaseModel {

    private static final long serialVersionUID = 2507581475925078858L;

    /** The asset. */
    private Asset asset;

    /** The addition amount. */
    private BigDecimal additionAmount;

    /** The deduction amount. */
    private BigDecimal deductionAmount;

    /** The activity date. */
    private Date activityDate;

    @Enumerated(value = EnumType.STRING)
    private AssetIdentifier identifier;

    private String description;

    /**
     * Gets the asset.
     *
     * @return the asset
     */
    public Asset getAsset() {
        return asset;
    }

    /**
     * Sets the asset.
     *
     * @param asset the new asset
     */
    public void setAsset(final Asset asset) {
        this.asset = asset;
    }

    /**
     * Gets the addition amount.
     *
     * @return the addition amount
     */
    public BigDecimal getAdditionAmount() {
        return additionAmount;
    }

    /**
     * Sets the addition amount.
     *
     * @param additionAmount the new addition amount
     */
    public void setAdditionAmount(final BigDecimal additionAmount) {
        this.additionAmount = additionAmount;
    }

    /**
     * Gets the deduction amount.
     *
     * @return the deduction amount
     */
    public BigDecimal getDeductionAmount() {
        return deductionAmount;
    }

    /**
     * Sets the deduction amount.
     *
     * @param deductionAmount the new deduction amount
     */
    public void setDeductionAmount(final BigDecimal deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    /**
     * Gets the activity date.
     *
     * @return the activity date
     */
    public Date getActivityDate() {
        return activityDate;
    }

    /**
     * Sets the activity date.
     *
     * @param activityDate the new activity date
     */
    public void setActivityDate(final Date activityDate) {
        this.activityDate = activityDate;
    }

    public AssetIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final AssetIdentifier identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

}
