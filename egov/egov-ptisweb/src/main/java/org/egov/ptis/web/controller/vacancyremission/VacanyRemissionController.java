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
package org.egov.ptis.web.controller.vacancyremission;

import static org.egov.ptis.constants.PropertyTaxConstants.ANONYMOUS_USER;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_VALIDATION;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCE_ONLINE;
import static org.egov.ptis.constants.PropertyTaxConstants.TARGET_TAX_DUES;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_VACANCY_REMISSION;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.Installment;
import org.egov.eis.entity.Assignment;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.property.VacancyRemissionService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = { "/vacancyremission","/citizen/vacancyremission" })
public class VacanyRemissionController extends GenericWorkFlowController {

    private static final String STATE_TYPE = "stateType";
    private static final String VACANCY_REMISSION = "VACANCY_REMISSION";
    private static final String VACANCYREMISSION_FORM = "vacancyRemission-form";
    private static final String VACANCYREMISSION_SUCCESS = "vacancyRemission-success";
    private static final String ERROR_MSG = "errorMsg";
    private static final String APPLICATION_SOURCE = "applicationSource";

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    private final PropertyTaxUtil propertyTaxUtil;

    private BasicProperty basicProperty;

    private VacancyRemission vacancyRemission;

    private final VacancyRemissionService vacancyRemissionService;
    private Boolean loggedUserIsMeesevaUser = Boolean.FALSE;
   
    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private SecurityUtils securityUtils;
    
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    public VacanyRemissionController(final VacancyRemissionService vacancyRemissionService,
            final PropertyTaxUtil propertyTaxUtil) {
        this.propertyTaxUtil = propertyTaxUtil;
        this.vacancyRemissionService = vacancyRemissionService;
    }

    @ModelAttribute
    public VacancyRemission vacancyRemissionModel(@PathVariable final String assessmentNo) {
        vacancyRemission = new VacancyRemission();
        basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        if (basicProperty != null)
            vacancyRemission.setBasicProperty((BasicPropertyImpl) basicProperty);
        return vacancyRemission;
    }

