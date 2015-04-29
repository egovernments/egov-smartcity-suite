<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:if test="%{!estimateNoList.isEmpty}">
	   	<s:iterator var="s" value="estimateNoList" status="status"> {
	   	"estimateNo":"<s:property />"
		 } <s:if test="!#status.last">,</s:if>
		</s:iterator>
	</s:if>
	<s:else>
		{"estimateNo":""} 
	</s:else>   
    ]
  }
}
