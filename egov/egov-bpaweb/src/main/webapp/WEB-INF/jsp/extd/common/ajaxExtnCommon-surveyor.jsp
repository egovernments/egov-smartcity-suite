<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
[<s:iterator var="s" value="surveyorDetail" status="status">
    {"id":"<s:property value="%{surveyor.id}" />",
  	"value":"<s:property value="%{surveyor.code}" />-<s:property value="%{surveyor.name}" />-<s:property value="%{surveyor.userDetail.mobileNumber}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>
    ]
