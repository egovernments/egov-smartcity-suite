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

package org.egov.tl.entity.view;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Immutable
@Table(name = "egtl_dcb_aggr_view")
public class InstallmentWiseDCB {

    private String licensenumber;
    @Id
    private Long licenseid;
    private String licaddress;
    private String username;
    private Long wardid;
    private Long locality;

    @Column(name = "curr_demand")
    private BigInteger currentdemand;

    @Column(name = "curr_coll")
    private BigInteger currentcollection;

    @Column(name = "curr_balance")
    private BigInteger currentbalance;

    @Transient
    private BigInteger arreardemand;
    @Transient
    private BigInteger arrearcollection;
    @Transient
    private BigInteger arrearbalance;

    @Type(type = "true_false")
    private Boolean active;

    private Date installment;

    public InstallmentWiseDCB(long licenseid, String licensenumber, Boolean active, BigInteger currentdemand,
                              BigInteger currentcollection, BigInteger currentbalance, BigInteger arreardemand,
                              BigInteger arrearcollection, BigInteger arrearbalance) {
        this.licenseid = licenseid;
        this.licensenumber = licensenumber;
        this.active = active;
        this.currentdemand = currentdemand;
        this.currentcollection = currentcollection;
        this.currentbalance = currentbalance;
        this.arreardemand = arreardemand;
        this.arrearcollection = arrearcollection;
        this.arrearbalance = arrearbalance;

    }

    public InstallmentWiseDCB(Long count) {

    }

    public InstallmentWiseDCB() {

    }

    public String getLicensenumber() {
        return licensenumber;
    }

    public void setLicensenumber(String licensenumber) {
        this.licensenumber = licensenumber;
    }

    public Long getLicenseid() {
        return licenseid;
    }

    public void setLicenseid(Long licenseid) {
        this.licenseid = licenseid;
    }

    public String getLicaddress() {
        return licaddress;
    }

    public void setLicaddress(String licaddress) {
        this.licaddress = licaddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getWardid() {
        return wardid;
    }

    public void setWardid(Long wardid) {
        this.wardid = wardid;
    }

    public Long getLocality() {
        return locality;
    }

    public void setLocality(Long locality) {
        this.locality = locality;
    }

    public BigInteger getCurrentdemand() {
        return currentdemand;
    }

    public void setCurrentdemand(BigInteger currentdemand) {
        this.currentdemand = currentdemand;
    }

    public BigInteger getCurrentcollection() {
        return currentcollection;
    }

    public void setCurrentcollection(BigInteger currentcollection) {
        this.currentcollection = currentcollection;
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

    public BigInteger getArrearcollection() {
        return arrearcollection;
    }

    public void setArrearcollection(BigInteger arrearcollection) {
        this.arrearcollection = arrearcollection;
    }

    public BigInteger getArrearbalance() {
        return arrearbalance;
    }

    public void setArrearbalance(BigInteger arrearbalance) {
        this.arrearbalance = arrearbalance;
    }

    public Date getInstallment() {
        return installment;
    }

    public void setInstallment(Date installment) {
        this.installment = installment;
    }

    public BigInteger getTotaldemand() {
        return (currentdemand == null ? BigInteger.ZERO : currentdemand).add(arreardemand == null ? BigInteger.ZERO
                : arreardemand);
    }

    public BigInteger getTotalcollection() {
        return (currentcollection == null ? BigInteger.ZERO : currentcollection)
                .add(arrearcollection == null ? BigInteger.ZERO : arrearcollection);
    }

    public BigInteger getTotalbalance() {
        return (currentbalance == null ? BigInteger.ZERO : currentbalance).add(arrearbalance == null ? BigInteger.ZERO
                : arrearbalance);
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }


}