<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/taglib/struts-tags.tld" %> 
{

    "Result":[
    <s:iterator var="s" value="subCategoryList" status="status">
    {"Text":"<s:property value="%{name}" />",
    "Value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  
}
