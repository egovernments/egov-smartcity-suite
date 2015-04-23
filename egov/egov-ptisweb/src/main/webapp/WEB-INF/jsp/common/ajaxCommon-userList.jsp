<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>   
{
"ResultSet": {
    "Result":[
	<s:iterator var="s" value="userList" status="status">  
    {"Value":"<s:property value="%{id}" />",
    "Text":"<s:property value="%{userName}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}
