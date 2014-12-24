<%@page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="scheduleOfRateList" status="status">  
    {"key":"<s:property value="%{id}" />",
    "value":"<s:property value="%{summary}" />(<s:property value="%{code}" />) under '<s:property value="%{category.code}" />'->'<s:property value="%{category.parent.description}" />'"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}