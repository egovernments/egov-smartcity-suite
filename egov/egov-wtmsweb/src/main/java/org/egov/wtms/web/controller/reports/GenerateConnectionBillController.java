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

package org.egov.wtms.web.controller.reports;

import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.demand.model.EgBill;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.wtms.application.service.GenerateConnectionBill;
import org.egov.wtms.application.service.GenerateConnectionBillService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.ApplicationType;
import org.egov.wtms.masters.entity.PropertyType;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.masters.service.PropertyTypeService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/report/generateBill/search")
public class GenerateConnectionBillController {

    @Autowired
    private PropertyTypeService propertyTypeService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private GenerateConnectionBillService generateConnectionBillService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @RequestMapping(method = GET)
    public String search(final Model model) {

        return "generateBill-Report";
    }

    @ModelAttribute
    public GenerateConnectionBill reportModel() {
        return new GenerateConnectionBill();
    }

    @ModelAttribute("zones")
    public List<Boundary> zones() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ZONE, REVENUE_HIERARCHY_TYPE);
    }

    public @ModelAttribute("revenueWards") List<Boundary> revenueWardList() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WaterTaxConstants.REVENUE_WARD,
                REVENUE_HIERARCHY_TYPE);
    }

    public @ModelAttribute("connectionTypes") Map<String, String> connectionTypes() {
        return waterConnectionDetailsService.getNonMeteredConnectionTypesMap();
    }

    public @ModelAttribute("propertyTypes") List<PropertyType> propertyTypes() {
        return propertyTypeService.getAllActivePropertyTypes();
    }

    public @ModelAttribute("applicationTypes") List<ApplicationType> applicationTypes() {
        return applicationTypeService.findAll();
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void searchResult(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ParseException {
        String zone = "";
        String ward = "";
        String propertyType = "";
        String applicationType = "";
        String connectionType = "";
        String consumerCode = "";
        String houseNumber = "";
        String assessmentNumber = "";

        if (null != request.getParameter("zone"))
            zone = request.getParameter("zone");
        if (null != request.getParameter("revenueWard"))
            ward = request.getParameter("revenueWard");
        if (null != request.getParameter("propertyType"))
            propertyType = request.getParameter("propertyType");
        if (null != request.getParameter("applicationType"))
            applicationType = request.getParameter("applicationType");
        if (null != request.getParameter("connectionType"))
            connectionType = request.getParameter("connectionType");
        if (null != request.getParameter("consumerCode"))
            consumerCode = request.getParameter("consumerCode");
        if (null != request.getParameter("houseNumber"))
            houseNumber = request.getParameter("houseNumber");
        if (null != request.getParameter("assessmentNumber"))
            assessmentNumber = request.getParameter("assessmentNumber");

        List<GenerateConnectionBill> generateConnectionBillList = new ArrayList<GenerateConnectionBill>();
        final SQLQuery query = generateConnectionBillService.getBillReportDetails(zone, ward, propertyType,
                applicationType, connectionType, consumerCode, houseNumber, assessmentNumber);
        generateConnectionBillList = query.list();
        String result = null;
        for (final GenerateConnectionBill connectionbill : generateConnectionBillList) {
            final EgBill egbill = generateConnectionBillService.getBIll(connectionbill.getHscNo());
            if (egbill != null) {
                connectionbill.setBillNo(egbill.getBillNo());
                connectionbill.setBillDate(egbill.getIssueDate().toString());
            }
        }
        result = new StringBuilder("{ \"data\":").append(toJSON(generateConnectionBillList)).append("}").toString();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(result, response.getWriter());
    }

    private Object toJSON(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(GenerateConnectionBill.class,
                new GenerateConnectionBillAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }

}
