<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="allUsers" status="status">  
    {"key":"<s:property value="%{userName}" />",
    "value":"<s:property value="%{userName}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}