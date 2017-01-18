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
package org.egov.works.web.controller.contractorbill;

import javax.servlet.http.HttpServletRequest;

import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.exception.ApplicationException;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.entity.SearchRequestContractorBill;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/contractorbill")
public class CancelContractorBillController extends GenericWorkFlowController {
    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    @RequestMapping(value = "/cancel/search", method = RequestMethod.GET)
    public String showSearchContractorBillForm(
            @ModelAttribute final SearchRequestContractorBill searchRequestContractorBill,
            final Model model) throws ApplicationException {
        model.addAttribute("departments", departmentService.getAllDepartments());
        final String defaultApproverDept = worksApplicationProperties.getDefaultApproverDepartment();
        if (defaultApproverDept != null) {
            final Department approverDepartment = departmentService.getDepartmentByName(defaultApproverDept);
            if (approverDepartment != null)
                searchRequestContractorBill.setDepartment(approverDepartment.getId());
        }
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
