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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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

import org.egov.common.entity.UOM;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.GreaterThan;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.works.workorder.entity.WorkOrderActivity;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EGW_CONTRACTOR_MB_DETAILS")
@SequenceGenerator(name = ContractorMBDetails.SEQ_EGW_CONTRACTOR_MB_DETAILS, sequenceName = ContractorMBDetails.SEQ_EGW_CONTRACTOR_MB_DETAILS, allocationSize = 1)
public class ContractorMBDetails extends AbstractAuditable {

    private static final long serialVersionUID = -5088074625605584344L;

    public static final String SEQ_EGW_CONTRACTOR_MB_DETAILS = "SEQ_EGW_CONTRACTOR_MB_DETAILS";

    @Id
    @GeneratedValue(generator = SEQ_EGW_CONTRACTOR_MB_DETAILS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Required(message = "mbdetails.mbheader.null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRACTOR_MBHEADER_ID", nullable = false)
    @JsonIgnore
    private ContractorMBHeader contractorMBHeader;

    @Required(message = "mbdetails.activity.null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WO_ACTIVITY_ID", nullable = true)
    @JsonIgnore
    private WorkOrderActivity workOrderActivity;

    @GreaterThan(value = 0, message = "mbdetails.quantity.non.negative")
    private double quantity;

    private double rate;

    private double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom", nullable = true)
    private UOM uom;

    private String description;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contractorMBDetails", targetEntity = ContractorMBMeasurementSheet.class)
    @OrderBy("id")
    private List<ContractorMBMeasurementSheet> measurementSheets = new ArrayList<>();

    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        if (contractorMBHeader != null
                && (contractorMBHeader.getId() == null || contractorMBHeader.getId() == 0 || contractorMBHeader.getId() == -1))
            validationErrors.add(new ValidationError("mbHeader", "mbdetails.mbheader.null"));
        if (workOrderActivity != null && (workOrderActivity.getId() == null || workOrderActivity.getId() == 0
                || workOrderActivity.getId() == -1))
            validationErrors.add(new ValidationError("workOrderActivity", "mbdetails.activity.null"));
        return validationErrors;
    }

    public void setContractorMBHeader(final ContractorMBHeader contractorMBHeader) {
        this.contractorMBHeader = contractorMBHeader;
    }

    public ContractorMBHeader getContractorMBHeader() {
        return contractorMBHeader;
    }

    public void setQuantity(final double quantity) {
        this.quantity = quantity;
    }

    public double getQuantity() {
        return quantity;
    }

    public WorkOrderActivity getWorkOrderActivity() {
        return workOrderActivity;
    }

    public void setWorkOrderActivity(final WorkOrderActivity workOrderActivity) {
        this.workOrderActivity = workOrderActivity;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(final double rate) {
        this.rate = rate;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public List<ContractorMBMeasurementSheet> getMeasurementSheets() {
        return measurementSheets;
    }

    public void setMeasurementSheets(final List<ContractorMBMeasurementSheet> measurementSheets) {
        this.measurementSheets = measurementSheets;
    }

    public UOM getUom() {
        return uom;
    }

    public void setUom(final UOM uom) {
        this.uom = uom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
