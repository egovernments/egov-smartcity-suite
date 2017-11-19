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
package org.egov.works.web.controller.letterofacceptance;

import org.apache.commons.lang3.StringUtils;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/letterofacceptance")
public class UpdateLetterOfAcceptanceController {

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private BudgetDetailsDAO budgetDetailsDAO;

    @ModelAttribute
    public WorkOrder getWorkOrder(@PathVariable final String id) {
        final WorkOrder workOrder = letterOfAcceptanceService.getWorkOrderById(Long.parseLong(id));
        return workOrder;
    }

    @RequestMapping(value = "/modify/{id}", method = RequestMethod.GET)
    public String viewLOA(@PathVariable final String id, final Model model,
            final HttpServletRequest request)
            throws ApplicationException {
        final WorkOrder workOrder = letterOfAcceptanceService.getWorkOrderById(Long.parseLong(id));
        final LineEstimateDetails lineEstimateDetails = lineEstimateService.findByEstimateNumber(workOrder.getEstimateNumber());
        final WorkOrder newWorkOrder = getWorkOrderDocuments(workOrder);
        model.addAttribute("workOrder", newWorkOrder);
        model.addAttribute("lineEstimateDetails", lineEstimateDetails);
        model.addAttribute("loggedInUser", securityUtils.getCurrentUser().getName());
        model.addAttribute("mode", "modify");
        return "letterOfAcceptance-modify";
    }

    @RequestMapping(value = "/modify/{id}", method = RequestMethod.POST)
    public String modify(@ModelAttribute("workOrder") final WorkOrder workOrder, final Model model,
            final BindingResult resultBinder,
            final HttpServletRequest request) throws ApplicationException {
        final List<String> workOrderNumbers = letterOfAcceptanceService.getApprovedWorkOrdersForCreateContractorBill(workOrder.getWorkOrderNumber());
        if(workOrderNumbers.isEmpty()) {
            resultBinder.rejectValue("", "error.modify.loa.finalbill.exists");
        }
        
        final LineEstimateDetails lineEstimateDetails = lineEstimateService.findByEstimateNumber(workOrder.getEstimateNumber());

        final Double revisedWorkOrderAmount = Double.valueOf(request.getParameter("revisedWorkOrderAmount"));
        final Double revisedValue = Double.valueOf(request.getParameter("revisedValue"));
        Double balanceAmount = 0.0;
        Double grossBillAmount = 0.0;
        final DecimalFormat df = new DecimalFormat("0.00");
        grossBillAmount = letterOfAcceptanceService.getGrossBillAmountOfBillsCreated(workOrder.getWorkOrderNumber(),
                WorksConstants.APPROVED, ContractorBillRegister.BillStatus.CANCELLED.toString());
        if (grossBillAmount == null)
            grossBillAmount = 0.0;
        final BigDecimal ledGrossBillAmount = lineEstimateDetails.getGrossAmountBilled();
        if (revisedWorkOrderAmount >= 0 && "-".equals(workOrder.getPercentageSign())) {
            if (lineEstimateDetails.getLineEstimate().isSpillOverFlag())
                balanceAmount = ledGrossBillAmount != null  ? workOrder.getWorkOrderAmount() - grossBillAmount - revisedValue
                        - ledGrossBillAmount.doubleValue() : workOrder.getWorkOrderAmount() - grossBillAmount - revisedValue;
            
            else
                balanceAmount = workOrder.getWorkOrderAmount() - grossBillAmount - revisedValue;
            if (balanceAmount < 0) {
                if(lineEstimateDetails.getLineEstimate().isSpillOverFlag())
                    grossBillAmount += lineEstimateDetails.getGrossAmountBilled().doubleValue();
                
                resultBinder.rejectValue("", "error.modify.loa.appropriation.amount",
                        new String[] { df.format(grossBillAmount), df.format(revisedWorkOrderAmount) },
                        null);
            }
        } else if (revisedWorkOrderAmount >= 0 && "+".equals(workOrder.getPercentageSign()))
            balanceAmount = revisedWorkOrderAmount - workOrder.getWorkOrderAmount();

        if (revisedWorkOrderAmount == 0)
            resultBinder.rejectValue(StringUtils.EMPTY, "error.modify.loa.agreement.amount");

        if (resultBinder.hasErrors()) {
            model.addAttribute("lineEstimateDetails", lineEstimateDetails);
            model.addAttribute("mode", "modify");
            model.addAttribute("lineEstimateDetails", lineEstimateDetails);
            model.addAttribute("revisedValue", request.getParameter("revisedValue"));
            return "letterOfAcceptance-modify";
        } else {
            WorkOrder savedWorkOrder = null;
            try {
                savedWorkOrder = letterOfAcceptanceService.update(workOrder, lineEstimateDetails, revisedValue,
                        revisedWorkOrderAmount);
            } catch (final ValidationException e) {
                final List<Long> budgetheadid = new ArrayList<>();
                budgetheadid.add(lineEstimateDetails.getLineEstimate().getBudgetHead().getId());

                try {
                    final BigDecimal budgetAvailable = budgetDetailsDAO.getPlanningBudgetAvailable(
                            lineEstimateService.getCurrentFinancialYear(new Date()).getId(),
                            Integer.parseInt(
                                    lineEstimateDetails.getLineEstimate().getExecutingDepartment().getId().toString()),
                            lineEstimateDetails.getLineEstimate().getFunction().getId(), null,
                            lineEstimateDetails.getLineEstimate().getScheme() == null ? null
                                    : Integer.parseInt(
                                            lineEstimateDetails.getLineEstimate().getScheme().getId().toString()),
                            lineEstimateDetails.getLineEstimate().getSubScheme() == null ? null
                                    : Integer.parseInt(
                                            lineEstimateDetails.getLineEstimate().getSubScheme().getId().toString()),
                            null, budgetheadid,
                            Integer.parseInt(lineEstimateDetails.getLineEstimate().getFund().getId().toString()));
                    final String errorMessage = messageSource.getMessage("error.budgetappropriation.amount",
                            new String[] { budgetAvailable.toString(), df.format(balanceAmount) }, null);
                    model.addAttribute("message", errorMessage);
                } catch (final ValidationException v) {
                    model.addAttribute("errorMessage", v.getErrors().get(0).getMessage());
                }

                return "lineestimate-success";
            }
            return "redirect:/letterofacceptance/loa-success?loaNumber=" + savedWorkOrder.getWorkOrderNumber() + "&isModify=true";
        }
    }

    private WorkOrder getWorkOrderDocuments(final WorkOrder workOrder) {
        List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
        documentDetailsList = worksUtils.findByObjectIdAndObjectType(workOrder.getId(),
                WorksConstants.WORKORDER);
        workOrder.setDocumentDetails(documentDetailsList);
        return workOrder;
    }
}
