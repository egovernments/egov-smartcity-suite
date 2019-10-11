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
package org.egov.adtax.web.controller.hoarding;

import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.ANONYMOUS_USER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.RequestDetails;
import org.egov.adtax.entity.SubCategory;
import org.egov.adtax.entity.enums.AdvertisementApplicationType;
import org.egov.adtax.entity.enums.AdvertisementStatus;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.adtax.web.controller.common.HoardingControllerSupport;
import org.egov.adtax.workflow.AdvertisementWorkFlowService;
import org.egov.commons.entity.Source;
import org.egov.eis.entity.Assignment;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hoarding")
public class CreateAdvertisementController extends HoardingControllerSupport {

    private static final String NOTEXISTS_POSITION = "notexists.position";
    private static final String INVALID_APPROVER = "invalid.approver";
    private static final String CURRENT_STATE = "currentState";
    private static final String HOARDING_CREATE = "hoarding-create";
    private static final String ADDITIONAL_RULE = "additionalRule";
    private static final String STATE_TYPE = "stateType";
    private static final String IS_EMPLOYEE = "isEmployee";
    private static final String APPROVAL_POSITION = "approvalPosition";
    private static final String APPLICATION_PDF = "application/pdf";
    protected String reportId;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AdvertisementWorkFlowService advertisementWorkFlowService;

