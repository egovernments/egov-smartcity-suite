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

import static org.egov.commons.entity.Source.CSC;
import static org.egov.commons.entity.Source.MEESEVA;
import static org.egov.commons.entity.Source.ONLINE;
import static org.egov.commons.entity.Source.WARDSECRETARY;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WARDSECRETARY_EVENTPUBLISH_MODE_CREATE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WARDSECRETARY_REDIRECTION_URL;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WARDSECRETARY_SOURCE_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WARDSECRETARY_TRANSACTIONID_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WARDSECRETARY_WSPORTAL_REQUEST;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.entity.Source;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.integration.service.ThirdPartyService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pims.commons.Position;
import org.egov.wtms.application.entity.ApplicationDocuments;
import org.egov.wtms.application.entity.WaterConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.AdditionalConnectionService;
import org.egov.wtms.application.service.ConnectionDetailService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.application.service.WaterConnectionService;
import org.egov.wtms.masters.entity.DocumentNames;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/application")
public class AdditionalConnectionController extends GenericConnectionController {
    private static final String MEESEVAAPPLICATIONNUMBER = "meesevaApplicationNumber";
    private static final String APPROVALPOSITION = "approvalPosition";
    private static final String ADDCONNECTION_FORM = "addconnection-form";
    private static final String STATETYPE = "stateType";
    private static final String ADDITIONALTULE = "additionalRule";
    private static final String CURRENTUSER = "currentUser";

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;
    @Autowired
    private ApplicationTypeService applicationTypeService;
    @Autowired
    private WaterConnectionService waterConnectionService;
    @Autowired
    private AdditionalConnectionService additionalConnectionService;
    @Autowired
    private WaterTaxUtils waterTaxUtils;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private ConnectionDetailService connectionDetailService;
    @Autowired
    private ThirdPartyService thirdPartyService;

    public @ModelAttribute("documentNamesList") List<DocumentNames> documentNamesList(
            @ModelAttribute final WaterConnectionDetails addConnection) {
        addConnection.setApplicationType(applicationTypeService.findByCode(WaterTaxConstants.ADDNLCONNECTION));
        return waterConnectionDetailsService.getAllActiveDocumentNames(addConnection.getApplicationType());
    }

    @RequestMapping(value = "/addconnection/{consumerCode}")
    public String showAdditionalApplicationForm(WaterConnectionDetails parentConnectionDetails,
            @ModelAttribute final WaterConnectionDetails addConnection, final Model model,
            @PathVariable final String consumerCode, final HttpServletRequest request) {
    	
    	boolean wsPortalRequest = Boolean.valueOf(request.getParameter(WARDSECRETARY_WSPORTAL_REQUEST));
        if (!thirdPartyService.isValidWardSecretaryRequest(wsPortalRequest))
            throw new ApplicationRuntimeException("WS.001");
        
        boolean isWardSecretaryUser = thirdPartyService.isWardSecretaryRequest(wsPortalRequest);
        final String meesevaApplicationNumber = request.getParameter(MEESEVAAPPLICATIONNUMBER);
        final WaterConnection connection = waterConnectionService.findByConsumerCode(consumerCode);
        final WorkflowContainer workflowContainer = new WorkflowContainer();
		if (isWardSecretaryUser) {
			String wsTransactionId = request.getParameter(WARDSECRETARY_TRANSACTIONID_CODE);
			String wsSource = request.getParameter(WARDSECRETARY_SOURCE_CODE);
			if (ThirdPartyService.validateWardSecretaryRequest(wsTransactionId, wsSource))
				throw new ApplicationRuntimeException("WS.001");
			else {
				model.addAttribute(WARDSECRETARY_TRANSACTIONID_CODE, wsTransactionId);
				model.addAttribute(WARDSECRETARY_SOURCE_CODE, wsSource);
				model.addAttribute(WARDSECRETARY_WSPORTAL_REQUEST, wsPortalRequest);
			}
		}
        workflowContainer.setAdditionalRule(addConnection.getApplicationType().getCode());
        prepareWorkflow(model, addConnection, workflowContainer);
        parentConnectionDetails = waterConnectionDetailsService.getParentConnectionDetails(
                connection.getPropertyIdentifier(), ConnectionStatus.ACTIVE);
        loadBasicDetails(addConnection, model, parentConnectionDetails, meesevaApplicationNumber);
        return ADDCONNECTION_FORM;
    }

