package org.egov.council.web.controller;

import static org.egov.council.utils.constants.CouncilConstants.CHECK_BUDGET;
import static org.egov.council.utils.constants.CouncilConstants.IMPLEMENTATIONSTATUS;
import static org.egov.council.utils.constants.CouncilConstants.IMPLEMENTATION_STATUS_FINISHED;
import static org.egov.council.utils.constants.CouncilConstants.MODULE_FULLNAME;
import static org.egov.council.utils.constants.CouncilConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.council.utils.constants.CouncilConstants.WARD;
import static org.egov.infra.utils.JsonUtils.toJSON;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.council.autonumber.PreambleNumberGenerator;
import org.egov.council.entity.CouncilPreamble;
import org.egov.council.entity.enums.PreambleType;
import org.egov.council.service.CouncilPreambleService;
import org.egov.council.service.CouncilThirdPartyService;
import org.egov.council.utils.constants.CouncilConstants;
import org.egov.council.web.adaptor.CouncilPreambleJsonAdaptor;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/councilpreamble")
public class CouncilPreambleController extends GenericWorkFlowController {
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
    private static final String COUNCILPREAMBLE_VIEW = "councilpreamble-view";
    private static final String COUNCILPREAMBLE_SEARCH = "councilpreamble-search";
    private static final String COUNCILPREAMBLE_UPDATE_STATUS = "councilpreamble-update-status";
    private static final String COMMONERRORPAGE = "common-error-page";
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

    @Qualifier("fileStoreService")
    @Autowired
    protected FileStoreService fileStoreService;
    @Autowired
    protected FileStoreUtils fileStoreUtils;
    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private AppConfigValueService appConfigValueService;

    private static final Logger LOGGER = Logger
            .getLogger(CouncilPreambleController.class);

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
        model.addAttribute(COUNCIL_PREAMBLE, councilPreamble);
        prepareWorkFlowOnLoad(model, councilPreamble);
        model.addAttribute(CURRENT_STATE, "NEW");

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
        if (errors.hasErrors()) {
            prepareWorkFlowOnLoad(model, councilPreamble);
            return COUNCILPREAMBLE_NEW;
        }

        if (attachments != null && attachments.getSize() > 0) {
            try {
                councilPreamble.setFilestoreid(fileStoreService.store(
                        attachments.getInputStream(),
                        attachments.getOriginalFilename(),
                        attachments.getContentType(),
                        CouncilConstants.MODULE_NAME));
            } catch (IOException e) {
                LOGGER.error("Error in loading documents" + e.getMessage(), e);
            }
        }
        PreambleNumberGenerator preamblenumbergenerator = autonumberServiceBeanResolver
                .getAutoNumberServiceFor(PreambleNumberGenerator.class);
        councilPreamble.setPreambleNumber(preamblenumbergenerator
                .getNextNumber(councilPreamble));
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

        councilPreambleService.create(councilPreamble, approvalPosition,
                approvalComment, workFlowAction);

