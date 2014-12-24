<%@page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="scheduleOfRateListforRC" status="status">  
    {"key":"<s:property value="indentDetail.nonSor.id" />~<s:property value="indentDetail.id" />~<s:property value="rateContract.id" />",
    "value":"<s:property value="indentDetail.nonSor.description" /> : <s:property value="rateContract.contractor.code" /> : <s:property value="rateContract.rcNumber" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}