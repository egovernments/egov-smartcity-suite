/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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

package org.egov.stms.web.controller.reports;

import static org.egov.stms.utils.constants.SewerageTaxConstants.BOUNDARYTYPE_WARD;
import static org.egov.stms.utils.constants.SewerageTaxConstants.HIERARCHYTYPE_REVENUE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.stms.entity.es.SewerageIndex;
import org.egov.stms.reports.entity.SewerageBaseRegisterResult;
import org.egov.stms.service.es.SewerageIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/reports")
public class SewerageBaseRegisterReportController {

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private CityService cityService;

    @Autowired
    private SewerageIndexService sewerageIndexService;

    @ModelAttribute
    public void getPropertyModel(final Model model) {
        model.addAttribute("sewerageBaseRegisterResult", new SewerageBaseRegisterResult());
    }

    @RequestMapping(value = "/baseregistersearch", method = RequestMethod.GET)
    public String newSearchForm(final Model model) {
        model.addAttribute("wards",
                boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(BOUNDARYTYPE_WARD, HIERARCHYTYPE_REVENUE));
        return "baseRegisterSearch-form";
    }

    @RequestMapping(value = "/baseregisterresult", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<SewerageBaseRegisterResult> springPaginationDataTableUpdate(
            @ModelAttribute final SewerageBaseRegisterResult sewerageBaseRegisterResult, final Model model,
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        List<SewerageIndex> resultList;
        final List<String> wardNames = new ArrayList<>();
        final List<SewerageBaseRegisterResult> baseRegisterResultList = new ArrayList<>();
        if (null != sewerageBaseRegisterResult.getWards() && !sewerageBaseRegisterResult.getWards().isEmpty())
            for (final Boundary boundary : sewerageBaseRegisterResult.getWards())
                if (boundary != null) {
                    sewerageBaseRegisterResult.setRevenueWard(boundary.getLocalName());
                    model.addAttribute("wardId", boundary.getId());
                    if (boundary.getLocalName() != null)
                        wardNames.add(boundary.getLocalName());
                }
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        if (cityWebsite != null) {
            resultList = sewerageIndexService.wardwiseBaseRegisterQueryFilter(cityWebsite.getName(), wardNames);

            for (final SewerageIndex sewerageIndex : resultList) {
                final SewerageBaseRegisterResult searchResult = new SewerageBaseRegisterResult();
                searchResult.setShscNumber(sewerageIndex.getShscNumber());
                searchResult.setRevenueWard(sewerageIndex.getWard());
                searchResult.setAssementNo(sewerageIndex.getPropertyIdentifier());
                searchResult.setOwnerName(sewerageIndex.getConsumerName());
                searchResult.setDoorNo(sewerageIndex.getDoorNo());
                searchResult.setPropertyType(sewerageIndex.getPropertyType());
                searchResult.setResidentialClosets(sewerageIndex.getNoOfClosets_residential());
                searchResult.setNonResidentialClosets(sewerageIndex.getNoOfClosets_nonResidential());
                searchResult.setPeriod(
                        sewerageIndex.getPeriod() != null ? sewerageIndex.getPeriod()
                                : org.apache.commons.lang.StringUtils.EMPTY);
                searchResult.setArrears(sewerageIndex.getArrearAmount());
                searchResult.setCurrentDemand(
                        sewerageIndex.getDemandAmount());
                searchResult.setAdvanceAmount(
                        sewerageIndex.getExtraAdvanceAmount());
                searchResult.setArrearsCollected(sewerageIndex.getCollectedArrearAmount());
                searchResult.setCurrentTaxCollected(sewerageIndex.getCollectedDemandAmount());
                searchResult
                        .setTotalTaxCollected(
                                sewerageIndex.getCollectedArrearAmount().add(sewerageIndex.getCollectedDemandAmount()));
                searchResult.setTotalDemand(sewerageIndex.getTotalAmount());
                baseRegisterResultList.add(searchResult);
            }
        }
        return baseRegisterResultList;
    }
}
