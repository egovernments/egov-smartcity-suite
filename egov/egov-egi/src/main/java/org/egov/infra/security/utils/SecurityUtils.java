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

package org.egov.infra.security.utils;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.security.authentication.SecureUser;
import org.egov.infra.persistence.entity.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityUtils {
    private static final long ANONYMOUS_USER_ID = 2L;
    @Autowired
    private UserService userService;

    public static boolean isCurrentUserAuthenticated() {
        final Optional<Authentication> authentication = getCurrentAuthentication();
        return authentication.isPresent() && authentication.get().isAuthenticated();
    }

    public static boolean isCurrentUserAnonymous() {
        return getCurrentAuthentication().get().getPrincipal() instanceof String;
    }

    public static Optional<Authentication> getCurrentAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    public User getCurrentUser() {
        if (isCurrentUserAuthenticated()) {
            if (isCurrentUserAnonymous())
                return userService.getUserById(ANONYMOUS_USER_ID);
            else
                return userService.getUserById(((SecureUser) getCurrentAuthentication().get().getPrincipal()).getUserId());
        } else
            return userService.getUserById(ANONYMOUS_USER_ID);

    }

    public UserType currentUserType() {
        return getCurrentAuthentication().isPresent() && !isCurrentUserAnonymous() ?
                ((SecureUser) getCurrentAuthentication().get().getPrincipal()).getUserType() : UserType.SYSTEM;
    }

    public boolean hasRole(final String role) {
        return getCurrentAuthentication().isPresent() && getCurrentAuthentication().get()
                .getAuthorities()
                .parallelStream()
                .map(grantedAuthority -> grantedAuthority.getAuthority().equals(role))
                .findFirst().get();
    }

    public boolean currentUserIsCitizen() {
        return currentUserType().equals(UserType.CITIZEN);
    }

    public boolean currentUserIsEmployee() {
        return currentUserType().equals(UserType.EMPLOYEE);
    }
}
