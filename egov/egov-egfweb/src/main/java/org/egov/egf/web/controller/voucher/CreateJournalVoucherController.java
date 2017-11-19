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
package org.egov.egf.web.controller.voucher;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.egov.commons.CVoucherHeader;
import org.egov.egf.utils.FinancialUtils;
import org.egov.egf.voucher.service.JournalVoucherService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author venki
 *
 */

@Controller
@RequestMapping(value = "/journalvoucher")
public class CreateJournalVoucherController extends BaseVoucherController {

    private static final String JOURNALVOUCHER_FORM = "journalvoucher-form";

    private static final String VOUCHER_NUMBER_GENERATION_AUTO = "voucherNumberGenerationAuto";

    private static final String STATE_TYPE = "stateType";

    private static final String APPROVAL_POSITION = "approvalPosition";

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private JournalVoucherService journalVoucherService;

    @Autowired
    private FinancialUtils financialUtils;

    public CreateJournalVoucherController(final AppConfigValueService appConfigValuesService) {
        super(appConfigValuesService);
    }

    @Override
    protected void setDropDownValues(final Model model) {
        super.setDropDownValues(model);
        model.addAttribute("voucherSubTypes", FinancialUtils.VOUCHER_SUBTYPES);
    }

    @RequestMapping(value = "/newform", method = RequestMethod.GET)
    public String showNewForm(@ModelAttribute("voucherHeader") final CVoucherHeader voucherHeader, final Model model) {
        voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL);
        setDropDownValues(model);
        model.addAttribute(STATE_TYPE, voucherHeader.getClass().getSimpleName());
        prepareWorkflow(model, voucherHeader, new WorkflowContainer());
        prepareValidActionListByCutOffDate(model);
        voucherHeader.setVoucherDate(new Date());
        model.addAttribute(VOUCHER_NUMBER_GENERATION_AUTO, isVoucherNumberGenerationAuto(voucherHeader));
        return JOURNALVOUCHER_FORM;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@ModelAttribute("voucherHeader") final CVoucherHeader voucherHeader, final Model model,
            final BindingResult resultBinder, final HttpServletRequest request, @RequestParam final String workFlowAction)
            throws IOException {

        voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL);
        voucherHeader.setEffectiveDate(voucherHeader.getVoucherDate());

        populateVoucherName(voucherHeader);
        populateAccountDetails(voucherHeader);

        if (resultBinder.hasErrors()) {
            setDropDownValues(model);
            model.addAttribute(STATE_TYPE, voucherHeader.getClass().getSimpleName());
            prepareWorkflow(model, voucherHeader, new WorkflowContainer());
            prepareValidActionListByCutOffDate(model);
            voucherHeader.setVoucherDate(new Date());
            model.addAttribute(VOUCHER_NUMBER_GENERATION_AUTO, isVoucherNumberGenerationAuto(voucherHeader));

            return JOURNALVOUCHER_FORM;
        } else {
            Long approvalPosition = 0l;
            String approvalComment = "";
            if (request.getParameter("approvalComment") != null)
                approvalComment = request.getParameter("approvalComent");
            if (request.getParameter(APPROVAL_POSITION) != null && !request.getParameter(APPROVAL_POSITION).isEmpty())
                approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));
            CVoucherHeader savedVoucherHeader;
            try {
                savedVoucherHeader = journalVoucherService.create(voucherHeader, approvalPosition, approvalComment, null,
                        workFlowAction);
            } catch (final ValidationException e) {
                setDropDownValues(model);
                model.addAttribute(STATE_TYPE, voucherHeader.getClass().getSimpleName());
                prepareWorkflow(model, voucherHeader, new WorkflowContainer());
                prepareValidActionListByCutOffDate(model);
                voucherHeader.setVoucherDate(new Date());
                model.addAttribute(VOUCHER_NUMBER_GENERATION_AUTO, isVoucherNumberGenerationAuto(voucherHeader));
                resultBinder.reject("", e.getErrors().get(0).getMessage());
                return JOURNALVOUCHER_FORM;
            }

            final String approverDetails = financialUtils.getApproverDetails(workFlowAction,
                    savedVoucherHeader.getState(), savedVoucherHeader.getId(), approvalPosition);

            return "redirect:/journalvoucher/success?approverDetails= " + approverDetails + "&voucherNumber="
                    + savedVoucherHeader.getVoucherNumber() + "&workFlowAction=" + workFlowAction;

        }
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String showSuccessPage(@RequestParam("voucherNumber") final String voucherNumber, final Model model,
            final HttpServletRequest request) {
        final String workFlowAction = request.getParameter("workFlowAction");
        final String[] keyNameArray = request.getParameter("approverDetails").split(",");
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

        final CVoucherHeader voucherHeader = journalVoucherService.getByVoucherNumber(voucherNumber);

        final String message = getMessageByStatus(voucherHeader, approverName, nextDesign, workFlowAction);

        model.addAttribute("message", message);

        return "expensebill-success";
    }

    private String getMessageByStatus(final CVoucherHeader voucherHeader, final String approverName, final String nextDesign,
            final String workFlowAction) {
        String message;

        if (FinancialConstants.PREAPPROVEDVOUCHERSTATUS.equals(voucherHeader.getStatus()))
            message = messageSource.getMessage("msg.journal.voucher.create.success",
                    new String[] { voucherHeader.getVoucherNumber(), approverName, nextDesign }, null);
        else if (FinancialConstants.CREATEDVOUCHERSTATUS.equals(voucherHeader.getStatus()))
            message = messageSource.getMessage("msg.journal.voucher.approved.success",
                    new String[] { voucherHeader.getVoucherNumber() }, null);
        else if (FinancialConstants.WORKFLOW_STATE_CANCELLED.equals(workFlowAction))
            message = messageSource.getMessage("msg.journal.voucher.cancel",
                    new String[] { voucherHeader.getVoucherNumber() }, null);
        else
            message = messageSource.getMessage("msg.journal.voucher.reject",
                    new String[] { voucherHeader.getVoucherNumber(), approverName, nextDesign }, null);

        return message;
    }
}