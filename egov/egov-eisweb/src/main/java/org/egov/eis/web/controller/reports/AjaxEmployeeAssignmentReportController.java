package org.egov.eis.web.controller.reports;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.entity.Employee;
import org.egov.eis.reports.entity.EmployeeAssignmentSearch;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.web.adaptor.EmployeeAssignmentSearchJson;
import org.egov.infra.web.utils.WebUtils;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/reports")
public class AjaxEmployeeAssignmentReportController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private DesignationService designationService;
    
    @Autowired
    private AssignmentService assignmentService;
    
    @RequestMapping(value = "/searchemployeecodes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> getEmployeeCodes(@RequestParam final String code) {
        List<Employee> employees = employeeService.findEmployeeByCodeLike(code);
        List<String> employeeCodes = new ArrayList<String>();
        for(Employee employee:employees) {
            employeeCodes.add(employee.getCode());
        }
        return employeeCodes;
    }
    
    @RequestMapping(value = "/searchdesignations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Designation> getDesignation(@RequestParam final String name) {
        return designationService.getDesignationsByName(name);
    }
    
    @RequestMapping(value = "/searchpositions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Position> getPositions(@RequestParam final String name) {
        return assignmentService.findPositionsForEmployees(name);
    } 
    
    @RequestMapping(value = "/employeeassignments/search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchEmployeeAssignments(@ModelAttribute final EmployeeAssignmentSearch employeeAssignmentSearch) {
        List<Employee> employeeList = assignmentService.searchEmployeeAssignments(employeeAssignmentSearch);
        final String result = new StringBuilder("{ \"data\":").append(WebUtils.toJSON(employeeList, Employee.class, EmployeeAssignmentSearchJson.class)).append("}").toString();
        return result;
    } 
    
}
