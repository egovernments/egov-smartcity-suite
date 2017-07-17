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
package org.egov.wtms.application.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.commons.Installment;
import org.egov.infra.persistence.validator.annotation.CompositeUnique;

@Entity
@Table(name = "egwtr_mv_inst_dem_coll")
@CompositeUnique(fields = { "waterMatView.waterChargeViewEmbedded.connectiondetailsid",
"installment.id" }, message = "duplicate present")
public class InstDmdCollResponse implements Serializable{

    

    @Id
    private Long id;
    
    private static final long serialVersionUID = -8679117166285878366L;
    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    @JoinColumn(name = "connectiondetailsid", nullable = false)
    private WaterChargeMaterlizeView waterMatView;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    @JoinColumn(name = "id_installment", nullable = false)
    private Installment installment;
    
    @Column(name = "watercharge")
    private double waterCharge;
    
    @Column(name = "waterchargecoll")
    private double waterchargecoll;
    
    @Column(name = "createddate")
    private Date createdDate;
    
    public WaterChargeMaterlizeView getWaterMatView() {
        return waterMatView;
    }
    public void setWaterMatView(WaterChargeMaterlizeView waterMatView) {
        this.waterMatView = waterMatView;
    }
    
    public Installment getInstallment() {
        return installment;
    }
    public void setInstallment(Installment installment) {
        this.installment = installment;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public double getWaterCharge() {
        return waterCharge;
    }
    public void setWaterCharge(double waterCharge) {
        this.waterCharge = waterCharge;
    }
    public double getWaterchargecoll() {
        return waterchargecoll;
    }
    public void setWaterchargecoll(double waterchargecoll) {
        this.waterchargecoll = waterchargecoll;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
   


}