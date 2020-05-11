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

import static org.egov.council.utils.constants.CouncilConstants.CHECK_BUDGET;
import static org.egov.council.utils.constants.CouncilConstants.IMPLEMENTATIONSTATUS;
import static org.egov.council.utils.constants.CouncilConstants.IMPLEMENTATION_STATUS_FINISHED;
import static org.egov.council.utils.constants.CouncilConstants.MODULE_FULLNAME;
import static org.egov.council.utils.constants.CouncilConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.council.utils.constants.CouncilConstants.WARD;
import static org.egov.infra.utils.JsonUtils.toJSON;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.council.entity.CouncilPreamble;
import org.egov.council.entity.CouncilSearchRequest;
import org.egov.council.entity.enums.PreambleType;
import org.egov.council.enums.PreambleTypeEnum;
import org.egov.council.service.BidderService;
import org.egov.council.service.CouncilPreambleService;
import org.egov.council.service.CouncilThirdPartyService;
import org.egov.council.utils.constants.CouncilConstants;
import org.egov.council.web.adaptor.CouncilPreambleJsonAdaptor;
import org.egov.egf.commons.FinancialMasterService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/councilpreamble")
public class CouncilPreambleController extends GenericWorkFlowController {
    private static final String COUNCIL_COMMON_WORKFLOW = "CouncilCommonWorkflow";
    private static final String PREAMBLE_NUMBER_AUTO = "PREAMBLE_NUMBER_AUTO";
    private static final String REDIRECT_COUNCILPREAMBLE_RESULT = "redirect:/councilpreamble/result/";
    private static final String MESSAGE2 = "message";
    private static final String APPLICATION_HISTORY = "applicationHistory";
    private static final String APPROVAL_POSITION = "approvalPosition";
    private static final String WORK_FLOW_ACTION = "workFlowAction";
    private static final String APPROVAL_COMENT = "approvalComent";
    private static final String CURRENT_STATE = "currentState";
    private static final String COUNCIL_PREAMBLE = "councilPreamble";
    private static final String COUNCILPREAMBLE_NEW = "councilpreamble-new";
    private static final String COUNCILPREAMBLE_RESULT = "councilpreamble-result";
    private static final String COUNCILPREAMBLE_EDIT = "councilpreamble-edit";
    private static final String COUNCILPREAMBLE_API_EDIT ="councilpreambleapi-edit";
    private static final String COUNCILPREAMBLE_VIEW = "councilpreamble-view";
    private static final String COUNCILPREAMBLE_SEARCH = "councilpreamble-search";
    private static final String COUNCILPREAMBLE_UPDATE_STATUS = "councilpreamble-update-status";
    private static final String COMMONERRORPAGE = "common-error-page";
    private static final String INVALID_APPROVER = "invalid.approver";
    private static final String NOT_AUTHORIZED = "notAuthorized";
    
    
    private static final String COUNCILPREAMBLE_API_VIEW = "councilpreamble-viewnew";
    private static final Logger LOGGER = Logger
            .getLogger(CouncilPreambleController.class);
    @Autowired
    protected FileStoreUtils fileStoreUtils;
    @Autowired
    private CouncilPreambleService councilPreambleService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private DepartmentService deptService;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    @Autowired
    private AutonumberServiceBeanResolver autonumberServiceBeanResolver;
    @Autowired
    private CouncilThirdPartyService councilThirdPartyService;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private AppConfigValueService appConfigValueService;
    @Autowired
    private BidderService bidderService;

    @Autowired
    private FinancialMasterService financialMasterService;

    @ModelAttribute("departments")
    public List<Department> getDepartmentList() {
        return deptService.getAllDepartments();
    }

