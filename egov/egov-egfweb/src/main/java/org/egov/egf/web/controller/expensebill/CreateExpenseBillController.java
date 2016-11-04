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
package org.egov.egf.web.controller.expensebill;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.egf.expensebill.service.ExpenseBillService;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
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
@RequestMapping(value = "/expensebill")
public class CreateExpenseBillController extends BaseBillController {

    public CreateExpenseBillController(final AppConfigValueService appConfigValuesService) {
        super(appConfigValuesService);
    }

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private ExpenseBillService expenseBillService;

    @Autowired
    @Qualifier("chartOfAccountsService")
    private ChartOfAccountsService chartOfAccountsService;

    @Override
    protected void setDropDownValues(final Model model) {
        super.setDropDownValues(model);
    }

    @RequestMapping(value = "/newform", method = RequestMethod.GET)
    public String showNewForm(@ModelAttribute("egBillregister") final EgBillregister egBillregister, final Model model) {
        setDropDownValues(model);
        egBillregister.setBilldate(new Date());
        return "expenseBill-form";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@ModelAttribute("egBillregister") final EgBillregister egBillregister, final Model model,
            final BindingResult resultBinder) throws IOException {

        validateBillNumber(egBillregister, resultBinder);
        validateLedgerAndSubledger(egBillregister, resultBinder);

        if (resultBinder.hasErrors()) {
            setDropDownValues(model);
            egBillregister.setBilldate(new Date());

            return "expenseBill-form";
        } else {

            populateBillDetails(egBillregister);
            try {
                expenseBillService.create(egBillregister);
            } catch (final ValidationException e) {
                // TODO: Used ApplicationRuntimeException for time being since
                // there is issue in session after
                // checkBudgetAndGenerateBANumber API call. Needs to replace
                // with errors.reject
                throw new ApplicationRuntimeException("error.expense.bill.budgetcheck.insufficient.amount");
            }

            return "redirect:/expensebill/success?billNumber=" + egBillregister.getBillnumber();

        }
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String showContractorBillSuccessPage(@RequestParam("billNumber") final String billNumber, final Model model,
            final HttpServletRequest request) {

        model.addAttribute("message",
                messageSource.getMessage("msg.expense.bill.create.success", new String[] { billNumber }, null));

        return "expenseBill-success";
    }

    @SuppressWarnings("unchecked")
    private void populateBillDetails(final EgBillregister egBillregister) {
        egBillregister.getEgBilldetailes().addAll(egBillregister.getBillDetails());
        for (final EgBilldetails details : egBillregister.getEgBilldetailes()) {
            if (egBillregister.getEgBillregistermis().getFunction() != null)
                details.setFunctionid(BigDecimal.valueOf(egBillregister.getEgBillregistermis().getFunction().getId()));
            details.setEgBillregister(egBillregister);
            details.setLastupdatedtime(new Date());
            details.setChartOfAccounts(chartOfAccountsService.findById(details.getGlcodeid().longValue(), false));
        }
        if (!egBillregister.getBillPayeedetails().isEmpty())
            populateBillPayeeDetails(egBillregister);
    }

    private void populateBillPayeeDetails(final EgBillregister egBillregister) {
        EgBillPayeedetails payeeDetail = null;
        for (final EgBilldetails details : egBillregister.getEgBilldetailes())
            for (final EgBillPayeedetails payeeDetails : egBillregister.getBillPayeedetails())
                if (details.getGlcodeid().equals(payeeDetails.getEgBilldetailsId().getGlcodeid())) {
                    payeeDetail = new EgBillPayeedetails();
                    payeeDetail.setEgBilldetailsId(details);
                    payeeDetail.setAccountDetailTypeId(payeeDetails.getAccountDetailTypeId());
                    payeeDetail.setAccountDetailKeyId(payeeDetails.getAccountDetailKeyId());
                    payeeDetail.setDebitAmount(payeeDetails.getDebitAmount());
                    payeeDetail.setCreditAmount(payeeDetails.getCreditAmount());
                    payeeDetail.setLastUpdatedTime(new Date());
                    details.getEgBillPaydetailes().add(payeeDetail);
                }
    }

    private void validateBillNumber(final EgBillregister egBillregister, final BindingResult resultBinder) {
        if (!expenseBillService.isBillNumberGenerationAuto())
            if (!isBillNumUnique(egBillregister.getBillnumber()))
                resultBinder.reject("msg.expense.bill.duplicate.bill.number",
                        new String[] { egBillregister.getBillnumber() }, null);
    }

    private boolean isBillNumUnique(final String billnumber) {
        final EgBillregister bill = expenseBillService.getByBillnumber(billnumber);
        return bill == null;
    }

    private void validateLedgerAndSubledger(final EgBillregister egBillregister, final BindingResult resultBinder) {
        BigDecimal totalDrAmt = BigDecimal.ZERO;
        BigDecimal totalCrAmt = BigDecimal.ZERO;
        for (final EgBilldetails details : egBillregister.getEgBilldetailes()) {
            if (details.getDebitamount() != null)
                totalDrAmt = totalDrAmt.add(details.getDebitamount());
            if (details.getCreditamount() != null)
                totalCrAmt = totalCrAmt.add(details.getCreditamount());
            if (details.getGlcodeid() == null)
                resultBinder.rejectValue("msg.expense.bill.accdetail.accmissing", "msg.expense.bill.accdetail.accmissing");

            if (details.getDebitamount() != null && details.getCreditamount() != null
                    && details.getDebitamount().equals(BigDecimal.ZERO) && details.getCreditamount().equals(BigDecimal.ZERO)
                    && details.getGlcodeid() != null)
                resultBinder.reject("msg.expense.bill.accdetail.amountzero",
                        new String[] { details.getChartOfAccounts().getGlcode() }, null);

            if (details.getDebitamount() != null && details.getCreditamount() != null
                    && details.getDebitamount().compareTo(BigDecimal.ZERO) == 1
                    && details.getCreditamount().compareTo(BigDecimal.ZERO) == 1)
                resultBinder.reject("msg.expense.bill.accdetail.amount",
                        new String[] { details.getChartOfAccounts().getGlcode() }, null);
        }
        if (totalDrAmt.compareTo(totalCrAmt) != 0)
            resultBinder.rejectValue("msg.expense.bill.accdetail.drcrmatch", "msg.expense.bill.accdetail.drcrmatch");
        validateSubledgerDetails(egBillregister, resultBinder);
    }

    private void validateSubledgerDetails(final EgBillregister egBillregister, final BindingResult resultBinder) {
        Boolean check;
        BigDecimal detailAmt;
        BigDecimal payeeDetailAmt;
        for (final EgBilldetails details : egBillregister.getEgBilldetailes()) {

            detailAmt = BigDecimal.ZERO;
            payeeDetailAmt = BigDecimal.ZERO;

            if (details.getDebitamount() != null && details.getDebitamount().compareTo(BigDecimal.ZERO) == 1)
                detailAmt = details.getDebitamount();
            else if (details.getCreditamount() != null &&
                    details.getCreditamount().compareTo(BigDecimal.ZERO) == 1)
                detailAmt = details.getCreditamount();

            for (final EgBillPayeedetails payeeDetails : egBillregister.getBillPayeedetails()) {

                if (payeeDetails.getDebitAmount() != null && payeeDetails.getCreditAmount() != null
                        && payeeDetails.getDebitAmount().equals(BigDecimal.ZERO)
                        && payeeDetails.getCreditAmount().equals(BigDecimal.ZERO))
                    resultBinder.reject("msg.expense.bill.subledger.amountzero",
                            new String[] { details.getChartOfAccounts().getGlcode() }, null);

                if (payeeDetails.getDebitAmount() != null && payeeDetails.getCreditAmount() != null
                        && payeeDetails.getDebitAmount().compareTo(BigDecimal.ZERO) == 1
                        && payeeDetails.getCreditAmount().compareTo(BigDecimal.ZERO) == 1)
                    resultBinder.reject("msg.expense.bill.subledger.amount",
                            new String[] { details.getChartOfAccounts().getGlcode() }, null);

                if (payeeDetails.getDebitAmount() != null && payeeDetails.getDebitAmount().compareTo(BigDecimal.ZERO) == 1)
                    payeeDetailAmt = payeeDetailAmt.add(payeeDetails.getDebitAmount());
                else if (payeeDetails.getCreditAmount() != null && payeeDetails.getCreditAmount().compareTo(BigDecimal.ZERO) == 1)
                    payeeDetailAmt = payeeDetailAmt.add(payeeDetails.getCreditAmount());

                check = false;
                for (final CChartOfAccountDetail coaDetails : details.getChartOfAccounts().getChartOfAccountDetails())
                    if (payeeDetails.getAccountDetailTypeId() == coaDetails.getDetailTypeId().getId())
                        check = true;
                if (!check)
                    resultBinder.reject("msg.expense.bill.subledger.mismatch",
                            new String[] { details.getChartOfAccounts().getGlcode() }, null);

            }

            if (detailAmt.compareTo(payeeDetailAmt) != 0)
                resultBinder.reject("msg.expense.bill.subledger.amtnotmatchinng",
                        new String[] { details.getChartOfAccounts().getGlcode() }, null);
        }
    }

}