    private void loadBasicDetails(final WaterConnectionDetails addConnection, final Model model,
            final WaterConnectionDetails parentConnectionDetails, final String meesevaApplicationNumber) {
        Boolean loggedUserIsMeesevaUser;
        addConnection.setConnectionStatus(ConnectionStatus.INPROGRESS);
        model.addAttribute("parentConnection", parentConnectionDetails.getConnection());
        model.addAttribute("waterConnectionDetails", parentConnectionDetails);
        model.addAttribute(
                "connectionType",
                waterConnectionDetailsService.getConnectionTypesMap().get(
                        parentConnectionDetails.getConnectionType().name()));
        model.addAttribute("addConnection", addConnection);
        model.addAttribute(STATETYPE, parentConnectionDetails.getClass().getSimpleName());
        model.addAttribute(CURRENTUSER, waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
        model.addAttribute(ADDITIONALTULE, addConnection.getApplicationType().getCode());
        model.addAttribute("mode", "addconnection");
        model.addAttribute("validationMessage",
                additionalConnectionService.validateAdditionalConnection(parentConnectionDetails));
        final BigDecimal waterTaxDueforParent = waterConnectionDetailsService.getWaterTaxDueAmount(parentConnectionDetails);
        model.addAttribute("waterTaxDueforParent", waterTaxDueforParent);

        loggedUserIsMeesevaUser = waterTaxUtils.isMeesevaUser(securityUtils.getCurrentUser());
        if (loggedUserIsMeesevaUser)
            if (meesevaApplicationNumber == null)
                throw new ApplicationRuntimeException("MEESEVA.005");
            else
                addConnection.setMeesevaApplicationNumber(meesevaApplicationNumber);

        model.addAttribute("typeOfConnection", WaterTaxConstants.ADDNLCONNECTION);
        model.addAttribute("citizenPortalUser", waterTaxUtils.isCitizenPortalUser(securityUtils.getCurrentUser()));
        model.addAttribute("isAnonymousUser", waterTaxUtils.isAnonymousUser(securityUtils.getCurrentUser()));
    }

    @RequestMapping(value = "/addconnection/addConnection-create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final WaterConnectionDetails addConnection,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes, final Model model,
            @RequestParam final String workFlowAction, final HttpServletRequest request, final BindingResult errors) {
		User currentUser = securityUtils.getCurrentUser();
		final Boolean isCSCOperator = waterTaxUtils.isCSCoperator(currentUser);
		final Boolean citizenPortalUser = waterTaxUtils.isCitizenPortalUser(currentUser);
		final Boolean loggedUserIsMeesevaUser = waterTaxUtils.isMeesevaUser(currentUser);
		final Boolean isAnonymousUser = waterTaxUtils.isAnonymousUser(currentUser);
		boolean wsPortalRequest = Boolean.valueOf(request.getParameter(WARDSECRETARY_WSPORTAL_REQUEST));
        boolean isWardSecretaryUser = thirdPartyService.isWardSecretaryRequest(wsPortalRequest);
		String wsTransactionId = request.getParameter(WARDSECRETARY_TRANSACTIONID_CODE);
        String wsSource = request.getParameter(WARDSECRETARY_SOURCE_CODE);

		if (!thirdPartyService.isValidWardSecretaryRequest(wsPortalRequest)
				|| (isWardSecretaryUser && ThirdPartyService.validateWardSecretaryRequest(wsTransactionId, wsSource)))
			throw new ApplicationRuntimeException("WS.001");

        model.addAttribute("isAnonymousUser", isAnonymousUser);
        if (loggedUserIsMeesevaUser && request.getParameter(MEESEVAAPPLICATIONNUMBER) != null)
            addConnection.setMeesevaApplicationNumber(request.getParameter(MEESEVAAPPLICATIONNUMBER));
        model.addAttribute("citizenPortalUser", citizenPortalUser);
		if (!isCSCOperator && !citizenPortalUser && !loggedUserIsMeesevaUser && !isAnonymousUser
				&& !isWardSecretaryUser) {
			final Boolean isJuniorAsstOrSeniorAsst = waterTaxUtils
					.isLoggedInUserJuniorOrSeniorAssistant(currentUser.getId());
			if (!isJuniorAsstOrSeniorAsst)
				throw new ValidationException("err.creator.application");
		}

        final WaterConnectionDetails parent = waterConnectionDetailsService.getActiveConnectionDetailsByConnection(addConnection
                .getConnection().getParentConnection());
        final String message = additionalConnectionService.validateAdditionalConnection(parent);
        if (!message.isEmpty() && !"".equals(message)){
			if (isWardSecretaryUser) {
				model.addAttribute(WARDSECRETARY_REDIRECTION_URL, "/wtms/application/addconnection/"
						.concat(addConnection.getConnection().getParentConnection().getConsumerCode()));
				model.addAttribute(WARDSECRETARY_TRANSACTIONID_CODE, wsTransactionId);
				model.addAttribute(WARDSECRETARY_SOURCE_CODE, wsSource);
				model.addAttribute(WARDSECRETARY_WSPORTAL_REQUEST, wsPortalRequest);
				return "wardsecretary-redirect";
			}
			else
				return "redirect:/application/addconnection/"
						+ addConnection.getConnection().getParentConnection().getConsumerCode();
        }
        
        final List<ApplicationDocuments> applicationDocs = new ArrayList<>();
        int i = 0;
        if (!addConnection.getApplicationDocs().isEmpty())
            for (final ApplicationDocuments applicationDocument : addConnection.getApplicationDocs()) {
                if (applicationDocument.getDocumentNumber() == null && applicationDocument.getDocumentDate() != null) {
                    final String fieldError = "applicationDocs[" + i + "].documentNumber";
                    resultBinder.rejectValue(fieldError, "documentNumber.required");
                }
                if (applicationDocument.getDocumentNumber() != null && applicationDocument.getDocumentDate() == null) {
                    final String fieldError = "applicationDocs[" + i + "].documentDate";
                    resultBinder.rejectValue(fieldError, "documentDate.required");
                } else if (connectionDetailService.validApplicationDocument(applicationDocument))
                    applicationDocs.add(applicationDocument);
                i++;
            }
        if (ConnectionType.NON_METERED.equals(addConnection.getConnectionType()))
            waterConnectionDetailsService.validateWaterRateAndDonationHeader(addConnection);
        if (addConnection.getState() == null)
            addConnection.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                    WaterTaxConstants.APPLICATION_STATUS_CREATED, WaterTaxConstants.MODULETYPE));

