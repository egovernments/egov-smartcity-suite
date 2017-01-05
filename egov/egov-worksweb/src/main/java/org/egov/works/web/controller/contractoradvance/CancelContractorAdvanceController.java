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
package org.egov.works.web.controller.contractoradvance;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationException;
import org.egov.model.bills.EgBillregister;
import org.egov.works.contractoradvance.entity.ContractorAdvanceRequisition;
import org.egov.works.contractoradvance.entity.SearchRequestContractorRequisition;
import org.egov.works.contractoradvance.service.ContractorAdvanceService;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/contractoradvance")
public class CancelContractorAdvanceController {

    public static final String CONTRACTORADVANCE_CANCEL_SUCCESS = "contractorAdvance-cancel-success";
    public static final String ERROR_MESSAGE = "errorMessage";

    @Autowired
    private ContractorAdvanceService contractorAdvanceService;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;

    @RequestMapping(value = "/cancel/search", method = RequestMethod.GET)
    public String SearchContractorAdvance(
            @ModelAttribute final SearchRequestContractorRequisition searchRequestContractorRequisition,
            final Model model)
            throws ApplicationException {
        final List<User> advanceRequisitionCreatedByUsers = contractorAdvanceService.getAdvanceRequisitionCreatedByUsers();

        model.addAttribute("advanceRequisitionCreatedByUsers", advanceRequisitionCreatedByUsers);
        model.addAttribute("searchRequestContractorRequisition", searchRequestContractorRequisition);
        return "contractorAdvance-cancel";
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancelContractorAdvance(final HttpServletRequest request,
            final Model model) throws ApplicationException {
        final Long contractorAdvanceId = Long.parseLong(request.getParameter("id"));
        final String cancellationReason = request.getParameter("cancellationReason");
        final String cancellationRemarks = request.getParameter("cancellationRemarks");

        ContractorAdvanceRequisition contractorAdvanceRequisition = contractorAdvanceService
                .getContractorAdvanceRequisitionById(contractorAdvanceId);

        if (contractorAdvanceRequisition.getEgAdvanceReqMises().getEgBillregister() != null) {
            final String advanceRequisitionBillNumbers = checkForAdvanceAdjustment(contractorAdvanceRequisition);

            if (!StringUtils.isBlank(advanceRequisitionBillNumbers)) {
                model.addAttribute(ERROR_MESSAGE, messageSource.getMessage("error.arf.advanceadjustment",
                        new String[] { advanceRequisitionBillNumbers }, null));
                return CONTRACTORADVANCE_CANCEL_SUCCESS;
            }
        }

        final String advanceRequistions = contractorAdvanceService.getAdvanceRequisitionGreaterThanCurrent(
                contractorAdvanceRequisition.getWorkOrderEstimate().getId(), contractorAdvanceRequisition.getCreatedDate());

        if (!StringUtils.EMPTY.equals(advanceRequistions)) {
            model.addAttribute(ERROR_MESSAGE, messageSource.getMessage("error.arfexists.greaterthancreateddate",
                    new String[] { advanceRequistions }, null));
            return CONTRACTORADVANCE_CANCEL_SUCCESS;
        }

        final EgBillregister billregister = contractorAdvanceRequisition.getEgAdvanceReqMises().getEgBillregister();
        if (billregister.getEgBillregistermis() != null
                && billregister.getEgBillregistermis().getVoucherHeader() != null
                && billregister.getEgBillregistermis().getVoucherHeader().getStatus() != 4) {
            model.addAttribute(ERROR_MESSAGE, messageSource.getMessage("error.arf.voucher.created",
                    new String[] { contractorAdvanceRequisition.getAdvanceRequisitionNumber(),
                            billregister.getEgBillregistermis().getVoucherHeader().getVoucherNumber() },
                    null));
            return CONTRACTORADVANCE_CANCEL_SUCCESS;
        }

        contractorAdvanceRequisition.setCancellationReason(cancellationReason);
        contractorAdvanceRequisition.setCancellationRemarks(cancellationRemarks);

        contractorAdvanceRequisition = contractorAdvanceService.cancelContractorAdvance(contractorAdvanceRequisition);
        model.addAttribute("contractorAdvanceRequisition", contractorAdvanceRequisition);
        model.addAttribute("message", messageSource.getMessage("msg.cancel.arf.success",
                new String[] { contractorAdvanceRequisition.getAdvanceRequisitionNumber(),
                        billregister.getBillnumber() },
                null));
        return CONTRACTORADVANCE_CANCEL_SUCCESS;

    }

    private String checkForAdvanceAdjustment(final ContractorAdvanceRequisition contractorAdvanceRequisition) {
        final List<CChartOfAccounts> contractorAdvanceAccountList = chartOfAccountsHibernateDAO
                .getAccountCodeByPurposeName(WorksConstants.CONTRACTOR_ADVANCE_PURPOSE);
        final StringBuilder glcodeIds = new StringBuilder();
        for (final CChartOfAccounts chartOfAccounts : contractorAdvanceAccountList)
            glcodeIds.append(',').append(chartOfAccounts.getId());
        final BigDecimal glCodes = new BigDecimal(glcodeIds.toString().replaceFirst(",", ""));
        final List<String> billNumbers = contractorBillRegisterService
                .getBillNumberToCancelAdvanceReqisition(contractorAdvanceRequisition.getWorkOrderEstimate().getId(), glCodes);
        final StringBuilder existingBillNumbers = new StringBuilder();
        for (final String details : billNumbers)
            existingBillNumbers.append(details).append(',');
        return existingBillNumbers.toString();

    }

}
