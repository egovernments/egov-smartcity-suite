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

package org.egov.wtms.es.service;

import java.math.BigDecimal;
import java.util.Iterator;

import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.es.entity.WaterChargeIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WaterChargeIndexService {


    @Autowired
    private CityService cityService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    

    public WaterChargeIndex createWaterChargeIndex(final WaterConnectionDetails waterConnectionDetails,
            final AssessmentDetails assessmentDetails, final BigDecimal amountTodisplayInIndex) {

        Iterator<OwnerName> ownerNameItr = assessmentDetails.getOwnerNames().iterator();
        if (ownerNameItr != null && ownerNameItr.hasNext())
            ownerNameItr.next().getMobileNumber();

        cityService.getCityByURL(ApplicationThreadLocals.getDomainName());

        final WaterChargeIndex waterChargeIndex = new WaterChargeIndex();

        waterChargeIndex.setZone(assessmentDetails.getBoundaryDetails().getZoneName());
        waterChargeIndex.setWard(assessmentDetails.getBoundaryDetails().getWardName());
        // waterChargeIndex.setAdminWard(assessmentDetails.getBoundaryDetails().getAdminWardName());
        waterChargeIndex.setDoorno(assessmentDetails.getHouseNo());
        waterChargeIndex.setTotaldue(assessmentDetails.getPropertyDetails().getTaxDue().longValue());
        waterChargeIndex.setIslegacy(waterConnectionDetails.getLegacy());
        waterChargeIndex.setClosureType(waterConnectionDetails.getCloseConnectionType());
        waterChargeIndex.setLocality(assessmentDetails.getBoundaryDetails().getLocalityName() != null
                ? assessmentDetails.getBoundaryDetails().getLocalityName() : "");
        waterChargeIndex.setPropertyid(waterConnectionDetails.getConnection().getPropertyIdentifier());
        waterChargeIndex.setApplicationcode(waterConnectionDetails.getApplicationType().getCode());
        waterChargeIndex.setStatus(waterConnectionDetails.getConnectionStatus().name());
        waterChargeIndex.setConnectiontype(waterConnectionDetails.getConnectionType().name());
        waterChargeIndex.setWaterTaxDue(amountTodisplayInIndex.longValue());
        waterChargeIndex.setWatersource(waterConnectionDetails.getWaterSource().getWaterSourceType());
        waterChargeIndex.setPropertytype(waterConnectionDetails.getPropertyType().getName());
        waterChargeIndex.setCategory(waterConnectionDetails.getCategory().getName());
        waterChargeIndex.setSumpcapacity(waterConnectionDetails.getSumpCapacity());
        waterChargeIndex.setPipesize(waterConnectionDetails.getPipeSize().getCode());
        waterChargeIndex.setNumberofperson(Long.valueOf(waterConnectionDetails.getNumberOfPerson()));
        waterChargeIndex
                .setCurrentDue(waterConnectionDetailsService.getTotalAmountTillCurrentFinYear(waterConnectionDetails)
                        .subtract(
                                waterConnectionDetailsService.getTotalAmountTillPreviousFinYear(waterConnectionDetails))
                        .longValue());
        waterChargeIndex.setArrearsDue(
                waterConnectionDetailsService.getTotalAmountTillPreviousFinYear(waterConnectionDetails).longValue());
        waterChargeIndex
                .setCurrentDemand(waterConnectionDetailsService.getTotalDemandTillCurrentFinYear(waterConnectionDetails)
                        .subtract(waterConnectionDetailsService.getArrearsDemand(waterConnectionDetails)).longValue());
        waterChargeIndex
                .setArrearsDemand(waterConnectionDetailsService.getArrearsDemand(waterConnectionDetails).longValue());
        if (connectionDemandService.getWaterRatesDetailsForDemandUpdate(waterConnectionDetails) != null
                && connectionDemandService.getWaterRatesDetailsForDemandUpdate(waterConnectionDetails)
                        .getMonthlyRate() != null)
            waterChargeIndex.setMonthlyRate(new BigDecimal(connectionDemandService
                    .getWaterRatesDetailsForDemandUpdate(waterConnectionDetails).getMonthlyRate()).longValue());
        else
            waterChargeIndex.setMonthlyRate(BigDecimal.ZERO.longValue());
        /*
         * if (assessmentDetails.getLatitude() != 0.0 &&
         * assessmentDetails.getLongitude() != 0.0)
         * consumerSearch.setPropertyLocation( new
         * GeoPoint(assessmentDetails.getLatitude(),
         * assessmentDetails.getLongitude())); if
         * (assessmentDetails.getBoundaryDetails().getAdminWardId() != null) {
         * final Boundary adminBoundary = boundaryService
         * .getBoundaryById(assessmentDetails.getBoundaryDetails().
         * getAdminWardId()); if (adminBoundary.getLatitude() != null &&
         * adminBoundary.getLongitude() != null)
         * consumerSearch.setWardLocation(new
         * GeoPoint(adminBoundary.getLatitude(), adminBoundary.getLongitude()));
         * }
         */
        ownerNameItr = assessmentDetails.getOwnerNames().iterator();
        if (ownerNameItr.hasNext()) {
            final OwnerName ownerName = ownerNameItr.next();
            waterChargeIndex.setConsumername(ownerName.getOwnerName());
            waterChargeIndex.setAadhaarnumber(ownerName.getAadhaarNumber() != null ? ownerName.getAadhaarNumber() : "");
            while (ownerNameItr.hasNext()) {
                final OwnerName multipleOwner = ownerNameItr.next();
                waterChargeIndex.setConsumername(
                        waterChargeIndex.getConsumername().concat(",".concat(multipleOwner.getOwnerName())));
                waterChargeIndex.setAadhaarnumber(waterChargeIndex.getAadhaarnumber().concat(
                        ",".concat(multipleOwner.getAadhaarNumber() != null ? multipleOwner.getAadhaarNumber() : "")));
            }

        }
        return waterChargeIndex;
    }

}
