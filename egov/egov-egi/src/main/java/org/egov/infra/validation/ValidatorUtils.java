/* eGov suite of products aim to improve the internal efficiency,transparency,
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

package org.egov.infra.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.egov.infra.validation.regex.Constants;

import com.google.gson.GsonBuilder;

public class ValidatorUtils {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(Constants.PASSWORD);

    public static boolean isValidPassword(final String passwd) {
        return StringUtils.isNotBlank(passwd) && passwd.length() > 7 && passwd.length() < 31&& PASSWORD_PATTERN.matcher(passwd).find();
    }

    public static boolean isCaptchaValid(final HttpServletRequest request) {
        try {
            final String url = "https://www.google.com/recaptcha/api/siteverify";
            final HttpClient client = HttpClientBuilder.create().build();
            final HttpPost post = new HttpPost(url);
            final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("secret", (String) request.getSession().getAttribute("cityRecaptchaPK")));
            urlParameters.add(new BasicNameValuePair("response", request.getParameter("g-recaptcha-response")));
            urlParameters.add(new BasicNameValuePair("remoteip", request.getRemoteAddr()));
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            final String responseJson = IOUtils.toString(client.execute(post).getEntity().getContent());
            return Boolean.valueOf(new GsonBuilder().create().fromJson(responseJson, HashMap.class).get("success").toString());
        } catch (UnsupportedOperationException | IOException e) {
            return false;
        }
    }
}
