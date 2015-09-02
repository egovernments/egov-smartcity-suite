package org.egov.license.trade.tradescheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.EGOVRuntimeException;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.infstr.auditing.service.AuditEventService;
import org.egov.infstr.scheduler.quartz.AbstractQuartzJob;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.LicenseStatus;
import org.egov.license.domain.entity.LicenseStatusValues;
import org.egov.license.trade.domain.entity.TradeLicense;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;


public class DailyCancelProvisionalNocTradeJob extends AbstractQuartzJob {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(DailyCancelProvisionalNocTradeJob.class);

	private PersistenceService persistenceService; 
	private AuditEventService auditEventService;
	
	@Override
	public void executeJob() {
		LOGGER.info("Started scheduling now " + DateUtils.now()	+ "-->Thread ID = " + Thread.currentThread().getId() + " isAlive = " + Thread.currentThread().isAlive());
		
		try{
			final List results;
			final Criteria criteria = this.getProvisionalNocTradeLicense();
			results = criteria.list();
			for (Object lic : results) {
				TradeLicense license = (TradeLicense) lic;
				cancelLicense(license);
				persistenceService.update(license);
			}
		} catch (Exception e) {
			LOGGER.error("Error while scheduling Cancellation of Provisional NOC Licenses",e);
			throw new EGOVRuntimeException("Error while scheduling Cancellation of Provisional NOC Licenses",e);
		} 
		
	}
	
	//Code Reviewed by Satyam, Suggested to remove date iteration and add the date comparison in the Query, Changes done accordingly
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
		criteria.add(Restrictions.le("dateOfCreation", nocExpireDate));
		criteria.createAlias("status", "sts");
		criteria.add(Restrictions.eq("sts.statusCode", "ACT"));
		return criteria;
	}

	private void cancelLicense(License license){
		String reasonforCancellation = "Auto Cancellation of Provisional NOC Licenses for which Certificate is not generated"; 

		LOGGER.debug("Cancel Hospital License Elements are:<<<<<<<<<<>>>>>>>>>>>>>:"+ this.toString());
		license.setActive(false);
		LicenseStatus licStatus = getLicenseStatusbyCode("CAN");
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
		doAuditing("Cancel License",reasonforCancellation, license);
	}
	
	protected void doAuditing(String action, String details, License license) {
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

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setAuditEventService(AuditEventService auditEventService) {
		this.auditEventService = auditEventService;
	}

}
