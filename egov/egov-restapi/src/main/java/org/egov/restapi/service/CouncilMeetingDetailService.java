/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *      Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *      For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *      For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.restapi.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.council.entity.CouncilMeeting;
import org.egov.council.entity.MeetingMOM;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.restapi.model.CouncilAgendaItems;
import org.egov.restapi.model.CouncilMeetingDetails;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouncilMeetingDetailService {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<CouncilMeeting> getMeetingDetailsByMeetingNo(String meetingNumber,
            String meetingType) {

        final StringBuilder queryString = new StringBuilder();
        queryString
                .append(" from CouncilMeeting C where C.committeeType.isActive=true");
        if (meetingNumber != null)
            queryString.append(" and C.meetingNumber =:meetingNumber ");
        if (meetingType != null)
            queryString.append(" and C.committeeType.name =:meetingType ");
        final Query query = entityManager.unwrap(Session.class).createQuery(queryString.toString());

        if (meetingNumber != null)
            query.setParameter("meetingNumber", meetingNumber);
        if (meetingType != null)
            query.setParameter("meetingType", meetingType);
        return query.list();
    }

    public List<CouncilMeetingDetails> getCouncilMeetingDetailsList(List<CouncilMeeting> councilMeetingList) {
        final List<CouncilMeetingDetails> councilMeetingDetailsList = new ArrayList<>();
        for (final CouncilMeeting councilMeeting : councilMeetingList) {
            final CouncilMeetingDetails councilMeetingDetails = new CouncilMeetingDetails();
            councilMeetingDetails.setMeetingDate(DateUtils.getDefaultFormattedDate(councilMeeting.getMeetingDate()));
            councilMeetingDetails.setMeetingNo(councilMeeting.getMeetingNumber());
            councilMeetingDetails.setMeetingPlace(councilMeeting.getMeetingLocation());
            councilMeetingDetails.setMeetingTime(councilMeeting.getMeetingTime());
            councilMeetingDetails.setMeetingType(councilMeeting.getCommitteeType().getName());
            final List<MeetingMOM> meetingMomList = councilMeeting.getMeetingMOMs();
            final List<CouncilAgendaItems> councilAgendaItemsList = new ArrayList<>();
            for (final MeetingMOM meetingMOM : meetingMomList) {
                final CouncilAgendaItems councilAgendaItems = new CouncilAgendaItems();
                councilAgendaItems.setAgendaNo(meetingMOM.getAgenda().getAgendaNumber());
                councilAgendaItems.setDepartment(meetingMOM.getPreamble().getDepartment().getName());
                councilAgendaItems.setGistOfPreamble(meetingMOM.getPreamble().getGistOfPreamble());
                councilAgendaItems.setSerialNo(meetingMOM.getItemNumber());
                councilAgendaItems.setPreambleNo(meetingMOM.getPreamble().getPreambleNumber());
                councilAgendaItems.setResolution(
                        meetingMOM.getResolutionDetail() != null ? meetingMOM.getResolutionDetail()
                                : StringUtils.EMPTY);
                councilAgendaItems.setResolutionNo(
                        meetingMOM.getResolutionNumber() != null ? meetingMOM.getResolutionNumber()
                                : StringUtils.EMPTY);
                councilAgendaItems.setStatus(meetingMOM.getResolutionStatus() != null ? meetingMOM.getResolutionStatus().getCode()
                        : StringUtils.EMPTY);
                councilAgendaItems.setSanctionAmount(meetingMOM.getPreamble().getSanctionAmount() != null
                        ? meetingMOM.getPreamble().getSanctionAmount() : BigDecimal.ZERO);
                councilAgendaItemsList.add(councilAgendaItems);

            }
            councilMeetingDetails.setAgendaItems(councilAgendaItemsList);
            councilMeetingDetailsList.add(councilMeetingDetails);
        }
        return councilMeetingDetailsList;
    }
}
