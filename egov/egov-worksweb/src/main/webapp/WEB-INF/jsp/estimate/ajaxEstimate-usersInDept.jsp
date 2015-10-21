<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="usersInExecutingDepartment" status="status">  
    {"Text":"<s:property value="%{name}" />",
    "Value":"<s:property value="%{id}" />",
    "Designation":"<s:property value="%{designation.name}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}
