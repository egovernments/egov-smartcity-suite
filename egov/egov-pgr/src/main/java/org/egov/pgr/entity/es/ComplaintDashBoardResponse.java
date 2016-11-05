/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.entity.es;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class ComplaintDashBoardResponse {
    private String regionName;
    private String districtName;
    private String ulbCode;
    private String ulbName;
    private String ulbGrade;
    private String wardName;
    private String domainURL;
    private String functionaryName;
    private String functionaryMobileNumber;
    private String localityName;
    private String departmentName;
    private String ComplaintTypeName;
    private long TotalComplaintCount;
    private long OpenComplaintCount;
    private long ClosedComplaintCount;
    private long OpenWithinSLACount;
    private long OpenOutSideSLACount;
    private long ClosedWithinSLACount;
    private long ClosedOutSideSLACount;
    private double AvgSatisfactionIndex;
    private long AgeingGroup1;
    private long AgeingGroup2;
    private long AgeingGroup3;
    private long AgeingGroup4;

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(String ulbName) {
        this.ulbName = ulbName;
    }

    public String getUlbGrade() {
        return ulbGrade;
    }

    public void setUlbGrade(String ulbGrade) {
        this.ulbGrade = ulbGrade;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getDomainURL() {
        return domainURL;
    }

    public void setDomainURL(String domainURL) {
        this.domainURL = domainURL;
    }

    public String getFunctionaryName() {
        return functionaryName;
    }

    public void setFunctionaryName(String functionaryName) {
        this.functionaryName = functionaryName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getComplaintTypeName() {
        return ComplaintTypeName;
    }

    public void setComplaintTypeName(String complaintTypeName) {
        ComplaintTypeName = complaintTypeName;
    }

    public long getTotalComplaintCount() {
        return TotalComplaintCount;
    }

    public void setTotalComplaintCount(long totalComplaintCount) {
        TotalComplaintCount = totalComplaintCount;
    }

    public long getOpenComplaintCount() {
        return OpenComplaintCount;
    }

    public void setOpenComplaintCount(long openComplaintCount) {
        OpenComplaintCount = openComplaintCount;
    }

    public long getClosedComplaintCount() {
        return ClosedComplaintCount;
    }

    public void setClosedComplaintCount(long closedComplaintCount) {
        ClosedComplaintCount = closedComplaintCount;
    }

    public long getOpenWithinSLACount() {
        return OpenWithinSLACount;
    }

    public void setOpenWithinSLACount(long openWithinSLACount) {
        OpenWithinSLACount = openWithinSLACount;
    }

    public long getOpenOutSideSLACount() {
        return OpenOutSideSLACount;
    }

    public void setOpenOutSideSLACount(long openOutSideSLACount) {
        OpenOutSideSLACount = openOutSideSLACount;
    }

    public long getClosedWithinSLACount() {
        return ClosedWithinSLACount;
    }

    public void setClosedWithinSLACount(long closedWithinSLACount) {
        ClosedWithinSLACount = closedWithinSLACount;
    }

    public long getClosedOutSideSLACount() {
        return ClosedOutSideSLACount;
    }

    public void setClosedOutSideSLACount(long closedOutSideSLACount) {
        ClosedOutSideSLACount = closedOutSideSLACount;
    }

    public double getAvgSatisfactionIndex() {
        return AvgSatisfactionIndex;
    }

    public void setAvgSatisfactionIndex(double avgSatisfactionIndex) {
        AvgSatisfactionIndex = avgSatisfactionIndex;
    }

    public long getAgeingGroup1() {
        return AgeingGroup1;
    }

    public void setAgeingGroup1(long ageingGroup1) {
        AgeingGroup1 = ageingGroup1;
    }

    public long getAgeingGroup2() {
        return AgeingGroup2;
    }

    public void setAgeingGroup2(long ageingGroup2) {
        AgeingGroup2 = ageingGroup2;
    }

    public long getAgeingGroup3() {
        return AgeingGroup3;
    }

    public void setAgeingGroup3(long ageingGroup3) {
        AgeingGroup3 = ageingGroup3;
    }

    public long getAgeingGroup4() {
        return AgeingGroup4;
    }

    public void setAgeingGroup4(long ageingGroup4) {
        AgeingGroup4 = ageingGroup4;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getFunctionaryMobileNumber() {
        return functionaryMobileNumber;
    }

    public void setFunctionaryMobileNumber(String functionaryMobileNumber) {
        this.functionaryMobileNumber = functionaryMobileNumber;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
