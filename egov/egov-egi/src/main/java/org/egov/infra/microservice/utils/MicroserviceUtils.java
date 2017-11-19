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

package org.egov.infra.microservice.utils;

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.microservice.contract.CreateUserRequest;
import org.egov.infra.microservice.contract.RequestInfoWrapper;
import org.egov.infra.microservice.contract.Task;
import org.egov.infra.microservice.contract.TaskResponse;
import org.egov.infra.microservice.contract.UserDetailResponse;
import org.egov.infra.microservice.contract.UserRequest;
import org.egov.infra.microservice.models.RequestInfo;
import org.egov.infra.microservice.models.UserInfo;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.support.ui.Inbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.utils.ApplicationConstant.CITIZEN_ROLE_NAME;
import static org.egov.infra.utils.DateUtils.toDefaultDateTimeFormat;

@Service
public class MicroserviceUtils {

    private static final Logger LOGGER = Logger.getLogger(MicroserviceUtils.class);
    private static final String CLIENT_ID = "client.id";

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private Environment environment;

    @Autowired
    private RoleService roleService;

    @Value("${egov.services.workflow.url}")
    private String workflowServiceUrl;

    @Value("${egov.services.user.create.url}")
    private String userServiceUrl;

    public RequestInfo createRequestInfo() {
        final RequestInfo requestInfo = new RequestInfo();
        requestInfo.setApiId("apiId");
        requestInfo.setVer("ver");
        requestInfo.setTs(new Date());
        requestInfo.setUserInfo(getUserInfo());
        return requestInfo;
    }

    public UserInfo getUserInfo() {
        final User user = securityUtils.getCurrentUser();
        final List<org.egov.infra.microservice.models.RoleInfo> roles = new ArrayList<org.egov.infra.microservice.models.RoleInfo>();
        user.getRoles().forEach(authority -> roles.add(new org.egov.infra.microservice.models.RoleInfo(authority.getName())));

        return new UserInfo(roles, user.getId(), user.getUsername(), user.getName(),
                user.getEmailId(), user.getMobileNumber(), user.getType().toString(),
                getTanentId());
    }

    public String getTanentId() {
        final String clientId = environment.getProperty(CLIENT_ID);
        String tenantId = ApplicationThreadLocals.getTenantID();
        if (isNotBlank(clientId)) {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(clientId).append('.').append(tenantId);
            tenantId = stringBuilder.toString();
        }
        return tenantId;
    }

    public void createUserMicroservice(final User user) {
        if (isNotBlank(userServiceUrl)) {

            if (user.getRoles().isEmpty() && user.getType().equals(UserType.CITIZEN))
                user.addRole(roleService.getRoleByName(CITIZEN_ROLE_NAME));

            final CreateUserRequest createUserRequest = new CreateUserRequest();
            final UserRequest userRequest = new UserRequest(user, getTanentId());
            createUserRequest.setUserRequest(userRequest);
            createUserRequest.setRequestInfo(createRequestInfo());

            final RestTemplate restTemplate = new RestTemplate();
            try {
                restTemplate.postForObject(userServiceUrl, createUserRequest, UserDetailResponse.class);
            } catch (final Exception e) {
                final String errMsg = "Exception while creating User in microservice ";                
                //throw new ApplicationRuntimeException(errMsg, e);
                LOGGER.fatal(errMsg, e);
            }
        }
    }

    public List<Task> getTasks() {

        List<Task> tasks = new ArrayList<>();
        if (isNotBlank(workflowServiceUrl)) {
            final RestTemplate restTemplate = new RestTemplate();
            TaskResponse tresp;
            try {
                RequestInfo createRequestInfo = createRequestInfo();
                RequestInfoWrapper requestInfo = new RequestInfoWrapper();
                requestInfo.setRequestInfo(createRequestInfo);
                tresp = restTemplate.postForObject(workflowServiceUrl, requestInfo, TaskResponse.class);
                tasks = tresp.getTasks();
            } catch (final Exception e) {
                final String errMsg = "Exception while getting inbox items from microservice ";
               // throw new ApplicationRuntimeException(errMsg, e);
                LOGGER.fatal(errMsg,e);
            }
        }
        return tasks;
    }

    public List<Inbox> getInboxItems() {
        List<Inbox> inboxItems = new LinkedList<>();
        if (hasWorkflowService()) {
            for (Task t : getTasks()) {
                Inbox inboxItem = new Inbox();
                inboxItem.setId(t.getId());
                inboxItem.setCreatedDate(t.getCreatedDate());
                inboxItem.setDate(toDefaultDateTimeFormat(t.getCreatedDate()));
                inboxItem.setSender(t.getSenderName());
                inboxItem.setTask(t.getNatureOfTask());
                inboxItem.setStatus(t.getStatus());
                inboxItem.setDetails(t.getDetails());
                inboxItem.setLink(t.getUrl());
                inboxItem.setSender(t.getSenderName());
                inboxItems.add(inboxItem);
            }
        }
        return inboxItems;
    }

    public boolean hasWorkflowService() {
        return isNotBlank(workflowServiceUrl);
    }
}