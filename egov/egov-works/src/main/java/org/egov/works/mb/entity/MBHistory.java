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
package org.egov.works.mb.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.works.abstractestimate.entity.Activity;

/**
 * @author venki
 */

public class MBHistory {

    private String ownerName;

    private String dateTime;

    private BigDecimal mbAmount;

    private String status;

    private Set<Activity> sorActivities = new HashSet<Activity>(0);

    private Set<Activity> nonSorActivities = new HashSet<Activity>(0);

    private Set<Activity> nonTenActivities = new HashSet<Activity>(0);

    private Set<Activity> lumpSumActivities = new HashSet<Activity>(0);

    private List<MBDetails> sorMbDetails = new ArrayList<MBDetails>(0);

    private List<MBDetails> nonSorMbDetails = new ArrayList<MBDetails>(0);

    private List<MBDetails> nonTenderedMbDetails = new ArrayList<MBDetails>(0);

    private List<MBDetails> lumpSumMbDetails = new ArrayList<MBDetails>(0);

    private List<MBDetails> mbDetails = new ArrayList<MBDetails>(0);

    public String getOwnerName() {
        return ownerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(final String dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getMbAmount() {
        return mbAmount;
    }

    public void setMbAmount(final BigDecimal mbAmount) {
        this.mbAmount = mbAmount;
    }

    public List<MBDetails> getMbDetails() {
        return mbDetails;
    }

    public void setMbDetails(final List<MBDetails> mbDetails) {
        this.mbDetails = mbDetails;
    }

    public List<MBDetails> getSorMbDetails() {
        return sorMbDetails;
    }

    public void setSorMbDetails(final List<MBDetails> sorMbDetails) {
        this.sorMbDetails = sorMbDetails;
    }

    public List<MBDetails> getNonSorMbDetails() {
        return nonSorMbDetails;
    }

    public void setNonSorMbDetails(final List<MBDetails> nonSorMbDetails) {
        this.nonSorMbDetails = nonSorMbDetails;
    }

    public List<MBDetails> getNonTenderedMbDetails() {
        return nonTenderedMbDetails;
    }

    public void setNonTenderedMbDetails(final List<MBDetails> nonTenderedMbDetails) {
        this.nonTenderedMbDetails = nonTenderedMbDetails;
    }

    public List<MBDetails> getLumpSumMbDetails() {
        return lumpSumMbDetails;
    }

    public void setLumpSumMbDetails(final List<MBDetails> lumpSumMbDetails) {
        this.lumpSumMbDetails = lumpSumMbDetails;
    }

    public Set<Activity> getSorActivities() {
        return sorActivities;
    }

    public List<Activity> getSorActivitiesAsList() {
        return new ArrayList<Activity>(sorActivities);
    }

    public void setSorActivities(final Set<Activity> sorActivities) {
        this.sorActivities = sorActivities;
    }

    public Set<Activity> getNonSorActivities() {
        return nonSorActivities;
    }

    public List<Activity> getNonSorActivitiesAsList() {
        return new ArrayList<Activity>(nonSorActivities);
    }

    public void setNonSorActivities(final Set<Activity> nonSorActivities) {
        this.nonSorActivities = nonSorActivities;
    }

    public Set<Activity> getNonTenActivities() {
        return nonTenActivities;
    }

    public List<Activity> getNonTenActivitiesAsList() {
        return new ArrayList<Activity>(nonTenActivities);
    }

    public void setNonTenActivities(final Set<Activity> nonTenActivities) {
        this.nonTenActivities = nonTenActivities;
    }

    public Set<Activity> getLumpSumActivities() {
        return lumpSumActivities;
    }

    public List<Activity> getLumpSumActivitiesAsList() {
        return new ArrayList<Activity>(lumpSumActivities);
    }

    public void setLumpSumActivities(final Set<Activity> lumpSumActivities) {
        this.lumpSumActivities = lumpSumActivities;
    }

}