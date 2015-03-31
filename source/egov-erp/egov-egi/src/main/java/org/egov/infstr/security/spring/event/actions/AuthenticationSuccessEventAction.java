package org.egov.infstr.security.spring.event.actions;

import java.util.Date;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.security.authentication.SecureUser;
import org.egov.infstr.commons.EgLoginLog;
import org.egov.infstr.security.utils.SecurityConstants;
import org.egov.lib.security.terminal.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class will get called when Authentication is successful. Now this class
 * only Logs the User Login information.
 **/
@Service
@Transactional
public class AuthenticationSuccessEventAction implements
        ApplicationSecurityEventAction<InteractiveAuthenticationSuccessEvent> {

    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void doAction(final InteractiveAuthenticationSuccessEvent authorizedEvent) {
        final Authentication authentication = authorizedEvent.getAuthentication();
        final HashMap<String, String> credentials = (HashMap<String, String>) authentication.getCredentials();
        final EgLoginLog login = new EgLoginLog();
        login.setLoginTime(new Date(authorizedEvent.getTimestamp()));
        login.setUser(userService.getUserById(((SecureUser) authentication.getPrincipal()).getUserId()));
        if (org.apache.commons.lang.StringUtils.isNotBlank(credentials.get(SecurityConstants.COUNTER_FIELD))) {
            final Location location = entityManager.find(Location.class,
                    Integer.valueOf(credentials.get(SecurityConstants.COUNTER_FIELD)));
            login.setLocation(location);
        }
        entityManager.persist(login);
        entityManager.flush();
        final String loginLogID = login.getId().toString();
        ((HashMap<String, String>) authentication.getCredentials()).put(SecurityConstants.LOGIN_LOG_ID, loginLogID);
    }
}