        String message = messageSource.getMessage("msg.councilPreamble.create",
                new String[] {
                        approverName.concat("~").concat(nextDesignation),
                        councilPreamble.getPreambleNumber() },
                null);
        redirectAttrs.addFlashAttribute(MESSAGE2, message);
        return "redirect:/councilpreamble/result/" + councilPreamble.getId();
    }

    @RequestMapping(value = "/downloadfile/{fileStoreId}")
    public void download(@PathVariable final String fileStoreId,
            final HttpServletResponse response) throws IOException {
        fileStoreUtils.fetchFileAndWriteToStream(fileStoreId,
                CouncilConstants.MODULE_NAME, false, response);
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

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CouncilPreamble councilPreamble,
            final Model model, @RequestParam final MultipartFile attachments,
            final BindingResult errors, final HttpServletRequest request,
            final RedirectAttributes redirectAttrs,
            @RequestParam String workFlowAction) {
        if (errors.hasErrors()) {
            prepareWorkFlowOnLoad(model, councilPreamble);
            model.addAttribute(CURRENT_STATE, councilPreamble
                    .getCurrentState().getValue());
            return COUNCILPREAMBLE_EDIT;
        }
        if (attachments != null && attachments.getSize() > 0) {
            try {
                councilPreamble.setFilestoreid(fileStoreService.store(
                        attachments.getInputStream(),
                        attachments.getOriginalFilename(),
                        attachments.getContentType(),
                        CouncilConstants.MODULE_NAME));
            } catch (IOException e) {
                LOGGER.error(
                        "Error in loading Employee photo" + e.getMessage(), e);
            }
        }

        Long approvalPosition = 0l;
        String approvalComment = StringUtils.EMPTY;
        String message = StringUtils.EMPTY;
        if (request.getParameter(APPROVAL_COMENT) != null)
            approvalComment = request.getParameter(APPROVAL_COMENT);
        if (request.getParameter(WORK_FLOW_ACTION) != null)
            workFlowAction = request.getParameter(WORK_FLOW_ACTION);
        if (request.getParameter(APPROVAL_POSITION) != null
                && !request.getParameter(APPROVAL_POSITION).isEmpty())
            approvalPosition = Long.valueOf(request
                    .getParameter(APPROVAL_POSITION));

        councilPreambleService.update(councilPreamble, approvalPosition,
                approvalComment, workFlowAction);
        if (null != workFlowAction) {
            if (CouncilConstants.WF_STATE_REJECT
                    .equalsIgnoreCase(workFlowAction)) {
                message = getMessage("msg.councilPreamble.reject",
                        councilPreamble);
            } else if (CouncilConstants.WF_APPROVE_BUTTON
                    .equalsIgnoreCase(workFlowAction)) {
                message = getMessage("msg.councilPreamble.success",
                        councilPreamble);
            } else if (CouncilConstants.WF_FORWARD_BUTTON
                    .equalsIgnoreCase(workFlowAction)) {
                message = getMessage("msg.councilPreamble.forward",
                        councilPreamble);
            } else if (CouncilConstants.WF_PROVIDE_INFO_BUTTON
                    .equalsIgnoreCase(workFlowAction)) {
                message = getMessage("msg.councilPreamble.moreInfo",
                        councilPreamble);
            }
            redirectAttrs.addFlashAttribute(MESSAGE2, message);
        }
        return "redirect:/councilpreamble/result/" + councilPreamble.getId();
    }

    @RequestMapping(value = "/updateimplimentaionstatus/{id}", method = RequestMethod.GET)
    public String updateStatus(@PathVariable("id") final Long id, final Model model,
            final HttpServletResponse response) throws IOException {
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
        return "redirect:/councilpreamble/result/" + councilPreamble.getId();
    }

    private String getMessage(String messageLabel,
            final CouncilPreamble councilPreamble) {
        String message;
        message = messageSource.getMessage(messageLabel,
                new String[] { councilPreamble.getPreambleNumber() }, null);
        return message;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model,
            final HttpServletResponse response) throws IOException {
        CouncilPreamble councilPreamble = councilPreambleService.findOne(id);
        WorkflowContainer workFlowContainer = new WorkflowContainer();
        prepareWorkflow(model, councilPreamble, workFlowContainer);
        model.addAttribute("stateType", councilPreamble.getClass()
                .getSimpleName());
        model.addAttribute(CURRENT_STATE, councilPreamble.getCurrentState()
                .getValue());
        model.addAttribute(COUNCIL_PREAMBLE, councilPreamble);
        model.addAttribute(APPLICATION_HISTORY,
                councilThirdPartyService.getHistory(councilPreamble));

        if ("PREAMBLEAPPROVEDFORMOM".equals(councilPreamble.getStatus().getCode())) {
            return COUNCILPREAMBLE_VIEW;
        }
        return COUNCILPREAMBLE_EDIT;

    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilPreamble councilPreamble = councilPreambleService.findOne(id);
        model.addAttribute(COUNCIL_PREAMBLE, councilPreamble);
        model.addAttribute(APPLICATION_HISTORY,
                councilThirdPartyService.getHistory(councilPreamble));

        return COUNCILPREAMBLE_VIEW;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        model.addAttribute(COUNCIL_PREAMBLE, new CouncilPreamble());
        return COUNCILPREAMBLE_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearch(@PathVariable("mode") final String mode, Model model,
            @ModelAttribute final CouncilPreamble councilPreamble) {
        List<CouncilPreamble> searchResultList;

        if ("edit".equalsIgnoreCase(mode)) {
            searchResultList = councilPreambleService
                    .searchFinalizedPreamble(councilPreamble);
        } else {
            searchResultList = councilPreambleService.search(councilPreamble);
        }
        return new StringBuilder("{\"data\":")
                .append(toJSON(searchResultList, CouncilPreamble.class,
                        CouncilPreambleJsonAdaptor.class))
                .append("}").toString();
    }

}