    @RequestMapping(value = "child-boundaries", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Boundary> childBoundaries(@RequestParam final Long parentBoundaryId) {
        return boundaryService.getActiveChildBoundariesByBoundaryId(parentBoundaryId);
    }

    @RequestMapping(value = "subcategories", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<SubCategory> hoardingSubcategories(@RequestParam final Long categoryId) {
        return subCategoryService.getAllActiveSubCategoryByCategoryId(categoryId);
    }

    @RequestMapping(value = { "/create" }, method = GET)
    public String createHoardingForm(@ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail,
            final Model model, HttpServletRequest request) {

        buildCreateHoardingForm(advertisementPermitDetail, model);
        model.addAttribute(IS_EMPLOYEE, !ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName())
                && advertisementWorkFlowService.isEmployee(securityUtils.getCurrentUser()));

        return HOARDING_CREATE;
    }

    @RequestMapping(value = { "/createbycitizen" }, method = GET)
    public String createHoardingByCitizen(@ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail,
            final Model model, HttpServletRequest request) {

        buildCreateHoardingForm(advertisementPermitDetail, model);
        model.addAttribute("applicationSource", "online");
        model.addAttribute("citizenCreated","yes");

        return HOARDING_CREATE;
    }

    @RequestMapping(value = { "/create", "/createbycitizen" }, method = POST)
    public String createAdvertisement(@Valid @ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail,
            final BindingResult resultBinder,
            final RedirectAttributes redirAttrib, final HttpServletRequest request, final Model model,
            @RequestParam String workFlowAction) {
        User currentuser = securityUtils.getCurrentUser();
        boolean isActiveApprover = false;
        Boolean isEmployee = isEmployee(currentuser);
        validateAssignmentForCscUser(advertisementPermitDetail, isEmployee, resultBinder);
        validateHoardingDocs(advertisementPermitDetail, resultBinder);
        validateAdvertisementDetails(advertisementPermitDetail, resultBinder);
        model.addAttribute(IS_EMPLOYEE, isEmployee);

        if (resultBinder.hasErrors()) {
            buildCreateHoardingForm(advertisementPermitDetail, model);
            return HOARDING_CREATE;
        }

        if (advertisementPermitDetail != null) {
            if (advertisementPermitDetail.getState() == null)
                advertisementPermitDetail.setStatus(advertisementPermitDetailService
                        .getStatusByModuleAndCode(AdvertisementTaxConstants.APPLICATION_STATUS_CREATED));
            advertisementPermitDetail.getAdvertisement().setStatus(AdvertisementStatus.WORKFLOW_IN_PROGRESS);

            advertisementPermitDetail.setSource(Source.SYSTEM.toString());
            advertisementPermitDetail.setApplicationtype(AdvertisementApplicationType.NEW);
        }
        storeHoardingDocuments(advertisementPermitDetail);
        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setIsEmployee(isEmployee);
        requestDetails.setApprovalComment(StringUtils.defaultString(request.getParameter("approvalComent"), ""));
        requestDetails.setWorkflowaction(StringUtils.defaultString(request.getParameter("workFlowAction"), ""));
        requestDetails.setApproverName(StringUtils.defaultString(request.getParameter("approverName"), ""));
        requestDetails.setNextDesignation(StringUtils.defaultString(request.getParameter("nextDesignation"), ""));
        requestDetails
                .setApprovalPosition(Long.valueOf(StringUtils.defaultString(request.getParameter(APPROVAL_POSITION), "0")));

        if (!isEmployee || ANONYMOUS_USER.equalsIgnoreCase(currentuser.getName())) {
            Assignment assignment = advertisementWorkFlowService.getMappedAssignmentForCscOperator(advertisementPermitDetail);
            if (assignment != null) {
                requestDetails.setApprovalPosition(
                        Long.valueOf(StringUtils.defaultString(assignment.getPosition().getId().toString(), "0")));
                requestDetails.setApproverName(StringUtils.defaultString(assignment.getEmployee().getName(), ""));
                requestDetails.setNextDesignation(StringUtils.defaultString(assignment.getDesignation().getName(), ""));
                isActiveApprover = assignment.getToDate().compareTo(new Date()) >= 0;

            }
        }

        String result;
        if (isEmployee && advertisementPermitDetail != null) {
            result = createAdvertisementOnApproverCheck(advertisementPermitDetail, redirAttrib, currentuser,
                    requestDetails, model);
            return result != null ? result : "redirect:/hoarding/success/".concat(advertisementPermitDetail.getId().toString());
        } else if (isActiveApprover && advertisementPermitDetail != null) {
            result = createAdvertisementOnApproverCheck(advertisementPermitDetail, redirAttrib,
                    currentuser, requestDetails, model);
            return result != null ? result : "redirect:/hoarding/showack/".concat(advertisementPermitDetail.getId().toString());
        } else {
            model.addAttribute("message", NOTEXISTS_POSITION);
            buildCreateHoardingForm(advertisementPermitDetail, model);
            return HOARDING_CREATE;
        }
    }

    private boolean isEmployee(User currentuser) {
        return !ANONYMOUS_USER.equalsIgnoreCase(currentuser.getName())
                && advertisementWorkFlowService.isEmployee(securityUtils.getCurrentUser());
    }

    private String createAdvertisementOnApproverCheck(final AdvertisementPermitDetail advertisementPermitDetail,
            final RedirectAttributes redirAttrib, User currentuser,
            RequestDetails requestDtls, Model model) {
        if (advertisementPermitDetail != null && advertisementPermitDetail.getAdvertisement() != null) {
            advertisementPermitDetail.getAdvertisement()
                    .setPenaltyCalculationDate(advertisementPermitDetail.getApplicationDate());

            advertisementPermitDetailService.createAdvertisementPermitDetail(advertisementPermitDetail,
                    requestDtls.getApprovalPosition(),
                    StringUtils.defaultString(requestDtls.getApprovalComment(), ""), "CREATEADVERTISEMENT",
                    StringUtils.defaultString(requestDtls.getWorkflowaction(), ""), currentuser);
            if (!advertisementPermitDetail.isValidApprover()) {
                model.addAttribute("message", INVALID_APPROVER);
                buildCreateHoardingForm(advertisementPermitDetail, model);
                return HOARDING_CREATE;
            }
            redirAttrib.addFlashAttribute("advertisementPermitDetail", advertisementPermitDetail);
            String message = messageSource.getMessage("msg.success.forward",
                    new String[] {
                            new StringBuilder(StringUtils.defaultString(requestDtls.getApproverName(), "")).append("~")
                                    .append(StringUtils.defaultString(requestDtls.getNextDesignation(), "")).toString(),
                            advertisementPermitDetail.getApplicationNumber() },
                    null);
            redirAttrib.addFlashAttribute("message", message);
        }
        return null;
    }

    @RequestMapping(value = "/success/{id}", method = GET)
    public ModelAndView successView(@PathVariable("id") final String id,
            @ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail) {

        return new ModelAndView("hoarding/hoarding-success", "hoarding",
                advertisementPermitDetailService.findBy(Long.valueOf(id)));

    }

    @RequestMapping(value = "/showack/{id}", method = GET)
    public String showAck(@PathVariable Long id, final Model model) {
        AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService.findBy(id);
        model.addAttribute("advertisementPermitDetail", advertisementPermitDetail);
        return "hoarding-ack";
    }

    @RequestMapping(value = "/printack/{id}", method = GET)
    @ResponseBody
    public ResponseEntity<byte[]> printAck(@PathVariable Long id, final Model model, final HttpServletRequest request) {
        byte[] reportOutput;
        final String cityMunicipalityName = (String) request.getSession()
                .getAttribute("citymunicipalityname");
        final String cityName = (String) request.getSession().getAttribute("cityname");
        AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService.findBy(id);

        if (advertisementPermitDetail != null) {
            reportOutput = advertisementService
                    .getReportParamsForAcknowdgement(advertisementPermitDetail, cityMunicipalityName, cityName)
                    .getReportOutputData();
            if (reportOutput != null) {
                final HttpHeaders headers = new HttpHeaders();

                headers.setContentType(MediaType.parseMediaType(APPLICATION_PDF));
                headers.add("content-disposition", "inline;filename=hoarding-ack.pdf");
                return new ResponseEntity<>(reportOutput, headers, HttpStatus.CREATED);
            }
        }

        return null;

    }

    private void buildCreateHoardingForm(final AdvertisementPermitDetail advertisementPermitDetail, final Model model) {
        if (advertisementPermitDetail != null) {
            WorkflowContainer workFlowContainer = new WorkflowContainer();
            workFlowContainer.setAdditionalRule(AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE);
            model.addAttribute(CURRENT_STATE, "NEW");
            prepareWorkflow(model, advertisementPermitDetail, workFlowContainer);
            model.addAttribute(ADDITIONAL_RULE, AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE);
            model.addAttribute(STATE_TYPE, advertisementPermitDetail.getClass().getSimpleName());
        }
    }

    public void validateAssignmentForCscUser(final AdvertisementPermitDetail advertisementPermitDetail, Boolean isEmployee,
            final BindingResult errors) {
        if (!isEmployee && advertisementPermitDetail != null) {
            final Assignment assignment = advertisementWorkFlowService.isCscOperator(securityUtils.getCurrentUser())
                    ? advertisementWorkFlowService.getAssignmentByDeptDesigElecWard(advertisementPermitDetail)
                    : null;
            if (assignment == null && advertisementWorkFlowService.getUserPositionByZone() == null)
                errors.reject(NOTEXISTS_POSITION, NOTEXISTS_POSITION);
        }
    }

}
