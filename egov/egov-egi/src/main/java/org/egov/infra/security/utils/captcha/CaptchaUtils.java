/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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

package org.egov.infra.security.utils.captcha;

import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CaptchaUtils {

    public static final String CITY_CAPTCHA_PRIV_KEY = "siteSecret";
    public static final String CITY_CAPTCHA_PUB_KEY = "siteKey";
    public static final String J_CAPTCHA_RESPONSE = "j_captcha_response";
    public static final String RECAPTCHA_RESPONSE = "g-recaptcha-response";

    private static final Logger LOG = LoggerFactory.getLogger(CaptchaUtils.class);
    private static final String J_CAPTCHA_KEY = "j_captcha_key";
    private static final String RECAPTCH_SECRET_KEY = "secret";
    private static final String RECAPTCHA_REMOTEIP_KEY = "remoteip";
    private static final String RECAPTCHA_RESPONSE_KEY = "response";

    @Value("${captcha.verification.url}")
    private String captchaVerificationUrl;

    @Value("#{'${captcha.strength}'.equals('high')}")
    private boolean highlySecure;

    @Autowired
    private DefaultCaptchaService captchaService;

    public boolean captchaIsValid(HttpServletRequest request) {
        try {
            if (highlySecure) {
                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair(RECAPTCH_SECRET_KEY, (String) request.getSession()
                        .getAttribute(CITY_CAPTCHA_PRIV_KEY)));
                urlParameters.add(new BasicNameValuePair(RECAPTCHA_RESPONSE_KEY, request.getParameter(RECAPTCHA_RESPONSE)));
                urlParameters.add(new BasicNameValuePair(RECAPTCHA_REMOTEIP_KEY, request.getRemoteAddr()));
                HttpPost post = new HttpPost(captchaVerificationUrl);
                post.setEntity(new UrlEncodedFormEntity(urlParameters));
                String responseJson = IOUtils.toString(HttpClientBuilder.create().build()
                        .execute(post).getEntity().getContent(), Charset.defaultCharset());
                return Boolean.valueOf(new GsonBuilder().create().fromJson(responseJson, HashMap.class).get("success").toString());
            } else {
                String captchaId = request.getParameter(J_CAPTCHA_KEY);
                String response = request.getParameter(J_CAPTCHA_RESPONSE);
                return captchaService.validateResponseForID(captchaId, response);
            }
        } catch (Exception e) {
            LOG.warn("Captcha verification failed", e);
            return false;
        }
    }
}
