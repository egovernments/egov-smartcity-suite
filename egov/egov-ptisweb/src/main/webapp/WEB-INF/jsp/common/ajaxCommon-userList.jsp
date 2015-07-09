<%@page contentType="text/json" %>
<%@taglib prefix="s" uri="/WEB-INF/taglib/struts-tags.tld" %>  
{
"ResultSet": {
    "Result":[
	<s:iterator var="s" value="assignmentList" status="status">  
    {"Value":"<s:property value="%{employee.id}" />",
    "Text":"<s:property value="%{employee.username}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}