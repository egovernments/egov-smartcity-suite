/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wtms.web.controller.application;

import static org.egov.wtms.utils.constants.WaterTaxConstants.ADDNLCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_NUMBER;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CLOSERDIGSIGNPENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CLOSERINITIATED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CLOSERINPROGRESS;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CREATED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_DIGITALSIGNPENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_FEEPAID;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_RECONNDIGSIGNPENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_WOGENERATED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS__RECONNCTIONINPROGRESS;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPROVAL_COMMENT;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPROVAL_POSITION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPROVEWORKFLOWACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ASSISTANT_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CATEGORY_BPL;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CHANGEOFUSE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CLOSINGCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.COMMISSIONER_DESGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.EXECUTIVE_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.FILESTORE_MODULECODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.FILE_STORE_ID_APPLICATION_NUMBER;
import static org.egov.wtms.utils.constants.WaterTaxConstants.JUNIOR_ASSISTANT_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.METERED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MUNICIPAL_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NEWCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PERMENENTCLOSECODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PREVIEWWORKFLOWACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.RECONNECTIONCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ROLE_CLERKFORADONI;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SENIOR_ASSISTANT_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SIGNED_DOCUMENT_PREFIX;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SIGNWORKFLOWACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SUBMITWORKFLOWACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SUPERIENTEND_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_CLOSERACKNOWLDGEENT_BUTTON;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_ESTIMATION_NOTICE_BUTTON;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_RECONNECTIONACKNOWLDGEENT_BUTTON;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_REJECTED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_WORKORDER_BUTTON;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WORKFLOW_ACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WORKFLOW_CLOSUREADDITIONALRULE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WORKFLOW_RECONNCTIONINITIATED;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.egov.commons.entity.ChairPerson;
import org.egov.commons.service.ChairPersonService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.wtms.application.entity.ApplicationDocuments;
import org.egov.wtms.application.entity.ConnectionEstimationDetails;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.entity.WaterDemandConnection;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.ReportGenerationService;
import org.egov.wtms.application.service.WaterDemandConnectionService;
import org.egov.wtms.autonumber.WorkOrderNumberGenerator;
import org.egov.wtms.masters.entity.ConnectionCategory;
import org.egov.wtms.masters.entity.enums.ClosureType;
import org.egov.wtms.masters.service.MeterCostService;
import org.egov.wtms.masters.service.RoadCategoryService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/application")
public class UpdateConnectionController extends GenericConnectionController {

    private final DepartmentService departmentService;

    @Autowired
    private ConnectionDemandService connectionDemandService;
    private static final String ADDITIONALRULE = "additionalRule";
    private static final String APPRIVALPOSITION = "approvalPosition";

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private RoadCategoryService roadCategoryService;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private ChairPersonService chairPersonService;

    @Autowired
    private MeterCostService meterCostService;

    @Autowired
    private ReportGenerationService reportGenerationService;

    @Autowired
    private WaterDemandConnectionService waterDemandConnectionService;

    @Autowired
    public UpdateConnectionController(
            final DepartmentService departmentService, final ConnectionDemandService connectionDemandService,
            final SmartValidator validator) {
        this.departmentService = departmentService;
    }

    @ModelAttribute
    public WaterConnectionDetails getWaterConnectionDetails(@PathVariable final String applicationNumber) {
        return waterConnectionDetailsService.findByApplicationNumber(applicationNumber);
    }

