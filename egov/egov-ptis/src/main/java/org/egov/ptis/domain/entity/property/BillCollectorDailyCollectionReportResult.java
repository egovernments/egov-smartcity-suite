/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;
import java.util.Date;

public class BillCollectorDailyCollectionReportResult {

    private String region;
    private String city;
    private String  type;
    
    private Date collectionDate;
    private String district;
    private String ulbName;
    private String ulbCode;
    private String collectorname;
    private String mobilenumber;
    private Double target_arrears_demand = 0.0;
    private Double target_current_demand = 0.0;
    private Double target_total_demand = 0.0;
    private BigDecimal day_target = BigDecimal.ZERO;

    private Double today_arrears_collection = 0.0;
    private Double today_currentyear_collection = 0.0;

    private Double today_total_collection = 0.0;

    private Double cummulative_arrears_collection = 0.0;
    private Double cummulative_currentyear_collection = 0.0;

    private Double cummulative_total_Collection = 0.0;
    private BigDecimal cummulative_currentYear_Percentage = BigDecimal.ZERO;
    private Double Percentage_compareWithLastYear = 0.0;
    private BigDecimal growth = BigDecimal.ZERO;

    private Double lastyear_collection = 0.0;
    private Double lastyear_cummulative_collection = 0.0;

    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(String ulbName) {
        this.ulbName = ulbName;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public String getCollectorname() {
        return collectorname;
    }

    public void setCollectorname(String collectorname) {
        this.collectorname = collectorname;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public Double getTarget_arrears_demand() {
        return target_arrears_demand;
    }

    public void setTarget_arrears_demand(Double target_arrears_demand) {
        this.target_arrears_demand = target_arrears_demand;
    }

    public Double getTarget_current_demand() {
        return target_current_demand;
    }

    public void setTarget_current_demand(Double target_current_demand) {
        this.target_current_demand = target_current_demand;
    }

    public Double getToday_arrears_collection() {
        return today_arrears_collection;
    }

    public void setToday_arrears_collection(Double today_arrears_collection) {
        this.today_arrears_collection = today_arrears_collection;
    }

    public Double getToday_currentyear_collection() {
        return today_currentyear_collection;
    }

    public void setToday_currentyear_collection(Double today_currentyear_collection) {
        this.today_currentyear_collection = today_currentyear_collection;
    }

    public Double getCummulative_arrears_collection() {
        return cummulative_arrears_collection;
    }

    public void setCummulative_arrears_collection(Double cummulative_arrears_collection) {
        this.cummulative_arrears_collection = cummulative_arrears_collection;
    }

    public Double getCummulative_currentyear_collection() {
        return cummulative_currentyear_collection;
    }

    public void setCummulative_currentyear_collection(Double cummulative_currentyear_collection) {
        this.cummulative_currentyear_collection = cummulative_currentyear_collection;
    }

    public Double getLastyear_collection() {
        return lastyear_collection;
    }

    public void setLastyear_collection(Double lastyear_collection) {
        this.lastyear_collection = lastyear_collection;
    }

    public Double getLastyear_cummulative_collection() {
        return lastyear_cummulative_collection;
    }

    public void setLastyear_cummulative_collection(Double lastyear_cummulative_collection) {
        this.lastyear_cummulative_collection = lastyear_cummulative_collection;
    }

    public Double getTarget_total_demand() {
        return target_total_demand;
    }

    public void setTarget_total_demand(Double target_total_demand) {
        this.target_total_demand = target_total_demand;
    }

    public BigDecimal getDay_target() {
        return day_target;
    }

    public void setDay_target(BigDecimal day_target) {
        this.day_target = day_target;
    }

    public Double getToday_total_collection() {
        return today_total_collection;
    }

    public void setToday_total_collection(Double today_total_collection) {
        this.today_total_collection = today_total_collection;
    }

    public Double getCummulative_total_Collection() {
        return cummulative_total_Collection;
    }

    public void setCummulative_total_Collection(Double cummulative_total_Collection) {
        this.cummulative_total_Collection = cummulative_total_Collection;
    }

    public BigDecimal getCummulative_currentYear_Percentage() {
        return cummulative_currentYear_Percentage;
    }

    public void setCummulative_currentYear_Percentage(BigDecimal cummulative_currentYear_Percentage) {
        this.cummulative_currentYear_Percentage = cummulative_currentYear_Percentage;
    }

    public Double getPercentage_compareWithLastYear() {
        return Percentage_compareWithLastYear;
    }

    public void setPercentage_compareWithLastYear(Double percentage_compareWithLastYear) {
        Percentage_compareWithLastYear = percentage_compareWithLastYear;
    }

    public BigDecimal getGrowth() {
        return growth;
    }

    public void setGrowth(BigDecimal growth) {
        this.growth = growth;
    }

  
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
