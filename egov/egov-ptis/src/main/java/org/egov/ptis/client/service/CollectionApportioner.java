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
package org.egov.ptis.client.service;

import org.apache.log4j.Logger;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillAccountDetails.PURPOSE;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.demand.model.EgBillDetails;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.ptis.constants.PropertyTaxConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.egov.ptis.constants.PropertyTaxConstants.GLCODES_FOR_CURRENTTAX;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODE_FOR_TAXREBATE;

public class CollectionApportioner {

    private static final String REBATE_STR = "REBATE";
    public static final String STRING_FULLTAX = "FULLTAX";
    public static final String STRING_ADVANCE = "ADVANCE";
    private static final Logger LOGGER = Logger.getLogger(CollectionApportioner.class);
    private boolean isEligibleForCurrentRebate;
    private boolean isEligibleForAdvanceRebate;

    public CollectionApportioner(boolean isEligibleForCurrentRebate, boolean isEligibleForAdvanceRebate,
            BigDecimal rebate) {
        this.isEligibleForCurrentRebate = isEligibleForCurrentRebate;
        this.isEligibleForAdvanceRebate = isEligibleForAdvanceRebate;
        // this.rebate = rebate;
    }

    public void apportion(BigDecimal amtPaid, List<ReceiptDetail> receiptDetails, Map<String, BigDecimal> instDmdMap) {
        LOGGER.info("receiptDetails before apportioning amount " + amtPaid + ": " + receiptDetails);
        Boolean isFullPayment = Boolean.FALSE;
        if (isEligibleForCurrentRebate) {
            BigDecimal totalCrAmountToBePaid = BigDecimal.ZERO;
            for (final ReceiptDetail receiptDetail : receiptDetails) {
                if (!PURPOSE.ADVANCE_AMOUNT.toString().equals(receiptDetail.getPurpose()))
                    totalCrAmountToBePaid = totalCrAmountToBePaid.add(receiptDetail.getCramountToBePaid());
            }
            if (amtPaid.compareTo(totalCrAmountToBePaid) >= 0) {
                isFullPayment = Boolean.TRUE;
            }
        }
        Amount balance = new Amount(amtPaid);
        BigDecimal crAmountToBePaid = null;
        for (ReceiptDetail rd : receiptDetails) {

            if (balance.isZero()) {
                // nothing left to apportion
                rd.zeroDrAndCrAmounts();
                continue;
            }

            crAmountToBePaid = rd.getCramountToBePaid();

            if (rd.getDescription().contains(REBATE_STR)) {
                if (isFullPayment) {
                    balance = balance.minus(crAmountToBePaid);
                } else {
                    rd.setDramount(BigDecimal.ZERO);
                }
            } else {
                if (balance.isLessThanOrEqualTo(crAmountToBePaid)) {
                    // partial or exact payment
                    rd.setCramount(balance.amount);
                    balance = Amount.ZERO;
                } else { // excess payment
                    rd.setCramount(crAmountToBePaid);
                    balance = balance.minus(crAmountToBePaid);
                }
            }
        }
        if (balance.isGreaterThanZero()) {
            LOGGER.error("Apportioning failed: excess payment!");
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "Paid Amount is greater than Total Amount to be paid",
                    "Paid Amount is greater than Total Amount to be paid")));
        }

        LOGGER.info("receiptDetails after apportioning: " + receiptDetails);
    }

    public List<ReceiptDetail> reConstruct(final BigDecimal amountPaid, final List<EgBillDetails> billDetails,
            FunctionHibernateDAO functionDAO, ChartOfAccountsHibernateDAO chartOfAccountsDAO) {
        final List<ReceiptDetail> receiptDetails = new ArrayList<ReceiptDetail>(0);
        LOGGER.info("receiptDetails before reApportion amount " + amountPaid + ": " + receiptDetails);
        LOGGER.info("billDetails before reApportion " + billDetails);
        Amount balance = new Amount(amountPaid);
        Boolean isFullPayment = Boolean.FALSE;
        BigDecimal crAmountToBePaid = BigDecimal.ZERO;

        if (isEligibleForCurrentRebate) {
            BigDecimal totalCrAmountToBePaid = BigDecimal.ZERO;
            for (final ReceiptDetail receiptDetail : receiptDetails) {
                totalCrAmountToBePaid = totalCrAmountToBePaid.add(receiptDetail.getCramountToBePaid());
            }
            if (amountPaid.compareTo(totalCrAmountToBePaid) >= 0) {
                isFullPayment = Boolean.TRUE;
            }
        }

        for (final EgBillDetails billDetail : billDetails) {
            final String glCode = billDetail.getGlcode();
            final ReceiptDetail receiptDetail = new ReceiptDetail();
            receiptDetail.setPurpose(billDetail.getPurpose());
            receiptDetail.setOrdernumber(Long.valueOf(billDetail.getOrderNo()));
            receiptDetail.setDescription(billDetail.getDescription());
            receiptDetail.setIsActualDemand(true);
            if(billDetail.getFunctionCode() !=null){
            receiptDetail.setFunction(functionDAO.getFunctionByCode(billDetail.getFunctionCode()));
            }
            receiptDetail.setAccounthead(chartOfAccountsDAO.getCChartOfAccountsByGlCode(glCode));
            receiptDetail.setCramountToBePaid(balance.amount);
            if (billDetail.getDescription().contains(REBATE_STR)) {
                receiptDetail.setDramount(billDetail.getDrAmount());
            } else {
                receiptDetail.setDramount(BigDecimal.ZERO);
            }

            if (balance.isZero()) {
                // nothing left to apportion
                receiptDetail.zeroDrAndCrAmounts();
                receiptDetails.add(receiptDetail);
                continue;
            }
            crAmountToBePaid = billDetail.getCrAmount();

            if (receiptDetail.getDescription().contains(REBATE_STR)) {
                if (isFullPayment) {
                    balance = balance.minus(crAmountToBePaid);
                } else {
                    receiptDetail.setDramount(BigDecimal.ZERO);
                }
            } else {
                if (balance.isLessThanOrEqualTo(crAmountToBePaid)) {
                    // partial or exact payment
                    receiptDetail.setCramount(balance.amount);
                    receiptDetail.setCramountToBePaid(balance.amount);
                    balance = Amount.ZERO;
                } else { // excess payment
                    receiptDetail.setCramount(crAmountToBePaid);
                    receiptDetail.setCramountToBePaid(crAmountToBePaid);
                    balance = balance.minus(crAmountToBePaid);
                }
            }
            receiptDetails.add(receiptDetail);
        }

        if (balance.isGreaterThanZero()) {
            LOGGER.error("reApportion failed: excess payment!");
            throw new ValidationException(Arrays.asList(
                    new ValidationError("Paid Amount is greater than Total Amount to be paid",
                            "Paid Amount is greater than Total Amount to be paid")));
        }

        LOGGER.info("receiptDetails after reApportion: " + receiptDetails);
        return receiptDetails;
    }

    private boolean isArrear(String glCode) {
        return !GLCODES_FOR_CURRENTTAX.contains(glCode) && !glCode.equals(GLCODE_FOR_TAXREBATE);
    }

    private boolean isCurrent(String glCode) {
        return GLCODES_FOR_CURRENTTAX.contains(glCode);
    }

    private boolean isAdvance(String glCode) {
        return glCode.equals(PropertyTaxConstants.GLCODE_FOR_ADVANCE);
    }

    private boolean isRebate(String glCode) {
        return glCode.equals(GLCODE_FOR_TAXREBATE)
                || glCode.equalsIgnoreCase(PropertyTaxConstants.GLCODE_FOR_ADVANCE_REBATE);
    }

    private static class Amount {
        private BigDecimal amount;
        private static Amount ZERO = new Amount(BigDecimal.ZERO);

        private Amount(BigDecimal amount) {
            this.amount = amount;
        }

        private boolean isZero() {
            return amount.compareTo(BigDecimal.ZERO) == 0;
        }

        private boolean isGreaterThan(BigDecimal bd) {
            return amount.compareTo(bd) > 0;
        }

        private boolean isGreaterThanZero() {
            return isGreaterThan(BigDecimal.ZERO);
        }

        private boolean isGreaterThanOrEqualTo(BigDecimal bd) {
            return amount.compareTo(bd) >= 0;
        }

        private boolean isLessThanOrEqualTo(BigDecimal bd) {
            return amount.compareTo(bd) <= 0;
        }

        private boolean isLessThan(BigDecimal bd) {
            return amount.compareTo(bd) < 0;
        }

        private boolean isGreaterThanOrEqualToZero() {
            return isGreaterThanOrEqualTo(BigDecimal.ZERO);
        }

        private Amount minus(BigDecimal bd) {
            return new Amount(amount.subtract(bd));
        }

        private Amount plus(BigDecimal bd) {
            return new Amount(amount.add(bd));
        }

        private Amount multiply(BigDecimal bd) {
            return new Amount(amount.multiply(bd));
        }

        private Amount divide(BigDecimal bd) {
            return new Amount(amount.divide(bd));
        }

    }

    void setEligibleForCurrentRebate(boolean isEligibleForCurrentRebate) {
        this.isEligibleForCurrentRebate = isEligibleForCurrentRebate;
    }

    void setEligibleForAdvanceRebate(boolean isEligibleForAdvanceRebate) {
        this.isEligibleForAdvanceRebate = isEligibleForAdvanceRebate;
    }
}
