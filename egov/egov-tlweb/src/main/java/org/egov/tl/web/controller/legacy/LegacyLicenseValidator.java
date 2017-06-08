/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
 */

package org.egov.tl.web.controller.legacy;

import org.egov.tl.entity.TradeLicense;
import org.egov.tl.service.ValidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LegacyLicenseValidator implements Validator {

    @Autowired
    private ValidityService validityService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return TradeLicense.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {

        final TradeLicense license = (TradeLicense) target;

        if(!license.getDocuments().isEmpty()){
        for (int index = 0; index < license.getFiles().length; index++)
            if (license.getDocuments().get(index).getType().isMandatory() && license.getFiles()[index].isEmpty()
                    && license.getDocuments().isEmpty())
                errors.rejectValue("documents[" + index + "].description", "TL-011");
        }
        if (validityService.getApplicableLicenseValidity(license) == null)
            errors.rejectValue("category", "validate.license.validity");

        if (license.getTradeArea_weight().intValue() < 1)
            errors.rejectValue("tradeArea_weight", "validate.fee.range");

    }

}
