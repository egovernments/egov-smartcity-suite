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

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "EG_COLLECTIONINDEX")
@SequenceGenerator(name = CollectionIndex.SEQ_COLLECTIONINDEX, sequenceName = CollectionIndex.SEQ_COLLECTIONINDEX, allocationSize = 1)
public class CollectionIndex extends AbstractAuditable {
    private static final long serialVersionUID = 1L;
    public static final String SEQ_COLLECTIONINDEX = "SEQ_EG_COLLECTIONINDEX";

    @Id
    @GeneratedValue(generator = SEQ_COLLECTIONINDEX, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private Date receiptDate;

    @NotNull
    @Length(max = 50)
    private String receiptNumber;

    @NotNull
    @Length(max = 100)
    private String billingService;

    @NotNull
    @Length(max = 50)
    private String paymentMode;

    private BigDecimal arrearAmount;

    private BigDecimal penaltyAmount;

    private BigDecimal currentAmount;

    @NotNull
    private BigDecimal totalAmount;

    private BigDecimal advanceAmount;

    @NotNull
    @Length(max = 50)
    private String channel;

    @Length(max = 100)
    private String paymentGateway;

    private String billNumber;

    @Length(max = 50)
    private String consumerCode;

    @NotNull
    @Length(max = 250)
    private String cityName;

    @Length(max = 250)
    private String districtName;

    @Length(max = 250)
    private String regionName;

    @NotNull
    @Length(max = 50)
    private String status;

    private BigDecimal latePaymentCharges;

    private BigDecimal arrearCess;

    private BigDecimal currentCess;

    @Length(max = 50)
    private String installmentFrom;

    @Length(max = 50)
    private String installmentTo;

    @Length(max = 256)
    private String consumerName;

    private BigDecimal reductionAmount;

    @Length(max = 50)
    private String cityGrade;

    @Length(max = 10)
    private String cityCode;

    @Length(max = 100)
    private String receiptCreator;

    private String revenueWard;

    @Length(max = 100)
    private String consumerType;

    private Integer conflict;

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

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(final String billNumber) {
        this.billNumber = billNumber;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(final String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(final String cityName) {
        this.cityName = cityName;
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

    public BigDecimal getLatePaymentCharges() {
        return latePaymentCharges;
    }

    public void setLatePaymentCharges(final BigDecimal latePaymentCharges) {
        this.latePaymentCharges = latePaymentCharges;
    }

    public BigDecimal getArrearCess() {
        return arrearCess;
    }

    public void setArrearCess(final BigDecimal arrearCess) {
        this.arrearCess = arrearCess;
    }

    public BigDecimal getCurrentCess() {
        return currentCess;
    }

    public void setCurrentCess(final BigDecimal currentCess) {
        this.currentCess = currentCess;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(final String consumerName) {
        this.consumerName = consumerName;
    }

    /**
     * @return the installmentFrom
     */
    public String getInstallmentFrom() {
        return installmentFrom;
    }

    /**
     * @param installmentFrom
     *            the installmentFrom to set
     */
    public void setInstallmentFrom(final String installmentFrom) {
        this.installmentFrom = installmentFrom;
    }

    /**
     * @return the installmentTo
     */
    public String getInstallmentTo() {
        return installmentTo;
    }

    /**
     * @param installmentTo
     *            the installmentTo to set
     */
    public void setInstallmentTo(final String installmentTo) {
        this.installmentTo = installmentTo;
    }

    /**
     * @return the reductionAmount
     */
    public BigDecimal getReductionAmount() {
        return reductionAmount;
    }

    /**
     * @param reductionAmount
     *            the reductionAmount to set
     */
    public void setReductionAmount(final BigDecimal reductionAmount) {
        this.reductionAmount = reductionAmount;
    }

    /**
     * @return the cityGrade
     */
    public String getCityGrade() {
        return cityGrade;
    }

    /**
     * @param cityGrade
     *            the cityGrade to set
     */
    public void setCityGrade(final String cityGrade) {
        this.cityGrade = cityGrade;
    }

    /**
     * @return the ulbCode
     */
    public String getCityCode() {
        return cityCode;
    }

    /**
     * @param ulbCode
     *            the ulbCode to set
     */
    public void setCityCode(final String cityCode) {
        this.cityCode = cityCode;
    }

    /**
     * @return the receiptCreator
     */
    public String getReceiptCreator() {
        return receiptCreator;
    }

    /**
     * @param receiptCreator
     *            the receiptCreator to set
     */
    public void setReceiptCreator(final String receiptCreator) {
        this.receiptCreator = receiptCreator;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(final String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(final String consumerType) {
        this.consumerType = consumerType;
    }

    public Integer getConflict() {
        return conflict;
    }

    public void setConflict(final Integer conflict) {
        this.conflict = conflict;
    }

    public static CollectionIndexBuilder builder() {
        return new CollectionIndexBuilder();
    }

    public static final class CollectionIndexBuilder {
        private Date receiptdate;
        private String receiptnumber;
        private String billingservice;
        private String paymentmode;
        private BigDecimal totalamount;
        private String channel;
        private BigDecimal arrearamount;
        private BigDecimal penaltyamount;
        private BigDecimal currentamount;
        private BigDecimal advanceamount;
        private String paymentgateway;
        private String billnumber;
        private BigDecimal latepaymentcharges;
        private BigDecimal arrearcess;
        private BigDecimal currentcess;
        private String installmentfrom;
        private String installmentto;
        private String consumername;
        private BigDecimal reductionamount;
        private String receiptcreator;
        private String revenueward;
        private String status;
        private String consumercode;
        private String consumerType;
        private Integer conflict;

        public CollectionIndex build() throws ApplicationRuntimeException {
            final CollectionIndex collectionIndex = new CollectionIndex();
            collectionIndex.setReceiptDate(receiptdate);
            collectionIndex.setReceiptNumber(receiptnumber);
            collectionIndex.setBillingService(billingservice);
            collectionIndex.setPaymentMode(paymentmode);
            collectionIndex.setConsumerCode(consumercode);
            collectionIndex.setTotalAmount(totalamount);
            collectionIndex.setArrearAmount(arrearamount);
            collectionIndex.setPenaltyAmount(penaltyamount);
            collectionIndex.setCurrentAmount(currentamount);
            collectionIndex.setAdvanceAmount(advanceamount);
            collectionIndex.setPaymentGateway(paymentgateway);
            collectionIndex.setBillNumber(billnumber);
            collectionIndex.setLatePaymentCharges(latepaymentcharges);
            collectionIndex.setArrearCess(arrearcess);
            collectionIndex.setCurrentCess(currentcess);
            collectionIndex.setInstallmentFrom(installmentfrom);
            collectionIndex.setInstallmentTo(installmentto);
            collectionIndex.setConsumerName(consumername);
            collectionIndex.setReductionAmount(reductionamount);
            collectionIndex.setReceiptCreator(receiptcreator);
            collectionIndex.setRevenueWard(revenueward);
            collectionIndex.setChannel(channel);
            collectionIndex.setStatus(status);
            collectionIndex.setConsumerType(consumerType);
            collectionIndex.setConflict(conflict);
            collectionIndex.validate();
            return collectionIndex;
        }

        private CollectionIndexBuilder() {
        }

        public CollectionIndexBuilder withReceiptDate(final Date receiptdate) {
            this.receiptdate = receiptdate;
            return this;
        }

        public CollectionIndexBuilder withReceiptnumber(final String receiptnumber) {
            this.receiptnumber = receiptnumber;
            return this;
        }

        public CollectionIndexBuilder withBillingservice(final String billingService) {
            billingservice = billingService;
            return this;
        }

        public CollectionIndexBuilder withPaymentMode(final String paymentMode) {
            paymentmode = paymentMode;
            return this;
        }

        public CollectionIndexBuilder withConsumerCode(final String consumercode) {
            this.consumercode = consumercode;
            return this;
        }

        public CollectionIndexBuilder withTotalamount(final BigDecimal totalamount) {
            this.totalamount = totalamount;
            return this;
        }

        public CollectionIndexBuilder withArrearAmount(final BigDecimal arrearAmount) {
            arrearamount = arrearAmount;
            return this;
        }

        public CollectionIndexBuilder withPenaltyAmount(final BigDecimal penaltyAmount) {
            penaltyamount = penaltyAmount;
            return this;
        }

        public CollectionIndexBuilder withCurrentAmount(final BigDecimal currentAmount) {
            currentamount = currentAmount;
            return this;
        }

        public CollectionIndexBuilder withAdvanceAmount(final BigDecimal advanceAmount) {
            advanceamount = advanceAmount;
            return this;
        }

        public CollectionIndexBuilder withPaymentGateway(final String paymentGateway) {
            paymentgateway = paymentGateway;
            return this;
        }

        public CollectionIndexBuilder withBillNumber(final String billNumber) {
            billnumber = billNumber;
            return this;
        }

        public CollectionIndexBuilder withLatePaymentChargesAmount(final BigDecimal latePaymentCharges) {
            latepaymentcharges = latePaymentCharges;
            return this;
        }

        public CollectionIndexBuilder withArrearCess(final BigDecimal arrearCess) {
            arrearcess = arrearCess;
            return this;
        }

        public CollectionIndexBuilder withCurrentCess(final BigDecimal currentCess) {
            currentcess = currentCess;
            return this;
        }

        public CollectionIndexBuilder withInstallmentFrom(final String installmentFrom) {
            installmentfrom = installmentFrom;
            return this;
        }

        public CollectionIndexBuilder withInstallmentTo(final String installmentTo) {
            installmentto = installmentTo;
            return this;
        }

        public CollectionIndexBuilder withConsumerName(final String consumerName) {
            consumername = consumerName;
            return this;
        }

        public CollectionIndexBuilder withReductionAmount(final BigDecimal reductionAmount) {
            reductionamount = reductionAmount;
            return this;
        }

        public CollectionIndexBuilder withReceiptCreator(final String receiptCreator) {
            receiptcreator = receiptCreator;
            return this;
        }

        public CollectionIndexBuilder withRevenueWard(final String revenueWard) {
            revenueward = revenueWard;
            return this;
        }

        public CollectionIndexBuilder withStatus(final String status) {
            this.status = status;
            return this;
        }

        public CollectionIndexBuilder withChannel(final String channel) {
            this.channel = channel;
            return this;
        }

        public CollectionIndexBuilder withConsumerType(final String consumerType) {
            this.consumerType = consumerType;
            return this;
        }
        
        public CollectionIndexBuilder withConflict(final Integer conflict) {
            this.conflict = conflict;
            return this;
        }
    }

    private void validate() throws ApplicationRuntimeException {
        if (getReceiptDate() == null)
            throw new ApplicationRuntimeException("Receipt Date is mandatory");
        if (getReceiptNumber() == null)
            throw new ApplicationRuntimeException("Receipt Number is mandatory");
        if (getPaymentMode() == null)
            throw new ApplicationRuntimeException("Payment Mode is mandatory");
        if (getTotalAmount() == null)
            throw new ApplicationRuntimeException("Total Amount is mandatory");
        if (getChannel() == null)
            throw new ApplicationRuntimeException("Channel is mandatory");
        if (getStatus() == null)
            throw new ApplicationRuntimeException("Receipt Status is mandatory");
    }
}
