package org.egov.eis.web.adaptor;

import java.lang.reflect.Type;

import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.infra.utils.DateUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class EmployeeAssignmentSearchJson implements JsonSerializer<Employee> {

    @Override
    public JsonElement serialize(final Employee employee, final Type typeOfSrc,
            final JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        int maxTempAssignments = 0;
        if (employee != null) {
            jsonObject.addProperty("employeeCode", employee.getCode());
            jsonObject.addProperty("employeeName", employee.getName());
            int i = 0;
            for (final Assignment assignment : employee.getAssignments()) {
                if (assignment.getPrimary()) {
                    jsonObject.addProperty("primaryDepartment", assignment.getDepartment().getName());
                    jsonObject.addProperty("primaryDesignation", assignment.getDesignation().getName());
                    jsonObject.addProperty("primaryPosition", assignment.getPosition().getName());
                    jsonObject.addProperty("primaryDateRange",
                            DateUtils.getDefaultFormattedDate(assignment.getFromDate()) + " - " + DateUtils.getDefaultFormattedDate((assignment.getToDate())));
                } else {
                    jsonObject.addProperty("temporaryDepartment_"+ i, assignment.getDepartment() != null ? assignment.getDepartment().getName() : "");
                    jsonObject.addProperty("temporaryDesignation_" + i, assignment.getDesignation() != null ? assignment.getDesignation().getName() : "");
                    jsonObject.addProperty("temporaryPosition_" + i , assignment.getPosition() != null ? assignment.getPosition().getName()  : "");
                    jsonObject.addProperty("temporaryDateRange_"+ i , assignment.getFromDate() != null && assignment.getToDate() != null ?
                            (DateUtils.getDefaultFormattedDate(assignment.getFromDate()) + " - " + DateUtils.getDefaultFormattedDate(assignment.getToDate())) : "" );
                    i++;
                }  
            }
            if(employee.getAssignments().size()  >= maxTempAssignments)
                maxTempAssignments = employee.getAssignments().size();
        }
        jsonObject.addProperty("tempPositions", maxTempAssignments);
        return jsonObject;
    }

}
