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
package org.egov.ptis.domain.entity.demand;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.ptis.domain.entity.property.Floor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * This class represents Floorwise Demand Calculations and this class
 * encapsulates the intermediate results of Demand calculations
 * 
 * @author Ramakrishna I
 * @version 1.00
 * @see org.egov.ptis.domain.entity.property.Floor
 * @since 1.00
 */
public class FloorwiseDemandCalculations {
    private Integer id = null;
    private Floor floor = null;
    private PTDemandCalculations pTDemandCalculations = null;
    private Date lastUpdatedTimeStamp;
    private Date createTimeStamp;
    private BigDecimal categoryAmt;
    private BigDecimal occupancyRebate;
    private BigDecimal constructionRebate;
    private BigDecimal depreciation;
    private BigDecimal usageRebate;
    private BigDecimal tax1 = BigDecimal.ZERO;
    private BigDecimal tax2 = BigDecimal.ZERO;
    private BigDecimal tax3 = BigDecimal.ZERO;
    private BigDecimal tax4 = BigDecimal.ZERO;
    private BigDecimal tax5 = BigDecimal.ZERO;
    private BigDecimal tax6 = BigDecimal.ZERO;
    private BigDecimal tax7 = BigDecimal.ZERO;
    private BigDecimal tax8 = BigDecimal.ZERO;
    private BigDecimal tax9 = BigDecimal.ZERO;
    private BigDecimal tax10 = BigDecimal.ZERO;
    private BigDecimal alv;
    private BigDecimal mrv;
    private BigDecimal totalTaxPayble;

    public FloorwiseDemandCalculations() {
        super();
    }

    public FloorwiseDemandCalculations(Integer id, Floor floor, PTDemandCalculations demandCalculations,
            Date lastUpdatedTimeStamp, Date createTimeStamp, BigDecimal categoryAmt, BigDecimal occupancyRebate,
            BigDecimal constructionRebate, BigDecimal depreciation, BigDecimal usageRebate) {
        super();
        this.id = id;
        this.floor = floor;
        pTDemandCalculations = demandCalculations;
        this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
        this.createTimeStamp = createTimeStamp;
        this.categoryAmt = categoryAmt;
        this.occupancyRebate = occupancyRebate;
        this.constructionRebate = constructionRebate;
        this.depreciation = depreciation;
        this.usageRebate = usageRebate;
    }

    public FloorwiseDemandCalculations(FloorwiseDemandCalculations thatFloorDmdCalc) {
        this.floor = thatFloorDmdCalc.floor;
        this.lastUpdatedTimeStamp = new Date();
        this.createTimeStamp = new Date();
        this.categoryAmt = thatFloorDmdCalc.categoryAmt;
        this.occupancyRebate = thatFloorDmdCalc.occupancyRebate;
        this.constructionRebate = thatFloorDmdCalc.constructionRebate;
        this.depreciation = thatFloorDmdCalc.depreciation;
        this.usageRebate = thatFloorDmdCalc.usageRebate;
        this.tax1 = thatFloorDmdCalc.getTax1();
        this.tax2 = thatFloorDmdCalc.getTax2();
        this.tax3 = thatFloorDmdCalc.getTax3();
        this.tax4 = thatFloorDmdCalc.getTax4();
        this.tax5 = thatFloorDmdCalc.getTax5();
        this.tax6 = thatFloorDmdCalc.getTax6();
        this.tax7 = thatFloorDmdCalc.getTax7();
        this.tax8 = thatFloorDmdCalc.getTax8();
        this.tax9 = thatFloorDmdCalc.getTax9();
        this.tax10 = thatFloorDmdCalc.getTax10();
        this.alv = thatFloorDmdCalc.getAlv();
        this.mrv = thatFloorDmdCalc.getMrv();
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the floor
     */
    public Floor getFloor() {
        return floor;
    }

    /**
     * @param floor
     *            the floor to set
     */
    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    /**
     * @return the pTDemandCalculations
     */
    public PTDemandCalculations getPTDemandCalculations() {
        return pTDemandCalculations;
    }

    /**
     * @param demandCalculations
     *            the pTDemandCalculations to set
     */
    public void setPTDemandCalculations(PTDemandCalculations demandCalculations) {
        pTDemandCalculations = demandCalculations;
    }

    /**
     * @return the lastUpdatedTimeStamp
     */
    public Date getLastUpdatedTimeStamp() {
        return lastUpdatedTimeStamp;
    }

    /**
     * @param lastUpdatedTimeStamp
     *            the lastUpdatedTimeStamp to set
     */
    public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
        this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
    }

