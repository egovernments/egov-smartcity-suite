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
package org.egov.ptis.web.controller.reports;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.DailyCollectionReportResult;
import org.egov.ptis.domain.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = "/report/dailyCollection")
public class DailyCollectionReportController {

    private static final String DAILY_COLLECTION_FORM = "dailyCollection-form";

    @Autowired
    private ReportService reportService;

    @Autowired
    private EgwStatusHibernateDAO egwStatushibernateDAO;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    
    @Autowired
    private BoundaryService boundaryService;

    @ModelAttribute
    public void getReportModel(final Model model) {
        final DailyCollectionReportResult dailyCollectionReportResut = new DailyCollectionReportResult();
        model.addAttribute("dailyCollectionReportResut", dailyCollectionReportResut);
    }

    @ModelAttribute("operators")
    public Set<User> loadCollectionOperators() {
        return reportService.getCollectionOperators();
    }

    @ModelAttribute("status")
    public List<EgwStatus> loadStatus() {
        return egwStatushibernateDAO.getStatusByModule("ReceiptHeader");
    }
    
    @ModelAttribute("wards")
    public List<Boundary> wardBoundaries() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(PropertyTaxConstants.WARD,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String seachForm(final Model model) {
        model.addAttribute("currDate", new Date());
        model.addAttribute("collectionMode",Source.values());
        return DAILY_COLLECTION_FORM;
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void springPaginationDataTablesUpdate(@RequestParam final Date fromDate,
            @RequestParam final Date toDate, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ParseException {
        String collectionMode = "";
        String collectionOperator = "";
        String status = "";
        String ward = "";
        if (null != request.getParameter("collectionMode")) {
            collectionMode = request.getParameter("collectionMode");
        }
        if (null != request.getParameter("collectionOperator")) {
            collectionOperator = request.getParameter("collectionOperator");
        }
        if (null != request.getParameter("status")) {
            status = request.getParameter("status");
        }
        
        if (null != request.getParameter("ward")) {
            ward = request.getParameter("ward");
        }
        final List<DailyCollectionReportResult> collectionDetailsList = reportService.getCollectionDetails(fromDate,
                toDate, collectionMode, collectionOperator, status, ward);
        final String result = new StringBuilder("{ \"data\":").append(toJSON(collectionDetailsList)).append("}")
                .toString();
        IOUtils.write(result, response.getWriter());
    }

    private Object toJSON(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(DailyCollectionReportResult.class,
                new DailyCollectionReportAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }

}
