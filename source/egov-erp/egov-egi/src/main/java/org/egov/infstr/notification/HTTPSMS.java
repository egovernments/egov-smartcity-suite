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
import org.egov.infstr.utils.EGovConfig;

/**
 * A simple example class that sends some text via some HTTP gateway.
 */
public class HTTPSMS {
	private static final Logger LOGGER = Logger.getLogger(HTTPSMS.class);

	/** 
	 * Simpler method to send SMS with given message to the given mobileNumber. 
	 * Since most of the time {@link HTTPSMS#sendSMS(String, String, String, String, String)}  
	 * api user has to send empty string. 
	 **/
	public static boolean sendSMS(final String message, final String mobileNumber) throws EGOVRuntimeException {
		try {
			return sendSMS(StringUtils.EMPTY, StringUtils.EMPTY, message, mobileNumber, StringUtils.EMPTY);
		} catch (final EGOVException e) {
			LOGGER.error(e.getMessage(),e);
			throw new EGOVRuntimeException(e.getMessage(), e);
		}
	}

	public static boolean sendSMS(final String username, final String password, final String text, final String phoneNumber, final String sender) throws EGOVException {
		final String unicelErrorCodes[] = { "0x200", "0x201", "0x202", "0x203", "0x204", "0x205", "0x206", "0x207", "0x208", "0x209", "0x210", "0x211", "0x212", "0x213" };
		final String unicelErrorMessages[] = { "Invalid Username or Password", "Account Suspended due to some reason",
				"Invalid Source Address/Sender Id. As per GSM standard the sender ID should be within 11 characters.", "Message Length Exceeded(more than 160 chars) if concat is set to 0",
				"Message Length Exceeded(more than 459 chars) if concat is set to 1", "DLR URL is not set",
				"Only the subscribed service type can be accessed so make sure that the service type you are trying to connect with.", "Invalid Source IP. Kindly check if the IP is responding.",
				"Account Deactivated/Expired.", "Invalid Message Length (less than 160 chars) if concat is set to 1", "Invalid Parameter values", "Invalid Message Length (more than 280 chars)",
				"Invalid Message Length", "Invalid Destination number" };
		try {
			final StringBuffer urlStr = new StringBuffer(EGovConfig.getProperty("UNICELURL", "", "GENERAL"));
			// replace second and third #
			final int phoneNumberIndex = urlStr.indexOf("#");
			final String encodedText = text.replaceAll(" ", "%20");
			urlStr.deleteCharAt(phoneNumberIndex);
			urlStr.insert(phoneNumberIndex, phoneNumber);
			final int textIndex = urlStr.lastIndexOf("#");
			urlStr.deleteCharAt(textIndex);
			urlStr.insert(textIndex, encodedText);
			LOGGER.debug("before opening conn" + urlStr);
			final URL url = new URL(urlStr.toString());
			LOGGER.debug("Just  opening Connection .............");
			final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			LOGGER.debug("after opening conn");
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			// Get the output stream and write the parameters
			// Read the response
			final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			final String response = in.readLine();
			in.close();
			connection.disconnect();
			LOGGER.debug(response);
			for (int i = 0; i < unicelErrorCodes.length; i++) {
				if (response.compareToIgnoreCase(unicelErrorCodes[i]) == 0) {
					LOGGER.error(unicelErrorMessages[i]);
					return false;
				}
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
