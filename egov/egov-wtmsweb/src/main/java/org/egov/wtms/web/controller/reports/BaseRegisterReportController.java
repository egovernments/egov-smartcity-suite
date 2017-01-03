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
package org.egov.wtms.web.controller.reports;

import static org.egov.infra.utils.JsonUtils.toJSON;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.wtms.application.entity.BaseRegisterResult;
import org.egov.wtms.application.service.BaseRegisterReportService;
import org.egov.wtms.reports.entity.BaseRegisterResultAdaptor;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/report/baseRegister")
public class BaseRegisterReportController {

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private BaseRegisterReportService baseRegisterReportService;

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private ModuleService moduleService;

    @ModelAttribute("wards")
    public List<Boundary> wardBoundaries() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WaterTaxConstants.REVENUE_WARD,
                WaterTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute
    public void getPropertyModel(final Model model) {
        final BaseRegisterResult baseRegisterResult = new BaseRegisterResult();
        model.addAttribute("baseRegisterResult", baseRegisterResult);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String searchForm(final Model model) {
        model.addAttribute("currDate", new Date());
        return "baseRegister-form";
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void springPaginationDataTableUpdate(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException, ParseException {
        String ward = "";
        if (null != request.getParameter("ward"))
            ward = request.getParameter("ward");
        List<BaseRegisterResult> baseRegisterResultList = new ArrayList<BaseRegisterResult>();
        final SQLQuery query = baseRegisterReportService.getBaseRegisterReportDetails(ward);
        baseRegisterResultList = query.list();
        final Module nonMeteredModule = moduleService
                .getModuleByName(WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE);
        final Module meteredModule = moduleService.getModuleByName(WaterTaxConstants.EGMODULE_NAME);
        final Installment currInstallmentForNonMetered = installmentDao
                .getInsatllmentByModuleForGivenDate(nonMeteredModule, new Date());
        final Installment currInstallmentForMetered = installmentDao
                .getInsatllmentByModuleForGivenDateAndInstallmentType(meteredModule, new Date(),
                        WaterTaxConstants.MONTHLY);
        for (final BaseRegisterResult br : baseRegisterResultList)
            if (br.getConnectionType().equals(WaterTaxConstants.NON_METERED_CODE))
                br.setPeriod(currInstallmentForNonMetered.getDescription());
            else
                br.setPeriod(currInstallmentForMetered.getDescription());
        String result = "";
        result = new StringBuilder("{ \"data\":")
                .append(toJSON(baseRegisterResultList, BaseRegisterResult.class, BaseRegisterResultAdaptor.class))
                .append("}").toString();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(result, response.getWriter());
    }
}
