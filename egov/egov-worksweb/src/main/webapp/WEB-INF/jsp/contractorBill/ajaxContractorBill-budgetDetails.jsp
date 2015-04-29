<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"xRowId":"<s:property value="%{rowId}" />",
    "budgAmount":"<s:property value="%{budgAmount}" />",
    "budgBalance":"<s:property value="%{budgBalance}" />",
    "errorMsg":"<s:property value="%{errorMsg}" />",
     "checkBudget":"<s:property value="%{checkBudget}" />"
    }    
    ]
  }
} 
