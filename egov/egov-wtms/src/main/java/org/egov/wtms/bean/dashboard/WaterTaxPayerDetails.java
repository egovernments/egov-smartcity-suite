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
package org.egov.wtms.bean.dashboard;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class WaterTaxPayerDetails implements Comparable<WaterTaxPayerDetails> {

    private String regionName = StringUtils.EMPTY;
    private String districtName = StringUtils.EMPTY;
    private String ulbGrade = StringUtils.EMPTY;
    private String ulbName = StringUtils.EMPTY;
    private String wardName = StringUtils.EMPTY;
    private String billCollector = StringUtils.EMPTY;
    private BigDecimal totalDmd = BigDecimal.ZERO;
    private BigDecimal currentYearTillDateDmd = BigDecimal.ZERO;
    private BigDecimal currentYearTillDateColl = BigDecimal.ZERO;
    private BigDecimal achievement = BigDecimal.ZERO;
    private BigDecimal currentYearTillDateBalDmd = BigDecimal.ZERO;
    private BigDecimal lastYearTillDateColl = BigDecimal.ZERO;
    private BigDecimal lastYearVar = BigDecimal.ZERO;

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(final String regionName) {
        this.regionName = regionName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(final String districtName) {
        this.districtName = districtName;
    }

    public String getUlbGrade() {
        return ulbGrade;
    }

    public void setUlbGrade(final String ulbGrade) {
        this.ulbGrade = ulbGrade;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(final String ulbName) {
        this.ulbName = ulbName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(final String wardName) {
        this.wardName = wardName;
    }

    public String getBillCollector() {
        return billCollector;
    }

    public void setBillCollector(final String billCollector) {
        this.billCollector = billCollector;
    }

    public BigDecimal getTotalDmd() {
        return totalDmd;
    }

    public void setTotalDmd(final BigDecimal totalDmd) {
        this.totalDmd = totalDmd;
    }

    public BigDecimal getAchievement() {
        return achievement;
    }

    public void setAchievement(final BigDecimal achievement) {
        this.achievement = achievement;
    }

    public BigDecimal getCurrentYearTillDateDmd() {
        return currentYearTillDateDmd;
    }

    public void setCurrentYearTillDateDmd(final BigDecimal currentYearTillDateDmd) {
        this.currentYearTillDateDmd = currentYearTillDateDmd;
    }

    public BigDecimal getCurrentYearTillDateBalDmd() {
        return currentYearTillDateBalDmd;
    }

    public void setCurrentYearTillDateBalDmd(final BigDecimal currentYearTillDateBalDmd) {
        this.currentYearTillDateBalDmd = currentYearTillDateBalDmd;
    }

    public BigDecimal getLastYearTillDateColl() {
        return lastYearTillDateColl;
    }

    public void setLastYearTillDateColl(final BigDecimal lastYearTillDateColl) {
        this.lastYearTillDateColl = lastYearTillDateColl;
    }

    public BigDecimal getLastYearVar() {
        return lastYearVar;
    }

    public void setLastYearVar(final BigDecimal lastYearVar) {
        this.lastYearVar = lastYearVar;
    }

    @Override
    public int compareTo(final WaterTaxPayerDetails object) {
        return achievement.compareTo(object.getAchievement());
    }

    public BigDecimal getCurrentYearTillDateColl() {
        return currentYearTillDateColl;
    }

    public void setCurrentYearTillDateColl(final BigDecimal currentYearTillDateColl) {
        this.currentYearTillDateColl = currentYearTillDateColl;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (achievement == null ? 0 : achievement.hashCode());
        result = prime * result + (billCollector == null ? 0 : billCollector.hashCode());
        result = prime * result + (currentYearTillDateBalDmd == null ? 0 : currentYearTillDateBalDmd.hashCode());
        result = prime * result + (currentYearTillDateColl == null ? 0 : currentYearTillDateColl.hashCode());
        result = prime * result + (currentYearTillDateDmd == null ? 0 : currentYearTillDateDmd.hashCode());
        result = prime * result + (districtName == null ? 0 : districtName.hashCode());
        result = prime * result + (lastYearTillDateColl == null ? 0 : lastYearTillDateColl.hashCode());
        result = prime * result + (lastYearVar == null ? 0 : lastYearVar.hashCode());
        result = prime * result + (regionName == null ? 0 : regionName.hashCode());
        result = prime * result + (totalDmd == null ? 0 : totalDmd.hashCode());
        result = prime * result + (ulbGrade == null ? 0 : ulbGrade.hashCode());
        result = prime * result + (ulbName == null ? 0 : ulbName.hashCode());
        result = prime * result + (wardName == null ? 0 : wardName.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final WaterTaxPayerDetails other = (WaterTaxPayerDetails) obj;
        if (achievement == null) {
            if (other.achievement != null)
                return false;
        } else if (!achievement.equals(other.achievement))
            return false;
        if (billCollector == null) {
            if (other.billCollector != null)
                return false;
        } else if (!billCollector.equals(other.billCollector))
            return false;
        if (currentYearTillDateBalDmd == null) {
            if (other.currentYearTillDateBalDmd != null)
                return false;
        } else if (!currentYearTillDateBalDmd.equals(other.currentYearTillDateBalDmd))
            return false;
        if (currentYearTillDateColl == null) {
            if (other.currentYearTillDateColl != null)
                return false;
        } else if (!currentYearTillDateColl.equals(other.currentYearTillDateColl))
            return false;
        if (currentYearTillDateDmd == null) {
            if (other.currentYearTillDateDmd != null)
                return false;
        } else if (!currentYearTillDateDmd.equals(other.currentYearTillDateDmd))
            return false;
        if (districtName == null) {
            if (other.districtName != null)
                return false;
        } else if (!districtName.equals(other.districtName))
            return false;
        if (lastYearTillDateColl == null) {
            if (other.lastYearTillDateColl != null)
                return false;
        } else if (!lastYearTillDateColl.equals(other.lastYearTillDateColl))
            return false;
        if (lastYearVar == null) {
            if (other.lastYearVar != null)
                return false;
        } else if (!lastYearVar.equals(other.lastYearVar))
            return false;
        if (regionName == null) {
            if (other.regionName != null)
                return false;
        } else if (!regionName.equals(other.regionName))
            return false;
        if (totalDmd == null) {
            if (other.totalDmd != null)
                return false;
        } else if (!totalDmd.equals(other.totalDmd))
            return false;
        if (ulbGrade == null) {
            if (other.ulbGrade != null)
                return false;
        } else if (!ulbGrade.equals(other.ulbGrade))
            return false;
        if (ulbName == null) {
            if (other.ulbName != null)
                return false;
        } else if (!ulbName.equals(other.ulbName))
            return false;
        if (wardName == null) {
            if (other.wardName != null)
                return false;
        } else if (!wardName.equals(other.wardName))
            return false;
        return true;
    }

}