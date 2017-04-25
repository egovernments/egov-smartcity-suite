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

package org.egov.wtms.web.controller.search;

import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WATER_TAX_INDEX_NAME;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.wtms.entity.es.ConnectionSearchRequest;
import org.egov.wtms.entity.es.WaterChargeDocument;
import org.egov.wtms.repository.es.WaterChargeDocumentRepository;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/search/waterSearch/")
public class WaterTaxSearchController {

    private final CityService cityService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private WaterChargeDocumentRepository waterChargeDocumentRepository;

    @Autowired
    public WaterTaxSearchController(final CityService cityService) {
        this.cityService = cityService;
    }

    @ModelAttribute
    public ConnectionSearchRequest searchRequest() {
        return new ConnectionSearchRequest();
    }

    /**
     * Assumptions: assuming appconfig Key "RolesForSearchWaterTaxConnection" List Contals 1st Entry "CSC Operator" order by value
     * asc
     *
     * @return String if Logged in User is CSC Operattor
     */
    @ModelAttribute("cscUserRole")
    public String getCurrentUserRole() {
        String cscUserRole = "";
        User currentUser = null;

        if (ApplicationThreadLocals.getUserId() != null)
            currentUser = userService.getUserById(ApplicationThreadLocals.getUserId());
        else
            currentUser = securityUtils.getCurrentUser();

        for (final Role userrole : currentUser.getRoles())
            if (userrole.getName().equals(WaterTaxConstants.ROLE_CSCOPERTAOR)) {
                cscUserRole = userrole.getName();
                break;
            }
        return cscUserRole;
    }

    @ModelAttribute("citizenRole")
    public Boolean getCitizenUserRole() {
        Boolean citizenrole = Boolean.FALSE;
        if (ApplicationThreadLocals.getUserId() != null) {
            final User currentUser = userService.getUserById(ApplicationThreadLocals.getUserId());
            for (final Role userrole : currentUser.getRoles())
                if (userrole.getName().equals(WaterTaxConstants.ROLE_CITIZEN)) {
                    citizenrole = Boolean.TRUE;
                    break;
                }
        } else
            citizenrole = Boolean.TRUE;
        return citizenrole;
    }

    /**
     * Assumptions: assuming appconfig Key "RolesForSearchWaterTaxConnection" List Contals 4th Entry "ULB Operator" order by value
     * asc
     *
     * @return String if Logged in User is ULB Operattor
     */
    @ModelAttribute("ulbUserRole")
    public String getUlbOperatorUserRole() {
        String userRole = "";
        User currentUser = null;
        if (ApplicationThreadLocals.getUserId() != null)
            currentUser = userService.getUserById(ApplicationThreadLocals.getUserId());
        else
            currentUser = securityUtils.getCurrentUser();
        for (final Role userrole : currentUser.getRoles())
            if (userrole.getName().equals(WaterTaxConstants.ROLE_ULBOPERATOR)) {
                userRole = userrole.getName();
                break;
            }
        return userRole;
    }

    /**
     * @return String if Logged in User is SUPER USER
     */
    @ModelAttribute("superUserRole")
    public String getSuperUserRole() {
        String userRole = "";
        User currentUser = null;

        if (ApplicationThreadLocals.getUserId() != null)
            currentUser = userService.getUserById(ApplicationThreadLocals.getUserId());
        else
            currentUser = securityUtils.getCurrentUser();
        for (final Role userrole : currentUser.getRoles())
            if (userrole.getName().equals(WaterTaxConstants.ROLE_SUPERUSER)) {
                userRole = userrole.getName();
                break;
            }

        return userRole;
    }

    /**
     * @return String if Logged in User is Property Administrator
     */
    @ModelAttribute("administratorRole")
    public String getAdminstratorRole() {
        String userRole = "";
        User currentUser = null;

        if (ApplicationThreadLocals.getUserId() != null)
            currentUser = userService.getUserById(ApplicationThreadLocals.getUserId());
        else
            currentUser = securityUtils.getCurrentUser();
        for (final Role userrole : currentUser.getRoles())
            if (userrole.getName().equals(WaterTaxConstants.ROLE_ADMIN)) {
                userRole = userrole.getName();
                break;
            }

        return userRole;
    }

    /**
     * Assumptions: assuming appconfig Key "RolesForSearchWaterTaxConnection" List Contals 3th Entry "Water Tax Approver" order by
     * value asc
     *
     * @return String if Logged in User is Water Tax Approver
     */
    @ModelAttribute("approverUserRole")
    public String getApproverUserRole() {
        String userRole = "";
        User currentUser = null;
        waterTaxUtils.getUserRolesForLoggedInUser();

        if (ApplicationThreadLocals.getUserId() != null)
            currentUser = userService.getUserById(ApplicationThreadLocals.getUserId());
        else
            currentUser = securityUtils.getCurrentUser();
        for (final Role userrole : currentUser.getRoles())
            if (userrole.getName().equals(WaterTaxConstants.ROLE_APPROVERROLE)) {
                userRole = userrole.getName();
                break;
            }
        return userRole;
    }

