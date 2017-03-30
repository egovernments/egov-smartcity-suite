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

package org.egov.egf.web.controller.advancepayment;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.service.FundService;
import org.egov.egf.advancepayment.SearchAdvanceRequisition;
import org.egov.egf.expensebill.service.ExpenseBillService;
import org.egov.egf.utils.FinancialUtils;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.model.advance.EgAdvanceRequisition;
import org.egov.model.bills.EgBillregister;
import org.egov.model.payment.Paymentheader;
import org.egov.services.masters.EgPartyTypeService;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/advancepayment")
public class AdvancePaymentController extends BasePaymentController {

    private static final String STATE_TYPE = "stateType";
    private static final String ADVANCEPAYMENT_NEW = "advancepayment-new";
    private static final String APPROVAL_POSITION = "approvalPosition";
    private static final String APPROVAL_DESIGNATION = "approvalDesignation";
    private static final String DESIGNATION = "designation";
    private static final String ADVANCEPAYMENT_VIEW = "advancepayment-view";
    private static final String ADVANCEREQUISITION_SEARCH = "advancerequisition-search";

    @Autowired
    private ExpenseBillService expenseBillService;

    @Autowired
    private FinancialUtils financialUtils;

    @Autowired
    private FundService fundService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private EgPartyTypeService egPartyTypeService;

    public AdvancePaymentController(final AppConfigValueService appConfigValuesService) {
        super(appConfigValuesService);
    }

