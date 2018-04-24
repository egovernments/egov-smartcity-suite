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
package org.egov.portal.firm.service;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.portal.entity.Firm;
import org.egov.portal.entity.FirmUser;
import org.egov.portal.entity.SearchRequestFirm;
import org.egov.portal.firm.repository.FirmRepository;
import org.egov.portal.firm.repository.FirmUserRepository;
import org.egov.portal.repository.specs.SearchFirmSpec;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FirmService {

    public static final String ROLE_BUSINESS_USER = "BUSINESS";

    @Autowired
    private FirmRepository firmRepository;

    @Autowired
    private FirmUserRepository firmUserRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Transactional
    public Firm create(final Firm firm) {
        validateFirmUsers(firm);
        FirmUser firmUsers;
        for (final FirmUser firmUser : firm.getTempFirmUsers()) {
            firmUsers = createFirmUserObject();
            if (firmUser.getEgUserId() == null) {
                User user;
                user = userService.getUserByUsername(firmUser.getEmailId());
                if (user == null)
                    user = createUser(firmUser);
                firmUsers.setUser(user);
            } else
                firmUsers.setUser(firmUser.getUser());
            firmUsers.setMobileNumber(firmUser.getMobileNumber());
            firmUsers.setEmailId(firmUser.getEmailId());
            firmUsers.setName(firmUser.getName());
            firmUsers.setFirm(firm);
            firm.getFirmUsers().add(firmUsers);
        }
        return firmRepository.save(firm);
    }

    private void validateFirmUsers(final Firm firm) throws ApplicationRuntimeException {
        // add validation for mandatory fields
        if (firm != null && firm.getTempFirmUsers() != null && !firm.getTempFirmUsers().isEmpty())
            for (final FirmUser firmUser : firm.getTempFirmUsers()) {
                initializeUser(firmUser);
                if (firmUser.getUser() == null) {
                    if (!(firmUser.getEmailId() == null || firmUser.getEmailId().isEmpty())
                            && getFirmUserByEmailId(firmUser.getEmailId()) != null)
                        throw new ApplicationRuntimeException(
                                "Email is already mapped to a Firm. Please Use different Email Id.");
                } else {
                    if (!firmUser.getUser().getType().toString().equalsIgnoreCase(UserType.BUSINESS.toString()))
                        throw new ApplicationRuntimeException("User should be a Business User.");
                    if (getFirmUserByUserId(firmUser.getUser().getId()) != null)
                        throw new ApplicationRuntimeException(
                                "User is already mapped to a Firm. Please Use different User Id.");
                }
            }
    }

    private void initializeUser(final FirmUser firmUser) {
        if (firmUser.getEgUserId() != null)
            firmUser.setUser(userService.getUserById(firmUser.getEgUserId()));
    }

    public Firm getFirmById(final Long firmId) {
        return firmRepository.findOne(firmId);
    }

    public Firm getFirmByPan(final String pan) {
        return firmRepository.findByPan(pan);
    }

    public FirmUser getFirmUserByEmailId(final String emailId) {
        return firmUserRepository.findByEmailId(emailId);
    }

    public FirmUser getFirmUserByUserId(final Long userId) {
        return firmUserRepository.findByUser_Id(userId);
    }

    @Transactional
    public void createFirm(final Firm firm) {
        FirmUser firmUsers = null;
        firm.getFirmUsers().clear();
        for (final FirmUser firmUser : firm.getTempFirmUsers()) {
            User user;
            firmUsers = createFirmUserObject();
            user = userService.getUserByUsername(firmUser.getEmailId());
            if (user == null)
                user = createUser(firmUser);
            firmUsers.setUser(user);
            firmUsers.setMobileNumber(firmUser.getMobileNumber());
            firmUsers.setEmailId(firmUser.getEmailId());
            firmUsers.setName(firmUser.getName());
            firmUsers.setFirm(firm);
            firm.getFirmUsers().add(firmUsers);
        }
        firmRepository.save(firm);
    }

    private User createUser(final FirmUser firmUser) {
        User user = new User(UserType.BUSINESS);
        user.setUsername(firmUser.getEmailId());
        user.setName(firmUser.getName());
        user.setMobileNumber(firmUser.getMobileNumber());
        user.setPassword(passwordEncoder.encode(firmUser.getMobileNumber()));
        user.setEmailId(firmUser.getEmailId());
        user.setActive(true);
        user.setPwdExpiryDate(DateTime.now().plusMonths(6).toDate());
        user.addRole(roleService.getRoleByName(ROLE_BUSINESS_USER));
        user = userService.createUser(user);
        return user;
    }

    private FirmUser createFirmUserObject() {
        return new FirmUser();
    }

    public List<Firm> searchFirm(final SearchRequestFirm searchRequestFirm) {
        final List<Firm> firms = firmRepository.findAll(SearchFirmSpec.searchFirm(searchRequestFirm));
        return firms;
    }

}