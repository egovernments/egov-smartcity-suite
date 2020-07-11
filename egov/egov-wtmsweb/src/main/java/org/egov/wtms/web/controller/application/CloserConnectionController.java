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

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.commons.entity.Source.CSC;
import static org.egov.commons.entity.Source.MEESEVA;
import static org.egov.commons.entity.Source.ONLINE;
import static org.egov.commons.entity.Source.WARDSECRETARY;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PERMENENTCLOSECODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WARDSECRETARY_EVENTPUBLISH_MODE_CREATE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WARDSECRETARY_SOURCE_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WARDSECRETARY_TRANSACTIONID_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WARDSECRETARY_WSPORTAL_REQUEST;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.integration.service.ThirdPartyService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pims.commons.Position;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.CloserConnectionService;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.masters.entity.ConnectionCategory;
import org.egov.wtms.masters.entity.PipeSize;
import org.egov.wtms.masters.entity.UsageType;
import org.egov.wtms.masters.entity.enums.ClosureType;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.service.ApplicationProcessTimeService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_STATE_CANCELLED;

@Controller
@RequestMapping(value = "/application")
public class CloserConnectionController extends GenericConnectionController {
    private static final String MEESEVA_APPLICATION_NUMBER = "meesevaApplicationNumber";
    private static final String WATERCONNECTIONDETAILS = "waterConnectionDetails";
    private static final String APPROVALPOSITION = "approvalPosition";

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
    private ApplicationProcessTimeService applicationProcessTimeService;
    
    @Autowired
    private ThirdPartyService thirdPartyService;

