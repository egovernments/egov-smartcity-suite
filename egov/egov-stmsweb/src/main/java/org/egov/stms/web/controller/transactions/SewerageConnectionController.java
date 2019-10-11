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
package org.egov.stms.web.controller.transactions;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.egov.commons.entity.Source.CITIZENPORTAL;
import static org.egov.commons.entity.Source.CSC;
import static org.egov.commons.entity.Source.SYSTEM;
import static org.egov.commons.entity.Source.ONLINE;
import static org.egov.infra.persistence.entity.enums.UserType.BUSINESS;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEE_INSPECTIONCHARGE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.NEWSEWERAGECONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_COLLECTINSPECTIONFEE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MODULETYPE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_CREATED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_CSCCREATED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_FEECOLLECTIONPENDING;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_ANONYMOUSCREATED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_REJECTED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_PDF;
import static org.egov.stms.utils.constants.SewerageTaxConstants.ROLE_CSCOPERTAOR;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.PropertyTaxDetails;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.stms.masters.entity.DocumentTypeMaster;
import org.egov.stms.masters.entity.FeesDetailMaster;
import org.egov.stms.masters.entity.SewerageApplicationType;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.masters.entity.enums.SewerageConnectionStatus;
import org.egov.stms.masters.service.DocumentTypeMasterService;
import org.egov.stms.masters.service.FeesDetailMasterService;
import org.egov.stms.masters.service.SewerageApplicationTypeService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageApplicationDetailsDocument;
import org.egov.stms.transactions.entity.SewerageConnection;
import org.egov.stms.transactions.entity.SewerageConnectionFee;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageConnectionService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.egov.stms.transactions.service.SewerageWorkflowService;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.web.controller.utils.SewerageApplicationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/transactions")
public class SewerageConnectionController extends GenericWorkFlowController {

    private static final String APPROVER_NAME = "approverName";
    private static final String INSPECTIONFEEREQUIRED = "inspectionFeesCollectionRequired";
    private static final Logger LOG = LoggerFactory.getLogger(SewerageConnectionController.class);
    @Autowired
    protected AssignmentService assignmentService;
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;
    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;
    @Autowired
    private SewerageConnectionService sewerageConnectionService;
    @Autowired
    private SewerageApplicationTypeService sewerageApplicationTypeService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private PropertyExternalService propertyExternalService;
    @Autowired
    private FeesDetailMasterService feesDetailMasterService;
    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;

    @Autowired
    private DocumentTypeMasterService documentTypeMasterService;

    @Autowired
    private SewerageApplicationValidator sewerageApplicationValidator;

    @Autowired
    private SewerageWorkflowService sewerageWorkflowService;

    @Autowired
    private MessageSource messageSource;

    @ModelAttribute("documentNamesList")
    public List<SewerageApplicationDetailsDocument> documentTypeMasterList(
            @ModelAttribute final SewerageApplicationDetails sewerageApplicationDetails) {
        final List<SewerageApplicationDetailsDocument> tempDocList = new ArrayList<>(
                0);
        final SewerageApplicationType applicationType = sewerageApplicationTypeService
                .findByCode(NEWSEWERAGECONNECTION);
        final List<DocumentTypeMaster> documentTypeMasterList = documentTypeMasterService
                .getAllActiveDocumentTypeMasterByApplicationType(applicationType);
        if (sewerageApplicationDetails != null)
            documentTypeMasterList.stream().forEach(documentTypeMaster -> {
                SewerageApplicationDetailsDocument sadd = new SewerageApplicationDetailsDocument();
                sadd.setDocumentTypeMaster(documentTypeMaster);
                tempDocList.add(sadd);
            });
        return tempDocList;
    }

    @GetMapping("/newConnection-newform")
    public String showNewApplicationForm(@ModelAttribute final SewerageApplicationDetails sewerageApplicationDetails,
                                         final Model model) {
        LOG.debug("Inside showNewApplicationForm method");
        final SewerageConnection connection = new SewerageConnection();
        sewerageApplicationDetails.setApplicationDate(new Date());
        connection.setStatus(SewerageConnectionStatus.INPROGRESS);
        sewerageApplicationDetails.setConnection(connection);
        prepareNewForm(sewerageApplicationDetails, model);
        final boolean inspectionFeeCollectionRequired = sewerageTaxUtils.isInspectionFeeCollectionRequired();
        model.addAttribute(INSPECTIONFEEREQUIRED, inspectionFeeCollectionRequired);
        if (inspectionFeeCollectionRequired)
            createSewerageConnectionFee(sewerageApplicationDetails, FEE_INSPECTIONCHARGE);
        model.addAttribute("mode", null);
        return "newconnection-form";
    }

