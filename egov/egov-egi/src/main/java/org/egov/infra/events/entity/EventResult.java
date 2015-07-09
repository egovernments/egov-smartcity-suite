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
package org.egov.infra.events.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.hibernate.search.annotations.DocumentId;

@Entity
@Table(name = "eg_event_result")
@SequenceGenerator(name = EventResult.SEQ_EVENTRESULT, sequenceName = EventResult.SEQ_EVENTRESULT, allocationSize = 1)
public class EventResult extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 5662966690272607421L;

    public static final String SEQ_EVENTRESULT = "SEQ_EG_EVENT_RESULT";

    @DocumentId
    @Id
    @GeneratedValue(generator = SEQ_EVENTRESULT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "module")
    private String module;

    @Column(name = "event_code")
    private String eventCode;

    @Column(name = "date_raised")
    private Date dateRaised;

    @Column(name = "result")
    private String result;

    @Column(name = "timeofprocessing")
    private Date timeOfProcessing;

    @Column(name = "details")
    private String details;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(final String module) {
        this.module = module;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(final String eventCode) {
        this.eventCode = eventCode;
    }

    public Date getDateRaised() {
        return dateRaised;
    }

    public void setDateRaised(final Date dateRaised) {
        this.dateRaised = dateRaised;
    }

    public String getResult() {
        return result;
    }

    public void setResult(final String result) {
        this.result = result;
    }

    public Date getTimeOfProcessing() {
        return timeOfProcessing;
    }

    public void setTimeOfProcessing(final Date timeOfProcessing) {
        this.timeOfProcessing = timeOfProcessing;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

}