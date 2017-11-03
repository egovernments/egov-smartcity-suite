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

public class DemandDetail implements Serializable {

    private Long id;
    private String installment;
    private String reasonMaster;
    private String reasonMasterDesc;
    private BigDecimal actualAmount;
    private BigDecimal revisedAmount;
    private BigDecimal actualCollection;
    private BigDecimal revisedCollection;

    public DemandDetail() {
    }

    public String getReasonMasterDesc() {
        return reasonMasterDesc;
    }

    public void setReasonMasterDesc(final String reasonMasterDesc) {
        this.reasonMasterDesc = reasonMasterDesc;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(final String installment) {
        this.installment = installment;
    }

    public String getReasonMaster() {
        return reasonMaster;
    }

    public void setReasonMaster(final String reasonMaster) {
        this.reasonMaster = reasonMaster;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(final BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getRevisedAmount() {
        return revisedAmount;
    }

    public void setRevisedAmount(final BigDecimal revisedAmount) {
        this.revisedAmount = revisedAmount;
    }

    public BigDecimal getActualCollection() {
        return actualCollection;
    }

    public void setActualCollection(final BigDecimal actualCollection) {
        this.actualCollection = actualCollection;
    }

    public BigDecimal getRevisedCollection() {
        return revisedCollection;
    }

    public void setRevisedCollection(final BigDecimal revisedCollection) {
        this.revisedCollection = revisedCollection;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

}
