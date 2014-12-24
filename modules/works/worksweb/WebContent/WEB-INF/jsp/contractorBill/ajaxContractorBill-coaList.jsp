<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {
     "checkBudget":"<s:property value="%{checkBudget}" />"
    },
    	<s:iterator var="s" value="coaList" status="status">  
		    {"Text":"<s:property value="%{glcode}"/>-<s:property value="%{name}"/>",
		    "Value":"<s:property value="%{id}" />"
		    }<s:if test="!#status.last">,</s:if>
		</s:iterator>
    ]
  }
}



