package org.egov.eis.web.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.eis.contract.EmployeeDetailsResponse;
import org.egov.eis.contract.EmployeeRequest;
import org.egov.eis.contract.EmployeeResponse;
import org.egov.eis.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/employeepositions")
public class EmployeePositionController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/_search", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    String checkPositionExistsInWF(@RequestBody final EmployeeRequest employeeRequest,
                                   @RequestParam final String tenantId,
                                   final HttpServletResponse response) throws Exception {

        //  List<ErrorDetail> errorList = new ArrayList<>(0);
        //  final ErrorDetail re = new ErrorDetail();
        final EmployeeDetailsResponse employeeDetailsResponse = new EmployeeDetailsResponse();
        final EmployeeResponse employeeResponse = new EmployeeResponse();
        if (employeeService.isPositionExistsInWF(employeeRequest.getPositionName(), employeeRequest.getIsPositionChanged(), employeeRequest.getFromDate(), employeeRequest.getToDate())) {

            employeeResponse.setCode(employeeRequest.getCode());
            employeeResponse.setFromDate(employeeRequest.getFromDate());
            employeeResponse.setToDate(employeeRequest.getToDate());
            employeeResponse.setPositionName(employeeRequest.getPositionName());
            employeeDetailsResponse.setEmployeeResponse(employeeResponse);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        return getJSONResponse(employeeDetailsResponse);
    }

    private String getJSONResponse(final Object obj) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
        final String jsonResponse = objectMapper.writeValueAsString(obj);
        return jsonResponse;
    }
}
