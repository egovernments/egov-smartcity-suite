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

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.egov.collection.entity.CollectionDishonorCheque.SEQ_EGCL_DISHONORCHEQUE;

@Entity
@Table(name = "EGCL_DISHONORCHEQUE")
@SequenceGenerator(name = SEQ_EGCL_DISHONORCHEQUE, sequenceName = SEQ_EGCL_DISHONORCHEQUE, allocationSize = 1)
public class CollectionDishonorCheque extends StateAware<Position> {

    public static final String SEQ_EGCL_DISHONORCHEQUE = "SEQ_EGCL_DISHONORCHEQUE";
    private static final long serialVersionUID = -6134188498111765210L;
    @Id
    @GeneratedValue(generator = SEQ_EGCL_DISHONORCHEQUE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "instrumentheader")
    private InstrumentHeader instrumentHeader;

    @ManyToOne
    @JoinColumn(name = "collectionheader")
    private ReceiptHeader collectionHeader;

    @ManyToOne
    @JoinColumn(name = "status")
    private EgwStatus status;

    @Column(name = "bankcharges")
    private BigDecimal bankChargesAmt;

    @ManyToOne
    @JoinColumn(name = "bankchargeschartofaccounts")
    private CChartOfAccounts bankcChargesChartofaccounts;

    private Date transactionDate;

    @Length(max = 20)
    private String bankReferenceNumber;

    private String instrumentDishonorReason;

    private String bankreason;

    @ManyToOne
    @JoinColumn(name = "reversalvoucher")
    private CVoucherHeader reversalVoucherHeader;

    @ManyToOne
    @JoinColumn(name = "bankchargesvoucher")
    private CVoucherHeader bankchargesVoucherHeader;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "dishonorcheque", targetEntity = CollectionDishonorChequeDetails.class)
    private Set<CollectionDishonorChequeDetails> details = new HashSet<>();

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
        return new StringBuilder().append("Instrument Number :").append(getInstrumentHeader().getInstrumentNumber())
                .append(" Amount : ").append(getInstrumentHeader().getInstrumentAmount()).toString();
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(final Date transactionDate) {
        this.transactionDate = transactionDate;
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

    public ReceiptHeader getCollectionHeader() {
        return collectionHeader;
    }

    public void setCollectionHeader(ReceiptHeader collectionHeader) {
        this.collectionHeader = collectionHeader;
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

    public CChartOfAccounts getBankcChargesChartofaccounts() {
        return bankcChargesChartofaccounts;
    }

    public void setBankcChargesChartofaccounts(CChartOfAccounts bankcChargesChartofaccounts) {
        this.bankcChargesChartofaccounts = bankcChargesChartofaccounts;
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

    public Set<CollectionDishonorChequeDetails> getDetails() {
        return details;
    }

    public void setDetails(final Set<CollectionDishonorChequeDetails> details) {
        this.details = details;
    }

    public Set<CollectionDishonorChequeDetails> addDishonorChqDetails(final CollectionDishonorChequeDetails chqDet) {
        details.add(chqDet);
        return details;
    }

    public String getInstrumentDishonorReason() {
        return instrumentDishonorReason;
    }

    public void setInstrumentDishonorReason(final String instrumentDishonorReason) {
        this.instrumentDishonorReason = instrumentDishonorReason;
    }

    public String getBankreason() {
        return bankreason;
    }

    public void setBankreason(final String bankreason) {
        this.bankreason = bankreason;
    }

    @Override
    public String myLinkId() {
        return getId().toString();
    }


}
