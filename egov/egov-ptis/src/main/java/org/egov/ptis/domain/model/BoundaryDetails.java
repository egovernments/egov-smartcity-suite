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
package org.egov.ptis.domain.model;

import java.io.Serializable;

/**
 * The BoundaryDetails class is used to contain boundary details such as zone details, ward details, block details, locality and
 * street details.
 * 
 * @author ranjit
 *
 */
@SuppressWarnings("serial")
public class BoundaryDetails implements Serializable {

    private Long zoneId;
    private Long zoneNumber;
    private String zoneName;
    private String zoneBoundaryType;
    private Long wardId;
    private Long wardNumber;
    private String wardName;
    private String wardBoundaryType;
    private Long blockId;
    private Long blockNumber;
    private String blockName;
    private Long localityId;
    private String localityName;
    private Long streetId;
    private String streetName;
    private Long adminWardId;
    private Long adminWardNumber;
    private String adminWardName;
    private String adminWardBoundaryType;

    public Long getZoneNumber() {
        return zoneNumber;
    }

    public void setZoneNumber(Long zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneBoundaryType() {
        return zoneBoundaryType;
    }

    public void setZoneBoundaryType(String zoneBoundaryType) {
        this.zoneBoundaryType = zoneBoundaryType;
    }

    public Long getWardNumber() {
        return wardNumber;
    }

    public void setWardNumber(Long wardNumber) {
        this.wardNumber = wardNumber;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getWardBoundaryType() {
        return wardBoundaryType;
    }

    public void setWardBoundaryType(String wardBoundaryType) {
        this.wardBoundaryType = wardBoundaryType;
    }

    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public Long getWardId() {
        return wardId;
    }

    public void setWardId(Long wardId) {
        this.wardId = wardId;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public Long getLocalityId() {
        return localityId;
    }

    public void setLocalityId(Long localityId) {
        this.localityId = localityId;
    }

    public Long getStreetId() {
        return streetId;
    }

    public void setStreetId(Long streetId) {
        this.streetId = streetId;
    }

    public Long getAdminWardId() {
        return adminWardId;
    }

    public void setAdminWardId(Long adminWardId) {
        this.adminWardId = adminWardId;
    }

    public Long getAdminWardNumber() {
        return adminWardNumber;
    }

    public void setAdminWardNumber(Long adminWardNumber) {
        this.adminWardNumber = adminWardNumber;
    }

    public String getAdminWardName() {
        return adminWardName;
    }

    public void setAdminWardName(String adminWardName) {
        this.adminWardName = adminWardName;
    }

    public String getAdminWardBoundaryType() {
        return adminWardBoundaryType;
    }

    public void setAdminWardBoundaryType(String adminWardBoundaryType) {
        this.adminWardBoundaryType = adminWardBoundaryType;
    }

    @Override
    public String toString() {
        return "BoundaryDetails [zoneNumber=" + zoneNumber + ", zoneName=" + zoneName + ", zoneBoundaryType="
                + zoneBoundaryType + ", wardNumber=" + wardNumber + ", wardName=" + wardName + ", wardBoundaryType="
                + wardBoundaryType + ", blockNumber=" + blockNumber + ", blockName=" + blockName + ", localityName="
                + localityName + ", streetName=" + streetName + "]";
    }
}