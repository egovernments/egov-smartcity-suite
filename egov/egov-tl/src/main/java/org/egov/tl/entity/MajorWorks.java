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

/**
 * Electrical License Major Works entity.
 */
public class MajorWorks {
    private Long id;
    @Required(message = "required")
    private License license;
    private String fireFighterSystem;
    private String detection;
    private String passiveProtection;

    public License getLicense() {
        return license;
    }

    public void setLicense(final License license) {
        this.license = license;
    }

    public MajorWorks() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getFireFighterSystem() {
        return fireFighterSystem;
    }

    public void setFireFighterSystem(final String fireFighterSystem) {
        this.fireFighterSystem = fireFighterSystem;
    }

    public String getDetection() {
        return detection;
    }

    public void setDetection(final String detection) {
        this.detection = detection;
    }

    public String getPassiveProtection() {
        return passiveProtection;
    }

    public void setPassiveProtection(final String passiveProtection) {
        this.passiveProtection = passiveProtection;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("MajorWorks [id=").append(id).append(", license=")
                .append(license).append(", fireFighterSystem=")
                .append(fireFighterSystem).append(", detection=")
                .append(detection).append(", passiveProtection=")
                .append(passiveProtection).append("]");
        return builder.toString();
    }
}
