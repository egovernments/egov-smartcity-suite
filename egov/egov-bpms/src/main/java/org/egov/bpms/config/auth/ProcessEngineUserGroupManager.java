/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.bpms.config.auth;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityImpl;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.egov.bpms.entity.Group;
import org.egov.bpms.service.GroupService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ProcessEngineUserGroupManager extends AbstractManager implements GroupEntityManager {

    private static final String UNSUPPORTED_CREATE_MESSAGE = "Process user group manager doesn't support creating a new group";
    private static final String UNSUPPORTED_DELETE_MESSAGE = "Process user group manager doesn't support delete a group";
    private GroupService userGroupService;

    public ProcessEngineUserGroupManager(final ProcessEngineConfigurationImpl processEngineConfiguration, GroupService userGroupService) {
        super(processEngineConfiguration);
        this.userGroupService = userGroupService;
    }

    @Override
    public GroupEntity findById(final String entityId) {
        return mapUserGroupToGroupEntity(userGroupService.findOne(Long.valueOf(entityId)));
    }

    @Override
    public List<org.activiti.engine.identity.Group> findGroupByQueryCriteria(final GroupQueryImpl query, final Page page) {
        if (isNotBlank(query.getName()))
            return Arrays.asList(findById(query.getName()));
        if (isNotBlank(query.getNameLike()))
            return userGroupService.getGroupsByNameLike(query.getNameLike()).stream().
                    map(this::mapUserGroupToGroupEntity).collect(Collectors.toList());
        return Collections.emptyList();
    }

    @Override
    public long findGroupCountByQueryCriteria(final GroupQueryImpl query) {
        return findGroupByQueryCriteria(query, null).size();
    }

    @Override
    public List<org.activiti.engine.identity.Group> findGroupsByUser(final String userId) {

        return userGroupService.findGroupsByUser(userId).stream().
                map(this::mapUserGroupToGroupEntity).collect(Collectors.toList());


    }

    @Override
    public List<org.activiti.engine.identity.Group> findGroupsByNativeQuery(final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        throw new ActivitiException("Process user group manager doesn't support native query on group");
    }

    @Override
    public long findGroupCountByNativeQuery(final Map<String, Object> parameterMap) {
        throw new ActivitiException("Process user group manager doesn't support native query on group count");
    }

    @Override
    public org.activiti.engine.identity.Group createNewGroup(final String groupId) {
        throw new ActivitiException(UNSUPPORTED_CREATE_MESSAGE);
    }

    @Override
    public GroupQuery createNewGroupQuery() {
        throw new ActivitiException(UNSUPPORTED_CREATE_MESSAGE);
    }

    @Override
    public boolean isNewGroup(final org.activiti.engine.identity.Group group) {
        throw new ActivitiException("Process user group manager doesn't support checking is new group");
    }

    @Override
    public GroupEntity create() {
        throw new ActivitiException(UNSUPPORTED_CREATE_MESSAGE);
    }


    @Override
    public void insert(final GroupEntity entity) {
        throw new ActivitiException(UNSUPPORTED_CREATE_MESSAGE);
    }

    @Override
    public void insert(final GroupEntity entity, final boolean fireCreateEvent) {
        throw new ActivitiException(UNSUPPORTED_CREATE_MESSAGE);
    }

    @Override
    public GroupEntity update(final GroupEntity entity) {
        throw new ActivitiException("Process user group manager doesn't support updating a group");
    }

    @Override
    public GroupEntity update(final GroupEntity entity, final boolean fireUpdateEvent) {
        throw new ActivitiException("Process user group manager doesn't support updating a group");
    }

    @Override
    public void delete(final String id) {
        throw new ActivitiException(UNSUPPORTED_DELETE_MESSAGE);
    }

    @Override
    public void delete(final GroupEntity entity) {
        throw new ActivitiException(UNSUPPORTED_DELETE_MESSAGE);
    }

    @Override
    public void delete(final GroupEntity entity, final boolean fireDeleteEvent) {
        throw new ActivitiException(UNSUPPORTED_DELETE_MESSAGE);
    }

    private GroupEntity mapUserGroupToGroupEntity(Group group) {
        GroupEntity groupEntity = new GroupEntityImpl();
        groupEntity.setName(group.getName());
        groupEntity.setType(group.getType());
        return groupEntity;
    }
}
