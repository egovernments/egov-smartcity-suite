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
package org.egov.wtms.application.entity;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.wtms.masters.entity.RoadCategory;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "egwtr_fieldinspection_details")
@SequenceGenerator(name = FieldInspectionDetails.SEQ_FIELDINSPECTIONDETAILS, sequenceName = FieldInspectionDetails.SEQ_FIELDINSPECTIONDETAILS, allocationSize = 1)
public class FieldInspectionDetails extends AbstractAuditable {

    private static final long serialVersionUID = 4381725568940895413L;

    public static final String SEQ_FIELDINSPECTIONDETAILS = "SEQ_EGWTR_FIELDINSPECTION_DETAILS";

    @Id
    @GeneratedValue(generator = SEQ_FIELDINSPECTIONDETAILS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "connectiondetailsid", nullable = false)
    private WaterConnectionDetails waterConnectionDetails;

    @ManyToOne
    @JoinColumn(name = "roadcategory")
    private RoadCategory roadCategory;

    @SafeHtml
    @Length(max = 50)
    private String existingPipeline;

    private double pipelineDistance;

    @NotNull
    private double estimationCharges;
    
    private double supervisionCharges;
    private double securityDeposit;
    private double roadCuttingCharges;
    private double applicationFee;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public WaterConnectionDetails getWaterConnectionDetails() {
        return waterConnectionDetails;
    }

    public void setWaterConnectionDetails(final WaterConnectionDetails waterConnectionDetails) {
        this.waterConnectionDetails = waterConnectionDetails;
    }

    public RoadCategory getRoadCategory() {
        return roadCategory;
    }

    public void setRoadCategory(final RoadCategory roadCategory) {
        this.roadCategory = roadCategory;
    }

    public String getExistingPipeline() {
        return existingPipeline;
    }

    public void setExistingPipeline(final String existingPipeline) {
        this.existingPipeline = existingPipeline;
    }

    public double getPipelineDistance() {
        return pipelineDistance;
    }

    public void setPipelineDistance(final double pipelineDistance) {
        this.pipelineDistance = pipelineDistance;
    }

    public double getEstimationCharges() {
        return estimationCharges;
    }

    public void setEstimationCharges(final double estimationCharges) {
        this.estimationCharges = estimationCharges;
    }
    
    public double getSupervisionCharges() {
        return supervisionCharges;
    }

    public void setSupervisionCharges(double supervisionCharges) {
        this.supervisionCharges = supervisionCharges;
    }

    public double getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(double securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public double getRoadCuttingCharges() {
        return roadCuttingCharges;
    }

    public void setRoadCuttingCharges(double roadCuttingCharges) {
        this.roadCuttingCharges = roadCuttingCharges;
    }

    public double getApplicationFee() {
        return applicationFee;
    }

    public void setApplicationFee(double applicationFee) {
        this.applicationFee = applicationFee;
    }

}
