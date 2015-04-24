<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
	<s:iterator var="s" value="designationMasterList" status="status">  
    {"Value":"<s:property value="%{designationId}" />",
    "Text":"<s:property value="%{designationName}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}
