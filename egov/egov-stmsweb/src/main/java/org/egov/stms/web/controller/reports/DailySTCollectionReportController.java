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
package org.egov.stms.web.controller.reports;

import org.egov.collection.constants.CollectionConstants;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.stms.elasticsearch.entity.DailySTCollectionReportSearch;
import org.egov.stms.service.es.SewerageIndexService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/reports/dailySTCollectionReport/search/")
public class DailySTCollectionReportController {

    @Autowired
    public EgwStatusHibernateDAO egwStatusHibernateDAO;
    @Autowired
    public AssignmentService assignmentService;
    @Autowired
    public AppConfigValueService appConfigValueService;
    @Autowired
    private CityService cityService;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private SewerageIndexService sewerageIndexService;
    
    @ModelAttribute
    public void reportModel(final Model model) {
        final DailySTCollectionReportSearch dailyCollectionReportResut = new DailySTCollectionReportSearch();
        model.addAttribute("dailySTCollectionReport", dailyCollectionReportResut);
    }

    @ModelAttribute("collectionMode")
    public Map<String, String> loadInstrumentTypes() {
        final Map<String, String> collectionModeMap = new LinkedHashMap<>(0);
        collectionModeMap.put(Source.ESEVA.toString(), Source.ESEVA.toString());
        collectionModeMap.put(Source.MEESEVA.toString(), Source.MEESEVA.toString());
        collectionModeMap.put(Source.APONLINE.toString(), Source.APONLINE.toString());
        collectionModeMap.put(Source.SOFTTECH.toString(), Source.SOFTTECH.toString());
        collectionModeMap.put(Source.SYSTEM.toString(), Source.SYSTEM.toString());
        return collectionModeMap;
    }

    @ModelAttribute("operators")
    public Set<User> loadCollectionOperators() {
        final String operatorDesignation = appConfigValueService.getAppConfigValueByDate(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.COLLECTION_DESIGNATIONFORCSCOPERATOR, new Date()).getValue();
        return assignmentService.getUsersByDesignations(operatorDesignation.split(","));
    }

    @ModelAttribute("status")
    public List<EgwStatus> loadStatus() {
        return egwStatusHibernateDAO.getStatusByModule(CollectionConstants.MODULE_NAME_RECEIPTHEADER);
    }

    @ModelAttribute("wards")
    public List<Boundary> wardBoundaries() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(PropertyTaxConstants.WARD,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @RequestMapping(method = GET)
    public String search(final Model model) {
        model.addAttribute("currentDate", new Date());
        return "dailySTCollection-search";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public List<DailySTCollectionReportSearch> searchCollection(@ModelAttribute final DailySTCollectionReportSearch searchRequest) {
        List<DailySTCollectionReportSearch> resultList;
        final City cityWebsite = cityService.getCityByName(ApplicationThreadLocals.getCityName());
        searchRequest.setUlbName(cityWebsite.getName());
        final BoolQueryBuilder boolQuery = sewerageIndexService.getDCRSearchResult(searchRequest);
        resultList = sewerageIndexService.getDCRSewerageReportResult(searchRequest, boolQuery);
        return resultList;
    }
}
