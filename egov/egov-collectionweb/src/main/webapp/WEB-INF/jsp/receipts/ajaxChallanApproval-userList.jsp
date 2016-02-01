<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
	<s:iterator var="s" value="postionUserList" status="status">  
    {"Value":"<s:property value="%{position.id}" />",
    "Text":"<s:property value="%{name}" />-<s:property value="%{position.name}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}
