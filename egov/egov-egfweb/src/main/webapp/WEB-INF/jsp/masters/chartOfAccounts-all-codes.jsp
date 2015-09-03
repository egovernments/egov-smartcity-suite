<%@ page contentType="text/json"%>
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
{ "ResultSet": { "Result":[
<s:iterator var="s" value="allChartOfAccounts" status="status">
    {"Text":"<s:property value="%{name}" />",
    "Value":"<s:property value="%{glcode}" />"
    }<s:if test="!#status.last">,</s:if>
</s:iterator>
] } }
