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
 * Created on Feb 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.GLEngine;

import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author siddhu
 *
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 * This class caches employee data for a transaction and clear at the end of each transaction
 *
 */
@Service
public class RequiredValidator {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;

    final static Logger LOGGER = Logger.getLogger(RequiredValidator.class);
    static HashMap employeeMap = null;
    static int empDetailId = 0;

    /**
     *
     */
    public static void clearEmployeeMap()
    {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("...................................................................................Clearing the employeeMap");
        if (employeeMap != null)
            employeeMap = null;
        empDetailId = 0;
    }

    public boolean isEmployee(final int detailId)
    {
        if (empDetailId == 0)
        {
            final List list = persistenceService.getSession()
                    .createQuery("select id From Accountdetailtype where name='EMPLOYEE'")
                    .list();
            if (list != null && list.size() > 0)
                empDetailId = (Integer) list.get(0);
        }
        if (empDetailId == detailId)
            return true;
        else
            return false;
    }

    public boolean validateKey(final int detailId, final String keyToValidate) throws TaskFailedException {

        final String sql = "select detailKey as \"detailKey\" ,detailName as \"detailName\"," +
                "groupID as \"groupID\",ID as \"ID\" from accountdetailkey where detailTypeId="
                + String.valueOf(detailId) + " and detailKey = " + String.valueOf(keyToValidate);
        final List list = persistenceService.getSession().createSQLQuery(sql).list();
        if (list != null && list.size() > 0)
            return true;
        else
            return false;
    }
}