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
package org.egov.works.web.controller.mb;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.works.abstractestimate.service.MeasurementSheetService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.mb.entity.MBDetails;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.mb.entity.MBHeader.MeasurementBookStatus;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.models.tender.OfflineStatus;
import org.egov.works.offlinestatus.service.OfflineStatusService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrder.OfflineStatuses;
import org.egov.works.workorder.entity.WorkOrderMeasurementSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
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

import com.google.gson.JsonObject;

@Controller
@RequestMapping(value = "/measurementbook")
public class UpdateMBController extends GenericWorkFlowController {

    public static final String MB_SUCCESS_MESSAGE = "message";

    @Autowired
    private MBHeaderService mbHeaderService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    protected AssignmentService assignmentService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private OfflineStatusService offlineStatusService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private MeasurementSheetService measurementSheetService;

    @Autowired
    private CityService cityService;

    @ModelAttribute
    public MBHeader getMBHeader(@PathVariable final String mbHeaderId) {
        final MBHeader mbHeader = mbHeaderService.getMBHeaderById(Long.parseLong(mbHeaderId));
        return mbHeader;
    }

    @RequestMapping(value = "/update/{mbHeaderId}", method = RequestMethod.GET)
    public String updateMBHeader(final Model model, @PathVariable final String mbHeaderId,
            final HttpServletRequest request, @RequestParam(value = "mode", required = false) final String mode)
            throws ApplicationException {
        final MBHeader mbHeader = getMBHeader(mbHeaderId);
        for (final MBDetails details : mbHeader.getMbDetails()) {
            final Double prevCumulativeQuantity = mbHeaderService.getPreviousCumulativeQuantity(
                    details.getMbHeader().getId(), details.getWorkOrderActivity().getId());
            details.setPrevCumlvQuantity(prevCumulativeQuantity != null ? prevCumulativeQuantity : 0);
            for (final WorkOrderMeasurementSheet woms : details.getWorkOrderActivity()
                    .getWorkOrderMeasurementSheets()) {
                final Double prevMBMSCumulativeQuantity = mbHeaderService
                        .getMeasurementsPreviousCumulativeQuantity(details.getMbHeader().getId(), woms.getId());
                woms.setCumulativeQuantity(prevMBMSCumulativeQuantity != null ? new BigDecimal(prevCumulativeQuantity)
                        : new BigDecimal(0));
            }
        }

        splitSorAndNonSorMBDetails(mbHeader);

        if (mode != null && mode.equalsIgnoreCase(WorksConstants.SAVE_ACTION))
            model.addAttribute("message",
                    messageSource.getMessage("msg.mbheader.saved", new String[] { mbHeader.getMbRefNo() }, null));
        model.addAttribute("defaultDepartmentId", worksUtils.getDefaultDepartmentId());
        return loadViewData(model, request, mbHeader);
    }

    private void splitSorAndNonSorMBDetails(final MBHeader mbHeader) {
        mbHeader.setSorMbDetails((List<MBDetails>) mbHeader.getSORMBDetails());
        mbHeader.setNonSorMbDetails((List<MBDetails>) mbHeader.getNonSORMBDetails());
        mbHeader.setNonTenderedMbDetails((List<MBDetails>) mbHeader.getNonTenderedMBDetails());
        mbHeader.setLumpSumMbDetails((List<MBDetails>) mbHeader.getLumpSumMBDetails());
    }

    @RequestMapping(value = "/save/{mbHeaderId}", method = RequestMethod.POST)
    public @ResponseBody String save(@ModelAttribute("mbHeader") final MBHeader mbHeader,
            @RequestParam("file") final MultipartFile[] files, final Model model, final BindingResult errors,
            final HttpServletRequest request, final BindingResult resultBinder, final HttpServletResponse response,
            @RequestParam("removedDetailIds") final String removedDetailIds) throws ApplicationException, IOException {

        Long approvalPosition = 0l;
        String approvalComment = "";
        String workFlowAction = "";
        String mode = "";
        if (request.getParameter("mode") != null)
            mode = request.getParameter("mode");
        if (request.getParameter("approvalComent") != null)
            approvalComment = request.getParameter("approvalComent");
        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");
        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
        final JsonObject jsonObject = new JsonObject();
        mbHeaderService.validateMBHeader(mbHeader, jsonObject, resultBinder, mode);

        if (jsonObject.toString().length() > 2) {
            sendAJAXResponse(jsonObject.toString(), response);
            return "";
        }

        MBHeader updatedMBHeader = getMbHeaderDocuments(mbHeader);
        updatedMBHeader = mbHeaderService.update(updatedMBHeader, approvalPosition, approvalComment, workFlowAction,
                removedDetailIds, files);
        mbHeaderService.fillWorkflowData(jsonObject, request, updatedMBHeader);
        if (workFlowAction.equalsIgnoreCase(WorksConstants.SAVE_ACTION))
            jsonObject.addProperty(MB_SUCCESS_MESSAGE,
                    messageSource.getMessage("msg.mbheader.saved", new String[] { mbHeader.getMbRefNo() }, null));
        else if (workFlowAction.equalsIgnoreCase(WorksConstants.CANCEL_ACTION))
            jsonObject.addProperty(MB_SUCCESS_MESSAGE,
                    messageSource.getMessage("msg.mbheader.cancelled", new String[] { mbHeader.getMbRefNo() }, null));
        else {
            final String pathVars = worksUtils.getPathVars(mbHeader.getEgwStatus(), mbHeader.getState(),
                    mbHeader.getId(), approvalPosition);

            final String[] keyNameArray = pathVars.split(",");
            String approverName = "";
            String nextDesign = "";
            if (keyNameArray.length != 0 && keyNameArray.length > 0)
                if (keyNameArray.length == 3)
                    approverName = keyNameArray[1];
                else {
                    approverName = keyNameArray[1];
                    nextDesign = keyNameArray[3];
                }
            if (mbHeader.getEgwStatus().getCode().equalsIgnoreCase(MeasurementBookStatus.APPROVED.toString()))
                jsonObject.addProperty(MB_SUCCESS_MESSAGE, messageSource.getMessage("msg.mbheader.approved",
                        new String[] { mbHeader.getMbRefNo() }, null));
            else
                jsonObject.addProperty(MB_SUCCESS_MESSAGE, messageSource.getMessage("msg.mbheader.created",
                        new String[] { approverName, nextDesign, mbHeader.getMbRefNo() }, null));
        }
        return jsonObject.toString();
    }

