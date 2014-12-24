<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>   
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="allChartOfAccounts" status="status">  
    {"key":"<s:property value="%{id}" />",
    "value":"<s:property value="%{glcode}" />~<s:property value="%{name}" />~<s:property value="%{id}" />" 
    }<s:if test="!#status.last">,</s:if> 
    </s:iterator>       
    ]
  }
}