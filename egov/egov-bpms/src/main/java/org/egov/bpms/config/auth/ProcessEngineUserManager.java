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
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityImpl;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.egov.infra.admin.master.service.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ProcessEngineUserManager extends AbstractManager implements UserEntityManager {

    private static final String UNSUPPORTED_DELETE_MESSAGE = "Process user manager doesn't support deleting user";
    private static final String UNSUPPORTED_UPDATE_MESSAGE = "Process user manager doesn't support updating user";
    private static final String UNSUPPORTED_CREATE_MESSAGE = "Process user manager doesn't support creating a new user";
    private UserService userService;

    public ProcessEngineUserManager(ProcessEngineConfigurationImpl processEngineConfiguration, UserService userService) {
        super(processEngineConfiguration);
        this.userService = userService;
    }

    @Override
    public UserEntity findById(final String entityId) {
        return mapUserToUserEntity(userService.getUserByUsername(entityId));
    }

    @Override
    public List<User> findUserByQueryCriteria(final UserQueryImpl query, final Page page) {
        if (isNotBlank(query.getId()))
            return Arrays.asList(mapUserToUserEntity(userService.getUserByUsername(query.getId())));
        else if (isNotBlank(query.getFullNameLike()))
            return userService.getUsersByUsernameLike(query.getFullNameLike()).stream().
                    map(this::mapUserToUserEntity).collect(Collectors.toList());
        else if (isNotBlank(query.getEmail()))
            return Arrays.asList(mapUserToUserEntity(userService.getUserByEmailId(query.getEmail())));
        return Collections.emptyList();
    }

    @Override
    public long findUserCountByQueryCriteria(final UserQueryImpl query) {
        return findUserByQueryCriteria(query, null).size();
    }

    @Override
    public Boolean checkPassword(final String userId, final String password) {
        if (password == null || password.length() == 0) {
            throw new ActivitiException("Null or empty passwords are not allowed!");
        }

        return findById(userId).getPassword().equals(password);
    }

    @Override
    public List<Group> findGroupsByUser(final String userId) {
        throw new ActivitiException("Process user manager doesn't support get group from userid");
    }

    @Override
    public List<User> findUsersByNativeQuery(final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        throw new ActivitiException("Process user manager doesn't support native query to find user");
    }

    @Override
    public long findUserCountByNativeQuery(final Map<String, Object> parameterMap) {
        throw new ActivitiException("Process user manager doesn't support native query search");
    }

    @Override
    public Picture getUserPicture(final String userId) {
        throw new ActivitiException("Process user manager doesn't support getting user picture");
    }

    @Override
    public UserQuery createNewUserQuery() {
        return new UserQueryImpl(Context.getProcessEngineConfiguration().getCommandExecutor());
    }

    @Override
    public boolean isNewUser(final User user) {
        throw new ActivitiException("Process user manager doesn't support new user check");
    }

    @Override
    public void setUserPicture(final String userId, final Picture picture) {
        throw new ActivitiException("Process user manager doesn't support to set user picture");
    }

    @Override
    public void deletePicture(final User user) {
        throw new ActivitiException("Process user manager doesn't support delete user picture");
    }

    @Override
    public User createNewUser(final String userId) {
        throw new ActivitiException(UNSUPPORTED_CREATE_MESSAGE);
    }

    @Override
    public UserEntity create() {
        throw new ActivitiException(UNSUPPORTED_CREATE_MESSAGE);
    }

    @Override
    public void insert(final UserEntity entity) {
        throw new ActivitiException(UNSUPPORTED_CREATE_MESSAGE);
    }

    @Override
    public void insert(final UserEntity entity, final boolean fireCreateEvent) {
        throw new ActivitiException(UNSUPPORTED_CREATE_MESSAGE);
    }

    @Override
    public void updateUser(final User updatedUser) {
        throw new ActivitiException(UNSUPPORTED_UPDATE_MESSAGE);
    }

    @Override
    public UserEntity update(final UserEntity entity) {
        throw new ActivitiException(UNSUPPORTED_UPDATE_MESSAGE);
    }

    @Override
    public UserEntity update(final UserEntity entity, final boolean fireUpdateEvent) {
        throw new ActivitiException(UNSUPPORTED_UPDATE_MESSAGE);
    }

    @Override
    public void delete(final String id) {
        throw new ActivitiException(UNSUPPORTED_DELETE_MESSAGE);
    }

    @Override
    public void delete(final UserEntity entity) {
        throw new ActivitiException(UNSUPPORTED_DELETE_MESSAGE);
    }

    @Override
    public void delete(final UserEntity entity, final boolean fireDeleteEvent) {
        throw new ActivitiException(UNSUPPORTED_DELETE_MESSAGE);
    }

    private UserEntity mapUserToUserEntity(org.egov.infra.admin.master.entity.User user) {
        UserEntity userEntity = new UserEntityImpl();
        userEntity.setEmail(user.getEmailId());
        userEntity.setFirstName(user.getName());
        userEntity.setLastName(user.getName());
        userEntity.setEmail(user.getEmailId());
        userEntity.setPassword(user.getPassword());
        userEntity.setId(user.getUsername());
        return userEntity;
    }
}
