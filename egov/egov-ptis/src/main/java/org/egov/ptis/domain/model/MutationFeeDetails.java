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
package org.egov.ptis.domain.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.egov.infra.persistence.entity.AbstractAuditable;

@Entity
@Table(name = "EGPT_MUTATION_FEE_DETAILS")
@SequenceGenerator(name = MutationFeeDetails.SEQ_MUTATION_FEE_DETAILS, sequenceName = MutationFeeDetails.SEQ_MUTATION_FEE_DETAILS, allocationSize = 1)
public class MutationFeeDetails extends AbstractAuditable {

    private static final long serialVersionUID = 1521640343172434478L;

    public static final String SEQ_MUTATION_FEE_DETAILS = "SEQ_EGPT_MUTATION_FEE_DETAILS";

    @Id
    @GeneratedValue(generator = SEQ_MUTATION_FEE_DETAILS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "low_limit")
    private BigDecimal lowLimit;

    @Column(name = "high_limit")
    private BigDecimal highLimit;

    private Date fromDate;

    private Date toDate;

    @Column(name = "flat_amount")
    private BigDecimal flatAmount;

    private BigDecimal percentage;

    @Column(name = "is_recursive")
    private Character isRecursive='N';

    @Column(name = "recursive_factor")
    private BigDecimal recursiveFactor;

    @Column(name = "recursive_amount")
    private BigDecimal recursiveAmount;

    @Column(name = "slab_name")
    private String slabName;

    public String getSlabName() {
        return slabName;
    }

    public void setSlabName(String slabName) {
        this.slabName = slabName;
    }

    @Override
    protected void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public BigDecimal getLowLimit() {
        return lowLimit;
    }

    public void setLowLimit(BigDecimal lowLimit) {
        this.lowLimit = lowLimit;
    }

    public BigDecimal getHighLimit() {
        return highLimit;
    }

    public void setHighLimit(BigDecimal highLimit) {
        this.highLimit = highLimit;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getFlatAmount() {
        return flatAmount;
    }

    public void setFlatAmount(BigDecimal flatAmount) {
        this.flatAmount = flatAmount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public Character getIsRecursive() {
        return isRecursive;
    }

    public void setIsRecursive(Character isRecursive) {
        this.isRecursive = isRecursive;
    }

    public BigDecimal getRecursiveFactor() {
        return recursiveFactor;
    }

    public void setRecursiveFactor(BigDecimal recursiveFactor) {
        this.recursiveFactor = recursiveFactor;
    }

    public BigDecimal getRecursiveAmount() {
        return recursiveAmount;
    }

    public void setRecursiveAmount(BigDecimal recursiveAmount) {
        this.recursiveAmount = recursiveAmount;
    }

}
