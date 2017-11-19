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
package org.egov.collection.entity;

import org.egov.commons.Bankaccount;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.infra.persistence.entity.Auditable;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class Remittance extends StateAware<Position> implements Auditable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String referenceNumber;
    private Date referenceDate;
    private CVoucherHeader voucherHeader;
    private CFunction function;
    private Fund fund;
    private String remarks;
    private String reasonForDelay;
    private EgwStatus status;
    private Set<RemittanceDetail> remittanceDetails = new LinkedHashSet<>();
    private Set<ReceiptHeader> collectionRemittance = new HashSet<>();
    private Set<RemittanceInstrument> remittanceInstruments = new LinkedHashSet<>();

    private Bankaccount bankAccount;

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
        return "";
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(final String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Date getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(final Date referenceDate) {
        this.referenceDate = referenceDate;
    }

    public CVoucherHeader getVoucherHeader() {
        return voucherHeader;
    }

    public void setVoucherHeader(final CVoucherHeader voucherHeader) {
        this.voucherHeader = voucherHeader;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public String getReasonForDelay() {
        return reasonForDelay;
    }

    public void setReasonForDelay(final String reasonForDelay) {
        this.reasonForDelay = reasonForDelay;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
    }

    public Set<ReceiptHeader> getCollectionRemittance() {
        return collectionRemittance;
    }

    public void setCollectionRemittance(final Set<ReceiptHeader> collectionRemittance) {
        this.collectionRemittance = collectionRemittance;
    }

    public Set<RemittanceDetail> getRemittanceDetails() {
        return remittanceDetails;
    }

    public void setRemittanceDetails(Set<RemittanceDetail> remittanceDetails) {
        this.remittanceDetails = remittanceDetails;
    }

    public Bankaccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(Bankaccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Set<RemittanceInstrument> getRemittanceInstruments() {
        return remittanceInstruments;
    }

    public void setRemittanceInstruments(Set<RemittanceInstrument> remittanceInstruments) {
        this.remittanceInstruments = remittanceInstruments;
    }


}