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

package org.egov.infra.admin.common.service;

import org.egov.infra.admin.common.entity.IdentityRecovery;
import org.egov.infra.admin.common.repository.IdentityRecoveryRepository;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.EnvironmentSettings;
import org.egov.infra.notification.service.NotificationService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.egov.infra.notification.entity.NotificationPriority.HIGH;
import static org.egov.infra.utils.StringUtils.uniqueString;

@Service
@Transactional(readOnly = true)
public class IdentityRecoveryService {
    private static final String USER_PWD_RECOVERY_TEMPLATE = "user.pwd.recovery";

    @Autowired
    private IdentityRecoveryRepository identityRecoveryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EnvironmentSettings environmentSettings;

    public Optional<IdentityRecovery> getByToken(String token) {
        return Optional.ofNullable(identityRecoveryRepository.findByToken(token));
    }

    @Transactional
    public IdentityRecovery generate(User user, Date timeToExpire, boolean byOTP) {
        IdentityRecovery identityRecovery = new IdentityRecovery();
        identityRecovery.setToken(byOTP ? uniqueString(5).toUpperCase() : UUID.randomUUID().toString());
        identityRecovery.setUser(user);
        identityRecovery.setExpiry(timeToExpire);
        return identityRecoveryRepository.save(identityRecovery);
    }

    @Transactional
    public boolean generateAndSendUserPasswordRecovery(String identity, String urlToSent, boolean byOTP) {
        Optional<User> user = userService.checkUserWithIdentity(identity);
        if (user.isPresent()) {
            IdentityRecovery identityRecovery = generate(user.get(), new DateTime().plusMinutes(5).toDate(), byOTP);
            if (byOTP) {
                String message = "Your OTP for recovering password is " + identityRecovery.getToken();
                notificationService.sendSMS(user.get().getMobileNumber(), message, HIGH);
                notificationService.sendEmail(user.get().getEmailId(), "Password Reset", message);
            } else {
                notificationService.sendEmail(identityRecovery.getUser(), "Password Recovery",
                        USER_PWD_RECOVERY_TEMPLATE, urlToSent, identityRecovery.getToken(), System.lineSeparator());
            }
        }
        return user.isPresent();
    }

    @Transactional
    public boolean validateAndResetPassword(String token, String newPassword) {
        boolean recoverd = false;
        Optional<IdentityRecovery> identityRecovery = getByToken(token);
        if (identityRecovery.isPresent()) {
            IdentityRecovery idRecovery = identityRecovery.get();
            if (idRecovery.getExpiry().isAfterNow()) {
                User user = idRecovery.getUser();
                user.updateNextPwdExpiryDate(environmentSettings.userPasswordExpiryInDays());
                user.setPassword(passwordEncoder.encode(newPassword));
                userService.updateUser(user);
                recoverd = true;
            }
            identityRecoveryRepository.delete(idRecovery);
        }
        return recoverd;
    }

    public boolean tokenValid(String token) {
        Optional<IdentityRecovery> identityRecovery = getByToken(token);
        return identityRecovery.isPresent() && identityRecovery.get().getExpiry().isAfterNow();
    }

}
