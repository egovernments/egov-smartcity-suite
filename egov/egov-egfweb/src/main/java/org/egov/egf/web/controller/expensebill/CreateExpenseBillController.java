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
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.egov.egf.expensebill.service.ExpenseBillService;
import org.egov.infra.admin.master.service.AppConfigValueService;
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

        if (resultBinder.hasErrors()) {
            setDropDownValues(model);

            return "expenseBill-form";
        } else {
            expenseBillService.create(egBillregister);
            return "redirect:/expensebill/success?billNumber=" + egBillregister.getBillnumber();
        }
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String showContractorBillSuccessPage(@RequestParam("billNumber") final String billNumber, final Model model,
            final HttpServletRequest request) {

        /*
         * final String[] keyNameArray = request.getParameter("pathVars").split(","); Long id = 0L; String approverName = "";
         * String currentUserDesgn = ""; String nextDesign = ""; if (keyNameArray.length != 0 && keyNameArray.length > 0) if
         * (keyNameArray.length == 1) id = Long.parseLong(keyNameArray[0]); else if (keyNameArray.length == 3) { id =
         * Long.parseLong(keyNameArray[0]); approverName = keyNameArray[1]; currentUserDesgn = keyNameArray[2]; } else { id =
         * Long.parseLong(keyNameArray[0]); approverName = keyNameArray[1]; currentUserDesgn = keyNameArray[2]; nextDesign =
         * keyNameArray[3]; } if (id != null) model.addAttribute("approverName", approverName);
         * model.addAttribute("currentUserDesgn", currentUserDesgn); model.addAttribute("nextDesign", nextDesign);
         */
        model.addAttribute("message",
                messageSource.getMessage("msg.expense.bill.create.success", new String[] { billNumber }, null));

        return "expenseBill-success";
    }

}