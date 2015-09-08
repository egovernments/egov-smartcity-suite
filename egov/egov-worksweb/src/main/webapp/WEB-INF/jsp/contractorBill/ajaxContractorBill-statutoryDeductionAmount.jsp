<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"xRowId":"<s:property value="%{rowId}" />",
    "statutoryAmount":"<s:property value="%{statutoryAmount}" />",
    "errorMsg":"<s:property value="%{errorMsg}" />"
    }    
    ]
  }
} 
