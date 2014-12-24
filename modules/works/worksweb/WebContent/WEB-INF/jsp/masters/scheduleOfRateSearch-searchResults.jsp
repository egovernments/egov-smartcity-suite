<%@page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="scheduleOfRateList" status="status">  
    {"key":"<s:property value="id" />",
    "value":"<s:property value="code" /> : <s:property value="summaryJSON" escape="false" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}
