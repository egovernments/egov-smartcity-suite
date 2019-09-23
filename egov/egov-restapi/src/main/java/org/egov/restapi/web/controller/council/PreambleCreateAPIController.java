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

package org.egov.restapi.web.controller.council;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.egov.council.entity.CouncilPreamble;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.restapi.service.PreambleCreateAPIService;
import org.egov.restapi.web.contracts.councilpreamble.CouncilPreambleRequest;
import org.egov.restapi.web.contracts.councilpreamble.CouncilPreambleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PreambleCreateAPIController {

    @Autowired
    private PreambleCreateAPIValidator preambleCreateAPIValidator;
    @Autowired
    private PreambleCreateAPIService preambleCreateAPIService;

    @PostMapping("/preamble/create")
    public CouncilPreambleResponse createLicense(@Valid @RequestBody CouncilPreambleRequest councilPreambleRequest,
            BindingResult binding) {
        return create(councilPreambleRequest, binding);
    }
    
    @PostMapping("/v1.0/council/preamble/create")
    public CouncilPreambleResponse securedCreateLicense(@Valid @RequestBody CouncilPreambleRequest councilPreambleRequest,
            BindingResult binding) {
        return create(councilPreambleRequest, binding);
    }

    /**
     * @param councilPreambleRequest
     * @param binding
     * @return
     */
    public CouncilPreambleResponse create(CouncilPreambleRequest councilPreambleRequest, BindingResult binding) {
        preambleCreateAPIValidator.validate(councilPreambleRequest, binding);
        if (binding.hasErrors()) {
            List<String> preambleResponses = binding.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return new CouncilPreambleResponse(HttpStatus.BAD_REQUEST.toString(), preambleResponses.toString());
        } else {
            CouncilPreamble councilPreamble = preambleCreateAPIService
                    .createPreamble(councilPreambleRequest);
            return new CouncilPreambleResponse(councilPreamble.getReferenceNumber(), councilPreamble.getPreambleNumber(),
                    HttpStatus.OK.toString(),
                    "Preamble Created Successfully");
        }
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> validationErrors(ValidationException exception) {
        List<String> errors = exception.getErrors().stream().map(ValidationError::getMessage).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CouncilPreambleResponse(HttpStatus.BAD_REQUEST.toString(), errors.toString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> restErrors() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CouncilPreambleResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Server Error"));
    }
    
    @ExceptionHandler(ApplicationRuntimeException.class)
    public ResponseEntity<Object> configurationErrors(ApplicationRuntimeException exception) 
    {
        String error[] = exception.getMessage().split(",");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CouncilPreambleResponse(error[0], "", HttpStatus.INTERNAL_SERVER_ERROR.toString(), error[1]));
    }
    
    
}
