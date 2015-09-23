<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"errorMsg":"<s:property value="%{errorMsg}" />",
    "isUnique":"<s:property value="%{isUnique}" />",
    "paramType":"<s:property value="%{paramType}" />"
    }<s:if test="!#status.last">,</s:if>    
    ]
  } 
}
