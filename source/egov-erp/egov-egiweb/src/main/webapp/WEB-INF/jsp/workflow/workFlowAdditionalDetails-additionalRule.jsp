<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="additionalRuleList" status="status">  
    {"Id":"<s:property value="%{additionalRule}" />",
    "Description":"<s:property value="%{additionalRule}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }  
}