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
package org.egov.stms.web.controller.utils;

import java.math.BigDecimal;

import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.masters.service.DonationMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SewerageApplicationCommonValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return SewerageApplicationCommonValidator.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        // override superclass validate method
    }

    @Autowired
    private DonationMasterService donationMasterService;

    public String getDonationAmount(final PropertyType propertyType, final Integer noofclosetsresidential,
            final Integer noofclosetsnonresidential) {
        BigDecimal donationAmount = BigDecimal.ZERO;
        BigDecimal residentialAmount = BigDecimal.ZERO;
        BigDecimal nonResidentialAmount = BigDecimal.ZERO;
        if (noofclosetsresidential != null && noofclosetsresidential != 0)
            residentialAmount = donationMasterService.getDonationAmountByNoOfClosetsAndPropertytypeForCurrentDate(
                    noofclosetsresidential, PropertyType.RESIDENTIAL);
        if (noofclosetsnonresidential != null && noofclosetsnonresidential != 0)
            nonResidentialAmount = donationMasterService.getDonationAmountByNoOfClosetsAndPropertytypeForCurrentDate(
                    noofclosetsnonresidential, PropertyType.NON_RESIDENTIAL);
        if (propertyType != null && propertyType.equals(PropertyType.MIXED) && residentialAmount != null
                && nonResidentialAmount != null)
            donationAmount = residentialAmount.add(nonResidentialAmount);
        else if (propertyType != null && propertyType.equals(PropertyType.RESIDENTIAL) && residentialAmount != null)
            donationAmount = residentialAmount;
        else if (propertyType != null && propertyType.equals(PropertyType.NON_RESIDENTIAL) && nonResidentialAmount != null)
            donationAmount = nonResidentialAmount;

        else if (propertyType != null)
            if (residentialAmount == null && nonResidentialAmount == null
                    || residentialAmount == null && noofclosetsresidential != null
                    || nonResidentialAmount == null && noofclosetsnonresidential != null)
                return null;
        return donationAmount.toString();
    }
}
