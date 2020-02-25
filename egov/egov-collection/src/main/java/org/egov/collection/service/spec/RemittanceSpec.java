/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
package org.egov.collection.service.spec;

import java.util.Date;

public class RemittanceSpec {

    private String[] serviceNameArray;
    private String[] approverIdArray;
    private String[] receiptDateArray;
    private String[] fundCodeArray;
    private String[] departmentCodeArray;
    private String[] instrumentIdArray;
    private String[] totalAmount;
    private Integer accountNumberId;
    private Date remittanceDate;

    private RemittanceSpec() {
    }

    public static Builder build() {
        return new Builder();
    }

    public static final class Builder {

        private String[] serviceNameArray;
        private String[] approverIdArray;
        private String[] receiptDateArray;
        private String[] fundCodeArray;
        private String[] departmentCodeArray;
        private String[] instrumentIdArray;
        private String[] totalAmount;
        private Integer accountNumberId;
        private Date remittanceDate;

        private Builder() {

        }

        public Builder withServiceNameArray(final String[] serviceNameArray) {
            this.serviceNameArray = serviceNameArray;
            return this;
        }

        public Builder withApproverIdArray(final String[] approverIdArray) {
            this.approverIdArray = approverIdArray;
            return this;
        }

        public Builder withReceiptDateArray(final String[] receiptDateArray) {
            this.receiptDateArray = receiptDateArray;
            return this;
        }

        public Builder withFundCodeArray(final String[] fundCodeArray) {
            this.fundCodeArray = fundCodeArray;
            return this;
        }

        public Builder withDepartmentCodeArray(final String[] departmentCodeArray) {
            this.departmentCodeArray = departmentCodeArray;
            return this;
        }

        public Builder withInstrumentIdArray(final String[] instrumentIdArray) {
            this.instrumentIdArray = instrumentIdArray;
            return this;
        }

        public Builder withTotalAmount(final String[] totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder withAccountNumberId(final Integer accountNumberId) {
            this.accountNumberId = accountNumberId;
            return this;
        }

        public Builder withRemittanceDate(final Date remittanceDate) {
            this.remittanceDate = remittanceDate;
            return this;
        }

        public RemittanceSpec build() {
            final RemittanceSpec remittanceSpec = new RemittanceSpec();
            remittanceSpec.serviceNameArray = this.serviceNameArray;
            remittanceSpec.approverIdArray = this.approverIdArray;
            remittanceSpec.receiptDateArray = this.receiptDateArray;
            remittanceSpec.fundCodeArray = this.fundCodeArray;
            remittanceSpec.departmentCodeArray = this.departmentCodeArray;
            remittanceSpec.instrumentIdArray = this.instrumentIdArray;
            remittanceSpec.totalAmount = this.totalAmount;
            remittanceSpec.accountNumberId = this.accountNumberId;
            remittanceSpec.remittanceDate = this.remittanceDate;
            return remittanceSpec;
        }
    }

    public String[] getServiceNameArray() {
        return serviceNameArray;
    }

    public void setServiceNameArray(String[] serviceNameArray) {
        this.serviceNameArray = serviceNameArray;
    }

    public String[] getApproverIdArray() {
        return approverIdArray;
    }

    public void setApproverIdArray(String[] approverIdArray) {
        this.approverIdArray = approverIdArray;
    }

    public String[] getReceiptDateArray() {
        return receiptDateArray;
    }

    public void setReceiptDateArray(String[] receiptDateArray) {
        this.receiptDateArray = receiptDateArray;
    }

    public String[] getFundCodeArray() {
        return fundCodeArray;
    }

    public void setFundCodeArray(String[] fundCodeArray) {
        this.fundCodeArray = fundCodeArray;
    }

    public String[] getDepartmentCodeArray() {
        return departmentCodeArray;
    }

    public void setDepartmentCodeArray(String[] departmentCodeArray) {
        this.departmentCodeArray = departmentCodeArray;
    }

    public String[] getInstrumentIdArray() {
        return instrumentIdArray;
    }

    public void setInstrumentIdArray(String[] instrumentIdArray) {
        this.instrumentIdArray = instrumentIdArray;
    }

    public String[] getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String[] totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getAccountNumberId() {
        return accountNumberId;
    }

    public void setAccountNumberId(Integer accountNumberId) {
        this.accountNumberId = accountNumberId;
    }

    public Date getRemittanceDate() {
        return remittanceDate;
    }

    public void setRemittanceDate(Date remittanceDate) {
        this.remittanceDate = remittanceDate;
    }
}
