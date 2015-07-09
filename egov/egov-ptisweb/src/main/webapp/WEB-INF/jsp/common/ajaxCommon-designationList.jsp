<%@page contentType="text/json" %>
<%@taglib prefix="s" uri="/WEB-INF/taglib/struts-tags.tld" %>  
{
"ResultSet": {
    "Result":[
	<s:iterator var="s" value="designationMasterList" status="status">  
    {"Value":"<s:property value="%{id}" />",
    "Text":"<s:property value="%{name}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}