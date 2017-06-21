package org.egov.works.web.controller.mb;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.mb.entity.FileStoreMapperWrapper;
import org.egov.works.mb.entity.MBDetails;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.mb.entity.MBHeaderWrapper;
import org.egov.works.mb.entity.MBMeasurementSheet;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.web.adaptor.MeasurementBookJsonAdaptor;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.egov.works.workorder.service.WorkOrderMeasurementSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@RestController
@RequestMapping(value = "/measurementbook")
public class CreateMBController {

    public static final String MB_SUCCESS_MESSAGE = "message";

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private MeasurementBookJsonAdaptor measurementBookJsonAdaptor;

    @Autowired
    private MBHeaderService mbHeaderService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private WorksUtils worksUtils;
    
    @Autowired
    private WorkOrderMeasurementSheetService workOrderMeasurementSheetService;

    public WorkOrderEstimate getWorkOrderEstimate(final Long workOrderEstimateId) {
        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService
                .getWorkOrderEstimateById(workOrderEstimateId);
        return workOrderEstimate;
    }

    @RequestMapping(value = "/create/{workOrderEstimateId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String createMeasurementBook(@PathVariable final Long workOrderEstimateId,
            final HttpServletRequest request, final HttpServletResponse response) {
        final WorkOrderEstimate workOrderEstimate = getWorkOrderEstimate(workOrderEstimateId);
        final String result = new StringBuilder().append(toSearchMilestoneTemplateJson(workOrderEstimate)).toString();
        return result;
    }

    public Object toSearchMilestoneTemplateJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(WorkOrderEstimate.class, measurementBookJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String create(@ModelAttribute("mbHeader") final MBHeader mbHeader, final Model model,
            final BindingResult errors, final HttpServletRequest request, final BindingResult resultBinder,
            final HttpServletResponse response, @RequestParam("file") final MultipartFile[] files)
            throws ApplicationException, IOException {

        Long approvalPosition = 0l;
        String approvalComment = "";
        String workFlowAction = "";
        String additionalRule = "";
        String mode = "";
        if (request.getParameter(WorksConstants.MODE) != null)
            mode = request.getParameter(WorksConstants.MODE);
        if (request.getParameter("approvalComent") != null)
            approvalComment = request.getParameter("approvalComent");
        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");
        if (request.getParameter(WorksConstants.ADDITIONAL_RULE) != null)
            additionalRule = request.getParameter(WorksConstants.ADDITIONAL_RULE);
        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

        mbHeader.setWorkOrder(letterOfAcceptanceService.getWorkOrderById(mbHeader.getWorkOrder().getId()));
        mbHeader.setWorkOrderEstimate(
                workOrderEstimateService.getWorkOrderEstimateById(mbHeader.getWorkOrderEstimate().getId()));

        final JsonObject jsonObject = new JsonObject();
        mbHeaderService.validateMBInDrafts(mbHeader.getWorkOrderEstimate().getId(), jsonObject, errors);
        mbHeaderService.validateMBInWorkFlow(mbHeader.getWorkOrderEstimate().getId(), jsonObject, errors);
        mbHeaderService.validateMBHeader(mbHeader, jsonObject, resultBinder, mode);
        mbHeaderService.validateWorkflowActionButton(mbHeader, jsonObject, errors, additionalRule, workFlowAction);

        workOrderEstimateService
                .getContratorBillForWorkOrderEstimateAndBillType(mbHeader.getWorkOrderEstimate().getId(), jsonObject);
        if (StringUtils.isBlank(workFlowAction))
            validateMBDateToSkipWorkflow(mbHeader, jsonObject, errors);
        if (jsonObject.toString().length() > 2) {
            sendAJAXResponse(jsonObject.toString(), response);
            return "";
        }

        final MBHeader savedMBHeader = mbHeaderService.create(mbHeader, files, approvalPosition, approvalComment,
                workFlowAction, additionalRule);

        mbHeaderService.fillWorkflowData(jsonObject, request, savedMBHeader);

        if (workFlowAction.equalsIgnoreCase(WorksConstants.SAVE_ACTION))
            jsonObject.addProperty(MB_SUCCESS_MESSAGE,
                    messageSource.getMessage("msg.mbheader.saved", new String[] { mbHeader.getMbRefNo() }, null));
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
            if (StringUtils.isBlank(workFlowAction))
                jsonObject.addProperty(MB_SUCCESS_MESSAGE, messageSource.getMessage("msg.mbheader.createdandapprove",
                        new String[] { mbHeader.getMbRefNo() }, null));
            else if (WorksConstants.CREATE_AND_APPROVE.equalsIgnoreCase(workFlowAction))
                jsonObject.addProperty(MB_SUCCESS_MESSAGE, messageSource.getMessage("msg.mbheader.approved",
                        new String[] { mbHeader.getMbRefNo() }, null));
            else
                jsonObject.addProperty(MB_SUCCESS_MESSAGE, messageSource.getMessage("msg.mbheader.created",
                        new String[] { approverName, nextDesign, mbHeader.getMbRefNo() }, null));
        }

        return jsonObject.toString();
    }
    
