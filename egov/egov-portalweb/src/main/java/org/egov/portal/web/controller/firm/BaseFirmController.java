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
package org.egov.portal.web.controller.firm;

import org.egov.infra.validation.regex.Constants;
import org.egov.portal.entity.Firm;
import org.egov.portal.entity.FirmUser;
import org.egov.portal.firm.service.FirmService;
import org.egov.portal.firm.service.FirmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

@Controller
public abstract class BaseFirmController {

    @Autowired
    private FirmService firmService;

    @Autowired
    private FirmUserService firmUserService;

    protected void validateFirm(final Firm firm, final BindingResult resultBinder) {

        validateFirmHeader(firm, resultBinder);

        if (firm.getPan() != null) {
            final Firm exitingFirm = firmService.getFirmByPan(firm.getPan());
            if (exitingFirm != null && firm.getId() != null && !exitingFirm.getId().equals(firm.getId()))
                resultBinder.reject("error.firm.pan.unique", "error.firm.pan.unique");
        }

        validateEmail(firm, resultBinder, "update");
    }

    protected void validateFirmHeader(final Firm firm, final BindingResult resultBinder) {
        if (firm.getTempFirmUsers() == null)
            resultBinder.reject("error.firm.altleastone.overusers.needed",
                    "error.firm.altleastone.firmusers.needed");
        if (firm.getName() == null)
            resultBinder.reject("error.firm.firmname", "error.firm.firmname");
        if (firm.getPan() == null)
            resultBinder.reject("error.firm.pan", "error.firm.pan");
    }

    protected void validateEmail(final Firm firm, final BindingResult resultBinder, final String mode) {

        int index = 0;
        for (final FirmUser firmUsers : firm.getTempFirmUsers()) {

            if (firmUsers.getEmailId() != null && !firmUsers.getEmailId().matches(Constants.EMAIL))
                resultBinder.rejectValue("tempFirmUsers[" + index + "].emailId",
                        "error.firm.emailid");
            if (firmUsers.getMobileNumber() != null && firmUsers.getMobileNumber().length() != 10)
                resultBinder.rejectValue("tempFirmUsers[" + index + "].mobileNumber",
                        "Pattern.citizen.mobileNumber");

            if (firmUsers.getEmailId() != null && firmUsers.getId() != null && mode.equalsIgnoreCase("update")) {
                final FirmUser existingFirmUser = firmUserService.getFirmUserByEmail(firmUsers.getEmailId());
                if (existingFirmUser != null && !existingFirmUser.getId().equals(firmUsers.getId()))
                    resultBinder.rejectValue("tempFirmUsers[" + index + "].emailId",
                            "error.firm.email.exist");

            }

            if (firmUsers.getEmailId() != null && mode.equalsIgnoreCase("create")) {
                final FirmUser existingFirmUser = firmUserService.getFirmUserByEmail(firmUsers.getEmailId());
                if (existingFirmUser != null)
                    resultBinder.rejectValue("tempFirmUsers[" + index + "].emailId",
                            "error.firm.email.exist");

            }
            index++;
        }

    }

}