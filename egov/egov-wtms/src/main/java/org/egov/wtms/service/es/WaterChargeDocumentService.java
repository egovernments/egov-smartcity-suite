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

package org.egov.wtms.service.es;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.egov.infra.utils.ApplicationConstant.CITY_CORP_GRADE_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_DIST_NAME_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_NAME_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_REGION_NAME_KEY;
import static org.egov.infra.utils.ApplicationConstant.ES_DATE_FORMAT;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.entity.es.WaterChargeDocument;
import org.egov.wtms.repository.es.WaterChargeDocumentRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WaterChargeDocumentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WaterChargeDocumentService.class);
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
    public WaterChargeDocumentService(final WaterChargeDocumentRepository waterChargeIndexRepository) {
        this.waterChargeIndexRepository = waterChargeIndexRepository;
    }

    public Iterable<WaterChargeDocument> searchwaterChargeIndex(final BoolQueryBuilder searchQuery) {
        return waterChargeIndexRepository.search(searchQuery);
    }

    public WaterChargeDocument updateWaterChargeIndex(final WaterChargeDocument waterChargeDocument,
            final WaterConnectionDetails waterConnectionDetails, final AssessmentDetails assessmentDetails,
            final BigDecimal amountTodisplayInIndex) {

        Iterator<OwnerName> ownerNameItr = assessmentDetails.getOwnerNames().iterator();
        Long monthlyRate = null;
        String consumername = "";
        String aadharNumber = "";
        String mobilNumber = "";
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(ES_DATE_FORMAT);
        String createdDate = null;

        if (waterConnectionDetails.getLegacy()) {
            if (waterConnectionDetails.getApplicationDate() != null)
                createdDate = dateFormatter.format(waterConnectionDetails.getApplicationDate());
        } else
            createdDate = dateFormatter.format(waterConnectionDetails.getExecutionDate());
        monthlyRate = monthlyRateFirld(waterConnectionDetails);
        if (ownerNameItr != null && ownerNameItr.hasNext())
            ownerNameItr.next().getMobileNumber();
        ownerNameItr = assessmentDetails.getOwnerNames().iterator();
        if (ownerNameItr.hasNext()) {
            final OwnerName ownerName = ownerNameItr.next();
            consumername = ownerName.getOwnerName();
            mobilNumber = ownerName.getMobileNumber();
            aadharNumber = ownerName.getAadhaarNumber() != null ? ownerName.getAadhaarNumber() : "";
            while (ownerNameItr.hasNext()) {
                final OwnerName multipleOwner = ownerNameItr.next();
                consumername = consumername.concat(",".concat(multipleOwner.getOwnerName()));
                aadharNumber = aadharNumber.concat(
                        ",".concat(multipleOwner.getAadhaarNumber() != null ? multipleOwner.getAadhaarNumber() : ""));
            }
        }
        final Map<String, Object> cityInfo = cityService.cityDataAsMap();
        try {
            waterChargeDocument.setZone(assessmentDetails.getBoundaryDetails().getZoneName());
            waterChargeDocument.setRevenueWard(assessmentDetails.getBoundaryDetails().getWardName());
            waterChargeDocument.setAdminWard(assessmentDetails.getBoundaryDetails().getAdminWardName());
            waterChargeDocument.setDoorNo(assessmentDetails.getHouseNo());
            waterChargeDocument.setTotalDue(assessmentDetails.getPropertyDetails().getTaxDue().longValue());
            waterChargeDocument.setLegacy(waterConnectionDetails.getLegacy());
            waterChargeDocument.setCityGrade(defaultString((String) cityInfo.get(CITY_CORP_GRADE_KEY)));
            waterChargeDocument.setRegionName(defaultString((String) cityInfo.get(CITY_REGION_NAME_KEY)));
            waterChargeDocument.setClosureType(waterConnectionDetails.getCloseConnectionType() != null
                    ? waterConnectionDetails.getCloseConnectionType() : "");
            waterChargeDocument.setLocality(assessmentDetails.getBoundaryDetails().getLocalityName() != null
                    ? assessmentDetails.getBoundaryDetails().getLocalityName() : "");
            waterChargeDocument.setPropertyId(waterConnectionDetails.getConnection().getPropertyIdentifier());
            waterChargeDocument.setApplicationCode(waterConnectionDetails.getApplicationType().getCode());
            waterChargeDocument.setCreatedDate(dateFormatter.parse(createdDate));
            waterChargeDocument.setMobileNumber(mobilNumber);
            waterChargeDocument.setStatus(waterConnectionDetails.getConnectionStatus().name());
            waterChargeDocument.setDistrictName(defaultString((String) cityInfo.get(CITY_DIST_NAME_KEY)));
            waterChargeDocument.setConnectionType(waterConnectionDetails.getConnectionType().name());
            waterChargeDocument.setWaterTaxDue(amountTodisplayInIndex.longValue());
            waterChargeDocument.setUsage(waterConnectionDetails.getUsageType().getName());
            waterChargeDocument.setConsumerCode(waterConnectionDetails.getConnection().getConsumerCode());
            waterChargeDocument.setWaterSource(waterConnectionDetails.getWaterSource().getWaterSourceType());
            waterChargeDocument.setPropertyType(waterConnectionDetails.getPropertyType().getName());
            waterChargeDocument.setCategory(waterConnectionDetails.getCategory().getName());
            waterChargeDocument.setCityName(defaultString((String) cityInfo.get(CITY_NAME_KEY)));
            waterChargeDocument.setCityCode(defaultString((String) cityInfo.get(cityService.getCityCode())));

            waterChargeDocument.setSumpCapacity(waterConnectionDetails.getSumpCapacity());
            waterChargeDocument.setPipeSize(waterConnectionDetails.getPipeSize().getCode());
            waterChargeDocument.setNumberOfPerson(waterConnectionDetails.getNumberOfPerson() != null
                    ? Long.valueOf(waterConnectionDetails.getNumberOfPerson()) : 0l);
            waterChargeDocument
                    .setCurrentDue(waterConnectionDetailsService.getTotalAmountTillCurrentFinYear(waterConnectionDetails)
                            .subtract(
                                    waterConnectionDetailsService.getTotalAmountTillPreviousFinYear(waterConnectionDetails))
                            .longValue());
            waterChargeDocument.setArrearsDue(
                    waterConnectionDetailsService.getTotalAmountTillPreviousFinYear(waterConnectionDetails).longValue());
            waterChargeDocument
                    .setCurrentDemand(waterConnectionDetailsService.getTotalDemandTillCurrentFinYear(waterConnectionDetails)
                            .subtract(waterConnectionDetailsService.getArrearsDemand(waterConnectionDetails)).longValue());
            waterChargeDocument
                    .setArrearsDemand(waterConnectionDetailsService.getArrearsDemand(waterConnectionDetails).longValue());
            waterChargeDocument.setMonthlyRate(monthlyRate);
            waterChargeDocument.setConsumerName(consumername);
            waterChargeDocument.setAadhaarNumber(aadharNumber);
            waterChargeDocument.setWardlocation(commonWardlocationField(assessmentDetails));
            waterChargeDocument.setPropertylocation(commonPropertylocationField(assessmentDetails));
        } catch (final ParseException exp) {
            LOGGER.error("Exception parsing Date " + exp.getMessage());
        }
        createWaterChargeDocument(waterChargeDocument);
        return waterChargeDocument;

    }

    private Long monthlyRateFirld(final WaterConnectionDetails waterConnectionDetails) {
        Long monthlyRate;
        if (connectionDemandService.getWaterRatesDetailsForDemandUpdate(waterConnectionDetails) != null
                && connectionDemandService.getWaterRatesDetailsForDemandUpdate(waterConnectionDetails)
                        .getMonthlyRate() != null)
            monthlyRate = new BigDecimal(connectionDemandService
                    .getWaterRatesDetailsForDemandUpdate(waterConnectionDetails).getMonthlyRate()).longValue();
        else
            monthlyRate = BigDecimal.ZERO.longValue();
        return monthlyRate;
    }

    // need to add TotalDemand and TotalCollection for both update and create
    // watertax Index
    public WaterChargeDocument createWaterChargeIndex(final WaterConnectionDetails waterConnectionDetails,
            final AssessmentDetails assessmentDetails, final BigDecimal amountTodisplayInIndex) {
        WaterChargeDocument waterChargeDocument = null;
        final Map<String, Object> cityInfo = cityService.cityDataAsMap();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(ES_DATE_FORMAT);

        if (waterConnectionDetails.getConnection() != null
                && waterConnectionDetails.getConnection().getConsumerCode() != null)
            waterChargeDocument = waterChargeIndexRepository.findByConsumerCodeAndCityName(
                    waterConnectionDetails.getConnection().getConsumerCode(),
                    defaultString((String) cityInfo.get(CITY_NAME_KEY)));
        if (waterChargeDocument == null) {
            Iterator<OwnerName> ownerNameItr = assessmentDetails.getOwnerNames().iterator();
            Long monthlyRate = null;
            String consumername = "";
            String aadharNumber = "";
            String mobilNumber = "";
            String createdDateStr = "";
            monthlyRate = monthlyRateFirld(waterConnectionDetails);
            if (ownerNameItr != null && ownerNameItr.hasNext())
                ownerNameItr.next().getMobileNumber();
            ownerNameItr = assessmentDetails.getOwnerNames().iterator();
            if (ownerNameItr.hasNext()) {
                final OwnerName ownerName = ownerNameItr.next();
                consumername = ownerName.getOwnerName();
                mobilNumber = ownerName.getMobileNumber();
                aadharNumber = ownerName.getAadhaarNumber() != null ? ownerName.getAadhaarNumber() : "";
                while (ownerNameItr.hasNext()) {
                    final OwnerName multipleOwner = ownerNameItr.next();
                    consumername = consumername.concat(",".concat(multipleOwner.getOwnerName()));
                    aadharNumber = aadharNumber.concat(","
                            .concat(multipleOwner.getAadhaarNumber() != null ? multipleOwner.getAadhaarNumber() : ""));
                }
            }
            if (waterConnectionDetails.getExecutionDate() != null)
                createdDateStr = dateFormatter.format(waterConnectionDetails.getExecutionDate());
            try {
                waterChargeDocument = WaterChargeDocument.builder()
                        .withZone(assessmentDetails.getBoundaryDetails().getZoneName())
                        .withRevenueWard(assessmentDetails.getBoundaryDetails().getWardName())
                        .withAdminward(assessmentDetails.getBoundaryDetails().getAdminWardName())
                        .withDoorNo(assessmentDetails.getHouseNo())
                        .withTotaldue(assessmentDetails.getPropertyDetails().getTaxDue().longValue())
                        .withLegacy(waterConnectionDetails.getLegacy())
                        .withCityGrade(defaultString((String) cityInfo.get(CITY_CORP_GRADE_KEY)))
                        .withRegionname(defaultString((String) cityInfo.get(CITY_REGION_NAME_KEY)))
                        .withClosureType(waterConnectionDetails.getCloseConnectionType() != null
                                ? waterConnectionDetails.getCloseConnectionType() : "")
                        .withLocality(assessmentDetails.getBoundaryDetails().getLocalityName() != null
                                ? assessmentDetails.getBoundaryDetails().getLocalityName() : "")
                        .withPropertyid(waterConnectionDetails.getConnection().getPropertyIdentifier())
                        .withApplicationcode(waterConnectionDetails.getApplicationType().getCode())
                        .withCreatedDate(createdDateStr.isEmpty()? new Date():dateFormatter.parse(createdDateStr) )
                        .withMobileNumber(mobilNumber)
                        .withStatus(waterConnectionDetails.getConnectionStatus().name())
                        .withDistrictName(defaultString((String) cityInfo.get(CITY_DIST_NAME_KEY)))
                        .withConnectiontype(waterConnectionDetails.getConnectionType().name())
                        .withWaterTaxDue(amountTodisplayInIndex.longValue())
                        .withUsage(waterConnectionDetails.getUsageType().getName())
                        .withConsumerCode(waterConnectionDetails.getConnection().getConsumerCode())
                        .withWatersource(waterConnectionDetails.getWaterSource().getWaterSourceType())
                        .withPropertytype(waterConnectionDetails.getPropertyType().getName())
                        .withCategory(waterConnectionDetails.getCategory().getName())
                        .withCityName(defaultString((String) cityInfo.get(CITY_NAME_KEY)))
                        .withCityCode(defaultString((String) cityInfo.get(cityService.getCityCode())))
                        .withSumpcapacity(waterConnectionDetails.getSumpCapacity())
                        .withPipesize(waterConnectionDetails.getPipeSize().getCode())
                        .withNumberOfPerson(waterConnectionDetails.getNumberOfPerson() != null
                                ? Long.valueOf(waterConnectionDetails.getNumberOfPerson()) : 0l)
                        .withCurrentDue(
                                waterConnectionDetailsService.getTotalAmountTillCurrentFinYear(waterConnectionDetails)
                                        .subtract(waterConnectionDetailsService
                                                .getTotalAmountTillPreviousFinYear(waterConnectionDetails))
                                        .longValue())
                        .withArrearsDue(waterConnectionDetailsService
                                .getTotalAmountTillPreviousFinYear(waterConnectionDetails).longValue())
                        .withCurrentDemand(
                                waterConnectionDetailsService.getTotalDemandTillCurrentFinYear(waterConnectionDetails)
                                        .subtract(
                                                waterConnectionDetailsService.getArrearsDemand(waterConnectionDetails))
                                        .longValue())
                        .withArrearsDemand(
                                waterConnectionDetailsService.getArrearsDemand(waterConnectionDetails).longValue())
                        .withMonthlyRate(monthlyRate).withConsumername(consumername).withAadhaarnumber(aadharNumber)
                        .withWardlocation(commonWardlocationField(assessmentDetails))
                        .withPropertylocation(commonPropertylocationField(assessmentDetails)).build();
            } catch (final ParseException exp) {
                LOGGER.error("Exception parsing Date " + exp.getMessage());
            }
            createWaterChargeDocument(waterChargeDocument);
        } else
            updateWaterChargeIndex(waterChargeDocument, waterConnectionDetails, assessmentDetails,
                    amountTodisplayInIndex);
        return waterChargeDocument;
    }

    public GeoPoint commonWardlocationField(final AssessmentDetails assessmentDetails) {
        GeoPoint wardlocation = null;
        if (assessmentDetails.getBoundaryDetails().getAdminWardId() != null) {
            final Boundary adminBoundary = boundaryService
                    .getBoundaryById(assessmentDetails.getBoundaryDetails().getAdminWardId());
            if (adminBoundary.getLatitude() != null && !adminBoundary.getLatitude().isNaN()
                    && adminBoundary.getLongitude() != null && !adminBoundary.getLongitude().isNaN())
                wardlocation = new GeoPoint(adminBoundary.getLatitude(), adminBoundary.getLongitude());
            else
                wardlocation = new GeoPoint(0, 0);
        }
        return wardlocation;
    }

    public GeoPoint commonPropertylocationField(final AssessmentDetails assessmentDetails) {
        GeoPoint propertylocation = null;
        if (assessmentDetails.getLatitude() != 0.0 && assessmentDetails.getLongitude() != 0.0)
            propertylocation = new GeoPoint(assessmentDetails.getLatitude(), assessmentDetails.getLongitude());
        return propertylocation;
    }

    @Transactional
    public WaterChargeDocument createWaterChargeDocument(final WaterChargeDocument waterChargeIndex) {
        waterChargeIndexRepository.save(waterChargeIndex);
        return waterChargeIndex;
    }

}