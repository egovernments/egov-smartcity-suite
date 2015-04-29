<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:if test="%{!approvedMBList.isEmpty}">
	   	<s:iterator var="s" value="approvedMBList" status="status"> {
	   	"mbRefNo":"<s:property value="%{mbRefNo}"/>"
		 } <s:if test="!#status.last">,</s:if>
		</s:iterator>
	</s:if>
	<s:else>
		{"mbRefNo":""} 
	</s:else>   
    ]
  }
}

