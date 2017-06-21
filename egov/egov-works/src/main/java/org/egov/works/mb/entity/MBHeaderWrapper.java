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
package org.egov.works.mb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.GreaterThan;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.utils.WorksConstants;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MBHeaderWrapper implements Serializable {

    private static final long serialVersionUID = 121631467636260459L;

    @Required(message = "mbheader.workorder.null")
    private String workOrderNumber;

    @Required(message = "mbheader.mbrefno.null")
    private String mbRefNo;

    @Length(max = 400, message = "mbheader.contractorComments.length")
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHALLSPECIALCHAR, message = "mb.contractorComments.alphaNumeric")
    private String contractorComments;

    @Required(message = "mbheader.mbdate.null")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateFormat(message = "invalid.fieldvalue.mbDate")
    @Temporal(value = TemporalType.DATE)
    private Date mbDate;

    @Temporal(value = TemporalType.DATE)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date mbIssuedDate;

    @NotNull
    @Length(max = 400, message = "mbheader.mbabstract.length")
    @OptionalPattern(regex = WorksConstants.ALPHANUMERICWITHALLSPECIALCHAR, message = "mb.mbabstract.alphaNumeric")
    private String mbAbstract;

    @Required(message = "mbheader.fromPageNo.null")
    @GreaterThan(value = 0, message = "mbheader.fromPageNo.non.negative")
    private Integer fromPageNo;

    @Min(value = 0, message = "mbheader.toPageNo.non.negative")
    private Integer toPageNo;

    private transient List<MBDetails> sorMbDetails = new ArrayList<MBDetails>(0);

    private transient List<MBDetails> nonSorMbDetails = new ArrayList<MBDetails>(0);

    private transient List<MBDetails> nonTenderedMbDetails = new ArrayList<MBDetails>(0);

    private transient List<MBDetails> lumpSumMbDetails = new ArrayList<MBDetails>(0);

    private final transient List<DocumentDetails> documentDetails = new ArrayList<DocumentDetails>(0);

    private boolean isLegacyMB;

    @NotNull
    private BigDecimal mbAmount;
    
    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        if (StringUtils.isEmpty(workOrderNumber))
            validationErrors.add(new ValidationError("workOrder", "mbheader.workorder.null"));

        if (fromPageNo != null && toPageNo != null && fromPageNo > toPageNo)
            validationErrors.add(new ValidationError("toPageNo", "mbheader.toPageNo.invalid"));

        return validationErrors;
    }

    public void setMbRefNo(final String mbRefNo) {
        this.mbRefNo = mbRefNo;
    }

    public String getMbRefNo() {
        return mbRefNo;
    }

    public void setMbDate(final Date mbDate) {
        this.mbDate = mbDate;
    }

    public Date getMbDate() {
        return mbDate;
    }

    public void setMbAbstract(final String mbAbstract) {
        this.mbAbstract = mbAbstract;
    }

    public String getMbAbstract() {
        return mbAbstract;
    }

    public Integer getFromPageNo() {
        return fromPageNo;
    }

    public void setFromPageNo(final Integer fromPageNo) {
        this.fromPageNo = fromPageNo;
    }

    public Integer getToPageNo() {
        return toPageNo;
    }

    public void setToPageNo(final Integer toPageNo) {
        this.toPageNo = toPageNo;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public String getContractorComments() {
        return contractorComments;
    }

    public void setContractorComments(final String contractorComments) {
        this.contractorComments = contractorComments;
    }

    public boolean getIsLegacyMB() {
        return isLegacyMB;
    }

    public void setIsLegacyMB(final boolean isLegacyMB) {
        this.isLegacyMB = isLegacyMB;
    }

    public BigDecimal getMbAmount() {
        return mbAmount;
    }

    public void setMbAmount(final BigDecimal mbAmount) {
        this.mbAmount = mbAmount;
    }

    public List<MBDetails> getSorMbDetails() {
        return sorMbDetails;
    }

    public void setSorMbDetails(final List<MBDetails> sorMbDetails) {
        this.sorMbDetails = sorMbDetails;
    }

    public List<MBDetails> getNonSorMbDetails() {
        return nonSorMbDetails;
    }

    public void setNonSorMbDetails(final List<MBDetails> nonSorMbDetails) {
        this.nonSorMbDetails = nonSorMbDetails;
    }

    public Date getMbIssuedDate() {
        return mbIssuedDate;
    }

    public void setMbIssuedDate(final Date mbIssuedDate) {
        this.mbIssuedDate = mbIssuedDate;
    }

    public List<DocumentDetails> getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(final List<DocumentDetails> documentDetails) {
        this.documentDetails.clear();
        if (documentDetails != null)
            this.documentDetails.addAll(documentDetails);
    }

    public List<MBDetails> getNonTenderedMbDetails() {
        return nonTenderedMbDetails;
    }

    public void setNonTenderedMbDetails(final List<MBDetails> nonTenderedMbDetails) {
        this.nonTenderedMbDetails = nonTenderedMbDetails;
    }

    public List<MBDetails> getLumpSumMbDetails() {
        return lumpSumMbDetails;
    }

    public void setLumpSumMbDetails(final List<MBDetails> lumpSumMbDetails) {
        this.lumpSumMbDetails = lumpSumMbDetails;
    }
}
