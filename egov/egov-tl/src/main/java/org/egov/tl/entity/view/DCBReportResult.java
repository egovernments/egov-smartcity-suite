/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 * 	Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.tl.entity.view;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Immutable
@Table(name = "EGTL_MV_DCB_VIEW")
public class DCBReportResult implements Serializable {

    private static final long serialVersionUID = 1188596286215178643L;

    private String licensenumber;

    @Id
    private Integer licenseid;

    private String username;

    @Column(name = "curr_demand")
    private BigInteger currentdemand;

    @Column(name = "arr_demand")
    private BigInteger arreardemand;

    @Column(name = "curr_coll")
    private BigInteger currentcollection;

    @Column(name = "arr_coll")
    private BigInteger arrearcollection;

    @Column(name = "curr_balance")
    private BigInteger currentbalance;

    @Column(name = "arr_balance")
    private BigInteger arrearbalance;

    @Type(type = "true_false")
    private Boolean active;

    private String licaddress;

    private Long wardid;

    private String wardname;

    private Long locality;

    public String getLicensenumber() {
        return licensenumber;
    }

    public void setLicensenumber(final String licensenumber) {
        this.licensenumber = licensenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public BigInteger getTotaldemand() {
        return (currentdemand == null ? BigInteger.ZERO : currentdemand).add(arreardemand == null ? BigInteger.ZERO
                : arreardemand);
    }

    public void setTotaldemand(final BigInteger totaldemand) {
    }

    public BigInteger getTotalcollection() {
        return (currentcollection == null ? BigInteger.ZERO : currentcollection)
                .add(arrearcollection == null ? BigInteger.ZERO : arrearcollection);
    }

    public void setTotalcollection(final BigInteger totalcollection) {
    }

    public BigInteger getCurrentbalance() {
        return (currentdemand == null ? BigInteger.ZERO : currentdemand).subtract(currentcollection == null ? BigInteger.ZERO
                : currentcollection);
    }

    public void setCurrentbalance(final BigInteger currentbalance) {
        this.currentbalance = currentbalance;
    }

    public BigInteger getArrearbalance() {
        return (arreardemand == null ? BigInteger.ZERO : arreardemand).subtract(arrearcollection == null ? BigInteger.ZERO
                : arrearcollection);
    }

    public void setArrearbalance(final BigInteger arrearbalance) {
        this.arrearbalance = arrearbalance;
    }

    public BigInteger getTotalbalance() {
        return (currentbalance == null ? BigInteger.ZERO : currentbalance).add(arrearbalance == null ? BigInteger.ZERO
                : arrearbalance);
    }

    public void setTotalbalance(final BigInteger totalbalance) {
    }

    public Integer getLicenseid() {
        return licenseid;
    }

    public void setLicenseid(final Integer licenseid) {
        this.licenseid = licenseid;
    }

    public String getLicaddress() {
        return licaddress;
    }

    public void setLicaddress(final String licaddress) {
        this.licaddress = licaddress;
    }

    public Long getWardid() {
        return wardid;
    }

    public void setWardid(final Long wardid) {
        this.wardid = wardid;
    }

    public String getWardname() {
        return wardname;
    }

    public void setWardname(String wardname) {
        this.wardname = wardname;
    }

    public Long getLocality() {
        return locality;
    }

    public void setLocality(final Long locality) {
        this.locality = locality;
    }

    public BigInteger getCurrentdemand() {
        return currentdemand;
    }

    public void setCurrentdemand(final BigInteger currentdemand) {
        this.currentdemand = currentdemand;
    }

    public BigInteger getArreardemand() {
        return arreardemand;
    }

    public void setArreardemand(final BigInteger arreardemand) {
        this.arreardemand = arreardemand;
    }

    public BigInteger getCurrentcollection() {
        return currentcollection;
    }

    public void setCurrentcollection(final BigInteger currentcollection) {
        this.currentcollection = currentcollection;
    }

    public BigInteger getArrearcollection() {
        return arrearcollection;
    }

    public void setArrearcollection(final BigInteger arrearcollection) {
        this.arrearcollection = arrearcollection;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}