    @ModelAttribute("wards")
    public List<Boundary> getWardsList() {
        return boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD,
                        REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("URL")
    public String getAppConfigValues() {
        List<AppConfigValues> appConfigValue = appConfigValueService
                .getConfigValuesByModuleAndKey(MODULE_FULLNAME, CHECK_BUDGET);
        if (appConfigValue != null && !appConfigValue.isEmpty())
            return appConfigValueService
                    .getConfigValuesByModuleAndKey(MODULE_FULLNAME,
                            CHECK_BUDGET)
                    .get(0).getValue();
        return "";
    }

    @ModelAttribute("implementationStatus")
    public List<EgwStatus> getImplementationStatusList() {
        return egwStatusHibernateDAO.getStatusByModule(IMPLEMENTATIONSTATUS);
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        CouncilPreamble councilPreamble = new CouncilPreamble();
        councilPreamble.setType(PreambleType.GENERAL);
        model.addAttribute("autoPreambleNoGenEnabled", isAutoPreambleNoGenEnabled());     
        model.addAttribute(COUNCIL_PREAMBLE, councilPreamble);
        model.addAttribute("additionalRule", COUNCIL_COMMON_WORKFLOW);
        prepareWorkFlowOnLoad(model, councilPreamble);
        model.addAttribute(CURRENT_STATE, "NEW");
        model.addAttribute("allowBudgetSearch", true);
        addFiancialData(model);
        return COUNCILPREAMBLE_NEW;
    }

    private void prepareWorkFlowOnLoad(final Model model,
                                       CouncilPreamble councilPreamble) {
        WorkflowContainer workFlowContainer = new WorkflowContainer();
        prepareWorkflow(model, councilPreamble, workFlowContainer);
        model.addAttribute("stateType", councilPreamble.getClass()
                .getSimpleName());
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CouncilPreamble councilPreamble,
                         final BindingResult errors,
                         @RequestParam final MultipartFile attachments, final Model model,
                         final HttpServletRequest request,
                         final RedirectAttributes redirectAttrs,
                         @RequestParam String workFlowAction) {
        validatePreamble(councilPreamble, errors);
        if (errors.hasErrors()) {
            prepareWorkFlowOnLoad(model, councilPreamble);
            return COUNCILPREAMBLE_NEW;
        }

        councilPreamble.setStatus(egwStatusHibernateDAO
                .getStatusByModuleAndCode(CouncilConstants.PREAMBLE_MODULENAME,
                        CouncilConstants.PREAMBLE_STATUS_CREATED));
        councilPreamble.setType(PreambleType.GENERAL);

        Long approvalPosition = 0l;
        String approvalComment = "";
        String approverName = "";
        String nextDesignation = "";
        if (request.getParameter(APPROVAL_COMENT) != null)
            approvalComment = request.getParameter(APPROVAL_COMENT);
        if (request.getParameter(WORK_FLOW_ACTION) != null)
            workFlowAction = request.getParameter(WORK_FLOW_ACTION);
        if (request.getParameter("approverName") != null)
            approverName = request.getParameter("approverName");
        if (request.getParameter("nextDesignation") != null)
            nextDesignation = request.getParameter("nextDesignation");
        if (request.getParameter(APPROVAL_POSITION) != null
                && !request.getParameter(APPROVAL_POSITION).isEmpty())
            approvalPosition = Long.valueOf(request
                    .getParameter(APPROVAL_POSITION));
        if (request.getParameter("budgetBalance") != null) {
            final BigDecimal budgetBalance;
            budgetBalance = BigDecimal.valueOf(Long.valueOf(request.getParameter("budgetBalance")));
            councilPreamble.setBudgetBalance(budgetBalance);
        }
        councilPreambleService.create(councilPreamble, approvalPosition,
                approvalComment, workFlowAction, attachments);
        if (!councilPreamble.isValidApprover()) {
        	model.addAttribute("validationMessage", messageSource.getMessage(INVALID_APPROVER, new String[] {}, null));
            prepareWorkFlowOnLoad(model, councilPreamble);
            return COUNCILPREAMBLE_NEW;
        } else {
        	String message = messageSource.getMessage("msg.councilPreamble.create",
                    new String[]{
                            approverName.concat("~").concat(nextDesignation),
                            councilPreamble.getPreambleNumber()},
                    null);
            redirectAttrs.addFlashAttribute(MESSAGE2, message);
            return REDIRECT_COUNCILPREAMBLE_RESULT.concat(councilPreamble.getId().toString());
        }
    }

    @RequestMapping(value = "/downloadfile/{fileStoreId}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> download(@PathVariable final String fileStoreId) {
        return fileStoreUtils.fileAsResponseEntity(fileStoreId,
                CouncilConstants.MODULE_NAME, false);
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, Model model) {
        CouncilPreamble councilPreamble = councilPreambleService.findOne(id);
        model.addAttribute(COUNCIL_PREAMBLE, councilPreamble);
        model.addAttribute(APPLICATION_HISTORY,
                councilThirdPartyService.getHistory(councilPreamble));
        prepareWorkFlowOnLoad(model, councilPreamble);
        return COUNCILPREAMBLE_RESULT;
    }

    public void validatePreamble(final CouncilPreamble councilPreamble, final BindingResult errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "department", "notempty.preamble.department");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gistOfPreamble", "notempty.preamble.gistOfPreamble");
        if (councilPreamble.getAttachments().getSize() == 0 && councilPreamble.getFilestoreid() == null)
            errors.rejectValue("attachments", "notempty.preamble.attachments");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CouncilPreamble councilPreamble,
                         final Model model, @RequestParam final MultipartFile attachments,
                         final BindingResult errors, final HttpServletRequest request,
                         final RedirectAttributes redirectAttrs,
                         @RequestParam String workFlowAction) {
        validatePreamble(councilPreamble, errors);
        if (errors.hasErrors()) {
            prepareWorkFlowOnLoad(model, councilPreamble);
            model.addAttribute(CURRENT_STATE, councilPreamble
                    .getCurrentState().getValue());
            return COUNCILPREAMBLE_EDIT;
        }
        List<Boundary> wardIdsList = new ArrayList<>();

        String selectedWardIds=request.getParameter("wardsHiddenIds");

        if (StringUtils.isNotEmpty(selectedWardIds)) {
            String[] wardIds = selectedWardIds.split(",");
            
            for (String wrdId : wardIds) {
                if (StringUtils.isNotEmpty(wrdId))
                    wardIdsList.add(boundaryService.getBoundaryById(Long.valueOf(wrdId)));
            }
        }
        councilPreamble.setWards(wardIdsList);

        Long approvalPosition = 0l;
        String approvalComment = StringUtils.EMPTY;
        String message = StringUtils.EMPTY;
        String nextDesignation = "";
        String approverName = "";

        if (request.getParameter(APPROVAL_COMENT) != null)
            approvalComment = request.getParameter(APPROVAL_COMENT);
        if (request.getParameter(WORK_FLOW_ACTION) != null)
            workFlowAction = request.getParameter(WORK_FLOW_ACTION);
        if (request.getParameter(APPROVAL_POSITION) != null
                && !request.getParameter(APPROVAL_POSITION).isEmpty())
            approvalPosition = Long.valueOf(request
                    .getParameter(APPROVAL_POSITION));
        if (request.getParameter("approverName") != null)
            approverName = request.getParameter("approverName");
        if( request.getParameter("nextDesignation") == null)
            nextDesignation=StringUtils.EMPTY;
            else
            nextDesignation = request.getParameter("nextDesignation");

        councilPreambleService.update(councilPreamble, approvalPosition,
                approvalComment, workFlowAction, attachments);
        if (!councilPreamble.isValidApprover()) {
        	model.addAttribute("validationMessage", messageSource.getMessage(INVALID_APPROVER, new String[] {}, null));
        	prepareWorkFlowOnLoad(model, councilPreamble);
            model.addAttribute(CURRENT_STATE, councilPreamble
                    .getCurrentState().getValue());
            return COUNCILPREAMBLE_EDIT;
        } else {
            if (null != workFlowAction) {
                if (CouncilConstants.WF_STATE_REJECT
                        .equalsIgnoreCase(workFlowAction)) {
                    message = getMessage("msg.councilPreamble.reject",nextDesignation,approverName,
                            councilPreamble);
                } else if (CouncilConstants.WF_APPROVE_BUTTON
                        .equalsIgnoreCase(workFlowAction)) {
                    message = getMessage("msg.councilPreamble.success",nextDesignation,approverName,
                            councilPreamble);
                } else if (CouncilConstants.WF_FORWARD_BUTTON
                        .equalsIgnoreCase(workFlowAction)) {
                    message = getMessage("msg.councilPreamble.forward",nextDesignation,approverName,
                            councilPreamble);
                } else if (CouncilConstants.WF_PROVIDE_INFO_BUTTON
                        .equalsIgnoreCase(workFlowAction)) {
                    message = getMessage("msg.councilPreamble.moreInfo",nextDesignation,approverName,
                            councilPreamble);
                }
                redirectAttrs.addFlashAttribute(MESSAGE2, message);
            }
            return REDIRECT_COUNCILPREAMBLE_RESULT.concat(councilPreamble.getId().toString());
        }
    }

