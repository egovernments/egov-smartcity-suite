/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.restapi.web.rest;

import static java.lang.String.format;
import static org.egov.restapi.constants.RestApiConstants.JSON_CONVERSION_ERROR_CODE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.restapi.model.ETransactionResponse;
import org.egov.restapi.service.ETransactionService;
import org.egov.restapi.util.JsonConvertor;
import org.egov.restapi.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * Controller to Publish(Read-only) electronic transaction statistics
 */
@RestController
public class RestETransactionController {

    public static final String API_STATISTICS = "/public/transaction/statistics";
    public static final String PARAM_FROM_DATE = "fromDate";
    public static final String PARAM_TO_DATE = "toDate";
    private static final Logger LOGGER = Logger.getLogger(RestETransactionController.class);

    @Autowired
    ETransactionService eTransactionService;

    @Autowired
    ValidationUtil validationUtil;

    /**
     * Formats a JSON representing the Errors
     *
     * @param ve A ValidationException containing list of ValidationError
     * @return String representing the list of ValidationError in Json
     */
    private String makeValidationErrorJson(ValidationException ve) {
        JsonObject resultObject = new JsonObject();
        JsonObject errorObject = new JsonObject();
        List<ValidationError> errorList = ve.getErrors();
        JsonArray validationErrorList = new JsonArray(errorList.size());
        for (ValidationError validationError : errorList) {
            JsonObject jsonError = new JsonObject();
            jsonError.addProperty("key", validationError.getKey());
            jsonError.addProperty("message", validationError.getMessage());
            validationErrorList.add(jsonError);
        }
        errorObject.addProperty("type", "validation_error");
        errorObject.add("details", validationErrorList);
        resultObject.addProperty("message", "Request Validation Error Occurred.");
        resultObject.add("error", errorObject);
        return resultObject.toString();
    }

    /**
     * Parses Input JSON and return a date range as {@code Pair<Date, Date>}
     * @param requestJson JSON string containing PARAM_FROM_DATE & PARAM_TO_DATE keys
     * @return pair/tuple of (fromDate, toDate)
     */
    private Pair<Date, Date> parseRequestAndGetDateRange(String requestJson) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(requestJson);
        if (!jsonElement.isJsonObject()) {
            throw new ValidationException(new ValidationError(JSON_CONVERSION_ERROR_CODE, "Expected a a Json Object."));
        }
        JsonObject request = jsonElement.getAsJsonObject();
        if (!request.has(PARAM_FROM_DATE) || !request.has(PARAM_TO_DATE)) {
            throw new ValidationException(
                    new ValidationError(
                            JSON_CONVERSION_ERROR_CODE,
                            format("Expected json object key %s & %s", PARAM_FROM_DATE, PARAM_TO_DATE)));
        }
        try {
            Date fromDate = validationUtil.convertStringToDate(request.get(PARAM_FROM_DATE).getAsString());
            Date toDate = validationUtil.convertStringToDate(request.get(PARAM_TO_DATE).getAsString());
            return Pair.of(fromDate, toDate);
        } catch (ParseException | AssertionError ex) {

            String validationMessage = format("Expected date in %s format",
                    ValidationUtil.DATE_FORMAT.toPattern());

            LOGGER.warn(format("%s: %s; requestJson: %s", "INVALID_DATE", validationMessage, requestJson));

            throw new ValidationException(
                    new ValidationError(
                            "INVALID_DATE",
                            validationMessage));
        }
    }

    @RequestMapping(value = API_STATISTICS, method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getETransactionStatistics(@RequestBody final String requestJson,
            final HttpServletResponse response) {

        List<ETransactionResponse> tnxInfoList;
        try {

            Pair<Date, Date> fromToDatePair = parseRequestAndGetDateRange(requestJson);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(format("parsed dates; from: %s, to: %s", fromToDatePair.getFirst(), fromToDatePair.getSecond()));
            }

            tnxInfoList = eTransactionService.getETransactionCount(fromToDatePair.getFirst(), fromToDatePair.getSecond());

        } catch (JsonSyntaxException syntaxEx) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.warn(format("JSON Syntax Error: %s;\nrequestJson: %s", API_STATISTICS, requestJson));
            }
            return makeValidationErrorJson(
                    new ValidationException(new ValidationError(JSON_CONVERSION_ERROR_CODE, "Invalid JSON")));
        } catch (ValidationException validationEx) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.warn(format("ValidationException: %s,\nrequestJson: %s", API_STATISTICS, requestJson));
            }
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return makeValidationErrorJson(validationEx);
        }

        return JsonConvertor.convert(tnxInfoList);
    }
}