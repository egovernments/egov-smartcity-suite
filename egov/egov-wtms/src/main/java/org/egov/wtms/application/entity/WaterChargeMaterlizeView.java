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
import org.hibernate.annotations.Immutable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "egwtr_mv_dcb_view")
@Immutable
public class WaterChargeMaterlizeView implements Serializable {

    private static final long serialVersionUID = -6146352214041057969L;
    @Id
    private Long connectionDetailsId;
    @SafeHtml
    @Length(max = 64)
    private String propertyId;
    @SafeHtml
    @Length(max = 50)
    private String hscNo;
    @SafeHtml
    private String address;
    @SafeHtml
    @Length(max = 50)
    private String oldHscNo;
    @SafeHtml
    @Length(max = 50)
    private String propertyType;
    @SafeHtml
    @Length(max = 50)
    private String applicationType;
    @SafeHtml
    @Length(max = 50)
    private String usageType;
    @SafeHtml
    @Length(max = 50)
    private String categoryType;
    @SafeHtml
    @Length(max = 25)
    private String pipeSize;
    @SafeHtml
    private String userName;
    @SafeHtml
    @Length(max = 32)
    private String houseNo;
    @SafeHtml
    @Length(max = 100)
    private String waterSource;
    @SafeHtml
    @Length(max = 20)
    private String connectionType;
    @SafeHtml
    @Length(max = 20)
    private String connectionStatus;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "demand")
    private EgDemand demand;

    private Long wardId;

    private Long zoneId;

    private Long street;
    private Long block;

    private Long locality;

    @Column(name = "curr_demand")
    private double currentDemand;
    @Column(name = "curr_coll")
    private double currentColl;
    @Column(name = "curr_balance")
    private double currentBalance;
    @Column(name = "arr_demand")
    private double arrearDemand;
    @Column(name = "arr_coll")
    private double arrearColl;
    @Column(name = "arr_balance")
    private double arrearBalance;

    @OneToMany(mappedBy = "waterMatView", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InstDmdCollResponse> instDmdColl = new ArrayList<>(0);

   
	public Long getConnectionDetailsId() {
		return connectionDetailsId;
	}

	public void setConnectionDetailsId(final Long connectionDetailsId) {
		this.connectionDetailsId = connectionDetailsId;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(final String propertyId) {
		this.propertyId = propertyId;
	}

	public String getHscNo() {
		return hscNo;
	}

	public void setHscNo(final String hscNo) {
		this.hscNo = hscNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public String getOldHscNo() {
		return oldHscNo;
	}

	public void setOldHscNo(final String oldHscNo) {
		this.oldHscNo = oldHscNo;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(final String propertyType) {
		this.propertyType = propertyType;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(final String applicationType) {
		this.applicationType = applicationType;
	}

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(final String usageType) {
		this.usageType = usageType;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(final String categoryType) {
		this.categoryType = categoryType;
	}

	public String getPipeSize() {
		return pipeSize;
	}

	public void setPipeSize(final String pipeSize) {
		this.pipeSize = pipeSize;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(final String houseNo) {
		this.houseNo = houseNo;
	}

	public String getWaterSource() {
		return waterSource;
	}

	public void setWaterSource(final String waterSource) {
		this.waterSource = waterSource;
	}

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(final String connectionType) {
		this.connectionType = connectionType;
	}

	public String getConnectionStatus() {
		return connectionStatus;
	}

	public void setConnectionStatus(final String connectionStatus) {
		this.connectionStatus = connectionStatus;
	}

	public EgDemand getDemand() {
		return demand;
	}

	public void setDemand(final EgDemand demand) {
		this.demand = demand;
	}

	public Long getWardId() {
		return wardId;
	}

	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public Long getStreet() {
		return street;
	}

	public void setStreet(Long street) {
		this.street = street;
	}

	public Long getBlock() {
		return block;
	}

	public void setBlock(Long block) {
		this.block = block;
	}

	public Long getLocality() {
		return locality;
	}

	public void setLocality(Long locality) {
		this.locality = locality;
	}

	public double getCurrentDemand() {
		return currentDemand;
	}

	public void setCurrentDemand(final double currentDemand) {
		this.currentDemand = currentDemand;
	}

	public double getCurrentColl() {
		return currentColl;
	}

	public void setCurrentColl(final double currentColl) {
		this.currentColl = currentColl;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(final double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public double getArrearDemand() {
		return arrearDemand;
	}

	public void setArrearDemand(final double arrearDemand) {
		this.arrearDemand = arrearDemand;
	}

	public double getArrearColl() {
		return arrearColl;
	}

	public void setArrearColl(final double arrearColl) {
		this.arrearColl = arrearColl;
	}

	public double getArrearBalance() {
		return arrearBalance;
	}

	public void setArrearBalance(final double arrearBalance) {
		this.arrearBalance = arrearBalance;
	}

	 public List<InstDmdCollResponse> getInstDmdColl() {
	        return instDmdColl;
	 }

	    public void setInstDmdColl(final List<InstDmdCollResponse> instDmdColl) {
	        this.instDmdColl = instDmdColl;
	 }
    

}
