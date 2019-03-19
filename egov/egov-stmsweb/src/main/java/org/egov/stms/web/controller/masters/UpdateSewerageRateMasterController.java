/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2019>  eGovernments Foundation
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
 *           Further, all user interfaces, including but not limited to citizen facing interfaces,
 *           Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *           derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	       For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	       For any further queries on attribution, including queries on brand guidelines,
 *           please contact contact@egovernments.org
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

package org.egov.stms.web.controller.masters;

import org.egov.stms.masters.entity.SewerageRatesMaster;
import org.egov.stms.masters.service.SewerageRatesMasterService;
import org.egov.stms.web.controller.utils.SewerageMasterDataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.egov.stms.utils.constants.SewerageTaxConstants.DATEFORMATHYPEN;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MESSAGE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SEWERAGE_RATES_SUCCESS_PAGE;

@Controller
@RequestMapping(value = "/masters")
public class UpdateSewerageRateMasterController {
    private static final String SEWERAGE_MONTHLY_RATES_UPDATE = "sewerageRates-update";
    private static final String SEWERAGE_MULTIPLE_CLOSET_UPDATE = "seweragemonthlyRates-update";
    @Autowired
    private SewerageRatesMasterService sewerageRatesMasterService;
    @Autowired
    private SewerageMasterDataValidator sewerageMasterDataValidator;

    @ModelAttribute
    public SewerageRatesMaster sewerageRatesMaster(@PathVariable Long id) {
        return sewerageRatesMasterService.findBy(id);
    }

    @GetMapping("update/{id}")
    public String updateSewerageRates() {
        return sewerageRatesMasterService.getMultipleClosetAppconfigValue() ? SEWERAGE_MULTIPLE_CLOSET_UPDATE
                : SEWERAGE_MONTHLY_RATES_UPDATE;
    }

    @PostMapping("update/{id}")
    public String update(@ModelAttribute final SewerageRatesMaster sewerageRatesMaster, final Model model,
                         final RedirectAttributes redirectAttrs, final BindingResult errors) throws ParseException {
        final SimpleDateFormat newFormat = new SimpleDateFormat(DATEFORMATHYPEN);
        final String todaysDate = newFormat.format(new Date());
        final String effectiveFromDate = newFormat.format(sewerageRatesMaster.getFromDate());


        Boolean isMultipleClosetRatesAllowed = sewerageRatesMasterService.getMultipleClosetAppconfigValue();
        sewerageMasterDataValidator.validate(sewerageRatesMaster, errors, isMultipleClosetRatesAllowed);
        if (errors.hasErrors()) {
            model.addAttribute("sewerageRateMaster", sewerageRatesMaster);
            return isMultipleClosetRatesAllowed ? SEWERAGE_MULTIPLE_CLOSET_UPDATE : SEWERAGE_MONTHLY_RATES_UPDATE;
        }
        final Date currentDate = newFormat.parse(todaysDate);
        final Date effectiveDate = newFormat.parse(effectiveFromDate);
        if (effectiveDate.compareTo(currentDate) < 0) {
            model.addAttribute(MESSAGE, "msg.seweragerate.modification.rejected");
            return SEWERAGE_MONTHLY_RATES_UPDATE;
        }
        if (isMultipleClosetRatesAllowed)
            sewerageRatesMasterService.updateMultipleClosetRates(sewerageRatesMaster);
        else
            sewerageRatesMasterService.update(sewerageRatesMaster);
        redirectAttrs.addFlashAttribute(MESSAGE, "msg.seweragemonthlyrate.update.success");
        return SEWERAGE_RATES_SUCCESS_PAGE + sewerageRatesMaster.getId();
    }
}
