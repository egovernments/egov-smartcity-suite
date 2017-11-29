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
package org.egov.council.entity;

import org.egov.commons.EgwStatus;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "egcncl_meeting_mom")
@SequenceGenerator(name = MeetingMOM.SEQ_MEETINGMOM, sequenceName = MeetingMOM.SEQ_MEETINGMOM, allocationSize = 1)
public class MeetingMOM implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String SEQ_MEETINGMOM = "seq_egcncl_meetingMom";

    @Id
    @GeneratedValue(generator = SEQ_MEETINGMOM, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "meeting", nullable = false)
    private CouncilMeeting meeting;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "agenda")
    private CouncilAgenda agenda;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "preamble", nullable = false)
    private CouncilPreamble preamble;

    @Column(name = "resolutionDetail")
    private String resolutionDetail;

    @ManyToOne
    @JoinColumn(name = "resolutionStatus")
    private EgwStatus resolutionStatus;

    @Column(name = "itemnumber")
    private String itemNumber;
    @Column(name = "resolutionNumber")
    private String resolutionNumber;
    
    private boolean isLegacy;

    public CouncilMeeting getMeeting() {
        return meeting;
    }

    public void setMeeting(CouncilMeeting meeting) {
        this.meeting = meeting;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CouncilAgenda getAgenda() {
        return agenda;
    }

    public void setAgenda(CouncilAgenda agenda) {
        this.agenda = agenda;
    }

    public CouncilPreamble getPreamble() {
        return preamble;
    }

    public void setPreamble(CouncilPreamble preamble) {
        this.preamble = preamble;
    }

    public String getResolutionDetail() {
        return resolutionDetail;
    }

    public void setResolutionDetail(String resolutionDetail) {
        this.resolutionDetail = resolutionDetail;
    }

    public EgwStatus getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(EgwStatus resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }

    public String getResolutionNumber() {
        return resolutionNumber;
    }

    public void setResolutionNumber(String resolutionNumber) {
        this.resolutionNumber = resolutionNumber;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public boolean isLegacy() {
        return isLegacy;
    }

    public void setLegacy(boolean isLegacy) {
        this.isLegacy = isLegacy;
    }

}