    /**
     * Assumptions: assuming appconfig Key "RolesForSearchWaterTaxConnection" List Contals 5th Entry "Operator"
     *
     * @return String if Logged in User is Operator
     */
    @ModelAttribute("operatorRole")
    public String getOperatorUserRole() {
        String userRole = "";
        User currentUser = null;
        if (ApplicationThreadLocals.getUserId() != null)
            currentUser = userService.getUserById(ApplicationThreadLocals.getUserId());
        else
            currentUser = securityUtils.getCurrentUser();
        for (final Role userrole : currentUser.getRoles())
            if (userrole.getName().equals(WaterTaxConstants.ROLE_OPERATOR)) {
                userRole = userrole.getName();
                break;
            }
        return userRole;
    }

    @ModelAttribute("billcollectionRole")
    public String getBillOperatorUserRole() {
        String userRole = "";
        User currentUser = null;
        if (ApplicationThreadLocals.getUserId() != null)
            currentUser = userService.getUserById(ApplicationThreadLocals.getUserId());
        else
            currentUser = securityUtils.getCurrentUser();
        for (final Role userrole : currentUser.getRoles())
            if (userrole.getName().equals(WaterTaxConstants.ROLE_BILLCOLLECTOR)) {
                userRole = userrole.getName();
                break;
            }
        return userRole;
    }

    public @ModelAttribute("revenueWards") List<Boundary> revenueWardList() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WaterTaxConstants.REVENUE_WARD,
                REVENUE_HIERARCHY_TYPE);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String newSearchForm(final Model model) {
        return "waterTaxSearch-newForm";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public List<ConnectionSearchRequest> searchConnection(@ModelAttribute final ConnectionSearchRequest searchRequest) {
        List<WaterChargeDocument> temList = new ArrayList<>();
        final List<ConnectionSearchRequest> finalResult = new ArrayList<>();
        temList = findAllWaterChargeIndexByFilter(searchRequest);
        for (final WaterChargeDocument waterChargeIndex : temList) {
            final ConnectionSearchRequest customerObj = new ConnectionSearchRequest();
            customerObj.setApplicantName(waterChargeIndex.getConsumerName());
            customerObj.setConsumerCode(waterChargeIndex.getConsumerCode());
            customerObj.setOldConsumerNumber(waterChargeIndex.getOldConsumerCode());
            customerObj.setAddress(waterChargeIndex.getLocality());
            customerObj.setApplicationcode(waterChargeIndex.getApplicationCode());
            customerObj.setUsage(waterChargeIndex.getUsage());
            customerObj.setIslegacy(waterChargeIndex.getLegacy());
            customerObj.setPropertyTaxDue(waterChargeIndex.getTotalDue());
            customerObj.setStatus(waterChargeIndex.getStatus());
            customerObj.setConnectiontype(waterChargeIndex.getConnectionType());
            customerObj.setWaterTaxDue(waterChargeIndex.getWaterTaxDue());
            finalResult.add(customerObj);
        }
        return finalResult;

    }

    private BoolQueryBuilder getFilterQuery(final ConnectionSearchRequest searchRequest) {
        final City cityWebsite = cityService.getCityByCode(ApplicationThreadLocals.getCityCode());
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery("cityName", cityWebsite.getName()));
        if (StringUtils.isNotBlank(searchRequest.getApplicantName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("consumerName", searchRequest.getApplicantName()));
        if (StringUtils.isNotBlank(searchRequest.getConsumerCode()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("consumerCode", searchRequest.getConsumerCode()));
        if (StringUtils.isNotBlank(searchRequest.getOldConsumerNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("oldConsumerCode", searchRequest.getOldConsumerNumber()));
        if (StringUtils.isNotBlank(searchRequest.getRevenueWard()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("revenueWard", searchRequest.getRevenueWard()));
        if (StringUtils.isNotBlank(searchRequest.getMobileNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("mobileNumber", searchRequest.getMobileNumber()));
        if (StringUtils.isNotBlank(searchRequest.getDoorNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("doorNo", searchRequest.getDoorNumber()));
        if (StringUtils.isNotBlank(searchRequest.getLocality()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("locality", searchRequest.getLocality()));

        return boolQuery;
    }

    public List<WaterChargeDocument> findAllWaterChargeIndexByFilter(final ConnectionSearchRequest searchRequest) {

        final BoolQueryBuilder query = getFilterQuery(searchRequest);
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(WATER_TAX_INDEX_NAME)
                .withQuery(query).withPageable(new PageRequest(0, 250)).build();

        final Iterable<WaterChargeDocument> sampleEntities = waterChargeDocumentRepository.search(searchQuery);
        final List<WaterChargeDocument> sampleEntitiesTemp = new ArrayList<>();
        for (final WaterChargeDocument document : sampleEntities)
            sampleEntitiesTemp.add(document);

        return sampleEntitiesTemp;
    }

}