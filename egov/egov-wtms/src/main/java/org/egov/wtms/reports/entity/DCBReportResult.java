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
import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;

public class DCBReportResult {

    private List<Boundary> zones;
    private List<Boundary> wards;
    private List<Boundary> blocks;
    private List<Boundary> localities;

    private String boundaryName;
    private String boundaryId;
    private String propertyId;
    private String address;
    private String mode;
    private String selectedModeBndry;
    private String reportType;
    private String hscNo;
    private String userName;
    private Integer zoneId;
    private Integer wardId;
    private Integer block;
    private Integer locality;
    private Integer street;

    private String connectionType;
    private BigInteger currDemand = BigInteger.ZERO;
    private BigInteger arrDemand = BigInteger.ZERO;
    private BigInteger currColl = BigInteger.ZERO;
    private BigInteger arrColl = BigInteger.ZERO;
    private BigInteger currBalance = BigInteger.ZERO;
    private BigInteger arrBalance = BigInteger.ZERO;

    private BigInteger countOfConsumerNo = BigInteger.ZERO;

    public Integer getId() {
        if (zoneId == null) {
            if (wardId == null) {
                if (block == null) {
                    if (locality == null)
                        return null;
                    else
                        return locality;
                } else
                    return block;
            } else
                return wardId;
        } else
            return zoneId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public String getSelectedModeBndry() {
        return selectedModeBndry;
    }

    public void setSelectedModeBndry(final String selectedModeBndry) {
        this.selectedModeBndry = selectedModeBndry;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(final String reportType) {
        this.reportType = reportType;
    }

    public List<Boundary> getZones() {
        return zones;
    }

    public void setZones(final List<Boundary> zones) {
        this.zones = zones;
    }

    public String getBoundaryName() {
        return boundaryName;
    }

    public void setBoundaryName(final String boundaryName) {
        this.boundaryName = boundaryName;
    }

    public String getBoundaryId() {
        return boundaryId;
    }

    public void setBoundaryId(final String boundaryId) {
        this.boundaryId = boundaryId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public Integer getBlock() {
        return block;
    }

    public void setBlock(final Integer block) {
        this.block = block;
    }

    public Integer getLocality() {
        return locality;
    }

    public void setLocality(final Integer locality) {
        this.locality = locality;
    }

    public Integer getStreet() {
        return street;
    }

    public void setStreet(final Integer street) {
        this.street = street;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(final String connectionType) {
        this.connectionType = connectionType;
    }

    public BigInteger getCurrDemand() {
        return currDemand;
    }

    public void setCurrDemand(final BigInteger currDemand) {
        this.currDemand = currDemand;
    }

    public BigInteger getArrDemand() {
        return arrDemand;
    }

    public void setArrDemand(final BigInteger arrDemand) {
        this.arrDemand = arrDemand;
    }

    public BigInteger getCurrColl() {
        return currColl;
    }

    public void setCurrColl(final BigInteger currColl) {
        this.currColl = currColl;
    }

    public BigInteger getArrColl() {
        return arrColl;
    }

    public void setArrColl(final BigInteger arrColl) {
        this.arrColl = arrColl;
    }

    public BigInteger getCurrBalance() {
        return currBalance;
    }

    public void setCurrBalance(final BigInteger currBalance) {
        this.currBalance = currBalance;
    }

    public BigInteger getArrBalance() {
        return arrBalance;
    }

    public void setArrBalance(final BigInteger arrBalance) {
        this.arrBalance = arrBalance;
    }

    public List<Boundary> getWards() {
        return wards;
    }

    public void setWards(final List<Boundary> wards) {
        this.wards = wards;
    }

    public List<Boundary> getBlocks() {
        return blocks;
    }

    public void setBlocks(final List<Boundary> blocks) {
        this.blocks = blocks;
    }

    public BigInteger getTotalDemand() {
        return (currDemand == null ? BigInteger.ZERO : currDemand).add(arrDemand == null ? BigInteger.ZERO
                : arrDemand);
    }

    public BigInteger getTotalColl() {
        return (currColl == null ? BigInteger.ZERO : currColl).add(arrColl == null ? BigInteger.ZERO : arrColl);
    }

    public BigInteger getTotalBalance() {
        return (currBalance == null ? BigInteger.ZERO : currBalance).add(arrBalance == null ? BigInteger.ZERO
                : arrBalance);
    }

    public List<Boundary> getLocalities() {
        return localities;
    }

    public void setLocalities(final List<Boundary> localities) {
        this.localities = localities;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public Integer getZoneId() {
        return zoneId;
    }

    public void setZoneId(final Integer zoneId) {
        this.zoneId = zoneId;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(final Integer wardId) {
        this.wardId = wardId;
    }

    public BigInteger getCountOfConsumerNo() {
        return countOfConsumerNo;
    }

    public void setCountOfConsumerNo(final BigInteger countOfConsumerNo) {
        this.countOfConsumerNo = countOfConsumerNo;
    }

}