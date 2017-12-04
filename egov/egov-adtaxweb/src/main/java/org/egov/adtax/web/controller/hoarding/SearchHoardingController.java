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

import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.SubCategory;
import org.egov.adtax.search.contract.HoardingSearch;
import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.service.AdvertisementPermitDetailService;
import org.egov.adtax.service.SubCategoryService;
import org.egov.adtax.web.controller.GenericController;
import org.egov.adtax.workflow.AdvertisementWorkFlowService;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.config.core.LocalizationSettings;
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
    private static final String RENEWAL_SEARCH = "renewal-search";
    private static final String HOARDING_SEARCH_FOR_UPDATE = "hoarding-search-for-update";
    private static final String HOARDING_SEARCH = "hoarding-search";
    private static final String ADVERTISEMENT_SEARCH = "advertisement-search";
    private static final String DATA = "{ \"data\":";
    @Autowired
    private FinancialYearDAO financialYearDAO;
    @Autowired
    private AdvertisementPermitDetailService advertisementPermitDetailService;
    @Autowired
    private AdvertisementDemandService advertisementDemandService;
    @Autowired
    private SubCategoryService subCategoryService;
    @Autowired
    private AdvertisementWorkFlowService advertisementWorkFlowService;

    @ModelAttribute
    public AdvertisementPermitDetail advertisementPermitDetail() {
        return new AdvertisementPermitDetail();
    }

    @RequestMapping(value = "/getsubcategories-by-category", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<SubCategory> hoardingSubcategories(@RequestParam final Long categoryId) {
        return subCategoryService.getAllActiveSubCategoryByCategoryId(categoryId);
    }

    @RequestMapping(value = "/search", method = GET)
    public String search() {
        return HOARDING_SEARCH;
    }

    @RequestMapping(value = "/adtax-search", method = GET)
    public String searchAdtaxForm() {
        return ADVERTISEMENT_SEARCH;
    }

    @RequestMapping(value = "/getsearch-adtax-result", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public void searchAdtaxResult(@ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail,
                                  final HttpServletRequest request,
                                  final HttpServletResponse response) throws IOException {
        IOUtils.write(DATA + new GsonBuilder().setDateFormat(LocalizationSettings.datePattern()).create()
                .toJson(advertisementPermitDetailService.getAdvertisementSearchResult(advertisementPermitDetail, "Advertisement"))
                + "}", response.getWriter());
    }

    @RequestMapping(value = "/hoarding-search-list", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public void searchResult(@ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail,
                             final HttpServletRequest request,
                             final HttpServletResponse response) throws IOException {
        final String searchType = request.getParameter("searchType");
        IOUtils.write(DATA + new GsonBuilder().setDateFormat(LocalizationSettings.datePattern()).create()
                .toJson(advertisementPermitDetailService.getAdvertisementSearchResult(advertisementPermitDetail, searchType))
                + "}", response.getWriter());
    }

    public String commonSearchResult(final AdvertisementPermitDetail advertisementPermitDetail, final String searchType) {
        final List<HoardingSearch> searchResult = advertisementPermitDetailService
                .getAdvertisementSearchResult(advertisementPermitDetail, searchType);
        return new StringBuilder(DATA).append(searchResult).append("}").toString();
    }

    @RequestMapping(value = "findhoarding-for-update", method = GET)
    public String searchHoardingForm(@ModelAttribute final HoardingSearch hoardingSearch) {
        return HOARDING_SEARCH_FOR_UPDATE;
    }

    @RequestMapping(value = "findhoarding-for-update", method = POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchHoarding(@ModelAttribute final HoardingSearch hoardingSearch) {
        return DATA + new GsonBuilder().setDateFormat(LocalizationSettings.datePattern()).create()
                .toJson(advertisementPermitDetailService.getAdvertisementSearchResult(hoardingSearch, "searchLegacyRecord"))
                + "}";
    }

    @RequestMapping(value = "view/{id}", method = GET)
    public String viewHoarding(@PathVariable final String id, final Model model) {
        AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService.findBy(Long.valueOf(id));
        model.addAttribute("advertisementPermitDetail", advertisementPermitDetail);
        model.addAttribute("arrearTax", advertisementDemandService.getPendingArrearsTax(advertisementPermitDetail));
        model.addAttribute("previousFinancialYear", financialYearDAO.getPreviousFinancialYearByDate(new Date()));
        model.addAttribute("applicationHistory", advertisementWorkFlowService.getHistory(advertisementPermitDetail));
        return "hoarding-view";
    }

    @RequestMapping(value = "/renewal-search", method = GET)
    public String renewalSearchForm() {
        return RENEWAL_SEARCH;
    }

    @RequestMapping(value = "/renewl-search-result", method = GET)
    @ResponseBody
    public void renewSearchResult(@ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail,
                                  final HttpServletRequest request,
                                  final HttpServletResponse response) throws IOException {
        IOUtils.write(DATA + new GsonBuilder().setDateFormat(LocalizationSettings.datePattern()).create()
                .toJson(advertisementPermitDetailService.getRenewalAdvertisementSearchResult(advertisementPermitDetail,
                        "Advertisement"))
                + "}", response.getWriter());
    }
}
