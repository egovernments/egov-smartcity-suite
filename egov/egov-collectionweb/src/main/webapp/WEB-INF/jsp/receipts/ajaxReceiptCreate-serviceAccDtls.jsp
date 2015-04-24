<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="accountDetails" status="status">
    {"functionIdDetail":"<s:property value="%{function.id}" />",
    "functionDetail":"<s:property value="%{function.name}" />",
    "glcodeIdDetail":"<s:property value="%{glCodeId.id}" />",
    "glcodeDetail":"<s:property value="%{glCodeId.glcode}" />",
    "accounthead":"<s:property value="%{glCodeId.name}" />",
    "creditAmountDetail":"<s:property value="%{amount}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}