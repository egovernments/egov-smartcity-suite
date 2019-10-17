/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2019  eGovernments Foundation
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

package org.egov.ptis.domain.model.demandvoucher;

import java.math.BigDecimal;

import org.egov.commons.Installment;

public class NormalizeDemandDetails {

    Installment installment;
    BigDecimal commonTax = BigDecimal.ZERO;
    BigDecimal commonTaxCollection = BigDecimal.ZERO;
    BigDecimal generalTax = BigDecimal.ZERO;
    BigDecimal generalTaxCollection = BigDecimal.ZERO;
    BigDecimal vacantLandTax = BigDecimal.ZERO;
    BigDecimal vacantLandTaxCollection = BigDecimal.ZERO;
    BigDecimal libraryCess = BigDecimal.ZERO;
    BigDecimal libraryCessCollection = BigDecimal.ZERO;
    BigDecimal penalty = BigDecimal.ZERO;
    BigDecimal penaltyCollection = BigDecimal.ZERO;
    BigDecimal advance = BigDecimal.ZERO;
    String purpose;

    public Installment getInstallment() {
        return installment;
    }

    public void setInstallment(Installment installment) {
        this.installment = installment;
    }

    public BigDecimal getCommonTax() {
        return commonTax;
    }

    public void setCommonTax(BigDecimal commonTax) {
        this.commonTax = commonTax;
    }

    public BigDecimal getCommonTaxCollection() {
        return commonTaxCollection;
    }

    public void setCommonTaxCollection(BigDecimal commonTaxCollection) {
        this.commonTaxCollection = commonTaxCollection;
    }

    public BigDecimal getGeneralTax() {
        return generalTax;
    }

    public void setGeneralTax(BigDecimal generalTax) {
        this.generalTax = generalTax;
    }

    public BigDecimal getGeneralTaxCollection() {
        return generalTaxCollection;
    }

    public void setGeneralTaxCollection(BigDecimal generalTaxCollection) {
        this.generalTaxCollection = generalTaxCollection;
    }

    public BigDecimal getVacantLandTax() {
        return vacantLandTax;
    }

    public void setVacantLandTax(BigDecimal vacantLandTax) {
        this.vacantLandTax = vacantLandTax;
    }

    public BigDecimal getVacantLandTaxCollection() {
        return vacantLandTaxCollection;
    }

    public void setVacantLandTaxCollection(BigDecimal vacantLandTaxCollection) {
        this.vacantLandTaxCollection = vacantLandTaxCollection;
    }

    public BigDecimal getLibraryCess() {
        return libraryCess;
    }

    public void setLibraryCess(BigDecimal libraryCess) {
        this.libraryCess = libraryCess;
    }

    public BigDecimal getLibraryCessCollection() {
        return libraryCessCollection;
    }

    public void setLibraryCessCollection(BigDecimal libraryCessCollection) {
        this.libraryCessCollection = libraryCessCollection;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }

    public BigDecimal getPenaltyCollection() {
        return penaltyCollection;
    }

    public void setPenaltyCollection(BigDecimal penaltyCollection) {
        this.penaltyCollection = penaltyCollection;
    }

    public BigDecimal getAdvance() {
        return advance;
    }

    public void setAdvance(BigDecimal advance) {
        this.advance = advance;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

}
