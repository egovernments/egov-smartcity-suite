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

import static org.egov.search.domain.Filter.termsStringFilter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.infra.admin.master.entity.City;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.pgr.entity.Complaint;
import org.egov.search.domain.Filter;
import org.egov.search.domain.Filters;
import org.egov.search.domain.Searchable;
import org.elasticsearch.common.geo.GeoPoint;

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
    private char complaintIsClosed;

	@Searchable
    private int ifClosed;

    @Searchable
    private String durationRange;

    @Searchable(name = "complaintLocation", group = Searchable.Group.COMMON)
    private GeoPoint complaintLocation;
    
    @Searchable(name = "source", group = Searchable.Group.COMMON)
    private String source;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private double complaintPeriod;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private long complaintSLADays;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private double complaintAgeingFromDue;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private char isSLA;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private int ifSLA;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private String currentFunctionaryName;
    
    @Searchable(name = "currentFunctionaryAssigneddate")
    private Date currentFunctionaryAssigneddate;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private long currentFunctionarySLADays;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private double currentFunctionaryAgeingFromDue;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private char currentFunctionaryIsSLA;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private int currentFunctionaryIfSLA;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private String closedByFunctionaryName;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private String initialFunctionaryName;
    
    @Searchable(name = "initialFunctionaryAssigneddate")
    private Date initialFunctionaryAssigneddate;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private long initialFunctionarySLADays;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private double initialFunctionaryAgeingFromDue;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private char initialFunctionaryIsSLA;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private int initialFunctionaryIfSLA;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private String escalation1FunctionaryName;
    
    @Searchable(name = "escalation1FunctionaryAssigneddate")
    private Date escalation1FunctionaryAssigneddate;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private long escalation1FunctionarySLADays;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private double escalation1FunctionaryAgeingFromDue;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private char escalation1FunctionaryIsSLA;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private int escalation1FunctionaryIfSLA;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private String escalation2FunctionaryName;
    
    @Searchable(name = "escalation2FunctionaryAssigneddate")
    private Date escalation2FunctionaryAssigneddate;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private long escalation2FunctionarySLADays;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private double escalation2FunctionaryAgeingFromDue;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private char escalation2FunctionaryIsSLA;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private int escalation2FunctionaryIfSLA;

    @Searchable(group = Searchable.Group.CLAUSES)
    private String escalation3FunctionaryName;
    
    @Searchable(name = "escalation3FunctionaryAssigneddate")
    private Date escalation3FunctionaryAssigneddate;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private long escalation3FunctionarySLADays;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private double escalation3FunctionaryAgeingFromDue;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private char escalation3FunctionaryIsSLA;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private int escalation3FunctionaryIfSLA;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private int escalationLevel;
    
    @Searchable(name = "complaintReOpenedDate")
    private Date complaintReOpenedDate;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private String reasonForRejection;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private int registered;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private int inProcess;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private int addressed;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private int rejected;
    
    @Searchable(group = Searchable.Group.CLAUSES)
    private int reOpened;
    
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

	public int getIfClosed() {
		return ifClosed;
	}

	public void setIfClosed(int ifClosed) {
		this.ifClosed = ifClosed;
	}

	public double getComplaintPeriod() {
		return complaintPeriod;
	}

	public void setComplaintPeriod(double complaintPeriod) {
		this.complaintPeriod = complaintPeriod;
	}

	public long getComplaintSLADays() {
		return complaintSLADays;
	}

	public void setComplaintSLADays(long complaintSLADays) {
		this.complaintSLADays = complaintSLADays;
	}

	public double getComplaintAgeingFromDue() {
		return complaintAgeingFromDue;
	}

	public void setComplaintAgeingFromDue(double complaintAgeingFromDue) {
		this.complaintAgeingFromDue = complaintAgeingFromDue;
	}

	public char getIsSLA() {
		return isSLA;
	}

	public void setIsSLA(char isSLA) {
		this.isSLA = isSLA;
	}

	public int getIfSLA() {
		return ifSLA;
	}

	public void setIfSLA(int ifSLA) {
		this.ifSLA = ifSLA;
	}

	public String getCurrentFunctionaryName() {
		return currentFunctionaryName;
	}

	public void setCurrentFunctionaryName(String currentFunctionaryName) {
		this.currentFunctionaryName = currentFunctionaryName;
	}

	public Date getCurrentFunctionaryAssigneddate() {
		return currentFunctionaryAssigneddate;
	}

	public void setCurrentFunctionaryAssigneddate(
			Date currentFunctionaryAssigneddate) {
		this.currentFunctionaryAssigneddate = currentFunctionaryAssigneddate;
	}

	public long getCurrentFunctionarySLADays() {
		return currentFunctionarySLADays;
	}

	public void setCurrentFunctionarySLADays(long currentFunctionarySLADays) {
		this.currentFunctionarySLADays = currentFunctionarySLADays;
	}

	public double getCurrentFunctionaryAgeingFromDue() {
		return currentFunctionaryAgeingFromDue;
	}

	public void setCurrentFunctionaryAgeingFromDue(
			double currentFunctionaryAgeingFromDue) {
		this.currentFunctionaryAgeingFromDue = currentFunctionaryAgeingFromDue;
	}

	public char getCurrentFunctionaryIsSLA() {
		return currentFunctionaryIsSLA;
	}

	public void setCurrentFunctionaryIsSLA(char currentFunctionaryIsSLA) {
		this.currentFunctionaryIsSLA = currentFunctionaryIsSLA;
	}

	public int getCurrentFunctionaryIfSLA() {
		return currentFunctionaryIfSLA;
	}

	public void setCurrentFunctionaryIfSLA(int currentFunctionaryIfSLA) {
		this.currentFunctionaryIfSLA = currentFunctionaryIfSLA;
	}

	public String getClosedByFunctionaryName() {
		return closedByFunctionaryName;
	}

	public void setClosedByFunctionaryName(String closedByFunctionaryName) {
		this.closedByFunctionaryName = closedByFunctionaryName;
	}

	public String getInitialFunctionaryName() {
		return initialFunctionaryName;
	}

	public void setInitialFunctionaryName(String initialFunctionaryName) {
		this.initialFunctionaryName = initialFunctionaryName;
	}

	public Date getInitialFunctionaryAssigneddate() {
		return initialFunctionaryAssigneddate;
	}

	public void setInitialFunctionaryAssigneddate(
			Date initialFunctionaryAssigneddate) {
		this.initialFunctionaryAssigneddate = initialFunctionaryAssigneddate;
	}

	public long getInitialFunctionarySLADays() {
		return initialFunctionarySLADays;
	}

	public void setInitialFunctionarySLADays(long initialFunctionarySLADays) {
		this.initialFunctionarySLADays = initialFunctionarySLADays;
	}

	public double getInitialFunctionaryAgeingFromDue() {
		return initialFunctionaryAgeingFromDue;
	}

	public void setInitialFunctionaryAgeingFromDue(
			double initialFunctionaryAgeingFromDue) {
		this.initialFunctionaryAgeingFromDue = initialFunctionaryAgeingFromDue;
	}

	public char getInitialFunctionaryIsSLA() {
		return initialFunctionaryIsSLA;
	}

	public void setInitialFunctionaryIsSLA(char initialFunctionaryIsSLA) {
		this.initialFunctionaryIsSLA = initialFunctionaryIsSLA;
	}

	public int getInitialFunctionaryIfSLA() {
		return initialFunctionaryIfSLA;
	}

	public void setInitialFunctionaryIfSLA(int initialFunctionaryIfSLA) {
		this.initialFunctionaryIfSLA = initialFunctionaryIfSLA;
	}

	public String getEscalation1FunctionaryName() {
		return escalation1FunctionaryName;
	}

	public void setEscalation1FunctionaryName(String escalation1FunctionaryName) {
		this.escalation1FunctionaryName = escalation1FunctionaryName;
	}

	public Date getEscalation1FunctionaryAssigneddate() {
		return escalation1FunctionaryAssigneddate;
	}

	public void setEscalation1FunctionaryAssigneddate(
			Date escalation1FunctionaryAssigneddate) {
		this.escalation1FunctionaryAssigneddate = escalation1FunctionaryAssigneddate;
	}

	public long getEscalation1FunctionarySLADays() {
		return escalation1FunctionarySLADays;
	}

	public void setEscalation1FunctionarySLADays(
			long escalation1FunctionarySLADays) {
		this.escalation1FunctionarySLADays = escalation1FunctionarySLADays;
	}

	public double getEscalation1FunctionaryAgeingFromDue() {
		return escalation1FunctionaryAgeingFromDue;
	}

	public void setEscalation1FunctionaryAgeingFromDue(
			double escalation1FunctionaryAgeingFromDue) {
		this.escalation1FunctionaryAgeingFromDue = escalation1FunctionaryAgeingFromDue;
	}

	public char getEscalation1FunctionaryIsSLA() {
		return escalation1FunctionaryIsSLA;
	}

	public void setEscalation1FunctionaryIsSLA(char escalation1FunctionaryIsSLA) {
		this.escalation1FunctionaryIsSLA = escalation1FunctionaryIsSLA;
	}

	public int getEscalation1FunctionaryIfSLA() {
		return escalation1FunctionaryIfSLA;
	}

	public void setEscalation1FunctionaryIfSLA(int escalation1FunctionaryIfSLA) {
		this.escalation1FunctionaryIfSLA = escalation1FunctionaryIfSLA;
	}

	public String getEscalation2FunctionaryName() {
		return escalation2FunctionaryName;
	}

	public void setEscalation2FunctionaryName(String escalation2FunctionaryName) {
		this.escalation2FunctionaryName = escalation2FunctionaryName;
	}

	public Date getEscalation2FunctionaryAssigneddate() {
		return escalation2FunctionaryAssigneddate;
	}

	public void setEscalation2FunctionaryAssigneddate(
			Date escalation2FunctionaryAssigneddate) {
		this.escalation2FunctionaryAssigneddate = escalation2FunctionaryAssigneddate;
	}

	public long getEscalation2FunctionarySLADays() {
		return escalation2FunctionarySLADays;
	}

	public void setEscalation2FunctionarySLADays(
			long escalation2FunctionarySLADays) {
		this.escalation2FunctionarySLADays = escalation2FunctionarySLADays;
	}

	public double getEscalation2FunctionaryAgeingFromDue() {
		return escalation2FunctionaryAgeingFromDue;
	}

	public void setEscalation2FunctionaryAgeingFromDue(
			double escalation2FunctionaryAgeingFromDue) {
		this.escalation2FunctionaryAgeingFromDue = escalation2FunctionaryAgeingFromDue;
	}

	public char getEscalation2FunctionaryIsSLA() {
		return escalation2FunctionaryIsSLA;
	}

	public void setEscalation2FunctionaryIsSLA(char escalation2FunctionaryIsSLA) {
		this.escalation2FunctionaryIsSLA = escalation2FunctionaryIsSLA;
	}

	public int getEscalation2FunctionaryIfSLA() {
		return escalation2FunctionaryIfSLA;
	}

	public void setEscalation2FunctionaryIfSLA(int escalation2FunctionaryIfSLA) {
		this.escalation2FunctionaryIfSLA = escalation2FunctionaryIfSLA;
	}

	public String getEscalation3FunctionaryName() {
		return escalation3FunctionaryName;
	}

	public void setEscalation3FunctionaryName(String escalation3FunctionaryName) {
		this.escalation3FunctionaryName = escalation3FunctionaryName;
	}

	public Date getEscalation3FunctionaryAssigneddate() {
		return escalation3FunctionaryAssigneddate;
	}

	public void setEscalation3FunctionaryAssigneddate(
			Date escalation3FunctionaryAssigneddate) {
		this.escalation3FunctionaryAssigneddate = escalation3FunctionaryAssigneddate;
	}

	public long getEscalation3FunctionarySLADays() {
		return escalation3FunctionarySLADays;
	}

	public void setEscalation3FunctionarySLADays(
			long escalation3FunctionarySLADays) {
		this.escalation3FunctionarySLADays = escalation3FunctionarySLADays;
	}

	public double getEscalation3FunctionaryAgeingFromDue() {
		return escalation3FunctionaryAgeingFromDue;
	}

	public void setEscalation3FunctionaryAgeingFromDue(
			double escalation3FunctionaryAgeingFromDue) {
		this.escalation3FunctionaryAgeingFromDue = escalation3FunctionaryAgeingFromDue;
	}

	public char getEscalation3FunctionaryIsSLA() {
		return escalation3FunctionaryIsSLA;
	}

	public void setEscalation3FunctionaryIsSLA(char escalation3FunctionaryIsSLA) {
		this.escalation3FunctionaryIsSLA = escalation3FunctionaryIsSLA;
	}

	public int getEscalation3FunctionaryIfSLA() {
		return escalation3FunctionaryIfSLA;
	}

	public void setEscalation3FunctionaryIfSLA(int escalation3FunctionaryIfSLA) {
		this.escalation3FunctionaryIfSLA = escalation3FunctionaryIfSLA;
	}

	public int getEscalationLevel() {
		return escalationLevel;
	}

	public void setEscalationLevel(int escalationLevel) {
		this.escalationLevel = escalationLevel;
	}
	
	public Date getComplaintReOpenedDate() {
		return complaintReOpenedDate;
	}

	public void setComplaintReOpenedDate(Date complaintReOpenedDate) {
		this.complaintReOpenedDate = complaintReOpenedDate;
	}

	public String getReasonForRejection() {
		return reasonForRejection;
	}

	public void setReasonForRejection(String reasonForRejection) {
		this.reasonForRejection = reasonForRejection;
	}
	
	public char getComplaintIsClosed() {
		return complaintIsClosed;
	}

	public void setComplaintIsClosed(char complaintIsClosed) {
		this.complaintIsClosed = complaintIsClosed;
	}

	public int getRegistered() {
		return registered;
	}

	public void setRegistered(int registered) {
		this.registered = registered;
	}

	public int getInProcess() {
		return inProcess;
	}

	public void setInProcess(int inProcess) {
		this.inProcess = inProcess;
	}

	public int getAddressed() {
		return addressed;
	}

	public void setAddressed(int addressed) {
		this.addressed = addressed;
	}

	public int getRejected() {
		return rejected;
	}

	public void setRejected(int rejected) {
		this.rejected = rejected;
	}

	public int getReOpened() {
		return reOpened;
	}

	public void setReOpened(int reOpened) {
		this.reOpened = reOpened;
	}

	public Filters searchFilters() {
        final List<Filter> andFilters = new ArrayList<>();
        andFilters.add(termsStringFilter("clauses.crn", this.getCrn()));
        return Filters.withAndFilters(andFilters);
    }
}