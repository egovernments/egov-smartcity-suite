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

package org.egov.wtms.service.es;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.egov.infra.utils.ApplicationConstant.CITY_CORP_GRADE_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_DIST_NAME_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_NAME_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_REGION_NAME_KEY;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.entity.es.WaterChargeDocument;
import org.egov.wtms.repository.es.WaterChargeDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WaterChargeDocumentService {

    private final WaterChargeDocumentRepository waterChargeIndexRepository;
    
    @Autowired
    private CityService cityService;
    
    @Autowired
    private BoundaryService boundaryService;
   
    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @Autowired
    public WaterChargeDocumentService(WaterChargeDocumentRepository waterChargeIndexRepository) {
        this.waterChargeIndexRepository = waterChargeIndexRepository;
    }

    public WaterChargeDocument createWaterChargeIndex(final WaterConnectionDetails waterConnectionDetails,
            final AssessmentDetails assessmentDetails, final BigDecimal amountTodisplayInIndex) {

        Iterator<OwnerName> ownerNameItr = assessmentDetails.getOwnerNames().iterator();
       
        Long monthlyRate=null;
        String consumername="";
        String aadharNumber="";
        String mobilNumber="";
        GeoPoint wardlocation=null;
        GeoPoint propertylocation=null;
        
        cityService.getCityByURL(ApplicationThreadLocals.getDomainName());

        if (connectionDemandService.getWaterRatesDetailsForDemandUpdate(waterConnectionDetails) != null
                && connectionDemandService.getWaterRatesDetailsForDemandUpdate(waterConnectionDetails)
                        .getMonthlyRate() != null)
            monthlyRate=new BigDecimal(connectionDemandService
                    .getWaterRatesDetailsForDemandUpdate(waterConnectionDetails).getMonthlyRate()).longValue();
        else
            monthlyRate=BigDecimal.ZERO.longValue();
        if (ownerNameItr != null && ownerNameItr.hasNext())
            ownerNameItr.next().getMobileNumber();
        ownerNameItr = assessmentDetails.getOwnerNames().iterator();
        if (ownerNameItr.hasNext()) {
            final OwnerName ownerName = ownerNameItr.next();
            consumername=ownerName.getOwnerName();
            mobilNumber=ownerName.getMobileNumber();
            aadharNumber=ownerName.getAadhaarNumber() != null ? ownerName.getAadhaarNumber() : "";
            while (ownerNameItr.hasNext()) {
                final OwnerName multipleOwner = ownerNameItr.next();
                consumername=
                        consumername.concat(",".concat(multipleOwner.getOwnerName()));
                aadharNumber=aadharNumber.concat(
                        ",".concat(multipleOwner.getAadhaarNumber() != null ? multipleOwner.getAadhaarNumber() : ""));
            }
        }
        
       if (assessmentDetails.getLatitude() != 0.0 &&  assessmentDetails.getLongitude() != 0.0)
            propertylocation=new GeoPoint(assessmentDetails.getLatitude(),assessmentDetails.getLongitude()); 
        
        if (assessmentDetails.getBoundaryDetails().getAdminWardId() != null) {
                final Boundary adminBoundary = boundaryService.getBoundaryById(assessmentDetails.getBoundaryDetails().getAdminWardId()); 
                if (adminBoundary.getLatitude() != null &&  adminBoundary.getLongitude() != null)
                    wardlocation=new GeoPoint(adminBoundary.getLatitude(), adminBoundary.getLongitude());
                }
                
        Map<String, Object> cityInfo = cityService.cityDataAsMap();
        WaterChargeDocument  waterChargeIndex = WaterChargeDocument.builder().withZone(assessmentDetails.getBoundaryDetails().getZoneName())
                .withWard(assessmentDetails.getBoundaryDetails().getWardName())
                .withAdminward(assessmentDetails.getBoundaryDetails().getAdminWardName())
                .withDoorNo(assessmentDetails.getHouseNo())
                .withTotaldue(assessmentDetails.getPropertyDetails().getTaxDue().longValue())
                .withIslegacy(waterConnectionDetails.getLegacy())
                .withGrade(defaultString((String) cityInfo.get(CITY_CORP_GRADE_KEY)))
                .withRegionname(defaultString((String) cityInfo.get(CITY_REGION_NAME_KEY)))
                .withClosureType(waterConnectionDetails.getCloseConnectionType()!=null?waterConnectionDetails.getCloseConnectionType():"" )
                .withLocality(assessmentDetails.getBoundaryDetails().getLocalityName() != null
                        ? assessmentDetails.getBoundaryDetails().getLocalityName() : "")
                .withPropertyid(waterConnectionDetails.getConnection().getPropertyIdentifier())
                .withApplicationcode(waterConnectionDetails.getApplicationType().getCode())
                .withCreatedDate(waterConnectionDetails.getExecutionDate())
                .withMobileNumber(mobilNumber)
                .withStatus(waterConnectionDetails.getConnectionStatus().name())
                .withDistrictName(defaultString((String) cityInfo.get(CITY_DIST_NAME_KEY)))
                .withConnectiontype(waterConnectionDetails.getConnectionType().name())
                .withWaterTaxDue(amountTodisplayInIndex.longValue())
                .withUsage(waterConnectionDetails.getUsageType().getCode())
                .withConsumerCode(waterConnectionDetails.getConnection().getConsumerCode())
                .withWatersource(waterConnectionDetails.getWaterSource().getWaterSourceType())
                .withPropertytype(waterConnectionDetails.getPropertyType().getName())
                .withCategory(waterConnectionDetails.getCategory().getName())
                .withUlbname(defaultString((String) cityInfo.get(CITY_NAME_KEY)))
                .withSumpcapacity(waterConnectionDetails.getSumpCapacity())
                .withPipesize(waterConnectionDetails.getPipeSize().getCode())
                .withNumberOfPerson((waterConnectionDetails.getNumberOfPerson() !=null ? Long.valueOf(waterConnectionDetails.getNumberOfPerson()):0l))
                .withCurrentDue(waterConnectionDetailsService.getTotalAmountTillCurrentFinYear(waterConnectionDetails)
                        .subtract(
                                waterConnectionDetailsService.getTotalAmountTillPreviousFinYear(waterConnectionDetails))
                        .longValue())
                .withArrearsDue(
                        waterConnectionDetailsService.getTotalAmountTillPreviousFinYear(waterConnectionDetails).longValue())
                .withCurrentDemand(waterConnectionDetailsService.getTotalDemandTillCurrentFinYear(waterConnectionDetails)
                        .subtract(waterConnectionDetailsService.getArrearsDemand(waterConnectionDetails)).longValue())
                .withArrearsDemand(waterConnectionDetailsService.getArrearsDemand(waterConnectionDetails).longValue())
                .withMonthlyRate(monthlyRate)
                .withConsumername(consumername)
                .withAadhaarnumber(aadharNumber)
                .withWardlocation(wardlocation)
                .withPropertylocation(propertylocation)
                .build();
         createWaterChargeDocument(waterChargeIndex);
         return waterChargeIndex;
    }
    
    @Transactional
    public WaterChargeDocument createWaterChargeDocument(WaterChargeDocument waterChargeIndex) {
        waterChargeIndexRepository.save(waterChargeIndex);
        return waterChargeIndex;
    }

}
