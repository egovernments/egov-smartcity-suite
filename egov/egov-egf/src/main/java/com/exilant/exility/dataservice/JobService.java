/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package com.exilant.exility.dataservice;

import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.common.XMLLoader;
import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;

import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;

public class JobService {
    private static final Logger LOGGER = Logger.getLogger(JobService.class);
    static JobService singletonInstance;
    // Job decared as Type help to XML loader to load jobs into job
    protected Job job;
    public HashMap jobs;

    public static JobService getInstance() {
        if (singletonInstance == null)
            singletonInstance = new JobService();
        return singletonInstance;
    }

    // singleton pattern
    private JobService() {
        super();
        final XMLLoader xmlLoader = new XMLLoader();
        final URL url = EGovConfig.class.getClassLoader().getResource("config/resource/Jobs.xml");
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("url in JobService=================="+url);
        xmlLoader.load(url.toString(), this);
    }

    public void doService(final DataCollection dc, final Connection con) throws TaskFailedException {
        final String serviceID = dc.getValue("serviceID");
        if (serviceID == null)
            dc.addMessage("exilNoServiceID", serviceID);
        else {
            final Job aJob = (Job) jobs.get(serviceID);// Get based on the Id from the hashmap.
            /*
             * if(aJob.previliged(dc.getValue("rolesToValidate"))){ dc.addMessage("exilRPError","User Has No Previlige"); throw
             * new TaskFailedException(); }
             */
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(aJob);
            if (aJob == null)
                dc.addMessage("exilNoJobDefinition", serviceID);
            else if (aJob.hasAccess(dc, con))
                aJob.execute(dc, con);
            else
                dc.addMessage("exilNoAccess", serviceID);
        }
    }
}
