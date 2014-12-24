<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"Designation":"<s:property value="%{assignment.desigId.designationName}" />"
    }
    ]
  }
  
  <% System.out.println("designationNegotiation"); %>
} 