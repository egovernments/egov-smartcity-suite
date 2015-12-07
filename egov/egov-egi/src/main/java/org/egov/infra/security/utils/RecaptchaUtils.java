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
package org.egov.infra.security.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class RecaptchaUtils {
    
    private static final Logger LOG = LoggerFactory.getLogger(RecaptchaUtils.class);
    
    private static final String CIPHER_INSTANCE_NAME = "AES/ECB/PKCS5Padding";
    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    

    public static boolean captchaIsValid(final HttpServletRequest request) {
        try {
            final HttpPost post = new HttpPost(RECAPTCHA_VERIFY_URL);
            final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("secret", (String) request.getSession().getAttribute("siteSecret")));
            urlParameters.add(new BasicNameValuePair("response", request.getParameter("g-recaptcha-response")));
            urlParameters.add(new BasicNameValuePair("remoteip", request.getRemoteAddr()));
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            final String responseJson = IOUtils.toString(HttpClientBuilder.create().build().execute(post).getEntity().getContent());
            return Boolean.valueOf(new GsonBuilder().create().fromJson(responseJson, HashMap.class).get("success").toString());
        } catch (UnsupportedOperationException | IOException e) {
            LOG.error("Recaptcha verification failed", e);
            return false;
        }
    }
    
    public static final String createSToken(final HttpSession session) {
        return encryptAes(createJsonToken(session.getId()), (String) session.getAttribute("siteSecret"));
    }
    
    private static final String createJsonToken(final String sessionId) {
        final JsonObject obj = new JsonObject();
        obj.addProperty("session_id", sessionId);
        obj.addProperty("ts_ms", System.currentTimeMillis());
        return new Gson().toJson(obj);
    }

    private static String encryptAes(final String input, final String siteSecret) {
            try {
                final SecretKeySpec secretKey = getKey(siteSecret);
                final Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_NAME);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                return BaseEncoding.base64Url().omitPadding().encode(cipher.doFinal(input.getBytes(StandardCharsets.UTF_8)));
            } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                    | BadPaddingException e) {
                LOG.error("Error occurred while encrypting recaptcha key", e);
            }
        return null;
    }

    private static SecretKeySpec getKey(final String siteSecret) {
        try {
            byte[] key = siteSecret.getBytes(StandardCharsets.UTF_8);
            key = Arrays.copyOf(MessageDigest.getInstance("SHA").digest(key), 16);
            return new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Error occurred while encrypting recaptcha key", e);
        }
        return null;
    }

}
