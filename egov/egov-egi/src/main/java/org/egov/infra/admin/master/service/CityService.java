/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
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

package org.egov.infra.admin.master.service;

import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.repository.CityRepository;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.messaging.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.egov.infra.utils.ApplicationConstant.CITY_CODE_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_CORP_GRADE_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_CORP_NAME_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_DIST_NAME_KEY;

@Service
@Transactional(readOnly = true)
public class CityService {

    private static final String CITY_PREFS_CK = "%s-cityPrefs";

    private final CityRepository cityRepository;

    @Autowired
    private MessagingService messagingService;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Object> cityPrefCache;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public CityService(final CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Transactional
    public City updateCity(final City city) {
        redisTemplate.delete(cityPrefCacheKey());
        return cityRepository.save(city);
    }

    public City getCityByURL(final String url) {
        return cityRepository.findByDomainURL(url);
    }

    public City getCityByName(final String cityName) {
        return cityRepository.findByName(cityName);
    }

    public City getCityByCode(final String code) {
        return cityRepository.findByCode(code);
    }

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public void sentFeedBackMail(final String email, final String subject, final String message) {
        messagingService.sendEmail(email, subject, message);
    }

    public Map<String, Object> cityDataAsMap() {
        Map<String, Object> cityPrefs = cityPrefCache.entries(cityPrefCacheKey());
        if (cityPrefs.isEmpty()) {
            cityPrefCache.putAll(cityPrefCacheKey(), getCityByURL(ApplicationThreadLocals.getDomainName()).toMap());
            cityPrefs = cityPrefCache.entries(cityPrefCacheKey());
        }
        return cityPrefs;
    }

    public String getCityCode() {
        return (String) cityDataForKey(CITY_CODE_KEY);
    }

    public String getMunicipalityName() {
        return (String) cityDataForKey(CITY_CORP_NAME_KEY);
    }

    public String getCityGrade() {
        return (String) cityDataForKey(CITY_CORP_GRADE_KEY);
    }

    public String getDistrictName() {
        return (String) cityDataForKey(CITY_DIST_NAME_KEY);
    }

    public String cityPrefCacheKey() {
        return String.format(CITY_PREFS_CK, ApplicationThreadLocals.getDomainName());
    }

    public Object cityDataForKey(final String key) {
        return cityPrefCache.entries(cityPrefCacheKey()).get(key);
    }
}
