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
package org.egov.adtax.web.controller.hoarding;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.adtax.entity.Advertisement;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.enums.AdvertisementStatus;
import org.egov.adtax.exception.HoardingValidationError;
import org.egov.adtax.web.controller.common.HoardingControllerSupport;
import org.egov.demand.model.EgDemandDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hoarding")
public class UpdateLegacyAdvertisementController extends HoardingControllerSupport {

    private static final String STR_HOARDING_UPDATE_LEGACY = "hoarding-updateLegacy";
    private static final String STR_COLLECT_ADVTAX_ERROR = "collectAdvtax-error";
    private static final String STR_COLL_UPDATE_NOT_ALLOWED = "msg.collection.updateRecordNotAllowed";
    private static final String STR_MESSAGE = "message";
    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @ModelAttribute("advertisementPermitDetail")
    public AdvertisementPermitDetail advertisementPermitDetail(@PathVariable final String id) {
        return advertisementPermitDetailService.findBy(Long.valueOf(id));

    }

    @RequestMapping(value = "/legacyUpdation/{id}", method = GET)
    public String updateHoarding(@PathVariable final String id, final Model model) {
        final AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService.findBy(Long.valueOf(id));
        final Advertisement advertisement = advertisementPermitDetail.getAdvertisement();
        if (advertisement == null) {
            model.addAttribute(STR_MESSAGE, STR_COLL_UPDATE_NOT_ALLOWED);
            return STR_COLLECT_ADVTAX_ERROR;
        }
        Boolean taxAlreadyCollectedForDemandInAnyYear = checkTaxAlreadyCollectedForAdvertisement(advertisement);

        if (!advertisement.getStatus().equals(AdvertisementStatus.ACTIVE)) {
            model.addAttribute(STR_MESSAGE, STR_COLL_UPDATE_NOT_ALLOWED);
            return STR_COLLECT_ADVTAX_ERROR;
        }
        if (taxAlreadyCollectedForDemandInAnyYear) {
            model.addAttribute(STR_MESSAGE, "msg.collection.taxAlreadyCollected");
            return STR_COLLECT_ADVTAX_ERROR;
        }

        if (advertisement.getDemandId() != null)
            for (EgDemandDetails egDemandDetail : advertisement.getDemandId().getEgDemandDetails())
                if (egDemandDetail.getAmount() != null && egDemandDetail.getAmtCollected() != null
                        && egDemandDetail.getAmtCollected().compareTo(BigDecimal.ZERO) > 0) {
                    advertisement.setTaxPaidForCurrentYear(true);
                    break;
                }
        model.addAttribute("advertisementPermitDetail", advertisement.getActiveAdvertisementPermit());
        model.addAttribute("advertisementDocuments", advertisement.getDocuments());
        return STR_HOARDING_UPDATE_LEGACY;
    }

    @RequestMapping(value = "/legacyUpdation/{id}", method = POST)
    public String updateHoarding(@Valid @ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail,
            final BindingResult resultBinder, final RedirectAttributes redirAttrib, final HttpServletRequest request,
            final Model model) {

        validateHoardingDocsOnUpdate(advertisementPermitDetail, resultBinder, redirAttrib);
        validateAdvertisementDetails(advertisementPermitDetail, resultBinder);

        if (resultBinder.hasErrors())
            return STR_HOARDING_UPDATE_LEGACY;
        try {
            updateHoardingDocuments(advertisementPermitDetail);
            advertisementPermitDetailService.updateAdvertisementPermitDetailForLegacy(advertisementPermitDetail);
            String message = messageSource.getMessage("hoarding.update.success",
                    new String[] { advertisementPermitDetail.getApplicationNumber() }, null);
            redirAttrib.addFlashAttribute(STR_MESSAGE, message);
            return "redirect:/hoarding/success/".concat(advertisementPermitDetail.getId().toString());
        } catch (final HoardingValidationError e) {
            resultBinder.rejectValue(e.fieldName(), e.errorCode());
            return STR_HOARDING_UPDATE_LEGACY;
        }
    }
}
