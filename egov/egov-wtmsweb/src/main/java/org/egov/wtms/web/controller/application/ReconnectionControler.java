package org.egov.wtms.web.controller.application;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.wtms.application.entity.ApplicationDocuments;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.ReConnectionService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.DocumentNames;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.egov.wtms.web.contract.WorkflowContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/application")
public class ReconnectionControler extends GenericConnectionController {

    @Autowired
    private ApplicationTypeService applicationTypeService;
    @Autowired
    private ConnectionDemandService connectionDemandService;
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private WaterConnectionDetailsRepository waterConnectionDetailsRepository;
    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private ReConnectionService reConnectionService;

    @Autowired
    private SecurityUtils securityUtils;

    @ModelAttribute
    public WaterConnectionDetails getWaterConnectionDetails(@PathVariable final String applicationCode) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(applicationCode, ConnectionStatus.CLOSED);
        return waterConnectionDetails;
    }

    public @ModelAttribute("documentNamesList") List<DocumentNames> documentNamesList(
            @ModelAttribute final WaterConnectionDetails waterConnectionDetails) {
        waterConnectionDetails.setApplicationType(applicationTypeService
                .findByCode(WaterTaxConstants.RECONNECTIONCONNECTION));
        return waterConnectionDetailsService.getAllActiveDocumentNames(waterConnectionDetails.getApplicationType());
    }

    @RequestMapping(value = "/reconnection/{applicationCode}", method = RequestMethod.GET)
    public String newForm(final Model model, @PathVariable final String applicationCode,
            final HttpServletRequest request) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(applicationCode, ConnectionStatus.CLOSED);
        return loadViewData(model, request, waterConnectionDetails);

    }

    private String loadViewData(final Model model, final HttpServletRequest request,
            final WaterConnectionDetails waterConnectionDetails) {
        model.addAttribute("stateType", waterConnectionDetails.getClass().getSimpleName());
        model.addAttribute("additionalRule", WaterTaxConstants.RECONNECTIONCONNECTION);
        model.addAttribute("currentUser", waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
        prepareWorkflow(model, waterConnectionDetails, new WorkflowContainer());

        model.addAttribute("waterConnectionDetails", waterConnectionDetails);
        model.addAttribute("feeDetails", connectionDemandService.getSplitFee(waterConnectionDetails));
        model.addAttribute(
                "connectionType",
                waterConnectionDetailsService.getConnectionTypesMap().get(
                        waterConnectionDetails.getConnectionType().name()));
        waterConnectionDetails.setApplicationType(applicationTypeService
                .findByCode(WaterTaxConstants.RECONNECTIONCONNECTION));
        model.addAttribute("applicationHistory", waterConnectionDetailsService.getHistory(waterConnectionDetails));
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
        model.addAttribute("typeOfConnection", WaterTaxConstants.RECONNECTIONCONNECTION);
        /*
         * model.addAttribute("validationMessage",
         * closerConnectionService.validateChangeOfUseConnection
         * (waterConnectionDetails));
         */
        return "reconnection-newForm";
    }

    @RequestMapping(value = "/reconnection/{applicationCode}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final WaterConnectionDetails waterConnectionDetails,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes,
            final HttpServletRequest request, final Model model) {

        String workFlowAction = "";

        if (request.getParameter("mode") != null)
            request.getParameter("mode");

        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");

        Long approvalPosition = 0l;
        String approvalComent = "";

        if (request.getParameter("approvalComent") != null)
            approvalComent = request.getParameter("approvalComent");
        final List<ApplicationDocuments> applicationDocs = new ArrayList<ApplicationDocuments>();
        int i = 0;
        if (!waterConnectionDetails.getApplicationDocs().isEmpty())
            for (final ApplicationDocuments applicationDocument : waterConnectionDetails.getApplicationDocs()) {
                if (applicationDocument.getDocumentNumber() == null && applicationDocument.getDocumentDate() != null) {
                    final String fieldError = "applicationDocs[" + i + "].documentNumber";
                    resultBinder.rejectValue(fieldError, "documentNumber.required");
                }
                if (applicationDocument.getDocumentNumber() != null && applicationDocument.getDocumentDate() == null) {
                    final String fieldError = "applicationDocs[" + i + "].documentDate";
                    resultBinder.rejectValue(fieldError, "documentDate.required");
                } else if (validApplicationDocument(applicationDocument))
                    applicationDocs.add(applicationDocument);
                i++;
            }
        waterConnectionDetails.setApplicationDocs(applicationDocs);
        processAndStoreApplicationDocuments(waterConnectionDetails);
        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
        // waterConnectionDetails.setCloseConnectionType(request.getParameter("closeConnectionType").charAt(0));
        final String addrule = request.getParameter("additionalRule");
        // waterConnectionDetails.setConnectionStatus(ConnectionStatus.CLOSED);
        final WaterConnectionDetails savedWaterConnectionDetails = reConnectionService.updateReConnection(
                waterConnectionDetails, approvalPosition, approvalComent, addrule, workFlowAction);
        model.addAttribute("waterConnectionDetails", savedWaterConnectionDetails);
        final String pathVars = waterConnectionDetails.getApplicationNumber() + ","
                + waterTaxUtils.getApproverUserName(approvalPosition);
        return "redirect:/application/application-success?pathVars=" + pathVars;

    }
}
