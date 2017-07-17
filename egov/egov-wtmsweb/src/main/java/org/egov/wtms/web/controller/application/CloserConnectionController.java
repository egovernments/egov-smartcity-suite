/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
package org.egov.wtms.web.controller.application;

import static org.egov.wtms.utils.constants.WaterTaxConstants.MODE;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pims.commons.Position;
import org.egov.wtms.application.entity.ApplicationDocuments;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.CloserConnectionService;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.ConnectionCategory;
import org.egov.wtms.masters.entity.DocumentNames;
import org.egov.wtms.masters.entity.PipeSize;
import org.egov.wtms.masters.entity.UsageType;
import org.egov.wtms.masters.entity.enums.ClosureType;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
public class CloserConnectionController extends GenericConnectionController {

    private final WaterConnectionDetailsService waterConnectionDetailsService;

    private final DepartmentService departmentService;

    @Autowired
    private ConnectionDemandService connectionDemandService;
    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private CloserConnectionService closerConnectionService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource wcmsMessageSource;

    @Autowired
    public CloserConnectionController(final WaterConnectionDetailsService waterConnectionDetailsService,
            final DepartmentService departmentService, final ConnectionDemandService connectionDemandService,
            final SmartValidator validator) {
        this.waterConnectionDetailsService = waterConnectionDetailsService;
        this.departmentService = departmentService;
    }

