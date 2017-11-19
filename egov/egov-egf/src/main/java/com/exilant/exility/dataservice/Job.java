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
package com.exilant.exility.dataservice;

import com.exilant.eGov.src.domain.User;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

import java.sql.Connection;

/*
 * This class should not be called by programmers directly. call to this has to
 * come through JobService. (modifier public is provided for XML Loader)
 *
 * A Job consists of tasks. The tasks are defined in resource/jobs.xml
 * (These are loaded and cached by JobService for performance)
 *
 * This class executes each tasks.
 */
public class Job {

    // fields loaded by XML
    public String id;
    public JobStep[] jobSteps;
    // public String accessRoles;
    // contains the permissible roles for the job
    public String roles;

    public Job() {
        super();
    }

    public void execute(final DataCollection dc, final Connection con) throws TaskFailedException {
        for (final JobStep jobStep : jobSteps)
            jobStep.execute(dc, con);
    }

    /*
     * public boolean previliged(String roleIDs){ String[] rolesToValidate=roleIDs.split(","); String[]
     * validRoles=accessRoles.split(","); for(int i=0;i<rolesToValidate.length;i++){ for(int j=0;j<validRoles.length;j++){
     * if(rolesToValidate[i].equalsIgnoreCase(validRoles[j])){ return true; } } } return false; }
     */

    public boolean hasAccess(final DataCollection dc, final Connection con) throws TaskFailedException {
        String currentUserName;
        String allowedRoles;
        String userRole;
        // Integer userId;
        boolean bAccess = false;
        currentUserName = dc.getValue("current_loginName");
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("######currentUserId: " + currentUserName);
        allowedRoles = roles;
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("######allowedRoles: " + allowedRoles);
        try {
            final User aUser = new User(currentUserName);
            userRole = aUser.getRole(con);
            if (allowedRoles != null && allowedRoles.equals("Everyone")
                    || allowedRoles != null && allowedRoles.indexOf(userRole) > 0)
                bAccess = true;
        } catch (final TaskFailedException te)
        {
            dc.addMessage("exilRPError", "Access not defined");
            throw new TaskFailedException();
        }
        return bAccess;
    }

}
