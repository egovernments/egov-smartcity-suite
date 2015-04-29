<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    	{
    		"xRowId":"<s:property value="%{rowId}" />"
    	},
    	<s:iterator var="s" value="standardDeductionAccountList" status="status">  
		    {"Text":"<s:property value="%{glcode}"/>-<s:property value="%{name}"/>",
		    "Value":"<s:property value="%{id}" />"
		    }<s:if test="!#status.last">,</s:if>
		</s:iterator>
    ]
  }
}