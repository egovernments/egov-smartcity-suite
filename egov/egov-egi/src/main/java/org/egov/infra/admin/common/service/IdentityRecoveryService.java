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

package org.egov.infra.admin.common.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.egov.infra.admin.common.entity.IdentityRecovery;
import org.egov.infra.admin.common.repository.IdentityRecoveryRepository;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.infra.messaging.MessagingService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.egov.infra.messaging.MessagePriority.HIGH;

@Service
@Transactional(readOnly = true)
public class IdentityRecoveryService {
    private static final String USER_PASWRD_RECOVERY_TMPLTE = "user.pwd.recovery";

    @Autowired
    private IdentityRecoveryRepository identityRecoveryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationProperties applicationProperties;

    public Optional<IdentityRecovery> getByToken(final String token) {
        return Optional.ofNullable(identityRecoveryRepository.findByToken(token));
    }

    @Transactional
    public IdentityRecovery generate(final User user, final Date timeToExpire, boolean byOTP) {
        final IdentityRecovery identityRecovery = new IdentityRecovery();
        identityRecovery.setToken(byOTP ?
                RandomStringUtils.random(5, Boolean.TRUE, Boolean.TRUE).toUpperCase() :
                UUID.randomUUID().toString());
        identityRecovery.setUser(user);
        identityRecovery.setExpiry(timeToExpire);
        return identityRecoveryRepository.save(identityRecovery);
    }

    @Transactional
    public boolean generateAndSendUserPasswordRecovery(final String identity, final String urlToSent, boolean byOTP) {
        final Optional<User> user = userService.checkUserWithIdentity(identity);
        if (user.isPresent()) {
            final IdentityRecovery identityRecovery = generate(user.get(), new DateTime().plusMinutes(5).toDate(), byOTP);
            if (byOTP) {
                String message = "Your OTP for recovering password is " + identityRecovery.getToken();
                messagingService.sendSMS(user.get().getMobileNumber(), message, HIGH);
                messagingService.sendEmail(user.get().getEmailId(), "Password Reset", message);
            } else
                messagingService.sendEmail(identityRecovery.getUser(), "Password Recovery", USER_PASWRD_RECOVERY_TMPLTE, urlToSent,
                        identityRecovery.getToken(), System.getProperty("line.separator"));
        }
        return user.isPresent();
    }

    @Transactional
    public boolean validateAndResetPassword(final String token, final String newPassword) {
        boolean recoverd = false;
        final Optional<IdentityRecovery> identityRecovery = getByToken(token);
        if (identityRecovery.isPresent()) {
            final IdentityRecovery idRecovery = identityRecovery.get();
            if (idRecovery.getExpiry().isAfterNow()) {
                final User user = idRecovery.getUser();
                user.updateNextPwdExpiryDate(applicationProperties.userPasswordExpiryInDays());
                user.setPassword(passwordEncoder.encode(newPassword));
                userService.updateUser(user);
                recoverd = true;
            }
            identityRecoveryRepository.delete(idRecovery);
        }
        return recoverd;
    }

    public boolean tokenValid(final String token) {
        final Optional<IdentityRecovery> identityRecovery = getByToken(token);
        return identityRecovery.isPresent() && identityRecovery.get().getExpiry().isAfterNow();
    }

}
