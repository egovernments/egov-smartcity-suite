/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
package org.egov.wtms.web.controller.application;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.egov.commons.entity.Source.CSC;
import static org.egov.commons.entity.Source.MEESEVA;
import static org.egov.commons.entity.Source.ONLINE;
import static org.egov.commons.entity.Source.SYSTEM;
import static org.egov.commons.entity.Source.WARDSECRETARY;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ADDNLCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CONN_NAME_ADDNLCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NEWCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PRIMARYCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WARDSECRETARY_SOURCE_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WARDSECRETARY_TRANSACTIONID_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WARDSECRETARY_WSPORTAL_REQUEST;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERCHARGES_CONSUMERCODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WARDSECRETARY_EVENTPUBLISH_MODE_CREATE;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.integration.service.ThirdPartyService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pims.commons.Position;
import org.egov.stms.masters.entity.SewerageApplicationType;
import org.egov.stms.masters.service.DocumentTypeMasterService;
import org.egov.stms.masters.service.SewerageApplicationTypeService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageApplicationDetailsDocument;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageConnectionService;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.egov.wtms.application.entity.ApplicationDocuments;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.NewConnectionService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.DocumentNames;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.masters.service.MeterCostService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "/application")
public class NewConnectionController extends GenericConnectionController {

    private static final String TYPE_OF_CONNECTION = "typeOfConnection";
    private static final String SEWERAGE_TAX_MANAGEMENT = "Sewerage Tax Management";
    private static final Logger LOG = LoggerFactory.getLogger(NewConnectionController.class);
    private static final String CONNECTION_PROPERTYID = "connection.propertyIdentifier";
    private static final String ADDITIONALRULE = "additionalRule";
    private static final String CURRENTUSER = "currentUser";
    private static final String RADIOBUTTONMAP = "radioButtonMap";
    private static final String STATETYPE = "stateType";
    private static final String NEWCONNECTION_FORM = "newconnection-form";
    private static final String VALIDIFPTDUEEXISTS = "validateIfPTDueExists";
    private static final String APPROVALPOSITION = "approvalPosition";

    @Autowired
    private WaterConnectionDetailsService waterConnectionDtlsService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @Autowired
    private NewConnectionService newConnectionService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private DocumentTypeMasterService documentTypeMasterService;

    @Autowired
    private SewerageApplicationTypeService sewerageApplicationTypeService;

    @Autowired
    private SewerageApplicationDetailsService sewerageDetailsService;

    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;

    @Autowired
    private SewerageConnectionService sewerageConnectionService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private MeterCostService meterCostService;
    
    @Autowired
    private transient ThirdPartyService thirdPartyService;

    @ModelAttribute("documentNamesList")
    public List<DocumentNames> documentNamesList(@ModelAttribute WaterConnectionDetails waterConnectionDetails) {
        waterConnectionDetails.setApplicationType(applicationTypeService.findByCode(NEWCONNECTION));
        return waterConnectionDtlsService.getAllActiveDocumentNames(waterConnectionDetails.getApplicationType());
    }