        if (resultBinder.hasErrors()) {
            final WaterConnectionDetails parentConnectionDetails = waterConnectionDetailsService
                    .getActiveConnectionDetailsByConnection(addConnection.getConnection());
            loadBasicDetails(addConnection, model, parentConnectionDetails, addConnection.getMeesevaApplicationNumber());
            final WorkflowContainer workflowContainer = new WorkflowContainer();
            workflowContainer.setAdditionalRule(addConnection.getApplicationType().getCode());
            prepareWorkflow(model, addConnection, workflowContainer);
            model.addAttribute("approvalPosOnValidate", request.getParameter(APPROVALPOSITION));
            model.addAttribute(ADDITIONALTULE, addConnection.getApplicationType().getCode());
            model.addAttribute(STATETYPE, addConnection.getClass().getSimpleName());
            model.addAttribute(CURRENTUSER, waterTaxUtils.getCurrentUserRole(currentUser));
			if (isWardSecretaryUser) {
				model.addAttribute(WARDSECRETARY_TRANSACTIONID_CODE, wsTransactionId);
				model.addAttribute(WARDSECRETARY_SOURCE_CODE, wsSource);
				model.addAttribute(WARDSECRETARY_WSPORTAL_REQUEST, wsPortalRequest);
			}
            return ADDCONNECTION_FORM;
        }
        addConnection.setApplicationDate(new Date());
        addConnection.getApplicationDocs().clear();
        addConnection.setApplicationDocs(applicationDocs);

