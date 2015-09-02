/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) <2015>  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *  	1) All versions of this program, verbatim or modified must carry this
 *  	   Legal Notice.
 *
 *  	2) Any misrepresentation of the origin of the material is prohibited. It
 *  	   is required that all modified versions of this material be marked in
 *  	   reasonable ways as different from the original version.
 *
 *  	3) This license does not grant any rights to any user of the program
 *  	   with regards to rights under trademark law for use of the trade names
 *  	   or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tradelicense.domain.entity;

import java.math.BigDecimal;

import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.infstr.models.BaseModel;

public class FeeMatrix extends BaseModel {
    private static final long serialVersionUID = 1L;
    private NatureOfBusiness businessNature;
    private SubCategory subcategory;
    private LicenseAppType applType;
    private FeeType feeType;
    private BigDecimal amount;
    private EgDemandReasonMaster demandReasonMaster;

    public EgDemandReasonMaster getDemandReasonMaster() {
        return demandReasonMaster;
    }

    public void setDemandReasonMaster(final EgDemandReasonMaster demandReasonMaster) {
        this.demandReasonMaster = demandReasonMaster;
    }

    public NatureOfBusiness getBusinessNature() {
        return businessNature;
    }

    public void setBusinessNature(final NatureOfBusiness businessNature) {
        this.businessNature = businessNature;
    }

    public SubCategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(final SubCategory subcategory) {
        this.subcategory = subcategory;
    }

    public LicenseAppType getApplType() {
        return applType;
    }

    public void setApplType(final LicenseAppType applType) {
        this.applType = applType;
    }

    public NatureOfBusiness getTradeNatureId() {
        return businessNature;
    }

    public FeeType getFeeType() {
        return feeType;
    }

    public void setFeeType(final FeeType feeType) {
        this.feeType = feeType;
    }

    public void setTradeNatureId(final NatureOfBusiness tradeNatureId) {
        businessNature = tradeNatureId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("FeeMatrix={");
        str.append("businessNature=").append(businessNature == null ? "null" : businessNature.toString());
        str.append("subcategory=").append(subcategory == null ? "null" : subcategory.toString());
        str.append("applType=").append(applType == null ? "null" : applType.toString());
        str.append("feeType=").append(feeType == null ? "null" : feeType.toString());
        str.append("amount=").append(amount == null ? "null" : amount.toString());
        str.append("}");
        return str.toString();
    }
}
