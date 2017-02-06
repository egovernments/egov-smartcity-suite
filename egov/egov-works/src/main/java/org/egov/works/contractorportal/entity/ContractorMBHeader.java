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
package org.egov.works.contractorportal.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.workorder.entity.WorkOrderActivity;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EGW_CONTRACTOR_MB_HEADER")
@SequenceGenerator(name = ContractorMBHeader.SEQ_EGW_CONTRACTOR_MB_HEADER, sequenceName = ContractorMBHeader.SEQ_EGW_CONTRACTOR_MB_HEADER, allocationSize = 1)
public class ContractorMBHeader extends AbstractAuditable {

    private static final long serialVersionUID = 121631467636260459L;

    public static final String SEQ_EGW_CONTRACTOR_MB_HEADER = "SEQ_EGW_CONTRACTOR_MB_HEADER";

    @Id
    @GeneratedValue(generator = SEQ_EGW_CONTRACTOR_MB_HEADER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Required(message = "mbheader.mbrefno.null")
    @Length(max = 50, message = "mbheader.mbrefno.length")
    @Column(name = "MB_REFNO")
    private String mbRefNo;

    @Required(message = "mbheader.mbdate.null")
    @ValidateDate(allowPast = true, dateFormat = "dd/MM/yyyy", message = "mbheader.mbDate.futuredate")
    @DateFormat(message = "invalid.fieldvalue.mbDate")
    @Temporal(value = TemporalType.DATE)
    @Column(name = "MB_DATE")
    private Date mbDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKORDER_ESTIMATE_ID")
    private WorkOrderEstimate workOrderEstimate;

    @Valid
    @JsonIgnore
    @OrderBy("id")
    @OneToMany(mappedBy = "contractorMBHeader", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = ContractorMBDetails.class)
    private final List<ContractorMBDetails> contractorMBDetails = new ArrayList<ContractorMBDetails>(0);

    private transient List<DocumentDetails> documentDetails = new ArrayList<DocumentDetails>(0);

    private transient List<ContractorMBDetails> workOrderBOQs = new ArrayList<ContractorMBDetails>(0);

    private transient List<ContractorMBDetails> additionalMBDetails = new ArrayList<ContractorMBDetails>(0);

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATUS_ID")
    private EgwStatus egwStatus;

    @Column(name = "MB_AMOUNT")
    private BigDecimal mbAmount;

    @Length(max = 400, message = "mbdetails.remark.length")
    private String remarks;

    public void setMbDate(final Date mbDate) {
        this.mbDate = mbDate;
    }

    public Date getMbDate() {
        return mbDate;
    }

    public WorkOrderEstimate getWorkOrderEstimate() {
        return workOrderEstimate;
    }

    public void setWorkOrderEstimate(final WorkOrderEstimate workOrderEstimate) {
        this.workOrderEstimate = workOrderEstimate;
    }

    public EgwStatus getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(final EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
    }

    public BigDecimal getMbAmount() {
        return mbAmount;
    }

    public void setMbAmount(final BigDecimal mbAmount) {
        this.mbAmount = mbAmount;
    }

    public BigDecimal getTotalMBAmount() {
        double amount;
        BigDecimal resultAmount = BigDecimal.ZERO;
        for (final ContractorMBDetails mbd : contractorMBDetails) {
            WorkOrderActivity woa = mbd.getWorkOrderActivity();
            if (woa != null) {
                if (woa.getActivity().getNonSor() == null)
                    amount = woa.getApprovedRate() * mbd.getQuantity() * woa.getConversionFactor();
                else
                    amount = woa.getApprovedRate() * mbd.getQuantity();
                resultAmount = resultAmount.add(BigDecimal.valueOf(amount));
            }
        }
        return resultAmount;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public List<DocumentDetails> getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(final List<DocumentDetails> documentDetails) {
        this.documentDetails.clear();
        if (documentDetails != null)
            this.documentDetails.addAll(documentDetails);
    }

    public List<ContractorMBDetails> getContractorMBDetails() {
        return contractorMBDetails;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public void setContractorMBDetails(final List<ContractorMBDetails> contractorMBDetails) {
        this.contractorMBDetails.clear();
        if (contractorMBDetails != null)
            this.contractorMBDetails.addAll(contractorMBDetails);
    }

    public String getMbRefNo() {
        return mbRefNo;
    }

    public void setMbRefNo(final String mbRefNo) {
        this.mbRefNo = mbRefNo;
    }

    public List<ContractorMBDetails> getAdditionalMBDetails() {
        return additionalMBDetails;
    }

    public void setAdditionalMBDetails(final List<ContractorMBDetails> additionalMBDetails) {
        this.additionalMBDetails = additionalMBDetails;
    }

    public List<ContractorMBDetails> getWorkOrderBOQs() {
        return workOrderBOQs;
    }

    public void setWorkOrderBOQs(final List<ContractorMBDetails> workOrderBOQs) {
        this.workOrderBOQs = workOrderBOQs;
    }

    public Collection<ContractorMBDetails> getWorkOrderBOQDetails() {
        return CollectionUtils.select(contractorMBDetails,
                detail -> ((ContractorMBDetails) detail).getWorkOrderActivity() != null);
    }

    public Collection<ContractorMBDetails> getAdditionalDetails() {
        return CollectionUtils.select(contractorMBDetails,
                detail -> ((ContractorMBDetails) detail).getWorkOrderActivity() == null);
    }
}