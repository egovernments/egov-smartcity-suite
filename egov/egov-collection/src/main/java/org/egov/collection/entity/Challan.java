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

import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.entity.Auditable;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.models.ServiceDetails;
import org.egov.pims.commons.Position;

import java.util.Date;

public class Challan extends StateAware<Position> implements Auditable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private ReceiptHeader receiptHeader;
    private EgwStatus status;
    private String challanNumber;
    private Date challanDate;
    private Date validUpto;
    private ServiceDetails service;
    private String reasonForCancellation;
    private CVoucherHeader voucherHeader;
    private String oldChallanNumber;

    public ReceiptHeader getReceiptHeader() {
        return receiptHeader;
    }

    public void setReceiptHeader(final ReceiptHeader receiptHeader) {
        this.receiptHeader = receiptHeader;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
    }

    public String getChallanNumber() {
        return challanNumber;
    }

    public void setChallanNumber(final String challanNumber) {
        this.challanNumber = challanNumber;
    }

    public Date getValidUpto() {
        return validUpto;
    }

    public void setValidUpto(final Date validUpto) {
        this.validUpto = validUpto;
    }

    public Date getChallanDate() {
        return null == challanDate ? null : challanDate;
    }

    public void setChallanDate(final Date challanDate) {
        this.challanDate = challanDate;
    }

    public ServiceDetails getService() {
        return service;
    }

    public void setService(final ServiceDetails service) {
        this.service = service;
    }

    public String getReasonForCancellation() {
        return reasonForCancellation;
    }

    public void setReasonForCancellation(final String reasonForCancellation) {
        this.reasonForCancellation = reasonForCancellation;
    }

    public CVoucherHeader getVoucherHeader() {
        return voucherHeader;
    }

    public void setVoucherHeader(final CVoucherHeader voucherHeader) {
        this.voucherHeader = voucherHeader;
    }

    public String getOldChallanNumber() {
        return oldChallanNumber;
    }

    public void setOldChallanNumber(final String oldChallanNumber) {
        this.oldChallanNumber = oldChallanNumber;
    }

    @Override
    public String getStateDetails() {
        return "Challan - " + challanNumber;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }
}
