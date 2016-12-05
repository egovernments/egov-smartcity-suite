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
package org.egov.works.masters.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.entity.component.Period;

@Entity
@Table(name = "EGW_OVERHEAD_RATE")
@SequenceGenerator(name = OverheadRate.SEQ_EGW_OVERHEAD_RATE, sequenceName = OverheadRate.SEQ_EGW_OVERHEAD_RATE, allocationSize = 1)
public class OverheadRate extends AbstractAuditable {

    private static final long serialVersionUID = 474905206086516812L;

    public static final String SEQ_EGW_OVERHEAD_RATE = "SEQ_EGW_OVERHEAD_RATE";

    @Id
    @GeneratedValue(generator = SEQ_EGW_OVERHEAD_RATE, strategy = GenerationType.SEQUENCE)
    private Long id;

    private Double lumpsumAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "overhead", nullable = false)
    private Overhead overhead;

    private Period validity;

    private Double percentage;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Overhead getOverhead() {
        return overhead;
    }

    public void setOverhead(final Overhead overhead) {
        this.overhead = overhead;
    }

    public Period getValidity() {
        return validity;
    }

    public void setValidity(final Period validity) {
        this.validity = validity;
    }

    public Double getLumpsumAmount() {
        return lumpsumAmount;
    }

    public void setLumpsumAmount(final Double lumpsumAmount) {
        this.lumpsumAmount = lumpsumAmount;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(final Double percentage) {
        this.percentage = percentage;
    }

}