<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
{
"ResultSet": {
    "Result":[
    {
   		"ErrorMsg":"<s:property value="%{loadBudgetGroupsValidationError}" />"
   	}
   	<s:if test="%{!budgetGroups.isEmpty()}">
   	,
    <s:iterator var="s" value="budgetGroups" status="status">
	    {"Text":"<s:property value="%{name}" escape="false" />",
	    "Value":"<s:property value="%{id}" />",
	    "Glcode":"<s:property value="%{maxCode.glcode}"/>"
	    }<s:if test="!#status.last">,</s:if>
    </s:iterator>     
     </s:if> 
    ]
  }
}