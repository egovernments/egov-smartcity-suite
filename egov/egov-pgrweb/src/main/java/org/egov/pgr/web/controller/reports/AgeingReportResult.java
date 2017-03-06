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
    private BigInteger grtthn30 = BigInteger.ZERO;
    private BigInteger btw10to30 = BigInteger.ZERO;
    private BigInteger btw5to10 = BigInteger.ZERO;
    private BigInteger btw2to5 = BigInteger.ZERO;
    private BigInteger lsthn2 = BigInteger.ZERO;
    private String name;

    public BigInteger getGrtthn30() {
        return grtthn30;
    }

    public void setGrtthn30(BigInteger grtthn30) {
        this.grtthn30 = grtthn30;
    }

    public BigInteger getBtw10to30() {
        return btw10to30;
    }

    public void setBtw10to30(BigInteger btw10to30) {
        this.btw10to30 = btw10to30;
    }

    public BigInteger getBtw5to10() {
        return btw5to10;
    }

    public void setBtw5to10(BigInteger btw5to10) {
        this.btw5to10 = btw5to10;
    }

    public BigInteger getBtw2to5() {
        return btw2to5;
    }

    public void setBtw2to5(BigInteger btw2to5) {
        this.btw2to5 = btw2to5;
    }

    public BigInteger getLsthn2() {
        return lsthn2;
    }

    public void setLsthn2(BigInteger lsthn2) {
        this.lsthn2 = lsthn2;
    }

    public BigInteger getTotal() {
        return grtthn30.add(btw10to30).add(btw5to10).add(btw2to5).add(lsthn2);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

}
