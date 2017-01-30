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

package org.egov.api.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;
import org.egov.api.adapter.ForwardDetailsAdapter;
import org.egov.api.adapter.UserAdapter;
import org.egov.api.controller.core.ApiController;
import org.egov.api.controller.core.ApiResponse;
import org.egov.api.controller.core.ApiUrl;
import org.egov.api.model.ForwardDetails;
import org.egov.eis.entity.EmployeeView;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.workflow.entity.State.StateStatus;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.inbox.InboxRenderServiceDeligate;
import org.egov.infra.workflow.service.WorkflowTypeService;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.pgr.service.PriorityService;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.egov.infra.utils.ApplicationConstant.N;
import static org.egov.infra.utils.ApplicationConstant.Y;
import static org.egov.infra.workflow.entity.StateAware.byCreatedDate;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/v1.0")
public class EmployeeController extends ApiController {

    public static final String EGOV_API_ERROR = "EGOV-API ERROR ";
    private static final Logger LOGGER = Logger.getLogger(EmployeeController.class);
    @Autowired
    InboxRenderServiceDeligate<StateAware> inboxRenderServiceDelegate;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    @Qualifier("entityQueryService")
    private PersistenceService entityQueryService;
    @Autowired
    private WorkflowTypeService workflowTypeService;
    @Autowired
    private PositionMasterService posMasterService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private EISServeable eisService;
    @Autowired
    private PriorityService priorityService;

