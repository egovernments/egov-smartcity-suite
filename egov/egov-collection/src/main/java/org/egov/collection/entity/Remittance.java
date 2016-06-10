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
package org.egov.collection.entity;

import java.util.Date;

import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.entity.Auditable;
import org.egov.infra.workflow.entity.StateAware;

public class Remittance extends StateAware implements Auditable {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private String referenceNumber;
    private Date referenceDate;
    private CVoucherHeader voucherHeader;
    private CFunction function;
    private String remarks;
    private String reasonForDelay;
    private EgwStatus status;

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

/*    @Override
    public void setVersion(Long version) {
        this.version = version;
    }*/

    /**
     * @return the referenceNumber
     */
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * @param referenceNumber the referenceNumber to set
     */
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    /**
     * @return the referenceDate
     */
    public Date getReferenceDate() {
        return referenceDate;
    }

    /**
     * @param referenceDate the referenceDate to set
     */
    public void setReferenceDate(Date referenceDate) {
        this.referenceDate = referenceDate;
    }

    /**
     * @return the voucherHeader
     */
    public CVoucherHeader getVoucherHeader() {
        return voucherHeader;
    }

    /**
     * @param voucherHeader the voucherHeader to set
     */
    public void setVoucherHeader(CVoucherHeader voucherHeader) {
        this.voucherHeader = voucherHeader;
    }

    /**
     * @return the function
     */
    public CFunction getFunction() {
        return function;
    }

    /**
     * @param function the function to set
     */
    public void setFunction(CFunction function) {
        this.function = function;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the reasonForDelay
     */
    public String getReasonForDelay() {
        return reasonForDelay;
    }

    /**
     * @param reasonForDelay the reasonForDelay to set
     */
    public void setReasonForDelay(String reasonForDelay) {
        this.reasonForDelay = reasonForDelay;
    }

    /**
     * @return the status
     */
    public EgwStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(EgwStatus status) {
        this.status = status;
    }

}