    @RequestMapping(value = "/newConnection-newform")
    public String showNewApplicationForm(@ModelAttribute WaterConnectionDetails waterConnectionDetails,
            Model model, HttpServletRequest request) {
    	User currentUser = securityUtils.getCurrentUser();
        boolean wsPortalRequest = Boolean.valueOf(request.getParameter(WARDSECRETARY_WSPORTAL_REQUEST));
        if (!thirdPartyService.isValidWardSecretaryRequest(wsPortalRequest))
            throw new ApplicationRuntimeException("WS.001");
        
        boolean isWardSecretaryUser = thirdPartyService.isWardSecretaryRequest(wsPortalRequest);
		waterConnectionDetails.setApplicationDate(new Date());
        waterConnectionDetails.setConnectionStatus(ConnectionStatus.INPROGRESS);
        model.addAttribute("allowIfPTDueExists", waterTaxUtils.isNewConnectionAllowedIfPTDuePresent());
        model.addAttribute(ADDITIONALRULE, waterConnectionDetails.getApplicationType().getCode());
        WorkflowContainer workflowContainer = new WorkflowContainer();
        workflowContainer.setAdditionalRule(waterConnectionDetails.getApplicationType().getCode());
        prepareWorkflow(model, waterConnectionDetails, workflowContainer);
        boolean sewerageTaxenabled = moduleService.getModuleByName(SEWERAGE_TAX_MANAGEMENT).isEnabled();
        model.addAttribute("sewerageTaxenabled", sewerageTaxenabled);
        // sewerage module integration
        if (sewerageTaxenabled)
            waterConnectionDtlsService.prepareNewForm(model, waterConnectionDetails);
        model.addAttribute(CURRENTUSER, waterTaxUtils.getCurrentUserRole(currentUser));
        model.addAttribute(STATETYPE, waterConnectionDetails.getClass().getSimpleName());
        model.addAttribute("documentName", waterTaxUtils.documentRequiredForBPLCategory());
        model.addAttribute(TYPE_OF_CONNECTION, NEWCONNECTION);
        model.addAttribute("isCSCOperator", waterTaxUtils.isCSCoperator(currentUser));
        model.addAttribute("citizenPortalUser", waterTaxUtils.isCitizenPortalUser(currentUser));
        model.addAttribute("isAnonymousUser", waterTaxUtils.isAnonymousUser(currentUser));
        model.addAttribute("isWardSecretaryUser", isWardSecretaryUser);
		if (waterTaxUtils.isMeesevaUser(currentUser))
			if (request.getParameter("applicationNo") == null)
				throw new ApplicationRuntimeException("MEESEVA.005");
			else
				waterConnectionDetails.setMeesevaApplicationNumber(request.getParameter("applicationNo"));
		if (isWardSecretaryUser) {
			String wsTransactionId = request.getParameter("transactionId");
			String wsSource = request.getParameter("source");
			if (ThirdPartyService.validateWardSecretaryRequest(wsTransactionId, wsSource))
				throw new ApplicationRuntimeException("WS.001");
			else {
				model.addAttribute(WARDSECRETARY_TRANSACTIONID_CODE, wsTransactionId);
				model.addAttribute(WARDSECRETARY_SOURCE_CODE, wsSource);
				model.addAttribute(WARDSECRETARY_WSPORTAL_REQUEST, wsPortalRequest);
			}
		}
        return NEWCONNECTION_FORM;
    }

    @GetMapping(value = "/newConnection-dataEntryForm")
    public String dataEntryForm(@ModelAttribute WaterConnectionDetails waterConnectionDetails, Model model) {

        waterConnectionDetails.setApplicationDate(new Date());
        waterConnectionDetails.setConnectionStatus(ConnectionStatus.ACTIVE);
        Map<Long, String> connectionTypeMap = new HashMap<>();
        connectionTypeMap.put(applicationTypeService.findByCode(NEWCONNECTION).getId(), PRIMARYCONNECTION);
        connectionTypeMap.put(applicationTypeService.findByCode(ADDNLCONNECTION).getId(), CONN_NAME_ADDNLCONNECTION);
        model.addAttribute(RADIOBUTTONMAP, connectionTypeMap);
        model.addAttribute("mode", "dataEntry");
        model.addAttribute(TYPE_OF_CONNECTION, NEWCONNECTION);
        return "newconnection-dataEntryForm";
    }

    @GetMapping(value = "/newConnection-existingMessage/{consumerCode}")
    public String dataEntryMessage(@PathVariable String consumerCode, Model model) {
        model.addAttribute(WATERCHARGES_CONSUMERCODE, consumerCode);
        WaterConnectionDetails waterConnectionDetails = waterConnectionDtlsService
                .findByApplicationNumberOrConsumerCode(consumerCode);
        model.addAttribute("connectionType", waterConnectionDtlsService.getConnectionTypesMap()
                .get(waterConnectionDetails.getConnectionType().name()));
        model.addAttribute("mode", waterConnectionDetails.getId() == null ? EMPTY : "edit");
        return "newconnection-dataEntryMessage";
    }

