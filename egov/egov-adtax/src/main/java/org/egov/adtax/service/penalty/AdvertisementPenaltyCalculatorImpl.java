/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2016>  eGovernments Foundation

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
package org.egov.adtax.service.penalty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.service.AdvertisementPenaltyRatesService;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdvertisementPenaltyCalculatorImpl implements AdvertisementPenaltyCalculator {

    @Autowired
    private AdvertisementPenaltyRatesService advertisementPenaltyRatesService;

    private @Autowired AppConfigValueService appConfigValuesService;

    @Override
    public BigDecimal calculatePenaltyByAdvertisementPermit(AdvertisementPermitDetail advPermitDetail) {
        double percentage = 0;
        int days = 0;

        BigDecimal penaltyAmt = BigDecimal.ZERO;

        final AppConfigValues ispenaltyCalculationRequired = penaltyCalculationRequired();

        if (ispenaltyCalculationRequired != null && "YES".equalsIgnoreCase(ispenaltyCalculationRequired.getValue())) {

            if (advPermitDetail != null && advPermitDetail.getAdvertisement() != null
                    && advPermitDetail.getAdvertisement().getDemandId() != null) {
                for (final EgDemandDetails demandDtl : advPermitDetail.getAdvertisement().getDemandId()
                        .getEgDemandDetails()) {

                    if (demandDtl.getAmount().subtract(demandDtl.getAmtCollected()).compareTo(BigDecimal.ZERO) > 0) {

                        days = calculateNumberOfDaysForPenaltyCalculation(advPermitDetail, demandDtl);

                        percentage = advertisementPenaltyRatesService
                                .findPenaltyRatesByNumberOfDays(Long.valueOf(days));
                        if (percentage > 0) {
                            penaltyAmt = penaltyAmt.add(demandDtl.getAmount().subtract(demandDtl.getAmtCollected())
                                    .multiply(BigDecimal.valueOf(percentage))
                                    .divide(BigDecimal.valueOf(100).setScale(0, BigDecimal.ROUND_HALF_UP))
                                    .setScale(0, BigDecimal.ROUND_HALF_UP));
                        }

                    }
                }
            }
        }
        return penaltyAmt;
    }

    private int calculateNumberOfDaysForPenaltyCalculation(AdvertisementPermitDetail advPermitDetail,
            final EgDemandDetails demandDtl) {
        int days = 0;
        // Eg: Next year installment
        if (demandDtl.getEgDemandReason().getEgInstallmentMaster().getFromDate().after(new Date())) {
            days = Days.daysBetween(new DateTime(demandDtl.getEgDemandReason().getEgInstallmentMaster().getFromDate()),
                    new DateTime(new Date())).getDays();

        } else if (demandDtl.getEgDemandReason().getEgInstallmentMaster().getFromDate().before(new Date())) {
            // Penalty calculation date or application date in
            // current year.
            // Decided based on penalty calculation date
            if (advPermitDetail.getAdvertisement().getPenaltyCalculationDate() != null
                    && demandDtl.getEgDemandReason().getEgInstallmentMaster().getFromDate()
                            .before(advPermitDetail.getAdvertisement().getPenaltyCalculationDate())
                    && (demandDtl.getEgDemandReason().getEgInstallmentMaster().getToDate()
                            .equals(advPermitDetail.getAdvertisement().getPenaltyCalculationDate()) || demandDtl.getEgDemandReason().getEgInstallmentMaster().getToDate()
                            .after(advPermitDetail.getAdvertisement().getPenaltyCalculationDate()) ) ) {
                days = Days.daysBetween(new DateTime(advPermitDetail.getAdvertisement().getPenaltyCalculationDate()),
                        new DateTime(new Date())).getDays();

            } else {
                days = Days.daysBetween(
                        new DateTime(demandDtl.getEgDemandReason().getEgInstallmentMaster().getFromDate()),
                        new DateTime(new Date())).getDays();

            }

        }
        return days;
    }

    @Override
    public Map<Installment, BigDecimal> getPenaltyByInstallment(AdvertisementPermitDetail advPermitDetail) {
        Map<Installment, BigDecimal> penaltyReasons = new HashMap<Installment, BigDecimal>();
        BigDecimal penaltyAmt = BigDecimal.ZERO;
        double percentage = 0;
        int days = 0;

        final AppConfigValues ispenaltyCalculationRequired = penaltyCalculationRequired();

        if (ispenaltyCalculationRequired != null && "YES".equalsIgnoreCase(ispenaltyCalculationRequired.getValue())) {

            if (advPermitDetail != null && advPermitDetail.getAdvertisement() != null
                    && advPermitDetail.getAdvertisement().getDemandId() != null) {

                for (final EgDemandDetails demandDtl : advPermitDetail.getAdvertisement().getDemandId()
                        .getEgDemandDetails()) {

                    penaltyAmt = BigDecimal.ZERO;

                    if (demandDtl.getAmount().subtract(demandDtl.getAmtCollected()).compareTo(BigDecimal.ZERO) > 0) {
                        final BigDecimal amount = demandDtl.getAmount().subtract(demandDtl.getAmtCollected());

                        days = calculateNumberOfDaysForPenaltyCalculation(advPermitDetail, demandDtl);

                        percentage = advertisementPenaltyRatesService
                                .findPenaltyRatesByNumberOfDays(Long.valueOf(days));

                        if (percentage > 0) {
                            penaltyAmt = penaltyAmt.add(demandDtl.getAmount().subtract(demandDtl.getAmtCollected())
                                    .multiply(BigDecimal.valueOf(percentage))
                                    .divide(BigDecimal.valueOf(100).setScale(0, BigDecimal.ROUND_HALF_UP))
                                    .setScale(0, BigDecimal.ROUND_HALF_UP));
                        }

                        if (penaltyReasons.get(demandDtl.getEgDemandReason().getEgInstallmentMaster()) == null)
                            penaltyReasons.put(demandDtl.getEgDemandReason().getEgInstallmentMaster(), penaltyAmt);
                        else
                            penaltyReasons.put(demandDtl.getEgDemandReason().getEgInstallmentMaster(), penaltyReasons
                                    .get(demandDtl.getEgDemandReason().getEgInstallmentMaster()).add(penaltyAmt));

                    }
                }
            }
        }
        return penaltyReasons;
    }

    private AppConfigValues penaltyCalculationRequired() {
        return appConfigValuesService.getConfigValuesByModuleAndKey(AdvertisementTaxConstants.MODULE_NAME,
                AdvertisementTaxConstants.PENALTYCALCULATIONREQUIRED).get(0);

    }

}
