/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.eb.service.receipt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.eb.utils.EBConstants;
import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly=true)
public class EBReceiptInfoService {
	
	private static final Logger LOGGER = Logger.getLogger(EBReceiptInfoService.class);
	@Transactional
	public List<Map<String, String>> fetchReceiptInfo(String rtgsNumber) throws MalformedURLException, IOException, NoSuchAlgorithmException, KeyManagementException   {
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Entered into fetchReceiptInfo, rtgsNumber=" + rtgsNumber);
		
		List<Map<String, String>> rtgsResponseData = new ArrayList<Map<String,String>>();
		Map<String, String> consumerRsponseData = new HashMap<String, String>();

		Long startTime = System.currentTimeMillis();
		
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}		
				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws CertificateException {
				}
				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws CertificateException {
				}
			}
		};
		
		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        
		URL url = new URL(EBConstants.IOB_RECEIPT_URL + rtgsNumber);
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "text/plain");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setConnectTimeout(60000);//setting connection time out to 1 minute

		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.flush();

		// Get the response
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		String errorMsg = "No response received for rtgs " + rtgsNumber;
		String originalResponse = reader.readLine();
		String[] responseParts = null;
				
		LOGGER.debug("Response received - " + originalResponse);
		
		if (StringUtils.isBlank(originalResponse)) {
			LOGGER.debug(errorMsg);
		} else {
			
			String[] response = originalResponse.split("\\$\\$");

			if (response == null) {
				LOGGER.debug(errorMsg);
			} else {
				for (String strResponsse : response) {
					responseParts = strResponsse.split("\\|");
					consumerRsponseData = new HashMap<String, String>();
					// response format
					// RTGSNo|RTGSDate|ConsumerNo|BillMonth|BillAmount|ReceiptNo|ReceiptDate|
					consumerRsponseData.put("RTGSNo", responseParts[0]);
					consumerRsponseData.put("RTGSDate", responseParts[1]);
					consumerRsponseData.put("ConsumerNo", responseParts[2]);
					consumerRsponseData.put("BillMonth", responseParts[3]);
					consumerRsponseData.put("BillAmount", responseParts[4]);
					consumerRsponseData.put("ReceiptNo", responseParts[5]);
					consumerRsponseData.put("ReceiptDate", responseParts[6]);
					rtgsResponseData.add(consumerRsponseData);
				}
			}
		}
		
		writer.close();
		reader.close();
		connection.disconnect();
		
		LOGGER.info("Took " + ((System.currentTimeMillis() - startTime) / 1000) + "sec(s)");

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("fetchReceiptInfo - responseData= " + rtgsResponseData + "\nExiting from fetchReceiptInfo");
		
		return rtgsResponseData;
	}
}

