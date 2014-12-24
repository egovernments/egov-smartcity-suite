<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="info" value="deptWiseEstimateList" status="status">  
    {"Department":"<s:property value="#info.department" />",
    "NoOfEstimates":"<s:property value="#info.NoOfEstimates" />",
    "Amount":"<s:property value="#info.amount" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}