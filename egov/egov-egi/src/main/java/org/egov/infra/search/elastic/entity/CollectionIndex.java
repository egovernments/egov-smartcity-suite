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
package org.egov.infra.search.elastic.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.search.domain.Searchable;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.Length;

/**
 * ApplicationIndex class
 *
 * @author rishi
 */

@Entity
@Table(name = "EG_COLLECTIONINDEX")
@SequenceGenerator(name = CollectionIndex.SEQ_COLLECTIONINDEX, sequenceName = CollectionIndex.SEQ_COLLECTIONINDEX, allocationSize = 1)
public class CollectionIndex extends AbstractAuditable {

    private static final long serialVersionUID = 1L;
    public static final String SEQ_COLLECTIONINDEX = "SEQ_EG_COLLECTIONINDEX";

    @DocumentId
    @Id
    @GeneratedValue(generator = SEQ_COLLECTIONINDEX, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Searchable(name = "receiptdate")
    private Date receiptDate;

    @NotNull
    @Length(max = 50)
    @Searchable(name = "receiptnumber", group = Searchable.Group.CLAUSES)
    private String receiptNumber;

    @NotNull
    @Length(max = 100)
    @Searchable(name = "billingservice", group = Searchable.Group.CLAUSES)
    private String billingService;

    @NotNull
    @Length(max = 50)
    @Searchable(name = "paymentmode", group = Searchable.Group.CLAUSES)
    private String paymentMode;

    @Searchable(name = "arrearamount", group = Searchable.Group.SEARCHABLE)
    private BigDecimal arrearAmount;

    @Searchable(name = "penaltyamount", group = Searchable.Group.SEARCHABLE)
    private BigDecimal penaltyAmount;

    @Searchable(name = "currentamount", group = Searchable.Group.SEARCHABLE)
    private BigDecimal currentAmount;

    @NotNull
    @Searchable(name = "totalamount", group = Searchable.Group.SEARCHABLE)
    private BigDecimal totalAmount;

    @Searchable(name = "advanceamount", group = Searchable.Group.SEARCHABLE)
    private BigDecimal advanceAmount;

    @NotNull
    @Length(max = 50)
    @Searchable(name = "channel", group = Searchable.Group.CLAUSES)
    private String channel;

    @Length(max = 100)
    @Searchable(name = "paymentgateway", group = Searchable.Group.CLAUSES)
    private String paymentGateway;

    @Searchable(name = "billnumber", group = Searchable.Group.CLAUSES)
    private Long billNumber;

    @NotNull
    @Length(max = 50)
    @Searchable(name = "consumercode", group = Searchable.Group.CLAUSES)
    private String consumerCode;

    @NotNull
    @Length(max = 250)
    @Searchable(name = "ulbname", group = Searchable.Group.CLAUSES)
    private String ulbName;

    @NotNull
    @Length(max = 250)
    @Searchable(name = "districtname", group = Searchable.Group.CLAUSES)
    private String districtName;

    @NotNull
    @Length(max = 250)
    @Searchable(name = "regionname", group = Searchable.Group.CLAUSES)
    private String regionName;

    @NotNull
    @Length(max = 50)
    @Searchable(group = Searchable.Group.CLAUSES)
    private String status;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(final Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(final String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getBillingService() {
        return billingService;
    }

    public void setBillingService(final String billingService) {
        this.billingService = billingService;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(final String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public BigDecimal getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(final BigDecimal advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    public BigDecimal getArrearAmount() {
        return arrearAmount;
    }

    public void setArrearAmount(final BigDecimal arrearAmount) {
        this.arrearAmount = arrearAmount;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(final BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(final BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(final String channel) {
        this.channel = channel;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(final String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public Long getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(final Long billNumber) {
        this.billNumber = billNumber;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(final String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(final String ulbName) {
        this.ulbName = ulbName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(final String districtName) {
        this.districtName = districtName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(final String regionName) {
        this.regionName = regionName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

}