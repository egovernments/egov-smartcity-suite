/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.model.instrument;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.workflow.entity.StateAware;

public class DishonorCheque extends StateAware {

    private static final long serialVersionUID = -6134188498111765210L;
    private Long id;
    private InstrumentHeader instrumentHeader;
    private Integer payinSlipCreator;
    private User payinSlipCreatorUser;
    private CVoucherHeader originalVoucherHeader;
    private EgwStatus status;
    private BigDecimal bankChargesAmt;

    private CChartOfAccounts bankchargeGlCodeId;
    private Date transactionDate;
    private String bankReferenceNumber;
    private String instrumentDishonorReason;
    private String bankreason;
    private CVoucherHeader reversalVoucherHeader;
    private CVoucherHeader bankchargesVoucherHeader;
    private boolean firstStepWk;
    private Set<DishonorChequeDetails> details = new HashSet<DishonorChequeDetails>(0);

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String getStateDetails() {
        final String instInfo = "Instrument Number :" + getInstrumentHeader().getInstrumentNumber() + " Amount : "
                + getInstrumentHeader().getInstrumentAmount();
        return instInfo;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(final Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Integer getPayinSlipCreator() {
        return payinSlipCreator;
    }

    public User getPayinSlipCreatorUser() {
        return payinSlipCreatorUser;
    }

    public void setPayinSlipCreatorUser(final User payinSlipCreatorUser) {
        this.payinSlipCreatorUser = payinSlipCreatorUser;
    }

    public void setPayinSlipCreator(final Integer payinSlipCreator) {
        this.payinSlipCreator = payinSlipCreator;
    }

    public String getBankReferenceNumber() {
        return bankReferenceNumber;
    }

    public void setBankReferenceNumber(final String bankReferenceNumber) {
        this.bankReferenceNumber = bankReferenceNumber;
    }

    public InstrumentHeader getInstrumentHeader() {
        return instrumentHeader;
    }

    public void setInstrumentHeader(final InstrumentHeader instrumentHeader) {
        this.instrumentHeader = instrumentHeader;
    }

    public CVoucherHeader getOriginalVoucherHeader() {
        return originalVoucherHeader;
    }

    public void setOriginalVoucherHeader(final CVoucherHeader originalVoucherHeader) {
        this.originalVoucherHeader = originalVoucherHeader;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
    }

    public BigDecimal getBankChargesAmt() {
        return bankChargesAmt;
    }

    public void setBankChargesAmt(final BigDecimal bankChargesAmt) {
        this.bankChargesAmt = bankChargesAmt;
    }

    public CChartOfAccounts getBankchargeGlCodeId() {
        return bankchargeGlCodeId;
    }

    public void setBankchargeGlCodeId(final CChartOfAccounts bankchargeGlCodeId) {
        this.bankchargeGlCodeId = bankchargeGlCodeId;
    }

    public CVoucherHeader getReversalVoucherHeader() {
        return reversalVoucherHeader;
    }

    public void setReversalVoucherHeader(final CVoucherHeader reversalVoucherHeader) {
        this.reversalVoucherHeader = reversalVoucherHeader;
    }

    public CVoucherHeader getBankchargesVoucherHeader() {
        return bankchargesVoucherHeader;
    }

    public void setBankchargesVoucherHeader(final CVoucherHeader bankchargesVoucherHeader) {
        this.bankchargesVoucherHeader = bankchargesVoucherHeader;
    }

    public Set<DishonorChequeDetails> getDetails() {
        return details;
    }

    public void setDetails(final Set<DishonorChequeDetails> details) {
        this.details = details;
    }

    public Set<DishonorChequeDetails> addDishonorChqDetails(final DishonorChequeDetails chqDet) {
        details.add(chqDet);
        return details;
    }

    public boolean isFirstStepWk() {
        return firstStepWk;
    }

    public void setFirstStepWk(final boolean firstStepWk) {
        this.firstStepWk = firstStepWk;
    }

    public String getInstrumentDishonorReason() {
        return instrumentDishonorReason;
    }

    public String getBankreason() {
        return bankreason;
    }

    public void setInstrumentDishonorReason(final String instrumentDishonorReason) {
        this.instrumentDishonorReason = instrumentDishonorReason;
    }

    public void setBankreason(final String bankreason) {
        this.bankreason = bankreason;
    }

    @Override
    public String myLinkId() {
        return getId().toString();
    }

}
