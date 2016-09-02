/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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
import static org.egov.council.utils.constants.CouncilConstants.APPROVED;
import static org.egov.council.utils.constants.CouncilConstants.COUNCILMEETING;
import static org.egov.council.utils.constants.CouncilConstants.MEETING_TIMINGS;
import static  org.egov.infra.web.utils.WebUtils.toJSON;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.council.autonumber.CouncilMeetingNumberGenerator;
import org.egov.council.entity.CommitteeType;
import org.egov.council.entity.CouncilAgenda;
import org.egov.council.entity.CouncilAgendaDetails;
import org.egov.council.entity.CouncilMeeting;
import org.egov.council.entity.MeetingAttendence;
import org.egov.council.entity.MeetingMOM;
import org.egov.council.service.CommitteeTypeService;
import org.egov.council.service.CouncilAgendaService;
import org.egov.council.service.CouncilMeetingService;
import org.egov.council.service.CouncilSmsAndEmailService;
import org.egov.council.web.adaptor.CouncilMeetingJsonAdaptor;
import org.egov.council.web.adaptor.MeetingAttendanceJsonAdaptor;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping("/councilmeeting")
public class CouncilMeetingController {
	
	 //private static final Logger LOGGER = Logger.getLogger(CouncilMeetingController.class);
	 
    private final static String COUNCILMEETING_NEW = "councilmeeting-new";
    private final static String COMMONERRORPAGE = "common-error-page";
    private final static String COUNCILMEETING_RESULT = "councilmeeting-result";
    private final static String COUNCILMEETING_EDIT = "councilmeeting-edit";
    private final static String COUNCILMEETING_VIEW = "councilmeeting-view";
    private final static String COUNCILMEETING_SEARCH = "councilmeeting-search";
    private final static String COUNCIL_MEETING_AGENDA_SEARCH = "councilmeetingAgenda-search";
    private final static String COUNCILMEETING_ATTENDANCE_SEARCH = "councilmeeting-attendsearch";
    private final static String COUNCILMEETING_ATTENDANCE_SEARCH_RESULT = "councilmeeting-attendsearch-view";
    private final static String COUNCILMEETING_SEND_SMS_EMAIL = "councilmeetingsearch-tosendsms-email";

	@Autowired
	private CouncilMeetingService councilMeetingService;
	@Autowired
	private EgwStatusHibernateDAO egwStatusHibernateDAO;
	@Autowired
	private CouncilAgendaService councilAgendaService;
	@Autowired
	private AutonumberServiceBeanResolver autonumberServiceBeanResolver;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CommitteeTypeService committeeTypeService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private CouncilSmsAndEmailService councilSmsAndEmailService;
	

	public @ModelAttribute("committeeType") List<CommitteeType> getCommitteTypeList() {
		return committeeTypeService.getActiveCommiteeType();
	}

	public @ModelAttribute("meetingTimingMap") LinkedHashMap<String, String> getMeetingTimingList() {
		return MEETING_TIMINGS;
	}

	public @ModelAttribute("departmentList") List<Department> getDepartmentList() {
		return departmentService.getAllDepartments();
	}

	@RequestMapping(value = "/new/{id}", method = RequestMethod.GET)
	public String newForm(@ModelAttribute final CouncilMeeting councilMeeting, @PathVariable("id") final Long id,
			final Model model) {

		CouncilAgenda councilAgenda = councilAgendaService.findOne(id);
		model.addAttribute("councilMeeting", councilMeeting);
		if (councilAgenda != null) {
			// TODO: CHECK AGENDA STATUS. THROW ERROR IF AGENDA ALREADY USED IN
			// MEETING.
			councilMeeting.setCommitteeType(councilAgenda.getCommitteeType());
			buildMeetingMomByUsingAgendaDetails(councilMeeting, councilAgenda);

		} else {
			model.addAttribute("message", "msg.invalid.agenda.details");
			return COMMONERRORPAGE;
		}

		return COUNCILMEETING_NEW;
	}

    private void buildMeetingMomByUsingAgendaDetails(final CouncilMeeting councilMeeting, CouncilAgenda councilAgenda) {
        for (CouncilAgendaDetails councilAgendaDetail : councilAgenda.getAgendaDetails()) {
            MeetingMOM meetingMom = new MeetingMOM();
            meetingMom.setMeeting(councilMeeting);
            meetingMom.setAgenda(councilAgendaDetail.getAgenda());
            meetingMom.setPreamble(councilAgendaDetail.getPreamble());

            councilMeeting.addMeetingMoms(meetingMom);
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CouncilMeeting councilMeeting, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (councilMeeting.getStatus() == null)
            councilMeeting.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(COUNCILMEETING, APPROVED));

        if (errors.hasErrors()) {
            return COUNCILMEETING_NEW;
        }
        CouncilMeetingNumberGenerator meetingNumberGenerator = autonumberServiceBeanResolver
                .getAutoNumberServiceFor(CouncilMeetingNumberGenerator.class);
        councilMeeting.setMeetingNumber(meetingNumberGenerator.getNextNumber(councilMeeting));

        for (MeetingMOM meetingMom : councilMeeting.getMeetingMOMs()) {
            meetingMom.setMeeting(councilMeeting);
            meetingMom.getAgenda()
                    .setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(AGENDA_MODULENAME, AGENDAUSEDINMEETING));
        }

