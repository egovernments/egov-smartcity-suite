/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
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

    private static final Logger LOG = LoggerFactory.getLogger(CaptchaUtils.class);

    @Value("${captcha.verification.url}")
    private String captchaVerificationUrl;

    @Value("#{'${captcha.strength}'.equals('high')}")
    private boolean highlySecure;

    @Autowired
    private DefaultCaptchaService captchaService;

    public boolean captchaIsValid(HttpServletRequest request) {
        try {
            if (highlySecure) {
                HttpPost post = new HttpPost(captchaVerificationUrl);
                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("secret", (String) request.getSession().getAttribute("siteSecret")));
                urlParameters.add(new BasicNameValuePair("response", request.getParameter("g-recaptcha-response")));
                urlParameters.add(new BasicNameValuePair("remoteip", request.getRemoteAddr()));
                post.setEntity(new UrlEncodedFormEntity(urlParameters));
                String responseJson = IOUtils.toString(HttpClientBuilder.create().build().execute(post).getEntity().getContent(), Charset.defaultCharset());
                return Boolean.valueOf(new GsonBuilder().create().fromJson(responseJson, HashMap.class).get("success").toString());
            } else {
                String captchaId = request.getParameter("j_captcha_key");
                String response = request.getParameter("j_captcha_response");
                return captchaService.validateResponseForID(captchaId, response);
            }
        } catch (Exception e) {
            LOG.error("Recaptcha verification failed", e);
            return false;
        }
    }
}
