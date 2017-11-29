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

package org.egov.infra.scheduler.quartz;

import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.CityPreferences;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.scheduler.GenericJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

/**
 * An abstract base class wrapper for {@link QuartzJobBean} and implements {@link GenericJob}. A class which extends this will be
 * eligible for doing Quartz Jobs. Those classes required Statefulness (Threadsafety) so need to annotate class
 * with @DisallowConcurrentExecution. This class also wrap up wiring of some of the common settings and beans.
 **/
public abstract class AbstractQuartzJob extends QuartzJobBean implements GenericJob {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractQuartzJob.class);

    @Resource(name = "cities")
    private transient List<String> cities;

    @Autowired
    private transient CityService cityService;

    @Autowired
    private transient UserService userService;

    private String userName;

    private String moduleName;

    private boolean cityDataRequired;

    @Override
    protected void executeInternal(JobExecutionContext jobCtx) throws JobExecutionException {
        try {
            MDC.put("appname", String.format("%s-%s", moduleName, jobCtx.getJobDetail().getKey().getName()));
            for (String tenant : this.cities) {
                MDC.put("ulbcode", tenant);

                this.prepareThreadLocal(tenant);
                this.executeJob();
            }
        } catch (Exception ex) {
            LOGGER.error("Unable to complete execution Scheduler ", ex);
            throw new JobExecutionException("Unable to execute batch job Scheduler", ex, false);
        } finally {
            ApplicationThreadLocals.clearValues();
            MDC.clear();
        }
    }

    public void setModuleName(final String moduleName) {
        this.moduleName = moduleName;
    }

    public void setUserName(String userName) {
        this.userName = defaultIfBlank(userName, "system");
    }

    public void setCityDataRequired(boolean cityDataRequired) {
        this.cityDataRequired = cityDataRequired;
    }

    private void prepareThreadLocal(String tenant) {
        ApplicationThreadLocals.setTenantID(tenant);
        ApplicationThreadLocals.setUserId(this.userService.getUserByUsername(this.userName).getId());
        if (cityDataRequired) {
            City city = this.cityService.findAll().get(0);
            ApplicationThreadLocals.setCityCode(city.getCode());
            ApplicationThreadLocals.setCityName(city.getName());
            CityPreferences cityPreferences = city.getPreferences();
            if (cityPreferences != null)
                ApplicationThreadLocals.setMunicipalityName(cityPreferences.getMunicipalityName());
            else
                LOGGER.warn("City preferences not set for {}", city.getName());
            ApplicationThreadLocals.setDomainName(city.getDomainURL());
        }
    }
}
