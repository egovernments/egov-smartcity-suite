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
package org.egov.ptis.web.controller.transactions.exemption;

import org.apache.commons.lang3.StringUtils;
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
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.TaxExemptionReason;
import org.egov.ptis.domain.service.exemption.TaxExemptionService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.egov.ptis.constants.PropertyTaxConstants.*;

/**
 * @author subhash
 */
@Controller
@RequestMapping(value = { "/exemption" })
public class TaxExemptionController extends GenericWorkFlowController {

    private static final String APPROVAL_POSITION = "approvalPosition";
    private static final String APPLICATION_SOURCE = "applicationSource";
    private static final String TAX_EXEMPTION = "TAX_EXEMPTION";
    protected static final String TAX_EXEMPTION_FORM = "taxExemption-form";
    protected static final String TAX_EXEMPTION_SUCCESS = "taxExemption-success";
    private static final String ERROR_MSG = "errorMsg";
    private static final String CHOULTRY_DOC = "choultryDocs";
    private static final String EDUINST_DOC = "eduinstDocs";
    private static final String NGO_DOC = "ngoDocs";
    private static final String WORSHIP_DOC = "worshipDocs";
    private static final String EXSERVICE_DOC = "exserviceDocs";
    private boolean citizenPortalUser;
    
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private PtDemandDao ptDemandDAO;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private TaxExemptionService taxExemptionService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @ModelAttribute
    public PropertyImpl property(@PathVariable("assessmentNo") String assessmentNo) {
        Optional<BasicProperty> basicProperty = Optional.ofNullable(basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo));
        if (basicProperty.isPresent())
            return (PropertyImpl) basicProperty.get().getActiveProperty().createPropertyclone();
        return null;
    }

    @SuppressWarnings("unchecked")
    @ModelAttribute("taxExemptionReasons")
    public List<TaxExemptionReason> getTaxExemptionReasons() {
        return taxExemptionService.getSession().createQuery("from TaxExemptionReason where isActive = true order by name").list();
    }
    
    @ModelAttribute("documentsEduInst")
    public List<DocumentType> documentsEduInst(TransactionType transactionType) {
        return taxExemptionService.getDocuments(TransactionType.TE_EDU_INST);
    }

    @ModelAttribute("documentsWorship")
    public List<DocumentType> documentsWorship(TransactionType transactionType) {
        return taxExemptionService.getDocuments(TransactionType.TE_PUBLIC_WORSHIP);
    }

    @ModelAttribute("documentsNGO")
    public List<DocumentType> documentsNGO(TransactionType transactionType) {
        return taxExemptionService.getDocuments(TransactionType.TE_PENSIONER_NGO);
    }

    @ModelAttribute("documentsExService")
    public List<DocumentType> documentsExService(TransactionType transactionType) {
        return taxExemptionService.getDocuments(TransactionType.TE_EXSERVICE);
    }

    @ModelAttribute("documentsChoultries")
    public List<DocumentType> documentsChoultries(TransactionType transactionType) {
        return taxExemptionService.getDocuments(TransactionType.TE_CHOULTRY);
    }

    @RequestMapping(value = "/form/{assessmentNo}", method = RequestMethod.GET)
    public String exemptionForm(@ModelAttribute PropertyImpl property, final HttpServletRequest request, final Model model,
            @RequestParam(required = false) final String meesevaApplicationNumber,
            @RequestParam(required = false) final String applicationSource,
            @PathVariable("assessmentNo") final String assessmentNo) {
        BasicProperty basicProperty = property.getBasicProperty();
        boolean isExempted = basicProperty.getActiveProperty().getIsExemptedFromTax();
        User loggedInUser = securityUtils.getCurrentUser();
        citizenPortalUser = propertyService.isCitizenPortalUser(loggedInUser);
        boolean loggedUserIsMeesevaUser = propertyService.isMeesevaUser(loggedInUser);
        if (!ANONYMOUS_USER.equalsIgnoreCase(loggedInUser.getName()) && propertyService.isEmployee(loggedInUser)
                && !propertyTaxCommonUtils.isEligibleInitiator(loggedInUser.getId())
                && !citizenPortalUser) {
            model.addAttribute(ERROR_MSG, "msg.initiator.noteligible");
            return PROPERTY_VALIDATION;
        }
        if (basicProperty != null) {
            property = (PropertyImpl) basicProperty.getProperty();
            final Ptdemand ptDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
            if (ptDemand == null || ptDemand != null && ptDemand.getEgDemandDetails() == null) {
                model.addAttribute(ERROR_MSG, "There is no tax for this property");
                return PROPERTY_VALIDATION;
            }

            else if (basicProperty.isUnderWorkflow()) {
                model.addAttribute("wfPendingMsg", "Could not do Tax exemption now, property is undergoing some work flow.");
                return TARGET_WORKFLOW_ERROR;
            }
            if (basicProperty.getActiveProperty().getPropertyDetail().isStructure()) {
                model.addAttribute(ERROR_MSG, "error.superstruc.prop.notallowed");
                return PROPERTY_VALIDATION;
            }
            if (taxExemptionService.isUnderWtmsWF(basicProperty.getUpicNo(), request)) {
                model.addAttribute(ERROR_MSG, "msg.under.wtms.wf.taxexemption");
                return PROPERTY_VALIDATION;
            }
            else if (!isExempted) {
                final Map<String, BigDecimal> propertyTaxDetails = propertyService
                        .getCurrentPropertyTaxDetails(basicProperty.getActiveProperty());
                BigDecimal currentPropertyTax;
                BigDecimal currentPropertyTaxDue;
                BigDecimal arrearPropertyTaxDue;
                final Map<String, Installment> installmentMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
                final Installment installmentFirstHalf = installmentMap.get(PropertyTaxConstants.CURRENTYEAR_FIRST_HALF);
                if (DateUtils.between(new Date(), installmentFirstHalf.getFromDate(), installmentFirstHalf.getToDate())) {
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
                final BigDecimal currentWaterTaxDue = taxExemptionService.getWaterTaxDues(basicProperty.getUpicNo(), request);
                model.addAttribute("assessementNo", basicProperty.getUpicNo());
                model.addAttribute("ownerName", basicProperty.getFullOwnerName());
                model.addAttribute("doorNo", basicProperty.getAddress().getHouseNoBldgApt());
                model.addAttribute("currentPropertyTax", currentPropertyTax);
                model.addAttribute("currentPropertyTaxDue", currentPropertyTaxDue);
                model.addAttribute("arrearPropertyTaxDue", arrearPropertyTaxDue);
                model.addAttribute("currentWaterTaxDue", currentWaterTaxDue);
                if (currentWaterTaxDue.add(currentPropertyTaxDue).add(arrearPropertyTaxDue).longValue() > 0) {
                    model.addAttribute("taxDuesErrorMsg", "Above tax dues must be payed before initiating "
                            + APPLICATION_TYPE_TAX_EXEMTION);
                    return TARGET_TAX_DUES;
                }
                final boolean hasChildPropertyUnderWorkflow = propertyTaxUtil
                        .checkForParentUsedInBifurcation(basicProperty.getUpicNo());
                if (hasChildPropertyUnderWorkflow) {
                    model.addAttribute(ERROR_MSG,
                            "Cannot proceed as this property is used in Bifurcation, which is under workflow");
                    return PROPERTY_VALIDATION;
                }
            }
        }
        if (loggedUserIsMeesevaUser)
            if (meesevaApplicationNumber == null)
                throw new ApplicationRuntimeException("MEESEVA.005");
            else
                property.setMeesevaApplicationNumber(meesevaApplicationNumber);

        model.addAttribute("property", property);
        model.addAttribute("stateType", property.getClass().getSimpleName());
        model.addAttribute("additionalRule", EXEMPTION);
        model.addAttribute("isExempted", isExempted);
        model.addAttribute(APPLICATION_SOURCE, applicationSource);
        model.addAttribute("isAlert", true);
        model.addAttribute(CHOULTRY_DOC, "");
        model.addAttribute(WORSHIP_DOC, "");
        model.addAttribute(EDUINST_DOC, "");
        model.addAttribute(EXSERVICE_DOC, "");
        model.addAttribute(NGO_DOC, "");
        model.addAttribute("citizenPortalUser", citizenPortalUser);
        model.addAttribute("endorsementNotices", null);
        taxExemptionService.addModelAttributes(model, basicProperty);
        prepareWorkflow(model, property, new WorkflowContainer());
        return TAX_EXEMPTION_FORM;
    }

    @Transactional
    @RequestMapping(value = "/form/{assessmentNo}", method = RequestMethod.POST)
    public String exemptionFormSubmit(@ModelAttribute final PropertyImpl property, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model, final HttpServletRequest request,
            @RequestParam String workFlowAction) {
        final Character status = STATUS_WORKFLOW;
        Long approvalPosition = 0l;
        String approvalComent = "";
        String taxExemptedReason = "";
        final Boolean propertyByEmployee = Boolean.valueOf(request.getParameter("propertyByEmployee"));
        String target;
        User loggedInUser = securityUtils.getCurrentUser();
        boolean loggedUserIsMeesevaUser = propertyService.isMeesevaUser(loggedInUser);
        PropertyImpl oldProperty = property.getBasicProperty().getActiveProperty();
        final Assignment assignment = propertyService.isCscOperator(loggedInUser)
                ? propertyService.getAssignmentByDeptDesigElecWard(property.getBasicProperty())
                : null;
        if ((!propertyByEmployee || loggedUserIsMeesevaUser) && assignment == null
                && propertyService.getUserPositionByZone(property.getBasicProperty(), false) == null) {
            model.addAttribute(ERROR_MSG, "No Senior or Junior assistants exists,Please check");
            model.addAttribute("stateType", property.getClass().getSimpleName());
            taxExemptionService.addModelAttributes(model, property.getBasicProperty());
            prepareWorkflow(model, property, new WorkflowContainer());
            target = TAX_EXEMPTION_FORM;
        } else {
            if (StringUtils.isNotBlank(request.getParameter(APPLICATION_SOURCE))
                    && SOURCE_ONLINE.equalsIgnoreCase(request.getParameter(APPLICATION_SOURCE))
                    && ANONYMOUS_USER.equalsIgnoreCase(loggedInUser.getName())) {
                property.setSource(propertyTaxCommonUtils.setSourceOfProperty(loggedInUser, Boolean.TRUE));
            } else {
                property.setSource(propertyTaxCommonUtils.setSourceOfProperty(loggedInUser, Boolean.FALSE));
            }
            if (request.getParameter("taxExemptedReason") != null)
                taxExemptedReason = request.getParameter("taxExemptedReason");
            if (request.getParameter("approvalComent") != null)
                approvalComent = request.getParameter("approvalComent");
            if (request.getParameter("workFlowAction") != null)
                workFlowAction = request.getParameter("workFlowAction");
            if (request.getParameter(APPROVAL_POSITION) != null && !request.getParameter(APPROVAL_POSITION).isEmpty())
                approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));
            if (property.getTaxExemptedReason() != null && checkCommercialProperty((PropertyImpl)property)) {
                model.addAttribute(ERROR_MSG, "error.commercial.prop.notallowed");
                return PROPERTY_VALIDATION;
            }
            if (property.getTaxExemptedReason() != null && hasTenant((PropertyImpl)property)) {
                model.addAttribute(ERROR_MSG, "error.tenant.exists");
                return PROPERTY_VALIDATION;
            }
            if (StringUtils.isNotBlank(taxExemptedReason))
                taxExemptionService.processAndStoreApplicationDocuments((PropertyImpl) property, taxExemptedReason, null);
            if (loggedUserIsMeesevaUser) {
                final HashMap<String, String> meesevaParams = new HashMap<>();
                meesevaParams.put("APPLICATIONNUMBER", (property.getMeesevaApplicationNumber()));

                if (StringUtils.isBlank(property.getApplicationNo())) {
                    property.setApplicationNo(property.getMeesevaApplicationNumber());
                    property.setSource(PropertyTaxConstants.SOURCE_MEESEVA);
                }
                taxExemptionService.saveProperty(property, oldProperty, status, approvalComent, workFlowAction,
                        approvalPosition, taxExemptedReason, propertyByEmployee, EXEMPTION, meesevaParams);
            } else
                taxExemptionService.saveProperty(property, oldProperty, status, approvalComent, workFlowAction,
                        approvalPosition, taxExemptedReason, propertyByEmployee, EXEMPTION);
            model.addAttribute("showAckBtn", Boolean.TRUE);
            model.addAttribute("isOnlineApplication", ANONYMOUS_USER.equalsIgnoreCase(loggedInUser.getName()));
            model.addAttribute("propertyId", property.getBasicProperty().getUpicNo());
            model.addAttribute(
                    "successMessage",
                    "Property exemption data saved successfully in the system and forwarded to "
                            + propertyTaxUtil.getApproverUserName(property.getState()
                                    .getOwnerPosition().getId())
                            + " with application number "
                            + property.getApplicationNo());
            if (loggedUserIsMeesevaUser)
                target = "redirect:/exemption/generate-meesevareceipt/"
                        + property.getBasicProperty().getUpicNo() + "?transactionServiceNumber="
                        + property.getApplicationNo();
            else

                target = TAX_EXEMPTION_SUCCESS;
        }
        return target;
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

    @ResponseBody
    @RequestMapping(value = "/printAck/{assessmentNo}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> printAck(final HttpServletRequest request, final Model model,
            @PathVariable("assessmentNo") final String assessmentNo) {
        final ReportOutput reportOutput = propertyTaxUtil.generateCitizenCharterAcknowledgement(assessmentNo, TAX_EXEMPTION,
                WFLOW_ACTION_NAME_EXEMPTION, null);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=CitizenCharterAcknowledgement.pdf");
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }
    
    public Boolean checkCommercialProperty(PropertyImpl property) {
        return !checkPrivateResdProperty(property) && (property.getTaxExemptedReason().getCode()
                .equals(PropertyTaxConstants.EXEMPTION_EXSERVICE)
                || property.getTaxExemptedReason().getCode().equals(PropertyTaxConstants.EXEMPTION_PUBLIC_WORSHIP));
    }

    public Boolean checkPrivateResdProperty(PropertyImpl property) {
        return property.getPropertyDetail().getCategoryType().equals(PropertyTaxConstants.CATEGORY_RESIDENTIAL)
                && property.getPropertyDetail().getPropertyTypeMaster().getCode()
                        .equals(PropertyTaxConstants.OWNERSHIP_TYPE_PRIVATE);
    }
    
    public Boolean hasTenant(PropertyImpl property){
        for(Floor floor : property.getPropertyDetail().getFloorDetails()){
            if(floor.getPropertyOccupation().getOccupancyCode().equals(PropertyTaxConstants.OCC_TENANT)
                    && property.getTaxExemptedReason().getCode()
                    .equals(PropertyTaxConstants.EXEMPTION_EXSERVICE))
                    return true;
        }
        return false;
    }

    public boolean isCitizenPortalUser() {
        return citizenPortalUser;
    }

    public void setCitizenPortalUser(boolean citizenPortalUser) {
        this.citizenPortalUser = citizenPortalUser;
    }
}
