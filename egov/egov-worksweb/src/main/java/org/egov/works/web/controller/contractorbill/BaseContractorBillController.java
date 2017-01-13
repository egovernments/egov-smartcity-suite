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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.model.bills.EgBilldetails;
import org.egov.works.contractoradvance.entity.ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus;
import org.egov.works.contractoradvance.service.ContractorAdvanceService;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

@Controller
public abstract class BaseContractorBillController extends GenericWorkFlowController {

    @Autowired
    private ChartOfAccountsService chartOfAccountsService;

    @Autowired
    private ContractorAdvanceService contractorAdvanceService;

    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;

    protected void validateContractorAdvanceDetails(final ContractorBillRegister contractorBillRegister,
            final WorkOrderEstimate workOrderEstimate, final BindingResult resultBinder) {
        final List<CChartOfAccounts> contractorAdvanceAccountCodes = chartOfAccountsService
                .getAccountCodeByPurposeName(WorksConstants.CONTRACTOR_ADVANCE_PURPOSE);
        final List<String> errorArgs = new ArrayList<>();
        final Double advancePaidSoFar = contractorAdvanceService.getTotalAdvanceBillsPaid(
                workOrderEstimate.getId(),
                ContractorAdvanceRequisitionStatus.APPROVED.toString());
        final Double advanceAdjustedSoFar = contractorBillRegisterService.getAdvanceAdjustedSoFar(
                workOrderEstimate.getId(),
                contractorBillRegister.getId(), contractorAdvanceAccountCodes);
        for (final EgBilldetails billdetails : contractorBillRegister.getAdvanceAdjustmentDetails())
            if (billdetails.getGlcodeid() != null && billdetails.getCreditamount() != null
                    && contractorAdvanceAccountCodes != null
                    && contractorAdvanceAccountCodes
                            .contains(chartOfAccountsService.findById(billdetails.getGlcodeid().longValue(), false))) {
                if (billdetails.getCreditamount().compareTo(BigDecimal.valueOf(advancePaidSoFar)) > 0)
                    resultBinder.reject("error.advance.greater.paid", "error.advance.greater.paid");
                if (billdetails.getCreditamount().compareTo(contractorBillRegister.getBillamount()) > 0)
                    resultBinder.reject("error.advance.greater.billamount", "error.advance.greater.billamount");
                if (billdetails.getCreditamount().add(BigDecimal.valueOf(advanceAdjustedSoFar))
                        .compareTo(BigDecimal.valueOf(advancePaidSoFar)) > 0)
                    resultBinder.reject("error.adjusted.greater.paid", "error.adjusted.greater.paid");
                if (contractorBillRegister.getBilltype().equalsIgnoreCase(WorksConstants.FINAL_BILL) && advancePaidSoFar > 0
                        && billdetails.getCreditamount().add(BigDecimal.valueOf(advanceAdjustedSoFar))
                                .compareTo(BigDecimal.valueOf(advancePaidSoFar)) != 0) {
                    final Double balance = advancePaidSoFar - advanceAdjustedSoFar;
                    errorArgs.add(advancePaidSoFar.toString());
                    errorArgs.add(advanceAdjustedSoFar.toString());
                    errorArgs.add(balance.toString());
                    resultBinder.reject("error.finalbill.adjust.remaining", errorArgs.toArray(),
                            "error.finalbill.adjust.remaining");
                }
            }
        if (contractorBillRegister.getBilltype().equalsIgnoreCase(WorksConstants.FINAL_BILL) && advancePaidSoFar > 0
                && advanceAdjustedSoFar == 0) {
            errorArgs.add(advancePaidSoFar.toString());
            resultBinder.reject("error.finalbill.adjusted.zero", errorArgs.toArray(),
                    "error.finalbill.adjusted.zero");
        }
    }
}