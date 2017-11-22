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
package org.egov.council.web.controller;

import static org.egov.council.utils.constants.CouncilConstants.APPROVED;
import static org.egov.council.utils.constants.CouncilConstants.COUNCIL_RESOLUTION;
import static org.egov.council.utils.constants.CouncilConstants.MEETINGRESOLUTIONFILENAME;
import static org.egov.council.utils.constants.CouncilConstants.MEETINGUSEDINRMOM;
import static org.egov.council.utils.constants.CouncilConstants.MEETING_MODULENAME;
import static org.egov.council.utils.constants.CouncilConstants.MEETING_TIMINGS;
import static org.egov.council.utils.constants.CouncilConstants.MODULE_NAME;
import static org.egov.council.utils.constants.CouncilConstants.MOM_FINALISED;
import static org.egov.council.utils.constants.CouncilConstants.PREAMBLE_MODULENAME;
import static org.egov.council.utils.constants.CouncilConstants.PREAMBLE_STATUS_ADJOURNED;
import static org.egov.council.utils.constants.CouncilConstants.RESOLUTION_APPROVED_PREAMBLE;
import static org.egov.council.utils.constants.CouncilConstants.RESOLUTION_STATUS_ADJURNED;
import static org.egov.council.utils.constants.CouncilConstants.RESOLUTION_STATUS_APPROVED;
import static org.egov.council.utils.constants.CouncilConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.council.utils.constants.CouncilConstants.WARD;
import static org.egov.infra.utils.JsonUtils.toJSON;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.council.autonumber.MOMResolutionNumberGenerator;
import org.egov.council.entity.CommitteeType;
import org.egov.council.entity.CouncilMeeting;
import org.egov.council.entity.CouncilMeetingType;
import org.egov.council.entity.MeetingMOM;
import org.egov.council.service.CommitteeTypeService;
import org.egov.council.service.CouncilMeetingService;
import org.egov.council.service.CouncilMeetingTypeService;
import org.egov.council.service.CouncilPreambleService;
import org.egov.council.service.CouncilReportService;
import org.egov.council.service.CouncilSmsAndEmailService;
import org.egov.council.service.es.CouncilMeetingIndexService;
import org.egov.council.web.adaptor.CouncilDepartmentJsonAdaptor;
import org.egov.council.web.adaptor.CouncilMeetingJsonAdaptor;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.FileUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.web.support.json.adapter.BoundaryAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Controller
@RequestMapping("/councilmom")
public class CouncilMomController {

    private static final String MESSAGE = "message";
    private static final String COUNCIL_MEETING = "councilMeeting";
    private static final String COUNCIL_MOM_MEETING_SEARCH = "councilmomMeeting-search";
    private static final String COUNCILMOM_NEW = "councilMom-new";
    private static final String COUNCILMEETING_EDIT = "councilmeeting-edit";
    private static final String COUNCILMOM_RESULT = "councilmom-result";
    private static final String COUNCILMOM_SEARCH = "councilmom-search";
    private static final String COUNCILMOM_VIEW = "councilmom-view";
    private static final String COMMONERRORPAGE = "common-error-page";
    private static final String APPLICATION_RTF = "application/rtf";

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CommitteeTypeService committeeTypeService;

    @Autowired
    private CouncilMeetingService councilMeetingService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private AutonumberServiceBeanResolver autonumberServiceBeanResolver;

    @Autowired
    private CouncilPreambleService councilPreambleService;

    @Autowired
    private CouncilReportService councilReportService;

    @Autowired
    private CouncilSmsAndEmailService councilSmsAndEmailService;

    @Autowired
    private CouncilMeetingIndexService councilMeetingIndexService;
    @Qualifier("fileStoreService")
    @Autowired
    private FileStoreService fileStoreService;
    @Autowired
    private CouncilMeetingTypeService councilMeetingTypeService;

    @ModelAttribute("committeeType")
    public List<CommitteeType> getCommitteTypeList() {
        return committeeTypeService.getActiveCommiteeType();
    }

    @ModelAttribute("meetingTimingMap")
    public Map<String, String> getMeetingTimingList() {
        return MEETING_TIMINGS;
    }
    
    @ModelAttribute("meetingType")
    public List<CouncilMeetingType> getmeetingTypeList() {
        return councilMeetingTypeService.findAllActiveMeetingType();
    }
    
    @ModelAttribute("resolutionStatus")
    public List<EgwStatus> getResolutionStatusList() {
        return egwStatusHibernateDAO.getStatusByModule(COUNCIL_RESOLUTION);
    }

    @RequestMapping(value = "/new/{id}", method = RequestMethod.GET)
    public String newForm(@PathVariable("id") final Long id, Model model) {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);

