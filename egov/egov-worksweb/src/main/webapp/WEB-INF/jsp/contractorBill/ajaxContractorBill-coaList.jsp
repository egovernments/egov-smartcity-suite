<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {
     "checkBudget":"<s:property value="%{checkBudget}" />",
     "showValidationMsg":"<s:property value="%{showValidationMsg}" />"
    }
    <s:if test="%{!coaList.isEmpty()}">
    ,
    	<s:iterator var="s" value="coaList" status="status">  
		    {"Text":"<s:property value="%{glcode}"/>-<s:property value="%{name}"/>",
		    "Value":"<s:property value="%{id}" />"
		    }<s:if test="!#status.last">,</s:if>
		</s:iterator>
	</s:if>	
		<s:iterator var="s1" value="assetList" status="status1">
		  <s:if test="#status1.first">,</s:if> 
		    {"AssetCode":"<s:property value="%{asset.code}"/>",
		    "AssetId":"<s:property value="%{asset.id}" />"
		    }<s:if test="!#status1.last">,</s:if>
		</s:iterator>
    ]
  }
}