        councilMeetingService.create(councilMeeting); 
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilMeeting.success", null, null));
        return "redirect:/councilmeeting/result/" + councilMeeting.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model, final HttpServletResponse response)
            throws IOException {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        model.addAttribute("councilMeeting", councilMeeting);
        
        return COUNCILMEETING_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CouncilMeeting councilMeeting, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            return COUNCILMEETING_EDIT;
        }
        councilMeetingService.update(councilMeeting);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilMeeting.success", null, null));
        return "redirect:/councilmeeting/result/" + councilMeeting.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        model.addAttribute("councilMeeting", councilMeeting);
        return COUNCILMEETING_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        model.addAttribute("councilMeeting", councilMeeting);
        return COUNCILMEETING_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        CouncilMeeting councilMeeting = new CouncilMeeting();
        model.addAttribute("councilMeeting", councilMeeting);
        return COUNCILMEETING_SEARCH;

    }

    @RequestMapping(value = "/agendasearch/{mode}", method = RequestMethod.GET)
    public String searchagenda(@PathVariable("mode") final String mode, Model model) {
        model.addAttribute("councilAgenda", new CouncilAgenda());
        return COUNCIL_MEETING_AGENDA_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(@PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CouncilMeeting councilMeeting) {
        List<CouncilMeeting> searchResultList = councilMeetingService.searchMeeting(councilMeeting);
        String result = new StringBuilder("{ \"data\":").append(toJSON(searchResultList,CouncilMeeting.class,  CouncilMeetingJsonAdaptor.class)).append("}")
                .toString();
        return result;
    }
    
    @RequestMapping(value = "/searchmeeting-tocreatemom", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchMeetingAndToCreateMOM( Model model,
            @ModelAttribute final CouncilMeeting councilMeeting) {
        List<CouncilMeeting> searchResultList = councilMeetingService.searchMeetingToCreateMOM(councilMeeting);
         String result = new StringBuilder("{ \"data\":").append(toJSON(searchResultList,CouncilMeeting.class,  CouncilMeetingJsonAdaptor.class)).append("}")
                .toString();
        return result;
    }
    
    @RequestMapping(value = "/viewsmsemail", method = RequestMethod.GET)
    public String retrieveSmsAndEmailDetailsForCouncilMeeting(final Model model) {
        CouncilMeeting councilMeeting = new CouncilMeeting();
        model.addAttribute("councilMeeting", councilMeeting);
        model.addAttribute("mode", "view");
        return COUNCILMEETING_SEND_SMS_EMAIL;
    }
    
    @RequestMapping(value = "/sendsmsemail", method = RequestMethod.GET)
    public @ResponseBody String sendSmsAndEmailDetailsForCouncilMeeting(@RequestParam("id") Long id,
            @RequestParam("msg") String msg,final Model model) {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        councilSmsAndEmailService.sendSms(councilMeeting,msg);
        //councilSmsAndEmailService.sendEmail(councilMeeting,msg,attachment);
        String result = new StringBuilder("{ \"success\":true }").toString();
        return result;
    }

    @RequestMapping(value = "/attendance/search", method = RequestMethod.GET)
    public String getSearchAttendance(final Model model) {
        CouncilMeeting councilMeeting = new CouncilMeeting();
        model.addAttribute("councilMeeting", councilMeeting);
        model.addAttribute("mode", "view");
        return COUNCILMEETING_ATTENDANCE_SEARCH;
    }

    @RequestMapping(value = "/attendance/report/search", method = RequestMethod.GET)
    public String getSearchReportForAttendance(final Model model) {
        CouncilMeeting councilMeeting = new CouncilMeeting();
        model.addAttribute("councilMeeting", councilMeeting);
        model.addAttribute("mode", "view");
        return COUNCILMEETING_ATTENDANCE_SEARCH;
    }

    @RequestMapping(value = "/attendance/search/view/{id}", method = RequestMethod.GET)
    public String searchAttendanceResult(@PathVariable("id") final CouncilMeeting councilMeeting, Model model) {
        model.addAttribute("id", councilMeeting.getId());
        model.addAttribute("currDate", new Date());
        return COUNCILMEETING_ATTENDANCE_SEARCH_RESULT;
    }

    @RequestMapping(value = "/attendance/ajaxsearch/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearc(@PathVariable("id") final CouncilMeeting id, Model model) {
        List<MeetingAttendence> searchResultList = councilMeetingService.findListOfAttendance(id);
       String result = new StringBuilder("{ \"data\":").append(toJSON(searchResultList,MeetingAttendence.class,  MeetingAttendanceJsonAdaptor.class)).append("}")
                .toString();
        return result;
    }


}