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
/**
 *
 */
package org.egov.egf.masters.model;

import org.apache.log4j.Logger;
import org.egov.commons.Bankaccount;
import org.egov.commons.CVoucherHeader;
import org.egov.infstr.models.BaseModel;
import org.egov.model.instrument.InstrumentHeader;

import java.math.BigDecimal;

/**
 * @author mani
 */
public class LoanGrantReceiptDetail extends BaseModel {
    private static final long serialVersionUID = -4039208357937783248L;
    final static Logger LOGGER = Logger.getLogger(LoanGrantReceiptDetail.class);
    private Bankaccount bankaccount;
    private LoanGrantHeader loanGrantHeader;
    private FundingAgency fundingAgency;
    private CVoucherHeader voucherHeader;
    private InstrumentHeader instrumentHeader;
    private BigDecimal amount;
    private String description;

    public BigDecimal getAmount() {
        return amount;
    }

    public Bankaccount getBankaccount() {
        return bankaccount;
    }

    public String getDescription() {
        return description;
    }

    public FundingAgency getFundingAgency() {
        return fundingAgency;
    }

    public InstrumentHeader getInstrumentHeader() {
        return instrumentHeader;
    }

    public LoanGrantHeader getLoanGrantHeader() {
        return loanGrantHeader;
    }

    public CVoucherHeader getVoucherHeader() {
        return voucherHeader;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public void setBankaccount(final Bankaccount bankaccount) {
        this.bankaccount = bankaccount;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setFundingAgency(final FundingAgency fundingAgency) {
        this.fundingAgency = fundingAgency;
    }

    public void setInstrumentHeader(final InstrumentHeader instrumentHeader) {
        this.instrumentHeader = instrumentHeader;
    }

    public void setLoanGrantHeader(final LoanGrantHeader loanGrantHeader) {
        this.loanGrantHeader = loanGrantHeader;
    }

    public void setVoucherHeader(final CVoucherHeader voucherHeader) {
        this.voucherHeader = voucherHeader;
    }

}