    @ModelAttribute
    public WaterConnectionDetails getWaterConnectionDetails(@PathVariable final String applicationCode) {
        return waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(applicationCode, ConnectionStatus.ACTIVE);
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

    @RequestMapping(value = "/close/{applicationCode}", method = RequestMethod.GET)
    public String view(final Model model, @PathVariable final String applicationCode,
            final HttpServletRequest request) {
    	return prepareClosureForm(model, applicationCode, request);
    }
    
	@RequestMapping(value = "/close/form/{applicationCode}", method = RequestMethod.POST)
	public String closureFormForWardSecretary(final Model model, @PathVariable final String applicationCode,
			final HttpServletRequest request) {
		return prepareClosureForm(model, applicationCode, request);
	}

	private String prepareClosureForm(final Model model, final String applicationCode,
			final HttpServletRequest request) {
		boolean wsPortalRequest = Boolean.valueOf(request.getParameter(WARDSECRETARY_WSPORTAL_REQUEST));
		String wsTransactionId = request.getParameter(WARDSECRETARY_TRANSACTIONID_CODE);
		String wsSource = request.getParameter(WARDSECRETARY_SOURCE_CODE);
		boolean isWardSecretaryUser = thirdPartyService.isWardSecretaryRequest(wsPortalRequest);
		boolean anonymousUser = waterTaxUtils.isAnonymousUser(securityUtils.getCurrentUser());
		boolean citizenUser = waterTaxUtils.isCitizenPortalUser(securityUtils.getCurrentUser());

		if (!thirdPartyService.isValidWardSecretaryRequest(wsPortalRequest)
				|| (isWardSecretaryUser && ThirdPartyService.validateWardSecretaryRequest(wsTransactionId, wsSource)))
			throw new ApplicationRuntimeException("WS.001");

		if (isWardSecretaryUser) {
			model.addAttribute(WARDSECRETARY_TRANSACTIONID_CODE, wsTransactionId);
			model.addAttribute(WARDSECRETARY_SOURCE_CODE, wsSource);
			model.addAttribute(WARDSECRETARY_WSPORTAL_REQUEST, wsPortalRequest);
		}
        final String meesevaApplicationNumber = request.getParameter(MEESEVA_APPLICATION_NUMBER);
        final WaterConnectionDetails waterConnectionDetails = getWaterConnectionDetails(applicationCode);
        if (waterConnectionDetails.getCloseConnectionType() != null &&
                PERMENENTCLOSECODE.equals(waterConnectionDetails.getCloseConnectionType()))
            throw new ApplicationRuntimeException("connection.closed");

		return loadViewData(model, request, waterConnectionDetails, meesevaApplicationNumber, isWardSecretaryUser,
				anonymousUser, citizenUser);
	}

	@Transactional(readOnly = true)
	public String loadViewData(final Model model, final HttpServletRequest request,
			final WaterConnectionDetails waterConnectionDetails, final String meesevaApplicationNumber,
			boolean isWardSecretaryUser, boolean anonymousUser, boolean citizenUser) {
        Boolean loggedUserIsMeesevaUser;
        
        if(applicationProcessTimeService.getApplicationProcessTime(applicationTypeService.findByCode(WaterTaxConstants.CLOSINGCONNECTION),
                waterConnectionDetails.getCategory())==null) {
            throw new ApplicationRuntimeException("err.applicationprocesstime.undefined");
        }
        
        waterConnectionDetails.setPreviousApplicationType(waterConnectionDetails.getApplicationType().getCode());
        model.addAttribute("previousApplicationType", waterConnectionDetails.getPreviousApplicationType());
        model.addAttribute("stateType", waterConnectionDetails.getClass().getSimpleName());
        model.addAttribute("applicationDocList",
                waterConnectionDetailsService.getApplicationDocForExceptClosureAndReConnection(waterConnectionDetails));
        model.addAttribute("additionalRule", WaterTaxConstants.CLOSECONNECTION);
        model.addAttribute("currentUser", waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        workflowContainer.setAdditionalRule(WaterTaxConstants.CLOSECONNECTION);
        prepareWorkflow(model, waterConnectionDetails, workflowContainer);
		if (("CSCUSER".equalsIgnoreCase(securityUtils.getCurrentUser().getUsername()) || isWardSecretaryUser
				|| anonymousUser || citizenUser) && waterConnectionDetails.getCurrentState() != null
				&& WF_STATE_CANCELLED.equalsIgnoreCase(waterConnectionDetails.getCurrentState().getValue())) {
            List<String> validActions = (List<String>) model.asMap().get("validActionList");
            if (validActions.isEmpty()) {
                validActions = Arrays.asList("Forward");
                model.addAttribute("validActionList", validActions);    
            }
        }
        model.addAttribute("radioButtonMap", Arrays.asList(ClosureType.values()));
        model.addAttribute("loggedInCSCUser", waterTaxUtils.getCurrentUserRole());
        model.addAttribute(WATERCONNECTIONDETAILS, waterConnectionDetails);
        model.addAttribute("feeDetails", connectionDemandService.getSplitFee(waterConnectionDetails));
        model.addAttribute("connectionType", waterConnectionDetailsService.getConnectionTypesMap()
                .get(waterConnectionDetails.getConnectionType().name()));
        model.addAttribute("applicationHistory", waterConnectionDetailsService.getHistory(waterConnectionDetails));
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
        model.addAttribute("typeOfConnection", WaterTaxConstants.CLOSINGCONNECTION);
        model.addAttribute(MODE, "closureConnection");
        model.addAttribute("validationMessage",
                closerConnectionService.validateChangeOfUseConnection(waterConnectionDetails));
        final BigDecimal waterTaxDueforParent = waterConnectionDetailsService.getWaterTaxDueAmount(waterConnectionDetails);
        model.addAttribute("waterTaxDueforParent", waterTaxDueforParent);
        model.addAttribute("citizenPortalUser", waterTaxUtils.isCitizenPortalUser(securityUtils.getCurrentUser()));
        model.addAttribute("isAnonymousUser", waterTaxUtils.isAnonymousUser(securityUtils.getCurrentUser()));
        loggedUserIsMeesevaUser = waterTaxUtils.isMeesevaUser(securityUtils.getCurrentUser());
        if (loggedUserIsMeesevaUser)
            if (meesevaApplicationNumber == null)
                throw new ApplicationRuntimeException("MEESEVA.005");
            else
                waterConnectionDetails.setMeesevaApplicationNumber(meesevaApplicationNumber);
        model.addAttribute("loggedUserIsMeesevaUser", loggedUserIsMeesevaUser);
        return "connection-closeForm";
    }

    @RequestMapping(value = "/close/{applicationCode}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final WaterConnectionDetails waterConnectionDetails,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes,
            final HttpServletRequest request, final Model model, final BindingResult errors,
            @RequestParam("files") final MultipartFile[] files) {

    	User currentUser = securityUtils.getCurrentUser();
        final Boolean isCSCOperator = waterTaxUtils.isCSCoperator(currentUser);
        final Boolean loggedUserIsMeesevaUser = waterTaxUtils.isMeesevaUser(currentUser);
        final Boolean isAnonymousUser = waterTaxUtils.isAnonymousUser(currentUser);
        model.addAttribute("isAnonymousUser", isAnonymousUser);
        if (loggedUserIsMeesevaUser && request.getParameter(MEESEVA_APPLICATION_NUMBER) != null)
            waterConnectionDetails.setMeesevaApplicationNumber(request.getParameter(MEESEVA_APPLICATION_NUMBER));

        final Boolean citizenPortalUser = waterTaxUtils.isCitizenPortalUser(currentUser);
        model.addAttribute("citizenPortalUser", citizenPortalUser);
        
        boolean wsPortalRequest = Boolean.valueOf(request.getParameter(WARDSECRETARY_WSPORTAL_REQUEST));
        boolean isWardSecretaryUser = thirdPartyService.isWardSecretaryRequest(wsPortalRequest);
		String wsTransactionId = request.getParameter(WARDSECRETARY_TRANSACTIONID_CODE);
        String wsSource = request.getParameter(WARDSECRETARY_SOURCE_CODE);

		if (!thirdPartyService.isValidWardSecretaryRequest(wsPortalRequest)
				|| (isWardSecretaryUser && ThirdPartyService.validateWardSecretaryRequest(wsTransactionId, wsSource)))
			throw new ApplicationRuntimeException("WS.001");
		
        if (!isCSCOperator && !citizenPortalUser && !loggedUserIsMeesevaUser && !isAnonymousUser && !isWardSecretaryUser) {
            final Boolean isJuniorAsstOrSeniorAsst = waterTaxUtils
                    .isLoggedInUserJuniorOrSeniorAssistant(currentUser.getId());
            if (!isJuniorAsstOrSeniorAsst)
                throw new ValidationException("err.creator.application");
        }
        String workFlowAction = "";

        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");

        Long approvalPosition = 0l;
        String approvalComent = "";

        if (request.getParameter("approvalComent") != null)
            approvalComent = request.getParameter("approvalComent");

        final Boolean applicationByOthers = waterTaxUtils.getCurrentUserRole();
        if (applicationByOthers != null && applicationByOthers.equals(true) || citizenPortalUser || loggedUserIsMeesevaUser
                || isAnonymousUser || isWardSecretaryUser) {
            final Position userPosition = waterTaxUtils
                    .getZonalLevelClerkForLoggedInUser(waterConnectionDetails.getConnection().getPropertyIdentifier(), isWardSecretaryUser);
            if (userPosition == null) {
                model.addAttribute("noJAORSAMessage", "No JA/SA exists to forward the application.");
                if (isWardSecretaryUser) {
    				model.addAttribute(WARDSECRETARY_TRANSACTIONID_CODE, wsTransactionId);
    				model.addAttribute(WARDSECRETARY_SOURCE_CODE, wsSource);
    				model.addAttribute(WARDSECRETARY_WSPORTAL_REQUEST, wsPortalRequest);
    			}
                return "connection-closeForm";
            } else
                approvalPosition = userPosition.getId();

        }

        waterConnectionDetails.setPreviousApplicationType(request.getParameter("previousApplicationType"));

        final Set<FileStoreMapper> fileStoreSet = addToFileStore(files);
        Iterator<FileStoreMapper> fsIterator = null;
        if (fileStoreSet != null && !fileStoreSet.isEmpty())
            fsIterator = fileStoreSet.iterator();
        if (fsIterator != null && fsIterator.hasNext())
            waterConnectionDetails.setFileStore(fsIterator.next());

        if (isNotBlank(request.getParameter(APPROVALPOSITION)))
            approvalPosition = Long.valueOf(request.getParameter(APPROVALPOSITION));
        if (request.getParameter("closeConnectionType") != null
                && request.getParameter("closeConnectionType").equals(WaterTaxConstants.PERMENENTCLOSECODE))
            waterConnectionDetails.setCloseConnectionType(ClosureType.Permanent.getName());
        else
            waterConnectionDetails.setCloseConnectionType(ClosureType.Temporary.getName());
        waterConnectionDetails.setConnectionStatus(ConnectionStatus.CLOSED);
        waterConnectionDetails
                .setApplicationType(applicationTypeService.findByCode(WaterTaxConstants.CLOSINGCONNECTION));

        if (isAnonymousUser)
            waterConnectionDetails.setSource(ONLINE);
        if(isCSCOperator)
        	waterConnectionDetails.setSource(CSC);
        if (citizenPortalUser && (waterConnectionDetails.getSource() == null
                || isBlank(waterConnectionDetails.getSource().toString())))
            waterConnectionDetails.setSource(waterTaxUtils.setSourceOfConnection(currentUser));
        if (loggedUserIsMeesevaUser) {
            waterConnectionDetails.setApplicationNumber(waterConnectionDetails.getMeesevaApplicationNumber());
            waterConnectionDetails.setSource(MEESEVA);
        } else if(isWardSecretaryUser) {
        	waterConnectionDetails.setSource(WARDSECRETARY);
        }
		if (isWardSecretaryUser)
			closerConnectionService.persistAndPublishEventForWardSecretary(waterConnectionDetails, request,
					approvalPosition, approvalComent, workFlowAction, WARDSECRETARY_EVENTPUBLISH_MODE_CREATE);
		else
			closerConnectionService.updatecloserConnection(waterConnectionDetails, approvalPosition, approvalComent,
					workFlowAction);
        model.addAttribute(WATERCONNECTIONDETAILS, waterConnectionDetails);
        model.addAttribute(MODE, "ack");
        if (loggedUserIsMeesevaUser)
            return "redirect:/application/generate-meesevareceipt?transactionServiceNumber="
                    + waterConnectionDetails.getApplicationNumber();
        else
            return "redirect:/application/citizeenAcknowledgement?pathVars=" + waterConnectionDetails.getApplicationNumber();

    }

}
