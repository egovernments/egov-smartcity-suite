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

package org.egov.pgr.elasticSearch.entity;

import org.egov.infra.admin.master.entity.City;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.pgr.entity.Complaint;
import org.egov.search.domain.Searchable;
import org.elasticsearch.common.geo.GeoPoint;

import java.util.Date;

public class ComplaintIndex extends Complaint {

    private static final long serialVersionUID = 1L;

    @Searchable(group = Searchable.Group.CLAUSES)
    private City citydetails;

    @Searchable
    private String zone;

    @Searchable
    private String ward;

    @Searchable
    private Date completionDate;

    @Searchable
    private double complaintDuration;

    @Searchable
    private boolean isClosed;

    @Searchable
    private String durationRange;

    @Searchable(name = "complaintLocation", group = Searchable.Group.COMMON)
    private GeoPoint complaintLocation;

    public City getCitydetails() {
        return citydetails;
    }

    public void setCitydetails(City citydetails) {
        this.citydetails = citydetails;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public double getComplaintDuration() {
        return complaintDuration;
    }

    public void setComplaintDuration(double complaintDuration) {
        this.complaintDuration = complaintDuration;
    }

    public boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public String getDurationRange() {
        return durationRange;
    }

    public void setDurationRange(String durationRange) {
        this.durationRange = durationRange;
    }

    public GeoPoint getComplaintLocation() {
        if (!(super.getLat() == 0.0 && super.getLng() == 0.0)) {
            this.complaintLocation = (new GeoPoint(super.getLat(), super.getLng()));
        }
        return complaintLocation;
    }

    public void setComplaintLocation(GeoPoint complaintLocation) {
        this.complaintLocation = complaintLocation;
    }

    @Override
    public String getIndexId() {
        return ApplicationThreadLocals.getCityCode() + "-" + super.getId().toString();
    }

    public static ComplaintIndex method(Complaint complaint) {
        ComplaintIndex complaintIndex = null;
        if (complaint instanceof ComplaintIndex) {
            complaintIndex = (ComplaintIndex) complaint;// downcasting

        }
        return complaintIndex;
    }

}
