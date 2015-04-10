package org.egov.infra.utils;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.utils.EGovConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmailUtils {
	
	private static Logger LOG = Logger.getLogger(EmailUtils.class);
	
	public static final String MAILSENDER = "mailSender";
	 
	@Autowired
    private JavaMailSenderImpl mailSender;
    
    @Autowired
	private SimpleMailMessage mailMessage;
    
    @Autowired
    private AppConfigValuesDAO appConfigValuesDAO;
    
	public boolean sendMail(final String toEmail, final String mailBody,
			final String subject) {
		boolean isSent = false;

		try {
			this.mailSender.setPort(Integer.valueOf(EGovConfig.getProperty("port", "", MAILSENDER)));
			this.mailSender.setHost(EGovConfig.getProperty("host", "", MAILSENDER));
			this.mailSender.setProtocol(EGovConfig.getProperty("protocol", "", MAILSENDER));

			final AppConfigValues mailUser = appConfigValuesDAO.getConfigValuesByModuleAndKey("egi", "mailSenderUserName").get(0);
			final AppConfigValues mailPwd = appConfigValuesDAO.getConfigValuesByModuleAndKey("egi", "mailSenderPassword").get(0);
			this.mailSender.setUsername(mailUser.getValue());
			this.mailSender.setPassword(mailPwd.getValue());

			final Properties mailProperties = new Properties();
			mailProperties.setProperty("mail.smtps.auth", EGovConfig.getProperty("mail_smtps_auth", "", MAILSENDER));
			mailProperties.setProperty("mail.smtps.starttls.enable", EGovConfig.getProperty("mail_smtps_starttls_enable", "", MAILSENDER));
			mailProperties.setProperty("mail.smtps.debug", EGovConfig.getProperty("mail_smtps_debug", "", MAILSENDER));
			this.mailSender.setJavaMailProperties(mailProperties);
			this.mailMessage.setTo(toEmail);
			this.mailMessage.setSubject(subject);
			this.mailMessage.setText(mailBody);
			this.mailSender.send(this.mailMessage);
			isSent = true;
		} catch (final Exception e) {
			LOG.error("Error occurred while trying to send mail", e);
		}
		return isSent;
	} 	
}
