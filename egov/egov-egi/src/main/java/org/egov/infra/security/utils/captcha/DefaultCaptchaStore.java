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

import com.octo.captcha.Captcha;
import com.octo.captcha.service.captchastore.CaptchaAndLocale;
import com.octo.captcha.service.captchastore.CaptchaStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Locale;


public class DefaultCaptchaStore implements CaptchaStore {

    private static final String CAPTCHA_KEY = "captcha_key";

    @Autowired
    private RedisTemplate redisTemplate;

    private BoundHashOperations<String, String, CaptchaAndLocale> captchaStore;

    @Override
    public boolean hasCaptcha(String key) {
        return captchaStore.hasKey(key);
    }

    @Override
    public void storeCaptcha(String key, Captcha captcha) {
        captchaStore.put(key, new CaptchaAndLocale(captcha));
    }

    @Override
    public void storeCaptcha(String key, Captcha captcha, Locale locale) {
        captchaStore.put(key, new CaptchaAndLocale(captcha, locale));
    }

    @Override
    public boolean removeCaptcha(String key) {
        if (captchaStore.hasKey(key)) {
            captchaStore.delete(key);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Captcha getCaptcha(String key) {
        CaptchaAndLocale captchaAndLocale = captchaStore.get(key);
        return captchaAndLocale != null ? captchaAndLocale.getCaptcha() : null;
    }

    @Override
    public Locale getLocale(String key) {
        CaptchaAndLocale captchaAndLocale = captchaStore.get(key);
        return captchaAndLocale != null ? captchaAndLocale.getLocale() : null;
    }

    @Override
    public int getSize() {
        return captchaStore.size().intValue();
    }

    @Override
    public Collection getKeys() {
        return captchaStore.keys();
    }

    @Override
    public void empty() {
        redisTemplate.delete(CAPTCHA_KEY);
    }

    @Override
    public void initAndStart() {
        captchaStore = redisTemplate.boundHashOps(CAPTCHA_KEY);
    }

    @Override
    public void cleanAndShutdown() {
        redisTemplate.delete(CAPTCHA_KEY);
    }
}
