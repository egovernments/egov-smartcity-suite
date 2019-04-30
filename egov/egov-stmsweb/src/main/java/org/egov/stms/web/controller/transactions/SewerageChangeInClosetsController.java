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

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_REJECTED;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.commons.entity.Source;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.config.core.ApplicationThreadLocals;
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
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.egov.stms.web.controller.utils.SewerageApplicationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/transactions")
public class SewerageChangeInClosetsController extends GenericWorkFlowController {

    private static final String APPROVAL_POSITION = "approvalPosition";
    private static final String MESSAGE = "message";
    private static final String COMMON_ERROR_PAGE = "common-error";
    private static final String PTASSESSMENT_NUMBER = "ptAssessmentNo";
    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;
    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;
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

    @GetMapping("/modifyConnection/{shscNumber}")
    public String showNewApplicationForm(@ModelAttribute final SewerageApplicationDetails sewerageApplicationDetails,
                                         @PathVariable final String shscNumber, final Model model, final HttpServletRequest request,
                                         final BindingResult errors) {
        BigDecimal taxDue;
        SewerageApplicationDetails applicationDetails;
        final SewerageConnection sewerageConnection = sewerageConnectionService.findByShscNumber(shscNumber);
        final SewerageApplicationDetails sewerageApplicationDetailsFromDB = sewerageApplicationDetailsService
                .findByShscNumberAndIsActive(shscNumber);
        if (isNotBlank(shscNumber)) {
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
        final boolean inspectionFeeCollectionRequired = sewerageTaxUtils.isInspectionFeeCollectionRequired();
        prepareChangeForm(sewerageApplicationDetails, shscNumber, model, securityUtils.currentUserIsCitizen());
        if (inspectionFeeCollectionRequired)
            createSewerageConnectionFee(sewerageApplicationDetails, SewerageTaxConstants.FEE_INSPECTIONCHARGE);
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

    @PostMapping("/modifyConnection/{shscNumber}")
    public String create(@Valid @ModelAttribute final SewerageApplicationDetails sewerageApplicationDetails,
                         @PathVariable final String shscNumber, final BindingResult resultBinder,
                         final HttpServletRequest request, final Model model,
                         @RequestParam("files") final MultipartFile[] files) {

        sewerageApplicationDetails.getConnectionDetail().setPropertyIdentifier(request.getParameter(PTASSESSMENT_NUMBER));
        sewerageApplicationValidator.validateChangeInClosetsApplication(sewerageApplicationDetails, resultBinder, request);
        final Boolean citizenPortalUser = securityUtils.currentUserIsCitizen();
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
            prepareChangeForm(sewerageApplicationDetails, shscNumber, model, citizenPortalUser);
            model.addAttribute("approvalPosOnValidate", request.getParameter(APPROVAL_POSITION));
            model.addAttribute(PTASSESSMENT_NUMBER, sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier());
            return "changeInClosetsConnection-form";
        }
        /**
         * If inspection fee required to be collected, then change status to fee collection pending.
         */
        if (sewerageApplicationDetails.getState() == null)
            if (securityUtils.currentUserIsEmployee()) {
                sewerageApplicationDetails.setSource(Source.SYSTEM.name());
                if (sewerageTaxUtils.isInspectionFeeCollectionRequired())
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                            SewerageTaxConstants.APPLICATION_STATUS_COLLECTINSPECTIONFEE, SewerageTaxConstants.MODULETYPE));
                else
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                            SewerageTaxConstants.APPLICATION_STATUS_CREATED, SewerageTaxConstants.MODULETYPE));
            } else if (citizenPortalUser) {
                sewerageApplicationDetails.setSource(Source.CITIZENPORTAL.toString());
                sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                        SewerageTaxConstants.APPLICATION_STATUS_FEECOLLECTIONPENDING, SewerageTaxConstants.MODULETYPE));
            }
        sewerageApplicationDetails.getAppDetailsDocument().clear();
        sewerageApplicationDetails.setAppDetailsDocument(applicationDocs);
        sewerageConnectionService.processAndStoreApplicationDocuments(sewerageApplicationDetails);
        /**
         * populate fee details
         */
        populateFeesDetails(sewerageApplicationDetails);
        final SewerageConnection sewerageConnection = sewerageConnectionService.findByShscNumber(shscNumber);
        sewerageConnection.setStatus(SewerageConnectionStatus.INPROGRESS);
        sewerageApplicationDetails.setConnection(sewerageConnection);
        sewerageConnection.addApplicantDetails(sewerageApplicationDetails);
        final SewerageApplicationDetails newSewerageApplicationDetails = sewerageApplicationDetailsService
                .createNewSewerageConnection(sewerageApplicationDetails, files, request);
        final Assignment currentUserAssignment = assignmentService.getPrimaryAssignmentForGivenRange(securityUtils
                .getCurrentUser().getId(), new Date(), new Date());
        Long approvalPosition = sewerageApplicationDetails.getWorkflowContainer().getApproverPositionId();
        String approverName = EMPTY;
        String nextDesignation = EMPTY;
        if (citizenPortalUser) {
            Assignment assignment = sewerageWorkflowService.getMappedAssignmentForCscOperator(sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier());
            if (assignment != null) {
                approvalPosition = assignment.getPosition().getId();
                approverName = assignment.getEmployee().getName();
                nextDesignation = assignment.getDesignation().getName();
            }
        } else {
            Assignment assignObj = null;
            List<Assignment> asignList = new ArrayList<>();
            if (approvalPosition != null)
                assignObj = assignmentService.getPrimaryAssignmentForPositon(approvalPosition);

            if (assignObj != null) {
                asignList.add(assignObj);
            } else if (assignObj == null && approvalPosition != null)
                asignList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());

            nextDesignation = asignList.isEmpty() ? EMPTY : asignList.get(0).getDesignation().getName();
        }
        final String pathVars = newSewerageApplicationDetails.getApplicationNumber() + ","
                + sewerageTaxUtils.getApproverName(approvalPosition) + ","
                + (currentUserAssignment != null ? currentUserAssignment.getDesignation().getName() : EMPTY) + ","
                + (nextDesignation == null ? EMPTY : nextDesignation);
        String message = messageSource.getMessage("msg.success.forward",
                new String[]{null != approverName ? approverName.concat("~").concat(nextDesignation) : EMPTY,
                        newSewerageApplicationDetails.getApplicationNumber()},
                null);
        model.addAttribute(MESSAGE, message);
        if (citizenPortalUser)
            return "redirect:/citizen/search/sewerageGenerateonlinebill/" + newSewerageApplicationDetails.getApplicationNumber() + "/" + newSewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier();
        else
            return "redirect:/transactions/changeInClosets-success?pathVars=" + pathVars;
    }

    @GetMapping("/changeInClosets-success")
    public ModelAndView successView(@ModelAttribute SewerageApplicationDetails changeInClosetObj, final HttpServletRequest request, final Model model, final ModelMap modelMap) {
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
            changeInClosetObj = sewerageApplicationDetailsService.findByApplicationNumber(applicationNumber);

        if (WF_STATE_REJECTED.equalsIgnoreCase(changeInClosetObj.getCurrentState().getValue())) {
            Position initiatorPos = changeInClosetObj.getCurrentState().getInitiatorPosition();
            List<Assignment> assignmentList = assignmentService.getAssignmentsForPosition(
                    initiatorPos.getId(), new Date());
            Optional<Assignment> assignment = assignmentList.stream().findAny();
            approverName = assignment.isPresent() ? assignment.get().getEmployee().getName() : EMPTY;
            nextDesign = initiatorPos.getDeptDesig().getDesignation().getName();
        }
        model.addAttribute("approverName", approverName);
        model.addAttribute("currentUserDesgn", currentUserDesgn);
        model.addAttribute("nextDesign", nextDesign);
        model.addAttribute("cityName", ApplicationThreadLocals.getCityName());
        model.addAttribute("mode", "ack");
        setCommonDetails(changeInClosetObj, modelMap, request);
        model.addAttribute("inspectionDetails", changeInClosetObj.getConnectionFees());

        final boolean inspectionFeeCollectionRequired = sewerageTaxUtils.isInspectionFeeCollectionRequired();
        model.addAttribute("inspectionFeesCollectionRequired", inspectionFeeCollectionRequired);
        if (inspectionFeeCollectionRequired) {
            model.addAttribute("documentNamesList",
                    sewerageConnectionService.getSewerageApplicationDoc(changeInClosetObj));
        }
        return new ModelAndView("changeInClosets-success", "sewerageApplicationDetails", changeInClosetObj);
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

    public void prepareChangeForm(final SewerageApplicationDetails sewerageApplicationDetails, final String shscNumber,
                                  final Model model, final Boolean isCitizenPortalUser) {
        model.addAttribute("allowIfPTDueExists", sewerageTaxUtils.isNewConnectionAllowedIfPTDuePresent());
        model.addAttribute("propertyTypes", PropertyType.values());
        model.addAttribute("shscNumber", shscNumber);
        model.addAttribute("additionalRule", sewerageApplicationDetails.getApplicationType().getCode());
        sewerageApplicationDetails.getWorkflowContainer().setAdditionalRule(sewerageApplicationDetails.getApplicationType().getCode());
        prepareWorkflow(model, sewerageApplicationDetails, sewerageApplicationDetails.getWorkflowContainer());
        model.addAttribute("stateType", sewerageApplicationDetails.getClass().getSimpleName());
        model.addAttribute("typeOfConnection", SewerageTaxConstants.CHANGEINCLOSETS);
        final boolean inspectionFeeCollectionRequired = sewerageTaxUtils.isInspectionFeeCollectionRequired();
        model.addAttribute("inspectionFeesCollectionRequired", inspectionFeeCollectionRequired);
        model.addAttribute("mode", "edit");
        model.addAttribute("isCitizenPortalUser", isCitizenPortalUser);
        model.addAttribute("additionalRule", sewerageApplicationDetails.getApplicationType().getCode());
    }
}