<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/taglib/struts-tags.tld" %> 
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="serviceList" status="status">
    {"Text":"<s:property value="%{name+'-'+code}" escapeJavaScript="true"/>",
    "Value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}
