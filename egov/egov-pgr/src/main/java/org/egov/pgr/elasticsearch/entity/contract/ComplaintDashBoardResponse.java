/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
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

package org.egov.pgr.elasticsearch.entity.contract;

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
    private long ageingGroup5;
    private long ageingGroup6;
    private long ageingGroup7;
    private long ageingGroup8;
    private long reOpenedComplaintCount;
    private long functionaryCount;

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(final String districtName) {
        this.districtName = districtName;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(final String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(final String ulbName) {
        this.ulbName = ulbName;
    }

    public String getUlbGrade() {
        return ulbGrade;
    }

    public void setUlbGrade(final String ulbGrade) {
        this.ulbGrade = ulbGrade;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(final String wardName) {
        this.wardName = wardName;
    }

    public String getDomainURL() {
        return domainURL;
    }

    public void setDomainURL(final String domainURL) {
        this.domainURL = domainURL;
    }

    public String getFunctionaryName() {
        return functionaryName;
    }

    public void setFunctionaryName(final String functionaryName) {
        this.functionaryName = functionaryName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(final String departmentName) {
        this.departmentName = departmentName;
    }

    public String getComplaintTypeName() {
        return ComplaintTypeName;
    }

    public void setComplaintTypeName(final String complaintTypeName) {
        ComplaintTypeName = complaintTypeName;
    }

    public long getTotalComplaintCount() {
        return TotalComplaintCount;
    }

    public void setTotalComplaintCount(final long totalComplaintCount) {
        TotalComplaintCount = totalComplaintCount;
    }

    public long getOpenComplaintCount() {
        return OpenComplaintCount;
    }

    public void setOpenComplaintCount(final long openComplaintCount) {
        OpenComplaintCount = openComplaintCount;
    }

    public long getClosedComplaintCount() {
        return ClosedComplaintCount;
    }

    public void setClosedComplaintCount(final long closedComplaintCount) {
        ClosedComplaintCount = closedComplaintCount;
    }

    public long getOpenWithinSLACount() {
        return OpenWithinSLACount;
    }

    public void setOpenWithinSLACount(final long openWithinSLACount) {
        OpenWithinSLACount = openWithinSLACount;
    }

    public long getOpenOutSideSLACount() {
        return OpenOutSideSLACount;
    }

    public void setOpenOutSideSLACount(final long openOutSideSLACount) {
        OpenOutSideSLACount = openOutSideSLACount;
    }

    public long getClosedWithinSLACount() {
        return ClosedWithinSLACount;
    }

    public void setClosedWithinSLACount(final long closedWithinSLACount) {
        ClosedWithinSLACount = closedWithinSLACount;
    }

    public long getClosedOutSideSLACount() {
        return ClosedOutSideSLACount;
    }

    public void setClosedOutSideSLACount(final long closedOutSideSLACount) {
        ClosedOutSideSLACount = closedOutSideSLACount;
    }

    public double getAvgSatisfactionIndex() {
        return AvgSatisfactionIndex;
    }

    public void setAvgSatisfactionIndex(final double avgSatisfactionIndex) {
        AvgSatisfactionIndex = avgSatisfactionIndex;
    }

    public long getAgeingGroup1() {
        return AgeingGroup1;
    }

    public void setAgeingGroup1(final long ageingGroup1) {
        AgeingGroup1 = ageingGroup1;
    }

    public long getAgeingGroup2() {
        return AgeingGroup2;
    }

    public void setAgeingGroup2(final long ageingGroup2) {
        AgeingGroup2 = ageingGroup2;
    }

    public long getAgeingGroup3() {
        return AgeingGroup3;
    }

    public void setAgeingGroup3(final long ageingGroup3) {
        AgeingGroup3 = ageingGroup3;
    }

    public long getAgeingGroup4() {
        return AgeingGroup4;
    }

    public void setAgeingGroup4(final long ageingGroup4) {
        AgeingGroup4 = ageingGroup4;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(final String localityName) {
        this.localityName = localityName;
    }

    public String getFunctionaryMobileNumber() {
        return functionaryMobileNumber;
    }

    public void setFunctionaryMobileNumber(final String functionaryMobileNumber) {
        this.functionaryMobileNumber = functionaryMobileNumber;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(final String regionName) {
        this.regionName = regionName;
    }

    public long getReOpenedComplaintCount() {
        return reOpenedComplaintCount;
    }

    public void setReOpenedComplaintCount(final long reOpenedComplaintCount) {
        this.reOpenedComplaintCount = reOpenedComplaintCount;
    }

    public long getAgeingGroup5() {
        return ageingGroup5;
    }

    public void setAgeingGroup5(long ageingGroup5) {
        this.ageingGroup5 = ageingGroup5;
    }

    public long getAgeingGroup6() {
        return ageingGroup6;
    }

    public void setAgeingGroup6(long ageingGroup6) {
        this.ageingGroup6 = ageingGroup6;
    }

    public long getAgeingGroup7() {
        return ageingGroup7;
    }

    public void setAgeingGroup7(long ageingGroup7) {
        this.ageingGroup7 = ageingGroup7;
    }

    public long getAgeingGroup8() {
        return ageingGroup8;
    }

    public void setAgeingGroup8(long ageingGroup8) {
        this.ageingGroup8 = ageingGroup8;
    }

    public long getFunctionaryCount() {
        return functionaryCount;
    }

    public void setFunctionaryCount(long functionaryCount) {
        this.functionaryCount = functionaryCount;
    }
}
