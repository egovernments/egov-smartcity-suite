<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="approverList" status="status">
    {"Text":"<s:property value="%{userMaster.userName}"/> ~ <s:property value="%{position.name}"/>",
    "Value":"<s:property value="%{position.id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}