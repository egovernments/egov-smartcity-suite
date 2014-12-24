<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"type":"<s:property value="%{type}" />",
    "number":"<s:property value="%{number}" />"}   
    ]
  }
}