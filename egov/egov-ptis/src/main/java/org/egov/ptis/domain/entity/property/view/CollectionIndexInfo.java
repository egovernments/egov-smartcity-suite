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

package org.egov.ptis.domain.entity.property.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "EGPT_COLLECTIONINDEX")
public class CollectionIndexInfo implements Serializable {

    private static final long serialVersionUID = 2264439155092199090L;

    @Id
    @Column(name = "receiptnumber")
    private String id;

    @Column(name = "receiptdate")
    private Date receiptDate;

    @Column(name = "createddate")
    private Date createdDate;

    @Column(name = "modifieddate")
    private Date modifiedDate;

    @Column(name = "billingservice")
    private String billingService;

    @Column(name = "paymentmode")
    private String paymentMode;

    @Column(name = "arrearamount")
    private BigDecimal arrearAmount;

    @Column(name = "arrearlibcess")
    private BigDecimal arrearLibCess;
    @Column(name = "currentamount")
    private BigDecimal currentAmount;

    @Column(name = "currentlibces")
    private BigDecimal currentLibCes;

    @Column(name = "arrearedutax")
    private BigDecimal arrearEduTax;

    @Column(name = "currentedutax")
    private BigDecimal currentEduTax;

    @Column(name = "penalty")
    private BigDecimal penalty;
    @Column(name = "latepaymentcharges")
    private BigDecimal latePaymentCharges;

    @Column(name = "totalamount")
    private BigDecimal totalAmount;
    @Column(name = "advanceamount")
    private BigDecimal advanceAmount;
    @Column(name = "rebateamount")
    private BigDecimal rebateAmount;

    @Column(name = "channel")
    private String channel;

    @Column(name = "paymentgateway")
    private String paymentGateway;

    @Column(name = "billnumber")
    private String billNumber;

    @Column(name = "consumercode")
    private String consumerCode;

    @Column(name = "frominstallment")
    private String fromInstallment;

    @Column(name = "toinstallment")
    private String toInstallment;

    @Column(name = "status")
    private String status;

    @Column(name = "payeename")
    private String payeeName;

    @Column(name = "ulbname")
    private String ulbName;

    @Column(name = "districtname")
    private String districtName;

    @Column(name = "regionname")
    private String regionName;

    @ManyToOne
    @JoinColumn(name = "createdby")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "modifiedby")
    private User modifiedBy;

    @Column(name = "revenueward")
    private String wardId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getBillingService() {
        return billingService;
    }

    public void setBillingService(String billingService) {
        this.billingService = billingService;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public BigDecimal getArrearAmount() {
        return arrearAmount;
    }

    public void setArrearAmount(BigDecimal arrearAmount) {
        this.arrearAmount = arrearAmount;
    }

    public BigDecimal getArrearLibCess() {
        return arrearLibCess;
    }

    public void setArrearLibCess(BigDecimal arrearLibCess) {
        this.arrearLibCess = arrearLibCess;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public BigDecimal getCurrentLibCes() {
        return currentLibCes;
    }

    public void setCurrentLibCes(BigDecimal currentLibCes) {
        this.currentLibCes = currentLibCes;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }

    public BigDecimal getLatePaymentCharges() {
        return latePaymentCharges;
    }

    public void setLatePaymentCharges(BigDecimal latePaymentCharges) {
        this.latePaymentCharges = latePaymentCharges;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(BigDecimal advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    public BigDecimal getRebateAmount() {
        return rebateAmount;
    }

    public void setRebateAmount(BigDecimal rebateAmount) {
        this.rebateAmount = rebateAmount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getFromInstallment() {
        return fromInstallment;
    }

    public void setFromInstallment(String fromInstallment) {
        this.fromInstallment = fromInstallment;
    }

    public String getToInstallment() {
        return toInstallment;
    }

    public void setToInstallment(String toInstallment) {
        this.toInstallment = toInstallment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(String ulbName) {
        this.ulbName = ulbName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }

    public BigDecimal getArrearEduTax() {
        return arrearEduTax;
    }

    public void setArrearEduTax(BigDecimal arrearEduTax) {
        this.arrearEduTax = arrearEduTax;
    }

    public BigDecimal getCurrentEduTax() {
        return currentEduTax;
    }

    public void setCurrentEduTax(BigDecimal currentEduTax) {
        this.currentEduTax = currentEduTax;
    }
}
