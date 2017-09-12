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
package org.egov.ptis.web.controller.demolition;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.Installment;
import org.egov.eis.entity.Assignment;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.service.demolition.PropertyDemolitionService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.egov.ptis.constants.PropertyTaxConstants.*;

@Controller
@RequestMapping(value = {"/property/demolition"})
public class PropertyDemolitionController extends GenericWorkFlowController {

    protected static final String COMMON_FORM = "commonForm";
    protected static final String DEMOLITION_FORM = "demolition-form";
    protected static final String DEMOLITION_SUCCESS = "demolition-success";
    private static final String ERROR_MSG = "errorMsg";

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private PtDemandDao ptDemandDAO;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private PropertyDemolitionService propertyDemolitionService;

    @Autowired
    private PropertyService propService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    
    @Autowired
    private PropertyService propertyService;

    @ModelAttribute
    public PropertyImpl property(@PathVariable("assessmentNo") String assessmentNo) {
        Optional<BasicProperty> basicProperty = Optional.ofNullable(basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo));
        if (basicProperty.isPresent())
            return (PropertyImpl) basicProperty.get().getActiveProperty().createPropertyclone();
        return null;
    }
    
    @RequestMapping(value = "/{assessmentNo}/{applicationSource}", method = RequestMethod.GET)
    public String newForm(@ModelAttribute PropertyImpl property, final Model model, @RequestParam(required = false) final String meesevaApplicationNumber ,@PathVariable("applicationSource") String applicationSource) {
        BasicProperty basicProperty = property.getBasicProperty();
        if (basicProperty.isUnderWorkflow()) {
            model.addAttribute("wfPendingMsg", "Could not do " + APPLICATION_TYPE_DEMOLITION
                    + " now, property is undergoing some work flow.");
            return TARGET_WORKFLOW_ERROR;
        }
        boolean hasChildPropertyUnderWorkflow = propertyTaxUtil.checkForParentUsedInBifurcation(basicProperty.getUpicNo());
        User loggedInUser = securityUtils.getCurrentUser();
        if (hasChildPropertyUnderWorkflow) {
            model.addAttribute(ERROR_MSG, "Cannot proceed as this property is used in Bifurcation, which is under workflow");
            return PROPERTY_VALIDATION;
        }
        if (!ANONYMOUS_USER.equalsIgnoreCase(loggedInUser.getName())
                && propService.isEmployee(loggedInUser) && !propertyTaxCommonUtils.isEligibleInitiator(loggedInUser.getId())) {
            model.addAttribute(ERROR_MSG, "msg.initiator.noteligible");
            return PROPERTY_VALIDATION;
        }
        if (basicProperty.getActiveProperty().getPropertyDetail().isStructure()) {
            model.addAttribute(ERROR_MSG, "error.superstruc.prop.notallowed");
            return PROPERTY_VALIDATION;
        }
        if (propertyService.isMeesevaUser(loggedInUser))
            if (meesevaApplicationNumber == null)
                throw new ApplicationRuntimeException("MEESEVA.005");
            else
                property.setMeesevaApplicationNumber(meesevaApplicationNumber);
        model.addAttribute("property", property);
        final Map<String, BigDecimal> propertyTaxDetails = ptDemandDAO.getDemandCollMap(property
                .getBasicProperty().getActiveProperty());
        Map<String, Installment> installmentMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        Installment installmentFirstHalf = installmentMap.get(PropertyTaxConstants.CURRENTYEAR_FIRST_HALF);
        BigDecimal currentPropertyTax;
        BigDecimal currentPropertyTaxDue;
        BigDecimal arrearPropertyTaxDue;
        BigDecimal currentFirstHalfTaxDue;
        if (DateUtils.between(new Date(), installmentFirstHalf.getFromDate(), installmentFirstHalf.getToDate())) {
            currentPropertyTax = propertyTaxDetails.get(CURR_FIRSTHALF_DMD_STR);
            currentPropertyTaxDue = propertyTaxDetails.get(CURR_FIRSTHALF_DMD_STR)
                    .subtract(propertyTaxDetails.get(CURR_FIRSTHALF_COLL_STR));
            arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR).subtract(propertyTaxDetails.get(ARR_COLL_STR));
        } else {
            currentPropertyTax = propertyTaxDetails.get(CURR_SECONDHALF_DMD_STR);
            currentPropertyTaxDue = propertyTaxDetails.get(CURR_SECONDHALF_DMD_STR)
                    .subtract(propertyTaxDetails.get(CURR_SECONDHALF_COLL_STR));
            currentFirstHalfTaxDue = propertyTaxDetails.get(CURR_FIRSTHALF_DMD_STR)
                    .subtract(propertyTaxDetails.get(CURR_FIRSTHALF_COLL_STR));
            arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR).subtract(propertyTaxDetails.get(ARR_COLL_STR))
                    .add(currentFirstHalfTaxDue);
        }
        model.addAttribute("currentPropertyTax", currentPropertyTax);
        model.addAttribute("currentPropertyTaxDue", currentPropertyTaxDue);
        model.addAttribute("arrearPropertyTaxDue", arrearPropertyTaxDue);
        if (arrearPropertyTaxDue.compareTo(BigDecimal.ZERO) > 0) {
            model.addAttribute("taxDuesErrorMsg", "Please clear property tax due for property demolition ");
            return TARGET_TAX_DUES;
        }
        model.addAttribute("isEmployee", !ANONYMOUS_USER.equalsIgnoreCase(loggedInUser.getName()) && propService.isEmployee(loggedInUser));
        propertyDemolitionService.addModelAttributes(model, basicProperty);
        model.addAttribute("stateType", property.getClass().getSimpleName());
        model.addAttribute("additionalRule", DEMOLITION);
        model.addAttribute("endorsementNotices", null);
        prepareWorkflow(model, property, new WorkflowContainer());
        return DEMOLITION_FORM;
    }


    @Transactional
    @RequestMapping(value = "/{assessmentNo}/{applicationSource}", method = RequestMethod.POST)
    public String demoltionFormSubmit(@ModelAttribute PropertyImpl property, final BindingResult errors, final Model model, final HttpServletRequest request,
                                      @RequestParam String workFlowAction) throws TaxCalculatorExeption {
        String target;
        User loggedInUser = securityUtils.getCurrentUser();
        propertyDemolitionService.validateProperty(property, errors, request);
        if (errors.hasErrors()) {
            prepareWorkflow(model, (PropertyImpl) property, new WorkflowContainer());
            model.addAttribute("stateType", property.getClass().getSimpleName());
            model.addAttribute("isEmployee", !ANONYMOUS_USER.equalsIgnoreCase(loggedInUser.getName()) && propService.isEmployee(loggedInUser));
            propertyDemolitionService.addModelAttributes(model, property.getBasicProperty());
            return DEMOLITION_FORM;
        } else {

            final Character status = STATUS_WORKFLOW;
            Long approvalPosition = 0l;
            String approvalComent = "";

            if (request.getParameter("approvalComent") != null)
                approvalComent = request.getParameter("approvalComent");
            if (request.getParameter("workFlowAction") != null)
                workFlowAction = request.getParameter("workFlowAction");
            if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
                approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
            if (propertyService.isMeesevaUser(loggedInUser)) {
                final HashMap<String, String> meesevaParams = new HashMap<>();
                meesevaParams.put("APPLICATIONNUMBER", ((PropertyImpl) property).getMeesevaApplicationNumber());
                if (StringUtils.isBlank(property.getApplicationNo())) {
                    property.setApplicationNo(((PropertyImpl) property).getMeesevaApplicationNumber());
                    property.setSource(PropertyTaxConstants.SOURCE_MEESEVA);
                }
                propertyDemolitionService.saveProperty(property.getBasicProperty().getActiveProperty(), property, status,
                        approvalComent, workFlowAction,
                        approvalPosition, DEMOLITION, meesevaParams);
            } else
            propertyDemolitionService.saveProperty(property.getBasicProperty().getActiveProperty(), property, status, approvalComent, workFlowAction,
                    approvalPosition, DEMOLITION);

            if (!propService.isEmployee(loggedInUser) || ANONYMOUS_USER.equalsIgnoreCase(loggedInUser.getName())) {
                Assignment assignment = propertyDemolitionService.getUserAssignment(loggedInUser, property);
                if (assignment != null)
                    approvalPosition = assignment.getPosition().getId();
            }
            model.addAttribute("showAckBtn", Boolean.TRUE);
            model.addAttribute("isOnlineApplication", ANONYMOUS_USER.equalsIgnoreCase(loggedInUser.getName()));
            model.addAttribute("propertyId", property.getBasicProperty().getUpicNo());
            model.addAttribute(
                    "successMessage",
                    "Property demolition data saved successfully in the system and forwarded to "
                            + propertyTaxUtil.getApproverUserName(approvalPosition));
            if (propertyService.isMeesevaUser(loggedInUser))
                target = "redirect:/property/demolition/generate-meesevareceipt/"
                        + ((PropertyImpl) property).getBasicProperty().getUpicNo() + "?transactionServiceNumber="
                        + ((PropertyImpl) property).getApplicationNo();
            else
                target = DEMOLITION_SUCCESS;
        return target;
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
}