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
package org.egov.ptis.bean.dashboard;

public class DayWiseCollection {

    private String ulbName;
    private DemandCollectionMIS day1DCB;
    private DemandCollectionMIS day2DCB;
    private DemandCollectionMIS day3DCB;
    private DemandCollectionMIS day4DCB;
    private DemandCollectionMIS day5DCB;
    private DemandCollectionMIS day6DCB;
    private DemandCollectionMIS day7DCB;

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(String ulbName) {
        this.ulbName = ulbName;
    }

    public DemandCollectionMIS getDay1DCB() {
        if (day1DCB == null) {
            day1DCB = new DemandCollectionMIS();
            day1DCB.setIntervalCount(1);
        }
        return day1DCB;
    }

    public void setDay1DCB(DemandCollectionMIS day1dcb) {
        day1DCB = day1dcb;
        day1DCB.setIntervalCount(1);
    }

    public DemandCollectionMIS getDay2DCB() {
        if (day2DCB == null) {
            day2DCB = new DemandCollectionMIS();
            day2DCB.setIntervalCount(2);
        }
        return day2DCB;
    }

    public void setDay2DCB(DemandCollectionMIS day2dcb) {
        day2DCB = day2dcb;
        day2DCB.setIntervalCount(2);
    }

    public DemandCollectionMIS getDay3DCB() {
        if (day3DCB == null) {
            day3DCB = new DemandCollectionMIS();
            day3DCB.setIntervalCount(3);
        }
        return day3DCB;
    }

    public void setDay3DCB(DemandCollectionMIS day3dcb) {
        day3DCB = day3dcb;
        day3DCB.setIntervalCount(3);
    }

    public DemandCollectionMIS getDay4DCB() {
        if (day4DCB == null) {
            day4DCB = new DemandCollectionMIS();
            day4DCB.setIntervalCount(4);
        }
        return day4DCB;
    }

    public void setDay4DCB(DemandCollectionMIS day4dcb) {
        day4DCB = day4dcb;
        day4DCB.setIntervalCount(4);
    }

    public DemandCollectionMIS getDay5DCB() {
        if (day5DCB == null) {
            day5DCB = new DemandCollectionMIS();
            day5DCB.setIntervalCount(5);
        }
        return day5DCB;
    }

    public void setDay5DCB(DemandCollectionMIS day5dcb) {
        day5DCB = day5dcb;
        day5DCB.setIntervalCount(5);
    }

    public DemandCollectionMIS getDay6DCB() {
        if (day6DCB == null) {
            day6DCB = new DemandCollectionMIS();
            day6DCB.setIntervalCount(6);
        }
        return day6DCB;
    }

    public void setDay6DCB(DemandCollectionMIS day6dcb) {
        day6DCB = day6dcb;
        day6DCB.setIntervalCount(6);
    }

    public DemandCollectionMIS getDay7DCB() {
        if (day7DCB == null) {
            day7DCB = new DemandCollectionMIS();
            day7DCB.setIntervalCount(7);
        }
        return day7DCB;
    }

    public void setDay7DCB(DemandCollectionMIS day7dcb) {
        day7DCB = day7dcb;
        day7DCB.setIntervalCount(7);
    }
}
