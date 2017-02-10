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
package org.egov.works.web.controller.masters;

import org.egov.commons.ContractorGrade;
import org.egov.infra.validation.regex.Constants;
import org.egov.works.masters.service.ContractorGradeService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

@Controller
public abstract class BaseContractorClassController {

    protected static final String CONTRACTORGRADE = "contractorGrade";

    @Autowired
    private ContractorGradeService contractorGradeService;

    protected void validateContractorClass(final ContractorGrade contractorGrade, final BindingResult resultBinder,
            final String mode) {
        checkExistingContractorClass(contractorGrade, resultBinder, mode);
        checkContractorClass(contractorGrade, resultBinder);

        if (contractorGrade.getDescription() == null)
            resultBinder.reject("error.contractorclass.description", "error.contractorclass.description");

        checkMinAmount(contractorGrade, resultBinder);

        checkMaxAmount(contractorGrade, resultBinder);

        if (contractorGrade.getMinAmount() != null && contractorGrade.getMaxAmount() != null
                && contractorGrade.getMinAmount().compareTo(contractorGrade.getMaxAmount()) == 1)
            resultBinder.reject("error.contractorclass.maxamount.invalid", "error.contractorclass.maxamount.invalid");

        if (!WorksConstants.EDIT.equalsIgnoreCase(mode))
            checkMinAndMaxAmountCombination(contractorGrade, resultBinder);

    }

    private void checkMinAndMaxAmountCombination(final ContractorGrade contractorGrade,
            final BindingResult resultBinder) {
        if (contractorGradeService.getByMinAndMaxAmount(contractorGrade.getMinAmount(),
                contractorGrade.getMaxAmount()) != null)
            resultBinder.reject("error.contractorclass.amount.exist", "error.contractorclass.amount.exist");
    }

    private void checkMaxAmount(final ContractorGrade contractorGrade, final BindingResult resultBinder) {
        if (contractorGrade.getMinAmount() == null || contractorGrade.getMinAmount().signum() == -1)
            resultBinder.reject("error.contractorclass.maxAmount.valid", "error.contractorclass.maxAmount.valid");
    }

    private void checkMinAmount(final ContractorGrade contractorGrade, final BindingResult resultBinder) {
        if (contractorGrade.getMinAmount() == null || contractorGrade.getMinAmount().signum() == -1)
            resultBinder.reject("error.contractorclass.minAmount.valid", "error.contractorclass.minAmount.valid");
    }

    private void checkContractorClass(final ContractorGrade contractorGrade, final BindingResult resultBinder) {
        if (contractorGrade.getGrade() == null)
            resultBinder.reject("error.contractorclass.grade.null", "error.contractorclass.grade.null");

        if (contractorGrade.getGrade() != null
                && !contractorGrade.getGrade().matches(Constants.ALPHABETSWITHHYPHENSLASHSPACE))
            resultBinder.reject("error.contractorclass.alphaNumeric", "error.contractorclass.alphaNumeric");
    }

    private void checkExistingContractorClass(final ContractorGrade contractorGrade, final BindingResult resultBinder,
            final String mode) {
        if (!WorksConstants.EDIT.equalsIgnoreCase(mode)
                && contractorGradeService.getByContractorClass(contractorGrade.getGrade()) != null)
            resultBinder.reject("error.contractorclass.grade.isunique", "error.contractorclass.grade.isunique");
        if (WorksConstants.EDIT.equalsIgnoreCase(mode)) {
            final ContractorGrade existingContactorGrade = contractorGradeService
                    .getByContractorClass(contractorGrade.getGrade());
            if (existingContactorGrade != null && !existingContactorGrade.getId().equals(contractorGrade.getId()))
                resultBinder.reject("error.contractorclass.exists", "error.contractorclass.exists");
            final ContractorGrade cg = contractorGradeService.getByMinAndMaxAmount(contractorGrade.getMinAmount(),
                    contractorGrade.getMaxAmount());
            if (cg != null && !cg.getId().equals(contractorGrade.getId()))
                checkMinAndMaxAmountCombination(contractorGrade, resultBinder);
        }
    }
}
