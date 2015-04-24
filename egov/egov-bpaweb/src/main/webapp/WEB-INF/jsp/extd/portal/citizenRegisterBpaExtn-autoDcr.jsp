<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
[<s:iterator var="s" value="approvedAutoDcrList" status="status">
    {"id":"<s:property value="%{id}" />",
  	"value":"<s:property value="%{autoDcrNum}" />|<s:property value="%{file_applicantName}" />|<s:property value="%{file_mobileNumber}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>
    ]
