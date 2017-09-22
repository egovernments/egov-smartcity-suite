/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.egov.dcb.bean.ChequePayment;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.restapi.model.BudgetCheck;
import org.egov.restapi.model.RestErrors;
import org.egov.restapi.service.BudgetCheckService;
import org.egov.restapi.util.JsonConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;

@RestController
public class RestBudgetController {

    @Autowired
    private BudgetCheckService budgetCheckService;

    /**
     * API to get Available budget amount
     * @throws IOException Returns the planning budget available
     *
     */
    @SuppressWarnings("null")
    @RequestMapping(value = "/egf/budget/planningbudgetavailable", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getBudgetAvailable(@RequestBody final String requestJson,
            final HttpServletResponse response) throws IOException {
        String planningBudgetAvailable;
        final BudgetCheck budgetCheck = (BudgetCheck) getObjectFromJSONRequest(requestJson,
                BudgetCheck.class);

        try {
            final List<RestErrors> restErrors = budgetCheckService.validateMandatoryFields(budgetCheck);
            if (!restErrors.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return JsonConvertor.convert(restErrors);
            }
            planningBudgetAvailable = budgetCheckService.getPlanningBudgetAvailable(budgetCheck);

        } catch (final ValidationException v) {
            final List<RestErrors> errorList = new ArrayList<>(0);
            final RestErrors re = new RestErrors();
            re.setErrorCode(v.getErrors().get(0).getMessage());
            re.setErrorMessage(v.getErrors().get(0).getMessage());
            errorList.add(re);
            return JsonConvertor.convert(errorList);
        }
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("AvailableBalance", planningBudgetAvailable);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return jsonObject.toString();
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