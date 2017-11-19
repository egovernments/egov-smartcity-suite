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

package org.egov.lcms.service.es;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.egov.infra.utils.ApplicationConstant.CITY_CORP_GRADE_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_DIST_NAME_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_REGION_NAME_KEY;

import java.text.ParseException;
import java.util.Map;

import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.lcms.entity.es.HearingsDocument;
import org.egov.lcms.repository.es.HearingsDocumentRepository;
import org.egov.lcms.transactions.entity.Hearings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class HearingsDocumentService {

    private final HearingsDocumentRepository hearingsDocumentRepository;

    @Autowired
    private CityService cityService;

    @Autowired
    public HearingsDocumentService(final HearingsDocumentRepository hearingsDocumentRepository) {
        this.hearingsDocumentRepository = hearingsDocumentRepository;
    }

    public HearingsDocument persistHearingsDocumentIndex(final Hearings hearings)
            throws ParseException {
        final Map<String, Object> cityInfo = cityService.cityDataAsMap();
        HearingsDocument hearingsDocument = null;
        if (hearings.getId() != null)
            hearingsDocument = hearingsDocumentRepository
                    .findOne(ApplicationThreadLocals.getCityCode() + "_" + hearings.getId());

        if (hearingsDocument != null)
            updateHearingsIndex(hearingsDocument, hearings);
        else {
            hearingsDocument = HearingsDocument.builder()
                    .withLcNumber(hearings.getLegalCase().getLcNumber()).withHearingDate(hearings.getHearingDate())
                    .withHearingOutcome(hearings.getHearingOutcome() != null ? hearings.getHearingOutcome() : "")
                    .withPurposeOfHearing(hearings.getPurposeofHearings())
                    .withAdditionalLawyer(hearings.getAdditionalLawyers() != null ? hearings.getAdditionalLawyers() : "")
                    .withEmployeeNames(hearings.getTempEmplyeeHearing() != null
                            ? hearings.getEmployeeHearing() : "")
                    .withStandingCounselPresent(hearings.getIsStandingCounselPresent())
                    .withCityName(ApplicationThreadLocals.getCityName())
                    .withCityCode(ApplicationThreadLocals.getCityCode())
                    .withId(ApplicationThreadLocals.getCityCode().concat("_").concat(String.valueOf(hearings.getId())))
                    .withCityGrade(defaultString((String) cityInfo.get(CITY_CORP_GRADE_KEY)))
                    .withRegionName(defaultString((String) cityInfo.get(CITY_REGION_NAME_KEY)))
                    .withDistrictName(defaultString((String) cityInfo.get(CITY_DIST_NAME_KEY)))
                    .withCreatedDate(hearings.getHearingDate()).build();
            createHearingsDocument(hearingsDocument);
        }

        return hearingsDocument;
    }

    public HearingsDocument updateHearingsIndex(final HearingsDocument hearingsDocument, final Hearings hearings)
            throws ParseException {
        hearingsDocument.setHearingDate(hearings.getHearingDate());
        hearingsDocument.setPurposeOfHearing(hearings.getPurposeofHearings());
        hearingsDocument.setHearingOutcome(hearings.getHearingOutcome());
        hearingsDocument.setAdditionalLawyer(hearings.getAdditionalLawyers());
        hearingsDocument.setEmployeeNames(
                hearings.getTempEmplyeeHearing() != null ? hearings.getEmployeeHearing() : "");
        hearingsDocument.setStandingCounselPresent(hearings.getIsStandingCounselPresent());
        createHearingsDocument(hearingsDocument);
        return hearingsDocument;

    }

    @Transactional
    public HearingsDocument createHearingsDocument(final HearingsDocument hearingsIndex) {
        hearingsDocumentRepository.save(hearingsIndex);
        return hearingsIndex;
    }
}
