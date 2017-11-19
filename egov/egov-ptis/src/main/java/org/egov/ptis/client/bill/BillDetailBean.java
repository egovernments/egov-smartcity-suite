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
package org.egov.ptis.client.bill;

import org.egov.commons.Installment;

import java.math.BigDecimal;

import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_REBATE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_ADVANCE_REBATE;

public class BillDetailBean {

    private Installment installment;
    private Integer orderNo;
    private BigDecimal amount;
    private String key;
    private String glCode;
    private String reasonMaster;
    private String description;
    private Integer isActualDemand;
    private String purpose;

    public BillDetailBean() {
    }

    public BillDetailBean(Installment installment, Integer orderNo, String key, BigDecimal billDetailAmount,
            String glCode, String reasonMaster, Integer isActualDemand, String purpose) {
        this.installment = installment;
        this.orderNo = orderNo;
        this.amount = billDetailAmount;
        this.key = key;
        this.glCode = glCode;
        this.reasonMaster = reasonMaster;
        this.isActualDemand = isActualDemand;
        this.purpose = purpose;

        if (reasonMaster.equalsIgnoreCase(DEMANDRSN_STR_ADVANCE_REBATE)) {
            this.description = reasonMaster + "-" + key;
        } else {
            this.description = reasonMaster + "-" + installment.getDescription();
        }
    }

    public boolean isRebate() {
        return reasonMaster.equalsIgnoreCase(DEMANDRSN_STR_ADVANCE_REBATE)
                || reasonMaster.equalsIgnoreCase(DEMANDRSN_CODE_REBATE) ? true : false;
    }

    public boolean invalidData() {
        return this.orderNo == null || this.amount == null || this.glCode == null ? true : false;
    }

    @Override
    public String toString() {
        return new StringBuilder(200).append("BillDetailBean [").append("installment=").append(installment)
                .append(", reasonMaster=").append(reasonMaster).append(", description=").append(description)
                .append(", glCode=").append(glCode).append(", orderNo=").append(orderNo).append(", key=").append(key)
                .append(", isActualDemand=").append(isActualDemand).append(purpose).append("]").toString();
    }

    public Installment getInstallment() {
        return installment;
    }

    public void setInstallment(Installment installment) {
        this.installment = installment;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(String glCode) {
        this.glCode = glCode;
    }

    public String getReasonMaster() {
        return reasonMaster;
    }

    public void setReasonMaster(String reasonMaster) {
        this.reasonMaster = reasonMaster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsActualDemand() {
        return isActualDemand;
    }

    public void setIsActualDemand(Integer isActualDemand) {
        this.isActualDemand = isActualDemand;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
