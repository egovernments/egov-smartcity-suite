<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="designationList" status="status">  
    {"key":"<s:property value="%{designationId}" />",
    "value":"<s:property value="%{designationName}"/>~<s:property value="%{designationDescription}"/>~<s:property value="%{designationId}"/>"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}






