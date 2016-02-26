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
package org.egov.tl.web.actions.tradescheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.scheduler.quartz.AbstractQuartzJob;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseStatus;
import org.egov.tl.entity.LicenseStatusValues;
import org.egov.tl.entity.TradeLicense;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class DailyCancelProvisionalNocTradeJob extends AbstractQuartzJob {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(DailyCancelProvisionalNocTradeJob.class);

    private PersistenceService persistenceService;

    // private AuditEventService auditEventService;

    @Override
    public void executeJob() {
        LOGGER.info("Started scheduling now " + DateUtils.now() + "-->Thread ID = " + Thread.currentThread().getId()
                + " isAlive = " + Thread.currentThread().isAlive());

        try {
            final List results;
            final Criteria criteria = getProvisionalNocTradeLicense();
            results = criteria.list();
            for (final Object lic : results) {
                final TradeLicense license = (TradeLicense) lic;
                cancelLicense(license);
                persistenceService.update(license);
            }
        } catch (final Exception e) {
            LOGGER.error("Error while scheduling Cancellation of Provisional NOC Licenses", e);
            throw new ApplicationRuntimeException("Error while scheduling Cancellation of Provisional NOC Licenses", e);
        }

    }

    // Code Reviewed by Satyam, Suggested to remove date iteration and add the date comparison in the Query, Changes done
    // accordingly
    private Criteria getProvisionalNocTradeLicense() {
        Criteria criteria = null;
        Date nocExpireDate;
        final Calendar instance = Calendar.getInstance();
        final Date newDate = new Date();
        instance.setTime(newDate);
        instance.add(Calendar.MONTH, -12);
        nocExpireDate = instance.getTime();
        criteria = persistenceService.getSession().createCriteria(TradeLicense.class);
        criteria.createAlias("tradeName", "trdname");
        criteria.add(Restrictions.eq("trdname.nocApplicable", true));
        criteria.add(Restrictions.isNull("isCertificateGenerated"));
        criteria.add(Restrictions.le("commencementDate", nocExpireDate));
        criteria.createAlias("status", "sts");
        criteria.add(Restrictions.eq("sts.statusCode", "ACT"));
        return criteria;
    }

    private void cancelLicense(final License license) {
        final String reasonforCancellation = "Auto Cancellation of Provisional NOC Licenses for which Certificate is not generated";

        LOGGER.debug("Cancel Hospital License Elements are:<<<<<<<<<<>>>>>>>>>>>>>:" + toString());
        license.setActive(false);
        final LicenseStatus licStatus = getLicenseStatusbyCode("CAN");
        license.setStatus(licStatus);
        final LicenseStatusValues licenseStatusValues = getCurrentStatus(license);
        if (licenseStatusValues != null)
            licenseStatusValues.setActive(false);
        final LicenseStatusValues newLicenseStatusValues = new LicenseStatusValues();
        newLicenseStatusValues.setLicense(license);
        newLicenseStatusValues.setLicenseStatus(licStatus);
        newLicenseStatusValues.setReason(licStatus.getID());
        newLicenseStatusValues.setRemarks(reasonforCancellation);
        newLicenseStatusValues.setActive(true);
        newLicenseStatusValues.setPreviousStatusVal(licenseStatusValues);
        license.addLicenseStatusValuesSet(newLicenseStatusValues);
        // doAuditing("Cancel License",reasonforCancellation, license);
    }

    /*
     * protected void doAuditing(String action, String details, License license) { final AuditEvent auditEvent = new
     * AuditEvent(AuditModule.TL, AuditEntity.TL_LIC, action, license.getLicenseNumber(), details);
     * auditEvent.setPkId(license.getId()); this.auditEventService.createAuditEvent(auditEvent, license.getClass()); }
     */

    private LicenseStatus getLicenseStatusbyCode(final String statusCode) {
        return (LicenseStatus) persistenceService.find(
                "FROM org.egov.tl.entity.LicenseStatus where statusCode=?", statusCode);
    }

    public LicenseStatusValues getCurrentStatus(final License license) {
        return (LicenseStatusValues) persistenceService.find(
                "from org.egov.tl.entity.LicenseStatusValues  where license=? and active=true", license);
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    /*
     * public void setAuditEventService(AuditEventService auditEventService) { this.auditEventService = auditEventService; }
     */

}