    private void createSewerageConnectionFee(final SewerageApplicationDetails sewerageApplicationDetails, final String feeCode) {
        final List<FeesDetailMaster> inspectionFeeList = feesDetailMasterService.findAllActiveFeesDetailByFeesCode(feeCode);
        inspectionFeeList.stream().forEach(feesDetailMaster -> {
            SewerageConnectionFee sewerageConnectionFee = new SewerageConnectionFee();
            sewerageConnectionFee.setFeesDetail(feesDetailMaster);
            if (feesDetailMaster.getIsFixedRate())
                sewerageConnectionFee.setAmount(feesDetailMaster.getAmount().doubleValue());
            sewerageConnectionFee.setApplicationDetails(sewerageApplicationDetails);
            sewerageApplicationDetails.getConnectionFees().add(sewerageConnectionFee);
        });
    }

    @PostMapping("/newConnection-create")
    public String create(@Valid @ModelAttribute final SewerageApplicationDetails sewerageApplicationDetails,
                         final BindingResult resultBinder,
                         final RedirectAttributes redirectAttributes,
                         final HttpServletRequest request, final Model model,
                         @RequestParam("files") final MultipartFile[] files) {
        sewerageApplicationValidator.validateSewerageNewApplication(sewerageApplicationDetails, resultBinder, request);
        final List<SewerageApplicationDetailsDocument> applicationDocs = new ArrayList<>();
        int i = 0;
        if (!sewerageApplicationDetails.getAppDetailsDocument().isEmpty())
            for (final SewerageApplicationDetailsDocument applicationDocument : sewerageApplicationDetails
                    .getAppDetailsDocument()) {
                sewerageConnectionService.validateDocuments(applicationDocs, applicationDocument, i, resultBinder);
                i++;
            }
        if (resultBinder.hasErrors()) {
            sewerageApplicationDetails.setApplicationDate(new Date());
            prepareNewForm(sewerageApplicationDetails, model);
            final boolean inspectionFeeCollectionRequired = sewerageTaxUtils.isInspectionFeeCollectionRequired();
            model.addAttribute(INSPECTIONFEEREQUIRED, inspectionFeeCollectionRequired);
            model.addAttribute("mode", null);
            return "newconnection-form";
        }
        final Boolean isEmployee = securityUtils.currentUserIsEmployee();
        final boolean citizenPortalUser = securityUtils.currentUserIsCitizen();
        if (!sewerageApplicationDetails.hasState()) {
            updateSourceAndStatus(sewerageApplicationDetails, isEmployee, citizenPortalUser);
        }

        sewerageApplicationDetails.getAppDetailsDocument().clear();
        sewerageApplicationDetails.setAppDetailsDocument(applicationDocs);
        sewerageConnectionService.processAndStoreApplicationDocuments(sewerageApplicationDetails);
        populateFeesDetails(sewerageApplicationDetails);
        final SewerageApplicationDetails newSewerageApplicationDetails = sewerageApplicationDetailsService
                .createNewSewerageConnection(sewerageApplicationDetails, files, request);

        final Assignment currentUserAssignment = assignmentService.getPrimaryAssignmentForGivenRange(securityUtils
                .getCurrentUser().getId(), new Date(), new Date());

        Long approvalPosition = 0l;
        String approverName = "";
        String nextDesignation = "";

        if (!isEmployee || citizenPortalUser) {
            final Assignment assignment = sewerageWorkflowService
                    .getMappedAssignmentForCscOperator(sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier());
            if (assignment != null) {
                approvalPosition = assignment.getPosition().getId();
                approverName = assignment.getEmployee().getName();
                nextDesignation = assignment.getDesignation().getName();
            }
        }

        final String message = messageSource.getMessage("msg.success.forward",
                new String[]{approverName.concat("~").concat(nextDesignation),
                        newSewerageApplicationDetails.getApplicationNumber()},
                null);
        model.addAttribute("message", message);
        if (isEmployee || BUSINESS.equals(securityUtils.getCurrentUser().getType())) {
            final String pathVars = newSewerageApplicationDetails.getApplicationNumber() + ","
                    + sewerageTaxUtils.getApproverName(approvalPosition) + ","
                    + (currentUserAssignment != null ? currentUserAssignment.getDesignation().getName() : "") + ","
                    + nextDesignation;
            return "redirect:/transactions/application-success?pathVars=" + pathVars;
        } else if (citizenPortalUser)
            return "redirect:/citizen/search/sewerageGenerateonlinebill/" + newSewerageApplicationDetails.getApplicationNumber()
                    + "/" + newSewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier();
        else {
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/transactions/new-sewerage-ackowledgement/" + newSewerageApplicationDetails.getApplicationNumber();
        }
    }

