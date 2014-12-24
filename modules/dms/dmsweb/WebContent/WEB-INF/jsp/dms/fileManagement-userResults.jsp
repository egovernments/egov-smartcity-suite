<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %> 
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="empInfoList" status="status">  
    {"Text":"<s:property value="%{employeeName}"/> / <s:property value="%{position.name}"/>",
    "Value":"<s:property value="%{position.id}"/>"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}