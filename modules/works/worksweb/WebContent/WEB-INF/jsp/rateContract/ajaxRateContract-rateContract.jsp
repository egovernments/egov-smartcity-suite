<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    	<s:iterator var="s" value="rateContracts" status="status">  
		    {"Text":"<s:property value="%{rcNumber}"/> '~' <s:property value="%{contractor.code}"/>",
		    "Value":"<s:property value="%{id}" />"
		    }<s:if test="!#status.last">,</s:if>
		</s:iterator>
    ]
  }
}