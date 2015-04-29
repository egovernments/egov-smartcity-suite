<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"Value":"<s:property value="%{isRCEstimate}" />",
     "estNo":"<s:property value="%{estNo}" />",
     "estimateId":"<s:property value="%{estimateId}" />",
     "woId":"<s:property value="%{woId}" />"}
    ]
  }
}