    /**
     * @return the createTimeStamp
     */
    public Date getCreateTimeStamp() {
        return createTimeStamp;
    }

    /**
     * @param createTimeStamp
     *            the createTimeStamp to set
     */
    public void setCreateTimeStamp(Date createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    /**
     * @return the categoryAmt
     */
    public BigDecimal getCategoryAmt() {
        return categoryAmt;
    }

    /**
     * @param categoryAmt
     *            the categoryAmt to set
     */
    public void setCategoryAmt(BigDecimal categoryAmt) {
        this.categoryAmt = categoryAmt;
    }

    /**
     * @return the occupancyRebate
     */
    public BigDecimal getOccupancyRebate() {
        return occupancyRebate;
    }

    /**
     * @param occupancyRebate
     *            the occupancyRebate to set
     */
    public void setOccupancyRebate(BigDecimal occupancyRebate) {
        this.occupancyRebate = occupancyRebate;
    }

    /**
     * @return the constructionRebate
     */
    public BigDecimal getConstructionRebate() {
        return constructionRebate;
    }

    /**
     * @param constructionRebate
     *            the constructionRebate to set
     */
    public void setConstructionRebate(BigDecimal constructionRebate) {
        this.constructionRebate = constructionRebate;
    }

    /**
     * @return the depreciation
     */
    public BigDecimal getDepreciation() {
        return depreciation;
    }

    /**
     * @param depreciation
     *            the depreciation to set
     */
    public void setDepreciation(BigDecimal depreciation) {
        this.depreciation = depreciation;
    }

    /**
     * @return the usageRebate
     */
    public BigDecimal getUsageRebate() {
        return usageRebate;
    }

    /**
     * @param usageRebate
     *            the usageRebate to set
     */
    public void setUsageRebate(BigDecimal usageRebate) {
        this.usageRebate = usageRebate;
    }

    /**
     * @return the tax1
     */
    public BigDecimal getTax1() {
        return tax1;
    }

    /**
     * @param tax1
     *            the tax1 to set
     */
    public void setTax1(BigDecimal tax1) {
        this.tax1 = tax1;
    }

    /**
     * @return the tax2
     */
    public BigDecimal getTax2() {
        return tax2;
    }

    /**
     * @param tax2
     *            the tax2 to set
     */
    public void setTax2(BigDecimal tax2) {
        this.tax2 = tax2;
    }

    /**
     * @return the tax3
     */
    public BigDecimal getTax3() {
        return tax3;
    }

    /**
     * @param tax3
     *            the tax3 to set
     */
    public void setTax3(BigDecimal tax3) {
        this.tax3 = tax3;
    }

    /**
     * @return the tax4
     */
    public BigDecimal getTax4() {
        return tax4;
    }

    /**
     * @param tax4
     *            the tax4 to set
     */
    public void setTax4(BigDecimal tax4) {
        this.tax4 = tax4;
    }

    /**
     * @return the tax5
     */
    public BigDecimal getTax5() {
        return tax5;
    }

    /**
     * @param tax5
     *            the tax5 to set
     */
    public void setTax5(BigDecimal tax5) {
        this.tax5 = tax5;
    }

    /**
     * @return the tax6
     */
    public BigDecimal getTax6() {
        return tax6;
    }

    /**
     * @param tax6
     *            the tax6 to set
     */
    public void setTax6(BigDecimal tax6) {
        this.tax6 = tax6;
    }

    /**
     * @return the tax7
     */
    public BigDecimal getTax7() {
        return tax7;
    }

    /**
     * @param tax7
     *            the tax7 to set
     */
    public void setTax7(BigDecimal tax7) {
        this.tax7 = tax7;
    }

    /**
     * @return the tax8
     */
    public BigDecimal getTax8() {
        return tax8;
    }

    /**
     * @param tax8
     *            the tax8 to set
     */
    public void setTax8(BigDecimal tax8) {
        this.tax8 = tax8;
    }

    /**
     * @return the tax9
     */
    public BigDecimal getTax9() {
        return tax9;
    }

    /**
     * @param tax9
     *            the tax9 to set
     */
    public void setTax9(BigDecimal tax9) {
        this.tax9 = tax9;
    }

    /**
     * @return the tax10
     */
    public BigDecimal getTax10() {
        return tax10;
    }

    /**
     * @param tax10
     *            the tax10 to set
     */
    public void setTax10(BigDecimal tax10) {
        this.tax10 = tax10;
    }

    public BigDecimal getAlv() {
        return alv;
    }

    public void setAlv(BigDecimal alv) {
        this.alv = alv;
    }

    public BigDecimal getMrv() {
        return mrv;
    }

    public void setMrv(BigDecimal mrv) {
        this.mrv = mrv;
    }

    public BigDecimal getTotalTaxPayble() {
        return totalTaxPayble;
    }

    public void setTotalTaxPayble(BigDecimal totalTaxPayble) {
        this.totalTaxPayble = totalTaxPayble;
    }

    /**
     * @return true if the given Object is equal to
     */
    public boolean equals(Object that) {
        if (that == null)
            return false;

        if (this == that)
            return true;
        if (that.getClass() != this.getClass())
            return false;

        final FloorwiseDemandCalculations thatFlWsDemand = (FloorwiseDemandCalculations) that;

        if (this.getId() != null && thatFlWsDemand.getId() != null) {
            if (getId().equals(thatFlWsDemand.getId())) {
                return true;
            } else
                return false;
        } else if (this.getFloor() != null && thatFlWsDemand.getFloor() != null) {
            if (getFloor().equals(thatFlWsDemand.getFloor())) {
                return true;
            } else
                return false;
        } else if (this.getPTDemandCalculations() != null && thatFlWsDemand.getPTDemandCalculations() != null) {
            if (getPTDemandCalculations().equals(thatFlWsDemand.getPTDemandCalculations())) {
                return true;
            } else
                return false;
        } else
            return false;

    }

    /**
     * @return Returns the hashCode
     */
    public int hashCode() {
        int hashCode = 0;
        if (getId() != null) {
            hashCode += this.getId().hashCode();
        }
        if (getFloor() != null) {
            hashCode += this.getFloor().hashCode();
        }

        return hashCode;
    }

    /**
     * @return Returns the boolean after validating the current object
     */
    public boolean validate() {
        if (getFloor() == null)
            throw new ApplicationRuntimeException("In  Validate : Property is Not Set, Please Check !!");
        if (getPTDemandCalculations() == null)
            throw new ApplicationRuntimeException("In  Validate : PTDemandCalculations is Not Set, Please Check !!");

        return true;
    }

    @Override
    public String toString() {
        StringBuilder objStr = new StringBuilder();

        objStr.append("Id: ").append(getId()).append("|OccupRebate: ").append(getOccupancyRebate())
                .append("|ConstrucRebate : ").append(getConstructionRebate()).append("|Depreciation: ")
                .append(getDepreciation()).append("|UsageRebate: ").append(getUsageRebate());
        return objStr.toString();
    }
}
