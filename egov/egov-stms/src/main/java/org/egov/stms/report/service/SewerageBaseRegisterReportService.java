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
package org.egov.stms.report.service;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.stms.entity.es.SewerageIndex;
import org.egov.stms.reports.entity.SewerageBaseRegisterResult;
import org.egov.stms.service.es.SewerageIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SewerageBaseRegisterReportService {

    @Autowired
    private CityService cityService;

    @Autowired
    private SewerageIndexService sewerageIndexService;

    @Autowired
    private BoundaryService boundaryService;

    @ReadOnly
    public Page<SewerageIndex> getBaseRegisterDetails(
            final SewerageBaseRegisterResult sewerageBaseRegisterResult, final Model model) {
        Page<SewerageIndex> searchResult = null;
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        if (cityWebsite != null)
            searchResult = sewerageIndexService.wardwiseBaseRegisterQueryFilter(cityWebsite.getName(),
                    getWardNames(sewerageBaseRegisterResult, model), sewerageBaseRegisterResult);
        return searchResult;

    }

    public List<SewerageBaseRegisterResult> getAllBaseRegisterRecords(final SewerageBaseRegisterResult sewerageBaseRegisterResult,
            final Model model) {

        List<SewerageIndex> resultList = null;
        final List<SewerageBaseRegisterResult> baseRegisterResultList = new ArrayList<>();

        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        if (cityWebsite != null)
            resultList = sewerageIndexService.getAllwardwiseBaseRegisterOrderByShscNumberAsc(cityWebsite.getName(),
                    getWardNames(sewerageBaseRegisterResult, model));
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
            searchResult.setArrears(sewerageIndex.getArrearAmount().setScale(0, BigDecimal.ROUND_HALF_EVEN));
            searchResult.setCurrentDemand(
                    sewerageIndex.getDemandAmount().setScale(0, BigDecimal.ROUND_HALF_EVEN));
            searchResult.setAdvanceAmount(
                    sewerageIndex.getExtraAdvanceAmount() != null
                            ? sewerageIndex.getExtraAdvanceAmount().setScale(0, BigDecimal.ROUND_HALF_EVEN)
                            : BigDecimal.ZERO);
            searchResult.setArrearsCollected(sewerageIndex.getCollectedArrearAmount().setScale(0, BigDecimal.ROUND_HALF_EVEN));
            searchResult.setCurrentTaxCollected(sewerageIndex.getCollectedDemandAmount().setScale(0, BigDecimal.ROUND_HALF_EVEN));
            searchResult
                    .setTotalTaxCollected(
                            sewerageIndex.getCollectedArrearAmount().add(sewerageIndex.getCollectedDemandAmount()).setScale(0,
                                    BigDecimal.ROUND_HALF_EVEN));
            searchResult.setTotalDemand(sewerageIndex.getTotalAmount().setScale(0, BigDecimal.ROUND_HALF_EVEN));
            baseRegisterResultList.add(searchResult);
        }
        return baseRegisterResultList;

    }

    public List<String> getWardNames(final SewerageBaseRegisterResult sewerageBaseRegisterResult, final Model model) {
        final List<String> wardNames = new ArrayList<>();
        String[] wardIds;
        final List<Boundary> wardList = new ArrayList<>();
        if (sewerageBaseRegisterResult.getMode() != null) {
            wardIds = sewerageBaseRegisterResult.getMode().split("~");
            for (final String idValue : wardIds) {
                final Boundary ward = boundaryService.getBoundaryById(Long.parseLong(idValue));
                if (ward != null)
                    wardList.add(ward);
            }
        }
        if (wardList != null && !wardList.isEmpty())
            sewerageBaseRegisterResult.setWards(wardList);

        if (null != sewerageBaseRegisterResult.getWards() && !sewerageBaseRegisterResult.getWards().isEmpty())
            for (final Boundary boundary : sewerageBaseRegisterResult.getWards())
                if (boundary != null) {
                    sewerageBaseRegisterResult.setRevenueWard(boundary.getLocalName());
                    model.addAttribute("wardId", boundary.getId());
                    if (boundary.getLocalName() != null)
                        wardNames.add(boundary.getLocalName());
                }
        return wardNames;
    }

    public List<BigDecimal> baseRegisterGrandTotal(final SewerageBaseRegisterResult sewerageBaseRegisterResult,
            final Model model) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());

        return sewerageIndexService.getGrandTotal(cityWebsite.getName(), getWardNames(sewerageBaseRegisterResult, model));

    }

}