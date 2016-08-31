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
package org.egov.works.abstractestimate.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Required;
import org.hibernate.validator.constraints.Length;

/**
 * Created by mani on 16/6/16.
 */
@Entity
@Table(name = "EGW_MEASUREMENTSHEET")
@SequenceGenerator(name = MeasurementSheet.SEQ, sequenceName = MeasurementSheet.SEQ, allocationSize = 1)

public class MeasurementSheet extends AbstractAuditable {
    /**
     *
     */
    private static final long serialVersionUID = -4863093339787656131L;

    public static final String SEQ = "SEQ_EGW_MEASUREMENTSHEET";

    @Id
    @GeneratedValue(generator = MeasurementSheet.SEQ, strategy = GenerationType.SEQUENCE)
    private Long id;
    private Integer slNo;
    private char identifier;

    @Length(max = 1024, message = "estimate.measurementSheet.remarks.length")
    private String remarks;

    @Max(1000)
    private BigDecimal no;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal depthOrHeight;
    @Required
    private BigDecimal quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activityid")
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private MeasurementSheet parent;

    @Override
    public Long getId() {
        return id;
    }

    public BigDecimal getLength() {
        return length;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Integer getSlNo() {
        return slNo;
    }

    public void setSlNo(final Integer slNo) {
        this.slNo = slNo;
    }

    public char getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final char identifier) {
        this.identifier = identifier;
    }

    public BigDecimal getNo() {
        return no;
    }

    public void setNo(final BigDecimal no) {
        this.no = no;
    }

    public void setLength(final BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(final BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getDepthOrHeight() {
        return depthOrHeight;
    }

    public void setDepthOrHeight(final BigDecimal depthOrHeight) {
        this.depthOrHeight = depthOrHeight;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(final BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(final Activity activity) {
        this.activity = activity;
    }

    public MeasurementSheet getParent() {
        return parent;
    }

    public void setParent(final MeasurementSheet parent) {
        this.parent = parent;
    }

}