    @PostMapping(value = "/newConnection-create")
    public String createNewConnection(@Valid @ModelAttribute WaterConnectionDetails waterConnectionDetails,
            BindingResult resultBinder, HttpServletRequest request,
            Model model, @RequestParam String workFlowAction,
            @RequestParam("files") MultipartFile... files) {

        User currentUser = securityUtils.getCurrentUser();
        boolean loggedUserIsMeesevaUser = waterTaxUtils.isMeesevaUser(currentUser);
        String wsTransactionId = request.getParameter(WARDSECRETARY_TRANSACTIONID_CODE);
        String wsSource = request.getParameter(WARDSECRETARY_SOURCE_CODE);
        boolean wsPortalRequest = Boolean.valueOf(request.getParameter(WARDSECRETARY_WSPORTAL_REQUEST));
        boolean isWardSecretaryUser = thirdPartyService.isWardSecretaryRequest(wsPortalRequest);
        
        if (!thirdPartyService.isValidWardSecretaryRequest(wsPortalRequest)
				|| (isWardSecretaryUser && ThirdPartyService.validateWardSecretaryRequest(wsTransactionId, wsSource)))
			throw new ApplicationRuntimeException("WS.001");
        
        boolean isCSCOperator = waterTaxUtils.isCSCoperator(currentUser);
        boolean citizenPortalUser = waterTaxUtils.isCitizenPortalUser(currentUser);
        model.addAttribute("citizenPortalUser", citizenPortalUser);
        boolean isAnonymousUser = waterTaxUtils.isAnonymousUser(currentUser);
        model.addAttribute("isAnonymousUser", isAnonymousUser);
        
		if (!isCSCOperator && !citizenPortalUser && !loggedUserIsMeesevaUser && !isAnonymousUser
				&& !isWardSecretaryUser
				&& !waterTaxUtils.isLoggedInUserJuniorOrSeniorAssistant(currentUser.getId()))
            throw new ValidationException("err.creator.application");
		
        newConnectionService.validatePropertyID(waterConnectionDetails, resultBinder);

        if (ConnectionType.METERED.equals(waterConnectionDetails.getConnectionType()))
            meterCostService.validateMeterMakeForPipesize(waterConnectionDetails.getPipeSize().getId());

        if (ConnectionType.NON_METERED.equals(waterConnectionDetails.getConnectionType()))
            waterConnectionDtlsService.validateWaterRateAndDonationHeader(waterConnectionDetails);
        List<ApplicationDocuments> applicationDocs = new ArrayList<>();
        int i = 0;
        String documentRequired = waterTaxUtils.documentRequiredForBPLCategory();
        if (!waterConnectionDetails.getApplicationDocs().isEmpty())
            for (ApplicationDocuments applicationDocument : waterConnectionDetails.getApplicationDocs()) {
                newConnectionService.validateDocuments(applicationDocs, applicationDocument, i, resultBinder,
                        waterConnectionDetails.getCategory().getId(), documentRequired);
                i++;
            }
        if (waterConnectionDetails.getState() == null)
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                    WaterTaxConstants.APPLICATION_STATUS_CREATED, WaterTaxConstants.MODULETYPE));
        
        if (resultBinder.hasErrors()) {
            waterConnectionDetails.setApplicationDate(new Date());
            boolean sewerageTaxenabled = moduleService.getModuleByName(SEWERAGE_TAX_MANAGEMENT).isEnabled();
            model.addAttribute("sewerageTaxenabled", sewerageTaxenabled);
            // sewerage module integration
            if (sewerageTaxenabled)
                waterConnectionDtlsService.prepareNewForm(model, waterConnectionDetails);
            model.addAttribute(VALIDIFPTDUEEXISTS, waterTaxUtils.isNewConnectionAllowedIfPTDuePresent());
            WorkflowContainer workflowContainer = new WorkflowContainer();
            workflowContainer.setAdditionalRule(waterConnectionDetails.getApplicationType().getCode());
            prepareWorkflow(model, waterConnectionDetails, workflowContainer);
            model.addAttribute(ADDITIONALRULE, waterConnectionDetails.getApplicationType().getCode());
            model.addAttribute(CURRENTUSER, waterTaxUtils.getCurrentUserRole(currentUser));
            model.addAttribute("approvalPosOnValidate", request.getParameter(APPROVALPOSITION));
            model.addAttribute(TYPE_OF_CONNECTION, NEWCONNECTION);
            model.addAttribute(STATETYPE, waterConnectionDetails.getClass().getSimpleName());
            model.addAttribute("documentName", waterTaxUtils.documentRequiredForBPLCategory());
            model.addAttribute("isCSCOperator", waterTaxUtils.isCSCoperator(currentUser));
            model.addAttribute("citizenPortalUser", waterTaxUtils.isCitizenPortalUser(currentUser));
            model.addAttribute("isAnonymousUser", waterTaxUtils.isAnonymousUser(currentUser));
			if (isWardSecretaryUser) {
				model.addAttribute("isWardSecretaryUser", isWardSecretaryUser);
				model.addAttribute(WARDSECRETARY_TRANSACTIONID_CODE, wsTransactionId);
				model.addAttribute(WARDSECRETARY_SOURCE_CODE, wsSource);
				model.addAttribute(WARDSECRETARY_WSPORTAL_REQUEST, wsPortalRequest);
			}
            return NEWCONNECTION_FORM;
        }
        waterConnectionDetails.getApplicationDocs().clear();
        waterConnectionDetails.setApplicationDocs(applicationDocs);

        processAndStoreApplicationDocuments(waterConnectionDetails);

        Long approvalPosition = 0l;
        String approvalComent = "";
        boolean applicationByOthers = waterTaxUtils.getCurrentUserRole(currentUser);

        if (request.getParameter("approvalComent") != null)
            approvalComent = request.getParameter("approvalComent");
        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");
        if (request.getParameter(APPROVALPOSITION) != null && !request.getParameter(APPROVALPOSITION).isEmpty())
            approvalPosition = Long.valueOf(request.getParameter(APPROVALPOSITION));

        if (applicationByOthers || citizenPortalUser || isAnonymousUser || isWardSecretaryUser) {
            Position userPosition = waterTaxUtils
                    .getZonalLevelClerkForLoggedInUser(waterConnectionDetails.getConnection().getPropertyIdentifier(), isWardSecretaryUser);
            if (userPosition != null)
                approvalPosition = userPosition.getId();
            else {
                model.addAttribute(VALIDIFPTDUEEXISTS, waterTaxUtils.isNewConnectionAllowedIfPTDuePresent());
                WorkflowContainer workflowContainer = new WorkflowContainer();
                workflowContainer.setAdditionalRule(waterConnectionDetails.getApplicationType().getCode());
                prepareWorkflow(model, waterConnectionDetails, workflowContainer);
                model.addAttribute(ADDITIONALRULE, waterConnectionDetails.getApplicationType().getCode());
                model.addAttribute("approvalPosOnValidate", request.getParameter(APPROVALPOSITION));
                model.addAttribute(CURRENTUSER, waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
                model.addAttribute(TYPE_OF_CONNECTION, NEWCONNECTION);
                model.addAttribute(STATETYPE, waterConnectionDetails.getClass().getSimpleName());
                resultBinder.rejectValue(CONNECTION_PROPERTYID, "err.validate.connection.user.mapping",
                        "err.validate.connection.user.mapping");
                model.addAttribute("noJAORSAMessage", "No JA/SA exists to forward the application.");
                return NEWCONNECTION_FORM;
            }

        }
        if (isAnonymousUser)
            waterConnectionDetails.setSource(ONLINE);
        else if (isCSCOperator)
            waterConnectionDetails.setSource(CSC);
        else if (citizenPortalUser && (waterConnectionDetails.getSource() == null
                || isBlank(waterConnectionDetails.getSource().toString())))
            waterConnectionDetails.setSource(waterTaxUtils.setSourceOfConnection(securityUtils.getCurrentUser()));
        else if (loggedUserIsMeesevaUser) {
            waterConnectionDetails.setSource(MEESEVA);
            if (waterConnectionDetails.getMeesevaApplicationNumber() != null)
                waterConnectionDetails.setApplicationNumber(waterConnectionDetails.getMeesevaApplicationNumber());
		} else if (isWardSecretaryUser) {
			if (WARDSECRETARY.toString().equals(wsSource))
				waterConnectionDetails.setSource(WARDSECRETARY);
		}
        else
            waterConnectionDetails.setSource(SYSTEM);

        boolean isSewerageChecked = request.getParameter("sewerageApplication") == null ? false : true;
        // sewerage integration
        SewerageApplicationDetails sewerageDetails = new SewerageApplicationDetails();
        if (moduleService.getModuleByName(SEWERAGE_TAX_MANAGEMENT).isEnabled() && isSewerageChecked) {
            if (isCSCOperator)
                sewerageDetails.setSource(CSC.toString());
			else if (isWardSecretaryUser) {
				if (WARDSECRETARY.toString().equals(wsSource))
					sewerageDetails.setSource(WARDSECRETARY.toString());
			}
            if (isAnonymousUser)
                sewerageDetails.setSource(ONLINE.toString());
            sewerageIntegration(waterConnectionDetails, resultBinder, sewerageDetails);
            sewerageApplicationCreate(waterConnectionDetails, request, workFlowAction, files, sewerageDetails, approvalPosition,
                    approvalComent);
        }

		if (isWardSecretaryUser)
			waterConnectionDtlsService.persistAndPublishEventForWardSecretary(waterConnectionDetails, request,
					workFlowAction, approvalPosition, approvalComent, WARDSECRETARY_EVENTPUBLISH_MODE_CREATE);
		else
			waterConnectionDtlsService.createNewWaterConnection(waterConnectionDetails, approvalPosition,
					approvalComent, waterConnectionDetails.getApplicationType().getCode(), workFlowAction);

        if (LOG.isDebugEnabled())
            LOG.debug("createNewWaterConnection is completed ");

        String sewerageAppnumber = sewerageDetails.getApplicationNumber() == null ? " " : sewerageDetails.getApplicationNumber();
        if (loggedUserIsMeesevaUser)
            return "redirect:/application/generate-meesevareceipt?transactionServiceNumber="
                    + waterConnectionDetails.getApplicationNumber();
        else
            return "redirect:/application/citizeenAcknowledgement?pathVars=" + waterConnectionDetails.getApplicationNumber()
                    + "&sewerageAppNo=" + sewerageAppnumber;
    }

    @ModelAttribute
    public WaterConnectionDetails loadByConsumerNo(@RequestParam(name = "id", required = false) Long id) {
        return id == null ? new WaterConnectionDetails() : waterConnectionDtlsService.findBy(id);
    }

    // used to create/update existing details
    @PostMapping(value = "/newConnection-dataEntryForm")
    public String createExisting(@Valid @ModelAttribute WaterConnectionDetails waterConnectionDetails,
            BindingResult resultBinder, Model model) {
        return createAndUpdateDataEntryRecord(waterConnectionDetails, resultBinder, model);
    }

    private String createAndUpdateDataEntryRecord(WaterConnectionDetails waterConnectionDetails,
            BindingResult resultBinder, Model model) {
        newConnectionService.validatePropertyIDForDataEntry(waterConnectionDetails, resultBinder);
        newConnectionService.validateExisting(waterConnectionDetails, resultBinder);
        if (resultBinder.hasErrors()) {
            model.addAttribute(VALIDIFPTDUEEXISTS, waterTaxUtils.isNewConnectionAllowedIfPTDuePresent());
            Map<Long, String> connectionTypeMap = new HashMap<>();

            connectionTypeMap.put(applicationTypeService.findByCode(NEWCONNECTION).getId(), PRIMARYCONNECTION);
            connectionTypeMap.put(applicationTypeService.findByCode(ADDNLCONNECTION).getId(), CONN_NAME_ADDNLCONNECTION);
            model.addAttribute(RADIOBUTTONMAP, connectionTypeMap);
            model.addAttribute(RADIOBUTTONMAP, connectionTypeMap);
            model.addAttribute("usageTypes", usageTypeService.getActiveUsageTypes());
            model.addAttribute("connectionCategories", connectionCategoryService.getAllActiveConnectionCategory());
            model.addAttribute("pipeSizes", pipeSizeService.getAllActivePipeSize());
            return waterConnectionDetails.getId() == null ? "newconnection-dataEntryForm" : "newconnection-dataEntryEditForm";
        }
        waterConnectionDtlsService.createExisting(waterConnectionDetails);
        return "redirect:newConnection-existingMessage/" + waterConnectionDetails.getConnection().getConsumerCode();
    }

    @GetMapping(value = "/generate-meesevareceipt")
    public RedirectView generateMeesevaReceipt(@ModelAttribute WaterConnectionDetails waterConnectionDetails,
            HttpServletRequest request) {

        String keyNameArray = request.getParameter("transactionServiceNumber");

        RedirectView redirect = new RedirectView(WaterTaxConstants.MEESEVA_REDIRECT_URL + keyNameArray, false);
        FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
        if (outputFlashMap != null)
            outputFlashMap.put("url", request.getRequestURL());
        return redirect;
    }

    @GetMapping(value = "/application-success")
    public ModelAndView successView(@ModelAttribute WaterConnectionDetails waterConnectionDetails,
            HttpServletRequest request, Model model) {

        String[] keyNameArray = request.getParameter("pathVars").split(",");
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
            waterConnectionDetails = waterConnectionDtlsService.findByApplicationNumber(applicationNumber);
        model.addAttribute("waterTaxDueforParent", waterConnectionDetailsService.getWaterTaxDueAmount(waterConnectionDetails));
        model.addAttribute("approverName", approverName);
        model.addAttribute("currentUserDesgn", currentUserDesgn);
        model.addAttribute("nextDesign", nextDesign);
        model.addAttribute("connectionType", waterConnectionDtlsService.getConnectionTypesMap()
                .get(waterConnectionDetails.getConnectionType().name()));
        model.addAttribute("cityName", waterTaxUtils.getMunicipalityName());
        model.addAttribute("applicationDocList",
                waterConnectionDtlsService.getApplicationDocForExceptClosureAndReConnection(waterConnectionDetails));
        model.addAttribute("feeDetails", connectionDemandService.getSplitFee(waterConnectionDetails));

        model.addAttribute("mode", "ack");
        return new ModelAndView("application/application-success", "waterConnectionDetails", waterConnectionDetails);

    }

    @GetMapping(value = "/newConnection-editExisting/{consumerCode}")
    public String editExisting(@PathVariable String consumerCode, Model model) {

        WaterConnectionDetails waterConnectionDetails = waterConnectionDtlsService
                .findByApplicationNumberOrConsumerCode(consumerCode);
        model.addAttribute("allowIfPTDueExists", waterTaxUtils.isNewConnectionAllowedIfPTDuePresent());
        model.addAttribute(ADDITIONALRULE, waterConnectionDetails.getApplicationType().getCode());
        model.addAttribute(CURRENTUSER, waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
        model.addAttribute(STATETYPE, waterConnectionDetails.getClass().getSimpleName());
        Map<Long, String> connectionTypeMap = new HashMap<>();

        connectionTypeMap.put(applicationTypeService.findByCode(NEWCONNECTION).getId(), PRIMARYCONNECTION);
        connectionTypeMap.put(applicationTypeService.findByCode(ADDNLCONNECTION).getId(), CONN_NAME_ADDNLCONNECTION);
        model.addAttribute(RADIOBUTTONMAP, connectionTypeMap);
        model.addAttribute("mode", "dataEntry");
        model.addAttribute("waterConnectionDetails", waterConnectionDetails);
        model.addAttribute("usageTypes", usageTypeService.getActiveUsageTypes());
        model.addAttribute("connectionCategories", connectionCategoryService.getAllActiveConnectionCategory());
        model.addAttribute("pipeSizes", pipeSizeService.getAllActivePipeSize());
        return "newconnection-dataEntryEditForm";
    }

    @PostMapping(value = "/newConnection-editExisting/{consumerCode}")
    public String modifyExisting(@Valid @ModelAttribute WaterConnectionDetails waterConnectionDetails,
            @PathVariable String consumerCode, BindingResult resultBinder, Model model) {
        return createAndUpdateDataEntryRecord(waterConnectionDetails, resultBinder, model);
    }

    // sewerage
    private void sewerageIntegration(WaterConnectionDetails waterConnectionDetails, BindingResult resultBinder,
            SewerageApplicationDetails sewerageDetails) {
        if (sewerageDetails.getState() == null)
            sewerageDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                    SewerageTaxConstants.APPLICATION_STATUS_CSCCREATED, SewerageTaxConstants.MODULETYPE));
        SewerageApplicationType sewerageType = sewerageApplicationTypeService
                .findBy(waterConnectionDetails.getSewerageApplicationDetails().getApplicationType().getId());

        sewerageDetails.setApplicationType(sewerageType);

        SewerageApplicationDetailsDocument sewerageDocs = new SewerageApplicationDetailsDocument();
        List<SewerageApplicationDetailsDocument> sewapplicationDocs = new ArrayList<>();
        int firstDoc = 0;
        if (!waterConnectionDetails.getApplicationDocs().isEmpty()) {
            sewerageDocs.setDocumentDate(waterConnectionDetails.getApplicationDocs().get(0).getDocumentDate());
            sewerageDocs
                    .setDocumentNumber(waterConnectionDetails.getApplicationDocs().get(0).getDocumentNumber());
            sewerageDocs.setDocumentTypeMaster(documentTypeMasterService
                    .findByApplicationTypeAndDescription(sewerageDetails.getApplicationType(), "Others"));
            sewerageDocs.setFiles(waterConnectionDetails.getApplicationDocs().get(0).getFiles());
            sewerageConnectionService.validateDocuments(sewapplicationDocs, sewerageDocs, firstDoc, resultBinder);
        }
        sewerageDetails.setAppDetailsDocument(sewapplicationDocs);
        sewerageConnectionService.processAndStoreApplicationDocuments(sewerageDetails);
    }

    private void sewerageApplicationCreate(WaterConnectionDetails waterConnectionDetails, HttpServletRequest request,
            String workFlowAction, MultipartFile[] files, SewerageApplicationDetails sewerageDetails,
            Long approvalPosition, String approvalComment) {

        sewerageDetails.setApplicationDate(waterConnectionDetails.getSewerageApplicationDetails().getApplicationDate());
        sewerageDetails.setConnectionFees(waterConnectionDetails.getSewerageApplicationDetails().getConnectionFees());
        sewerageDetails.setConnectionDetail(waterConnectionDetails.getSewerageApplicationDetails().getConnectionDetail());
        sewerageDetails.getConnectionDetail()
                .setPropertyIdentifier(waterConnectionDetails.getConnection().getPropertyIdentifier());
        sewerageDetails.setConnection(waterConnectionDetails.getSewerageApplicationDetails().getConnection());
        sewerageDetails.getConnection()
                .setStatus(waterConnectionDetails.getSewerageApplicationDetails().getConnection().getStatus());
        sewerageDetails.setCreatedBy(securityUtils.getCurrentUser());
        sewerageDetails.getWorkflowContainer().setWorkFlowAction(workFlowAction);
        sewerageDetails.getWorkflowContainer().setApproverPositionId(approvalPosition);
        sewerageDetails.getWorkflowContainer().setApproverComments(approvalComment);
        sewerageDetails.getWorkflowContainer().setAdditionalRule(sewerageDetails.getApplicationType().getCode());
        // sewerage application create
        sewerageDetailsService
                .createNewSewerageConnection(sewerageDetails, files, request);
    }

}