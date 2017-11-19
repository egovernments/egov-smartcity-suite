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
package org.egov.portal.service;

import org.egov.infra.admin.master.service.RoleService;
import org.egov.infra.config.core.EnvironmentSettings;
import org.egov.infra.notification.service.NotificationService;
import org.egov.infra.security.token.service.TokenService;
import org.egov.portal.entity.Citizen;
import org.egov.portal.repository.CitizenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.egov.infra.config.core.ApplicationThreadLocals.getDomainURL;
import static org.egov.infra.config.core.ApplicationThreadLocals.getMunicipalityName;
import static org.egov.infra.notification.entity.NotificationPriority.HIGH;
import static org.egov.infra.utils.ApplicationConstant.CITIZEN_ROLE_NAME;
import static org.egov.infra.utils.ApplicationConstant.CITY_LOGIN_URL;

@Service
@Transactional(readOnly = true)
public class CitizenService {

    private static final String CITIZEN_REG_SERVICE = "Citizen Registration";

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EnvironmentSettings environmentSettings;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public void create(Citizen citizen) {
        citizen.addRole(roleService.getRoleByName(CITIZEN_ROLE_NAME));
        citizen.updateNextPwdExpiryDate(environmentSettings.userPasswordExpiryInDays());
        citizen.setPassword(passwordEncoder.encode(citizen.getPassword()));
        citizen.setActive(true);
        citizenRepository.saveAndFlush(citizen);
        notificationService.sendSMS(citizen.getMobileNumber(), getMessage("citizen.reg.sms"));
        notificationService.sendEmail(citizen.getEmailId(), getMessage("citizen.reg.mail.subject"),
                getMessage("citizen.reg.mail.body", citizen.getName(),
                        format(CITY_LOGIN_URL, getDomainURL()), getMunicipalityName()));
    }

    @Transactional
    public void update(Citizen citizen) {
        citizenRepository.save(citizen);
    }

    public Citizen getCitizenByEmailId(String emailId) {
        return citizenRepository.findByEmailId(emailId);
    }

    public Citizen getCitizenByUserName(String userName) {
        return citizenRepository.findByUsername(userName);
    }

    @Transactional
    public boolean isValidOTP(String otp, String mobileNumber) {
        return tokenService.redeemToken(otp, mobileNumber, CITIZEN_REG_SERVICE);
    }

    @Transactional
    public boolean sendOTPMessage(String mobileNumber) {
        String otp = randomNumeric(5);
        tokenService.generate(otp, mobileNumber, CITIZEN_REG_SERVICE);
        notificationService.sendSMS(mobileNumber, getMessage("citizen.reg.otp.sms", otp), HIGH);
        return TRUE;
    }

    private String getMessage(String msgKey, Object... arg) {
        return messageSource.getMessage(msgKey, arg, Locale.getDefault());
    }
}