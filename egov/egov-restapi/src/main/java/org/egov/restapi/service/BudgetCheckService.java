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
package org.egov.restapi.service;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.service.FunctionService;
import org.egov.commons.service.FundService;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.model.BudgetCheck;
import org.egov.restapi.model.RestErrors;
import org.egov.services.budget.BudgetGroupService;
import org.egov.services.masters.SchemeService;
import org.egov.services.masters.SubSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BudgetCheckService {

    @Autowired
    private FundService fundService;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private SubSchemeService subSchemeService;

    @Autowired
    private BudgetGroupService budgetGroupService;

    @Autowired
    private BudgetDetailsDAO budgetDetailsDAO;

    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;

    @Autowired
    private DepartmentService departmentService;

    public List<RestErrors> validateMandatoryFields(final BudgetCheck budgetCheck) {
        RestErrors restErrors;
        final List<RestErrors> errors = new ArrayList<>();
        if (StringUtils.isBlank(budgetCheck.getDepartmentCode())
                || departmentService.getDepartmentByCode(budgetCheck.getDepartmentCode()) == null) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_DEPARTMENT);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_DEPARTMENT);
            errors.add(restErrors);
        }
        if (StringUtils.isBlank(budgetCheck.getFunctionCode())
                || functionService.findByCode(budgetCheck.getFunctionCode()) == null) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_FUNCTION);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_FUNCTION);
            errors.add(restErrors);
        }
        if (StringUtils.isBlank(budgetCheck.getFundCode())
                || fundService.findByCode(budgetCheck.getFundCode()) == null) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_FUND);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_FUND);
            errors.add(restErrors);
        }
        if (StringUtils.isBlank(budgetCheck.getBudgetHeadName())
                || budgetGroupService.getBudgetGroupByName(budgetCheck.getBudgetHeadName()) == null) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_BUDGETHEAD);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_BUDGETHEAD);
            errors.add(restErrors);
        }
        return errors;
    }

    public String getPlanningBudgetAvailable(final BudgetCheck budgetCheck) {
        BigDecimal budgetAvailable;
        final Scheme scheme = schemeService.findByCode(budgetCheck.getSchemeCode());
        final SubScheme subScheme = subSchemeService.findByCode(budgetCheck.getSubSchemeCode());

        final List<Long> budgetheadid = new ArrayList<>();
        budgetheadid.add(budgetGroupService.getBudgetGroupByName(budgetCheck.getBudgetHeadName()).getId());

        budgetAvailable = budgetDetailsDAO.getPlanningBudgetAvailable(
                financialYearHibernateDAO.getFinYearByDate(new Date()).getId(),
                Integer.parseInt(
                        departmentService.getDepartmentByCode(budgetCheck.getDepartmentCode()).getId().toString()),
                functionService.findByCode(budgetCheck.getFunctionCode()).getId(), null,
                scheme == null ? null
                        : Integer.parseInt(
                                scheme.getId().toString()),
                subScheme == null ? null
                        : Integer.parseInt(
                                subScheme.getId().toString()),
                null, budgetheadid,
                Integer.parseInt(fundService.findByCode(budgetCheck.getFundCode()).getId().toString()));

        return budgetAvailable.toString();
    }

}
