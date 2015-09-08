<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld" %> 
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="positions" status="status">
    {"Text":"<s:property value="%{employeeName+'--'+position.name}" />",
    "Value":"<s:property value="%{position.id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}
