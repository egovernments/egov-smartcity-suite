/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.admin.master.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.repository.UserRepository;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.microservice.contract.CreateUserRequest;
import org.egov.infra.microservice.contract.UserDetailResponse;
import org.egov.infra.microservice.contract.UserRequest;
import org.egov.infra.microservice.utils.MicroserviceUtils;
import org.egov.infra.persistence.entity.enums.Gender;
import org.egov.infra.persistence.entity.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(readOnly = true)
public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class);

    private static final String ROLE_EMPLOYEE = "Employee";
    private static final String ROLE_CITIZEN = "Citizen";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private MicroserviceUtils microserviceUtils;

    @Autowired
    private RoleService roleService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public User updateUser(final User user) {
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    public User createUser(final User user) {
        final String createUserServiceUrl = applicationProperties.getCreateUserServiceUrl();

        if (StringUtils.isNotBlank(createUserServiceUrl))
            return createUserMicroservice(user, createUserServiceUrl);
        else
            return userRepository.save(user);
    }

    public Set<User> getUsersByUsernameLike(final String userName) {
        return userRepository.findByUsernameContainingIgnoreCase(userName);
    }

    public Set<Role> getRolesByUsername(final String userName) {
        return userRepository.findUserRolesByUserName(userName);
    }

    public User getUserById(final Long id) {
        return userRepository.findOne(id);
    }

    public User getUserRefById(final Long id) {
        return userRepository.getOne(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Set<User> getActiveUsers() {
        return userRepository.findByActiveTrue();
    }

    public User getUserByUsername(final String userName) {
        return userRepository.findByUsername(userName);
    }

    public User getUserByEmailId(final String emailId) {
        return userRepository.findByEmailId(emailId);
    }

    public User getUserByAadhaarNumber(final String aadhaarNumber) {
        return userRepository.findByAadhaarNumber(aadhaarNumber);
    }

    public List<User> getUserByAadhaarNumberAndType(final String aadhaarNumber, final UserType type) {
        return userRepository.findByAadhaarNumberAndType(aadhaarNumber, type);
    }

    public User getUserByMobileNumber(final String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber);
    }

    public Optional<User> checkUserWithIdentity(final String identity) {
        return Optional.ofNullable(getUserByUsername(identity));
    }

    public List<User> findAllByMatchingUserNameForType(final String username, final UserType type) {
        return userRepository.findByUsernameContainingIgnoreCaseAndTypeAndActiveTrue(username, type);
    }

    public Set<User> getUsersByRoleName(final String roleName) {
        return userRepository.findUsersByRoleName(roleName);
    }

    public List<User> getAllEmployeeUsers() {
        return userRepository.findByTypeAndActiveTrueOrderByNameAsc(UserType.EMPLOYEE);
    }

    public List<User> getUsersByUsernameAndRolename(final String userName, final String roleName) {
        return userRepository.findUsersByUserAndRoleName(userName, roleName);
    }

    public User getUserByNameAndMobileNumberForGender(final String name, final String mobileNumber, final Gender gender) {
        return userRepository.findByNameAndMobileNumberAndGender(name, mobileNumber, gender);
    }

    private User createUserMicroservice(final User user, final String createUserServiceUrl) {

        if (user.getRoles().isEmpty())
            if (user.getType().equals(UserType.CITIZEN))
                user.addRole(roleService.getRoleByName(ROLE_CITIZEN));
            else if (user.getType().equals(UserType.EMPLOYEE))
                user.addRole(roleService.getRoleByName(ROLE_EMPLOYEE));

        final CreateUserRequest createUserRequest = new CreateUserRequest();
        final UserRequest userRequest = new UserRequest(user, microserviceUtils.getTanentId());
        createUserRequest.setUserRequest(userRequest);
        createUserRequest.setRequestInfo(microserviceUtils.createRequestInfo());

        final RestTemplate restTemplate = new RestTemplate();
        UserDetailResponse udr = null;
        try {
            udr = restTemplate.postForObject(createUserServiceUrl, createUserRequest, UserDetailResponse.class);
        } catch (final Exception e) {
            final String errMsg = "Exception while creating User in microservice ";
            LOGGER.error(errMsg, e);
            throw new ApplicationRuntimeException(errMsg, e);
        }
        final Long userId = udr.getUser().get(0).getId();
        insertUser(user, userId);
        return getUserById(userId);
    }

    private void insertUser(final User user, final Long userId) {
        final Query query = entityManager
                .createNativeQuery("insert into eg_user (createdBy, createdDate, lastModifiedBy, lastModifiedDate, "
                        + "aadhaarNumber, accountLocked, active, altContactNumber, dob, emailId, gender, guardian, guardianRelation, locale, "
                        + "mobileNumber, name, pan, password, pwdExpiryDate, salutation, signature, type, username, id) "
                        + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        query.setParameter(1, ApplicationThreadLocals.getUserId());
        query.setParameter(2, new Date(), TemporalType.TIMESTAMP);
        query.setParameter(3, ApplicationThreadLocals.getUserId());
        query.setParameter(4, new Date(), TemporalType.TIMESTAMP);
        query.setParameter(5, user.getAadhaarNumber());
        query.setParameter(6, user.isAccountLocked());
        query.setParameter(7, user.isActive());
        query.setParameter(8, user.getAltContactNumber());

        query.setParameter(9, user.getDob(), TemporalType.DATE);
        query.setParameter(10, user.getEmailId());
        if (user.getGender() != null)
            query.setParameter(11, user.getGender().toString());
        else
            query.setParameter(11, null);
        query.setParameter(11, user.getGender().ordinal());
        query.setParameter(12, user.getGuardian());
        query.setParameter(13, user.getGuardianRelation());
        query.setParameter(14, user.getLocale());
        query.setParameter(15, user.getMobileNumber());
        query.setParameter(16, user.getName());
        query.setParameter(17, user.getPan());
        query.setParameter(18, user.getPassword());
        query.setParameter(19, user.getPwdExpiryDate().toDate(), TemporalType.TIMESTAMP);
        query.setParameter(20, user.getSalutation());
        query.setParameter(21, user.getSignature());
        if (user.getType() != null)
            query.setParameter(22, user.getType().toString());
        else
            query.setParameter(22, null);
        query.setParameter(23, user.getUsername());
        query.setParameter(24, userId);
        query.executeUpdate();
        final Query seqQuery = entityManager
                .createNativeQuery("SELECT nextval (?)");
        seqQuery.setParameter(1, "seq_eg_user");
        seqQuery.getSingleResult();
        insertUserRole(user, userId);
    }

    private void insertUserRole(final User user, final Long userId) {
        for (final Role role : user.getRoles()) {
            final Query query = entityManager.createNativeQuery("insert into eg_userrole (userid, roleid) values (?, ?)");
            query.setParameter(1, userId);
            query.setParameter(2, role.getId());
            query.executeUpdate();
        }
    }

}