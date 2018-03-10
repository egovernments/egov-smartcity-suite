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

package org.egov.council.service;

import org.apache.commons.lang.StringUtils;
import org.egov.council.entity.CouncilSequenceNumber;
import org.egov.council.repository.CouncilSequenceNumberRepository;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;

@Service
@Transactional(readOnly = true)
public class CouncilSequenceGenerationService {

    @Autowired
    private CouncilSequenceNumberRepository councilSequenceNumberRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Transactional
    public CouncilSequenceNumber create(CouncilSequenceNumber councilSequenceNumber) {
        councilSequenceNumberRepository.save(councilSequenceNumber);
        return councilSequenceNumber;
    }

    @Transactional
    public void update(String sequenceName, String seqnumber) throws SQLException {
        int seqnum = Integer.parseInt(seqnumber) + 1;
        final String sql = "alter sequence " + sequenceName + " restart " + seqnum;
        Query query = entityManager.unwrap(Session.class).createSQLQuery(sql);
        query.executeUpdate();
    }

    @Transactional
    public void updatesequences(CouncilSequenceNumber councilSequenceNumber) throws SQLException {
        String preamblesequence = "seq_egcncl_preamble_number";
        String agendasequence = "seq_egcncl_agenda_number";
        String resolutionsequence = "seq_egcncl_mom_number";
        String meetingsequence = "seq_egcncl_meeting_number";

        if (councilSequenceNumber.getPreambleSeqNumber() != null)
            update(preamblesequence, councilSequenceNumber.getPreambleSeqNumber());

        if (councilSequenceNumber.getAgendaSeqNumber() != null)
            update(agendasequence, councilSequenceNumber.getAgendaSeqNumber());

        if (councilSequenceNumber.getResolutionSeqNumber() != null)
            update(resolutionsequence, councilSequenceNumber.getResolutionSeqNumber());
        
        if (councilSequenceNumber.getMeetingSeqNumber() != null)
            update(meetingsequence, councilSequenceNumber.getMeetingSeqNumber());
    }

    @Transactional
    public String getPreambleLastSeq() {
        String sql = "select last_value from seq_egcncl_preamble_number ";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        return query.getSingleResult() != null ? query.getSingleResult().toString() : StringUtils.EMPTY;
    }

    @Transactional
    public String getAgendaLastSeq() {
        String sql = "select last_value from seq_egcncl_agenda_number";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        return query.getSingleResult() != null ? query.getSingleResult().toString() : StringUtils.EMPTY;
    }

    @Transactional
    public String getresolutionsequence() {
        String sql = "select last_value from seq_egcncl_mom_number";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        return query.getSingleResult() != null ? query.getSingleResult().toString() : StringUtils.EMPTY;
    }
   
    @Transactional
    public String getMeetingSeqNumber() {
        String sql = "select last_value from seq_egcncl_meeting_number";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        return query.getSingleResult() != null ? query.getSingleResult().toString() : StringUtils.EMPTY;
    }

    public void validate(final Errors error, final CouncilSequenceNumber councilSequenceNumber, String preambleseq,
            String resolutionseq, String agendaSeq,String meetingSeq) {
        if (councilSequenceNumber.getPreambleSeqNumber() != null
                && Integer.valueOf(councilSequenceNumber.getPreambleSeqNumber()).compareTo(Integer.valueOf(preambleseq)) <= 0) {
            error.rejectValue("preambleSeqNumber", "err.preamble.sequence");
        }
        if (councilSequenceNumber.getAgendaSeqNumber() != null
                && Integer.valueOf(councilSequenceNumber.getAgendaSeqNumber()).compareTo(Integer.valueOf(agendaSeq)) <= 0) {
            error.rejectValue("agendaSeqNumber", "err.agenda.sequence");
        }
        if (councilSequenceNumber.getResolutionSeqNumber() != null && Integer
                .valueOf(councilSequenceNumber.getResolutionSeqNumber()).compareTo(Integer.valueOf(resolutionseq)) <= 0) {
            error.rejectValue("resolutionSeqNumber", "err.resolution.sequence");
        }
        if (councilSequenceNumber.getMeetingSeqNumber() != null && Integer
                .valueOf(councilSequenceNumber.getMeetingSeqNumber()).compareTo(Integer.valueOf(meetingSeq)) <= 0) {
            error.rejectValue("meetingSeqNumber", "err.meeting.sequence");
        }
    }
}
