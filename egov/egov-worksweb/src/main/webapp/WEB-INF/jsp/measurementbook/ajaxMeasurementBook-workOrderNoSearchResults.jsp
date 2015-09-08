<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="workOrderList" status="status">  
    {"key":"<s:property value="%{id}" />",
    "value":"<s:property value="%{workOrderNumber}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}
