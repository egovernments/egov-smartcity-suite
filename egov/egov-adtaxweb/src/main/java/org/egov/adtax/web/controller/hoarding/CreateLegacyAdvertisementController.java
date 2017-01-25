/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
package org.egov.adtax.web.controller.hoarding;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.validation.Valid;

import org.egov.adtax.entity.Advertisement;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.enums.AdvertisementStatus;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.adtax.web.controller.common.HoardingControllerSupport;
import org.egov.commons.Installment;
import org.egov.infra.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hoarding")
public class CreateLegacyAdvertisementController extends HoardingControllerSupport {

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @RequestMapping(value = "adtaxCreateLegacy", method = GET)
    public String createLegacyHoardingForm(@ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail) {
        if (advertisementPermitDetail != null && advertisementPermitDetail.getAdvertisement() == null)
            advertisementPermitDetail.setAdvertisement(new Advertisement());
        advertisementPermitDetail.getAdvertisement().setStatus(AdvertisementStatus.ACTIVE);
        advertisementPermitDetail.getAdvertisement().setLegacy(Boolean.TRUE);
        return "hoarding-createLegacy";
    }

    private void validateLegacyApplicationDate(final AdvertisementPermitDetail advertisementPermitDetail,
            final BindingResult resultBinder) {

        if (advertisementPermitDetail != null && advertisementPermitDetail.getApplicationDate() != null) {
            final Installment installmentObj = advertisementDemandService.getCurrentInstallment();
            if (installmentObj != null && installmentObj.getToDate() != null)
                if (advertisementPermitDetail.getApplicationDate()
                        .after(DateUtils.endOfDay(installmentObj.getToDate())))
                    resultBinder.rejectValue("applicationDate", "invalid.applicationDateForLegacy");
        }
        if (advertisementPermitDetail != null
                && advertisementPermitDetail.getPermissionstartdate() != null
                && advertisementPermitDetail.getPermissionenddate() != null
                && advertisementPermitDetail.getPermissionstartdate().after(
                        advertisementPermitDetail.getPermissionenddate()))
            resultBinder.rejectValue("permissionstartdate", "invalid.permissionFromDateAndToDateCompare");

    }

    @RequestMapping(value = "adtaxCreateLegacy", method = POST)
    public String createLegacyHoarding(@Valid @ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail,
            final BindingResult resultBinder, final RedirectAttributes redirAttrib) {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        validateHoardingDocs(advertisementPermitDetail, resultBinder);
        validateAdvertisementDetails(advertisementPermitDetail, resultBinder);
        validateLegacyApplicationDate(advertisementPermitDetail, resultBinder);
        if (resultBinder.hasErrors())
            return "hoarding-createLegacy";
        storeHoardingDocuments(advertisementPermitDetail);
        advertisementPermitDetail.setIsActive(Boolean.TRUE);
        advertisementPermitDetail.setStatus(advertisementPermitDetailService
                .getStatusByModuleAndCode(AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXPERMITGENERATED));
        final Installment installmentObj = advertisementDemandService.getCurrentInstallment();
        if (installmentObj != null && installmentObj.getFromDate() != null)
            try {
                advertisementPermitDetail.getAdvertisement()
                        .setPenaltyCalculationDate(formatter.parse(formatter.format(installmentObj.getFromDate())));
            } catch (final ParseException e) {
                // TODO: CHECK THIS CASE AGAIN.
            }

        advertisementPermitDetailService.createAdvertisementPermitDetail(advertisementPermitDetail, null, null, null, null);
        final String message = messageSource.getMessage("hoarding.create.success",
                new String[] { advertisementPermitDetail.getAdvertisement().getAdvertisementNumber(),
                        advertisementPermitDetail.getApplicationNumber(), advertisementPermitDetail.getPermissionNumber() },
                null);
        redirAttrib.addFlashAttribute("message", message);
        // return "redirect:/hoarding/createLegacy";
        return "redirect:/hoarding/success/" + advertisementPermitDetail.getId();
    }

}
