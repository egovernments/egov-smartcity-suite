<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {
     "checkBudget":"<s:property value="%{checkBudget}" />", 
     "totalWorkValue":"<s:property value="%{totalWorkValueRecorded}" />"
    },
   	<s:iterator var="s" value="approvedMBHeaderList" status="status">  
	    {"Text":"<s:property value="%{mbRefNo}"/>",
	    "FromPageNo":"<s:property value="%{fromPageNo}"/>",
	    "ToPageNo":"<s:property value="%{toPageNo}"/>",
	    "Value":"<s:property value="%{id}" />"}
	    <s:if test="!#status.last">,</s:if></s:iterator>
    ]
  }
} 