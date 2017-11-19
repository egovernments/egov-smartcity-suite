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
/*
 * Created on Jun 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.util.List;

/**
 * @author sahinab
 *
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly = true)
public class User {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;

    private static final Logger LOGGER = Logger.getLogger(User.class);
    private String userName;
    private String role;

    /**
     * @return Returns the role.
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role The role to set.
     */
    public void setRole(final String role) {
        this.role = role;
    }

    /**
     * @return Returns the userId.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userId The userId to set.
     */
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * @param userId
     */
    public User(final String userName) {
        super();
        this.userName = userName;
    }

    // this method gets the assigned role for the user from the database.
    public String getRole(final Connection con) throws TaskFailedException {
        // if(LOGGER.isDebugEnabled()) LOGGER.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>domain user");
        final String query = "select r.Role_name as role from EG_ROLES r, EG_USER u,EG_USERROLE ur where u.user_name=? and ur.id_role=r.id_role and u.id_user=ur.id_user ";
        String role = "";
        try {
            final Query ps = persistenceService.getSession().createSQLQuery(query);
            ps.setString(0, userName);
            final List<Object[]> rs = ps.list();
            for (final Object[] element : rs)
                role = element[0].toString();
        } catch (final Exception ex) {
            LOGGER.error("Task Failed Error" + ex.getMessage(), ex);
            throw new TaskFailedException();
        }
        return role;
    }

    public int getId() throws TaskFailedException {
        final String query = "select id_user from EG_USER where user_name=? ";
        int userId = 0;
        try {
            final Query ps = persistenceService.getSession().createSQLQuery(query);
            ps.setString(0, userName);
            final List<Object[]> rs = ps.list();
            for (final Object[] element : rs)
                userId = Integer.parseInt(element[0].toString());
        } catch (final Exception ex) {
            LOGGER.error("EXP in getId" + ex.getMessage());
            throw new TaskFailedException();
        }
        return userId;
    }

}