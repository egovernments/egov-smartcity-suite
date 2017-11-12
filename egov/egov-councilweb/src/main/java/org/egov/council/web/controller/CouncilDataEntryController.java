/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
package org.egov.council.web.controller;

import static org.egov.council.utils.constants.CouncilConstants.AGENDAUSEDINMEETING;
import static org.egov.council.utils.constants.CouncilConstants.AGENDA_MODULENAME;
import static org.egov.council.utils.constants.CouncilConstants.MEETING_MODULENAME;
import static org.egov.council.utils.constants.CouncilConstants.MEETING_TIMINGS;
import static org.egov.council.utils.constants.CouncilConstants.MOM_FINALISED;
import static org.egov.council.utils.constants.CouncilConstants.PREAMBLE_MODULENAME;
import static org.egov.council.utils.constants.CouncilConstants.RESOLUTION_APPROVED_PREAMBLE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.council.entity.CommitteeType;
import org.egov.council.entity.CouncilAgendaDetails;
import org.egov.council.entity.CouncilMeeting;
import org.egov.council.entity.CouncilPreamble;
import org.egov.council.entity.MeetingMOM;
import org.egov.council.entity.enums.PreambleType;
import org.egov.council.service.CommitteeTypeService;
import org.egov.council.service.CouncilMeetingService;
import org.egov.council.service.CouncilPreambleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/councilmom")
public class CouncilDataEntryController {
    
    private static final String COUNCIL_MEETING = "councilMeeting";
    private static final String COUNCILMOM_DATAENTRY = "councilMom-dataentry";
    private static final String COUNCILMOM_VIEW = "councilmom-view";
    private static final String MEETING_MOM = "MeetingMOM";

    @Autowired
    private CommitteeTypeService committeeTypeService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private CouncilMeetingService councilMeetingService;

    @Autowired
    private CouncilPreambleService councilPreambleService;

    @ModelAttribute("committeeType")
    public List<CommitteeType> getCommitteTypeList() {
        return committeeTypeService.getActiveCommiteeType();
    }

    @ModelAttribute("meetingTimingMap")
    public Map<String, String> getMeetingTimingList() {
        return MEETING_TIMINGS;
    }

    @RequestMapping(value = "/createdataentry", method = RequestMethod.GET)
    public String showCouncilForm(final Model model) {
        MeetingMOM meetingMOM = new MeetingMOM();
        model.addAttribute(MEETING_MOM, meetingMOM);
        return COUNCILMOM_DATAENTRY;
    }

    @RequestMapping(value = "/savedataentry", method = RequestMethod.POST)
    public String update(
            @ModelAttribute final MeetingMOM meetingMOM,
            final BindingResult errors, final Model model,
            final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            return COUNCILMOM_DATAENTRY;
        }
        List<MeetingMOM> meetingMOMList = new ArrayList<>();
        List<CouncilAgendaDetails> preambleList = new ArrayList<>();
        for (MeetingMOM meetingMoMs : meetingMOM.getMeeting().getMeetingMOMs()) {
            meetingMoMs.getPreamble().setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(
                    PREAMBLE_MODULENAME, RESOLUTION_APPROVED_PREAMBLE));
            meetingMoMs.getPreamble().setType(PreambleType.GENERAL);
            meetingMoMs.setMeeting(meetingMOM.getMeeting());
            meetingMoMs.getMeeting().setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(MEETING_MODULENAME, MOM_FINALISED));
            meetingMoMs.setAgenda(meetingMOM.getAgenda());
            meetingMoMs.getAgenda().setCommitteeType(meetingMOM.getMeeting().getCommitteeType());
            meetingMoMs.getAgenda()
                    .setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(AGENDA_MODULENAME, AGENDAUSEDINMEETING));
            meetingMoMs.setLegacy(true);
            CouncilAgendaDetails councilAgendaDetails = new CouncilAgendaDetails();
            councilAgendaDetails.setAgenda(meetingMOM.getAgenda());
            councilAgendaDetails.setPreamble(meetingMoMs.getPreamble());
            councilAgendaDetails.setItemNumber(meetingMoMs.getItemNumber());
            preambleList.add(councilAgendaDetails);
            meetingMOMList.add(meetingMoMs);
        }
        meetingMOM.getAgenda().setAgendaDetails(preambleList);
        councilMeetingService.createDataEntry(meetingMOMList);
        CouncilMeeting councilMeeting = councilMeetingService.findOne(meetingMOM.getMeeting().getId());
        councilMeetingService.sortMeetingMomByItemNumber(councilMeeting);
        model.addAttribute(COUNCIL_MEETING, councilMeeting);
        return COUNCILMOM_VIEW;
    }

    /**
     * @param meetingNumber
     * @return
     */
    @RequestMapping(value = "/checkUnique-MeetingNo", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean uniqueMeetingNumber(@RequestParam final String meetingNumber) {
        CouncilMeeting councilMeeting = councilMeetingService.findByMeetingNumber(meetingNumber);
        return councilMeeting != null ? false : true;
    }

    /**
     * @param preambleNumber
     * @return
     */
    @RequestMapping(value = "/checkUnique-preambleNo", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean uniquePreambleNumber(@RequestParam final String preambleNumber) {
        CouncilPreamble councilPreamble = councilPreambleService.findbyPreambleNumber(preambleNumber);
        return councilPreamble != null ? false : true;
    }

    /**
     * @param resolutionNumber
     * @return
     */
    @RequestMapping(value = "/checkUnique-resolutionNo", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean uniqueResolutionNumber(@RequestParam final String resolutionNumber) {
        MeetingMOM meetingMOM = councilMeetingService.findByResolutionNumber(resolutionNumber);
        return meetingMOM != null ? false : true;
    }
}