    @RequestMapping(value = "/updateimplimentaionstatus/{id}", method = RequestMethod.GET)
    public String updateStatus(@PathVariable("id") final Long id, final Model model,
                               final HttpServletResponse response) {
        CouncilPreamble councilPreamble = councilPreambleService.findOne(id);
        if (null != councilPreamble.getImplementationStatus()
                && IMPLEMENTATION_STATUS_FINISHED.equals(councilPreamble.getImplementationStatus().getCode())) {
            model.addAttribute(MESSAGE2, "msg.councilPreamble.alreadyfinished");
            return COMMONERRORPAGE;
        }
        model.addAttribute(COUNCIL_PREAMBLE, councilPreamble);
        model.addAttribute(APPLICATION_HISTORY,
                councilThirdPartyService.getHistory(councilPreamble));
        return COUNCILPREAMBLE_UPDATE_STATUS;
    }

    @RequestMapping(value = "/updateimplimentaionstatus", method = RequestMethod.POST)
    public String updateImplementationStatus(
            @Valid @ModelAttribute final CouncilPreamble councilPreamble,
            final Model model, final BindingResult errors,
            final HttpServletRequest request,
            final RedirectAttributes redirectAttrs) {

        if (councilPreamble.getImplementationStatus().getCode() != null) {
            councilPreambleService.updateImplementationStatus(councilPreamble);
        }
        redirectAttrs.addFlashAttribute(MESSAGE2, messageSource.getMessage("msg.councilPreamble.update", null, null));
        return REDIRECT_COUNCILPREAMBLE_RESULT.concat(councilPreamble.getId().toString());
    }

