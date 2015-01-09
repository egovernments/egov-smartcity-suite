<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="entity" value="entityList" status="status">  
    {"Text":"<s:property value="%{entityName}"/>",
    "Value":"<s:property value="%{entityName}"/>"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}