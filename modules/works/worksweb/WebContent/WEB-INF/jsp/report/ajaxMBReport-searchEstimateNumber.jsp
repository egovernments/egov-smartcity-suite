<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>   

{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="estimateList" status="status">  
    {"key":"<s:property value="top" />",
    "value":"<s:property value="top" />" 
    }<s:if test="!#status.last">,</s:if> 
    </s:iterator>       
    ]
  }
}