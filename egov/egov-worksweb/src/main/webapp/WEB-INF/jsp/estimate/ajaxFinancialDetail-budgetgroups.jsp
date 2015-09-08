<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="budgetGroups" status="status">
    {"Text":"<s:property value="%{name}" escape="false" />",
    "Value":"<s:property value="%{id}" />",
    "Glcode":"<s:property value="%{maxCode.glcode}"/>"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}
