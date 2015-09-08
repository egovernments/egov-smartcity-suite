<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"Designation":"<s:property value="%{assignment.designation.name}" />"
    }
    ]
  }
  
  <% System.out.println("designation"); %>
} 
