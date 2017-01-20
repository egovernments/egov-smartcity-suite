/* eGov suite of products aim to improve the internal efficiency,transparency,
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

package org.egov.mrs.web.controller.application.registration;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.repository.FileStoreMapperRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.web.utils.WebUtils;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.application.MarriageUtils;
import org.egov.mrs.application.service.MarriageCertificateService;
import org.egov.mrs.domain.entity.MarriageCertificate;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Controller to correct the registration data
 *
 * @author nayeem
 */

@Controller
@RequestMapping(value = "/registration")
public class UpdateMarriageRegistrationController extends MarriageRegistrationController {

    private static final String MARRIAGE_REGISTRATION = "marriageRegistration";
    private static final Logger LOG = Logger.getLogger(UpdateMarriageRegistrationController.class);
    private static final String MRG_REGISTRATION_EDIT = "registration-correction";
    private static final String MRG_REGISTRATION_EDIT_APPROVED = "registration-update-approved";
    private static final String MRG_REGISTRATION_SUCCESS = "registration-ack";

    @Autowired
    private FileStoreService fileStoreService;
    @Autowired
    private MarriageFormValidator marriageFormValidator;
    @Autowired
    protected AssignmentService assignmentService;
    @Autowired
    protected MarriageUtils marriageUtils;
    @Autowired
    protected MarriageCertificateService marriageCertificateService;
    @Autowired
    protected FileStoreMapperRepository fileStoreMapperRepository;

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String showRegistration(@PathVariable final Long id, final Model model) {
        buildMrgRegistrationUpdateResult(id, model);
        return MRG_REGISTRATION_EDIT;
    }

    @RequestMapping(value = "/update-approved/{id}", method = RequestMethod.GET)
    public String editApprovedRegistration(@PathVariable final Long id, final Model model) {
        buildMrgRegistrationUpdateResult(id, model);
        return MRG_REGISTRATION_EDIT_APPROVED;
    }

    private void buildMrgRegistrationUpdateResult(final Long id, final Model model) {
        final MarriageRegistration marriageRegistration = marriageRegistrationService.get(id);
        if (!marriageRegistration.isLegacy()) {
            final AppConfigValues allowValidation = marriageFeeService.getDaysValidationAppConfValue(
                    MarriageConstants.MODULE_NAME, MarriageConstants.MARRIAGEREGISTRATION_DAYS_VALIDATION);
            model.addAttribute("allowDaysValidation",
                    allowValidation != null && !allowValidation.getValue().isEmpty() ? allowValidation.getValue()
                            : "NO");
        } else
            model.addAttribute("allowDaysValidation", "NO");
        marriageRegistrationService.prepareDocumentsForView(marriageRegistration);
        marriageApplicantService.prepareDocumentsForView(marriageRegistration.getHusband());
        marriageApplicantService.prepareDocumentsForView(marriageRegistration.getWife());
        model.addAttribute("applicationHistory", marriageRegistrationService.getHistory(marriageRegistration));
        prepareWorkFlowForNewMarriageRegistration(marriageRegistration, model);

        marriageRegistration.getWitnesses().forEach(
                witness -> {
                    try {
                        if (witness.getPhotoFileStore() != null) {
                            final File file = fileStoreService.fetch(witness.getPhotoFileStore().getFileStoreId(),
                                    MarriageConstants.FILESTORE_MODULECODE);
                            witness.setEncodedPhoto(Base64.getEncoder().encodeToString(
                                    FileCopyUtils.copyToByteArray(file)));
                        }
                    } catch (final IOException e) {
                        LOG.error("Error while preparing the document for view", e);
                    }
                });
        model.addAttribute(MARRIAGE_REGISTRATION, marriageRegistration);
    }

