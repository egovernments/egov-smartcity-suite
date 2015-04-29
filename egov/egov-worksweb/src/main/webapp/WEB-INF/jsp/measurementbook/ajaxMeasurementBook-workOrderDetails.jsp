<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    	{
    		"xWoId":"<s:property value="%{workOrder.id}" />",
    		"xContractor":"<s:property value="%{workOrder.contractor.name}" />",
    		"xContractorId":"<s:property value="%{workOrder.contractor.id}" />",
    		"xProjectCode":"<s:property value="%{workOrder.abstractEstimate.projectCode.code}" />"
    	},
    	<s:iterator var="s" value="usersInExecutingDepartment" status="status">  
		    {"Text":"<s:property value="%{employeeName}" />",
		    "Value":"<s:property value="%{id}" />"
		    }<s:if test="!#status.last">,</s:if>
	    </s:iterator>
    ]
  }
}