    @RequestMapping(value = "/rest-create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String createFromApp(@RequestBody @Valid final MBHeaderWrapper mbHeaderWrapper, final Model model,
            final BindingResult errors, final HttpServletRequest request, final BindingResult resultBinder,
            final HttpServletResponse response)
            throws ApplicationException, IOException {
        
        final MBHeader mbHeader = new MBHeader();
        final WorkOrder workOrder = letterOfAcceptanceService.getWorkOrderByWorkOrderNumber(mbHeaderWrapper.getWorkOrderNumber());
        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService.getWorkOrderEstimateByWorkOrderId(workOrder.getId());
        mbHeader.setWorkOrder(workOrder);
        mbHeader.setMbRefNo(mbHeaderWrapper.getMbRefNo());
        mbHeader.setContractorComments(mbHeaderWrapper.getContractorComments());
        mbHeader.setMbDate(mbHeaderWrapper.getMbDate());
        mbHeader.setMbIssuedDate(mbHeaderWrapper.getMbIssuedDate());
        mbHeader.setMbAbstract(mbHeaderWrapper.getMbAbstract());
        mbHeader.setFromPageNo(mbHeaderWrapper.getFromPageNo());
        mbHeader.setToPageNo(mbHeaderWrapper.getToPageNo());
        mbHeader.setWorkOrderEstimate(workOrderEstimate);
        mbHeader.setSorMbDetails(mbHeaderWrapper.getSorMbDetails());
        mbHeader.setNonSorMbDetails(mbHeaderWrapper.getNonSorMbDetails());
        mbHeader.setNonTenderedMbDetails(mbHeaderWrapper.getNonTenderedMbDetails());
        mbHeader.setLumpSumMbDetails(mbHeaderWrapper.getLumpSumMbDetails());
        mbHeader.setDocumentDetails(mbHeaderWrapper.getDocumentDetails());
        mbHeader.setIsLegacyMB(mbHeaderWrapper.getIsLegacyMB());
        mbHeader.setMbAmount(mbHeaderWrapper.getMbAmount());
        
        setWOMeasurementSheet(mbHeader.getSorMbDetails());
        setWOMeasurementSheet(mbHeader.getNonSorMbDetails());
        setWOMeasurementSheet(mbHeader.getNonTenderedMbDetails());
        setWOMeasurementSheet(mbHeader.getLumpSumMbDetails());

        final String jsonResponse = create(mbHeader, model, errors, request, resultBinder, response, null);
        if (!jsonResponse.contains("\"stateType\":\"MBHeader\"")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonResponse.replace("\"", "");
    }
    
    private void setWOMeasurementSheet(List<MBDetails> mbDetails) {
        for (final MBDetails details : mbDetails)
            for (final MBMeasurementSheet sheet : details.getMeasurementSheets())
                sheet.setWoMeasurementSheet(workOrderMeasurementSheetService.findOne(sheet.getWoMeasurementSheet().getId()));
    }

    @RequestMapping(value = "/rest-create-documents", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> saveDocuments(final Model model, final HttpServletRequest request,
            final HttpServletResponse response, @RequestParam("files") final MultipartFile[] files)
            throws ApplicationException, IOException {
        final FileStoreMapperWrapper wrapper = new FileStoreMapperWrapper();
        List<FileStoreMapper> fileStoreMappers = mbHeaderService.saveDocuments(files);
        wrapper.setFileStoreMappers(fileStoreMappers);
        return new ResponseEntity<FileStoreMapperWrapper>(wrapper, HttpStatus.OK);
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

    @RequestMapping(value = "/mb-success", method = RequestMethod.GET)
    public ModelAndView successView(@ModelAttribute MBHeader mbHeader, @RequestParam("mbHeader") Long id,
            @RequestParam("approvalPosition") final Long approvalPosition, final HttpServletRequest request,
            final Model model) {

        if (id != null)
            mbHeader = mbHeaderService.getMBHeaderById(id);

        final String pathVars = worksUtils.getPathVars(mbHeader.getEgwStatus(), mbHeader.getState(), mbHeader.getId(),
                approvalPosition);

        final String[] keyNameArray = pathVars.split(",");
        String approverName = "";
        String currentUserDesgn = "";
        String nextDesign = "";
        if (keyNameArray.length != 0 && keyNameArray.length > 0)
            if (keyNameArray.length == 1 && keyNameArray[0] != null)
                id = Long.parseLong(keyNameArray[0]);
            else if (keyNameArray.length == 3) {
                if (keyNameArray[0] != null)
                    id = Long.parseLong(keyNameArray[0]);
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
            } else {
                if (keyNameArray[0] != null)
                    id = Long.parseLong(keyNameArray[0]);
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
                nextDesign = keyNameArray[3];
            }

        model.addAttribute("approverName", approverName);
        model.addAttribute("currentUserDesgn", currentUserDesgn);
        model.addAttribute("nextDesign", nextDesign);

        final String message = getMessageByStatus(mbHeader, approverName, nextDesign);

        model.addAttribute(MB_SUCCESS_MESSAGE, message);

        return new ModelAndView("mb-success", "mbHeader", mbHeader);
    }

    private String getMessageByStatus(final MBHeader mbHeader, final String approverName, final String nextDesign) {
        String message = "";

        if (mbHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.NEW.toString()))
            message = messageSource.getMessage("msg.mbheader.saved", new String[] { mbHeader.getMbRefNo() }, null);
        else if (mbHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.CREATED.toString())
                && !mbHeader.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            message = messageSource.getMessage("msg.mbheader.created",
                    new String[] { approverName, nextDesign, mbHeader.getMbRefNo() }, null);
        else if (mbHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.RESUBMITTED.toString())
                && !mbHeader.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            message = messageSource.getMessage("msg.mbheader.resubmitted",
                    new String[] { approverName, nextDesign, mbHeader.getMbRefNo() }, null);
        else if (mbHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.APPROVED.toString()))
            message = messageSource.getMessage("msg.mbheader.approved", new String[] { mbHeader.getMbRefNo() }, null);
        else if (mbHeader.getState() != null && mbHeader.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            message = messageSource.getMessage("msg.mbheader.rejected",
                    new String[] { mbHeader.getMbRefNo(), approverName, nextDesign }, null);
        else if (mbHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.CANCELLED.toString()))
            message = messageSource.getMessage("msg.mbheader.cancelled", new String[] { mbHeader.getMbRefNo() }, null);

        else if (MBHeader.MeasurementBookStatus.CHECKED.toString().equalsIgnoreCase(mbHeader.getEgwStatus().getCode()))
            message = messageSource.getMessage("msg.mbheader.checked",
                    new String[] { approverName, nextDesign, mbHeader.getMbRefNo() }, null);

        return message;
    }

    private void validateMBDateToSkipWorkflow(final MBHeader mBHeader, final JsonObject jsonObject,
            final BindingResult errors) {
        final Date cutOffDate = worksUtils.getCutOffDate();
        final SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
        if (cutOffDate != null && mBHeader.getMbDate().after(cutOffDate)) {
            final String message = messageSource.getMessage("error.mbdate.cutoffdate",
                    new String[] { fmt.format(cutOffDate) }, null);
            jsonObject.addProperty("cutoffdateerror", message);
            if (errors != null)
                errors.reject("cutoffdateerror", message);
        }

    }
}
