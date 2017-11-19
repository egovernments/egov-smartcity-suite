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
package org.egov.works.models.measurementbook;

public class MeasurementBookPDF {
    private String scheduleNo;
    private String workDescription;
    private Double completedMeasurement;
    private Double unitRate;
    private String uom;
    private Double completedCost;
    private String pageNo;
    private Double prevMeasurement;
    private Double currentMeasurement;
    private Double currentCost;
    private String revisionType;

    public String getScheduleNo() {
        return scheduleNo;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public Double getCompletedMeasurement() {
        return completedMeasurement;
    }

    public Double getUnitRate() {
        return unitRate;
    }

    public String getUom() {
        return uom;
    }

    public Double getCompletedCost() {
        return completedCost;
    }

    public String getPageNo() {
        return pageNo;
    }

    public Double getCurrentMeasurement() {
        return currentMeasurement;
    }

    public Double getCurrentCost() {
        return currentCost;
    }

    public void setScheduleNo(final String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public void setWorkDescription(final String workDescription) {
        this.workDescription = workDescription;
    }

    public void setCompletedMeasurement(final Double completedMeasurement) {
        this.completedMeasurement = completedMeasurement;
    }

    public void setUnitRate(final Double unitRate) {
        this.unitRate = unitRate;
    }

    public void setUom(final String uom) {
        this.uom = uom;
    }

    public void setCompletedCost(final Double completedCost) {
        this.completedCost = completedCost;
    }

    public void setPageNo(final String pageNo) {
        this.pageNo = pageNo;
    }

    public void setCurrentMeasurement(final Double currentMeasurement) {
        this.currentMeasurement = currentMeasurement;
    }

    public void setCurrentCost(final Double currentCost) {
        this.currentCost = currentCost;
    }

    public String getRevisionType() {
        return revisionType;
    }

    public void setRevisionType(final String revisionType) {
        this.revisionType = revisionType;
    }

    public Double getPrevMeasurement() {
        return prevMeasurement;
    }

    public void setPrevMeasurement(final Double prevMeasurement) {
        this.prevMeasurement = prevMeasurement;
    }

}
