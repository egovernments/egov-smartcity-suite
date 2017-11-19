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

package org.egov.ptis.client.model.calculator;

import org.egov.commons.Installment;

import java.math.BigDecimal;

public class DemandNoticeDetailsInfo {

    private Installment installment;
    private String fromDate;
    private String toDate;
    private BigDecimal propertyTax = new BigDecimal(0);
    private BigDecimal penalty = new BigDecimal(0);
    private BigDecimal waterTax = new BigDecimal(0);
    private BigDecimal drinageSewarageTax = new BigDecimal(0);
    private BigDecimal vacantLandTax = new BigDecimal(0);
    private final BigDecimal total = new BigDecimal(0);

    public Installment getInstallment() {
        return installment;
    }

    public void setInstallment(final Installment installment) {
        this.installment = installment;
    }

    public BigDecimal getPropertyTax() {
        if (propertyTax.equals(new BigDecimal(0)))
            return null;
        else
            return propertyTax;
    }

    public void setPropertyTax(final BigDecimal propertyTax) {
        this.propertyTax = propertyTax;
    }

    public BigDecimal getPenalty() {
        if (penalty.equals(new BigDecimal(0)))
            return null;
        else
            return penalty;
    }

    public void setPenalty(final BigDecimal penalty) {
        this.penalty = penalty;
    }

    public BigDecimal getWaterTax() {
        if (waterTax.equals(new BigDecimal(0)))
            return null;
        else
            return waterTax;
    }

    public void setWaterTax(final BigDecimal waterTax) {
        this.waterTax = waterTax;
    }

    public BigDecimal getDrinageSewarageTax() {
        if (drinageSewarageTax.equals(new BigDecimal(0)))
            return null;
        else
            return drinageSewarageTax;
    }

    public void setDrinageSewarageTax(final BigDecimal drinageSewarageTax) {
        this.drinageSewarageTax = drinageSewarageTax;
    }

    public BigDecimal getVacantLandTax() {
        if (vacantLandTax.equals(new BigDecimal(0)))
            return null;
        else
            return vacantLandTax;
    }

    public void setVacantLandTax(final BigDecimal vacantLandTax) {
        this.vacantLandTax = vacantLandTax;
    }

    public BigDecimal getTotal() {
        return addTotal();
    }

    public BigDecimal addTotal() {
        BigDecimal sumAmount = new BigDecimal(0);
        if (propertyTax != null)
            sumAmount = sumAmount.add(propertyTax);
        if (penalty != null)
            sumAmount = sumAmount.add(penalty);
        if (waterTax != null)
            sumAmount = sumAmount.add(waterTax);
        if (vacantLandTax != null)
            sumAmount = sumAmount.add(vacantLandTax);
        if (drinageSewarageTax != null)
            sumAmount = sumAmount.add(drinageSewarageTax);
        return sumAmount;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(final String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(final String toDate) {
        this.toDate = toDate;
    }

}
