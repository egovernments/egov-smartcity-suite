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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.entity.Boundary;

@Entity
@Table(name = "egwtr_mv_dcb_view")
public class WaterChargeMaterlizeView {

    @EmbeddedId
    private WaterChargeViewEmbedded id;

    private String hscno;
    private String address;

    private String oldhscno;
    private String username;
    private String houseno;
    @ManyToOne
    @JoinColumn(name = "wardid")
    private Boundary wardid;
    @ManyToOne
    @JoinColumn(name = "zoneid")
    private Boundary zoneid;
    @ManyToOne
    @JoinColumn(name = "street")
    private Boundary street;
    @ManyToOne
    @JoinColumn(name = "block")
    private Boundary block;
    @ManyToOne
    @JoinColumn(name = "locality")
    private Boundary locality;
    private String mobileno;
    private String aadharno;
    private String propertytype;
    private String applicationtype;
    private String usagetype;
    private String categorytype;
    private String pipesize;
    private String watersource;

    private String connectiontype;
    private String connectionstatus;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "demand")
    private EgDemand demand;
    private String numberofperson;
    private String numberofrooms;
    private String sumpcapacity;
    private Date executiondate;
    private Long donationcharges;
    private Boolean legacy;
    private String approvalnumber;
    @Column(name = "advance_coll")
    private BigDecimal advancecoll;
    @Column(name = "due_period")
    private Long dueperiod;
    @Column(name = "curr_demand")
    private BigDecimal currentdemand;
    @Column(name = "curr_coll")
    private BigDecimal currentcoll;
    @Column(name = "curr_balance")
    private BigDecimal currentbalance;
    @Column(name = "arr_demand")
    private BigDecimal arreardemand;
    @Column(name = "arr_coll")
    private BigDecimal arrearcoll;
    @Column(name = "arr_balance")
    private BigDecimal arrearbalance;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    List<InstDmdCollResponse> instDmdCollResponseList;

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getHscno() {
        return hscno;
    }

    public void setHscno(final String hscno) {
        this.hscno = hscno;
    }

    public String getOldhscno() {
        return oldhscno;
    }

    public void setOldhscno(final String oldhscno) {
        this.oldhscno = oldhscno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getHouseno() {
        return houseno;
    }

    public void setHouseno(final String houseno) {
        this.houseno = houseno;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(final String mobileno) {
        this.mobileno = mobileno;
    }

    public String getAadharno() {
        return aadharno;
    }

    public void setAadharno(final String aadharno) {
        this.aadharno = aadharno;
    }

    public String getPropertytype() {
        return propertytype;
    }

    public void setPropertytype(final String propertytype) {
        this.propertytype = propertytype;
    }

    public String getApplicationtype() {
        return applicationtype;
    }

    public void setApplicationtype(final String applicationtype) {
        this.applicationtype = applicationtype;
    }

    public String getUsagetype() {
        return usagetype;
    }

    public void setUsagetype(final String usagetype) {
        this.usagetype = usagetype;
    }

    public String getCategorytype() {
        return categorytype;
    }

    public void setCategorytype(final String categorytype) {
        this.categorytype = categorytype;
    }

    public String getPipesize() {
        return pipesize;
    }

    public void setPipesize(final String pipesize) {
        this.pipesize = pipesize;
    }

    public String getWatersource() {
        return watersource;
    }

    public void setWatersource(final String watersource) {
        this.watersource = watersource;
    }

    public String getConnectiontype() {
        return connectiontype;
    }

    public void setConnectiontype(final String connectiontype) {
        this.connectiontype = connectiontype;
    }

    public String getConnectionstatus() {
        return connectionstatus;
    }

    public void setConnectionstatus(final String connectionstatus) {
        this.connectionstatus = connectionstatus;
    }

    public String getNumberofperson() {
        return numberofperson;
    }

    public void setNumberofperson(final String numberofperson) {
        this.numberofperson = numberofperson;
    }

    public String getNumberofrooms() {
        return numberofrooms;
    }

    public void setNumberofrooms(final String numberofrooms) {
        this.numberofrooms = numberofrooms;
    }

    public String getSumpcapacity() {
        return sumpcapacity;
    }

    public void setSumpcapacity(final String sumpcapacity) {
        this.sumpcapacity = sumpcapacity;
    }

    public Date getExecutiondate() {
        return executiondate;
    }

    public void setExecutiondate(final Date executiondate) {
        this.executiondate = executiondate;
    }

    public Long getDonationcharges() {
        return donationcharges;
    }

    public void setDonationcharges(final Long donationcharges) {
        this.donationcharges = donationcharges;
    }

    public Boolean getLegacy() {
        return legacy;
    }

    public void setLegacy(final Boolean legacy) {
        this.legacy = legacy;
    }

    public String getApprovalnumber() {
        return approvalnumber;
    }

    public void setApprovalnumber(final String approvalnumber) {
        this.approvalnumber = approvalnumber;
    }

    public List<InstDmdCollResponse> getInstDmdCollResponseList() {
        return instDmdCollResponseList;
    }

    public void setInstDmdCollResponseList(final List<InstDmdCollResponse> instDmdCollResponseList) {
        this.instDmdCollResponseList = instDmdCollResponseList;
    }

    public Boundary getWardid() {
        return wardid;
    }

    public void setWardid(final Boundary wardid) {
        this.wardid = wardid;
    }

    public Boundary getZoneid() {
        return zoneid;
    }

    public void setZoneid(final Boundary zoneid) {
        this.zoneid = zoneid;
    }

    public Boundary getStreet() {
        return street;
    }

    public void setStreet(final Boundary street) {
        this.street = street;
    }

    public Boundary getBlock() {
        return block;
    }

    public void setBlock(final Boundary block) {
        this.block = block;
    }

    public Boundary getLocality() {
        return locality;
    }

    public void setLocality(final Boundary locality) {
        this.locality = locality;
    }

    public WaterChargeViewEmbedded getId() {
        return id;
    }

    public void setId(final WaterChargeViewEmbedded id) {
        this.id = id;
    }

    public EgDemand getDemand() {
        return demand;
    }

    public void setDemand(final EgDemand demand) {
        this.demand = demand;
    }

    public BigDecimal getAdvancecoll() {
        return advancecoll;
    }

    public void setAdvancecoll(final BigDecimal advancecoll) {
        this.advancecoll = advancecoll;
    }

    public Long getDueperiod() {
        return dueperiod;
    }

    public void setDueperiod(final Long dueperiod) {
        this.dueperiod = dueperiod;
    }

    public BigDecimal getCurrentdemand() {
        return currentdemand;
    }

    public void setCurrentdemand(final BigDecimal currentdemand) {
        this.currentdemand = currentdemand;
    }

    public BigDecimal getCurrentcoll() {
        return currentcoll;
    }

    public void setCurrentcoll(final BigDecimal currentcoll) {
        this.currentcoll = currentcoll;
    }

    public BigDecimal getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(final BigDecimal currentbalance) {
        this.currentbalance = currentbalance;
    }

    public BigDecimal getArreardemand() {
        return arreardemand;
    }

    public void setArreardemand(final BigDecimal arreardemand) {
        this.arreardemand = arreardemand;
    }

    public BigDecimal getArrearcoll() {
        return arrearcoll;
    }

    public void setArrearcoll(final BigDecimal arrearcoll) {
        this.arrearcoll = arrearcoll;
    }

    public BigDecimal getArrearbalance() {
        return arrearbalance;
    }

    public void setArrearbalance(final BigDecimal arrearbalance) {
        this.arrearbalance = arrearbalance;
    }

}
