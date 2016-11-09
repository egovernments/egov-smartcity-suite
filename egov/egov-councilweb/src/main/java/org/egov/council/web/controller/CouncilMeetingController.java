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
import static org.egov.infra.web.utils.WebUtils.toJSON;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

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
	
	 //private static final Logger LOGGER = Logger.getLogger(CouncilMeetingController.class);
	 
    private final static String COUNCILMEETING_NEW = "councilmeeting-new";
    private final static String COMMONERRORPAGE = "common-error-page";
    private final static String COUNCILMEETING_RESULT = "councilmeeting-result";
    private final static String COUNCILMEETING_EDIT = "councilmeeting-edit";
    private final static String COUNCILMEETING_VIEW = "councilmeeting-view";
    private final static String COUNCILMEETING_SEARCH = "councilmeeting-search";
    private final static String COUNCIL_MEETING_AGENDA_SEARCH = "councilmeetingAgenda-search";
    private final static String COUNCILMEETING_ATTENDANCE_SEARCH = "councilmeeting-attendsearch";
    private final static String COUNCILMEETING_ATTENDANCE_VIEW = "councilmeeting-attendsearch-view";
    private final static String COUNCILMEETING_SEND_SMS_EMAIL = "councilmeetingsearch-tosendsms-email";
    private final static String COUNCILMEETING_EDIT_ATTENDANCE = "councilmeeting-attend-form";
    private final static String COUNCILMEETING_ATTENDANCE_RESULT = "councilmeeting-attend-result";
	
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
	@Autowired  private CouncilReportService councilReportService;
	protected @Autowired FileStoreUtils fileStoreUtils;  
	@Qualifier("fileStoreService")
	private @Autowired FileStoreService fileStoreService;
	
	@Autowired
	private CouncilCommitteeMemberService committeeMemberService;

	public @ModelAttribute("committeeType") List<CommitteeType> getCommitteTypeList() {
		return committeeTypeService.getActiveCommiteeType();
	}

	public @ModelAttribute("meetingTimingMap") LinkedHashMap<String, String> getMeetingTimingList() {
		return MEETING_TIMINGS;
	}

	public @ModelAttribute("departments") List<Department> getDepartmentList() {
		return departmentService.getAllDepartments();
	}
	
	@RequestMapping(value = "/new/{id}", method = RequestMethod.GET)
	public String newForm(@ModelAttribute final CouncilMeeting councilMeeting, @PathVariable("id") final Long id,
			final Model model) {

		CouncilAgenda councilAgenda = councilAgendaService.findOne(id);
		model.addAttribute("councilMeeting", councilMeeting);
		if(councilAgenda != null && AGENDAUSEDINMEETING.equals(councilAgenda.getStatus().getCode())){
			model.addAttribute("message", "msg.agenda.exist");
			return COMMONERRORPAGE;
		} else if (councilAgenda != null) {
			councilMeeting.setCommitteeType(councilAgenda.getCommitteeType());
			buildMeetingMomByUsingAgendaDetails(councilMeeting, councilAgenda);
			return COUNCILMEETING_NEW;

		} else {
			model.addAttribute("message", "msg.invalid.agenda.details");
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
            meetingMom.setItemNumber(itemNumber.toString());itemNumber++;
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
        sortMeetingMomByItemNumber(councilMeeting);
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
        sortMeetingMomByItemNumber(councilMeeting);
        model.addAttribute("councilMeeting", councilMeeting);
        return COUNCILMEETING_VIEW;
    }

    private void sortMeetingMomByItemNumber(CouncilMeeting councilMeeting) {
        Collections.sort(councilMeeting.getMeetingMOMs(), new Comparator<MeetingMOM>() {
                @Override
                public int compare(MeetingMOM f1, MeetingMOM f2) {
                    return f1.getItemNumber().compareTo(f2.getItemNumber());
                }
            });
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        model.addAttribute("councilMeeting", councilMeeting);
        model.addAttribute("commiteemembelist", councilMeeting.getCommitteeType().getCommiteemembers());
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
        if (null != mode && !mode.equals("")) {
            List<CouncilMeeting> searchResultList;

            if (mode.equalsIgnoreCase("edit")) {
                searchResultList = councilMeetingService.searchMeetingForEdit(councilMeeting);
            } else {
                searchResultList = councilMeetingService.searchMeeting(councilMeeting);
            }
            return new StringBuilder("{ \"data\":")
                    .append(toJSON(searchResultList, CouncilMeeting.class, CouncilMeetingJsonAdaptor.class))
                    .append("}").toString();
        }
      return null;
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
            @RequestParam("msg") String msg, final Model model, final HttpServletRequest request) {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        councilSmsAndEmailService.sendSms(councilMeeting, msg);
        final String url = WebUtils.extractRequestDomainURL(request, false);
        String logoPath = url.concat(ReportConstants.IMAGE_CONTEXT_PATH).concat(
                (String) request.getSession().getAttribute("citylogo"));
        councilSmsAndEmailService.sendEmail(councilMeeting, msg,
                councilReportService.generatePDFForAgendaDetails(councilMeeting, logoPath));
        String result = new StringBuilder("{ \"success\":true }").toString();
        return result;
    }
    

    @RequestMapping(value = "/generateresolution/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> viewDemandNoticeReport(@PathVariable final Long id,
            final HttpSession session,HttpServletRequest request) {
     
        byte[] reportOutput = null;
        CouncilMeeting councilMeeting =councilMeetingService.findOne(id);
        final String url = WebUtils.extractRequestDomainURL(request, false);
        
        String logoPath = url.concat(ReportConstants.IMAGE_CONTEXT_PATH).concat(
                (String) request.getSession().getAttribute("citylogo"));
        reportOutput= councilReportService.generatePDFForMom(councilMeeting, logoPath);
        
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=Resolution.pdf");
        return new ResponseEntity<byte[]>(reportOutput, headers, HttpStatus.CREATED);
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
    
    
    @RequestMapping(value = "/attendance/search/edit/{id}", method = RequestMethod.GET)
    public String editAttendance(@PathVariable("id") final CouncilMeeting councilMeeting1, Model model) {
    	 CouncilMeeting councilMeeting = councilMeetingService.findOne(councilMeeting1.getId());
    	 
    	 if(councilMeeting != null && councilMeeting.getCommitteeType().getCommiteemembers().size() <= 0){
    		 model.addAttribute("message", "msg.committee.members.not.added");
  			return COMMONERRORPAGE;
    	 }
    	 if ( councilMeeting != null && councilMeeting.getStatus() != null){
 			if( ATTENDANCEFINALIZED.equals(councilMeeting.getStatus().getCode())) {
 			model.addAttribute("message", "msg.attendance.already.finalizd");
 			return COMMONERRORPAGE;
 			}
    	 }
    	 
    	 if ( councilMeeting != null && councilMeeting.getCommitteeType() != null && councilMeeting.getMeetingAttendence().isEmpty()) {
				List<MeetingAttendence> attendencesList = new ArrayList<MeetingAttendence>();
				for (CommitteeMembers committeeMembers : committeeMemberService
						.findAllByCommitteType(councilMeeting.getCommitteeType())) {
					MeetingAttendence attendence = new MeetingAttendence();
					attendence.setCommitteeMembers(committeeMembers);
					attendencesList.add(attendence);
				}
				councilMeeting1.setMeetingAttendence(attendencesList);
			}
         sortMeetingMomByItemNumber(councilMeeting);
         model.addAttribute("councilMeeting", councilMeeting);
        return COUNCILMEETING_EDIT_ATTENDANCE;
    }
    
    @RequestMapping(value = "/attendance/update", method = RequestMethod.POST)
    public String updateAttendance(@Valid @ModelAttribute final CouncilMeeting councilMeeting, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
    	if (errors.hasErrors()) {
            return "redirect:councilmeeting/attendance/search/edit/"+ councilMeeting.getId();
        }
    	buildAttendanceDetailsForMeeting(councilMeeting);
        councilMeetingService.update(councilMeeting);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilMeeting.attendance.success", null, null));
        return "redirect:result/" + councilMeeting.getId();
    }

    @RequestMapping(value = "/attendance/finalizeattendance", method = RequestMethod.POST)
    public String updateFinalizedAttendance(@Valid @ModelAttribute final CouncilMeeting councilMeeting, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
        	 return "redirect:councilmeeting/attendance/search/edit/"+councilMeeting.getId();
        }
        buildAttendanceDetailsForMeeting(councilMeeting);
        councilMeeting.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(MEETING_MODULENAME, ATTENDANCEFINALIZED));
        councilMeetingService.update(councilMeeting);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilMeeting.attendance.success", null, null));
        return "redirect:result/"+councilMeeting.getId();
    }
    
    private void buildAttendanceDetailsForMeeting(final CouncilMeeting councilMeeting) {
		for (MeetingAttendence attendence : councilMeeting
				.getMeetingAttendence()) {
			if (attendence.getChecked()) {
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

					if (reportOutput != null ) {
						councilMeeting.setFilestore(fileStoreService.store(new ByteArrayInputStream(reportOutput),
								MEETINGRESOLUTIONFILENAME, "application/pdf", MODULE_NAME));
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
		byte[] reportOutput = null;
		final String url = WebUtils.extractRequestDomainURL(request, false);
		String logoPath = url.concat(ReportConstants.IMAGE_CONTEXT_PATH)
				.concat((String) request.getSession().getAttribute("citylogo"));
		reportOutput = councilReportService.generatePDFForMom(councilMeeting, logoPath);
		return reportOutput;
	}

	private void fetchMeetingResolutionByFileStoreId(final HttpServletResponse response, CouncilMeeting councilMeeting)
			throws IOException {
		fileStoreUtils.fetchFileAndWriteToStream(councilMeeting.getFilestore().getFileStoreId(),
				CouncilConstants.MODULE_NAME, false, response);
	}
    
    @RequestMapping(value = "/attendance/ajaxsearch/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearc(@PathVariable("id") final CouncilMeeting id, Model model) {
        List<MeetingAttendence> searchResultList = councilMeetingService.findListOfAttendance(id);
       String result = new StringBuilder("{ \"data\":").append(toJSON(searchResultList,MeetingAttendence.class,  MeetingAttendanceJsonAdaptor.class)).append("}")
                .toString();
        return result;
    }
    
    @RequestMapping(value = "/generateagenda/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> printAgendaDetails(@PathVariable("id") final Long id,final Model model, final HttpServletRequest request){
    	byte[] reportOutput = null;
    	CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
    	final String url = WebUtils.extractRequestDomainURL(request, false);
        String logoPath = url.concat(ReportConstants.IMAGE_CONTEXT_PATH).concat(
                (String) request.getSession().getAttribute("citylogo"));
        reportOutput = councilReportService.generatePDFForAgendaDetails(councilMeeting, logoPath);
        
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=AgendaNotice.pdf");
        return new ResponseEntity<byte[]>(reportOutput, headers, HttpStatus.CREATED);
        
    } 
}