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

public class WeeklyDCB {
    private String boundaryName;
    private String billCollectorName;
    private DemandCollectionMIS week1DCB;
    private DemandCollectionMIS week2DCB;
    private DemandCollectionMIS week3DCB;
    private DemandCollectionMIS week4DCB;
    private DemandCollectionMIS week5DCB;

    public String getBoundaryName() {
        return boundaryName;
    }

    public void setBoundaryName(String boundaryName) {
        this.boundaryName = boundaryName;
    }

    public String getBillCollectorName() {
        return billCollectorName;
    }

    public void setBillCollectorName(String billCollectorName) {
        this.billCollectorName = billCollectorName;
    }

    public DemandCollectionMIS getWeek1DCB() {
        if(week1DCB == null){
            week1DCB = new DemandCollectionMIS();
            week1DCB.setIntervalCount(1);
        }
        return week1DCB;
    }

    public void setWeek1DCB(DemandCollectionMIS week1dcb) {
        week1dcb.setIntervalCount(1);
        week1DCB = week1dcb;
    }

    public DemandCollectionMIS getWeek2DCB() {
        if(week2DCB == null){
            week2DCB = new DemandCollectionMIS();
            week2DCB.setIntervalCount(2);
        }
        return week2DCB;
    }

    public void setWeek2DCB(DemandCollectionMIS week2dcb) {
        week2dcb.setIntervalCount(2);
        week2DCB = week2dcb;
    }

    public DemandCollectionMIS getWeek3DCB() {
        if(week3DCB == null){
            week3DCB = new DemandCollectionMIS();
            week3DCB.setIntervalCount(3);
        }
        return week3DCB;
    }

    public void setWeek3DCB(DemandCollectionMIS week3dcb) {
        week3dcb.setIntervalCount(3);
        week3DCB = week3dcb;
    }

    public DemandCollectionMIS getWeek4DCB() {
        if(week4DCB == null){
            week4DCB = new DemandCollectionMIS();
            week4DCB.setIntervalCount(4);
        }
        return week4DCB;
    }

    public void setWeek4DCB(DemandCollectionMIS week4dcb) {
        week4dcb.setIntervalCount(4);
        week4DCB = week4dcb;
    }

    public DemandCollectionMIS getWeek5DCB() {
        if(week5DCB == null){
            week5DCB = new DemandCollectionMIS();
            week5DCB.setIntervalCount(5);
        }
        return week5DCB;
    }

    public void setWeek5DCB(DemandCollectionMIS week5dcb) {
        week5dcb.setIntervalCount(5);
        week5DCB = week5dcb;
    }
}