    private void updateSourceAndStatus(@Valid @ModelAttribute SewerageApplicationDetails sewerageApplicationDetails, Boolean isEmployee, boolean citizenPortalUser) {
        if (isEmployee) {
            String status = sewerageTaxUtils.isInspectionFeeCollectionRequired()
                    ? APPLICATION_STATUS_COLLECTINSPECTIONFEE : APPLICATION_STATUS_CREATED;
            setSourceAndStatus(sewerageApplicationDetails, SYSTEM.toString(), status);
        } else if (securityUtils.getCurrentUser().hasRole(ROLE_CSCOPERTAOR))
            setSourceAndStatus(sewerageApplicationDetails, CSC.toString(), APPLICATION_STATUS_CSCCREATED);
        else if (citizenPortalUser)
            setSourceAndStatus(sewerageApplicationDetails, CITIZENPORTAL.toString(), APPLICATION_STATUS_FEECOLLECTIONPENDING);
        else
            setSourceAndStatus(sewerageApplicationDetails, ONLINE.toString(), APPLICATION_STATUS_ANONYMOUSCREATED);
    }

    private void setSourceAndStatus(SewerageApplicationDetails sewerageApplicationDetails, String source, String status) {
        sewerageApplicationDetails.setSource(source);
        sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                status, MODULETYPE));
    }

    private void populateFeesDetails(final SewerageApplicationDetails sewerageApplicationDetails) {
        if (sewerageApplicationDetails.getConnectionFees() != null
                && !sewerageApplicationDetails.getConnectionFees().isEmpty())
            for (final SewerageConnectionFee scf : sewerageApplicationDetails.getConnectionFees())
                scf.setApplicationDetails(sewerageApplicationDetails);
    }

    @GetMapping("/application-success")
    public ModelAndView successView(@ModelAttribute SewerageApplicationDetails sewerageApplicationDetails,
                                    final HttpServletRequest request, final Model model, final ModelMap modelMap) {
        List<String> pathVarList = Arrays.asList(request.getParameter("pathVars").split(","));
        String applicationNumber;
        String approverName = EMPTY;
        String currentUserDesgn = EMPTY;
        String nextDesign = EMPTY;
        if (pathVarList.size() == 1)
            applicationNumber = pathVarList.get(0);
        else if (pathVarList.size() == 3) {
            applicationNumber = pathVarList.get(0);
            approverName = pathVarList.get(1);
            currentUserDesgn = pathVarList.get(2);
        } else {
            applicationNumber = pathVarList.get(0);
            approverName = pathVarList.get(1);
            currentUserDesgn = pathVarList.get(2);
            nextDesign = pathVarList.get(3);
        }
        if (applicationNumber != null)
            sewerageApplicationDetails = sewerageApplicationDetailsService.findByApplicationNumber(applicationNumber);
        if (WF_STATE_REJECTED.equalsIgnoreCase(sewerageApplicationDetails.getCurrentState().getValue())) {
            Position initiatorPos = sewerageApplicationDetails.getCurrentState().getInitiatorPosition();
            List<Assignment> assignmentList = assignmentService.getAssignmentsForPosition(
                    initiatorPos.getId(), new Date());
            Optional<Assignment> assignment = assignmentList.stream().findAny();
            approverName = assignment.isPresent() ? assignment.get().getEmployee().getName() : "";
            nextDesign = initiatorPos.getDeptDesig().getDesignation().getName();
        }
        model.addAttribute(APPROVER_NAME, approverName);
        model.addAttribute("currentUserDesgn", currentUserDesgn);
        model.addAttribute("nextDesign", nextDesign);

        model.addAttribute("cityName", ApplicationThreadLocals.getCityName());
        model.addAttribute("mode", "ack");
        setCommonDetails(sewerageApplicationDetails, modelMap, request);
        final boolean inspectionFeeCollectionRequired = sewerageTaxUtils.isInspectionFeeCollectionRequired();
        model.addAttribute(INSPECTIONFEEREQUIRED, inspectionFeeCollectionRequired);
        if (inspectionFeeCollectionRequired)
            model.addAttribute("inspectionDetails", sewerageApplicationDetails.getConnectionFees());
        model.addAttribute("documentNamesList",
                sewerageConnectionService.getSewerageApplicationDoc(sewerageApplicationDetails));
        return new ModelAndView("application-success", "sewerageApplicationDetails", sewerageApplicationDetails);
    }

    private void setCommonDetails(final SewerageApplicationDetails sewerageApplicationDetails, final ModelMap modelMap,
                                  final HttpServletRequest request) {
        final String assessmentNumber = sewerageApplicationDetails.getConnectionDetail()
                .getPropertyIdentifier();

        final AssessmentDetails propertyOwnerDetails = sewerageThirdPartyServices.getPropertyDetails(assessmentNumber,
                request);
        if (propertyOwnerDetails != null)
            modelMap.addAttribute("propertyOwnerDetails", propertyOwnerDetails);
        final PropertyTaxDetails propertyTaxDetails = propertyExternalService.getPropertyTaxDetails(assessmentNumber, null, null);
        modelMap.addAttribute("propertyTax", propertyTaxDetails.getTotalTaxAmt());
    }

    @GetMapping("/new-sewerage-ackowledgement/{appNo}")
    public String showAcknowledgment(@PathVariable final String appNo, final Model model) {
        model.addAttribute("applicationNo", appNo);
        return "sewerage-acknowledgement";
    }

    @GetMapping("/printacknowledgement")
    @ResponseBody
    public ResponseEntity<byte[]> printAck(@RequestParam("appNo") final String appNo, final Model model,
                                           final HttpServletRequest request) {
        byte[] reportOutput;
        final String cityMunicipalityName = (String) request.getSession()
                .getAttribute("citymunicipalityname");
        final String cityName = (String) request.getSession().getAttribute("cityname");
        final SewerageApplicationDetails sewerageApplicationDetails = sewerageApplicationDetailsService
                .findByApplicationNumber(appNo);

        if (sewerageApplicationDetails != null) {
            reportOutput = sewerageApplicationDetailsService
                    .getReportParamsForSewerageAcknowledgement(sewerageApplicationDetails, cityMunicipalityName, cityName)
                    .getReportOutputData();
            if (reportOutput != null) {
                final HttpHeaders headers = new HttpHeaders();

                headers.setContentType(MediaType.parseMediaType(APPLICATION_PDF));
                headers.add("content-disposition", "inline;filename=new-sewerage-ack.pdf");
                return new ResponseEntity<>(reportOutput, headers, HttpStatus.CREATED);
            }
        }
        return null;
    }

    public void prepareNewForm(final SewerageApplicationDetails sewerageApplicationDetails,
                               final Model model) {
        final Boolean isEmployee = securityUtils.currentUserIsEmployee();
        final Boolean isCitizenPortalUser = securityUtils.currentUserIsCitizen();
        final SewerageApplicationType applicationType = sewerageApplicationTypeService
                .findByCode(NEWSEWERAGECONNECTION);
        sewerageApplicationDetails.setApplicationType(applicationType);
        model.addAttribute("allowIfPTDueExists", sewerageTaxUtils.isNewConnectionAllowedIfPTDuePresent());
        model.addAttribute("propertyTypes", PropertyType.values());
        model.addAttribute("isEmployee", isEmployee);
        model.addAttribute("isCitizenPortalUser", isCitizenPortalUser);
        model.addAttribute("additionalRule", sewerageApplicationDetails.getApplicationType().getCode());
        sewerageApplicationDetails.getWorkflowContainer().setAdditionalRule(sewerageApplicationDetails.getApplicationType().getCode());
        prepareWorkflow(model, sewerageApplicationDetails, sewerageApplicationDetails.getWorkflowContainer());
        model.addAttribute("stateType", sewerageApplicationDetails.getClass().getSimpleName());
        model.addAttribute("typeOfConnection", NEWSEWERAGECONNECTION);

    }
}