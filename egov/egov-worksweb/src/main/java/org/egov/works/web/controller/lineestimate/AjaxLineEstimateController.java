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
package org.egov.works.web.controller.lineestimate;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.exception.ApplicationException;
import org.egov.services.masters.SchemeService;
import org.egov.works.web.adaptor.SubSchemeAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = "/lineestimate")
public class AjaxLineEstimateController {
    @Autowired
    private SchemeService schemeService;

    @Autowired
    private CrossHierarchyService crossHierarchyService;

    @RequestMapping(value = "/ajax-getlocation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Boundary> getChildBoundariesById(@RequestParam final Long id) {
        final List<Boundary> boundaries = crossHierarchyService.getActiveChildBoundariesByBoundaryId(id);
        return boundaries;
    }

    @RequestMapping(value = "/getsubschemesbyschemeid/{schemeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getAllSubSchemesBySchemeId(final Model model, @PathVariable final String schemeId)
            throws JsonGenerationException, JsonMappingException, IOException, NumberFormatException, ApplicationException {
        final Scheme scheme = schemeService.findById(Integer.parseInt(schemeId), false);
        final Set<SubScheme> subSchemes = scheme.getSubSchemes();
        final String jsonResponse = toJSON(subSchemes);
        return jsonResponse;
    }
    

    public String toJSON(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(SubScheme.class, new SubSchemeAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
}