    private void prepareWorkFlowForNewMarriageRegistration(final MarriageRegistration registration, final Model model) {
        final WorkflowContainer workFlowContainer = new WorkflowContainer();
        //Set pending actions based on digitalsignature configuration value
        if (registration.getStatus().getCode().equalsIgnoreCase(MarriageConstants.APPROVED))
            if (marriageUtils.isDigitalSignEnabled()) {
                model.addAttribute("pendingActions", MarriageConstants.WFLOW_PENDINGACTION_DIGISIGNPENDING);
                workFlowContainer.setPendingActions(MarriageConstants.WFLOW_PENDINGACTION_DIGISIGNPENDING);
            } else {
                model.addAttribute("pendingActions", MarriageConstants.WFLOW_PENDINGACTION_PRINTCERTIFICATE);
                workFlowContainer.setPendingActions(MarriageConstants.WFLOW_PENDINGACTION_PRINTCERTIFICATE);
            }
        workFlowContainer.setAdditionalRule(MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        prepareWorkflow(model, registration, workFlowContainer);
        model.addAttribute("additionalRule", MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        model.addAttribute("stateType", registration.getClass().getSimpleName());
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateRegistration(final WorkflowContainer workflowContainer,
            @ModelAttribute final MarriageRegistration marriageRegistration, final Model model,
            final HttpServletRequest request, final BindingResult errors) throws IOException {

        String workFlowAction = "";
        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");

        validateApplicationDate(marriageRegistration, errors, request);
        marriageFormValidator.validate(marriageRegistration, errors, "registration");
        if (errors.hasErrors()) {
            model.addAttribute(MARRIAGE_REGISTRATION, marriageRegistration);
            return MRG_REGISTRATION_EDIT;
        }
        String message = org.apache.commons.lang.StringUtils.EMPTY;
        String approverName;
        String nextDesignation;
        if (workFlowAction != null && !workFlowAction.isEmpty()) {
            workflowContainer.setWorkFlowAction(workFlowAction);
            final Assignment wfInitiator = assignmentService.getPrimaryAssignmentForUser(marriageRegistration
                    .getCreatedBy().getId());
            approverName = wfInitiator.getEmployee().getName();
            nextDesignation = wfInitiator.getDesignation().getName();
            workflowContainer.setApproverComments(request.getParameter("approvalComent"));
            if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_REJECT)) {
                marriageRegistrationService.rejectRegistration(marriageRegistration, workflowContainer);
                message = messageSource.getMessage(
                        "msg.rejected.registration",
                        new String[] { marriageRegistration.getApplicationNo(),
                                approverName.concat("~").concat(nextDesignation) }, null);
            } else if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_CANCEL)) {
                marriageRegistrationService.rejectRegistration(marriageRegistration, workflowContainer);
                message = messageSource.getMessage("msg.cancelled.registration",
                        new String[] { marriageRegistration.getApplicationNo(), null }, null);
            } else if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_APPROVE)) {
                //If digital signature is configured, after approve appl shld remain in commissioner inbox for digital signature
                // otherwise gets fwded to creator for print certificate.
                if (marriageUtils.isDigitalSignEnabled()) {
                    model.addAttribute("pendingActions", MarriageConstants.WFLOW_PENDINGACTION_APPRVLPENDING_DIGISIGN);
                    workflowContainer.setPendingActions(MarriageConstants.WFLOW_PENDINGACTION_APPRVLPENDING_DIGISIGN);
                    marriageRegistrationService.approveRegistration(marriageRegistration, workflowContainer);
                    message = messageSource.getMessage("msg.approved.registration",
                            new String[] { marriageRegistration.getRegistrationNo() }, null);
                } else {
                    model.addAttribute("pendingActions", MarriageConstants.WFLOW_PENDINGACTION_APPRVLPENDING_PRINTCERT);
                    workflowContainer.setPendingActions(MarriageConstants.WFLOW_PENDINGACTION_APPRVLPENDING_PRINTCERT);
                    marriageRegistrationService.approveRegistration(marriageRegistration, workflowContainer);
                    message = messageSource.getMessage(
                            "msg.approved.forwarded.registration",
                            new String[] { marriageRegistration.getRegistrationNo(),
                                    approverName.concat("~").concat(nextDesignation) }, null);
                }
            } else if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_DIGISIGN)) {
                //Generates certificate, sends for digital sign and calls callback url for workflow transition.
                final MarriageCertificate marriageCertificate = marriageRegistrationService
                        .generateMarriageCertificate(marriageRegistration, workflowContainer, request);
                model.addAttribute("fileStoreIds", marriageCertificate.getFileStore().getFileStoreId());
                model.addAttribute("ulbCode", ApplicationThreadLocals.getCityCode());
               //Adding applicationNo and its filestoreid to be digitally signed to session
                final HttpSession session = request.getSession();
                session.setAttribute(MarriageConstants.APPROVAL_COMMENT, request.getParameter("approvalComent"));
                session.setAttribute(MarriageConstants.APPLICATION_NUMBER, marriageCertificate.getRegistration()
                        .getApplicationNo());
                final Map<String, String> fileStoreIdsApplicationNoMap = new HashMap<String, String>();
                fileStoreIdsApplicationNoMap.put(marriageCertificate.getFileStore().getFileStoreId(),
                        marriageCertificate.getRegistration().getApplicationNo());
                session.setAttribute(MarriageConstants.FILE_STORE_ID_APPLICATION_NUMBER, fileStoreIdsApplicationNoMap);
                 model.addAttribute("isDigitalSignatureEnabled", marriageUtils.isDigitalSignEnabled());
                return "marriagereg-digitalsignature";
            } else if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_PRINTCERTIFICATE)) {
                marriageRegistrationService.printCertificate(marriageRegistration, workflowContainer, request);
                message = messageSource.getMessage("msg.printcertificate.registration", null, null);
            } else {
                approverName = request.getParameter("approverName");
                nextDesignation = request.getParameter("nextDesignation");
                workflowContainer.setApproverPositionId(Long.valueOf(request.getParameter("approvalPosition")));
                marriageRegistrationService.forwardRegistration(marriageRegistration, workflowContainer);
                message = messageSource.getMessage("msg.forward.registration", new String[] {
                        approverName.concat("~").concat(nextDesignation), marriageRegistration.getApplicationNo() },
                        null);
            }
        }
        // On print certificate, output registration certificate
        if (workFlowAction != null && !workFlowAction.isEmpty()
                && workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_PRINTCERTIFICATE))
            return "redirect:/certificate/registration?id=" + marriageRegistration.getId();

        model.addAttribute("message", message);
        return MRG_REGISTRATION_SUCCESS;
    }

    /**
     * @description call back url to do workflow transition after certificate is digitally signed
     * @param request
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/digiSignWorkflow", method = RequestMethod.POST)
    public String digiSignTransitionWorkflow(final HttpServletRequest request, final Model model) throws IOException {
        final String fileStoreIds = request.getParameter("fileStoreId");
        final String[] fileStoreIdArr = fileStoreIds.split(",");
        final HttpSession session = request.getSession();
        final String approvalComent = (String) session.getAttribute(MarriageConstants.APPROVAL_COMMENT);
        //Gets the digitally signed applicationNo and its filestoreid from session
        final Map<String, String> appNoFileStoreIdsMap = (Map<String, String>) session
                .getAttribute(MarriageConstants.FILE_STORE_ID_APPLICATION_NUMBER);
        MarriageRegistration marriageRegistrationObj = null;
        WorkflowContainer workflowContainer;
        for (final String fileStoreId : fileStoreIdArr) {
            final String applicationNumber = appNoFileStoreIdsMap.get(fileStoreId);
            if (null != applicationNumber && !applicationNumber.isEmpty()) {
                workflowContainer = new WorkflowContainer();
                workflowContainer.setApproverComments(approvalComent);
                workflowContainer.setWorkFlowAction(MarriageConstants.WFLOW_ACTION_STEP_DIGISIGN);
                workflowContainer.setPendingActions(MarriageConstants.WFLOW_PENDINGACTION_DIGISIGNPENDING);
                marriageRegistrationObj = marriageRegistrationService.findByApplicationNo(applicationNumber);
                marriageRegistrationService.digiSignCertificate(marriageRegistrationObj, workflowContainer, request);
            }
        }
        final Assignment wfInitiator = assignmentService.getPrimaryAssignmentForUser(marriageRegistrationObj
                .getCreatedBy().getId());
        final String message = messageSource.getMessage("msg.digisign.success.registration", new String[] { wfInitiator
                .getEmployee().getName().concat("~").concat(wfInitiator.getDesignation().getName()) }, null);
        model.addAttribute("successMessage", message);
        model.addAttribute("fileStoreId", fileStoreIdArr.length == 1 ? fileStoreIdArr[0] : "");
        return "mrdigitalsignature-success";
    }

    /**
     * @description download digitally signed certificate.
     * @param request
     * @param response
     */
    @RequestMapping(value = "/downloadSignedCertificate")
    public void downloadSignedCertificate(final HttpServletRequest request, final HttpServletResponse response) {
        final String signedFileStoreId = request.getParameter("signedFileStoreId");
        final File file = fileStoreService.fetch(signedFileStoreId, MarriageConstants.FILESTORE_MODULECODE);
        try {
            final FileStoreMapper fileStoreMapper = fileStoreMapperRepository.findByFileStoreId(signedFileStoreId);
            final List<InputStream> pdfs = new ArrayList<InputStream>();
            final byte[] bFile = FileUtils.readFileToByteArray(file);
            pdfs.add(new ByteArrayInputStream(bFile));
            getServletResponse(response, pdfs, fileStoreMapper.getFileName());
        } catch (final FileNotFoundException fileNotFoundExcep) {
            throw new ApplicationRuntimeException("Exception while loading file : " + fileNotFoundExcep);
        } catch (final IOException ioExcep) {
            throw new ApplicationRuntimeException("Exception while generating work order : " + ioExcep);
        }
    }

    private HttpServletResponse getServletResponse(final HttpServletResponse response, final List<InputStream> pdfs,
            final String filename) {
        try {
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            final byte[] data = concatPDFs(pdfs, output);
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".pdf");
            response.setContentType("application/pdf");
            response.setContentLength(data.length);
            response.getOutputStream().write(data);
            return response;
        } catch (final IOException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    /**
     * @description download digitally signed certificate in pdf format.
     * @param streamOfPDFFiles
     * @param outputStream
     * @return
     */
    private byte[] concatPDFs(final List<InputStream> streamOfPDFFiles, final ByteArrayOutputStream outputStream) {
        Document document = null;
        try {
            final List<InputStream> pdfs = streamOfPDFFiles;
            final List<PdfReader> readers = new ArrayList<PdfReader>();
            final Iterator<InputStream> iteratorPDFs = pdfs.iterator();

            // Create Readers for the pdfs.
            while (iteratorPDFs.hasNext()) {
                final InputStream pdf = iteratorPDFs.next();
                final PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                if (null == document)
                    document = new Document(pdfReader.getPageSize(1));
            }
            // Create a writer for the outputstream
            final PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();
            final PdfContentByte cb = writer.getDirectContent(); // Holds the
            // PDF
            // data

            PdfImportedPage page;
            int pageOfCurrentReaderPDF = 0;
            final Iterator<PdfReader> iteratorPDFReader = readers.iterator();

            // Loop through the PDF files and add to the output.
            while (iteratorPDFReader.hasNext()) {
                final PdfReader pdfReader = iteratorPDFReader.next();

                // Create a new page in the target for each source page.
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
                    page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);
                }
                pageOfCurrentReaderPDF = 0;
            }
            outputStream.flush();
            document.close();
            outputStream.close();

        } catch (final Exception e) {

            throw new ValidationException(e.getMessage());
        } finally {
            if (document.isOpen())
                document.close();
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (final IOException ioe) {
                throw new ValidationException(ioe.getMessage());
            }
        }
        return outputStream.toByteArray();
    }

    /**
     * @description Modify registered marriage applications
     * @param id
     * @param registration
     * @param model
     * @param request
     * @param errors
     * @return
     */
    @RequestMapping(value = "/update-approved", method = RequestMethod.POST)
    public String modifyRegisteredApplication(@RequestParam final Long id,
            @ModelAttribute final MarriageRegistration registration, final Model model,
            final HttpServletRequest request, final BindingResult errors) {

        validateApplicationDate(registration, errors, request);
        if (errors.hasErrors()) {
            model.addAttribute(MARRIAGE_REGISTRATION, registration);
            return MRG_REGISTRATION_EDIT_APPROVED;
        }
        marriageRegistrationService.updateRegistration(registration);
        model.addAttribute("message", messageSource.getMessage("msg.update.registration", null, null));
        return MRG_REGISTRATION_SUCCESS;
    }

    /**
     * @param serialNo
     * @return
     */
    @RequestMapping(value = "/checkunique-serialno", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean uniqueSerialNo(@RequestParam final String serialNo) {
        MarriageRegistration registration = null;
        if (serialNo != null && serialNo != "")
            registration = marriageRegistrationService.findBySerialNo(serialNo);
        if (registration != null)
            return true;
        return false;
    }

    /**
     * @description To show preview of marriage certificate before digital sign
     * @param id
     * @param session
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/viewCertificate/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> viewReport(@PathVariable final Long id, final HttpSession session,
            final HttpServletRequest request) throws IOException {
        ReportOutput reportOutput;
        final HttpHeaders headers = new HttpHeaders();
        final MarriageRegistration marriageRegistrationObj = marriageRegistrationService.get(id);
        final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();
        final String url = WebUtils.extractRequestDomainURL(request, false);
        final String cityLogo = url.concat(MarriageConstants.IMAGE_CONTEXT_PATH).concat(
                (String) request.getSession().getAttribute("citylogo"));
        reportOutput = marriageCertificateService.generate(marriageRegistrationObj, cityName, cityLogo, "");
        if (reportOutput != null && reportOutput.getReportOutputData() != null) {
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.add("content-disposition", "inline;filename=WorkOrderNotice.pdf");
            return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
        } else
            return redirect();
    }

    /**
     * @description display error message if certificate preview fails
     * @return
     */
    private ResponseEntity<byte[]> redirect() {
        String errorMessage = "Error Generating Certificate Preview.";
        errorMessage = "<html><body><p style='color:red;border:1px solid gray;padding:15px;'>" + errorMessage
                + "</p></body></html>";
        final byte[] byteData = errorMessage.getBytes();
        return new ResponseEntity<byte[]>(byteData, HttpStatus.CREATED);
    }
}