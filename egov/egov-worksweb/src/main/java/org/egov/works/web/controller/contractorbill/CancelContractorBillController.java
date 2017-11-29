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
package org.egov.works.web.controller.contractorbill;

import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.exception.ApplicationException;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.entity.SearchRequestContractorBill;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/contractorbill")
public class CancelContractorBillController extends GenericWorkFlowController {
    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private WorksUtils worksUtils;

    @RequestMapping(value = "/cancel/search", method = RequestMethod.GET)
    public String showSearchContractorBillForm(
            @ModelAttribute final SearchRequestContractorBill searchRequestContractorBill,
            final Model model) throws ApplicationException {
        model.addAttribute("departments", departmentService.getAllDepartments());
        searchRequestContractorBill.setDepartment(worksUtils.getDefaultDepartmentId());
        model.addAttribute("searchRequestContractorBill", searchRequestContractorBill);

        return "searchcontractorbill-cancel";
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancelContractorBill(final HttpServletRequest request,
            final Model model) throws ApplicationException {
        final Long contractorId = Long.parseLong(request.getParameter("id"));
        final String cancellationReason = request.getParameter("cancellationReason");
        final String cancellationRemarks = request.getParameter("cancellationRemarks");
        ContractorBillRegister contractorBillRegister = contractorBillRegisterService.getContractorBillById(contractorId);
        if (contractorBillRegister.getEgBillregistermis() != null
                && contractorBillRegister.getEgBillregistermis().getVoucherHeader() != null
                && contractorBillRegister.getEgBillregistermis().getVoucherHeader().getStatus() != 4) {
            model.addAttribute("errorMessage", messageSource.getMessage("error.contractorbill.voucher.created",
                    new String[] {}, null) + " "
                    + contractorBillRegister.getEgBillregistermis().getVoucherHeader().getVoucherNumber());
            return "contractorBill-success";
        }
        contractorBillRegister.setCancellationReason(cancellationReason);
        contractorBillRegister.setCancellationRemarks(cancellationRemarks);
        contractorBillRegister = contractorBillRegisterService.cancel(contractorBillRegister);
        model.addAttribute("contractorBillRegister", contractorBillRegister);
        model.addAttribute("message", messageSource.getMessage("msg.contractorbill.cancel",
                new String[] { contractorBillRegister.getBillnumber() }, null));
        return "contractorBill-success";
    }
}