    @ModelAttribute("documentsList")
    public List<DocumentType> documentsList(@ModelAttribute final VacancyRemission vacancyRemission) {
        return vacancyRemissionService.getDocuments(TransactionType.VACANCYREMISSION);
    }
    @RequestMapping(value = "/create/{assessmentNo},{mode}", method = RequestMethod.GET)
    public String newForm(final Model model, @PathVariable final String assessmentNo, @PathVariable final String mode,
            @RequestParam(required = false) final String meesevaApplicationNumber, final HttpServletRequest request,
            @RequestParam(required = false) final String applicationSource) {
        if (basicProperty != null) {
            final Property property = basicProperty.getActiveProperty();
            List<DocumentType> documentTypes;
            User loggedInUser = securityUtils.getCurrentUser();
            documentTypes = propertyService.getDocumentTypesForTransactionType(TransactionType.VACANCYREMISSION);
            if (!ANONYMOUS_USER.equalsIgnoreCase(loggedInUser.getName())
                    && propertyService.isEmployee(loggedInUser)
                    && !propertyTaxCommonUtils.isEligibleInitiator(loggedInUser.getId())) {
                model.addAttribute(ERROR_MSG, "msg.initiator.noteligible");
                return PROPERTY_VALIDATION;
            }
            if (basicProperty.getActiveProperty().getPropertyDetail().isStructure()) {
                model.addAttribute(ERROR_MSG, "error.superstruc.prop.notallowed");
                return PROPERTY_VALIDATION;
            }
            if (vacancyRemissionService.isUnderWtmsWF(basicProperty.getUpicNo(), request)) {
                model.addAttribute(ERROR_MSG, "msg.under.wtms.wf.vr");
                return PROPERTY_VALIDATION;
            }
            if (property != null)
                // When called from common search
                if ("commonSearch".equalsIgnoreCase(mode)) {
                    Boolean enableVacancyRemission = Boolean.FALSE;
                    if (property.getPropertyDetail().getPropertyTypeMaster().getCode()
                            .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
                        model.addAttribute(ERROR_MSG, "msg.vlt.error");
                        return PROPERTY_VALIDATION;
                    } else if (property.getIsExemptedFromTax()) {
                        model.addAttribute(ERROR_MSG, "msg.property.exempted");
                        return PROPERTY_VALIDATION;
                    } else if (basicProperty.isUnderWorkflow()) {
                        model.addAttribute(ERROR_MSG,"msg.under.workflow");
                        return PROPERTY_VALIDATION;
                    } else {
                        final List<VacancyRemission> remissionList = vacancyRemissionService
                                .getAllVacancyRemissionByUpicNo(basicProperty.getUpicNo());
                        if (!remissionList.isEmpty()) {
                            final VacancyRemission vacancyRemission = remissionList.get(remissionList.size() - 1);
                            if (vacancyRemission != null)
                                if (vacancyRemission.getStatus().equalsIgnoreCase(
                                        PropertyTaxConstants.VR_STATUS_APPROVED)) {
                                    if (org.apache.commons.lang.time.DateUtils.isSameDay(
                                            vacancyRemission.getVacancyToDate(), new Date()))
                                        enableVacancyRemission = true;
                                    else if (vacancyRemission.getVacancyToDate().compareTo(new Date()) < 0)
                                        enableVacancyRemission = true;
                                } else if (vacancyRemission.getStatus().equalsIgnoreCase(
                                        PropertyTaxConstants.VR_STATUS_REJECTION_ACK_GENERATED))
                                    enableVacancyRemission = true;
                                else if (vacancyRemission.getStatus().equalsIgnoreCase(
                                        PropertyTaxConstants.VR_STATUS_WORKFLOW)) {
                                    model.addAttribute(ERROR_MSG, "msg.under.workflow");
                                    return PROPERTY_VALIDATION;
                                }
                        }
                        if (remissionList.isEmpty() || enableVacancyRemission) {
                            final Map<String, BigDecimal> propertyTaxDetails = propertyService
                                    .getCurrentPropertyTaxDetails(basicProperty.getActiveProperty());
                            BigDecimal currentPropertyTax;
                            BigDecimal currentPropertyTaxDue;
                            BigDecimal arrearPropertyTaxDue;
                            Map<String, Installment> installmentMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
                            Installment installmentFirstHalf = installmentMap.get(PropertyTaxConstants.CURRENTYEAR_FIRST_HALF);
                            if(DateUtils.between(new Date(), installmentFirstHalf.getFromDate(), installmentFirstHalf.getToDate())){
                            currentPropertyTax = propertyTaxDetails.get(CURR_FIRSTHALF_DMD_STR);
                            currentPropertyTaxDue = propertyTaxDetails.get(CURR_FIRSTHALF_DMD_STR).subtract(
                                    propertyTaxDetails.get(CURR_FIRSTHALF_COLL_STR));
                            } else {
                            	currentPropertyTax = propertyTaxDetails.get(CURR_SECONDHALF_DMD_STR);
                                currentPropertyTaxDue = propertyTaxDetails.get(CURR_SECONDHALF_DMD_STR).subtract(
                                        propertyTaxDetails.get(CURR_SECONDHALF_COLL_STR));
                            }
                            arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR).subtract(
                                    propertyTaxDetails.get(ARR_COLL_STR));
                            final BigDecimal currentWaterTaxDue = vacancyRemissionService.getWaterTaxDues(
                                    basicProperty.getUpicNo(), request);
                            
                            model.addAttribute("assessementNo", basicProperty.getUpicNo());
                            model.addAttribute("ownerName", basicProperty.getFullOwnerName());
                            model.addAttribute("doorNo", basicProperty.getAddress().getHouseNoBldgApt());
                            model.addAttribute("currentPropertyTax", currentPropertyTax);
                            model.addAttribute("currentPropertyTaxDue", currentPropertyTaxDue);
                            model.addAttribute("arrearPropertyTaxDue", arrearPropertyTaxDue);
                            model.addAttribute("currentWaterTaxDue", currentWaterTaxDue);
                            if (currentWaterTaxDue.add(currentPropertyTaxDue).add(arrearPropertyTaxDue).longValue() > 0) {
                                model.addAttribute("taxDuesErrorMsg",
                                        "Please clear property tax due for availing vacancy remission for your property ");
                                return TARGET_TAX_DUES;
                            }

                            prepareWorkflow(model, vacancyRemission, new WorkflowContainer());
                            model.addAttribute(STATE_TYPE, vacancyRemission.getClass().getSimpleName());
                            model.addAttribute("documentTypes", documentTypes);
                            model.addAttribute(APPLICATION_SOURCE, applicationSource);
                            vacancyRemissionService.addModelAttributes(model, basicProperty);
                        }
                        loggedUserIsMeesevaUser = propertyService.isMeesevaUser(vacancyRemissionService
                                .getLoggedInUser());
                        if (loggedUserIsMeesevaUser) {
                            if (meesevaApplicationNumber == null)
                                throw new ApplicationRuntimeException("MEESEVA.005");
                            else
                                vacancyRemission.setMeesevaApplicationNumber(meesevaApplicationNumber);
                        }
                    }
                } else {
                	boolean hasChildPropertyUnderWorkflow = propertyTaxUtil.checkForParentUsedInBifurcation(basicProperty.getUpicNo());
                    if(hasChildPropertyUnderWorkflow){
                    	model.addAttribute(ERROR_MSG, "Cannot proceed as this property is used in Bifurcation, which is under workflow");
                        return PROPERTY_VALIDATION;
                    }
                    final Map<String, BigDecimal> propertyTaxDetails = propertyService
                            .getCurrentPropertyTaxDetails(basicProperty.getActiveProperty());
                    BigDecimal currentPropertyTax;
                    BigDecimal currentPropertyTaxDue;
                    BigDecimal arrearPropertyTaxDue;
                    Map<String, Installment> installmentMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
                    Installment installmentFirstHalf = installmentMap.get(PropertyTaxConstants.CURRENTYEAR_FIRST_HALF);
                    if(DateUtils.between(new Date(), installmentFirstHalf.getFromDate(), installmentFirstHalf.getToDate())){
                    currentPropertyTax = propertyTaxDetails.get(CURR_FIRSTHALF_DMD_STR);
                    currentPropertyTaxDue = propertyTaxDetails.get(CURR_FIRSTHALF_DMD_STR).subtract(
                            propertyTaxDetails.get(CURR_FIRSTHALF_COLL_STR));
                    } else {
                    	currentPropertyTax = propertyTaxDetails.get(CURR_SECONDHALF_DMD_STR);
                        currentPropertyTaxDue = propertyTaxDetails.get(CURR_SECONDHALF_DMD_STR).subtract(
                                propertyTaxDetails.get(CURR_SECONDHALF_COLL_STR));
                    }
                    arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR).subtract(
                            propertyTaxDetails.get(ARR_COLL_STR));
                    final BigDecimal currentWaterTaxDue = vacancyRemissionService.getWaterTaxDues(basicProperty.getUpicNo(),
                            request);
                    model.addAttribute("currentPropertyTax", currentPropertyTax);
                    model.addAttribute("currentPropertyTaxDue", currentPropertyTaxDue);
                    model.addAttribute("arrearPropertyTaxDue", arrearPropertyTaxDue);
                    model.addAttribute("currentWaterTaxDue", currentWaterTaxDue);
                    if (currentWaterTaxDue.add(currentPropertyTaxDue).add(arrearPropertyTaxDue).longValue() > 0) {
                        model.addAttribute("taxDuesErrorMsg",
                                "Please clear property tax due for availing vacancy remission for your property ");
                        return TARGET_TAX_DUES;
                    }
                    model.addAttribute("documentTypes", documentTypes);
                    prepareWorkflow(model, vacancyRemission, new WorkflowContainer());
                    model.addAttribute(STATE_TYPE, vacancyRemission.getClass().getSimpleName());
                    vacancyRemissionService.addModelAttributes(model, basicProperty);
                }
        }
        return VACANCYREMISSION_FORM;
    }

    @RequestMapping(value = "/create/{assessmentNo},{mode}", method = RequestMethod.POST)
    public String saveVacancyRemission(@Valid @ModelAttribute final VacancyRemission vacancyRemission,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes, final Model model,
            final HttpServletRequest request, @RequestParam String workFlowAction) {

        final Boolean propertyByEmployee = Boolean.valueOf(request.getParameter("propertyByEmployee"));
        List<Document> documents = new ArrayList<>();
        loggedUserIsMeesevaUser = propertyService.isMeesevaUser(vacancyRemissionService.getLoggedInUser());
        validateDates(vacancyRemission, resultBinder, request);
        vacancyRemissionSource(vacancyRemission, request);
        final Assignment assignment = propertyService.isCscOperator(vacancyRemissionService.getLoggedInUser())
                ? propertyService.getAssignmentByDeptDesigElecWard(basicProperty)
                : null;
        if (resultBinder.hasErrors()) {
            if (basicProperty != null) {
                prepareWorkflow(model, vacancyRemission, new WorkflowContainer());
                model.addAttribute(STATE_TYPE, vacancyRemission.getClass().getSimpleName());
                vacancyRemissionService.addModelAttributes(model, basicProperty);
            }
            return VACANCYREMISSION_FORM;
        } else if ((!propertyByEmployee || loggedUserIsMeesevaUser) && assignment == null
                && propertyService.getUserPositionByZone(basicProperty, false) == null) {
            prepareWorkflow(model, vacancyRemission, new WorkflowContainer());
            model.addAttribute(STATE_TYPE, vacancyRemission.getClass().getSimpleName());
            model.addAttribute(ERROR_MSG, "No Senior or Junior assistants exists,Please check");
            vacancyRemissionService.addModelAttributes(model, basicProperty);
            return VACANCYREMISSION_FORM;
        } else {
            Long approvalPosition = 0l;
            String approvalComent = "";

            if (request.getParameter("approvalComent") != null)
                approvalComent = request.getParameter("approvalComent");
            if (request.getParameter("workFlowAction") != null)
                workFlowAction = request.getParameter("workFlowAction");
            if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
                approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
            
            if(!vacancyRemission.getDocuments().isEmpty()){
                documents.addAll(vacancyRemission.getDocuments());
                vacancyRemission.getDocuments().clear();
                vacancyRemission.getDocuments().addAll(documents);
                processAndStoreApplicationDocuments(vacancyRemission);
            }

            if (loggedUserIsMeesevaUser) {
                final HashMap<String, String> meesevaParams = new HashMap<>();
                meesevaParams.put("APPLICATIONNUMBER", vacancyRemission.getMeesevaApplicationNumber());

                if (StringUtils.isBlank(vacancyRemission.getApplicationNumber())){
                    vacancyRemission.setApplicationNumber(vacancyRemission.getMeesevaApplicationNumber());
                }
                vacancyRemissionService.saveVacancyRemission(vacancyRemission, approvalPosition, approvalComent, "",
                        workFlowAction, propertyByEmployee, meesevaParams);
            } else
                vacancyRemissionService.saveVacancyRemission(vacancyRemission, approvalPosition, approvalComent, null,
                        workFlowAction, propertyByEmployee);

            final String successMsg = "Vacancy Remission Saved Successfully in the System and forwarded to : "
                    + propertyTaxUtil.getApproverUserName(vacancyRemission.getState().getOwnerPosition().getId())
                    + " with application number : " + vacancyRemission.getApplicationNumber();
            model.addAttribute("successMessage", successMsg);
        }
        model.addAttribute("showAckBtn", Boolean.TRUE);
        model.addAttribute("isOnlineApplication", ANONYMOUS_USER.equalsIgnoreCase(vacancyRemissionService.getLoggedInUser().getName()));
        model.addAttribute("propertyId", basicProperty.getUpicNo());
        if (loggedUserIsMeesevaUser)
            return "redirect:/vacancyremission/generate-meesevareceipt/"
                    + vacancyRemission.getBasicProperty().getUpicNo() + "?transactionServiceNumber="
                    + vacancyRemission.getApplicationNumber();
        else
            return VACANCYREMISSION_SUCCESS;
    }
    
    private void vacancyRemissionSource(final VacancyRemission vacancyRemission, final HttpServletRequest request) {
        User loggedInUser = securityUtils.getCurrentUser();
        if (StringUtils.isNotBlank(request.getParameter(APPLICATION_SOURCE))
                && SOURCE_ONLINE.equalsIgnoreCase(request.getParameter(APPLICATION_SOURCE))
                && ANONYMOUS_USER.equalsIgnoreCase(loggedInUser.getName())) {
            vacancyRemission.setSource(propertyTaxCommonUtils.setSourceOfProperty(loggedInUser, Boolean.TRUE));
        } else {
            vacancyRemission.setSource(propertyTaxCommonUtils.setSourceOfProperty(loggedInUser, Boolean.FALSE));
        }
    }

    @RequestMapping(value = "/generate-meesevareceipt/{assessmentNo}", method = RequestMethod.GET)
    public RedirectView generateMeesevaReceipt(final HttpServletRequest request, final Model model) {
        final String keyNameArray = request.getParameter("transactionServiceNumber");

        final RedirectView redirect = new RedirectView(PropertyTaxConstants.MEESEVA_REDIRECT_URL + keyNameArray, false);
        final FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
        if (outputFlashMap != null)
            outputFlashMap.put("url", request.getRequestURL());
        return redirect;
    }

    private void validateDates(final VacancyRemission vacancyRemission, final BindingResult errors,
            final HttpServletRequest request) {

        final int noOfMonths = DateUtils.noOfMonths(vacancyRemission.getVacancyFromDate(),
                vacancyRemission.getVacancyToDate());
        if (noOfMonths < 6)
            errors.rejectValue("vacancyToDate", "vacancyToDate.incorrect");
    }
    
    protected void processAndStoreApplicationDocuments(final VacancyRemission vacancyRemission) {
        if (!vacancyRemission.getDocuments().isEmpty())
            for (final Document applicationDocument : vacancyRemission.getDocuments()) {
                if(applicationDocument.getFile() != null) {
                    applicationDocument.setType(vacancyRemissionService.getDocType(applicationDocument.getType().getName()));
                    applicationDocument.setFiles(propertyService.addToFileStore(applicationDocument.getFile()));
                }
            }
    }
    
    @ResponseBody
    @RequestMapping(value = "/printAck/{assessmentNo}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> printAck(final HttpServletRequest request, final Model model,
            @PathVariable("assessmentNo") final String assessmentNo) {
        ReportOutput reportOutput = propertyTaxUtil.generateCitizenCharterAcknowledgement(assessmentNo , VACANCY_REMISSION , NATURE_VACANCY_REMISSION);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=CitizenCharterAcknowledgement.pdf");
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

}
