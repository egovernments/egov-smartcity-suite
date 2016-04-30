package org.egov.api.oauth2.provider;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.security.authentication.SecureUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = null;
        if (userName.contains("@") && userName.contains(".")) {
            user = userService.getUserByEmailId(userName);
        } else {
            user = userService.getUserByUsername(userName);
        }
        if (user == null) {
            throw new OAuth2Exception("Invalid login credentials");
        }

        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

        if (bcrypt.matches(password, user.getPassword())) {

            if (!user.isActive()) {
                throw new OAuth2Exception("Please activate your account");
            }
            /**
             * We assume that there will be only one type. If it is multimple then we have change below code Seperate by comma or
             * other and iterate
             */
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + user.getType()));
            user.setLastModifiedDate(new Date());
            Authentication auth = new UsernamePasswordAuthenticationToken(new SecureUser(user), password, grantedAuths);
            return auth;
        } else {
            throw new OAuth2Exception("Invalid login credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}