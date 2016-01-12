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
package org.egov.tl.domain.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.egov.tl.domain.entity.FeeMatrix;
import org.egov.tl.domain.entity.License;
import org.egov.tl.domain.entity.LicenseAppType;
import org.egov.tl.domain.entity.NatureOfBusiness;
import org.egov.tl.domain.entity.LicenseSubCategory;
import org.egov.tl.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;

public class FeeService extends PersistenceService<FeeMatrix, Long> {
    public static final String FEE_BY_NAME_TYPE_NATURE = "FEE_BY_NAME_TYPE_NATURE";
    public static final String CNC = "CNC";
    public static final String PFA = "PFA";
    public static final Logger LOGGER = Logger.getLogger(FeeService.class);
    public static final String HOM = "HOM";
    @Autowired
    private FeeMatrixService feeMatrixService;

    @SuppressWarnings("unchecked")
    public List<FeeMatrix> getFeeList(final LicenseSubCategory tradeName, final LicenseAppType appType,
            final NatureOfBusiness natureOfBusiness) {
        LOGGER.debug("tradeName:::" + tradeName);
        return findAllByNamedQuery(FEE_BY_NAME_TYPE_NATURE, tradeName.getId(), appType.getId());
    }

    /**
     * @param tradeName -- is mandatory
     * @param appType --is mandatory
     * @param tradeNatureId
     * @param otherCharges --will be added to sum
     * @param deduction -- will be subtracted from sum
     * @return calculatedFee This api will also set the LicenseNumber prefix when new class is added needs to update the api to
     * set corresponding prefix(fee type) eg: TradeLicense has PFA or CNC HospitalLicense has HOM only put loggers
     */
    public BigDecimal calculateFee(final License license) {
        BigDecimal totalFee = BigDecimal.ZERO;
      /*  boolean isPFA = false;
        final List<FeeMatrix> feeList = feeMatrixService.getFeeLifest(license);
        totalFee = totalFee.add(otherCharges != null ? otherCharges : BigDecimal.ZERO);
        totalFee = totalFee.subtract(deduction != null ? deduction : BigDecimal.ZERO);
        if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.TRADELICENSE)) {
            for (final FeeMatrix fee : feeList) {
                if (fee.getFeeType().getName().equalsIgnoreCase(PFA))
                    isPFA = true;
                totalFee = totalFee.add(fee.getAmount());
            }
                        LOGGER.warn("Fee Type prefix is not set");*/
        return totalFee;
    }

    public BigDecimal calculateFeeForExisting(final License license, final LicenseSubCategory tradeName, final LicenseAppType appType,
            final NatureOfBusiness natureOfBusiness, final BigDecimal otherCharges, final BigDecimal deduction) {
        BigDecimal totalFee = BigDecimal.ZERO;
        boolean isPFA = false;

        final List<FeeMatrix> feeList = getFeeList(tradeName, appType, natureOfBusiness);
        if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.TRADELICENSE)) {
            for (final FeeMatrix fee : feeList) {
                if (fee.getFeeType().getName().equalsIgnoreCase(PFA))
                    isPFA = true;
                totalFee = totalFee.add(fee.getAmount());
            }
        }
        return totalFee;
    }

    public void setFeeType(final List<FeeMatrix> feeList, final License license) {
        boolean isPFA = false;

        if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.TRADELICENSE)) {
            for (final FeeMatrix fee : feeList)
                if (fee.getFeeType().getName().equalsIgnoreCase(PFA))
                    isPFA = true;
            if (isPFA)
                license.setFeeTypeStr(PFA);
            else
                license.setFeeTypeStr(CNC);
        } else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.HOSPITALLICENSE))
            license.setFeeTypeStr(HOM);
        else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.HAWKERLICENSE)) {
            for (final FeeMatrix fee : feeList)
                if (fee.getFeeType().getName().equalsIgnoreCase(PFA))
                    isPFA = true;
            if (isPFA)
                license.setFeeTypeStr(PFA);
            else
                license.setFeeTypeStr(CNC);
        } else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.WATERWORKSLICENSE)) {
            for (final FeeMatrix fee : feeList)
                if (fee.getFeeType().getName().equalsIgnoreCase(PFA))
                    isPFA = true;
            if (isPFA)
                license.setFeeTypeStr(PFA);
            else
                license.setFeeTypeStr(CNC);
        } else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.PWDCONTRACTORLICENSE))
            license.setFeeTypeStr(CNC);
        else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.VETERINARYLICENSE))
            license.setFeeTypeStr(CNC);
        else if (license.getClass().getSimpleName().equalsIgnoreCase(Constants.ELECTRICALCONTRACTORLICENSE))
            license.setFeeTypeStr(CNC);
        else
            LOGGER.warn("Fee Type prefix is not set");
    }
}
