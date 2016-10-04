package org.egov.infra.config.process.auth;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityImpl;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.egov.infra.config.process.entity.Group;
import org.egov.infra.config.process.service.GroupService;

public class ProcessUserGroupManager extends AbstractManager implements GroupEntityManager {

    private GroupService userGroupService;

    public ProcessUserGroupManager(final ProcessEngineConfigurationImpl processEngineConfiguration, GroupService userGroupService) {
        super(processEngineConfiguration);
        this.userGroupService = userGroupService;
    }

    @Override
    public GroupEntity findById(final String entityId) {
        return mapUserGroupToGroupEntity(userGroupService.getGroupByName(entityId));
    }

    @Override
    public List<org.activiti.engine.identity.Group> findGroupByQueryCriteria(final GroupQueryImpl query, final Page page) {
        if (isNotBlank(query.getName()))
            return Arrays.asList(findById(query.getName()));
        if (isNotBlank(query.getNameLike()))
            return userGroupService.getGroupsByNameLike(query.getNameLike()).stream().
                    map(this::mapUserGroupToGroupEntity).collect(Collectors.toList());
        return null;
    }

    @Override
    public long findGroupCountByQueryCriteria(final GroupQueryImpl query) {
        return findGroupByQueryCriteria(query, null).size();
    }

    @Override
    public List<org.activiti.engine.identity.Group> findGroupsByUser(final String userId) {
        //TODO has to implement if required
        return null;
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
        throw new ActivitiException("Process user group manager doesn't support creating a new group");
    }

    @Override
    public GroupQuery createNewGroupQuery() {
        throw new ActivitiException("Process user group manager doesn't support creating a new group");
    }

    @Override
    public boolean isNewGroup(final org.activiti.engine.identity.Group group) {
        throw new ActivitiException("Process user group manager doesn't support checking is new group");
    }

    @Override
    public GroupEntity create() {
        throw new ActivitiException("Process user group manager doesn't support creating a new group");
    }



    @Override
    public void insert(final GroupEntity entity) {
        throw new ActivitiException("Process user group manager doesn't support creating a new group");
    }

    @Override
    public void insert(final GroupEntity entity, final boolean fireCreateEvent) {
        throw new ActivitiException("Process user group manager doesn't support creating a new group");
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
        throw new ActivitiException("Process user group manager doesn't support delete a group");
    }

    @Override
    public void delete(final GroupEntity entity) {
        throw new ActivitiException("Process user group manager doesn't support delete a group");
    }

    @Override
    public void delete(final GroupEntity entity, final boolean fireDeleteEvent) {
        throw new ActivitiException("Process user group manager doesn't support delete a group");
    }

    private GroupEntity mapUserGroupToGroupEntity(Group group) {
        GroupEntity groupEntity = new GroupEntityImpl();
        groupEntity.setName(group.getName());
        groupEntity.setType(group.getType());
        return groupEntity;
    }
}
