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
package org.egov.restapi.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.service.FunctionService;
import org.egov.commons.service.FundService;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.dcb.bean.ChequePayment;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.model.BudgetCheackHelper;
import org.egov.restapi.model.RestErrors;
import org.egov.restapi.util.JsonConvertor;
import org.egov.services.budget.BudgetGroupService;
import org.egov.services.masters.SchemeService;
import org.egov.services.masters.SubSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;

@RestController
public class RestBudgetController {

    @Autowired
    private BudgetDetailsDAO budgetDetailsDAO;

    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;

    @Autowired
    private FundService fundService;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private SubSchemeService subSchemeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private BudgetGroupService budgetGroupService;

    /**
     * API to get Available budget amount
     * @throws IOException Returns the planning budget available
     *
     */
    @SuppressWarnings("null")
    @RequestMapping(value = "/egf/budget/budgetavailable", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getBudgetAvailable(@RequestBody final String requestJson,
            final HttpServletResponse response) throws IOException {
        BigDecimal budgetAvailable;
        ApplicationThreadLocals.setUserId(2L);
        final BudgetCheackHelper budgetCheackHelper = (BudgetCheackHelper) getObjectFromJSONRequest(requestJson,
                BudgetCheackHelper.class);

        try {
            final List<RestErrors> restErrors = validateMandatoryFields(budgetCheackHelper);
            if (!restErrors.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return JsonConvertor.convert(restErrors);
            }
            final Scheme scheme = schemeService.findByCode(budgetCheackHelper.getSchemeCode());
            final SubScheme subScheme = subSchemeService.findByCode(budgetCheackHelper.getSubSchemeCode());

            final List<Long> budgetheadid = new ArrayList<>();
            budgetheadid.add(budgetGroupService.getBudgetGroupByName(budgetCheackHelper.getBudgetHeadName()).getId());

            budgetAvailable = budgetDetailsDAO.getPlanningBudgetAvailable(
                    financialYearHibernateDAO.getFinYearByDate(new Date()).getId(),
                    Integer.parseInt(
                            departmentService.getDepartmentByCode(budgetCheackHelper.getDepartmentCode()).getId().toString()),
                    functionService.findByCode(budgetCheackHelper.getFunctionCode()).getId(), null,
                    scheme == null ? null
                            : Integer.parseInt(
                                    scheme.getId().toString()),
                    subScheme == null ? null
                            : Integer.parseInt(
                                    subScheme.getId().toString()),
                    null, budgetheadid,
                    Integer.parseInt(fundService.findByCode(budgetCheackHelper.getFundCode()).getId().toString()));

        } catch (final ValidationException v) {
            final List<RestErrors> errorList = new ArrayList<>(0);
            final RestErrors re = new RestErrors();
            re.setErrorCode(v.getErrors().get(0).getMessage());
            re.setErrorMessage(v.getErrors().get(0).getMessage());
            errorList.add(re);
            return JsonConvertor.convert(errorList);
        }
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("AvailableBalance", budgetAvailable.toString());
        response.setStatus(HttpServletResponse.SC_CREATED);
        return jsonObject.toString();
    }

    private List<RestErrors> validateMandatoryFields(final BudgetCheackHelper budgetCheackHelper) {
        RestErrors restErrors;
        final List<RestErrors> errors = new ArrayList<>();
        if (StringUtils.isBlank(budgetCheackHelper.getDepartmentCode())
                || departmentService.getDepartmentByCode(budgetCheackHelper.getDepartmentCode()) == null) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_DEPARTMENT);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_DEPARTMENT);
            errors.add(restErrors);
        }
        if (StringUtils.isBlank(budgetCheackHelper.getFunctionCode())
                || functionService.findByCode(budgetCheackHelper.getFunctionCode()) == null) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_FUNCTION);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_FUNCTION);
            errors.add(restErrors);
        }
        if (StringUtils.isBlank(budgetCheackHelper.getFundCode())
                || fundService.findByCode(budgetCheackHelper.getFundCode()) == null) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_FUND);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_FUND);
            errors.add(restErrors);
        }
        if (StringUtils.isBlank(budgetCheackHelper.getBudgetHeadName())
                || budgetGroupService.getBudgetGroupByName(budgetCheackHelper.getBudgetHeadName()) == null) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_BUDGETHEAD);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_BUDGETHEAD);
            errors.add(restErrors);
        }
        return errors;
    }

    @SuppressWarnings("unchecked")
    private Object getObjectFromJSONRequest(final String jsonString, final Class cls)
            throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, true);
        mapper.setDateFormat(ChequePayment.CHEQUE_DATE_FORMAT);
        return mapper.readValue(jsonString, cls);
    }
}