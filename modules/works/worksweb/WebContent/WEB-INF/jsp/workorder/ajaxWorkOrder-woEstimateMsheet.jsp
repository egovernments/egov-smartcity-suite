<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
	    <s:iterator var="s" value="measurementSheet" status="status">
	    {
		     "Id":"<s:property value="%{id}" />",
		      <s:if test="%{remarks!=null}">
		   		 "description":"<s:property value="%{remarks}" />",
		   	 </s:if>
		   	 <s:else>
		   	 	"description":"",
		   	 </s:else>
		   
		     <s:if test="%{no!=null}">
		   		 "no":"<s:property value="%{no}" />",
		   	 </s:if>
		   	 <s:else>
		   	 	"no":"",
		   	 </s:else>
		    <s:if test="%{uomLength!=0.0}">
		   		 "uomLength":"<s:property value="%{uomLength}" />",
		   	 </s:if>
		   	 <s:else>
		   	 	"uomLength":"",
		   	 </s:else>
		    <s:if test="%{width!=0.0}">
		   		 "width":"<s:property value="%{width}" />",
		   	 </s:if>
		   	 <s:else>
		   	 	"width":"",
		   	 </s:else>
		   	 <s:if test="%{depthOrHeight!=0.0}">
		   		 "depthOrHeight":"<s:property value="%{depthOrHeight}" />", 
		   	 </s:if>
		   	 <s:else>
		   	 	"depthOrHeight":"",
		   	 </s:else>
		  	 "identifier":"<s:property value="%{identifier}" />",
		  	 "uom":"<s:property value="%{activity.uom.uom}" />",
		  	 "quantity":"<s:property value="%{quantity}" />"
		}<s:if test="!#status.last">,</s:if> 
	   </s:iterator>        
 	]
  } 
}
