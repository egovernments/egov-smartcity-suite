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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.restapi.model.ETransactionRequest;
import org.egov.restapi.model.ETransactionResponse;
import org.egov.restapi.service.ETransactionService;
import org.egov.restapi.util.JsonConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Controller to Publish(Read-only) electronic transaction statistics See:
 * {@link #getETransactionStatistics(ETransactionRequest, HttpServletResponse)}
 */
@RestController
@SuppressWarnings("unused")
public class RestETransactionController {

    public static final String API_STATISTICS = "/public/transaction/statistics";
    private static final Logger LOGGER = Logger.getLogger(RestETransactionController.class);

    @Autowired
    ETransactionService eTransactionService;

    /**
     * Formats a JSON representing the Errors
     *
     * @param ve A ValidationException containing list of ValidationError
     * @return String representing the list of ValidationError in JSON
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
            String args[] = validationError.getArgs();
            int args_i = 0;
            if (args.length % 2 == 0) {
                while (args_i < args.length) {
                    jsonError.addProperty(args[args_i++], args[args_i++]);
                }
            }
            validationErrorList.add(jsonError);
        }
        errorObject.addProperty("type", "validation_error");
        errorObject.add("details", validationErrorList);
        resultObject.addProperty("message", "Request Validation Failed.");
        resultObject.add("error", errorObject);
        return resultObject.toString();
    }

    @ExceptionHandler(Throwable.class)
    @SuppressWarnings("unused")
    public ResponseEntity<String> handleAllException(Throwable ex) {
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ValidationException) {
            return bodyBuilder.body(makeValidationErrorJson((ValidationException) ex));
        } else if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException manve = (MethodArgumentNotValidException) ex;
            BindingResult bindingResult = manve.getBindingResult();

            List<ObjectError> objectErrorList = bindingResult.getAllErrors();
            List<ValidationError> veList = new ArrayList<>(objectErrorList.size());
            ValidationException ve = new ValidationException(veList);

            for (ObjectError oe : objectErrorList) {
                if (oe instanceof FieldError) {
                    FieldError fe = (FieldError) oe;
                    veList.add(new ValidationError(fe.getField(), fe.getDefaultMessage(), "code", oe.getCode()));
                } else {
                    veList.add(new ValidationError(oe.getObjectName(), oe.getDefaultMessage(), "code", oe.getCode()));
                }
            }
            return bodyBuilder.body(makeValidationErrorJson(ve));
        } else {
            LOGGER.warn("An unknown exception caught", ex);
            return bodyBuilder.body(
                    "{\"message\": \"An Error Occurred\", \"error\": \"" + ex.getMessage().replaceAll("[\"]", "\\\\\"") + "\"}");
        }
    }

    @RequestMapping(value = API_STATISTICS, method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @SuppressWarnings("unused")
    public String getETransactionStatistics(@Valid @RequestBody final ETransactionRequest request,
            final HttpServletResponse response) {

        Date fromDate = request.getParsedFromDate();
        Date toDate = request.getParsedToDate();
        if(fromDate.equals(toDate)) {
            toDate = DateUtils.endOfDay(toDate);
        }
        return JsonConvertor
                .convert(eTransactionService.getETransactionCount(fromDate, toDate));

    }

}