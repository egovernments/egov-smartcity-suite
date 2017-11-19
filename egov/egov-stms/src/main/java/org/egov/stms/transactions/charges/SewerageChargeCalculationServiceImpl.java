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

package org.egov.stms.transactions.charges;

import java.math.BigDecimal;

import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.masters.service.DonationMasterService;
import org.egov.stms.masters.service.SewerageRatesMasterService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageConnectionDetail;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SewerageChargeCalculationServiceImpl implements SewerageChargeCalculationService {

    @Autowired
    private DonationMasterService donationMasterService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private SewerageRatesMasterService sewerageRatesMasterService;

    /**
     * @param sewerageApplicationDetails
     * @return This will return donation charges based on NoOfClosets and Property Type.
     */
    @Override
    public BigDecimal calculateDonationCharges(final SewerageApplicationDetails sewerageApplicationDetails) {
        BigDecimal amount = BigDecimal.ZERO;
        Integer noOfClosets;

        if (sewerageApplicationDetails.getConnection() != null
                && sewerageApplicationDetails.getConnectionDetail() != null) {
            final SewerageConnectionDetail sewerageConnectionDetail = sewerageApplicationDetails.getConnectionDetail();
            if (sewerageConnectionDetail != null
                    && sewerageConnectionDetail.getPropertyType().equals(PropertyType.MIXED)) {

                final BigDecimal amountForResidential = donationMasterService
                        .getDonationAmountByNoOfClosetsAndPropertytypeForCurrentDate(
                                sewerageConnectionDetail.getNoOfClosetsResidential(), PropertyType.RESIDENTIAL);
                final BigDecimal amountForNonResidential = donationMasterService
                        .getDonationAmountByNoOfClosetsAndPropertytypeForCurrentDate(
                                sewerageConnectionDetail.getNoOfClosetsNonResidential(), PropertyType.NON_RESIDENTIAL);
                if (amountForResidential != null)
                    return amountForNonResidential != null ? amountForResidential.add(amountForNonResidential)
                            : amountForResidential;
                else
                    return amountForNonResidential != null ? amountForNonResidential : BigDecimal.ZERO;
            } else {
                noOfClosets = sewerageConnectionDetail.getPropertyType().equals(PropertyType.RESIDENTIAL)
                        ? sewerageConnectionDetail
                                .getNoOfClosetsResidential()
                        : sewerageConnectionDetail.getPropertyType().equals(
                                PropertyType.NON_RESIDENTIAL) ? sewerageConnectionDetail.getNoOfClosetsNonResidential() : 0;
                amount = donationMasterService.getDonationAmountByNoOfClosetsAndPropertytypeForCurrentDate(noOfClosets,
                        sewerageConnectionDetail.getPropertyType());
            }
        }
        return amount != null ? amount : BigDecimal.ZERO;
    }

    /**
     * @param sewerageApplicationDetails
     * @return This will return sewerage charges for monthly based on NoOfClosets and Property Type.
     */

    @Override
    public BigDecimal calculateSewerageCharges(final SewerageApplicationDetails sewerageApplicationDetails) {
        Integer noOfClosets;
        Integer numberOfInstallments = 1;
        BigDecimal sewerateRate = BigDecimal.ZERO;
        Double monthlyRateAmount = 0.0;
        if (sewerageApplicationDetails.getConnection() != null
                && sewerageApplicationDetails.getConnectionDetail() != null) {
            final SewerageConnectionDetail sewerageConnectionDetail = sewerageApplicationDetails.getConnectionDetail();

            if (sewerageConnectionDetail != null) {

                final AppConfigValues advanceSewerageTaxInmonths = appConfigValuesService.getConfigValuesByModuleAndKey(
                        SewerageTaxConstants.MODULE_NAME, SewerageTaxConstants.APPCONFIG_NUMBEROFMONTHS_ADVANCESEWERAGETAX)
                        .get(0);
                if (advanceSewerageTaxInmonths != null && advanceSewerageTaxInmonths.getValue() != null
                        && !"".equals(advanceSewerageTaxInmonths.getValue())) {
                    numberOfInstallments = Integer.valueOf(advanceSewerageTaxInmonths.getValue());

                    if (numberOfInstallments == 0)
                        numberOfInstallments = 1; // added default=1.
                }

                final AppConfigValues showmonthlyrates = sewerageRatesMasterService.getAppConfigValuesForSeweargeRate(
                        SewerageTaxConstants.MODULE_NAME, SewerageTaxConstants.SEWERAGE_MONTHLY_RATES);
                if (showmonthlyrates != null && showmonthlyrates.getValue().equalsIgnoreCase("YES")) {
                    if (sewerageConnectionDetail.getPropertyType().equals(PropertyType.MIXED)) {
                        noOfClosets = sewerageConnectionDetail.getNoOfClosetsResidential();
                        monthlyRateAmount = sewerageRatesMasterService
                                .getSewerageMonthlyRatesByPropertytype(sewerageConnectionDetail.getNoOfClosetsResidential(),
                                        PropertyType.RESIDENTIAL);

                        if (monthlyRateAmount != null)
                            sewerateRate = BigDecimal.valueOf(numberOfInstallments * monthlyRateAmount);
                        noOfClosets = sewerageConnectionDetail.getNoOfClosetsNonResidential();
                        monthlyRateAmount = sewerageRatesMasterService
                                .getSewerageMonthlyRatesByPropertytype(sewerageConnectionDetail.getNoOfClosetsNonResidential(),
                                        PropertyType.NON_RESIDENTIAL);

                        if (monthlyRateAmount != null)
                            sewerateRate = sewerateRate
                                    .add(BigDecimal.valueOf(numberOfInstallments * monthlyRateAmount));
                        return sewerateRate;
                    } else {
                        noOfClosets = sewerageConnectionDetail.getPropertyType().equals(PropertyType.RESIDENTIAL)
                                ? sewerageConnectionDetail
                                        .getNoOfClosetsResidential()
                                : sewerageConnectionDetail.getPropertyType().equals(
                                        PropertyType.NON_RESIDENTIAL) ? sewerageConnectionDetail.getNoOfClosetsNonResidential()
                                                : 0;
                        monthlyRateAmount = sewerageRatesMasterService
                                .getSewerageMonthlyRatesByPropertytype(noOfClosets, sewerageConnectionDetail.getPropertyType());
                        if (monthlyRateAmount != null)
                            sewerateRate = BigDecimal.valueOf(numberOfInstallments * monthlyRateAmount);
                    }
                } else if (sewerageConnectionDetail.getPropertyType().equals(PropertyType.MIXED)) {
                    noOfClosets = sewerageConnectionDetail.getNoOfClosetsResidential();
                    monthlyRateAmount = sewerageRatesMasterService
                            .getSewerageMonthlyRatesByPropertytype(PropertyType.RESIDENTIAL);

                    if (monthlyRateAmount != null)
                        sewerateRate = BigDecimal.valueOf(numberOfInstallments * noOfClosets * monthlyRateAmount);
                    noOfClosets = sewerageConnectionDetail.getNoOfClosetsNonResidential();
                    monthlyRateAmount = sewerageRatesMasterService
                            .getSewerageMonthlyRatesByPropertytype(PropertyType.NON_RESIDENTIAL);

                    if (monthlyRateAmount != null)
                        sewerateRate = sewerateRate
                                .add(BigDecimal.valueOf(numberOfInstallments * noOfClosets * monthlyRateAmount));
                    return sewerateRate;
                } else {
                    noOfClosets = sewerageConnectionDetail.getPropertyType().equals(PropertyType.RESIDENTIAL)
                            ? sewerageConnectionDetail
                                    .getNoOfClosetsResidential()
                            : sewerageConnectionDetail.getPropertyType().equals(
                                    PropertyType.NON_RESIDENTIAL) ? sewerageConnectionDetail.getNoOfClosetsNonResidential()
                                            : 0;
                    monthlyRateAmount = sewerageRatesMasterService
                            .getSewerageMonthlyRatesByPropertytype(sewerageConnectionDetail.getPropertyType());
                    if (monthlyRateAmount != null)
                        sewerateRate = BigDecimal.valueOf(numberOfInstallments * monthlyRateAmount * noOfClosets);
                }
            }
        }
        return sewerateRate != null ? sewerateRate : BigDecimal.ZERO;
    }
}
