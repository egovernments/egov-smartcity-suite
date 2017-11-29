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
package com.exilant.exility.updateservice;

import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigInteger;
import java.util.List;

public class PrimaryKeyGenerator
{
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;

    private static final Logger LOGGER = Logger.getLogger(PrimaryKeyGenerator.class);
    private static PrimaryKeyGenerator singletonInstance;

    // private static HashMap nextKeys = new HashMap();
    static
    {
        singletonInstance = new PrimaryKeyGenerator();
    }

    public static PrimaryKeyGenerator getKeyGenerator() {
        // if(singletonInstance == null)singletonInstance = new PrimaryKeyGenerator();
        return singletonInstance;
    }

    private PrimaryKeyGenerator()
    {
        super();
    }

    public static long getNextKey(final String tableName)
    {
        long key = 0;
        final String sql = "select nextval('seq_" + tableName + "')";
        try
        {
            final Query pst = null;
                    //persistenceService.getSession().createSQLQuery(sql);
            final List<BigInteger> rs = pst.list();
            key = rs != null ? rs.get(0).longValue() : 0l;
            if (rs == null || rs.size() == 0)
                throw new Exception();
        } catch (final Exception e)
        {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Exp=" + e.getMessage());
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Error getting value from sequence " + e.toString());
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("PK for " + tableName + " is " + key);
        return key;
    }

}