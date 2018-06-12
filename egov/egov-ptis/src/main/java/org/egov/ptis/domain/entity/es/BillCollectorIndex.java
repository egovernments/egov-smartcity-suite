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

package org.egov.ptis.domain.entity.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "billcollector", type = "billcollectordetails")
public class BillCollectorIndex {

    @Id
    private String id;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityGrade;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String districtName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityCode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String regionName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueWard;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityWard;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String billCollector;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String billCollectorMobileNo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String billCollectorCode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueInspector;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueInspectorMobileNo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueInspectorCode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueOfficer;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueOfficerMobileNo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueOfficerCode;

    public String getCityGrade() {
        return cityGrade;
    }

    public void setCityGrade(String cityGrade) {
        this.cityGrade = cityGrade;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getCityWard() {
		return cityWard;
	}

	public void setCityWard(String cityWard) {
		this.cityWard = cityWard;
	}

	public String getBillCollector() {
        return billCollector;
    }

    public void setBillCollector(String billCollector) {
        this.billCollector = billCollector;
    }

    public String getBillCollectorMobileNo() {
        return billCollectorMobileNo;
    }

    public void setBillCollectorMobileNo(String billCollectorMobileNo) {
        this.billCollectorMobileNo = billCollectorMobileNo;
    }

    public String getBillCollectorCode() {
        return billCollectorCode;
    }

    public void setBillCollectorCode(String billCollectorCode) {
        this.billCollectorCode = billCollectorCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRevenueInspector() {
        return revenueInspector;
    }

    public void setRevenueInspector(String revenueInspector) {
        this.revenueInspector = revenueInspector;
    }

    public String getRevenueInspectorMobileNo() {
        return revenueInspectorMobileNo;
    }

    public void setRevenueInspectorMobileNo(String revenueInspectorMobileNo) {
        this.revenueInspectorMobileNo = revenueInspectorMobileNo;
    }

    public String getRevenueInspectorCode() {
        return revenueInspectorCode;
    }

    public void setRevenueInspectorCode(String revenueInspectorCode) {
        this.revenueInspectorCode = revenueInspectorCode;
    }

    public String getRevenueOfficer() {
        return revenueOfficer;
    }

    public void setRevenueOfficer(String revenueOfficer) {
        this.revenueOfficer = revenueOfficer;
    }

    public String getRevenueOfficerMobileNo() {
        return revenueOfficerMobileNo;
    }

    public void setRevenueOfficerMobileNo(String revenueOfficerMobileNo) {
        this.revenueOfficerMobileNo = revenueOfficerMobileNo;
    }

    public String getRevenueOfficerCode() {
        return revenueOfficerCode;
    }

    public void setRevenueOfficerCode(String revenueOfficerCode) {
        this.revenueOfficerCode = revenueOfficerCode;
    }
}
