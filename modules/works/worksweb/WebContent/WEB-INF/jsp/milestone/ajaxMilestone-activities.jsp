<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="milestoneTemplate.activities" status="status">
    {
    "stageOrderNo":"<s:property value="%{stageOrderNo}" />",
    "description":"<s:property value="%{description}" />",
    "percentage":"<s:property value="%{percentage}" />"
   } <s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}