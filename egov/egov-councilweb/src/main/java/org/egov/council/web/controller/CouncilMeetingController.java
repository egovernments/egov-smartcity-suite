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
import static org.egov.council.utils.constants.CouncilConstants.ATTENDANCEFINALIZED;
import static org.egov.council.utils.constants.CouncilConstants.COUNCILMEETING;
import static org.egov.council.utils.constants.CouncilConstants.MEETINGRESOLUTIONFILENAME;
import static org.egov.council.utils.constants.CouncilConstants.MEETING_MODULENAME;
import static org.egov.council.utils.constants.CouncilConstants.MEETING_TIMINGS;
import static org.egov.council.utils.constants.CouncilConstants.MODULE_NAME;
import static org.egov.council.utils.constants.CouncilConstants.MOM_FINALISED;
import static org.egov.infra.utils.JsonUtils.toJSON;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.council.autonumber.CouncilMeetingNumberGenerator;
import org.egov.council.entity.CommitteeMembers;
import org.egov.council.entity.CommitteeType;
import org.egov.council.entity.CouncilAgenda;
import org.egov.council.entity.CouncilAgendaDetails;
import org.egov.council.entity.CouncilMeeting;
import org.egov.council.entity.MeetingAttendence;
import org.egov.council.entity.MeetingMOM;
import org.egov.council.service.CommitteeTypeService;
import org.egov.council.service.CouncilAgendaService;
import org.egov.council.service.CouncilCommitteeMemberService;
import org.egov.council.service.CouncilMeetingService;
import org.egov.council.service.CouncilReportService;
import org.egov.council.service.CouncilSmsAndEmailService;
import org.egov.council.utils.constants.CouncilConstants;
import org.egov.council.web.adaptor.CouncilMeetingJsonAdaptor;
import org.egov.council.web.adaptor.MeetingAttendanceJsonAdaptor;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.web.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    private static final String APPLICATION_PDF = "application/pdf";
    private static final String DATA = "{ \"data\":";
    private static final String MSG_ATTENDANCE_ALREADY_FINALIZD = "msg.attendance.already.finalizd";
    private static final String CITYLOGO = "citylogo";
    private static final String COUNCIL_MEETING = "councilMeeting";
    private static final String MESSAGE = "message";
    private static final String COUNCILMEETING_NEW = "councilmeeting-new";
    private static final String COMMONERRORPAGE = "common-error-page";
    private static final String COUNCILMEETING_RESULT = "councilmeeting-result";
    private static final String COUNCILMEETING_EDIT = "councilmeeting-edit";
    private static final String COUNCILMEETING_VIEW = "councilmeeting-view";
    private static final String COUNCILMEETING_SEARCH = "councilmeeting-search";
    private static final String COUNCIL_MEETING_AGENDA_SEARCH = "councilmeetingAgenda-search";
    private static final String COUNCILMEETING_ATTENDANCE_SEARCH = "councilmeeting-attendsearch";
    private static final String COUNCILMEETING_ATTENDANCE_VIEW = "councilmeeting-attendsearch-view";
    private static final String COUNCILMEETING_SEND_SMS_EMAIL = "councilmeetingsearch-tosendsms-email";
    private static final String COUNCILMEETING_EDIT_ATTENDANCE = "councilmeeting-attend-form";
    private static final String COUNCILMEETING_ATTENDANCE_RESULT = "councilmeeting-attend-result";

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
    @Autowired
    private CouncilReportService councilReportService;
    @Autowired
    protected FileStoreUtils fileStoreUtils;
    @Qualifier("fileStoreService")
    @Autowired
    private FileStoreService fileStoreService;
    @Autowired
    private CouncilCommitteeMemberService committeeMemberService;

    @ModelAttribute("committeeType")
    public List<CommitteeType> getCommitteTypeList() {
        return committeeTypeService.getActiveCommiteeType();
    }

    @ModelAttribute("meetingTimingMap")
    public Map<String, String> getMeetingTimingList() {
        return MEETING_TIMINGS;
    }

    @ModelAttribute("departments")
    public List<Department> getDepartmentList() {
        return departmentService.getAllDepartments();
    }

    @RequestMapping(value = "/new/{id}", method = RequestMethod.GET)
    public String newForm(@ModelAttribute final CouncilMeeting councilMeeting, @PathVariable("id") final Long id,
            final Model model) {

        CouncilAgenda councilAgenda = councilAgendaService.findOne(id);
        model.addAttribute(COUNCIL_MEETING, councilMeeting);
        if (councilAgenda != null && AGENDAUSEDINMEETING.equals(councilAgenda.getStatus().getCode())) {
            model.addAttribute(MESSAGE, "msg.agenda.exist");
            return COMMONERRORPAGE;
        } else if (councilAgenda != null) {
            councilMeeting.setCommitteeType(councilAgenda.getCommitteeType());
            buildMeetingMomByUsingAgendaDetails(councilMeeting, councilAgenda);
            return COUNCILMEETING_NEW;

        } else {
            model.addAttribute(MESSAGE, "msg.invalid.agenda.details");
            return COMMONERRORPAGE;
        }

    }

    private void buildMeetingMomByUsingAgendaDetails(final CouncilMeeting councilMeeting, CouncilAgenda councilAgenda) {
        Long itemNumber = Long.valueOf(1);
        for (CouncilAgendaDetails councilAgendaDetail : councilAgenda.getAgendaDetails()) {
            MeetingMOM meetingMom = new MeetingMOM();
            meetingMom.setMeeting(councilMeeting);
            meetingMom.setAgenda(councilAgendaDetail.getAgenda());
            meetingMom.setPreamble(councilAgendaDetail.getPreamble());
            meetingMom.setItemNumber(itemNumber.toString());
            itemNumber++;
            councilMeeting.addMeetingMoms(meetingMom);
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CouncilMeeting councilMeeting, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs, final HttpServletRequest request) {
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
        councilSmsAndEmailService.sendSms(councilMeeting, null);
        final String url = WebUtils.extractRequestDomainURL(request, false);
        String logoPath = url.concat(ReportConstants.IMAGE_CONTEXT_PATH).concat(
                (String) request.getSession().getAttribute(CITYLOGO));
        councilSmsAndEmailService.sendEmail(councilMeeting, null,
                councilReportService.generatePDFForAgendaDetails(councilMeeting, logoPath));
        redirectAttrs.addFlashAttribute(MESSAGE, messageSource.getMessage("msg.councilMeeting.success", null, null));
        return "redirect:/councilmeeting/result/" + councilMeeting.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model, final HttpServletResponse response)
            throws IOException {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        sortMeetingMomByItemNumber(councilMeeting);
        model.addAttribute(COUNCIL_MEETING, councilMeeting);

        return COUNCILMEETING_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CouncilMeeting councilMeeting, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            return COUNCILMEETING_EDIT;
        }
        councilMeetingService.update(councilMeeting);
        redirectAttrs.addFlashAttribute(MESSAGE, messageSource.getMessage("msg.councilMeeting.success", null, null));
        return "redirect:/councilmeeting/result/" + councilMeeting.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        sortMeetingMomByItemNumber(councilMeeting);
        model.addAttribute(COUNCIL_MEETING, councilMeeting);
        return COUNCILMEETING_VIEW;
    }

    private void sortMeetingMomByItemNumber(CouncilMeeting councilMeeting) {
        councilMeeting.getMeetingMOMs().sort((MeetingMOM f1, MeetingMOM f2) -> f1.getItemNumber().compareTo(f2.getItemNumber()));
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        model.addAttribute(COUNCIL_MEETING, councilMeeting);
        model.addAttribute("commiteemembelist", councilMeeting.getCommitteeType().getCommiteemembers());
        return COUNCILMEETING_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        CouncilMeeting councilMeeting = new CouncilMeeting();
        model.addAttribute(COUNCIL_MEETING, councilMeeting);
        return COUNCILMEETING_SEARCH;

    }

    @RequestMapping(value = "/agendasearch/{mode}", method = RequestMethod.GET)
    public String searchagenda(@PathVariable("mode") final String mode, Model model) {
        model.addAttribute("councilAgenda", new CouncilAgenda());
        return COUNCIL_MEETING_AGENDA_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearch(@PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CouncilMeeting councilMeeting) {
        if (null != mode && !"".equals(mode)) {
            List<CouncilMeeting> searchResultList;

            if ("edit".equalsIgnoreCase(mode)) {
                searchResultList = councilMeetingService.searchMeetingForEdit(councilMeeting);
            } else {
                searchResultList = councilMeetingService.searchMeeting(councilMeeting);
            }
            return new StringBuilder(DATA)
                    .append(toJSON(searchResultList, CouncilMeeting.class, CouncilMeetingJsonAdaptor.class))
                    .append("}").toString();
        }
        return null;
    }

    @RequestMapping(value = "/searchmeeting-tocreatemom", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchMeetingAndToCreateMOM(Model model,
            @ModelAttribute final CouncilMeeting councilMeeting) {
        List<CouncilMeeting> searchResultList = councilMeetingService.searchMeetingToCreateMOM(councilMeeting);
        return new StringBuilder(DATA).append(toJSON(searchResultList, CouncilMeeting.class, CouncilMeetingJsonAdaptor.class))
                .append("}")
                .toString();
    }

    @RequestMapping(value = "/viewsmsemail", method = RequestMethod.GET)
    public String retrieveSmsAndEmailDetailsForCouncilMeeting(final Model model) {
        CouncilMeeting councilMeeting = new CouncilMeeting();
        model.addAttribute(COUNCIL_MEETING, councilMeeting);
        model.addAttribute("mode", "view");
        return COUNCILMEETING_SEND_SMS_EMAIL;
    }

    @RequestMapping(value = "/sendsmsemail", method = RequestMethod.GET)
    @ResponseBody
    public String sendSmsAndEmailDetailsForCouncilMeeting(@RequestParam("id") Long id,
            @RequestParam("msg") String msg, final Model model, final HttpServletRequest request) {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        councilSmsAndEmailService.sendSms(councilMeeting, msg);
        final String url = WebUtils.extractRequestDomainURL(request, false);
        String logoPath = url.concat(ReportConstants.IMAGE_CONTEXT_PATH).concat(
                (String) request.getSession().getAttribute(CITYLOGO));
        councilSmsAndEmailService.sendEmail(councilMeeting, msg,
                councilReportService.generatePDFForAgendaDetails(councilMeeting, logoPath));
        return new StringBuilder("{ \"success\":true }").toString();
    }

    @RequestMapping(value = "/generateresolution/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> viewDemandNoticeReport(@PathVariable final Long id,
            final HttpSession session, HttpServletRequest request) {

        byte[] reportOutput;
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        final String url = WebUtils.extractRequestDomainURL(request, false);

        String logoPath = url.concat(ReportConstants.IMAGE_CONTEXT_PATH).concat(
                (String) request.getSession().getAttribute(CITYLOGO));
        reportOutput = councilReportService.generatePDFForMom(councilMeeting, logoPath);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(APPLICATION_PDF));
        headers.add("content-disposition", "inline;filename=Resolution.pdf");
        return new ResponseEntity<byte[]>(reportOutput, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/attendance/search", method = RequestMethod.GET)
    public String getSearchAttendance(final Model model) {
        CouncilMeeting councilMeeting = new CouncilMeeting();
        model.addAttribute(COUNCIL_MEETING, councilMeeting);
        model.addAttribute("mode", "view");
        return COUNCILMEETING_ATTENDANCE_SEARCH;
    }

    @RequestMapping(value = "/attendance/report/search", method = RequestMethod.GET)
    public String getSearchReportForAttendance(final Model model) {
        CouncilMeeting councilMeeting = new CouncilMeeting();
        model.addAttribute(COUNCIL_MEETING, councilMeeting);
        model.addAttribute("mode", "view");
        return COUNCILMEETING_ATTENDANCE_SEARCH;
    }

    @RequestMapping(value = "/attendance/search/view/{id}", method = RequestMethod.GET)
    public String viewAttendanceDetails(@PathVariable("id") final CouncilMeeting councilMeeting, Model model) {
        model.addAttribute("id", councilMeeting.getId());
        model.addAttribute("currDate", new Date());
        return COUNCILMEETING_ATTENDANCE_VIEW;
    }

    @RequestMapping(value = "/attendance/result/{id}", method = RequestMethod.GET)
    public String showAttendanceResult(@PathVariable("id") final CouncilMeeting councilMeeting, Model model) {
        model.addAttribute("id", councilMeeting.getId());
        model.addAttribute("currDate", new Date());
        return COUNCILMEETING_ATTENDANCE_RESULT;
    }

    @RequestMapping(value = "/attend/search/edit/{id}", method = RequestMethod.GET)
    public String editAttendance(@PathVariable("id") final CouncilMeeting councilMeeting1, Model model) {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(councilMeeting1.getId());

        if (councilMeeting != null && councilMeeting.getCommitteeType().getCommiteemembers().isEmpty()) {
            model.addAttribute(MESSAGE, "msg.committee.members.not.added");
            return COMMONERRORPAGE;
        }
        if (councilMeeting != null && councilMeeting.getStatus() != null
                && ATTENDANCEFINALIZED.equals(councilMeeting.getStatus().getCode())) {
            model.addAttribute(MESSAGE, MSG_ATTENDANCE_ALREADY_FINALIZD);
            return COMMONERRORPAGE;
        }

        buildAttendanceDetails(councilMeeting);
        sortMeetingMomByItemNumber(councilMeeting);
        model.addAttribute(COUNCIL_MEETING, councilMeeting);
        return COUNCILMEETING_EDIT_ATTENDANCE;
    }

    private void buildAttendanceDetails(CouncilMeeting councilMeeting) {
        if (councilMeeting != null && councilMeeting.getCommitteeType() != null) {
            List<MeetingAttendence> attendencesList = new ArrayList<>();
            List<Long> attendenceIdList = new ArrayList<>();

            for (MeetingAttendence meetingAttendance : councilMeeting.getMeetingAttendence()) {
                if (meetingAttendance.getAttendedMeeting())
                    attendenceIdList.add(meetingAttendance.getCouncilMember().getId());
            }
            for (CommitteeMembers committeeMembers : committeeMemberService
                    .findAllByCommitteTypeMemberIsActive(councilMeeting.getCommitteeType())) {
                MeetingAttendence attendence = new MeetingAttendence();
                attendence.setCouncilMember(committeeMembers.getCouncilMember());
                if (attendenceIdList.indexOf(committeeMembers.getCouncilMember().getId()) > -1) {
                    attendence.setAttendedMeeting(true);
                }
                attendencesList.add(attendence);
            }
            councilMeeting.setUpdateMeetingAttendance(attendencesList);
        }
    }

    @RequestMapping(value = "/attendance/update", method = RequestMethod.POST)
    public String updateAttendance(@Valid @ModelAttribute final CouncilMeeting councilMeeting, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {

        if (councilMeeting != null && councilMeeting.getStatus() != null
                && ATTENDANCEFINALIZED.equals(councilMeeting.getStatus().getCode())) {
            model.addAttribute(MESSAGE, MSG_ATTENDANCE_ALREADY_FINALIZD);
            return COMMONERRORPAGE;
        }
        deleteAtteandance(councilMeeting);
        setAttendanceDetails(councilMeeting);
        if (errors.hasErrors()) {
            return "redirect:councilmeeting/attend/search/edit/" + councilMeeting.getId();
        }
        buildAttendanceDetailsForMeeting(councilMeeting);
        councilMeetingService.update(councilMeeting);
        redirectAttrs.addFlashAttribute(MESSAGE, messageSource.getMessage("msg.councilMeeting.attendance.success", null, null));
        return "redirect:result/" + councilMeeting.getId();
    }

    private void setAttendanceDetails(final CouncilMeeting councilMeeting) {
        councilMeeting.setMeetingAttendence(councilMeeting.getUpdateMeetingAttendance());
    }

    private void deleteAtteandance(final CouncilMeeting councilMeeting) {
        councilMeetingService.deleteAttendance(councilMeeting.getMeetingAttendence());
    }

    @RequestMapping(value = "/attendance/finalizeattendance", method = RequestMethod.POST)
    public String updateFinalizedAttendance(@Valid @ModelAttribute final CouncilMeeting councilMeeting,
            final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {

        if (councilMeeting != null && councilMeeting.getStatus() != null
                && ATTENDANCEFINALIZED.equals(councilMeeting.getStatus().getCode())) {
            model.addAttribute(MESSAGE, MSG_ATTENDANCE_ALREADY_FINALIZD);
            return COMMONERRORPAGE;
        }
        deleteAtteandance(councilMeeting);
        if (councilMeeting != null && councilMeeting.getUpdateMeetingAttendance() != null) {
            setAttendanceDetails(councilMeeting);
        }
        if (errors.hasErrors()) {
            return "redirect:councilmeeting/attend/search/edit/" + councilMeeting.getId();
        }
        buildAttendanceDetailsForMeeting(councilMeeting);
        councilMeeting.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(MEETING_MODULENAME, ATTENDANCEFINALIZED));
        councilMeetingService.update(councilMeeting);
        redirectAttrs.addFlashAttribute(MESSAGE, messageSource.getMessage("msg.councilMeeting.attendance.success", null, null));
        return "redirect:result/" + councilMeeting.getId();
    }

    private void buildAttendanceDetailsForMeeting(final CouncilMeeting councilMeeting) {
        for (MeetingAttendence attendence : councilMeeting
                .getMeetingAttendence()) {
            if (attendence.getChecked() != null && attendence.getChecked()) {
                attendence.setAttendedMeeting(true);
            } else {
                attendence.setAttendedMeeting(false);
            }
        }
    }

    @RequestMapping(value = "/downloadfile/{id}")
    public void download(@PathVariable("id") final Long id, final HttpServletResponse response,
            final HttpServletRequest request) throws IOException {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        if (null != councilMeeting) {
            if (councilMeeting.getFilestore() != null) {
                fetchMeetingResolutionByFileStoreId(response, councilMeeting);
            } else {
                if (MOM_FINALISED.equals(councilMeeting.getStatus().getCode())) {
                    byte[] reportOutput = generatePdfByPassingMeetingObject(request, councilMeeting);

                    if (reportOutput != null) {
                        councilMeeting.setFilestore(fileStoreService.store(new ByteArrayInputStream(reportOutput),
                                MEETINGRESOLUTIONFILENAME, APPLICATION_PDF, MODULE_NAME));
                        councilMeetingService.update(councilMeeting);
                    }

                    if (councilMeeting.getFilestore() != null) {
                        fetchMeetingResolutionByFileStoreId(response, councilMeeting);
                    }
                }
            }
        }

    }

    private byte[] generatePdfByPassingMeetingObject(final HttpServletRequest request, CouncilMeeting councilMeeting) {
        byte[] reportOutput;
        final String url = WebUtils.extractRequestDomainURL(request, false);
        String logoPath = url.concat(ReportConstants.IMAGE_CONTEXT_PATH)
                .concat((String) request.getSession().getAttribute(CITYLOGO));
        reportOutput = councilReportService.generatePDFForMom(councilMeeting, logoPath);
        return reportOutput;
    }

    private void fetchMeetingResolutionByFileStoreId(final HttpServletResponse response, CouncilMeeting councilMeeting)
            throws IOException {
        fileStoreUtils.fetchFileAndWriteToStream(councilMeeting.getFilestore().getFileStoreId(),
                CouncilConstants.MODULE_NAME, false, response);
    }

    @RequestMapping(value = "/attendance/ajaxsearch/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearc(@PathVariable("id") final CouncilMeeting id, Model model) {
        List<MeetingAttendence> searchResultList = councilMeetingService.findListOfAttendance(id);
        return new StringBuilder(DATA)
                .append(toJSON(searchResultList, MeetingAttendence.class, MeetingAttendanceJsonAdaptor.class)).append("}")
                .toString();
    }

    @RequestMapping(value = "/generateagenda/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> printAgendaDetails(@PathVariable("id") final Long id, final Model model,
            final HttpServletRequest request) {
        byte[] reportOutput;
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        final String url = WebUtils.extractRequestDomainURL(request, false);
        String logoPath = url.concat(ReportConstants.IMAGE_CONTEXT_PATH).concat(
                (String) request.getSession().getAttribute(CITYLOGO));
        reportOutput = councilReportService.generatePDFForAgendaDetails(councilMeeting, logoPath);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(APPLICATION_PDF));
        headers.add("content-disposition", "inline;filename=AgendaNotice.pdf");
        return new ResponseEntity<byte[]>(reportOutput, headers, HttpStatus.CREATED);

    }
}