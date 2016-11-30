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

package org.egov.infstr.security.spring.dao;

import org.egov.infra.security.audit.entity.LoginAttempt;
import org.egov.infra.security.audit.service.LoginAttemptService;
import org.egov.infra.security.utils.RecaptchaUtils;
import org.egov.infra.security.utils.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Optional;

import static org.egov.infra.security.utils.SecurityConstants.MAX_LOGIN_ATTEMPT_ALLOWED;

public class EgovDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private RecaptchaUtils recaptchaUtils;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            return super.authenticate(authentication);
        } catch (BadCredentialsException e) {
            Optional<LoginAttempt>  loginAttempt = loginAttemptService.updateFailedAttempt(authentication.getName());
            if(loginAttempt.isPresent()) {
                if (loginAttempt.get().getFailedAttempts() == MAX_LOGIN_ATTEMPT_ALLOWED) {
                    throw new LockedException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"));
                } else if (loginAttempt.get().getFailedAttempts() > 2) {
                    String message = "Too many attempts [" + (MAX_LOGIN_ATTEMPT_ALLOWED - loginAttempt.get().getFailedAttempts()) + "]";
                    throw new BadCredentialsException(message);
                }
            }
            throw e;
        } catch (LockedException le) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            if(request.getParameter("g-recaptcha-response") != null || request.getParameter("recaptcha_response_field") != null ) {
                if (recaptchaUtils.captchaIsValid(request)) {
                    this.loginAttemptService.resetFailedAttempt(authentication.getName());
                    return super.authenticate(authentication);
                } else {
                    throw new LockedException(le.getMessage()+" - Recaptcha Invalid");
                }
            }
            throw le;
        }
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        final HashMap<String, String> authenticationCredentials = (HashMap<String, String>) authentication.getCredentials();
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        final String presentedPassword = authenticationCredentials.get(SecurityConstants.PWD_FIELD);
        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }

    }
}
