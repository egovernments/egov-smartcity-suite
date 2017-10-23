/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
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

package org.egov.infra.config.security.authentication.provider;

import org.egov.infra.security.audit.entity.LoginAttempt;
import org.egov.infra.security.audit.service.LoginAttemptService;
import org.egov.infra.security.utils.captcha.CaptchaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Optional;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.security.utils.SecurityConstants.MAX_LOGIN_ATTEMPT_ALLOWED;
import static org.egov.infra.security.utils.SecurityConstants.PWD_FIELD;

public class ApplicationAuthenticationProvider extends DaoAuthenticationProvider {

    private static final String BAD_CRED_MSG_KEY = "AbstractUserDetailsAuthenticationProvider.badCredentials";
    private static final String BAD_CRED_DEFAULT_MSG = "Bad credentials";
    private static final String ACCOUNT_LOCKED_MSG_KEY = "AbstractUserDetailsAuthenticationProvider.locked";
    private static final String ACCOUNT_LOCKED_DEFAULT_MSG = "User account is locked";
    private static final String TOO_MANY_ATTEMPTS_MSG_FORMAT = "Too many attempts [%d]";
    private static final String INVALID_CAPTCHA_MSG_FORMAT = "%s - Recaptcha Invalid";
    private static final String CAPTCHA_FIELD = "j_captcha_response";
    private static final String RECAPTCHA_FIELD = "g-recaptcha-response";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private CaptchaUtils recaptchaUtils;

    @Override
    public Authentication authenticate(Authentication authentication) {
        try {
            return super.authenticate(authentication);
        } catch (BadCredentialsException ex) {
            lockAccount(authentication);
            throw ex;
        } catch (LockedException le) {
            return unlockAccount(authentication, le);
        }
    }

    private Authentication unlockAccount(Authentication authentication, LockedException le) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        if (isNotBlank(request.getParameter(RECAPTCHA_FIELD)) || isNotBlank(request.getParameter(CAPTCHA_FIELD))) {
            if (recaptchaUtils.captchaIsValid(request)) {
                loginAttemptService.resetFailedAttempt(authentication.getName());
                return super.authenticate(authentication);
            } else {
                throw new LockedException(format(INVALID_CAPTCHA_MSG_FORMAT, le.getMessage()));
            }
        }
        throw le;
    }

    private void lockAccount(Authentication authentication) {
        Optional<LoginAttempt> loginAttempt = loginAttemptService.updateFailedAttempt(authentication.getName());
        if (loginAttempt.isPresent()) {
            if (loginAttempt.get().getFailedAttempts() == MAX_LOGIN_ATTEMPT_ALLOWED) {
                throw new LockedException(messages.getMessage(ACCOUNT_LOCKED_MSG_KEY, ACCOUNT_LOCKED_DEFAULT_MSG));
            } else if (loginAttempt.get().getFailedAttempts() > 2) {
                throw new BadCredentialsException(format(TOO_MANY_ATTEMPTS_MSG_FORMAT,
                        MAX_LOGIN_ATTEMPT_ALLOWED - loginAttempt.get().getFailedAttempts()));
            }
        }
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
        HashMap<String, String> authenticationCredentials = (HashMap<String, String>) authentication.getCredentials();
        if (authenticationCredentials == null ||
                !passwordEncoder.matches(authenticationCredentials.get(PWD_FIELD), userDetails.getPassword())) {
            throw new BadCredentialsException(messages.getMessage(BAD_CRED_MSG_KEY, BAD_CRED_DEFAULT_MSG));
        }
    }
}
