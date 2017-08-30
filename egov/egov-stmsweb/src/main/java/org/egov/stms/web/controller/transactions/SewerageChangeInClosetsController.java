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
package org.egov.stms.web.controller.transactions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.entity.Source;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.security.utils.SecurityUtils;
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
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.egov.stms.web.controller.utils.SewerageApplicationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/transactions")
public class SewerageChangeInClosetsController extends GenericWorkFlowController {

    private final SewerageApplicationDetailsService sewerageApplicationDetailsService;
    private static final String MESSAGE = "message";
    private static final String COMMON_ERROR_PAGE = "common-error";
    private static final String PTASSESSMENT_NUMBER = "ptAssessmentNo";

    @Autowired
    private final SewerageTaxUtils sewerageTaxUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private FeesDetailMasterService feesDetailMasterService;

    @Autowired
    private SewerageConnectionService sewerageConnectionService;

    @Autowired
    private SewerageApplicationTypeService sewerageApplicationTypeService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private DocumentTypeMasterService documentTypeMasterService;

    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;

    @Autowired
    private PropertyExternalService propertyExternalService;

    @Autowired
    private SewerageApplicationValidator sewerageApplicationValidator;
    @Autowired
    private SewerageWorkflowService sewerageWorkflowService;
    @Autowired
    private MessageSource messageSource;


    @Autowired
    public SewerageChangeInClosetsController(final SewerageApplicationDetailsService sewerageApplicationDetailsService,
            final SewerageTaxUtils sewerageTaxUtils) {
        this.sewerageApplicationDetailsService = sewerageApplicationDetailsService;
        this.sewerageTaxUtils = sewerageTaxUtils;
    }

