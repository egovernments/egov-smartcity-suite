<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
[<s:iterator var="s" value="revisedRegnAppList" status="status">
    {"id":"<s:property value="%{id}" />",
    "label":"<s:property value="%{planSubmissionNum}" /> ",
     "value":"<s:property value="%{planSubmissionNum}" /> "
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>
    ]
