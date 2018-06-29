/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.pushbox.config;

import static org.egov.pushbox.utils.constants.PushboxConstants.AUTH_PROVIDER_CERT_URL;
import static org.egov.pushbox.utils.constants.PushboxConstants.AUTH_URI;
import static org.egov.pushbox.utils.constants.PushboxConstants.CLIENT_CERT_URL;
import static org.egov.pushbox.utils.constants.PushboxConstants.CLIENT_EMAIL;
import static org.egov.pushbox.utils.constants.PushboxConstants.CLIENT_ID;
import static org.egov.pushbox.utils.constants.PushboxConstants.DBURL;
import static org.egov.pushbox.utils.constants.PushboxConstants.PRIVATE_KEY;
import static org.egov.pushbox.utils.constants.PushboxConstants.PRIVATE_KEY_ID;
import static org.egov.pushbox.utils.constants.PushboxConstants.PROJECT_ID;
import static org.egov.pushbox.utils.constants.PushboxConstants.TOKEN_URI;
import static org.egov.pushbox.utils.constants.PushboxConstants.TYPE;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.pushbox.entity.contracts.PushboxProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.json.JsonFactory;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class PushboxConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushboxConfiguration.class);

    @Autowired
    private Environment env;

    @Bean
    public PushboxProperties initPushBoxProperties() {
        PushboxProperties pushboxProperties = new PushboxProperties();
        pushboxProperties.setType(env.getProperty(TYPE));
        pushboxProperties.setProjectId(env.getProperty(PROJECT_ID));
        pushboxProperties.setPrivateKeyId(env.getProperty(PRIVATE_KEY_ID));
        pushboxProperties.setPrivateKey(env.getProperty(PRIVATE_KEY));
        pushboxProperties.setClientEmail(env.getProperty(CLIENT_EMAIL));
        pushboxProperties.setClientId(env.getProperty(CLIENT_ID));
        pushboxProperties.setAuthUri(env.getProperty(AUTH_URI));
        pushboxProperties.setTokenUri(env.getProperty(TOKEN_URI));
        pushboxProperties.setAuthProviderCertUrl(env.getProperty(AUTH_PROVIDER_CERT_URL));
        pushboxProperties.setClientCertUrl(env.getProperty(CLIENT_CERT_URL));
        pushboxProperties.setDatabaseUrl(env.getProperty(DBURL));
        return pushboxProperties;
    }

    /**
     * This method take the required parameters from the properties file and initialize the firebase. It will initialize only
     * once.
     */
    @PostConstruct
    public void setUpFirebaseApp() {
        if (FirebaseApp.getApps().isEmpty()) {
            PushboxProperties pushboxProperties = initPushBoxProperties();
            JsonFactory jsonFactory = Utils.getDefaultJsonFactory();
            Map<String, Object> secretJson = new ConcurrentHashMap<>();
            secretJson.put(TYPE, pushboxProperties.getType());
            secretJson.put(PROJECT_ID, pushboxProperties.getProjectId());
            secretJson.put(PRIVATE_KEY_ID, pushboxProperties.getPrivateKeyId());
            secretJson.put(PRIVATE_KEY, pushboxProperties.getPrivateKey());
            secretJson.put(CLIENT_EMAIL, pushboxProperties.getClientEmail());
            secretJson.put(CLIENT_ID, pushboxProperties.getClientId());
            secretJson.put(AUTH_URI, pushboxProperties.getAuthUri());
            secretJson.put(TOKEN_URI, pushboxProperties.getTokenUri());
            secretJson.put(AUTH_PROVIDER_CERT_URL, pushboxProperties.getAuthProviderCertUrl());
            secretJson.put(CLIENT_CERT_URL, pushboxProperties.getClientCertUrl());

            try (InputStream refreshTokenStream = new ByteArrayInputStream(jsonFactory.toByteArray(secretJson))) {

                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(refreshTokenStream))
                        .setDatabaseUrl(pushboxProperties.getDatabaseUrl()).build();

                FirebaseApp.initializeApp(options);
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("##PushBoxFox## : Firebase App Initialized");
            } catch (IOException e) {
                LOGGER.error("##PushBoxFox## : Error in setup firebase app", e);
                throw new ApplicationRuntimeException("Error occurred while setup firebase app", e);
            }
        }
    }
}