    private String getMessage(String messageLabel,String designation,String approver,
                              final CouncilPreamble councilPreamble) {
        String message;
        message = messageSource.getMessage(messageLabel,
                new String[] { councilPreamble.getPreambleNumber() ,approver.concat("~").concat(designation) }, null);
        return message;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model,
            final HttpServletResponse response) {
        CouncilPreamble councilPreamble = councilPreambleService.findOne(id);
        if (!councilPreambleService.isApplicationOwner(councilPreamble))
            return NOT_AUTHORIZED;
        WorkflowContainer workFlowContainer = new WorkflowContainer();
        //Setting pending action based on owner
        if (CouncilConstants.DESIGNATION_MANAGER.equalsIgnoreCase(councilPreamble.getState().getOwnerPosition().getDeptDesig().getDesignation().getName())
                && CouncilConstants.MANAGER_APPROVALPENDING.equalsIgnoreCase(councilPreamble.getState().getNextAction())) {
            workFlowContainer.setPendingActions(councilPreamble.getState().getNextAction());
        }
        if (CouncilConstants.DESIGNATION_COMMISSIONER
                .equalsIgnoreCase(councilPreamble.getState().getOwnerPosition().getDeptDesig().getDesignation().getName())
                && CouncilConstants.COMMISSIONER_APPROVALPENDING.equalsIgnoreCase(councilPreamble.getState().getNextAction())) {
            workFlowContainer.setPendingActions(councilPreamble.getState().getNextAction());
        }
        if(CouncilConstants.REJECTED.equalsIgnoreCase(councilPreamble.getStatus().getCode()))
        {
            model.addAttribute("additionalRule", COUNCIL_COMMON_WORKFLOW);

        }
        prepareWorkflow(model, councilPreamble, workFlowContainer);
        model.addAttribute("stateType", councilPreamble.getClass()
                .getSimpleName());
        model.addAttribute(CURRENT_STATE, councilPreamble.getCurrentState()
                .getValue());
        model.addAttribute(COUNCIL_PREAMBLE, councilPreamble);
        model.addAttribute(APPLICATION_HISTORY,
                councilThirdPartyService.getHistory(councilPreamble));
        model.addAttribute("wfNextAction", councilPreamble.getState().getNextAction());
        if ("PREAMBLEAPPROVEDFORMOM".equals(councilPreamble.getStatus().getCode())
                && !PreambleTypeEnum.WORKS.equals(councilPreamble.getTypeOfPreamble())) {
            return COUNCILPREAMBLE_VIEW;
        }
        if (PreambleTypeEnum.WORKS.equals(councilPreamble.getTypeOfPreamble())) {
            model.addAttribute("bidders", bidderService.getBidderDetails(councilPreamble.getId()));
            if ("PREAMBLEAPPROVEDFORMOM".equals(councilPreamble.getStatus().getCode()))
                return COUNCILPREAMBLE_API_VIEW;
            return COUNCILPREAMBLE_API_EDIT;
        }
        return COUNCILPREAMBLE_EDIT;
                 
    }
   

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilPreamble councilPreamble = councilPreambleService.findOne(id);
        model.addAttribute(COUNCIL_PREAMBLE, councilPreamble);
        model.addAttribute(APPLICATION_HISTORY,
                councilThirdPartyService.getHistory(councilPreamble));
        if (PreambleTypeEnum.WORKS.equals(councilPreamble.getTypeOfPreamble())) {
            model.addAttribute("bidders", bidderService.getBidderDetails(councilPreamble.getId()));
            return COUNCILPREAMBLE_API_VIEW;
        } else
            return COUNCILPREAMBLE_VIEW;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        model.addAttribute("councilSearchRequest", new CouncilSearchRequest());
        return COUNCILPREAMBLE_SEARCH;

    }
    
	@RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String ajaxsearch(@PathVariable("mode") final String mode,
			@Valid @ModelAttribute CouncilSearchRequest councilSearchRequest, BindingResult resultBinder) {

		if (resultBinder.hasErrors()) {
			List<CouncilSearchRequest> errors = new ArrayList<>();
			CouncilSearchRequest searchRequest;
			String criteriaName;
			for (ObjectError error : resultBinder.getAllErrors()) {
				searchRequest = new CouncilSearchRequest();
				criteriaName = error.getCodes()[0].split("\\.")[2];
				searchRequest.setErrorMessage(
						new StringBuilder().append("Invalid input for ").append(criteriaName).toString());
				errors.add(searchRequest);
			}
			return new StringBuilder("{ \"error\":").append(new GsonBuilder().create().toJson(errors)).append("}")
					.toString();
		} else {
			List<CouncilPreamble> searchResultList;
			if ("edit".equalsIgnoreCase(mode)) {
				searchResultList = councilPreambleService.searchFinalizedPreamble(councilSearchRequest);
			} else {
				searchResultList = councilPreambleService.search(councilSearchRequest);
			}
			return new StringBuilder("{\"data\":")
					.append(toJSON(searchResultList, CouncilPreamble.class, CouncilPreambleJsonAdaptor.class))
					.append("}").toString();
		}
	}
    
    public Boolean isAutoPreambleNoGenEnabled() {
        return councilPreambleService.autoGenerationModeEnabled(
                MODULE_FULLNAME, PREAMBLE_NUMBER_AUTO);
    }

    private void addFiancialData(Model model) {
        model.addAttribute("funds", financialMasterService.getAllActiveFunds());
        model.addAttribute("functions", financialMasterService.getFunctions());
        model.addAttribute("financialYears", financialMasterService.getAllActiveFinYears());
    }

}
