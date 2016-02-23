/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.adtax.web.controller.deactivate;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.SubCategory;
import org.egov.adtax.entity.enums.AdvertisementStatus;
import org.egov.adtax.search.contract.HoardingSearch;
import org.egov.adtax.service.AdvertisementPermitDetailService;
import org.egov.adtax.service.SubCategoryService;
import org.egov.adtax.web.controller.GenericController;
import org.egov.infra.config.properties.ApplicationProperties;
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

import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/deactivate")
public class DeactivateAdvertisementController extends GenericController {

    @Autowired
    private AdvertisementPermitDetailService advertisementPermitDetailService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private SubCategoryService subCategoryService;

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


    @RequestMapping(value = "/search-activerecord-list", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody void searchResult(@ModelAttribute final AdvertisementPermitDetail advertisementPermitDetailRecord,
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        final String searchType = request.getParameter("searchType");
        IOUtils.write("{ \"data\":" + new GsonBuilder().setDateFormat(applicationProperties.defaultDatePattern()).create()
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
    public @ResponseBody List<SubCategory> hoardingSubcategories(@RequestParam final Long categoryId) {
        return subCategoryService.getAllActiveSubCategoryByCategoryId(categoryId);
    }

    @RequestMapping(value = "/result/{id}", method = GET)
    public String viewHoardingByApplicationNumber(@PathVariable final Long id, final Model model) {
        // TODO: Calculate the amount pending -> (Demand - collection)
        AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService.findById(id);
        model.addAttribute("advertisementPermitDetailStatus", advertisementPermitDetail);
        return "statusChange-result";
    }

    @RequestMapping(value = "/deactive", method = RequestMethod.POST)
    public String deactivate(@ModelAttribute AdvertisementPermitDetail advertisementPermitDetailStatus, final Model model) {

        // TODO: CHECK WHETHER DEACTIVATION REMARK ENTERED ? Deactivation date should be less than current date.

        AdvertisementPermitDetail existingRateObject = advertisementPermitDetailService
                .findByApplicationNumber(advertisementPermitDetailStatus.getApplicationNumber());
        // advertisementServiceRecord.findStatus( advertisementPermitDetailStatus.getApplicationNumber());

        if (existingRateObject != null) {
            existingRateObject.setDeactivation_remarks(advertisementPermitDetailStatus.getDeactivation_remarks());
            existingRateObject.setDeactivation_date(advertisementPermitDetailStatus.getDeactivation_date());
            existingRateObject.getAdvertisement().setStatus(AdvertisementStatus.INACTIVE);
            advertisementPermitDetailService.updateAdvertisementPermitDetail(existingRateObject);
            // advertisementPermitDetailStatus = advertisementServiceRecord.createScheduleOfRate(existingRateObject);
        }

        return "statusChange-success";
    }
}
