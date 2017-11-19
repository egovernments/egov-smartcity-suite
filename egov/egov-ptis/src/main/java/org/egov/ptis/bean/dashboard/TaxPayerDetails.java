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
package org.egov.ptis.bean.dashboard;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class TaxPayerDetails implements Comparable<TaxPayerDetails> {

    private String regionName = StringUtils.EMPTY;
    private String districtName = StringUtils.EMPTY;
    private String ulbGrade = StringUtils.EMPTY;
    private String ulbName = StringUtils.EMPTY;
    private String wardName = StringUtils.EMPTY;
    private String billCollector = StringUtils.EMPTY;
    private String mobileNumber = StringUtils.EMPTY;
    private BigDecimal totalDmd = BigDecimal.ZERO;
    private BigDecimal cytdDmd = BigDecimal.ZERO;
    private BigDecimal cytdColl = BigDecimal.ZERO;
    private BigDecimal achievement = BigDecimal.ZERO;
    private BigDecimal cytdBalDmd = BigDecimal.ZERO;
    private BigDecimal lytdColl = BigDecimal.ZERO;
    private BigDecimal lyVar = BigDecimal.ZERO;
    private BigDecimal arrearColl = BigDecimal.ZERO;
    private BigDecimal currentColl = BigDecimal.ZERO;
    private BigDecimal interestColl = BigDecimal.ZERO;
    private BigDecimal arrearDemand = BigDecimal.ZERO;
    private BigDecimal currentDemand = BigDecimal.ZERO;
    private BigDecimal proportionalArrearDemand = BigDecimal.ZERO;
    private BigDecimal proportionalCurrentDemand = BigDecimal.ZERO;
    private BigDecimal arrearInterestDemand = BigDecimal.ZERO;
    private BigDecimal currentInterestDemand = BigDecimal.ZERO;

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getUlbGrade() {
        return ulbGrade;
    }

    public void setUlbGrade(String ulbGrade) {
        this.ulbGrade = ulbGrade;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(String ulbName) {
        this.ulbName = ulbName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getBillCollector() {
        return billCollector;
    }

    public void setBillCollector(String billCollector) {
        this.billCollector = billCollector;
    }

    public BigDecimal getTotalDmd() {
        return totalDmd;
    }

    public void setTotalDmd(BigDecimal totalDmd) {
        this.totalDmd = totalDmd;
    }

    public BigDecimal getCytdDmd() {
        return cytdDmd;
    }

    public void setCytdDmd(BigDecimal cytdDmd) {
        this.cytdDmd = cytdDmd;
    }

    public BigDecimal getCytdColl() {
        return cytdColl;
    }

    public void setCytdColl(BigDecimal cytdColl) {
        this.cytdColl = cytdColl;
    }

    public BigDecimal getAchievement() {
        return achievement;
    }

    public void setAchievement(BigDecimal achievement) {
        this.achievement = achievement;
    }

    public BigDecimal getCytdBalDmd() {
        return cytdBalDmd;
    }

    public void setCytdBalDmd(BigDecimal cytdBalDmd) {
        this.cytdBalDmd = cytdBalDmd;
    }

    public BigDecimal getLytdColl() {
        return lytdColl;
    }

    public void setLytdColl(BigDecimal lytdColl) {
        this.lytdColl = lytdColl;
    }

    public BigDecimal getLyVar() {
        return lyVar;
    }

    public void setLyVar(BigDecimal lyVar) {
        this.lyVar = lyVar;
    }

    @Override
    public int compareTo(TaxPayerDetails object) {
        return achievement.compareTo(object.getAchievement());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((achievement == null) ? 0 : achievement.hashCode());
        result = prime * result + ((cytdBalDmd == null) ? 0 : cytdBalDmd.hashCode());
        result = prime * result + ((cytdColl == null) ? 0 : cytdColl.hashCode());
        result = prime * result + ((cytdDmd == null) ? 0 : cytdDmd.hashCode());
        result = prime * result + ((districtName == null) ? 0 : districtName.hashCode());
        result = prime * result + ((lyVar == null) ? 0 : lyVar.hashCode());
        result = prime * result + ((lytdColl == null) ? 0 : lytdColl.hashCode());
        result = prime * result + ((regionName == null) ? 0 : regionName.hashCode());
        result = prime * result + ((totalDmd == null) ? 0 : totalDmd.hashCode());
        result = prime * result + ((ulbGrade == null) ? 0 : ulbGrade.hashCode());
        result = prime * result + ((ulbName == null) ? 0 : ulbName.hashCode());
        result = prime * result + ((wardName == null) ? 0 : wardName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TaxPayerDetails other = (TaxPayerDetails) obj;
        if (achievement == null) {
            if (other.achievement != null)
                return false;
        } else if (!achievement.equals(other.achievement))
            return false;
        if (cytdBalDmd == null) {
            if (other.cytdBalDmd != null)
                return false;
        } else if (!cytdBalDmd.equals(other.cytdBalDmd))
            return false;
        if (cytdColl == null) {
            if (other.cytdColl != null)
                return false;
        } else if (!cytdColl.equals(other.cytdColl))
            return false;
        if (cytdDmd == null) {
            if (other.cytdDmd != null)
                return false;
        } else if (!cytdDmd.equals(other.cytdDmd))
            return false;
        if (districtName == null) {
            if (other.districtName != null)
                return false;
        } else if (!districtName.equals(other.districtName))
            return false;
        if (lyVar == null) {
            if (other.lyVar != null)
                return false;
        } else if (!lyVar.equals(other.lyVar))
            return false;
        if (lytdColl == null) {
            if (other.lytdColl != null)
                return false;
        } else if (!lytdColl.equals(other.lytdColl))
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public BigDecimal getArrearColl() {
        return arrearColl;
    }

    public void setArrearColl(BigDecimal arrearColl) {
        this.arrearColl = arrearColl;
    }

    public BigDecimal getCurrentColl() {
        return currentColl;
    }

    public void setCurrentColl(BigDecimal currentColl) {
        this.currentColl = currentColl;
    }

    public BigDecimal getInterestColl() {
        return interestColl;
    }

    public void setInterestColl(BigDecimal interestColl) {
        this.interestColl = interestColl;
    }

    public BigDecimal getArrearDemand() {
        return arrearDemand;
    }

    public void setArrearDemand(BigDecimal arrearDemand) {
        this.arrearDemand = arrearDemand;
    }

    public BigDecimal getCurrentDemand() {
        return currentDemand;
    }

    public void setCurrentDemand(BigDecimal currentDemand) {
        this.currentDemand = currentDemand;
    }

    public BigDecimal getProportionalArrearDemand() {
        return proportionalArrearDemand;
    }

    public void setProportionalArrearDemand(BigDecimal proportionalArrearDemand) {
        this.proportionalArrearDemand = proportionalArrearDemand;
    }

    public BigDecimal getProportionalCurrentDemand() {
        return proportionalCurrentDemand;
    }

    public void setProportionalCurrentDemand(BigDecimal proportionalCurrentDemand) {
        this.proportionalCurrentDemand = proportionalCurrentDemand;
    }

    public BigDecimal getArrearInterestDemand() {
        return arrearInterestDemand;
    }

    public void setArrearInterestDemand(BigDecimal arrearInterestDemand) {
        this.arrearInterestDemand = arrearInterestDemand;
    }

    public BigDecimal getCurrentInterestDemand() {
        return currentInterestDemand;
    }

    public void setCurrentInterestDemand(BigDecimal currentInterestDemand) {
        this.currentInterestDemand = currentInterestDemand;
    }

}
