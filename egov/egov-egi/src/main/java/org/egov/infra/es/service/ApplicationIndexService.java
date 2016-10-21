/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.es.service;

import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.es.entity.ApplicationIndex;
import org.egov.infra.es.repository.ApplicationIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for the Application Index
 *
 * @author rishi
 */
@Service
@Transactional(readOnly = true)
public class ApplicationIndexService {

    private final ApplicationIndexRepository applicationIndexRepository;

    @Autowired
    private CityService cityService;

    @Autowired
    public ApplicationIndexService(final ApplicationIndexRepository applicationIndexRepository) {
        this.applicationIndexRepository = applicationIndexRepository;
    }

    @Transactional
    public ApplicationIndex createApplicationIndex(final ApplicationIndex applicationIndex) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        applicationIndex.setCityCode(cityWebsite.getCode() != null ? cityWebsite.getCode() : "");
        applicationIndex.setCityName(cityWebsite.getName() != null ? cityWebsite.getName() : "");
        applicationIndex.setCityGrade(cityWebsite.getGrade() != null ? cityWebsite.getGrade() : "");
        applicationIndex.setDistrictName(cityWebsite.getDistrictName() != null ? cityWebsite.getDistrictName() : "");
        applicationIndex.setRegionName(cityWebsite.getRegionName() != null ? cityWebsite.getRegionName() : "");
        applicationIndexRepository.save(applicationIndex);
        return applicationIndex;
    }

    @Transactional
    public ApplicationIndex updateApplicationIndex(final ApplicationIndex applicationIndex) {
        applicationIndexRepository.save(applicationIndex);
        return applicationIndex;
    }

    public ApplicationIndex findByApplicationNumber(final String applicationNumber) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        return applicationIndexRepository.findByApplicationNumberAndCityName(applicationNumber, cityWebsite.getName());
    }

}
