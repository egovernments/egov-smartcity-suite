<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:if test="%{!workOrderList.isEmpty}">
	   	<s:iterator var="s" value="workOrderList" status="status"> {
	   	"workOrderNo":"<s:property value="%{workOrderNumber}"/>"
		 } <s:if test="!#status.last">,</s:if>
		</s:iterator>
	</s:if>
	<s:else>
		{"workOrderNo":""} 
	</s:else>   
    ]
  }
}

