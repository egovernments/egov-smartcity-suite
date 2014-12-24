<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[ 
     <s:iterator var="woa" value="woactivityList" status="status">
		    <s:iterator var="s" value="woMeasurementSheetList" status="status1"> {
		    "cumulativeQuantity":"<s:property value="%{cumulativeQuantity}" />",
	    	"Id":"<s:property value="%{woActivity.parent.id}" />",
		    "estimateNo":"<s:property value="%{measurementSheet.activity.abstractEstimate.estimateNumber}" />",
		     <s:if test="%{measurementSheet.activity.revisionType!=null}">
		    "revisionType":"<s:property value="%{measurementSheet.activity.revisionType}" />",  
		    </s:if>
		     <s:else>
		   	 "revisionType":"",
		   	 </s:else> 
		   	 
		    <s:if test="%{measurementSheet.mbExtraItemSlNo!=null}">
		   	 	"msheetSlNo":"<s:property value="%{measurementSheet.mbExtraItemSlNo}" />",
		   	 </s:if>
		   	 <s:else>
		   	 	"msheetSlNo":"", 
		   	 </s:else>
		   	 "estimateMsheetId":"<s:property value="%{measurementSheet.id}" />",
		    <s:if test="%{measurementSheet.remarks!=null}"> 
		   		 "description":"<s:property value="%{measurementSheet.remarks}" />",
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
		    <s:if test="%{length!=0.0}">
		   		 "uomLength":"<s:property value="%{length}" />",
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
			 "identifier":"<s:property value="%{measurementSheet.identifier}" />", 
		      	 "uom":"<s:property value="%{woActivity.activity.uom.uom}" />",
		   	 "quantity":"<s:property value="%{quantity}" />"
		   }<s:if test="!#status1.last">,</s:if> 
		   </s:iterator> 
		 
		<s:if test="!#status.last">,</s:if>    
	 </s:iterator>  		         
   ]
  }
}