    @ModelAttribute
    public WaterConnectionDetails getWaterConnectionDetails(@PathVariable final String applicationCode) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(applicationCode, ConnectionStatus.ACTIVE);
        return waterConnectionDetails;
    }

    @ModelAttribute("connectionCategories")
    public List<ConnectionCategory> connectionCategories() {
        return connectionCategoryService.getAllActiveConnectionCategory();
    }

    @ModelAttribute("usageTypes")
    public List<UsageType> usageTypes() {
        return usageTypeService.getActiveUsageTypes();
    }

    @ModelAttribute("pipeSizes")
    public List<PipeSize> pipeSizes() {
        return pipeSizeService.getAllActivePipeSize();
    }

    /*
     * public @ModelAttribute("documentNamesList") List<DocumentNames> documentNamesList(
     * @ModelAttribute final WaterConnectionDetails waterConnectionDetails) {
     * waterConnectionDetails.setApplicationType(applicationTypeService .findByCode(WaterTaxConstants.CLOSINGCONNECTION)); return
     * waterConnectionDetailsService .getAllActiveDocumentNames(waterConnectionDetails.getApplicationType()); }
     */

    @RequestMapping(value = "/close/{applicationCode}", method = RequestMethod.GET)
    public String view(final Model model, @PathVariable final String applicationCode,
            final HttpServletRequest request) {
        final WaterConnectionDetails waterConnectionDetails = getWaterConnectionDetails(applicationCode);

        return loadViewData(model, request, waterConnectionDetails);
    }

    @Transactional(readOnly = true)
    public String loadViewData(final Model model, final HttpServletRequest request,
            final WaterConnectionDetails waterConnectionDetails) {
        waterConnectionDetails.setPreviousApplicationType(waterConnectionDetails.getApplicationType().getCode());
        model.addAttribute("previousApplicationType", waterConnectionDetails.getPreviousApplicationType());
        model.addAttribute("stateType", waterConnectionDetails.getClass().getSimpleName());
        model.addAttribute("applicationDocList",
                waterConnectionDetailsService.getApplicationDocForExceptClosureAndReConnection(waterConnectionDetails));
        model.addAttribute("additionalRule", WaterTaxConstants.WORKFLOW_CLOSUREADDITIONALRULE);
        model.addAttribute("currentUser", waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        workflowContainer.setAdditionalRule(WaterTaxConstants.WORKFLOW_CLOSUREADDITIONALRULE);
        prepareWorkflow(model, waterConnectionDetails, workflowContainer);
        model.addAttribute("radioButtonMap", Arrays.asList(ClosureType.values()));
        model.addAttribute("loggedInCSCUser", waterTaxUtils.getCurrentUserRole());
        model.addAttribute("waterConnectionDetails", waterConnectionDetails);
        model.addAttribute("feeDetails", connectionDemandService.getSplitFee(waterConnectionDetails));
        model.addAttribute("connectionType", waterConnectionDetailsService.getConnectionTypesMap()
                .get(waterConnectionDetails.getConnectionType().name()));
        model.addAttribute("applicationHistory", waterConnectionDetailsService.getHistory(waterConnectionDetails));
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
        model.addAttribute("typeOfConnection", WaterTaxConstants.CLOSINGCONNECTION);
        model.addAttribute(MODE, "closureConnection");
        model.addAttribute("validationMessage",
                closerConnectionService.validateChangeOfUseConnection(waterConnectionDetails));
        final BigDecimal waterTaxDueforParent = waterConnectionDetailsService.getTotalAmount(waterConnectionDetails);
        model.addAttribute("waterTaxDueforParent", waterTaxDueforParent);
        model.addAttribute("citizenPortalUser", waterTaxUtils.isCitizenPortalUser(securityUtils.getCurrentUser()));

        return "connection-closeForm";
    }

    @RequestMapping(value = "/close/{applicationCode}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final WaterConnectionDetails waterConnectionDetails,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes,
            final HttpServletRequest request, final Model model, final BindingResult errors,
            @RequestParam("files") final MultipartFile[] files) {
        final Boolean isCSCOperator = waterTaxUtils.isCSCoperator(securityUtils.getCurrentUser());
        final Boolean citizenPortalUser = waterTaxUtils.isCitizenPortalUser(securityUtils.getCurrentUser());
        model.addAttribute("citizenPortalUser", citizenPortalUser);
        if (!isCSCOperator && !citizenPortalUser) {
            final Boolean isJuniorAsstOrSeniorAsst = waterTaxUtils
                    .isLoggedInUserJuniorOrSeniorAssistant(securityUtils.getCurrentUser().getId());
            if (!isJuniorAsstOrSeniorAsst)
                throw new ValidationException("err.creator.application");
        }
        final String sourceChannel = request.getParameter("Source");
        String workFlowAction = "";

        if (request.getParameter(MODE) != null)
            request.getParameter(MODE);

        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");

        Long approvalPosition = 0l;
        String approvalComent = "";

        if (request.getParameter("approvalComent") != null)
            approvalComent = request.getParameter("approvalComent");

        final Boolean applicationByOthers = waterTaxUtils.getCurrentUserRole();
        if (applicationByOthers != null && applicationByOthers.equals(true) || citizenPortalUser) {
            final Position userPosition = waterTaxUtils
                    .getZonalLevelClerkForLoggedInUser(waterConnectionDetails.getConnection().getPropertyIdentifier());
            if (userPosition == null) {
                model.addAttribute("noJAORSAMessage", "No JA/SA exists to forward the application.");
                return "connection-closeForm";
            } else
                approvalPosition = userPosition.getId();

        }

        waterConnectionDetails.setPreviousApplicationType(request.getParameter("previousApplicationType"));

        final List<DocumentNames> documentListForClosed = waterConnectionDetailsService
                .getAllActiveDocumentNames(applicationTypeService.findByCode(WaterTaxConstants.CLOSINGCONNECTION));
        if (!documentListForClosed.isEmpty()) {
            final ApplicationDocuments applicationDocument = new ApplicationDocuments();
            applicationDocument.setDocumentNames(documentListForClosed.get(0));

            applicationDocument.setWaterConnectionDetails(waterConnectionDetails);
            applicationDocument.setSupportDocs(addToFileStore(files));
            // TODO: as off now saving 111 temp number for Closure Connection In
            // ApplicationDocument as it is Its nOT NUll
            applicationDocument.setDocumentNumber("111");
            applicationDocument.setDocumentDate(new Date());
            waterConnectionDetails.getApplicationDocs().add(applicationDocument);
        }
        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
        if (request.getParameter("closeConnectionType").equals(WaterTaxConstants.PERMENENTCLOSECODE))
            waterConnectionDetails.setCloseConnectionType(ClosureType.Permanent.getName());
        else
            waterConnectionDetails.setCloseConnectionType(ClosureType.Temporary.getName());
        final String addrule = request.getParameter("additionalRule");
        waterConnectionDetails.setConnectionStatus(ConnectionStatus.CLOSED);
        waterConnectionDetails
                .setApplicationType(applicationTypeService.findByCode(WaterTaxConstants.CLOSINGCONNECTION));
        if(citizenPortalUser){
            if (waterConnectionDetails.getSource() == null || StringUtils.isBlank(waterConnectionDetails.getSource().toString()))
                waterConnectionDetails.setSource(waterTaxUtils.setSourceOfConnection(securityUtils.getCurrentUser()));
        }
        final WaterConnectionDetails savedWaterConnectionDetails = closerConnectionService.updatecloserConnection(
                waterConnectionDetails, approvalPosition, approvalComent, addrule, workFlowAction, sourceChannel);
        model.addAttribute("waterConnectionDetails", savedWaterConnectionDetails);
        model.addAttribute(MODE, "ack");
        return "redirect:/application/citizeenAcknowledgement?pathVars=" + waterConnectionDetails.getApplicationNumber();

    }

}
