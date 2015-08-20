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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.commons.entity.ChairPerson;
import org.egov.commons.service.ChairPersonService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.wtms.application.entity.ConnectionEstimationDetails;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.ConnectionCategory;
import org.egov.wtms.masters.service.RoadCategoryService;
import org.egov.wtms.masters.service.UsageTypeService;
import org.egov.wtms.utils.WaterTaxNumberGenerator;
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

    private final WaterConnectionDetailsService waterConnectionDetailsService;

    private final DepartmentService departmentService;

    private WaterConnectionDetails waterConnectionDetails;

    @Autowired
    private ConnectionDemandService connectionDemandService;
    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private RoadCategoryService roadCategoryService;

    @Autowired
    protected UsageTypeService usageTypeService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private WaterTaxNumberGenerator waterTaxNumberGenerator;

    @Autowired
    private ChairPersonService chairPersonService;

    @Autowired
    public UpdateConnectionController(final WaterConnectionDetailsService waterConnectionDetailsService,
            final DepartmentService departmentService, final ConnectionDemandService connectionDemandService,
            final SmartValidator validator) {
        this.waterConnectionDetailsService = waterConnectionDetailsService;
        this.departmentService = departmentService;
    }

    @ModelAttribute
    public WaterConnectionDetails getWaterConnectionDetails(@PathVariable final String applicationNumber) {
        waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumber(applicationNumber);
        return waterConnectionDetails;
    }

    @Override
    @ModelAttribute
    public StateAware getModel() {
        return waterConnectionDetails;
    }

    @RequestMapping(value = "/update/{applicationNumber}", method = RequestMethod.GET)
    public String view(final Model model, @PathVariable final String applicationNumber, final HttpServletRequest request) {
        waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumber(applicationNumber);
        return loadViewData(model, request, waterConnectionDetails);
    }

    private String loadViewData(final Model model, final HttpServletRequest request,
            final WaterConnectionDetails waterConnectionDetails) {
        model.addAttribute("stateType", waterConnectionDetails.getClass().getSimpleName());
        model.addAttribute("additionalRule", waterConnectionDetails.getApplicationType().getCode());
        model.addAttribute("currentUser", waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
        model.addAttribute("waterConnectionDetails", waterConnectionDetails);
        model.addAttribute("feeDetails", connectionDemandService.getSplitFee(waterConnectionDetails));
        model.addAttribute("connectionType",
                waterConnectionDetailsService.getConnectionTypesMap().get(waterConnectionDetails.getConnectionType().name()));
        model.addAttribute("applicationHistory", waterConnectionDetailsService.getHistory(waterConnectionDetails));
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());

        if (waterConnectionDetails.getEgwStatus() != null && waterConnectionDetails.getEgwStatus().getCode()
                .equalsIgnoreCase(WaterTaxConstants.APPLICATION_STATUS_FEEPAID)) {
            final ChairPerson chairPerson = chairPersonService.getActiveChairPersonAsOnCurrentDate();
            model.addAttribute("chairPerson", chairPerson);
        }

        appendModeBasedOnApplicationCreator(model, request, waterConnectionDetails);
        return "newconnection-edit";
    }

    private void appendModeBasedOnApplicationCreator(final Model model, final HttpServletRequest request,
            final WaterConnectionDetails waterConnectionDetails) {
        final Boolean recordCreatedBYNonEmployee = waterTaxUtils.getCurrentUserRole(waterConnectionDetails.getCreatedBy());
        // if record from csc to Clerk
        if (recordCreatedBYNonEmployee && null == request.getAttribute("mode")
                && waterConnectionDetails.getState().getHistory().isEmpty()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("approvalPositionExist", waterConnectionDetailsService
                    .getApprovalPositionByMatrixDesignation(waterConnectionDetails, 0l,
                            waterConnectionDetails.getApplicationType().getCode(), "edit"));
        }
        // "edit" mode for AE inbox record FROM CSC and Record from Clerk
        else if (recordCreatedBYNonEmployee && request.getAttribute("mode") == null
                && waterConnectionDetails.getEgwStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_CREATED)
                && waterConnectionDetails
                        .getState().getHistory() != null
                || !recordCreatedBYNonEmployee && waterConnectionDetails.getEgwStatus() != null
                        && waterConnectionDetails.getEgwStatus().getCode()
                                .equals(WaterTaxConstants.APPLICATION_STATUS_CREATED)) {
            model.addAttribute("mode", "fieldInspection");
            model.addAttribute("approvalPositionExist", waterConnectionDetailsService
                    .getApprovalPositionByMatrixDesignation(waterConnectionDetails, 0l,
                            waterConnectionDetails.getApplicationType().getCode(), "fieldInspection"));
            model.addAttribute("roadCategoryList", roadCategoryService.getAllRoadCategory());
            model.addAttribute("usageTypes", usageTypeService.getActiveUsageTypes());
        } else
            model.addAttribute("approvalPositionExist", waterConnectionDetailsService
                    .getApprovalPositionByMatrixDesignation(waterConnectionDetails, 0l,
                            waterConnectionDetails.getApplicationType().getCode(), ""));
        if (waterConnectionDetails.getCurrentState().getValue().equals("Rejected"))
            model.addAttribute("mode", "");
    }

    @RequestMapping(value = "/update/{applicationNumber}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute WaterConnectionDetails waterConnectionDetails,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes,
            final HttpServletRequest request, final Model model, @RequestParam("files") final MultipartFile[] files) {

        String mode = "";
        String workFlowAction = "";

        if (request.getParameter("mode") != null)
            mode = request.getParameter("mode");

        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");

        if (waterConnectionDetails.getEgwStatus().getCode().equalsIgnoreCase(WaterTaxConstants.APPLICATION_STATUS_CREATED) &&
                mode.equalsIgnoreCase("fieldInspection"))
            if (workFlowAction.equalsIgnoreCase(WaterTaxConstants.SUBMITWORKFLOWACTION)) {
                final ConnectionCategory connectionCategory = connectionCategoryService
                        .findBy(waterConnectionDetails.getCategory().getId());
                if (connectionCategory != null && !connectionCategory.getCode().equalsIgnoreCase(WaterTaxConstants.CATEGORY_BPL)
                        && waterConnectionDetails.getBplCardHolderName() != null)
                    waterConnectionDetails.setBplCardHolderName(null);

                populateEstimationDetails();
                waterConnectionDetails.setDemand(connectionDemandService.createDemand(waterConnectionDetails));

                // Attach any other file during field inspection and estimation
                final Set<FileStoreMapper> fileStoreSet = addToFileStore(files);
                Iterator<FileStoreMapper> fsIterator = null;
                if (fileStoreSet != null && !fileStoreSet.isEmpty())
                    fsIterator = fileStoreSet.iterator();
                if (fsIterator != null && fsIterator.hasNext())
                    waterConnectionDetails.setFileStore(fsIterator.next());
            } else if (workFlowAction.equalsIgnoreCase(WaterTaxConstants.WFLOW_ACTION_STEP_REJECT)) {
                waterConnectionDetails = waterConnectionDetailsService.findBy(waterConnectionDetails.getId());
                waterConnectionDetails.setFieldInspectionDetails(null);
                waterConnectionDetails.setEstimationDetails(null);
                waterConnectionDetails.setFileStore(null);
            }

        Long approvalPosition = 0l;
        String approvalComent = "";

        if (request.getParameter("approvalComent") != null)
            approvalComent = request.getParameter("approvalComent");

        if (workFlowAction != null && workFlowAction.equals(WaterTaxConstants.APPROVEWORKFLOWACTION)
                && waterConnectionDetails.getEgwStatus() != null
                && waterConnectionDetails.getEgwStatus().getCode() != null
                && waterConnectionDetails.getEgwStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_FEEPAID))
            validateSanctionDetails(waterConnectionDetails, resultBinder);
        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

        if (approvalPosition == null || approvalPosition.equals(Long.valueOf(0)))
            approvalPosition = waterConnectionDetailsService.getApprovalPositionByMatrixDesignation(
                    waterConnectionDetails, approvalPosition, waterConnectionDetails.getApplicationType().getCode(), mode);
        appendModeBasedOnApplicationCreator(model, request, waterConnectionDetails);

        if (!resultBinder.hasErrors()) {
            if (null != workFlowAction && !workFlowAction.isEmpty()
                    && workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_WORKORDER_BUTTON)) {
                waterConnectionDetails.setWorkOrderDate(new Date());
                waterConnectionDetails.setWorkOrderNumber(waterTaxNumberGenerator.generateWorkOrderNumber());
            }

            waterConnectionDetailsService.updateWaterConnection(waterConnectionDetails, approvalPosition,
                    approvalComent, waterConnectionDetails.getApplicationType().getCode(), workFlowAction, mode);

            if (workFlowAction != null && !workFlowAction.isEmpty()
                    && workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_ESTIMATION_NOTICE_BUTTON))
                return "redirect:/application/estimationNotice?pathVar=" + waterConnectionDetails.getApplicationNumber();

            if (null != workFlowAction && !workFlowAction.isEmpty()
                    && workFlowAction.equalsIgnoreCase(WaterTaxConstants.WF_WORKORDER_BUTTON))
                return "redirect:/application/workorder?pathVar=" + waterConnectionDetails.getApplicationNumber();

            final String pathVars = waterConnectionDetails.getApplicationNumber() + ","
                    + waterTaxUtils.getApproverUserName(approvalPosition);
            return "redirect:/application/application-success?pathVars=" + pathVars;
        } else
            return loadViewData(model, request, waterConnectionDetails);
    }

    private void validateSanctionDetails(final WaterConnectionDetails waterConnectionDetails, final BindingResult errors) {

        if (waterConnectionDetails.getApprovalNumber() == null)
            errors.rejectValue("approvalNumber", "approvalNumber.required");

        if (waterConnectionDetails.getApprovalDate() == null)
            errors.rejectValue("approvalDate", "approvalDate.required");
    }

    private void populateEstimationDetails() {
        final List<ConnectionEstimationDetails> estimationDetails = new ArrayList<ConnectionEstimationDetails>();
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
        if (connectionEstimationDetails == null
                || connectionEstimationDetails != null && connectionEstimationDetails.getItemDescription() == null)
            return false;
        return true;
    }

}
