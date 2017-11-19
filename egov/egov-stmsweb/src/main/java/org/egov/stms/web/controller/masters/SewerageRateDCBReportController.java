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
package org.egov.stms.web.controller.masters;

import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.config.core.LocalizationSettings;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.masters.pojo.DCBReportWardwiseResult;
import org.egov.stms.service.es.SewerageIndexService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageDCBReporService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.egov.stms.utils.constants.SewerageTaxConstants.ALL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.BOUNDARYTYPE_WARD;
import static org.egov.stms.utils.constants.SewerageTaxConstants.HIERARCHYTYPE_REVENUE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MIXED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.NONRESIDENTIAL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.RESIDENTIAL;

@Controller
@RequestMapping(value = "/reports")
public class SewerageRateDCBReportController {

    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;

    @Autowired
    private SewerageDCBReporService sewerageDCBReporService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;

    @Autowired
    private CityService cityService;

    @Autowired
    private SewerageIndexService sewerageIndexService;

    @ModelAttribute
    public DCBReportWardwiseResult dCBReportWardWise() {
        return new DCBReportWardwiseResult();
    }

    @RequestMapping(value = "/sewerageRateReportView/{consumernumber}/{assessmentnumber}", method = RequestMethod.GET)
    public ModelAndView getSewerageRateReport(@PathVariable final String consumernumber,
                                              @PathVariable final String assessmentnumber, final Model model,
                                              final HttpServletRequest request) {
        SewerageApplicationDetails sewerageApplicationDetails = null;
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        final String currentDate = formatter.format(new Date());
        if (consumernumber != null) {
            sewerageApplicationDetails = sewerageApplicationDetailsService.findByApplicationNumber(consumernumber);
            if (sewerageApplicationDetails != null) {
                final AssessmentDetails propertyOwnerDetails = sewerageThirdPartyServices.getPropertyDetails(
                        assessmentnumber, request);
                model.addAttribute("applicationNumber", sewerageApplicationDetails.getApplicationNumber());
                model.addAttribute("propertyOwnerDetails", propertyOwnerDetails);
                model.addAttribute("assessmentnumber", assessmentnumber);
                model.addAttribute("currentDate", currentDate);
                model.addAttribute("dcbResultList",
                        sewerageDCBReporService.getSewerageRateDCBReport(sewerageApplicationDetails));
            }
        }
        return new ModelAndView("report-sewerageRate-view", "sewerageApplicationDetails", sewerageApplicationDetails);
    }

    @RequestMapping(value = "/dcb-report-wardwise", method = RequestMethod.GET)
    public String wardWiseSearch(@ModelAttribute final DCBReportWardwiseResult dcbReportWardwiseResult, final Model model) {
        model.addAttribute("dcbReportWardwiseResult", dcbReportWardwiseResult);
        final List<String> propertytype = new ArrayList<>();
        for (final PropertyType s : PropertyType.values())
            propertytype.add(s.toString());
        final List<String> boundaryList = new ArrayList<>();
        List<Boundary> bList;
        boundaryList.add(ALL);
        bList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Ward", "REVENUE");
        for (final Boundary b : bList)
            boundaryList.add(b.toString());
        model.addAttribute("propertyType", propertytype);
        model.addAttribute("wards", boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Ward", "REVENUE"));
        return "sewerage-dcbwardwise";
    }

    @RequestMapping(value = "/dcbReportWardwiseList", method = RequestMethod.GET)
    @ResponseBody
    public void searchApplication(@ModelAttribute final DCBReportWardwiseResult searchRequest, final Model model,
                                  final HttpServletResponse response) throws IOException {
        final List<Boundary> wardList = new ArrayList<>();
        List<DCBReportWardwiseResult> wardwiseResultList;
        final List<Boundary> wards = new ArrayList<>();
        final List<String> propertyTypeList = new ArrayList<>();
        String[] wardIds;
        final List<String> wardNames = new ArrayList<>();
        Map<String, List<SewerageApplicationDetails>> dcbMap;
        if (searchRequest.getMode() != null) {
            wardIds = searchRequest.getMode().split("~");
            for (final String idValue : wardIds) {
                final Boundary ward = boundaryService.getBoundaryById(Long.parseLong(idValue));
                if (ward != null)
                    wardList.add(ward);
            }
        }
        if (wardList != null && !wardList.isEmpty())
            searchRequest.setWards(wardList);
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        searchRequest.setUlbName(cityWebsite.getName());
        if (null != searchRequest.getWards() && !searchRequest.getWards().isEmpty() && searchRequest.getWards().get(0) != null)
            wards.addAll(searchRequest.getWards());
        else
            wards.addAll(
                    boundaryService.getBoundariesByBndryTypeNameAndHierarchyTypeName(BOUNDARYTYPE_WARD, HIERARCHYTYPE_REVENUE));

        for (final Boundary boundary : wards)
            if (boundary != null) {
                searchRequest.setRevenueWard(boundary.getLocalName());
                model.addAttribute("wardId", boundary.getId());
                if (boundary.getLocalName() != null)
                    wardNames.add(boundary.getLocalName());
            }

        if (searchRequest.getPropertyType().equals(ALL)) {
            propertyTypeList.add(RESIDENTIAL);
            propertyTypeList.add(NONRESIDENTIAL);
            propertyTypeList.add(MIXED);
        } else
            propertyTypeList.add(searchRequest.getPropertyType());

        dcbMap = sewerageIndexService.wardWiseBoolQueryFilter(searchRequest.getUlbName(), wardNames, propertyTypeList);
        wardwiseResultList = sewerageDCBReporService.getSewerageRateDCBWardwiseReport(dcbMap, searchRequest.getPropertyType());

        IOUtils.write("{ \"data\":" + new GsonBuilder().setDateFormat(LocalizationSettings.datePattern()).create()
                .toJson(wardwiseResultList)
                + "}", response.getWriter());

    }

    @RequestMapping(value = "/dcbViewWardConnections/{id}", method = RequestMethod.GET)
    public String getConnectionDCBReport(@ModelAttribute final DCBReportWardwiseResult searchRequest, final Model model,
                                         @PathVariable final String id, final HttpServletRequest request) {
        String propType = null;
        String ulbName;
        Long wardId;
        String revenueWard = null;
        final List<String> propertyTypeList = new ArrayList<>();
        final String[] wardDtl = id.split("~");
        final List<String> wardList = new ArrayList<>();
        Map<String, List<SewerageApplicationDetails>> wardConnectionMap;
        for (final String value : wardDtl)
            wardList.add(value);
        wardId = Long.parseLong(wardList.get(0));
        if (wardId != null) {
            final Boundary boundary = boundaryService.getBoundaryById(wardId);
            revenueWard = boundary.getLocalName();
        }
        if (wardList.size() == 2 && StringUtils.isNotBlank(wardList.get(1)))
            propType = wardList.get(1);

        if (ALL.equals(propType)) {
            propertyTypeList.add(NONRESIDENTIAL);
            propertyTypeList.add(MIXED);
            propertyTypeList.add(RESIDENTIAL);
        } else
            propertyTypeList.add(propType);
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        ulbName = cityWebsite.getName();

        wardConnectionMap = sewerageIndexService.wardWiseConnectionQueryFilter(propertyTypeList, revenueWard, ulbName);
        model.addAttribute("currentDate", new Date());
        model.addAttribute("revenueWard", revenueWard);
        model.addAttribute("dcbResultList",
                sewerageDCBReporService.getSewerageDCBWardConnections(wardConnectionMap, propType));
        return "sewerage-dcbWardConnections";
    }
}