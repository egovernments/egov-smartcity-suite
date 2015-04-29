<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"Value":"<s:property value="%{yearEndApprCheck}" />",
     "estNo":"<s:property value="%{estimateNo}" />"}
    ]
  }
}