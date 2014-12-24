<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
{
 "ResultSet": {
    "Result":[
    <s:iterator var="s" value="abstractEstimateList" status="status">
    {"Text":"<s:property value="%{estimateNumber}" />",
     "Value":"<s:property value="%{estimateNumber}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}