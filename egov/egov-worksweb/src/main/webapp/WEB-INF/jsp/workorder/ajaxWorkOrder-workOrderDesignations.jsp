<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %> 
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="workOrderDesigList" status="status">
    {"Text":"<s:property value="%{designationName}" />",
    "Value":"<s:property value="%{designationId}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}