    protected void sendAJAXResponse(final String msg, final HttpServletResponse response) {
        try {
            final Writer httpResponseWriter = response.getWriter();
            IOUtils.write(msg, httpResponseWriter);
            IOUtils.closeQuietly(httpResponseWriter);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/update/{mbHeaderId}", method = RequestMethod.POST)
    public String update(@ModelAttribute("mbHeader") final MBHeader mbHeader, final BindingResult errors,
            final RedirectAttributes redirectAttributes, final Model model, final HttpServletRequest request,
            @RequestParam("removedDetailIds") final String removedDetailIds,
            @RequestParam("file") final MultipartFile[] files) throws ApplicationException, IOException {

        String mode = "";
        String workFlowAction = "";
        MBHeader updatedMBHeader = null;

        if (request.getParameter("mode") != null)
            mode = request.getParameter("mode");

        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");

        Long approvalPosition = 0l;
        String approvalComment = "";

        if (request.getParameter("approvalComent") != null)
            approvalComment = request.getParameter("approvalComent");

        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

        // For Get Configured ApprovalPosition from workflow history
        if (approvalPosition == null || approvalPosition.equals(Long.valueOf(0)))
            approvalPosition = mbHeaderService.getApprovalPositionByMatrixDesignation(mbHeader, approvalPosition, null,
                    mode, workFlowAction);

        if ((approvalPosition == null || approvalPosition.equals(Long.valueOf(0)))
                && request.getParameter("approvalPosition") != null
                && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

        final JsonObject jsonObject = new JsonObject();
        mbHeaderService.validateMBHeader(mbHeader, jsonObject, errors, mode);

        if (errors.hasErrors()) {
            model.addAttribute("removedMBDetailIds", removedDetailIds);
            return loadViewData(model, request, mbHeader);
        } else {
            if (null != workFlowAction)
                updatedMBHeader = mbHeaderService.update(mbHeader, approvalPosition, approvalComment, workFlowAction,
                        removedDetailIds, files);
            redirectAttributes.addFlashAttribute("mbHeader", updatedMBHeader);

            if (updatedMBHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.NEW.toString()))
                return "redirect:/measurementbook/update/" + updatedMBHeader.getId() + "?mode=save";

            if (approvalPosition == null)
                return "redirect:/measurementbook/mb-success?mbHeader=" + updatedMBHeader.getId()
                        + "&approvalPosition=";
            else
                return "redirect:/measurementbook/mb-success?mbHeader=" + updatedMBHeader.getId() + "&approvalPosition="
                        + approvalPosition;
        }
    }

    private String loadViewData(final Model model, final HttpServletRequest request, final MBHeader mbHeader) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Boolean isMBEditable = false;
        model.addAttribute("stateType", mbHeader.getClass().getSimpleName());
        if (mbHeader.getCurrentState() != null && !mbHeader.getCurrentState().getValue().equals(WorksConstants.NEW))
            model.addAttribute("currentState", mbHeader.getCurrentState().getValue());
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        if (mbHeader.getState() != null && mbHeader.getState().getNextAction() != null) {
            model.addAttribute("nextAction", mbHeader.getState().getNextAction());
            model.addAttribute("pendingActions", mbHeader.getState().getNextAction());
        }
        model.addAttribute("amountRule", mbHeader.getMbAmount());

        workflowContainer.setAmountRule(mbHeader.getMbAmount());
        workflowContainer.setPendingActions(mbHeader.getState().getNextAction());
        workflowContainer.setAdditionalRule(
                (String) cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
        model.addAttribute(WorksConstants.ADDITIONAL_RULE,
                cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
        prepareWorkflow(model, mbHeader, workflowContainer);
        List<String> validActionList = Collections.emptyList();
        validActionList = new ArrayList<String>(getValidActions(mbHeader, workflowContainer));
        validActionList.add(WorksConstants.CONTRACTOR_MEASUREMENTS);

        model.addAttribute("validActionList", validActionList);
        if (mbHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.NEW.toString())) {
            List<String> validActions = Collections.emptyList();

            validActions = customizedWorkFlowService.getNextValidActions(mbHeader.getStateType(),
                    workflowContainer.getWorkFlowDepartment(), workflowContainer.getAmountRule(),
                    workflowContainer.getAdditionalRule(), WorksConstants.NEW, workflowContainer.getPendingActions(),
                    mbHeader.getCreatedDate());
            model.addAttribute("validActionList", validActions);

        }

        List<AppConfigValues> values = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_MB_QUANTITY_TOLERANCE_LEVEL);
        AppConfigValues value = values.get(0);

        model.addAttribute("quantityTolerance", value.getValue());

        values = appConfigValuesService.getConfigValuesByModuleAndKey(WorksConstants.WORKS_MODULE_NAME,
                WorksConstants.APPCONFIG_KEY_MB_SECOND_LEVEL_EDIT);
        value = values.get(0);
        if (value.getValue().equalsIgnoreCase("Yes"))
            isMBEditable = true;

        model.addAttribute("workflowHistory", worksUtils.getHistory(mbHeader.getState(), mbHeader.getStateHistory()));
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
        model.addAttribute("approvalDesignation", request.getParameter("approvalDesignation"));
        model.addAttribute("approvalPosition", request.getParameter("approvalPosition"));
        model.addAttribute("exceptionaluoms", worksUtils.getExceptionalUOMS());

        final MBHeader updatedMBHeader = getMbHeaderDocuments(mbHeader);
        model.addAttribute("documentDetails", updatedMBHeader.getDocumentDetails());
        model.addAttribute("mbHeader", updatedMBHeader);
        final OfflineStatus offlineStatus = offlineStatusService.getOfflineStatusByObjectIdAndObjectTypeAndStatus(
                updatedMBHeader.getWorkOrderEstimate().getWorkOrder().getId(), WorksConstants.WORKORDER,
                OfflineStatuses.WORK_COMMENCED.toString().toUpperCase());
        if (offlineStatus != null)
            model.addAttribute("workCommencedDate", sdf.format(offlineStatus.getStatusDate()));

        final Double totalMBAmountOfMBs = mbHeaderService.getTotalMBAmountOfMBs(updatedMBHeader.getId(),
                updatedMBHeader.getWorkOrderEstimate().getId(), MBHeader.MeasurementBookStatus.CANCELLED.toString());
        if (totalMBAmountOfMBs != null)
            model.addAttribute("totalMBAmountOfMBs", totalMBAmountOfMBs - updatedMBHeader.getMbAmount().doubleValue());

        final List<MBHeader> previousMBHeaders = mbHeaderService.getPreviousMBHeaders(mbHeader.getId(),
                mbHeader.getWorkOrderEstimate().getId());

        if (!previousMBHeaders.isEmpty())
            model.addAttribute("previousMBDate",
                    sdf.format(previousMBHeaders.get(previousMBHeaders.size() - 1).getMbDate()));
        else
            model.addAttribute("previousMBDate", "");

        model.addAttribute("isMeasurementsExist",
                measurementSheetService.existsByEstimate(mbHeader.getWorkOrderEstimate().getEstimate().getId()));

        if (!mbHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.NEW.toString())
                && !updatedMBHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.REJECTED.toString())
                && isMBEditable)
            model.addAttribute("isMBHeaderEditable", "false");
        else
            model.addAttribute("isMBHeaderEditable", "true");
        if (mbHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.NEW.toString()))
            model.addAttribute("showHistory", "false");
        else
            model.addAttribute("showHistory", "true");
        model.addAttribute("nameOfWork", updatedMBHeader.getWorkOrderEstimate().getEstimate().getName());
        model.addAttribute("loaNumber", updatedMBHeader.getWorkOrderEstimate().getWorkOrder().getWorkOrderNumber());
        model.addAttribute("loaAmount", updatedMBHeader.getWorkOrderEstimate().getWorkOrder().getWorkOrderAmount());

        // TODO: check if only quantities to be edited or the whole mb can be editable
        if (mbHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.NEW.toString()) ||
                updatedMBHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.REJECTED.toString())
                || isMBEditable) {
            model.addAttribute(WorksConstants.MODE, WorksConstants.EDIT);
            return "mbHeader-form";
        } else {
            model.addAttribute(WorksConstants.MODE, "workflowView");
            return "mbheader-view";
        }
    }

    private MBHeader getMbHeaderDocuments(final MBHeader mbHeader) {
        List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
        documentDetailsList = worksUtils.findByObjectIdAndObjectType(mbHeader.getId(), WorksConstants.MBHEADER);
        mbHeader.setDocumentDetails(documentDetailsList);
        return mbHeader;
    }
}
