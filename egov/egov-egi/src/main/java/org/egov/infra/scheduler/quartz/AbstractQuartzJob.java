/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infra.scheduler.quartz;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.scheduler.GenericJob;
import org.egov.infra.utils.EgovThreadLocals;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * An abstract base class wrapper for {@link QuartzJobBean} and implements {@link GenericJob}. A class which extends this will be
 * eligible for doing Quartz Jobs. Those classes required Statefulness (Threadsafety) so need to annotate class
 * with @DisallowConcurrentExecution. This class also wrap up wiring of some of the common settings and beans.
 **/
public abstract class AbstractQuartzJob extends QuartzJobBean implements GenericJob {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractQuartzJob.class);
    private boolean isTransactional;
    private String userName;

    @Resource(name = "tenants")
    protected List<String> tenants;

    @Autowired
    private UserService userService;

    /**
     * This method will wrap up the Transaction (if isTransactional set to true) and call the executeJob implementation on
     * individual job class.
     **/
    @Override
    protected void executeInternal(final JobExecutionContext jobCtx) throws JobExecutionException {
        try {
            if (isTransactional)
                for (final String tenant : tenants) {
                    MDC.put("ulbcode", tenant);
                    setTractionalSupport(tenant);
                    setUserInThreadLocal();
                    executeJob();
                }
            else
                executeJob();

        } catch (final Exception ex) {
            LOGGER.error("Unable to complete execution Scheduler ", ex);
            throw new JobExecutionException("Unable to execute batch job Scheduler", ex, false);
        } finally {
            clearTheadLocal();
            MDC.remove("ulbcode");
        }
    }

    public void setUserName(final String userName) {
        if (StringUtils.isBlank(userName))
            this.userName = "egovernments";
        else
            this.userName = userName;
    }

    public void setTransactional(final boolean isTransactional) {
        this.isTransactional = isTransactional;
    }

    protected void setTractionalSupport(final String tenant) {
        EgovThreadLocals.setTenantID(tenant);
    }

    protected void setUserInThreadLocal() {
        EgovThreadLocals.setUserId(userService.getUserByUsername(userName).getId());
    }

    protected void clearTheadLocal() {
        EgovThreadLocals.setTenantID(null);
        EgovThreadLocals.setUserId(null);
    }
}
