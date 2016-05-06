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

package org.egov.pgr.web.controller.reports;

import java.math.BigInteger;

public class AgeingReportResult {
    private String zoneName;
    private BigInteger grtthn90 = BigInteger.ZERO;
    private BigInteger btw45to90 = BigInteger.ZERO;
    private BigInteger btw15to45 = BigInteger.ZERO;
    private BigInteger lsthn15 = BigInteger.ZERO;
    private String complainttype;
    private String name;

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(final String zoneName) {
        this.zoneName = zoneName;
    }

    public BigInteger getGrtthn90() {
        return grtthn90;
    }

    public void setGrtthn90(final BigInteger grtthn90) {
        this.grtthn90 = grtthn90;
    }

    public BigInteger getBtw45to90() {
        return btw45to90;
    }

    public void setBtw45to90(final BigInteger btw45to90) {
        this.btw45to90 = btw45to90;
    }

    public BigInteger getBtw15to45() {
        return btw15to45;
    }

    public void setBtw15to45(final BigInteger btw15to45) {
        this.btw15to45 = btw15to45;
    }

    public BigInteger getLsthn15() {
        return lsthn15;
    }

    public void setLsthn15(final BigInteger lsthn15) {
        this.lsthn15 = lsthn15;
    }

    public String getComplainttype() {
        return complainttype;
    }

    public void setComplainttype(final String complainttype) {
        this.complainttype = complainttype;
    }

    public BigInteger getTotal() {
        return grtthn90.add(btw45to90).add(btw15to45).add(lsthn15);
    }

    public void setTotal(final BigInteger total) {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

}
