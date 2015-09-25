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

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.adtax.entity.AgencyWiseResult;
import org.egov.adtax.entity.Hoarding;
import org.egov.adtax.entity.SubCategory;
import org.egov.adtax.service.HoardingService;
import org.egov.adtax.service.SubCategoryService;
import org.egov.adtax.web.adaptor.AgencyAdaptor;
import org.egov.adtax.web.adaptor.HoardingAdaptor;
import org.egov.adtax.web.controller.GenericController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/hoarding")
public class SearchHoardingController extends GenericController {

    private final HoardingService hoardingService;

    private final SubCategoryService subCategoryService;

    @Autowired
    public SearchHoardingController(final HoardingService hoardingService, final SubCategoryService subCategoryService) {
        this.hoardingService = hoardingService;
        this.subCategoryService = subCategoryService;
    }

    @ModelAttribute
    public Hoarding hoarding() {
        return new Hoarding();
    }

    @RequestMapping(value = "/subcategories-by-category", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody List<SubCategory> hoardingSubcategories(@RequestParam final Long categoryId) {
        return subCategoryService.getAllActiveSubCategoryByCategoryId(categoryId);
    }

    @RequestMapping(value = "/search", method = GET)
    public String search() {
        return "hoarding-search";
    }

    @RequestMapping(value = "/search-list", method = GET, produces=APPLICATION_JSON_VALUE)
    public @ResponseBody void searchResult(@ModelAttribute final Hoarding hoarding, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        final String searchType = request.getParameter("searchType");
        final String hoardingJSONData = commonSearchResult(hoarding, searchType);
        IOUtils.write(hoardingJSONData, response.getWriter());
    }

    public String commonSearchResult(final Hoarding hoarding, final String searchType) {
        final List<Object[]> searchResult = hoardingService.searchBySearchType(hoarding, searchType);
        return new StringBuilder("{ \"data\":").append(toJSON(searchResult, searchType)).append("}").toString();
    }

    private String toJSON(final Object object, final String searchType) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = null;
        if ("hoarding".equalsIgnoreCase(searchType))
            gson = gsonBuilder.registerTypeAdapter(Hoarding.class, new HoardingAdaptor()).create();
        else
            gson = gsonBuilder.registerTypeAdapter(AgencyWiseResult.class, new AgencyAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }

}
