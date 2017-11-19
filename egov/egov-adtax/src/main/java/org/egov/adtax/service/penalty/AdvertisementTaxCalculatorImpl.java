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

package org.egov.adtax.service.penalty;

import org.egov.adtax.entity.AdvertisementRatesDetails;
import org.egov.adtax.service.AdvertisementRateService;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdvertisementTaxCalculatorImpl implements AdvertisementTaxCalculator {
    protected @Autowired AdvertisementRateService advertisementRateService;

    private @Autowired AppConfigValueService appConfigValuesService;

    @Override
    public Double calculateTaxAmount(final Long unitOfMeasureId, final Double measurement, final Long subCategoryId,
            final Long rateClassId) {
        AdvertisementRatesDetails rate = null;

        rate = advertisementRateService.getRatesBySubcategoryUomClassAndMeasurementByFinancialYearInDecendingOrder(
                subCategoryId, unitOfMeasureId, rateClassId, measurement);

        if (rate != null) {
            // get data based on financial year, if not present, get from
            // previous year data.
            // MULTIPLY WITH MEASUREMENT TO GET TOTAL AMOUNT.

            // CHECK WHETHER CALCULATION REQUIRED BASED ON PERUNIT BASIS OR
            // NORMAL
            // WAY ?
            final List<AppConfigValues> calculateSorByUnit = appConfigValuesService.getConfigValuesByModuleAndKey(
                    AdvertisementTaxConstants.MODULE_NAME, AdvertisementTaxConstants.CALCULATESORBYUNIT);
            if (!calculateSorByUnit.isEmpty()) {
                if (calculateSorByUnit.get(0).getValue().equalsIgnoreCase("NO")) {
                    return BigDecimal.valueOf(rate.getAmount()).multiply(BigDecimal.valueOf(measurement))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                } else if (calculateSorByUnit.get(0).getValue().equalsIgnoreCase("YES")) {

                    final BigDecimal unitRate = rate.getAdvertisementRate().getUnitrate() != null ? BigDecimal
                            .valueOf(rate.getAdvertisementRate().getUnitrate()) : BigDecimal.ZERO;

                    // MULTIPLY WITH MEASUREMENT TO GET TOTAL AMOUNT.
                    if (unitRate != BigDecimal.valueOf(0)) {
                        return BigDecimal
                                .valueOf(rate.getAmount())
                                .multiply(
                                        BigDecimal.valueOf(measurement).divide(unitRate, 4, RoundingMode.HALF_UP)
                                                .setScale(0, RoundingMode.UP)).setScale(4, BigDecimal.ROUND_HALF_UP)
                                .doubleValue();
                    } else
                        return Double.valueOf(0);
                }
            }
        }

        return Double.valueOf(0);

    }

}