        processAndStoreApplicationDocuments(addConnection);

        Long approvalPosition = 0l;
        String approvalComment = "";
        String workFlowActionValue = "";

        if (request.getParameter("approvalComent") != null)
            approvalComment = request.getParameter("approvalComent");

        if (request.getParameter("workFlowAction") != null)
            workFlowActionValue = request.getParameter("workFlowAction");

        if (request.getParameter(APPROVALPOSITION) != null && !request.getParameter(APPROVALPOSITION).isEmpty())
            approvalPosition = Long.valueOf(request.getParameter(APPROVALPOSITION));

        final Boolean applicationByOthers = waterTaxUtils.getCurrentUserRole(currentUser);

		if (applicationByOthers != null && applicationByOthers.equals(true) || citizenPortalUser || isAnonymousUser
				|| isWardSecretaryUser) {
            final Position userPosition = waterTaxUtils.getZonalLevelClerkForLoggedInUser(addConnection.getConnection()
                    .getPropertyIdentifier(), isWardSecretaryUser);
            if (userPosition != null)
                approvalPosition = userPosition.getId();
            else {
                final WaterConnectionDetails parentConnectionDetails = waterConnectionDetailsService
                        .getActiveConnectionDetailsByConnection(addConnection.getConnection());
                loadBasicDetails(addConnection, model, parentConnectionDetails, null);
                final WorkflowContainer workflowContainer = new WorkflowContainer();
                workflowContainer.setAdditionalRule(addConnection.getApplicationType().getCode());
                prepareWorkflow(model, addConnection, workflowContainer);
                model.addAttribute(ADDITIONALTULE, addConnection.getApplicationType().getCode());
                model.addAttribute(STATETYPE, addConnection.getClass().getSimpleName());
                model.addAttribute(CURRENTUSER, waterTaxUtils.getCurrentUserRole(currentUser));
                errors.rejectValue("connection.propertyIdentifier", "err.validate.connection.user.mapping",
                        "err.validate.connection.user.mapping");
                model.addAttribute("noJAORSAMessage", "No JA/SA exists to forward the application.");
                if (isWardSecretaryUser) {
    				model.addAttribute(WARDSECRETARY_TRANSACTIONID_CODE, wsTransactionId);
    				model.addAttribute(WARDSECRETARY_SOURCE_CODE, wsSource);
    				model.addAttribute(WARDSECRETARY_WSPORTAL_REQUEST, wsPortalRequest);
    			}
                return ADDCONNECTION_FORM;

            }
        }

        if (isAnonymousUser)
            addConnection.setSource(ONLINE);
        else if (isCSCOperator)
            addConnection.setSource(CSC);
        else if (isWardSecretaryUser)
            addConnection.setSource(WARDSECRETARY);
        else if (citizenPortalUser && (addConnection.getSource() == null || StringUtils.isBlank(addConnection.getSource().toString())))
            addConnection.setSource(waterTaxUtils.setSourceOfConnection(currentUser));
        else if (loggedUserIsMeesevaUser) {
            addConnection.setSource(MEESEVA);
            if (addConnection.getMeesevaApplicationNumber() != null)
                addConnection.setApplicationNumber(addConnection.getMeesevaApplicationNumber());
        }
        else
            addConnection.setSource(Source.SYSTEM);

		if (isWardSecretaryUser)
			waterConnectionDetailsService.persistAndPublishEventForWardSecretary(addConnection, request, workFlowAction,
					approvalPosition, approvalComment, WARDSECRETARY_EVENTPUBLISH_MODE_CREATE);
		else
			waterConnectionDetailsService.createNewWaterConnection(addConnection, approvalPosition, approvalComment,
					addConnection.getApplicationType().getCode(), workFlowActionValue);

        if (loggedUserIsMeesevaUser)
            return "redirect:/application/generate-meesevareceipt?transactionServiceNumber="
                    + addConnection.getApplicationNumber();
        else
            return "redirect:/application/citizeenAcknowledgement?pathVars=" + addConnection.getApplicationNumber();
    }

}
