<%@ page contentType="text/json"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
{ "ResultSet": { "Result":[
<s:iterator var="s" value="contractorsList" status="status">  
    {"value":"<s:property value="%{name}" /> ~/ <s:property
		value="%{code}" />"
    }<s:if test="!#status.last">,</s:if>
</s:iterator>
] } }

