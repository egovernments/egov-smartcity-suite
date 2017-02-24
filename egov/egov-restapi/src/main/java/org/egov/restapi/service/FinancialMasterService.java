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
package org.egov.restapi.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.dao.ChartOfAccountsDAO;
import org.egov.commons.service.AccountPurposeService;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.commons.service.FunctionService;
import org.egov.commons.service.FundService;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.service.BudgetingGroupService;
import org.egov.restapi.model.BudgetGroupHelper;
import org.egov.restapi.model.ChartOfAccountHelper;
import org.egov.restapi.model.FunctionHelper;
import org.egov.restapi.model.FundHelper;
import org.egov.restapi.model.SchemeHelper;
import org.egov.restapi.model.SubSchemeHelper;
import org.egov.services.masters.SchemeService;
import org.egov.services.masters.SubSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FinancialMasterService {

    @Autowired
    private FundService fundService;

    @Autowired
    private SubSchemeService subSchemeService;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private ChartOfAccountsService chartOfAccountsService;

    @Autowired
    private AccountPurposeService accountPurposeService;

    @Autowired
    private BudgetingGroupService budgetingGroupService;

    @Autowired
    private ChartOfAccountsDAO chartOfAccountsDAO;

    public List<FunctionHelper> populateFunction() {
        final List<CFunction> cFunctions = functionService.findAllActive();

        final List<FunctionHelper> functionHelpers = new ArrayList<>();
        for (final CFunction cFunction : cFunctions)
            createFunctionHelper(functionHelpers, cFunction);
        return functionHelpers;
    }

    private void createFunctionHelper(final List<FunctionHelper> functionHelpers, final CFunction cFunction) {
        final FunctionHelper functionHelper = new FunctionHelper();
        functionHelper.setCode(cFunction.getCode());
        functionHelper.setName(cFunction.getName());
        functionHelpers.add(functionHelper);
    }

    public List<SchemeHelper> populateScheme() {
        final List<Scheme> schemes = schemeService.getByIsActive();
        final List<SchemeHelper> schemeHelpers = new ArrayList<>();

        for (final Scheme scheme : schemes)
            createSchemeHelper(schemeHelpers, scheme);
        return schemeHelpers;
    }

    private void createSchemeHelper(final List<SchemeHelper> schemeHelpers, final Scheme scheme) {
        final SchemeHelper schemeHelper = new SchemeHelper();
        schemeHelper.setCode(scheme.getCode());
        schemeHelper.setName(scheme.getName());
        schemeHelper.setDescription(scheme.getDescription());
        schemeHelper.setValidFrom(scheme.getValidfrom());
        schemeHelper.setValidTo(scheme.getValidto());
        schemeHelper.setFund(scheme.getFund().getName());
        schemeHelpers.add(schemeHelper);
    }

    public List<SubSchemeHelper> populateSubScheme() {

        final List<SubScheme> subSchemes = subSchemeService.getByIsActive();
        final List<SubSchemeHelper> subSchemeHelpers = new ArrayList<>();

        for (final SubScheme subScheme : subSchemes)
            createSubSchemeHelper(subSchemeHelpers, subScheme);
        return subSchemeHelpers;
    }

    private void createSubSchemeHelper(final List<SubSchemeHelper> subSchemeHelpers, final SubScheme subScheme) {
        final SubSchemeHelper subSchemeHelper = new SubSchemeHelper();
        subSchemeHelper.setCode(subScheme.getCode());
        subSchemeHelper.setName(subScheme.getName());
        subSchemeHelper.setScheme(subScheme.getScheme().getName());
        subSchemeHelpers.add(subSchemeHelper);
    }

    public List<FundHelper> populateFund() {
        final List<Fund> funds = fundService.findAllActiveAndIsnotleaf();

        final List<FundHelper> fundHelpers = new ArrayList<>();
        for (final Fund fund : funds)
            createFundHelper(fundHelpers, fund);
        return fundHelpers;
    }

    private void createFundHelper(final List<FundHelper> fundHelpers, final Fund fund) {
        final FundHelper fundHelper = new FundHelper();
        fundHelper.setCode(fund.getCode());
        fundHelper.setName(fund.getName());
        fundHelpers.add(fundHelper);
    }

    public List<ChartOfAccountHelper> populateChartOfAccounts() {
        final List<CChartOfAccounts> cChartOfAccounts = chartOfAccountsService.getAllAccountCodesByIsactiveAndClassification();

        final List<ChartOfAccountHelper> chartOfAccountHelpers = new ArrayList<>();

        for (final CChartOfAccounts cChartOfAccount : cChartOfAccounts)
            createChartOfAccountHelper(chartOfAccountHelpers, cChartOfAccount);
        return chartOfAccountHelpers;
    }

    private void createChartOfAccountHelper(final List<ChartOfAccountHelper> chartOfAccountHelpers,
            final CChartOfAccounts cChartOfAccount) {
        final ChartOfAccountHelper chartOfAccountHelper = new ChartOfAccountHelper();
        chartOfAccountHelper.setGlCode(cChartOfAccount.getGlcode());
        chartOfAccountHelper.setName(cChartOfAccount.getName());
        if (cChartOfAccount.getPurposeId() != null)
            chartOfAccountHelper.setPurpose(accountPurposeService.getByPurposeId(cChartOfAccount.getPurposeId()).getName());
        else
            chartOfAccountHelper.setPurpose(StringUtils.EMPTY);
        chartOfAccountHelper.setType(cChartOfAccount.getType());
        if (cChartOfAccount.getBudgetCheckReq() != null)
            chartOfAccountHelper.setBudgetCheckReqired(cChartOfAccount.getBudgetCheckReq());
        else
            chartOfAccountHelper.setBudgetCheckReqired(Boolean.FALSE);
        if (cChartOfAccount.getFunctionReqd() != null)
            chartOfAccountHelper.setFunctionReqired(cChartOfAccount.getFunctionReqd());
        else
            chartOfAccountHelper.setFunctionReqired(Boolean.FALSE);

        chartOfAccountHelper.initializeArray(cChartOfAccount.getChartOfAccountDetails().size());
        int i = 0;
        for (final CChartOfAccountDetail accountDetail : cChartOfAccount.getChartOfAccountDetails()) {
            chartOfAccountHelper.addSubledger(accountDetail.getDetailTypeId().getName(), i);
            i++;
        }
        chartOfAccountHelpers.add(chartOfAccountHelper);
    }

    public List<BudgetGroupHelper> populateBudgetGroup() throws Exception {
        final List<BudgetGroup> budgetGroups = budgetingGroupService.getActiveBudgetGroups();

        final List<BudgetGroupHelper> budgetGroupHelpers = new ArrayList<>();

        for (final BudgetGroup budgetGroup : budgetGroups)
            createBudgetGroupHelper(budgetGroupHelpers, budgetGroup);
        return budgetGroupHelpers;
    }

    private void createBudgetGroupHelper(final List<BudgetGroupHelper> budgetGroupHelpers, final BudgetGroup budgetGroup)
            throws Exception {
        final BudgetGroupHelper budgetGroupHelper = new BudgetGroupHelper();
        if (budgetGroup.getMinCode().equals(budgetGroup.getMaxCode())) {
            budgetGroupHelper.initializeArray(1);
            budgetGroupHelper.addAccountCode(budgetGroup.getMaxCode().getGlcode(), 0);
        } else {
            budgetGroupHelper
                    .initializeArray(chartOfAccountsDAO
                            .getGlcode(budgetGroup.getMinCode().getId().toString(), budgetGroup.getMaxCode().getId().toString(),
                                    budgetGroup.getMajorCode() != null ? budgetGroup.getMajorCode().getId().toString()
                                            : StringUtils.EMPTY)
                            .size());
            budgetGroupHelper
                    .setAccountCode((String[]) chartOfAccountsDAO
                            .getGlcode(budgetGroup.getMinCode().getId().toString(), budgetGroup.getMaxCode().getId().toString(),
                                    budgetGroup.getMajorCode() != null ? budgetGroup.getMajorCode().getId().toString()
                                            : StringUtils.EMPTY)
                            .toArray());
        }
        budgetGroupHelper.setName(budgetGroup.getName());
        budgetGroupHelpers.add(budgetGroupHelper);
    }

}
