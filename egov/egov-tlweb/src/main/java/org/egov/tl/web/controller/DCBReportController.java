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

package org.egov.tl.web.controller;

import org.apache.commons.io.IOUtils;
import org.egov.tl.service.DCBReportService;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.egov.infra.web.utils.WebUtils.toJSON;

@Controller
@RequestMapping(value = { "/tlreports", "/public/report" })
public class DCBReportController {

    public static final String LICENSE = "license";
    @Autowired
    private DCBReportService dCBReportService;

    @PersistenceContext
    EntityManager entityManager;

    @ModelAttribute("dCBReportResult")
    public DCBReportResult dCBReportResultModel() {
        return new DCBReportResult();
    }

    @RequestMapping(value = "/dCBReport/licenseNumberWise", method = RequestMethod.GET)
    public String licenseNumberWisesearch(final Model model) {
        model.addAttribute("mode", LICENSE);
        model.addAttribute("reportType", LICENSE);
        return "dCBReport-search";
    }

    @RequestMapping(value = "/dCBReportList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void search(final HttpServletRequest request, final HttpServletResponse response,
            final Model model) throws IOException {
        List<DCBReportResult> resultList = new ArrayList<DCBReportResult>();
        String licensenumber = "", mode = "", reportType = "";
        if (request.getParameter("mode") != null && !"".equals(request.getParameter("mode")))
            mode = request.getParameter("mode");
        if (request.getParameter("reportType") != null && !"".equals(request.getParameter("reportType")))
            reportType = request.getParameter("reportType");
        if (request.getParameter("licensenumber") != null && !"".equals(request.getParameter("licensenumber")))
            licensenumber = request.getParameter("licensenumber");
        final SQLQuery query = dCBReportService.prepareQuery(licensenumber, mode, reportType);
        query.setResultTransformer(new AliasToBeanResultTransformer(DCBReportResult.class));
        resultList = query.list();
        String result = null;
        result = new StringBuilder("{ \"data\":").append(toJSON(resultList, DCBReportResult.class, DCBReportHelperAdaptor.class)).append("}").toString();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(result, response.getWriter());
    }
}