        if (null != councilMeeting && null != councilMeeting.getStatus()) {
            if (APPROVED.equals(councilMeeting.getStatus().getCode())) {
                model.addAttribute(MESSAGE, "msg.attendance.not.finalizd");
                return COMMONERRORPAGE;
            }
            if (MOM_FINALISED.equals(councilMeeting.getStatus().getCode())) {
                model.addAttribute(MESSAGE, "msg.mom.alreadyfinalized");
                return COMMONERRORPAGE;
            }

        }
        if (councilMeeting != null) {
            sortMeetingMomByItemNumber(councilMeeting);
            model.addAttribute(COUNCIL_MEETING, councilMeeting);
        }
        return COUNCILMOM_NEW;
    }

    private void sortMeetingMomByItemNumber(CouncilMeeting councilMeeting) {
        councilMeeting.getMeetingMOMs().sort(
                (MeetingMOM f1, MeetingMOM f2) -> Long.valueOf(f1.getItemNumber()).compareTo(Long.valueOf(f2.getItemNumber())));

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(
            @Valid @ModelAttribute final CouncilMeeting councilMeeting,
            final BindingResult errors, final Model model,
            final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            return COUNCILMEETING_EDIT;
        }
        EgwStatus preambleResolutionApprovedStatus = egwStatusHibernateDAO.getStatusByModuleAndCode(PREAMBLE_MODULENAME,
                RESOLUTION_APPROVED_PREAMBLE);
        Long itemNumber = Long.valueOf(0);
        for (final MeetingMOM meetingMOM : councilMeeting.getMeetingMOMs())
            if (meetingMOM.getId() != null)
                itemNumber++;

        for (MeetingMOM meetingMOM : councilMeeting.getMeetingMOMs()) {
            if (meetingMOM.getPreamble().getId() == null) {
                meetingMOM
                        .setPreamble(councilPreambleService
                                .buildSumotoPreamble(meetingMOM,
                                        preambleResolutionApprovedStatus));
                meetingMOM.setMeeting(councilMeeting);
                itemNumber++;
                meetingMOM.setItemNumber(itemNumber.toString());

            }
        }
        councilMeeting
                .setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(
                        MEETING_MODULENAME, MEETINGUSEDINRMOM));
        if (councilMeeting.getFiles() != null && councilMeeting.getFiles().length > 0) {
            councilMeeting.setSupportDocs(councilMeetingService.addToFileStore(councilMeeting.getFiles()));
        }
        councilMeetingService.update(councilMeeting);

        redirectAttrs.addFlashAttribute(MESSAGE, messageSource.getMessage(
                "msg.councilMeeting.success", null, null));
        return "redirect:/councilmom/result/" + councilMeeting.getId();
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        sortMeetingMomByItemNumber(councilMeeting);
        model.addAttribute(COUNCIL_MEETING, councilMeeting);
        return COUNCILMOM_RESULT;
    }

    @RequestMapping(value = "/meetingsearch/{mode}", method = RequestMethod.GET)
    public String searchMeeting(@PathVariable("mode") final String mode,
                                Model model) {
        model.addAttribute(COUNCIL_MEETING, new CouncilMeeting());
        return COUNCIL_MOM_MEETING_SEARCH;

    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilMeeting councilMeeting = councilMeetingService.findOne(id);
        sortMeetingMomByItemNumber(councilMeeting);
        model.addAttribute(COUNCIL_MEETING, councilMeeting);

        return COUNCILMOM_VIEW;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        CouncilMeeting councilMeeting = new CouncilMeeting();
        model.addAttribute(COUNCIL_MEETING, councilMeeting);
        return COUNCILMOM_SEARCH;

    }

    @RequestMapping(value = "/searchcreated-mom/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchCreatedMOM(@PathVariable("mode") final String mode, @ModelAttribute final CouncilMeeting councilMeeting) {

        if (null != mode && !"".equals(mode)) {
            List<CouncilMeeting> searchResultList;

            if ("edit".equalsIgnoreCase(mode)) {
                searchResultList = councilMeetingService.searchMeetingWithMomCreatedStatus(councilMeeting);
            } else {
                searchResultList = councilMeetingService.searchMeeting(councilMeeting);
            }
            return new StringBuilder("{ \"data\":")
                    .append(toJSON(searchResultList, CouncilMeeting.class,
                            CouncilMeetingJsonAdaptor.class))
                    .append("}")
                    .toString();
        }

        return null;
    }

    @RequestMapping(value = "/departmentlist", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearch(@ModelAttribute final CouncilMeeting councilMeeting) {
        List<Department> departmentList = departmentService.getAllDepartments();
        return new StringBuilder("{ \"departmentLists\":")
                .append(toJSON(departmentList, Department.class,
                        CouncilDepartmentJsonAdaptor.class))
                .append("}")
                .toString();
    }

    @RequestMapping(value = "/resolutionlist", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearchResolutionlist(@ModelAttribute final CouncilMeeting councilMeeting) {
        List<EgwStatus> resolutionList = egwStatusHibernateDAO
                .getStatusByModule(COUNCIL_RESOLUTION);
        Gson gson = new Gson();
        Type type = new TypeToken<List<EgwStatus>>() {
        }.getType();
        String json = gson.toJson(resolutionList, type);
        return new StringBuilder("{ \"resolutionLists\":")
                .append(json).append("}").toString();
    }

    @RequestMapping(value = "/wardlist", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearchWardlist(@ModelAttribute final CouncilMeeting councilMeeting) {
        List<Boundary> wardList = boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD,
                        REVENUE_HIERARCHY_TYPE);
        return new StringBuilder("{ \"wardLists\":")
                .append(toJSON(wardList, Boundary.class, BoundaryAdapter.class))
                .append("}").toString();
    }

    @RequestMapping(value = "/generateresolution", method = RequestMethod.POST)
    public String generateResolutionnumber(
            @Valid @ModelAttribute final CouncilMeeting councilMeeting) throws ParseException {
        byte[] reportOutput;

        EgwStatus resoulutionApprovedStatus = egwStatusHibernateDAO.getStatusByModuleAndCode(COUNCIL_RESOLUTION,
                RESOLUTION_STATUS_APPROVED);
        EgwStatus resoulutionAdjurnedStatus = egwStatusHibernateDAO.getStatusByModuleAndCode(COUNCIL_RESOLUTION,
                RESOLUTION_STATUS_ADJURNED);
        EgwStatus preambleAdjurnedStatus = egwStatusHibernateDAO.getStatusByModuleAndCode(PREAMBLE_MODULENAME,
                PREAMBLE_STATUS_ADJOURNED);
        EgwStatus resolutionApprovedStatus = egwStatusHibernateDAO.getStatusByModuleAndCode(PREAMBLE_MODULENAME,
                RESOLUTION_APPROVED_PREAMBLE);

        Long itemNumber = Long.valueOf(0);
        for (final MeetingMOM meetingMOM : councilMeeting.getMeetingMOMs())
            if (meetingMOM.getId() != null)
                itemNumber++;

        for (MeetingMOM meetingMOM : councilMeeting.getMeetingMOMs()) {
            if (meetingMOM.getPreamble().getId() == null) {
                itemNumber++;
                meetingMOM.setItemNumber(itemNumber.toString());
                meetingMOM.setPreamble(councilPreambleService
                        .buildSumotoPreamble(meetingMOM, resolutionApprovedStatus));
                meetingMOM.setMeeting(councilMeeting);
            }
        }

        for (MeetingMOM meetingMOM : councilMeeting.getMeetingMOMs()) {
            // if mom status is approved, generate resolution number
            if (meetingMOM.getResolutionStatus().getCode().equals(resoulutionApprovedStatus.getCode())) {
                MOMResolutionNumberGenerator momResolutionNumberGenerator = autonumberServiceBeanResolver
                        .getAutoNumberServiceFor(MOMResolutionNumberGenerator.class);
                meetingMOM.setResolutionNumber(
                        meetingMOM.getResolutionNumber() != null ? meetingMOM.getResolutionNumber() : momResolutionNumberGenerator
                                .getNextNumber(meetingMOM));
                meetingMOM.getPreamble().setStatus(resolutionApprovedStatus);
                // if mom status adjourned, update preamble status to adjurned. These record will be used in next meeting.
            } else if (meetingMOM.getResolutionStatus().getCode().equals(resoulutionAdjurnedStatus.getCode())) {
                meetingMOM.getPreamble()
                        .setStatus(preambleAdjurnedStatus);
            }
        }

        reportOutput = generateMomPdfByPassingMeeting(councilMeeting);
        if (reportOutput != null) {
            councilMeeting.setFilestore(fileStoreService.store(FileUtils.byteArrayToFile(reportOutput, MEETINGRESOLUTIONFILENAME,"rtf" ).toFile(), MEETINGRESOLUTIONFILENAME,
                    APPLICATION_RTF, MODULE_NAME));
        }
        councilMeeting.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(MEETING_MODULENAME, MOM_FINALISED));
        councilMeetingService.update(councilMeeting);
        councilMeetingIndexService.createCouncilMeetingIndex(councilMeeting);
        councilSmsAndEmailService.sendSms(councilMeeting, null);
        councilSmsAndEmailService.sendEmail(councilMeeting, null, reportOutput);
        return "forward:/councilmeeting/generateresolution/" + councilMeeting.getId();
    }

    private byte[] generateMomPdfByPassingMeeting(final CouncilMeeting councilMeeting) {
        return councilReportService.generatePDFForMom(councilMeeting);
    }
}