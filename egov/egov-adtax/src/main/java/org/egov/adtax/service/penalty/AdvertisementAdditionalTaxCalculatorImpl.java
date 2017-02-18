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

package org.egov.adtax.service.penalty;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.adtax.entity.AdvertisementAdditionalTaxRate;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.service.AdvertisementAdditinalTaxRateService;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdvertisementAdditionalTaxCalculatorImpl implements AdvertisementAdditionalTaxCalculator {

    private @Autowired AppConfigValueService appConfigValuesService;

    @Autowired
    private AdvertisementAdditinalTaxRateService advertisementAdditinalTaxRateService;

    private Boolean serviceTaxAndCessCalculationRequired() {

        final AppConfigValues isServiceTaxAndCessCollectionRequired = appConfigValuesService.getConfigValuesByModuleAndKey(
                AdvertisementTaxConstants.MODULE_NAME, AdvertisementTaxConstants.SERVICETAXANDCESSCOLLECTIONREQUIRED).get(0);

        if (isServiceTaxAndCessCollectionRequired != null
                && "YES".equalsIgnoreCase(isServiceTaxAndCessCollectionRequired.getValue()))
            return true;
        return false;

    }
/**
 * will return demand reason code and tax amount.
 */
    @Override
    public Map<String, BigDecimal> getAdditionalTaxes(final AdvertisementPermitDetail advPermitDetail) {

        Map<String, BigDecimal> additionalTaxesMasterList = new HashMap<String, BigDecimal>();

        final Map<String, BigDecimal> additionalTaxes = new HashMap<String, BigDecimal>();
        BigDecimal totalTaxableAmount = BigDecimal.ZERO;
        if (serviceTaxAndCessCalculationRequired()) {

            additionalTaxesMasterList = getAdditionalTaxRates();
            totalTaxableAmount = getTotalTaxAmountForAdditionalTaxCalculation(advPermitDetail, additionalTaxesMasterList);
            if (totalTaxableAmount != null && totalTaxableAmount.compareTo(BigDecimal.ZERO) > 0) {
                for (final Map.Entry<String, BigDecimal> entry : additionalTaxesMasterList.entrySet())
                    additionalTaxes.put(entry.getKey(), totalTaxableAmount
                            .multiply(entry.getValue())
                            .divide(BigDecimal.valueOf(100)).setScale(4, BigDecimal.ROUND_HALF_UP));

                if (additionalTaxes != null && additionalTaxes.size() > 0)
                    for (final Map.Entry<String, BigDecimal> additionalTax : additionalTaxes.entrySet())
                        additionalTax.setValue(additionalTax.getValue().setScale(0, BigDecimal.ROUND_HALF_UP));
            }
        }

        return additionalTaxes;

    }

    private Map<String, BigDecimal> getAdditionalTaxRates() {
        final Map<String, BigDecimal> additionalTaxes = new HashMap<String, BigDecimal>();

        final List<AdvertisementAdditionalTaxRate> additionalTaxRates = advertisementAdditinalTaxRateService
                .getAllActiveAdditinalTaxRates();

        for (final AdvertisementAdditionalTaxRate taxRates : additionalTaxRates)
            additionalTaxes.put(taxRates.getReasonCode(), taxRates.getPercentage());
        return additionalTaxes;
    }

    private BigDecimal getTotalTaxAmountForAdditionalTaxCalculation(final AdvertisementPermitDetail advPermitDetail,
            final Map<String, BigDecimal> additionalTaxes) {
        BigDecimal totalTaxableAmount = BigDecimal.ZERO;
        BigDecimal creaditAmt;
        if (advPermitDetail != null && advPermitDetail.getAdvertisement() != null
                && advPermitDetail.getAdvertisement().getDemandId() != null)
            for (final EgDemandDetails demandDtl : advPermitDetail.getAdvertisement().getDemandId()
                    .getEgDemandDetails()) {

                creaditAmt = demandDtl.getAmount().subtract(demandDtl.getAmtCollected());

                if (creaditAmt.compareTo(BigDecimal.ZERO) > 0
                        && (!AdvertisementTaxConstants.DEMANDREASON_PENALTY.equalsIgnoreCase(demandDtl
                                .getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()) ||
                                !additionalTaxes.containsKey(demandDtl
                                        .getEgDemandReason().getEgDemandReasonMaster().getCode())))
                    totalTaxableAmount = totalTaxableAmount.add(creaditAmt);

            }
        return totalTaxableAmount;
    }

    private Map<Installment, BigDecimal>  getTotalTaxAmountByInstallmentForAdditionalTaxCalculation(final AdvertisementPermitDetail advPermitDetail,
            final Map<String, BigDecimal> additionalTaxes) {
        final Map<Installment, BigDecimal> totalTaxableAmount = new HashMap<Installment, BigDecimal>();

        if (advPermitDetail != null && advPermitDetail.getAdvertisement() != null
                && advPermitDetail.getAdvertisement().getDemandId() != null)
            for (final EgDemandDetails demandDtl : advPermitDetail.getAdvertisement().getDemandId()
                    .getEgDemandDetails()) {

                if (demandDtl.getBalance().compareTo(BigDecimal.ZERO) > 0
                        && (!AdvertisementTaxConstants.DEMANDREASON_PENALTY.equalsIgnoreCase(demandDtl
                                .getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()) ||
                                !additionalTaxes.containsKey(demandDtl
                                        .getEgDemandReason().getEgDemandReasonMaster().getCode()))) {
                    if (totalTaxableAmount.get(demandDtl.getEgDemandReason().getEgInstallmentMaster()) == null)
                        totalTaxableAmount.put(demandDtl.getEgDemandReason().getEgInstallmentMaster(), demandDtl.getBalance());
                    else
                        totalTaxableAmount.put(demandDtl.getEgDemandReason().getEgInstallmentMaster(), totalTaxableAmount
                                .get(demandDtl.getEgDemandReason().getEgInstallmentMaster()).add(demandDtl.getBalance()));

                }

            }
        return totalTaxableAmount;
    }
    /**
     * Based on demand detail pending balance, additional fee will be calculated.
     */
    @Override
    public BigDecimal getAdditionalTaxAmountByPassingDemandDetailAndAdditionalTaxes(final EgDemandDetails demandDtl,
            final List<AdvertisementAdditionalTaxRate> additionalTaxRates) {
        BigDecimal totalTaxableAmount = BigDecimal.ZERO;
        final Map<String, BigDecimal> additionalTaxes = new HashMap<String, BigDecimal>();

        if (serviceTaxAndCessCalculationRequired()) {
            for (final AdvertisementAdditionalTaxRate taxRates : additionalTaxRates)
                additionalTaxes.put(taxRates.getReasonCode(), taxRates.getPercentage());

            for (final AdvertisementAdditionalTaxRate taxRates : additionalTaxRates)
                if (demandDtl.getBalance().compareTo(BigDecimal.ZERO) > 0
                        && (!AdvertisementTaxConstants.DEMANDREASON_PENALTY.equalsIgnoreCase(demandDtl
                                .getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()) ||
                                !additionalTaxes.containsKey(demandDtl
                                        .getEgDemandReason().getEgDemandReasonMaster().getCode())))
                    totalTaxableAmount = totalTaxableAmount.add(demandDtl.getBalance()
                            .multiply(taxRates.getPercentage())
                            .divide(BigDecimal.valueOf(100)).setScale(4, BigDecimal.ROUND_HALF_UP));
        }
        return totalTaxableAmount;
    }

    @Override
    public BigDecimal getTotalAdditionalTaxesByPassingAdvertisementPermit(final AdvertisementPermitDetail advPermitDetail) {

        BigDecimal totalAdditionalTax = BigDecimal.ZERO;
        BigDecimal totalTaxableAmount = BigDecimal.ZERO;
        Map<String, BigDecimal> additionalTaxesMasterList = new HashMap<String, BigDecimal>();

        if (serviceTaxAndCessCalculationRequired()) {

            additionalTaxesMasterList = getAdditionalTaxRates();
            totalTaxableAmount = getTotalTaxAmountForAdditionalTaxCalculation(advPermitDetail, additionalTaxesMasterList);
            if (totalTaxableAmount != null && totalTaxableAmount.compareTo(BigDecimal.ZERO) > 0)
                for (final Map.Entry<String, BigDecimal> entry : additionalTaxesMasterList.entrySet())
                    totalAdditionalTax = totalAdditionalTax.add(totalTaxableAmount
                            .multiply(entry.getValue())
                            .divide(BigDecimal.valueOf(100)).setScale(4, BigDecimal.ROUND_HALF_UP));
        }

        return totalAdditionalTax;

    }
    @Override
    public Map<Installment, BigDecimal> getAdditionalTaxesByInstallment(AdvertisementPermitDetail activeAdvertisementPermit) {

        Map<String, BigDecimal> additionalTaxesMasterList = new HashMap<String, BigDecimal>();

        final Map<Installment, BigDecimal> additionalTaxes = new HashMap<Installment, BigDecimal>();
        Map<Installment, BigDecimal> installmentWiseTaxableAmtMap = new HashMap<Installment, BigDecimal>();
        if (serviceTaxAndCessCalculationRequired()) {

            additionalTaxesMasterList = getAdditionalTaxRates();
            installmentWiseTaxableAmtMap = getTotalTaxAmountByInstallmentForAdditionalTaxCalculation(activeAdvertisementPermit,
                    additionalTaxesMasterList);
            if (installmentWiseTaxableAmtMap != null) {
                for (final Map.Entry<Installment, BigDecimal> taxableAmtObj : installmentWiseTaxableAmtMap.entrySet()) {
                    BigDecimal tax = BigDecimal.ZERO;
                    for (final Map.Entry<String, BigDecimal> entry : additionalTaxesMasterList.entrySet()) {
                        tax = tax.add(entry.getValue().multiply(taxableAmtObj.getValue()).divide(BigDecimal.valueOf(100))
                                .setScale(4, BigDecimal.ROUND_HALF_UP));
                    }
                    additionalTaxes.put(taxableAmtObj.getKey(), tax); // For each installment calculate total amount
                }

                if (additionalTaxes != null && additionalTaxes.size() > 0)
                    for (final Map.Entry<Installment, BigDecimal> additionalTax : additionalTaxes.entrySet())
                        additionalTax.setValue(additionalTax.getValue().setScale(0, BigDecimal.ROUND_HALF_UP));
            }
        }

        return additionalTaxes;

    }

}