    @RequestMapping(value = "/modifyConnection/{shscNumber}", method = RequestMethod.GET)
    public String showNewApplicationForm(@ModelAttribute final SewerageApplicationDetails sewerageApplicationDetails,
            @PathVariable final String shscNumber, final Model model, final HttpServletRequest request,
            final BindingResult errors) {
        BigDecimal taxDue;
        SewerageApplicationDetails applicationDetails;
        final SewerageConnection sewerageConnection = sewerageConnectionService.findByShscNumber(shscNumber);
        final SewerageApplicationDetails sewerageApplicationDetailsFromDB = sewerageApplicationDetailsService
                .findByConnection_ShscNumberAndIsActive(shscNumber);
        final Boolean isCitizenPortalUser= sewerageWorkflowService.isCitizenPortalUser(securityUtils.getCurrentUser());
        if (StringUtils.isNotBlank(shscNumber)) {
            applicationDetails = sewerageApplicationDetailsService.isApplicationInProgress(shscNumber);
            if (applicationDetails != null)
                if (SewerageTaxConstants.CHANGEINCLOSETS.equalsIgnoreCase(applicationDetails.getApplicationType().getCode())) {
                    model.addAttribute(MESSAGE, "msg.validate.changenoofclosets.application.inprogress");
                    return COMMON_ERROR_PAGE;
                } else if (SewerageTaxConstants.CLOSESEWERAGECONNECTION
                        .equalsIgnoreCase(applicationDetails.getApplicationType().getCode())) {
                    model.addAttribute(MESSAGE, "msg.validate.closeconnection.application.inprogress");
                    return COMMON_ERROR_PAGE;
                }

            if (sewerageApplicationDetailsFromDB.getConnectionDetail() != null
                    && sewerageApplicationDetailsFromDB.getConnectionDetail().getPropertyIdentifier() != null) {
                final AssessmentDetails assessmentDetails = sewerageThirdPartyServices
                        .getPropertyDetails(sewerageApplicationDetailsFromDB.getConnectionDetail().getPropertyIdentifier(),
                                request);
                if (assessmentDetails != null && assessmentDetails.getPropertyDetails() != null &&
                        assessmentDetails.getPropertyDetails().getTaxDue().compareTo(BigDecimal.ZERO) > 0) {
                    model.addAttribute(MESSAGE, "msg.sewerageapplication.propertytax.isdue");
                    return COMMON_ERROR_PAGE;
                }

                final HashMap<String, Object> result = sewerageThirdPartyServices.getWaterTaxDueAndCurrentTax(
                        sewerageApplicationDetailsFromDB.getConnectionDetail().getPropertyIdentifier(), request);
                final BigDecimal waterTaxDue = (BigDecimal) result.get("WATERTAXDUE");
                if (waterTaxDue.compareTo(BigDecimal.ZERO) > 0) {
                    model.addAttribute(MESSAGE, "msg.sewerageapplication.watertax.isdue");
                    return COMMON_ERROR_PAGE;
                }
            }
        }

        sewerageApplicationDetails.setConnection(sewerageConnection);
        if (sewerageApplicationDetailsFromDB != null) {
            taxDue = sewerageApplicationDetailsService.getPendingTaxAmount(sewerageApplicationDetailsFromDB);
            if (taxDue.compareTo(BigDecimal.ZERO) > 0) {
                model.addAttribute(MESSAGE, "msg.validate.demandamountdue");
                return COMMON_ERROR_PAGE;
            }

            sewerageApplicationDetails.setConnectionDetail(sewerageApplicationDetailsFromDB.getConnectionDetail());
            model.addAttribute(PTASSESSMENT_NUMBER, sewerageApplicationDetailsFromDB.getConnectionDetail()
                    .getPropertyIdentifier());
        }
        final SewerageApplicationType applicationType = sewerageApplicationTypeService
                .findByCode(SewerageTaxConstants.CHANGEINCLOSETS);
        sewerageApplicationDetails.setApplicationType(applicationType);
        sewerageApplicationDetails.setApplicationDate(new Date());

        model.addAttribute("allowIfPTDueExists", sewerageTaxUtils.isNewConnectionAllowedIfPTDuePresent());
        model.addAttribute("propertyTypes", PropertyType.values());
        model.addAttribute("shscNumber", shscNumber);
        model.addAttribute("additionalRule", sewerageApplicationDetails.getApplicationType().getCode());
        final WorkflowContainer workFlowContainer = new WorkflowContainer();
        workFlowContainer.setAdditionalRule(sewerageApplicationDetails.getApplicationType().getCode());
        prepareWorkflow(model, sewerageApplicationDetails, workFlowContainer);
        model.addAttribute("currentUser", sewerageTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
        model.addAttribute("stateType", sewerageApplicationDetails.getClass().getSimpleName());
        model.addAttribute("typeOfConnection", SewerageTaxConstants.CHANGEINCLOSETS);
        final boolean inspectionFeeCollectionRequired = sewerageTaxUtils.isInspectionFeeCollectionRequired();
        model.addAttribute("inspectionFeesCollectionRequired", inspectionFeeCollectionRequired);
        if (inspectionFeeCollectionRequired)
            createSewerageConnectionFee(sewerageApplicationDetails, SewerageTaxConstants.FEE_INSPECTIONCHARGE);

        model.addAttribute("mode", "edit");
        model.addAttribute("isCitizenPortalUser", isCitizenPortalUser);
        return "changeInClosetsConnection-form";
    }

    @ModelAttribute("documentNamesList")
    public List<SewerageApplicationDetailsDocument> documentTypeMasterList(
            @ModelAttribute final SewerageApplicationDetails sewerageApplicationDetails) {
        final List<SewerageApplicationDetailsDocument> tempDocList = new ArrayList<>(0);
        final SewerageApplicationType applicationType = sewerageApplicationTypeService
                .findByCode(SewerageTaxConstants.CHANGEINCLOSETS);
        final List<DocumentTypeMaster> documentTypeMasterList = documentTypeMasterService
                .getAllActiveDocumentTypeMasterByApplicationType(applicationType);
        if (sewerageApplicationDetails != null)
            for (final DocumentTypeMaster dtm : documentTypeMasterList) {
                final SewerageApplicationDetailsDocument sad = new SewerageApplicationDetailsDocument();
                if (dtm != null) {
                    sad.setDocumentTypeMaster(dtm);
                    tempDocList.add(sad);
                }
            }
        return tempDocList;
    }

    @RequestMapping(value = "/modifyConnection/{shscNumber}", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final SewerageApplicationDetails sewerageApplicationDetails,
            @PathVariable final String shscNumber, final BindingResult resultBinder, final RedirectAttributes redirectAttributes,
            final HttpServletRequest request, final Model model, @RequestParam String workFlowAction,
            @RequestParam("files") final MultipartFile[] files) {

        sewerageApplicationDetails.getConnectionDetail().setPropertyIdentifier(request.getParameter(PTASSESSMENT_NUMBER));
        sewerageApplicationValidator.validateChangeInClosetsApplication(sewerageApplicationDetails, resultBinder, request);
        final Boolean citizenPortalUser = sewerageWorkflowService.isCitizenPortalUser(securityUtils.getCurrentUser());
        final Boolean isEmployee = sewerageWorkflowService.isEmployee(securityUtils.getCurrentUser());

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
            model.addAttribute("validateIfPTDueExists", sewerageTaxUtils.isNewConnectionAllowedIfPTDuePresent());
            model.addAttribute("propertyTypes", PropertyType.values());
            model.addAttribute("mode", "edit");
            model.addAttribute(PTASSESSMENT_NUMBER, sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier());
            prepareWorkflow(model, sewerageApplicationDetails, new WorkflowContainer());
            model.addAttribute("additionalRule", sewerageApplicationDetails.getApplicationType().getCode());
            model.addAttribute("currentUser", sewerageTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
            model.addAttribute("approvalPosOnValidate", request.getParameter("approvalPosition"));
            model.addAttribute("stateType", sewerageApplicationDetails.getClass().getSimpleName());
            return "changeInClosetsConnection-form";
        }
        /**
         * If inspection fee required to be collected, then change status to fee collection pending.
         */
        if (null == sewerageApplicationDetails.getState()) {
            if (isEmployee) {
                sewerageApplicationDetails.setSource(Source.SYSTEM.name());
                if (sewerageTaxUtils.isInspectionFeeCollectionRequired())
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                            SewerageTaxConstants.APPLICATION_STATUS_COLLECTINSPECTIONFEE, SewerageTaxConstants.MODULETYPE));
                else
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                            SewerageTaxConstants.APPLICATION_STATUS_CREATED, SewerageTaxConstants.MODULETYPE));
            }
            else if(citizenPortalUser){
                    sewerageApplicationDetails.setSource(Source.CITIZENPORTAL.toString()); 
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                            SewerageTaxConstants.APPLICATION_STATUS_FEECOLLECTIONPENDING, SewerageTaxConstants.MODULETYPE));
            }
        }
        sewerageApplicationDetails.getAppDetailsDocument().clear();
        sewerageApplicationDetails.setAppDetailsDocument(applicationDocs);
        sewerageConnectionService.processAndStoreApplicationDocuments(sewerageApplicationDetails);

        Long approvalPosition = 0l;
        String approvalComment = "";
        String approverName = "";
        String nextDesignation = "";
        if (request.getParameter("approvalComment") != null)
            approvalComment = request.getParameter("approvalComent");
        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");
        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
        if (citizenPortalUser) {
            Assignment assignment = sewerageWorkflowService.getMappedAssignmentForCscOperator(sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier());
            if (assignment != null) {
                approvalPosition = assignment.getPosition().getId();
                approverName = assignment.getEmployee().getName();
                nextDesignation = assignment.getDesignation().getName();

            }
        }
        populateFeesDetails(sewerageApplicationDetails);

        final SewerageConnection sewerageConnection = sewerageConnectionService.findByShscNumber(shscNumber);
        sewerageConnection.setStatus(SewerageConnectionStatus.INPROGRESS);
        sewerageApplicationDetails.setConnection(sewerageConnection);
        sewerageConnection.addApplicantDetails(sewerageApplicationDetails);

        final SewerageApplicationDetails newSewerageApplicationDetails = sewerageApplicationDetailsService
                .createNewSewerageConnection(sewerageApplicationDetails, approvalPosition, approvalComment,
                        sewerageApplicationDetails.getApplicationType().getCode(), files, workFlowAction, request);

        final Assignment currentUserAssignment = assignmentService.getPrimaryAssignmentForGivenRange(securityUtils
                .getCurrentUser().getId(), new Date(), new Date());

        Assignment assignObj = null;
        List<Assignment> asignList = null;
        if (approvalPosition != null)
            assignObj = assignmentService.getPrimaryAssignmentForPositon(approvalPosition);

        if (assignObj != null) {
            asignList = new ArrayList<>();
            asignList.add(assignObj);
        } else if (assignObj == null && approvalPosition != null)
            asignList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());

        final String nextDesign = asignList != null && !asignList.isEmpty() ? asignList.get(0).getDesignation().getName() : "";

        final String pathVars = newSewerageApplicationDetails.getApplicationNumber() + ","
                + sewerageTaxUtils.getApproverName(approvalPosition) + ","
                + (currentUserAssignment != null ? currentUserAssignment.getDesignation().getName() : "") + ","
                + (nextDesign != null ? nextDesign : "");
        String message = messageSource.getMessage("msg.success.forward",
                new String[] { approverName.concat("~").concat(nextDesignation),
                        newSewerageApplicationDetails.getApplicationNumber() },
                null);
        model.addAttribute("message", message);
         if(citizenPortalUser)
            return "redirect:/citizen/search/sewerageGenerateonlinebill/" + newSewerageApplicationDetails.getApplicationNumber() +"/"+ newSewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier();
         else
        return "redirect:/transactions/changeInClosets-success?pathVars=" + pathVars;
    }

    @RequestMapping(value = "/changeInClosets-success", method = RequestMethod.GET)
    public ModelAndView successView(@ModelAttribute SewerageApplicationDetails sewerageApplicationDetails,
            final HttpServletRequest request, final Model model, final ModelMap modelMap) {
        final String[] keyNameArray = request.getParameter("pathVars").split(",");
        String applicationNumber = "";
        String approverName = "";
        String currentUserDesgn = "";
        String nextDesign = "";
        if (keyNameArray.length != 0 && keyNameArray.length > 0)
            if (keyNameArray.length == 1)
                applicationNumber = keyNameArray[0];
            else if (keyNameArray.length == 3) {
                applicationNumber = keyNameArray[0];
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
            } else {
                applicationNumber = keyNameArray[0];
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
                nextDesign = keyNameArray[3];
            }

        if (applicationNumber != null)
            sewerageApplicationDetails = sewerageApplicationDetailsService.findByApplicationNumber(applicationNumber);
        model.addAttribute("approverName", approverName);
        model.addAttribute("currentUserDesgn", currentUserDesgn);
        model.addAttribute("nextDesign", nextDesign);

        model.addAttribute("cityName", ApplicationThreadLocals.getCityName());
        model.addAttribute("mode", "ack");
        setCommonDetails(sewerageApplicationDetails, modelMap, request);
        final boolean inspectionFeeCollectionRequired = sewerageTaxUtils.isInspectionFeeCollectionRequired();
        model.addAttribute("inspectionFeesCollectionRequired", inspectionFeeCollectionRequired);
        if (inspectionFeeCollectionRequired)
            model.addAttribute("inspectionDetails", sewerageApplicationDetails.getConnectionFees());
        model.addAttribute("documentNamesList",
                sewerageConnectionService.getSewerageApplicationDoc(sewerageApplicationDetails));
        return new ModelAndView("changeInClosets-success", "sewerageApplicationDetails", sewerageApplicationDetails);
    }

    private void setCommonDetails(final SewerageApplicationDetails sewerageApplicationDetails, final ModelMap modelMap,
            final HttpServletRequest request) {
        final String assessmentNumber = sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier();

        final AssessmentDetails propertyOwnerDetails = sewerageThirdPartyServices.getPropertyDetails(assessmentNumber,
                request);
        if (propertyOwnerDetails != null)
            modelMap.addAttribute("propertyOwnerDetails", propertyOwnerDetails);
        final PropertyTaxDetails propertyTaxDetails = propertyExternalService.getPropertyTaxDetails(assessmentNumber, null, null);
        modelMap.addAttribute("propertyTax", propertyTaxDetails.getTotalTaxAmt());
    }

    private void populateFeesDetails(final SewerageApplicationDetails sewerageApplicationDetails) {
        if (sewerageApplicationDetails.getConnectionFees() != null
                && !sewerageApplicationDetails.getConnectionFees().isEmpty())
            for (final SewerageConnectionFee scf : sewerageApplicationDetails.getConnectionFees())
                scf.setApplicationDetails(sewerageApplicationDetails);
    }

    public void createSewerageConnectionFee(final SewerageApplicationDetails sewerageApplicationDetails,
            final String feeCode) {

        final List<FeesDetailMaster> inspectionFeeList = feesDetailMasterService
                .findAllActiveFeesDetailByFeesCode(feeCode);
        for (final FeesDetailMaster feeDetailMaster : inspectionFeeList) {
            final SewerageConnectionFee connectionFee = new SewerageConnectionFee();
            connectionFee.setFeesDetail(feeDetailMaster);
            if (feeDetailMaster.getIsFixedRate())
                connectionFee.setAmount(feeDetailMaster.getAmount().doubleValue());
            connectionFee.setApplicationDetails(sewerageApplicationDetails);
            sewerageApplicationDetails.getConnectionFees().add(connectionFee);

        }
    }

}