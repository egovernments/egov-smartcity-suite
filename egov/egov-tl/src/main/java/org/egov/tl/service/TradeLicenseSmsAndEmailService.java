package org.egov.tl.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.egov.infra.messaging.MessagingService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.tl.entity.License;
import org.egov.tl.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.support.ResourceBundleMessageSource;

@Service
@Transactional(readOnly = true)
public class TradeLicenseSmsAndEmailService 
{
    @Autowired
    private MessagingService messagingService;
    
    @Autowired
    private ResourceBundleMessageSource messageSource;
    
    public void sendSMSOnLicense(final String mobileNumber, final String smsBody) {
        messagingService.sendSMS(mobileNumber, smsBody);
    }

    public void sendEmailOnLicense(final String email, final String emailBody, final String emailSubject) {
        messagingService.sendEmail(email, emailSubject, emailBody);
    }
    
    public String getMunicipalityName() {
        return EgovThreadLocals.getMunicipalityName();
    }
    public void sendSmsAndEmail(final License license, final String workFlowAction) {
        String mobileNumber=(license.getLicensee()!=null && license.getLicensee().getMobilePhoneNumber()!=null ? license.getLicensee().getMobilePhoneNumber():null);
        String email=(license.getLicensee()!=null && license.getLicensee().getEmailId()!=null ? license.getLicensee().getEmailId():null);
        getSmsAndEmailForNewTradeLicense(license,workFlowAction,email,mobileNumber);
    }
   
    public void getSmsAndEmailForNewTradeLicense(final License license, String workFlowAction,final String email,
            final String mobileNumber) {
        String smsMsg = null;
        String emailBody = "";
        String emailSubject = "";
        final Locale locale = LocaleContextHolder.getLocale();
        String [] strarr=getMunicipalityName().split(" ");
        String cityname=strarr[0];
        String smsCode="";
        String emailCode="";
        if ((license.getState().getHistory().isEmpty()
                && Constants.STATUS_ACKNOLEDGED.equalsIgnoreCase(license.getStatus().getStatusCode())) ||(license.getState().getValue().contains(Constants.WF_STATE_SANITORY_INSPECTOR_APPROVAL_PENDING))) {
            if(license.getLicenseAppType()!=null && license.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE))
            {
                smsCode="msg.renewTradeLicensecreator.sms"; 
                emailCode="msg.renewTradeLicensecreate.email.body";
            }
            else{
                smsCode="msg.newTradeLicensecreator.sms"; 
                emailCode="msg.newTradeLicensecreate.email.body";
            }
            smsMsg =  messageSource.getMessage(
                    smsCode,
                    new String[] { license.getLicensee().getApplicantName(), license.getApplicationNumber(),
                           getMunicipalityName() }, null); 
            emailBody = messageSource.getMessage(
                    emailCode,
                    new String[] { license.getLicensee().getApplicantName(), license.getApplicationNumber(),
                           getMunicipalityName() }, null);
            emailSubject= messageSource.getMessage("msg.newTradeLicensecreate.email.subject", new String[] { license.getApplicationNumber() }, locale);
         } 
        else if (workFlowAction.equals(Constants.BUTTONAPPROVE) && Constants.STATUS_UNDERWORKFLOW.equalsIgnoreCase(license.getStatus()
                .getStatusCode())) {
            BigDecimal demAmt=(license.getCurrentDemand()!=null ?license.getCurrentDemand().getBaseDemand():BigDecimal.ZERO);
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            if(license.getLicenseAppType()!=null && license.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE))
            {
                emailCode="msg.renewTradeLicenseapproval.email.body";
            }else{
                emailCode="msg.newTradeLicenseapproval.email.body";
            }
            smsMsg =  messageSource.getMessage(
                    "msg.newTradeLicenseapproval.sms",
                    new String[] { license.getLicensee().getApplicantName(), license.getLicenseNumber(),
                            demAmt.toString(), formatter.format(license.getApplicationDate()),cityname,getMunicipalityName() }, null);
            emailBody = messageSource.getMessage(
                    emailCode,
                    new String[] { license.getLicensee().getApplicantName(), license.getLicenseNumber(),
                            demAmt.toString(), formatter.format(license.getApplicationDate()),cityname,getMunicipalityName() }, null);
            emailSubject= messageSource.getMessage("msg.newTradeLicenseApproval.email.subject", new String[] { license.getLicenseNumber()}, locale);
        } 
        else if (Constants.STATUS_CANCELLED.equalsIgnoreCase(license.getStatus()
                .getStatusCode())) {
            smsMsg =  messageSource.getMessage(
                    "msg.newTradeLicensecancelled.sms",
                    new String[] { license.getLicensee().getApplicantName(), license.getApplicationNumber(),
                            cityname, getMunicipalityName() }, null);
            emailBody = messageSource.getMessage(
                    "msg.newTradeLicensecancelled.email.body",
                    new String[] { license.getLicensee().getApplicantName(), license.getApplicationNumber(),
                            cityname, getMunicipalityName() }, null);
            emailSubject= messageSource.getMessage("msg.newTradeLicensecancelled.email.subject", new String[] { license.getApplicationNumber() }, locale);
        } 
        if (mobileNumber != null && smsMsg != null)
            sendSMSOnLicense(mobileNumber, smsMsg);
        if (email != null && emailSubject !=null && !"".equals(emailSubject)&& emailBody != null && !"".equals(emailBody))
           sendEmailOnLicense(email, emailBody, emailSubject);
    }
    
  
}
