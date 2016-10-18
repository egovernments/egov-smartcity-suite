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

package org.egov.ptis.elasticsearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "propertytax", type = "propertytaxdetails")
public class PropertyTaxIndex {

    @Id
    private String consumercode;

    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed)
    private Double builtuparea;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String citygrade;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityname;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String districtname;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ulbcode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String adminwardname;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String propertytype;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String regionname;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revwardname;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revzonename;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueblock;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String sitalarea;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String propertyusage;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String propertycategory;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String locationname;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String aadhaarnumber;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String mobilenumber;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ptassesmentno;

    /*
     * @GeoPointField private GeoPoint propertygeo;
     * @GeoPointField private GeoPoint boundarygeo;
     */

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String propertyaddress;

    @Field(type = FieldType.String)
    private String billcollector;

    @Field(type = FieldType.Double)
    private Double annualcollection;

    @Field(type = FieldType.Double)
    private Double annualdemand;

    @Field(type = FieldType.Double)
    private Double arrearbalance;

    @Field(type = FieldType.Double)
    private Double arrearcollection;

    @Field(type = FieldType.Double)
    private Double arreardemand;

    @Field(type = FieldType.Double)
    private Double arvamount;

    @Field(type = FieldType.Double)
    private Double firstinstallmentdemand;

    @Field(type = FieldType.Double)
    private Double firstinstallmentcollection;

    @Field(type = FieldType.Double)
    private Double secondinstallmentdemand;

    @Field(type = FieldType.Double)
    private Double secondinstallmentcollection;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String consumername;

    @Field(type = FieldType.Double)
    private Double totalbalance;

    @Field(type = FieldType.Double)
    private Double annualbalance;

    @Field(type = FieldType.Double)
    private Double totaldemand;

    @Field(type = FieldType.Double)
    private Double arrearinterestdemand;

    @Field(type = FieldType.Double)
    private Double arrearinterestcollection;

    @Field(type = FieldType.Double)
    private Double currentinterestdemand;

    @Field(type = FieldType.Double)
    private Double currentinterestcollection;

    @Field(type = FieldType.Double)
    private Double currentyearcoll;

    public Double getBuiltuparea() {
        return builtuparea;
    }

    public void setBuiltuparea(Double builtuparea) {
        this.builtuparea = builtuparea;
    }

    public String getCitygrade() {
        return citygrade;
    }

    public void setCitygrade(String citygrade) {
        this.citygrade = citygrade;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(String districtname) {
        this.districtname = districtname;
    }

    public String getUlbcode() {
        return ulbcode;
    }

    public void setUlbcode(String ulbcode) {
        this.ulbcode = ulbcode;
    }

    public String getAdminwardname() {
        return adminwardname;
    }

    public void setAdminwardname(String adminwardname) {
        this.adminwardname = adminwardname;
    }

    public String getPropertytype() {
        return propertytype;
    }

    public void setPropertytype(String propertytype) {
        this.propertytype = propertytype;
    }

    public String getRegionname() {
        return regionname;
    }

    public void setRegionname(String regionname) {
        this.regionname = regionname;
    }

    public String getRevwardname() {
        return revwardname;
    }

    public void setRevwardname(String revwardname) {
        this.revwardname = revwardname;
    }

    public String getRevzonename() {
        return revzonename;
    }

    public void setRevzonename(String revzonename) {
        this.revzonename = revzonename;
    }

    public String getRevenueblock() {
        return revenueblock;
    }

    public void setRevenueblock(String revenueblock) {
        this.revenueblock = revenueblock;
    }

    public String getSitalarea() {
        return sitalarea;
    }

    public void setSitalarea(String sitalarea) {
        this.sitalarea = sitalarea;
    }

    public String getPropertyusage() {
        return propertyusage;
    }

    public void setPropertyusage(String propertyusage) {
        this.propertyusage = propertyusage;
    }

    public String getPropertycategory() {
        return propertycategory;
    }

    public void setPropertycategory(String propertycategory) {
        this.propertycategory = propertycategory;
    }

    public String getLocationname() {
        return locationname;
    }

    public void setLocationname(String locationname) {
        this.locationname = locationname;
    }

    public String getAadhaarnumber() {
        return aadhaarnumber;
    }

    public void setAadhaarnumber(String aadhaarnumber) {
        this.aadhaarnumber = aadhaarnumber;
    }

    public String getConsumercode() {
        return consumercode;
    }

    public void setConsumercode(String consumercode) {
        this.consumercode = consumercode;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getPtassesmentno() {
        return ptassesmentno;
    }

    public void setPtassesmentno(String ptassesmentno) {
        this.ptassesmentno = ptassesmentno;
    }

    /*
     * public GeoPoint getPropertygeo() { return propertygeo; } public void
     * setPropertygeo(GeoPoint propertygeo) { this.propertygeo = propertygeo; }
     * public GeoPoint getBoundarygeo() { return boundarygeo; } public void
     * setBoundarygeo(GeoPoint boundarygeo) { this.boundarygeo = boundarygeo; }
     */

    public String getPropertyaddress() {
        return propertyaddress;
    }

    public void setPropertyaddress(String propertyaddress) {
        this.propertyaddress = propertyaddress;
    }

    public String getBillcollector() {
        return billcollector;
    }

    public void setBillcollector(String billcollector) {
        this.billcollector = billcollector;
    }

    public Double getAnnualcollection() {
        return annualcollection;
    }

    public void setAnnualcollection(Double annualcollection) {
        this.annualcollection = annualcollection;
    }

    public Double getAnnualdemand() {
        return annualdemand;
    }

    public void setAnnualdemand(Double annualdemand) {
        this.annualdemand = annualdemand;
    }

    public Double getArrearbalance() {
        return arrearbalance;
    }

    public void setArrearbalance(Double arrearbalance) {
        this.arrearbalance = arrearbalance;
    }

    public Double getArrearcollection() {
        return arrearcollection;
    }

    public void setArrearcollection(Double arrearcollection) {
        this.arrearcollection = arrearcollection;
    }

    public Double getArreardemand() {
        return arreardemand;
    }

    public void setArreardemand(Double arreardemand) {
        this.arreardemand = arreardemand;
    }

    public Double getArvamount() {
        return arvamount;
    }

    public void setArvamount(Double arvamount) {
        this.arvamount = arvamount;
    }

    public Double getFirstinstallmentdemand() {
        return firstinstallmentdemand;
    }

    public void setFirstinstallmentdemand(Double firstinstallmentdemand) {
        this.firstinstallmentdemand = firstinstallmentdemand;
    }

    public Double getFirstinstallmentcollection() {
        return firstinstallmentcollection;
    }

    public void setFirstinstallmentcollection(Double firstinstallmentcollection) {
        this.firstinstallmentcollection = firstinstallmentcollection;
    }

    public Double getSecondinstallmentdemand() {
        return secondinstallmentdemand;
    }

    public void setSecondinstallmentdemand(Double secondinstallmentdemand) {
        this.secondinstallmentdemand = secondinstallmentdemand;
    }

    public Double getSecondinstallmentcollection() {
        return secondinstallmentcollection;
    }

    public void setSecondinstallmentcollection(Double secondinstallmentcollection) {
        this.secondinstallmentcollection = secondinstallmentcollection;
    }

    public String getConsumername() {
        return consumername;
    }

    public void setConsumername(String consumername) {
        this.consumername = consumername;
    }

    public Double getTotalbalance() {
        return totalbalance;
    }

    public void setTotalbalance(Double totalbalance) {
        this.totalbalance = totalbalance;
    }

    public Double getAnnualbalance() {
        return annualbalance;
    }

    public void setAnnualbalance(Double annualbalance) {
        this.annualbalance = annualbalance;
    }

    public Double getTotaldemand() {
        return totaldemand;
    }

    public void setTotaldemand(Double totaldemand) {
        this.totaldemand = totaldemand;
    }

    public Double getArrearinterestdemand() {
        return arrearinterestdemand;
    }

    public void setArrearinterestdemand(Double arrearinterestdemand) {
        this.arrearinterestdemand = arrearinterestdemand;
    }

    public Double getArrearinterestcollection() {
        return arrearinterestcollection;
    }

    public void setArrearinterestcollection(Double arrearinterestcollection) {
        this.arrearinterestcollection = arrearinterestcollection;
    }

    public Double getCurrentinterestdemand() {
        return currentinterestdemand;
    }

    public void setCurrentinterestdemand(Double currentinterestdemand) {
        this.currentinterestdemand = currentinterestdemand;
    }

    public Double getCurrentinterestcollection() {
        return currentinterestcollection;
    }

    public void setCurrentinterestcollection(Double currentinterestcollection) {
        this.currentinterestcollection = currentinterestcollection;
    }

    public Double getCurrentyearcoll() {
        return currentyearcoll;
    }

    public void setCurrentyearcoll(Double currentyearcoll) {
        this.currentyearcoll = currentyearcoll;
    }
}