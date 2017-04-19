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

package org.egov.tl.entity.dto;


import java.math.BigInteger;


public class InstallmentWiseDCBForm {
    private String licensenumber;
    private Integer licenseid;
    private BigInteger currentdemand = BigInteger.ZERO;
    private BigInteger currentcoll = BigInteger.ZERO;
    private BigInteger currentbalance = BigInteger.ZERO;
    private BigInteger arreardemand = BigInteger.ZERO;
    private BigInteger arrearcoll = BigInteger.ZERO;
    private BigInteger arrearbalance = BigInteger.ZERO;

    public String getLicensenumber() {
        return licensenumber;
    }

    public void setLicensenumber(String licensenumber) {
        this.licensenumber = licensenumber;
    }

    public Integer getLicenseid() {
        return licenseid;
    }

    public void setLicenseid(Integer licenseid) {
        this.licenseid = licenseid;
    }

    public BigInteger getCurrentdemand() {
        return currentdemand;
    }

    public void setCurrentdemand(BigInteger currentdemand) {
        this.currentdemand = currentdemand;
    }

    public BigInteger getCurrentcoll() {
        return currentcoll;
    }

    public void setCurrentcoll(BigInteger currentcoll) {
        this.currentcoll = currentcoll;
    }

    public BigInteger getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(BigInteger currentbalance) {
        this.currentbalance = currentbalance;
    }

    public BigInteger getArreardemand() {
        return arreardemand;
    }

    public void setArreardemand(BigInteger arreardemand) {
        this.arreardemand = arreardemand;
    }

    public BigInteger getArrearcoll() {
        return arrearcoll;
    }

    public void setArrearcoll(BigInteger arrearcoll) {
        this.arrearcoll = arrearcoll;
    }

    public BigInteger getArrearbalance() {
        return arrearbalance;
    }

    public void setArrearbalance(BigInteger arrearbalance) {
        this.arrearbalance = arrearbalance;
    }

    public BigInteger getTotaldemand() {
        return (currentdemand == null ? BigInteger.ZERO : currentdemand).add(arreardemand == null ? BigInteger.ZERO
                : arreardemand);
    }

    public BigInteger getTotalcoll() {
        return (currentcoll == null ? BigInteger.ZERO : currentcoll).add(arrearcoll == null ? BigInteger.ZERO : arrearcoll);
    }

    public BigInteger getTotalbalance() {
        return (currentbalance == null ? BigInteger.ZERO : currentbalance).add(arrearbalance == null ? BigInteger.ZERO
                : arrearbalance);
    }

}
