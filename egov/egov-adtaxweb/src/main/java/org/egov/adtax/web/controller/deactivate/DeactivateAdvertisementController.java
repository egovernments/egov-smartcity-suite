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

package org.egov.adtax.web.controller.deactivate;

import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.Agency;
import org.egov.adtax.entity.SubCategory;
import org.egov.adtax.entity.enums.AdvertisementStatus;
import org.egov.adtax.search.contract.HoardingSearch;
import org.egov.adtax.service.AdvertisementPermitDetailService;
import org.egov.adtax.service.AgencyService;
import org.egov.adtax.service.SubCategoryService;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.adtax.web.controller.GenericController;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.config.core.LocalizationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/deactivate")
public class DeactivateAdvertisementController extends GenericController {

    private final AgencyService agencyService;

    @Autowired
    private AdvertisementPermitDetailService advertisementPermitDetailService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    public DeactivateAdvertisementController(final AgencyService agencyService) {
        this.agencyService = agencyService;
    }

    @ModelAttribute
    public Agency agency() {
        return new Agency();
    }

    @ModelAttribute(value = "agencies")
    public List<Agency> getAgencies() {
        return agencyService.findAll();
    }

    @RequestMapping(value = "/result", method = GET)
    public String changeStatus(
            @ModelAttribute("advertisementPermitDetailStatus") AdvertisementPermitDetail advertisementPermitDetailStatus,
            @PathVariable final String applicationNumber, BindingResult result, Model model) {
        model.addAttribute("advertisementPermitDetailStatus",
                advertisementPermitDetailService.findByApplicationNumber(applicationNumber));
        return "statusChange-result";
    }

    @RequestMapping(value = "/search", method = GET)
    public String statusChange(Model model) {
        model.addAttribute("advertisementPermitDetailRecord", new AdvertisementPermitDetail());
        return "statuschange-search";
    }

    @RequestMapping(value = "/activerecord-list-search", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public void searchResult(@ModelAttribute final AdvertisementPermitDetail advertisementPermitDetailRecord,
                             final HttpServletRequest request,
                             final HttpServletResponse response) throws IOException {
        final String searchType = request.getParameter("searchType");
        IOUtils.write("{ \"data\":" + new GsonBuilder().setDateFormat(LocalizationSettings.datePattern()).create()
                .toJson(advertisementPermitDetailService.getActiveAdvertisementSearchResult(advertisementPermitDetailRecord,
                        searchType))
                + "}", response.getWriter());
    }

    public String commonSearchResult(final AdvertisementPermitDetail advertisementPermitDetailRecord, final String searchType) {
        final List<HoardingSearch> searchResult = advertisementPermitDetailService
                .getAdvertisementSearchResult(advertisementPermitDetailRecord, searchType);
        return new StringBuilder("{ \"data\":").append(searchResult).append("}").toString();
    }

    @RequestMapping(value = "/subcategories-by-categoryselect", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<SubCategory> hoardingSubcategories(@RequestParam final Long categoryId) {
        return subCategoryService.getAllActiveSubCategoryByCategoryId(categoryId);
    }

    @RequestMapping(value = "/result/{id}", method = GET)
    public String viewHoardingByApplicationNumber(@PathVariable final Long id, final Model model) {
        AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService.findById(id);

        if (advertisementPermitDetail != null && advertisementPermitDetail.getAdvertisement() != null
                && advertisementPermitDetail.getAdvertisement().getStatus() != null
                && advertisementPermitDetail.getAdvertisement().getStatus().equals(AdvertisementStatus.WORKFLOW_IN_PROGRESS)) {
            model.addAttribute("message", "msg.deactivate.alreadyInWorkFlow");
            return "deactive-error";

        }
        if (advertisementPermitDetail != null && advertisementPermitDetail.getAdvertisement() != null
                && advertisementPermitDetail.getAdvertisement().getStatus() != null
                && advertisementPermitDetail.getAdvertisement().getStatus().equals(AdvertisementStatus.ACTIVE) &&
                advertisementPermitDetail.getStatus() != null && advertisementPermitDetail.getStatus().getCode()
                .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_APPROVED)) {
            model.addAttribute("message", "msg.deactivate.paymentPending");
            return "deactive-error";
        }
        Set<EgDemandDetails> demandDetails = new HashSet<EgDemandDetails>();
        demandDetails = advertisementPermitDetail.getAdvertisement().getDemandId().getEgDemandDetails();
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalCollectedAmount = BigDecimal.ZERO;
        BigDecimal pendingTax = BigDecimal.ZERO;

        for (EgDemandDetails demandObject : demandDetails) {
            totalAmount = totalAmount.add(demandObject.getAmount());
            totalCollectedAmount = totalCollectedAmount.add(demandObject.getAmtCollected());

        }
        pendingTax = totalAmount.subtract(totalCollectedAmount);
        advertisementPermitDetail.getAdvertisement().setPendingTax(pendingTax);

        model.addAttribute("advertisementPermitDetailStatus", advertisementPermitDetail);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        model.addAttribute("applicationDate", sdf.format(advertisementPermitDetail.getApplicationDate()));
        return "statusChange-deactivate";
    }

    @RequestMapping(value = "/deactive/{id}", method = RequestMethod.POST)
    public String deactivate(@ModelAttribute AdvertisementPermitDetail advertisementPermitDetailStatus, final Model model,
                             @PathVariable final Long id) {

        AdvertisementPermitDetail existingRateObject = advertisementPermitDetailService
                .findById(id);

        if (existingRateObject != null) {
            existingRateObject.setDeactivation_remarks(advertisementPermitDetailStatus.getDeactivation_remarks());
            existingRateObject.setDeactivation_date(advertisementPermitDetailStatus.getDeactivation_date());
            existingRateObject.getAdvertisement().setStatus(AdvertisementStatus.INACTIVE);
            advertisementPermitDetailService.updateAdvertisementPermitDetail(existingRateObject);
        }

        return "statusChange-success";
    }

    @RequestMapping(value = "agencies", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Agency> findAgencies(@RequestParam final String name) {
        return agencyService.findAllByNameLike(name);
    }
}
