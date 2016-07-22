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
package org.egov.works.workorder.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.assets.model.Asset;
import org.egov.infra.persistence.entity.AbstractAuditable;

@Entity
@Table(name = "EGW_WORKORDER_ASSETS")
@SequenceGenerator(name = AssetsForWorkOrder.SEQ_EGW_WORKORDER_ASSETS, sequenceName = AssetsForWorkOrder.SEQ_EGW_WORKORDER_ASSETS, allocationSize = 1)
public class AssetsForWorkOrder extends AbstractAuditable {

    private static final long serialVersionUID = 1921548931869645727L;

    public static final String SEQ_EGW_WORKORDER_ASSETS = "SEQ_EGW_WORKORDER_ASSETS";

    @Id
    @GeneratedValue(generator = SEQ_EGW_WORKORDER_ASSETS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSET_ID", nullable = false)
    private Asset asset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKORDER_ESTIMATE_ID", nullable = false)
    private WorkOrderEstimate workOrderEstimate;

    public AssetsForWorkOrder() {
    }

    public AssetsForWorkOrder(final WorkOrderEstimate workOrderEstimate, final Asset asset) {
        this.workOrderEstimate = workOrderEstimate;
        this.asset = asset;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(final Asset asset) {
        this.asset = asset;
    }

    public WorkOrderEstimate getWorkOrderEstimate() {
        return workOrderEstimate;
    }

    public void setWorkOrderEstimate(final WorkOrderEstimate workOrderEstimate) {
        this.workOrderEstimate = workOrderEstimate;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

}
