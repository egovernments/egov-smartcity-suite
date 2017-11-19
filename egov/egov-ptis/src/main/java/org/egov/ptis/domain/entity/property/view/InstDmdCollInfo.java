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

package org.egov.ptis.domain.entity.property.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "EGPT_VIEW_INST_DEM_COLL")
public class InstDmdCollInfo implements Serializable{
	
	private static final long serialVersionUID = 3736399639203706920L;
	
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_BASIC_PROPERTY")
	private PropertyMVInfo propMatView;
	
	@Id
	@Column(name = "id_installment")
    private Integer installment;
	
    private BigDecimal generalTax;
	
    private BigDecimal libCessTax;
	
    private BigDecimal eduCessTax;
	
    private BigDecimal unauthPenaltyTax;
	
    private BigDecimal penaltyFinesTax;
	
    private BigDecimal sewTax;
	
    private BigDecimal vacantLandTax;
	
    private BigDecimal pubSerChrgTax;
	
    private BigDecimal generalTaxColl;
	
    private BigDecimal libCessTaxColl;
	
    private BigDecimal eduCessTaxColl;
	
    private BigDecimal unauthPenaltyTaxColl;
	
    private BigDecimal penaltyFinesTaxColl;
	
    private BigDecimal sewTaxColl;
	
    private BigDecimal vacantLandTaxColl;
	
    private BigDecimal pubSerChrgTaxColl;
	
    private Date createdDate;

    public PropertyMVInfo getPropMatView() {
        return propMatView;
    }

    public void setPropMatView(final PropertyMVInfo propMatView) {
        this.propMatView = propMatView;
    }

    public Integer getInstallment() {
        return installment;
    }

    public void setInstallment(final Integer installment) {
        this.installment = installment;
    }

    public BigDecimal getGeneralTax() {
        return generalTax;
    }

    public void setGeneralTax(final BigDecimal generalTax) {
        this.generalTax = generalTax;
    }

    public BigDecimal getLibCessTax() {
        return libCessTax;
    }

    public void setLibCessTax(final BigDecimal libCessTax) {
        this.libCessTax = libCessTax;
    }

    public BigDecimal getEduCessTax() {
        return eduCessTax;
    }

    public void setEduCessTax(final BigDecimal eduCessTax) {
        this.eduCessTax = eduCessTax;
    }

    public BigDecimal getUnauthPenaltyTax() {
        return unauthPenaltyTax;
    }

    public void setUnauthPenaltyTax(final BigDecimal unauthPenaltyTax) {
        this.unauthPenaltyTax = unauthPenaltyTax;
    }

    public BigDecimal getPenaltyFinesTax() {
        return penaltyFinesTax;
    }

    public void setPenaltyFinesTax(final BigDecimal penaltyFinesTax) {
        this.penaltyFinesTax = penaltyFinesTax;
    }

    public BigDecimal getSewTax() {
        return sewTax;
    }

    public void setSewTax(final BigDecimal sewTax) {
        this.sewTax = sewTax;
    }

    public BigDecimal getVacantLandTax() {
        return vacantLandTax;
    }

    public void setVacantLandTax(final BigDecimal vacantLandTax) {
        this.vacantLandTax = vacantLandTax;
    }

    public BigDecimal getPubSerChrgTax() {
        return pubSerChrgTax;
    }

    public void setPubSerChrgTax(final BigDecimal pubSerChrgTax) {
        this.pubSerChrgTax = pubSerChrgTax;
    }

    public BigDecimal getGeneralTaxColl() {
        return generalTaxColl;
    }

    public void setGeneralTaxColl(final BigDecimal generalTaxColl) {
        this.generalTaxColl = generalTaxColl;
    }

    public BigDecimal getLibCessTaxColl() {
        return libCessTaxColl;
    }

    public void setLibCessTaxColl(final BigDecimal libCessTaxColl) {
        this.libCessTaxColl = libCessTaxColl;
    }

    public BigDecimal getEduCessTaxColl() {
        return eduCessTaxColl;
    }

    public void setEduCessTaxColl(final BigDecimal eduCessTaxColl) {
        this.eduCessTaxColl = eduCessTaxColl;
    }

    public BigDecimal getUnauthPenaltyTaxColl() {
        return unauthPenaltyTaxColl;
    }

    public void setUnauthPenaltyTaxColl(final BigDecimal unauthPenaltyTaxColl) {
        this.unauthPenaltyTaxColl = unauthPenaltyTaxColl;
    }

    public BigDecimal getPenaltyFinesTaxColl() {
        return penaltyFinesTaxColl;
    }

    public void setPenaltyFinesTaxColl(final BigDecimal penaltyFinesTaxColl) {
        this.penaltyFinesTaxColl = penaltyFinesTaxColl;
    }

    public BigDecimal getSewTaxColl() {
        return sewTaxColl;
    }

    public void setSewTaxColl(final BigDecimal sewTaxColl) {
        this.sewTaxColl = sewTaxColl;
    }

    public BigDecimal getVacantLandTaxColl() {
        return vacantLandTaxColl;
    }

    public void setVacantLandTaxColl(final BigDecimal vacantLandTaxColl) {
        this.vacantLandTaxColl = vacantLandTaxColl;
    }

    public BigDecimal getPubSerChrgTaxColl() {
        return pubSerChrgTaxColl;
    }

    public void setPubSerChrgTaxColl(final BigDecimal pubSerChrgTaxColl) {
        this.pubSerChrgTaxColl = pubSerChrgTaxColl;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

}
