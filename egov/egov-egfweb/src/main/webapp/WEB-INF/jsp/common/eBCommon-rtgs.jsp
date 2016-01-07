<%@ page contentType="text/json"%>
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
{ "ResultSet": { "Result":[
<s:iterator var="s" value="rtgsNumbers" status="status">
    {"Text":"<s:property />",
    "Value":"<s:property />"
    }<s:if test="!#status.last">,</s:if>
</s:iterator>
] } }
