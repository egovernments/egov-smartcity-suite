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

import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.SubCategory;
import org.egov.adtax.search.contract.HoardingSearch;
import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.service.AdvertisementPermitDetailService;
import org.egov.adtax.service.SubCategoryService;
import org.egov.adtax.web.controller.GenericController;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.config.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/hoarding")
public class SearchHoardingController extends GenericController {
    @Autowired
    private FinancialYearDAO financialYearDAO;
    @Autowired
    private AdvertisementPermitDetailService advertisementPermitDetailService;
    @Autowired
    private AdvertisementDemandService advertisementDemandService;
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private SubCategoryService subCategoryService;

    @ModelAttribute
    public AdvertisementPermitDetail advertisementPermitDetail() {
        return new AdvertisementPermitDetail();
    }

    @RequestMapping(value = "/subcategories-by-category", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody List<SubCategory> hoardingSubcategories(@RequestParam final Long categoryId) {
        return subCategoryService.getAllActiveSubCategoryByCategoryId(categoryId);
    }

    @RequestMapping(value = "/search", method = GET)
    public String search() {
        return "hoarding-search";
    }

    @RequestMapping(value = "/search-adtax", method = GET)
    public String searchAdtaxForm() {
        return "advertisement-search";
    }

    @RequestMapping(value = "/search-adtax-result", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody void searchAdtaxResult(@ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail,
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        IOUtils.write("{ \"data\":" + new GsonBuilder().setDateFormat(applicationProperties.defaultDatePattern()).create()
                .toJson(advertisementPermitDetailService.getAdvertisementSearchResult(advertisementPermitDetail, "Advertisement"))
                + "}", response.getWriter());
    }

    @RequestMapping(value = "/search-list", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody void searchResult(@ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail,
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        final String searchType = request.getParameter("searchType");
        IOUtils.write("{ \"data\":" + new GsonBuilder().setDateFormat(applicationProperties.defaultDatePattern()).create()
                .toJson(advertisementPermitDetailService.getAdvertisementSearchResult(advertisementPermitDetail, searchType))
                + "}", response.getWriter());
    }

    public String commonSearchResult(final AdvertisementPermitDetail advertisementPermitDetail, final String searchType) {
        final List<HoardingSearch> searchResult = advertisementPermitDetailService
                .getAdvertisementSearchResult(advertisementPermitDetail, searchType);
        return new StringBuilder("{ \"data\":").append(searchResult).append("}").toString();
    }

    @RequestMapping(value = "search-for-update", method = GET)
    public String searchHoardingForm(@ModelAttribute final HoardingSearch hoardingSearch) {
        return "hoarding-search-for-update";
    }

    @RequestMapping(value = "search-for-update", method = POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchHoarding(@ModelAttribute final HoardingSearch hoardingSearch) {
        return "{ \"data\":" + new GsonBuilder().setDateFormat(applicationProperties.defaultDatePattern()).create()
                .toJson(advertisementPermitDetailService.getAdvertisementSearchResult(hoardingSearch, "searchLegacyRecord"))
                + "}";
    }

    @RequestMapping(value = "view/{id}", method = GET)
    public String viewHoarding(@PathVariable final String id, final Model model, @ModelAttribute AdvertisementPermitDetail advertisementPermitDetail) {
        advertisementPermitDetail = advertisementPermitDetailService.findBy(Long.valueOf(id));
        model.addAttribute("advertisementPermitDetail", advertisementPermitDetail);
        model.addAttribute("arrearTax", advertisementDemandService.getPendingArrearsTax(advertisementPermitDetail));
        model.addAttribute("previousFinancialYear", financialYearDAO.getPreviousFinancialYearByDate(new Date()));
        return "hoarding-view";
    }

    @RequestMapping(value = "/renewal-search", method = GET)
    public String renewalSearchForm() {
        return "renewal-search";
    }

    @RequestMapping(value = "/renewal-search-result", method = GET)
    public @ResponseBody void renewSearchResult(@ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail,
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        IOUtils.write("{ \"data\":" + new GsonBuilder().setDateFormat(applicationProperties.defaultDatePattern()).create()
                .toJson(advertisementPermitDetailService.getRenewalAdvertisementSearchResult(advertisementPermitDetail,
                        "Advertisement"))
                + "}", response.getWriter());
    }
}
