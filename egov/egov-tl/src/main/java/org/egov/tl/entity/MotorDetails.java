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

import org.egov.infra.persistence.validator.annotation.Required;

import java.math.BigDecimal;

/**
 * TradelicInstalledMotor entity.
 */
public class MotorDetails {
    private Long id;
    @Required(message = "tradelicense.error.trader.tradedetails")
    private License license;
    @Required(message = "tradelicense.error.trader.motorhorsepower")
    private BigDecimal hp;
    private Long noOfMachines;
    private boolean history;

    public License getLicense() {
        return license;
    }

    public void setLicense(final License license) {
        this.license = license;
    }

    public MotorDetails() {
    }

    public boolean isHistory() {
        return history;
    }

    public void setHistory(final boolean history) {
        this.history = history;
    }

    public BigDecimal getHp() {
        return hp;
    }

    public void setHp(final BigDecimal hp) {
        this.hp = hp;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getNoOfMachines() {
        return noOfMachines;
    }

    public void setNoOfMachines(final Long noOfMachines) {
        this.noOfMachines = noOfMachines;
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("MotorDetails={");
        str.append("  id=").append(id);
        str.append("  hp=").append(hp == null ? "null" : hp.toString());
        str.append("  noOfMachines=").append(noOfMachines == null ? "null" : noOfMachines.toString());
        str.append("  history=").append(history);
        str.append("}");
        return str.toString();

    }
}
