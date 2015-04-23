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

public class EBReceiptInfoService {
	
	private static final Logger LOGGER = Logger.getLogger(EBReceiptInfoService.class);
	
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

