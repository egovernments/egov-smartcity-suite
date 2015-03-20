package org.egov.infra.events.processing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.events.entity.schema.EmailType;
import org.egov.infra.events.entity.schema.Response;
import org.egov.infra.events.entity.schema.SMSType;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.mail.Email;
import org.egov.infstr.mail.Email.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ResponseHandlerImpl implements ResponseHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseHandlerImpl.class);

    private GenericHibernateDaoFactory genericHibernateDaoFactory;

    public ResponseHandlerImpl() {
    }

    public ResponseHandlerImpl(final GenericHibernateDaoFactory genericHibernateDaoFactory) {
        this.genericHibernateDaoFactory = genericHibernateDaoFactory;
    }

    @Override
    public void respond(final Response r) {
        /*
         * See what is the type of the response and call its respond() method.
         */
        if (r.getEmail() != null && r.getEmail().size() != 0) {
            if (LOG.isDebugEnabled())
                LOG.debug("::::Response Type Email::::");
            for (int i = 0; i < r.getEmail().size(); i++)
                respondViaEmail(r.getEmail().get(i));
        }

        if (r.getSms() != null && r.getSms().size() != 0) {
            if (LOG.isDebugEnabled())
                LOG.debug("::::Response Type Sms::::");
            for (int i = 0; i < r.getSms().size(); i++)
                respondViaSms(r.getSms().get(i));
        }
    }

    /**
     * @param mail
     */
    private void respondViaEmail(final EmailType mail) {
        if (mail.getTo() != null && !mail.getTo().isEmpty()) {
            final Builder builder = new Email.Builder(getAddressList(mail.getTo()), mail.getBody());

            if (mail.getCc() != null && mail.getCc().size() != 0)
                builder.addCc(getAddressList(mail.getCc()));

            if (mail.getBcc() != null && mail.getBcc().size() != 0)
                builder.addBcc(getAddressList(mail.getBcc()));

            if (mail.getSubject() != null && mail.getSubject().length() != 0)
                builder.subject(mail.getSubject());

            final Email email = builder.build(genericHibernateDaoFactory);
            email.send();
            if (LOG.isDebugEnabled())
                LOG.debug("::::Email Sent Successfully::::");
        }
    }

    /**
     * @param recipientList
     * @return
     */
    private List<String> getAddressList(final List<String> recipientList) {
        String[] recipientAddress = null;
        final List<String> mailAddress = new ArrayList<String>();
        for (int i = 0; i < recipientList.size(); i++) {
            recipientAddress = recipientList.get(i).toString().split(",");
            for (final String recipientAddres : recipientAddress)
                mailAddress.add(recipientAddres);
        }
        return mailAddress;
    }

    /**
     * @param sms
     */
    private void respondViaSms(final SMSType sms) {
        if (sms.getPhonenumber() != null && !sms.getPhonenumber().isEmpty()) {
            final AppConfigValuesDAO appConfValDao = genericHibernateDaoFactory.getAppConfigValuesDAO();
            /*
             * Reading Sender Information : Name / PhoneNumber from Appconfig
             */
            final String sender = appConfValDao.getConfigValuesByModuleAndKey("egi", "smsSender").get(0).getValue();

            sendSMS(sms.getMessage(), sms.getPhonenumber(), sender);
            if (LOG.isDebugEnabled())
                LOG.debug("::::Sms Sent Successfully::::");
        }

    }

    /**
     * @param text
     * @param phoneNumber
     * @param sender
     */
    private void sendSMS(final String text, final List<String> phoneNumber, final String sender) {
        final String errorCodes[] = { "0x200", "0x201", "0x202", "0x203", "0x204", "0x205", "0x206", "0x207", "0x208",
                "0x209", "0x210", "0x211", "0x212", "0x213" };
        final String errorMessages[] = {
                "Invalid Username or Password",
                "Account Suspended due to some reason",
                "Invalid Source Address/Sender Id. As per GSM standard the sender ID should be within 11 characters.",
                "Message Length Exceeded(more than 160 chars) if concat is set to 0",
                "Message Length Exceeded(more than 459 chars) if concat is set to 1",
                "DLR URL is not set",
                "Only the subscribed service type can be accessed so make sure that the service type you are trying to connect with.",
                "Invalid Source IP. Kindly check if the IP is responding.", "Account Deactivated/Expired.",
                "Invalid Message Length (less than 160 chars) if concat is set to 1", "Invalid Parameter values",
                "Invalid Message Length (more than 280 chars)", "Invalid Message Length", "Invalid Destination number" };

        try {

            final AppConfigValuesDAO appConfValDao = genericHibernateDaoFactory.getAppConfigValuesDAO();
            /*
             * Reading SMS ServiceProvider URL from Appconfig
             */
            final StringBuffer urlStr = new StringBuffer(appConfValDao
                    .getConfigValuesByModuleAndKey("egi", "serviceProviderUrl").get(0).getValue());

            for (int i = 0; i < phoneNumber.size(); i++) {
                final String[] phoneNo = phoneNumber.get(i).toString().split(",");
                for (final String element : phoneNo) {
                    /*
                     * Replace second and third # from the url
                     */
                    final int phoneNumberIndex = urlStr.indexOf("#");
                    final String encodedText = text.replaceAll(" ", "%20");
                    urlStr.deleteCharAt(phoneNumberIndex);
                    urlStr.insert(phoneNumberIndex, phoneNumber);
                    final int textIndex = urlStr.lastIndexOf("#");
                    urlStr.deleteCharAt(textIndex);
                    urlStr.insert(textIndex, encodedText);
                    if (LOG.isDebugEnabled())
                        LOG.debug("before opening conn" + urlStr);
                    final URL url = new URL(urlStr.toString());
                    final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    if (LOG.isDebugEnabled())
                        LOG.debug("after opening conn");
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    /*
                     * Get the output stream and write the parameters. Read the
                     * response
                     */
                    final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    final String response = in.readLine();
                    in.close();
                    if (LOG.isDebugEnabled())
                        LOG.debug("::::response::::" + response);
                    for (int k = 0; k < errorCodes.length; k++)
                        if (response.compareToIgnoreCase(errorCodes[k]) == 0) {
                            LOG.error(errorMessages[k]);
                            throw new EGOVRuntimeException(errorMessages[k]);
                        }
                    if (LOG.isDebugEnabled())
                        LOG.debug("Success the message id is :" + response);
                }
            }
        } catch (final MalformedURLException mue) {
            LOG.error("HTTPExample: MalformedURLException; " + mue.getMessage());
            throw new EGOVRuntimeException("Error occured in sending sms!!", mue);
        } catch (final ProtocolException pe) {
            LOG.error("HTTPExample: ProtocolException; " + pe.getMessage());
            throw new EGOVRuntimeException("Error occured in sending sms!!", pe);
        } catch (final IOException ioe) {
            LOG.error("HTTPExample: IOException; " + ioe.getMessage());
            throw new EGOVRuntimeException("Error occured in sending sms!!", ioe);
        }
    }
}
