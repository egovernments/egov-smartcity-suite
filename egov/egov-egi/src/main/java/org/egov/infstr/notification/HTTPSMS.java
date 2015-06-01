/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.infstr.notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.config.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A simple example class that sends some text via some HTTP gateway.
 */
@Component
public class HTTPSMS {
    private static final Logger LOGGER = Logger.getLogger(HTTPSMS.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     * Simpler method to send SMS with given message to the given mobileNumber.
     * Since most of the time
     * {@link HTTPSMS#sendSMS(String, String, String, String, String)} api user
     * has to send empty string.
     **/
    public boolean sendSMS(final String message, final String mobileNumber) throws EGOVRuntimeException {
        try {
            return applicationProperties.smsEnabled()
                    && sendSMS(StringUtils.EMPTY, StringUtils.EMPTY, message, mobileNumber, StringUtils.EMPTY);
        } catch (final EGOVException e) {
            LOGGER.error(e);
            throw new EGOVRuntimeException("SMS sending failed", e);
        }
    }

    public boolean sendSMS(final String username, final String password, final String text, final String phoneNumber,
            final String sender) throws EGOVException {
        final String unicelErrorCodes[] = { "0x200", "0x201", "0x202", "0x203", "0x204", "0x205", "0x206", "0x207", "0x208",
                "0x209", "0x210", "0x211", "0x212", "0x213" };
        final String unicelErrorMessages[] = { "Invalid Username or Password", "Account Suspended due to some reason",
                "Invalid Source Address/Sender Id. As per GSM standard the sender ID should be within 11 characters.",
                "Message Length Exceeded(more than 160 chars) if concat is set to 0",
                "Message Length Exceeded(more than 459 chars) if concat is set to 1", "DLR URL is not set",
                "Only the subscribed service type can be accessed so make sure that the service type you are trying to connect with.",
                "Invalid Source IP. Kindly check if the IP is responding.", "Account Deactivated/Expired.",
                "Invalid Message Length (less than 160 chars) if concat is set to 1", "Invalid Parameter values",
                "Invalid Message Length (more than 280 chars)", "Invalid Message Length", "Invalid Destination number" };
        try {
            final StringBuffer urlStr = new StringBuffer(applicationProperties.smsProviderURL()).append("?uname=")
                    .append(applicationProperties.smsSenderUsername()).append("&pass=")
                    .append(applicationProperties.smsSenderPassword()).append("&send=").append(applicationProperties.smsSender())
                    .append("&dest=").append(phoneNumber).append("&msg=").append(text.replaceAll(" ", "%20"));
            final URL url = new URL(urlStr.toString());
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // Get the output stream and write the parameters,read the response
            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final String response = in.readLine();
            in.close();
            connection.disconnect();
            for (int i = 0; i < unicelErrorCodes.length; i++)
                if (response.compareToIgnoreCase(unicelErrorCodes[i]) == 0) {
                    LOGGER.error(unicelErrorMessages[i]);
                    return false;
                }
            LOGGER.debug("Success the message id is :" + response);
            return true;
        } catch (final MalformedURLException mue) {
            throw new EGOVException("Error occurred in sending SMS !!  Exception: MalformedURLException", mue);
        } catch (final ProtocolException pe) {
            throw new EGOVException("Error occurred in sending SMS !!  Exception: ProtocolException", pe);
        } catch (final IOException ioe) {
            throw new EGOVException("Error occurred in sending SMS !!  Exception: IOException", ioe);
        }
    }
}
