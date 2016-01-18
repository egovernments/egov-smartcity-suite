/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.adtax.web.controller.hoarding;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.SubCategory;
import org.egov.adtax.repository.AdvertisementPermitDetailRepository;
import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.service.AdvertisementPermitDetailService;
import org.egov.adtax.service.AdvertisementService;
import org.egov.adtax.service.SubCategoryService;
import org.egov.adtax.web.controller.GenericController;
import org.egov.demand.model.EgDemand;
import org.egov.infra.config.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/hoarding")
public class generateDemandHoardingController extends GenericController {

    @Autowired
    private AdvertisementPermitDetailService advertisementPermitDetailService;
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private AdvertisementDemandService advertisementDemandService;

    @Autowired
    private SubCategoryService subCategoryService;
    private @Autowired AdvertisementService advertisementService;
    private @Autowired AdvertisementPermitDetailRepository advertisementPermitDetailRepository;

    @ModelAttribute
    public AdvertisementPermitDetail advertisementPermitDetail() {
        return new AdvertisementPermitDetail();
    }

    @RequestMapping(value = "/11subcategories-by-category", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody List<SubCategory> hoardingSubcategories(@RequestParam final Long categoryId) {
        return subCategoryService.getAllActiveSubCategoryByCategoryId(categoryId);
    }

    @RequestMapping(value = "/generate-search", method = GET)
    public String search() {
        return "hoarding-generate";
    }

    @RequestMapping(value = "/genareteDemand-list", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody void searchResult(@ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        request.getParameter("searchType");
        IOUtils.write(
                "{ \"data\":"
                        + new GsonBuilder()
                                .setDateFormat(applicationProperties.defaultDatePattern())
                                .create()
                                .toJson(advertisementPermitDetailService
                                        .getAdvertisementSearchResult(advertisementPermitDetail,null)) + "}",
                response.getWriter());
    }

    @RequestMapping(value = "/generateDemand/{hoardingId}", method = GET)
    public String payTaxOnline(@ModelAttribute AdvertisementPermitDetail advertisementPermitDetail,
            final RedirectAttributes redirectAttributes, @PathVariable final Long hoardingId, final Model model) {
        advertisementPermitDetail = advertisementPermitDetailService.findBy(hoardingId);
        return generateDemandAndUpdateAdTax(advertisementPermitDetail, model,redirectAttributes);
    }

    public String generateDemandAndUpdateAdTax(final AdvertisementPermitDetail advertisementPermitDetail,
            final Model model,RedirectAttributes redirectAttributes) {
        final EgDemand updateddemand = advertisementDemandService
                .generateNextYearDemandForAdvertisement(advertisementPermitDetail);
        advertisementPermitDetail.getAdvertisement().setDemandId(updateddemand);
        advertisementPermitDetailRepository.save(advertisementPermitDetail);
        redirectAttributes.addFlashAttribute("message","Demand Updated Successfully");
        return "generateDemand-success";
    }

}
