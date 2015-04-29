<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"Value":"<s:property value="%{yearEndApprCheck}" />",
    "woId":"<s:property value="%{workOrderId}" />",
    "estNo":"<s:property value="%{estimateNo}" />"}
    ]
  }
}