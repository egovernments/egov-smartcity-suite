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

import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.stms.masters.service.DonationMasterService;
import org.egov.stms.masters.service.SewerageRatesMasterService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageConnectionDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static org.egov.stms.masters.entity.enums.PropertyType.MIXED;
import static org.egov.stms.masters.entity.enums.PropertyType.NON_RESIDENTIAL;
import static org.egov.stms.masters.entity.enums.PropertyType.RESIDENTIAL;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPCONFIG_NUMBEROFMONTHS_ADVANCESEWERAGETAX;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MODULE_NAME;

@Service
public class SewerageChargeCalculationServiceImpl implements SewerageChargeCalculationService {

    @Autowired
    private DonationMasterService donationMasterService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private SewerageRatesMasterService sewerageRatesMasterService;


    @Override
    public BigDecimal calculateDonationCharges(final SewerageApplicationDetails sewerageApplicationDetails) {
        BigDecimal amount = ZERO;
        Integer noOfClosets;

        if (sewerageApplicationDetails.getConnection() != null
                && sewerageApplicationDetails.getConnectionDetail() != null) {
            final SewerageConnectionDetail sewerageConnectionDetail = sewerageApplicationDetails.getConnectionDetail();
            if (sewerageConnectionDetail != null) {
                if (sewerageConnectionDetail.getPropertyType().equals(MIXED)) {

                    final BigDecimal amountForResidential = donationMasterService
                            .getDonationAmountByNoOfClosetsAndPropertytypeForCurrentDate(
                                    sewerageConnectionDetail.getNoOfClosetsResidential(), RESIDENTIAL);
                    final BigDecimal amountForNonResidential = donationMasterService
                            .getDonationAmountByNoOfClosetsAndPropertytypeForCurrentDate(
                                    sewerageConnectionDetail.getNoOfClosetsNonResidential(), NON_RESIDENTIAL);
                    if (amountForResidential != null)
                        return amountForNonResidential != null ? amountForResidential.add(amountForNonResidential)
                                : amountForResidential;
                    else
                        return amountForNonResidential == null ? ZERO : amountForNonResidential;
                } else {
                    noOfClosets = RESIDENTIAL.equals(sewerageConnectionDetail.getPropertyType())
                            ? sewerageConnectionDetail.getNoOfClosetsResidential()
                            : NON_RESIDENTIAL.equals(sewerageConnectionDetail.getPropertyType())
                            ? sewerageConnectionDetail.getNoOfClosetsNonResidential() : 0;
                    amount = donationMasterService.getDonationAmountByNoOfClosetsAndPropertytypeForCurrentDate(noOfClosets,
                            sewerageConnectionDetail.getPropertyType());
                }
            }
        }
        return amount == null ? ZERO : amount;
    }

    @Override
    public BigDecimal calculateSewerageCharges(final SewerageApplicationDetails sewerageApplicationDetails) {
        Integer noOfClosets;
        Integer numberOfInstallments = 1;
        BigDecimal sewerageRate = ZERO;
        Double monthlyRateAmount;
        if (sewerageApplicationDetails.getConnection() != null
                && sewerageApplicationDetails.getConnectionDetail() != null) {
            final SewerageConnectionDetail sewerageConnectionDetail = sewerageApplicationDetails.getConnectionDetail();

            if (sewerageConnectionDetail != null) {

                final AppConfigValues advanceSewerageTaxInmonths = appConfigValuesService.getConfigValuesByModuleAndKey(
                        MODULE_NAME, APPCONFIG_NUMBEROFMONTHS_ADVANCESEWERAGETAX)
                        .get(0);
                if (advanceSewerageTaxInmonths != null && advanceSewerageTaxInmonths.getValue() != null
                        && !"".equals(advanceSewerageTaxInmonths.getValue())) {
                    numberOfInstallments = Integer.valueOf(advanceSewerageTaxInmonths.getValue());

                    if (numberOfInstallments == 0)
                        numberOfInstallments = 1; // added default=1.
                }

                final Boolean isMultipleClosetRatesAllowed = sewerageRatesMasterService.getMultipleClosetAppconfigValue();
                if (isMultipleClosetRatesAllowed) {
                    if (sewerageConnectionDetail.getPropertyType().equals(MIXED)) {
                        monthlyRateAmount = sewerageRatesMasterService
                                .getSewerageMonthlyRatesByClosetsAndPropertyType(sewerageConnectionDetail.getNoOfClosetsResidential(),
                                        RESIDENTIAL);

                        if (monthlyRateAmount != null)
                            sewerageRate = BigDecimal.valueOf(numberOfInstallments * monthlyRateAmount);
                        monthlyRateAmount = sewerageRatesMasterService
                                .getSewerageMonthlyRatesByClosetsAndPropertyType(sewerageConnectionDetail.getNoOfClosetsNonResidential(),
                                        NON_RESIDENTIAL);

                        if (monthlyRateAmount != null)
                            sewerageRate = sewerageRate
                                    .add(BigDecimal.valueOf(numberOfInstallments * monthlyRateAmount));
                        return sewerageRate;
                    } else {
                        noOfClosets = RESIDENTIAL.equals(sewerageConnectionDetail.getPropertyType())
                                ? sewerageConnectionDetail
                                .getNoOfClosetsResidential()
                                : NON_RESIDENTIAL.equals(sewerageConnectionDetail.getPropertyType())
                                ? sewerageConnectionDetail.getNoOfClosetsNonResidential()
                                : 0;
                        monthlyRateAmount = sewerageRatesMasterService
                                .getSewerageMonthlyRatesByClosetsAndPropertyType(noOfClosets, sewerageConnectionDetail.getPropertyType());
                        if (monthlyRateAmount != null)
                            sewerageRate = BigDecimal.valueOf(numberOfInstallments * monthlyRateAmount);
                    }
                } else if (MIXED.equals(sewerageConnectionDetail.getPropertyType())) {
                    noOfClosets = sewerageConnectionDetail.getNoOfClosetsResidential();
                    monthlyRateAmount = sewerageRatesMasterService
                            .getSewerageMonthlyRatesByPropertyType(RESIDENTIAL);

                    if (monthlyRateAmount != null)
                        sewerageRate = BigDecimal.valueOf(numberOfInstallments * noOfClosets * monthlyRateAmount);
                    noOfClosets = sewerageConnectionDetail.getNoOfClosetsNonResidential();
                    monthlyRateAmount = sewerageRatesMasterService
                            .getSewerageMonthlyRatesByPropertyType(NON_RESIDENTIAL);

                    if (monthlyRateAmount != null)
                        sewerageRate = sewerageRate
                                .add(BigDecimal.valueOf(numberOfInstallments * noOfClosets * monthlyRateAmount));
                    return sewerageRate;
                } else {
                    noOfClosets = RESIDENTIAL.equals(sewerageConnectionDetail.getPropertyType())
                            ? sewerageConnectionDetail.getNoOfClosetsResidential()
                            : NON_RESIDENTIAL.equals(sewerageConnectionDetail.getPropertyType())
                            ? sewerageConnectionDetail.getNoOfClosetsNonResidential()
                            : 0;
                    monthlyRateAmount = sewerageRatesMasterService
                            .getSewerageMonthlyRatesByPropertyType(sewerageConnectionDetail.getPropertyType());
                    if (monthlyRateAmount != null)
                        sewerageRate = BigDecimal.valueOf(numberOfInstallments * monthlyRateAmount * noOfClosets);
                }
            }
        }
        return sewerageRate == null ? ZERO : sewerageRate;
    }
}
