package org.egov.mrs.domain.service;

import static org.egov.mrs.application.MarriageConstants.MODULE_NAME;
import static org.egov.mrs.application.MarriageConstants.SENDEMAILFROOMMARRIAGEMODULE;
import static org.egov.mrs.application.MarriageConstants.SENDSMSFROOMMARRIAGEMODULE;

import java.util.List;

import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.messaging.MessagingService;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class MarriageSmsAndEmailService {
	private final String MSG_KEY_SMS_REGISTRATION_NEW = "msg.newregistration.sms";
	private final String MSG_KEY_SMS_REGISTRATION_REJECTION = "msg.rejectregistration.sms";
	private final String MSG_KEY_SMS_REGISTRATION_REGISTERED = "msg.registration.registered.sms";
	private final String MSG_KEY_EMAIL_REGISTRATION_NEW_EMAIL = "msg.newregistration.mail";
	private final String MSG_KEY_EMAIL_REGISTRATION_NEW_SUBJECT = "msg.newregistration.mail.subject";
	private final String MSG_KEY_EMAIL_REGISTRATION_REJECTION_EMAIL = "msg.rejectionregistration.mail";
	private final String MSG_KEY_EMAIL_REGISTRATION_REJECTION_SUBJECT = "msg.rejectionregistration.mail.subject";
	private final String MSG_KEY_EMAIL_REGISTRATION_REGISTERED = "msg.registration.registered.mail";
	private final String MSG_KEY_EMAIL_REGISTRATION_REGISTERED_SUBJECT = "msg.registration.registered.mail.subject";
	@Autowired
	private MessagingService messagingService;

	@Autowired
	@Qualifier("parentMessageSource")
	private MessageSource mrsMessageSource;

	@Autowired
	private AppConfigValueService appConfigValuesService;

	public void sendSMS(final MarriageRegistration registration, String status) {
		String msgKey = MSG_KEY_SMS_REGISTRATION_NEW;

		if (isSmsEnabled() && registration.getApplicationNo()!=null) {

			if (registration.getStatus() != null && registration.getStatus().getCode()
					.equalsIgnoreCase(MarriageRegistration.RegistrationStatus.REJECTED.toString()))
				msgKey = MSG_KEY_SMS_REGISTRATION_REJECTION;
			else if (registration.getStatus() != null && registration.getStatus().getCode()
					.equalsIgnoreCase(MarriageRegistration.RegistrationStatus.REGISTERED.toString()))
				msgKey = MSG_KEY_SMS_REGISTRATION_REGISTERED;

			final String message = buildEmailMessage(registration, msgKey);
			if (registration.getHusband() != null && registration.getHusband().getContactInfo() != null
					&& registration.getHusband().getContactInfo().getMobileNo() != null)
				messagingService.sendSMS(registration.getHusband().getContactInfo().getMobileNo(), message);
			if (registration.getWife() != null && registration.getWife().getContactInfo() != null
					&& registration.getWife().getContactInfo().getMobileNo() != null)
				messagingService.sendSMS(registration.getWife().getContactInfo().getMobileNo(), message);
		}
	}

	public void sendEmail(final MarriageRegistration registration, String status) {
		String msgKeyMail = MSG_KEY_EMAIL_REGISTRATION_NEW_EMAIL;
		String msgKeyMailSubject = MSG_KEY_EMAIL_REGISTRATION_NEW_SUBJECT;

		if (isEmailEnabled() && registration.getApplicationNo()!=null) {

			if (registration.getStatus() != null && registration.getStatus().getCode()
					.equalsIgnoreCase(MarriageRegistration.RegistrationStatus.REJECTED.toString())) {
				msgKeyMail = MSG_KEY_EMAIL_REGISTRATION_REJECTION_EMAIL;
				msgKeyMailSubject = MSG_KEY_EMAIL_REGISTRATION_REJECTION_SUBJECT;
			} else if (registration.getStatus() != null && registration.getStatus().getCode()
					.equalsIgnoreCase(MarriageRegistration.RegistrationStatus.REGISTERED.toString())) {
				msgKeyMail = MSG_KEY_EMAIL_REGISTRATION_REGISTERED;
				msgKeyMailSubject = MSG_KEY_EMAIL_REGISTRATION_REGISTERED_SUBJECT;
			}
			final String message = buildEmailMessage(registration, msgKeyMail);

			final String subject = mrsMessageSource.getMessage(msgKeyMailSubject, null, null);
			if (registration.getHusband() != null && registration.getHusband().getContactInfo() != null
					&& registration.getHusband().getContactInfo().getEmail() != null)
				messagingService.sendEmail(registration.getHusband().getContactInfo().getEmail(), subject, message);
			if (registration.getWife() != null && registration.getWife().getContactInfo() != null
					&& registration.getWife().getContactInfo().getEmail() != null)
				messagingService.sendEmail(registration.getWife().getContactInfo().getEmail(), subject, message);
		}
	}

	private String buildEmailMessage(final MarriageRegistration registration, String msgKeyMail) {
		final String message = mrsMessageSource.getMessage(msgKeyMail,
				new String[] { registration.getHusband().getFullName(), registration.getWife().getFullName(),
						registration.getRegistrationNo() },
				null);
		return message;
	}

	public Boolean isSmsEnabled() {

		return getAppConfigValueByPassingModuleAndType(MODULE_NAME, SENDSMSFROOMMARRIAGEMODULE);
	}

	private Boolean getAppConfigValueByPassingModuleAndType(String moduleName, String sendsmsoremail) {
		final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(moduleName,
				sendsmsoremail);

		return "YES".equalsIgnoreCase(
				appConfigValue != null && appConfigValue.size() > 0 ? appConfigValue.get(0).getValue() : "NO");
	}

	public Boolean isEmailEnabled() {

		return getAppConfigValueByPassingModuleAndType(MODULE_NAME, SENDEMAILFROOMMARRIAGEMODULE);

	}

}