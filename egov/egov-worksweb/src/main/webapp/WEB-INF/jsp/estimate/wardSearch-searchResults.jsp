<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="boundaryList" status="status">  
    {"key":"<s:property value="%{id}" />",
    "value":"<s:property value="%{name}" /><s:if test="%{parent!=null}">(<s:property value="%{parent.name}" />)</s:if>"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}