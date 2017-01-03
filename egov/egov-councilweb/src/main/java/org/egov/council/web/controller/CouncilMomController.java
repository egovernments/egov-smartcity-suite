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
import static org.egov.council.utils.constants.CouncilConstants.PREAMBLE_STATUS_APPROVED;
import static org.egov.council.utils.constants.CouncilConstants.RESOLUTION_APPROVED_PREAMBLE;
import static org.egov.council.utils.constants.CouncilConstants.RESOLUTION_STATUS_ADJURNED;
import static org.egov.council.utils.constants.CouncilConstants.RESOLUTION_STATUS_APPROVED;
import static org.egov.council.utils.constants.CouncilConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.council.utils.constants.CouncilConstants.WARD;
import static org.egov.infra.utils.JsonUtils.toJSON;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.council.autonumber.MOMResolutionNumberGenerator;
import org.egov.council.entity.CommitteeType;
import org.egov.council.entity.CouncilMeeting;
import org.egov.council.entity.MeetingMOM;
import org.egov.council.service.CommitteeTypeService;
import org.egov.council.service.CouncilMeetingService;
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
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.web.support.json.adapter.BoundaryAdapter;
import org.egov.infra.web.utils.WebUtils;
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
    @ModelAttribute("committeeType")
    public List<CommitteeType> getCommitteTypeList() {
        return committeeTypeService.getActiveCommiteeType();
    }

    @ModelAttribute("meetingTimingMap")
    public Map<String, String> getMeetingTimingList() {
        return MEETING_TIMINGS;
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
        sortMeetingMomByItemNumber(councilMeeting);
        model.addAttribute(COUNCIL_MEETING, councilMeeting);

        return COUNCILMOM_NEW;
    }

    private void sortMeetingMomByItemNumber(CouncilMeeting councilMeeting) {
        councilMeeting.getMeetingMOMs().sort((MeetingMOM f1, MeetingMOM f2) -> f1.getItemNumber().compareTo(f2.getItemNumber()));

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(
            @Valid @ModelAttribute final CouncilMeeting councilMeeting,
            final BindingResult errors, final Model model,
            final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            return COUNCILMEETING_EDIT;
        }
        EgwStatus preambleApprovedStatus = egwStatusHibernateDAO.getStatusByModuleAndCode(PREAMBLE_MODULENAME,
                PREAMBLE_STATUS_APPROVED);
        Long itemNumber = Long.valueOf(councilMeeting.getMeetingMOMs().size());
        for (MeetingMOM meetingMOM : councilMeeting.getMeetingMOMs()) {
            if (meetingMOM.getPreamble().getId() == null) {
                meetingMOM
                        .setPreamble(councilPreambleService
                                .buildSumotoPreamble(meetingMOM,
                                        preambleApprovedStatus));
                meetingMOM.setMeeting(councilMeeting);
                meetingMOM.setItemNumber(itemNumber.toString());
                itemNumber++;
            }
        }
        councilMeeting
                .setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(
                        MEETING_MODULENAME, MEETINGUSEDINRMOM));
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
    public String searchCreatedMOM(
            @PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CouncilMeeting councilMeeting) {

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
    public String ajaxsearch(final String mode, Model model,
            @ModelAttribute final CouncilMeeting councilMeeting) {
        List<Department> departmentList = departmentService.getAllDepartments();
        return new StringBuilder("{ \"departmentLists\":")
                .append(toJSON(departmentList, Department.class,
                        CouncilDepartmentJsonAdaptor.class))
                .append("}")
                .toString();
    }

    @RequestMapping(value = "/resolutionlist", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearchResolutionlist(final String mode,
            Model model, @ModelAttribute final CouncilMeeting councilMeeting) {
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
    public String ajaxsearchWardlist(final String mode,
            Model model, @ModelAttribute final CouncilMeeting councilMeeting) {
        List<Boundary> wardList = boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD,
                        REVENUE_HIERARCHY_TYPE);
        return new StringBuilder("{ \"wardLists\":")
                .append(toJSON(wardList, Boundary.class, BoundaryAdapter.class))
                .append("}").toString();
    }

    @RequestMapping(value = "/generateresolution", method = RequestMethod.GET)
    public String generateResolutionnumber(
            @Valid @ModelAttribute final CouncilMeeting councilMeeting,
            final BindingResult errors, final Model model,
            final RedirectAttributes redirectAttrs, final HttpServletRequest request) throws ParseException {
        byte[] reportOutput;
        EgwStatus preambleApprovedStatus = egwStatusHibernateDAO.getStatusByModuleAndCode(PREAMBLE_MODULENAME,
                PREAMBLE_STATUS_APPROVED);
        EgwStatus resoulutionApprovedStatus = egwStatusHibernateDAO.getStatusByModuleAndCode(COUNCIL_RESOLUTION,
                RESOLUTION_STATUS_APPROVED);
        EgwStatus resoulutionAdjurnedStatus = egwStatusHibernateDAO.getStatusByModuleAndCode(COUNCIL_RESOLUTION,
                RESOLUTION_STATUS_ADJURNED);
        EgwStatus preambleAdjurnedStatus = egwStatusHibernateDAO.getStatusByModuleAndCode(PREAMBLE_MODULENAME,
                PREAMBLE_STATUS_ADJOURNED);
        EgwStatus resolutionApprovedStatus = egwStatusHibernateDAO.getStatusByModuleAndCode(PREAMBLE_MODULENAME,
                RESOLUTION_APPROVED_PREAMBLE);

        Long itemNumber = Long.valueOf(councilMeeting.getMeetingMOMs().size());
        for (MeetingMOM meetingMOM : councilMeeting.getMeetingMOMs()) {
            if (meetingMOM.getPreamble().getId() == null) {
                meetingMOM.setItemNumber(itemNumber.toString());
                itemNumber++;
                meetingMOM.setPreamble(councilPreambleService
                        .buildSumotoPreamble(meetingMOM, preambleApprovedStatus));
                meetingMOM.setMeeting(councilMeeting);
            }
        }

        for (MeetingMOM meetingMOM : councilMeeting.getMeetingMOMs()) {
            // if mom status is approved, generate resolution number
            if (meetingMOM.getResolutionStatus().getCode().equals(resoulutionApprovedStatus.getCode())) {
                MOMResolutionNumberGenerator momResolutionNumberGenerator = autonumberServiceBeanResolver
                        .getAutoNumberServiceFor(MOMResolutionNumberGenerator.class);
                meetingMOM.setResolutionNumber(momResolutionNumberGenerator
                        .getNextNumber(meetingMOM));
                meetingMOM.getPreamble().setStatus(resolutionApprovedStatus);
                // if mom status adjourned, update preamble status to adjurned. These record will be used in next meeting.
            } else if (meetingMOM.getResolutionStatus().getCode().equals(resoulutionAdjurnedStatus.getCode())) {
                meetingMOM.getPreamble()
                        .setStatus(preambleAdjurnedStatus);
            }
        }

        reportOutput = generateMomPdfByPassingMeeting(councilMeeting, request);
        if (reportOutput != null) {
            councilMeeting.setFilestore(fileStoreService.store(new ByteArrayInputStream(reportOutput), MEETINGRESOLUTIONFILENAME,
                    "application/pdf", MODULE_NAME));
        }
        councilMeeting.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(MEETING_MODULENAME, MOM_FINALISED));
        councilMeetingService.update(councilMeeting);
        councilMeetingIndexService.createCouncilMeetingIndex(councilMeeting);
        councilSmsAndEmailService.sendSms(councilMeeting, null);
        councilSmsAndEmailService.sendEmail(councilMeeting, null, reportOutput);
        return "redirect:/councilmeeting/generateresolution/" + councilMeeting.getId();
    }

    private byte[] generateMomPdfByPassingMeeting(final CouncilMeeting councilMeeting,
            final HttpServletRequest request) {
        byte[] reportOutput;
        final String url = WebUtils.extractRequestDomainURL(request, false);
        String logoPath = url.concat(ReportConstants.IMAGE_CONTEXT_PATH)
                .concat((String) request.getSession().getAttribute("citylogo"));
        reportOutput = councilReportService.generatePDFForMom(councilMeeting, logoPath);
        return reportOutput;
    }
}