    @Override
    public void prepare(final Model model) {
        super.prepare(model);
        model.addAttribute("bankaccounts", Collections.emptyList());
        model.addAttribute("modeOfPaymentMap", getModeOfPayments());
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@ModelAttribute final SearchAdvanceRequisition searchAdvanceRequisition, final Model model) {
        model.addAttribute("partyType", egPartyTypeService.findAll());
        model.addAttribute("searchAdvanceRequisition", searchAdvanceRequisition);
        model.addAttribute("fund", fundService.getByIsActive(true));
        return ADVANCEREQUISITION_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchBill(
            @ModelAttribute final SearchAdvanceRequisition searchAdvanceRequisition) {
        final List<EgAdvanceRequisition> advanceRequisitionList = advancePaymentService
                .searchAdvanceBill(searchAdvanceRequisition);
        String data;
        String result = "[";
        if (!advanceRequisitionList.isEmpty())
            for (final EgAdvanceRequisition eg : advanceRequisitionList) {
                data = "{\"advanceReqSourcePath\":\"" + eg.getEgAdvanceReqMises().getSourcePath() + "\",\"billId\":\""
                        + eg.getEgAdvanceReqMises().getEgBillregister().getId() + "\",\"voucherId\":\""
                        + eg.getEgAdvanceReqMises().getEgBillregister().getEgBillregistermis().getVoucherHeader().getId()
                        + "\",\"advanceRequisitionNumber\":\""
                        + eg.getAdvanceRequisitionNumber()
                        + "\",\"partyType\":\""
                        + eg.getArftype()
                        + "\",\"voucherNumber\":\""
                        + eg.getEgAdvanceReqMises().getEgBillregister().getEgBillregistermis().getVoucherHeader()
                                .getVoucherNumber()
                        + "\",\"amount\":"
                        + eg.getEgAdvanceReqMises().getEgBillregister().getBillamount() + "},";
                result = result.concat(data);

            }
        final StringBuilder results = new StringBuilder("{ \"data\":").append(result);
        if (advanceRequisitionList.isEmpty())
            return results.append("]}").toString();
        else
            return results.deleteCharAt(results.length() - 1).append("]}").toString();
    }

    @RequestMapping(value = "/newform", method = RequestMethod.GET)
    public String newForm(@RequestParam("billId") final String billId, final Model model) {
        prepare(model);
        final EgBillregister egBillRegister = expenseBillService.getById(Long.parseLong(billId));
        loadbankBranch(egBillRegister.getEgBillregistermis().getFund(), model);
        final CVoucherHeader voucherHeader = egBillRegister.getEgBillregistermis().getVoucherHeader();
        voucherHeader.setVoucherDate(new Date());
        model.addAttribute("voucherHeader", voucherHeader);
        model.addAttribute("egBillregister", egBillRegister);
        model.addAttribute(STATE_TYPE, Paymentheader.class.getSimpleName());
        model.addAttribute("billIds", billId);
        prepareWorkflow(model, egBillRegister.getEgBillregistermis().getVoucherHeader(), new WorkflowContainer());
        prepareValidActionListByCutOffDate(model);

        return ADVANCEPAYMENT_NEW;

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@ModelAttribute("voucherHeader") final CVoucherHeader voucherHeader, final Model model,
            final BindingResult resultBinder, final HttpServletRequest request, @RequestParam final String workFlowAction)
            throws IOException, ApplicationException {

        Paymentheader paymentHeader;
        String modeOfPay = "";
        String bankAccount = "";
        if (StringUtils.isNotBlank(request.getParameter("bankaccount")))
            bankAccount = request.getParameter("bankaccount");
        final EgBillregister egBillRegister = expenseBillService.getById(Long.parseLong(request.getParameter("billId")));
        if (request.getParameter("modeOfPayment") != null && StringUtils.isNotBlank(request.getParameter("modeOfPayment")))
            modeOfPay = request.getParameter("modeOfPayment");
        final EgAdvanceRequisition egAdvanceRequisition = advancePaymentService.validateVoucherDetails(egBillRegister, modeOfPay,
                resultBinder);
        if (resultBinder.hasErrors()) {
            setUp(model, egBillRegister, voucherHeader, request);
            return ADVANCEPAYMENT_NEW;
        } else {
            Long approvalPosition = 0l;
            String approvalComment = "";
            if (request.getParameter("approvalComent") != null)
                approvalComment = request.getParameter("approvalComent");
            if (request.getParameter(APPROVAL_POSITION) != null && !request.getParameter(APPROVAL_POSITION).isEmpty())
                approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));

            try {

                paymentHeader = advancePaymentService.createAdvancePayment(egBillRegister, voucherHeader, egAdvanceRequisition,
                        bankAccount,
                        modeOfPay, workFlowAction, approvalComment, approvalPosition,
                        egAdvanceRequisition.getAdvanceRequisitionAmount());
            } catch (final ValidationException e) {
                setUp(model, egBillRegister, voucherHeader, request);
                return ADVANCEPAYMENT_NEW;
            }
            final String approverDetails = financialUtils.getApproverDetails(workFlowAction,
                    paymentHeader.getState(), paymentHeader.getId(), approvalPosition);
            return "redirect:/advancepayment/success?approverDetails= " + approverDetails + "&voucherNumber="
                    + paymentHeader.getVoucherheader().getVoucherNumber() + "&workFlowAction=" + workFlowAction;
        }
    }

    @RequestMapping(value = "/view/{paymentId}", method = RequestMethod.GET)
    public String viewForm(final Model model, @PathVariable final String paymentId,
            final HttpServletRequest request) throws ApplicationException {
        final String[] payId = paymentId.split("&");
        final Paymentheader paymentHeader = paymentService.findById(Long.parseLong(payId[0]), false);
        final EgBillregister egBillRegister = advancePaymentService.getBillRegisterFromPayVhid(paymentHeader);
        prepare(model);
        model.addAttribute("egBillregister", egBillRegister);
        model.addAttribute("totalAmount", paymentHeader.getPaymentAmount());
        model.addAttribute("stateType", Paymentheader.class.getSimpleName());
        if (paymentHeader.getState() != null)
            model.addAttribute("currentState", paymentHeader.getState().getValue());
        model.addAttribute("workflowHistory",
                financialUtils.getHistory(paymentHeader.getState(), paymentHeader.getStateHistory()));
        prepareWorkflow(model, paymentHeader, new WorkflowContainer());
        model.addAttribute("paymentHeader", paymentHeader);
        model.addAttribute("voucherHeader", paymentHeader.getVoucherheader());
        if (payId.length > 1 && StringUtils.isNotBlank(paymentId.split("&")[1]))
            model.addAttribute("mode", "readOnly");
        else
            model.addAttribute("mode", "view");
        return ADVANCEPAYMENT_VIEW;
    }

    @RequestMapping(value = "/view/{paymentId}", method = RequestMethod.POST)
    public String forward(@ModelAttribute("voucherHeader") final CVoucherHeader voucherHeader,
            final BindingResult resultBinder, final Model model,
            final HttpServletRequest request, @RequestParam final String workFlowAction)
            throws ApplicationException, IOException {

        final Paymentheader paymentheader = paymentService
                .findById(Long.parseLong(request.getParameter("payHeaderId")), false);
        Long approvalPosition = 0l;
        String approvalComment = "";
        if (request.getParameter("approvalComent") != null)
            approvalComment = request.getParameter("approvalComent");
        if (request.getParameter(APPROVAL_POSITION) != null && !request.getParameter(APPROVAL_POSITION).isEmpty())
            approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));
        paymentheader.setVoucherheader(setVoucherStatus(paymentheader.getVoucherheader(), workFlowAction));
        final Paymentheader updatedPaymentHeader = advancePaymentService.updatePaymentHeader(paymentheader, workFlowAction,
                approvalPosition, approvalComment, null);

        final String approverDetails = financialUtils.getApproverDetails(workFlowAction,
                updatedPaymentHeader.getState(), updatedPaymentHeader.getId(), approvalPosition);
        return "redirect:/advancepayment/success?approverDetails= " + approverDetails + "&voucherNumber="
                + updatedPaymentHeader.getVoucherheader().getVoucherNumber() + "&workFlowAction=" + workFlowAction;
    }

    private void setUp(final Model model, final EgBillregister egBillregister, final CVoucherHeader cVoucherHeader,
            final HttpServletRequest request) {
        prepare(model);
        loadbankBranch(egBillregister.getEgBillregistermis().getFund(), model);
        model.addAttribute(STATE_TYPE, Paymentheader.class.getSimpleName());
        prepareWorkflow(model, egBillregister.getEgBillregistermis().getVoucherHeader(), new WorkflowContainer());
        model.addAttribute(APPROVAL_DESIGNATION, request.getParameter(APPROVAL_DESIGNATION));
        model.addAttribute(APPROVAL_POSITION, request.getParameter(APPROVAL_POSITION));
        model.addAttribute(DESIGNATION, request.getParameter(DESIGNATION));
        model.addAttribute("voucherHeader", cVoucherHeader);
        model.addAttribute("egBillregister", egBillregister);
        model.addAttribute("billIds", request.getParameter("billId"));
        prepareValidActionListByCutOffDate(model);
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String showSuccessPage(@RequestParam("voucherNumber") final String voucherNumber, final Model model,
            final HttpServletRequest request) {
        final String[] keyNameArray = request.getParameter("approverDetails").split(",");
        final String workFlowAction = request.getParameter("workFlowAction");
        Long id = 0L;
        String approverName = "";
        String currentUserDesgn = "";
        String nextDesign = "";
        if (keyNameArray.length != 0 && keyNameArray.length > 0)
            if (keyNameArray.length == 1)
                id = Long.parseLong(keyNameArray[0].trim());
            else if (keyNameArray.length == 3) {
                id = Long.parseLong(keyNameArray[0].trim());
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
            } else {
                id = Long.parseLong(keyNameArray[0].trim());
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
                nextDesign = keyNameArray[3];
            }

        if (id != null)
            model.addAttribute("approverName", approverName);
        model.addAttribute("currentUserDesgn", currentUserDesgn);
        model.addAttribute("nextDesign", nextDesign);
        final String message = getMessageByStatus(voucherNumber, approverName, nextDesign, workFlowAction);
        model.addAttribute("message", message);

        return "advancepayment-success";
    }

    private String getMessageByStatus(final String paymentheader, final String approverName, final String nextDesign,
            final String workFlowAction) {
        String message = "";

        if (FinancialConstants.BUTTONFORWARD.toString().equalsIgnoreCase(workFlowAction))
            message = messageSource.getMessage("msg.advance.payment.create.success",
                    new String[] { paymentheader, approverName, nextDesign }, null);
        else if (FinancialConstants.BUTTONAPPROVE.toString().equalsIgnoreCase(workFlowAction))
            message = messageSource.getMessage("msg.advance.payment.approved.success",
                    new String[] { paymentheader }, null);
        else if (FinancialConstants.BUTTONREJECT.toString().equalsIgnoreCase(workFlowAction))
            message = messageSource.getMessage("msg.advance.payment.reject",
                    new String[] { paymentheader, approverName, nextDesign }, null);
        else if (FinancialConstants.BUTTONCANCEL.toString().equalsIgnoreCase(workFlowAction))
            message = messageSource.getMessage("msg.advance.payment.cancel",
                    new String[] { paymentheader }, null);
        else if (FinancialConstants.CREATEANDAPPROVE.toString().equalsIgnoreCase(workFlowAction))
            message = messageSource.getMessage("msg.advance.payment.createapprove.success",
                    new String[] { paymentheader }, null);

        return message;
    }

}
