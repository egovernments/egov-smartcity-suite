<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="drawingOfficerList" status="status">
	    {"key":"<s:property value="%{doid}" />",
	     "value":"<s:property value="%{docode}" /> - <s:property value="%{doname}" />(<s:property value="%{empcode}" /> - <s:property value="%{empname}" />)"
	    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}