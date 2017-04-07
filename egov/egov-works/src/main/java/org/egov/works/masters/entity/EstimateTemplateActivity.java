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

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.common.entity.UOM;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.works.abstractestimate.entity.NonSor;

@Entity
@Table(name = "EGW_EST_TEMPLATE_ACTIVITY")
@Unique(id = "id", tableName = "EGW_EST_TEMPLATE_ACTIVITY", enableDfltMsg = true)
@SequenceGenerator(name = EstimateTemplateActivity.SEQ_EGW_EST_TEMPLATE_ACTIVITY, sequenceName = EstimateTemplateActivity.SEQ_EGW_EST_TEMPLATE_ACTIVITY, allocationSize = 1)
public class EstimateTemplateActivity extends AbstractAuditable {

    private static final long serialVersionUID = 7697746931463590763L;

    public static final String SEQ_EGW_EST_TEMPLATE_ACTIVITY = "SEQ_EGW_EST_TEMPLATE_ACTIVITY";

    @Id
    @GeneratedValue(generator = SEQ_EGW_EST_TEMPLATE_ACTIVITY, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTIMATE_TEMPLATE_ID", nullable = false, updatable = false)
    private EstimateTemplate estimateTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULEOFRATE_ID")
    private ScheduleOfRate schedule;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "NONSOR_ID")
    private NonSor nonSor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UOM_ID")
    private UOM uom;

    private double value;

    private transient Date estimateDate;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public EstimateTemplate getEstimateTemplate() {
        return estimateTemplate;
    }

    public void setEstimateTemplate(final EstimateTemplate estimateTemplate) {
        this.estimateTemplate = estimateTemplate;
    }

    public ScheduleOfRate getSchedule() {
        return schedule;
    }

    public void setSchedule(final ScheduleOfRate schedule) {
        this.schedule = schedule;
    }

    public NonSor getNonSor() {
        return nonSor;
    }

    public void setNonSor(final NonSor nonSor) {
        this.nonSor = nonSor;
    }

    public UOM getUom() {
        return uom;
    }

    public void setUom(final UOM uom) {
        this.uom = uom;
    }

    public Date getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(final Date estimateDate) {
        this.estimateDate = estimateDate;
    }

    public double getValue() {
        return value;
    }

    public void setValue(final double value) {
        this.value = value;
    }

}
