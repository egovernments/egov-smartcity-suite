<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
{
 "ResultSet": {
    "Result":[
    <s:iterator var="s" value="revEstimateList" status="status">
    {"key":"<s:property value="%{id}" />",
     "value":"<s:property value="%{estimateNumber}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}