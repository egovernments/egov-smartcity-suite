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
package org.egov.portal.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.infra.messaging.MessagingService;
import org.egov.portal.entity.Citizen;
import org.egov.portal.repository.CitizenRepository;
import org.egov.portal.utils.constants.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.egov.infra.messaging.MessagePriority.HIGH;

@Service
@Transactional(readOnly = true)
public class CitizenService {

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Transactional
    public void create(final Citizen citizen) {
        citizen.addRole(roleService.getRoleByName(CommonConstants.CITIZEN_ROLE));
        citizen.updateNextPwdExpiryDate(applicationProperties.userPasswordExpiryInDays());
        citizen.setPassword(passwordEncoder.encode(citizen.getPassword()));
        citizen.setActivationCode(RandomStringUtils.random(5, Boolean.TRUE, Boolean.TRUE).toUpperCase());
        citizenRepository.save(citizen);
        sendActivationMessage(citizen);
    }

    @Transactional
    public void update(final Citizen citizen) {
        citizenRepository.save(citizen);
    }

    public Citizen getCitizenByEmailId(final String emailId) {
        return citizenRepository.findByEmailId(emailId);
    }

    public Citizen getCitizenByUserName(final String userName) {
        return citizenRepository.findByUsername(userName);
    }

    public Citizen getCitizenByActivationCode(final String activationCode) {
        return citizenRepository.findByActivationCode(activationCode);
    }

    @Transactional
    public Citizen activateCitizen(final String activationCode) {
        final Citizen citizen = getCitizenByActivationCode(StringUtils.defaultString(activationCode));
        if (citizen != null) {
            citizen.setActive(true);
            citizen.setActivationCode(null);
            update(citizen);
            messagingService
                    .sendEmail(
                            citizen.getEmailId(),
                            "Portal Registration Success",
                            String.format("Dear %s,\r\n You have successfully registered into our portal, you can use your registered mobile number " +
                                    "as Username to login to our portal.\r\nRegards,\r\n%s", citizen.getName(), ApplicationThreadLocals.getMunicipalityName()));
            messagingService.sendSMS(citizen.getMobileNumber(), "Your portal registration completed, please use your registered mobile number as Username to login");
        }
        return citizen;
    }

    @Transactional
    public void resendActivationCode(Citizen citizen) {
        citizen.setActivationCode(RandomStringUtils.random(5, Boolean.TRUE, Boolean.TRUE).toUpperCase());
        sendActivationMessage(citizen);
        citizenRepository.save(citizen);
    }

    public void sendActivationMessage(final Citizen citizen) {
        messagingService
                .sendEmail(
                        citizen.getEmailId(),
                        "Portal Activation",
                        String.format("Dear %s,\r%n Your Portal Activation Code is : %s", citizen.getName(),
                                citizen.getActivationCode()));
        messagingService.sendSMS(citizen.getMobileNumber(), "Your Portal Activation Code is : " + citizen.getActivationCode(), HIGH);
    }
}