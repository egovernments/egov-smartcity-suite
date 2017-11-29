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
package org.egov.wtms.reports.entity;

import java.math.BigInteger;

public class WaterConnectionReportResult {
    private String zoneName;
    private BigInteger newconnection = BigInteger.ZERO;
    private BigInteger addconnection = BigInteger.ZERO;
    private BigInteger changeofusage = BigInteger.ZERO;
    private BigInteger closeconnection = BigInteger.ZERO;
    private BigInteger reconnection = BigInteger.ZERO;
    private String name;
    private String ward;
    private String block;
    private String locality;
    private String boundaryname;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getBoundaryname() {
        return boundaryname;
    }

    public void setBoundaryname(final String boundaryname) {
        this.boundaryname = boundaryname;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(final String zoneName) {
        this.zoneName = zoneName;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(final String ward) {
        this.ward = ward;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(final String block) {
        this.block = block;
    }

    public BigInteger getNewconnection() {
        return newconnection;
    }

    public void setNewconnection(final BigInteger newconnection) {
        this.newconnection = newconnection;
    }

    public BigInteger getAddconnection() {
        return addconnection;
    }

    public void setAddconnection(final BigInteger addconnection) {
        this.addconnection = addconnection;
    }

    public BigInteger getChangeofusage() {
        return changeofusage;
    }

    public void setChangeofusage(final BigInteger changeofusage) {
        this.changeofusage = changeofusage;
    }

    public BigInteger getCloseconnection() {
        return closeconnection;
    }

    public void setCloseconnection(final BigInteger closeconnection) {
        this.closeconnection = closeconnection;
    }

    public BigInteger getReconnection() {
        return reconnection;
    }

    public void setReconnection(final BigInteger reconnection) {
        this.reconnection = reconnection;
    }
    public BigInteger getTotal() {
        return newconnection.add(addconnection).add(changeofusage).add(closeconnection).add(reconnection);
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

}
