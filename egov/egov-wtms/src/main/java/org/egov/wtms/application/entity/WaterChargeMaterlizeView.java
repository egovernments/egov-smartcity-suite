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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.egov.demand.model.EgDemand;

@Entity
@Table(name = "egwtr_mv_dcb_view")
public class WaterChargeMaterlizeView implements Serializable {

    private static final long serialVersionUID = -6146352214041057969L;
    @Id
    private Long connectiondetailsid;
    private String propertyid;
    private String hscno;
    private String address;
    private String oldhscno;
    private String propertytype;
    private String applicationtype;
    private String usagetype;
    private String categorytype;
    private String pipesize;
    private String username;
    private String houseno;
    private String watersource;
    private String connectiontype;
    private String connectionstatus;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "demand")
    private EgDemand demand;

    @Column(name = "curr_demand")
    private double currentdemand;
    @Column(name = "curr_coll")
    private double currentcoll;
    @Column(name = "curr_balance")
    private double currentbalance;
    @Column(name = "arr_demand")
    private double arreardemand;
    @Column(name = "arr_coll")
    private double arrearcoll;
    @Column(name = "arr_balance")
    private double arrearbalance;

    @OneToMany(mappedBy = "waterMatView", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InstDmdCollResponse> instDmdColl = new ArrayList<InstDmdCollResponse>(0);

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

    public String getPropertyid() {
        return propertyid;
    }

    public void setPropertyid(final String propertyid) {
        this.propertyid = propertyid;
    }

    public EgDemand getDemand() {
        return demand;
    }

    public void setDemand(final EgDemand demand) {
        this.demand = demand;
    }

    public Long getConnectiondetailsid() {
        return connectiondetailsid;
    }

    public void setConnectiondetailsid(final Long connectiondetailsid) {
        this.connectiondetailsid = connectiondetailsid;
    }

    public List<InstDmdCollResponse> getInstDmdColl() {
        return instDmdColl;
    }

    public void setInstDmdColl(final List<InstDmdCollResponse> instDmdColl) {
        this.instDmdColl = instDmdColl;
    }

    public double getCurrentdemand() {
        return currentdemand;
    }

    public void setCurrentdemand(final double currentdemand) {
        this.currentdemand = currentdemand;
    }

    public double getCurrentcoll() {
        return currentcoll;
    }

    public void setCurrentcoll(final double currentcoll) {
        this.currentcoll = currentcoll;
    }

    public double getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(final double currentbalance) {
        this.currentbalance = currentbalance;
    }

    public double getArreardemand() {
        return arreardemand;
    }

    public void setArreardemand(final double arreardemand) {
        this.arreardemand = arreardemand;
    }

    public double getArrearcoll() {
        return arrearcoll;
    }

    public void setArrearcoll(final double arrearcoll) {
        this.arrearcoll = arrearcoll;
    }

    public double getArrearbalance() {
        return arrearbalance;
    }

    public void setArrearbalance(final double arrearbalance) {
        this.arrearbalance = arrearbalance;
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

}
