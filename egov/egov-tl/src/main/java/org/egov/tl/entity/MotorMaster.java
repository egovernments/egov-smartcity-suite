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
package org.egov.tl.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.models.BaseModel;

public class MotorMaster extends BaseModel {
    private static final long serialVersionUID = 1L;
    @Required(message = "masters.master.licensefee.effectivedate")
    private Date effectiveFrom;
    private Date effectiveTo;
    private Long id;
    @Required(message = "masters.erection.motorhpfrom")
    private BigDecimal motorHpFrom;
    @Required(message = "masters.erection.motorhpto")
    private BigDecimal motorHpTo;
    @Required(message = "masters.erection.usingfee")
    private BigDecimal usingFee;

    public Date getEffectiveFrom() {
        return effectiveFrom;
    }

    public Date getEffectiveTo() {
        return effectiveTo;
    }

    @Override
    public Long getId() {
        return id;
    }

    public BigDecimal getMotorHpFrom() {
        return motorHpFrom;
    }

    public BigDecimal getMotorHpTo() {
        return motorHpTo;
    }

    public BigDecimal getUsingFee() {
        return usingFee;
    }

    public void setEffectiveFrom(final Date effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public void setEffectiveTo(final Date effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public void setMotorHpFrom(final BigDecimal motorHpFrom) {
        this.motorHpFrom = motorHpFrom;
    }

    public void setMotorHpTo(final BigDecimal motorHpTo) {
        this.motorHpTo = motorHpTo;
    }

    public void setUsingFee(final BigDecimal usingFee) {
        this.usingFee = usingFee;
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("MotorMaster={ ");
        str.append("serialVersionUID=").append(serialVersionUID);
        str.append("Id=").append(id);
        str.append("effectiveFrom=").append(effectiveFrom == null ? "null" : effectiveFrom.toString());
        str.append("effectiveTo=").append(effectiveTo == null ? "null" : effectiveTo.toString());
        str.append("motorHpFrom=").append(motorHpFrom == null ? "null" : motorHpFrom.toString());
        str.append("motorHpTo=").append(motorHpTo == null ? "null" : motorHpTo.toString());
        str.append("usingFee=").append(usingFee == null ? "null" : usingFee.toString());
        str.append("}");
        return str.toString();
    }
}
