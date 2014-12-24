<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"workOrderNumber":"<s:property value="%{workOrderNumber}" />"}  
    ]
  }
}