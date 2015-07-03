/*    eGov suite of products aim to improve the internal efficiency,transparency,
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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.infra.workflow.entity.StateAware;

public class PropertyMutation extends StateAware {

    private static final long serialVersionUID = -3387659460257524470L;
    private Long id;
    private PropertyMutationMaster mutationReason;
    private Date mutationDate;
    private BigDecimal mutationFee;
    private BigDecimal marketValue;
    private BigDecimal otherFee;
    private String receiptNum;
    private Date receiptDate;
    private String applicationNo;
    private String applicantName;
    private BasicProperty basicProperty;
    private Property property;
    private List<PropertyOwnerInfo> transferorInfos = new ArrayList<PropertyOwnerInfo>();
    private boolean feePayable;
    private String deedNo;
    private Date deedDate;
    private String documentNumber;

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getStateDetails() {
        return "Transfer Property";
    }

    public PropertyMutationMaster getMutationReason() {
        return mutationReason;
    }

    public void setMutationReason(final PropertyMutationMaster mutationReason) {
        this.mutationReason = mutationReason;
    }

    public Date getMutationDate() {
        return mutationDate;
    }

    public void setMutationDate(final Date mutationDate) {
        this.mutationDate = mutationDate;
    }

    public BigDecimal getMutationFee() {
        return mutationFee;
    }

    public void setMutationFee(final BigDecimal mutationFee) {
        this.mutationFee = mutationFee;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(final BigDecimal marketValue) {
        this.marketValue = marketValue;
    }

    public BigDecimal getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(final BigDecimal otherFee) {
        this.otherFee = otherFee;
    }

    public String getReceiptNum() {
        return receiptNum;
    }

    public void setReceiptNum(final String receiptNum) {
        this.receiptNum = receiptNum;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(final Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(final String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(final String applicantName) {
        this.applicantName = applicantName;
    }

    public BasicProperty getBasicProperty() {
        return basicProperty;
    }

    public void setBasicProperty(final BasicProperty basicProperty) {
        this.basicProperty = basicProperty;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(final Property property) {
        this.property = property;
    }

    public List<PropertyOwnerInfo> getTransferorInfos() {
        return transferorInfos;
    }

    public void setTransferorInfos(final List<PropertyOwnerInfo> transferorInfos) {
        this.transferorInfos = transferorInfos;
    }

    public boolean isFeePayable() {
        return feePayable;
    }

    public void setFeePayable(final boolean feePayable) {
        this.feePayable = feePayable;
    }

    public String getDeedNo() {
        return deedNo;
    }

    public void setDeedNo(final String deedNo) {
        this.deedNo = deedNo;
    }

    public Date getDeedDate() {
        return deedDate;
    }

    public void setDeedDate(final Date deedDate) {
        this.deedDate = deedDate;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
    }
}
