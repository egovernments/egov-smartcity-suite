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

package org.egov.adtax.service.es;

import java.math.BigDecimal;
import java.util.Map;

import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.es.AdvertisementIndex;
import org.egov.adtax.repository.es.AdvertisementIndexRepository;
import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.commons.entity.Source;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdvertisementIndexService {

    private static final String DEMAND_AMOUNT = "demandAmount";

    private static final String COLLECTED_AMOUNT = "collectedAmount";

    private static final String TOTAL_AMOUNT = "totalAmount";

    private static final String TOTAL_AMOUNT_COLLECTED = "totalAmountCollected";

    private static final String TOTAL = "Total";

    @Autowired
    private CityService cityService;

    @Autowired
    private AdvertisementIndexRepository advertisementIndexRepository;

    @Autowired
    private AdvertisementDemandService advertisementDemandService;

    public AdvertisementIndex createAdvertisementIndex(final AdvertisementPermitDetail advertisementPermitDetailIndex) {
        AdvertisementIndex advertisementSearch = null;
        if (advertisementPermitDetailIndex.getStatus() != null &&
                (advertisementPermitDetailIndex.getStatus().getCode()
                        .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_APPROVED)
                        || advertisementPermitDetailIndex.getStatus().getCode()
                                .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXAMOUNTPAID)
                        || advertisementPermitDetailIndex.getStatus().getCode()
                                .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXPERMITGENERATED)
                        || advertisementPermitDetailIndex.getStatus().getCode()
                                .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_CANCELLED)))
            advertisementSearch = createOrUpdateAdvIndex(advertisementPermitDetailIndex);
        return advertisementSearch;
    }

    public AdvertisementIndex createOrUpdateAdvIndex(final AdvertisementPermitDetail advertisementPermitDetail) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        final AdvertisementIndex advertisementSearch = new AdvertisementIndex(
                advertisementPermitDetail.getAdvertisement().getAdvertisementNumber(),
                cityWebsite.getName(), cityWebsite.getCode(), advertisementPermitDetail.getCreatedDate(),
                cityWebsite.getDistrictName(), cityWebsite.getRegionName(),
                cityWebsite.getGrade());
        advertisementSearch.setId(cityWebsite.getCode().concat("-").concat(advertisementPermitDetail.getApplicationNumber()));
        advertisementSearch.setAddress(getAddress(advertisementPermitDetail));
        advertisementSearch.setAdvertisementClass(advertisementPermitDetail.getAdvertisement().getRateClass().getDescription());
        advertisementSearch.setAdvertisementCreatedBy(advertisementPermitDetail.getAdvertisement().getCreatedBy().getName());
        advertisementSearch.setAdvertisement_duration(advertisementPermitDetail.getAdvertisementDuration().name());
        advertisementSearch.setAdvertisementNumber(advertisementPermitDetail.getAdvertisement().getAdvertisementNumber());

        advertisementSearch.setAdvertisement_status(advertisementPermitDetail.getAdvertisement().getStatus().name());
        advertisementSearch.setType(advertisementPermitDetail.getAdvertisement().getType().name());
        advertisementSearch.setAdvertiser(getAdvertiser(advertisementPermitDetail));
        advertisementSearch.setAdvertiserParticular(getAdvertiserParticular(advertisementPermitDetail));
        advertisementSearch.setAgencyName(getAgencyName(advertisementPermitDetail));
        advertisementSearch.setApplicationDate(advertisementPermitDetail.getApplicationDate());

        advertisementSearch.setApplicationNumber(advertisementPermitDetail.getApplicationNumber());

        advertisementSearch.setBlock(getBlockName(advertisementPermitDetail));
        advertisementSearch
                .setBreadth(getBreadth(advertisementPermitDetail));
        advertisementSearch.setCategory(advertisementPermitDetail.getAdvertisement().getCategory().getName());

        advertisementSearch.setCreatedDate(advertisementPermitDetail.getAdvertisement().getCreatedDate());
        advertisementSearch.setElectionWard(getElectionWard(advertisementPermitDetail));
        advertisementSearch
                .setElectricityServiceNumber(getElectricityServiceNumber(advertisementPermitDetail));
        advertisementSearch.setEncroachmentFee(getEncroachmentFee(advertisementPermitDetail));
        advertisementSearch.setIslegacy(advertisementPermitDetail.getAdvertisement().getLegacy());
        advertisementSearch.setLength(advertisementPermitDetail.getLength());
        advertisementSearch.setLocality(getLocalityName(advertisementPermitDetail));
        advertisementSearch.setMeasurement(advertisementPermitDetail.getMeasurement());
        advertisementSearch.setMobileNumber(getMobileNumber(advertisementPermitDetail));
        advertisementSearch.setOwnerDetail(getOwnerDetail(advertisementPermitDetail));

        advertisementSearch.setPermissionEndDate(advertisementPermitDetail.getPermissionenddate());
        advertisementSearch.setPermissionNumber(advertisementPermitDetail.getPermissionNumber());

        advertisementSearch.setPermissionStartDate(advertisementPermitDetail.getPermissionstartdate());
        advertisementSearch.setPermitStatus(advertisementPermitDetail.getStatus().getDescription());
        advertisementSearch.setAssessmentNumber(getAssessmentNumber(advertisementPermitDetail));
        advertisementSearch.setPropertyType(advertisementPermitDetail.getAdvertisement().getPropertyType().name());
        advertisementSearch.setRevenueInspector(advertisementPermitDetail.getAdvertisement().getRevenueInspector().getName());
        advertisementSearch.setStreet(getStreetName(advertisementPermitDetail));
        advertisementSearch.setSubCategory(advertisementPermitDetail.getAdvertisement().getSubCategory().getCode());
        advertisementSearch.setTaxAmount(advertisementPermitDetail.getTaxAmount());
        advertisementSearch.setTotalHeight(getTotalHeight(advertisementPermitDetail));
        advertisementSearch.setUom(advertisementPermitDetail.getUnitOfMeasure().getCode());
        advertisementSearch.setWard(getWardName(advertisementPermitDetail));
        advertisementSearch.setWidth(getWidth(advertisementPermitDetail));
        String consumerName = getConsumerName(advertisementPermitDetail);
        advertisementSearch.setConsumerName(consumerName);
        // added to support dashboard reports
        advertisementSearch.setConsumerName_Clauses(consumerName);
        advertisementSearch.setConsumerNumber(advertisementPermitDetail.getApplicationNumber());
        advertisementSearch.setAdvertisementNumber_Clauses(advertisementPermitDetail.getAdvertisement().getAdvertisementNumber());
        advertisementSearch.setPermissionNumber_Clauses(advertisementPermitDetail.getPermissionNumber());
        advertisementSearch.setAgencyName_Clauses(
                getAgencyName(advertisementPermitDetail));
        advertisementSearch.setSource(getSource(advertisementPermitDetail));

        // Demand and Collection Details
        advertisementSearch.setTax_demand(advertisementPermitDetail.getAdvertisement().getDemandId().getBaseDemand());
        advertisementSearch.setTax_collected(advertisementPermitDetail.getAdvertisement().getDemandId().getAmtCollected());

        Map<String, Map<String, BigDecimal>> demandCollectionMap = advertisementDemandService
                .getReasonWiseDemandAndCollection(advertisementPermitDetail);

        advertisementSearch.setEncroachmentfee_demand(getEncroachmentFeeDemand(demandCollectionMap));

        advertisementSearch.setEncroachmentfee_collected(getEncroachmentFeeCollected(demandCollectionMap));

        advertisementSearch.setArrears_demand(getArrearsDemand(demandCollectionMap));

        advertisementSearch.setArrears_collected(getArrearsCollected(demandCollectionMap));

        advertisementSearch.setPenalty_demand(getPenaltyDemand(demandCollectionMap));

        advertisementSearch.setPenalty_collected(getPenaltyCollected(demandCollectionMap));

        advertisementSearch.setTotalamount(getToalAmount(demandCollectionMap));

        advertisementSearch.setTotalamountcollected(getTotalAmountcollected(demandCollectionMap));

        advertisementSearch
                .setTotalbalance(advertisementSearch.getTotalamount().subtract(advertisementSearch.getTotalamountcollected()));
        if (advertisementPermitDetail.getAdvertisement().getLatitude() != 0.0
                && advertisementPermitDetail.getAdvertisement().getLongitude() != 0.0)
            advertisementSearch.setAdvertisementLocation(new GeoPoint(advertisementPermitDetail.getAdvertisement().getLatitude(),
                    advertisementPermitDetail.getAdvertisement().getLongitude()));

        // Deactivation
        if (advertisementPermitDetail.getAdvertisement().getStatus().name().equalsIgnoreCase("INACTIVE")) {

            advertisementSearch.setDeactivationDate(advertisementPermitDetail.getDeactivationDate());
            advertisementSearch.setDeactivationRemarks(advertisementPermitDetail.getDeactivationRemarks());
        }
        advertisementIndexRepository.save(advertisementSearch);
        return advertisementSearch;
    }

    private String getSource(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getSource() == null ? Source.SYSTEM.toString() : advertisementPermitDetail.getSource();
    }

    private String getConsumerName(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getAgency() != null ? advertisementPermitDetail.getAgency().getName()
                : advertisementPermitDetail.getOwnerDetail();
    }

    private double getWidth(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getWidth() != null ? advertisementPermitDetail.getWidth() : 0.0;
    }

    private String getWardName(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getAdvertisement().getWard() != null
                ? advertisementPermitDetail.getAdvertisement().getWard().getName()
                : "";
    }

    private double getTotalHeight(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getTotalHeight() != null ? advertisementPermitDetail.getTotalHeight() : 0.0;
    }

    private String getStreetName(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getAdvertisement().getStreet() != null
                ? advertisementPermitDetail.getAdvertisement().getStreet().getName()
                : "";
    }

    private String getAssessmentNumber(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getAdvertisement().getPropertyNumber() != null
                ? advertisementPermitDetail.getAdvertisement().getPropertyNumber()
                : "";
    }

    private String getOwnerDetail(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getOwnerDetail() != null ? advertisementPermitDetail.getOwnerDetail() : "";
    }

    private String getMobileNumber(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getAgency() != null ? advertisementPermitDetail.getAgency().getMobileNumber() : "";
    }

    private String getLocalityName(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getAdvertisement().getLocality() != null
                ? advertisementPermitDetail.getAdvertisement().getLocality().getName()
                : "";
    }

    private BigDecimal getEncroachmentFee(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getEncroachmentFee() != null
                ? advertisementPermitDetail.getEncroachmentFee()
                : BigDecimal.ZERO;
    }

    private String getElectricityServiceNumber(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getAdvertisement().getElectricityServiceNumber() != null
                ? advertisementPermitDetail.getAdvertisement().getElectricityServiceNumber()
                : "";
    }

    private String getElectionWard(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getAdvertisement().getElectionWard() != null
                ? advertisementPermitDetail.getAdvertisement().getElectionWard().getName()
                : "";
    }

    private double getBreadth(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getBreadth() != null ? advertisementPermitDetail.getBreadth() : 0.0;
    }

    private String getBlockName(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getAdvertisement().getBlock() != null
                ? advertisementPermitDetail.getAdvertisement().getBlock().getName()
                : "";
    }

    private String getAgencyName(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getAgency() != null ? advertisementPermitDetail.getAgency().getName() : "";
    }

    private String getAdvertiserParticular(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getAdvertisementParticular() != null
                ? advertisementPermitDetail.getAdvertisementParticular()
                : "";
    }

    private String getAdvertiser(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getAdvertiser() != null ? advertisementPermitDetail.getAdvertiser() : "";
    }

    private String getAddress(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getAdvertisement().getAddress() != null
                ? advertisementPermitDetail.getAdvertisement().getAddress()
                : "";
    }

    private BigDecimal getEncroachmentFeeDemand(Map<String, Map<String, BigDecimal>> demandCollectionMap) {
        if (demandCollectionMap.get(AdvertisementTaxConstants.DEMANDREASON_ENCROCHMENTFEE) != null)
            return demandCollectionMap
                    .get(AdvertisementTaxConstants.DEMANDREASON_ENCROCHMENTFEE).get(DEMAND_AMOUNT) != null
                            ? demandCollectionMap.get(AdvertisementTaxConstants.DEMANDREASON_ENCROCHMENTFEE)
                                    .get(DEMAND_AMOUNT)
                            : BigDecimal.ZERO;
        return BigDecimal.ZERO;
    }

    private BigDecimal getEncroachmentFeeCollected(Map<String, Map<String, BigDecimal>> demandCollectionMap) {
        if (demandCollectionMap.get(AdvertisementTaxConstants.DEMANDREASON_ENCROCHMENTFEE) != null)
            return demandCollectionMap
                    .get(AdvertisementTaxConstants.DEMANDREASON_ENCROCHMENTFEE).get(COLLECTED_AMOUNT) != null
                            ? demandCollectionMap.get(AdvertisementTaxConstants.DEMANDREASON_ENCROCHMENTFEE)
                                    .get(COLLECTED_AMOUNT)
                            : BigDecimal.ZERO;
        return BigDecimal.ZERO;
    }

    private BigDecimal getArrearsDemand(Map<String, Map<String, BigDecimal>> demandCollectionMap) {
        if (demandCollectionMap.get(AdvertisementTaxConstants.DEMANDREASON_ARREAR_ADVERTISEMENTTAX) != null)
            return demandCollectionMap.get(AdvertisementTaxConstants.DEMANDREASON_ARREAR_ADVERTISEMENTTAX)
                    .get(DEMAND_AMOUNT) != null
                            ? demandCollectionMap.get(AdvertisementTaxConstants.DEMANDREASON_ARREAR_ADVERTISEMENTTAX)
                                    .get(DEMAND_AMOUNT)
                            : BigDecimal.ZERO;
        return BigDecimal.ZERO;
    }

    private BigDecimal getArrearsCollected(Map<String, Map<String, BigDecimal>> demandCollectionMap) {
        if (demandCollectionMap.get(AdvertisementTaxConstants.DEMANDREASON_ARREAR_ADVERTISEMENTTAX) != null)
            return demandCollectionMap.get(AdvertisementTaxConstants.DEMANDREASON_ARREAR_ADVERTISEMENTTAX)
                    .get(COLLECTED_AMOUNT) != null
                            ? demandCollectionMap.get(AdvertisementTaxConstants.DEMANDREASON_ARREAR_ADVERTISEMENTTAX)
                                    .get(COLLECTED_AMOUNT)
                            : BigDecimal.ZERO;
        return BigDecimal.ZERO;
    }

    private BigDecimal getPenaltyDemand(Map<String, Map<String, BigDecimal>> demandCollectionMap) {
        if (demandCollectionMap.get(AdvertisementTaxConstants.DEMANDREASON_PENALTY) != null)
            return demandCollectionMap.get(AdvertisementTaxConstants.DEMANDREASON_PENALTY).get(DEMAND_AMOUNT) != null
                    ? demandCollectionMap.get(AdvertisementTaxConstants.DEMANDREASON_PENALTY).get(DEMAND_AMOUNT)
                    : BigDecimal.ZERO;
        return BigDecimal.ZERO;
    }

    private BigDecimal getPenaltyCollected(Map<String, Map<String, BigDecimal>> demandCollectionMap) {
        if (demandCollectionMap.get(AdvertisementTaxConstants.DEMANDREASON_PENALTY) != null)
            return demandCollectionMap.get(AdvertisementTaxConstants.DEMANDREASON_PENALTY).get(COLLECTED_AMOUNT) != null
                    ? demandCollectionMap.get(AdvertisementTaxConstants.DEMANDREASON_PENALTY).get(COLLECTED_AMOUNT)
                    : BigDecimal.ZERO;
        return BigDecimal.ZERO;
    }

    private BigDecimal getToalAmount(Map<String, Map<String, BigDecimal>> demandCollectionMap) {
        if (demandCollectionMap.get(TOTAL) != null)
            return demandCollectionMap.get(TOTAL).get(TOTAL_AMOUNT) != null
                    ? demandCollectionMap.get(TOTAL).get(TOTAL_AMOUNT)
                    : BigDecimal.ZERO;
        return BigDecimal.ZERO;
    }

    private BigDecimal getTotalAmountcollected(Map<String, Map<String, BigDecimal>> demandCollectionMap) {
        if (demandCollectionMap.get(TOTAL) != null)
            return demandCollectionMap.get(TOTAL).get(TOTAL_AMOUNT_COLLECTED) != null
                    ? demandCollectionMap.get(TOTAL).get(TOTAL_AMOUNT_COLLECTED)
                    : BigDecimal.ZERO;

        return BigDecimal.ZERO;
    }

}