    @RequestMapping(value = ApiUrl.EMPLOYEE_INBOX_LIST_WFT_COUNT, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getWorkFlowTypesWithItemsCount(final HttpServletRequest request) {
        final ApiResponse res = ApiResponse.newInstance();
        try {
            return res.setDataAdapter(new UserAdapter())
                    .success(getWorkflowTypesWithCount(securityUtils.getCurrentUser().getId(), posMasterService
                            .getPositionsForEmployee(securityUtils.getCurrentUser().getId(), new Date()).parallelStream()
                            .map(position -> position.getId()).collect(Collectors.toList())));
        } catch (final Exception ex) {
            LOGGER.error(EGOV_API_ERROR, ex);
            return res.error(getMessage("server.error"));
        }
    }

    @RequestMapping(value = ApiUrl.EMPLOYEE_INBOX_LIST_FILTER_BY_WFT, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getInboxListByWorkFlowType(@PathVariable final String workFlowType,
                                                             @PathVariable final int resultsFrom, @PathVariable final int resultsTo,
                                                             @RequestParam(value="priority", required = false) final String priority) {
        final ApiResponse res = ApiResponse.newInstance();
        try {
            return res.setDataAdapter(new UserAdapter())
                    .success(createInboxData(getWorkflowItemsByUserAndWFType(securityUtils.getCurrentUser().getId(),
                            posMasterService.getPositionsForEmployee(securityUtils.getCurrentUser().getId(), new Date())
                                    .parallelStream()
                                    .map(position -> position.getId()).collect(Collectors.toList()),
                            workFlowType, resultsFrom, resultsTo, priority)));
        } catch (final Exception ex) {
            LOGGER.error(EGOV_API_ERROR, ex);
            return res.error(getMessage("server.error"));
        }
    }
/*
    @RequestMapping(value = ApiUrl.EMPLOYEE_SEARCH_INBOX, method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> searchEmployeeInbox(@PathVariable final Integer pageno, @PathVariable final Integer limit,
            @RequestBody final ComplaintSearchRequest searchRequest) {
        try {

            final org.egov.search.domain.Page page = org.egov.search.domain.Page.at(pageno);
            page.ofSize(limit);

            final SearchResult searchResult = searchService.search(
                    asList(Index.PGR.toString()),
                    asList(IndexType.COMPLAINT.toString()),
                    searchRequest.searchQuery(), searchRequest.searchFilters(),
                    Sort.by().field("common.createdDate", SortOrder.DESC), page);

            final String jsonString = searchResult.rawResponse();

            final JSONObject respObj = (JSONObject) new JSONParser().parse(jsonString);

            final JSONObject jObjHits = (JSONObject) respObj.get("hits");

            final Long total = (Long) jObjHits.get("total");

            final boolean hasNextPage = total > pageno * limit;

            final ArrayList<Document> inboxItems = new ArrayList<>();

            for (final Document document : searchResult.getDocuments()) {
                final JSONObject jResourceObj = document.getResource();

                final LinkedHashMap<String, Object> jSearchableObj = (LinkedHashMap<String, Object>) jResourceObj
                        .get("searchable");

                final LinkedHashMap<String, Object> jOwnerObj = (LinkedHashMap<String, Object>) jSearchableObj.get("owner");

                if ((int) jOwnerObj.get("id") == posMasterService.getPositionByUserId(securityUtils.getCurrentUser().getId())
                        .getId())
                    inboxItems.add(document);
            }

            final JsonArray result = (JsonArray) new Gson().toJsonTree(inboxItems,
                    new TypeToken<List<Document>>() {
                    }.getType());

            final JsonObject jsonResp = new JsonObject();
            jsonResp.add("searchItems", result);
            jsonResp.addProperty("hasNextPage", hasNextPage);

            return getResponseHandler().success(jsonResp);

        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage("server.error"));
        }

    }*/

    // --------------------------------------------------------------------------------//

    /**
     * Clear the session
     *
     * @param request
     * @return
     */
    @RequestMapping(value = ApiUrl.EMPLOYEE_LOGOUT, method = RequestMethod.POST)
    public ResponseEntity<String> logout(final HttpServletRequest request, final OAuth2Authentication authentication) {
        try {
            final OAuth2AccessToken token = tokenStore.getAccessToken(authentication);
            if (token == null)
                return ApiResponse.newInstance().error(getMessage("msg.logout.unknown"));

            tokenStore.removeAccessToken(token);
            return ApiResponse.newInstance().success("", getMessage("msg.logout.success"));
        } catch (final Exception ex) {
            LOGGER.error(EGOV_API_ERROR, ex);
            return ApiResponse.newInstance().error(getMessage("server.error"));
        }
    }

    @RequestMapping(value = ApiUrl.EMPLOYEE_FORWARD_DEPT_DESIGNATION_USER, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getForwardDetails(
            @RequestParam(value = "department", required = true) final Integer departmentId,
            @RequestParam(value = "designation", required = false) final Integer designationId) {
        try {

            final ForwardDetails forwardDetails = new ForwardDetails();

            // identify requesting for users or designations with this if condition
            if (departmentId != null && designationId != null) {
                final Set<EmployeeView> users = new HashSet<>();
                final HashMap<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("departmentId", String.valueOf(departmentId));
                paramMap.put("designationId", String.valueOf(designationId));
                final List<EmployeeView> empViewList = (List<EmployeeView>) eisService.getEmployeeInfoList(paramMap);
                empViewList.stream().forEach(user -> {
                    user.getEmployee().getRoles().stream().forEach(role -> {
                        if (role.getName().matches("Redressal Officer|Grievance Officer|Grievance Routing Officer"))
                            users.add(user);
                    });
                });
                forwardDetails.setUsers(users);
            } else if (departmentId != null)
                forwardDetails.setDesignations(eisService.getAllDesignationByDept(departmentId, new Date()));

            return getResponseHandler().setDataAdapter(new ForwardDetailsAdapter()).success(forwardDetails);

        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage("server.error"));
        }
    }

    /* DATA RELATED FUCNTIONS */

    private JsonArray createInboxData(final List<StateAware> inboxStates) {
        final JsonArray inboxItems = new JsonArray();
        inboxStates.sort(byCreatedDate());
        for (final StateAware stateAware : inboxStates)
            inboxItems.add(new JsonParser().parse(stateAware.getStateInfoJson()).getAsJsonObject());
        return inboxItems;
    }

    @SuppressWarnings("unchecked")
    public List<StateAware> getWorkflowItemsByUserAndWFType(final Long userId, final List<Long> owners, final String workFlowType,
                                                            final int resultsFrom, final int resultsTo, String priority) throws HibernateException, ClassNotFoundException {
    	
    	Criterion criterion=Restrictions.not(Restrictions.conjunction().add(Restrictions.eq("state.status", StateStatus.STARTED))
                .add(Restrictions.eq("createdBy.id", userId)));
    	
    	if(StringUtils.isNotBlank(priority))
    	{
    		criterion=Restrictions.conjunction(criterion).add(Restrictions.eq("priority.id", priorityService.getPriorityByCode(priority).getId()));
    	}
    	
        return entityQueryService.getSession()
                .createCriteria(Class.forName(workflowTypeService.getEnabledWorkflowTypeByType(workFlowType).getTypeFQN()))
                .setFirstResult(resultsFrom)
                .setMaxResults(resultsTo)
                .setFetchMode("state", FetchMode.JOIN).createAlias("state", "state")
                .setFlushMode(FlushMode.MANUAL).setReadOnly(true).setCacheable(true)
                .add(Restrictions.eq("state.type", workFlowType))
                .add(Restrictions.in("state.ownerPosition.id", owners))
                .add(Restrictions.ne("state.status", StateStatus.ENDED))
                .add(criterion)
                .addOrder(Order.desc("state.createdDate"))
                .list();

    }

    @SuppressWarnings("unchecked")
    public Number getWorkflowItemsCountByWFType(final Long userId, final List<Long> owners, final String workFlowType)
            throws HibernateException, ClassNotFoundException {
        return (Number) entityQueryService.getSession()
                .createCriteria(Class.forName(workflowTypeService.getEnabledWorkflowTypeByType(workFlowType).getTypeFQN()))
                .setFetchMode("state", FetchMode.JOIN).createAlias("state", "state")
                .setFlushMode(FlushMode.MANUAL).setReadOnly(true).setCacheable(true)
                .setProjection(Projections.rowCount())
                .add(Restrictions.eq("state.type", workFlowType))
                .add(Restrictions.in("state.ownerPosition.id", owners))
                .add(Restrictions.ne("state.status", StateStatus.ENDED))
                .add(Restrictions.not(Restrictions.conjunction().add(Restrictions.eq("state.status", StateStatus.STARTED))
                        .add(Restrictions.eq("createdBy.id", userId))))
                .uniqueResult();
    }

    public List<HashMap<String, Object>> getWorkflowTypesWithCount(final Long userId, final List<Long> ownerPostitions)
            throws HibernateException, ClassNotFoundException {

        final List<HashMap<String, Object>> workFlowTypesWithItemsCount = new ArrayList<>();
        final Query query = entityQueryService.getSession().createQuery(
                "select type, count(type) from State  where ownerPosition.id in (:ownerPositions) and status != :statusEnded and createdBy.id != :userId group by type");
        query.setParameterList("ownerPositions", ownerPostitions);
        query.setParameter("statusEnded", StateStatus.ENDED);
        query.setParameter("userId", userId);

        final List<Object[]> result = query.list();
        for (final Object[] rowObj : result) {
            final Long wftitemscount = (Long) getWorkflowItemsCountByWFType(userId, ownerPostitions, String.valueOf(rowObj[0]));
            if (wftitemscount > 0) {
                final HashMap<String, Object> workFlowType = new HashMap<>();
                final WorkflowTypes workFlowTypeObj = workflowTypeService.getEnabledWorkflowTypeByType(String.valueOf(rowObj[0]));
                workFlowType.put("workflowtype", rowObj[0]);
                workFlowType.put("inboxlistcount", wftitemscount);
                workFlowType.put("workflowtypename", workFlowTypeObj.getDisplayName());
                workFlowType.put("workflowgroupYN", workFlowTypeObj.isGrouped() ? Y : N);
                workFlowTypesWithItemsCount.add(workFlowType);
            }
        }
        return workFlowTypesWithItemsCount;
    }
}
