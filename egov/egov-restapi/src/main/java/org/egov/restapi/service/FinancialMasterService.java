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

import org.egov.commons.CFunction;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.service.FunctionService;
import org.egov.commons.service.FundService;
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

}