    @RequestMapping(value = "/update/{applicationNumber}", method = RequestMethod.GET)
    public String view(final Model model, @PathVariable final String applicationNumber,
            final HttpServletRequest request) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByApplicationNumber(applicationNumber);
        return loadViewData(model, request, waterConnectionDetails);
    }

    private String loadViewData(final Model model, final HttpServletRequest request,
            final WaterConnectionDetails waterConnectionDetails) {
        model.addAttribute("stateType", waterConnectionDetails.getClass().getSimpleName());
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        Boolean isCommissionerLoggedIn = Boolean.FALSE;
        Boolean isSanctionedDetailEnable = isCommissionerLoggedIn;
        final String loggedInUserDesignation = waterTaxUtils.loggedInUserDesignation(waterConnectionDetails);
        if (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_FEEPAID)
                || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_DIGITALSIGNPENDING)
                || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CLOSERDIGSIGNPENDING)
                || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_RECONNDIGSIGNPENDING)
                || (waterConnectionDetails.getStatus().getCode()
                        .equals(APPLICATION_STATUS__RECONNCTIONINPROGRESS)
                        || waterConnectionDetails.getStatus().getCode()
                                .equals(APPLICATION_STATUS_CLOSERINPROGRESS))
                        &&
                        !waterConnectionDetails.getState().getValue().equals(WF_STATE_REJECTED))
            workflowContainer.setCurrentDesignation(loggedInUserDesignation);
        if (loggedInUserDesignation != null
                && (loggedInUserDesignation.equalsIgnoreCase(SENIOR_ASSISTANT_DESIGN) ||
                        loggedInUserDesignation.equalsIgnoreCase(JUNIOR_ASSISTANT_DESIGN))
                && waterConnectionDetails.getStatus() != null
                && (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CREATED) ||
                        waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CLOSERINITIATED)
                        ||
                        waterConnectionDetails.getStatus().getCode().equals(WORKFLOW_RECONNCTIONINITIATED))
                || loggedInUserDesignation.equalsIgnoreCase(ASSISTANT_ENGINEER_DESIGN) &&
                        (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CLOSERINITIATED)
                                ||
                                waterConnectionDetails.getStatus().getCode()
                                        .equals(WORKFLOW_RECONNCTIONINITIATED)))
            workflowContainer.setCurrentDesignation(null);
        if (waterConnectionDetails.getCloseConnectionType() != null
                && waterConnectionDetails.getReConnectionReason() == null) {
            model.addAttribute(ADDITIONALRULE, WORKFLOW_CLOSUREADDITIONALRULE);
            workflowContainer.setAdditionalRule(WORKFLOW_CLOSUREADDITIONALRULE);
            if (waterConnectionDetails.getCloseConnectionType().equals(PERMENENTCLOSECODE))
                waterConnectionDetails.setCloseConnectionType(ClosureType.Permanent.getName());
            else
                waterConnectionDetails.setCloseConnectionType(ClosureType.Temporary.getName());

            model.addAttribute("radioButtonMap", Arrays.asList(ClosureType.values()));
        }
        model.addAttribute("applicationDocList",
                waterConnectionDetailsService.getApplicationDocForExceptClosureAndReConnection(waterConnectionDetails));
        if (waterConnectionDetails.getCloseConnectionType() != null
                && (waterConnectionDetails.getReConnectionReason() == null
                        || waterConnectionDetails.getReConnectionReason() != null))
            if (!waterConnectionDetails.getApplicationDocs().isEmpty())
                for (final ApplicationDocuments appDoc : waterConnectionDetails.getApplicationDocs()) {
                    if (appDoc.getDocumentNames() != null && appDoc.getDocumentNames().getApplicationType().getCode()
                            .equals(CLOSINGCONNECTION)) {
                        final List<ApplicationDocuments> tempListDoc = new ArrayList<>();
                        tempListDoc.add(appDoc);
                        model.addAttribute("appforDocumentList", tempListDoc);
                    }
                    if (appDoc.getDocumentNames() != null && appDoc.getDocumentNames().getApplicationType().getCode()
                            .equals(RECONNECTIONCONNECTION)) {
                        final List<ApplicationDocuments> tempListDocrecon = new ArrayList<>();
                        tempListDocrecon.add(appDoc);
                        model.addAttribute("appforDocumentList", tempListDocrecon);
                    }

                }
            else
                model.addAttribute("appforDocumentList", waterConnectionDetails.getApplicationDocs());

        if (waterConnectionDetails.getCloseConnectionType() != null
                && waterConnectionDetails.getReConnectionReason() != null) {
            model.addAttribute(ADDITIONALRULE, RECONNECTIONCONNECTION);
            workflowContainer.setAdditionalRule(RECONNECTIONCONNECTION);

        } else if ("".equals(workflowContainer.getAdditionalRule()) || workflowContainer.getAdditionalRule() == null) {
            workflowContainer.setAdditionalRule(waterConnectionDetails.getApplicationType().getCode());
            model.addAttribute(ADDITIONALRULE, waterConnectionDetails.getApplicationType().getCode());

        }
        prepareWorkflow(model, waterConnectionDetails, workflowContainer);
        model.addAttribute("currentState", waterConnectionDetails.getCurrentState().getValue());
        model.addAttribute("currentUser", waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
        model.addAttribute("waterConnectionDetails", waterConnectionDetails);
        model.addAttribute("feeDetails", connectionDemandService.getSplitFee(waterConnectionDetails));
        model.addAttribute("connectionType", waterConnectionDetailsService.getConnectionTypesMap()
                .get(waterConnectionDetails.getConnectionType().name()));
        model.addAttribute("applicationHistory", waterConnectionDetailsService.getHistory(waterConnectionDetails));
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
        if (waterConnectionDetails.getStatus() != null && waterConnectionDetails.getStatus().getCode()
                .equalsIgnoreCase(APPLICATION_STATUS_WOGENERATED))
            model.addAttribute("meterCostMasters",
                    meterCostService.findByPipeSize(waterConnectionDetails.getPipeSize()));

        if (waterConnectionDetails.getStatus() != null && waterConnectionDetails.getStatus().getCode()
                .equalsIgnoreCase(APPLICATION_STATUS_FEEPAID)) {
            final ChairPerson chairPerson = chairPersonService.getActiveChairPersonAsOnCurrentDate();
            model.addAttribute("chairPerson", chairPerson);
        }
        if (CHANGEOFUSE.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())) {
            waterConnectionDetails.getConnection().setMeter(null);
            waterConnectionDetails.getConnection().setInitialReading(null);
            waterConnectionDetails.getConnection().setMeterSerialNumber("");
        }
        appendModeBasedOnApplicationCreator(model, request, waterConnectionDetails);
        if (loggedInUserDesignation != null && !"".equals(loggedInUserDesignation)
                && loggedInUserDesignation.equalsIgnoreCase(COMMISSIONER_DESGN)
                && (waterConnectionDetails.getStatus()
                        .getCode().equals(APPLICATION_STATUS_DIGITALSIGNPENDING) ||
                        waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_FEEPAID)
                        || waterConnectionDetails.getStatus().getCode()
                                .equals(APPLICATION_STATUS_CLOSERDIGSIGNPENDING)
                        || waterConnectionDetails.getStatus().getCode()
                                .equals(APPLICATION_STATUS__RECONNCTIONINPROGRESS)
                        || waterConnectionDetails.getStatus().getCode()
                                .equals(APPLICATION_STATUS_CLOSERINPROGRESS)
                        || waterConnectionDetails.getStatus().getCode()
                                .equals(APPLICATION_STATUS_RECONNDIGSIGNPENDING)))
            isCommissionerLoggedIn = Boolean.TRUE;
        if (loggedInUserDesignation.equalsIgnoreCase(COMMISSIONER_DESGN)
                || loggedInUserDesignation.equalsIgnoreCase(WaterTaxConstants.DEPUTY_ENGINEER_DESIGN)
                || loggedInUserDesignation.equalsIgnoreCase(EXECUTIVE_ENGINEER_DESIGN)
                || loggedInUserDesignation.equalsIgnoreCase(MUNICIPAL_ENGINEER_DESIGN)
                || loggedInUserDesignation.equalsIgnoreCase(SUPERIENTEND_ENGINEER_DESIGN) &&
                        waterConnectionDetails.getApprovalNumber() == null)
            isSanctionedDetailEnable = Boolean.TRUE;

        final BigDecimal waterTaxDueforParent = waterConnectionDetailsService.getTotalAmount(waterConnectionDetails);
        model.addAttribute("waterTaxDueforParent", waterTaxDueforParent);
        if (loggedInUserDesignation != null && (loggedInUserDesignation.equals(SENIOR_ASSISTANT_DESIGN) ||
                loggedInUserDesignation.equals(JUNIOR_ASSISTANT_DESIGN))
                && waterConnectionDetails.getStatus() != null
                && (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CREATED)
                        || waterConnectionDetails.getStatus().getCode()
                                .equals(APPLICATION_STATUS_CLOSERINITIATED)
                        ||
                        waterConnectionDetails.getStatus().getCode().equals(WORKFLOW_RECONNCTIONINITIATED))
                || loggedInUserDesignation.equalsIgnoreCase(ASSISTANT_ENGINEER_DESIGN) &&
                        (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CLOSERINITIATED)
                                ||
                                waterConnectionDetails.getStatus().getCode()
                                        .equals(WORKFLOW_RECONNCTIONINITIATED)))
            model.addAttribute("currentDesignation", "");
        else
            model.addAttribute("currentDesignation", loggedInUserDesignation);
        model.addAttribute("isCommissionerLoggedIn", isCommissionerLoggedIn);
        model.addAttribute("isSanctionedDetailEnable", isSanctionedDetailEnable);
        model.addAttribute("usageTypes", usageTypeService.getActiveUsageTypes());
        model.addAttribute("connectionCategories", connectionCategoryService.getAllActiveConnectionCategory());
        model.addAttribute("pipeSizes", pipeSizeService.getAllActivePipeSize());
        model.addAttribute("typeOfConnection", waterConnectionDetails.getApplicationType().getCode());
        return "newconnection-edit";
    }

    private void appendModeBasedOnApplicationCreator(final Model model, final HttpServletRequest request,
            final WaterConnectionDetails waterConnectionDetails) {
        final Boolean recordCreatedBYNonEmployee = waterTaxUtils.getCurrentUserRole(waterConnectionDetails
                .getCreatedBy());
        // if record from csc to Clerk
        // TODO: as off now using anonymous created for MeeSeva after we need to
        // craete role for this and add in appconfig for
        // recordCreatedBYNonEmployee API
        // so it will work as CSC Operator record
        if ((recordCreatedBYNonEmployee || userService.getUserById(waterConnectionDetails.getCreatedBy().getId())
                .getUsername().equals("anonymous"))
                && null == request.getAttribute("mode") && waterConnectionDetails.getState().getHistory().isEmpty()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("approvalPositionExist", waterConnectionDetailsService
                    .getApprovalPositionByMatrixDesignation(waterConnectionDetails, 0l, waterConnectionDetails
                            .getApplicationType().getCode(), "edit", ""));
        }
        // "edit" mode for AE inbox record FROM CSC and Record from Clerk
        else if (recordCreatedBYNonEmployee && request.getAttribute("mode") == null
                && waterConnectionDetails.getState().getHistory() != null && waterConnectionDetails.getStatus() != null) {
            String additionalRule = "";
            if (waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CLOSERINITIATED))
                additionalRule = WORKFLOW_CLOSUREADDITIONALRULE;
            else if (waterConnectionDetails.getStatus().getCode().equals(WORKFLOW_RECONNCTIONINITIATED))
                additionalRule = RECONNECTIONCONNECTION;
            model.addAttribute("mode", "fieldInspection");
            model.addAttribute("approvalPositionExist", waterConnectionDetailsService
                    .getApprovalPositionByMatrixDesignation(waterConnectionDetails, 0l, additionalRule, "fieldInspection", ""));
            model.addAttribute("roadCategoryList", roadCategoryService.getAllRoadCategory());

        } else if (!recordCreatedBYNonEmployee
                && waterConnectionDetails.getStatus() != null
                && waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CREATED)) {
            model.addAttribute("mode", "fieldInspection");
            model.addAttribute("approvalPositionExist", waterConnectionDetailsService
                    .getApprovalPositionByMatrixDesignation(waterConnectionDetails, 0l, waterConnectionDetails
                            .getApplicationType().getCode(), "fieldInspection", ""));
            model.addAttribute("roadCategoryList", roadCategoryService.getAllRoadCategory());

        } else if (waterConnectionDetails.getCloseConnectionType() != null
                && waterConnectionDetails.getReConnectionReason() == null)
            model.addAttribute("approvalPositionExist", waterConnectionDetailsService
                    .getApprovalPositionByMatrixDesignation(waterConnectionDetails, 0l, "CLOSECONNECTION", "", ""));

        else if (waterConnectionDetails.getCloseConnectionType() != null
                && waterConnectionDetails.getReConnectionReason() != null)
            model.addAttribute("approvalPositionExist", waterConnectionDetailsService
                    .getApprovalPositionByMatrixDesignation(waterConnectionDetails, 0l,
                            RECONNECTIONCONNECTION, "", ""));
        else
            model.addAttribute("approvalPositionExist", waterConnectionDetailsService
                    .getApprovalPositionByMatrixDesignation(waterConnectionDetails, 0l, waterConnectionDetails
                            .getApplicationType().getCode(), "", ""));
        if (waterConnectionDetails.getCurrentState().getValue().equals("Rejected"))
            model.addAttribute("mode", "");

        if (waterConnectionDetails.getCloseConnectionType() != null
                && waterConnectionDetails.getReConnectionReason() == null
                && waterConnectionDetails.getStatus().getCode()
                        .equals(APPLICATION_STATUS_CLOSERINITIATED))
            model.addAttribute("mode", "closereditForAE");
        if ((waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CLOSERINPROGRESS)
                || waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_CLOSERINITIATED))
                && waterConnectionDetails.getReConnectionReason() == null
                && waterConnectionDetails.getCloseConnectionType() != null
                && waterConnectionDetails.getState().getValue().equals("Rejected"))
            model.addAttribute("mode", "closeredit");
        if (waterConnectionDetails.getReConnectionReason() != null
                && waterConnectionDetails.getState().getValue().equals("Rejected")
                && waterConnectionDetails.getReConnectionReason() != null
                && waterConnectionDetails.getStatus().getCode()
                        .equals(WORKFLOW_RECONNCTIONINITIATED))
            model.addAttribute("mode", "reconnectioneredit");
        if (waterConnectionDetails.getReConnectionReason() != null
                && waterConnectionDetails.getStatus().getCode().equals(WORKFLOW_RECONNCTIONINITIATED))
            model.addAttribute("mode", "reconEditForAE");
    }

    @RequestMapping(value = "/update/{applicationNumber}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute WaterConnectionDetails waterConnectionDetails,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes,
            final HttpServletRequest request, final Model model, @RequestParam("files") final MultipartFile[] files) {

        String mode = "";
        String workFlowAction = "";
        Double donationCharges = 0d;
        final String sourceChannel = request.getParameter("Source");

        if (request.getParameter("donationCharges") != null)
            donationCharges = Double.valueOf(request.getParameter("donationCharges"));
        if (request.getParameter("mode") != null)
            mode = request.getParameter("mode");

        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");
        request.getSession().setAttribute(WORKFLOW_ACTION, workFlowAction);

        // For Submit Button
        if (waterConnectionDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_CREATED)
                && mode.equalsIgnoreCase("fieldInspection"))
            if (workFlowAction.equalsIgnoreCase(SUBMITWORKFLOWACTION)) {
                final ConnectionCategory connectionCategory = connectionCategoryService
                        .findOne(waterConnectionDetails.getCategory().getId());
                if (connectionCategory != null
                        && !connectionCategory.getCode().equalsIgnoreCase(CATEGORY_BPL)
                        && waterConnectionDetails.getBplCardHolderName() != null)
                    waterConnectionDetails.setBplCardHolderName(waterConnectionDetails.getBplCardHolderName());
                populateEstimationDetails(waterConnectionDetails);
                final WaterDemandConnection waterDemandConnection = waterTaxUtils.getCurrentDemand(waterConnectionDetails);
                if (waterConnectionDetails.getConnectionType().toString().equalsIgnoreCase(METERED)
                        && waterConnectionDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_CREATED))
                    waterConnectionDetails.setDonationCharges(donationCharges);
                waterDemandConnection.setDemand(connectionDemandService.createDemand(waterConnectionDetails));
                waterDemandConnection.setWaterConnectionDetails(waterConnectionDetails);
                waterConnectionDetails.addWaterDemandConnection(waterDemandConnection);
                waterDemandConnectionService.createWaterDemandConnection(waterDemandConnection);
                waterConnectionDetailsService.save(waterConnectionDetails);
                waterConnectionDetailsService.getCurrentSession().flush();
                // Attach any other file during field inspection and estimation
                final Set<FileStoreMapper> fileStoreSet = addToFileStore(files);
                Iterator<FileStoreMapper> fsIterator = null;

                if (fileStoreSet != null && !fileStoreSet.isEmpty())
                    fsIterator = fileStoreSet.iterator();
                if (fsIterator != null && fsIterator.hasNext())
                    waterConnectionDetails.setFileStore(fsIterator.next());
            } else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)) {
                waterConnectionDetailsService.getCurrentSession().evict(waterConnectionDetails);
                waterConnectionDetails = waterConnectionDetailsService.findBy(waterConnectionDetails.getId());
            }

        Long approvalPosition = 0l;
        String approvalComent = "";

        if (request.getParameter("approvalComent") != null)
            approvalComent = request.getParameter("approvalComent");

        if (workFlowAction != null && workFlowAction.equals(APPROVEWORKFLOWACTION)
                && waterConnectionDetails.getStatus() != null && waterConnectionDetails.getStatus().getCode() != null
                && waterConnectionDetails.getStatus().getCode().equals(APPLICATION_STATUS_FEEPAID))
            validateSanctionDetails(waterConnectionDetails, resultBinder);
        if (request.getParameter(APPRIVALPOSITION) != null && !request.getParameter(APPRIVALPOSITION).isEmpty())
            approvalPosition = Long.valueOf(request.getParameter(APPRIVALPOSITION));
        // For Get Configured ApprovalPosition from workflow history
        if (approvalPosition == null || approvalPosition.equals(Long.valueOf(0)))
            if (waterConnectionDetails.getCloseConnectionType() != null)
                approvalPosition = waterConnectionDetailsService.getApprovalPositionByMatrixDesignation(
                        waterConnectionDetails, approvalPosition, request.getParameter(ADDITIONALRULE), mode,
                        workFlowAction);
            else
                approvalPosition = waterConnectionDetailsService.getApprovalPositionByMatrixDesignation(
                        waterConnectionDetails, approvalPosition, waterConnectionDetails.getApplicationType().getCode(),
                        mode, workFlowAction);
        // to get modes to hide and show details in every user inbox

        request.getSession().setAttribute("APPROVAL_POSITION", approvalPosition);

        if (request.getParameter(APPRIVALPOSITION) == null)
            appendModeBasedOnApplicationCreator(model, request, waterConnectionDetails);

        if ((approvalPosition == null || approvalPosition.equals(Long.valueOf(0)))
                && request.getParameter(APPRIVALPOSITION) != null
                && !request.getParameter(APPRIVALPOSITION).isEmpty())
            approvalPosition = Long.valueOf(request.getParameter(APPRIVALPOSITION));
        // For ReConnection and Closure Connection
        if ((workFlowAction.equals(WFLOW_ACTION_STEP_REJECT)
                || workFlowAction.equalsIgnoreCase(WF_RECONNECTIONACKNOWLDGEENT_BUTTON))
                && (waterConnectionDetails.getStatus().getCode()
                        .equals(WORKFLOW_RECONNCTIONINITIATED)
                        || waterConnectionDetails.getStatus().getCode()
                                .equals(APPLICATION_STATUS__RECONNCTIONINPROGRESS)
                        || waterConnectionDetails.getStatus().getCode()
                                .equals(APPLICATION_STATUS_CLOSERINPROGRESS)
                        || waterConnectionDetails.getStatus().getCode()
                                .equals(APPLICATION_STATUS_CLOSERINITIATED)))
            approvalPosition = waterTaxUtils.getApproverPosition(ROLE_CLERKFORADONI,
                    waterConnectionDetails);
        if (approvalPosition == null)
            throw new ValidationException("err.nouserdefinedforworkflow");
        if (!resultBinder.hasErrors()) {
            try {
                // For Closure Connection
                if (waterConnectionDetails.getCloseConnectionType() != null)
                    if (waterConnectionDetails.getCloseConnectionType().equals(PERMENENTCLOSECODE))
                        waterConnectionDetails.setCloseConnectionType(ClosureType.Permanent.getName());
                    else
                        waterConnectionDetails.setCloseConnectionType(ClosureType.Temporary.getName());

                if (null != workFlowAction)
                    if (workFlowAction.equalsIgnoreCase(PREVIEWWORKFLOWACTION)
                            && (waterConnectionDetails.getApplicationType().getCode()
                                    .equals(NEWCONNECTION)
                                    || waterConnectionDetails.getApplicationType().getCode()
                                            .equals(ADDNLCONNECTION)
                                    || waterConnectionDetails.getApplicationType().getCode()
                                            .equals(CHANGEOFUSE)))
                        return "redirect:/application/workorder?pathVar="
                                + waterConnectionDetails.getApplicationNumber();
                    else if (workFlowAction.equalsIgnoreCase(PREVIEWWORKFLOWACTION)
                            && waterConnectionDetails.getApplicationType().getCode()
                                    .equals(CLOSINGCONNECTION))
                        return "redirect:/application/acknowlgementNotice?pathVar="
                                + waterConnectionDetails.getApplicationNumber();
                    else if (workFlowAction.equalsIgnoreCase(PREVIEWWORKFLOWACTION)
                            && waterConnectionDetails.getApplicationType().getCode()
                                    .equals(RECONNECTIONCONNECTION))
                        return "redirect:/application/ReconnacknowlgementNotice?pathVar="
                                + waterConnectionDetails.getApplicationNumber();
                    else if (workFlowAction.equals(SIGNWORKFLOWACTION)) { // Sign
                        WaterConnectionDetails upadtedWaterConnectionDetails = null;
                        final WorkOrderNumberGenerator workOrderGen = beanResolver
                                .getAutoNumberServiceFor(WorkOrderNumberGenerator.class);
                        if (waterConnectionDetails.getApplicationType().getCode()
                                .equals(NEWCONNECTION)
                                || waterConnectionDetails.getApplicationType().getCode()
                                        .equals(ADDNLCONNECTION)
                                || waterConnectionDetails.getApplicationType().getCode()
                                        .equals(CHANGEOFUSE)) {
                            waterConnectionDetails.setWorkOrderDate(new Date());
                            waterConnectionDetails.setWorkOrderNumber(workOrderGen.generateWorkOrderNumber());
                        }
                        final String cityMunicipalityName = (String) request.getSession()
                                .getAttribute("citymunicipalityname");
                        final String districtName = (String) request.getSession().getAttribute("districtName");
                        ReportOutput reportOutput;
                        reportOutput = getReportOutputObject(waterConnectionDetails, workFlowAction,
                                cityMunicipalityName, districtName);

                        // Setting FileStoreMap object while Commissioner Signs
                        // the document
                        if (reportOutput != null) {
                            String fileName;
                            if (waterConnectionDetails.getApplicationType().getCode()
                                    .equals(CLOSINGCONNECTION))
                                fileName = SIGNED_DOCUMENT_PREFIX
                                        + waterConnectionDetails.getApplicationNumber() + ".pdf";
                            else if (waterConnectionDetails.getApplicationType().getCode()
                                    .equals(RECONNECTIONCONNECTION))
                                fileName = SIGNED_DOCUMENT_PREFIX
                                        + waterConnectionDetails.getApplicationNumber() + ".pdf";
                            else
                                fileName = SIGNED_DOCUMENT_PREFIX
                                        + waterConnectionDetails.getWorkOrderNumber() + ".pdf";

                            final InputStream fileStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
                            final FileStoreMapper fileStore = fileStoreService.store(fileStream, fileName,
                                    "application/pdf", FILESTORE_MODULECODE);
                            waterConnectionDetails.setFileStore(fileStore);
                            upadtedWaterConnectionDetails = waterConnectionDetailsService
                                    .updateWaterConnectionDetailsWithFileStore(waterConnectionDetails);
                        }
                        model.addAttribute("fileStoreIds",
                                upadtedWaterConnectionDetails.getFileStore().getFileStoreId());
                        model.addAttribute("ulbCode", ApplicationThreadLocals.getCityCode());
                        final HttpSession session = request.getSession();
                        session.setAttribute(MODE, mode);
                        session.setAttribute(APPROVAL_POSITION, approvalPosition);
                        session.setAttribute(APPROVAL_COMMENT, approvalComent);
                        session.setAttribute(APPLICATION_NUMBER,
                                upadtedWaterConnectionDetails.getApplicationNumber());
                        final Map<String, String> fileStoreIdsApplicationNoMap = new HashMap<>();
                        fileStoreIdsApplicationNoMap.put(upadtedWaterConnectionDetails.getFileStore().getFileStoreId(),
                                upadtedWaterConnectionDetails.getApplicationNumber());
                        session.setAttribute(FILE_STORE_ID_APPLICATION_NUMBER,
                                fileStoreIdsApplicationNoMap);
                        model.addAttribute("isDigitalSignatureEnabled", waterTaxUtils.isDigitalSignatureEnabled());
                        return "newConnection-digitalSignatureRedirection";
                    } else
                        waterConnectionDetailsService.updateWaterConnection(waterConnectionDetails, approvalPosition,
                                approvalComent, waterConnectionDetails.getApplicationType().getCode(), workFlowAction,
                                mode, null, sourceChannel);
            } catch (final ValidationException e) {
                throw new ValidationException(e.getMessage());
            }
            if (workFlowAction != null && !workFlowAction.isEmpty()
                    && workFlowAction.equalsIgnoreCase(WF_ESTIMATION_NOTICE_BUTTON))// Generate
                // Esitmation
                // Notice
                // Button
                return "redirect:/application/estimationNotice?pathVar="
                        + waterConnectionDetails.getApplicationNumber();

            if (null != workFlowAction && !workFlowAction.isEmpty()
                    && workFlowAction.equalsIgnoreCase(WF_WORKORDER_BUTTON))// For
                // Generate
                // WorkOrder
                return "redirect:/application/workorder?pathVar=" + waterConnectionDetails.getApplicationNumber();
            if (workFlowAction != null && !workFlowAction.isEmpty()
                    && workFlowAction.equalsIgnoreCase(WF_CLOSERACKNOWLDGEENT_BUTTON))// For
                // Closure
                // Connection
                // Generate
                // Acknowlegement
                return "redirect:/application/acknowlgementNotice?pathVar="

                        + waterConnectionDetails.getApplicationNumber();

            if (workFlowAction != null && !workFlowAction.isEmpty()
                    && workFlowAction.equalsIgnoreCase(WF_RECONNECTIONACKNOWLDGEENT_BUTTON))// For
                // ReConnection
                // Generate
                // ReConnection
                // Ack
                return "redirect:/application/ReconnacknowlgementNotice?pathVar="

                        + waterConnectionDetails.getApplicationNumber();

            final Assignment currentUserAssignment = assignmentService
                    .getPrimaryAssignmentForGivenRange(securityUtils.getCurrentUser().getId(), new Date(), new Date());
            String nextDesign = "";
            Assignment assignObj = null;
            List<Assignment> asignList = null;
            if (approvalPosition != null)
                assignObj = assignmentService.getPrimaryAssignmentForPositon(approvalPosition);
            if (assignObj != null) {
                asignList = new ArrayList<>();
                asignList.add(assignObj);
            } else if (assignObj == null && approvalPosition != null)
                asignList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());
            nextDesign = !asignList.isEmpty() ? asignList.get(0).getDesignation().getName() : "";

            final String pathVars = waterConnectionDetails.getApplicationNumber() + ","
                    + waterTaxUtils.getApproverName(approvalPosition) + ","
                    + (currentUserAssignment != null ? currentUserAssignment.getDesignation().getName() : "") + ","
                    + (nextDesign != null ? nextDesign : "");
            return "redirect:/application/application-success?pathVars=" + pathVars;
        } else {
            if (waterConnectionDetails.getStatus().getCode()
                    .equalsIgnoreCase(APPLICATION_STATUS_WOGENERATED))
                model.addAttribute("meterFocus", true);
            return loadViewData(model, request, waterConnectionDetails);
        }
    }

    private ReportOutput getReportOutputObject(final WaterConnectionDetails waterConnectionDetails, final String workFlowAction,
            final String cityMunicipalityName, final String districtName) {
        ReportOutput reportOutput;
        if (waterConnectionDetails.getApplicationType().getCode().equals(CLOSINGCONNECTION))
            reportOutput = reportGenerationService.generateClosureConnectionReport(waterConnectionDetails,
                    workFlowAction, cityMunicipalityName, districtName);
        else if (waterConnectionDetails.getApplicationType().getCode()
                .equals(RECONNECTIONCONNECTION))
            reportOutput = reportGenerationService.generateReconnectionReport(waterConnectionDetails, workFlowAction,
                    cityMunicipalityName, districtName);
        else
            reportOutput = reportGenerationService.getReportOutput(waterConnectionDetails, workFlowAction,
                    cityMunicipalityName, districtName);

        return reportOutput;
    }

    private void validateSanctionDetails(final WaterConnectionDetails waterConnectionDetails,
            final BindingResult errors) {

        if (waterConnectionDetails.getApprovalNumber() == null)
            errors.rejectValue("approvalNumber", "approvalNumber.required");

        if (waterConnectionDetails.getApprovalDate() == null)
            errors.rejectValue("approvalDate", "approvalDate.required");
    }

    private void populateEstimationDetails(final WaterConnectionDetails waterConnectionDetails) {
        final List<ConnectionEstimationDetails> estimationDetails = new ArrayList<>();
        if (!waterConnectionDetails.getEstimationDetails().isEmpty())
            for (final ConnectionEstimationDetails estimationDetail : waterConnectionDetails.getEstimationDetails())
                if (validEstimationDetail(estimationDetail)) {
                    estimationDetail.setWaterConnectionDetails(waterConnectionDetails);
                    estimationDetails.add(estimationDetail);
                }

        waterConnectionDetails.getEstimationDetails().clear();
        waterConnectionDetails.setEstimationDetails(estimationDetails);
    }

    private boolean validEstimationDetail(final ConnectionEstimationDetails connectionEstimationDetails) {
        if (connectionEstimationDetails != null && connectionEstimationDetails.getItemDescription() != null)
            return true;
        return false;
    }

}