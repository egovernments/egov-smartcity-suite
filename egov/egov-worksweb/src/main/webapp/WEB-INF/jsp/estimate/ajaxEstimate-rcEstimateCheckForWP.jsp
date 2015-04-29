<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"Value":"<s:property value="%{isRCEstimate}" />",
     "estimateNum":"<s:property value="%{estimateNum}" />",
     "estimateIds":"<s:property value="%{estimateIds}" />"}
    ]
  }
}