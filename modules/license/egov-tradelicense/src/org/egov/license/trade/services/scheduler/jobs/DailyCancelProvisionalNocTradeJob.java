/*
 * @(#)DailyCancelProvisionalNocTradeJob.java 3.0, 29 Jul, 2013 5:07:08 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.trade.services.scheduler.jobs;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.infstr.auditing.service.AuditEventService;
import org.egov.infstr.scheduler.quartz.AbstractQuartzJob;
import org.egov.infstr.services.PersistenceService;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.LicenseStatus;
import org.egov.license.domain.entity.LicenseStatusValues;
import org.egov.license.trade.domain.entity.TradeLicense;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class DailyCancelProvisionalNocTradeJob extends AbstractQuartzJob {

	private static final long serialVersionUID = 1L;
	private PersistenceService<TradeLicense, Long> tradeLicenseService;
	private PersistenceService persistenceService;
	private AuditEventService auditEventService;

	@Override
	public void executeJob() {
		for (final TradeLicense license : (List<TradeLicense>) this.getProvisionalNocTradeLicense().list()) {
			cancelLicense(license);
			this.tradeLicenseService.update(license);
		}
	}

	private Criteria getProvisionalNocTradeLicense() {
		Criteria criteria = null;
		Date nocExpireDate;
		final Calendar instance = Calendar.getInstance();
		final Date newDate = new Date();
		instance.setTime(newDate);
		instance.add(Calendar.MONTH, -12);
		nocExpireDate = instance.getTime();
		criteria = this.tradeLicenseService.getSession().createCriteria(TradeLicense.class);
		criteria.createAlias("tradeName", "trdname");
		criteria.add(Restrictions.eq("trdname.nocApplicable", true));
		criteria.add(Restrictions.isNull("isCertificateGenerated"));
		criteria.add(Restrictions.le("dateOfCreation", nocExpireDate));
		criteria.createAlias("status", "sts");
		criteria.add(Restrictions.eq("sts.statusCode", "ACT"));
		return criteria;
	}

	private void cancelLicense(final License license) {
		final String reasonforCancellation = "Auto Cancellation of Provisional NOC Licenses for which Certificate is not generated";
		license.setActive(false);
		final LicenseStatus licStatus = getLicenseStatusbyCode("CAN");
		license.setStatus(licStatus);
		final LicenseStatusValues licenseStatusValues = getCurrentStatus(license);
		if (licenseStatusValues != null) {
			licenseStatusValues.setActive(false);
		}
		final LicenseStatusValues newLicenseStatusValues = new LicenseStatusValues();
		newLicenseStatusValues.setLicense(license);
		newLicenseStatusValues.setLicenseStatus(licStatus);
		newLicenseStatusValues.setReason(licStatus.getID());
		newLicenseStatusValues.setRemarks(reasonforCancellation);
		newLicenseStatusValues.setActive(true);
		newLicenseStatusValues.setPreviousStatusVal(licenseStatusValues);
		license.addLicenseStatusValuesSet(newLicenseStatusValues);
		doAuditing("Cancel License", reasonforCancellation, license);
	}

	protected void doAuditing(final String action, final String details, final License license) {
		final AuditEvent auditEvent = new AuditEvent(AuditModule.TL, AuditEntity.TL_LIC, action, license.getLicenseNumber(), details);
		auditEvent.setPkId(license.getId());
		this.auditEventService.createAuditEvent(auditEvent, license.getClass());
	}

	private LicenseStatus getLicenseStatusbyCode(final String statusCode) {
		return (LicenseStatus) this.persistenceService.find("FROM org.egov.license.domain.entity.LicenseStatus where statusCode=?", statusCode);
	}

	public LicenseStatusValues getCurrentStatus(final License license) {
		return (LicenseStatusValues) this.persistenceService.find("from org.egov.license.domain.entity.LicenseStatusValues  where license=? and active=true", license);
	}

	public void setTradeLicenseService(final PersistenceService<TradeLicense, Long> tradeLicenseService) {
		this.tradeLicenseService = tradeLicenseService;
	}

	public void setPersistenceService(final PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setAuditEventService(final AuditEventService auditEventService) {
		this.auditEventService = auditEventService;
	}
}
