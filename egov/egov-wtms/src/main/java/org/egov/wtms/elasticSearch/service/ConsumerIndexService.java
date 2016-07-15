/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.wtms.elasticSearch.service;

import java.math.BigDecimal;
import java.util.Iterator;

import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.search.elastic.annotation.Indexing;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.elasticSearch.entity.ConsumerSearch;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ConsumerIndexService {

    @Autowired
    private CityService cityService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @Indexing(name = Index.WATERCHARGES, type = IndexType.CONNECTIONSEARCH)
    public ConsumerSearch createConsumerIndex(final WaterConnectionDetails waterConnectionDetails,
            final AssessmentDetails assessmentDetails, final BigDecimal amountTodisplayInIndex) {

        String mobileNumber = null;
        Iterator<OwnerName> ownerNameItr = assessmentDetails.getOwnerNames().iterator();
        if (ownerNameItr != null && ownerNameItr.hasNext())
            mobileNumber = ownerNameItr.next().getMobileNumber();

        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());

        final ConsumerSearch consumerSearch = new ConsumerSearch(
                waterConnectionDetails.getConnection().getConsumerCode(), mobileNumber,
                waterConnectionDetails.getUsageType().getName(), cityWebsite.getName(),
                waterConnectionDetails.getCreatedDate(), cityWebsite.getDistrictName(), cityWebsite.getRegionName(),
                cityWebsite.getGrade());

        consumerSearch.setZone(assessmentDetails.getBoundaryDetails().getZoneName());
        consumerSearch.setWard(assessmentDetails.getBoundaryDetails().getWardName());
        consumerSearch.setAdminWard(assessmentDetails.getBoundaryDetails().getAdminWardName());
        consumerSearch.setDoorno(assessmentDetails.getHouseNo());
        consumerSearch.setTotalDue(assessmentDetails.getPropertyDetails().getTaxDue());
        consumerSearch.setIslegacy(waterConnectionDetails.getLegacy());
        consumerSearch.setClosureType(waterConnectionDetails.getCloseConnectionType());
        consumerSearch.setLocality(assessmentDetails.getBoundaryDetails().getLocalityName() != null
                ? assessmentDetails.getBoundaryDetails().getLocalityName() : "");
        consumerSearch.setPropertyId(waterConnectionDetails.getConnection().getPropertyIdentifier());
        consumerSearch.setApplicationCode(waterConnectionDetails.getApplicationType().getCode());
        consumerSearch.setStatus(waterConnectionDetails.getConnectionStatus().name());
        consumerSearch.setConnectionType(waterConnectionDetails.getConnectionType().name());
        consumerSearch.setWaterTaxDue(amountTodisplayInIndex);
        consumerSearch.setWaterSourceType(waterConnectionDetails.getWaterSource().getWaterSourceType());
        consumerSearch.setPropertyType(waterConnectionDetails.getPropertyType().getName());
        consumerSearch.setCategory(waterConnectionDetails.getCategory().getName());
        consumerSearch.setSumpCapacity(waterConnectionDetails.getSumpCapacity());
        consumerSearch.setPipeSize(waterConnectionDetails.getPipeSize().getCode());
        consumerSearch.setNumberOfPerson(waterConnectionDetails.getNumberOfPerson());
        consumerSearch.setCurrentDue(
                waterConnectionDetailsService.getTotalAmountTillCurrentFinYear(waterConnectionDetails).subtract(
                        waterConnectionDetailsService.getTotalAmountTillPreviousFinYear(waterConnectionDetails)));
        consumerSearch
                .setArrearsDue(waterConnectionDetailsService.getTotalAmountTillCurrentFinYear(waterConnectionDetails));
        if (connectionDemandService.getWaterRatesDetailsForDemandUpdate(waterConnectionDetails) != null
                && connectionDemandService.getWaterRatesDetailsForDemandUpdate(waterConnectionDetails)
                        .getMonthlyRate() != null)
            consumerSearch.setMonthlyRate(new BigDecimal(connectionDemandService
                    .getWaterRatesDetailsForDemandUpdate(waterConnectionDetails).getMonthlyRate()));
        else
            consumerSearch.setMonthlyRate(BigDecimal.ZERO);
        if (assessmentDetails.getLatitude() != 0.0 && assessmentDetails.getLongitude() != 0.0)
            consumerSearch.setPropertyLocation(
                    new GeoPoint(assessmentDetails.getLatitude(), assessmentDetails.getLongitude()));

        if (assessmentDetails.getBoundaryDetails().getAdminWardId() != null) {
            final Boundary adminBoundary = boundaryService
                    .getBoundaryById(assessmentDetails.getBoundaryDetails().getAdminWardId());

            if (adminBoundary.getLatitude() != null && adminBoundary.getLongitude() != null)
                consumerSearch.setWardLocation(new GeoPoint(adminBoundary.getLatitude(), adminBoundary.getLongitude()));
        }

        ownerNameItr = assessmentDetails.getOwnerNames().iterator();
        if (ownerNameItr.hasNext()) {
            final OwnerName ownerName = ownerNameItr.next();
            consumerSearch.setConsumerName(ownerName.getOwnerName());
            consumerSearch.setAadhaarNumber(ownerName.getAadhaarNumber() != null ? ownerName.getAadhaarNumber() : "");
            while (ownerNameItr.hasNext()) {
                final OwnerName multipleOwner = ownerNameItr.next();
                consumerSearch.setConsumerName(
                        consumerSearch.getConsumerName().concat(",".concat(multipleOwner.getOwnerName())));
                consumerSearch.setAadhaarNumber(consumerSearch.getAadhaarNumber().concat(
                        ",".concat(multipleOwner.getAadhaarNumber() != null ? multipleOwner.getAadhaarNumber() : "")));
            }

        }
        return consumerSearch;
    }
}
